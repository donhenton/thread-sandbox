package com.dhenton9000.demo.dead;

// http://www.codejava.net/java-core/concurrency/java-synchronization-tutorial-part-1-the-problems-of-unsynchronized-code
import com.dhenton9000.demo.dead.bank.Bank;
import com.dhenton9000.demo.dead.bank.LockBank;
import com.dhenton9000.demo.dead.bank.UnSyncedBank;
import com.dhenton9000.thread.sandbox.DemoApp;
import static com.dhenton9000.thread.sandbox.ThreadUtils.shutdownAndAwaitTermination;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeadLockDemo implements DemoApp {

    public static final int NUMBER_OF_ACCTS = 30;
    private static final Logger LOG = LoggerFactory.getLogger(DeadLockDemo.class);
    // show or solve
    private String actionType = null;
    private final Bank unSyncedBank = new UnSyncedBank(NUMBER_OF_ACCTS);
    private final Bank lockBank = new LockBank(NUMBER_OF_ACCTS);

    public DeadLockDemo(String actionType) {

        this.actionType = actionType;

    }

    @Override
    public void doDemo() {

        this.doDemoCode();

    }

    private void doDemoCode() {

        ScheduledExecutorService executor
                = Executors.newScheduledThreadPool(NUMBER_OF_ACCTS);
        List<ScheduledFuture> futures = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_ACCTS; i++) {

            Runnable selectedItem = null;
            if (this.actionType.equals("show")) {
                selectedItem = new Transaction(unSyncedBank, i);
            }

            if (this.actionType.equals("lock")) {
                 selectedItem = new Transaction(lockBank, i);
            }

            if (selectedItem == null) {
                throw new RuntimeException("parm must be 'lock' or 'show'");
            }
            ScheduledFuture<?> readScheduledItem
                    = executor.scheduleAtFixedRate(selectedItem, 0, 1, TimeUnit.MILLISECONDS);
            futures.add(readScheduledItem);

        }
        futures.forEach(f -> {

            try {
                f.get();
            } catch (ExecutionException e) {
                Throwable rootException = e.getCause();
                LOG.error("Error in thread ", rootException);
                shutdownAndAwaitTermination(executor);

            } catch (InterruptedException ex) {
                LOG.error("Interrupt Issue " + ex.getMessage());
            }

        });

    }
}
