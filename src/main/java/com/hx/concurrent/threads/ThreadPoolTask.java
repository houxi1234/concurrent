package com.hx.concurrent.threads;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Descrition TODO
 * @Author houxi
 * @Date 2019/4/24 20:40
 **/
public class ThreadPoolTask implements Runnable {

    private Object threadPoolTaskData;

    private static int consumerTaskSleepTime = 2000;

    ThreadPoolTask(Object tasks) {
        this.threadPoolTaskData = tasks;
    }
    @Override
    public void run() {
        // 处理一个任务，这里的处理方式太简单了，仅仅是一个打印语句
        System.out.println("start .." + threadPoolTaskData);
        try {
            //便于观察，等待一段时间
            Thread.sleep(consumerTaskSleepTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("finish " + threadPoolTaskData);
        threadPoolTaskData = null;
    }
}