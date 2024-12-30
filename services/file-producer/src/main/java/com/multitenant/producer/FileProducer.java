package com.multitenant.producer;

import com.multitenant.events.FileMessage;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class FileProducer {

    private static final Logger logger = LoggerFactory.getLogger(FileProducer.class);
    private static final String TENANT_HEADER = "X-TenantId";

    private final KafkaTemplate<String, FileMessage> tenantKafkaTemplate;

    private final String tenantTopic;

    public FileProducer(KafkaTemplate<String, FileMessage> tenantKafkaTemplate,
                        @Value("${multi-tenant.kafka.topic.tenant-topic}") String tenantTopic) {
        this.tenantKafkaTemplate = tenantKafkaTemplate;
        this.tenantTopic = tenantTopic;
    }

    public void sendTenantEvent(String tenantId, FileMessage fileMessage) {
        ProducerRecord<String, FileMessage> fileMessageProducerRecord = new ProducerRecord<>(tenantTopic, fileMessage);
        fileMessageProducerRecord.headers().add(TENANT_HEADER, tenantId.getBytes(StandardCharsets.UTF_8));
        tenantKafkaTemplate.send(fileMessageProducerRecord)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        logger.info("Sent message=[{}] with offset=[{}]", fileMessage, result.getRecordMetadata().offset());
                    } else {
                        logger.error("Unable to send message=[{}] due to : {}", fileMessage, ex.getMessage());
                    }
                });
    }
}
