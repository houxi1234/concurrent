package com.hx.concurrent.threads;

/**
 * @ClassName D
 * @Descrition TODO
 * @Author houxi
 * @Date 2019/4/15 20:38
 * Version 1.0
 **/
public class WaitDemo extends Thread{
    private Object lock;
    public WaitDemo(Object lock) {
        this.lock = lock;
    }
    @Override
    public void run() {
        synchronized (lock){
            System.out.println("开始执行 thread wait");
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("执行结束 thread wait");
        }
    }
    public static void main(String[] args) {
        Object lock = new Object();
        new WaitDemo(lock).start();
        new NotifyDemo(lock).start();
    }
}
