/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.demo.agg;
//http://java.dzone.com/articles/javautilconcurrentfuture

import com.dhenton9000.demo.agg.services.AlphaService;
import com.dhenton9000.demo.agg.services.BetaService;
import com.dhenton9000.demo.agg.services.GammaService;
import com.dhenton9000.demo.agg.services.IService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class FutureAggregator {

    IService alphaService = new AlphaService();
    IService betaService = new BetaService();
    IService gammaService = new GammaService();
    List<IService> services = new ArrayList<>();
    List<Future<String>> futures = new ArrayList<>();
    private static final Logger LOG = LoggerFactory.getLogger(FutureAggregator.class);

    public FutureAggregator() {
        services.add(alphaService);
        services.add(betaService);
        services.add(gammaService);

    }

    public void doAggregate() {
        ExecutorService executor = Executors.newFixedThreadPool(services.size());
        for (final IService service : services) {

            Future<String> oneFuture = executor.submit(new Callable<String>() {

                @Override
                public String call() throws Exception {
                    return service.getMessage("call ");
                }

            });
            futures.add(oneFuture);

        }// end for services loop
        String totalString = "";
        // this says you are loaded, now do your thing
        // the gets below will 'keep looking' until they are down

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                // the first shutdown didn't succeed

                LOG.error("first time shutdown fail");
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            LOG.error("Couldn't shut down thread pool", ex);
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        } finally {
            if (!executor.isTerminated()) {
                LOG.error("cancel non-finished tasks");
            }
            executor.shutdownNow();
            LOG.info("shutdown finished");
        }

        // The gets block so use a timeout
        for (Future<String> f : futures) {
            try {
                //  totalString += f.get();
                totalString += f.get(200, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ex) {
                LOG.error("interrupt ex on get " + ex.getMessage());
            } catch (ExecutionException ex) {
                LOG.error("Exceution ex on get " + ex.getMessage());
            } catch (TimeoutException ex) {
                LOG.error("Timeout ex on get " + ex.getMessage());
            }
        }

        LOG.info("\nFinished with\n" + totalString);

    }// end doAggregate
}
