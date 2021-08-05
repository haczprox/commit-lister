package com.lister.errorhandling;

import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.stream.Collectors;

import static com.lister.errorhandling.ErrorPayload.Field;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
@Slf4j
@ApplicationScoped
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(final ConstraintViolationException exception) {

        log.error("{} exception caught: {}", ConstraintViolationException.class.getName(), exception);

        final String genericErrorMessage = "Constraint violation(s) occurred during input validation.";
        final List<Field> fields = exception.getConstraintViolations().stream()
                                                         .map(i -> Field.of(i.getPropertyPath().toString(), i.getMessage()))
                                                         .collect(Collectors.toList());


        final ErrorPayload errorPayload = ErrorPayload.builder()
                                                      .code(ErrorCodeImpl.BAD_REQUEST.getCode())
                                                      .message(genericErrorMessage)
                                                      .fields(fields)
                                                      .build();

        return Response.status(BAD_REQUEST)
                       .entity(errorPayload)
                       .build();
    }
}
