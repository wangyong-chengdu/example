package cd.wangyong.simple_storage;

/**
 * @author andy
 * @since 2020/9/29
 */
public interface Storage<K, V> {
    V get(K key);

    void put(K key, V value);
}
