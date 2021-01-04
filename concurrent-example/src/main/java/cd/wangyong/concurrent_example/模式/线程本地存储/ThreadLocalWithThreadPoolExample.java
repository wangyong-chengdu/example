package cd.wangyong.concurrent_example.模式.线程本地存储;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池、线程本地存储同时使用如何避免内存泄露
 * @author andy
 * @since 2020/12/27
 */
public class ThreadLocalWithThreadPoolExample {
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private ThreadLocal threadLocal;

    public void run() {
        executorService.execute(() -> {
            threadLocal.set(new Object());
            try {
                // todo: 业务逻辑
            }
            finally {
                // 手动清理ThreadLocal，避免内存泄露
                threadLocal.remove();
            }
        });
    }

}
