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

package com.cdg.ngp.esb.location.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.cdg.ngp.esb.location.config.properties.CommonProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cdg.ngp.esb.location.message.ESBLocationMessage;
import com.cdg.ngp.esb.common.enums.VehicleStatusEnum;
import com.cdg.ngp.esb.common.data.Message;
import com.cdg.ngp.esb.common.data.MessageContent;
import com.cdg.ngp.esb.common.data.MessageHeader;
import com.cdg.ngp.esb.common.data.VehicleLocationJson;
import com.cdg.ngp.esb.location.utils.GsonUtil;
import com.cdg.ngp.esb.location.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Class name : ESBLocationUpdateHANADAO.java
 * @Description :Get the current vehicle location and status push to hana database
 * @Author KYJ
 *  **/

@Component
public class ESBLocationUpdateHANADAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ESBLocationUpdateHANADAO.class);
    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
    private ESBLocationUpdateDAO locationOrcaleDao;

	@Autowired
    private HttpClientUtil httpClient;
    private String authenKey;
    private String restApiUrl;

	public ESBLocationUpdateHANADAO(CommonProperties commonProperties){
		authenKey = commonProperties.getEsbHanaAuthorization();
		restApiUrl = commonProperties.getEsbHanaInsertRestApi();
	}
    
	public ESBLocationUpdateDAO getLocationOrcaleDao() {
		return locationOrcaleDao;
	}

	public void setLocationOrcaleDao(ESBLocationUpdateDAO locationOrcaleDao) {
		this.locationOrcaleDao = locationOrcaleDao;
	}
    
	public void persistVehicleStatus(List<ESBLocationMessage> messageList){
		 long start = 0L;
	        List<ESBLocationMessage> localMsgList = new ArrayList<>();
	        List<Message> mastermsglist = new ArrayList<>();
	        List<VehicleLocationJson> vehicleLocationList = new ArrayList<>();
	        try {
	        	LOGGER.info("Populate the driver id and vehicle id to list ");
	        	mastermsglist.addAll(messageList);
	        	localMsgList = locationOrcaleDao.updateOtherInfo(mastermsglist);
	        	start = System.currentTimeMillis();
	            
	            for (ESBLocationMessage message : localMsgList) {
	                MessageHeader messageHeader = message.getMessageHeader();
	                MessageContent messageContent = message.getMessageContent();
	                VehicleLocationJson vlLocation = new VehicleLocationJson();
	                vlLocation.setLOGDT(dateTimeFormat.format(messageHeader.getTimestamp()));
	                vlLocation.setIVDID(Integer.toString(message.getMessageHeader().getMobileId()));
	                vlLocation.setDRIVERID(messageContent.getDriverID());
	                vlLocation.setVEHICLEID(messageContent.getVehicleID());
	                vlLocation.setJOBNO(message.getJobNumber());
	                vlLocation.setLAT(messageHeader.getyOffset());
	                vlLocation.setLONG(messageHeader.getxOffset());
	                vlLocation.setSTATUS(VehicleStatusEnum.getEnumByVehStatusNo(Integer.valueOf(messageHeader.getIvdStatus())).getVehStatusLabel().toUpperCase());
	                vehicleLocationList.add(vlLocation);
	                LOGGER.debug("persistVehicleStatus:" + message.getMessageHeader().toString());
	            }
	            
	            String jsonString = GsonUtil.generateToJson(vehicleLocationList);
	            if(StringUtils.isNotEmpty(jsonString)){
	            	LOGGER.debug("Authen Key ["+authenKey+"] url ["+restApiUrl+"]");
	            	String response = httpClient.httpPost(authenKey, restApiUrl, null, new StringEntity(jsonString));
	            	LOGGER.debug("response from url ["+response+"]");
	            }
	            LOGGER.info("Inside persistVehicleStatus():batchSize[" + localMsgList.size() + "]: completed in-->"
	                    + (System.currentTimeMillis() - start) + " milliseconds");
	           
	        }catch (Exception e) {
	            LOGGER.error("Exception while persisting vehicleLog:",e);
	        }
	}

	public HttpClientUtil getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClientUtil httpClient) {
		this.httpClient = httpClient;
	}

	public String getRestApiUrl() {
		return restApiUrl;
	}

	public void setRestApiUrl(String restApiUrl) {
		this.restApiUrl = restApiUrl;
	}

	public String getAuthenKey() {
		return authenKey;
	}

	public void setAuthenKey(String authenKey) {
		this.authenKey = authenKey;
	}
}
