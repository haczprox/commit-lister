package com.lister.model.jpa;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
public class RepositoryProjectionWithoutCommits extends PanacheEntityBase {
    private String name;
}
