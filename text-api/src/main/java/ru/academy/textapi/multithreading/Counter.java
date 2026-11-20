package ru.academy.textapi.multithreading;

import java.util.concurrent.atomic.AtomicInteger;

final class Counter {
    private int plainValue;
    private int synchronizedValue;
    private final AtomicInteger atomicValue = new AtomicInteger();

    void incrementPlain() {
        plainValue++;
    }

    synchronized void incrementSynchronized() {
        synchronizedValue++;
    }

    void incrementAtomic() {
        atomicValue.incrementAndGet();
    }

    int plainValue() {
        return plainValue;
    }

    synchronized int synchronizedValue() {
        return synchronizedValue;
    }

    int atomicValue() {
        return atomicValue.get();
    }
}
