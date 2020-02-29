package com.jiaobuchong.grpc.errorservice;

import com.jiaobuchong.proto.errorservice.EchoRequest;
import com.jiaobuchong.proto.errorservice.EchoResponse;
import com.jiaobuchong.proto.errorservice.ErrorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class GrpcErrorHandlingUnknownClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        ErrorServiceGrpc.ErrorServiceBlockingStub stub
                = ErrorServiceGrpc.newBlockingStub(channel);

        System.out.println("-----------------------------------");
        try {
            EchoResponse echoResponse = stub.customUnwrapException(EchoRequest.newBuilder().build());
        } catch (StatusRuntimeException e) {
            e.printStackTrace();
        }



        channel.shutdown();
    }
}
