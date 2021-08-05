package com.lister.model.mapper;

import com.lister.model.api.dto.GitCLICommitDto;
import com.lister.model.api.dto.GitHubApiCommitDto;
import com.lister.model.jpa.Commit;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommitMapperTest {

    CommitMapper mapper = Mappers.getMapper(CommitMapper.class);

    @Test
    void gitCLICommitDtoToCommit() {
        final GitCLICommitDto cliCommitDto = createTestCLICommitDto();
        final Commit commit = mapper.gitCLICommitDtoToCommit(cliCommitDto);

        assertEquals(cliCommitDto.getSha(), commit.getSha());
        assertEquals(cliCommitDto.getAuthor(), commit.getAuthor());
        assertEquals(cliCommitDto.getMessage(), commit.getMessage());
        assertEquals(cliCommitDto.getDate(), commit.getDate());
    }

    @Test
    void GitHubApiCommitDtoToCommit() {
        final GitHubApiCommitDto apiCommitDto = createTestAPICommitDto();
        final Commit commit = mapper.GitHubApiCommitDtoToCommit(apiCommitDto);
        final String commitAuthor = apiCommitDto.getCommit().getAuthor().getName() + " <"
                                    + apiCommitDto.getCommit().getAuthor().getEmail() + ">";
        assertEquals(apiCommitDto.getSha(), commit.getSha());
        assertEquals(commitAuthor, commit.getAuthor());
        assertEquals(apiCommitDto.getCommit().getMessage(), commit.getMessage());
        assertEquals(apiCommitDto.getCommit().getAuthor().getDate(), commit.getDate());
    }

    private GitHubApiCommitDto createTestAPICommitDto() {
        final GitHubApiCommitDto commitDto = new GitHubApiCommitDto();
        final GitHubApiCommitDto.Commit commit = new GitHubApiCommitDto.Commit();
        final GitHubApiCommitDto.CommitAuthor author = new GitHubApiCommitDto.CommitAuthor();

        author.setDate(Instant.now());
        author.setName("Test Author");
        author.setEmail("test@testing.com");

        commit.setAuthor(author);
        commit.setMessage("This is a test commit.");

        commitDto.setSha("test-sha");
        commitDto.setCommit(commit);

        return commitDto;
    }

    private GitCLICommitDto createTestCLICommitDto() {
        final GitCLICommitDto commitDto = new GitCLICommitDto();
        commitDto.setSha("test-sha");
        commitDto.setAuthor("Test author <test@testing.com>");
        commitDto.setMessage("This is a test commit.");
        commitDto.setDate("Thu, 29 Jul 2021 09:32:16 -0700");
        return commitDto;
    }
}
