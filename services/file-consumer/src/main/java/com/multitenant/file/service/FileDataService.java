package com.multitenant.file.service;


import com.multitenant.events.FileMessage;
import com.multitenant.file.model.FileData;

import java.util.Optional;

public interface FileDataService {

    void createTenantFile (FileMessage fileMessage);

    Optional<FileData> listFileByDeviceId (Integer deviceId);

    void deleteTenantFile (FileMessage fileMessage);
}
