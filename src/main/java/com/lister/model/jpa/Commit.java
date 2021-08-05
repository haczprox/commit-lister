package com.lister.model.jpa;

import com.lister.model.api.dto.CommitPage;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Table(name = "Commit")
public class Commit extends PanacheEntity {
    private String sha;
    private String author;
    private Instant date;

    @Column(length = 512)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    private Repository repository;

    public Commit setRepository(final Repository repository) {
        this.repository = repository;
        return this;
    }

    public static PanacheQuery<Commit> getCommitsForRepository (final String repository, final int page, final int perPage) {
        PanacheQuery<Commit> commits = Commit.find("repository_name", repository)
                                             .page(Page.of(page - 1, perPage));
        return commits;
    }

}
