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
package com.cdg.ngp.esb.ms.simulator;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

//import sg.com.cdgtaxi.comms.tlv.util.BytesSize;
//import sg.com.cdgtaxi.comms.tlv.util.BytesUtil;
/** 
 * @Class name : LocationUtils.java
 * @Description :set the min and max coordinates
 * @Author tend
 * @Since 22 Feb, 2016
**/
public class LocationUtils {

  //  static final Logger log = LoggerFactory.getLogger(LocationUtils.class);
    private static final Map<Integer, Object[]> areas = new HashMap<Integer, Object[]>();
    private static final Map<Integer, double[]> fixedPositions =
            new HashMap<Integer, double[]>();
    private static double[] minCoordinates = new double[] {1.26401, 103.63300};
    private static double[] maxCoordinates = new double[] {1.47131, 104.03263};

    /**
     * @method Name: setMinMax
     * @param minLat
     * @param maxLat
     * @param minLon
     * @param maxLon
     * @return void 
     */
    public static void setMinMax(double minLat, double maxLat, double minLon, double maxLon) {
    	minCoordinates[0] = minLat<0 ? minCoordinates[0] : minLat;
    	minCoordinates[1] = minLon<0 ? minCoordinates[1] : minLon;
    	maxCoordinates[0] = maxLat<0 ? maxCoordinates[0] : maxLat;
    	maxCoordinates[1] = maxLon<0 ? maxCoordinates[1] : maxLon;
    }
    /**
     *@method Name : getRandomPosition
     *@return double
     */
    public static double[] getRandomPosition() {
        return getRandomPositionForArea(minCoordinates, maxCoordinates);
    }
    /**
     *@method Name : getRandomDirection
     *@return int
     */
    public static int getRandomDirection() {
        Random ran = new Random();
        return ran.nextInt(32);
    }
    

    /*public static Object[] getByteRandomPosition(int zoneId) {
        
        int[] randomPos = getIntRandomPositionByArea(zoneId);

        int ilat = randomPos[0];
        int ilon = randomPos[1];

        byte[] lats = new byte[2];
        byte[] lons = new byte[2];
        try {
            lats = BytesUtil.convertToBytes(ilat, BytesSize.INT16);

            lons = BytesUtil.convertToBytes(ilon, BytesSize.INT16);
        } catch (Exception ex) {
            log.error("Exception when get Random location", ex);
        }

        Object[] objects = new Object[2];
        objects[0] = lons;
        objects[1] = lats;
        return objects;
    }*/
    /**
     *@method Name : getIntRandomPositionByArea
     *@param zoneId
     *@return int
     */
    public static int[] getIntRandomPositionByArea(int zoneId) {
        Object[] areaMaxMin = getAreasLatLon(zoneId);
        
        double[] randomPositionByArea = 
                getRandomPositionForArea((double[]) areaMaxMin[1], 
                        (double[]) areaMaxMin[0]);
        
        int[] ilatlon = parsePositionToIntOffset(randomPositionByArea);
        return ilatlon;
    }
    /**
     *@method Name : getIntFixedPositionByArea
     *@param zoneId
     *@return int
     */
    public static int[] getIntFixedPositionByArea(int zoneId) {
        double[] fixedAreaPosition = getFixedPositionByArea(zoneId);
        
        if (fixedAreaPosition == null) {
            return null;
        }
        
        int[] ilatlon = parsePositionToIntOffset(fixedAreaPosition);
        
        return ilatlon;
    }
    /**
     *@method Name : parsePositionToIntOffset
     *@param position
     *@return int
     */
    private static int[] parsePositionToIntOffset(double[] position) {
        int baseLat = 100000;
        int baseLon = 10355000;
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumIntegerDigits(3);
        format.setMaximumFractionDigits(5);
        
        Float fLat = Float.parseFloat(format.format(position[1])) * 100000;
        Float fLon = Float.parseFloat(format.format(position[0])) * 100000;
        
        int ilat = fLat.intValue() - baseLat;
        int ilon = fLon.intValue() - baseLon;
        
        return new int[] {ilat, ilon};
    }
  /**
     * 
     * @param minCoordinates [0] min lat; [1] min lon
     * @param maxCoordinates [0] max lat; [1] max lon
     * @return double
     */
    public static double[] getRandomPositionForArea(double[] minCoordinates, double[] maxCoordinates) {
        double[] position = new double[2];
        double minCoLat = minCoordinates[0];
        double minCoLon = minCoordinates[1];
        double maxCoLat = maxCoordinates[0];
        double maxCoLon = maxCoordinates[1];

        position[0] = minCoLat
                + (double) (Math.random() * (maxCoLat - minCoLat));
        position[1] = minCoLon
                + (double) (Math.random() * (maxCoLon - minCoLon));

        return position;
    }
    /**
     *@method Name : getAreasLatLon
     *@param zoneId
     *@return Object
     */
    public static Object[] getAreasLatLon(int zoneId) {
        return areas.get(zoneId);
    }
    /**
     *@method Name : getFixedPositionByArea
     *@param zoneId
     *@return double
     */
    public static double[] getFixedPositionByArea(int zoneId) {
        return fixedPositions.get(zoneId);
    }
    
