package info.kgeorgiy.ja.ibragimov.hello;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Said Ibragimov on 12.05.2022 00:22
 */
public class HelloUDPServer extends AbstractServer {
    private DatagramSocket socket;
    private ExecutorService service;

    // :NOTE: утечка ресурсов при повторных вызовах
    @Override
    public void start(final int port, final int threadsNumber) {
        try {
            socket = new DatagramSocket(port);
            service = Executors.newFixedThreadPool(threadsNumber);

            final int bufSize = socket.getSendBufferSize();

            for (int threadID = 0; threadID < threadsNumber; threadID++) {
                service.submit(() -> {
                    while (!socket.isClosed()) {
                        final DatagramPacket packet = Utils.createPacket(bufSize);
                        try {
                            socket.receive(packet);
                            // :NOTE: Несмотря на то, что текущий способ получения ответа по запросу очень прост,
                            // сервер должен быть рассчитан на ситуацию,
                            // когда этот процесс может требовать много ресурсов и времени.
                            packet.setData(Utils.stringToBytes(Utils.createResponse(Utils.getPacketBody(packet))));
                            socket.send(packet);
                        } catch (IOException e) {
//                            Utils.error("Error when sending a request or receiving a response: ", e);
                        }
                    }
                });
            }
        } catch (SocketException e) {
//            Utils.error("Problems with socket: ", e);
        }
    }

    @Override
    public void close() {
        socket.close();
        Utils.closeExecutorService(5L, service);
    }

    public static void main(String[] args) {
        runMain(HelloUDPServer.class, args);
    }
}
