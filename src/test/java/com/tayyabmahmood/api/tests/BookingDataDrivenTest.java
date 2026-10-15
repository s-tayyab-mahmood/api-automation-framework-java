package com.tayyabmahmood.api.tests;

import com.tayyabmahmood.api.data.BookingFactory;
import com.tayyabmahmood.api.data.GuestRecord;
import com.tayyabmahmood.api.db.TestDataRepository;
import com.tayyabmahmood.api.model.Booking;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Booking API")
@Feature("Data-driven (database)")
@Tag("regression")
class BookingDataDrivenTest extends BaseTest {

    static List<GuestRecord> guestsFromDatabase() {
        return TestDataRepository.activeGuests();
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("guestsFromDatabase")
    @Description("Each active guest row in the DB becomes a booking; the API copy and the DB audit row must agree")
    void booking_round_trip_matches_database_record(GuestRecord guest) {
        Booking expected = BookingFactory.fromGuest(guest);

        int id = createTrackedBooking(expected).bookingid();
        Booking actual = bookingClient.getBooking(id).then().statusCode(200).extract().as(Booking.class);

        assertThat(actual).isEqualTo(expected);
        assertThat(TestDataRepository.findCreatedBookingName(id))
                .as("audit row in CREATED_BOOKINGS")
                .contains(guest.firstname() + " " + guest.lastname());
    }

    @Test
    void inactive_guests_are_excluded_from_the_data_set() {
        assertThat(TestDataRepository.activeGuests())
                .extracting(GuestRecord::firstname)
                .doesNotContain("Retired")
                .hasSize(4);
    }
}
