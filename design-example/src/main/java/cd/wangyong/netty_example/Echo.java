package cd.wangyong.netty_example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * echo网络程序
 * @author andy
 * @since 2020/12/31
 */
public class Echo {
    /**
     * Echo连接处理
     */
    static class EchoServerHandler extends ChannelInboundHandlerAdapter {
        // 处理读事件
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ctx.write(msg);
        }

        // 处理读完成事件
        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        // 处理异常事件
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final EchoServerHandler serverHandler = new EchoServerHandler();

        // boss线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);

        // worker线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(serverHandler);
                        }
                    });

            ChannelFuture channelFuture = bootstrap.bind(9090).sync();
            channelFuture.channel().closeFuture().sync();
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
