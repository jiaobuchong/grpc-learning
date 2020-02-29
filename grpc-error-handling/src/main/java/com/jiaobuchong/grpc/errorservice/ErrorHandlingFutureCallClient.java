package com.jiaobuchong.grpc.errorservice;

import com.google.common.util.concurrent.ListenableFuture;
import com.jiaobuchong.proto.errorservice.EchoRequest;
import com.jiaobuchong.proto.errorservice.EchoResponse;
import com.jiaobuchong.proto.errorservice.ErrorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.ExecutionException;

public class ErrorHandlingFutureCallClient {
    public static void main(String[] args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        ErrorServiceGrpc.ErrorServiceFutureStub stub
                = ErrorServiceGrpc.newFutureStub(channel);

        try {
            ListenableFuture<EchoResponse> echoResponse = stub.customException(
                    EchoRequest.newBuilder().setMessage("error").build());
            System.out.println(echoResponse.get().getMessage());
        } catch (ExecutionException e) {
            // 异步请求抛出来的请求会不一样，需要处理 cause
            e.printStackTrace();
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
