package ru.academy.textapi.practice.generics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        List<Integer> ages = new ArrayList<>(List.of(7, 3, 12, 1));
        System.out.println("Список до:    " + ages);
        GenericUtils.bubbleSort(ages);
        System.out.println("Список после: " + ages);
    }
}