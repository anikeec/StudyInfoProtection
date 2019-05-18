/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.studyinfoprotection.lab5.REST.api;

import com.apu.studyinfoprotection.REST.api.RestBasePacket;
import com.apu.studyinfoprotection.REST.api.RestPacketType;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author apu
 */
public class Lab4RestSignMessageResponse extends RestBasePacket {
    
    @Getter @Setter
    private String message;
    
    @Getter @Setter
    private String publicKey;
    
    @Getter @Setter
    private String publicKeyDetailed;
    
    @Getter @Setter
    private String privateKey;
    
    @Getter @Setter
    private String signedMessage;

    public Lab4RestSignMessageResponse() {
        this.packetType = RestPacketType.SIGN_MESSAGE_RESPONSE;
    }
    
}
