package com.lister.service;

import com.lister.errorhandling.ServiceException;
import com.lister.model.api.dto.CommitPage;
import com.lister.model.api.dto.GitHubApiCommitDto;
import com.lister.model.mapper.CommitMapper;
import com.lister.model.jpa.Commit;
import com.lister.model.jpa.Repository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.client.exception.ResteasyWebApplicationException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.lister.errorhandling.ErrorCodeImpl.NOT_FOUND_ERROR;
import static com.lister.errorhandling.ErrorCodeImpl.SERVICE_UNAVAILABLE_ERROR;

@Slf4j
@ApplicationScoped
public class GitHubAPIService {

    @Inject
    @RestClient
    GitHubAPIClient gitHubAPIClient;

    @Inject
    CommitMapper commitMapper;

    @Transactional
    public CommitPage getCommitList(final int page, final int perPage,
                                             final String repositoryOwner,
                                             final String repositoryName) {

        log.info("Retrieving commits with Github Rest client");
        final String fullRepoName = repositoryOwner + "/" + repositoryName;
        Repository repository;

        if (Repository.hasDataForRepositoryWithName(fullRepoName)) {
            return getCommitPage(fullRepoName, page, perPage);
        }

        final List<GitHubApiCommitDto> gitHubApiCommitDtoList = new ArrayList<>();
        List<GitHubApiCommitDto> githubCommitPage;
        int index = 1;

        do {
            try {
                githubCommitPage = gitHubAPIClient.getCommitHistory(repositoryOwner, repositoryName, index++, 100);
                gitHubApiCommitDtoList.addAll(githubCommitPage);
            } catch (ResteasyWebApplicationException ex) {
                // Throw exception based on code
                System.out.println("Ex:" + ex);
                final int statusCode = ex.unwrap().getResponse().getStatus();
                if (statusCode == 404) {
                    throw new ServiceException(NOT_FOUND_ERROR);
                }
                throw new ServiceException(SERVICE_UNAVAILABLE_ERROR);
            }

            //setting hard limit of 5 pages due to GitHubApi rate limits
        } while (index <= 5 && githubCommitPage.size() > 0);

        List<Commit> commitList = new ArrayList<>();
        repository = Repository.of(fullRepoName, null);
        for (GitHubApiCommitDto commitDto: gitHubApiCommitDtoList) {
            commitList.add(commitMapper.GitHubApiCommitDtoToCommit(commitDto)
                                       .setRepository(repository));
        }
        repository.setCommitList(commitList);
        repository.persistAndFlush();

        log.info("Created entity for the" + repository.getName() + "repository.");

        return getCommitPage(fullRepoName, page, perPage);
    }

    private CommitPage getCommitPage(final String repositoryName, final int page, final int perPage) {
        PanacheQuery<Commit> commits = Commit.getCommitsForRepository(repositoryName, page, perPage);
        List<Commit> commitItems = commits.list();
        final int pageCount = commits.pageCount();
        final long recordCount = commits.count();

        return CommitPage.builder()
                         .page(page)
                         .perPage(perPage)
                         .count(commitItems.size())
                         .total(recordCount)
                         .totalPages(pageCount)
                         .items(commitMapper.commitListToCommitItemDtoList(commitItems))
                         .build();
    }


}
