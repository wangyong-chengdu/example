package cd.wangyong.simple_rpc.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * @author andy
 * @since 2020/9/27
 */
public class KryoSerializable implements Serializable {
    private static final ThreadLocal<Kryo> THREAD_LOCAL =ThreadLocal.withInitial(() -> {
       Kryo kryo = new Kryo();
       kryo.setReferences(true);
       kryo.setRegistrationRequired(false);
       return kryo;
    });

    public Kryo getKryo() {
        return THREAD_LOCAL.get();
    }


    @Override
    public SerializeType getType() {
        return SerializeType.KRYO;
    }

    @Override
    public byte[] serialize(Object object) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);

        getKryo().writeClassAndObject(output, object);
        output.flush();

        return outputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(inputStream);
        return (T) getKryo().readClassAndObject(input);
    }
}
