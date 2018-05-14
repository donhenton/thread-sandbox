package com.dhenton9000.demo.simple.join;

import com.dhenton9000.thread.sandbox.DemoApp;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * join is for threads that are not thru the executor service, this does the
 * same thing, but via the Executors
 *
 * @author dhenton
 */
public class SimpleJoinDemo implements DemoApp {

    private int count = 0;
    private final static Logger LOG
            = LoggerFactory.getLogger(SimpleJoinDemo.class);

    @Override
    public void doDemo() {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable r1 = () -> {
            LOG.debug("Entered Thread 1");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            LOG.debug("Exiting Thread 1");
        };
        Runnable r2 = () -> {
            LOG.debug("Entered Thread 2");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            LOG.debug("Exiting Thread 2");
        };

        LOG.debug("Starting thread 1");
        Future<?> tresult = executor.submit(r1);
        LOG.debug("Waiting for Thread 1 to complete");

        try {
            tresult.get(10, TimeUnit.SECONDS); //blocks
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            LOG.error("problem with r1 submit " + ex.getClass().getName()
                    + " " + ex.getMessage());
        }

        LOG.debug("Waited enough! Starting Thread 2 now");
        Future<?> tresult1 = executor.submit(r2);

        try {
            tresult1.get(10, TimeUnit.SECONDS); //blocks
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            LOG.error("problem with r2 submit " + ex.getClass().getName()
                    + " " + ex.getMessage());
        }
        
        
        executor.shutdown();

    }

}
