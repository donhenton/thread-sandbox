 

package com.dhenton9000.demo.dead.bank;

 
public class UnSyncedBank extends Bank{

    public UnSyncedBank(int numberOfAccounts) {
        super(numberOfAccounts);
    }

   
    
     @Override
     public String transfer(int from, int to, int amount) {
        String info = null;
        if (amount <= getAccounts()[from].getBalance()) {
            getAccounts()[from].withdraw(amount);
            getAccounts()[to].deposit(amount);

            String message = "%s transfered %d from %s to %s. Total balance: %d";
            String threadName = Thread.currentThread().getName();
            int totalBalance = getTotalBalance();
            if (totalBalance != getExpectedBalance()) {
             info = String.format(message, threadName, amount, from, to, totalBalance);
             setExpectedBalance(totalBalance);
            }
           // LOG.debug(info);
            
        }
         return info;
    }

     
    
    
}
