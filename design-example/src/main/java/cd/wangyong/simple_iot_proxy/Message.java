package cd.wangyong.simple_iot_proxy;

/**
 * 主题-队列-消息
 * @author andy
 * @since 2020/10/10
 */
public class Message {

    private String topic;
    private String queueId;
    private String message;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
