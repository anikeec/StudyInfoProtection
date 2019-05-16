/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.studyinfoprotection.lab1;

/**
 *
 * @author apu
 */
public abstract class NarayanaAlhorithm {
    
    // Возвращает true, если value_0 меньше value_1, иначе — false
    public static final <T extends Comparable> boolean less (final T value_0, final T value_1) {
        return value_0.compareTo(value_1) < 0;
    }

    // Возвращает true, если value_0 больше value_1, иначе — false
    public static final <T extends Comparable> boolean greater (final T value_0, final T value_1) {
        return value_0.compareTo(value_1) > 0;
    }

    // Инициализация последовательности
    public static final void initSequence (Integer[] sequence) {
        // Заполнение последовательности значениями 1, 2, 3…
        for (int value = sequence.length; value > 0; --value)
            sequence[value - 1] = value - 1;
    }
    
    // Функция, задающая отношение порядка для значений типа T: < либо >
    @FunctionalInterface
    public interface Predicate2 <T extends Comparable> {
        boolean compare (final T value_0, final T value_1);
    }

    // Поиск очередной перестановки
    public static final <T extends Comparable> boolean nextPermutation (T[] sequence, Predicate2 comparator) {
        // Этап № 1
        int i = sequence.length;
        do {
            if (i < 2)
                return false; // Перебор закончен
            --i;
        } while (!comparator.compare(sequence[i - 1], sequence[i]));
        // Этап № 2
        int j = sequence.length;
        while (i < j && !comparator.compare(sequence[i - 1], sequence[--j]));
        _swapItems(sequence, i - 1, j);
        // Этап № 3
        j = sequence.length;
        while (i < --j)
            _swapItems(sequence, i++, j);
        return true;
    }

    // Обмен значениями двух элементов последовательности
    private static final <T> void _swapItems (T[] sequence, int index_0, int index_1) {
        T temp = sequence[index_0];
        sequence[index_0] = sequence[index_1];
        sequence[index_1] = temp;
    }
    
}
