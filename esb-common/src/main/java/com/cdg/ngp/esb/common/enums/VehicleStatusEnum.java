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
package com.cdg.ngp.esb.common.enums;

import java.util.HashMap;
import java.util.Map;
/** 
 * @method name : VehicleStatusEnum
 **/
public enum VehicleStatusEnum {
	VEH_FREE(0,"free"),
	VEH_BUSY(1,"busy"),
	VEH_BREAK(2,"break"),

	VEH_STC(3,"STC"),

	VEH_ONCALL(4,"oncall"),

	VEH_POB(5,"POB"),

	VEH_ARRIVED(6,"arrived"),

	VEH_NOSHOW(7,"noshow"),

	VEH_PAYMENT(8,"payment"),

	VEH_FORWARD(9,"forward"),

	VEH_POWEROFF(10,"poweroff"),

	VEH_OFFLINE(11,"offline"),

	VEH_OFFERED(12,"offered"),

	VEH_QUERY(13,"query"),

	VEH_SUSPEND(14,"suspend"),

	VEH_SUSPEND_ACK(15,"suspendAck"),

	VEH_SUSPEND_NO_ACK(16,"suspendNA");

	private final int vehStatusNo;
	private final String vehStatusLabel;
	
	
	private static Map<Integer, VehicleStatusEnum> vehStatusNoMap = new HashMap<Integer, VehicleStatusEnum>();

    static {
        for (VehicleStatusEnum vehicleStatusEnum : VehicleStatusEnum.values()) {
        	vehStatusNoMap.put(vehicleStatusEnum.vehStatusNo, vehicleStatusEnum);
        }
    }
	
	
	VehicleStatusEnum(int vehStatusNo, String vehStatusLabel){
		this.vehStatusNo=vehStatusNo;
		this.vehStatusLabel=vehStatusLabel;
	}
	/** 
	 * @method name : getEnumByVehStatusNo
	 * @param : vehStatusNo
	 * 
	 **/
	public static VehicleStatusEnum getEnumByVehStatusNo(int vehStatusNo){
		return vehStatusNoMap.get(Integer.valueOf(vehStatusNo));
	}
	/** 
	 * @method name : getVehStatusNo
	 * @param : vehStatusNo
	 * 
	 **/
	public int getVehStatusNo() {
		return vehStatusNo;
	}
	/** 
	 * @method name : getVehStatusLabel
	 * @param : vehStatusLabel
	 * 
	 **/
	public String getVehStatusLabel() {
		return vehStatusLabel;
	}
	
}
