package com.multitenant.config;

import com.multitenant.tenant.Tenant;
import com.multitenant.tenant.TenantService;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Component
public class TenantFlywayMigration implements InitializingBean, Ordered {
    private static final String TENANT_MIGRATION_LOCATION = "db/migration/tenants";

    private final DataSource dataSource;
    private final Flyway defaultFlyway;
    private final TenantService tenantService;

    @Override
    public void afterPropertiesSet() throws Exception {
        tenantService.findAll().stream()
                .map(Tenant::schema)
                .forEach(this::flywayMigrate);
    }

    private void flywayMigrate (String schema) {
        Flyway.configure()
                .configuration(defaultFlyway.getConfiguration())
                .locations(TENANT_MIGRATION_LOCATION)
                .dataSource(dataSource)
                .schemas(schema)
                .load()
                .migrate();
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