    static {
        areas.put(1, new Object[] { new double[] { 103.873348, 1.286694 }, new double[] { 103.850509, 1.268688 } });
        areas.put(2, new Object[] { new double[] { 103.902059, 1.296119 }, new double[] { 103.866754, 1.279959 } });
        areas.put(3, new Object[] { new double[] { 103.875307, 1.304659 }, new double[] { 103.855143, 1.277875 } });
        areas.put(4, new Object[] { new double[] { 103.859412, 1.289163 }, new double[] { 103.846534, 1.279852 } });
        areas.put(5, new Object[] { new double[] { 103.849343, 1.291556 }, new double[] { 103.839585, 1.28067 } });
        areas.put(6, new Object[] { new double[] { 103.853193, 1.284201 }, new double[] { 103.844109, 1.275064 } });
        areas.put(7, new Object[] { new double[] { 103.855719, 1.28067 }, new double[] { 103.84204, 1.260352 } });
        areas.put(8, new Object[] { new double[] { 103.844109, 1.283472 }, new double[] { 103.829151, 1.263889 } });
        areas.put(9, new Object[] { new double[] { 103.848624, 1.280463 }, new double[] { 103.806367, 1.237458 } });
        areas.put(10, new Object[] { new double[] { 103.817595, 1.284209 }, new double[] { 103.80079, 1.265553 } });
        areas.put(11, new Object[] { new double[] { 103.806987, 1.304073 }, new double[] { 103.746928, 1.262077 } });
        areas.put(12, new Object[] { new double[] { 103.77592, 1.327086 }, new double[] { 103.747278, 1.288047 } });
        areas.put(13, new Object[] { new double[] { 103.802185, 1.319348 }, new double[] { 103.771161, 1.284209 } });
        areas.put(14, new Object[] { new double[] { 103.815819, 1.312189 }, new double[] { 103.791382, 1.285353 } });
        areas.put(15, new Object[] { new double[] { 103.825562, 1.29575 }, new double[] { 103.80079, 1.278575 } });
        areas.put(16, new Object[] { new double[] { 103.842422, 1.293509 }, new double[] { 103.822569, 1.275062 } });
        areas.put(17, new Object[] { new double[] { 103.857919, 1.298637 }, new double[] { 103.842848, 1.286414 } });
        areas.put(18, new Object[] { new double[] { 103.860458, 1.305323 }, new double[] { 103.847906, 1.291952 } });
        areas.put(19, new Object[] { new double[] { 103.868817, 1.307344 }, new double[] { 103.855984, 1.298589 } });
        areas.put(20, new Object[] { new double[] { 103.86451, 1.315988 }, new double[] { 103.850937, 1.303176 } });
        areas.put(21, new Object[] { new double[] { 103.858713, 1.319863 }, new double[] { 103.845522, 1.305051 } });
        areas.put(22, new Object[] { new double[] { 103.850623, 1.314313 }, new double[] { 103.828697, 1.298941 } });
        areas.put(23, new Object[] { new double[] { 103.847923, 1.307557 }, new double[] { 103.828506, 1.289205 } });
        areas.put(24, new Object[] { new double[] { 103.833138, 1.311429 }, new double[] { 103.802208, 1.292534 } });
        areas.put(25, new Object[] { new double[] { 103.83875, 1.323326 }, new double[] { 103.803728, 1.305536 } });
        areas.put(26, new Object[] { new double[] { 103.81343, 1.331746 }, new double[] { 103.788014, 1.311429 } });
        areas.put(27, new Object[] { new double[] { 103.802907, 1.334181 }, new double[] { 103.774455, 1.306338 } });
        areas.put(28, new Object[] { new double[] { 103.820091, 1.351516 }, new double[] { 103.788864, 1.323326 } });
        areas.put(29, new Object[] { new double[] { 103.843857, 1.346052 }, new double[] { 103.81343, 1.320793 } });
        areas.put(30, new Object[] { new double[] { 103.853317, 1.328988 }, new double[] { 103.825445, 1.313146 } });
        areas.put(31, new Object[] { new double[] { 103.864868, 1.34402 }, new double[] { 103.837608, 1.329111 } });
        areas.put(32, new Object[] { new double[] { 103.868421, 1.33063 }, new double[] { 103.841217, 1.315988 } });
        areas.put(33, new Object[] { new double[] { 103.875301, 1.328067 }, new double[] { 103.858713, 1.304821 } });
        areas.put(34, new Object[] { new double[] { 103.881317, 1.343498 }, new double[] { 103.868422, 1.320185 } });
        areas.put(35, new Object[] { new double[] { 103.872412, 1.351486 }, new double[] { 103.857419, 1.328067 } });
        areas.put(36, new Object[] { new double[] { 103.889147, 1.344357 }, new double[] { 103.871032, 1.327672 } });
        areas.put(37, new Object[] { new double[] { 103.891123, 1.332391 }, new double[] { 103.880365, 1.322116 } });
        areas.put(38, new Object[] { new double[] { 103.892455, 1.332101 }, new double[] { 103.868817, 1.309727 } });
        areas.put(39, new Object[] { new double[] { 103.894252, 1.315459 }, new double[] { 103.867958, 1.298298 } });
        areas.put(40, new Object[] { new double[] { 103.905964, 1.337777 }, new double[] { 103.888909, 1.315163 } });
        areas.put(41, new Object[] { new double[] { 103.926292, 1.357427 }, new double[] { 103.896318, 1.315789 } });
        areas.put(42, new Object[] { new double[] { 103.918516, 1.318765 }, new double[] { 103.896843, 1.301346 } });
        areas.put(43, new Object[] { new double[] { 103.903197, 1.316017 }, new double[] { 103.866754, 1.294398 } });
        areas.put(44, new Object[] { new double[] { 103.938766, 1.312138 }, new double[] { 103.90062, 1.295327 } });
        areas.put(45, new Object[] { new double[] { 103.934414, 1.322956 }, new double[] { 103.913353, 1.30752 } });
        areas.put(46, new Object[] { new double[] { 103.961634, 1.337073 }, new double[] { 103.918879, 1.306173 } });
        areas.put(47, new Object[] { new double[] { 103.939095, 1.347693 }, new double[] { 103.911171, 1.328139 } });
        areas.put(48, new Object[] { new double[] { 103.984233, 1.353215 }, new double[] { 103.942413, 1.312902 } });
        areas.put(49, new Object[] { new double[] { 103.992759, 1.393648 }, new double[] { 103.965977, 1.345212 } });
        areas.put(50, new Object[] { new double[] { 103.987562, 1.393431 }, new double[] { 103.961109, 1.352715 } });
        areas.put(51, new Object[] { new double[] { 103.953903, 1.396448 }, new double[] { 103.919921, 1.365511 } });
        areas.put(52, new Object[] { new double[] { 103.964216, 1.375862 }, new double[] { 103.908994, 1.332067 } });
        areas.put(53, new Object[] { new double[] { 103.934832, 1.400935 }, new double[] { 103.870816, 1.333509 } });
        areas.put(54, new Object[] { new double[] { 103.914166, 1.401545 }, new double[] { 103.876509, 1.366486 } });
        areas.put(55, new Object[] { new double[] { 103.877111, 1.378951 }, new double[] { 103.85548, 1.343326 } });
        areas.put(56, new Object[] { new double[] { 103.87159, 1.392444 }, new double[] { 103.831013, 1.352015 } });
        areas.put(57, new Object[] { new double[] { 103.857933, 1.380998 }, new double[] { 103.803902, 1.341464 } });
        areas.put(58, new Object[] { new double[] { 103.805939, 1.371123 }, new double[] { 103.76734, 1.332642 } });
        areas.put(59, new Object[] { new double[] { 103.781456, 1.348773 }, new double[] { 103.748596, 1.317159 } });
        areas.put(60, new Object[] { new double[] { 103.756369, 1.353588 }, new double[] { 103.723968, 1.291562 } });
        areas.put(61, new Object[] { new double[] { 103.733144, 1.347606 }, new double[] { 103.705085, 1.300256 } });
        areas.put(62, new Object[] { new double[] { 103.740457, 1.335011 }, new double[] { 103.64703, 1.222033 } });
        areas.put(63, new Object[] { new double[] { 103.709045, 1.380689 }, new double[] { 103.627814, 1.303886 } });
        areas.put(64, new Object[] { new double[] { 103.733232, 1.373923 }, new double[] { 103.692747, 1.338891 } });
        areas.put(65, new Object[] { new double[] { 103.770317, 1.378202 }, new double[] { 103.730591, 1.339306 } });
        areas.put(66, new Object[] { new double[] { 103.767742, 1.379765 }, new double[] { 103.754204, 1.353683 } });
        areas.put(67, new Object[] { new double[] { 103.783975, 1.402583 }, new double[] { 103.753334, 1.375527 } });
        areas.put(68, new Object[] { new double[] { 103.761213, 1.389925 }, new double[] { 103.731552, 1.376278 } });
        areas.put(69, new Object[] { new double[] { 103.73822, 1.413595 }, new double[] { 103.666641, 1.356546 } });
        areas.put(70, new Object[] { new double[] { 103.727976, 1.430613 }, new double[] { 103.654964, 1.379095 } });
        areas.put(71, new Object[] { new double[] { 103.742693, 1.451801 }, new double[] { 103.681825, 1.41169 } });
        areas.put(72, new Object[] { new double[] { 103.799775, 1.440382 }, new double[] { 103.727934, 1.384317 } });
        areas.put(73, new Object[] { new double[] { 103.772467, 1.44849 }, new double[] { 103.751946, 1.410058 } });
        areas.put(74, new Object[] { new double[] { 103.797577, 1.45151 }, new double[] { 103.770513, 1.410083 } });
        areas.put(75, new Object[] { new double[] { 103.816256, 1.471298 }, new double[] { 103.771318, 1.425877 } });
        areas.put(76, new Object[] { new double[] { 103.865072, 1.456033 }, new double[] { 103.817777, 1.400719 } });
        areas.put(77, new Object[] { new double[] { 103.826294, 1.43938 }, new double[] { 103.777229, 1.367467 } });
        areas.put(78, new Object[] { new double[] { 103.843337, 1.406414 }, new double[] { 103.81543, 1.370555 } });
        areas.put(79, new Object[] { new double[] { 103.895661, 1.428887 }, new double[] { 103.833693, 1.384149 } });
        areas.put(80, new Object[] { new double[] { 103.876833, 1.392444 }, new double[] { 103.847855, 1.376479 } });
        areas.put(81, new Object[] { new double[] { 104.004554, 1.391349 }, new double[] { 103.972182, 1.325077 } });
        areas.put(82, new Object[] { new double[] { 103.928354, 1.422174 }, new double[] { 103.888448, 1.386726 } });
        areas.put(90, new Object[] { new double[] { 103.978361, 1.388366 }, new double[] { 103.953195, 1.360291 } });
        areas.put(93, new Object[] { new double[] { 103.650676, 1.324596 }, new double[] { 103.605297, 1.212666 } });
        areas.put(95, new Object[] { new double[] { 103.850827, 1.471297 }, new double[] { 103.812803, 1.423727 } });
        areas.put(97, new Object[] { new double[] { 103.791405, 1.381323 }, new double[] { 103.761213, 1.357298 } });
        areas.put(99, new Object[] { new double[] { 104.033244, 1.327969 }, new double[] { 103.975143, 1.309374 } });
        
        fixedPositions.put(57, new double[] {103.8369210000, 1.3593250000});
    }
}
