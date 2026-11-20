package ru.academy.textapi.multithreading;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LockOrderingDemo {
    private final Object textLock = new Object();
    private final Object statisticsLock = new Object();
    private int completed;

    public static void main(String[] args) throws InterruptedException {
        var demo = new LockOrderingDemo();
        if (Arrays.asList(args).contains("--deadlock")) {
            demo.demonstrateDeadlock();
        } else {
            demo.demonstrateOrderedLocks();
        }
    }

    private void demonstrateOrderedLocks() throws InterruptedException {
        Thread first = new Thread(this::update, "text-update");
        Thread second = new Thread(this::update, "statistics-update");
        first.start();
        second.start();
        first.join();
        second.join();

        if (completed != 2) {
            throw new IllegalStateException("Expected two completed updates");
        }
        System.out.println("Updates completed: " + completed);
    }

    private void update() {
        synchronized (textLock) {
            synchronized (statisticsLock) {
                completed++;
            }
        }
    }

    private void demonstrateDeadlock() throws InterruptedException {
        var firstLocksHeld = new CountDownLatch(2);
        Thread first = deadlockingThread("text-first", textLock, statisticsLock, firstLocksHeld);
        Thread second = deadlockingThread("statistics-first", statisticsLock, textLock, firstLocksHeld);
        first.start();
        second.start();

        var threads = ManagementFactory.getThreadMXBean();
        long started = System.nanoTime();
        while (System.nanoTime() - started < TimeUnit.SECONDS.toNanos(3)) {
            long[] deadlocked = threads.findMonitorDeadlockedThreads();
            if (deadlocked != null && Arrays.stream(deadlocked).anyMatch(id -> id == first.getId())
                    && Arrays.stream(deadlocked).anyMatch(id -> id == second.getId())) {
                System.out.printf("Deadlock detected: %s and %s%n", first.getName(), second.getName());
                return;
            }
            Thread.sleep(10);
        }
        throw new IllegalStateException("The demo did not detect its deadlock in time");
    }

    private static Thread deadlockingThread(String name, Object first, Object second, CountDownLatch ready) {
        Thread thread = new Thread(() -> {
            synchronized (first) {
                ready.countDown();
                try {
                    ready.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                synchronized (second) {
                    System.out.println("Acquired both locks: " + name);
                }
            }
        }, name);
        thread.setDaemon(true);
        return thread;
    }
}
