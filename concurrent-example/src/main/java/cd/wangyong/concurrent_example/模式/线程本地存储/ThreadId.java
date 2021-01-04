package cd.wangyong.concurrent_example.模式.线程本地存储;


import java.util.concurrent.atomic.AtomicLong;

/**
 * 线程ID
 * @author andy
 * @since 2020/12/27
 */
public class ThreadId {
    private static final AtomicLong nextId = new AtomicLong(0);
    private static final ThreadLocal<Long> threadLocal = ThreadLocal.withInitial(() -> nextId.getAndIncrement());

    public static long get() {
        return threadLocal.get();
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("T" + ThreadId.get());
            }
        };

        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}
