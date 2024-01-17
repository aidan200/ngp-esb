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



import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.cdg.taxi.fuse.dto.IVDMessageDetails;
//import com.cdg.taxi.fuse.handler.Cn2ServerHandler;
//import com.cdg.taxi.fuse.handler.LocationServiceHandler;

import sg.com.cdgtaxi.comms.tlv.msg.IVDFieldTag;
import sg.com.cdgtaxi.comms.tlv.msg.IVDMessage;
import sg.com.cdgtaxi.comms.tlv.msg.IVDMessageType;
import sg.com.cdgtaxi.comms.tlv.util.BytesSize;
import sg.com.cdgtaxi.comms.tlv.util.BytesUtil;




/**Class Name: Helper
 * <p>
 *     This class do the helper methods for the whole application
 *     
 * </p>
 *
 * @since Dec 14, 2016
 * @author Ganesan Dharmendiran
 *
 */
public class Helper {
	private static final Logger log = LoggerFactory.getLogger(Helper.class);
	/**
	 * private empty constructor to hide the implicit public one
	 * As standard utility classes should not have public constructors.
	 */
	private Helper() {
	}
	/**
	 * Append two byte length infront of message its from port 8004
	 * 
	 * @param msg
	 * @return
	 */
	public static byte[] appendLength(byte[] msg) {
		int length = msg.length;
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.putShort((short) length);
		byte[] msgLength = buffer.array();
		ArrayUtils.reverse(msgLength);
		return ArrayUtils.addAll(msgLength, msg);
	}
	/**
	 * Exclude the first two length represent bytes from message
	 * @param msg
	 * @return
	 */
	public static byte[] lengthExcludedMsgBytes(byte[] msg) {
		return ArrayUtils.subarray(msg,2,msg.length);		
	}
    /**
     * Convert String to bytes[]
     * @param strHex
     * @return
     */
	public static byte[] convertToBytes(String strHex) {
	        String[] hexStrArr = strHex.split("\\s+");
	        byte[] bytes = new byte[hexStrArr.length];

	        for (int i = 0; i < hexStrArr.length; i++) {
	            try {
	                bytes[i] = (byte) (Integer.parseInt(hexStrArr[i], 16));
	            } catch (Exception ex) {
	            	log.info("Caught an exception :" + ex);
	                bytes[i] = 0x0000;
	            }
	        }
	        return bytes;
	 }
	/**
	 * Find the length based on first two bytes
	 * @param msgBytes
	 * @return
	 */
	public static int findLengthFromFirstTwoBytes(byte[] msgBytes) {
		byte[] msgLengthInBytes=ArrayUtils.subarray(msgBytes, 0, 2);
		int length = ((msgLengthInBytes[1] & 0xff) << 8) | (msgLengthInBytes[0] & 0xff);
		return length;
	}
	/**
	 * Get Last 15 bytes to identify the ip address
	 * @param msgBytes
	 * @return 
	 */
	public static byte[] getIPBytes(byte[] msgBytes) {
	
		return ArrayUtils.subarray(msgBytes,msgBytes.length-15,msgBytes.length);
	
	}
	/**
	 * Get the physical ip address from ip with trimmed empty spaces
	 * @param ipBytes
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getPhysicalIpByBytes(byte[] ipBytes) throws UnsupportedEncodingException {
		String physicalIP = new String(ipBytes, "UTF-8");
		return physicalIP.trim();
	}
	
	/**
	 * Get the First Bit value from message bytes to check zero or one
	 * @param inpBytes
	 * @return
	 */

	public static int getFirstBitValue(byte[] msgBytes) {		
		return ((msgBytes[1] & 128) >> 7);		
	}
	
	/**
	 * Convert byte to integer value	
	 * @param inByte
	 * @return
	 */
	public static int convertByteToInt(byte inByte) {		
		return (inByte & 0xff);
	}
	
	/**
     * Remove the last 15 bit ips in message
     * @param msgBytes
     * @return
     */
    public static byte[] removeIvdIpBytes(byte[] msgBytes) {        
        if(msgBytes.length>15){
        byte[] responseBytes=ArrayUtils.subarray(msgBytes, 0, msgBytes.length-15);
            return responseBytes;
        }else{
            return msgBytes;
        } 
    }
    
