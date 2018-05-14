package com.dhenton9000.demo.dead.bank;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.LoggerFactory;



// http://www.codejava.net/java-core/concurrency/understanding-deadlock-livelock-and-starvation-with-code-examples-in-java

public class DeadLockBank extends Bank {

    private final Lock bankLock;
    private final Condition availableFund;
    private final static org.slf4j.Logger LOG
            = LoggerFactory.getLogger(Bank.class);

    public DeadLockBank(int numberOfAccounts) {
        super(numberOfAccounts);
        bankLock = new ReentrantLock();
        availableFund = bankLock.newCondition();
    }

    @Override
    public String transfer(int from, int to, int amount) {
        String info = null;
        bankLock.lock();
        try {

            while (getAccounts()[from].getBalance() < amount) {
                availableFund.await();
            }

            // if (amount <= getAccounts()[from].getBalance()) {
            getAccounts()[from].withdraw(amount);
            getAccounts()[to].deposit(amount);

            String message = "%s transfered %d from %s to %s. Total balance: %d";
            String threadName = Thread.currentThread().getName();
            int totalBalance = getTotalBalance();
            info = String.format(message, threadName, amount, from, to, totalBalance);
            LOG.debug(info);
            if (totalBalance != getExpectedBalance()) {
                info = String.format("FAIL "+ message, threadName, amount, from, to, totalBalance);
                setExpectedBalance(totalBalance);
            }

            return info;
        } catch (InterruptedException ex) {
            LOG.error("interrupt " + ex.getMessage(), ex);
            return null;
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

    @Override
    protected int getMaxAmount() {
        return 250;
    }

}
