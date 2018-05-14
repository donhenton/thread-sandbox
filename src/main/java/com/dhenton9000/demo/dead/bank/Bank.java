package com.dhenton9000.demo.dead.bank;

import com.dhenton9000.demo.dead.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bank.java This class represents a bank that manages accounts and provides
 * money transfer function.
 *
 * @author www.codejava.net
 */
public abstract class Bank {
    public static final int MAX_AMOUNT = 100;
    public static final int INITIAL_BALANCE = 100;
    private final static Logger LOG
            = LoggerFactory.getLogger(Bank.class);

    private final Account[] accounts ;
    private  int expectedBalance;

    public Bank(int numberOfAccounts) {
        this.expectedBalance = numberOfAccounts * INITIAL_BALANCE;
        LOG.debug("expected is "+this.expectedBalance);
        this.accounts = new Account[numberOfAccounts];
        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new Account(INITIAL_BALANCE);
        }
    }

    public abstract String transfer(int from, int to, int amount) ;

    protected int getTotalBalance() {
        int total = 0;

        for (Account account : getAccounts()) {
            total += account.getBalance();
        }

        return total;
    }

    /**
     * @return the accounts
     */
    protected Account[] getAccounts() {
        return accounts;
    }

    /**
     * @return the expectedBalance
     */
    public int getExpectedBalance() {
        return expectedBalance;
    }
    
    public void setExpectedBalance(int i) {
        this.expectedBalance = i;
    }
}
