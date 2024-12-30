package com.multitenant.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "multi-tenant.aws.s3")
public record S3Properties (
        String accessKey,
        String secretKey,
        String region,
        String minioUrl
) {}
