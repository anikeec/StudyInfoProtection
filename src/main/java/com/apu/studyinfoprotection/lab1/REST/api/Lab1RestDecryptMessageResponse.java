/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.studyinfoprotection.lab1.REST.api;

import com.apu.studyinfoprotection.REST.api.RestBasePacket;
import com.apu.studyinfoprotection.REST.api.RestPacketType;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author apu
 */
public class Lab1RestDecryptMessageResponse extends RestBasePacket {
    
    @Getter @Setter
    private String sourceMessage;
    
    @Getter @Setter
    private String rowWord;
    
    @Getter @Setter
    private String columnWord;
    
    @Getter @Setter
    private String amount;
    
    @Getter @Setter
    List<Lab1DecryptedMessage> list = new ArrayList<>();

    public Lab1RestDecryptMessageResponse() {
        this.packetType = RestPacketType.DECRYPT_MESSAGE_RESPONSE;
    }
    
}
