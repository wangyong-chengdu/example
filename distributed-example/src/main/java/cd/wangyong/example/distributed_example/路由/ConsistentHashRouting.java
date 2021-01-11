package cd.wangyong.example.distributed_example.路由;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一致性Hash
 * @author andy
 * @since 2021/1/11
 */
public class ConsistentHashRouting<T> extends HashRouting<T> {
    private final Map<Integer, TreeMap<Long, T>> map = new ConcurrentHashMap<>();

    @Override
    protected <K> T doSelect(K key, int routeType, List<T> list) {
        TreeMap<Long, T> treeMap = map.computeIfAbsent(routeType, k -> {
            TreeMap<Long, T> map = new TreeMap<>();
            list.forEach(ele -> {
                map.put(hash(ele), ele);
            });
            return map;
        });
        Map.Entry<Long, T> entry = treeMap.ceilingEntry(hash(key));
        return entry != null ? entry.getValue() : treeMap.ceilingEntry(1L).getValue();
    }

    // hash计算： key.getBytes -> md5 -> hash
    private <K> long hash(K key) {
        byte[] bytes = key.toString().getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            bytes = md5.digest(bytes);
        } catch (Exception e) {

        }
        return hash(bytes, 0);
    }

    // copy from dubbo，移位运算、或运算
    private long hash(byte[] digest, int number) {
        return (((long) (digest[3 + number * 4] & 0xFF) << 24)
                | ((long) (digest[2 + number * 4] & 0xFF) << 16)
                | ((long) (digest[1 + number * 4] & 0xFF) << 8)
                | (digest[number * 4] & 0xFF))
                & 0xFFFFFFFFL;
    }
}
