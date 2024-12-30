package com.multitenant.exceptions;

import java.util.Arrays;
import java.util.Optional;

public enum TenantExceptionCode {

    TENANT_BLANK_001("TENANT_BLANK_001"),
    TENANT_NOT_FOUND_002("TENANT_NOT_FOUND_002"),
    DATA_NOT_FOUND_003("DATA_NOT_FOUND_003");

    private final String errorCode;

    TenantExceptionCode (String errorCode) {
        this.errorCode = errorCode;
    }

    public Optional<TenantExceptionCode> fromStr (String strErrorCode) {
        return Arrays.stream(values())
                .filter(tec -> strErrorCode.equalsIgnoreCase(tec.errorCode))
                .findFirst();
    }

}
