package com.cdg.ngp.esb.location.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName CommonProperties
 * @Description Project common configuration attribute class
 * @Author siy
 * @Date 2024/1/16 19:32
 * @Version 1.0
 */

@Data
@ConfigurationProperties(prefix = "siy.common")
public class CommonProperties {

    private String ESBLocationMsgQ;

    private String ESBVehicleLogMsgQ;

    private String ESBVehicleUpdateMsgQ;

    private int esbLocationServiceConsumers;

    private int esbLocationServiceCombinedConsumers;

    private int esbLocationServiceUpdateConsumers;

    private int esbLocationServiceLoggingConsumers;

    private String esbHanaAuthorization;

    private String esbHanaInsertRestApi;

    private int esbLocationServiceBatchSize;

    private int esbLocationServiceBatchInterval;

    private int esbLocationToHana;
}
