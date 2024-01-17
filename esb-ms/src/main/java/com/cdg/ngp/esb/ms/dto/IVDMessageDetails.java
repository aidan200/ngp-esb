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
package com.cdg.ngp.esb.ms.dto;

import java.net.InetSocketAddress;

import org.apache.commons.lang3.ArrayUtils;

import sg.com.cdgtaxi.comms.tlv.msg.*;
import static sg.com.cdgtaxi.comms.tlv.msg.IVDHeaderType.*;
/** 
 * @Class name : IVDMessageDetails.java
 * @description : IVD message details are stored in message bytes
 **/
public class IVDMessageDetails {
	byte[] messageBytes;
	IVDMessageHeader header;
	boolean isNew;
	int contentStartIndex;
	InetSocketAddress peerAddr;

	/**
	 * @method Name : IVDMessageDetails
	 * @param messageBytes
	 * @param isNew
	 * @param peerAddr
	 */
	public IVDMessageDetails(byte[] messageBytes, boolean isNew,
			InetSocketAddress peerAddr) {
		this.messageBytes = messageBytes;
		this.isNew = isNew;
		this.peerAddr = peerAddr;
		int headerLength = isNew ? COMM_RPTDATA_TSTMP.getLength()
				: COMM_RPTDATA.getLength();
		int headerStartIndex = isNew ? 2 : 0;
		this.contentStartIndex = headerStartIndex + headerLength;
		byte[] headerBytes = ArrayUtils.subarray(messageBytes,
				headerStartIndex, contentStartIndex);
		this.header = new IVDMessageHeader(headerBytes);
	}

	/**
	 * @method Name : getMessageBytes
	 * @return messageBytes
	 */
	public byte[] getMessageBytes() {
		return messageBytes;
	}
	/**
	 * @method name:setMessageBytes
	 * @param messageBytes
	 * @return void 
	 */
	public void setMessageBytes(byte[] messageBytes) {
		this.messageBytes = messageBytes;
	}
	/**
	 * @method Name : getContentStartIndex
	 * @return contentStartIndex
	 */
	public int getContentStartIndex() {
		return contentStartIndex;
	}
	/**
	 * @method setContentStartIndex
	 * @param contentStartIndex
	 * @return void 
	 */
	public void setContentStartIndex(int contentStartIndex) {
		this.contentStartIndex = contentStartIndex;
	}

	/**
	 * @method Name : getHeader
	 * @return IVDMessageHeader
	 */
	public IVDMessageHeader getHeader() {
		return header;
	}

	/**
	 * @method Name : setHeader
	 * @param header
	 * @return void 
	 */
	public void setHeader(IVDMessageHeader header) {
		this.header = header;
	}

	/**
	 * @method Name : isNew
	 * @return isNew
	 */
	public boolean isNew() {
		return isNew;
	}

	/**
	 * @method Name : setNew
	 * @param isNew
	 * @return void 
	 */
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	/**
	 * @method Name:getPeerAddr
	 * @return peerAddr
	 */
	public InetSocketAddress getPeerAddr() {
		return peerAddr;
	}

	/**
	 * @method setPeerAddr
	 * @param peerAddr
	 * @return void 
	 */
	public void setPeerAddr(InetSocketAddress peerAddr) {
		this.peerAddr = peerAddr;
	}

	@Override
	public String toString() {
		return "IVDMessage [" + header.getType() + "," + header.getMobileID()
				+ "," + (isNew ? "NEW" : "OLD") + ","
				+ peerAddr.getAddress().getHostAddress() + ":"
				+ peerAddr.getPort() + "]";
	}
}
