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
public class Lab1DecryptedMessage {
    
    @Getter @Setter
    private String rowWord;
    
    @Getter @Setter
    private String columnWord;
    
    @Getter @Setter
    private String message;

    public Lab1DecryptedMessage(String message, String rowWord, String columnWord) {
        this.rowWord = rowWord;
        this.columnWord = columnWord;
        this.message = message;
    }
    
    
    
}
