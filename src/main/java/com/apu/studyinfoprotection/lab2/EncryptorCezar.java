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
    
    public String encrypt(String text, int numberA, int numberB) {
        
        int gcd = 0;
        if((gcd = CezarCryptoMethods.gcd(numberA, alphabet.length)) != 1) {
            throw new IllegalArgumentException("GCD for numbers is " + gcd);
        }       
        
        String input = text;
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);

            if (findInAlphabet(c) != (-1)) {
                int y = ((numberA * findInAlphabet(c) + numberB)) % NUMBER_OF_ALPHABET;
                output.append(alphabet[y]);
            } else {
                output.append(c);
            }
        }
        return output.toString();
    }
    
}
