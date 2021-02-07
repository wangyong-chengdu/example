package cd.wangyong.effective_java_example.函数式编程;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * @author andy
 * @since 2021/2/1
 */
public class IdGenerator {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static Supplier<Integer> supplier = counter::incrementAndGet;

    public static int get() {
        return supplier.get();
    }
}
