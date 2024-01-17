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
 * @Class name : LocationServiceHandler.java
 * @Description :for processing the LocationService and VheicleUpdateInfo data
 * @Author Zhao Zilong
 * @Since 12 Jan 2016
**/
package com.cdg.ngp.esb.ms.handler;
import static sg.com.cdgtaxi.comms.tlv.msg.IVDFieldTag.DRIVER_ID;
import static sg.com.cdgtaxi.comms.tlv.msg.IVDFieldTag.VEHICLE_PLATE_NUMBER;
import static sg.com.cdgtaxi.comms.tlv.msg.IVDMessageType.LOGON_REQUEST;
import static sg.com.cdgtaxi.comms.tlv.msg.IVDMessageType.STORE_FORWARD_MESSAGE;
import static sg.com.cdgtaxi.comms.tlv.msg.IVDMessageType.LOGOUT_REQUEST;

import org.apache.camel.Exchange;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cdg.ngp.esb.ms.dto.IVDMessageDetails;
import com.cdg.ngp.esb.common.data.Message;
import com.cdg.ngp.esb.common.data.MessageContent;
import com.cdg.ngp.esb.common.data.MessageHeader;

import sg.com.cdgtaxi.comms.tlv.msg.IVDMessage;
import sg.com.cdgtaxi.comms.tlv.msg.IVDMessageHeader;
import sg.com.cdgtaxi.comms.tlv.msg.IVDMessageType;


public class LocationServiceHandler {
	private static final Logger log = LoggerFactory.getLogger(LocationServiceHandler.class);
	/**
	 * cutting the first 2 bytes of new mdt message. the first 2 bytes is msg
	 * length
	 */
	private final static int NEW_MESSAGE_START_INDEX = 2;
	
	private String offlineIvdStatus;
	

	public String getOfflineIvdStatus() {
		return offlineIvdStatus;
	}

	public void setOfflineIvdStatus(String offlineIvdStatus) {
		this.offlineIvdStatus = offlineIvdStatus;
	}

	/**
	 * sending LocationReport to LocationService
	 * 
	 * @param exchange
	 */
	public void prepareForLocationService(Exchange exchange) {
		IVDMessageDetails detail = (IVDMessageDetails) exchange.getIn().getHeader("IVD_MESSAGE");
		if (!detail.isNew()&&detail.getHeader().getType().compareTo(STORE_FORWARD_MESSAGE) == 0) {
			return;
		}
		setExchangeBody(exchange, null, null);
	}
   
	/**
	 * sending VehicleInfoUpdate to LocationService
	 * 
	 * @param exchange
	 */
	public void prepareLoginInfo(Exchange exchange) {
		IVDMessageDetails detail = (IVDMessageDetails) exchange.getIn().getHeader("IVD_MESSAGE");
		byte[] msgBytes = detail.getMessageBytes();
		String driverId;
		String vehicleId;
		// New Message
		if (detail.isNew()) {
			IVDMessage msg = new IVDMessage(ArrayUtils.subarray(msgBytes, NEW_MESSAGE_START_INDEX, msgBytes.length));
			if (detail.getHeader().getType().compareTo(LOGON_REQUEST) == 0) {
				driverId = msg.getText(DRIVER_ID);
				setExchangeBody(exchange, driverId, null);
			} else {
				vehicleId = msg.getText(VEHICLE_PLATE_NUMBER);
				setExchangeBody(exchange, null, vehicleId);
			}
			// Old Message
		} else {
			// driverId is not null means Log On Request
			if (detail.getHeader().getType().compareTo(LOGON_REQUEST) == 0) {
				byte[] logOnBytes = ArrayUtils.subarray(msgBytes, detail.getContentStartIndex(),
						detail.getContentStartIndex() + 7);
				driverId = new String(logOnBytes);
				setExchangeBody(exchange, driverId, null);
				// driverId is null means Power UP
			} else {
				byte[] powerOnBytes = ArrayUtils.subarray(msgBytes, detail.getContentStartIndex() + 43,
						detail.getContentStartIndex() + 43 + 8);
				vehicleId = new String(powerOnBytes);
				setExchangeBody(exchange, null, vehicleId);
			}
		}
	}

	/**
	 * settting LocationMessage.obj
	 * 
	 * @param IVDHeader
	 * @param isNew
	 * @return
	 */
	private Message setLocationMsg(IVDMessageDetails detail, String driverId, String vehicleId) {
		MessageHeader msgHeader = new MessageHeader();
		MessageContent msgContent = new MessageContent();
		byte[] msgBytes = detail.getMessageBytes();
		IVDMessageHeader header = detail.getHeader();
		IVDMessageType msgType = header.getType();
		
		Message msg = null;
		try {
			msgHeader.setMobileId(header.getMobileID());
			
			/*temporary fix, flip the x,y*/
			
		//	msgHeader.setxOffset(header.getYOffset());
		//	msgHeader.setyOffset(header.getXOffset());
			
			/////////////////////////////////////////////////////

			msgHeader.setxOffset(header.getYOffset());
			msgHeader.setyOffset(header.getXOffset());
			
			msgContent.setDriverID(driverId);
			msgContent.setVehicleID(vehicleId);
			msgHeader.setDirection(header.getDirection());
			msgHeader.setEmergencyStatus(header.getEmergencyStatus());
			msgHeader.setGpsFixStatus(header.getGpsFixStatus());
			
			if (msgType.compareTo(LOGOUT_REQUEST) == 0){
				msgHeader.setIvdStatus(Integer.parseInt(offlineIvdStatus));//byte=1011: offline (not yet logon)
			}else{
				msgHeader.setIvdStatus(header.getIVDStatus());
			}
			
			msgHeader.setSuspensionStatus(header.getSuspension());
			msgHeader.setSerialNum(header.getSerialNumber());
			msgHeader.setSpeed(header.getSpeed());
			msgHeader.setTaxiMeterStatus(header.getTaxiMeterStatus());
			
			msgHeader.setZoneOrRank(header.getZoneOrRank());
			msgHeader.setAcknowledgement(header.getAcknowledgement());
			msgHeader.setExpressway(header.getExpressway());
			msgHeader.setMessageType(header.getType().getId());
			if (detail.isNew()) {
				msgContent.setHeaderInfo(ArrayUtils.subarray(msgBytes, 2, 19));
				msgHeader.setTimestamp(header.getTimestamp());
				msgHeader.setSequence(header.getSequence());
			} else {
				msgContent.setHeaderInfo(ArrayUtils.subarray(msgBytes, 0, 12));
				msgHeader.setTimestamp(System.currentTimeMillis());
				msgHeader.setSequence(null);//CITYNET-938 requirement
			}
			msg = new Message(msgHeader, msgContent);
//			log.debug("LocationMessage msgType: "+msgType+ ".msg header and content:" + msg);
			log.debug("MDT to locationMsgQ. MDT IP: " + detail.getPeerAddr().getAddress().getHostAddress());//log mdt ip address
		} catch (Exception e) {
			log.error("LocationMessage set error: " + msg + e);
		}
		return msg;
	}

	/**
	 * set Exchange body
	 */
	private void setExchangeBody(Exchange exchange, String driverId, String vehicleId) {
		exchange.getIn()
				// set Body
				.setBody(
						// set up obj LocationMsg
						setLocationMsg((IVDMessageDetails) exchange.getIn().getHeader("IVD_MESSAGE"),driverId,vehicleId));
	}

}
