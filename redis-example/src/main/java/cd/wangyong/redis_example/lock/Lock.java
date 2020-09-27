package cd.wangyong.redis_example.lock;

public interface Lock {
    boolean lock();
    boolean tryLock(long expires);
    boolean tryLock();
    boolean unLock();
}
