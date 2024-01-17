package com.cdg.ngp.esb.ms.util;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**  Class name : HttpClientUtil.java
 * This class handles the http request
 * @author Yuvarani
 */
public class HttpClientUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);
	private HttpClient httpClient;
	protected static final String ACCEPT = "*/*";
	protected static final String LANGUAGE = "en-US,en;q=0.5";
	protected static final String KEEPALIVE = "keep-alive";
	protected static final String XMLREQUEST = "XMLHttpRequest";
	protected static final int STATUS_CODE = 200;
	public static final int CONNECT_TIMEOUT_MS = 28000;
	public static final int CONNECTION_REQUEST_TIMEOUT_MS = 28000;
	public static final int SOCKET_TIMEOUT_MS = 28000;
	
		
	/** Sends http request 
	 * @param url
	 * @param paramEntity
	 * @return responseString
	 * @throws IOException 
	 */
	public String httpPost(String url, HttpEntity paramEntity) throws IOException {
		String responseString = "";
		CloseableHttpClient client = null;        
        CloseableHttpResponse response = null;  
        
        try {
            HttpPost request = new HttpPost(url);            
            request.setHeader("Accept", ACCEPT);
            request.setHeader("Accept-Language", LANGUAGE);
            request.setHeader("Connection", KEEPALIVE);
            request.setHeader("Content-Type", "text/plain");
			request.setEntity(paramEntity);
            
	        RequestConfig.Builder builder = RequestConfig.custom()
	                .setConnectTimeout(CONNECT_TIMEOUT_MS)
				    .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT_MS)
				    .setSocketTimeout(SOCKET_TIMEOUT_MS);	        
	        RequestConfig config = builder.build();	        
        	client = HttpClients.custom().setDefaultRequestConfig(config).build();            
        	response = client.execute(request);     
        	
        	if(response != null && response.getStatusLine().getStatusCode() == STATUS_CODE){
				responseString = response.getEntity() != null ? EntityUtils.toString(response.getEntity()) : null;
			}
        	
        } catch(ClientProtocolException ec){
			LOGGER.error("Get the client protocol exception ",ec);
		} catch(SocketTimeoutException et){
			LOGGER.error("Get the timeout exception ",et);
		} catch(Exception e){
			LOGGER.error("Occur Exception on httpPost",e);
		} finally {
        	if (response != null) {
        		response.close();
        	}        	
        	if (client != null) {
        		client.close();
        	}
        }
        return responseString;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
	
}
