package ru.academy.textapi.multithreading;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/multithreading/runs")
public class ThreadLogController {
    private static final int MAX_ITERATIONS = 1000;
    private static final long PAUSE_MILLIS = 100;

    private Run latestRun;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public synchronized void startRun(
            @RequestParam(name = "iterations", defaultValue = "10") int iterations,
            @RequestParam(name = "sleep", defaultValue = "false") boolean sleep,
            @RequestParam(name = "differentPriorities", defaultValue = "false") boolean differentPriorities) {
        if (iterations < 1 || iterations > MAX_ITERATIONS) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "iterations must be between 1 and " + MAX_ITERATIONS);
        }
        if (latestRun != null && latestRun.isRunning()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A run is already in progress");
        }

        latestRun = new Run(iterations, sleep, differentPriorities);
        latestRun.start();
    }

    @GetMapping
    public synchronized List<LogEntry> getLog() {
        return latestRun == null ? List.of() : latestRun.snapshot();
    }

    public record LogEntry(String threadName, int iteration) {
    }

    private static final class Run {
        private final List<LogEntry> entries = Collections.synchronizedList(new ArrayList<>());
        private final Thread firstWorker;
        private final Thread secondWorker;

        private Run(int iterations, boolean sleep, boolean differentPriorities) {
            Runnable task = () -> appendEntries(iterations, sleep);
            firstWorker = new Thread(task, "log-worker-1");
            secondWorker = new Thread(task, "log-worker-2");
            firstWorker.setDaemon(true);
            secondWorker.setDaemon(true);
            if (differentPriorities) {
                firstWorker.setPriority(Thread.MIN_PRIORITY);
                secondWorker.setPriority(Thread.MAX_PRIORITY);
            }
        }

        private void start() {
            firstWorker.start();
            try {
                secondWorker.start();
            } catch (RuntimeException | Error failure) {
                firstWorker.interrupt();
                throw failure;
            }
        }

        private boolean isRunning() {
            return firstWorker.isAlive() || secondWorker.isAlive();
        }

        private void appendEntries(int iterations, boolean sleep) {
            Thread worker = Thread.currentThread();
            try {
                for (int iteration = 1; iteration <= iterations && !worker.isInterrupted(); iteration++) {
                    entries.add(new LogEntry(worker.getName(), iteration));
                    if (sleep && iteration < iterations) {
                        Thread.sleep(PAUSE_MILLIS);
                    }
                }
            } catch (InterruptedException interruption) {
                worker.interrupt();
            }
        }

        private List<LogEntry> snapshot() {
            synchronized (entries) {
                return List.copyOf(entries);
            }
        }
    }
}
