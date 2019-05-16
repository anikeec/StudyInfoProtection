/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.studyinfoprotection;

import com.apu.studyinfoprotection.lab1.NarayanaAlhorithm;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author apu
 */
public class NarayanaAlhorithmTest {
    
    public NarayanaAlhorithmTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    

    /**
     * Test of nextPermutation method, of class NarayanaAlhorithm.
     */
    @Test
    public void testNextPermutation() {
        //char[] sequence = "table".toCharArray();
        Integer[] sequence = new Integer[4];
        NarayanaAlhorithm.initSequence(sequence); // Формирование исходной последовательности
        System.out.println("Неубывающая последовательность и её перестановки:");
        do {
            System.out.println();
            for(int i=0; i<sequence.length; i++) {
                System.out.print(sequence[i]);
            }
//            System.out.println(Arrays.deepToString(sequence));
        } while (NarayanaAlhorithm.nextPermutation(sequence, NarayanaAlhorithm::less)); // x < y — критерий сравнения для неубывающей последовательности
        System.out.println("Невозрастающая последовательность и её перестановки:");
        do {
            System.out.println(Arrays.deepToString(sequence));
        } while (NarayanaAlhorithm.nextPermutation(sequence, NarayanaAlhorithm::greater)); // x > y — критерий сравнения для невозрастающей последовательности
    }
    
}
