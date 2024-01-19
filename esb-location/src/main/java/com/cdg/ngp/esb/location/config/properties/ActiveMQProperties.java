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
@ConfigurationProperties(prefix = "siy.mq.active")
public class ActiveMQProperties {
    /**
     * Message queue server path
     */
    private String url;

    private String username;

    private String password;

    private boolean enabled;

    private long esbLocationServiceJmsTtl;
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    public boolean isEnabled() {
//        return enabled;
//    }
//
//    public void setEnabled(boolean enabled) {
//        this.enabled = enabled;
//    }
}
