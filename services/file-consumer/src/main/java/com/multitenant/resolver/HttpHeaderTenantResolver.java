package com.multitenant.resolver;

import com.multitenant.exceptions.TenantNotFoundException;
import com.multitenant.resolvers.TenantResolver;
import com.multitenant.tenant.Tenant;
import com.multitenant.tenant.TenantService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HttpHeaderTenantResolver implements TenantResolver<HttpServletRequest> {
    private static final String TENANT_HEADER = "X-TenantId";

    private final TenantService tenantService;

    @Override
    public Tenant resolveTenant(HttpServletRequest source) {
        var tenantId = source.getHeader(TENANT_HEADER);
        if (StringUtils.isNotBlank(tenantId)) {
            if (!tenantService.isTenantValid(tenantId)) {
                throw new TenantNotFoundException("The " + TENANT_HEADER + "=" + tenantId + " doesn't exist or not enabled");
            }
            return tenantService.findByTenantId(tenantId)
                    .orElse(null);
        }
        throw new TenantNotFoundException("The " + TENANT_HEADER + " header not present in the request.");
    }

}
