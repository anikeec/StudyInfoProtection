/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.studyinfoprotection.REST.api;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author apu
 */
public class RestDecryptMessageResponse extends RestBasePacket {
    
    @Getter @Setter
    private String sourceMessage;
    
    @Getter @Setter
    private String rowWord;
    
    @Getter @Setter
    private String columnWord;
    
    @Getter @Setter
    List<DecryptedMessage> list = new ArrayList<>();

    public RestDecryptMessageResponse() {
        this.packetType = RestPacketType.DECRYPT_MESSAGE_RESPONSE;
    }
    
}
