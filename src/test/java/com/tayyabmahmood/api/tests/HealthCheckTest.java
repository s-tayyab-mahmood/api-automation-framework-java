package com.tayyabmahmood.api.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Epic("Booking API")
@Feature("Health check")
@Tag("smoke")
class HealthCheckTest extends BaseTest {

    @Test
    @Severity(SeverityLevel.BLOCKER)
    void service_is_up_and_responding() {
        // 201, not 200 - API quirk
        bookingClient.healthCheck().then().statusCode(201);
    }
}
