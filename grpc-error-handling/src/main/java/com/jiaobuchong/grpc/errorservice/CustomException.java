package com.jiaobuchong.grpc.errorservice;

public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }
}
