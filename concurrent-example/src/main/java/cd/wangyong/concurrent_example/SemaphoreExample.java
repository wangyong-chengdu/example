package cd.wangyong.concurrent_example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.RateLimiter;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**限流测试
 * @author andy
 * @since 2020/8/22
 */
public class SemaphoreExample {
    private static ExecutorService exec = new ThreadPoolExecutor(
            5, 10, 5000, TimeUnit.MILLISECONDS, new SynchronousQueue<>(),
            new ThreadFactoryBuilder().setNameFormat("Semaphore-Test-Thread-%d").build(), new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) {
        final Semaphore sem = new Semaphore(5);
        for (int i = 0; i < 100; i++) {
            exec.submit(() -> {
                try {
                    sem.acquire();
                    Thread.sleep((long) Math.random());
                    sem.release();
                    System.out.println(Thread.currentThread().getName() + "-剩余许可：" + sem.availablePermits());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        while (Thread.activeCount() > 0) {
            Thread.yield();
        }
        exec.shutdown();
    }

    public static void macain(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(1);
        for (int i = 0; i < 100; i++) {
            exec.submit(() -> {
                try {
                    rateLimiter.acquire();
                    System.out.println(Thread.currentThread().getName() + "-获取许可");
                    Thread.sleep((long) Math.random());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        while (Thread.activeCount() > 0) {
            Thread.yield();
        }
        exec.shutdown();
    }
}
