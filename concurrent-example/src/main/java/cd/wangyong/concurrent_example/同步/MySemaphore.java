package cd.wangyong.concurrent_example.同步;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author andy
 * @since 2020/12/23
 */
public class MySemaphore {
    private int cnt;
    private Queue<Thread> queue = new LinkedList<>();
    private final Object o = new Object();

    public MySemaphore(int cnt) {
        this.cnt = cnt;
    }

    public synchronized void down() {
        this.cnt--;
        if (cnt < 0) {
            queue.add(Thread.currentThread());
            try {
                o.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void up() {
        this.cnt++;
        if (this.cnt >= 0) {
            queue.remove();
            o.notify();
        }
    }
}
