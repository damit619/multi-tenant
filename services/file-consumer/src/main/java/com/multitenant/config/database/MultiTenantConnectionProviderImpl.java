package com.multitenant.config.database;

import com.multitenant.tenant.Tenant;
import com.multitenant.tenant.TenantService;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
@Component
public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider<String> {
    public static final String DEFAULT_TENANT = "DEFAULT_FAKE";

    private final DataSource datasource;
    private final TenantService tenantService;

    @Override
    public Connection getAnyConnection() throws SQLException {
        return getConnection(DEFAULT_TENANT);
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        String schema = tenantService.findByTenantId(tenantIdentifier)
                            .map(Tenant::schema)
                            .orElse(tenantIdentifier);
        Connection connection = datasource.getConnection();
        connection.setSchema(schema);
        return connection;
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        connection.setSchema(DEFAULT_TENANT);
        releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        throw new UnsupportedOperationException("Unimplemented method 'unwrap'.");
    }

}
