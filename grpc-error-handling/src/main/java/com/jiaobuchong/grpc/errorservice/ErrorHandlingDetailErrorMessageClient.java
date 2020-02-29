package com.jiaobuchong.grpc.errorservice;

import com.jiaobuchong.proto.errorservice.EchoRequest;
import com.jiaobuchong.proto.errorservice.EchoResponse;
import com.jiaobuchong.proto.errorservice.ErrorInfo;
import com.jiaobuchong.proto.errorservice.ErrorServiceGrpc;
import io.grpc.*;
import io.grpc.protobuf.ProtoUtils;

public class ErrorHandlingDetailErrorMessageClient {
    private static final Metadata.Key<ErrorInfo> ERROR_INFO_TRAILER_KEY =
            ProtoUtils.keyForProto(ErrorInfo.getDefaultInstance());

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        ErrorServiceGrpc.ErrorServiceBlockingStub stub
                = ErrorServiceGrpc.newBlockingStub(channel);

        try {
            EchoResponse echoResponse = stub.detailErrorMessage(
                    EchoRequest.newBuilder().setMessage("error").build());
            System.out.println(echoResponse.getMessage());
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.INVALID_ARGUMENT) {
                Metadata trailers = Status.trailersFromThrowable(e);
                if (trailers.containsKey(ERROR_INFO_TRAILER_KEY)) {
                    ErrorInfo errorInfo = trailers.get(ERROR_INFO_TRAILER_KEY);
                    if (errorInfo.getMessageList() != null && errorInfo.getMessageList().size() != 0) {
                        // 这就是我们想要的自定义异常的信息
                        System.out.println(errorInfo.getMessageList());
                    }
                }
            } else {
                throw e;
            }
        }
        channel.shutdown();
    }
}
