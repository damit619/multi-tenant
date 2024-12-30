package com.multitenant.file.dto;

import com.multitenant.file.model.FileData;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TenantFileDataDTO {
    private Integer deviceId;
    private String model;
    private String manufacturer;
    private String deviceType;
    private LocalDate approvalDate;

    public static TenantFileDataDTO toTenantFileDTO (FileData fileData) {
        return TenantFileDataDTO.builder()
                .deviceId(fileData.getDeviceId())
                .deviceType(fileData.getDeviceType())
                .model(fileData.getModel())
                .manufacturer(fileData.getManufacturer())
                .approvalDate(fileData.getApprovalDate())
                .build();
    }
}
