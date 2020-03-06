package com.hx.concurrent.threads;

import java.util.concurrent.*;

/**
 * @Descrition TODO
 * @Author houxi
 * @Date 2019/4/24 17:12
 **/
public class executors {
    /** 核心线程数 */
    private static final int corePoolSize = 5;
    /** 最大线程数 */
    private static final int maximumPoolSize = 10;
    /** 超时时间，特指超出核心线程数量以外的线程空余存活时间 */
    private static final long keepAliveTime = 60L;
    /** 存活时间单位*/
    private static final TimeUnit unit = TimeUnit.SECONDS;
    /** 保存执行任务的队列*/

    public static void main(String[] args)  {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

}
