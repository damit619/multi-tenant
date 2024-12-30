package com.multitenant.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.net.URI;

@Configuration
public class S3Config {

    @Bean
    public S3Client s3Client(S3Properties s3Properties) {
        S3ClientBuilder s3ClientBuilder = S3Client.builder()
                .credentialsProvider(credentialsProvider(s3Properties))
                .region(Region.of(s3Properties.region()))
                .httpClientBuilder(ApacheHttpClient.builder());

        if (StringUtils.isNotBlank(s3Properties.minioUrl())) {
            s3ClientBuilder.endpointOverride(URI.create(s3Properties.minioUrl()));
            s3ClientBuilder.forcePathStyle(true);
        }

        return s3ClientBuilder.build();
    }

    private AwsCredentialsProvider credentialsProvider (S3Properties s3Properties) {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(s3Properties.accessKey(), s3Properties.secretKey()));

    }
}
