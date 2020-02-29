package com.jiaobuchong.grpc.errorservice;

import com.jiaobuchong.proto.errorservice.ErrorServiceGrpc;
import com.jiaobuchong.proto.errorservice.HiRequest;
import com.jiaobuchong.proto.errorservice.HiResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class HelloWorldGrpcClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        ErrorServiceGrpc.ErrorServiceBlockingStub stub
                = ErrorServiceGrpc.newBlockingStub(channel);

        try {
            HiResponse helloResponse = stub.sayHello(HiRequest.newBuilder()
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
