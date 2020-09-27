package cd.wangyong.redis_example.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.HashMap;
import java.util.Map;

public class RedisReentrantLock {
    private ThreadLocal<Map<String, Integer>> lockers = new ThreadLocal<>();
    private Jedis jedis;

    public RedisReentrantLock(Jedis jedis) {
        this.jedis = jedis;
    }

    public boolean lock(String key, int expires) {
        Map<String, Integer> lockCountMap = getLockCountMap();
        Integer count = lockCountMap.get(key);
        if (count != null) {
            lockCountMap.put(key, count + 1);
            return true;
        }
        String res = jedis.set(key, "", SetParams.setParams().nx().ex(expires));
        if (res == null) return false;
        lockCountMap.put(key, 1);
        return true;
    }

    public boolean unLock(String key) {
        Map<String, Integer> lockCountMap = getLockCountMap();
        Integer count = lockCountMap.get(key);
        if (count == null) return false;
        count--;
        if (count > 0) lockCountMap.put(key, count);
        else {
            lockCountMap.remove(key);
            jedis.del(key);
        }
        return true;
    }

    private Map<String, Integer> getLockCountMap() {
        Map<String, Integer> lockCountMap = lockers.get();
        if (lockCountMap != null) return lockCountMap;
        lockers.set(new HashMap<>());
        return lockers.get();
    }

    public static void main(String[] args) {
        RedisReentrantLock lock = new RedisReentrantLock(new Jedis("127.0.0.1", 6379));
        final String LOCK_KEY = "lock_a";

        Thread thread1 = new Thread(()->{
            try {
                while (!lock.lock(LOCK_KEY, 5)) {
                    System.out.println("thread1:wait lock");
                    Thread.sleep(500);
                }
                System.out.println("thread1:get lock");
                Thread.sleep(200);
                lock.unLock(LOCK_KEY);
                System.out.println("thread1:release lock");
            } catch (InterruptedException e) {
            }
        });

        Thread thread2 = new Thread(()->{
            try {
                while (!lock.lock(LOCK_KEY, 5)) {
                    System.out.println("thread2:wait lock");
                    Thread.sleep(500);
                }
                System.out.println("thread2:get lock");
                Thread.sleep(200);
                lock.unLock(LOCK_KEY);
                System.out.println("thread2:release lock");
            } catch (InterruptedException e) {
            }
        });

        thread1.start();
        thread2.start();

        while (Thread.activeCount() > 0)
            Thread.yield();
    }
}
