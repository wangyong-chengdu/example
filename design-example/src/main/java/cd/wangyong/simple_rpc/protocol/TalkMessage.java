package cd.wangyong.simple_rpc.protocol;

/**
 * 谈话消息
 * @author andy
 * @since 2020/9/28
 */
public class TalkMessage implements Message, Cloneable{
    /**
     * 第几轮编号
     */
    private Integer roundId;

    /**
     * 消息类型
     */
    private MessageType messageType;

    /**
     * 包类型
     */
    private PackageType packageType;

    /**
     * 会话ID
     */
    private byte sessionId;

    /**
     * 说话者
     */
    private String speaker;

    /**
     * 说话内容
     */
    private String content;

    public TalkMessage(MessageType messageType, PackageType packageType, byte sessionId, String speaker, String content) {
        this.messageType = messageType;
        this.packageType = packageType;
        this.sessionId = sessionId;
        this.speaker = speaker;
        this.content = content;
    }

    public TalkMessage setRoundId(Integer roundId) {
        this.roundId = roundId;
        return this;
    }

    public Integer getRoundId() {
        return roundId;
    }

    @Override
    public MessageType getMessageType() {
        return this.messageType;
    }

    @Override
    public PackageType getPackageType() {
        return this.packageType;
    }

    public byte getSessionId() {
        return sessionId;
    }

    public String getSpeaker() {
        return this.speaker;
    }

    public String getContent() {
        return content;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "RoundId:" + roundId + ",SessionId:" + sessionId + "," + packageType.getName() + "[" + speaker + ":" + content + " ]";
    }
}
