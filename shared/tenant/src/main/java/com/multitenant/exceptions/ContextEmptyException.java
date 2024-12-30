package com.multitenant.exceptions;

public class ContextEmptyException extends SystemException {

    public ContextEmptyException(TenantExceptionCode tenantExceptionCode, Throwable error) {
        super(tenantExceptionCode, error);
    }

    public ContextEmptyException() {
        super(TenantExceptionCode.TENANT_BLANK_001, "TenantId should not be blank or null.");
    }
}
