package ru.academy.textapi.practice.generics;

import java.util.Arrays;

public class GenericsDemo {

    public static void main(String[] args) {
        String[] names = {"Мурзик", "Барсик", "Васька", "Тема"};
        System.out.println("Строки до:    " + Arrays.toString(names));
        GenericUtils.bubbleSort(names);
        System.out.println("Строки после: " + Arrays.toString(names));

        Double[] numbers = {3.5, -1.2, 10.0, 0.7, 2.25};
        System.out.println("Числа до:     " + Arrays.toString(numbers));
        GenericUtils.bubbleSort(numbers);
        System.out.println("Числа после:  " + Arrays.toString(numbers));
    }
}