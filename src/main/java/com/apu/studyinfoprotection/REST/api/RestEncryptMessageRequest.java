/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.studyinfoprotection.REST.api;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author apu
 */
public class RestEncryptMessageRequest extends RestBasePacket {
    
    @Getter 
    private String sourceMessage;
    
    @Getter @Setter
    private String rowWord;
    
    @Getter @Setter
    private String columnWord;
    
    @Getter @Setter
    private String rowSequence;
    
    @Getter @Setter
    private String columnSequence;

    public RestEncryptMessageRequest() {
        this.packetType = RestPacketType.ENCRYPT_MESSAGE_REQUEST;
    }
    
}
