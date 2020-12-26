package cd.wangyong.concurrent_example.模式.copy_on_write;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author andy
 * @since 2020/12/26
 */
public class RouterExample {
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

    public class RouterTable {
        private ConcurrentHashMap<String, CopyOnWriteArraySet<Router>> table = new ConcurrentHashMap<>();

        public Set<Router> get(String iface) {
            return table.get(iface);
        }

        // 删除路由
        public void remove(Router router) {
            Set<Router> set = get(router.iface);
            if (set != null) {
                set.remove(router);
            }
        }

        public void add(Router router) {
            CopyOnWriteArraySet<Router> routers = table.computeIfAbsent(router.iface, r -> new CopyOnWriteArraySet<>());
            routers.add(router);
        }
    }
}
