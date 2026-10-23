package com.tayyabmahmood.api.client;

import com.tayyabmahmood.api.config.ConfigManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class BaseApiClient {

    private static final RestAssuredConfig CONFIG = RestAssuredConfig.config()
            .httpClient(HttpClientConfig.httpClientConfig()
                    .setParam("http.connection.timeout", ConfigManager.getInt("http.timeout.ms"))
                    .setParam("http.socket.timeout", ConfigManager.getInt("http.timeout.ms")));

    private static final RequestSpecification BASE_SPEC = new RequestSpecBuilder()
            .setBaseUri(ConfigManager.baseUrl())
            .setContentType(ContentType.JSON)
            // must be exactly application/json - the API returns 418 for anything else
            .setAccept("application/json")
            .setConfig(CONFIG)
            .addFilter(new AllureRestAssured())
            .log(LogDetail.URI)
            .build();

    protected RequestSpecification request() {
        return given()
                .spec(BASE_SPEC)
                .filter((req, res, ctx) -> {
                    var response = ctx.next(req, res);
                    if (response.statusCode() >= 500) {
                        System.err.printf("[%d] %s %s%n%s%n", response.statusCode(),
                                req.getMethod(), req.getURI(), response.asPrettyString());
                    }
                    return response;
                });
    }
}
