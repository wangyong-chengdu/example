package cd.wangyong.concurrent_example.分工;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * 烧水泡茶程序:
 * 并发编程的第一步是分解任务
 * @author andy
 * @since 2020/12/25
 */
public class BrewTeaExample {
    // 任务2: 洗茶壶、洗茶杯、拿茶叶
    private FutureTask<String> ft2 = new FutureTask<>(() -> {
        System.out.println("T2:洗茶壶...");
        TimeUnit.SECONDS.sleep(1);

        System.out.println("T2:洗茶杯...");
        TimeUnit.SECONDS.sleep(1);

        System.out.println("T2:拿茶叶...");
        TimeUnit.SECONDS.sleep(1);

        return "龙井";
    });

    // 任务1： 洗茶壶、烧开水、泡茶
    private FutureTask<String> ft1 = new FutureTask<>(() -> {
        System.out.println("T1:洗茶壶...");
        TimeUnit.SECONDS.sleep(1);

        System.out.println("T1:烧开水...");
        TimeUnit.SECONDS.sleep(15);

        String tea = ft2.get();
        System.out.println("T1:拿到茶叶:"+ tea);
        System.out.println("T1:泡茶:" + tea);
        return "T1:上茶：" + tea;
    });

    public void brewTea() throws ExecutionException, InterruptedException {
        Thread t1 = new Thread(ft1);
        Thread t2 = new Thread(ft2);

        t1.start();
        t2.start();

        System.out.println(ft1.get());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        BrewTeaExample example = new BrewTeaExample();
        example.brewTea();
    }
}



