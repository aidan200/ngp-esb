package com.cdg.ngp.esb.ms.simulator;


import java.util.Arrays;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sg.com.cdgtaxi.comms.tlv.msg.IVDFieldTag;
import sg.com.cdgtaxi.comms.tlv.msg.IVDMessage;
import sg.com.cdgtaxi.comms.tlv.msg.IVDMessageContent;
import sg.com.cdgtaxi.comms.tlv.util.BytesUtil;
import sg.com.cet.escalade.uc.comms.domain.GenericVO;

public class SimulatorBean {
	private static final Logger log = LoggerFactory
			.getLogger(SimulatorBean.class);
	
	public void dispatchMessageFromTlvQ(Exchange exchange){
		GenericVO vo = (GenericVO) exchange.getIn().getBody();
		byte[] inBytes = vo.getByteArray();
		int nMsgId = (char) (inBytes[0] & 0xFF);
		log.info("msgId："+nMsgId+"#####:"+BytesUtil.toString(inBytes));
//		if(nMsgId == 10){                     //JobOffer
//			int [] arr = {0,1,2,3,4};
//			int index=(int)(Math.random()*arr.length);
//			int rand = arr[index];
//			if(rand != 1){//JobAccept
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				byte[] aceeptBytes = jobAcceptSimulatorNew(inBytes);
//				exchange.getIn().setBody(aceeptBytes);
//			}else{//JobReject
//				byte[] rejectBytes = jobRejectSimulatorNew(inBytes);
//				exchange.getIn().setBody(rejectBytes);
//			}
//		}else if(nMsgId == 11){
//			byte[] confirmAckBytes = jobConfirmAckSimulatorNew(inBytes);
//			exchange.getIn().setBody(confirmAckBytes);
//		}else{
//			log.info("New Not handing msgId:" + nMsgId);
//			exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
//		}
		if(nMsgId == 17){
			byte[] aceeptBytes = myWorkFlowSimulator(inBytes);
			exchange.getIn().setBody(aceeptBytes);
		}
	}
	
	/**
	 * simulate new JobAccept 
	 * @param inBytes
	 */
	public byte[] jobAcceptSimulatorNew(byte[] inBytes){
		double[] locations = LocationUtils.getRandomPosition();
		double vehX =locations[1];
		double vehY =locations[0];
		int nOffsetY = (int) ((vehX - 1) * 100000);
		int nOffsetX = (int) ((vehY - 103.55) * 100000);
		
		//compose the header
		byte[] bytes = new byte[1000];
		Arrays.fill(bytes, (byte)0);
		bytes[0] = (byte) 154;
		bytes[2] = inBytes[2];
		bytes[3] = inBytes[3];
		bytes[4] = (byte) nOffsetY;
		bytes[5] = (byte) (nOffsetY >> 8);
		bytes[6] = (byte) nOffsetX;
		bytes[7] = (byte) (nOffsetX >> 8);
		byte[] outHeader = Arrays.copyOf(bytes, 17);
		SimulatorUtils.fillTimestamp(outHeader, System.currentTimeMillis(), 12);
		
		//compose content of outBytes
		IVDMessage inMessage = new IVDMessage(inBytes);
		String jobNo = SimulatorUtils.convertData(inMessage, IVDFieldTag.JOB_NUMBER,
				String.class);
		//randomly get ETA
		int [] arr = {120,240,360,480};
		int index=(int)(Math.random()*arr.length);
		int rand = arr[index];
		String eTA = String.valueOf(rand);
		int dispatchLevel = Integer.parseInt(SimulatorUtils.convertData(inMessage, IVDFieldTag.SOURCE, String.class));
		IVDMessageContent message = new IVDMessageContent();
		SimulatorUtils.putItemIntoIvdMessage(jobNo, IVDFieldTag.JOB_NUMBER,
				message);
		SimulatorUtils.putItemIntoIvdMessage(eTA, IVDFieldTag.ETA,
				message);
		SimulatorUtils.putItemIntoIvdMessage(dispatchLevel, IVDFieldTag.SOURCE,
				message);
		byte[] outContent = message.toHexBytes();
		
		//combine the header,content,ip
		byte[] outBytes = BytesUtil.joinBytes(outHeader, outContent);
		byte[] ipBytes = SimulatorUtils.getIPBytes(inBytes);
		byte[] bytesOutWithIp = BytesUtil.joinBytes(outBytes, ipBytes);
		
		log.info("New Accept bytesOutWithIp:" + BytesUtil.toString(bytesOutWithIp));
		return bytesOutWithIp;
	}
	
