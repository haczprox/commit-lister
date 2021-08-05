package com.lister.service;

import com.lister.errorhandling.ServiceException;
import com.lister.model.api.dto.CommitPage;
import com.lister.model.api.dto.GitCLICommitDto;
import com.lister.model.jpa.Commit;
import com.lister.model.jpa.Repository;
import com.lister.model.mapper.CommitMapper;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.lister.errorhandling.ErrorCodeImpl.SERVICE_UNAVAILABLE_ERROR;

@Slf4j
@ApplicationScoped
public class GitHubCLIService {

    @Inject
    GitHubCLIClient githubCliClient;

    @Inject
    CommitMapper commitMapper;

    @Transactional
    public CommitPage getCommitList(final int page, final int perPage,
                                             final String repositoryOwner,
                                             final String repositoryName) {

        log.info("Retrieving commits with CLI client");

        final String fullRepoName = repositoryOwner + "/" + repositoryName;
        Repository repository;

        if (Repository.hasDataForRepositoryWithName(fullRepoName)) {
            return getCommitPage(fullRepoName, page, perPage);
        }

        List<GitCLICommitDto> gitCLICommitDtoList;
        try {
            gitCLICommitDtoList = githubCliClient.getCommitHistory(repositoryOwner, repositoryName);
        } catch (IOException e) {
            throw new ServiceException(SERVICE_UNAVAILABLE_ERROR);
        }

        List<Commit> commitList = new ArrayList<>();
        repository = Repository.of(fullRepoName, null);

        for (GitCLICommitDto gitCLICommitDto : gitCLICommitDtoList) {
            commitList.add(commitMapper.gitCLICommitDtoToCommit(gitCLICommitDto)
                                       .setRepository(repository));
        }

        repository.setCommitList(commitList);
        repository.persist();

        log.info("Created entity for the" + repository.getName() + " repository.");

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
                         .total(recordCount)
                         .count(commitItems.size())
                         .totalPages(pageCount)
                         .items(commitMapper.commitListToCommitItemDtoList(commitItems))
                         .build();
    }
}
