package cd.wangyong.concurrent_example.互斥.原子类;

/**
 * 模拟CAS指令原子特性
 * @author andy
 * @since 2020/12/24
 */
public class SimulatedCAS {
    private int cnt;

    public synchronized int cas(int expect, int newValue) {
        int curValue = cnt;
        if (curValue == expect) {
            cnt = newValue;
        }
        return curValue;
    }
}
