package cd.wangyong.simple_rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author andy
 * @since 2020/9/28
 */
public class NettyClient {
    private volatile Bootstrap bootstrap;

    public NettyClient start(ChannelHandler channelHandler) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        bootstrap =  new Bootstrap().group(workerGroup)
                .channel(NioSocketChannel .class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
                        ch.pipeline().addLast(channelHandler);
                    }
                });
        return this;
    }

    public ChannelFuture connect(String host, int port) {
        if (bootstrap == null) {
            throw new RuntimeException("before connect, please execute start() first.");
        }

        return bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("connect host: " + host + ",port:" + port + " success.");
            } else {
                System.err.println("connect host: " + host + ",port:" + port + " fail.");
            }
        });
    }
}
