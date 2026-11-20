package ru.academy.textapi.multithreading;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

public final class BoundedTextQueue {
    private final Queue<String> texts = new ArrayDeque<>();
    private final int capacity;

    public BoundedTextQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
    }

    public synchronized void put(String text) throws InterruptedException {
        Objects.requireNonNull(text, "text");
        while (texts.size() == capacity) {
            wait();
        }
        texts.add(text);
        notifyAll();
    }

    public synchronized String take() throws InterruptedException {
        while (texts.isEmpty()) {
            wait();
        }
        String text = texts.remove();
        notifyAll();
        return text;
    }
}
