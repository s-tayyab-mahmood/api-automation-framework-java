package com.tayyabmahmood.api.tests;

import com.tayyabmahmood.api.data.BookingFactory;
import com.tayyabmahmood.api.model.Booking;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Booking API")
@Feature("Authorisation")
@Tag("regression")
class BookingSecurityTest extends BaseTest {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    void update_without_token_is_forbidden_and_data_is_unchanged() {
        Booking original = BookingFactory.randomBooking();
        int id = createTrackedBooking(original).bookingid();

        bookingClient.updateBookingWithoutAuth(id, BookingFactory.randomBooking())
                .then().statusCode(403);

        assertThat(bookingClient.getBooking(id).as(Booking.class)).isEqualTo(original);
    }

    @ParameterizedTest(name = "[{index}] token=''{0}''")
    @ValueSource(strings = {"invalid-token", "abc123", "' OR '1'='1"})
    void tampered_tokens_cannot_delete_a_booking(String badToken) {
        int id = createTrackedBooking(BookingFactory.randomBooking()).bookingid();

        bookingClient.deleteBooking(id, badToken).then().statusCode(403);
        bookingClient.getBooking(id).then().statusCode(200);
    }

    @Test
    void partial_update_with_invalid_token_is_forbidden() {
        int id = createTrackedBooking(BookingFactory.randomBooking()).bookingid();

        bookingClient.partialUpdateBooking(id, Map.of("firstname", "Hacker"), "not-a-real-token")
                .then().statusCode(403);
    }
}
