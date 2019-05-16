/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.studyinfoprotection.lab2.REST.controller;

import com.apu.studyinfoprotection.lab1.REST.comtroller.lab1RestController;
import com.apu.studyinfoprotection.REST.api.RestBasePacket;
import com.apu.studyinfoprotection.lab1.REST.api.Lab1RestEncryptMessageRequest;
import com.apu.studyinfoprotection.lab2.REST.api.Lab2RestDecryptMessageRequest;
import com.apu.studyinfoprotection.lab2.REST.api.Lab2RestDecryptMessageResponse;
import com.apu.studyinfoprotection.lab2.REST.api.Lab2RestEncryptMessageRequest;
import com.apu.studyinfoprotection.lab2.REST.api.Lab2RestEncryptMessageResponse;
import com.apu.studyinfoprotection.REST.api.RestErrorPacket;
import com.apu.studyinfoprotection.lab2.DecryptorCezar;
import com.apu.studyinfoprotection.lab2.EncryptorCezar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author apu
 */
@RestController
public class lab2RestController {
    
    private static final Logger LOGGER = 
                        LogManager.getLogger(lab1RestController.class);
    
    @RequestMapping(path="/rest/lab2/encrypt",  method=RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestBasePacket encryptMessage(@RequestBody Lab2RestEncryptMessageRequest request) {
        
        String message = request.getMessage().trim();
        if(message.length() == 0) {
            return new RestErrorPacket("Wrong message length");
        }
        
        int number = 0;
        String numberStr = request.getNumber().trim();
        try {
            number = Integer.parseInt(numberStr);
        } catch(NumberFormatException ex) {
            return new RestErrorPacket("Wrong number");
        }
        
        EncryptorCezar cezarEncryptor = new EncryptorCezar(message, number);
     
        Lab2RestEncryptMessageResponse response = new Lab2RestEncryptMessageResponse();
        response.setEncryptedMessage(cezarEncryptor.getEncText());
        response.setMessage(message);
        response.setNumber("" + number);
        
        return response;
    }
    
    @RequestMapping(path="/rest/lab2/decrypt",  method=RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestBasePacket decryptMessage(@RequestBody Lab2RestDecryptMessageRequest request) {
        
        String message = request.getMessage().trim();
        if(message.length() == 0) {
            return new RestErrorPacket("Wrong message length");
        }
        
        int number = 0;
        String numberStr = request.getNumber().trim();
        try {
            number = Integer.parseInt(numberStr);
        } catch(NumberFormatException ex) {
            return new RestErrorPacket("Wrong number");
        }
        
        DecryptorCezar cezarDecryptor = new DecryptorCezar(message, number);
     
        Lab2RestDecryptMessageResponse response = new Lab2RestDecryptMessageResponse();
        response.setDecryptedMessage(cezarDecryptor.getDecText());
        response.setMessage(message);
        response.setNumber("" + number);
        
        return response;
    }
    
}
