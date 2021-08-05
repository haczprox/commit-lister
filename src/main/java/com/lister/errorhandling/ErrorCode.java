package com.lister.errorhandling;

public interface ErrorCode {
    String getCode();

    String getMessage();

    int getHttpStatusCode();
}
