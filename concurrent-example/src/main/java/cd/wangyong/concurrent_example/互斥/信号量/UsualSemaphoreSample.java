package cd.wangyong.concurrent_example.互斥.信号量;

import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

/**
 * @author andy
 * @since 2021/1/18
 */
public class UsualSemaphoreSample {

    public static void main(String[] args) {
        System.out.println("Action...GO!");

        Semaphore semaphore = new Semaphore(5);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + " is waiting for a permit");
                try {
                    semaphore.acquire();
                    System.out.println(threadName + " acquired a permit");
                    System.out.println(threadName + " executed!");
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    System.out.println(threadName + " released a permit");
                    semaphore.release();
                }
            }).start();
        }
    }

}
