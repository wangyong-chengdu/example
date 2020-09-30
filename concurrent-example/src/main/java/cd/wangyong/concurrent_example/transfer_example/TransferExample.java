package cd.wangyong.concurrent_example.transfer_example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author andy
 * @since 2020/9/30
 */
public class TransferExample {

    private static ExecutorService executorService = new ThreadPoolExecutor(1000, 1000, 5,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(200), new ThreadPoolExecutor.CallerRunsPolicy());

    private static final Lock lock = new ReentrantLock();
    static class CustomInteger{
        int value;
        public CustomInteger(int value) {
            this.value = value;
        }
    }

    private static AtomicInteger balance = new AtomicInteger(0);

    public static void main1(String[] args) throws InterruptedException {
        CustomInteger balance = new CustomInteger(0);
        CountDownLatch latch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {
            executorService.submit(() -> transfer(balance, 1, lock, latch));
        }

        latch.await();
        System.out.println("balance = " + balance.value);
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {
            executorService.submit(() -> transferCas(balance, 1, latch));
        }

        latch.await();
        System.out.println("balance = " + balance.get());
    }

    private static void transfer(CustomInteger balance, int amount, Lock lock, CountDownLatch latch) {
        lock.lock();
        balance.value = balance.value + amount;
        lock.unlock();
        latch.countDown();
    }

    private static void transferCas(AtomicInteger balance, int amount, CountDownLatch latch) {
        while (true) {
            int oldValue = balance.get();
            if (balance.compareAndSet(oldValue, oldValue + amount)) {
                break;
            }
        }
        latch.countDown();
    }
}
