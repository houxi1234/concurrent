package com.hx.concurrent.threads;

/**
 * @author hx
 * @date 2019/4/1 20:06
 */
public class Volatile_002 {
    private static volatile boolean flag = false;
    private static int i;
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
            while (!flag) {
                i++;
            }
        });
        t1.start();
        Thread.sleep(1000);
        flag = true;
        System.out.println(i);
    }
}
