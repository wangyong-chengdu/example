package cd.wangyong.concurrent_example.互斥.信号量;

/**
 * @author andy
 * @since 2020/12/23
 */
public class ObjectPoolTest {

    public static void main(String[] args) {
        ObjectPool<Long, String> pool = new ObjectPool<Long, String>(10, 2L);
        pool.exec(t -> {
            System.out.println(t);
            return t.toString();
        });
    }
}
