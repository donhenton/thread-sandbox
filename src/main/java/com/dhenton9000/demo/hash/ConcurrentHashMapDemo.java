package com.dhenton9000.demo.hash;

import com.dhenton9000.thread.sandbox.DemoApp;
import static com.dhenton9000.thread.sandbox.ThreadUtils.shutdownAndAwaitTermination;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://www.codejava.net/java-core/concurrency/java-concurrent-collection-concurrenthashmap-examples
 *
 * @author dhenton
 */
public class ConcurrentHashMapDemo implements DemoApp {

    private static final Logger LOG = LoggerFactory.getLogger(ConcurrentHashMapDemo.class);
    private final ConcurrentHashMap<Integer, String> mainMap = new ConcurrentHashMap<>();
    private final LocalDateTime start = LocalDateTime.now();

    @Override
    public void doDemo() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
        List<ScheduledFuture> futures = new ArrayList<>();

        //Reader Tasks
        for (int i = 0; i < 3; i++) {
            ReaderTask readTask = new ReaderTask(mainMap, "reader" + (i + 1),start);
            ScheduledFuture<?> readScheduledItem
                    = executor.scheduleAtFixedRate(readTask, 1, 2, TimeUnit.SECONDS);
            futures.add(readScheduledItem);

        }
        
        //Writer Tasks
        for (int i = 0; i < 2; i++) {
            WriterTask writerTask = new WriterTask(mainMap, "writer" + (i + 1),start);
            ScheduledFuture<?> writeScheduledItem
                    = executor.scheduleAtFixedRate(writerTask, 1, 1, TimeUnit.SECONDS);
            futures.add(writeScheduledItem);

        }

        // this is the method of detecting errors thrown inside the loop
        futures.forEach(f -> {

            try {
                f.get();
            } catch (ExecutionException e) {
                Throwable rootException = e.getCause();
                LOG.error("Error in thread ", rootException);
                shutdownAndAwaitTermination(executor);

            } catch (InterruptedException ex) {
                LOG.error("Interrupt Issue " + ex.getMessage());
            }

        });

    }
    
   

}
