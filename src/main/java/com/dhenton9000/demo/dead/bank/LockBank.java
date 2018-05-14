package com.dhenton9000.demo.dead.bank;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// http://www.codejava.net/java-core/concurrency/java-synchronization-tutorial-part-2-using-lock-and-condition-objects
public class LockBank extends Bank {

    private Lock bankLock;

    public LockBank(int numberOfAccounts) {
        super(numberOfAccounts);
        this.bankLock = new ReentrantLock();
    }

    @Override
    public String transfer(int from, int to, int amount) {
        String info = null;
        bankLock.lock();
        try {
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
        } finally {
            bankLock.unlock();
        }
    }

    @Override
    public int getExpectedBalance() {

        bankLock.lock();
        try {
            return super.getExpectedBalance();
        } finally {
            bankLock.unlock();
        }
    }

}
