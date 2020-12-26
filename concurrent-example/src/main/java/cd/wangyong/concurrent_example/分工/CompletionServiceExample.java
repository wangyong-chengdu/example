package cd.wangyong.concurrent_example.分工;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

/**
 * @author andy
 * @since 2020/12/25
 */
public class CompletionServiceExample {
    private final BlockingQueue<Future<Integer>> completionQueue = new LinkedBlockingDeque<>(20);
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);
    private final CompletionService<Integer> completionService = new ExecutorCompletionService<>(executorService, completionQueue);

    public void getAndSavePrice() throws InterruptedException, ExecutionException {
        // 加入阻塞队列
        completionService.submit(() -> getPriceByS1());
        completionService.submit(() -> getPriceByS2());
        completionService.submit(() -> getPriceByS3());

        for (int i = 0; i < 3; i++) {
            Integer price = completionService.take().get();
            executorService.execute(() -> save(price));
        }


    }

    private void save(Integer price) {
        // TODO: 2020/12/25
        System.out.println(price);
    }

    private int getPriceByS1() {
        // TODO: 2020/12/25
        return 1;
    }

    private int getPriceByS2() {
        // TODO: 2020/12/25
        return 2;
    }

    private int getPriceByS3() {
        // TODO: 2020/12/25
        return 3;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletionServiceExample example = new CompletionServiceExample();
        example.getAndSavePrice();
    }
}
