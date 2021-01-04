package cd.wangyong.concurrent_example.分工;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author andy
 * @since 2020/12/27
 */
public class ThreadPerMessageExample {

    /**
     * 服务端请求处理：为每个请求创建一个线程处理。可行性不高。
     * @throws IOException
     */
    public void handle() throws IOException {
        final ServerSocketChannel ssc = ServerSocketChannel.open().bind(new InetSocketAddress(8080));
        try {
            while (true) {
                // accept a connect
                SocketChannel sc = ssc.accept();
                new Thread(() -> {
                    try {
                        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
                        sc.read(buffer);

                        //模拟处理请求
                        Thread.sleep(2000);
                        // 调用flip之后，读写指针指到缓存头部，并且设置了最多只能读出之前写入的数据长度(而不是整个缓存的容量大小)
                        ByteBuffer writeBuffer = (ByteBuffer) buffer.flip();
                        sc.write(writeBuffer);
                        sc.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
        finally {
            ssc.close();
        }
    }

    public static void main(String[] args) throws IOException {
        ThreadPerMessageExample example = new ThreadPerMessageExample();
        example.handle();
    }
}
