package cd.wangyong.zk_example.Watch例子;

import org.apache.zookeeper.AsyncCallback.StatCallback;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 数据监控
 * 异步事件驱动
 * @author andy
 * @since 2021/2/6
 */
public class DataMonitor implements Watcher, StatCallback {
    private final ZooKeeper zk;
    private final String zNode;
    private final Watcher chainedWatcher;
    private final DataMonitorListener listener;

    private boolean dead;
    private byte[] prevData;

    public DataMonitor(ZooKeeper zk, String zNode, Watcher chainedWatcher, DataMonitorListener listener) {
        this.zk = zk;
        this.zNode = zNode;
        this.chainedWatcher = chainedWatcher;
        this.listener = listener;

        // 数据监控启动需要先检查node是否存在
        zk.exists(zNode, true, this, null);
    }

    /**
     * 处理Watch事件
     */
    public void process(WatchedEvent event) {
        String path = event.getPath();
        if (event.getType() == Event.EventType.None) {
            switch (event.getState()) {
                case SyncConnected:
                    break;
                case Expired:
                    // 会话过期，结束
                    dead = true;
                    listener.closing(KeeperException.Code.SESSIONEXPIRED.intValue());
                    break;
            }
        }
        else {
            if (path != null && path.equals(zNode)) {
                // retry,检查在节点上一定发生了一些改变
                zk.exists(zNode, true, this, null);
            }
        }

        if (chainedWatcher != null) {
            chainedWatcher.process(event);
        }
    }

    /**
     * 检索节点状态，并做后续动作
     */
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        boolean exists;
        Code code = Code.get(rc);
        switch (code) {
            case OK:
                exists = true;
                break;
            case NONODE:
                exists = false;
                break;
            case SESSIONEXPIRED:
            case NOAUTH:
                dead = true;
                listener.closing(rc);
                return;
            default:
                // retry errors
                zk.exists(zNode, true, this, null);
                return;
        }

        byte[] b = null;
        if (exists) {
            try {
                zk.getData(zNode, false, null);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                return;
            }
        }

        if (b != prevData) {
            listener.exists(b);
            prevData = b;
        }
    }

    public boolean isDead() {
        return dead;
    }
}
