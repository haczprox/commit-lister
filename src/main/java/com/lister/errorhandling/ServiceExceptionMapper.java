package com.lister.errorhandling;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Slf4j
public class ServiceExceptionMapper implements ExceptionMapper<ServiceException> {

    public ServiceExceptionMapper() {
    }

    @Override
    public Response toResponse(final ServiceException exception) {

        log.error("{} exception caught: ", ServiceException.class.getName(), exception);

        ErrorPayload errorPayload = buildErrorPayload(exception);

        return Response.status(exception.getErrorCode().getHttpStatusCode())
                       .entity(errorPayload)
                       .build();
    }

    private ErrorPayload buildErrorPayload(final ServiceException exception) {
        return ErrorPayload.builder()
                           .code(exception.getErrorCode().getCode())
                           .message(exception.getMessage())
                           .build();
    }
}
