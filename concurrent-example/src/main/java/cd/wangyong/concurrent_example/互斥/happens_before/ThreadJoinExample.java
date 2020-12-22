package cd.wangyong.concurrent_example.互斥.happens_before;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * @author andy
 * @since 2020/12/18
 */
public class ThreadJoinExample {

    public static void main(String[] args) throws InterruptedException {
        final Map<Integer, Integer> map = new HashMap<>();
        Thread t = new Thread(() -> map.put(1, 1));
        t.start();
        t.join();
        Assert.isTrue(map.get(1) == 1, "违背Happens-Before join规则");
    }
}
