package com.hx.concurrent.threads;

import java.util.concurrent.TimeUnit;

/**
 * @author hx
 * @date 2019/3/29 21:06
 */
public class Interrupt_003 {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(()->{
            while (true) {
                try{
                    Thread.sleep(10000L);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }, "demo");
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread.interrupt();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(thread.isInterrupted());
    }
}
