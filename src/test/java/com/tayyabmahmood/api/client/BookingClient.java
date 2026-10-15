package com.tayyabmahmood.api.client;

import com.tayyabmahmood.api.model.Booking;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Map;

public class BookingClient extends BaseApiClient {

    private static final String BOOKING = "/booking";
    private static final String BOOKING_BY_ID = "/booking/{id}";

    @Step("GET /booking")
    public Response getBookingIds() {
        return request().get(BOOKING);
    }

    @Step("GET /booking?firstname={firstname}&lastname={lastname}")
    public Response getBookingIdsByName(String firstname, String lastname) {
        return request()
                .queryParam("firstname", firstname)
                .queryParam("lastname", lastname)
                .get(BOOKING);
    }

    @Step("GET /booking/{id}")
    public Response getBooking(int id) {
        return request().pathParam("id", id).get(BOOKING_BY_ID);
    }

    @Step("POST /booking")
    public Response createBooking(Object body) {
        return request().body(body).post(BOOKING);
    }

    @Step("PUT /booking/{id}")
    public Response updateBooking(int id, Booking booking, String token) {
        return request()
                .pathParam("id", id)
                .cookie("token", token)
                .body(booking)
                .put(BOOKING_BY_ID);
    }

    @Step("PUT /booking/{id} without a token")
    public Response updateBookingWithoutAuth(int id, Booking booking) {
        return request().pathParam("id", id).body(booking).put(BOOKING_BY_ID);
    }

    @Step("PATCH /booking/{id}")
    public Response partialUpdateBooking(int id, Map<String, Object> fields, String token) {
        return request()
                .pathParam("id", id)
                .cookie("token", token)
                .body(fields)
                .patch(BOOKING_BY_ID);
    }

    @Step("DELETE /booking/{id}")
    public Response deleteBooking(int id, String token) {
        return request()
                .pathParam("id", id)
                .cookie("token", token)
                .delete(BOOKING_BY_ID);
    }

    @Step("GET /ping")
    public Response healthCheck() {
        return request().get("/ping");
    }
}
