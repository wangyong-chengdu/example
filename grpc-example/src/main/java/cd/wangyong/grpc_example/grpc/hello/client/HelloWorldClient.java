package cd.wangyong.grpc_example.grpc.hello.client;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import cd.wangyong.grpc_example.grpc.hello.HelloReply;
import cd.wangyong.grpc_example.grpc.hello.HelloRequest;
import cd.wangyong.grpc_example.grpc.hello.HelloServiceGrpc;

public class HelloWorldClient {
    private final ManagedChannel channel;
    private final HelloServiceGrpc.HelloServiceBlockingStub blockingStub;

    public HelloWorldClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext().build());
    }

    public HelloWorldClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = HelloServiceGrpc.newBlockingStub(channel);
    }

    /**
     * 手动关闭
     * @throws InterruptedException
     */
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void say(String name) {
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply reply;
        try {
            reply = blockingStub.say(request);
        } catch (Exception e) {
            return;
        }
        System.out.println(reply);
    }

    public static void main(String[] args) throws InterruptedException {
        HelloWorldClient client = new HelloWorldClient("127.0.0.1", 50051);
        try {
            client.say("andy");
            client.say("lulu");
            client.say("world");
        } finally {
            client.shutdown();
        }
    }
}

