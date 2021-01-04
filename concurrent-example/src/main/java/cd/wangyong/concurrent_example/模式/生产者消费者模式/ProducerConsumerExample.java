package cd.wangyong.concurrent_example.模式.生产者消费者模式;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author andy
 * @since 2020/12/30
 */
public class ProducerConsumerExample {

    static class Task {

    }

    private BlockingQueue<Task> blockingQueue = new LinkedBlockingQueue<>(2000);

    public void start() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executorService.execute(() -> {
                while (true) {
                    List<Task> tasks = null;
                    try {
                        tasks = pollTasks();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    executeTasks(tasks);
                }
            });
        }
    }

    private void executeTasks(List<Task> tasks) {
        // TODO: 2020/12/30
    }

    private List<Task> pollTasks() throws InterruptedException {
        List<Task> tasks = new LinkedList<>();

        Task task = blockingQueue.take();
        while (task != null) {
            tasks.add(task);
            task = blockingQueue.poll();
        }
        return tasks;
    }


}
