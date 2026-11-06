package ru.academy.textapi.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TextService {

    private static final String VOWELS = "аеёиоуыэюяaeiou";
    private static final String SIGNS = "ьъ";   // буквы, но не согласные

    public String removeVowels(String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (!isVowel(c)) {
                result.append(c);
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
        Map<Character, Integer> map = new LinkedHashMap<>();
        for (char c : text.toCharArray()) {
            char lower = Character.toLowerCase(c);
            if (Character.isLetter(lower)) {
                map.put(lower, map.getOrDefault(lower, 0) + 1);
            }
        }
        return map;
    }

    public Map<Character, Integer> countChars(String text) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char c : text.toCharArray()) {
            if (counts.containsKey(c)) {
                counts.put(c, counts.get(c) + 1);
            } else {
                counts.put(c, 1);
            }
        }
        return counts;
    }

    public Map<Character, Integer> countCharsStream(String text) {
        List<Character> list = text.chars()
                .mapToObj(x -> (Character) (char) x)
                .collect(Collectors.toList());

        Map<Character, Integer> counts = new HashMap<>();
        list.stream().forEach(c -> counts.put(c, counts.getOrDefault(c, 0) + 1));
        return counts;
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