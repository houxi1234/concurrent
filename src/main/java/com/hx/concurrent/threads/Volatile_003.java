package com.hx.concurrent.threads;

import java.util.concurrent.CountDownLatch;

/**
 * @ClassName Volatile_003
 * @Descrition TODO
 * @Author houxi
 * @Date 2019/4/15 19:46
 * Version 1.0
 **/
public class Volatile_003 {
    volatile int i;
    public void incr(){
        i++;
    }
    public static void main(String[] args) throws InterruptedException {
        Volatile_003 volatile_003 = new Volatile_003();
        for (int i = 0; i < 1000; i++) {
            new Thread(()->{
                volatile_003.incr();

            }).start();
        }
        //保证前面的线程都执行完
        while(Thread.activeCount()>1)  {
            Thread.yield();
        }
        System.out.println(volatile_003.i);
    }

}
//CountDownLatch latch = new CountDownLatch(times);
//        //并发测试
//        for (int i = 0; i < times; i++) {
//            Thread thread = new Thread() {
//                @Override
//                public void run() {
//                    try {
//                        latch.await();
//                        GirlFriendTypeThree instance = GirlFriendTypeThree.getInstance();
//                        System.out.println(instance);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//            thread.start();
//            latch.countDown();
//        }
//    }