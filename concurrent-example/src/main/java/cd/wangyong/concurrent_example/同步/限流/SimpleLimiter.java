package cd.wangyong.concurrent_example.同步.限流;

import java.util.concurrent.TimeUnit;

/**
 * 模拟RateLimiter实现
 * @author andy
 * @since 2020/12/30
 */
public class SimpleLimiter {
    /**
     * 当前令牌桶中的令牌数
     */
    private long storedPermits = 0;

    /**
     * 令牌桶的容量
     */
    private long maxPermits = 3;

    /**
     * 下一个令牌产生时间
     */
    long next = System.nanoTime();

    /**
     * 令牌发放时间间隔
     */
    long interval = 1000000000;

    /**
     * 预占令牌，返回能够获取令牌的时间
     */
    public synchronized long reserve(long reqTime) {
        // 如果reqTime在之后，则重置next = reqTime,这样reqTime就满足 <= next,且调整桶中令牌数
        if (reqTime > next) {
            adjust(reqTime);
        }

        // 如果请求时间在next之前，即reqTime <= next
        long at = next;
        // 当前允许1个或0个给予预占
        long allowPermits = Math.min(1, storedPermits);// 取令牌数，让已有令牌与当前需要的令牌1做比较， storPermits = 0，则next += interval;
        this.next += (1- allowPermits) * interval; // 刷新next:如果令牌充足，则不用刷新next;否则，令牌不充足，需要等下一个间隔；
        this.storedPermits -= allowPermits;
        return Math.max(at, 0L);
    }

    /**
     * 请求时间在下一令牌产生时间之后，需要调整：
     * 1. 重新计算令牌桶中的令牌数；
     * 2.将下一个令牌发放时间重置为请求时间
     * @param reqTime
     */
    private void adjust(long reqTime) {
        // 新产生的令牌数
        long newPermits = (reqTime - next) / interval;
        // 新产生的令牌增加到令牌桶
        this.storedPermits = Math.min(storedPermits + newPermits, maxPermits);
        this.next = reqTime;
    }

    /**
     * 申请令牌，每次动态计算调整令牌桶
     */
    public void acquire() {
        long reqTime = System.nanoTime(); // 申请令牌时间
        long at = reserve(reqTime); // 预占令牌时间

        long waitTime = Math.max(at - reqTime, 0L);
        if (waitTime > 0) {
            try {
                TimeUnit.NANOSECONDS.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
