package com.hx.concurrent.threads;

/**
 * @author hx
 * @date 2019/3/28 21:49
 */
public class MyThread_001 extends Thread{


    @Override
    public void run() {
        System.out.println("多线程-->继承方式");
    }

    public static void main(String[] args) {
        new MyThread_001().start();
        new MyThread_001().start();
    }

}
