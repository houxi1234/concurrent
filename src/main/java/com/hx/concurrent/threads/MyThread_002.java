package com.hx.concurrent.threads;

/**
 * @author hx
 * @date 2019/3/28 21:54
 */
public class MyThread_002 implements Runnable {
    @Override
    public void run() {
//        System.out.println("多线程-->实现方式");
    }
    public static void main(String[] args) {
       new Thread(new MyThread_002()).start();
       new Thread(new MyThread_002()).start();
    }
}
