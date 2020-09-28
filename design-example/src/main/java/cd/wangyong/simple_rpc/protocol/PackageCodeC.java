package cd.wangyong.simple_rpc.protocol;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import cd.wangyong.simple_rpc.serialization.JSONSerializable;
import cd.wangyong.simple_rpc.serialization.KryoSerializable;
import cd.wangyong.simple_rpc.serialization.ProtoStuffSerializable;
import cd.wangyong.simple_rpc.serialization.Serializable;
import cd.wangyong.simple_rpc.serialization.SerializeType;

/**
 * 协议编解码
 *
 * 通信协议
 * |魔数（2字节）|数据长度（4字节）|消息ID（4字节）|消息类型（1字节）|协议版本（1字节）|序列化方式（1字节）|
 * |---------------------------协议体:Payload（N字节）--------------------------------------|
 * @author andy
 * @since 2020/9/28
 */
public class PackageCodeC {
    private static final int MAGIC_NUMBER = 0x9110;
    private static final Byte PROTOCOL_VERSION = 1;

    private static final Map<Byte, Serializable> serializableMap;
    static {
        serializableMap = new HashMap<>();
        serializableMap.put(SerializeType.JSON.getCode(), new JSONSerializable());
        serializableMap.put(SerializeType.PROTOBUF.getCode(), new ProtoStuffSerializable());
        serializableMap.put(SerializeType.KRYO.getCode(), new KryoSerializable());
    }

    private static final Map<Byte, Class<? extends Message>> messageTypeMap;
    static {
        messageTypeMap = new HashMap<>();
        messageTypeMap.put(MessageType.TALK.getCode(), TalkMessage.class);
    }

    private Class<? extends Message> messageClazz;
    private Serializable serializable;
    private AtomicInteger count = new AtomicInteger(0);

    public PackageCodeC(Byte serializeType, byte messageType) {
        this.serializable = serializableMap.get(serializeType) != null ? serializableMap.get(serializeType) : new ProtoStuffSerializable();
        this.messageClazz = messageTypeMap.get(messageType) != null ? messageTypeMap.get(messageType) : TalkMessage.class;
    }

    /**
     * 编码
     */
    public ByteBuf encode(Message message) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byte[] payload = serializable.serialize(message);

        // 编码过程
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeInt(payload.length);
        byteBuf.writeInt(count.getAndIncrement());
        byteBuf.writeByte(message.getMessageType().getCode());
        byteBuf.writeByte(PROTOCOL_VERSION);
        byteBuf.writeByte(serializable.getType().getCode());
        byteBuf.writeBytes(payload);

        return byteBuf;
    }

    /**
     * 解码
     */
    public Message decode(ByteBuf byteBuf) {
        // 跳过2字节魔数位
        byteBuf.skipBytes(2);

        // 数据包长度
        int length = byteBuf.readInt();

        // 消息ID(4字节)
        int messageId = byteBuf.readInt();

        // 消息类型
        Byte messageType = byteBuf.readByte();

        // 跳过协议版本
        byteBuf.skipBytes(1);

        // 序列化方式
        byte serializableType = byteBuf.readByte();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Serializable serializable = getSerializable(serializableType);
        Class<? extends Message> messageClazz = getMessageType(messageType);

        if (serializable != null) {
            return serializable.deserialize(messageClazz, bytes);
        }

        System.out.println("serializable not found. serializableType=" + serializableType + " messageId = " + messageId);
        return null;
    }

    private Class<? extends Message> getMessageType(Byte messageType) {
        return messageTypeMap.get(messageType);
    }

    private Serializable getSerializable(byte serializableType) {
        return serializableMap.get(serializableType);
    }


}
