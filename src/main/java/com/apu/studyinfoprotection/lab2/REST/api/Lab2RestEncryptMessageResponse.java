/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.studyinfoprotection.lab2.REST.api;

import com.apu.studyinfoprotection.REST.api.RestBasePacket;
import com.apu.studyinfoprotection.REST.api.RestPacketType;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author apu
 */
public class Lab2RestEncryptMessageResponse extends RestBasePacket {
    
    @Getter @Setter
    private String message;
    
    @Getter @Setter
    private String number;
    
    @Getter @Setter
    private String encryptedMessage;

    public Lab2RestEncryptMessageResponse() {
        this.packetType = RestPacketType.ENCRYPT_MESSAGE_RESPONSE;
    }
    
}
