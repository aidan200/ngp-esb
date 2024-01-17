/*
 * Copyright (c) 2015, COMFORT TRANSPORTATION PTE. LTD./COMFORTDELGRO PTE. LTD.
 * All right reserved.
 *
 * This software is confidential and a proprietary property of
 * COMFORT TRANSPORTATION PTE. LTD./COMFORTDELGRO PTE. LTD.
 *
 * The contents of this software shall not be modified or disclosed and shall
 * only be used in accordance with the terms and conditions stated in
 * the contract or license agreement with COMFORT TRANSPORTATION PTE. LTD./COMFORTDELGRO PTE. LTD.
 *
 * Redistribution and use in source or binary forms, with or without
 * modification, in fraction or whole are permitted provided that the following
 * conditions are met:
 *
 *   - Upon written approval from COMFORT TRANSPORTATION PTE. LTD./COMFORTDELGRO PTE. LTD.
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of COMFORT TRANSPORTATION PTE. LTD./COMFORTDELGRO PTE. LTD.
 *     nor the names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 */

package com.cdg.ngp.esb.ms.component.common;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

/** 
 * This class is used by components to perform tracking of JMS connections to JMS sessions. Currently only
 * used by {@link com.cdg.taxi.fuse.component.wmqr.WMQRConnector} as it provides multiple sessions to each 
 * connection.
**/
public class CSMap {
	Connection connection;
	Session[] sessions;
	MessageProducer[] producers;
	MessageConsumer[] consumers;
	boolean isConnected;
	long lastConnectedTime;
	
	public CSMap(int numSessionsPerConnection) {
		sessions = new Session[numSessionsPerConnection];
		consumers = new MessageConsumer[numSessionsPerConnection];
		producers = new MessageProducer[numSessionsPerConnection];
		isConnected = false;
		lastConnectedTime = 0;
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Session[] getSessions() {
		return sessions;
	}

	public void setSessions(Session[] sessions) {
		this.sessions = sessions;
	}

	public MessageProducer[] getProducers() {
		return producers;
	}

	public void setProducers(MessageProducer[] producers) {
		this.producers = producers;
	}

	public MessageConsumer[] getConsumers() {
		return consumers;
	}

	public void setConsumers(MessageConsumer[] consumers) {
		this.consumers = consumers;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	public long getLastConnectedTime() {
		return lastConnectedTime;
	}

	public void setLastConnectedTime(long lastConnectedTime) {
		this.lastConnectedTime = lastConnectedTime;
	}
}
