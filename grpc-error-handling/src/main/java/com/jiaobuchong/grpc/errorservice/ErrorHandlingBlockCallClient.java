package com.jiaobuchong.grpc.errorservice;

import com.jiaobuchong.proto.errorservice.EchoRequest;
import com.jiaobuchong.proto.errorservice.EchoResponse;
import com.jiaobuchong.proto.errorservice.ErrorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

public class ErrorHandlingBlockCallClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        ErrorServiceGrpc.ErrorServiceBlockingStub stub
                = ErrorServiceGrpc.newBlockingStub(channel);

        try {
            EchoResponse echoResponse = stub.customException(
                    EchoRequest.newBuilder().setMessage("error").build());
            System.out.println(echoResponse.getMessage());
        } catch (StatusRuntimeException e) {
            e.printStackTrace();
            // INVALID_ARGUMENT: occurs exception
            // 这个message 会包含 INVALID_ARGUMENT, 不是我们想需要的
            System.out.println(e.getMessage());
            if (e.getStatus().getCode() == Status.Code.INVALID_ARGUMENT) {
                // 这就是我们想要的自定义异常的信息
                System.out.println(e.getStatus().getDescription());
                // 抛出 CustomException, 方便我们的 ExceptionHandler 处理
                throw new CustomException(e.getStatus().getDescription());
            } else {
                throw e;
            }
        }
        channel.shutdown();
    }
}
