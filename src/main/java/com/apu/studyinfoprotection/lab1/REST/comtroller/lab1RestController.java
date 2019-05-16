/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.studyinfoprotection.lab1.REST.comtroller;
import com.apu.studyinfoprotection.lab1.Dictionaries;
import com.apu.studyinfoprotection.lab1.REST.api.Lab1DecryptedMessage;
import com.apu.studyinfoprotection.REST.api.RestBasePacket;
import com.apu.studyinfoprotection.lab1.REST.api.Lab1RestDecryptMessageRequest;
import com.apu.studyinfoprotection.lab1.REST.api.Lab1RestDecryptMessageResponse;
import com.apu.studyinfoprotection.lab1.REST.api.Lab1RestEncryptMessageRequest;
import com.apu.studyinfoprotection.lab1.REST.api.Lab1RestEncryptMessageResponse;
import com.apu.studyinfoprotection.REST.api.RestErrorPacket;
import com.apu.studyinfoprotection.lab1.EncryptedWord;
import com.apu.studyinfoprotection.lab1.Lab1;
import static com.apu.studyinfoprotection.lab1.Lab1.MATRIX_SIDE;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class lab1RestController {
    
    private static final Logger LOGGER = 
                        LogManager.getLogger(lab1RestController.class);
    
    private final int SEQUENCE_SIZE = (MATRIX_SIDE * 2) - 1;
    private final int TEXT_SIZE_MAX = MATRIX_SIDE * MATRIX_SIDE; 
    
    @RequestMapping(path="/rest/lab1/encrypt",  method=RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestBasePacket encryptMessage(@RequestBody Lab1RestEncryptMessageRequest request) {
   
        String sourceMessage = request.getSourceMessage().trim();
        if(sourceMessage.length() == 0) {
            return new RestErrorPacket("Wrong sourceMessage length");
        }
        if(sourceMessage.length() < TEXT_SIZE_MAX) {
            StringBuilder sb = new StringBuilder();
            for(int i=0; i<(TEXT_SIZE_MAX - sourceMessage.length()); i++) {
                sb.append(" ");
            }
            sourceMessage += sb.toString();
        }
        if(sourceMessage.length() > TEXT_SIZE_MAX) {
            sourceMessage = sourceMessage.substring(0, TEXT_SIZE_MAX);
        }        
        
        String rowWord = request.getRowWord().trim();
        if((rowWord.length() < MATRIX_SIDE) || (rowWord.length() > MATRIX_SIDE)) {
            return new RestErrorPacket("Wrong rowWord length");
        }
        
        String columnWord = request.getColumnWord().trim();
        if((columnWord.length() < MATRIX_SIDE) || (columnWord.length() > MATRIX_SIDE)) {
            return new RestErrorPacket("Wrong columnWord length");
        }
        
        String rowSequenceStr = request.getRowSequence().trim();
        if((rowSequenceStr.length() < SEQUENCE_SIZE) || (rowSequenceStr.length() > SEQUENCE_SIZE)) {
            return new RestErrorPacket("Wrong rowSequenceStr length");
        }
        
        String columnSequenceStr = request.getColumnSequence().trim();
        if((columnSequenceStr.length() < SEQUENCE_SIZE) || (columnSequenceStr.length() > SEQUENCE_SIZE)) {
            return new RestErrorPacket("Wrong columnSequenceStr length");
        }
        
        char[][] sourceMessageStrArray = new char[MATRIX_SIDE][MATRIX_SIDE];
            for(int y=0; y<MATRIX_SIDE; y++) {
                for(int x=0; x<MATRIX_SIDE; x++) {                
                    if(x + (y*MATRIX_SIDE) >= sourceMessage.length())
                        continue;
                    sourceMessageStrArray[y][x] = sourceMessage.charAt(x + (y*MATRIX_SIDE));
                }
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
        
        char[] encodedRowWord = new char[MATRIX_SIDE];
        char[] encodedColumnWord = new char[MATRIX_SIDE];
        for(int i=0; i<MATRIX_SIDE; i++) {
            encodedRowWord[i] = rowWord.charAt(rowSequence[i]);
            encodedColumnWord[i] = columnWord.charAt(columnSequence[i]);
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
        
        Lab1RestEncryptMessageResponse response = new Lab1RestEncryptMessageResponse();
        
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
        response.setEncryptedColumnWord(new String(encodedColumnWord));
        response.setEncryptedRowWord(new String(encodedRowWord));
        
        return response;
    }
    
    @RequestMapping(path="/rest/lab1/decrypt",  method=RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestBasePacket decryptMessage(@RequestBody Lab1RestDecryptMessageRequest request) {
        
        Lab1 lab1 = new Lab1();
        
        String sourceMessage = request.getSourceMessage().trim();
        if(sourceMessage.length() == 0) {
            return new RestErrorPacket("Wrong sourceMessage length");
        }
        if(sourceMessage.length() < TEXT_SIZE_MAX) {
            StringBuilder sb = new StringBuilder();
            for(int i=0; i<(TEXT_SIZE_MAX - sourceMessage.length()); i++) {
                sb.append(" ");
            }
            sourceMessage += sb.toString();
        }
        if(sourceMessage.length() > TEXT_SIZE_MAX) {
            sourceMessage = sourceMessage.substring(0, TEXT_SIZE_MAX);
        }
        
        String rowWord = request.getRowWord().trim();
        if((rowWord.length() < MATRIX_SIDE) || (rowWord.length() > MATRIX_SIDE)) {
            return new RestErrorPacket("Wrong rowWord length");
        }
        
        String columnWord = request.getColumnWord().trim();
        if((columnWord.length() < MATRIX_SIDE) || (columnWord.length() > MATRIX_SIDE)) {
            return new RestErrorPacket("Wrong columnWord length");
        }
        
        Set<EncryptedWord> rowWordListAll = new HashSet<>();
        Set<EncryptedWord> columnWordListAll = new HashSet<>();
        
        char[] tempBuffer = new char[MATRIX_SIDE];
        
        Set<String> combinationSet = lab1.getCombinationSet(MATRIX_SIDE);
        
        for(String sequence:combinationSet) {
            System.out.println();
            for(int i=0; i<MATRIX_SIDE; i++) {
                tempBuffer[i] = rowWord.charAt((byte)(sequence.charAt(i) - '0'));
                System.out.print(tempBuffer[i]);
            }
            rowWordListAll.add(
                    new EncryptedWord(rowWord, sequence, new String(tempBuffer)));
            for(int i=0; i<MATRIX_SIDE; i++) {
                tempBuffer[i] = columnWord.charAt((byte)(sequence.charAt(i) - '0'));
                System.out.print(tempBuffer[i]);
            }
            columnWordListAll.add(
                    new EncryptedWord(columnWord, sequence, new String(tempBuffer)));
        }
        
        Set<EncryptedWord> rowWordGoodList = new HashSet<>();
        Set<EncryptedWord> columnWordGoodList = new HashSet<>();
        
        Dictionaries.findGoodResultWordsCombination(rowWordListAll, 
                                                    columnWordListAll, 
                                                    rowWordGoodList, 
                                                    columnWordGoodList);
        
        List<Lab1DecryptedMessage> decrMsgList = new ArrayList<>();
        for(EncryptedWord foundRowWord:rowWordGoodList) {
            for(EncryptedWord foundColumnWord:columnWordGoodList) {
                Set<Lab1DecryptedMessage> decryptedMsgSet = 
                    lab1.decryptMessage(sourceMessage, foundRowWord, foundColumnWord);
                decrMsgList.addAll(decryptedMsgSet);
            }
        }        

        Lab1RestDecryptMessageResponse response = new Lab1RestDecryptMessageResponse();
        
        response.setSourceMessage(sourceMessage);
        response.setRowWord(rowWord);
        response.setColumnWord(columnWord);
        response.setList(decrMsgList);
        
        return response;
    }

}
