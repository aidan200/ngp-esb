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
/** 
 * @Class name : AMQConnectionFactorySupport.java
 * @Description :TODO
 * @Author tend
 * @Since 12 Feb, 2016
**/
package com.cdg.ngp.esb.ms.util;

import java.net.InetAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is an extension of ActiveMQConnectionFactory. It provides the accessor setAllBrokerURI
 * to allow a list of broker URIs to be specified in any order. Failover features are transparently
 * added so that the local IP is set to priority and all other IPs will be failed over in a random order.
 * 
 * @author tend
 *
 */
public class AMQConnectionFactorySupport extends ActiveMQConnectionFactory {
	private static final Logger LOGGER  = LoggerFactory.getLogger(AMQConnectionFactorySupport.class);
	
	private String allBrokerURI;
	private String[] localIPs = null;
	
	public String getAllBrokerURI() {
		return allBrokerURI;
	}

	public void setAllBrokerURI(String allBrokerURI) {
		this.allBrokerURI = allBrokerURI;
		
		String newAllBrokerURI = "";

		String[] brokerURIs = allBrokerURI.split(",");
		HashMap<String,String> ip2uri = new HashMap<String,String>();
		for (String brokerURI : brokerURIs) {
			try {
				URI uri = new URI(brokerURI);
				String host = uri.getHost();
				host = InetAddress.getByName(host).getHostAddress(); 
				while (ip2uri.containsKey(host))
					host+="_";
				ip2uri.put(host,brokerURI);
			}
			catch (Exception e) {
				LOGGER.error("Invalid URI : "+brokerURI,e);
			}
		}
		
		synchronized(this) {
			if (localIPs==null) {
				try {localIPs = Helper.getLocalIPs();} catch (Exception e) {LOGGER.info("Caught an exception :" + e);}
			}
		}
		if (localIPs!=null) {
			for (String localIP : localIPs) {
				String localUri = ip2uri.get(localIP);
				if (localUri!=null) {
					newAllBrokerURI = localUri;
					ip2uri.remove(localIP);
					break;
				}
			}
		}
		
		Random rn = new Random();
		ArrayList<String> uris = new ArrayList<String>(ip2uri.values());
		while (uris.size()>0) {
			int i = rn.nextInt(uris.size());
			newAllBrokerURI+=(","+uris.get(i));
			uris.remove(i);
		}
		if (newAllBrokerURI.startsWith(","))
			newAllBrokerURI = newAllBrokerURI.substring(1);
		
		LOGGER.info("Adjusted allBrokerURI to "+newAllBrokerURI);
		
		setBrokerURL("failover:("+newAllBrokerURI+")?randomize=false&priorityBackup=true");
	}
}