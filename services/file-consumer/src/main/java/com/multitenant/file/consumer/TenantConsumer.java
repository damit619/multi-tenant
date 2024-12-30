package com.multitenant.file.consumer;

import com.multitenant.events.FileMessage;
import com.multitenant.file.service.FileDataService;
import com.multitenant.TenantContextHolder;
import com.multitenant.exceptions.TenantNotFoundException;
import com.multitenant.resolver.KafkaMessageTenantResolver;
import com.multitenant.tenant.Tenant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TenantConsumer {
    private static final Logger logger = LoggerFactory.getLogger(TenantConsumer.class);
    private static final String TENANT_HEADER = "X-TenantId";
    private static final String MDC_TENANT_ID = "tenant_id";

    private final KafkaMessageTenantResolver kafkaMessageTenantResolver;
    private final FileDataService fileDataService;

    @KafkaListener(topics = "${multi-tenant.kafka.topic.tenant-topic}", containerFactory = "tenantContainerFactory")
    public void processRecord(@Payload FileMessage fileMessage,
                              @Header(TENANT_HEADER) String tenantId,
                              Acknowledgment acknowledgment) {
        logger.info("TenantConsumer received tenantData.bucketName={}, fileName={}", fileMessage.getBucketName(), fileMessage.getFileName());
        try {
            Tenant tenant = kafkaMessageTenantResolver.resolveTenant(tenantId);
            TenantContextHolder.setTenant(tenant);
            configureMDC(tenant.tenantId());
            createFile(fileMessage);
            acknowledgment.acknowledge();
        } catch (TenantNotFoundException tenantNotFoundException) {
            logger.error("Tenant not found. errorMessage={}, errorCode={}", tenantNotFoundException.getMessage(), tenantNotFoundException.getTenantExceptionCode().name());
        } finally {
            clear();
        }
    }

    public void createFile (FileMessage fileMessage) {
        try {
            fileDataService.createTenantFile(fileMessage);
            fileDataService.deleteTenantFile(fileMessage);
        } catch (Exception exception) {
            logger.error("Failed to save file data.", exception);
        }
    }

    private void configureMDC (String tenantId) {
        MDC.put(MDC_TENANT_ID, tenantId);
    }

    private void clear() {
        MDC.remove(MDC_TENANT_ID);
        TenantContextHolder.clear();
    }
}
