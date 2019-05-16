/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.studyinfoprotection.lab1;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author apu
 */
public class EncryptedWord {
    
    @Getter @Setter
    String srcWord;
    
    @Getter @Setter
    String combination;
    
    @Getter @Setter
    String resultWord;

    public EncryptedWord(String srcWord, String combination, String resultWord) {
        this.srcWord = srcWord;
        this.combination = combination;
        this.resultWord = resultWord;
    }
    
}
