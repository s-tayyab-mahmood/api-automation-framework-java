package com.tayyabmahmood.api.tests;

import com.tayyabmahmood.api.config.ConfigManager;
import com.tayyabmahmood.api.model.AuthRequest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

@Epic("Booking API")
@Feature("Authentication")
class AuthTest extends BaseTest {

    @Test
    @Tag("smoke")
    @Severity(SeverityLevel.BLOCKER)
    void valid_credentials_return_a_token() {
        authClient.createToken(new AuthRequest(ConfigManager.username(), ConfigManager.password()))
                .then()
                .statusCode(200)
                .body("token", not(emptyOrNullString()))
                .body("reason", nullValue());
    }

    @ParameterizedTest(name = "[{index}] user=''{0}'' password=''{1}''")
    @Tag("regression")
    @CsvSource({
            "admin, wrong-password",
            "unknown-user, password123",
            "'', ''"
    })
    void invalid_credentials_are_rejected(String username, String password) {
        authClient.createToken(new AuthRequest(username, password))
                .then()
                .statusCode(200) // API returns 200 even for bad creds
                .body("reason", equalTo("Bad credentials"))
                .body("token", nullValue());
    }
}
