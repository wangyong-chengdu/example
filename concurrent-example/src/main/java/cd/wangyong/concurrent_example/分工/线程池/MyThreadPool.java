package cd.wangyong.concurrent_example.分工.线程池;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * @author andy
 * @since 2020/12/25
 */
public class MyThreadPool {
    class WorkerThread extends Thread {
        public void run() {
            // 循环取任务并执行
            while(true) {
                try {
                    Runnable task = workQueue.take();
                    task.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 利用阻塞队列实现生产者-消费者模式
    private BlockingQueue<Runnable> workQueue;
    // 工作线程
    List<WorkerThread> threads = new ArrayList<>();

    public MyThreadPool(int poolSize, BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
        // 创建工作线程
        for (int i = 0; i < poolSize; i++) {
            WorkerThread thread = new WorkerThread();
            thread.start();
            threads.add(thread);
        }
    }

    public void execute(Runnable command) throws InterruptedException {
        workQueue.put(command);
    }
}
