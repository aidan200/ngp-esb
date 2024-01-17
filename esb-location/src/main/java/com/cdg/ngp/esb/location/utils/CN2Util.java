//This Class created as a replacement of DispatchUtil.java in CN2. Currently reused few methods of that class.
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
package com.cdg.ngp.esb.location.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @class Name: CN2Util.java
 * @Description :This Class created as a replacement of DispatchUtil.java in CN2. Currently reused few methods of that class.
 * 
 */
public class CN2Util {
	public static final Logger LOGGER = LoggerFactory.getLogger(CN2Util.class);
	
	public static final String defaultVehAtt = "00000000000000000000000000000000";
	public static final String defaultVehGrpAtt = "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
	public static final String defaultDrvAtt = "00000000000000000000000000000000";
	public static final String defaultDrvPdtAtt = "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
	
	
	/**
	 * 
	 * @param i
	 * @param length
	 * @return
	 */
	public static String intToBinary(int i,int length) {
		if(length<1)
			length=1;//don't modify Binary value;
		return String.format("%0"+length+"d",Integer.parseInt(Integer.toBinaryString(i)));
		
	}
	/**
     * @method Name : getVehAtt
     * @param : vehAtt
     * @return : int
     */
	public static int[] getVehAtt(String vehAtt) {
		  return getAttHex(vehAtt == "" ? defaultVehAtt : vehAtt);
	  }
	/**
     * @method Name : getAttHex
     * @param : att
     * @return : int
     */
	  public static int[] getAttHex(String att) {
		  int[] t = new int[8];
		  for (int i = 0; i < 8; i++) {
			  t[i] = Integer.parseInt(att.substring(i * 4, i * 4 + 4), 16);
		  }
		  
		  return t;
	  }
	  /**
	     * @method Name : getAttBin
	     * @param : att
	     * @return : int
	     */
	  private static int[] getAttBin(String att) {
		  int[] t = new int[8];
		  for (int i = 0; i < 8; i++) {
			  t[i] = Integer.parseInt(att.substring(i * 16, i * 16 + 16), 2);
		  }
		  
		  return t;
	  }
	  /**
	     * @method Name : getVehGrpAtt
	     * @param : vehGrpAtt
	     * @return : int
	     */
	  public static int[] getVehGrpAtt(String vehGrpAtt) {
		  return getAttBin(vehGrpAtt == "" ? defaultVehGrpAtt : vehGrpAtt);
	  }
	  /**
	     * @method Name : getDrvAtt
	     * @param : drvAtt
	     * @return : int
	     */
	  public static int[] getDrvAtt(String drvAtt) {
		  return getAttHex(drvAtt == "" ? defaultDrvAtt : drvAtt);
	  }
	  /**
	     * @method Name : getDrvPdtAtt
	     * @param : drvPdtAtt
	     * @return : int
	     */
	  public static int[] getDrvPdtAtt(String drvPdtAtt) {
		  return getAttBin(drvPdtAtt == "" ? defaultDrvPdtAtt : drvPdtAtt);
	  }
	  /**
	     * @method Name : convStringToLong
	     * @param : str
	     * @return : Long
	     */
	  public static Long convStringToLong(Object str)
	  {
		  String str1 = setDefaultValueForNull(str);
		  return Long.parseLong(str1);
		  
	  }
	  /**
	     * @method Name : convStringToInteger
	     * @param : str
	     * @return : Integer
	     */
	  public static Integer convStringToInteger(Object str)
	  {
		  String str1 = setDefaultValueForNull(str);
		  return Integer.parseInt(str1);
	  }
	  /**
	     * @method Name : convStringToDouble
	     * @param : str
	     * @return : Double
	     */
	  public static Double convStringToDouble(Object str)
	  {
		  String str1 = setDefaultValueForNull(str);
		  return Double.parseDouble(str1);
	  }
	  /**
	     * @method Name : convStringToBoolean
	     * @param : str
	     * @return : Boolean
	     */
	  public static Boolean convStringToBoolean(Object str)
	  {
		  String str1 = isNullCheck(str);
		  boolean flag = false;
		  if(str1.equalsIgnoreCase("1"))
		  {
		  	  flag = true;
		  }
		  return flag;
	  }
	  /**
	     * @method Name : isNullCheck
	     * @param : str
	     * @return : String
	     */
	  public static String isNullCheck(Object str)
	  {
		  if(null != str)
		  {
			  return str.toString();
		  }
		  else
			  return "";
	  }
	  /**
	     * @method Name : setDefaultValueForNull
	     * @param : str
	     * @return : String
	     */
	  public static String setDefaultValueForNull(Object str)
	  {
		  if(null != str)
		  {
			  return str.toString();
		  }
		  else
			  return "0";
	  }
	  /**
	     * @method Name : formatDate
	     * @param : date
	     * @param : format
	     * @return : Long
	     */
	  //Method to convert Date to Long Milliseconds
	  public static Long formatDate(String date, String format )
	  {
		  SimpleDateFormat f = new SimpleDateFormat(format);
		  Date d = null;
		  try {
				d = f.parse(CN2Util.isNullCheck(date));
		  } catch (ParseException e) {
				LOGGER.error("Exception while parsing", e);
				d=new Date();
		  }
		  return d.getTime();
	  }
	  /**
	     * @method Name : formatDate
	     * @param : date
	     * @return : Long
	     */
	//Method to convert Date to Long Milliseconds
	  public static Long formatDate(String date)
	  {
		 return new Date(date).getTime();
		  
	  }
	  /**
	     * @method Name : prepareSearchString
	     * @param : v
	     * @return : String
	     */
	  public static String prepareSearchString(Vector<String> v)
	  {
		  String madeUpString = "";
		  for(String str : v)
		  {
			  madeUpString = madeUpString + "'"+str+"',";
		  }
		  String finalString = madeUpString.substring(0,madeUpString.length()-1 );
		  return finalString;
		  
		  
	  }
	 
	  public static SimpleDateFormat sdf = new SimpleDateFormat("DD-MM-YYYY hh:mm:ss.S");
	  /**
	     * @method Name : formatLongtoDate
	     * @param :timeMilliSec
	     * @return : String
	     */
	  public static String formatLongtoDate(Long timeMilliSec)
	  {
		  Date currentDate = new Date(timeMilliSec);
		  DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
		  return df.format(currentDate);
		  
		  
	  }
	  /**
	   	 * @method :  fixZeroesInFront
		 * @Description Insert "0" in front of numbers to make it fixed length
		 * @param nValue         Number to be fixed with "0" in front
		 * @param nTotalLen      Total length after inserting "0"
		 * @return Converted number as String
		 */
		public static String fixZeroesInFront(int nValue, int nTotalLen) {
			String tmp = "";
			String strTemp = String.valueOf(nValue);
			int nStrLen = strTemp.length();
			for (int i = 0; i < (nTotalLen - nStrLen); i++) {
				tmp += "0";
			}
			return tmp + strTemp;
		}
		/**
	     * @method Name : convIntegerToString
	     * @param :i
	     * @return : String
	     */
	  
		public String convIntegerToString(int i)
		{
				return Integer.toString(i);
				
		}
		/**
	     * @method Name : convDoubleToBigDecimal
	     * @param :d
	     * @return : BigDecimal
	     */
		public static BigDecimal convDoubleToBigDecimal(double d)
		{
			return BigDecimal.valueOf(d);
			
		}

}
