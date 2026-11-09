package ru.academy.textapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.academy.textapi.service.TextService;

import java.util.Map;

@RestController
@RequestMapping("/api/text")
public class TextController {

    private final TextService textService;

    public TextController(TextService textService) {
        this.textService = textService;
    }

    @GetMapping("/remove-vowels")
    public String removeVowels(@RequestParam String text) {
        return textService.removeVowels(text);
    }

    @GetMapping("/consonants-count")
    public int countConsonants(@RequestParam String text) {
        return textService.countConsonants(text);
    }

    @GetMapping("/letter-map")
    public Map<Character, Integer> letterMap(@RequestParam String text) {
        return textService.letterMap(text);
    }

    @GetMapping("/char-count")
    public Map<Character, Integer> countChars(@RequestParam String text) {
        return textService.countChars(text);
    }

    @GetMapping("/char-count-stream")
    public Map<Character, Integer> countCharsStream(@RequestParam String text) {
        return textService.countCharsStream(text);
    }
}
