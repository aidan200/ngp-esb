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

package com.cdg.ngp.esb.ms.component.tcp;

/** 
 * This is the interface that codecs used by the {@link TCPConnector} must implement. The codec provides
 * two methods, {@link #Receive(byte[])} and {@link #BytesToSend(Object)}. 
 * 
 * @author tend
**/
public interface TCPCodec {
	/**
	 * Implement this method to de-serialize the received bytes. If the bytes can be de-serialized, the
	 * returned array should contain the object followed by an integer representing the number of bytes
	 * read from the front of the byte array to make the object.
	 * @param bytes The byte array read by the {@link TCPConnector}.
	 * @return an object array as described above or null if the bytes cannot be de-serialized.
	 */
	Object[] Receive(byte[] bytes) throws Exception;
	/**
	 * Implement this method to serialize the object to be sent.
	 * @param msg The object to be serialized.
	 * @return a byte array containing the serialized bytes.
	 */
	byte[] BytesToSend(Object msg) throws Exception;
}
