package cd.wangyong.simple_rpc.serialization;

/**
 * 序列化类型
 */
public enum SerializeType {
    JSON((byte) 0),
    PROTOBUF((byte) 1),
    KRYO((byte) 2);

    private byte code;

    SerializeType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
