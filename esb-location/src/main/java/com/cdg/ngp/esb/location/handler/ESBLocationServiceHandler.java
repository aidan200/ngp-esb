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
package com.cdg.ngp.esb.location.handler;
import java.util.ArrayList;
import java.util.List;

import com.cdg.ngp.esb.location.config.properties.CommonProperties;
import org.apache.camel.Exchange;

import com.cdg.ngp.esb.location.dao.ESBLocationUpdateDAO;
import com.cdg.ngp.esb.common.data.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Class name : LocationServiceHandler.java
 * @Description :for processing the LocationService and VheicleUpdateInfo data
 * @Author Mohan
 * @Since 12 Jan 2016
**/
@Component("esbLocationServiceHandler")
public class ESBLocationServiceHandler {
	
	private static List<Message> messageListBuffer = new ArrayList<Message>();
	private long lastBatchTime = System.currentTimeMillis();
	private long batchSize = 100;
	private long batchMaxIntervelMS = 5000;
	
	public long getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(long batchSize) {
		this.batchSize = batchSize;
	}

	public long getBatchMaxIntervelMS() {
		return batchMaxIntervelMS;
	}

	public void setBatchMaxIntervelMS(long batchMaxIntervelMS) {
		this.batchMaxIntervelMS = batchMaxIntervelMS;
	}

	public ESBLocationServiceHandler(CommonProperties commonProperties){
		this.batchSize = commonProperties.getEsbLocationServiceBatchSize();
		this.batchMaxIntervelMS = commonProperties.getEsbLocationServiceBatchInterval();
	}

	@Autowired
	private ESBLocationUpdateDAO locationUpdateDAO;
	
	public ESBLocationUpdateDAO getLocationUpdateDAO() {
		return locationUpdateDAO;
	}

	public void setLocationUpdateDAO(ESBLocationUpdateDAO locationUpdateDAO) {
		this.locationUpdateDAO = locationUpdateDAO;
	}

	synchronized private List<Message> getMessageListFromBuffer(){
		List<Message> messageList = new ArrayList<Message>();
		long currentTime = System.currentTimeMillis();
		long diff = currentTime - lastBatchTime;
		synchronized (messageListBuffer) {
			if((diff>batchMaxIntervelMS && messageListBuffer.size()>0) || (messageListBuffer.size()>(batchSize-1))){		
					lastBatchTime = currentTime;
					messageList.addAll(messageListBuffer);
					messageListBuffer.clear();
			}
		}
		
		return messageList;
	}
	
	synchronized private void addMessageToBuffer(Message message){
		messageListBuffer.add(message);		
	}
	
	/**
	 * @method :process
	 * @param exchange
	 * @return void 
	 */
	public void process(Exchange exchange) {
		Message message = (Message) exchange.getIn().getBody();
		addMessageToBuffer(message);
		List<Message> messageList = getMessageListFromBuffer();
		if(messageList!=null && messageList.size()>0){
			exchange.getIn().setBody(locationUpdateDAO.updateOtherInfo(messageList));
		}else{
			exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);	
		}
	}

}
