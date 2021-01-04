package cd.wangyong.concurrent_example.模式.写时复制;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author andy
 * @since 2020/12/26
 */
public class RouterExample {
    /**
     * 路由结点
     */
    static final class Router {
        private final String ip;
        private final int port;
        private final String iface;

        public Router(String ip, int port, String iface) {
            this.ip = ip;
            this.port = port;
            this.iface = iface;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Router router = (Router) o;
            return port == router.port &&
                    Objects.equals(ip, router.ip) &&
                    Objects.equals(iface, router.iface);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ip, port, iface);
        }
    }

    /**
     * 路由表
     */
    public class RouterTable {
        private ConcurrentHashMap<String, CopyOnWriteArraySet<Router>> table = new ConcurrentHashMap<>();

        private volatile boolean changed;

        private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        public void backup2Local() {
            scheduledExecutorService.scheduleWithFixedDelay(() -> autoSave(), 1, 1, TimeUnit.MINUTES);
        }

        private void autoSave() {
            if (!changed) return;
            changed = false;
            save2Local();
        }

        private void save2Local() {

        }


        public Set<Router> get(String iface) {
            return table.get(iface);
        }

        // 删除路由
        public void remove(Router router) {
            Set<Router> set = get(router.iface);
            if (set != null) {
                set.remove(router);
                changed = true;
            }
        }

        /**
         * 增加路由
         */
        public void add(Router router) {
            CopyOnWriteArraySet<Router> routers = table.computeIfAbsent(router.iface, r -> new CopyOnWriteArraySet<>());
            routers.add(router);
            changed = true;
        }
    }
}
