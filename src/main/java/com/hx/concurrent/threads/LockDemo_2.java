package com.hx.concurrent.threads;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @ClassName LockDemo_2
 * @Descrition TODO
 * @Author houxi
 * @Date 2019/4/17 17:41
 * Version 1.0
 **/
public class LockDemo_2 {
    static Map<String, Object> cacheMap = new HashMap<>();
    static ReadWriteLock rw = new ReentrantReadWriteLock();
    static Lock read = rw.readLock();
    static Lock write = rw.writeLock();

    public static final Object get(String key) {
        System.out.println("开始读取数据");
        read.lock(); //读锁
        try {
            return cacheMap.get(key);
        } finally {
            read.unlock();
        }
    }

    public static final Object put(String key, Object value) {
        write.lock();
        System.out.println("开始写数据");
        try {
            return cacheMap.put(key, value);
        } finally {
            write.unlock();
        }
    }
}
