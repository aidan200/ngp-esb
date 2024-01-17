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
package com.cdg.ngp.esb.ms.util;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Exchange;
/** 
 * @Class name : BatchingQueue.java
 * @Description :TODO
 * @Author tend
 * @Since 22 Feb, 2016
**/
public class BatchingQueue extends LinkedBlockingQueue<Object> {
	public static final String ADD_MSG_HEADER_NAME = "ADD_MSG";
	private static final long serialVersionUID = 3993738166162203596L;
	
	@Override
	public Object poll(long arg0, TimeUnit arg1) throws InterruptedException {
		Object o = super.poll(arg0, arg1);
		if (o!=null) {
			ArrayList<Object> list = new ArrayList<Object>();
			if (super.drainTo(list)>0) {
				Exchange exchange = (Exchange)o;
				exchange.getIn().setHeader(ADD_MSG_HEADER_NAME,list);
			}
		}
		return o;
	}

	

}
