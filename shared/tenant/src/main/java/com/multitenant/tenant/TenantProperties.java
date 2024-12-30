package com.multitenant.tenant;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("multi-tenant")
public record TenantProperties (List<Tenant> tenants) { }
