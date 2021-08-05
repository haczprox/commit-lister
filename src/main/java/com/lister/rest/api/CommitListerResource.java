package com.lister.rest.api;

import com.lister.model.api.dto.CommitPage;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.PATH;
import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.QUERY;

@Path("/lister")
@Produces(APPLICATION_JSON)
public interface CommitListerResource {

    @Path("/commits/{owner}/{repository}")
    @GET
    @APIResponse(description = "Paginated list of commits.",
                 responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = CommitPage.class)))
    Response commitLister(
        @Valid
        @QueryParam("page")
        @Parameter(
            name = "page",
            description = "Page being requested.",
            example = "1",
            in = QUERY,
            schema = @Schema(type = SchemaType.INTEGER, minimum = "1", defaultValue = "1"))
        @DefaultValue("1")
        @Positive Integer page,
        @QueryParam("per_page")
        @Parameter(
            name = "per_page",
            description = "Number of records per page.",
            example = "5",
            in = QUERY,
            schema = @Schema(type = SchemaType.INTEGER, minimum = "1", defaultValue = "5", maximum = "25"))
        @DefaultValue("5")
        @Max(25)
        @Positive Integer perPage,
        @PathParam("owner")
        @Parameter(
            name = "owner",
            description = "Owner of the GitHub repository.",
            example = "resteasy",
            in = PATH,
            schema = @Schema(type = SchemaType.STRING))
            String owner,
        @PathParam("repository")
        @Parameter(
            name = "repository",
            description = "Name of the GitHub repository.",
            example = "Resteasy",
            in = PATH,
            schema = @Schema(type = SchemaType.STRING))
            String repository) throws IOException, InterruptedException;
}