package com.multitenant.aws.clients;


import com.multitenant.events.FileMessage;

import java.io.InputStream;

public interface TenantS3Client {

    void upload(String bucketName, String fileName, String data);

    InputStream download (FileMessage fileMessage);

    void delete(FileMessage fileMessage);
}
