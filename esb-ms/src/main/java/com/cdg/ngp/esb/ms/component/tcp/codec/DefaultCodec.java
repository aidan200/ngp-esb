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
package com.cdg.ngp.esb.ms.component.tcp.codec;

import com.cdg.ngp.esb.ms.component.tcp.TCPCodec;
/** 
 * @Class name : DefaultCodec.java
 * @Description :Message Recieve Bytes to Send Object
 * @Author tend
 * @since 15 June 2016
 **/
public class DefaultCodec implements TCPCodec {

	@Override
	public Object[] Receive(byte[] bytes) throws Exception {
    	String msg = new String(bytes);
        int p = msg.indexOf('\n');
        return p==-1 ? null : new Object[]{msg.substring(0,p),p+1};
    }

	@Override
	public byte[] BytesToSend(Object msg) throws Exception {
    	return (msg+"\n").getBytes();
    }
	
}
