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
public class Lab2RestDecryptMessageRequest extends RestBasePacket {
    
    @Getter 
    private String message;
    
    @Getter @Setter
    private String number;

    public Lab2RestDecryptMessageRequest() {
        this.packetType = RestPacketType.DECRYPT_MESSAGE_REQUEST;
    }
    
}
