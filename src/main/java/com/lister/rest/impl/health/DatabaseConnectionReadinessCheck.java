package com.lister.rest.impl.health;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

@Slf4j
@Readiness
@ApplicationScoped
public class DatabaseConnectionReadinessCheck implements HealthCheck {


    @Inject
    protected EntityManager entityManager;

    @ConfigProperty(name = "com.lister.rest.impl.health.ready.datasource.table")
    protected String table;

    @ConfigProperty(name = "com.lister.rest.impl.health.ready.datasource.schema")
    protected String schema;

    public HealthCheckResponse call() {
        HealthCheckResponseBuilder
            healthCheckResponseBuilder = HealthCheckResponse.named("Database readiness check");

        try {
            this.entityManager.createNativeQuery(String.format(this.sql(), this.sqlParameters())).getResultList();
        } catch (PersistenceException var3) {
            log.error("Cannot connect to db", var3);
            return healthCheckResponseBuilder.withData("sql", "Error executing query: " + this.sql()).down().build();
        }

        return healthCheckResponseBuilder.up().build();
    }

    private String[] sqlParameters() {
        return new String[] {
            schema,
            table
        };
    }

    private String sql() {
        return "select 1 from %s.%s limit 1";
    }
}