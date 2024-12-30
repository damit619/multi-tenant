package com.multitenant.resolvers;

import com.multitenant.tenant.Tenant;

@FunctionalInterface
public interface TenantResolver<T> {

    Tenant resolveTenant(T source);
}
