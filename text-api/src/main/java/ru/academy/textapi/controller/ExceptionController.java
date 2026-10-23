package ru.academy.textapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.academy.textapi.service.ExceptionService;

import java.io.IOException;

@RestController
@RequestMapping("/api/exceptions")
public class ExceptionController {

    private final ExceptionService exceptionService;

    public ExceptionController(ExceptionService exceptionService) {
        this.exceptionService = exceptionService;
    }

    @GetMapping("/checked")
    public String checkedException() throws IOException {
        exceptionService.checkCheckedExceptionRequiredThrows();
        return "exception";
    }

    @GetMapping("/unchecked")
    public String uncheckedException() {
        exceptionService.throwUncheckedException();
        return "exception";
    }

    @GetMapping("/caught")
    public String caughtException() {
        return exceptionService.catchAndLogException();
    }
}