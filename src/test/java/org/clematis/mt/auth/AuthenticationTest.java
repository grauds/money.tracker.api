package org.clematis.mt.auth;

import org.clematis.mt.ClematisMoneyTrackerApplicationTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.tngtech.keycloakmock.api.ServerConfig.aServerConfig;
import static com.tngtech.keycloakmock.api.TokenConfig.aTokenConfig;
import com.tngtech.keycloakmock.junit5.KeycloakMockExtension;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.restassured.RestAssured;

@SuppressFBWarnings
class AuthenticationTest extends ClematisMoneyTrackerApplicationTests {

    @RegisterExtension
    static KeycloakMockExtension mock =
            new KeycloakMockExtension(aServerConfig().withRealm("clematis").build());

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
    }

    @Test
    void no_authentication_fails() {
        // if auth is on RestAssured.given().when().get("/api/hello").then().statusCode(401);
        // if auth is off
        RestAssured.given().when().get("/api/hello").then().statusCode(404);
    }

    @Test
    void authentication_works() {
        RestAssured.given()
                .auth()
                .preemptive()
                .oauth2(mock.getAccessToken(aTokenConfig().build()))
                .when()
                .get("/api/organizations")
                .then()
                .statusCode(200);
    }

//    @Test
//    void authentication_without_role_fails() {
//        RestAssured.given()
//                .auth()
//                .preemptive()
//                .oauth2(mock.getAccessToken(aTokenConfig().build()))
//                .when()
//                .get("/api/organizations")
//                .then()
//                .statusCode(403);
//    }

//    @Test
//    void authentication_with_role_works() {
//        RestAssured.given()
//                .auth()
//                .preemptive()
//                .oauth2(mock.getAccessToken(aTokenConfig().withRealmRole("vip").build()))
//                .when()
//                .get("/api/vip")
//                .then()
//                .statusCode(200)
//                .and()
//                .body(equalTo("you may feel very special here"));
//    }
}
