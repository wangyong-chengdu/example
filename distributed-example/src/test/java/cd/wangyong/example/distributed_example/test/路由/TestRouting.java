package cd.wangyong.example.distributed_example.test.路由;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import cd.wangyong.example.distributed_example.路由.ConsistentHashRouting;
import cd.wangyong.example.distributed_example.路由.RemainderHashRouting;
import cd.wangyong.example.distributed_example.路由.Routing;

/**
 * @author andy
 * @since 2021/1/11
 */
@RunWith(JUnit4.class)
public class TestRouting {
    private List<String> ips;
    private Routing<String> routing;

    @Before
    public void init() {
        ips = Arrays.asList("192.168.0.1", "192.168.0.2", "192.168.0.3", "192.168.0.4", "192.168.0.5", "192.168.0.6", "255.255.255.255", "127.0.0.1");
    }


    @Test
    public void testRemainderHash() {
        routing = new RemainderHashRouting<>();
        for (char c = 'a'; c <= 'z'; c++) {
            System.out.println(c + " -> " + routing.select(c, 1, ips));
        }
    }

    @Test
    public void testConsistentHash() {
        routing = new ConsistentHashRouting<>();
        for (char c = 'A'; c <= 'z'; c++) {
            System.out.println(c + " -> " + routing.select(c, 1, ips));
        }

    }
}
