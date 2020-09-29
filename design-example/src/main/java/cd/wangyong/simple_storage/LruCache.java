package cd.wangyong.simple_storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 双向链表 + 字典
 * 1.字典用于K，V查询；2.双向链表用于维护队列顺序；
 * @author andy
 * @since 2020/9/29
 */
public class LruCache<K, V> implements Storage<K, V> {
    class DLinkedNode {
        K key;
        V value;
        DLinkedNode pre;
        DLinkedNode post;
    }

//    /**
//     * 低速存储，比如磁盘、数据持久化数据库等。
//     */
//    private final Storage<K,V> lowSpeedStorage;

    /**
     * 缓存
     */
    private final Map<K, DLinkedNode> cache = new ConcurrentHashMap<>();
    /**
     * 缓存容量
     */
    private final int capacity;
    /**
     * 缓存数据计数
     */
    private int count;
    /**
     * 双向链表头尾结点（头尾结点是哨兵）
     */
    private DLinkedNode head, tail;

    public LruCache(int capacity, Storage<K, V> lowSpeedStorage) {
        this.count = 0;
        this.capacity = capacity;

        this.head = new DLinkedNode();
        head.pre = null;

        this.tail = new DLinkedNode();
        tail.post = null;

        head.post = tail;
        tail.pre = head;

//        this.lowSpeedStorage = lowSpeedStorage;
    }

    @Override
    public V get(K key) {
        DLinkedNode node = cache.get(key);
        if (node == null) return null;
        // 命中过放入到链表头部
        moveToHead(node);
        return node.value;
    }

    private void moveToHead(DLinkedNode node) {
        // 从原位置移除
        removeNode(node);
        // 添加到头部
        addNode(node);
    }

    private void addNode(DLinkedNode node) {
        node.pre = head;
        node.post = head.post;

        head.post.pre = node;
        head.post = node;
    }

    private void removeNode(DLinkedNode node) {
        DLinkedNode pre = node.pre;
        DLinkedNode post = node.post;
        pre.post = post;
        post.pre = pre;
    }

    @Override
    public void put(K key, V value) {
        DLinkedNode node = cache.get(key);
        if (node != null) {
            node.value = value;
            moveToHead(node);
            return;
        }

        DLinkedNode newNode = new DLinkedNode();
        newNode.key = key;
        newNode.value = value;

        cache.put(key, newNode);
        addNode(newNode);

        ++count;

        if(count > capacity){
            // pop the tail
            DLinkedNode tail = popTail();
            cache.remove(tail.key);
            --count;
        }
    }

    private DLinkedNode popTail() {
        DLinkedNode res = tail.pre;
        removeNode(res);
        return res;
    }
}
