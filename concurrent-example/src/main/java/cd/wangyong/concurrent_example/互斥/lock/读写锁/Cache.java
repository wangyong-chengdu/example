package cd.wangyong.concurrent_example.互斥.lock.读写锁;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author andy
 * @since 2020/12/23
 */
public class Cache<K, V> {
    private final Map<K, V> map = new HashMap<>();
    private volatile boolean cacheValid;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public V get(K key) {
        readLock.lock();
        try {
            return map.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public void put(K key, V value) {
        writeLock.lock();
        try {
            map.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    public void processCachedData() {
        readLock.lock();
        if (!cacheValid) {
            readLock.unlock();
            writeLock.lock();
            try {
                if (!cacheValid) {
                    // todo:获取数据

                    cacheValid = true;
                }

                readLock.lock();
            }
            finally {
                writeLock.unlock();
            }
        }

        try {
            // todo: 使用数据
        }
        finally {
            readLock.unlock();
        }

    }
}
