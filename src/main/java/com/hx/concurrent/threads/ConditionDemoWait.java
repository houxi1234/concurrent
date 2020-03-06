package com.hx.concurrent.threads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @ClassName R
 * @Descrition TODO
 * @Author houxi
 * @Date 2019/4/17 20:39
 * Version 1.0
 **/
public class ConditionDemoWait implements Runnable{
    private Lock lock;
    private Condition condition;
    public ConditionDemoWait(Lock lock, Condition condition){
        this.lock=lock;
        this.condition=condition;
    }
    @Override
    public void run() {
        System.out.println("begin -ConditionDemoWait");
        try {
            lock.lock();
            condition.await();
            System.out.println("end - ConditionDemoWait");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
