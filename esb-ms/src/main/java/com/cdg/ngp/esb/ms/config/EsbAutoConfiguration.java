package com.cdg.ngp.esb.ms.config;

import com.cdg.ngp.esb.ms.config.properties.CommonProperties;
import com.cdg.ngp.esb.ms.handler.*;
import com.cdg.ngp.esb.ms.simulator.SimulatorBean;
import com.cdg.ngp.esb.ms.util.BatchingQueue;
import com.cdg.ngp.esb.ms.util.BeanHelper;
import com.cdg.ngp.esb.ms.util.HttpClientUtil;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName EsbAutoConfiguration
 * @Description Project configuration class
 * @Author siy
 * @Date 2024/1/17 10:58
 * @Version 1.0
 */

@Configuration
@EnableConfigurationProperties({CommonProperties.class})
public class EsbAutoConfiguration {

    @Autowired
    private CommonProperties commonProperties;
    @Autowired
    private ProducerTemplate combinedProducerTemplate;
    @Autowired
    private ProducerTemplate locationMsgQProducerTemplate;
    @Autowired
    private ProducerTemplate ODRDlocationMsgQProducerTemplate;
    @Autowired
    private ProducerTemplate esbLocationMsgQProducerTemplate;
    @Autowired
    private ProducerTemplate trafficScanQueueProducerTemplate;
    @Autowired
    private ProducerTemplate fuseMonitorResponseProducerTemplate;

    @Bean
    public HttpClientUtil httpClientUtil(){
        return new HttpClientUtil();
    }

    @Bean
    public SimulatorBean simulatorBean(){
        return new SimulatorBean();
    }

    @Bean
    public BeanHelper beanHelper(){
        return new BeanHelper();
    }

    @Bean
    public BatchingQueue batchingQueue(){
        return new BatchingQueue();
    }

    @Bean
    public TrafficScanHandler trafficScanHandler(){
        return new TrafficScanHandler();
    }

    @Bean
    public LocationServiceHandler locationServiceHandler(){
        LocationServiceHandler locationServiceHandler = new LocationServiceHandler();
        locationServiceHandler.setOfflineIvdStatus(commonProperties.getOfflineIvdStatus());
        return locationServiceHandler;
    }

    @Bean
    public AuthenticationServiceHandler authenticationServiceHandler(){
        AuthenticationServiceHandler authenticationServiceHandler = new AuthenticationServiceHandler();
        authenticationServiceHandler.setMdtPort(commonProperties.getLocalNewUdpPort());
        return authenticationServiceHandler;
    }

    @Bean
    public AuthenticationServiceRestHandler authenticationServiceRestHandler(HttpClientUtil httpClientUtil, AuthenticationServiceHandler authenticationServiceHandler){
        AuthenticationServiceRestHandler authenticationServiceRestHandler = new AuthenticationServiceRestHandler();
        authenticationServiceRestHandler.setHttpClientUtil(httpClientUtil);
        authenticationServiceRestHandler.setAuthServerRestApiUrl(commonProperties.getMsAsRestApi());
        authenticationServiceRestHandler.setAuthenticationServiceHandler(authenticationServiceHandler);
        return authenticationServiceRestHandler;
    }

    @Bean
    public StoreAndForwardHandler storeAndForwardHandler(){
        StoreAndForwardHandler storeAndForwardHandler = new StoreAndForwardHandler();
        storeAndForwardHandler.setMdtNewPort(commonProperties.getLocalNewUdpPort());
        storeAndForwardHandler.setMdtOldPort(commonProperties.getLocalOldUdpPort());
        List<Integer> list = new ArrayList<>();
        list.add(139);
        list.add(140);
        list.add(159);
        list.add(160);
        list.add(161);
        list.add(169);
        list.add(172);
        list.add(212);
        list.add(212);
        list.add(221);
        storeAndForwardHandler.setStoreAndFowardList(list);
        return storeAndForwardHandler;
    }

    @Bean
    public Cn2ServerHandler cn2ServerHandler(){
        Cn2ServerHandler cn2ServerHandler = new Cn2ServerHandler();
        cn2ServerHandler.setMdtNewPort(commonProperties.getLocalNewUdpPort());
        return cn2ServerHandler;
    }

    @Bean
    public IVDHandler ivdHandler(TrafficScanHandler trafficScanHandler, LocationServiceHandler locationServiceHandler){
        IVDHandler ivdHandler = new IVDHandler();
        ivdHandler.setEsbLocationUpdateSwitchRouteName("esbLocationUpdateSwitch");
        ivdHandler.setEapLocationUpdateSwitchRouteName("eapLocationUpdateSwitch");
        ivdHandler.setFuseHeartbeatString(commonProperties.getFuseHeartbeatString());
        ivdHandler.setOdrdEnabled(commonProperties.getOdrdEnabled());
        ivdHandler.setOdrdVehicleEnabled(commonProperties.getOdrdVehicleEnabled());
        // Producer
        ivdHandler.setCombinedProducer(this.combinedProducerTemplate);
        ivdHandler.setLocationMsgQProducer(this.locationMsgQProducerTemplate);
        ivdHandler.setFuseMonitorResponseProducer(this.fuseMonitorResponseProducerTemplate);
        ivdHandler.setTrafficScanQueueProducer(this.trafficScanQueueProducerTemplate);
        ivdHandler.setEsbLocationMsgQProducer(this.esbLocationMsgQProducerTemplate);
        ivdHandler.setOdrdLocationMsgQProducer(this.ODRDlocationMsgQProducerTemplate);
        ivdHandler.setTrafficScanHandler(trafficScanHandler);
        ivdHandler.setLocationServiceHandler(locationServiceHandler);
        return ivdHandler;
    }

}
