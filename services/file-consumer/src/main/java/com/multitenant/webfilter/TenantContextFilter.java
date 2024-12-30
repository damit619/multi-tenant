package com.multitenant.webfilter;

import com.multitenant.TenantContextHolder;
import com.multitenant.resolver.HttpHeaderTenantResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class TenantContextFilter extends OncePerRequestFilter {
    private static final String MDC_TENANT_ID = "tenant_id";

    private final HttpHeaderTenantResolver httpHeaderTenantResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tenant = httpHeaderTenantResolver.resolveTenant(request);
        TenantContextHolder.setTenant(tenant);
        configureMDC(tenant.tenantId());
        try {
            filterChain.doFilter(request, response);
        } finally {
            clear();
        }
    }

    private void configureMDC (String tenantId) {
        MDC.put(MDC_TENANT_ID, tenantId);
    }

    private void clear() {
        MDC.remove(MDC_TENANT_ID);
        TenantContextHolder.clear();
    }
}