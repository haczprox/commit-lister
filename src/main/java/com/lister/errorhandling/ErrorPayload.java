package com.lister.errorhandling;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorPayload {
    private String code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Field> fields;

    @Data
    @AllArgsConstructor(staticName = "of")
    @Schema(name = "error_field")
    public static class Field {
        private String name;
        private String description;
    }
}
