package com.lister.rest;

import com.lister.errorhandling.ErrorCodeImpl;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
public class CommitListerResourceIT {

    @Test
    public void testGetCommitsExistingInDB() {
        given()
          .when()
          .get("lister/commits/test/TestRepo?page=1&per_page=5")
          .then()
          .statusCode(OK.getStatusCode())
          .body("page", equalTo(1),
                "per_page", equalTo(5),
                "count", equalTo(4),
                "total_pages", equalTo(1),
                "total", equalTo(4),
                "items", hasSize(4));
    }

    @Test
    public void testGetCommitsExistingInDBWithSmallerPage() {
        given()
            .when()
            .get("lister/commits/test/TestRepo?page=1&per_page=2")
            .then()
            .statusCode(OK.getStatusCode())
            .body("page", equalTo(1),
                  "per_page", equalTo(2),
                  "count", equalTo(2),
                  "total_pages", equalTo(2),
                  "total", equalTo(4),
                  "items", hasSize(2));
    }

    @Test
    public void testGetRepoNotFound() {
        given()
            .when()
            .get("lister/commits/haczprox/test-not-found")
            .then()
            .statusCode(NOT_FOUND.getStatusCode())
            .body("code", equalTo(ErrorCodeImpl.NOT_FOUND_ERROR.getCode()),
                  "message", equalTo(ErrorCodeImpl.NOT_FOUND_ERROR.getMessage()));
    }

    @Test
    public void testGetExistingRepo() {
        given()
            .when()
            .get("lister/commits/haczprox/commit-lister")
            .then()
            .statusCode(OK.getStatusCode());
    }
}
