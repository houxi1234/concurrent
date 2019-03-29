package com.hx.concurrent.threads;

import java.util.concurrent.TimeUnit;

/**
 * @author hx
 * @date 2019/3/29 20:32
 */
public class Interrupt_001 {

    private static int i;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(()->{

           System.out.println("isInterrupted默认值：" + Thread.currentThread().isInterrupted());

           while (!Thread.currentThread().isInterrupted()){
               i++;
           }
           System.out.println("num:" + i);
        }, "demo");
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread.interrupt();
    }
}
