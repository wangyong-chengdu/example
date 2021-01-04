package cd.wangyong.concurrent_example.模式.软件事务内存;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * STM事务实现类
 * @author andy
 * @since 2021/1/4
 */
public class STMTxn implements Txn {
    /**
     * 事务ID生成器
     */
    private static AtomicLong txnSeq = new AtomicLong(0L);

    /**
     * 当前事务中所有读写数据的快照
     */
    private Map<TxnRef, VersionedRef> inTxnMap = new HashMap<>();

    /**
     * 当前事务所有需要修改的数据
     */
    private Map<TxnRef, Object> writeMap = new HashMap<>();

    /**
     * 当前事务ID
     */
    private long txnId;

    public STMTxn() {
        this.txnId = txnSeq.incrementAndGet();
    }

    @Override
    public <T> T get(TxnRef<T> ref) {
        if (!inTxnMap.containsKey(ref)) {
            inTxnMap.put(ref, ref.getCurRef());
        }

        return (T) inTxnMap.get(ref).getValue();
    }

    @Override
    public <T> void set(TxnRef<T> ref, T value) {
        if (!inTxnMap.containsKey(ref)) {
            inTxnMap.put(ref, ref.getCurRef());
        }
        writeMap.put(ref, value);
    }

    /**
     * 提交事务
     */
    public boolean commit() {
        synchronized (STM.commitLock) {
            boolean isValid = true;

            for (Map.Entry<TxnRef, VersionedRef> entry : inTxnMap.entrySet()) {
                VersionedRef curRef = entry.getKey().getCurRef();
                VersionedRef readRef = entry.getValue();

                //通过版本号来验证当前数据是否发生过变化
                if (curRef.getVersion() != readRef.getVersion()) {
                    isValid = false;
                    break;
                }
            }

            //如果校验通过，则所有更改生效
            if (isValid) {
                writeMap.forEach((k, v) -> {
                    k.curRef = new VersionedRef(v, txnId);
                });
            }
            return isValid;
        }
    }
}
