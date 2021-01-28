package cd.wangyong.concurrent_example.互斥.死锁;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author andy
 * @since 2020/9/30
 */
public class DeadLockExample {
    private Lock lockA = new ReentrantLock();
    private Lock lockB = new ReentrantLock();

    public void func1() {
        while (true) {
            System.out.println("Thread1:Try to acquire lockA...");
            lockA.lock();
            System.out.println("Thread1:lockA acquired, Try acquire lockB");
            lockB.lock();
            System.out.println("Thread1:Both lockA and lockB acquired.");
            lockB.unlock();
            lockA.unlock();
        }
    }

    public void func2() {
        while (true) {
            System.out.println("Thread2:Try to acquire lockB...");
            lockB.lock();
            System.out.println("Thread2:lockB acquired, Try acquire lockA");
            lockA.lock();
            System.out.println("Thread2:Both lockA and lockB acquired.");
            lockA.unlock();
            lockB.unlock();
        }
    }

    public static void main(String[] args) {
        DeadLockExample deadLockExample = new DeadLockExample();
        Thread t1 = new Thread(() -> deadLockExample.func1());
        Thread t2 = new Thread(() -> deadLockExample.func2());

        t1.start();
        t2.start();

        while (Thread.activeCount() > 0) {
            Thread.yield();
        }
    }

}
