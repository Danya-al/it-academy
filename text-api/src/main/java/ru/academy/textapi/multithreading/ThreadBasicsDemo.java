package ru.academy.textapi.multithreading;

import java.util.Arrays;
import java.util.List;

public class ThreadBasicsDemo {
    public static void main(String[] args) throws InterruptedException {
        List<String> options = Arrays.asList(args);
        long delayMillis = options.contains("--pause") ? 100 : 0;

        Runnable numbers = () -> printNumbers(delayMillis);
        Thread numbersThread = new Thread(numbers, "numbers-worker");
        Thread lettersThread = options.contains("--subclass")
                ? new LettersThread(delayMillis)
                : new Thread(() -> printLetters(delayMillis), "letters-worker");

        if (options.contains("--priorities")) {
            numbersThread.setPriority(Thread.MAX_PRIORITY);
            lettersThread.setPriority(Thread.MIN_PRIORITY);
        }

        numbersThread.start();
        lettersThread.start();
        numbersThread.join();
        lettersThread.join();
        System.out.println("Done");
    }

    private static void printNumbers(long delayMillis) {
        printSequence(1, delayMillis, false);
    }

    private static void printLetters(long delayMillis) {
        printSequence('A', delayMillis, true);
    }

    private static void printSequence(int first, long delayMillis, boolean letters) {
        try {
            for (int offset = 0; offset < 10; offset++) {
                int value = first + offset;
                String item = letters ? Character.toString(value) : Integer.toString(value);
                System.out.printf("%s: %s%n", Thread.currentThread().getName(), item);
                if (delayMillis > 0 && offset < 9) {
                    Thread.sleep(delayMillis);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static final class LettersThread extends Thread {
        private final long delayMillis;

        private LettersThread(long delayMillis) {
            super("letters-worker");
            this.delayMillis = delayMillis;
        }

        @Override
        public void run() {
            printLetters(delayMillis);
        }
    }
}
