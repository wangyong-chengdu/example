package cd.wangyong.concurrent_example.同步;

import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 对账
 * @author andy
 * @since 2020/12/24
 */
public class DuiZhangExample<P, V, D> {
    private Vector<P> pVector;
    private Vector<V> vVector;

    private Executor executor = Executors.newFixedThreadPool(1);
    private final CyclicBarrier barrier = new CyclicBarrier(2, /* 消费者 */ () -> executor.execute(() -> duiZhang()));

    public void checkAll() {
        // 生产者1
        Thread t1 = new Thread(() -> {
           while(existUncheckedOrder()) {
               pVector.add(getPOrder());
               try {
                   barrier.await(); // barrier - 1
               } catch (InterruptedException e) {
                   e.printStackTrace();
               } catch (BrokenBarrierException e) {
                   e.printStackTrace();
               }
           }
        });
        t1.start();

        // 生产者2
        Thread t2 = new Thread(() -> {
            while(existUncheckedOrder()) {
                vVector.add(getVOrder());
                try {
                    barrier.await(); // barrier - 1
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
        t2.start();
    }

    private V getVOrder() {
        return null;
    }

    private P getPOrder() {
        return null;
    }

    private boolean existUncheckedOrder() {
        // TODO: 2020/12/24
        return false;
    }


    private void duiZhang() {
        P p = pVector.remove(0);
        V v = vVector.remove(0);

        D diff = check(p, v);
        save(diff);
    }

    private D check(P p, V v) {
        return null;
    }

    private void save(D diff) {
        // TODO: 2020/12/24 save
    }
}
