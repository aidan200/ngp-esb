package com.cdg.ngp.esb.location;

import com.cdg.ngp.esb.common.data.Message;
import com.cdg.ngp.esb.common.data.MessageContent;
import com.cdg.ngp.esb.common.data.MessageHeader;
import com.cdg.ngp.esb.location.dao.ESBLocationUpdateDAO;
import com.cdg.ngp.esb.location.dao.ESBLocationUpdateHANADAO;
import com.cdg.ngp.esb.location.message.ESBLocationMessage;
import com.cdg.ngp.esb.location.utils.HttpClientUtil;
import jakarta.jms.JMSException;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.params.Test;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;

/**
 * @ClassName MyApplicationTest
 * @Description TODO
 * @Author siy
 * @Date 2024/1/16 16:00
 * @Version 1.0
 */

@CamelSpringBootTest
@SpringBootApplication
public class MyApplicationTest {


    @Autowired
    private ProducerTemplate template;

    private ESBLocationUpdateHANADAO hanadao;
    private HttpClientUtil httpClient;

    @Before("test_send")
    public void init(){
        httpClient = mock(HttpClientUtil.class);

        String authenKey = "Basic REFVQVQ6SGFuYTEyMzQh";
        String restApiUrl = "http://10.2.140.142:8031/cn3da/xsjs/vehiclelocationupdate.xsjs?method=INSERT";
        ESBLocationUpdateDAO dao = mock(ESBLocationUpdateDAO.class);
        hanadao = mock(ESBLocationUpdateHANADAO.class);
        doCallRealMethod().when(hanadao).setAuthenKey(authenKey);
        hanadao.setAuthenKey(authenKey);
        doCallRealMethod().when(hanadao).setRestApiUrl(restApiUrl);
        hanadao.setRestApiUrl(restApiUrl);
        doCallRealMethod().when(hanadao).setLocationOrcaleDao(dao);
        hanadao.setLocationOrcaleDao(dao);
        doCallRealMethod().when(hanadao).setHttpClient(httpClient);
        hanadao.setHttpClient(httpClient);
    }


//    @Test
//    public void testReceive() throws Exception {
//        //mock.expectedBodiesReceived("Hello");
//        MessageHeader messageHeader=new MessageHeader();
//        MessageContent messageContent=new MessageContent();
//        Message message;
//
//        ESBLocationMessage m;
//
//        messageHeader.setMobileId(8888);
//        messageHeader.setxOffset(1);
//        messageHeader.setyOffset(2);
//        messageHeader.setZoneOrRank("\"11");
//        messageHeader.setIvdStatus(0);
//        messageHeader.setSpeed(100);
//        messageHeader.setDirection(123);
//        messageHeader.setTaxiMeterStatus(1);
//        messageHeader.setEmergencyStatus(2);
//        messageHeader.setMessageType(0);
//        messageHeader.setTimestamp(System.currentTimeMillis());
//        messageContent.setVehicleID("0001");
//        messageContent.setDriverID("007");
//        byte[] b=new byte[]{1};
//        messageContent.setHeaderInfo(b);
//        message=new Message(messageHeader, messageContent);
//
//        template.sendBody("amqr:queue:siy.test", message);
//        //mock.assertIsSatisfied();
//    }

    @Test
    public void test_send() throws JMSException {

        List<ESBLocationMessage> list=new ArrayList<ESBLocationMessage>();
        List<Message> messageList=new ArrayList<Message>();
        MessageHeader messageHeader=new MessageHeader();
        MessageContent messageContent=new MessageContent();
        Message message;

        ESBLocationMessage m;

        messageHeader.setMobileId(8888);
        messageHeader.setxOffset(1);
        messageHeader.setyOffset(2);
        messageHeader.setZoneOrRank("\"11");
        messageHeader.setIvdStatus(0);
        messageHeader.setSpeed(100);
        messageHeader.setDirection(123);
        messageHeader.setTaxiMeterStatus(1);
        messageHeader.setEmergencyStatus(2);
        messageHeader.setMessageType(0);
        messageHeader.setTimestamp(System.currentTimeMillis());
        messageContent.setVehicleID("0001");
        messageContent.setDriverID("007");
        byte[] b=new byte[]{1};
        messageContent.setHeaderInfo(b);
        message=new Message(messageHeader, messageContent);
        messageList.add(message);
        m=new ESBLocationMessage(message);
        m.setJobNumber("1000888");
        list.add(m);

        doCallRealMethod().when(hanadao).persistVehicleStatus(list);
        hanadao.persistVehicleStatus(list);
    }

}
