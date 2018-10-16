package main.com.grpc.hello.demo;


import com.grpc.practice.generated.GreeterGrpc;
import com.grpc.practice.generated.HelloReply;
import com.grpc.practice.generated.HelloRequest;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class HelloWorldServer {
	private Server server;

	public static void main(String[] args) throws IOException, InterruptedException {
		HelloWorldServer server = new HelloWorldServer();
		server.start();
		server.blockUntilShutdown();
	}

	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	private void start() throws IOException {
		int port = 50051;
		server = ServerBuilder.forPort(port)
				.addService(new GreeterImpl())
				.build()
				.start();
		System.out.println("Started...");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("Shutting down...");
				HelloWorldServer.this.stop();
			}
		});
	}

	private void stop() {
		if (server != null) {
			server.shutdown();
		}
	}

	class GreeterImpl extends GreeterGrpc.GreeterImplBase {
		@Override
		public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
			System.out.println("Got request for : " + request.getName());
			HelloReply helloReply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
			responseObserver.onNext(helloReply);
			responseObserver.onCompleted();
		}
	}
}