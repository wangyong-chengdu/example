package cd.wangyong.curator_example.test;

import java.util.concurrent.TimeUnit;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author andy
 * @since 2021/2/1
 */
@RunWith(JUnit4.class)
public class CuratorStartTest {
    private CuratorFramework client;


    @Before
    public void doCreateClient() throws Exception {
        String zookeeperConnectionString = "127.0.0.1:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
        client.start();

    }

    @Test
    public void testCreatePath() throws Exception {
        client.create().forPath("/my", "wangyong".getBytes());
    }

    @Test
    public void testDistributedLock() throws Exception {
        String lockPath = "/my/lock";
        InterProcessMutex lock = new InterProcessMutex(client, lockPath);
        if (lock.acquire(5000, TimeUnit.MILLISECONDS)) {
            try {
                System.out.println("Get Distributed Lock");
                Thread.sleep(10000);
            } finally {
                lock.release();
            }
        }
    }

    @Test
    public void testLeaderElection() {
        LeaderSelectorListener listener = new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                System.out.println("I am the leader.");
            }
        };

        String path = "/my/leader";
        LeaderSelector selector = new LeaderSelector(client, path, listener);
        selector.autoRequeue();
        selector.start();
    }



    @After
    public void doAfter() {
        while (Thread.activeCount() > 0) Thread.yield();
    }
}
