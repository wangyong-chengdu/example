package cd.wangyong.concurrent_example.模式.软件事务内存;

/**
 * 带版本号的对象引用
 * @author andy
 * @since 2021/1/4
 */
public final class VersionedRef<T> {
    private final T value;
    private final long version;

    public VersionedRef(T value, long version) {
        this.value = value;
        this.version = version;
    }

    public T getValue() {
        return value;
    }

    public long getVersion() {
        return version;
    }
}
