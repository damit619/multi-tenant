package com.multitenant.file.resources.exception;

import com.multitenant.exceptions.SystemException;
import com.multitenant.exceptions.TenantExceptionCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DataNotFoundException extends SystemException {
    private HttpStatus httpStatus;

    public DataNotFoundException(TenantExceptionCode tenantExceptionCode, Throwable error) {
        super(tenantExceptionCode, error);
    }

    public DataNotFoundException(String message) {
        super(TenantExceptionCode.DATA_NOT_FOUND_003, message);
        this.httpStatus = HttpStatus.NOT_FOUND;
    }

}
