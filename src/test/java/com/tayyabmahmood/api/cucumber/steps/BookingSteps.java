package com.tayyabmahmood.api.cucumber.steps;

import com.tayyabmahmood.api.client.AuthClient;
import com.tayyabmahmood.api.client.BookingClient;
import com.tayyabmahmood.api.cucumber.ScenarioContext;
import com.tayyabmahmood.api.model.Booking;
import com.tayyabmahmood.api.model.BookingDates;
import com.tayyabmahmood.api.model.CreateBookingResponse;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingSteps {

    private final ScenarioContext context;
    private final BookingClient bookingClient = new BookingClient();
    private final AuthClient authClient = new AuthClient();

    public BookingSteps(ScenarioContext context) {
        this.context = context;
    }

    @Given("the booking service is available")
    public void theBookingServiceIsAvailable() {
        bookingClient.healthCheck().then().statusCode(201);
    }

    @Given("a guest {string} {string} wants to stay {int} nights for {int} with deposit {word}")
    public void aGuestWantsToStay(String first, String last, int nights, int price, String deposit) {
        LocalDate checkin = LocalDate.now().plusDays(10);
        context.booking(new Booking(first, last, price, deposit.equals("paid"),
                new BookingDates(checkin.toString(), checkin.plusDays(nights).toString()), "Breakfast"));
    }

    @Given("an existing booking for {string} {string}")
    public void anExistingBookingFor(String first, String last) {
        aGuestWantsToStay(first, last, 2, 300, "paid");
        theBookingIsSubmitted();
        theBookingIsConfirmed();
    }

    @And("I am authenticated as an admin")
    public void iAmAuthenticated() {
        context.token(authClient.getToken());
    }

    @When("the booking is submitted")
    public void theBookingIsSubmitted() {
        context.lastResponse(bookingClient.createBooking(context.booking()));
        if (context.lastResponse().statusCode() == 200) {
            context.bookingId(context.lastResponse().path("bookingid"));
        }
    }

    @When("I change the first name to {string}")
    public void iChangeTheFirstName(String newName) {
        context.lastResponse(bookingClient.partialUpdateBooking(
                context.bookingId(), Map.of("firstname", newName), context.token()));
        context.lastResponse().then().statusCode(200);
    }

    @When("an unauthenticated user tries to cancel the booking")
    public void unauthenticatedCancel() {
        context.lastResponse(bookingClient.deleteBooking(context.bookingId(), ""));
    }

    @When("I cancel the booking")
    public void iCancelTheBooking() {
        bookingClient.deleteBooking(context.bookingId(), context.token()).then().statusCode(201);
    }

    @Then("the booking is confirmed with a booking id")
    public void theBookingIsConfirmed() {
        CreateBookingResponse created = context.lastResponse()
                .then().statusCode(200)
                .extract().as(CreateBookingResponse.class);
        assertThat(created.bookingid()).isPositive();
        assertThat(created.booking()).isEqualTo(context.booking());
    }

    @Then("retrieving the booking returns the same details")
    public void retrievingReturnsSameDetails() {
        Booking fetched = bookingClient.getBooking(createdBookingId())
                .then().statusCode(200).extract().as(Booking.class);
        assertThat(fetched).isEqualTo(context.booking());
    }

    @Then("the booking shows first name {string}")
    public void theBookingShowsFirstName(String expected) {
        assertThat(bookingClient.getBooking(createdBookingId()).as(Booking.class).firstname())
                .isEqualTo(expected);
    }

    @Then("the request is rejected with status {int}")
    public void theRequestIsRejected(int status) {
        assertThat(context.lastResponse().statusCode()).isEqualTo(status);
    }

    @Then("the booking still exists")
    public void theBookingStillExists() {
        bookingClient.getBooking(createdBookingId()).then().statusCode(200);
    }

    @Then("retrieving the booking returns status {int}")
    public void retrievingReturnsStatus(int status) {
        bookingClient.getBooking(createdBookingId()).then().statusCode(status);
    }

    private int createdBookingId() {
        assertThat(context.bookingId())
                .as("booking id - was the booking created? last status: %s",
                        context.lastResponse() == null ? "none" : context.lastResponse().statusCode())
                .isNotNull();
        return context.bookingId();
    }
}
