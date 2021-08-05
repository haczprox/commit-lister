package com.lister.model.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;

@Data
@JsonNaming
public class GitHubApiCommitDto {
    private String sha;
    private Commit commit;

    @Data
    public static class Commit {
        private CommitAuthor author;
        private String message;

        @JsonProperty("message")
        public void setMessage(final String message){
            this.message = StringUtils.abbreviate(message, 512);
        }
    }

    @Data
    public static class CommitAuthor {
        private String name;
        private String email;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
        private Instant date;
    }
}
