package com.tayyabmahmood.api.db;

import com.tayyabmahmood.api.data.GuestRecord;
import io.qameta.allure.Step;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class TestDataRepository {

    private TestDataRepository() {
    }

    @Step("DB: load active guests")
    public static List<GuestRecord> activeGuests() {
        String sql = """
                SELECT id, firstname, lastname, totalprice, depositpaid, nights, additionalneeds
                FROM guests
                WHERE active = TRUE
                ORDER BY id
                """;
        List<GuestRecord> guests = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                guests.add(new GuestRecord(
                        rs.getInt("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getInt("totalprice"),
                        rs.getBoolean("depositpaid"),
                        rs.getInt("nights"),
                        rs.getString("additionalneeds")));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to load guests", e);
        }
        return guests;
    }

    @Step("DB: record created booking {bookingId}")
    public static void recordCreatedBooking(int bookingId, String firstname, String lastname) {
        String sql = "INSERT INTO created_bookings (booking_id, firstname, lastname) VALUES (?, ?, ?)";
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.setString(2, firstname);
            ps.setString(3, lastname);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to record booking " + bookingId, e);
        }
    }

    @Step("DB: find created booking {bookingId}")
    public static Optional<String> findCreatedBookingName(int bookingId) {
        String sql = "SELECT firstname || ' ' || lastname AS full_name FROM created_bookings WHERE booking_id = ?";
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(rs.getString("full_name")) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to query booking " + bookingId, e);
        }
    }

    public static List<Integer> bookingsPendingCleanup() {
        String sql = "SELECT booking_id FROM created_bookings WHERE deleted = FALSE";
        List<Integer> ids = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to read cleanup list", e);
        }
        return ids;
    }

    public static void markDeleted(int bookingId) {
        String sql = "UPDATE created_bookings SET deleted = TRUE WHERE booking_id = ?";
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to mark booking " + bookingId + " deleted", e);
        }
    }
}
