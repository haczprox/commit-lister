package com.lister.rest;

import com.lister.errorhandling.ErrorCodeImpl;
import com.lister.model.api.dto.CommitPage;
import com.lister.service.GitHubAPIClient;
import com.lister.service.GitHubAPIService;
import com.lister.service.GitHubCLIClient;
import com.lister.service.GitHubCLIService;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;

@QuarkusTest
public class CommitListerMockedIT {


    @InjectMock
    GitHubAPIService apiService;

    @InjectMock
    GitHubCLIService cliService;

    @InjectMock
    GitHubCLIClient mockGitHubCLIClient;

    @Test
    public void testTimeoutExceptionMapper() throws IOException {

        Mockito.when(apiService.getCommitList(anyInt(), anyInt(), anyString(), anyString()))
               .thenThrow(new TimeoutException());

        Mockito.when(cliService.getCommitList(anyInt(), anyInt(), anyString(), anyString()))
               .thenThrow(new TimeoutException());

        QuarkusMock.installMockForType(apiService, GitHubAPIService.class);
        QuarkusMock.installMockForType(cliService, GitHubCLIService.class);

        given()
            .when()
            .get("lister/commits/test/test-timeout")
            .then()
            .statusCode(504)
            .body("message", equalTo(ErrorCodeImpl.REQUEST_TIMEOUT.getMessage()),
                  "code", equalTo(ErrorCodeImpl.REQUEST_TIMEOUT.getCode()));
    }

    @Test
    public void testFallback() {

        Mockito.when(apiService.getCommitList(anyInt(), anyInt(), anyString(), anyString()))
               .thenThrow(new TimeoutException());

        Mockito.when(cliService.getCommitList(anyInt(), anyInt(), anyString(), anyString()))
               .thenReturn(CommitPage.builder().build());

        QuarkusMock.installMockForType(apiService, GitHubAPIService.class);
        QuarkusMock.installMockForType(cliService, GitHubCLIService.class);

        given()
            .when()
            .get("lister/commits/test/test-fallback")
            .then()
            .statusCode(200);
    }
}