package com.tayyabmahmood.api.tests;

import com.tayyabmahmood.api.model.Booking;
import com.tayyabmahmood.api.model.BookingDates;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Booking API")
@Feature("Booking dates")
@Tag("regression")
class BookingDatesTest extends BaseTest {

    @ParameterizedTest(name = "[{index}] {2}: {0} -> {1}")
    @CsvSource({
            "2030-06-10, 2030-06-11, one night",
            "2030-12-30, 2031-01-02, over new year",
            "2028-02-28, 2028-02-29, into leap day",
            "2028-02-29, 2028-03-01, out of leap day",
            "2030-01-01, 2030-03-31, long stay"
    })
    void dates_are_stored_exactly_as_sent(String checkin, String checkout, String label) {
        Booking booking = new Booking("Date", "Check", 200, true,
                new BookingDates(checkin, checkout), "None");

        int id = createTrackedBooking(booking).bookingid();
        BookingDates stored = bookingClient.getBooking(id)
                .then().statusCode(200)
                .extract().as(Booking.class)
                .bookingdates();

        assertThat(stored).isEqualTo(new BookingDates(checkin, checkout));
    }
}
