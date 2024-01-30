package com.cdg.ngp.esb.location.config.mq;

import com.cdg.ngp.esb.common.data.Message;
import com.cdg.ngp.esb.location.config.properties.ActiveMQProperties;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ActiveMqAutoConfiguration
 * @Description ActiveMq Configuration class
 * @Author siy
 * @Date 2024/1/16 13:09
 * @Version 1.0
 */

@Configuration
@EnableConfigurationProperties({ActiveMQProperties.class})
public class ActiveMqAutoConfiguration {


    @Bean
    public ConnectionFactory connectionFactory(ActiveMQProperties activeMqProperties) {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(activeMqProperties.getUrl());
        if (activeMqProperties.getUsername() != null && activeMqProperties.getPassword() != null) {
            factory.setUserName(activeMqProperties.getUsername());
            factory.setPassword(activeMqProperties.getPassword());
        }
        factory.setTrustAllPackages(true);
        return factory;
    }

    @Bean
    public JmsComponent amqs(ActiveMQProperties activeMqProperties, ConnectionFactory connectionFactory){
        JmsConfiguration configuration = new JmsConfiguration();
        configuration.setConnectionFactory(connectionFactory);
        configuration.setTimeToLive(activeMqProperties.getEsbLocationServiceJmsTtl());
        configuration.setDeliveryPersistent(false);
        JmsComponent jmsComponent = new JmsComponent();
        jmsComponent.setConfiguration(configuration);
        return jmsComponent;
    }

//    @Bean
//    public KafkaComponent amqr(){
//        KafkaComponent component = new KafkaComponent();
//        component.getConfiguration().setBrokers("localhost:9092");
//        component.getConfiguration().setTopic("siy.test");
//        component.getConfiguration().setGroupId("test1");
//        Map<String, Object> additionalProperties = new HashMap<>();
//        additionalProperties.put("spring.json.trusted.packages", "*");
//        component.getConfiguration().setAdditionalProperties(additionalProperties);
//        component.getConfiguration().setKeyDeserializer(StringDeserializer.class.getName());
//        //JsonDeserializer<Message> valueDeserializer = new JsonDeserializer<>(Message.class);
//        component.getConfiguration().setValueDeserializer(JsonDeserializer.class.getName());
//        return component;
//    }

    @Bean
    public JmsComponent amqr(ConnectionFactory connectionFactory){
        JmsConfiguration configuration = new JmsConfiguration();
        configuration.setConnectionFactory(connectionFactory);
        configuration.setAsyncConsumer(true);
        JmsComponent jmsComponent = new JmsComponent();
        jmsComponent.setConfiguration(configuration);
        return jmsComponent;
    }

}
