package com.multitenant.tenant;

public record Tenant (
    String tenantId,
    boolean enabled,
    String schema
) { }
