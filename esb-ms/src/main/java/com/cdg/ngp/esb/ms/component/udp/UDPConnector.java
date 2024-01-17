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
package com.cdg.ngp.esb.ms.component.udp;

import java.net.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Class name : UDPConnector.java
 * @Description It will throw Runtime Camel Exception in UDPConnector Exception 
 **/
public class UDPConnector {
	private static final Logger log = LoggerFactory.getLogger(UDPConnector.class);
    private DatagramSocket clientSocket;

    private boolean started;
    /** 
   	 * @Method name : isStarted
   	 * @return started
   	 **/
	public boolean isStarted() {
		return started;
	}
	 /** 
   	 * @Method name : setStarted
   	 * @param started
   	 * @return void
   	 **/
	public void setStarted(boolean started) {
		this.started = started;
	}
	 /** 
   	 * @Method name : Send
   	 * @param msg
   	 * @param destIP
   	 * @param destPort
   	 * @return void
   	 **/
    final public void Send(byte[] msg,String destIP,int destPort) throws UDPConnectorException {
    	if (!started)
    		throw new UDPConnectorException("Not connected!");
    	
    	try {
	    	InetAddress destAddress = InetAddress.getByName(destIP);
			DatagramPacket sendPacket = new DatagramPacket(msg, msg.length, destAddress, destPort);
	    	clientSocket.send(sendPacket);
    	}
    	catch (Exception e) {
    		throw new UDPConnectorException(e);
    	}
    }
	 /** 
   	 * @Method name : UDPConnector
   	 * @param msg
   	 * @param destIP
   	 * @param destPort
   	 * @return void
   	 **/
    public UDPConnector() throws UnknownHostException
    {
        this.started = false;
    }
	 /** 
   	 * @Method name : Start
  	 * @return void
   	 **/
    public final void Start() throws UDPConnectorException
    {
		if (!started) {
	        started = true;
	
	        try {
	        	clientSocket = new DatagramSocket();
	        }
	        catch (Exception e)
	        {
	            Stop();
	            
	            throw new UDPConnectorException(e);
	        }
		}
    }
    /** 
   	 * @Method name : Stop
  	 * @return void
   	 **/
    public final void Stop()
    {
        started = false;

        try {clientSocket.close();} catch (Exception e) {log.info("Caught an exception :" + e);}
    }
    
//    public static void main(String args[]) throws Exception {
//    	System.out.println("Starting up connector...");
//    	
//    	UDPConnector connector = new UDPConnector();
//    	connector.Start();
//    	
//    	java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
//    	
//    	while (true) {
//	    	System.out.println("PLEASE ENTER SOMETHING, NOTHING TO EXIT : ");
//	    	String s = br.readLine();
//	    	if (s.length()==0)
//	    		break;
//	    	
//	    	System.out.println("YOU ENTERED : ->"+s+"<- with length "+s.length());
//	    	
//	    	connector.Send(s.getBytes(),"localhost",8890);
//    	}
//    	
//    	br.close();
//    	
//    	System.out.println("Shutting down connector...");
//    	
//    	connector.Stop();
//    }
}
