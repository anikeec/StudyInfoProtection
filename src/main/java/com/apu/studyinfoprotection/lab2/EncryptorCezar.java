/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.studyinfoprotection.lab2;

import static com.apu.studyinfoprotection.lab2.CezarCryptoMethods.*;

/**
 *
 * @author apu
 */
public class EncryptorCezar {
    
    private String output;
    private int k;

    public EncryptorCezar(String text, int k) {
        this.k = k;
        String input = text;
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);

            if (isEnglish(c)) {
                    int y = (findInEnglish(c) + k) % NUMBER_OF_ENG;

                    if (Character.isUpperCase(c)) {
                            output.append(engslish[y]);
                    } else {
                            output.append(Character.toLowerCase(engslish[y]));
                    }

                    continue;
            }

            if (isRussian(c)) {
                    int y = (findInRussian(c) + k) % NUMBER_OF_RUS;

                    if (Character.isUpperCase(c)) {
                            output.append(russian[y]);
                    } else {
                            output.append(Character.toLowerCase(russian[y]));
                    }

                    continue;
            } 

            if (!isEnglish(c) && !isRussian(c)) {
                    output.append(c);
            }
        }
        this.output = output.toString();
    }

    public String getEncText() {
        return this.output;
    }

    public int getK() {
        return this.k;
    }
    
}
