package com.dhenton9000.demo.hash;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriterTask implements Runnable {

    private final ConcurrentMap<Integer, String> map;
    private final String name;
    private static final Logger LOG = LoggerFactory.getLogger(WriterTask.class);
    private final LocalDateTime start;

    public WriterTask(ConcurrentMap<Integer, String> map, String name, LocalDateTime start) {
        this.map = map;
        this.name = name;
        this.start = start;
    }

    @Override
    public void run() {

        Integer key = ThreadLocalRandom.current().nextInt(0, 10 + 1);
        String value = name;
        LocalDateTime current = LocalDateTime.now();
        long diff = ChronoUnit.SECONDS.between(this.start,current);

        if (map.putIfAbsent(key, value) == null) {
             
            String output = String.format("time (%d): %s has put [%d => %s]",
                    diff, name, key, value);
            LOG.debug(output);
        }

        Integer keyToRemove = ThreadLocalRandom.current().nextInt(0, 10 + 1);

        if (map.remove(keyToRemove, value)) {
             
            String output = String.format("time (%d): %s has removed [%d => %s]",
                    diff, name, keyToRemove, value);
            LOG.debug(output);
        }

    }
    
    
    
    
}
