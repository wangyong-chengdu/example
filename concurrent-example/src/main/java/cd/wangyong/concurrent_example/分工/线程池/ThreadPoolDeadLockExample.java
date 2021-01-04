package cd.wangyong.concurrent_example.分工.线程池;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 提交到线程池中的任务一定要相对独立，具有协同的子任务提交时有时很危险，可能会出现死锁。
 * andy@andy:~$ jstack 173925
 * 2020-12-30 11:26:45
 * Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.151-b12 mixed mode):
 *
 * "Attach Listener" #12 daemon prio=9 os_prio=0 tid=0x00007f0a10001000 nid=0x2a790 waiting on condition [0x0000000000000000]
 *    java.lang.Thread.State: RUNNABLE
 *
 * "pool-1-thread-2" #11 prio=5 os_prio=0 tid=0x00007f0a50341800 nid=0x2a779 waiting on condition [0x00007f0a38da2000]
 *    java.lang.Thread.State: WAITING (parking)
 * 	at sun.misc.Unsafe.park(Native Method)
 * 	- parking to wait for  <0x0000000782995140> (a java.util.concurrent.CountDownLatch$Sync)
 * 	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
 * 	at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
 * 	at java.util.concurrent.locks.AbstractQueuedSynchronizer.doAcquireSharedInterruptibly(AbstractQueuedSynchronizer.java:997)
 * 	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireSharedInterruptibly(AbstractQueuedSynchronizer.java:1304)
 * 	at java.util.concurrent.CountDownLatch.await(CountDownLatch.java:231)
 * 	at cd.wangyong.concurrent_example.分工.线程池.ThreadPoolDeadLockExample.lambda$main$1(ThreadPoolDeadLockExample.java:28)
 * 	at cd.wangyong.concurrent_example.分工.线程池.ThreadPoolDeadLockExample$$Lambda$1/1531448569.run(Unknown Source)
 * 	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
 * 	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
 * 	at java.lang.Thread.run(Thread.java:748)
 *
 * "pool-1-thread-1" #10 prio=5 os_prio=0 tid=0x00007f0a50340000 nid=0x2a778 waiting on condition [0x00007f0a38ea3000]
 *    java.lang.Thread.State: WAITING (parking)
 * 	at sun.misc.Unsafe.park(Native Method)
 * 	- parking to wait for  <0x00000007828a46b8> (a java.util.concurrent.CountDownLatch$Sync)
 * 	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
 * 	at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
 * 	at java.util.concurrent.locks.AbstractQueuedSynchronizer.doAcquireSharedInterruptibly(AbstractQueuedSynchronizer.java:997)
 * 	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireSharedInterruptibly(AbstractQueuedSynchronizer.java:1304)
 * 	at java.util.concurrent.CountDownLatch.await(CountDownLatch.java:231)
 * 	at cd.wangyong.concurrent_example.分工.线程池.ThreadPoolDeadLockExample.lambda$main$1(ThreadPoolDeadLockExample.java:28)
 * 	at cd.wangyong.concurrent_example.分工.线程池.ThreadPoolDeadLockExample$$Lambda$1/1531448569.run(Unknown Source)
 * 	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
 * 	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
 * 	at java.lang.Thread.run(Thread.java:748)
 * @author andy
 * @since 2020/12/30
 */
public class ThreadPoolDeadLockExample {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(2);

        CountDownLatch l1 = new CountDownLatch(2);

        for (int i = 0; i < 2; i++) {
            System.out.println("L1");
            es.execute(() -> {
                CountDownLatch l2 = new CountDownLatch(2);
                for (int j = 0; j < 2; j++) {
                    es.execute(() -> {
                        System.out.println("L2");
                        l2.countDown();
                    });
                }
                try {
                    l2.await();// 会造成线程池2线程永远阻塞，因为线程池满了，没有多余的线程给子任务执行了。
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                l1.countDown();
            });
        }

        l1.await();
        System.out.println("end");
    }
}
