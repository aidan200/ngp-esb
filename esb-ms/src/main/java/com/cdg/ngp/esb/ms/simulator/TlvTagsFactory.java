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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lombok.extern.slf4j.Slf4j;
//import org.apache.log4j.Logger;

import sg.com.cdgtaxi.comms.tlv.annotation.TagByteSize;
import sg.com.cdgtaxi.comms.tlv.msg.IVDFieldTag;
import sg.com.cdgtaxi.comms.tlv.util.BytesSize;
/** 
 * @Class name : TlvTagsFactory.java
 **/

@Slf4j
public class TlvTagsFactory {
    //private static final Logger log = Logger.getLogger(TlvTagsFactory.class);
    private static final ConcurrentMap<IVDFieldTag, BytesSize> tagByteSizeMap =
            new ConcurrentHashMap<IVDFieldTag, BytesSize>();
    
    /** 
   	 * @Method name : getTagByteSize
   	 * @param tag
   	 * @return tag
   	 **/
    public static BytesSize getTagByteSize(IVDFieldTag tag) {
        return tagByteSizeMap.get(tag);
    }
    static {
    	TlvTagsFactory.init();
    }
    /** 
   	 * @Method name : init
   	 * @return void
   	 **/
    public static void init() {
        IVDFieldTag[] allFields = IVDFieldTag.values();
        try {
            for (IVDFieldTag field : allFields) {
                TagByteSize byteSize = 
                        field.getClass().getField(field.name())
                             .getAnnotation(TagByteSize.class);

                if (byteSize == null) continue;

                BytesSize size = byteSize.size();
                tagByteSizeMap.put(field, size);
            }
        } catch (Exception e) {
            log.error("Exception when generate tagByteSize", e);
        }
    }
}

