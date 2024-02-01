package com.cdg.ngp.esb.ms.config.camel;

import com.cdg.ngp.esb.ms.component.process.ByteBufProcess;
import com.cdg.ngp.esb.ms.component.tcp.TCPComponent;
import com.cdg.ngp.esb.ms.component.tcp.codec.TCPLengthFieldCodec;
import com.cdg.ngp.esb.ms.component.udp.UDPComponent;
import com.cdg.ngp.esb.ms.config.properties.CommonProperties;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.engine.DefaultShutdownStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName CamelAutoConfiguration
 * @Description Project Camel configuration class
 * @Author siy
 * @Date 2024/1/17 10:56
 * @Version 1.0
 */

@Configuration
public class CamelAutoConfiguration {

    @Autowired
    private CommonProperties commonProperties;

    @Bean
    public DefaultShutdownStrategy defaultShutdownStrategy(){
        DefaultShutdownStrategy defaultShutdownStrategy = new DefaultShutdownStrategy();
        defaultShutdownStrategy.setTimeout(1L);
        return defaultShutdownStrategy;
    }

    @Bean
    public TCPComponent tcp(){
        TCPComponent tcpComponent = new TCPComponent();
        tcpComponent.setCodec(new TCPLengthFieldCodec());
        return tcpComponent;
    }

    @Bean
    public UDPComponent udps(){
        return new UDPComponent();
    }

    @Bean
    public ByteBufProcess byteBufProcess(){
        return new ByteBufProcess();
    }

    @Bean
    public ProducerTemplate combinedProducerTemplate(CamelContext camelContext) {
        ProducerTemplate template = camelContext.createProducerTemplate();
        template.setDefaultEndpointUri("seda:combined");
        return template;
    }

//    @Bean
//    public ProducerTemplate locationMsgQProducerTemplate(CamelContext camelContext) {
//        ProducerTemplate template = camelContext.createProducerTemplate();
//        template.setDefaultEndpointUri("amqs:" + commonProperties.getLocationMessageQueue() + "?timeToLive=" + commonProperties.getLocationMessageTtl());
//        return template;
//    }
//
//    @Bean
//    public ProducerTemplate ODRDlocationMsgQProducerTemplate(CamelContext camelContext) {
//        ProducerTemplate template = camelContext.createProducerTemplate();
//        template.setDefaultEndpointUri("amqs:" + commonProperties.getOdrdMdtLocationMessageQueue() + "?timeToLive=" + commonProperties.getOdrdMdtLocationMessageTtl());
//        return template;
//    }
//
//    @Bean
//    public ProducerTemplate esbLocationMsgQProducerTemplate(CamelContext camelContext) {
//        ProducerTemplate template = camelContext.createProducerTemplate();
//        template.setDefaultEndpointUri("amqs:" + commonProperties.getEsbLocationUpdateQueue() + "?timeToLive=" + commonProperties.getEsbLocationUpdateTtl());
//        return template;
//    }
//
//    @Bean
//    public ProducerTemplate trafficScanQueueProducerTemplate(CamelContext camelContext) {
//        ProducerTemplate template = camelContext.createProducerTemplate();
//        template.setDefaultEndpointUri("amqs:trafficScanQueue?timeToLive=" + commonProperties.getTrafficScanTtl());
//        return template;
//    }

    @Bean
    public ProducerTemplate fuseMonitorResponseProducerTemplate(CamelContext camelContext) {
        ProducerTemplate template = camelContext.createProducerTemplate();
        template.setDefaultEndpointUri("seda:fuseMonitorResponseQueue");
        return template;
    }

    @Bean
    public ProducerTemplate locationMsgQProducerTemplate(CamelContext camelContext) {
        ProducerTemplate template = camelContext.createProducerTemplate();
        template.setDefaultEndpointUri("amqs:" + commonProperties.getLocationMessageQueue());
        return template;
    }

    @Bean
    public ProducerTemplate ODRDlocationMsgQProducerTemplate(CamelContext camelContext) {
        ProducerTemplate template = camelContext.createProducerTemplate();
        template.setDefaultEndpointUri("amqs:" + commonProperties.getOdrdMdtLocationMessageQueue());
        return template;
    }

    @Bean
    public ProducerTemplate esbLocationMsgQProducerTemplate(CamelContext camelContext) {
        ProducerTemplate template = camelContext.createProducerTemplate();
        template.setDefaultEndpointUri("amqs:" + commonProperties.getEsbLocationUpdateQueue());
        return template;
    }

    @Bean
    public ProducerTemplate trafficScanQueueProducerTemplate(CamelContext camelContext) {
        ProducerTemplate template = camelContext.createProducerTemplate();
        template.setDefaultEndpointUri("amqs:trafficScanQueue");
        return template;
    }

}
