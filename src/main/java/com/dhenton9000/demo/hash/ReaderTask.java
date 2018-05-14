package com.dhenton9000.demo.hash;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReaderTask implements Runnable {

    private final ConcurrentHashMap<Integer, String> map;
    private final String name;
    private static final Logger LOG = LoggerFactory.getLogger(ReaderTask.class);
    private final LocalDateTime start;

    public ReaderTask(ConcurrentHashMap<Integer, String> map, String threadName,LocalDateTime start) {
        this.map = map;
        this.name = threadName;
        this.start = start;
        
    }

    @Override
    public void run() {

        ConcurrentHashMap.KeySetView<Integer, String> keySetView = map.keySet();
        Iterator<Integer> iterator = keySetView.iterator();
        LocalDateTime current = LocalDateTime.now();
        Duration diff = Duration.between( this.start,current);
        
       
      // ChronoUnit.SECONDS.between(current,this.now);

        
        String output = "time ("+diff.getSeconds() + ") " + name + ": ";

        while (iterator.hasNext()) {
            Integer key = iterator.next();
            String value = this.map.get(key);
            output += key + "=>" + value + "; ";
        }

        LOG.debug(output);

        //300
    }
}
