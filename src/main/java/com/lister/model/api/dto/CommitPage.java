package com.lister.model.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Builder
@JsonPropertyOrder({"page", "per_page", "count", "total", "total_pages", "items"})
public class CommitPage {

    @JsonProperty("page")
    @Schema(
        description = "The requested page.",
        example = "1"
    )
    private int page;

    @JsonProperty("per_page")
    @Schema(
        description = "The number of items per page.",
        example = "25",
        name = "per_page"
    )
    private int perPage;

    @JsonProperty("count")
    @Schema(
        description = "The number of items in this page.",
        example = "25"
    )
    private int count;

    @JsonProperty("total")
    @Schema(
        description = "The total number of items.",
        example = "500"
    )
    private long total;

    @JsonProperty("total_pages")
    @Schema(
        description = "The total number of pages.",
        example = "20",
        name = "total_pages"
    )
    private int totalPages;

    @JsonProperty("items")
    @Schema(
        description = "The list of commits.",
        type = SchemaType.ARRAY
    )
    private List<CommitItemDto> items;
}
