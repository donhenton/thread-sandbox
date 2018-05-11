package com.dhenton9000.thread.sandbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.dhenton9000.thread.sandox")
@SpringBootApplication
public class BasicApplication {
    
     private final static Logger LOG
            = LoggerFactory.getLogger(BasicApplication.class);
    
    

	public static void main(String[] args) {
		SpringApplication.run(BasicApplication.class, args);
	}
}
