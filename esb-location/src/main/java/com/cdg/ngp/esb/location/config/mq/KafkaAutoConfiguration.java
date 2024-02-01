package com.cdg.ngp.esb.location.config.mq;

import com.cdg.ngp.esb.location.config.properties.CommonProperties;
import com.cdg.ngp.esb.location.config.properties.KafkaProperties;
import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName KafkaAutoConfiguration
 * @Description TODO
 * @Author siy
 * @Date 2024/2/1 13:49
 * @Version 1.0
 */
@Configuration
@EnableConfigurationProperties({KafkaProperties.class})
public class KafkaAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "esb.mq.kafka", name = "enabled", havingValue = "true")
    public KafkaComponent amqr(KafkaProperties kafkaProperties){
        KafkaComponent component = new KafkaComponent();
        component.getConfiguration().setBrokers(kafkaProperties.getUrl());
        //component.getConfiguration().setTopic(commonProperties.getESBLocationMsgQ());
        component.getConfiguration().setGroupId("location");
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
    public KafkaComponent amqs(KafkaProperties kafkaProperties){
        KafkaComponent component = new KafkaComponent();
        component.getConfiguration().setBrokers(kafkaProperties.getUrl());
        //component.getConfiguration().setTopic("ESBLocationMsgQ");
        component.getConfiguration().setGroupId("location");
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
