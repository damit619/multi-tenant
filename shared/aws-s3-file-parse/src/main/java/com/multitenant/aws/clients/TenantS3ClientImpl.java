package com.multitenant.aws.clients;

import com.multitenant.events.FileMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;

@Component
public class TenantS3ClientImpl implements TenantS3Client {
    private final Logger logger = LoggerFactory.getLogger(TenantS3ClientImpl.class);

    private static final String COMMA_DELIMITER = ",";
    private final S3Client s3Client;

    public TenantS3ClientImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public void upload(String bucketName, String fileName, String data) {
        if (!isBucketExist(bucketName)) {
            createBucket(bucketName);
        }
        logger.info("Uploading object... to bucket {}", bucketName);
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build(),
                RequestBody.fromString(data));
        logger.info("Uploaded object... to bucket {}", bucketName);
    }

    @Override
    public InputStream download(FileMessage fileMessage) {
        GetObjectRequest tenantFileRequest = GetObjectRequest.builder()
                .bucket(fileMessage.getBucketName())
                .key(fileMessage.getFileName())
                .build();
        try {
           return s3Client.getObject(tenantFileRequest, ResponseTransformer.toInputStream());
        } catch (AwsServiceException e) {
            logger.error("Failed to get file bucketName={}, fileName={}", fileMessage.getBucketName(), fileMessage.getFileName(), e);
            throw e;
        }
    }

    @Override
    public void delete(FileMessage fileMessage) {
        try {
           DeleteObjectRequest tenantFileDeleteReq = DeleteObjectRequest.builder()
                            .bucket(fileMessage.getBucketName())
                            .key(fileMessage.getFileName())
                            .build();
            s3Client.deleteObject(tenantFileDeleteReq);
        } catch (AwsServiceException e) {
            logger.error("Failed to delete file bucketName={}, fileName={}", fileMessage.getBucketName(), fileMessage.getFileName(), e);
            throw e;
        }
    }


    public void createBucket(String bucketName) {
        try {
            s3Client.createBucket(CreateBucketRequest
                    .builder()
                    .bucket(bucketName)
                    .build());
            s3Client.waiter().waitUntilBucketExists(HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build());
            logger.info("Creating bucket: {}", bucketName);
        } catch (S3Exception e) {
            logger.error("Failed to delete file bucketName={}", bucketName, e);
            throw e;
        }
    }

    private boolean isBucketExist (String bucketName) {
        try {
            s3Client.headBucket(HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build());
            return true;
        } catch (NoSuchBucketException ignored) {}
        return false;
    }
}
