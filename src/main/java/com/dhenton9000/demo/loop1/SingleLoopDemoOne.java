package com.dhenton9000.demo.loop1;

 
import com.dhenton9000.thread.sandbox.DemoApp;
import com.dhenton9000.thread.sandbox.ThreadUtils;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DEMONSTRATES:
 * error handling with a Runnable
 * shutdown routine
 * 
 * @author dhenton  
 */

public class SingleLoopDemoOne implements DemoApp{

    
    private static final Logger LOG = LoggerFactory.getLogger(SingleLoopDemoOne.class);
    private int ct = 0;
    
    public void doDemo() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        DemoTask dTask = new DemoTask();

        ScheduledFuture<?> scheduleItem
                = executor.scheduleAtFixedRate(dTask, 1, 2, TimeUnit.SECONDS);
        
        try {
            scheduleItem.get();
        } catch (ExecutionException e) {
            Throwable rootException = e.getCause();
            LOG.error("Error in thread ", rootException);
            ThreadUtils.shutdownAndAwaitTermination(executor);

        } catch (InterruptedException ex) {
            LOG.error("Interrupt Issue " + ex.getMessage());
        }
        
        
    }

    private class DemoTask implements Runnable {

        public DemoTask() {
        }

        @Override
        public void run() {
            ct ++;
            LOG.debug("count is now " +ct);
            if (ct > 5) {
                throw new RuntimeException("demo error !");
            }
        }
    }

}
