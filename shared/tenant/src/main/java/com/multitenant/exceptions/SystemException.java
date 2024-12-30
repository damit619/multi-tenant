package com.multitenant.exceptions;

import lombok.Getter;

public class SystemException extends RuntimeException {

    @Getter
    private TenantExceptionCode tenantExceptionCode;
    private Throwable error;

    public SystemException(TenantExceptionCode tenantExceptionCode, Throwable error) {
        super(error);
        this.tenantExceptionCode = tenantExceptionCode;
    }

    public SystemException(TenantExceptionCode tenantExceptionCode, String message) {
        super(message);
        this.tenantExceptionCode = tenantExceptionCode;
    }

}
