package com.dhenton9000.demo.dead;

import com.dhenton9000.demo.dead.bank.Bank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Transaction implements Runnable {

    private final Bank bank;
    private final int thisAccountId;

    private final static Logger LOG
            = LoggerFactory.getLogger(Transaction.class);

    public Transaction(Bank bank, int fromAccountId) {
        this.bank = bank;

        this.thisAccountId = fromAccountId;
    }

    @Override
    public void run() {

        
            int toAccount = (int) (Math.random() * DeadLockDemo.NUMBER_OF_ACCTS);

           // if (toAccount == thisAccountId) {
           //     return;
           // }

            int amount = (int) (Math.random() * Bank.MAX_AMOUNT);

           // if (amount == 0) {
           //     return;
           // }

            String info = bank.transfer(thisAccountId, toAccount, amount);
            if (info !=null) {
               // throw new RuntimeException("data corruption\n"+info);
               LOG.warn(info);  
            }

        
    }
}

