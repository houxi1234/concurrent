package com.hx.concurrent.threads;

/**
 * @ClassName d
 * @Descrition TODO
 * @Author houxi
 * @Date 2019/4/15 20:40
 * Version 1.0
 **/
public class NotifyDemo extends Thread{
    private Object lock;

    public NotifyDemo(Object lock) {
        this.lock = lock;
    }
    @Override
    public void run() {
        synchronized (lock){
            System.out.println("开始执行 thread notify");
            lock.notify();
            System.out.println("执行结束 thread notify");
        }
    }
}
