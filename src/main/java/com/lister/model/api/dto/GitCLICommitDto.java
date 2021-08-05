package com.lister.model.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Data
@JsonNaming
@JsonPropertyOrder({"sha", "author", "message", "date"})
public class GitCLICommitDto {
    private String sha;
    private String author;
    private String message;
    private Instant date;

    @JsonProperty("message")
    public void setMessage(final String message){
        this.message = StringUtils.abbreviate(message, 512);
    }

    @JsonProperty("date")
    public void setDate(String date) {
        ZonedDateTime zonedDate = ZonedDateTime.parse(date, DateTimeFormatter.RFC_1123_DATE_TIME);
        this.date = zonedDate.toInstant();
    }
}
