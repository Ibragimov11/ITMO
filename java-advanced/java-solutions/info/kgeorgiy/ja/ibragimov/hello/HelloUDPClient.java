package info.kgeorgiy.ja.ibragimov.hello;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Said Ibragimov on 12.05.2022 00:22
 */
public class HelloUDPClient extends AbstractClient {
    @Override
    public void run(final String host, final int port, final String prefix, final int threadsNumber, final int requestsNumber) {
        final SocketAddress socketAddress = new InetSocketAddress(host, port);
        final ExecutorService service = Executors.newFixedThreadPool(threadsNumber);

        for (int threadId = 0; threadId < threadsNumber; threadId++) {
            service.submit(getRunnable(socketAddress, prefix, threadId, requestsNumber));
        }

        Utils.closeExecutorService(10L * threadsNumber * requestsNumber, service);
    }

    private Runnable getRunnable(final SocketAddress socketAddress, final String prefix,
                                 final int threadId, final int requestsNumber) {
        return () -> {
            try (final DatagramSocket socket = new DatagramSocket()) {
                socket.setSoTimeout(100);

                final int bufSize = socket.getReceiveBufferSize();
                final DatagramPacket requestDatagramPacket = Utils.createPacketWithAddress(bufSize, socketAddress);

                for (int requestId = 0; requestId < requestsNumber; requestId++) {
                    final String request = Utils.createRequest(prefix, threadId, requestId);
                    requestDatagramPacket.setData(Utils.stringToBytes(request));

                    while (!Thread.interrupted() && !socket.isClosed()) {
                        try {
                            socket.send(requestDatagramPacket);

                            final DatagramPacket responseDatagramPacket = Utils.createPacketWithAddress(bufSize, socketAddress);
                            socket.receive(responseDatagramPacket);

                            final String response = Utils.getPacketBody(responseDatagramPacket);
                            if (Utils.isValidResponse(request, response)) {
//                                    Utils.log("Response: " + response);
                                break;
                            }
                        } catch (IOException e) {
//                                    error("Error when sending a request or receiving a response: ", e);
                        }
                    }
                }
            } catch (SocketException e) {
//                    Utils.error("Problems with socket: ", e);
            }
        };
    }

    public static void main(String[] args) {
        runMain(HelloUDPClient.class, args);
    }
}
