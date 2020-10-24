package cd.wangyong.simple_rpc.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import io.netty.channel.ChannelFuture;

import cd.wangyong.simple_rpc.client.NettyClient;
import cd.wangyong.simple_rpc.client.ZhangHandler;
import cd.wangyong.simple_rpc.server.LiHandler;
import cd.wangyong.simple_rpc.server.NettyRemoteServer;
import cd.wangyong.simple_rpc.server.RemoteServer;

/**
 * @author andy
 * @since 2020/9/28
 */
@RunWith(JUnit4.class)
public class SimpleRpcTest {
    public static final String HOST = "127.0.0.1";
    public static final int PORT = 18000;
    public static final int TOTAL = 1000000;

    @Test
    public void test() throws InterruptedException, CloneNotSupportedException {
        RemoteServer remoteServer = new NettyRemoteServer();
        ChannelFuture serverChannelFuture = remoteServer.start(new LiHandler()).bind(PORT);

        NettyClient client = new NettyClient();
        ZhangHandler zhangHandler = new ZhangHandler();
        ChannelFuture clientChannelFuture = client.start(zhangHandler).connect(HOST, PORT);

        serverChannelFuture.await();
        clientChannelFuture.await();

        for (int i = 0; i < TOTAL; i++) {
            zhangHandler.invoke(HOST);
        }

        while (Thread.activeCount() > 0) {
            Thread.yield();
        }
    }
}
