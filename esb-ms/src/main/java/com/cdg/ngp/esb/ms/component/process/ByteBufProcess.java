package com.cdg.ngp.esb.ms.component.process;

import io.netty.buffer.ByteBuf;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * @ClassName ByteBufProcess
 * @Description ByteBuf Copy Process
 * @Author siy
 * @Date 2024/1/18 16:48
 * @Version 1.0
 */
public class ByteBufProcess implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        ByteBuf buf = exchange.getIn().getBody(ByteBuf.class);
        if (buf != null) {
            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);
            exchange.getIn().setBody(data);
        }
    }
}
