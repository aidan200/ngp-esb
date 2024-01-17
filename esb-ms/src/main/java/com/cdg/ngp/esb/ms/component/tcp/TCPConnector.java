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

import java.io.ByteArrayOutputStream;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cdg.ngp.esb.ms.component.common.Signal;
import com.cdg.ngp.esb.ms.component.tcp.codec.DefaultCodec;
/** 
 * @Class name : UDPConnectorException.java
 * @Description It will throw Runtime Camel Exception in UDPConnector Exception 
 **/
public abstract class TCPConnector {
	private static final Logger log = LoggerFactory.getLogger(TCPConnector.class);
	
	public final int DEFAULT_BUFFER_SIZE = 1024;
    public final int CONNECT_TIMEOUT = 60000;
    public final int SOCKET_TIMEOUT = 0;

    public final long RECONNECT_DELAY = 5000L;
    
    private String serverIP;
    private int port;
    
    private TCPCodec codec = new DefaultCodec();
    
    private Socket clientSocket;
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
	/** 
	 * @Method name : getCodec
	 * @return codec
	 **/
	public TCPCodec getCodec() {
		return codec;
	}
	 /** 
	 * @Method name : setCodec
	 * @param codec
	 * @return void
	**/
	public void setCodec(TCPCodec codec) {
		this.codec = codec;
	}
	
	protected abstract void ReceiveCallback(Object msg) throws Exception;
	protected abstract boolean ReconnectCallback();
	protected abstract void DisconnectCallback();
	protected abstract void ExceptionCallback(TCPConnectorException e);
     /** 
	  * @Method name : Send
	  * @param msg
	  * @return void
	  **/
    final public void Send(Object msg) throws TCPConnectorException {
    	if ((!started)||(clientSocket==null)||(!clientSocket.isConnected()))
    		throw new TCPConnectorException("Not connected!");
    	
    	try {
    		clientSocket.getOutputStream().write(codec.BytesToSend(msg));
    	}
    	catch (Exception e) {
    		throw new TCPConnectorException(e);
    	}
    }
    /** 
	 * @Method name : TCPConnector
	 * @param serverIP 
	 * @param port
	 * @return codec
	 **/
    public TCPConnector(String serverIP, int port)
    {
        this.serverIP = serverIP;
        this.port = port;

        this.started = false;
        
        log.info("TCPConnector Client Socket constructor:: serverIP :: {}, port :: {}",serverIP,port);
    }
    /** 
   	 * @Method name : Start
   	 * @return void
   	 **/
    public final void Start() throws TCPConnectorException
    {
		if (!started) {
	        started = true;
	
	        try {
	        	clientSocket = null;

		        receiveT = new ReceiveThread();
		        receiveT.start();
	        }
	        catch (Exception e)
	        {
	        	log.error(ExceptionUtils.getStackTrace(e));
	        	
	        	Shutdown();
	            
            	throw new TCPConnectorException(e);
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
    		log.info("TCPConnector Client Socket details :: serverIP :: {}, port :: {}",serverIP,port);
            while (started)
            {
            	try {
			        clientSocket = new Socket();
			        clientSocket.connect(new InetSocketAddress(serverIP,port),CONNECT_TIMEOUT);
			        clientSocket.setSoTimeout(SOCKET_TIMEOUT);
			        log.info("TCPConnector Client Socket :: {}",clientSocket);
	            	ByteArrayOutputStream bOS = new ByteArrayOutputStream();
	            	
	                while (started)
	                {
	    				int len = clientSocket.getInputStream().read(buffer);
	    				bOS.write(buffer,0,len);
	    				
	    				// There is a loop here so that Receive is called repeatedly until outputMessages
	    				// can no longer be formed from the received bytes.
	    				Object[] result = null;
	    				do {
		    				byte[] inputBytes = bOS.toByteArray();
		    				try {
		    					result = codec.Receive(inputBytes);
		    				}
		    				catch (Exception e) {
		    					ExceptionCallback(new TCPConnectorException(e));
		    					
		    					throw e;
		    				}
		    				
		    				if (result!=null) {
		    					Object outputMessage = result[0];
		    					int usedLength = (Integer)result[1];
		    					
		    					// Absorb any exceptions generated in user processes
		    					if (outputMessage!=null)
		    						try {
		    							ReceiveCallback(outputMessage);
		    						} catch (Exception e) {
		    							ExceptionCallback(new TCPConnectorException(e));
		    						}
		    					
		    					bOS.reset();
		    					bOS.write(inputBytes,usedLength,inputBytes.length-usedLength);
		    				}
	    				}
	    				while (result!=null);
	                }
	            }
	            catch (Exception e) {
	            	log.error(ExceptionUtils.getStackTrace(e));
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
            try {clientSocket.close();} catch (Exception e) {log.info("Caught an exception :" + e);}
            clientSocket = null;
    	}
    }

//    public static void main(String args[]) throws Exception {
//    	System.out.println("Starting up connector...");
//    	
//    	TCPCodec codec = new TCPCodec() {
//			@Override
//			public Object[] Receive(byte[] bytes) throws Exception {
//				int size = bytes.length;
//				if (size<2)
//					return null;
//				int msgSize = 2+bytes[0]+(bytes[1]<<8);
//				if (size<msgSize)
//					return null;
//				return new Object[]{ArrayUtils.subarray(bytes,0,msgSize),msgSize};
//			}
//			@Override
//			public byte[] BytesToSend(Object msg) throws Exception {
//				byte[] sBytes = msg.toString().getBytes();
//				int size = sBytes.length;
//				byte[] sizeBytes = new byte[]{(byte)(size&255),(byte)(size>>8)};
//				return ArrayUtils.addAll(sizeBytes,sBytes);
//			}
//    	};
//    	
//    	TCPConnector connector = new TCPConnector("localhost",8890) {
//			@Override
//			protected void ReceiveCallback(Object msg) {
//				byte[] msgBytes = (byte[])msg;
//				String s = new String(ArrayUtils.subarray(msgBytes,2,msgBytes.length));
//				System.out.println("Received : ->"+s+"<- with length "+s.length());
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
//			protected void ExceptionCallback(TCPConnectorException e) {
//				System.out.println(e);
//			}
//    	};
//    	connector.setCodec(codec);
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
//	    	connector.Send(s);
//    	}
//    	
//    	br.close();
//    	
//    	System.out.println("Shutting down connector...");
//    	
//    	connector.Shutdown();
//    }
}
