/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.studyinfoprotection.lab1.REST.api;

import com.apu.studyinfoprotection.REST.api.RestBasePacket;
import com.apu.studyinfoprotection.REST.api.RestPacketType;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author apu
 */
public class Lab1RestEncryptMessageResponse extends RestBasePacket {
    
    @Getter @Setter
    private String sourceMessage;
    
    @Getter @Setter
    private String rowWord;
    
    @Getter @Setter
    private String columnWord;
    
    @Getter @Setter
    private String rowSequence;
    
    @Getter @Setter
    private String columnSequence;
    
    @Getter @Setter
    private String encryptedMessage;
    
    @Getter @Setter
    private String encryptedRowWord;
    
    @Getter @Setter
    private String encryptedColumnWord;

    public Lab1RestEncryptMessageResponse() {
        this.packetType = RestPacketType.ENCRYPT_MESSAGE_RESPONSE;
    }
    
}
