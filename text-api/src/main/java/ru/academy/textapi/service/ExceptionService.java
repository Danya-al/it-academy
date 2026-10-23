package ru.academy.textapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ExceptionService {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionService.class);

    public void checkCheckedExceptionRequiredThrows() throws IOException {
        throw new IOException("file not found");
    }

    public void throwUncheckedException() {
        String test = null;
        test.length();
    }

    public String catchAndLogException() {
        try {
            checkCheckedExceptionRequiredThrows();
            return "no exception";
        } catch (IOException e) {
            logger.error("Caught IOException in catchAndLogException", e); // сообщение, потом исключение
            e.printStackTrace();
            return "Exception caught and logged: " + e.getMessage();
        }
    }
}