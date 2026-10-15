package com.tayyabmahmood.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookingDates(String checkin, String checkout) {
}
