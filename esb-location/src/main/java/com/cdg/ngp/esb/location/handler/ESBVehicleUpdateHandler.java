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
import java.util.List;

import com.cdg.ngp.esb.location.config.properties.CommonProperties;
import org.apache.camel.Exchange;

import com.cdg.ngp.esb.location.dao.ESBLocationUpdateDAO;
import com.cdg.ngp.esb.location.dao.ESBLocationUpdateHANADAO;
import com.cdg.ngp.esb.location.message.ESBLocationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Class name : LocationServiceHandler.java
 * @Description :for processing the LocationService and VheicleUpdateInfo data
 * @Author Mohan
 * @Since 12 Jan 2016
**/
@Component("esbVehicleUpdateHandler")
public class ESBVehicleUpdateHandler {

	@Autowired
	private ESBLocationUpdateDAO locationUpdateDAO;
	@Autowired
	private ESBLocationUpdateHANADAO locationUpdateHanaDAO;
	private Integer switchVal;

	public ESBVehicleUpdateHandler(CommonProperties commonProperties){
		this.switchVal = commonProperties.getEsbLocationToHana();
	}

	/**
	 * @method Name : process
	 * @param exchange
	 * @return void
	 */
	public void process(Exchange exchange) {
		List<ESBLocationMessage> messageList = (List<ESBLocationMessage>) exchange.getIn().getBody();
		locationUpdateDAO.persistVehicleUpdate(messageList);
		if(switchVal == 1)
			locationUpdateHanaDAO.persistVehicleStatus(messageList);
	}
	/**
	 * @method Name : getLocationUpdateDAO
	 * @return ESBLocationUpdateDAO
	 */
	public ESBLocationUpdateDAO getLocationUpdateDAO() {
		return locationUpdateDAO;
	}
	/**
	 * @method Name : setLocationUpdateDAO
	 * @param locationUpdateDAO
	 * @return void
	 */
	public void setLocationUpdateDAO(ESBLocationUpdateDAO locationUpdateDAO) {
		this.locationUpdateDAO = locationUpdateDAO;
	}
	
	/**
	 * @method Name : getLocationUpdateHanaDAO
	 * @param locationUpdateHanaDAO
	 * @return void
	 */
	public ESBLocationUpdateHANADAO getLocationUpdateHanaDAO() {
		return locationUpdateHanaDAO;
	}
	
	/**
	 * @method Name : setLocationUpdateHanaDAO
	 * @param locationUpdateHanaDAO
	 * @return void
	 */
	public void setLocationUpdateHanaDAO(ESBLocationUpdateHANADAO locationUpdateHanaDAO) {
		this.locationUpdateHanaDAO = locationUpdateHanaDAO;
	}
	public Integer getSwitchVal() {
		return switchVal;
	}
	public void setSwitchVal(Integer switchVal) {
		this.switchVal = switchVal;
	}
	
	
}
