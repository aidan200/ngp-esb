package com.cdg.ngp.esb.location.route;

import com.cdg.ngp.esb.common.data.Message;
import com.cdg.ngp.esb.location.config.properties.CommonProperties;
import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.activemq.ActiveMQEndpoint;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @ClassName EsbLocationServiceRoute
 * @Description camel routing configuration class
 * @Author siy
 * @Date 2024/1/16 12:14
 * @Version 1.0
 */

@Component
public class EsbLocationServiceRoute extends RouteBuilder {

    @Autowired
    private CommonProperties commonProperties;


    @Override
    public void configure() throws Exception {

//        from("timer://myTimer?period=2000").setBody().simple("myTimertest1")
//                        .to("seda:combined");
        //AMQ
//        from("amqr:queue:" + commonProperties.getESBLocationMsgQ() + "?concurrentConsumers=" + commonProperties.getEsbLocationServiceConsumers())
//                .routeId("esbLocationServiceRoute")
//                .bean("esbLocationServiceHandler", "process")
//                .to("seda:combined");
        //Kafka
        from("amqr:" + commonProperties.getESBLocationMsgQ())
                .threads(commonProperties.getEsbLocationServiceConsumers())
                .routeId("esbLocationServiceRoute")
                .log("xxxxx")
                .bean("esbLocationServiceHandler", "process")
                .to("seda:combined");
        //AMQ
//        from("seda:combined?concurrentConsumers=" + commonProperties.getEsbLocationServiceCombinedConsumers())
//                .routeId("combinedRoute")
//                .multicast().parallelProcessing()
//                .to("amqs:queue:" + commonProperties.getESBVehicleUpdateMsgQ(), "amqs:queue:" + commonProperties.getESBVehicleLogMsgQ())
//                .end();
        //Kafka
        from("seda:combined?concurrentConsumers=" + commonProperties.getEsbLocationServiceCombinedConsumers())
                .routeId("combinedRoute")
                .multicast().parallelProcessing()
                .to("amqs:" + commonProperties.getESBVehicleUpdateMsgQ(), "amqs:" + commonProperties.getESBVehicleLogMsgQ())
                .end();

        //AMQ
//        from("amqr:queue:" + commonProperties.getESBVehicleUpdateMsgQ() + "?concurrentConsumers=" + commonProperties.getEsbLocationServiceUpdateConsumers())
//                .routeId("esbVehicleUpdateRoute")
//                .bean("esbVehicleUpdateHandler", "process");
        //Kafka
        from("amqr:" + commonProperties.getESBVehicleUpdateMsgQ())
                .threads(commonProperties.getEsbLocationServiceUpdateConsumers())
                .routeId("esbVehicleUpdateRoute")
                .bean("esbVehicleUpdateHandler", "process");
        //AMQ
//        from("amqr:queue:" + commonProperties.getESBVehicleLogMsgQ() + "?concurrentConsumers=" + commonProperties.getEsbLocationServiceLoggingConsumers())
//                .routeId("esbVehicleLogRoute")
//                .bean("esbVehicleLogHandler","process");
        //Kafka
        from("amqr:" + commonProperties.getESBVehicleLogMsgQ())
                .threads(commonProperties.getEsbLocationServiceLoggingConsumers())
                .routeId("esbVehicleLogRoute")
                .bean("esbVehicleLogHandler","process");

                //.log("Created %${body.amount} discount for ${body.product.name}");
//        Message message = new Message(null, null);
//
//        from("timer://myTimer?period=5000").setBody().simple("myTimertest1")
//        .transform(constant(message))
//                .to("amqs:queue:siy.test");

    }
}
