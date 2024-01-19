package com.cdg.ngp.esb.location.config.mq;

import com.cdg.ngp.esb.location.config.properties.ActiveMQProperties;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
