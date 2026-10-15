package com.tayyabmahmood.api.support;

import com.tayyabmahmood.api.client.AuthClient;
import com.tayyabmahmood.api.client.BookingClient;
import com.tayyabmahmood.api.db.TestDataRepository;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.LauncherSessionListener;

import java.util.List;

// Runs once at the end of the whole run (registered in META-INF/services)
public class TestDataCleanupListener implements LauncherSessionListener {

    @Override
    public void launcherSessionClosed(LauncherSession session) {
        List<Integer> pending;
        try {
            pending = TestDataRepository.bookingsPendingCleanup();
        } catch (RuntimeException e) {
            return;
        }
        if (pending.isEmpty()) {
            return;
        }
        BookingClient bookings = new BookingClient();
        String token = new AuthClient().getToken();
        for (int id : pending) {
            int status = bookings.deleteBooking(id, token).statusCode();
            // 405 = already deleted
            if (status == 201 || status == 405) {
                TestDataRepository.markDeleted(id);
            }
        }
        System.out.printf("Test data cleanup: removed %d booking(s)%n", pending.size());
    }
}
