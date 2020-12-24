package cd.wangyong.concurrent_example.互斥.lock;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.StampedLock;

/**
 * 计算到原点的距离
 * @author andy
 * @since 2020/12/23
 */
public class StampedLockExample {

    static class Point {
        private final StampedLock stampedLock = new StampedLock();

        private double x;
        private double y;

        /**
         * 计算到原点的距离
         */
        public double distanceFromOrigin() {
            // 无锁乐观读
            long stamp = stampedLock.tryOptimisticRead();

            double curX = x, curY = y;

            // 如果同时存在写操作，则验证不通过，升级为悲观读
            if (!stampedLock.validate(stamp)) {
                stamp = stampedLock.readLock();
                try {
                    curX = x;
                    curY = y;
                }
                finally {
                    stampedLock.unlockRead(stamp);
                }
            }
            return Math.sqrt(curX * curX + curY * curY);
        }

        // 存在问题的方法
        void moveIfAtOrigin(double newX, double newY) {
            long stamp = stampedLock.readLock();
            try {
                while (x == 0.0 && y == 0.0) {
                    long ws = stampedLock.tryConvertToWriteLock(stamp);
                    if (ws != 0L) {
                        x = newX;
                        y = newY;
                        stamp = ws;
                        break;
                    } else {
                        stampedLock.unlockRead(stamp);
                        stamp = stampedLock.writeLock();
                    }
                }
            } finally {
                stampedLock.unlock(stamp);
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        final StampedLock lock = new StampedLock();

        Thread t1 = new Thread(() -> {
            lock.writeLock();
            LockSupport.park();
        });
        t1.start();
        Thread.sleep(100);

        Thread t2 = new Thread(() -> {
            lock.readLock();
        });
        t2.start();
        Thread.sleep(100);

        t2.interrupt();
        t2.join();
    }

    public static void main2(String[] args) {

    }
}
