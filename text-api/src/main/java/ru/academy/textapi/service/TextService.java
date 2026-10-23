package ru.academy.textapi.service;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class TextService {

    private static final String VOWELS = "аеёиоуыэюяaeiou";
    private static final String SIGNS = "ьъ";   // буквы, но не согласные

    public String removeVowels(String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {      // for-each по символам строки
            if (!isVowel(c)) {
                result.append(c);                // оставляем всё, что не гласная
            }
        }
        return result.toString();
    }

    public int countConsonants(String text) {
        int count = 0;
        for (char c : text.toCharArray()) {
            if (isConsonant(c)) {
                count++;
            }
        }
        return count;
    }

    public Map<Character, Integer> letterMap(String text) {
        Map<Character, Integer> map = new LinkedHashMap<>();  // сохраняет порядок появления букв
        for (char c : text.toCharArray()) {
            char lower = Character.toLowerCase(c);
            if (Character.isLetter(lower)) {
                map.put(lower, map.getOrDefault(lower, 0) + 1);
            }
        }
        return map;
    }

    private boolean isVowel(char c) {
        return VOWELS.indexOf(Character.toLowerCase(c)) >= 0;
    }

    private boolean isConsonant(char c) {
        char lower = Character.toLowerCase(c);
        return Character.isLetter(lower)
                && !isVowel(lower)
                && SIGNS.indexOf(lower) < 0;
    }
}