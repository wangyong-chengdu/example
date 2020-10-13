package cd.wangyong.simple_rpc2.client;

import cd.wangyong.simple_rpc2.transport.Transport;

/**
 * @author andy
 * @since 2020/10/12
 */
public interface ServiceStub {
    void setTransport(Transport transport);
}
