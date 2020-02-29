package com.jiaobuchong.grpc.errorservice;

import com.google.rpc.DebugInfo;
import com.jiaobuchong.proto.errorservice.*;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.stub.StreamObserver;

public class ErrorServiceImpl extends ErrorServiceGrpc.ErrorServiceImplBase {

    private static final Metadata.Key<ErrorInfo> ERROR_INFO_TRAILER_KEY =
            ProtoUtils.keyForProto(ErrorInfo.getDefaultInstance());

    @Override
    public void sayHello(HiRequest request, StreamObserver<HiResponse> responseObserver) {
        System.out.println("sayHello.......");
        String greeting = new StringBuilder()
                .append("Hello, ")
                .append(request.getFirstName())
                .append(" ")
                .append(request.getLastName())
                .toString();

        HiResponse response = HiResponse.newBuilder()
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

    @Override
    public void customUnwrapException(EchoRequest request, StreamObserver<EchoResponse> responseObserver) {
        responseObserver.onError(new CustomException("custom exception"));
    }

    @Override
    public void detailErrorMessage(EchoRequest request, StreamObserver<EchoResponse> responseObserver) {
        try {
            if (request.getMessage().equals("error")) {
                throw new CustomException("custom exception message");
            }
            EchoResponse echoResponse = EchoResponse.newBuilder().build();
            responseObserver.onNext(echoResponse);
            responseObserver.onCompleted();
        } catch (CustomException e) {
            Metadata trailers = new Metadata();
            ErrorInfo.Builder builder = ErrorInfo.newBuilder()
                    .addMessage(e.getMessage());
            trailers.put(ERROR_INFO_TRAILER_KEY, builder.build());
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withCause(e)
                    .asRuntimeException(trailers));
        }
    }
}
