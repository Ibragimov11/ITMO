package info.kgeorgiy.ja.ibragimov.hello;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Said Ibragimov on 18.05.2022 16:07
 */
public class HelloUDPNonblockingClient extends AbstractClient {
    private static final class ChannelContext {
        private final int threadId;
        private int requestId;

        private ChannelContext(final int threadId, final int requestId) {
            this.threadId = threadId;
            this.requestId = requestId;
        }

        public int requestId() {
            return requestId;
        }

        public int threadId() {
            return threadId;
        }

        public void incrementRequestId() {
            requestId++;
        }
    }

    @Override
    public void run(final String host, final int port, final String prefix, final int threadsNumber, final int requestsNumber) {
        try (final Selector selector = Selector.open()) {
            final SocketAddress socketAddress = new InetSocketAddress(host, port);

            for (int threadId = 0; threadId < threadsNumber; threadId++) {
                try {
                    DatagramChannel.open()
                            .connect(socketAddress)
                            .configureBlocking(false)
                            .register(selector, SelectionKey.OP_WRITE, new ChannelContext(threadId, 0));
                } catch (IOException e) {
//                    Utils.error("Error creating the DatagramChannel", e);
                }
            }

            while (!Thread.interrupted() && !selector.keys().isEmpty()) {
                try {
                    selector.select(100);
                } catch (IOException e) {
//                    Utils.error("Error with selector.select", e);
                }

                final Set<SelectionKey> selectedKeys = selector.selectedKeys();
                if (!selectedKeys.isEmpty()) {
                    for (final Iterator<SelectionKey> iterator = selectedKeys.iterator(); iterator.hasNext(); ) {
                        final SelectionKey selectionKey = iterator.next();

                        if (selectionKey.isValid()) {
                            if (selectionKey.isReadable()) {
                                receive(prefix, requestsNumber, selectionKey);
                            } else if (selectionKey.isWritable()) {
                                send(prefix, socketAddress, selectionKey);
                            }
                        }

                        iterator.remove();
                    }
                } else {
                    selector.keys().forEach(key -> key.interestOps(SelectionKey.OP_WRITE));
                }
            }
        } catch (IOException e) {
//            Utils.error("Error opening the Selector", e);
        }
    }

    private void receive(final String prefix, final int requestsNumber, final SelectionKey selectionKey) {
        final DatagramChannel datagramChannel = (DatagramChannel) selectionKey.channel();
        final ChannelContext context = (ChannelContext) selectionKey.attachment();
        final ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        try {
            datagramChannel.receive(byteBuffer);
        } catch (IOException e) {
//            Utils.error("Error when reading the request", e);
        }

        byteBuffer.flip();

        final String request = Utils.createRequest(prefix, context.threadId(), context.requestId());
        final String response = Utils.byteBufferToString(byteBuffer);

        if (Utils.isValidResponse(request, response)) {
//            Utils.log("Response: " + response);
            context.incrementRequestId();
        }

        if (context.requestId() < requestsNumber) {
            selectionKey.interestOps(SelectionKey.OP_WRITE);
        } else {
            try {
                selectionKey.channel().close();
            } catch (IOException e) {
//                Utils.error("Error when closing the DatagramChannel", e);
            }
        }
    }

    private void send(final String prefix, final SocketAddress socketAddress, final SelectionKey selectionKey) {
        final DatagramChannel datagramChannel = (DatagramChannel) selectionKey.channel();
        final ChannelContext context = (ChannelContext) selectionKey.attachment();
        final String request = Utils.createRequest(prefix, context.threadId(), context.requestId());

        try {
            datagramChannel.send(Utils.stringToByteBuffer(request), socketAddress);
        } catch (IOException e) {
//            Utils.error("Error when writing the response", e);
        }

        selectionKey.interestOps(SelectionKey.OP_READ);
    }

    public static void main(String[] args) {
        runMain(HelloUDPNonblockingClient.class, args);
    }
}
