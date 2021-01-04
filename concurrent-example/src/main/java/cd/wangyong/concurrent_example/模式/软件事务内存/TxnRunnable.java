package cd.wangyong.concurrent_example.模式.软件事务内存;

/**
 * @author andy
 * @since 2021/1/4
 */
@FunctionalInterface
public interface TxnRunnable {
    void run(Txn txn);
}
