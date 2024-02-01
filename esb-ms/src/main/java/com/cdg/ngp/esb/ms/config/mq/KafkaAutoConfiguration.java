package com.cdg.ngp.esb.ms.config.mq;

import com.cdg.ngp.esb.ms.config.properties.KafkaProperties;
import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName ActiveMqAutoConfiguration
 * @Description ActiveMqAutoConfiguration
 * @Author siy
 * @Date 2024/1/16 13:09
 * @Version 1.0
 */

@Configuration
@EnableConfigurationProperties({KafkaProperties.class})
public class KafkaAutoConfiguration {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    @ConditionalOnProperty(prefix = "esb.mq.kafka", name = "enabled", havingValue = "true")
    public KafkaComponent amqs(){
        KafkaComponent component = new KafkaComponent();
        component.getConfiguration().setBrokers(kafkaProperties.getAllBrokerUri());
        String groupId = UUID.randomUUID().toString();
        component.getConfiguration().setGroupId(groupId);
        Map<String, Object> additionalProperties = new HashMap<>();
        additionalProperties.put("spring.json.trusted.packages", "*");
        component.getConfiguration().setAdditionalProperties(additionalProperties);
        component.getConfiguration().setKeyDeserializer(StringDeserializer.class.getName());
        component.getConfiguration().setKeySerializer(StringSerializer.class.getName());
        component.getConfiguration().setValueDeserializer(JsonDeserializer.class.getName());
        component.getConfiguration().setValueSerializer(JsonSerializer.class.getName());
        return component;
    }

    @Bean
    @ConditionalOnProperty(prefix = "esb.mq.kafka", name = "enabled", havingValue = "true")
    public KafkaComponent amqr(){
        KafkaComponent component = new KafkaComponent();
        component.getConfiguration().setBrokers(kafkaProperties.getLocalBrokerUri());
        Map<String, Object> additionalProperties = new HashMap<>();
        additionalProperties.put("spring.json.trusted.packages", "*");
        component.getConfiguration().setAdditionalProperties(additionalProperties);
        component.getConfiguration().setKeyDeserializer(StringDeserializer.class.getName());
        component.getConfiguration().setKeySerializer(StringSerializer.class.getName());
        component.getConfiguration().setValueDeserializer(JsonDeserializer.class.getName());
        component.getConfiguration().setValueSerializer(JsonSerializer.class.getName());
        return component;
    }

}
