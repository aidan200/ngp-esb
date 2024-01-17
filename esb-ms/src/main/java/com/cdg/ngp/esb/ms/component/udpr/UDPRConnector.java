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

package com.cdg.ngp.esb.ms.component.udpr;

import java.io.ByteArrayOutputStream;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cdg.ngp.esb.ms.component.common.Signal;
/**
 * @Class name : UDPRConnector.java
 **/
public abstract class UDPRConnector {
	private static final Logger log = LoggerFactory.getLogger(UDPRConnector.class);

	public final int DEFAULT_BUFFER_SIZE = 1024;

    public final long RECONNECT_DELAY = 5000L;
    
    private String serverIP;
    private int port;
    
    private DatagramSocket serverSocket;
    private ReceiveThread receiveT;
    private byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

    private boolean started;
    private Signal reconnectLock = new Signal();
    
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
	
	protected abstract void ReceiveCallback(Object msg) throws Exception;
	protected abstract boolean ReconnectCallback();
	protected abstract void DisconnectCallback();
	protected abstract void ExceptionCallback(UDPRConnectorException e);
	/** 
   	 * @Method name : UDPRConnector
   	 * @param serverIP
   	 * @param port
   	 * @return void
   	 **/  
    public UDPRConnector(String serverIP, int port)
    {
        this.serverIP = serverIP;
        this.port = port;
        this.started = false;
    }
	/** 
   	 * @Method name : Start
   	 * @return void
   	 **/ 
    public final void Start() throws UDPRConnectorException
    {
		if (!started) {
	        started = true;
	
	        try {
	        	serverSocket = null;

		        receiveT = new ReceiveThread();
		        receiveT.start();
	        }
	        catch (Exception e)
	        {
	        	log.debug(ExceptionUtils.getStackTrace(e));
	        	
	        	Shutdown();
	            
            	throw new UDPRConnectorException(e);
	        }
		}
    }
	/** 
   	 * @Method name : Shutdown
   	 * @return void
   	 **/ 
    public final void Shutdown()
    {
    	if (started) {
	        started = false;
	        
			try {receiveT.stopConnection();} catch (Exception e) {log.info("Caught an exception :" + e);}
		    try {receiveT.interrupt();} catch (Exception e) {log.info("Caught an exception :" + e);}
	        
	    	try {reconnectLock.waitUntilEQ(0,0);} catch (Exception e) {log.info("Caught an exception :" + e);}
    	}
    }
	/** 
   	 * @class name : ReceiveThread
   	 * @return void
   	 **/  
    class ReceiveThread extends Thread {
    	public ReceiveThread() {
    		setDaemon(true);
    	}
    	/** 
       	 * @Method name : run
       	 * @return void
       	 **/
    	public void run() {
    		reconnectLock.increment();
    		
            while (started)
            {
            	try {
            		serverSocket = new DatagramSocket(port,InetAddress.getByName(serverIP));
    				DatagramPacket receivePacket = new DatagramPacket(buffer,buffer.length);
		
	                while (started)
	                {
	                	serverSocket.receive(receivePacket);
		    					
    					// Absorb any exceptions generated in user processes
						try {
							ReceiveCallback(receivePacket);
						} catch (Exception e) {
							ExceptionCallback(new UDPRConnectorException(e));
						}
	                }
	            }
	            catch (Exception e) {
	            	log.debug(ExceptionUtils.getStackTrace(e));
	            }

            	stopConnection();
		            
    		    if (started && ReconnectCallback()) {
    		    	try {Thread.sleep(RECONNECT_DELAY);} catch (Exception ee) {log.info("Caught an exception :" + ee);}
    		    }
    		    else {
                	DisconnectCallback();        
                	break;
                }
	    	}

            reconnectLock.decrementAndNotify();
    	}
    	/** 
       	 * @Method name : stopConnection
       	 * @return void
       	 **/
    	public void stopConnection() {
            try {serverSocket.close();} catch (Exception e) {log.info("Caught an exception :" + e);}
            serverSocket = null;
    	}
    }

//    public static void main(String args[]) throws Exception {
//    	System.out.println("Starting up connector...");
//    	   	
//    	UDPRConnector connector = new UDPRConnector("localhost",8890) {
//			@Override
//			protected void ReceiveCallback(Object msg) {
//				DatagramPacket receivePacket = (DatagramPacket)msg;
//				byte[] receivedData = ArrayUtils.subarray(receivePacket.getData(),0,receivePacket.getLength());
//				SocketAddress address = receivePacket.getSocketAddress();
//				String s = new String(receivedData);
//				System.out.println("Received : ->"+s+"<- with length "+s.length()+" from "+address);
//			}
//			@Override
//			protected boolean ReconnectCallback() {
//				return true;
//			}
//			@Override
//			protected void DisconnectCallback() {
//				System.out.println("Disconnected!");
//			}
//			@Override
//			protected void ExceptionCallback(UDPRConnectorException e) {
//				System.out.println(e);
//			}
//    	};
//    	connector.Start();
//    	
//    	java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
//    	
//    	while (true) {
//	    	System.out.println("PLEASE ENTER NOTHING TO EXIT : ");
//	    	String s = br.readLine();
//	    	if (s.length()==0)
//	    		break;
//    	}
//    	
//    	br.close();
//    	
//    	System.out.println("Shutting down connector...");
//    	
//    	connector.Shutdown();
//    }
}
