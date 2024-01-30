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
import static sg.com.cdgtaxi.comms.tlv.msg.IVDMessageType.REGULAR_REPORT;
import static sg.com.cdgtaxi.comms.tlv.msg.IVDMessageType.STORE_FORWARD_MESSAGE;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.ServiceStatus;
import org.apache.camel.component.netty.NettyConstants;
import org.apache.camel.component.seda.SedaEndpoint;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cdg.ngp.esb.ms.dto.IVDMessageDetails;
import com.cdg.ngp.esb.ms.util.BatchingQueue;

import sg.com.cdgtaxi.comms.tlv.msg.IVDMessageType;
import sg.com.cdgtaxi.comms.tlv.util.BytesUtil;
/** 
 * @Class name : IVDHandler.java
 * @Description : IVD message Type and regular reports that have ACK flag is set and Processed.
 * @Author Tend
 **/

public class IVDHandler {
	private class DoProcessResult {
		IVDMessageType msgType;
		boolean isAck;
		
		public DoProcessResult(IVDMessageType msgType,boolean isAck) {
			this.msgType = msgType;
			this.isAck = isAck;
		}

        /**)
         * @see Object#toString()
         */
        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this);
        }
	}
	
	private static final Logger log = LoggerFactory.getLogger(IVDHandler.class);
	
	private TrafficScanHandler trafficScanHandler;
	private LocationServiceHandler locationServiceHandler;
    private ProducerTemplate combinedProducer;
    private ProducerTemplate locationMsgQProducer;
    private ProducerTemplate odrdLocationMsgQProducer;
    private ProducerTemplate esbLocationMsgQProducer;
    private ProducerTemplate trafficScanQueueProducer;
    private ProducerTemplate fuseMonitorResponseProducer;
    
	private byte[] fuseHeartbeatBytes;
	private String esbLocationUpdateSwitchRouteName;
	private String eapLocationUpdateSwitchRouteName;
	private String odrdEnabled;
	private String odrdVehicleEnabled;
	
	public ProducerTemplate getEsbLocationMsgQProducer() {
		return esbLocationMsgQProducer;
	}

	public void setEsbLocationMsgQProducer(ProducerTemplate esbLocationMsgQProducer) {
		this.esbLocationMsgQProducer = esbLocationMsgQProducer;
	}

	public TrafficScanHandler getTrafficScanHandler() {
		return trafficScanHandler;
	}

	public void setTrafficScanHandler(TrafficScanHandler trafficScanHandler) {
		this.trafficScanHandler = trafficScanHandler;
	}

	public LocationServiceHandler getLocationServiceHandler() {
		return locationServiceHandler;
	}

	public void setLocationServiceHandler(LocationServiceHandler locationServiceHandler) {
		this.locationServiceHandler = locationServiceHandler;
	}

	public ProducerTemplate getCombinedProducer() {
		return combinedProducer;
	}

	public void setCombinedProducer(ProducerTemplate combinedProducer) {
		this.combinedProducer = combinedProducer;
	}

	public ProducerTemplate getLocationMsgQProducer() {
		return locationMsgQProducer;
	}

	public void setLocationMsgQProducer(ProducerTemplate locationMsgQProducer) {
		this.locationMsgQProducer = locationMsgQProducer;
	}
	
	public ProducerTemplate getOdrdlocationMsgQProducer() {
		return odrdLocationMsgQProducer;
	}

	public void setOdrdLocationMsgQProducer(ProducerTemplate odrdLocationMsgQProducer) {
		this.odrdLocationMsgQProducer = odrdLocationMsgQProducer;
	}

	public ProducerTemplate getTrafficScanQueueProducer() {
		return trafficScanQueueProducer;
	}

	public void setTrafficScanQueueProducer(ProducerTemplate trafficScanQueueProducer) {
		this.trafficScanQueueProducer = trafficScanQueueProducer;
	}

	public ProducerTemplate getFuseMonitorResponseProducer() {
		return fuseMonitorResponseProducer;
	}

	public void setFuseMonitorResponseProducer(ProducerTemplate fuseMonitorResponseProducer) {
		this.fuseMonitorResponseProducer = fuseMonitorResponseProducer;
	}

	public String getFuseHeartbeatString() {
		return Arrays.toString(fuseHeartbeatBytes);
	}

	public void setFuseHeartbeatString(String fuseHeartbeatString) {
		this.fuseHeartbeatBytes = fuseHeartbeatString.getBytes();
	}

	public String getEsbLocationUpdateSwitchRouteName() {
		return esbLocationUpdateSwitchRouteName;
	}

	public void setEsbLocationUpdateSwitchRouteName(String esbLocationUpdateSwitchRouteName) {
		this.esbLocationUpdateSwitchRouteName = esbLocationUpdateSwitchRouteName;
	}
	
	public String getEapLocationUpdateSwitchRouteName() {
		return eapLocationUpdateSwitchRouteName;
	}

	public void setEapLocationUpdateSwitchRouteName(
			String eapLocationUpdateSwitchRouteName) {
		this.eapLocationUpdateSwitchRouteName = eapLocationUpdateSwitchRouteName;
	}
	
	

	public String getOdrdEnabled() {
		return odrdEnabled;
	}

	public void setOdrdEnabled(String odrdEnabled) {
		this.odrdEnabled = odrdEnabled;
	}

	public String getOdrdVehicleEnabled() {
		return odrdVehicleEnabled;
	}

	public void setOdrdVehicleEnabled(String odrdVehicleEnabled) {
		this.odrdVehicleEnabled = odrdVehicleEnabled;
	}

	/**
	 * @method Name : doProcess
	 * @param exchange
	 * @param isNew
	 * @return DoProcessResult
	 */
	public DoProcessResult doProcess(Exchange exchange, boolean isNew) {
		// TODO unknown risk
		ByteBuf byteBuf = exchange.getIn().getBody(ByteBuf.class);
		if (byteBuf == null || !byteBuf.isReadable()) {
			throw new RuntimeException("udp byteBuf is unreadable !");
		}
		byte[] msgBytes = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes(msgBytes);

		if (Arrays.equals(fuseHeartbeatBytes, ArrayUtils.subarray(msgBytes, 0, fuseHeartbeatBytes.length))) {
			exchange.getIn().setHeader("HEARTBEAT_NO", msgBytes[msgBytes.length-1]-'0');
			//exchange.getIn().setBody(msgBytes);
			fuseMonitorResponseProducer.send(exchange);
			return null;
		} else {
			InetSocketAddress addr = (InetSocketAddress) exchange.getIn().getHeader(NettyConstants.NETTY_REMOTE_ADDRESS);

			if (!isNew && (msgBytes[0] & 0xFF) == STORE_FORWARD_MESSAGE.getId()) {
				msgBytes = ArrayUtils.remove(msgBytes, 0);
			}

			IVDMessageDetails msgDetails = new IVDMessageDetails(msgBytes, isNew, addr);
			String ivdMsgType = msgDetails.getHeader().getType().toString();
			log.info("====begin===ivdMsgType[{}]====", ivdMsgType);
			exchange.getIn().setHeader("IVD_MESSAGE_TYPE", ivdMsgType);
			exchange.getIn().setHeader("IVD_MESSAGE", msgDetails);
			exchange.getIn().setHeader("IVD_MESSAGE_STRING", msgDetails.toString());

			if (log.isDebugEnabled()) {
				CamelContext ctx = exchange.getContext();
				SedaEndpoint sedaUNB = (SedaEndpoint)ctx.getEndpoint("seda:udpNewBuffer");
				SedaEndpoint sedaUOB = (SedaEndpoint)ctx.getEndpoint("seda:udpOldBuffer");
				SedaEndpoint sedaC = (SedaEndpoint)ctx.getEndpoint("seda:combined");
				SedaEndpoint sedaAS = (SedaEndpoint)ctx.getEndpoint("seda:authenticationService");
				SedaEndpoint sedaLS = (SedaEndpoint)ctx.getEndpoint("seda:locationService");
				SedaEndpoint sedaCS = (SedaEndpoint)ctx.getEndpoint("seda:cn2Server");
				SedaEndpoint sedaOS = (SedaEndpoint)ctx.getEndpoint("seda:OTAService");
				SedaEndpoint sedaSF = (SedaEndpoint)ctx.getEndpoint("seda:storeAndForward");
				log.debug(sedaUNB.getExchanges().size() + "," + sedaUOB.getExchanges().size() +
						"," + sedaC.getExchanges().size() + "," + sedaAS.getExchanges().size() +
						"," + sedaLS.getExchanges().size() + "," + sedaCS.getExchanges().size() +
						"," + sedaOS.getExchanges().size() + "," + sedaSF.getExchanges().size() +
						" - "+ exchange.getIn().getHeader("IVD_MESSAGE_STRING") +
						". Message (header+body) bytes:" + BytesUtil.toString(msgBytes));
				//Zilong changes here. Woon's requirement
			}
			else {
				int contentStartIndex = msgDetails.getContentStartIndex();
				byte[] headerBytes = ArrayUtils.subarray(msgBytes, 0, contentStartIndex);
				log.info(exchange.getIn().getHeader("IVD_MESSAGE_STRING") +
						". Message header bytes:" + BytesUtil.toString(headerBytes) +
						". Content length:" + (msgBytes.length-contentStartIndex));
				//Zilong changes here. Woon's requirement
			}

			exchange.getIn().setBody(null);

			return new DoProcessResult(
				msgDetails.getHeader().getType(),
				!isNew && (((msgBytes[1] & 128) >> 7) > 0)
			);
		}
	}
	
	/**
	 * @method Name : processRegularReport
	 * @param exchange
	 * @return void
	 */
	public void processRegularReport(Exchange exchange) {
		Exchange trafficScanCopy = exchange.copy();
		trafficScanHandler.prepareForTrafficScan(trafficScanCopy);
		trafficScanQueueProducer.send(trafficScanCopy);
		
		locationServiceHandler.prepareForLocationService(exchange);
		//TODO api out of date
		if(ServiceStatus.Started.equals(exchange.getContext().getRouteController().getRouteStatus(esbLocationUpdateSwitchRouteName))){
		//if(ServiceStatus.Started.equals(exchange.getContext().getRouteStatus(esbLocationUpdateSwitchRouteName))){
			esbLocationMsgQProducer.send(exchange);
		}

		//TODO api out of date
		if(ServiceStatus.Started.equals(exchange.getContext().getRouteController().getRouteStatus(eapLocationUpdateSwitchRouteName))){
		//if(ServiceStatus.Started.equals(exchange.getContext().getRouteStatus(eapLocationUpdateSwitchRouteName))){
			if(Boolean.TRUE.equals(BooleanUtils.toBoolean(odrdVehicleEnabled))) {
				odrdLocationMsgQProducer.send(exchange);
				if(Boolean.FALSE.equals(BooleanUtils.toBoolean(odrdEnabled))) {
					locationMsgQProducer.send(exchange);
				}
			}else {
				if(Boolean.FALSE.equals(BooleanUtils.toBoolean(odrdEnabled))) {
					locationMsgQProducer.send(exchange);
					log.info("locationMsgQ Data: " + exchange.getIn().getBody());
				}else {
					odrdLocationMsgQProducer.send(exchange);
					log.info("locationMsgQ Data: " + exchange.getIn().getBody());
				}
			}
		}
	}
	/**
	 * @method Name : process
	 * @param exchange
	 * @param isNew
	 * @return void
	 */
	public void process(Exchange exchange, boolean isNew) {
	    
	    if(log.isDebugEnabled()){
	        log.debug(".....INSIDE process()...isNew: "+isNew);
	    }
		DoProcessResult doProcessResult = doProcess(exchange,isNew);
		if (doProcessResult==null)
			exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
		// Tend : Regular Reports that have ACK flag set must be processed normally
		else if (doProcessResult.msgType==REGULAR_REPORT && !doProcessResult.isAck) {
			processRegularReport(exchange);
			exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);			
		}

		ArrayList<Object> addMsg = (ArrayList<Object>) exchange.getIn().getHeader(BatchingQueue.ADD_MSG_HEADER_NAME);
		if (addMsg!=null) {
			exchange.getIn().removeHeader(BatchingQueue.ADD_MSG_HEADER_NAME);
			for (Object msg : addMsg) {
				Exchange exch = (Exchange)msg;
				doProcessResult = doProcess(exch,isNew);
				if (doProcessResult!=null) {
					// Tend : Regular Reports that have ACK flag set must be processed normally
					if (doProcessResult.msgType==REGULAR_REPORT && !doProcessResult.isAck)
						processRegularReport(exch);
					else
						combinedProducer.send(exch);
				}
			}
		}
		
		if(log.isDebugEnabled()){
            log.debug(".....EXIT process()...isNew: "+isNew);
		}
	}
}
