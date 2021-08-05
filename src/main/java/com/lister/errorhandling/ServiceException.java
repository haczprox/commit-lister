package com.lister.errorhandling;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    private final ErrorCode errorCode;

    public ServiceException(final ErrorCode errorCode) {
        this(errorCode, null);
    }

    public ServiceException(final ErrorCode errorCode, final Throwable cause) {

        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

}
