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
/** 
 * @Class name : Message.java
 * @Description :Message Bean comes from ESB through active mq
 * So keeping the same package structure .
 * @author : Tend
**/
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    protected MessageHeader messageHeader;
    protected MessageContent messageContent;
    /**
     * @method Name : getMessageHeader
     * @return MessageHeader
     */
    public MessageHeader getMessageHeader() {
        return messageHeader;
    }
    /**
     * @method Name : setMessageHeader
     * @param messageHeader
     * @return void
     */
    public void setMessageHeader(MessageHeader messageHeader) {
        this.messageHeader = messageHeader;
    }
    /**
     * @method Name : getMessageContent
     * @return MessageContent
     */
    public MessageContent getMessageContent() {
        return messageContent;
    }
    /**
     * @method Name : setMessageContent
     * @param messageContent
     * @return void
     */
    public void setMessageContent(MessageContent messageContent) {
        this.messageContent = messageContent;
    }

    public Message(){

    }

    /**
     * @method Name : Message
     * @param messageHeader
     * @param messageContent
     */
    public Message(MessageHeader messageHeader, MessageContent messageContent) {
        super();
        this.messageHeader = messageHeader;
        this.messageContent = messageContent;
    }

    @Override
    public String toString() {
        return "Message [messageHeader=" + messageHeader + ", messageContent=" + messageContent + "]";
    }

}
