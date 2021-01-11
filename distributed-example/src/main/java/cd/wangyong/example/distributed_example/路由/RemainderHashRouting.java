package cd.wangyong.example.distributed_example.路由;

import java.util.List;

/**
 * 余数Hash
 * @author andy
 * @since 2021/1/11
 */
public class RemainderHashRouting<T> extends HashRouting<T> {
    @Override
    protected <K> T doSelect(K key, int routeType, List<T> list) {
        return list.get(key.hashCode() % list.size());
    }
}
