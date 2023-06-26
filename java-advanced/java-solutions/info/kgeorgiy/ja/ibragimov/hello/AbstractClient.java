package info.kgeorgiy.ja.ibragimov.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

/**
 * @author Said Ibragimov on 26.05.2022 05:09
 */
public abstract class AbstractClient implements HelloClient {
    static protected void runMain(final Class<? extends AbstractClient> clientClass, final String... args) {
        if (Utils.validMainArgs(args, 5, new Utils.PredicateWithMessage(
                a -> Stream.of(a[1], a[3], a[4]).allMatch(Utils::isPositiveNumber),
                "Invalid args: the second, fourth and fifth should be a positive number"))) {
            try {
                clientClass.getDeclaredConstructor().newInstance()
                        .run(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
            } catch (NumberFormatException e) {
                Utils.error("Invalid number in args: it is too large");
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                     NoSuchMethodException ignored) {
            }
        }

    }
}
