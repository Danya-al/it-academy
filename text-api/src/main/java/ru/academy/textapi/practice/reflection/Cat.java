package ru.academy.textapi.practice.reflection;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class Cat {

    private String name;
    private int age;

    private String secret(String word) {
        return name + " talk: " + word;
    }

}
