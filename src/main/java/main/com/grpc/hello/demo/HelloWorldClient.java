package main.com.grpc.hello.demo;


import com.grpc.practice.generated.GreeterGrpc;
import com.grpc.practice.generated.HelloReply;
import com.grpc.practice.generated.HelloRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class HelloWorldClient {
	private final ManagedChannel channel;
	private final GreeterGrpc.GreeterBlockingStub blockingStub;

	public HelloWorldClient(String host, int port) {
		this(ManagedChannelBuilder.forAddress(host, port)
				.usePlaintext()
				.build());
	}

	public HelloWorldClient(ManagedChannel channel) {
		this.channel = channel;
		this.blockingStub = GreeterGrpc.newBlockingStub(channel);

	}

	public static void main(String[] args) throws InterruptedException {
		HelloWorldClient client = new HelloWorldClient("localhost", 50051);
		String user = args.length > 0 ? args[0] : "world";
		try {
			client.greet(user);
		} finally {
			client.shutdown();
		}
	}

	private void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	private void greet(String user) {
		HelloRequest request = HelloRequest.newBuilder().setName(user).build();
		HelloReply response = blockingStub.sayHello(request);
		System.out.println("Greeting: " + response.getMessage());
	}
}