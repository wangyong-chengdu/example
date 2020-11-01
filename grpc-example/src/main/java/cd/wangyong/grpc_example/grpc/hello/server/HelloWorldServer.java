package cd.wangyong.grpc_example.grpc.hello.server;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class HelloWorldServer {
    private volatile Server server;

    /**
     * 服务暴露
     */
    private void start() throws IOException {
        int port = 50051;
        server = ServerBuilder.forPort(port)
                .addService(new HelloServiceImpl())
                .build()
                .start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                HelloWorldServer.this.stop();
            }
        });
    }

    /**
     * 关闭端口
     */
    private void stop() {
        if (server != null)
            server.shutdown();
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null)
            server.awaitTermination();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final HelloWorldServer server = new HelloWorldServer();
        server.start();
        while(Thread.activeCount() > 0) {
            Thread.yield();
        }
        server.blockUntilShutdown();
    }
}
