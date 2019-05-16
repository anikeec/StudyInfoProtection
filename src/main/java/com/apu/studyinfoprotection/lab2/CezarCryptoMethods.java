/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.studyinfoprotection.lab2;

/**
 *
 * @author apu
 */
public class CezarCryptoMethods {

    protected static char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G',
                    'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                    'U', 'V', 'W', 'X', 'Y', 'Z',
                                        'a', 'b', 'c', 'd', 'e', 'f', 'g',
                    'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                    'u', 'v', 'w', 'x', 'y', 'z', 
                                        'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж',
                    'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У',
                    'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я',
                                        'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж',
                    'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у',
                    'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'};
    
    protected static int NUMBER_OF_ALPHABET = alphabet.length;
    
    public static int findBack(int number, int module) {
        int ostatok = 1;
        int result = 0;
        while((number * result) % module != ostatok) {
            result++;
        } 
        return result;
    }
    
    public static int gcd(int a, int b) {
	if (b == 0)
		return Math.abs(a);
	return gcd(b, a % b);
    }
    
    public static int findInAlphabet(char c) {
            int rez = -1;

            for (int i = 0; i < alphabet.length; ++i) {
                if (c == alphabet[i]) {
                    rez = i;
                }
            }
            return rez;
    }
        
}
