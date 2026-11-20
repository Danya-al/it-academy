package ru.academy.textapi.multithreading;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public final class ConcurrentStartDemo {
    public static void main(String[] args) throws InterruptedException {
        List<String> texts = List.of("java", "thread", "latch", "monitor", "queue");
        int[] lengths = new int[texts.size()];
        CountDownLatch ready = new CountDownLatch(texts.size());
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch finished = new CountDownLatch(texts.size());
        List<Thread> workers = new ArrayList<>();

        for (int index = 0; index < texts.size(); index++) {
            int resultIndex = index;
            String text = texts.get(index);
            Thread worker = new Thread(() -> {
                ready.countDown();
                try {
                    start.await();
                    lengths[resultIndex] = text.length();
                    System.out.println(Thread.currentThread().getName() + " finished " + text);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    finished.countDown();
                }
            }, "text-worker-" + (index + 1));
            workers.add(worker);
        }

        try {
            workers.forEach(Thread::start);
            if (!ready.await(5, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Workers did not get ready in time");
            }
            System.out.println("Start");
            start.countDown();
            if (!finished.await(5, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Workers did not finish in time");
            }
        } finally {
            workers.forEach(Thread::interrupt);
        }

        int[] expected = {4, 6, 5, 7, 5};
        if (!Arrays.equals(expected, lengths)) {
            throw new IllegalStateException("Incorrect text lengths: " + Arrays.toString(lengths));
        }
        System.out.println("Lengths in input order: " + Arrays.toString(lengths));
    }
}
