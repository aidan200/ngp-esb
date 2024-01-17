package com.cdg.ngp.esb.ms.config.mq;

import com.cdg.ngp.esb.ms.config.properties.ActiveMQProperties;
import com.cdg.ngp.esb.ms.util.AMQConnectionFactorySupport;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ActiveMqAutoConfiguration
 * @Description ActiveMqAutoConfiguration
 * @Author siy
 * @Date 2024/1/16 13:09
 * @Version 1.0
 */

@Configuration
@EnableConfigurationProperties({ActiveMQProperties.class})
public class ActiveMqAutoConfiguration {

    @Autowired
    private ActiveMQProperties activeMQProperties;

    @Bean
    public ConnectionFactory pooledAllCF(){
        AMQConnectionFactorySupport factorySupport = new AMQConnectionFactorySupport();
        factorySupport.setAllBrokerURI(activeMQProperties.getAllBrokerUri());
        factorySupport.setUserName(activeMQProperties.getFuseUsername());
        factorySupport.setPassword(activeMQProperties.getFusePassword());
        PooledConnectionFactory factory = new PooledConnectionFactory();
        factory.setConnectionFactory(factorySupport);
        factory.setMaxConnections(activeMQProperties.getJmsMaxConnections());
        return factory;
    }

    @Bean
    public ConnectionFactory pooledMSCF(){
        AMQConnectionFactorySupport factorySupport = new AMQConnectionFactorySupport();
        factorySupport.setAllBrokerURI(activeMQProperties.getMsBrokerUri());
        factorySupport.setUserName(activeMQProperties.getMsUsername());
        factorySupport.setPassword(activeMQProperties.getMsPassword());
        PooledConnectionFactory factory = new PooledConnectionFactory();
        factory.setConnectionFactory(factorySupport);
        factory.setMaxConnections(activeMQProperties.getJmsMaxConnections());
        return factory;
    }

    @Bean
    public ConnectionFactory localCF() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(activeMQProperties.getLocalBrokerUri());
        factory.setUserName(activeMQProperties.getFuseUsername());
        factory.setPassword(activeMQProperties.getFusePassword());
        return factory;
    }

    @Bean
    public JmsComponent amqs(){
        JmsConfiguration configuration = new JmsConfiguration();
        configuration.setConnectionFactory(pooledAllCF());
        configuration.setDeliveryPersistent(false);
        JmsComponent jmsComponent = new JmsComponent();
        jmsComponent.setConfiguration(configuration);
        return jmsComponent;
    }

    @Bean
    public JmsComponent amqr(){
        JmsConfiguration configuration = new JmsConfiguration();
        configuration.setConnectionFactory(localCF());
        configuration.setAsyncConsumer(true);
        JmsComponent jmsComponent = new JmsComponent();
        jmsComponent.setConfiguration(configuration);
        return jmsComponent;
    }

    @Bean
    public JmsComponent amqsms(){
        JmsConfiguration configuration = new JmsConfiguration();
        configuration.setConnectionFactory(pooledMSCF());
        configuration.setDeliveryPersistent(false);
        JmsComponent jmsComponent = new JmsComponent();
        jmsComponent.setConfiguration(configuration);
        return jmsComponent;
    }

    @Bean
    public JmsComponent amqrms(){
        JmsConfiguration configuration = new JmsConfiguration();
        configuration.setConnectionFactory(pooledMSCF());
        configuration.setAsyncConsumer(true);
        JmsComponent jmsComponent = new JmsComponent();
        jmsComponent.setConfiguration(configuration);
        return jmsComponent;
    }
}
