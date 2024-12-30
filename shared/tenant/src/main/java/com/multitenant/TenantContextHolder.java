package com.multitenant;

import com.multitenant.exceptions.ContextEmptyException;
import com.multitenant.exceptions.TenantNotFoundException;
import com.multitenant.tenant.Tenant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TenantContextHolder {

    private static final Logger logger = LoggerFactory.getLogger(TenantContextHolder.class);

    private static final ThreadLocal<Tenant> CURRENT_TENANT = new ThreadLocal<>();

    private TenantContextHolder() {}

    public static void setTenant(Tenant currentTenant) {
        if (StringUtils.isBlank(currentTenant.tenantId())) {
            throw new ContextEmptyException();
        }
        logger.trace("Tenant context set to={}", currentTenant);
        CURRENT_TENANT.set(currentTenant);
    }

    public static Tenant getTenant() {
        return CURRENT_TENANT.get();
    }

    public static Tenant getRequiredTenant() {
        var tenant = getTenant();
        if (StringUtils.isBlank(tenant.tenantId())) {
            throw new TenantNotFoundException();
        }
        return tenant;
    }

    public static void clear() {
        logger.trace("Cleaning up current tenant context.");
        CURRENT_TENANT.remove();
    }
}
