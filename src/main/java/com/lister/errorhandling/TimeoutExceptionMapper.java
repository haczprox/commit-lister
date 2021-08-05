package com.lister.errorhandling;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.GATEWAY_TIMEOUT;

@Provider
@Slf4j
@ApplicationScoped
public class TimeoutExceptionMapper implements ExceptionMapper<TimeoutException> {

    @Override
    public Response toResponse(final TimeoutException exception) {

        log.error("{} exception caught: {}", TimeoutException.class.getName(), exception);

        final String genericErrorMessage = "Request has timed out.";

        final ErrorPayload errorPayload = ErrorPayload.builder()
                                                      .code(ErrorCodeImpl.REQUEST_TIMEOUT.getCode())
                                                      .message(genericErrorMessage)
                                                      .build();

        return Response.status(GATEWAY_TIMEOUT)
                       .entity(errorPayload)
                       .build();
    }
}
