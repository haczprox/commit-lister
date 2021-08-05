package com.lister.model.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.Instant;

@Data
@JsonNaming
@JsonPropertyOrder({"sha", "author", "message", "date"})
public class CommitItemDto {
    @JsonProperty("SHA")
    @Schema(
        description = "Checksum that identifies the commit.",
        example = "8e64b0650e0066ac1b73299affac3facd182a988",
        type = SchemaType.STRING
    )
    private String sha;

    @JsonProperty("author")
    @Schema(
        description = "The author of the commit.",
        example = "Luis Ventura",
        type = SchemaType.STRING
    )
    private String author;

    @JsonProperty("message")
    @Schema(
        description = "The commit message, including subject and body.",
        example = "Initial commit",
        type = SchemaType.STRING
    )
    private String message;

    @Schema(
        description = "The date of the commit.",
        example = "2021-07-21T15:13:31Z",
        type = SchemaType.STRING
    )
    private Instant date;
}
