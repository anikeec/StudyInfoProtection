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
    
//    protected static final int nEng = 26;  //Мощность алфавита
//    protected static final int nRus = 33;

    protected static char[] engslish = {'A', 'B', 'C', 'D', 'E', 'F', 'G',
                    'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                    'U', 'V', 'W', 'X', 'Y', 'Z',
                                        'a', 'b', 'c', 'd', 'e', 'f', 'g',
                    'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                    'u', 'v', 'w', 'x', 'y', 'z'};

    protected static char[] russian = { 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж',
                    'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У',
                    'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я',
                                        'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж',
                    'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у',
                    'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'};
    
    protected static int NUMBER_OF_ENG = engslish.length;
    protected static final int NUMBER_OF_RUS = russian.length;

    public static int findInEnglish(char c) {
            int rez = -1;

            for (int i = 0; i < engslish.length; ++i) {
                    if (Character.toUpperCase(c) == engslish[i])
                            rez = i;
            }
            return rez;
    }

    public static int findInRussian(char c) {
            int rez = -1;

            for (int i = 0; i < russian.length; ++i) {
                    if (Character.toUpperCase(c) == russian[i])
                            rez = i;
            }
            return rez;
    }

    public static boolean isRussian(char c){
            int i = (int) c;

            //1105 и 1025 - коды буквы ё и Ё
            return (i>=1040 && i<=1103 || i == 1105 || i == 1025) ? true : false;
    }

    public static boolean isEnglish(char c){
            int i = (int) c;

            return (i>=65 && i<=122);
    } 
        
}
