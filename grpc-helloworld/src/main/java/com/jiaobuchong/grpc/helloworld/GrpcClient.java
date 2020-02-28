package com.jiaobuchong.grpc.helloworld;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.jiaobuchong.proto.helloworld.GreeterServiceGrpc;
import com.jiaobuchong.proto.helloworld.HelloRequest;
import com.jiaobuchong.proto.helloworld.HelloResponse;
import io.grpc.StatusRuntimeException;

public class GrpcClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        GreeterServiceGrpc.GreeterServiceBlockingStub stub
                = GreeterServiceGrpc.newBlockingStub(channel);

        try {
            HelloResponse helloResponse = stub.sayHello(HelloRequest.newBuilder()
                    .setFirstName("jack ")
                    .setLastName("chou")
                    .build());
            System.out.println(helloResponse.getMessage());
        } catch (StatusRuntimeException e) {
            e.printStackTrace();
        }



        channel.shutdown();
    }
}
