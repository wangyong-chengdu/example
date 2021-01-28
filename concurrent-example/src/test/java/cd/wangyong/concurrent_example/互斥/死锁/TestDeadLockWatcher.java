package cd.wangyong.concurrent_example.互斥.死锁;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author andy
 * @since 2021/1/18
 */
@RunWith(JUnit4.class)
public class TestDeadLockWatcher {

    @Test
    public void testDeadLock() throws InterruptedException {
        // 打开死锁监控：注意有性能开销
        DeadLockWatcher.watch();

        final String lockA = "lockA";
        final String lockB = "lockB";

        DeadLockExample2 t1 = new DeadLockExample2("Thread1", lockA, lockB);
        DeadLockExample2 t2 = new DeadLockExample2("Thread2", lockB, lockA);

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    @Test
    public void testNoDeadLock() throws InterruptedException {
        // 打开死锁监控：注意有性能开销
        DeadLockWatcher.watch();

        final String lockA = "lockA";
        final String lockB = "lockB";

        DeadLockExample2 t1 = new DeadLockExample2("Thread1", lockA, lockB);
        DeadLockExample2 t2 = new DeadLockExample2("Thread2", lockA, lockB);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        Thread.sleep(1000000000);
    }

}
