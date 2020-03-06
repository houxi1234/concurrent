package com.hx.concurrent.threads;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName AtomicDemo
 * @Descrition TODO
 * @Author houxi
 * @Date 2019/4/17 17:29
 * Version 1.0
 **/
public class LockDemo {

    private static int count = 0;
    static Lock lock = new ReentrantLock();

    public static void inc() {
        lock.lock();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count++;
        lock.unlock();
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                LockDemo.inc();
            }).start();
        }
        Thread.sleep(3000);
        System.out.println("result:" + count);
    }
}
