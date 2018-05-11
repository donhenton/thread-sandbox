package com.dhenton9000.demo.sync1;

import com.dhenton9000.thread.sandbox.DemoApp;
import java.util.concurrent.ExecutorService;
import static com.dhenton9000.thread.sandbox.ThreadUtils.*;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://winterbe.com/posts/2015/04/30/java8-concurrency-tutorial-synchronized-locks-examples/
 *
 * When unsynchronized, the counter can be accessed by threads and that access
 * might not be in order, so when the method sends the value back to storage it
 * has an older value, and makes the storage 'step back'
 *
 * @author dhenton
 */
public class SyncOneDemo implements DemoApp {

    private int count = 0;
    private final static Logger LOG
            = LoggerFactory.getLogger(SyncOneDemo.class);
    private ReentrantLock lock = new ReentrantLock();

    @Override
    public void doDemo() {
        unsynchronizedDemo();
        count = 0;
        synchronizedDemo();
        count = 0;
        reEntrantDemo();
        count = 0;
        competeForLockDemo();
    }

    private void unsynchronizedDemo() {
        LOG.debug("beginning demo unsynchronized");
        ExecutorService executor = Executors.newFixedThreadPool(3);

        IntStream.range(0, 10000)
                .forEach(i -> executor.submit(this::increment));

        stop(executor);

        LOG.debug("Finished count should be 10000 but is " + count);  // 9965
    }

    private void synchronizedDemo() {
        LOG.debug("beginning demo unsynchronized");
        ExecutorService executor = Executors.newFixedThreadPool(3);

        IntStream.range(0, 10000)
                .forEach(i -> executor.submit(this::incrementSync));

        stop(executor);

        LOG.debug("Finished count should be 10000 but is " + count);
    }

    private void reEntrantDemo() {
        LOG.debug("beginning demo reentrant");
        ExecutorService executor = Executors.newFixedThreadPool(3);

        IntStream.range(0, 10000)
                .forEach(i -> executor.submit(this::incrementWithLock));

        stop(executor);

        LOG.debug("Finished count should be 10000 but is " + count);
    }

    private void competeForLockDemo() {
        LOG.debug("starting compete for lock demo");
        ExecutorService executor = Executors.newFixedThreadPool(2);
        ReentrantLock lock = new ReentrantLock();

        executor.submit(() -> {
            lock.lock();
            try {
                sleep(1);
            } finally {
                lock.unlock();
            }
        });

        executor.submit(() -> {
            LOG.debug("Locked: " + lock.isLocked());
           LOG.debug("Held by me: " + lock.isHeldByCurrentThread());
            boolean locked = lock.tryLock();
            LOG.debug("Lock acquired: " + locked);
        });

        stop(executor);

    }

    private void increment() {
        count++;
    }

    private void incrementSync() {
        synchronized (this) {
            count++;
        }

    }

    private void incrementWithLock() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

}
