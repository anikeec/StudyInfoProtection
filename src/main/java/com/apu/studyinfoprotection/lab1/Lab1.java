/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.studyinfoprotection.lab1;

import com.apu.studyinfoprotection.lab1.REST.api.Lab1DecryptedMessage;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author apu
 */
public class Lab1 {
    
    public static int MATRIX_SIDE = 4;
    
    public Set<Lab1DecryptedMessage> decryptMessage(String encryptedMsg,
                                        EncryptedWord foundRowWord, 
                                        EncryptedWord foundColumnWord) {

        Set<Lab1DecryptedMessage> result = new HashSet<>();
        
        String message = decryptMessage(encryptedMsg, 
                                            foundRowWord.getCombination(), 
                                            foundColumnWord.getCombination());
        
        result.add(new Lab1DecryptedMessage(message,
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
    
    public Set<String> getCombinationSet(int size) {
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
    
}
