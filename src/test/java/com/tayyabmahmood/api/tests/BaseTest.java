package com.tayyabmahmood.api.tests;

import com.tayyabmahmood.api.client.AuthClient;
import com.tayyabmahmood.api.client.BookingClient;
import com.tayyabmahmood.api.db.TestDataRepository;
import com.tayyabmahmood.api.model.Booking;
import com.tayyabmahmood.api.model.CreateBookingResponse;

public abstract class BaseTest {

    protected static final AuthClient authClient = new AuthClient();
    protected static final BookingClient bookingClient = new BookingClient();

    protected static CreateBookingResponse createTrackedBooking(Booking booking) {
        CreateBookingResponse created = bookingClient.createBooking(booking)
                .then().statusCode(200)
                .extract().as(CreateBookingResponse.class);
        TestDataRepository.recordCreatedBooking(created.bookingid(), booking.firstname(), booking.lastname());
        return created;
    }
}
