package cd.wangyong.concurrent_example.互斥.原子类;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author andy
 * @since 2020/12/25
 */
public class SafeStockWithCAS {
    static class StockRange {
        final int upper;
        final int lower;

        StockRange(int upper, int lower) {
            this.upper = upper;
            this.lower = lower;
        }
    }

    private final AtomicReference<StockRange> reference = new AtomicReference<>(new StockRange(0, 0));

    /**
     * 无锁设置：线程安全：不会导致线程阻塞
     */
    public void setUpper(int v) {
        StockRange nr;
        StockRange or;
        do {
            or = reference.get();
            if (v < or.lower) {
                throw new IllegalArgumentException();
            }
            nr = new StockRange(v, or.lower);
        } while (!reference.compareAndSet(or, nr));
    }


}
