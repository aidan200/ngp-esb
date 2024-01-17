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
package com.cdg.ngp.esb.ms.handler;


import java.io.UnsupportedEncodingException;

import org.apache.camel.Exchange;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sg.com.cdgtaxi.comms.tlv.msg.IVDMessageType;
import sg.com.cdgtaxi.comms.tlv.util.BytesUtil;

import com.cdg.ngp.esb.ms.dto.IVDMessageDetails;
import com.cdg.ngp.esb.ms.util.Helper;

/**  Class name : AuthenticationServiceHandler.java
 * <p>
 *     This class handle the Authentication message from ivd port 9004 and tcp
 *     response from Authentication Server
 * </p>
 *
 * @since Dec 10, 2016
 * @author Ganesan Dharmendiran
 *
 */
public class AuthenticationServiceHandler {
	private static final Logger LOGGER  = LoggerFactory.getLogger(AuthenticationServiceHandler.class);
	
	private static final int MDT_IP_LENGTH=15;
	private int mdtPort;
	
	public int getMdtPort() {
		return mdtPort;
	}
	public void setMdtPort(int mdtPort) {
		this.mdtPort = mdtPort;
	}
	/**
	 * Message from UDP
	 * @param exchange
	 */
	public void prepareAuthentication(Exchange exchange) {
 
		IVDMessageDetails msgDetails = (IVDMessageDetails) exchange.getIn().getHeader("IVD_MESSAGE");
		byte[] messageBytes = msgDetails.getMessageBytes();
//		LOGGER.debug("Authentication message received from MDT");
//	    LOGGER.info("Authentication Message from IVD-->"+BytesUtil.toString(messageBytes));
		byte[] receivedMessageContent=Helper.lengthExcludedMsgBytes(messageBytes);
//     	LOGGER.debug("Authentication messge from MDT its IP is-->"+msgDetails.getPeerAddr().getAddress().getHostAddress());
//     	LOGGER.debug("Length of the whole message from IVD or MDT-->"+messageBytes.length);
//		LOGGER.debug("Convert incoming ivd ip to ipbytes-->"+BytesUtil.toString(msgDetails.getPeerAddr().getAddress().getHostAddress().getBytes()));
		String mdtIpAddress=msgDetails.getPeerAddr().getAddress().getHostAddress();
		byte[] mdtIPBytes=Helper.convertPhysicalIpToBytes(mdtIpAddress, MDT_IP_LENGTH);			
		LOGGER.info("Authentication request message with ivd IP -->"+mdtIpAddress);
		byte[] msgByteToAuth=ArrayUtils.addAll(receivedMessageContent, mdtIPBytes);
//		LOGGER.debug("Message after append IP and send to Authentication Server--->"+BytesUtil.toString(msgByteToAuth));
		exchange.getIn().setBody(msgByteToAuth);
	}
	/**
	 * Message from Tcp (Response Message)
	 * @param exchange
	 */
	public void prepareAuthenticationResponseFromTcp(Exchange exchange){		  

		byte[] msgBytes = (byte[]) exchange.getIn().getBody();
		int size = msgBytes.length;
		if (size > 2) {
			int length = Helper.findLengthFromFirstTwoBytes(msgBytes);
			// Helper.infoLogFromBackend(LOGGER, true, msgBytes);
			// LOGGER.info("Authentication Response Received from Authentication Server");
			LOGGER.info("Authentication Response Received from AuthServer as TCP packet -----> : "+BytesUtil.toString(msgBytes));
			// LOGGER.info("Authentication Response Length ---->" + length);
			byte[] msgBytesWithoutLength = ArrayUtils.subarray(msgBytes, 2, msgBytes.length);
			// LOGGER.debug("Authentication Tcp Response message bytes --->"+BytesUtil.toString(msgBytesWithoutLength));
			byte[] mdtIpBytes = Helper.getIPBytes(msgBytesWithoutLength);
			// byte[] responseBytes=ArrayUtils.subarray(msgBytesWithoutLength,
			// 0, msgBytesWithoutLength.length-15);
			byte[] responseBytes = Helper.removeIvdIpBytes(msgBytesWithoutLength);
			// troubleshoot yx
			if (IVDMessageType.getType(responseBytes[0] & 0xFF) == IVDMessageType.POWER_UP_RESPONSE) {
				responseBytes = ArrayUtils.subarray(responseBytes, 0, responseBytes.length - 8);
			}
			// LOGGER.info("Authentication response Last 15 bytes IP removed successfully before send back to IVD");
			// LOGGER.info("MDT BYTES for Authentication response --->"+BytesUtil.toString(mdtIpBytes));
			String mdtIp = null;
			try {
				mdtIp = Helper.getPhysicalIpByBytes(mdtIpBytes);
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("Unable to convert the received mdt IP bytes to physical IP"+ExceptionUtils.getRootCauseMessage(e));
			}
			byte[] responseWithNewLengthBytes = Helper.appendLength(responseBytes);
			LOGGER.info("Authentication Response to MDT or IVD IP-->" + mdtIp);
			// LOGGER.info("Authentication Response sent to MDT or IVD");
			// LOGGER.info("Authentication Response bytes back to MDT or IVD -->"+BytesUtil.toString(responseWithNewLengthBytes));
			exchange.getIn().setHeader("UDP_DEST_NAME", mdtIp);
			exchange.getIn().setHeader("UDP_DEST_PORT", mdtPort);
			exchange.getIn().setBody(responseWithNewLengthBytes);
		} else {
			LOGGER.info("Authentication Response Byts doesn't meet the required format, Atleast two bytes to show the length");
		}
			
	}
	
}
