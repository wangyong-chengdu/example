package cd.wangyong.concurrent_example.互斥.等待通知机制;

import java.util.Objects;

/**
 * @author andy
 * @since 2020/12/27
 */
public class GuardedSuspensionExample {
    static class Message {
        String id;
        String content;

        public Message(String id, String content) {
            this.id = id;
            this.content = content;
        }
    }

    /**
     * 发送消息
     */
    public void send(Message message) {
        // TODO: 2020/12/27  
    }

    /**
     * 消费消息
     */
    public void onMessage(Message message) {
        GuardedObject.fireEvent(message.id, message);
    }

    /**
     * 处理浏览器发来的请求
     */
    public void handleWebReq() {
        int id = 1;
        Message msg = new Message(id + "", "{...}");
        GuardedObject guardedObject = GuardedObject.create(id);
        
        // 发送消息
        send(msg);
        
        // 等待mq返回的消息
        Message message = (Message) guardedObject.get(Objects::nonNull);
    }
}
