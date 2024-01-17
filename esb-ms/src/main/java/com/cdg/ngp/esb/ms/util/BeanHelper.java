package com.cdg.ngp.esb.ms.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.camel.Exchange;
import org.apache.camel.ServiceStatus;
//import org.jboss.netty.buffer.ChannelBuffers;
import org.slf4j.Logger;

import sg.com.cdgtaxi.comms.tlv.msg.IVDMessageType;
import sg.com.cdgtaxi.comms.tlv.util.BytesUtil;

public class BeanHelper {
	public String checkTTL(Exchange exchange) throws Exception {
		Object obj = exchange.getIn().getHeader("JMSExpiration");
		if (obj==null)
			return "true";
		long ttl = (long) obj;
		return System.currentTimeMillis()<ttl ? "true" : "false";
	}
	
	public void changeBytesToChannelBuffer(Exchange exchange){
		//TODO unknown risk
		ByteBuf buffer = Unpooled.wrappedBuffer((byte[]) exchange.getIn().getBody());
		exchange.getIn().setBody(buffer);
		//exchange.getIn().setBody(ChannelBuffers.wrappedBuffer((byte[]) exchange.getIn().getBody()));
	}
	
	/**
	 * @author Zhao Zilong
	 * @NoticeThe message from backend to MDT only have the header.  
	 * @Description Woon's requirement:The formatted info level log for message from backend to MDT.
	 * @Format: IVD_PING to new MDT. Message(header) bytes:19 xx xx xx xx 
	 * @param exchange:The original message bytes from backend contained in the body of the exchange
	 * @return String
	 */
	public String msgHeaderBytesFromBackend(Exchange exchange) {
		byte[] msgHeaderBytes = (byte[])exchange.getIn().getBody();
		return "message(header) bytes:" + BytesUtil.toString(msgHeaderBytes);
	}

	public String checkStarted(Exchange exchange,String routeName) throws Exception {
		//TODO api out of date
		return ServiceStatus.Started.equals(exchange.getContext().getRouteController().getRouteStatus(routeName)) ? "true" : "false";
		//return ServiceStatus.Started.equals(exchange.getContext().getRouteStatus(routeName)) ? "true" : "false";
	}
}
