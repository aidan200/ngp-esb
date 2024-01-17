package com.cdg.ngp.esb.ms.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName ActiveMqProperties
 * @Description ActiveMqProperties
 * @Author siy
 * @Date 2024/1/14 14:49
 * @Version 1.0
 */

@Data
@ConfigurationProperties(prefix = "siy.mq.active")
public class ActiveMQProperties {

    private String allBrokerUri;

    private String localBrokerUri;

    private String fuseUsername;

    private String fusePassword;

    private String msBrokerUri;

    private String msUsername;

    private String msPassword;

    private int jmsMaxConnections;


}
