package info.kgeorgiy.ja.ibragimov.hello;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Said Ibragimov on 18.05.2022 16:08
 */
public class HelloUDPNonblockingServer extends AbstractServer {
    private Selector selector;
    private ExecutorService listener;

    @Override
    public void start(int port, int threads) {
        try {
            selector = Selector.open();
        } catch (IOException e) {
//            Utils.error("Error opening the Selector", e);
            return;
        }

        listener = Executors.newSingleThreadExecutor();
        listener.submit(() -> {
            try (final DatagramChannel channel = DatagramChannel.open()) {
                channel
                        .bind(new InetSocketAddress(port))
                        .configureBlocking(false)
                        .register(selector, SelectionKey.OP_READ);

                while (!Thread.interrupted() && selector.isOpen()) {
                    try {
                        selector.select(100);
                    } catch (IOException e) {
//                        Utils.error("Error with select", e);
                    }

                    for (final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator(); iterator.hasNext(); ) {
                        final SelectionKey selectionKey = iterator.next();

                        if (selectionKey.isValid() && selectionKey.isReadable()) {
                            receiveAndSend(selectionKey);
                        }

                        iterator.remove();
                    }
                }
            } catch (IOException e) {
//                Utils.error("Error creating the DatagramChannel", e);
            }
        });
    }

    private void receiveAndSend(SelectionKey selectionKey) {
        final DatagramChannel datagramChannel = (DatagramChannel) selectionKey.channel();
        final ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        try {
            final SocketAddress socketAddress = datagramChannel.receive(byteBuffer);
            byteBuffer.flip();
            datagramChannel.send(Utils.stringToByteBuffer(Utils.createResponse(Utils.byteBufferToString(byteBuffer))), socketAddress);
        } catch (IOException e) {
//            Utils.error("Error when reading the request or writing the response", e);
        }
    }

    @Override
    public void close() {
        try {
            selector.close();
        } catch (IOException e) {
//            Utils.error("Error when closing the selector", e);
        }

        Utils.closeExecutorService(5L, listener);
    }

    public static void main(String[] args) {
        runMain(HelloUDPNonblockingServer.class, args);
    }
}
