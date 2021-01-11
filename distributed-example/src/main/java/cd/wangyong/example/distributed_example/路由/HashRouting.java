package cd.wangyong.example.distributed_example.路由;

import java.util.List;

/**
 * @author andy
 * @since 2021/1/11
 */
public abstract class HashRouting<T> implements Routing<T> {

    @Override
    public <K> T select(K key, int routeType, List<T> list) {
        assert list != null;
        int size = list.size();
        if (size == 0) return null;
        else if (size == 1) return list.get(0);
        else return doSelect(key, routeType, list);
    }

    protected abstract <K> T doSelect(K key, int routeType, List<T> list);
}
