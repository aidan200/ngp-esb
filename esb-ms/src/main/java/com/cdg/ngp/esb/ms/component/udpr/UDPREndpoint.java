/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cdg.ngp.esb.ms.component.udpr;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.netty.NettyConstants;
import org.apache.camel.support.BridgeExceptionHandlerToErrorHandler;
import org.apache.camel.support.DefaultEndpoint;
import org.apache.camel.MultipleConsumersSupport;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.URI;
import java.util.*;
import java.util.regex.*;

/**
 * Represents a TCP endpoint.
 */
public class UDPREndpoint extends DefaultEndpoint implements MultipleConsumersSupport {
	private static final Logger log = LoggerFactory.getLogger(UDPREndpoint.class);

	UDPRConnector connector = null;
	UDPRConsumer consumer = null;

	int messageCount = 0;
	
    public UDPREndpoint() {
    }

    public UDPREndpoint(String uri, UDPRComponent component) {
        super(uri, component);
    }

	// TODO api out of date
//    public UDPREndpoint(String endpointUri) {
//        super(endpointUri);
//    }

    public Producer createProducer() throws Exception {
    	throw new UDPRConnectorException("Producer not supported");
    }

    public Consumer createConsumer(Processor processor) throws Exception {
    	consumer = new UDPRConsumer(this, processor);
    	consumer.setExceptionHandler(new BridgeExceptionHandlerToErrorHandler(consumer));

        return consumer;
    }

    public boolean isSingleton() {
        return true;
    }
    
    @Override
    protected void doStart() throws Exception {
		System.out.println("====================滴滴～！");
//    	URI uri = getEndpointConfiguration().getURI();
//
//    	synchronized(this) {
//    		if (connector==null) {
//		    	connector = new UDPRConnector(uri.getHost(),uri.getPort()) {
//					@Override
//					protected void ReceiveCallback(Object msg) throws Exception {
//						long startTime = System.nanoTime();
//
//						DatagramPacket receivePacket = (DatagramPacket)msg;
//						//TODO unknown risk
//						ByteBuf buffer = Unpooled.wrappedBuffer(receivePacket.getData(),0,receivePacket.getLength());
//						//ChannelBuffer buffer = ChannelBuffers.copiedBuffer(receivePacket.getData(),0,receivePacket.getLength());
//						long A = (System.nanoTime()-startTime);
//				        Exchange exchange = createExchange();
//						long B = (System.nanoTime()-startTime);
//				        exchange.getIn().setBody(buffer);
//				        exchange.getIn().setHeader(NettyConstants.NETTY_REMOTE_ADDRESS,receivePacket.getSocketAddress());
//				        if (consumer!=null)
//							consumer.getProcessor().process(exchange);
//						long C = (System.nanoTime()-startTime);
//
//				        messageCount++;
//						log.info("Message count : "+messageCount+". Time in ReceiveCallback : "+A+", "+B+", "+C);
//					}
//					@Override
//					protected boolean ReconnectCallback() {
//						return true;
//					}
//					@Override
//					protected void DisconnectCallback() {
//				        if (consumer!=null)
//							consumer.getExceptionHandler().handleException(new UDPRConnectorException("UDPRConnector disconnected!"));
//					}
//					@Override
//					protected void ExceptionCallback(UDPRConnectorException e) {
//				        if (consumer!=null)
//							consumer.getExceptionHandler().handleException(e);
//					}
//		    	};
//    		}
//    		if (!connector.isStarted())
//    			connector.Start();
//    	}
    }

    @Override
    protected void doStop() throws Exception {
    	synchronized(this) {
    		if (connector!=null)
    			connector.Shutdown();
    	}
    }
    
    public UDPRConnector getConnector() {
    	return connector;
    }
    
	@Override
	public boolean isMultipleConsumersSupported() {
		return false;
	}
}
