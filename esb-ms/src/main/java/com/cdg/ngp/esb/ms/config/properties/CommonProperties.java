package com.cdg.ngp.esb.ms.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName CommonProperties
 * @Description Project general configuration file class
 * @Author siy
 * @Date 2024/1/16 19:32
 * @Version 1.0
 */

@Data
@ConfigurationProperties(prefix = "esb.common")
public class CommonProperties {

    private String localUdpIp;

    private int localOldUdpPort;

    private int localNewUdpPort;

    private int udpBufferSize;

    private int bufferConsumers;

    private String offlineIvdStatus;

    private String msAsRestApi;

    private String fuseHeartbeatString;

    private String odrdEnabled;

    private String odrdVehicleEnabled;

    private String locationMessageQueue;

    private String locationMessageTtl;

    private String odrdMdtLocationMessageQueue;

    private String odrdMdtLocationMessageTtl;

    private String esbLocationUpdateQueue;

    private String esbLocationUpdateTtl;

    private String trafficScanTtl;

    private int combinedConsumers;

    private int trafficScanConsumers;

    private String trafficScanIp;

    private int trafficScanPort;

    private int jmsRedeliveryDelay;

    private String msTlvComQueue;

    private int msQueueConsumers;

    private int pingMsg;

    private String pingQ;

    private String msTlvIvdQueue;

    private int ivdResponseConsumers;

    private int localHeartbeatUdpPort;


}
