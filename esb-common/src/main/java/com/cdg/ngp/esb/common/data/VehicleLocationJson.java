/*
 * Copyright (c) 2015, COMFORT TRANSPORTATION PTE. LTD./COMFORTDELGRO PTE. LTD.
 * */
package com.cdg.ngp.esb.common.data;

/** 
 * @Class name : VehicleLocationJson.java
 * @Description :Vehicle Location Json Object
 * @Author Khoo Yong Jian
 * @Since 19 June 2017
**/
public class VehicleLocationJson {

	private String LOGDT;
	private String IVDID;
	private String DRIVERID;
	private String VEHICLEID;
	private String JOBNO;
	private Double LAT;
	private Double LONG;
	private String STATUS;
	
	public VehicleLocationJson() {
		super();
	}

	/**
	 * @return the lOGDT
	 */
	public String getLOGDT() {
		return LOGDT;
	}

	/**
	 * @param lOGDT the lOGDT to set
	 */
	public void setLOGDT(String lOGDT) {
		LOGDT = lOGDT;
	}

	/**
	 * @return the iVDID
	 */
	public String getIVDID() {
		return IVDID;
	}

	/**
	 * @param iVDID the iVDID to set
	 */
	public void setIVDID(String iVDID) {
		IVDID = iVDID;
	}

	/**
	 * @return the dRIVERID
	 */
	public String getDRIVERID() {
		return DRIVERID;
	}

	/**
	 * @param dRIVERID the dRIVERID to set
	 */
	public void setDRIVERID(String dRIVERID) {
		DRIVERID = dRIVERID;
	}

	/**
	 * @return the vEHICLEID
	 */
	public String getVEHICLEID() {
		return VEHICLEID;
	}

	/**
	 * @param vEHICLEID the vEHICLEID to set
	 */
	public void setVEHICLEID(String vEHICLEID) {
		VEHICLEID = vEHICLEID;
	}

	/**
	 * @return the jOBNO
	 */
	public String getJOBNO() {
		return JOBNO;
	}

	/**
	 * @param jOBNO the jOBNO to set
	 */
	public void setJOBNO(String jOBNO) {
		JOBNO = jOBNO;
	}

	/**
	 * @return the lAT
	 */
	public Double getLAT() {
		return LAT;
	}

	/**
	 * @param lAT the lAT to set
	 */
	public void setLAT(Double lAT) {
		LAT = lAT;
	}

	/**
	 * @return the lONG
	 */
	public Double getLONG() {
		return LONG;
	}

	/**
	 * @param lONG the lONG to set
	 */
	public void setLONG(Double lONG) {
		LONG = lONG;
	}

	/**
	 * @return the sTATUS
	 */
	public String getSTATUS() {
		return STATUS;
	}

	/**
	 * @param sTATUS the sTATUS to set
	 */
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
}
