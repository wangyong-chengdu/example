package cd.wangyong.effecitve_java_example.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import cd.wangyong.effective_java_example.函数式编程.IdGenerator;

/**
 * @author andy
 * @since 2021/2/1
 */
@RunWith(JUnit4.class)
public class IdGeneratorTest {

    @Test
    public void test() {

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100000; i++) {
            executorService.submit(() -> {
                try {
                    Thread.sleep(1000);
                    System.out.println(IdGenerator.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        while (Thread.activeCount() > 0) {
            Thread.yield();
        }
    }
}
