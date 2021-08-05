package com.lister.model.mapper;

import com.lister.model.api.dto.CommitItemDto;
import com.lister.model.api.dto.GitCLICommitDto;
import com.lister.model.api.dto.GitHubApiCommitDto;
import com.lister.model.jpa.Commit;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "cdi")
public abstract class CommitMapper {

    public abstract Commit gitCLICommitDtoToCommit(GitCLICommitDto gitCLICommitDto);

    @Mapping(expression = "java(commitDto.getCommit().getAuthor().getName() " +
                          "+ \" <\" + commitDto.getCommit().getAuthor().getEmail() + \">\" )", target = "author")
    @Mapping(source = "commit.author.date", target = "date")
    @Mapping(source = "commit.message", target = "message")
    public abstract Commit GitHubApiCommitDtoToCommit(GitHubApiCommitDto commitDto);

    @IterableMapping(qualifiedByName = "CommitToCommitDto")
    public abstract List<CommitItemDto> commitListToCommitItemDtoList(List<Commit> commitList);

    @Named("CommitToCommitDto")
    public abstract CommitItemDto commitToCommitItemDto(Commit commit);
}
