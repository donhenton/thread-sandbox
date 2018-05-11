/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.future.demo;
//http://java.dzone.com/articles/javautilconcurrentfuture

import com.dhenton9000.future.demo.services.AlphaService;
import com.dhenton9000.future.demo.services.BetaService;
import com.dhenton9000.future.demo.services.GammaService;
import com.dhenton9000.future.demo.services.IService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
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
        }

        for (Future<String> f : futures) {
            try {
                totalString += f.get();
            } catch (InterruptedException ex) {
                LOG.error("interrupt ex on get " + ex.getMessage());
            } catch (ExecutionException ex) {
                LOG.error("Exceution ex on get " + ex.getMessage());
            }
        }

        LOG.info("\nFinished with\n" + totalString);

    }// end doAggregate
}
