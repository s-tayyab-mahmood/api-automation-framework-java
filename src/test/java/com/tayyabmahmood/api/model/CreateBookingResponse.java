package com.tayyabmahmood.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateBookingResponse(int bookingid, Booking booking) {
}
