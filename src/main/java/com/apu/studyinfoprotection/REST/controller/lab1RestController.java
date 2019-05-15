/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.studyinfoprotection.REST.controller;
import com.apu.studyinfoprotection.REST.api.RestBasePacket;
import com.apu.studyinfoprotection.REST.api.RestEncryptMessageRequest;
import com.apu.studyinfoprotection.REST.api.RestEncryptMessageResponse;
import com.apu.studyinfoprotection.REST.api.RestErrorPacket;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author apu
 */
@RestController
public class lab1RestController {
    
    private static final Logger LOGGER = 
                        LogManager.getLogger(lab1RestController.class);
    
    private final int MATRIX_SIDE = 4;
    
    @RequestMapping(path="/rest/lab1/encrypt",  method=RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestBasePacket encryptMessage(@RequestBody RestEncryptMessageRequest request) {
   
        String sourceMessage = request.getSourceMessage();
        if(sourceMessage.length() == 0) {
            return new RestErrorPacket("Wrong sourceMessage length");
        }
        if(sourceMessage.length() > 16) {
            sourceMessage = sourceMessage.substring(0, 16);
        }
        
        String rowWord = request.getRowWord();
        if((rowWord.length() < 4) || (rowWord.length() > 4)) {
            return new RestErrorPacket("Wrong rowWord length");
        }
        
        String columnWord = request.getColumnWord();
        if((columnWord.length() < 4) || (columnWord.length() > 4)) {
            return new RestErrorPacket("Wrong columnWord length");
        }
        
        String rowSequenceStr = request.getRowSequence();
        if((rowSequenceStr.length() < 7) || (rowSequenceStr.length() > 7)) {
            return new RestErrorPacket("Wrong rowSequenceStr length");
        }
        
        String columnSequenceStr = request.getColumnSequence();
        if((columnSequenceStr.length() < 7) || (columnSequenceStr.length() > 7)) {
            return new RestErrorPacket("Wrong columnSequenceStr length");
        }
        
        char[][] sourceMessageStrArray = new char[MATRIX_SIDE][MATRIX_SIDE];
        try {
            for(int y=0; y<MATRIX_SIDE; y++) {
                for(int x=0; x<MATRIX_SIDE; x++) {                
                    if(x + (y*MATRIX_SIDE) >= sourceMessage.length())
                        continue;
                    sourceMessageStrArray[y][x] = sourceMessage.charAt(x + (y*MATRIX_SIDE));
                }
            }
        } catch(NumberFormatException ex) {
            return new RestErrorPacket("Wrong sourceMessage format");
        }
        
        String[] rowSequenceStrArray = rowSequenceStr.split(" ");
        int[] rowSequence = new int[MATRIX_SIDE];
        try {
            for(int x=0; x<MATRIX_SIDE; x++) {
                rowSequence[x] = Integer.parseInt(rowSequenceStrArray[x]);
            }
        } catch(NumberFormatException ex) {
            return new RestErrorPacket("Wrong rowSequence format");
        }
        
        String[] columnSequenceStrArray = columnSequenceStr.split(" ");
        int[] columnSequence = new int[MATRIX_SIDE];
        try {
            for(int x=0; x<MATRIX_SIDE; x++) {
                columnSequence[x] = Integer.parseInt(columnSequenceStrArray[x]);
            }
        } catch(NumberFormatException ex) {
            return new RestErrorPacket("Wrong columnSequence format");
        }
        
        //encode
        char[][] encodedMessageStrArrayX = new char[MATRIX_SIDE][MATRIX_SIDE];
        for(int y=0; y<MATRIX_SIDE; y++) {
            for(int x=0; x<MATRIX_SIDE; x++) {   
                encodedMessageStrArrayX[y][x] = sourceMessageStrArray[y][columnSequence[x]];
            }
        }
        
        char[][] encodedMessageStrArrayY = new char[MATRIX_SIDE][MATRIX_SIDE];
        for(int x=0; x<MATRIX_SIDE; x++) {   
            for(int y=0; y<MATRIX_SIDE; y++) {
                encodedMessageStrArrayY[y][x] = encodedMessageStrArrayX[rowSequence[y]][x];
            }
        }
        
        RestEncryptMessageResponse response = new RestEncryptMessageResponse();
        
        StringBuilder sb = new StringBuilder();
        
        for(int y=0; y<MATRIX_SIDE; y++) {
            sb.append(new String(sourceMessageStrArray[y]));
        }
        response.setSourceMessage(sb.toString());
        
        sb = new StringBuilder();
        for(int y=0; y<MATRIX_SIDE; y++) {
            sb.append(new String(encodedMessageStrArrayY[y]));
        }       
        response.setEncryptedMessage(sb.toString());
        
        response.setColumnSequence(columnSequenceStr);
        response.setRowSequence(rowSequenceStr);
        response.setColumnWord(columnWord);
        response.setRowWord(rowWord);
        
        return response;
    }
    
}
