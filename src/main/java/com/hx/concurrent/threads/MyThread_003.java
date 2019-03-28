package com.hx.concurrent.threads;

import java.util.concurrent.*;

/**
 * @author hx
 * @date 2019/3/28 21:59
 */
public class MyThread_003 implements Callable<String> {

    @Override
    public String call() throws Exception {
        return "多线程-->Callable实现";
    }
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(1);

        Callable<String> callable = new MyThread_003();
        Future<String> submit = executor.submit(callable);
        String s = submit.get();
        System.out.println(s);
        executor.shutdown();
    }
}
