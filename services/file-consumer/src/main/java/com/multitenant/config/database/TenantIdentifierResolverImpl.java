package com.multitenant.config.database;

import com.multitenant.TenantContextHolder;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver<String> {
    public static final String DEFAULT_TENANT = "DEFAULT_FAKE";

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantContextHolder.getTenant() != null ? TenantContextHolder.getTenant().tenantId() : null;
        return Objects.requireNonNullElse(tenantId, DEFAULT_TENANT);
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}