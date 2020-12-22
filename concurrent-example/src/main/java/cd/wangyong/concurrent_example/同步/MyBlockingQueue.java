package cd.wangyong.concurrent_example.同步;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 用管程MESA模型实现阻塞队列
 * @author andy
 * @since 2020/12/21
 */
public class MyBlockingQueue<T> {
    // 互斥锁
    private final Lock lock = new ReentrantLock();
    // 条件变量，队列不满
    private final Condition notFull = lock.newCondition();
    // 条件变量，队列不空
    private final Condition notEmpty = lock.newCondition();
    // 队列容量
    private final int capacity;
    // 队列大小
    private volatile int size;
    // 线程非安全共享变量
    private Queue<T> queue = new LinkedList<>();

    public MyBlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * 入队
     */
    public void enq(T x) {
        lock.lock();
        try {
            // 为什么是while而不是if,因为如果条件满足，线程会进入等待队列，但真正再次执行时，可能条件又不满足了。
            while (isFull()) {
                notFull.await();
            }
            queue.add(x);
            size++;
            notEmpty.signalAll();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    /**
     * 出队
     */
    public T deq() {
        lock.lock();
        try {
            while (isEmpty()) {
                notEmpty.await();
            }
            T x = queue.remove();
            size--;
            notFull.signalAll();
            return x;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
        return null;
    }

    private boolean isFull() {
        return capacity - size > 0;
    }

    private boolean isEmpty() {
        return size == 0;
    }
}
