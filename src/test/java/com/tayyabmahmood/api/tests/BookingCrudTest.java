package com.tayyabmahmood.api.tests;

import com.tayyabmahmood.api.config.ConfigManager;
import com.tayyabmahmood.api.data.BookingFactory;
import com.tayyabmahmood.api.db.TestDataRepository;
import com.tayyabmahmood.api.model.Booking;
import com.tayyabmahmood.api.model.CreateBookingResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.lessThan;

@Epic("Booking API")
@Feature("Booking CRUD")
@Tag("regression")
class BookingCrudTest extends BaseTest {

    @Test
    @Tag("smoke")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Create returns an id plus an exact echo of the submitted booking, matching the contract")
    void create_booking_returns_id_and_echoes_payload() {
        Booking booking = BookingFactory.randomBooking();

        CreateBookingResponse created = bookingClient.createBooking(booking)
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/create-booking-schema.json"))
                .extract().as(CreateBookingResponse.class);
        TestDataRepository.recordCreatedBooking(created.bookingid(), booking.firstname(), booking.lastname());

        assertThat(created.bookingid()).isPositive();
        assertThat(created.booking()).isEqualTo(booking);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    void get_booking_returns_the_stored_booking_within_sla() {
        Booking booking = BookingFactory.randomBooking();
        int id = createTrackedBooking(booking).bookingid();

        Booking fetched = bookingClient.getBooking(id)
                .then()
                .statusCode(200)
                .time(lessThan((long) ConfigManager.getInt("max.response.time.ms")))
                .body(matchesJsonSchemaInClasspath("schemas/booking-schema.json"))
                .extract().as(Booking.class);

        assertThat(fetched).isEqualTo(booking);
    }

    @Test
    void booking_can_be_found_by_guest_name() {
        Booking booking = BookingFactory.randomBooking();
        int id = createTrackedBooking(booking).bookingid();

        List<Integer> ids = bookingClient.getBookingIdsByName(booking.firstname(), booking.lastname())
                .then().statusCode(200)
                .extract().jsonPath().getList("bookingid", Integer.class);

        assertThat(ids).contains(id);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    void full_update_replaces_every_field() {
        int id = createTrackedBooking(BookingFactory.randomBooking()).bookingid();
        Booking replacement = BookingFactory.randomBooking();

        Booking updated = bookingClient.updateBooking(id, replacement, authClient.getToken())
                .then().statusCode(200)
                .extract().as(Booking.class);

        assertThat(updated).isEqualTo(replacement);
        assertThat(bookingClient.getBooking(id).as(Booking.class)).isEqualTo(replacement);
    }

    @Test
    void partial_update_changes_only_the_given_fields() {
        Booking original = BookingFactory.randomBooking();
        int id = createTrackedBooking(original).bookingid();

        Booking patched = bookingClient
                .partialUpdateBooking(id, Map.of("firstname", "Patched", "totalprice", 777), authClient.getToken())
                .then().statusCode(200)
                .extract().as(Booking.class);

        assertThat(patched).isEqualTo(original.withFirstname("Patched").withTotalprice(777));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    void deleted_booking_is_no_longer_retrievable() {
        int id = createTrackedBooking(BookingFactory.randomBooking()).bookingid();

        // API returns 201 for DELETE (see KNOWN_ISSUES.md)
        bookingClient.deleteBooking(id, authClient.getToken()).then().statusCode(201);
        TestDataRepository.markDeleted(id);

        bookingClient.getBooking(id).then().statusCode(404);
    }
}
