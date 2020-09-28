package cd.wangyong.simple_rpc.serialization;

import com.alibaba.fastjson.JSON;

/**
 * @author andy
 * @since 2020/9/27
 */
public class JSONSerializable implements Serializable {
    @Override
    public SerializeType getType() {
        return SerializeType.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
