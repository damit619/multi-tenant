package com.multitenant.tenant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TenantServiceImpl implements TenantService {

    private final TenantProperties tenantProperties;

    @Override
    public List<Tenant> findAll() {
        return tenantProperties.tenants();
    }

    @Override
    public Optional<Tenant> findByTenantId(String tenantId) {
        return tenantProperties.tenants().stream()
                .filter(tenant -> tenant.tenantId().equalsIgnoreCase(tenantId))
                .filter(Tenant::enabled)
                .findFirst();
    }

    @Override
    public boolean isTenantValid(String tenantId) {
        return findByTenantId(tenantId)
                .isPresent();
    }
}
