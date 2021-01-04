package cd.wangyong.concurrent_example.模式.软件事务内存;

/**
 * @author andy
 * @since 2021/1/4
 */
public final class STM {
    /**
     * 提交数据需要用到的全局锁
     */
    static final Object commitLock = new Object();

    private STM() {
    }

    public static void atomic(TxnRunnable action) {
        boolean committed = false;
        while (!committed) {
            STMTxn txn = new STMTxn();
            action.run(txn);
            committed = txn.commit();
        }
    }
}
