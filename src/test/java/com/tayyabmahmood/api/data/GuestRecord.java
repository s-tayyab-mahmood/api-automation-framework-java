package com.tayyabmahmood.api.data;

public record GuestRecord(
        int id,
        String firstname,
        String lastname,
        int totalprice,
        boolean depositpaid,
        int nights,
        String additionalneeds) {

    @Override
    public String toString() {
        return firstname + " " + lastname + " (" + nights + " nights)";
    }
}
