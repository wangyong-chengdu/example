package cd.wangyong.concurrent_example.互斥.死锁;

/**
 * @author andy
 * @since 2021/1/17
 */
public class DeadLockExample2 extends Thread {
    private final String first;
    private final String second;

    public DeadLockExample2(String name, String first, String second) {
        super(name);
        this.first = first;
        this.second = second;
    }

    @Override
    public void run() {
        synchronized (first) {
            System.out.println(this.getName() + " obtained: " + first);

            try {
                Thread.sleep(1000L);

                synchronized (second) {
                    System.out.println(this.getName() + " obtained: " + second);
                }
            } catch (InterruptedException e) {

            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 打开死锁监控：注意有性能开销
        DeadLockWatcher.watch();

        final String lockA = "lockA";
        final String lockB = "lockB";

        DeadLockExample2 t1 = new DeadLockExample2("Thread1", lockA, lockB);
        DeadLockExample2 t2 = new DeadLockExample2("Thread2", lockB, lockA);

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}
