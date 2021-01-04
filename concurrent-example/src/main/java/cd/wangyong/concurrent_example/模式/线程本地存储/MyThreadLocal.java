package cd.wangyong.concurrent_example.模式.线程本地存储;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author andy
 * @since 2020/12/27
 */
public class MyThreadLocal<T> {
    private Map<Thread, T> map = new ConcurrentHashMap<>();

    public T get() {
        return map.get(Thread.currentThread());
    }

    public void set(T t) {
        map.put(Thread.currentThread(), t);
    }
}
