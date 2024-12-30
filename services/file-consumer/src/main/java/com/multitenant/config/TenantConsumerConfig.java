package com.multitenant.config;

import com.multitenant.events.FileMessage;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.springframework.kafka.listener.ContainerProperties.AckMode.*;

@EnableKafka
@Configuration
public class TenantConsumerConfig {

    @Value("${multi-tenant.kafka.bootstrap-servers:localhost:29092}")
    private String bootstrapServers;

    @Value("${multi-tenant.kafka.tenant.consumer.group-id}")
    private String groupId;

    public ConsumerFactory<String, FileMessage> tenantConsumerFactory() {
        JsonDeserializer<FileMessage> jsonDeserializer = new JsonDeserializer<>(FileMessage.class);
        jsonDeserializer.addTrustedPackages("com.capgemini.task.consumer.model.TenantData");
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(GROUP_ID_CONFIG, groupId);
        props.put(CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, jsonDeserializer);
        props.put(ENABLE_AUTO_COMMIT_CONFIG, false);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), jsonDeserializer);
        }

    @Bean("tenantContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, FileMessage> tenantKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, FileMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(tenantConsumerFactory());
        factory.getContainerProperties().setAckMode(MANUAL_IMMEDIATE);
        return factory;
    }

}
