package com.dhenton9000.demo.atomic;

import com.dhenton9000.demo.loop1.SingleLoopDemoOne;
import com.dhenton9000.thread.sandbox.DemoApp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.dhenton9000.thread.sandbox.ThreadUtils.*;

/**
 * DEMONSTRATES: atomic increments concurrentmap
 * http://winterbe.com/posts/2015/05/22/java8-concurrency-tutorial-atomic-concurrent-map-examples/
 *
 * @author dhenton
 */
public class AtomicDemo implements DemoApp {

    AtomicInteger atomicInt = null;
    private static final Logger LOG = LoggerFactory.getLogger(AtomicDemo.class);

    @Override
    public void doDemo() {
        doAtomicIntDemo();
        doAtomicIntDemoUpdate();
        doAtomicSum();

    }

    private void doAtomicIntDemo() {
        LOG.debug("starting atomic int demo");
        atomicInt = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, 1000)
                .forEach(i -> executor.submit(atomicInt::incrementAndGet));

        stop(executor);

        LOG.debug("Atomic int accumulator " + atomicInt.get());
    }

    private void doAtomicIntDemoUpdate() {
        LOG.debug("starting atomic int update demo");
        atomicInt = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, 1000)
                .forEach(i -> {

                    Runnable task = ()
                            -> atomicInt.updateAndGet(n -> n + 2);
                    executor.submit(task);
                });

        stop(executor);

        LOG.debug("Atomic int accumulator " + atomicInt.get());
    }

    private void doAtomicSum() {
        LOG.debug("starting atomic sum demo");
        atomicInt = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, 1000)
                .forEach(i -> {

                    Runnable task = ()
                            -> atomicInt.accumulateAndGet(i, (n, m) -> n + m);
                    executor.submit(task);
                });

        stop(executor);

        LOG.debug("Atomic sum " + atomicInt.get());
    }

}
