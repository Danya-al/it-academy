package ru.academy.textapi.multithreading;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class BatchTextAnalyzer {
    private static final int MAX_WORKERS = 3;

    private BatchTextAnalyzer() {
    }

    public record TextStatistics(int codePointCount, int wordCount) {
    }

    public static List<TextStatistics> analyze(List<String> texts, Duration timeout)
            throws InterruptedException, ExecutionException, TimeoutException {
        if (timeout.isNegative()) {
            throw new IllegalArgumentException("Timeout must be non-negative");
        }
        long timeoutNanos = timeout.toNanos();
        List<Callable<TextStatistics>> tasks = new ArrayList<>();
        for (String text : List.copyOf(texts)) {
            tasks.add(() -> analyzeOne(text));
        }
        if (tasks.isEmpty()) {
            return List.of();
        }

        ExecutorService executor = Executors.newFixedThreadPool(Math.min(tasks.size(), MAX_WORKERS));
        try {
            List<Future<TextStatistics>> futures = executor.invokeAll(tasks, timeoutNanos, TimeUnit.NANOSECONDS);
            if (futures.stream().anyMatch(Future::isCancelled)) {
                throw new TimeoutException("Text analysis timed out");
            }
            List<TextStatistics> results = new ArrayList<>(futures.size());
            for (Future<TextStatistics> future : futures) {
                results.add(future.get());
            }
            return List.copyOf(results);
        } finally {
            executor.shutdownNow();
        }
    }

    private static TextStatistics analyzeOne(String text) throws InterruptedException {
        int codePointCount = 0;
        int wordCount = 0;
        boolean insideWord = false;
        for (int index = 0; index < text.length();) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            int codePoint = text.codePointAt(index);
            index += Character.charCount(codePoint);
            codePointCount++;

            boolean separator = Character.isWhitespace(codePoint) || Character.isSpaceChar(codePoint);
            if (!separator && !insideWord) {
                wordCount++;
            }
            insideWord = !separator;
        }
        return new TextStatistics(codePointCount, wordCount);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        List<String> texts = List.of("one two", "", "Java\tthreads", "\uD83D\uDE42");
        List<TextStatistics> expected = List.of(
                new TextStatistics(7, 2),
                new TextStatistics(0, 0),
                new TextStatistics(12, 2),
                new TextStatistics(1, 1)
        );
        List<TextStatistics> results = analyze(texts, Duration.ofSeconds(5));
        if (!results.equals(expected)) {
            throw new IllegalStateException("Unexpected analysis results: " + results);
        }
        System.out.println(results);

        try {
            analyze(texts, Duration.ZERO);
            throw new IllegalStateException("A zero timeout should cancel unfinished analysis");
        } catch (TimeoutException expectedTimeout) {
            System.out.println(expectedTimeout.getMessage());
        }
    }
}
