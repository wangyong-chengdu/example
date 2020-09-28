package cd.wangyong.simple_rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 服务端启动、关闭
 * @author andy
 * @since 2020/9/28
 */
public class NettyRemoteServer implements RemoteServer{

    private volatile EventLoopGroup group;
    private volatile ServerBootstrap bootstrap;

    @Override
    public RemoteServer start(ChannelHandler channelHandler) {
        group = new NioEventLoopGroup();
        try {
            bootstrap = new ServerBootstrap().group(group)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
                            socketChannel.pipeline().addLast(channelHandler);
                        }
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public ChannelFuture bind(int port) {
        if (bootstrap == null) {
            throw new RuntimeException("before bind, please execute start() first.");
        }

        return bootstrap.bind(port).addListener(future -> {
            if (!future.isSuccess()) System.err.println("bind port:"+ port + " fail.");
            else System.out.println("bind port:"+ port + " success.");
        });
    }

    @Override
    public void shutdown()  {
        try {
            if (group != null)
                group.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
