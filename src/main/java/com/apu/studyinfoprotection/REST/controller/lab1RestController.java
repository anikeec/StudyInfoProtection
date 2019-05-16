/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.studyinfoprotection.REST.controller;
import com.apu.studyinfoprotection.NarayanaAlhorithm;
import com.apu.studyinfoprotection.REST.api.DecryptedMessage;
import com.apu.studyinfoprotection.REST.api.RestBasePacket;
import com.apu.studyinfoprotection.REST.api.RestDecryptMessageRequest;
import com.apu.studyinfoprotection.REST.api.RestDecryptMessageResponse;
import com.apu.studyinfoprotection.REST.api.RestEncryptMessageRequest;
import com.apu.studyinfoprotection.REST.api.RestEncryptMessageResponse;
import com.apu.studyinfoprotection.REST.api.RestErrorPacket;
import com.apu.studyinfoprotection.lab1.EncryptedWord;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        response.setColumnWord(new String(encodedColumnWord));
        response.setRowWord(new String(encodedRowWord));
        response.setEncryptedColumnWord(columnWord);
        response.setEncryptedRowWord(rowWord);
        
        return response;
    }
    
    @RequestMapping(path="/rest/lab1/decrypt",  method=RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RestBasePacket decryptMessage(@RequestBody RestDecryptMessageRequest request) {
        
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
        
        Set<EncryptedWord> rowWordListAll = new HashSet<>();
        Set<EncryptedWord> columnWordListAll = new HashSet<>();
        
        char[] tempBuffer = new char[MATRIX_SIDE];
        
        Set<String> combinationSet = getCombinationSet(MATRIX_SIDE);
        
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
        
        Set<EncryptedWord> rowWordGoodList = findGoodResultWords(rowWordListAll);
        Set<EncryptedWord> columnWordGoodList = findGoodResultWords(columnWordListAll);
        
        List<DecryptedMessage> decrMsgList = new ArrayList<>();
        for(EncryptedWord foundRowWord:rowWordGoodList) {
            for(EncryptedWord foundColumnWord:columnWordGoodList) {
                Set<DecryptedMessage> decryptedMsgSet = 
                        decryptMessage(sourceMessage, foundRowWord, foundColumnWord);
                decrMsgList.addAll(decryptedMsgSet);
            }
        }        

        RestDecryptMessageResponse response = new RestDecryptMessageResponse();
        
        response.setSourceMessage(sourceMessage);
        response.setRowWord(rowWord);
        response.setColumnWord(columnWord);
        response.setList(decrMsgList);
        
        return response;
    }
    
    private Set<EncryptedWord> findGoodResultWords(Set<EncryptedWord> srcWordsSet) {
        
        //temp state
        return srcWordsSet;
    }
    
    private Set<DecryptedMessage> decryptMessage(String encryptedMsg,
                                        EncryptedWord foundRowWord, 
                                        EncryptedWord foundColumnWord) {
        
//        Set<String> rowDecryptionCombinationSet = 
//                            getDecryptionCombinations(foundRowWord, encryptedRowWord);
//        Set<String> columnDecryptionCombinationSet = 
//                            getDecryptionCombinations(foundColumnWord, encryptedColumnWord);
        
//        Set<String> result = new HashSet<>();
//        for(String rowCombination:rowDecryptionCombinationSet) {
//            for(String columnCombination:columnDecryptionCombinationSet) {
//                result.add(decryptMessage(encryptedMsg, rowCombination, columnCombination));
//            }
//        }

        Set<DecryptedMessage> result = new HashSet<>();
        
        String message = decryptMessage(encryptedMsg, 
                                            foundRowWord.getCombination(), 
                                            foundColumnWord.getCombination());
        
        result.add(new DecryptedMessage(message,
                                        foundRowWord.getResultWord(),
                                        foundColumnWord.getResultWord()
                                        ));

        return result;
    }
    
    private String decryptMessage(String encryptedMsg,
                                String decryptionRowCombination, 
                                String decryptionColumnCombination) {
        
        char[][] sourceMessageStrArray = new char[MATRIX_SIDE][MATRIX_SIDE];
            for(int y=0; y<MATRIX_SIDE; y++) {
                for(int x=0; x<MATRIX_SIDE; x++) {                
                    if(x + (y*MATRIX_SIDE) >= encryptedMsg.length())
                        continue;
                    sourceMessageStrArray[y][x] = encryptedMsg.charAt(x + (y*MATRIX_SIDE));
                }
            }
        
        //encode
        char[][] encodedMessageStrArrayX = new char[MATRIX_SIDE][MATRIX_SIDE];
        for(int y=0; y<MATRIX_SIDE; y++) {
            for(int x=0; x<MATRIX_SIDE; x++) {   
                encodedMessageStrArrayX[y][x] = 
                        sourceMessageStrArray[y][(byte)(decryptionColumnCombination.charAt(x) - '0')];
            }
        }
        
        char[][] encodedMessageStrArrayY = new char[MATRIX_SIDE][MATRIX_SIDE];
        for(int x=0; x<MATRIX_SIDE; x++) {   
            for(int y=0; y<MATRIX_SIDE; y++) {
                encodedMessageStrArrayY[y][x] = 
                        encodedMessageStrArrayX[(byte)(decryptionRowCombination.charAt(y) - '0')][x];
            }
        }
        
        StringBuilder sb = new StringBuilder();
        for(int y=0; y<MATRIX_SIDE; y++) {
            sb.append(new String(encodedMessageStrArrayY[y]));
        } 
        
        return sb.toString();
    }
    
    private Set<String> getDecryptionCombinations(String srcWord, String resultWord) {
        Set<String> combinationSet = getCombinationSet(MATRIX_SIDE);
        
        Set<String> resultCombinationSet = new HashSet<>();
        for(String combination: combinationSet) {
            if(checkDecryptionCombination(srcWord, resultWord, combination)) {
                resultCombinationSet.add(combination);
            }
        }
        
        return resultCombinationSet;
    }
    
    private boolean checkDecryptionCombination(String srcWord, String resultWord, String combination) {
        char[] srcWordStrArray = new char[srcWord.length()];
        for(int x=0; x<srcWord.length(); x++) {                
            srcWordStrArray[x] = srcWord.charAt(x);
        }
        
        //encode
        char[] encodedSrcWordX = new char[srcWord.length()];
        for(int x=0; x<MATRIX_SIDE; x++) {   
            encodedSrcWordX[x] = 
                    srcWordStrArray[(byte)(combination.charAt(x) - '0')];
        }
        
        return resultWord.equals(new String(encodedSrcWordX));
    }
    
    private Set<String> getCombinationSet(int size) {
        Integer[] sequence = new Integer[size];        
        NarayanaAlhorithm.initSequence(sequence);// Формирование исходной последовательности
        
        Set<String> resultSet = new HashSet<>();
        char[] tempBuffer = new char[size];
        System.out.println("Неубывающая последовательность и её перестановки:");
        do {
            for(int i=0; i<MATRIX_SIDE; i++) {
                tempBuffer[i] = (char)('0' + sequence[i]);
            }
            resultSet.add(new String(tempBuffer));
        } while (NarayanaAlhorithm.nextPermutation(sequence, NarayanaAlhorithm::less));
        
        System.out.println("Невозрастающая последовательность и её перестановки:");
        do {
            for(int i=0; i<MATRIX_SIDE; i++) {
                tempBuffer[i] = (char)('0' + sequence[i]);
            }
            resultSet.add(new String(tempBuffer));
        } while (NarayanaAlhorithm.nextPermutation(sequence, NarayanaAlhorithm::greater));
        
        return resultSet;
    }
    
    
    {
        //        Integer[] sequence = new Integer[MATRIX_SIDE];        
//        NarayanaAlhorithm.initSequence(sequence);// Формирование исходной последовательности
//        
//        System.out.println("Неубывающая последовательность и её перестановки:");
//        do {
//            System.out.println();
//            for(int i=0; i<MATRIX_SIDE; i++) {
//                tempBuffer[i] = rowWord.charAt(sequence[i]);
//                System.out.print(tempBuffer[i]);
//            }
//            rowWordListAll.add(new String(tempBuffer));
//            for(int i=0; i<MATRIX_SIDE; i++) {
//                tempBuffer[i] = columnWord.charAt(sequence[i]);
//                System.out.print(tempBuffer[i]);
//            }
//            columnWordListAll.add(new String(tempBuffer));
//        } while (NarayanaAlhorithm.nextPermutation(sequence, NarayanaAlhorithm::less));
//        
//        System.out.println("Невозрастающая последовательность и её перестановки:");
//        do {
//            System.out.println();
//            for(int i=0; i<MATRIX_SIDE; i++) {
//                tempBuffer[i] = rowWord.charAt(sequence[i]);
//                System.out.print(tempBuffer[i]);
//            }
//            rowWordListAll.add(new String(tempBuffer));
//            for(int i=0; i<MATRIX_SIDE; i++) {
//                tempBuffer[i] = columnWord.charAt(sequence[i]);
//                System.out.print(tempBuffer[i]);
//            }
//            columnWordListAll.add(new String(tempBuffer));
//        } while (NarayanaAlhorithm.nextPermutation(sequence, NarayanaAlhorithm::greater));
    }
    
    
    
}
