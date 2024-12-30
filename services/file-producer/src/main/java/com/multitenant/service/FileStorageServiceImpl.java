package com.multitenant.service;

import com.multitenant.aws.clients.TenantS3Client;
import com.multitenant.csv.CSVParser;
import com.multitenant.events.FileMessage;
import com.multitenant.csv.FileDataCSV;
import com.multitenant.producer.FileProducer;
import com.multitenant.tenant.TenantService;
import com.multitenant.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    private final TenantS3Client tenantS3Client;
    private final CSVParser csvParser;
    private final FileProducer fileProducer;
    private final TenantService tenantService;

    @Override
    public void storeFile(String fileName) {
        //Is file contains multiple tenant Id than parse file and create different buckets.
        logger.info("uploading file.. {}", fileName);
        csvParser.readFile(fileName).stream()
                .collect(Collectors.groupingBy(FileDataCSV::getTenantId))
                .entrySet()
                .stream()
                .filter(entry -> tenantService.isTenantValid(entry.getKey()))
                .forEach(entry -> {
                    String bucketName = entry.getKey().replace("_", "").toLowerCase() + "-bucket";
                    String s3FileName = DateUtils.currentTimeStamp(LocalDateTime.now()) + ".csv";
                    uploadFile(bucketName, s3FileName, entry.getValue());
                    sendMessage(entry.getKey(), bucketName, s3FileName);
                });
    }

    private void uploadFile(String bucketName, String fileName, List<FileDataCSV> fileData) {
        String data = csvParser.writeFile(fileData);
        tenantS3Client.upload(bucketName, fileName, data);
    }

    private void sendMessage (String tenantId, String bucketName, String fileName) {
        fileProducer.sendTenantEvent(tenantId, getFileMessage(bucketName, fileName));
    }

    private FileMessage getFileMessage (String bucketName, String fileName) {
        return FileMessage.builder()
                    .bucketName(bucketName)
                    .fileName(fileName)
                    .build();
    }

}
