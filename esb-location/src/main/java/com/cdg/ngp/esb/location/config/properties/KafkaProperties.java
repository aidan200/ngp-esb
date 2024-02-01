package com.cdg.ngp.esb.location.config.properties;

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
@ConfigurationProperties(prefix = "esb.mq.kafka")
public class KafkaProperties {
    /**
     * Message queue server path
     */
    private String url;

    private String username;

    private String password;

    private boolean enabled;

    private long esbLocationServiceJmsTtl;
    
}
