package cd.wangyong.concurrent_example.分工;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author andy
 * @since 2021/1/4
 */
@RunWith(JUnit4.class)
public class 串行执行 {
    int cnt = 0;

    @Test
    public void testUseCondition() throws InterruptedException {
        final Lock lock = new ReentrantLock();
        final Condition conA = lock.newCondition();
        final Condition conB = lock.newCondition();
        final Condition conC = lock.newCondition();

        Thread a = new Thread(() -> {
            lock.lock();
            try {
                while (cnt != 0) {
                    conA.await();
                }
                System.out.println("a");
                cnt++;
                conB.signal();
            } catch (Exception e) {

            }
            finally {
                lock.unlock();
            }
        });

        Thread b = new Thread(() -> {
            lock.lock();
            try {
                while (cnt != 1) {
                    conB.await();
                }
                System.out.println("b");
                cnt++;
                conC.signal();
            }
            catch (Exception e) {

            }
            finally {
                lock.unlock();
            }
        });

        Thread c = new Thread(() -> {
            lock.lock();
            try {
                while (cnt != 2) {
                    conC.await();
                }
                System.out.println("c");
                cnt++;
                conA.signal();
            }
            catch (Exception e) {

            }
            finally {
                lock.unlock();
            }
        });

        a.start();
        b.start();
        c.start();

        Thread.sleep(1000000);
    }

    @Test
    public void testUseSemaphore() throws InterruptedException {
        Semaphore A = new Semaphore(1);
        Semaphore B = new Semaphore(0);
        Semaphore C = new Semaphore(0);

        Thread a = new Thread(() -> {
            try {
                A.acquire();
                System.out.println("a");
                B.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread b =new Thread(() -> {
            try {
                B.acquire();
                System.out.println("b");
                C.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread c = new Thread(() -> {
            try {
                C.acquire();
                System.out.println("c");
                A.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        a.start();
        b.start();
        c.start();

        Thread.sleep(10000000);
    }

    @Test
    public void testUseJoin() throws InterruptedException {
        Thread a = new Thread(() -> {
            System.out.println("a");
        });

        Thread b = new Thread(() -> {
            try {
                a.join();
                System.out.println("b");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread c = new Thread(() -> {
            try {
                b.join();
                System.out.println("c");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        a.start();
        b.start();
        c.start();

        Thread.sleep(100000);
    }
}
