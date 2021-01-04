package cd.wangyong.concurrent_example.模式.软件事务内存;

/**
 * 支持事务的引用
 * @author andy
 * @since 2021/1/4
 */
public class TxnRef<T> {
    // 当前数据，带版本号
    volatile VersionedRef curRef;

    public TxnRef(T value) {
        this.curRef = new VersionedRef(value, 0L);
    }

    /**
     * 获取当前数据
     */
    public VersionedRef getCurRef() {
        return curRef;
    }

    /**
     * 获取当前事务的数据
     * @param txn 当前事务
     */
    public T getValue(Txn txn) {
        return txn.get(this);
    }

    /**
     * 在当前事务设置数据
     */
    public void setValue(T value, Txn txn) {
        txn.set(this, value);
    }
}
