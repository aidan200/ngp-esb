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
package com.cdg.ngp.esb.common.data;

import java.io.Serializable;
import java.util.Arrays;

/** 
 * @Class name : MessageContent.java
 * @Description :location message content object
 * @Author Zhao Zilong
 * @Since 12 Jan 2016
**/
public class MessageContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String vehicleID;

    private String driverID;

    private byte[] headerInfo;

    public byte[] getHeaderInfo() {
        return headerInfo;
    }

    public void setHeaderInfo(byte[] headerInfo) {
        this.headerInfo = headerInfo;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    @Override
    public String toString() {
        return "MessageContent [vehicleID=" + vehicleID + ", driverID=" + driverID + ", headerInfo="
                + Arrays.toString(headerInfo) + "]";
    }

}