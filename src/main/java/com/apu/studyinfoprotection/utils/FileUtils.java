/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.studyinfoprotection.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author apu
 */
public class FileUtils {
    
    public static String getTextFromFile(String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = 
                new BufferedReader(new FileReader(filename));
            while(reader.ready()) {
                sb.append(reader.readLine()).append("/r/n");
            }
        } catch (FileNotFoundException ex) {
            return null;
        }
        return sb.toString();
    }
    
}
