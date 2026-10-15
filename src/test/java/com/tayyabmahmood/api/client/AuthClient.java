package com.tayyabmahmood.api.client;

import com.tayyabmahmood.api.config.ConfigManager;
import com.tayyabmahmood.api.model.AuthRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class AuthClient extends BaseApiClient {

    private static volatile String cachedToken;

    @Step("POST /auth")
    public Response createToken(AuthRequest request) {
        return request().body(request).post("/auth");
    }

    public String getToken() {
        if (cachedToken == null) {
            synchronized (AuthClient.class) {
                if (cachedToken == null) {
                    cachedToken = createToken(new AuthRequest(ConfigManager.username(), ConfigManager.password()))
                            .then().statusCode(200)
                            .extract().path("token");
                }
            }
        }
        return cachedToken;
    }
}
