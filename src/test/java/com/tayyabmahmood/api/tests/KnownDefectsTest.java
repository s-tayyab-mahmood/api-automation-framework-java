package com.tayyabmahmood.api.tests;

import com.tayyabmahmood.api.model.Booking;
import com.tayyabmahmood.api.model.BookingDates;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.both;

// What the API *should* do. These fail today - see KNOWN_ISSUES.md.
// Skipped by default; run with: mvn test -Dgroups=known-defect -Dexcluded.groups=none
@Epic("Booking API")
@Feature("Known defects")
@Tag("known-defect")
class KnownDefectsTest extends BaseTest {

    @Test
    @Issue("1")
    void missing_required_fields_returns_400() {
        bookingClient.createBooking(Map.of("firstname", "OnlyAFirstName"))
                .then().statusCode(400);
    }

    @Test
    @Issue("6")
    void checkout_before_checkin_is_rejected() {
        Booking backwards = new Booking("Back", "Wards", 100, true,
                new BookingDates("2030-05-10", "2030-05-01"), "None");

        bookingClient.createBooking(backwards)
                .then().statusCode(both(greaterThanOrEqualTo(400)).and(lessThan(500)));
    }

    @Test
    @Issue("7")
    void invalid_date_format_is_rejected() {
        Booking badDate = new Booking("Bad", "Date", 100, true,
                new BookingDates("not-a-date", "2030-05-01"), "None");

        bookingClient.createBooking(badDate)
                .then().statusCode(both(greaterThanOrEqualTo(400)).and(lessThan(500)));
    }
}
