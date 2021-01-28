package cd.wangyong.concurrent_example.互斥.死锁;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 死锁监控
 * @author andy
 * @since 2021/1/17
 */
public class DeadLockWatcher {
    // 状态
    private static volatile boolean started = false;

    private DeadLockWatcher() {
    }

    // 注意防止竞态条件，保证只执行一次
    // 对线程进行快照本身是一个相对重量级的操作，还是要慎重选择频度和时机。
    public static void watch() {
        if (!started) {
            synchronized (DeadLockWatcher.class) {
                if (!started) {
                    started = true;
                    doWatch();
                }
            }
        }
    }

    private static void doWatch() {
        ThreadMXBean mBean = ManagementFactory.getThreadMXBean();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(()->{
            long[] threadIds = mBean.findDeadlockedThreads();
            if (threadIds != null) {
                System.out.println("Detected deadlock threads:");
                Arrays.stream(mBean.getThreadInfo(threadIds)).forEach(threadInfo -> System.out.println(threadInfo.getThreadName()));
            }
        }, 5L, 10L, TimeUnit.SECONDS);
    }
}
