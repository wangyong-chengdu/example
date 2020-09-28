package cd.wangyong.simple_rpc.serialization;

import cd.wangyong.simple_rpc.protocol.Message;

/**
 * @author andy
 * @since 2020/9/27
 */
public interface Serializable {
    /**
     * 序列化类型
     * @return
     */
    SerializeType getType();

    /**
     * 序列化
     * @param object
     * @return
     */
    byte[] serialize(Object object);

    /**
     * 反序列化
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
