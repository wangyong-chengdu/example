package cd.wangyong.concurrent_example.互斥.信号量;

import java.util.concurrent.Semaphore;

/**
 * 5个 5 个放行
 * @author andy
 * @since 2021/1/18
 */
public class AbnormalSemaphoreSample {

    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);

        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println("Executed");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.start();
        }

        System.out.println("Action...GO");
        semaphore.release(5);
        System.out.println("Wait for permits off");

        // 主线程等待依赖sleep轮询
        while (semaphore.availablePermits() != 0) {
            Thread.sleep(100L);
        }
        System.out.println("Action...GO again");
        semaphore.release(5);
    }


}
