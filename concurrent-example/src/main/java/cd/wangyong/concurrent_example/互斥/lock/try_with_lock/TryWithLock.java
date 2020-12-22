package cd.wangyong.concurrent_example.互斥.lock.try_with_lock;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author andy
 * @since 2020/9/30
 */
public class TryWithLock implements Lock, Closeable {
    private final Lock lock;

    public TryWithLock(Lock lock) {
        this.lock = lock;
    }

    @Override
    public void close() throws IOException {
        lock.unlock();
    }

    @Override
    public void lock() {
        this.lock.lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        this.lock.lockInterruptibly();
    }

    @Override
    public boolean tryLock() {
        return this.lock.tryLock();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return this.lock.tryLock(time, unit);
    }

    @Override
    public void unlock() {
        this.lock.unlock();
    }

    @Override
    public Condition newCondition() {
        return this.lock.newCondition();
    }
}
