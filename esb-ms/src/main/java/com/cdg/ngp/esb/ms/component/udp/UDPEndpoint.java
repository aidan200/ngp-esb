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
package com.cdg.ngp.esb.ms.component.udp;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.support.DefaultEndpoint;
import org.apache.camel.MultipleConsumersSupport;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.*;
import java.util.regex.*;

/**
 * Represents a UDP endpoint.
 */
public class UDPEndpoint extends DefaultEndpoint implements MultipleConsumersSupport {
	UDPConnector connector = null;

    public UDPEndpoint() {
    }

    public UDPEndpoint(String uri, UDPComponent component) {
        super(uri, component);
    }

//    TODO  api out of date
//    public UDPEndpoint(String endpointUri) {
//        super(endpointUri);
//    }

    public Producer createProducer() throws Exception {
        return new UDPProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
    	throw new UDPConnectorException("Consumer not supported");
    }

    public boolean isSingleton() {
        return true;
    }
    
    @Override
    protected void doStart() throws Exception {          	
    	connector = new UDPConnector();
		connector.Start();
    }

    @Override
    protected void doStop() throws Exception {
    	connector.Stop();
    }
    
    public UDPConnector getConnector() {
    	return connector;
    }
    
	@Override
	public boolean isMultipleConsumersSupported() {
		return false;
	}
}
