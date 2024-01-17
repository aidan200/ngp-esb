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
 * @Class name : StoreAndForwardHandler.java
 * @Description :TODO
 * @Author chaizhichao
 * @Since 13 Jan, 2016
**/
package com.cdg.ngp.esb.ms.handler;

import java.util.List;
import java.util.Random;

import org.apache.camel.Exchange;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sg.com.cdgtaxi.comms.tlv.util.BytesUtil;

import com.cdg.ngp.esb.ms.dto.IVDMessageDetails;


public class StoreAndForwardHandler {
	private static final Logger log = LoggerFactory.getLogger(StoreAndForwardHandler.class);
	private Integer mdtOldPort;
	private Integer mdtNewPort;
	private List<Integer> storeAndFowardList;
	/**
     * @method Name : getMdtOldPort
     * @return mdtOldPort
     */
	public Integer getMdtOldPort() {
		return mdtOldPort;
	}
	 /**
     * @method Name : setMdtOldPort
     * @param mdtOldPort
     * @return void
     */
	public void setMdtOldPort(Integer mdtOldPort) {
		this.mdtOldPort = mdtOldPort;
	}
	/**
     * @method Name : getMdtNewPort
     * @return mdtNewPort
     */
	public Integer getMdtNewPort() {
		return mdtNewPort;
	}
	/**
     * @method Name : setMdtNewPort
     * @param mdtNewPort
     * @return void
     */
	public void setMdtNewPort(Integer mdtNewPort) {
		this.mdtNewPort = mdtNewPort;
	}
	/**
     * @method Name : getStoreAndFowardList
     * @return storeAndFowardList
     */
	public List<Integer> getStoreAndFowardList() {
		return storeAndFowardList;
	}
	 /**
     * @method Name : setStoreAndFowardList
     * @param storeAndFowardList
     * @return void
     */
	public void setStoreAndFowardList(List<Integer> storeAndFowardList) {
		this.storeAndFowardList = storeAndFowardList;
	}
	/**
     * @method Name : prepareStoreAndForward
     * @param exchange
     * @return void
     */
	public void prepareStoreAndForward(Exchange exchange){
		IVDMessageDetails msgDetails = (IVDMessageDetails) exchange.getIn().getHeader("IVD_MESSAGE");
		//1.MDT-IP 2.MDT-PORT 3.message type
		String mdtIpAddress = msgDetails.getPeerAddr().getAddress().getHostAddress();
		int mdtPort = msgDetails.getPeerAddr().getPort();
		String msgType = (String)exchange.getIn().getHeader("IVD_MESSAGE_TYPE");
//		log.debug("PrepareStoreAndForward processing "+ (msgDetails.isNew()?"New":"Old")+" Message ");
		byte[] msgBytes = msgDetails.getMessageBytes();
//		log.debug("PrepareStoreAndForward msgBytes from exchange:" + BytesUtil.toString(msgDetails.getMessageBytes()));
		int messageId = msgDetails.getHeader().getType().getId();
		log.info("{} Message id received {} {}", messageId, msgDetails.isNew(), ((msgBytes[1] & 128) >> 7));
		//Got the acknowledgement flag from the second byte
//		log.debug("acknowledgement flag:" + ((msgBytes[1] & 128) >> 7));
		if((((msgBytes[1] & 128) >> 7) > 0 && !msgDetails.isNew()) || (storeAndFowardList.contains(messageId)&&!msgDetails.isNew())){
			byte[] msgRespBytes = new byte[6];
			Random r = new Random();
			int randomNumber = r.nextInt(127) + 1;// value:1~127
			msgRespBytes[0] = (byte)125;
			msgRespBytes[1] = (byte)randomNumber;
			msgRespBytes[2] = msgBytes[2];
			msgRespBytes[3] = msgBytes[3];
			msgRespBytes[4] = msgBytes[0];
			msgRespBytes[5] = msgBytes[1];
			//Got the IP 
			String mdtIP = msgDetails.getPeerAddr().getAddress().getHostAddress();
			//set up exchange
			exchange.getIn().setHeader("UDP_DEST_NAME", mdtIP);
			//if(msgDetails.isNew()){
			//	exchange.getIn().setHeader("UDP_DEST_PORT", mdtNewPort);
			//}else{
				exchange.getIn().setHeader("UDP_DEST_PORT", mdtOldPort);
			//}
			exchange.getIn().setBody(msgRespBytes);
			exchange.getIn().setHeader("ACK_FlAG", "TRUE");
			log.info("StoreAndForwardHandler process " + msgType +" message from MDT[" + mdtIpAddress +":" + mdtPort +"]");
//			log.debug("StoreAndForward ReplyBytes:" + BytesUtil.toString(msgRespBytes));
		}else{
			log.info("StoreAndForwardHandler No need send acknowledgement to MDT");
		}
		
	}

}