	/**
	 * simulate new JobReject
	 * @param inBytes
	 */
	public byte[] jobRejectSimulatorNew(byte[] inBytes){
		double[] locations = LocationUtils.getRandomPosition();
		double vehX =locations[1];
		double vehY =locations[0];
		int nOffsetY = (int) ((vehX - 1) * 100000);
		int nOffsetX = (int) ((vehY - 103.55) * 100000);
		
		//compose the header
		byte[] bytes = new byte[1000];
		Arrays.fill(bytes, (byte)0);
		bytes[0] = (byte) 155;
		bytes[2] = inBytes[2];
		bytes[3] = inBytes[3];
		bytes[4] = (byte) nOffsetY;
		bytes[5] = (byte) (nOffsetY >> 8);
		bytes[6] = (byte) nOffsetX;
		bytes[7] = (byte) (nOffsetX >> 8);
		byte[] outHeader = Arrays.copyOf(bytes, 17);
		SimulatorUtils.fillTimestamp(outHeader, System.currentTimeMillis(), 12);
		
		//compose content of outBytes
		IVDMessage inMessage = new IVDMessage(inBytes);
		String jobNo = SimulatorUtils.convertData(inMessage, IVDFieldTag.JOB_NUMBER,
				String.class);
		int dispatchLevel = Integer.parseInt(SimulatorUtils.convertData(inMessage, IVDFieldTag.SOURCE, String.class));
		
		IVDMessageContent message = new IVDMessageContent();
		SimulatorUtils.putItemIntoIvdMessage(jobNo, IVDFieldTag.JOB_NUMBER,
				message);
		int [] arr = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14};
		int index=(int)(Math.random()*arr.length);
		int rejectCode = arr[index];
		SimulatorUtils.putItemIntoIvdMessage(rejectCode, IVDFieldTag.REJECT_REASON_CODE,
				message);
		SimulatorUtils.putItemIntoIvdMessage(dispatchLevel, IVDFieldTag.SOURCE,
				message);
		byte[] outContent = message.toHexBytes();
		
		//combine the header,content,ip
		byte[] outBytes = BytesUtil.joinBytes(outHeader, outContent);
		byte[] ipBytes = SimulatorUtils.getIPBytes(inBytes);
		byte[] bytesOutWithIp = BytesUtil.joinBytes(outBytes, ipBytes);
		log.info("New Reject bytesOutWithIp:"+BytesUtil.toString(bytesOutWithIp));
		
		return bytesOutWithIp;
	}
	
	/**
	 * simulate New JobConfirmAck
	 * @param inBytes
	 */
	public byte[] jobConfirmAckSimulatorNew(byte[] inBytes){
		double[] locations = LocationUtils.getRandomPosition();
		double vehX =locations[1];
		double vehY =locations[0];
		int nOffsetY = (int) ((vehX - 1) * 100000);
		int nOffsetX = (int) ((vehY - 103.55) * 100000);
		
		//compose the header
		byte[] bytes = new byte[100];
		Arrays.fill(bytes, (byte)0);
		bytes[0] = (byte) 188;
		bytes[2] = inBytes[2];
		bytes[3] = inBytes[3];
		bytes[4] = (byte) nOffsetY;
		bytes[5] = (byte) (nOffsetY >> 8);
		bytes[6] = (byte) nOffsetX;
		bytes[7] = (byte) (nOffsetX >> 8);
		byte[] outHeader = Arrays.copyOf(bytes, 17);
		SimulatorUtils.fillTimestamp(outHeader, System.currentTimeMillis(), 12);
		
		//compose content of outBytes
		IVDMessage inMessage = new IVDMessage(inBytes);
		String jobNo = SimulatorUtils.convertData(inMessage, IVDFieldTag.JOB_NUMBER,
				String.class);
		int dispatchLevel = Integer.parseInt(SimulatorUtils.convertData(inMessage, IVDFieldTag.SOURCE, String.class));
		IVDMessageContent message = new IVDMessageContent();
		SimulatorUtils.putItemIntoIvdMessage(jobNo, IVDFieldTag.JOB_NUMBER,
				message);
		SimulatorUtils.putItemIntoIvdMessage(dispatchLevel, IVDFieldTag.SOURCE,
				message);
		byte[] outContent = message.toHexBytes();
		
		//combine the header,content,ip
		byte[] outBytes = BytesUtil.joinBytes(outHeader, outContent);
		byte[] ipBytes = SimulatorUtils.getIPBytes(inBytes);
		byte[] bytesOutWithIp = BytesUtil.joinBytes(outBytes, ipBytes);
		
		log.info("New ConfirmAck bytesOutWithIp:" + BytesUtil.toString(bytesOutWithIp));
		return bytesOutWithIp;
	}
	
	/**
	 * simulate New MyWorkFlowResp
	 * @param inBytes
	 */
	public byte[] myWorkFlowSimulator(byte[] inBytes){
		double[] locations = LocationUtils.getRandomPosition();
		double vehX =locations[1];
		double vehY =locations[0];
		int nOffsetY = (int) ((vehX - 1) * 100000);
		int nOffsetX = (int) ((vehY - 103.55) * 100000);
		
		//compose the header
		byte[] bytes = new byte[100];
		Arrays.fill(bytes, (byte)0);
		bytes[0] = (byte) 151;
		bytes[2] = inBytes[2];
		bytes[3] = inBytes[3];
		bytes[4] = (byte) nOffsetY;
		bytes[5] = (byte) (nOffsetY >> 8);
		bytes[6] = (byte) nOffsetX;
		bytes[7] = (byte) (nOffsetX >> 8);
		byte[] outHeader = Arrays.copyOf(bytes, 17);
		SimulatorUtils.fillTimestamp(outHeader, System.currentTimeMillis(), 12);
		
		//compose content of outBytes
		IVDMessage inMessage = new IVDMessage(inBytes);
		String jobNo = SimulatorUtils.convertData(inMessage, IVDFieldTag.JOB_NUMBER,
				String.class);
		int dispatchLevel = Integer.parseInt(SimulatorUtils.convertData(inMessage, IVDFieldTag.SOURCE, String.class));
		IVDMessageContent message = new IVDMessageContent();
		SimulatorUtils.putItemIntoIvdMessage(jobNo, IVDFieldTag.JOB_NUMBER,
				message);
		SimulatorUtils.putItemIntoIvdMessage(dispatchLevel, IVDFieldTag.SOURCE,
				message);
		int [] arr = {0,1};
		int index = (int)(Math.random()*arr.length);
		SimulatorUtils.putItemIntoIvdMessage(index, IVDFieldTag.CONVERT_STREET_HAIL_RESPONSE,
				message);
		
		byte[] outContent = message.toHexBytes();
		
		//combine the header,content,ip
		byte[] outBytes = BytesUtil.joinBytes(outHeader, outContent);
		byte[] ipBytes = SimulatorUtils.getIPBytes(inBytes);
		byte[] bytesOutWithIp = BytesUtil.joinBytes(outBytes, ipBytes);
		
		log.info("New MyWorkFlowResp bytesOutWithIp:" + BytesUtil.toString(bytesOutWithIp));
		return bytesOutWithIp;
	}
	
	
	
	
