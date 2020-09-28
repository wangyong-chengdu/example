package cd.wangyong.simple_rpc.serialization;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author andy
 * @since 2020/9/27
 */
public class ProtoStuffSerializable implements Serializable {
    @Override
    public SerializeType getType() {
        return SerializeType.PROTOBUF;
    }

    @Override
    public byte[] serialize(Object object) {
        Schema<Object> schema = (Schema<Object>) RuntimeSchema.getSchema(object.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(512);
        return ProtostuffIOUtil.toByteArray(object, schema, buffer);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T t = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, t, schema);
        return t;
    }
}
