package cd.wangyong.concurrent_example.互斥.信号量;

import java.util.concurrent.Semaphore;

/**
 * @author andy
 * @since 2020/12/23
 */
public class SemaphoreExample {

    private int cnt;
    private final Semaphore s = new Semaphore(1);

    public void addOne() throws InterruptedException {
        s.acquire();
        try {
            cnt++;
        } finally {
            s.release();
        }
    }
}
