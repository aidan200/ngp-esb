package com.cdg.ngp.esb.ms.handler;

import org.apache.camel.Exchange;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cdg.ngp.esb.ms.util.HttpClientUtil;

import sg.com.cdgtaxi.comms.tlv.util.BytesUtil;

/**  Class name : AuthenticationServiceHandler.java
 * This class handle the Authentication message from MDT and forward message to AS
 * Rest API connection for Authentication Server
 * @author Yuvarani
 */

public class AuthenticationServiceRestHandler {
	
	private static final Logger LOGGER  = LoggerFactory.getLogger(AuthenticationServiceRestHandler.class);
	
	private static final String AUTH_ACK_FLAG = "AUTH_ACK_FLAG";
	
	private HttpClientUtil httpClientUtil;
    private String authServerRestApiUrl;
    private AuthenticationServiceHandler authenticationServiceHandler;	
	
	public HttpClientUtil getHttpClientUtil() {
		return httpClientUtil;
	}
	public void setHttpClientUtil(HttpClientUtil httpClientUtil) {
		this.httpClientUtil = httpClientUtil;
	}
	public String getAuthServerRestApiUrl() {
		return authServerRestApiUrl;
	}
	public void setAuthServerRestApiUrl(String authServerRestApiUrl) {
		this.authServerRestApiUrl = authServerRestApiUrl;
	}	
	public AuthenticationServiceHandler getAuthenticationServiceHandler() {
		return authenticationServiceHandler;
	}
	public void setAuthenticationServiceHandler(AuthenticationServiceHandler authenticationServiceHandler) {
		this.authenticationServiceHandler = authenticationServiceHandler;
	}
	/**
	 * TOC - 1154 AS Rest Service Implementation
	 * Message from UDP
	 * @param exchange
	 */
	public void sendMessageToAuthServer(Exchange exchange) {
		//Process the request before sending to AS
		authenticationServiceHandler.prepareAuthentication(exchange); 
		byte[] msgByteToAuth = (byte[]) exchange.getIn().getBody();
		LOGGER.debug("Authenication Server REST API uri {}",authServerRestApiUrl);
    	try {
    		//Call the the Authentication rest API
    		StringEntity inputMsg = new StringEntity(BytesUtil.toString(msgByteToAuth));
    		String authResponse = httpClientUtil.httpPost(authServerRestApiUrl, inputMsg);
    		LOGGER.debug("Response from Authentication Server [{}, Length::{}]",authResponse,authResponse.length());
    		if(authResponse.length() > 0) {
    			byte[] authResponseMsgBytes = convertToBytes(authResponse);			
    			exchange.getIn().setBody(authResponseMsgBytes);
    			//Process the response received from AS			
    			authenticationServiceHandler.prepareAuthenticationResponseFromTcp(exchange); 
    			exchange.getIn().setHeader(AUTH_ACK_FLAG, "TRUE");
    			LOGGER.debug("Send Authentication message to MDT :: {}",exchange.getIn().getHeaders().entrySet());
    		} else {
    			exchange.getIn().setHeader(AUTH_ACK_FLAG, "FALSE");
    			LOGGER.debug("Authentication Response Byts doesn't meet the required format, Atleast two bytes to show the length");
    		}			
    	}catch (Exception e) {
            LOGGER.error("Exception while connecting Authentication Server:",e);
            exchange.getIn().setHeader(AUTH_ACK_FLAG, "FALSE");
            LOGGER.debug("Cannot send message to Authentication for {}",exchange.getIn().getHeaders().entrySet());
        }	        
	}
	
	/** To convert incoming Hex String message to byte array */
	private byte[] convertToBytes(String strHex) {
        String[] hexStrArr = strHex.split("\\s+");
        byte[] bytes = new byte[hexStrArr.length];

        for (int i = 0; i < hexStrArr.length; i++) {
            try {
                bytes[i] = (byte) (Integer.parseInt(hexStrArr[i], 16));
            } catch (Exception ex) {
                bytes[i] = 0x0000;
                LOGGER.error("Error in converting hex message to byte array::{}",ex);
            }
        }
        return bytes;
    }
	
}
