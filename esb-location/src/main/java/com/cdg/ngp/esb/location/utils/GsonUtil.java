package com.cdg.ngp.esb.location.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(GsonUtil.class);

	static Gson gson;
	
	static{
		try{
			gson = new GsonBuilder().create();
		}catch(Exception e){
			LOGGER.error("Erron on init gson",e);
		}
	}
	
	public static String generateToJson(Object obj){
		String jsonString = new String();
		try{
			jsonString = gson.toJson(obj);
		}catch(Exception e){
			LOGGER.error("Error on generateToJson ",e);
		}
		return jsonString;
	}

	public static Gson getGson() {
		return gson;
	}

	public static void setGson(Gson gson) {
		GsonUtil.gson = gson;
	}
}
