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
    
    public String decrypt(String text, int numberA, int numberB) {
        
        int gcd = 0;
        if((gcd = CezarCryptoMethods.gcd(numberA, alphabet.length)) != 1) {
            throw new IllegalArgumentException("GCD for numbers is " + gcd);
        } 

        String input = text;
        StringBuilder dec = new StringBuilder();

        long back = findBack(numberA, alphabet.length);
        
        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);

            int position = findInAlphabet(c);
            if (position != (-1)) {
                int x = (int)((back * (position + NUMBER_OF_ALPHABET - numberB)) % NUMBER_OF_ALPHABET);
                dec.append(alphabet[x]);
            } else {
                dec.append(c);
            }
        }

        return dec.toString();
    }
    
}
