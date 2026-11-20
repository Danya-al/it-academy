package ru.academy.textapi.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class RaceConditionDemo {
    private static final int THREAD_COUNT = 10;
    private static final int INCREMENTS_PER_THREAD = 100_000;

    public static void main(String[] args) throws InterruptedException {
        var counter = new Counter();
        runWorkers(counter::incrementPlain);
        runWorkers(counter::incrementSynchronized);
        runWorkers(counter::incrementAtomic);

        int expected = THREAD_COUNT * INCREMENTS_PER_THREAD;
        System.out.println("Expected: " + expected);
        System.out.println("Plain int: " + counter.plainValue());
        System.out.println("synchronized: " + counter.synchronizedValue());
        System.out.println("AtomicInteger: " + counter.atomicValue());

        if (counter.synchronizedValue() != expected || counter.atomicValue() != expected) {
            throw new IllegalStateException("A thread-safe counter lost increments");
        }
    }

    private static void runWorkers(Runnable increment) throws InterruptedException {
        var ready = new CountDownLatch(THREAD_COUNT);
        var start = new CountDownLatch(1);
        List<Thread> workers = new ArrayList<>(THREAD_COUNT);

        try {
            for (int i = 1; i <= THREAD_COUNT; i++) {
                Thread worker = new Thread(() -> {
                    ready.countDown();
                    try {
                        start.await();
                        for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                            increment.run();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }, "counter-" + i);
                worker.start();
                workers.add(worker);
            }

            ready.await();
        } finally {
            start.countDown();
        }

        for (Thread worker : workers) {
            worker.join();
        }
    }
}
