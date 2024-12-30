package com.multitenant.tenant;

import java.util.List;
import java.util.Optional;

public interface TenantService {

    List<Tenant> findAll();

    Optional<Tenant> findByTenantId(String tenantId);

    boolean isTenantValid(String tenantId);
}
