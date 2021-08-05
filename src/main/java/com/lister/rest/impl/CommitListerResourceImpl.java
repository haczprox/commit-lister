package com.lister.rest.impl;

import com.lister.errorhandling.ServiceException;
import com.lister.rest.api.CommitListerResource;
import com.lister.service.GitHubAPIService;
import com.lister.service.GitHubCLIService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.swing.text.html.Option;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class CommitListerResourceImpl implements CommitListerResource {

    @Inject
    GitHubCLIService gitHubCLIService;

    @Inject
    GitHubAPIService gitHubAPIService;

    @Override
    @Timeout(15000)
    @Fallback(fallbackMethod = "commitListerFallback", skipOn = ServiceException.class)
    public Response commitLister(final Integer page, final Integer perPage,
                                 final String owner, final String repository) {

        return Response.ok(gitHubAPIService.getCommitList(page, perPage, owner, repository))
                       .build();
    }

    @Timeout(15000)
    public Response commitListerFallback(final Integer page, final Integer perPage,
                                 final String owner, final String repository) {

        log.warn("Running fallback commit retriever.");
        return Response.ok(gitHubCLIService.getCommitList(page, perPage, owner, repository))
                       .build();
    }
}
