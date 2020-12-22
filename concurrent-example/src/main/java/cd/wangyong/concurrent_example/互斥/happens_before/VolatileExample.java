package cd.wangyong.concurrent_example.互斥.happens_before;

import org.springframework.util.Assert;

/**
 * volatile happens-before规则：
 * 1.volatile变量规则：由于一个线程是对volatile v写，一个是volatile v读，按照Happens-Before规则，这个顺序的是固定了的。
 * 2.程序顺序性规则：x = 42 happens before 写变量 v = true;
 * 3.传递性：x = 42 happens before 读变量 v = true
 * @author andy
 * @since 2020/12/18
 */
public class VolatileExample {
    int x = 0;
    volatile boolean v = false;

    public void write() {
        x = 42;
        v = true;
    }

    public void read() {
        if (v) {
            Assert.isTrue(x == 42, "违背Happens-Before volatile规则！");
        }
        else {
            System.out.println("x = " + x);
        }
    }

    /**
     *  * volatile happens-before规则：
     *  * 1.volatile变量规则：由于一个线程是对volatile v写，一个是volatile v读，按照Happens-Before规则，这个顺序的是固定了的。
     *  * 2.程序顺序性规则：x = 42 happens before 写变量 v = true;
     *  * 3.传递性：x = 42 happens before 读变量 v = true
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        final VolatileExample example = new VolatileExample();
        Thread t1 = new Thread(() -> example.write());
        Thread t2 = new Thread(() -> example.read());

        // 不管是t1.start先还是t2.start先，由于一个线程是对volatile v写，一个是volatile v读，按照Happens-Before规则，这个顺序的是固定了的。
        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}
