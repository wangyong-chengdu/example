package cd.wangyong.concurrent_example.分工.线程池;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author andy
 * @since 2020/12/25
 */
public class MyThreadPoolTest {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Runnable> queue = new LinkedBlockingDeque<>(2);
        MyThreadPool threadPool = new MyThreadPool(10, queue);

        threadPool.execute(() -> System.out.println("hello"));
    }
}
