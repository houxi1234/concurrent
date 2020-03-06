package com.hx.concurrent.threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName ConditionDemoSignal
 * @Descrition TODO
 * @Author houxi
 * @Date 2019/4/17 20:41
 * Version 1.0
 **/
public class ConditionDemoSignal implements Runnable {
    private Lock lock;
    private Condition condition;

    public ConditionDemoSignal(Lock lock, Condition condition) {
        this.lock = lock;
        this.condition = condition;
    }

    @Override
    public void run() {
        System.out.println("begin -ConditionDemoSignal");
        try {
            lock.lock();
            condition.signal();
            System.out.println("end - ConditionDemoSignal");
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        new Thread(new ConditionDemoWait(lock, condition)).start();
        new Thread(new ConditionDemoSignal(lock, condition)).start();
    }
}
