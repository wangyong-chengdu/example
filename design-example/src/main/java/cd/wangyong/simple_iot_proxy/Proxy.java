package cd.wangyong.simple_iot_proxy;

/**
 * @author andy
 * @since 2020/10/10
 */
public interface Proxy {

    /**
     * 发送消息
     * @param message
     * @return
     */
    boolean send(Message message);

    /**
     * 接收消息
     * @return
     */
    Message receive();
}


