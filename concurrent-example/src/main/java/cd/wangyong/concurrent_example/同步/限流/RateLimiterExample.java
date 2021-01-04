package cd.wangyong.concurrent_example.同步.限流;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.RateLimiter;

/**
 * 底层实现令牌桶：
 * 109
 * 389
 * 499
 * 500
 * 499
 * 500
 * 500
 * 500
 * 500
 * 499
 * 500
 * 500
 * 500
 * 499
 * 500
 * 500
 * 500
 * 499
 * 500
 * 500
 * @author andy
 * @since 2020/12/30
 */
public class RateLimiterExample {

    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(2);
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        // 测试20次执行
        for (int i = 0; i < 20; i++) {
            final long prev = System.nanoTime();
            rateLimiter.acquire();
            executorService.execute(() -> {
                long cur = System.nanoTime();
                System.out.println((cur - prev) / 1000000);
            });
        }
    }

}
