package cd.wangyong.concurrent_example.互斥.happens_before;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * @author andy
 * @since 2020/12/18
 */
public class ThreadStartExample {

    public static void main(String[] args) throws InterruptedException {
        final Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 8);
        Thread t = new Thread(() -> Assert.isTrue(map.get(1) == 10, "违反了Happens Before规则。"));

        map.put(1, 10);
        t.start();
        t.join();
    }
}