    /**
     * Convert physical Ip and append empty spaces (rightPad)
     * @param ip
     * @param totalLength
     * @return
     */
    public static byte[] convertPhysicalIpToBytes(String ip,int totalLength) {
    	String ipWithRequiredLength=StringUtils.rightPad(ip, 15);
		byte[] ipBytes=ipWithRequiredLength.getBytes();
		return ipBytes;
    }
   
    /**
     *
     * Convert byte array to string
     * @param bytes
     * @return stringValue
     * @throws UnsupportedEncodingException
     */
    public static String convertBytesToString(byte[] bytes) throws UnsupportedEncodingException{
    	String stringValue = new String(bytes, "UTF-8");
		return stringValue.trim();
    }
    
    /**
     * Returns a list of IPs belonging to the local host machine.
     * @return String[]
     * @throws SocketException 
     */
    public static String[] getLocalIPs() throws SocketException {
    	ArrayList<String> ips = new ArrayList<String>();
		for (Enumeration<NetworkInterface> nie=NetworkInterface.getNetworkInterfaces();nie.hasMoreElements();) {
			NetworkInterface ni = nie.nextElement();
			for (Enumeration<InetAddress> iae=ni.getInetAddresses();iae.hasMoreElements();) {
				InetAddress ia = iae.nextElement();
				ips.add(ia.getHostAddress());
			}
		}
		return ips.toArray(new String[]{});
    }
    /**
     * @author Zhao Zilong
     * @Description Woon's requirement:The formatted info level log for message from MDT to backend. 
     * @Format:REGULAR_REPORT from new MDT. Message header bytes:BF 58 FD C5 7F 8C 54 70 00 20 00 39 31 30 2E 36 31 2E XXXX. ContentLenght:16 
     * @param log: class's Logger
     * @param ivdMsgType:String ivdMsgType
     * @param isNew
     * @param headerBytes
     * @param contentLength
     * @return void
     */
	public static void infoLogFromMDT(Logger log, String ivdMsgType, boolean isNew, byte[] headerBytes,
			int contentLength) {
		log.info(ivdMsgType + " from" + (isNew ? "new" : "old") + " MDT." + " Message header bytes:"
				+ BytesUtil.toString(headerBytes) + ". ContentLength:" + contentLength);
	}
	/**
	 * @author Zhao Zilong
     * @Description Woon's requirement:The formatted debug level log for message from MDT to backend. 
     * @Format:old message(header+body) from MDT:19 xx xx xx xx 
	 * @Description debugLogFromMDT
	 * @param log:class's Logger
	 * @param isNew
	 * @param msgBytes
	 * @return void
	 */
	public static void debugLogFromMDT(Logger log, boolean isNew, byte[] msgBytes) {
		log.debug(isNew ? "new" : "old" + " message(header+body) bytes from MDT:" + BytesUtil.toString(msgBytes));
	}
	
	@SuppressWarnings({ "unchecked", "static-access" })
	public static  <T extends Object> T convertData(IVDMessage message, IVDFieldTag tag,
	            Class<T> type) {
		
		    TlvTagsFactory tlvTagsFactory = new TlvTagsFactory();
	        BytesSize size = tlvTagsFactory.getTagByteSize(tag);
	        
	        if (size == null) {
	            return (T) message.getBytes(tag);
	        }
	        
	        Object data = message.get(tag, size);
	        
	        if (data == null) return null;
	        
	        if (type == Date.class) {
	            if (data instanceof Date) {
	                return (T) data;
	            } else {
	                long milliseconds = (Long) data;
	                return (T) new Date(milliseconds);
	            }
	        }
	        
	        String value;
	        if (size == BytesSize.BOOLEAN) {
	            value = String.valueOf(((Boolean) data) ? 1 : 0); 
	        } else {
	            value = String.valueOf(data);
	        }
	        
	        // Cast to expected type
	        if (type == Integer.class) {
	            return (T) Integer.valueOf(value);
	        } else if (type == Short.class) {
	            return (T) Short.valueOf(value);
	        }else if (type == Long.class) {
	            return (T) Long.valueOf(value);
	        } else if (type == Double.class) {
	            return (T) Double.valueOf(value);
	        } else if (type == Boolean.class) {
	            return (T) Boolean.valueOf(value);
	        } else if (type == Byte.class) {
	            return (T) Byte.valueOf(value);
	        } else {
	            return (T) value;
	        }
	    }
	
}
