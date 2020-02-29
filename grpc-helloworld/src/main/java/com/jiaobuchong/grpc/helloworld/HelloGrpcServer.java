package com.jiaobuchong.grpc.helloworld;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * grpc server创建参考： https://www.baeldung.com/grpc-introduction
 */
public class HelloGrpcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder
                .forPort(8080)
                .addService(new HelloServiceImpl()).build();

        System.out.println("grpc server starting ");
        server.start();
        server.awaitTermination();
    }
}
