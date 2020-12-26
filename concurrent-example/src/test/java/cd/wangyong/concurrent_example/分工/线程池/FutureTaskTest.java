package cd.wangyong.concurrent_example.分工.线程池;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author andy
 * @since 2020/12/25
 */
@RunWith(JUnit4.class)
public class FutureTaskTest {

    @Test
    public void testGet() throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<>(() -> 1 + 2);
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(futureTask);
        System.out.println(futureTask.get());
    }

    @Test
    public void testThreadSubmit() {

    }
}
