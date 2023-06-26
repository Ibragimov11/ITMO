package info.kgeorgiy.ja.ibragimov.hello;

import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * @author Said Ibragimov on 12.05.2022 05:48
 */
class Utils {
    private Utils() {
    }

    static DatagramPacket createPacket(final int bufSize) {
        return new DatagramPacket(new byte[bufSize], bufSize);
    }

    static DatagramPacket createPacketWithAddress(final int bufSize, final SocketAddress socketAddress) {
        return new DatagramPacket(new byte[bufSize], bufSize, socketAddress);
    }

    static String getPacketBody(final DatagramPacket packet) {
        return new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
    }

    static String createRequest(final String prefix, final int threadId, final int requestId) {
        return prefix + threadId + "_" + requestId;
    }

    static String createResponse(final String s) {
        return "Hello, " + s;
    }

    static boolean isValidResponse(final String request, final String response) {
        return response.contains(request);
    }

    static byte[] stringToBytes(final String s) {
        return s.getBytes(StandardCharsets.UTF_8);
    }

    static ByteBuffer stringToByteBuffer(final String s) {
        return ByteBuffer.wrap(stringToBytes(s));
    }

    static String byteBufferToString(final ByteBuffer byteBuffer) {
        return StandardCharsets.UTF_8.decode(byteBuffer).toString();
    }

    static void closeExecutorService(final long timeInSeconds, final ExecutorService service) {
        service.shutdown();
        try {
            if (!service.awaitTermination(timeInSeconds, TimeUnit.SECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException ignored) {
        }
    }

    static boolean isPositiveNumber(final String s) {
        return s.matches("[1-9]+\\d*");
    }

    record PredicateWithMessage(Predicate<String[]> predicate, String msg) {
    }

    static boolean validMainArgs(final String[] args, final int expectedSize, final PredicateWithMessage... predicates) {
        if (args == null || Arrays.stream(args).anyMatch(Objects::isNull)) {
            error("Null is not allowed in args");
            return false;
        }

        if (args.length != expectedSize) {
            error("Incorrect number of args: there should be " + expectedSize);
            return false;
        }

        for (final PredicateWithMessage p : predicates) {
            if (!p.predicate.test(args)) {
                error(p.msg);
                return false;
            }
        }

        return true;
    }

    static void error(final String msg, final Exception e) {
        error(msg + e.getMessage());
    }

    static void error(final String msg) {
        System.err.println(msg);
    }

    static void log(final String msg) {
        System.out.println(msg);
    }
}
