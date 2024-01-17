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

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cdg.ngp.esb.ms.component.tcp.TCPCodec;
import com.cdg.ngp.esb.ms.component.tcp.TCPConnector;
/** 
 * @Class name : TCPLengthFieldCodec.java
 * @author : Tend
**/
public class TCPLengthFieldCodec implements TCPCodec {
	@Override
	public Object[] Receive(byte[] bytes) throws Exception {
		int size = bytes.length;
		if (size<2)
			return null;
		int msgSize = 2+(bytes[0]&0xff)+((bytes[1]&0xff)<<8);
		if (size<msgSize)
			return null;
		return new Object[]{ArrayUtils.subarray(bytes,0,msgSize),msgSize};
	}
	/** 
	 * @method  name : BytesToSend
	 * @param : msg
	 * @return :sizeBytes,sBytes
	**/
	@Override
	public byte[] BytesToSend(Object msg) throws Exception {
		byte[] sBytes = (byte[]) msg;
		int size = sBytes.length;
		byte[] sizeBytes = new byte[]{(byte)(size&255),(byte)(size>>8)};
		return ArrayUtils.addAll(sizeBytes,sBytes);
	}
}
