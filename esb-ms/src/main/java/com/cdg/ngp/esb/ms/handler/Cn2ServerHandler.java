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
 * @Class name : Cn2ServerHandler.java
 * @Description :DONE
 * @Author chaizhichao
 * @Since 12 Jan, 2016
**/
package com.cdg.ngp.esb.ms.handler;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;

import org.apache.camel.Exchange;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cdg.ngp.esb.ms.util.Helper;

import sg.com.cdgtaxi.comms.tlv.msg.IVDMessageType;
import sg.com.cdgtaxi.comms.tlv.util.BytesUtil;
import sg.com.cet.escalade.uc.comms.domain.GenericVO;

public class Cn2ServerHandler {
	private static final Logger log = LoggerFactory.getLogger(Cn2ServerHandler.class);
	private Integer mdtNewPort;

	public Integer getMdtNewPort() {
		return mdtNewPort;
	}

	public void setMdtNewPort(Integer mdtNewPort) {
		this.mdtNewPort = mdtNewPort;
	}


	/**
	 *
	 * @Description Listen to the TlvCOMQueue  and  send newMsg to MDT
	 * @param exchange
	 * @return void
	 */
	public void sendNewMsgToMdt(Exchange exchange){
		GenericVO vo = (GenericVO) exchange.getIn().getBody();
		byte[] msgBytes = vo.getByteArray();
//		log.info("msgID: " + msgBytes[0]);
//		IVDMessage message = new IVDMessage(msgBytes);
//		int seqNo = Helper.convertData(message, IVDFieldTag.SEQUENCE_NUMBER, Integer.class);
//		log.info("seqNo : " + seqNo);
//		int refNo = Helper.convertData(message, IVDFieldTag.REFERENCE_NUMBER, Integer.class);
//		log.info("refNo : " + refNo);
//		log.debug("the newMsgBytes from TlvCOMQueue: " + BytesUtil.toString(msgBytes));
		//for feedback to vehicle-commom
		HashMap<String,HashMap<String , Long>> dateMap = new HashMap<String , HashMap<String , Long>>();
		HashMap<String , Long> date = new HashMap<String , Long>();
		if(msgBytes[0] == IVDMessageType.IVD_PING.getId()) {
			date.put("t2", new Date().getTime());
		}
		//get the ip from msgbytes and convert to String
		byte[] ipBytes = Helper.getIPBytes(msgBytes);
		String mdtIP = null;
		try {
			 mdtIP = Helper.getPhysicalIpByBytes(ipBytes);
		} catch (UnsupportedEncodingException e) {
			log.info("Caught an exception :" + e);
		}
		log.debug("Listen to the TlvCOMQueue and send back to MDT[" + mdtIP  + ":" + mdtNewPort + "] msgbytes content:" + BytesUtil.toString(msgBytes));

		//recalculate the message length and prepend into the message
		byte[] msgBytesWithoutLength= ArrayUtils.subarray(msgBytes, 0, msgBytes.length);
		byte[] responseBytes = ArrayUtils.subarray(msgBytesWithoutLength, 0, msgBytesWithoutLength.length-15);
		byte[] responseWithNewLengthBytes = Helper.appendLength(responseBytes);

		//set up exchange
		exchange.getIn().setHeader("UDP_DEST_NAME", mdtIP);
		exchange.getIn().setHeader("UDP_DEST_PORT", mdtNewPort);


		// check PING msg (35) for CITYNET-890
		if(msgBytes[0] == IVDMessageType.IVD_PING.getId()) {
			// need create COM_RECV_APP

			exchange.getIn().setHeader("PING", String.valueOf(IVDMessageType.IVD_PING.getId()));
			exchange.getIn().setHeader("RESPONSE_BYTES",responseWithNewLengthBytes);
			date.put("t3", new Date().getTime());
			//pingDate.setDt3(new Date());
			//dateMap.put(String.valueOf(seqNo), date);
			exchange.getIn().setBody(dateMap);
		} else {
			exchange.getIn().setBody(responseWithNewLengthBytes);
		}


	}

}
