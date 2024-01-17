package com.cdg.ngp.esb.location.utils;

import java.net.SocketTimeoutException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

public class HttpClientUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);
	private CookieStore httpCookieStor;
	private HttpClient client;
	protected final String ACCEPT = "*/*";
	protected final String LANGUAGE = "en-US,en;q=0.5";
	protected final String KEEPALIVE = "keep-alive";
	protected final String XMLREQUEST = "XMLHttpRequest";
	protected final int STATUS_CODE = 200;
	protected final int FINAL_TIMEOUT_IN_SECONDS = 20000;


	public void init(){
		try{
			client = createHttpClient();
		}catch(Exception e){
			LOGGER.error("Error on init http client ",e);
		}
	}
	
	private HttpClient createHttpClient() {
	    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
	    cm.setMaxTotal(128);
	    cm.setDefaultMaxPerRoute(24);
	    RequestConfig.Builder requestBuilder = RequestConfig.custom();
	    requestBuilder.setConnectTimeout(FINAL_TIMEOUT_IN_SECONDS);
	    requestBuilder.setConnectionRequestTimeout(FINAL_TIMEOUT_IN_SECONDS);
	    requestBuilder.setSocketTimeout(FINAL_TIMEOUT_IN_SECONDS);

	    HttpClientBuilder builder = HttpClientBuilder.create();
	    httpCookieStor = new BasicCookieStore();
	    builder.setDefaultCookieStore(httpCookieStor);
	    builder.setRedirectStrategy(new LaxRedirectStrategy());
	    builder.setDefaultRequestConfig(requestBuilder.build());
	    builder.setConnectionManager(cm);
	    return builder.build();
	}
	
	public String httpGet(String authenKey, String parameter, String url, String cookie){
		String responseString = new String();
		HttpResponse response = null;
		try{
			HttpGet httpgetRequest = new HttpGet(String.format("%s?%s", url, parameter));
			httpgetRequest.setHeader("Accept", ACCEPT);
			httpgetRequest.setHeader("Accept-Language", LANGUAGE);
			httpgetRequest.setHeader("Connection", KEEPALIVE);
			httpgetRequest.setHeader("X-Requested-With", XMLREQUEST);
			httpgetRequest.setHeader("Authorization", authenKey);
			if(StringUtils.isNotBlank(cookie))
				httpgetRequest.setHeader("Cookie", cookie);
			response = client.execute(httpgetRequest);
			if(response != null){
				if(response.getStatusLine().getStatusCode() == STATUS_CODE){
					responseString = EntityUtils.toString(response.getEntity());
				}else{
					responseString = EntityUtils.toString(response.getEntity());
					LOGGER.debug(responseString);
				}
			}
		}catch(ClientProtocolException ec){
			LOGGER.error("Get the client protocol exception ",ec);
		}catch(SocketTimeoutException et){
			LOGGER.error("Get the timeout exception ",et);
		}catch(Exception e){
			LOGGER.error("Occur Exception on httpGet",e);
		}
		
		return responseString;
	}
	
	public String httpPost(String authenKey, String url, String cookie, HttpEntity paramEntity){
		String responseString = new String();
		HttpResponse response = null;
		try{
			HttpPost httpgetRequest = new HttpPost(url);
			httpgetRequest.setHeader("Accept", ACCEPT);
			httpgetRequest.setHeader("Accept-Language", LANGUAGE);
			httpgetRequest.setHeader("Connection", KEEPALIVE);
			httpgetRequest.setHeader("X-Requested-With", XMLREQUEST);
			httpgetRequest.setHeader("Authorization", authenKey);
			if(StringUtils.isNotBlank(cookie))
				httpgetRequest.setHeader("Cookie", cookie);
			httpgetRequest.setEntity(paramEntity);
			response = client.execute(httpgetRequest);
			
			if(response != null){
				if(response.getStatusLine().getStatusCode() == STATUS_CODE){
					responseString = EntityUtils.toString(response.getEntity());
				}else{
					responseString = EntityUtils.toString(response.getEntity());
					LOGGER.debug(responseString);
				}
			}
		}catch(ClientProtocolException ec){
			LOGGER.error("Get the client protocol exception ",ec);
		}catch(SocketTimeoutException et){
			LOGGER.error("Get the timeout exception ",et);
		}catch(Exception e){
			LOGGER.error("Occur Exception on httpPost",e);
		}
		
		return responseString;
	}
	
	
	public HttpClient getClient() {
		return client;
	}
	
	public void setClient(HttpClient client) {
		this.client = client;
	}
}
