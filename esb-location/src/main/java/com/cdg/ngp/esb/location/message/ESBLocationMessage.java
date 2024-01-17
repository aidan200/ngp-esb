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
package com.cdg.ngp.esb.location.message;

import com.cdg.ngp.esb.common.data.Message;
/** 
 * @Class name : ESBLocationMessage.java
 * @Description Get the MessageHeader and MessageContent
 **/
public class ESBLocationMessage extends Message {

	private static final long serialVersionUID = 4019888383475636897L;
	/** 
	 * @Method name : ESBLocationMessage
	 * @param message
	 **/
	public ESBLocationMessage(Message message){
		super(message.getMessageHeader(), message.getMessageContent());
	}
	
	private String jobNumber;
	
	/** 
	 * @Method name : getJobNumber
	 * @return jobNumber
	 **/
	
	public String getJobNumber() {
		return jobNumber;
	}
	/** 
	 * @Method name : setJobNumber
	 * @param jobNumber
	 * @return void
	 **/
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	
}
