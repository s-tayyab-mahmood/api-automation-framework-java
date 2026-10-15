package com.tayyabmahmood.api.tests;

import com.tayyabmahmood.api.db.TestDataRepository;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;

@Epic("Booking API")
@Feature("Validation & error handling")
@Tag("regression")
class BookingNegativeTest extends BaseTest {

    @ParameterizedTest(name = "[{index}] id={0}")
    @ValueSource(ints = {0, -1, 999_999_999})
    void unknown_booking_id_returns_404(int id) {
        bookingClient.getBooking(id).then().statusCode(404);
    }

    @Test
    void booking_with_missing_required_fields_is_rejected() {
        Map<String, Object> incomplete = Map.of("firstname", "OnlyAFirstName");

        bookingClient.createBooking(incomplete)
                .then().statusCode(both(greaterThanOrEqualTo(400)).and(lessThan(600)));
    }

    @Test
    @Disabled("Known defect: missing required fields return 500 instead of 400 - see KNOWN_ISSUES.md")
    @Issue("1")
    void booking_with_missing_required_fields_returns_400() {
        bookingClient.createBooking(Map.of("firstname", "OnlyAFirstName"))
                .then().statusCode(400);
    }

    @Test
    void wrong_type_for_total_price_is_rejected_or_coerced_safely() {
        Map<String, Object> body = Map.of(
                "firstname", "Type", "lastname", "Check",
                "totalprice", "not-a-number", "depositpaid", true,
                "bookingdates", Map.of("checkin", "2030-01-01", "checkout", "2030-01-02"));

        var response = bookingClient.createBooking(body);
        if (response.statusCode() == 200) {
            int id = response.path("bookingid");
            TestDataRepository.recordCreatedBooking(id, "Type", "Check");
            Object price = bookingClient.getBooking(id).path("totalprice");
            assertThat(price)
                    .as("totalprice must never be stored as a non-numeric string")
                    .isNotEqualTo("not-a-number");
        }
    }
}
