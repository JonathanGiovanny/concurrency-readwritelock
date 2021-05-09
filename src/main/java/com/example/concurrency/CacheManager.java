package com.example.concurrency;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheManager <K, V> {

    private final Map<K, V> cache;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public CacheManager() {
        this.cache = new HashMap<>();
    }

    public void addData(K key, V value) {
        writeLock.lock();
        try {
            System.out.println("Adding key");
            this.cache.put(key, value);
            Thread.sleep(2000);
            System.out.println("Added key");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Pre release write lock");
            writeLock.unlock();
            System.out.println("Post release write lock");
        }
    }

    public V get(K key) {
        System.out.print("ReadLock ");
        readLock.lock();
        try {
            return this.cache.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public void clear() {
        writeLock.lock();
        try {
            this.cache.clear();
        } finally {
            writeLock.unlock();
        }
    }

}
