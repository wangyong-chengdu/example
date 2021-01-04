package cd.wangyong.concurrent_example.模式.软件事务内存;

/**
 * 事务接口
 * @author andy
 * @since 2021/1/4
 */
public interface Txn {
    <T> T get(TxnRef<T> ref);
    <T> void set(TxnRef<T> ref, T value);
}
