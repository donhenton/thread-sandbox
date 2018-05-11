package com.dhenton9000.thread.sandbox;

import com.dhenton9000.demo.agg.FutureAggregator;
import com.dhenton9000.demo.loop1.SingleLoopDemoOne;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;

@ComponentScan(basePackages = "com.dhenton9000.thread.sandox")
@SpringBootApplication
public class BasicApplication {

    @Value("${app}")
    private String app;

    private final static Logger LOG
            = LoggerFactory.getLogger(BasicApplication.class);

    public static void main(String[] args) {

        ConfigurableApplicationContext contextVar
                = SpringApplication.run(BasicApplication.class, args);
        contextVar.getBean(BasicApplication.class).beginApplication();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        LOG.debug("hello world, I have just started up");
    }

    private void beginApplication() {
        LOG.debug("app is " + app);
        
        if (app.equals("agg")) {
            FutureAggregator agg = new FutureAggregator();
            agg.doAggregate();
        }
        if (app.equals("loop1")) {
            SingleLoopDemoOne demo = new SingleLoopDemoOne();
            demo.doDemo();
        }

    }

}
