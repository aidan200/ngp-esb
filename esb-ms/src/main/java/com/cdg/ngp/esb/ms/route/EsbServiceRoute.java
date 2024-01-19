package com.cdg.ngp.esb.ms.route;

import com.cdg.ngp.esb.ms.config.properties.CommonProperties;
import io.netty.buffer.ByteBuf;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.ConnectException;

/**
 * @ClassName EsbServiceRoute
 * @Description camel routing configuration class
 * @Author siy
 * @Date 2024/1/17 10:40
 * @Version 1.0
 */

@Component
public class EsbServiceRoute extends RouteBuilder {

    @Autowired
    private CommonProperties commonProperties;


    @Override
    public void configure() throws Exception {
        from("netty:udp://" + commonProperties.getLocalUdpIp() + ":" + commonProperties.getLocalNewUdpPort() + "?allowDefaultCodec=false&sync=false&receiveBufferSizePredictor=" + commonProperties.getUdpBufferSize())
                .routeId("ivd-udp-new")
                .process("byteBufProcess")//ByteBuf Copy Process
                .to("seda:udpNewBuffer?queue=#batchingQueue");

        from("timer://myTimer?period=2000").setBody().simple("myTimertest1")
                .to("seda:ivdResponse");

        from("seda:udpNewBuffer?concurrentConsumers=" + commonProperties.getBufferConsumers())
                .routeId("udp-new-buffer")
                .bean("ivdHandler", "process(${exchange},true)")
                .to("seda:combined");

        from("seda:combined?concurrentConsumers=" + commonProperties.getCombinedConsumers())
                .routeId("combinedRoute")
                .multicast().parallelProcessing()
                .to("seda:locationService") //send locationService
                .to("seda:trafficScan")
                .to("seda:authenticationService")
                .to("seda:storeAndForward")
                .end();

        //locationService seda
        from("seda:locationService?concurrentConsumers=" + commonProperties.getCombinedConsumers())
                .routeId("locationServiceRoute")
                .choice()
                    .when().simple("${header.IVD_MESSAGE_TYPE} in 'POWER_UP,LOGON_REQUEST'")
                        .bean("locationServiceHandler", "prepareLoginInfo")
                        .to("direct:locationServiceDirect")
                    .when().simple("${header.IVD_MESSAGE_TYPE} not in 'VOICE_STREAMING_MESSAGE'")
                        .bean("locationServiceHandler", "prepareForLocationService")
                        .to("direct:locationServiceDirect")
                .endChoice();

        //locationService Direct
        from("direct:locationServiceDirect")
                .routeId("locationServiceDirectRoute")
                .choice()
                    .when().simple("${bean:beanHelper?method=checkStarted(${exchange},'eapLocationUpdateSwitch')}")
                    .log("IVDMessage send message to LocationMessageQueue")
                        .to("amqs:" + commonProperties.getLocationMessageQueue() + "?timeToLive=" + commonProperties.getLocationMessageTtl())
                    .choice()
                        .when().simple( commonProperties.getOdrdVehicleEnabled() + " == 'true' || " + commonProperties.getOdrdEnabled() + " == 'true'")
                            .log("IVDMessage send message to OdrdMdtLocationMessageQueue")
                            .to("amqs:" + commonProperties.getOdrdMdtLocationMessageQueue() + "?timeToLive=" + commonProperties.getOdrdMdtLocationMessageTtl())
                    .endChoice()
                .choice()
                    .when().simple("${bean:beanHelper?method=checkStarted(${exchange},'esbLocationUpdateSwitch')} && ${header.IVD_MESSAGE_TYPE} == 'REGULAR_REPORT'")
                        .log("IVDMessage send message to EsbLocationUpdateQueue")
                        .to("amqs:" + commonProperties.getEsbLocationUpdateQueue() + "?timeToLive=" + commonProperties.getEsbLocationUpdateTtl())
                .endChoice();

        from("seda:trafficScan?concurrentConsumers=" + commonProperties.getCombinedConsumers())
                .routeId("trafficScanRoute")
                .choice()
                    .when().simple("${header.IVD_MESSAGE_TYPE} not in 'POWER_UP,INIT_MESSAGE,VOICE_STREAMING_MESSAGE,PAYMENT_MESSAGE,NETS_MESSAGE,EVOUCHER_MESSAGE,OTA_UPDATE_STATUS,OTA_UPLINK,MESSAGE_ACKNOWLEDGE,REPORT_TRIP_INFORMATION_NORMAL'")
                        .bean("trafficScanHandler", "prepareForTrafficScan")
                        .to("amqs:trafficScanQueue?timeToLive=" + commonProperties.getTrafficScanTtl())
                .endChoice();

        from("amqr:trafficScanQueue?concurrentConsumers=" + commonProperties.getTrafficScanConsumers())
                .routeId("trafficScanQueueRoute")
                .bean("beanHelper", "changeBytesToChannelBuffer")
                .to("netty:tcp://" + commonProperties.getTrafficScanIp() + ":" + commonProperties.getTrafficScanPort() + "?allowDefaultCodec=false&sync=false")
                .log("Finish sending message to Traffic Scan for ${header.IVD_MESSAGE_STRING}")
                .onException(ConnectException.class, Exception.class)
                //.process("byteBufProcess")
                .retryWhile(simple("${bean:beanHelper?method=checkTTL}"))
                    .log("retry Traffic Scan")
                    .maximumRedeliveries(-1).redeliveryDelay(commonProperties.getJmsRedeliveryDelay())
                .handled(true)
                .log(LoggingLevel.WARN, "Cannot send message to Traffic Scan for ${header.IVD_MESSAGE_STRING}");

        from("seda:authenticationService?concurrentConsumers=" + commonProperties.getCombinedConsumers())
                .routeId("authenticationServiceRoute")
                .log("authenticationService ivd message type ${header.IVD_MESSAGE_TYPE}")
                .choice()
                    .when().simple("${header.IVD_MESSAGE_TYPE} in 'POWER_UP,LOGON_REQUEST,IVD_HARDWARE_INFO,LOGOUT_REQUEST' && ${header.IVD_MESSAGE.isNew} == true")
                        .log("Send message to Authentication for ${header.IVD_MESSAGE_STRING} ")
                        .bean("authenticationServiceRestHandler", "sendMessageToAuthServer")
                        .log("authenticationService got message from Authentication Server")
                    .when().simple("${header.AUTH_ACK_FLAG} in 'TRUE'")
                        .to("seda:ivdResponse")
                .endChoice();

        from("seda:storeAndForward?concurrentConsumers=" + commonProperties.getCombinedConsumers())
                .routeId("storeAndForwardRoute")
                .bean("storeAndForwardHandler", "prepareStoreAndForward")
                .choice()
                .when().simple("${header.ACK_FlAG} in 'TRUE'")
                .to("seda:ivdResponse")
                .endChoice();

//        from("amqrms:" + commonProperties.getMsTlvComQueue() + "?concurrentConsumers=" + commonProperties.getMsQueueConsumers())
//                .routeId("MicroServicesToMdtRoute-new")
//                .log("CN2ServerHandler got message from MSTlvCOMQueue")
//                .bean("cn2ServerHandler", "sendNewMsgToMdt")
//                .choice()
//                    .when().simple("${header.PING} in '" + commonProperties.getPingMsg() + "'")
//                        .log("start send ................")
//                        .to("amqsms:" + commonProperties.getPingQ())
//                        .log("sending PING feed back to vehicle-common service")
//                        .log("body1 : '${body}' ")
//                        .setBody().simple("${header.RESPONSE_BYTES}")
//                        .log("body1 : '${body}' ")
//                        .to("seda:ivdResponse")
//                    .otherwise()
//                        .to("seda:ivdResponse")
//                .endChoice();
        // TODO Is it abandoned ？
        from("direct:tlvTap")
                .routeId("simulatorRoute_new")
                .bean("simulatorBean", "dispatchMessageFromTlvQ")
                .to("amqsms:" + commonProperties.getMsTlvIvdQueue());

        from("seda:ivdResponse?concurrentConsumers=" + commonProperties.getIvdResponseConsumers())
                .routeId("ivdResponse")
                .log("Message sent to MDT, ip:${header.UDP_DEST_NAME}, port:${header.UDP_DEST_PORT}, ${bean:beanHelper?method=msgHeaderBytesFromBackend}")
                .to("udps:ivd");

        from("seda:esbLocationUpdateSwitch")
                .routeId("esbLocationUpdateSwitch")
                .log("Dummy route for ESB Location Update Switch");

        from("seda:eapLocationUpdateSwitch")
                .routeId("eapLocationUpdateSwitch")
                .log("Dummy route for EAP Location Update Switch");

        from("seda:fuseMonitorResponseQueue")
                .routeId("fuseMonitorResponse")
                .to("netty:udp://localhost:" + commonProperties.getLocalHeartbeatUdpPort() + "?allowDefaultCodec=false&sync=false")
                .log(LoggingLevel.DEBUG, "Responded to fuse heartbeat ${header.HEARTBEAT_NO}");

    }
}
