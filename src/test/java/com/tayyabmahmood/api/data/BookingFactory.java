package com.tayyabmahmood.api.data;

import com.tayyabmahmood.api.model.Booking;
import com.tayyabmahmood.api.model.BookingDates;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public final class BookingFactory {

    private static final Faker FAKER = new Faker();

    private BookingFactory() {
    }

    public static Booking randomBooking() {
        LocalDate checkin = LocalDate.now().plusDays(ThreadLocalRandom.current().nextInt(1, 60));
        LocalDate checkout = checkin.plusDays(ThreadLocalRandom.current().nextInt(1, 14));
        return new Booking(
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                ThreadLocalRandom.current().nextInt(50, 2000),
                FAKER.bool().bool(),
                new BookingDates(checkin.toString(), checkout.toString()),
                FAKER.options().option("Breakfast", "Late checkout", "Airport transfer", "None"));
    }

    public static Booking fromGuest(GuestRecord guest) {
        LocalDate checkin = LocalDate.now().plusDays(7);
        return new Booking(
                guest.firstname(),
                guest.lastname(),
                guest.totalprice(),
                guest.depositpaid(),
                new BookingDates(checkin.toString(), checkin.plusDays(guest.nights()).toString()),
                guest.additionalneeds());
    }
}
