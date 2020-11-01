package cd.wangyong.grpc_example.grpc.hello.server;

import io.grpc.stub.StreamObserver;

import cd.wangyong.grpc_example.grpc.hello.HelloReply;
import cd.wangyong.grpc_example.grpc.hello.HelloRequest;
import cd.wangyong.grpc_example.grpc.hello.HelloServiceGrpc;

public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void say(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
        System.out.println("server reply:" + reply);
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
