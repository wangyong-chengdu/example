package cd.wangyong.concurrent_example.互斥.happens_before;

import org.springframework.util.Assert;

/**
 * 由于没有使用volatile关键字，各个线程都缓存了cnt共享变量导致最后的执行结果不是我们预期的
 * @author andy
 * @since 2020/12/18
 */
public class VisibilityExample {
    private long cnt = 0;

    private void add10k() {
        int id = 0;
        while (id++ < 10000) {
            cnt++;
        }
    }

    public static long cal() throws InterruptedException {
        final VisibilityExample example = new VisibilityExample();

        Thread t1 = new Thread(() -> example.add10k());
        Thread t2 = new Thread(() -> example.add10k());
        Thread t3 = new Thread(() -> example.add10k());

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        return example.cnt;
    }

    public static void main(String[] args) throws InterruptedException {
        Assert.isTrue(cal() != 30000, "随机碰到了，由于没有做任何可见性控制，大部分情况下应该在10000 ~ 30000之间！");
    }
}
