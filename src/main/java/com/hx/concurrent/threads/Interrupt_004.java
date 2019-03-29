package com.hx.concurrent.threads;

import java.util.concurrent.TimeUnit;

/**
 * @author hx
 * @date 2019/3/29 21:21
 */
public class Interrupt_004 {

    private volatile static boolean stop = true;
    private static int i;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(()->{
            while (stop) {
                i++;
            }
            System.out.println(i);
        }, "demo");
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        stop = false;
    }
}
