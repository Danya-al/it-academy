package ru.academy.textapi.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public final class ProducerConsumerDemo {
    private static final int CAPACITY = 2;
    private static final long JOIN_TIMEOUT_MILLIS = 5000;

    public static void main(String[] args) throws InterruptedException {
        List<String> texts = List.of("Java", "streams", "queue", "latch", "thread");

        BoundedTextQueue monitorQueue = new BoundedTextQueue(CAPACITY);
        List<String> monitorResult = transfer(texts, monitorQueue::put, monitorQueue::take);

        ArrayBlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(CAPACITY);
        List<String> blockingResult = transfer(texts, blockingQueue::put, blockingQueue::take);

        if (!texts.equals(monitorResult) || !texts.equals(blockingResult)) {
            throw new IllegalStateException("A queue did not transfer every text in order");
        }
        System.out.println("wait / notifyAll: " + monitorResult);
        System.out.println("ArrayBlockingQueue: " + blockingResult);
    }

    private static List<String> transfer(List<String> texts, PutText put, TakeText take)
            throws InterruptedException {
        List<String> received = new ArrayList<>();
        Thread producer = new Thread(() -> {
            try {
                for (String text : texts) {
                    put.put(text);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "text-producer");
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < texts.size(); i++) {
                    received.add(take.take());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "text-consumer");

        try {
            consumer.start();
            producer.start();
            awaitCompletion(producer);
            awaitCompletion(consumer);
            return List.copyOf(received);
        } finally {
            producer.interrupt();
            consumer.interrupt();
        }
    }

    private static void awaitCompletion(Thread worker) throws InterruptedException {
        worker.join(JOIN_TIMEOUT_MILLIS);
        if (worker.isAlive()) {
            throw new IllegalStateException(worker.getName() + " did not finish in time");
        }
    }

    @FunctionalInterface
    private interface PutText {
        void put(String text) throws InterruptedException;
    }

    @FunctionalInterface
    private interface TakeText {
        String take() throws InterruptedException;
    }
}
