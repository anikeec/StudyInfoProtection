/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.studyinfoprotection.REST.api;

import lombok.Getter;

/**
 *
 * @author apu
 */
public class RestErrorPacket extends RestBasePacket {
    
    @Getter
    private String message;
    
    public RestErrorPacket() {
        this.packetType = RestPacketType.ERROR_PACKET;
    }

    public RestErrorPacket(String message) {
        this();
        this.message = message;
    }
    
}
