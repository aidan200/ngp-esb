package com.cdg.ngp.esb.ms.simulator;

import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import sg.com.cdgtaxi.comms.tlv.msg.IVDFieldTag;
import sg.com.cdgtaxi.comms.tlv.msg.IVDMessage;
import sg.com.cdgtaxi.comms.tlv.msg.IVDMessageContent;
import sg.com.cdgtaxi.comms.tlv.util.BytesSize;
import sg.com.cdgtaxi.comms.tlv.util.BytesUtil;

public class SimulatorUtils {
	 public static void fillTimestamp(byte[] sourceArray, long timestamp, int timestampStartIndex) {
	        byte[] binaryTimestamp = getBinaryTimestamp(timestamp);
	        
	        for (int i = 0; i < binaryTimestamp.length; i++) {
	            Arrays.fill(sourceArray, 
	                    timestampStartIndex + i, timestampStartIndex + i + 1, binaryTimestamp[i]);
	        }
	    }
	    
 private static byte[] getBinaryTimestamp(long timestamp) {
     long epoch = timestamp / 1000;
     byte[] bytesTimestamp = BytesUtil.convertToBytes(epoch, BytesSize.UINT32);
     // convert to LITTLE_ENDIAN
     BytesUtil.reverseBytes(bytesTimestamp);
     return bytesTimestamp;
 }
 
 
 @SuppressWarnings("static-access")
	public static void putItemIntoIvdMessage(Object data, IVDFieldTag tag,
			IVDMessageContent message) {
		// Don't put the null obj to the message
		if (data == null)
			return;

		// Get BytesSize associated with this tag
		TlvTagsFactory tlvTagsFactory = new TlvTagsFactory();
		BytesSize size = tlvTagsFactory.getTagByteSize(tag);

		if (size == null)
			return;

		Object objValue;

		switch (size) {
		case BOOLEAN:
			boolean bValue = false;
			if (data instanceof Boolean) {
				bValue = (Boolean) data;
			} else {
				bValue = Boolean.valueOf(String.valueOf(data));
			}

			message.putBoolean(tag, bValue);
			break;
		case CHAR:
			char cValue = data.toString().charAt(0);
			message.putChar(tag, cValue);
			break;
		case BYTE:
			byte binary;
			if (data instanceof Byte) {
				binary = (Byte) data;
			} else {
				binary = Byte.parseByte(String.valueOf(data));
			}

			message.putByte(tag, binary);
			break;
		case COORD:
		case MONEY:
		case SPEED:
		case TIME:
		case UINT16:
		case INT32:
			int iValue;
			if (data instanceof Integer) {
				iValue = (Integer) data;
			} else {
				iValue = Integer.parseInt(String.valueOf(data));
			}
			message.putInt32(tag, iValue);
			break;
		case DATETIME:
			if (data instanceof Date) {
				message.putDateTime(tag, (Date) data);
			} else if (data instanceof Long) {
				message.putDateTime(tag, (Long) data);
			}
			break;
		case INT16:
			short sValue;
			if (data instanceof Short) {
				sValue = (Short) data;
			} else {
				sValue = Short.valueOf(String.valueOf(data));
			}
			message.putInt16(tag, sValue);
			break;
		case DISTANCE:
		case TIMESTAMP:
		case UINT32:
		case INT64:
		case UINT64:
			long lValue;
			if ((data instanceof Long) || (data instanceof Double)) {
				lValue = (Long) data;
			} else {
				lValue = Long.valueOf(String.valueOf(data));
			}
			message.putInt64(tag, lValue);
			break;
		default:
			objValue = parseObject(data, size);
			message.put(tag, objValue, size);
		}
	}
 
	private static Object parseObject(Object data, BytesSize size) {
		Object objValue = null;
		objValue = BytesUtil.parse(String.valueOf(data), size);
		return objValue;
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
	
	/**
	 * Get Last 15 bytes to identify the ip address
	 * @param msgBytes
	 * @return 
	 */
	public static byte[] getIPBytes(byte[] msgBytes) {
	
		return ArrayUtils.subarray(msgBytes,msgBytes.length-15,msgBytes.length);
	
	}
	
	/**
	 * Insert "0" in front of numbers to make it fixed length
	 *
	 * @param lValue
	 *            Number to be fixed with "0" in front
	 * @param nTotalLen
	 *            Total length after inserting "0"
	 * @return Converted number as String
	 */
	public static String fixZeroesInFront(long lValue, int nTotalLen) {
		String tmp = "";
		//TODO api out of date
		String strTemp = String.valueOf(lValue);
		//String strTemp = new Long(lValue).toString();
		int nStrLen = strTemp.length();
		for (int i = 0; i < (nTotalLen - nStrLen); i++) {
			tmp += "0";
		}
		return tmp + strTemp;
	}

}
