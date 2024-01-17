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
 * @Class name : TrafficScanHandler.java
 * @Description :DONE
 * @Author chaizhichao
 * @Since 12 Jan, 2016
**/
package com.cdg.ngp.esb.ms.handler;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cdg.ngp.esb.ms.dto.IVDMessageDetails;
import com.cdg.ngp.esb.ms.util.Helper;

public class TrafficScanHandler {
	private static final Logger log = LoggerFactory.getLogger(TrafficScanHandler.class);
	/**
	 * 
	 * @Description prepare data for TrafficScan
	 * @param exchange
	 * @return void
	 */
	public void prepareForTrafficScan(Exchange exchange) {
		IVDMessageDetails msgDetails = (IVDMessageDetails) exchange.getIn()
				.getHeader("IVD_MESSAGE");
		//1.MDT-IP 2.MDT-PORT 3.message type
		String mdtIpAddress = msgDetails.getPeerAddr().getAddress().getHostAddress();
		int mdtPort = msgDetails.getPeerAddr().getPort();
		String msgType = (String)exchange.getIn().getHeader("IVD_MESSAGE_TYPE");
		byte[] messageBytes = msgDetails.getMessageBytes();
		if (msgDetails.isNew()) {
			exchange.getIn().setBody(messageBytes);
//			log.debug("TrafficScan NewMessageContent:"+BytesUtil.toString(messageBytes));
			log.debug("TrafficScanHandler process new " + msgType + " message from MDT[" + mdtIpAddress + ":" + mdtPort + "]");
		} else {
			byte[] oldMessageBytes = Helper.appendLength(messageBytes);
			exchange.getIn().setBody(oldMessageBytes);
//			log.debug("TrafficScan OldMessageContent:"+ BytesUtil.toString(oldMessageBytes));
			log.debug("TrafficScanHandler process old " + msgType + " message from MDT[" + mdtIpAddress + ":" + mdtPort + "]");
		}
	}

}
