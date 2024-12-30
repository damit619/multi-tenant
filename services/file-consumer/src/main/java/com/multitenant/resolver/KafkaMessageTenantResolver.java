package com.multitenant.resolver;

import com.multitenant.exceptions.TenantNotFoundException;
import com.multitenant.resolvers.TenantResolver;
import com.multitenant.tenant.Tenant;
import com.multitenant.tenant.TenantService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class KafkaMessageTenantResolver implements TenantResolver<String> {

    private final TenantService tenantService;

    @Override
    public Tenant resolveTenant(String source) {
        if (StringUtils.isNotBlank(source)) {
            if (!tenantService.isTenantValid(source)) {
                throw new TenantNotFoundException("Kafka message has no valid tenant identifier.");
            }
            return tenantService.findByTenantId(source)
                    .orElse(null);
        }
        throw new TenantNotFoundException("Tenant identifier is not present in kafka message.");

    }
}
