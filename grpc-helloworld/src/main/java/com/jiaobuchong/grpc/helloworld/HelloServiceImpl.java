package com.jiaobuchong.grpc.helloworld;

import com.jiaobuchong.proto.helloworld.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class HelloServiceImpl extends GreeterServiceGrpc.GreeterServiceImplBase {
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        System.out.println("sayHello.......");
        String greeting = new StringBuilder()
                .append("Hello, ")
                .append(request.getFirstName())
                .append(" ")
                .append(request.getLastName())
                .toString();

        HelloResponse response = HelloResponse.newBuilder()
                .setMessage(greeting)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // 自定义异常处理
    @Override
    public void customException(EchoRequest request, StreamObserver<EchoResponse> responseObserver) {

        try {
            if (request.getMessage().equals("error")) {
                throw new CustomException("custom exception message");
            }
            EchoResponse echoResponse = EchoResponse.newBuilder().build();
            responseObserver.onNext(echoResponse);
            responseObserver.onCompleted();
        } catch (CustomException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription(e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }
}
