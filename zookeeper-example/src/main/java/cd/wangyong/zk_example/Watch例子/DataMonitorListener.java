package cd.wangyong.zk_example.Watch例子;

/**
 * 数据监控Listener，设计用于DataMonitor回调容器Executor
 * @author andy
 * @since 2021/2/6
 */
public interface DataMonitorListener {

    /**
     * 当节点发生变更的存在状态
     * @param data
     */
    void exists(byte[] data);

    /**
     * zk session is no longer valid.
     */
    void closing(int rc);
}
