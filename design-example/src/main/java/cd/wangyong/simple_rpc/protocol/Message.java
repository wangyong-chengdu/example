package cd.wangyong.simple_rpc.protocol;

/**
 * @author andy
 * @since 2020/9/28
 */
public interface Message {

    /**
     * 消息类型
     */
    MessageType getMessageType();

    /**
     * 会话ID
     */
    byte getSessionId();

    /**
     * 包类型
     */
    PackageType getPackageType();

    /**
     * 消息内容
     */
    String getContent();
}
