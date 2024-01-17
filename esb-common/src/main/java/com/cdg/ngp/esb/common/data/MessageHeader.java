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
/** 
 * @Class name : MessageHeader.java
 * @Description :location message header object
 * @Warning: better do not change setter parameter type. because the fuse might occur some exception called "no such method found error"
 * @Author Zhao Zilong
 * @Since 12 Jan 2016
**/
package com.cdg.ngp.esb.common.data;

import java.io.Serializable;

public class MessageHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean acknowledgement;

    private boolean expressway;

    private int mobileId;

    private double xOffset;

    private double yOffset;

    private int serialNum;

    private double speed;

    private String zoneOrRank;

    private int messageType;

    private int direction;

    private long timestamp;

    private boolean suspensionStatus;

    private int taxiMeterStatus;

    private int emergencyStatus;

    private boolean gpsFixStatus;

    private int ivdStatus;

    private Integer sequence;
    /**
     * @method Name : getMessageType
     * @return messageType
     */
    public int getMessageType() {
        return messageType;
    }
    /**
     * @method Name : setMessageType
     * @param messageType
     * @return void
     */
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
    /**
     * @method Name : getMobileId
     * @return mobileId
     */
    public int getMobileId() {
        return mobileId;
    }
    /**
     * @method Name : setMobileId
     * @param mobileId
     * @return void
     */
    public void setMobileId(int mobileId) {
        this.mobileId = mobileId;
    }
    /**
     * @method Name : getxOffset
     * @return xOffset
     */
    public double getxOffset() {
        return xOffset;
    }
    /**
     * @method Name : setxOffset
     * @param xOffset
     * @return void
     */
    public void setxOffset(int xOffset) {
        //this.xOffset = ((double) xOffset) / 10000 + 103.55;
        this.xOffset = 103.55 + ((double) xOffset / 100000);
    }
    /**
     * @method Name : getyOffset
     * @return yOffset
     */
    public double getyOffset() {
        return yOffset;
    }
    /**
     * @method Name : setyOffset
     * @param yOffset
     * @return void
     */
    public void setyOffset(int yOffset) {
        //this.yOffset = ((double) yOffset) / 10000 + 1;
        this.yOffset = 1 + ((double) yOffset / 100000);
    }
    /**
     * @method Name : getSerialNum
     * @return serialNum
     */
    public int getSerialNum() {
        return serialNum;
    }
    /**
     * @method Name : setSerialNum
     * @param serialNum
     * @return void
     */
    public void setSerialNum(int serialNum) {
        this.serialNum = serialNum;
    }
    /**
     * @method Name : getSpeed
     * @return speed
     */
    public double getSpeed() {
        return speed;
    }
    /**
     * @method Name : setSpeed
     * @param speed
     * @return void
     */
    public void setSpeed(int speed) {
        this.speed = (double) speed;
    }
    /**
     * @method Name : getZoneOrRank
     * @return String
     */
    public String getZoneOrRank() {
        return zoneOrRank;
    }
    /**
     * @method Name : setZoneOrRank
     * @param zoneOrRank
     * @return void
     */
    public void setZoneOrRank(int zoneOrRank) {
        if (zoneOrRank < 10) {
            this.zoneOrRank = "0" + zoneOrRank;
        } else {
            this.zoneOrRank = zoneOrRank + "";
        }
    }
    /**
     * @method Name : getDirection
     * @return direction
     */
    public int getDirection() {
        return direction;
    }
    /**
     * @method Name : setDirection
     * @param direction
     * @return void
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }
    /**
     * @method Name : getTimestamp
     * @return timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }
    /**
     * @method Name : setTimestamp
     * @param timestamp
     * @return void
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    /**
     * @method Name : isSuspensionStatus
     * @return suspensionStatus
     */
    public boolean isSuspensionStatus() {
        return suspensionStatus;
    }
    /**
     * @method Name : setSuspensionStatus
     * @param suspensionStatus
     * @return void
     */
    public void setSuspensionStatus(boolean suspensionStatus) {
        this.suspensionStatus = suspensionStatus;
    }
    /**
     * @method Name : getTaxiMeterStatus
     * @return taxiMeterStatus
     */
    public int getTaxiMeterStatus() {
        return taxiMeterStatus;
    }
    /**
     * @method Name : setTaxiMeterStatus
     * @param taxiMeterStatus
     * @return void
     */
    public void setTaxiMeterStatus(int taxiMeterStatus) {
        this.taxiMeterStatus = taxiMeterStatus;
    }
    /**
     * @method Name : getEmergencyStatus
     * @return emergencyStatus
     */
    public int getEmergencyStatus() {
        return emergencyStatus;
    }
    /**
     * @method Name : setEmergencyStatus
     * @param emergencyStatus
     * @return void
     */
    public void setEmergencyStatus(int emergencyStatus) {
        this.emergencyStatus = emergencyStatus;
    }
    /**
     * @method Name : isGpsFixStatus
     * @return gpsFixStatus
     */
    public boolean isGpsFixStatus() {
        return gpsFixStatus;
    }
    /**
     * @method Name : setGpsFixStatus
     * @param gpsFixStatus
     * @return void
     */
    public void setGpsFixStatus(boolean gpsFixStatus) {
        this.gpsFixStatus = gpsFixStatus;
    }
    /**
     * @method Name : getIvdStatus
     * @return ivdStatus
     */
    public int getIvdStatus() {
        return ivdStatus;
    }
    /**
     * @method Name : setIvdStatus
     * @param ivdStatus
     * @return void
     */
    public void setIvdStatus(int ivdStatus) {
        this.ivdStatus = ivdStatus;
    }
    /**
     * @method Name : isAcknowledgement
     * @return acknowledgement
     */
    public boolean isAcknowledgement() {
        return acknowledgement;
    }
    /**
     * @method Name : setAcknowledgement
     * @param acknowledgement
     * @return void
     */
    public void setAcknowledgement(boolean acknowledgement) {
        this.acknowledgement = acknowledgement;
    }
    /**
     * @method Name : isExpressway
     * @return expressway
     */
    public boolean isExpressway() {
        return expressway;
    }
    /**
     * @method Name : setMessageType
     * @param messageType
     * @return void
     */
    public void setExpressway(boolean expressway) {
        this.expressway = expressway;
    }
    /**
     * @method Name : setZoneOrRank
     * @param zoneOrRank
     * @return void
     */
    public void setZoneOrRank(String zoneOrRank) {
        this.zoneOrRank = zoneOrRank;
    }
    /**
     * @method Name : getSequence
     * @return sequence
     */
    public Integer getSequence() {
        return sequence;
    }
    /**
     * @method Name : setSequence
     * @param sequence
     * @return void
     */
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @Override
    public String toString() {
        return "MessageHeader [acknowledgement=" + acknowledgement + ", expressway=" + expressway + ", mobileId="
                + mobileId + ", xOffset=" + xOffset + ", yOffset=" + yOffset + ", serialNum=" + serialNum + ", speed="
                + speed + ", zoneOrRank=" + zoneOrRank + ", messageType=" + messageType + ", direction=" + direction
                + ", timestamp=" + timestamp + ", suspensionStatus=" + suspensionStatus + ", taxiMeterStatus="
                + taxiMeterStatus + ", emergencyStatus=" + emergencyStatus + ", gpsFixStatus=" + gpsFixStatus
                + ", ivdStatus=" + ivdStatus + ", sequence=" + sequence + "]";
    }

}
