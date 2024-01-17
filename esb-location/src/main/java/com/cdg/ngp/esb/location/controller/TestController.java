package com.cdg.ngp.esb.location.controller;

import com.cdg.ngp.esb.common.data.Message;
import com.cdg.ngp.esb.common.data.MessageContent;
import com.cdg.ngp.esb.common.data.MessageHeader;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Description TODO
 * @Author siy
 * @Date 2024/1/16 16:43
 * @Version 1.0
 */

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    //@Qualifier("amqs")
    private JmsTemplate jt;


    @GetMapping("/t1")
    public String Test(){
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setMobileId(1);
        MessageContent messageContent = new MessageContent();
        messageContent.setDriverID("a1");
        messageContent.setVehicleID("b1");
        Message message = new Message(messageHeader, messageContent);
        jt.convertAndSend("ESBLocationMsgQ", message);

        return "ok";
    }


}
