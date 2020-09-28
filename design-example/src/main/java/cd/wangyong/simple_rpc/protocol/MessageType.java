package cd.wangyong.simple_rpc.protocol;

/**
 * 消息类型
 * @author andy
 * @since 2020/9/28
 */
public enum MessageType {
    TALK((byte) 1, "TALK PACKAGE");

    private byte code;
    private String name;

    MessageType(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
