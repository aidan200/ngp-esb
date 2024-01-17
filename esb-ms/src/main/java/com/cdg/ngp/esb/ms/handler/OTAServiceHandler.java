///*
// * Copyright (c) 2015, COMFORT TRANSPORTATION PTE. LTD./COMFORTDELGRO PTE. LTD.
// * All right reserved.
// *
// * This software is confidential and a proprietary property of
// * COMFORT TRANSPORTATION PTE. LTD./COMFORTDELGRO PTE. LTD.
// *
// * The contents of this software shall not be modified or disclosed and shall
// * only be used in accordance with the terms and conditions stated in
// * the contract or license agreement with COMFORT TRANSPORTATION PTE. LTD./COMFORTDELGRO PTE. LTD.
// *
// * Redistribution and use in source or binary forms, with or without
// * modification, in fraction or whole are permitted provided that the following
// * conditions are met:
// *
// *   - Upon written approval from COMFORT TRANSPORTATION PTE. LTD./COMFORTDELGRO PTE. LTD.
// *
// *   - Redistributions of source code must retain the above copyright
// *     notice, this list of conditions and the following disclaimer.
// *
// *   - Redistributions in binary form must reproduce the above copyright
// *     notice, this list of conditions and the following disclaimer in the
// *     documentation and/or other materials provided with the distribution.
// *
// *   - Neither the name of COMFORT TRANSPORTATION PTE. LTD./COMFORTDELGRO PTE. LTD.
// *     nor the names of its contributors may be used to endorse or promote products
// *     derived from this software without specific prior written permission.
// */
///**
// * @Class name : OTAServiceHandler.java
// * @Description :for processing the message between OTA server and MDT
// * @Author Zhao Zilong
// * @Since 12 Jan 2016
// **/
//package com.cdg.ngp.esb.ms.handler;
//
//import sg.com.cet.escalade.uc.comms.domain.GenericVO;
//import sg.com.cdgtaxi.comms.tlv.msg.IVDFieldTag;
//import sg.com.cdgtaxi.comms.tlv.msg.IVDMessage;
//import sg.com.cdgtaxi.comms.tlv.msg.IVDMessageType;
//import sg.com.cdgtaxi.comms.tlv.util.BytesUtil;
//
//import java.io.UnsupportedEncodingException;
//
//
//
//import org.apache.camel.Exchange;
//import org.apache.commons.lang3.ArrayUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.cdg.ngp.esb.ms.dto.IVDMessageDetails;
//import com.cdg.ngp.esb.ms.util.Helper;
//
////import static sg.com.cdgtaxi.comms.tlv.msg.IVDMessageType.OTA_SOFTWARE_UPDATE;
//
//public class OTAServiceHandler {
//	private static final Logger log = LoggerFactory
//			.getLogger(OTAServiceHandler.class);
//	private static final int MDT_IP_LENGTH=15;
//	private String ipAddress;
//	private Integer otaServicePort;
//	private Integer mdtNewPort;
//	private Integer mdtOldPort;
//	private String otaServiceDest;
//	private String oldServiceDest;
//	private String newServiceDest;
//
//
//
//	public String getIpAddress() {
//		return ipAddress;
//	}
//
//	public void setIpAddress(String ipAddress) {
//		this.ipAddress = ipAddress;
//	}
//
//	public Integer getOtaServicePort() {
//		return otaServicePort;
//	}
//
//	public void setOtaServicePort(Integer otaServicePort) {
//		this.otaServicePort = otaServicePort;
//	}
//
//	public Integer getMdtNewPort() {
//		return mdtNewPort;
//	}
//
//	public void setMdtNewPort(Integer mdtNewPort) {
//		this.mdtNewPort = mdtNewPort;
//	}
//
//	public Integer getMdtOldPort() {
//		return mdtOldPort;
//	}
//
//	public void setMdtOldPort(Integer mdtOldPort) {
//		this.mdtOldPort = mdtOldPort;
//	}
//
//	public String getOtaServiceDest() {
//		return otaServiceDest;
//	}
//
//	public void setOtaServiceDest(String otaServiceDest) {
//		this.otaServiceDest = otaServiceDest;
//	}
//
//	public String getOldServiceDest() {
//		return oldServiceDest;
//	}
//
//	public void setOldServiceDest(String oldServiceDest) {
//		this.oldServiceDest = oldServiceDest;
//	}
//
//	public String getNewServiceDest() {
//		return newServiceDest;
//	}
//
//	public void setNewServiceDest(String newServiceDest) {
//		this.newServiceDest = newServiceDest;
//	}
//
//	/**
//	 * Story 112 receive old msg from OTAServer via COMQueue and send the msg
//	 * chopped off IP to MDT through different port
//	 *
//	 * @param exchange
//	 * @throws Exception
//	 */
//	public void listenerForCOMQueue(Exchange exchange) throws Exception {
//		// convert obj into GenericVO
//		GenericVO vo = (GenericVO) exchange.getIn().getBody();
//		// get the msg bytes
//		byte[] msgBytes = vo.getByteArray();
////		IVDMessageType msgType = IVDMessageType.getType(msgBytes[0] & 0xFF);
//		int msgId = msgBytes[0] & 0xFF;
//
//		try {
//			String ipAddr = Helper.getPhysicalIpByBytes(Helper.getIPBytes(msgBytes));
//
//			log.debug("OTAServer old to MDT. MDT IP:" + ipAddr + ". The bytes before chop IP:"
//					+ BytesUtil.toString(msgBytes));
//
//			//zhichao change here ,because there is no OTA_SOFTWARE_UPDATE in the external comTlv jar,so use msgId to check
////			if (msgType == OTA_SOFTWARE_UPDATE) {
//			if(msgId == 42){
//				// process the ota_software_update msg
//				setListenerExchange(exchange, ipAddr, otaServicePort, chopOffIPBytes(msgBytes));
//			} else {
//				// process other OTA server old msg
//				setListenerExchange(exchange, ipAddr, mdtOldPort, chopOffIPBytes(msgBytes));
//			}
//		} catch (UnsupportedEncodingException e) {
//			log.info("Caught an exception :" + e);
//			log.error("OTAService encoding error: " + e);
//			throw e;
//		}
//
//	}
//
//	/**
//	 * Story 112 receive new msg from OTAServer via TlvCOMQueue and send the msg
//	 * chopped off IP and add the msg_length to MDT through different port
//	 *
//	 * @param exchange
//	 * @throws Exception
//	 */
//	public void listenerForTlvCOMQueue(Exchange exchange) throws Exception {
//		// convert obj into GenericVO
//		GenericVO vo = (GenericVO) exchange.getIn().getBody();
//		// get the msg bytes
//		byte[] msgBytes = vo.getByteArray();
//		try {
//			String ipAddr = Helper.getPhysicalIpByBytes(Helper.getIPBytes(msgBytes));
//			log.debug("OTAServer to new MDT. MDT IP:" + ipAddr + ". The bytes before chop IP:"
//					+ BytesUtil.toString(msgBytes));
//			// process OTA server new msg
//			setListenerExchange(exchange, ipAddr, mdtNewPort,
//					// add the msg_length to the msg bytes
//					Helper.appendLength(chopOffIPBytes(msgBytes)));
//		} catch (UnsupportedEncodingException e) {
//			log.info("Caught an exception :" + e);
//			log.error("OTAService encoding error: " + e);
//			throw e;
//		}
//
//	}
//
//	/**
//	 * Story 113 receive OTA Service msg from MDT and send to OTAServer by
//	 * weblogicQueue
//	 *
//	 * @param exchange
//	 */
//	public void OTAService(Exchange exchange) {
////		log.debug("Processing OTAService");
//
//		IVDMessageDetails detail = (IVDMessageDetails) exchange.getIn().getHeader("IVD_MESSAGE");
//		byte[] msgBytes = detail.getMessageBytes();
//
//		/*2016-02-19 new changes:add IVD ip address at the end of msgByes*/
//		String mdtIpAddress=detail.getPeerAddr().getAddress().getHostAddress();
//		byte[] mdtIPBytes=Helper.convertPhysicalIpToBytes(mdtIpAddress, MDT_IP_LENGTH);
//		msgBytes = ArrayUtils.addAll(msgBytes, mdtIPBytes);
//		/*2016-02-19 new changes:add IVD ip address at the end of msgByes*/
//
//		GenericVO vo = new GenericVO();
//		vo.setByteArray(msgBytes);
//		vo.setByteArraySize(msgBytes.length);
//		vo.setMobileId(String.valueOf(((char) (msgBytes[3] & 0xFF) << 8) + (char) (msgBytes[2] & 0xFF)));
//		log.debug("MDT to OTAMsgTopic. MDT IP: " + detail.getPeerAddr().getAddress().getHostAddress());//log mdt ip address
//		setSenderExchange(exchange, ipAddress, otaServiceDest, vo);
//	}
//
//	/**
//	 * Story 113 receive Voice Streaming Service msg from MDT and send to
//	 * OTAServer by weblogicQueue
//	 *
//	 * @param exchange
//	 */
//	public void voiceStreamingService(Exchange exchange) {
//
//		IVDMessageDetails detail = (IVDMessageDetails) exchange.getIn().getHeader("IVD_MESSAGE");
//		byte[] msgBytes = detail.getMessageBytes();
//
//		/*2016-02-19 new changes:add IVD ip address at the end of msgByes*/
//		String mdtIpAddress=detail.getPeerAddr().getAddress().getHostAddress();
//		byte[] mdtIPBytes=Helper.convertPhysicalIpToBytes(mdtIpAddress, MDT_IP_LENGTH);
//		msgBytes = ArrayUtils.addAll(msgBytes, mdtIPBytes);
//		/*2016-02-19 new changes:add IVD ip address at the end of msgByes*/
//		log.debug("MDT to IVDQueue. MDT IP: " + detail.getPeerAddr().getAddress().getHostAddress());//log mdt ip address
//		setSenderExchange(exchange, ipAddress, oldServiceDest, msgBytes);
//
//	}
//
//	/**
//	 * Story 113 receive Dispatch Service msg from MDT and send to OTAServer by
//	 * weblogicQueue
//	 *
//	 * @param exchange
//	 */
//	public void dispatchService(Exchange exchange) {
//		IVDMessageDetails detail = (IVDMessageDetails) exchange.getIn()
//				.getHeader("IVD_MESSAGE");
//		byte[] msgBytes = detail.getMessageBytes();
//
//		/*2016-02-19 new changes:add IVD ip address at the end of msgByes*/
//		String mdtIpAddress=detail.getPeerAddr().getAddress().getHostAddress();
//		byte[] mdtIPBytes=Helper.convertPhysicalIpToBytes(mdtIpAddress, MDT_IP_LENGTH);
//		msgBytes = ArrayUtils.addAll(msgBytes, mdtIPBytes);
//		/*2016-02-19 new changes:add IVD ip address at the end of msgByes*/
//
//		if (detail.isNew()) {
//			log.debug("MDT to TlvIVDQueue. MDT IP: " + detail.getPeerAddr().getAddress().getHostAddress());//log mdt ip address
//			setSenderExchange(exchange, ipAddress, newServiceDest,
//					ArrayUtils.subarray(msgBytes, 2, msgBytes.length));
//			// Old Message
//		} else {
//			log.debug("MDT to IVDQueue. MDT IP: " + detail.getPeerAddr().getAddress().getHostAddress());//log mdt ip address
//			setSenderExchange(exchange, ipAddress, oldServiceDest,
//					msgBytes);
//		}
//	}
//
//	/**
//	 * CITYNET-876
//	 *
//	 * @param exchange
//	 */
//	public void msDispatchService(Exchange exchange) {
//		IVDMessageDetails detail = (IVDMessageDetails) exchange.getIn()
//				.getHeader("IVD_MESSAGE");
//		byte[] msgBytes = detail.getMessageBytes();
//
//		/* 2016-02-19 new changes:add IVD ip address at the end of msgByes */
//		String mdtIpAddress = detail.getPeerAddr().getAddress()
//				.getHostAddress();
//		byte[] mdtIPBytes = Helper.convertPhysicalIpToBytes(mdtIpAddress,
//				MDT_IP_LENGTH);
//		msgBytes = ArrayUtils.addAll(msgBytes, mdtIPBytes);
//		if (detail.isNew()) {
//			exchange.getIn().setHeader("MESSAGE_FLAG", "NEW");
//			msgBytes = ArrayUtils.subarray(msgBytes, 2, msgBytes.length);
//		}else{
//			exchange.getIn().setHeader("MESSAGE_FLAG", "OLD");
//		}
//		exchange.getIn().setBody(msgBytes);
//	}
//
//	/**
//	 * Story 113 receive StoreAndForward Service msg from MDT and send to
//	 * backend by weblogicQueue
//	 *
//	 * @param exchange
//	 */
//	public void storeForwardService(Exchange exchange) {
//		IVDMessageDetails detail = (IVDMessageDetails) exchange.getIn()
//				.getHeader("IVD_MESSAGE");
//		byte[] msgBytes = detail.getMessageBytes();
//
//		/*2016-02-19 new changes:add IVD ip address at the end of msgByes*/
//		String mdtIpAddress=detail.getPeerAddr().getAddress().getHostAddress();
//		byte[] mdtIPBytes=Helper.convertPhysicalIpToBytes(mdtIpAddress, MDT_IP_LENGTH);
//		msgBytes = ArrayUtils.addAll(msgBytes, mdtIPBytes);
//		/*2016-02-19 new changes:add IVD ip address at the end of msgByes*/
//
//		// New Message,remove the first 2 bytes
//		if (detail.isNew()) {
//			log.debug("MDT to TlvIVDQueue. MDT IP: " + detail.getPeerAddr().getAddress().getHostAddress());//log mdt ip address
//			setSenderExchange(exchange, ipAddress, newServiceDest,
//					ArrayUtils.subarray(msgBytes, 2, msgBytes.length));
//			// Old Message,just remove the first bytes
//		} else {
//			log.debug("MDT to IVDQueue. MDT IP: " + detail.getPeerAddr().getAddress().getHostAddress());//log mdt ip address
//			setSenderExchange(exchange, ipAddress, oldServiceDest, msgBytes);
//		}
//	}
//
//	/**
//	 * Ticket-1122
//	 * @Description processStoreForwardService
//	 * @param exchange
//	 * @return void
//	 * @author zhichao
//	 */
//	public void processStoreForwardService(Exchange exchange) {
//
//		IVDMessageDetails detail = (IVDMessageDetails) exchange.getIn()
//				.getHeader("IVD_MESSAGE");
//		if(detail.isNew()){
//			byte[] bytes = detail.getMessageBytes();
//			//remove the length
//			byte[] msgBytes= ArrayUtils.subarray(bytes, 2 , bytes.length);
//			log.info("processStoreForwardService msgBytes:" + BytesUtil.toString(msgBytes));
//			//get the original message bytes
//			IVDMessage ivdMessage = new IVDMessage(msgBytes);
//			byte[] originalBytes = ivdMessage.getBytes(IVDFieldTag.STORE_FORWARD_CONTENT);
//
//			IVDMessageDetails msgDetails = new IVDMessageDetails(originalBytes, detail.isNew(), detail.getPeerAddr());
//			String ivdMsgType = msgDetails.getHeader().getType().toString();
//			exchange.getIn().setHeader("IVD_MESSAGE_TYPE", ivdMsgType);
//			exchange.getIn().setHeader("IVD_MESSAGE", msgDetails);
//			exchange.getIn().setHeader("IVD_MESSAGE_STRING", msgDetails.toString());
//			log.info("processStoreForwardService msgDetails:" + msgDetails.toString() + "originalBytes:" + BytesUtil.toString(originalBytes));
//			exchange.getIn().setBody(null);
//		}else{
//			exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
//		}
//
//	}
//
//	/**
//	 * chop off the last 15 bytes of IP address and remain the msg content
//	 *
//	 * @param msgBytes
//	 * @return
//	 */
//	private byte[] chopOffIPBytes(byte[] msgBytes) {
//		return ArrayUtils.subarray(msgBytes, 0, msgBytes.length - 15);
//	}
//
//	/**
//	 * Story 112 OTA to MDT
//	 *
//	 * @param exchange
//	 * @param ip
//	 * @param port
//	 * @param msgContent
//	 */
//	private void setListenerExchange(Exchange exchange, String ip,
//			Integer port, byte[] msgContent) {
//		// set up the udp destination ip address
//		exchange.getIn().setHeader("UDP_DEST_NAME", ip);
//		// set up the udp destination ip port
//		exchange.getIn().setHeader("UDP_DEST_PORT", port);
//		exchange.getIn().setBody(msgContent);
//	}
//
//	/**
//	 * Story 113 MDT to OTA, JMS sender, sending byte[]
//	 *
//	 * @param exchange
//	 * @param IPAddress
//	 * @param queueName
//	 * @param body
//	 */
//	private void setSenderExchange(Exchange exchange, String ip,
//			String queueName, byte[] body) {
//		// set up the queue ip address
//		exchange.getIn().setHeader("WMQS_DEST_URI", "t3://" + ip);
//		// set up the queue name
//		exchange.getIn().setHeader("WMQS_DEST_QUEUE", queueName);
//		exchange.getIn().setBody(body);
//	}
//	/**
//	 * Story 113 MDT to OTA, JMS sender, sending Object
//	 *
//	 * @param exchange
//	 * @param IPAddress
//	 * @param queueName
//	 * @param obj
//	 */
//	private void setSenderExchange(Exchange exchange, String ip,
//			String queueName, Object obj) {
//		// set up the queue ip address
//		exchange.getIn().setHeader("WMQS_DEST_URI", "t3://" + ip);
//		// set up the queue name
//		exchange.getIn().setHeader("WMQS_DEST_QUEUE", queueName);
//		exchange.getIn().setBody(obj);
//	}
//}