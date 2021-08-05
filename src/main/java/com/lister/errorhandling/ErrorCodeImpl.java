package com.lister.errorhandling;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.HttpURLConnection;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public enum ErrorCodeImpl implements ErrorCode {

    NOT_FOUND_ERROR("001", "The given repository was not found. Please check that the name is correct, " +
                           "and it is not private.", HttpURLConnection.HTTP_NOT_FOUND),
    SERVICE_UNAVAILABLE_ERROR("002", "Couldn't contact GitHub service.", HttpURLConnection.HTTP_UNAVAILABLE),
    BAD_REQUEST("003", "Invalid request.", HttpURLConnection.HTTP_BAD_REQUEST),
    REQUEST_TIMEOUT("004", "Request has timed out.", HttpURLConnection.HTTP_GATEWAY_TIMEOUT);

    private final String code;
    private final String message;
    private final int httpStatusCode;
}
