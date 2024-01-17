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
package com.cdg.ngp.esb.ms.component.tcp;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.support.BridgeExceptionHandlerToErrorHandler;
import org.apache.camel.support.DefaultEndpoint;
import org.apache.camel.MultipleConsumersSupport;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.*;
import java.util.regex.*;

/**
 * Represents a TCP endpoint.
 */
public class TCPEndpoint extends DefaultEndpoint implements MultipleConsumersSupport {
	TCPConnector connector = null;
	TCPConsumer consumer = null;

    public TCPEndpoint() {
    }

    public TCPEndpoint(String uri, TCPComponent component) {
        super(uri, component);
    }

//    TODO api out of date
//    public TCPEndpoint(String endpointUri) {
//        super(endpointUri);
//    }

    public Producer createProducer() throws Exception {
        return new TCPProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
    	consumer = new TCPConsumer(this, processor);
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
//		    	connector = new TCPConnector(uri.getHost(),uri.getPort()) {
//					@Override
//					protected void ReceiveCallback(Object msg) throws Exception {
//				        Exchange exchange = createExchange();
//				        exchange.getIn().setBody(msg);
//				        if (consumer!=null)
//							consumer.getProcessor().process(exchange);
//					}
//					@Override
//					protected boolean ReconnectCallback() {
//						return true;
//					}
//					@Override
//					protected void DisconnectCallback() {
//				        if (consumer!=null)
//							consumer.getExceptionHandler().handleException(new TCPConnectorException("TCPConnector disconnected!"));
//					}
//					@Override
//					protected void ExceptionCallback(TCPConnectorException e) {
//				        if (consumer!=null)
//							consumer.getExceptionHandler().handleException(e);
//					}
//		    	};
//		    	connector.setCodec(((TCPComponent)getComponent()).getCodec());
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
    
    public TCPConnector getConnector() {
    	return connector;
    }
    
	@Override
	public boolean isMultipleConsumersSupported() {
		return false;
	}
}
