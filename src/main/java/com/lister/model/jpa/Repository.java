package com.lister.model.jpa;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "Repository")
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class Repository extends PanacheEntityBase {

    @Id
    @Column(name = "name")
    private String name;

    @OneToMany(
        mappedBy = "repository",
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER
    )
    private List<Commit> commitList;

    public static boolean hasDataForRepositoryWithName(final String repository) {
        return find("name", repository).project(RepositoryProjectionWithoutCommits.class)
                                                 .firstResult()
               != null;
    }

}
