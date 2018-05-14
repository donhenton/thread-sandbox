package com.dhenton9000.demo.vol;

import com.dhenton9000.thread.sandbox.DemoApp;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//https://www.callicoder.com/java-concurrency-issues-and-thread-synchronization/
public class VoliatleDemo implements DemoApp {

    private final static Logger LOG
            = LoggerFactory.getLogger(VoliatleDemo.class);

    // remove the volatile keyword to see what happens
    // the app will freeze as the thread doesn't know about
    // mods to the boolean done by the main thread
    
    // private static volatile boolean sayHello = false;
    private static   boolean sayHello = false;

    @Override
    public void doDemo() {
        Thread thread = new Thread(() -> {
            while (!sayHello) {
            }

            LOG.debug("Hello World!");

            while (sayHello) {
            }

            LOG.debug("Good Bye!");
        });

        thread.start();

        try {
            Thread.sleep(1000);

            LOG.debug("Say Hello..");
            sayHello = true;

            Thread.sleep(1000);
            LOG.debug("Say Bye..");
            sayHello = false;

        } catch (InterruptedException ex) {
            LOG.error("interrupt problem "+ex.getMessage(),ex);
        }
    }

}
