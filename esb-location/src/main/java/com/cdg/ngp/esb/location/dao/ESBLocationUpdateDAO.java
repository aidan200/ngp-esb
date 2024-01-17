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

package com.cdg.ngp.esb.location.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import com.cdg.ngp.esb.location.message.ESBLocationMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cdg.ngp.esb.common.enums.VehicleStatusEnum;
import com.cdg.ngp.esb.common.data.Message;
import com.cdg.ngp.esb.common.data.MessageContent;
import com.cdg.ngp.esb.common.data.MessageHeader;
import com.cdg.ngp.esb.location.utils.CN2Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Class name : ESBLocationUpdateDAO.java
 * @Description :Query to update the ESBLocation
 * @Author Tend
 *  **/

@Component
public class ESBLocationUpdateDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ESBLocationUpdateDAO.class);

    @Autowired
    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @method Name : updateOtherInfo
     * @param messageList
     * @return List<ESBLocationMessage>
     */
    public List<ESBLocationMessage> updateOtherInfo(List<Message> messageList) {
        List<ESBLocationMessage> esbLocationMessageList = new ArrayList<ESBLocationMessage>();
        Map<Integer, ESBLocationMessage> messageMap = new TreeMap<Integer, ESBLocationMessage>();
        long start = System.currentTimeMillis();
        StringBuilder innerBuilder = new StringBuilder();

        for (Message message : messageList) {
            messageMap.put(message.getMessageHeader().getMobileId(), new ESBLocationMessage(message));
            innerBuilder.append("?,");
        }

        String inClause = innerBuilder.deleteCharAt(innerBuilder.length() - 1).toString();

        String queryString = "SELECT V.IVD_NO,VU.VEHICLE_ID,VU.DRIVER_ID,JOB_NO FROM "
                + " ESC_VEHICLE_UPDATE VU, ESC_VEHICLE V WHERE " + " VU.VEHICLE_ID = V.VEHICLE_ID AND V.IVD_NO IN ("
                + inClause + ")";
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(queryString)) {
                int i = 1;
                for (Message message : messageList) {
                    pstmt.setString(i++, Integer.toString(message.getMessageHeader().getMobileId()));
                }
                ResultSet resultSet = pstmt.executeQuery();
                while (resultSet != null && resultSet.next()) {
                    Integer ivdNumber = Integer.parseInt(resultSet.getString("IVD_NO"));
                    //Integer ivdNumber = new Integer(resultSet.getString("IVD_NO") + "");
                    ESBLocationMessage message = messageMap.get(ivdNumber);
                    message.getMessageContent().setVehicleID(resultSet.getString("VEHICLE_ID"));
                    message.getMessageContent().setDriverID(resultSet.getString("DRIVER_ID"));
                    message.setJobNumber(resultSet.getString("JOB_NO"));
                    messageMap.put(ivdNumber, message);
                }
            } catch (Exception e) {
                LOGGER.error("Exception while updating other info:" + e);
            }
        } catch (Exception e) {
            LOGGER.error("Exception while updating other info:" + e);
        }
        
        //CITYNET-1196, Added by Rajalakshmi D.N. on 24 May 2017
        for(Message message : messageList){
            Integer ivdNo = message.getMessageHeader().getMobileId();
            String driverId = messageMap.get(ivdNo).getMessageContent().getDriverID();
            String vehicleId = messageMap.get(ivdNo).getMessageContent().getVehicleID(); 

            //Added for CITYNET-1324
            if(StringUtils.isEmpty(vehicleId)){
            	LOGGER.error("Not able to get the vehicleId from DB for IVD["+message.getMessageHeader().getMobileId()
            			+"] IVD Header::"+message.getMessageHeader());
            	continue;
            }
            
            ESBLocationMessage esbLocMsg = new ESBLocationMessage(message);
            esbLocMsg.getMessageContent().setVehicleID(vehicleId);
            esbLocMsg.getMessageContent().setDriverID(driverId);
            esbLocationMessageList.add(esbLocMsg);
        }
        
        //End..CITYNET-1196, Added by Rajalakshmi D.N. on 24 May 2017

        //esbLocationMessageList.addAll(messageMap.values());//CITYNET-1196, Commented by Rajalakshmi D.N.
        LOGGER.info("Inside updateOtherInfo():batchSize[" + esbLocationMessageList.size() + "]: completed in-->"
                + (System.currentTimeMillis() - start) + " milliseconds");
        return esbLocationMessageList;
    }
    /**
     * @method Name : persistVehicleUpdate
     * @param messageList
     * @return void
     */
    public void persistVehicleUpdate(List<ESBLocationMessage> messageList) {

        Connection conn = null;
        PreparedStatement pstmt = null;

        Map<String, ESBLocationMessage> messageMap = new TreeMap<String, ESBLocationMessage>();
        for (Message message : messageList) {
            messageMap.put(message.getMessageContent().getVehicleID(), new ESBLocationMessage(message));
        }
        List<ESBLocationMessage> uniqueMessageList = new ArrayList<ESBLocationMessage>();
        uniqueMessageList.addAll(messageMap.values());
        
        try {
            conn = dataSource.getConnection();
            StringBuilder sb = new StringBuilder(210);
            sb.append("UPDATE ESC_VEHICLE_UPDATE SET ");
            sb.append("VEH_X=?, ");//1
            sb.append("VEH_Y=?, ");//2
            sb.append("ZONE_ID=?, ");//3
            sb.append("VEH_STATUS=?, ");//4
            sb.append("SPEED=?, ");//5
            sb.append("DIRECTION=?, ");//6
            sb.append("ONEXPRESS_FLAG=?, ");//7
            sb.append("GPS_FIX_FLAG=?, ");//8
            sb.append("METER_STATUS=?, ");//9
            sb.append("EMERG_FLAG=?, ");//10
            sb.append("SUSPEND_FLAG=?, ");//11
            sb.append("UPDATE_DT=? ");//12
            sb.append("WHERE VEHICLE_ID= ?");//13

            long start = System.currentTimeMillis();
            pstmt = conn.prepareStatement(sb.toString());

            for (ESBLocationMessage message : uniqueMessageList) {
                MessageHeader messageHeader = message.getMessageHeader();
                MessageContent messageContent = message.getMessageContent();
                pstmt.setBigDecimal(1, CN2Util.convDoubleToBigDecimal(messageHeader.getxOffset()));
                pstmt.setBigDecimal(2, CN2Util.convDoubleToBigDecimal(messageHeader.getyOffset()));
                pstmt.setString(3, messageHeader.getZoneOrRank());
                pstmt.setString(4, VehicleStatusEnum.getEnumByVehStatusNo(Integer.valueOf(messageHeader.getIvdStatus()))
                        .getVehStatusLabel());
                pstmt.setBigDecimal(5, CN2Util.convDoubleToBigDecimal(messageHeader.getSpeed()));
                pstmt.setString(6, CN2Util.setDefaultValueForNull(messageHeader.getDirection()));
                pstmt.setString(7, messageHeader.isExpressway() ? "1" : "0");
                pstmt.setString(8, messageHeader.isGpsFixStatus() ? "1" : "0");
                pstmt.setString(9, CN2Util.setDefaultValueForNull(messageHeader.getTaxiMeterStatus()));
                pstmt.setString(10, CN2Util.setDefaultValueForNull(messageHeader.getEmergencyStatus()));
                pstmt.setString(11, messageHeader.isSuspensionStatus() ? "1" : "0");
                pstmt.setTimestamp(12, new Timestamp(new Date().getTime()));
                pstmt.setString(13, messageContent.getVehicleID());
                pstmt.addBatch();
                LOGGER.debug("persistVehicleUpdate:" + message.getMessageHeader().toString());
            }

            int[] insertResult = pstmt.executeBatch();
            for (int i = 0; i < insertResult.length; i++) {
                if (insertResult[i] == 0) {
                    LOGGER.error("Insert vehicle log failed for Message::" + messageList.get(i));
                }
            }

            LOGGER.info("Inside persistVehicleUpdate():batchSize[" + messageList.size() + "]: completed in-->"
                    + (System.currentTimeMillis() - start) + " milliseconds");

        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();

                }
            } catch (Exception e1) {
                LOGGER.error("Could not able to rollback" + e1);
            }
            LOGGER.error("Exception while persisting vehicleUpdate:" + e);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                LOGGER.error("Could not able to close PreparedStatement" + e);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                LOGGER.error("Could not able to close Connection" + e);
            }
        }
    }
    /**
     * @method Name : persistVehicleLog
     * @param messageList
     * @return void
     */
    public void persistVehicleLog(List<ESBLocationMessage> messageList) {

        long start = System.currentTimeMillis();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dataSource.getConnection();
            StringBuilder sb = new StringBuilder(400);
            sb.append("INSERT INTO ESC_VEHICLE_LOG( ");
            sb.append("VEHICLE_ID, ");//1
            sb.append("HEADER_INFO, ");//2
            sb.append("EVENT_CODE, ");//3
            sb.append("JOB_NO, ");//4
            sb.append("DRIVER_ID, ");//5
            sb.append("LOG_DT, ");//6
            sb.append("EVENT_DT, ");//7
            sb.append("IVD_MESSAGE_ID, ");//8
            sb.append("IVD_SERIAL_NUMBER, ");//9
            sb.append("IVD_ACK_FLAG, ");//10
            sb.append("IVD_NO, ");//11
            sb.append("IVD_LAT, ");//12
            sb.append("IVD_LON, ");//13
            sb.append("IVD_SPEED, ");//14
            sb.append("IVD_DIRECTION, ");//15
            sb.append("IVD_STATUS, ");//16
            sb.append("IVD_ZONE, ");//17
            sb.append("IVD_SEQUENCE, ");//18
            sb.append("IVD_EXPRESSWAY_FLAG, ");//19
            sb.append("IVD_GPS_FIXED, ");//20
            sb.append("IVD_EMERGENCY, ");//21
            sb.append("IVD_SUSPENSION_STATUS, ");//22
            sb.append("IVD_METER_STATUS ");//23
            sb.append(") values ( ");//
            for (int i = 0; i < 23; i++) {
                sb.append("?,");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(" )");
            pstmt = conn.prepareStatement(sb.toString());

            for (ESBLocationMessage message : messageList) {

                MessageHeader messageHeader = message.getMessageHeader();//
                MessageContent messageContent = message.getMessageContent();//
                pstmt.setString(1, messageContent.getVehicleID());//
                pstmt.setBytes(2, messageContent.getHeaderInfo());//
                pstmt.setDouble(3, messageHeader.getMessageType());//
                pstmt.setString(4, message.getJobNumber());//
                pstmt.setString(5, messageContent.getDriverID());//
                pstmt.setTimestamp(6, new Timestamp(new Date().getTime()));//
                pstmt.setTimestamp(7, new Timestamp(messageHeader.getTimestamp()));//
                pstmt.setInt(8, messageHeader.getMessageType());//
                pstmt.setInt(9, messageHeader.getSerialNum());//
                pstmt.setString(10, messageHeader.isAcknowledgement() ? "1" : "0");//
                pstmt.setDouble(11, messageHeader.getMobileId());
                pstmt.setDouble(12, messageHeader.getyOffset());//
                pstmt.setDouble(13, messageHeader.getxOffset());//
                pstmt.setDouble(14, messageHeader.getSpeed());//
                pstmt.setDouble(15, Double.parseDouble(CN2Util.setDefaultValueForNull(messageHeader.getDirection())));//
                pstmt.setString(16, CN2Util.intToBinary(messageHeader.getIvdStatus(),4));//
                pstmt.setDouble(17, Double.parseDouble(messageHeader.getZoneOrRank()));//
                if (messageHeader.getSequence() == null) {
                    pstmt.setNull(18, 4);
                } else {
                    pstmt.setInt(18, messageHeader.getSequence());
                } //
                pstmt.setString(19, messageHeader.isExpressway() ? "1" : "0");//
                pstmt.setString(20, messageHeader.isGpsFixStatus() ? "1" : "0");//
                pstmt.setString(21, CN2Util.intToBinary(messageHeader.getEmergencyStatus(),2));//
                pstmt.setString(22, messageHeader.isSuspensionStatus() ? "1" : "0");//
                pstmt.setString(23, CN2Util.intToBinary(messageHeader.getTaxiMeterStatus(),2));//
                pstmt.addBatch();
                LOGGER.debug("persistVehicleLog:" + message.getMessageHeader().toString());
            }

            int[] insertResult = pstmt.executeBatch();
            for (int i = 0; i < insertResult.length; i++) {
                if (insertResult[i] == 0) {
                    LOGGER.error("Insert vehicle log failed for Message::" + messageList.get(i));
                }
            }

            LOGGER.info("Inside persistVehicleLog():batchSize[" + messageList.size() + "]: completed in-->"
                    + (System.currentTimeMillis() - start) + " milliseconds");
        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception e1) {
                LOGGER.error("Could not able to rollback" + e1);
            }
            LOGGER.error("Exception while persisting vehicleLog:" + e);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                LOGGER.error("Could not able to close PreparedStatement" + e);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                LOGGER.error("Could not able to close Connection" + e);
            }
        }
    }

}
