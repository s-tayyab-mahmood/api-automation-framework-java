package com.tayyabmahmood.api.cucumber;

import com.tayyabmahmood.api.model.Booking;
import io.restassured.response.Response;

public class ScenarioContext {

    private Booking booking;
    private Integer bookingId;
    private String token;
    private Response lastResponse;

    public Booking booking() { return booking; }
    public void booking(Booking booking) { this.booking = booking; }

    public Integer bookingId() { return bookingId; }
    public void bookingId(Integer bookingId) { this.bookingId = bookingId; }

    public String token() { return token; }
    public void token(String token) { this.token = token; }

    public Response lastResponse() { return lastResponse; }
    public void lastResponse(Response lastResponse) { this.lastResponse = lastResponse; }
}
