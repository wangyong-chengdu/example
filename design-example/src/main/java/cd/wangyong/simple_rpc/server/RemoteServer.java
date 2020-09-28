package cd.wangyong.simple_rpc.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;

/**
 * @author andy
 * @since 2020/9/28
 */
public interface RemoteServer {
    RemoteServer start(ChannelHandler channelHandler);
    ChannelFuture bind(int port);
    void shutdown();
}
