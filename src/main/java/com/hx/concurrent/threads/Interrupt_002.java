package com.hx.concurrent.threads;

import java.util.concurrent.TimeUnit;

/**
 * @author hx
 * @date 2019/3/29 20:58
 */
public class Interrupt_002 {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(()->{
            while (true) {
                boolean flag = Thread.currentThread().isInterrupted();
                if (flag){
                    System.out.println("复位前：" + flag);
                    Thread.interrupted();
                    System.out.println("复位后：" +
                            Thread.currentThread().isInterrupted());

                }
            }
        }, "demo");
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread.interrupt();
    }
}
