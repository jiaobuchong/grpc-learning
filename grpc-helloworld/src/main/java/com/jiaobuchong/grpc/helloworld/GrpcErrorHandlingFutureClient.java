package com.jiaobuchong.grpc.helloworld;

import com.google.common.util.concurrent.ListenableFuture;
import com.jiaobuchong.proto.helloworld.EchoRequest;
import com.jiaobuchong.proto.helloworld.EchoResponse;
import com.jiaobuchong.proto.helloworld.GreeterServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.ExecutionException;

public class GrpcErrorHandlingFutureClient {
    public static void main(String[] args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        GreeterServiceGrpc.GreeterServiceFutureStub stub
                = GreeterServiceGrpc.newFutureStub(channel);

        try {
            ListenableFuture<EchoResponse> echoResponse = stub.customException(
                    EchoRequest.newBuilder().setMessage("error").build());
            System.out.println(echoResponse.get().getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            // INVALID_ARGUMENT: occurs exception
            // 这个message 会包含 INVALID_ARGUMENT, 不是我们想需要的
            System.out.println(e.getMessage());
            if (e.getCause() instanceof StatusRuntimeException) {
                StatusRuntimeException statusRuntimeException = (StatusRuntimeException) e.getCause();
                if (statusRuntimeException.getStatus().getCode() == Status.Code.INVALID_ARGUMENT) {
                    // 这就是我们想要的自定义异常的信息
                    System.out.println(statusRuntimeException.getStatus().getDescription());
                    // 抛出 CustomException, 方便我们的 ExceptionHandler 处理
                    throw new CustomException(statusRuntimeException.getStatus().getDescription());
                }
            }

        }


        channel.shutdown();
    }
}
