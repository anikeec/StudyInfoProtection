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
public class RestDecryptMessageRequest extends RestBasePacket {
    
    @Getter 
    private String sourceMessage;
    
    @Getter @Setter
    private String rowWord;
    
    @Getter @Setter
    private String columnWord;

    public RestDecryptMessageRequest() {
        this.packetType = RestPacketType.DECRYPT_MESSAGE_REQUEST;
    }
    
}
