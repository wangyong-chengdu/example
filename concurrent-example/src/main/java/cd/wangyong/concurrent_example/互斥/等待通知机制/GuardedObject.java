package cd.wangyong.concurrent_example.互斥.等待通知机制;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

/**
 * @author andy
 * @since 2020/12/27
 */
public class GuardedObject<T> {

    private static final Map<Object, GuardedObject> context = new ConcurrentHashMap<>();

    /**
     * 受保护对象
     */
    private T object;

    /**
     * 互斥锁
     */
    private Lock lock = new ReentrantLock();

    /**
     * 条件变量：完成
     */
    private final Condition done = lock.newCondition();

    /**
     * 等待超时时间
     */
    private final int TIME_OUT = 2;

    /**
     * 创建GuardedObject
     */
    public static <K> GuardedObject create(K key) {
        GuardedObject guardedObject = new GuardedObject();
        context.put(key, guardedObject);
        return guardedObject;
    }

    /**
     * 触发事件
     */
    public static <K, T> void fireEvent(K key, T object) {
        GuardedObject guardedObject = context.remove(key);
        if (guardedObject != null) {
            guardedObject.onChange(object);
        }
    }

    /**
     * 获取受保护对象
     */
    public T get(Predicate<T> predicate) {
        lock.lock();
        try {
            while(!predicate.test(object)) {
                done.await(TIME_OUT, TimeUnit.SECONDS);
            }
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            lock.unlock();
        }
        return object;
    }

    /**
     * 消息通知
     */
    private void onChange(T object) {
        lock.lock();
        try {
            this.object = object;
            done.signalAll();
        }
        finally {
            lock.unlock();
        }
    }
}
