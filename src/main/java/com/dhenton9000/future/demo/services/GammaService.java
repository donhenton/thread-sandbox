/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dhenton9000.future.demo.services;

/**
 *
 * @author dhenton
 */
public class GammaService extends BaseService {

    
      

    @Override
    public long setDelay() {
         return 300L;
    }

    @Override
    public String getInfo() {
       return "gamma ";
    }
    
}
