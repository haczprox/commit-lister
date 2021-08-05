package com.lister.service;

import com.lister.model.api.dto.GitHubApiCommitDto;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@ApplicationScoped
@RegisterRestClient
public interface GitHubAPIClient {
    @GET
    @Path("/repos/{owner}/{repository}/commits")
    @Produces(APPLICATION_JSON)
    List<GitHubApiCommitDto> getCommitHistory(@PathParam("owner") String owner,
                                              @PathParam("repository") String repository,
                                              @QueryParam("page") int page,
                                              @QueryParam("per_page") int perPage);
}
