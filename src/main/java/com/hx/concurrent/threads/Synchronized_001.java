package com.hx.concurrent.threads;

/**
 * @ClassName Synchronized_001
 * @Descrition TODO
 * @Author houxi
 * @Date 2019/4/15 19:39
 * Version 1.0
 **/
public class Synchronized_001 {
    private static int count=0;
    public static void inc(){
        synchronized (Synchronized_001.class) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count++;
        }
    }
    public static void main(String[] args) throws InterruptedException {
        for(int i=0;i<1000;i++){
            new Thread(()->Synchronized_001.inc()).start();
        }
        Thread.sleep(3000);
        System.out.println("运行结果"+count);
    }

}
