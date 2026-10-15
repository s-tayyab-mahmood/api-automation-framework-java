package com.tayyabmahmood.api.cucumber;

import com.tayyabmahmood.api.client.AuthClient;
import com.tayyabmahmood.api.client.BookingClient;
import io.cucumber.java.After;

public class Hooks {

    private final ScenarioContext context;

    public Hooks(ScenarioContext context) {
        this.context = context;
    }

    @After
    public void cleanUp() {
        if (context.bookingId() != null) {
            new BookingClient().deleteBooking(context.bookingId(), new AuthClient().getToken());
        }
    }
}
