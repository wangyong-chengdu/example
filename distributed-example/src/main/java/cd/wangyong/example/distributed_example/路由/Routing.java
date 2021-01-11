package cd.wangyong.example.distributed_example.路由;

import java.util.List;

/**
 * 路由
 * @author andy
 * @since 2021/1/11
 */
public interface Routing<T> {

    <K> T select(K key, int routeType, List<T> list);
}
