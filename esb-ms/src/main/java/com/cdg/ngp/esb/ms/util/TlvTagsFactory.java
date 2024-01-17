package com.cdg.ngp.esb.ms.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lombok.extern.slf4j.Slf4j;

import sg.com.cdgtaxi.comms.tlv.annotation.TagByteSize;
import sg.com.cdgtaxi.comms.tlv.msg.IVDFieldTag;
import sg.com.cdgtaxi.comms.tlv.util.BytesSize;


@Slf4j
public class TlvTagsFactory {
    //private static final Logger log = Logger.getLogger(TlvTagsFactory.class);
    private static final ConcurrentMap<IVDFieldTag, BytesSize> tagByteSizeMap =
            new ConcurrentHashMap<IVDFieldTag, BytesSize>();
    
    
    public static BytesSize getTagByteSize(IVDFieldTag tag) {
        return tagByteSizeMap.get(tag);
    }
    static {
    	TlvTagsFactory.init();
    }
    
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

