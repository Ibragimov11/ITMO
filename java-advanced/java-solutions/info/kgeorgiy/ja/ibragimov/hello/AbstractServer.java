package info.kgeorgiy.ja.ibragimov.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * @author Said Ibragimov on 26.05.2022 05:07
 */
public abstract class AbstractServer implements HelloServer {
    static protected void runMain(final Class<? extends AbstractServer> serverClass, final String... args) {
        if (Utils.validMainArgs(args, 2, new Utils.PredicateWithMessage(
                a -> Arrays.stream(a).allMatch(Utils::isPositiveNumber),
                "Invalid args: both should be a positive number"))) {
            try (HelloServer server = serverClass.getDeclaredConstructor().newInstance()) {
                server.start(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            } catch (NumberFormatException e) {
                Utils.error("Invalid number in args: it is too large");
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                     NoSuchMethodException ignored) {
            }
        }
    }
}
