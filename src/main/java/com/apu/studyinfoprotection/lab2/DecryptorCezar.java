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
public class DecryptorCezar {
    
    private String output;

    public DecryptorCezar(String text, int k) {

        String input = text;
        StringBuilder dec = new StringBuilder();

        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);

            if (isEnglish(c)) {
                    int x = (findInEnglish(c) - k + NUMBER_OF_ENG) % NUMBER_OF_ENG;

                    if (Character.isUpperCase(c)) {
                            dec.append(engslish[x]);
                    } else {
                            dec.append(Character.toLowerCase(engslish[x]));
                    }

                    continue;
            }

            if (isRussian(c)) {
                    int x = (findInRussian(c) - k + NUMBER_OF_RUS) % NUMBER_OF_RUS;

                    if (Character.isUpperCase(c)) {
                            dec.append(russian[x]);
                    } else {
                            dec.append(Character.toLowerCase(russian[x]));
                    }

                    continue;
            }

            if (!isEnglish(c) && !isRussian(c)) {
                    dec.append(c);
            }
        }

        output = dec.toString();
    }

    public DecryptorCezar(EncryptorCezar enc) {
        int k = enc.getK();
        String input = enc.getEncText();

        DecryptorCezar dec = new DecryptorCezar(input, k);
        output = dec.getDecText();

    }

    public String getDecText() {
        return output;
    }
    
}
