package com.multitenant.exceptions;

public class TenantNotFoundException extends SystemException {

    public TenantNotFoundException(TenantExceptionCode tenantExceptionCode, Throwable error) {
        super(tenantExceptionCode, error);
    }

    public TenantNotFoundException(String message) {
        super(TenantExceptionCode.TENANT_NOT_FOUND_002, message);
    }

    public TenantNotFoundException() {
        super(TenantExceptionCode.TENANT_NOT_FOUND_002, "No tenant found in the current tenant context");
    }
}
