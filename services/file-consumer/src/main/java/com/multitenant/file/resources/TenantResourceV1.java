package com.multitenant.file.resources;

import com.multitenant.file.resources.exception.DataNotFoundException;
import com.multitenant.file.dto.TenantFileDataDTO;
import com.multitenant.file.service.FileDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(APIRoute.TENANT_RESOURCE)
public class TenantResourceV1 {

    private final FileDataService fileDataService;

    @GetMapping
    public ResponseEntity<TenantFileDataDTO> listFileData (@RequestParam("device-id") Integer deviceId) {
        return fileDataService.listFileByDeviceId(deviceId)
                .map(TenantFileDataDTO::toTenantFileDTO)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new DataNotFoundException("File Data not found for device-id=" + deviceId));
    }
}
