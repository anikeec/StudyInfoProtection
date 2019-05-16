/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.studyinfoprotection;

import com.apu.studyinfoprotection.lab1.EncryptedWord;
import com.apu.studyinfoprotection.utils.FileUtils;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author apu
 */
public class Dictionaries {
    
    private static final String RUSSIAN_DICTIONARY = "./pldb-win.txt";
    private static final String ENGLISH_DICTIONARY = "./english.txt";
    
    private static Set<EncryptedWord> findGoodResultWords(Set<EncryptedWord> srcWordsSet) {
        
        String text = null;
        try {
            text = FileUtils.getTextFromFile(RUSSIAN_DICTIONARY);
        } catch(IOException ex) {
            return srcWordsSet;
        }
        
        Set<EncryptedWord> resultSet = new HashSet<>();
        for(EncryptedWord encryptedWord: srcWordsSet) {
            if(text.contains(" " + encryptedWord.getResultWord() + "/r/n")) {
                resultSet.add(encryptedWord);
            }
        }
        
        try {
            text = FileUtils.getTextFromFile(ENGLISH_DICTIONARY);
        } catch(IOException ex) {
            return srcWordsSet;
        }
        
        for(EncryptedWord encryptedWord: srcWordsSet) {
            if(text.contains(encryptedWord.getResultWord() + "/r/n")) {
                resultSet.add(encryptedWord);
            }
        }

        return resultSet;
    }
    
    public static void findGoodResultWordsCombination(Set<EncryptedWord> rowWordSetAll, 
                                                Set<EncryptedWord> columnWordSetAll, 
                                                Set<EncryptedWord> rowWordGoodSet, 
                                                Set<EncryptedWord> columnWordGoodSet) {
        
        Set<EncryptedWord> rowWordTempSet = findGoodResultWords(rowWordSetAll);
        Set<EncryptedWord> columnWordTempSet = findGoodResultWords(columnWordSetAll);
        
        String russianDictionary;
        String englishDictionary;
        try {
            russianDictionary = FileUtils.getTextFromFile(RUSSIAN_DICTIONARY);
            englishDictionary = FileUtils.getTextFromFile(ENGLISH_DICTIONARY);
        } catch(IOException ex) {
            rowWordGoodSet.addAll(rowWordTempSet);
            columnWordGoodSet.addAll(columnWordTempSet);
            return;
        }       
        
        Set<EncryptedWord> resultSet = new HashSet<>();
        for(EncryptedWord encryptedColumnWord: columnWordTempSet) {
            for(EncryptedWord encryptedRowWord: rowWordTempSet) {
                if((russianDictionary.contains(" " + encryptedRowWord.getResultWord() + "/r/n") ||
                        englishDictionary.contains("/r/n" + encryptedRowWord.getResultWord() + "/r/n")) && 
                    (russianDictionary.contains(" " + encryptedColumnWord.getResultWord() + "/r/n") ||
                        englishDictionary.contains("/r/n" + encryptedColumnWord.getResultWord() + "/r/n"))) {
                    rowWordGoodSet.add(encryptedRowWord);
                    columnWordGoodSet.add(encryptedColumnWord);
                }
            }
        }
    };
    
}
