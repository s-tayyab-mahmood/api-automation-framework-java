package com.tayyabmahmood.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record Booking(
        String firstname,
        String lastname,
        Integer totalprice,
        Boolean depositpaid,
        BookingDates bookingdates,
        String additionalneeds) {

    public Booking withFirstname(String newFirstname) {
        return new Booking(newFirstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds);
    }

    public Booking withTotalprice(Integer newPrice) {
        return new Booking(firstname, lastname, newPrice, depositpaid, bookingdates, additionalneeds);
    }
}