/***************************************************************************************************************/	
	
	
	public void dispatchMessageFromQ(Exchange exchange){
		GenericVO vo = (GenericVO) exchange.getIn().getBody();
		byte[] inBytes = vo.getByteArray();
		int nMsgId = (char) (inBytes[0] & 0xFF);
		log.info("msgId："+nMsgId+"#####:"+BytesUtil.toString(inBytes));
		if(nMsgId == 10){                     //JobOffer
			int [] arr = {0,1,2,3,4};
			int index=(int)(Math.random()*arr.length);
			int rand = arr[index];
			if(rand != 1){//JobAccept
				//delay 2 second
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				byte[] aceeptBytes = jobAcceptSimulator(inBytes);
				exchange.getIn().setBody(aceeptBytes);
			}else{//JobReject
				byte[] rejectBytes = jobRejectSimulator(inBytes);
				exchange.getIn().setBody(rejectBytes);
			}
		}else if(nMsgId == 11){
			byte[] confirmAckBytes = jobConfirmAckSimulator(inBytes);
			exchange.getIn().setBody(confirmAckBytes);
		}else{
			log.info("Old Not handing msgId:" + nMsgId);
			exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
		}
	}
	
	/**
	 * simulate old JobAccept
	 * @param inBytes
	 */
	public byte[] jobAcceptSimulator(byte[] inBytes){
		double[] locations = LocationUtils.getRandomPosition();
		double vehX =locations[1];
		double vehY =locations[0];
		int nOffsetY = (int) ((vehX - 1) * 100000);
		int nOffsetX = (int) ((vehY - 103.55) * 100000);
		
		//compose 
		byte[] bytes = new byte[100];
		Arrays.fill(bytes, (byte)0);
		bytes[0] = (byte) 154;
		bytes[2] = inBytes[2];
		bytes[3] = inBytes[3];
		bytes[4] = (byte) nOffsetY;
		bytes[5] = (byte) (nOffsetY >> 8);
		bytes[6] = (byte) nOffsetX;
		bytes[7] = (byte) (nOffsetX >> 8);
		byte[] outBytes = Arrays.copyOf(bytes, 22);
		outBytes[12] = (byte) (((inBytes[4] << 4)& 0x0F) | ((inBytes[4] >> 4)& 0x0F));
		SimulatorUtils.fillTimestamp(outBytes, System.currentTimeMillis(), 13);
		outBytes[17] = inBytes[5];
		outBytes[18] = inBytes[6];
		outBytes[19] = inBytes[7];
		outBytes[20] = inBytes[8];
		//randomly get ETA
		int [] arr = {2,4,6,8};
		int index=(int)(Math.random()*arr.length);
		int ETA = arr[index];
		outBytes[21] = (byte)ETA;//ETA
		
		byte[] ipBytes = SimulatorUtils.getIPBytes(inBytes);
		byte[] bytesOutWithIp = BytesUtil.joinBytes(outBytes, ipBytes);
		log.info("Old Accept bytesOutWithIp:"+BytesUtil.toString(bytesOutWithIp));
		return bytesOutWithIp;
	}
	
	/**
	 * simulate old JobRject
	 * @param inBytes
	 */
	public byte[] jobRejectSimulator(byte[] inBytes){
		double[] locations = LocationUtils.getRandomPosition();
		double vehX =locations[1];
		double vehY =locations[0];
		int nOffsetY = (int) ((vehX - 1) * 100000);
		int nOffsetX = (int) ((vehY - 103.55) * 100000);
		
		//compose 
		byte[] bytes = new byte[100];
		Arrays.fill(bytes, (byte)0);
		bytes[0] = (byte) 155;
		bytes[2] = inBytes[2];
		bytes[3] = inBytes[3];
		bytes[4] = (byte) nOffsetY;
		bytes[5] = (byte) (nOffsetY >> 8);
		bytes[6] = (byte) nOffsetX;
		bytes[7] = (byte) (nOffsetX >> 8);
		byte[] outBytes = Arrays.copyOf(bytes, 22);
		
		outBytes[12] = (byte) (((inBytes[4] << 4)& 0x0F) | ((inBytes[4] >> 4)& 0x0F));//dispatchLevel
		SimulatorUtils.fillTimestamp(outBytes, System.currentTimeMillis(), 13);
		outBytes[17] = inBytes[5];
		outBytes[18] = inBytes[6];
		outBytes[19] = inBytes[7];
		outBytes[20] = inBytes[8];
		int [] arr = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14};
		int index=(int)(Math.random()*arr.length);
		int rejectCode = arr[index];
		outBytes[21] = (byte)rejectCode;//reject reason
		
		byte[] ipBytes = SimulatorUtils.getIPBytes(inBytes);
		byte[] bytesOutWithIp = BytesUtil.joinBytes(outBytes, ipBytes);
		log.info("Old Reject bytesOutWithIp:"+BytesUtil.toString(bytesOutWithIp));
		return bytesOutWithIp;
	}
	
	/**
	 * simulate old JobConfirmAck
	 * @param inBytes
	 */
	public byte[] jobConfirmAckSimulator(byte[] inBytes){
		double[] locations = LocationUtils.getRandomPosition();
		double vehX =locations[1];
		double vehY =locations[0];
		int nOffsetY = (int) ((vehX - 1) * 100000);
		int nOffsetX = (int) ((vehY - 103.55) * 100000);
		
		//compose 
		byte[] bytes = new byte[100];
		Arrays.fill(bytes, (byte)0);
		bytes[0] = (byte) 188;
		bytes[2] = inBytes[2];
		bytes[3] = inBytes[3];
		bytes[4] = (byte) nOffsetY;
		bytes[5] = (byte) (nOffsetY >> 8);
		bytes[6] = (byte) nOffsetX;
		bytes[7] = (byte) (nOffsetX >> 8);
		byte[] outBytes = Arrays.copyOf(bytes, 17);
		outBytes[12] = (byte) (((inBytes[4] << 4)& 0x0F) | ((inBytes[4] >> 4)& 0x0F));
		outBytes[13] = inBytes[5];
		outBytes[14] = inBytes[6];
		outBytes[15] = inBytes[7];
		outBytes[16] = inBytes[8];
		
		byte[] ipBytes = SimulatorUtils.getIPBytes(inBytes);
		byte[] bytesOutWithIp = BytesUtil.joinBytes(outBytes, ipBytes);
		log.info("Old ConfirmAck bytesOutWithIp:"+BytesUtil.toString(bytesOutWithIp));
		return bytesOutWithIp;
	}
	public static void main(String[] args){
		String strHex = "0A 52 18 65 F0 70 15 FC 11 E0 07 3B 94 21 58 DA 93 56 6F 01 D0 00 6A 33 ";
		byte[] bytes = BytesUtil.toBytes(strHex);
		log.info("Before process:"+BytesUtil.toString(bytes));
		bytes[4] = (byte) (((bytes[4] << 4)& 0x0F) | ((bytes[4] >> 4)& 0x0F));
		log.info("After process:"+BytesUtil.toString(bytes));
	}
}
