package cd.wangyong.concurrent_example.互斥.信号量;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

/**
 * @author andy
 * @since 2020/12/23
 */
public class ObjectPool<T, R> {
    private final List<T> pool;
    private final Semaphore sem;

    public ObjectPool(int size, T t) {
        this.pool = new Vector<T>(){};
        for (int i = 0; i < size; i++) {
            pool.add(t);
        }
        this.sem = new Semaphore(size);
    }

    public R exec(Function<T, R> function) {
        T t = null;
        try {
            sem.acquire();
            t = pool.remove(0);
            return function.apply(t);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            pool.add(t);
            sem.release();
        }
        return null;
    }
}
