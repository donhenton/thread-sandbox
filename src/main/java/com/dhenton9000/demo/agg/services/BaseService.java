/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.demo.agg.services;

import com.dhenton9000.demo.agg.FutureAggregator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public abstract class BaseService implements IService {
private static final Logger LOG = LoggerFactory.getLogger(BaseService.class);
//    private Timer timer = new Timer();
//    private TimerTask task;
//
//    public void initialize() {
//
//        task = new TimerTask() {
//
//            @Override
//            public void run() {
//
//            }
//
//        };
//        timer.schedule(task, setDelay());
//    }

    public abstract long setDelay();
    public abstract String getInfo();

    @Override
    public String getMessage(String input) {

        try
        {
            Thread.sleep(setDelay());
            
        }
        catch (InterruptedException ie)
        {
            
        }
        LOG.info("service "+getInfo());
        return input + " "+ getInfo();
    }
}
