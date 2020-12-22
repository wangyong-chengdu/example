package cd.wangyong.concurrent_example.互斥.happens_before;

/**
 * @author andy
 * @since 2020/12/19
 */
public class SynchronizedExample {
    long val = 0L;

    public void unsafeRead() {
        System.out.println("unsafeRead val:" + val);
    }

    public synchronized void safeRead() {
        System.out.println("safeRead val:" + val);
    }

    public synchronized void add10k() {
        for (int i = 0; i < 10000; i++) val++;
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedExample example = new SynchronizedExample();
        Thread t1 = new Thread(() -> example.add10k());
        Thread t2 = new Thread(() -> example.safeRead());
        Thread t3 = new Thread(() -> example.unsafeRead());

        t1.start();
        t2.start();
        t3.start();

        t2.join();
        t3.join();
        t1.join();
    }

}
