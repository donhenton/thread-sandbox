package com.dhenton9000.demo.aref;

import com.dhenton9000.demo.atomic.AtomicDemo;
import com.dhenton9000.thread.sandbox.DemoApp;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.LoggerFactory;

/**
 * https://examples.javacodegeeks.com/core-java/util/concurrent/atomic/atomicreference/java-atomicreference-example/
 *
 * @author dhenton
 */
public class AtomicRefDemo implements DemoApp {

    private static String message;
    private static Person person;
    private static AtomicReference<String> aRmessage;
    private static AtomicReference<Person> aRperson;
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(AtomicDemo.class);

    @Override
    public void doDemo() {

        Thread t1 = new Thread(new MyRun1());
        Thread t2 = new Thread(new MyRun2());
        message = "hello";
        person = new Person("Phillip", 23);
        aRmessage = new AtomicReference<>(message);
        aRperson = new AtomicReference<>(person);
        LOG.debug("Message is: " + message
                + "\nPerson is " + person.toString());
        LOG.debug("Atomic Reference of Message is: " + aRmessage.get()
                + "\nAtomic Reference of Person is " + aRperson.get().toString());
        t1.start();
        t2.start();

        try {
            //this pauses the CURRENT MAIN THREAD until t1 and t2 are complete
            t1.join();
            t2.join();
        } catch (InterruptedException ex) {
            LOG.error("interrupt problem " + ex.getMessage());
        }

        LOG.debug("\nNow Message is: " + message
                + "\nPerson is " + person.toString());
        LOG.debug("Atomic Reference of Message is: " + aRmessage.get()
                + "\nAtomic Reference of Person is " + aRperson.get().toString());
    }

    private class MyRun1 implements Runnable {

        @Override
        public void run() {
            aRmessage.compareAndSet(message, "Thread 1");
            message = message.concat("-Thread 1!");
            person.setAge(person.getAge() + 1);
            person.setName("Thread 1");
            aRperson.getAndSet(new Person("Thread 1", 1));
            LOG.debug("\n" + Thread.currentThread().getName() + " Values "
                    + message + " - " + person.toString());
            LOG.debug("\n" + Thread.currentThread().getName() + " Atomic References "
                    + message + " - " + person.toString());
        }
    }

    private class MyRun2 implements Runnable {

        @Override
        public void run() {
            message = message.concat("-Thread 2");
            person.setAge(person.getAge() + 2);
            person.setName("Thread 2");
            aRmessage.lazySet("Thread 2");
            aRperson.set(new Person("Thread 2", 2));
            LOG.debug("\n" + Thread.currentThread().getName() + " Values: "
                    + message + " - " + person.toString());
            LOG.debug("\n" + Thread.currentThread().getName() + " Atomic References: "
                    + aRmessage.get() + " - " + aRperson.get().toString());
        }
    }

    private class Person {

        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "[name " + this.name + ", age " + this.age + "]";
        }
    }
}
