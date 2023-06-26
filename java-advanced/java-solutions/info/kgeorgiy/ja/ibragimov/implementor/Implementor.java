package info.kgeorgiy.ja.ibragimov.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.JarImpler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

/**
 * Class Implementor. This class allows you to create java files and generate <var>.jar</var> files.
 */
public class Implementor implements Impler, JarImpler {

    /**
     * "Impl" suffix for generated classes.
     */
    private static final String IMPL = "Impl";

    /**
     * Java keyword {@code package} for generated classes.
     */
    private static final String PACKAGE = "package";

    /**
     * Java keyword {@code class} for generated classes;
     */
    private static final String CLASS = "class";

    /**
     * Java keyword {@code implements} for generated classes.
     */
    private static final String IMPLEMENTS = "implements";

    /**
     * Java keyword {@code extends} for generated classes.
     */
    private static final String EXTENDS = "extends";

    /**
     * Java keyword {@code throws} for generated classes.
     */
    private static final String THROWS = "throws";

    /**
     * Java keyword {@code super} for generated classes.
     */
    private static final String SUPER = "super";

    /**
     * Java keyword {@code return} for generated classes.
     */
    private static final String RETURN = "return";

    /**
     * String representation of {@code null}
     */
    private static final String NULL = "null";

    /**
     * Zero.
     */
    private static final String ZERO = "0";

    /**
     * String representation of {@code false}.
     */
    private static final String FALSE = "false";

    /**
     * Open bracket. Start od each parameter list.
     */
    private static final String OPEN_BRACKET = "(";

    /**
     * Close bracket. End of each parameter list.
     */
    private static final String CLOSE_BRACKET = ")";

    /**
     * Open curly bracket. Start of each code block.
     */
    private static final String OPEN_CURLY_BRACKET = "{";

    /**
     * Close curly bracket. End of each code block.
     */
    private static final String CLOSE_CURLY_BRACKET = "}";

    /**
     * Space.
     */
    private static final String SPACE = " ";

    /**
     * Empty string.
     */
    private static final String EMPTY = "";

    /**
     * Comma - separator for parameters.
     */
    private static final String COMMA = ",";

    /**
     * Semicolon - end of each code expression for generated classes.
     */
    private static final String SEMICOLON = ";";

    /**
     * Tabulation symbol.
     */
    private static final String TAB = "\t";

    /**
     * Line separator for generated class.
     * On UNIX systems, it is "\n"; on Microsoft Windows systems it is "\r\n".
     */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * End of .java files.
     */
    private static final String END_JAVA = ".java";

    /**
     * End of .class files.
     */
    private static final String END_CLASS = ".class";

    /**
     * Wrapper class for {@link Method}.
     * Created to override methods {@link Object#equals(Object)} and {@link Object#hashCode()}
     *
     * @param method initial method
     */
    private record UniqueMethod(Method method) {

        /**
         * Comparing by {@link Method#getName()}, {@link Method#getReturnType()}
         * and {@link Method#getParameterTypes()}.
         *
         * @param o object to be compared with.
         * @return {@code true} if equal, {@code false} otherwise.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o instanceof final UniqueMethod that) {
                return method.getName().equals(that.method.getName()) &&
                        // :NOTE: в jvm методы не могут отличаться по возвращаемому значению => можно было не указывать
                        Objects.equals(method.getReturnType(), that.method.getReturnType()) &&
                        Arrays.equals(method.getParameterTypes(), that.method.getParameterTypes());
            }

            return false;
        }

        /**
         * Computing hash by {@link Method#getName()}, {@link Method#getReturnType()} and
         * {@link Method#getParameterTypes()}.
         *
         * @return hash code.
         */
        @Override
        public int hashCode() {
            return Objects.hash(method.getName(), method.getReturnType(), Arrays.hashCode(method.getParameterTypes()));
        }
    }

    /**
     * Generates code for the implemented class via {@link Implementor#implement(Class, Path)}
     * and creates a jar file from it.
     *
     * @param token   type token to create implementation for.
     * @param jarFile target <var>.jar</var> file.
     * @throws ImplerException in the next cases: <ul>
     *                         <li>if <var>jarFile</var> is null</li>
     *                         <li>if the <var>token</var> cannot be implemented</li>
     *                         <li>if an I/O error occurs</li>
     *                         </ul>
     * @see Implementor#implement(Class, Path)
     * @see Implementor#compile(Class, Path)
     * @see Implementor#generateManifest(Class, Path, Path)
     */
    @Override
    public void implementJar(final Class<?> token, final Path jarFile) throws ImplerException {
        if (jarFile == null) {
            throw new ImplerException("JarFile is null", new IllegalArgumentException());
        }

        createDirs(jarFile);
        try {
            final Path tempDir = Files.createTempDirectory(jarFile.toAbsolutePath().getParent(), "temp");
            try {
                implement(token, tempDir);
                compile(token, tempDir);
                generateManifest(token, tempDir, jarFile);
            } finally {
                Files.walkFileTree(tempDir, new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
        } catch (IOException e) {
            throw new ImplerException("Problems with jarFile: cannot create or delete files or directories", e);
        }
    }

    /**
     * Compiles {@link Class} via system java compiler.
     *
     * @param token given class.
     * @param dir   <var>class</var> directory.
     * @throws ImplerException if error occurs when compiling class.
     * @see ToolProvider#getSystemJavaCompiler()
     */
    private void compile(final Class<?> token, final Path dir) throws ImplerException {
        final String classPath;
        try {
            classPath = Path.of(token.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
        } catch (URISyntaxException e) {
            throw new ImplerException("Failed to convert URL to URI", e);
        }

        final JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        final String[] args = new String[]{generatePath(token, dir, END_JAVA).toString(), "-cp", classPath};
        final int exitCode = javaCompiler.run(null, null, null, args);
        if (exitCode != 0) {
            throw new ImplerException("Cannot compile file");
        }
    }

    /**
     * Generates {@link Manifest} by {@link Class}, directory and jarFile path.
     *
     * @param token   given class.
     * @param dir     given directory.
     * @param jarFile given jarFile.
     * @throws ImplerException if an error occurs when writing in {@link JarOutputStream}.
     */
    private void generateManifest(final Class<?> token, final Path dir, final Path jarFile) throws ImplerException {
        final Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        final String compiledClass = token.getPackageName().replace(".", "/") + "/" + getImplName(token) + END_CLASS;

        try (JarOutputStream jarOutputStream = new JarOutputStream(Files.newOutputStream(jarFile), manifest)) {
            jarOutputStream.putNextEntry(new ZipEntry(compiledClass));
            Files.copy(generatePath(token, dir, END_CLASS), jarOutputStream);
        } catch (IOException e) {
            throw new ImplerException("Cannot create jarFile or write to it ", e);
        }
    }

    /**
     * Creates a new compiled java file and writes the generated code for an implemented class or interface there.
     *
     * @param token type token to create implementation for.
     * @param root  root directory.
     * @throws ImplerException in the next cases: <ul>
     *                         <li>if <var>token</var> or <var>root</var> is null</li>
     *                         <li>if the <var>token</var> cannot be implemented</li>
     *                         <li>if an I/O error occurs</li>
     *                         </ul>
     */
    @Override
    public void implement(final Class<?> token, final Path root) throws ImplerException {
        if (token == null || root == null) {
            throw new ImplerException("Token or root is null", new IllegalArgumentException());
        }

        final int modifiers = token.getModifiers();
        if (token.isPrimitive() || token.isArray() || token.equals(Enum.class)
                || Modifier.isFinal(modifiers) || Modifier.isPrivate(modifiers)) {
            throw new ImplerException("Incorrect class", new IllegalArgumentException());
        }

        final Path file = generatePath(token, root, END_JAVA);
        createDirs(file);
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            write(writer, generateCode(token));
        } catch (IOException e) {
            throw new ImplerException("Cannot create a writer or write the result to it", e);
        }
    }

    /**
     * Generates all code for given {@link Class}.
     *
     * @param token given class.
     * @return generated code.
     * @throws ImplerException if it is not allowed to implement a class.
     * @see Implementor#generatePackage(Class)
     * @see Implementor#generateDeclaration(Class)
     * @see Implementor#generateConstructors(Class)
     * @see Implementor#generateMethods(Class)
     */
    private String generateCode(final Class<?> token) throws ImplerException {
        return String.join(LINE_SEPARATOR.repeat(2),
                generatePackage(token),
                generateDeclaration(token) + SPACE + OPEN_CURLY_BRACKET,
                generateConstructors(token),
                generateMethods(token),
                CLOSE_CURLY_BRACKET);
    }

    /**
     * Generates path to the file by given class, root directory and file's format.
     *
     * @param token given class.
     * @param root  root directory.
     * @param end   file's format.
     * @return path to the file from the directory.
     */
    private Path generatePath(final Class<?> token, final Path root, final String end) {
        return Path.of(root.toString(),
                token.getPackageName().replace(".", File.separator),
                getImplName(token) + end);
    }

    /**
     * Generates package name for given {@link Class}.
     * <p>For example, if getting {@code java.util.Random}, this method returns {@code java.util}.</p>
     *
     * @param token given class.
     * @return generated package name.
     */
    private String generatePackage(final Class<?> token) {
        final var tokenPackage = token.getPackage();

        if (tokenPackage == null) {
            return EMPTY;
        } else {
            return PACKAGE + SPACE + tokenPackage.getName() + SEMICOLON;
        }
    }

    /**
     * Generates declaration for given {@link Class}.
     * <p>The result will look something like:
     * <pre>
     *     {@code class nameImpl extends ...}    - for the class
     *     {@code class nameImpl implements ...} - for the interface
     * </pre>
     *
     * @param token given class.
     * @return generated declaration.
     */
    private String generateDeclaration(final Class<?> token) {
        return String.join(SPACE,
                CLASS,
                getImplName(token),
                token.isInterface() ? IMPLEMENTS : EXTENDS,
                token.getCanonicalName());
    }

    /**
     * Generates constructors for given {@link Class}.
     * Each of constructor is generated via {@link Implementor#generateConstructor(Constructor, String)}.
     *
     * @param token given class.
     * @return generated constructors.
     * @throws ImplerException if all constructors are private.
     * @see Implementor#generateConstructor(Constructor, String)
     * @see Implementor#generateExecutable(Executable, String, String, String)
     */
    private String generateConstructors(final Class<?> token) throws ImplerException {
        final Constructor<?>[] constructors = token.getDeclaredConstructors();
        if (constructors.length == 0) {
            return EMPTY;
        }

        if (allConstructorsArePrivate(constructors)) {
            throw new ImplerException("All constructors of the class are private",
                    new IllegalArgumentException());
        }

        final var name = getImplName(token);
        return Arrays.stream(constructors)
                .map(c -> generateConstructor(c, name))
                .collect(Collectors.joining(LINE_SEPARATOR.repeat(2)));
    }

    /**
     * Returns {@code true} if all constructors are private, {@code false} otherwise.
     *
     * @param constructors given constructors for check.
     * @return {@code true} if all constructors are private, {@code false} otherwise.
     */
    private boolean allConstructorsArePrivate(final Constructor<?>[] constructors) {
        return Arrays.stream(constructors).allMatch(c -> Modifier.isPrivate(c.getModifiers()));
    }

    /**
     * Generates string representation for {@link Constructor}.
     *
     * @param constructor given constructor.
     * @param name        name of <var>constructor</var>.
     * @return string representation of <var>constructor</var>.
     * @see Implementor#generateExecutable(Executable, String, String, String)
     */
    private String generateConstructor(final Constructor<?> constructor, final String name) {
        return generateExecutable(constructor, name, EMPTY,
                SUPER + generateParameters(constructor.getParameters(), false));
    }

    /**
     * Generates methods for given {@link Class}.
     * Each of method is generated via {@link Implementor#generateMethod(Method)}.
     *
     * @param token given class.
     * @return generated methods.
     * @see Implementor#generateMethod(Method)
     * @see Implementor#generateExecutable(Executable, String, String, String)
     */
    private String generateMethods(Class<?> token) {
        final Set<UniqueMethod> methodSet = new HashSet<>();

        addMethodsToSet(methodSet, token.getMethods());
        while (token != null) {
            addMethodsToSet(methodSet, token.getDeclaredMethods());
            token = token.getSuperclass();
        }

        return methodSet.stream()
                .map(UniqueMethod::method)
                .filter(m -> Modifier.isAbstract(m.getModifiers()))
                .map(this::generateMethod)
                .collect(Collectors.joining(LINE_SEPARATOR.repeat(2)));
    }

    /**
     * Wraps methods in {@link UniqueMethod} and adds them to the set.
     *
     * @param methodSet set of methods.
     * @param methods   methods for adding to a set.
     */
    private void addMethodsToSet(final Set<UniqueMethod> methodSet, final Method[] methods) {
        methodSet.addAll(Arrays.stream(methods).map(UniqueMethod::new).collect(Collectors.toCollection(HashSet::new)));
    }

    /**
     * Generates string representation for {@link Method}.
     *
     * @param method given method.
     * @return string representation of <var>method</var>.
     * @see Implementor#generateExecutable(Executable, String, String, String)
     */
    private String generateMethod(final Method method) {
        return generateExecutable(method, method.getName(), method.getReturnType().getCanonicalName(),
                RETURN + SPACE + generateDefaultValue(method.getReturnType()));
    }

    /**
     * Generates string representation for {@link Executable}. The result of this method will be
     * a code fragment corresponding to the {@link Method} or {@link Constructor} in string form.
     *
     * @param executable     given executable.
     * @param name           name of {@code Method} or {@code Constructor}.
     * @param returnTypeName return type for {@code Method}. It should be {@code ""} if <var>executable</var>
     *                       is a Constructor.
     * @param body           string form of <var>executable</var>'s body.
     * @return generated executable.
     */
    private String generateExecutable(
            final Executable executable,
            final String name,
            final String returnTypeName,
            final String body
    ) {
        return String.join(LINE_SEPARATOR,
                TAB + String.join(SPACE,
                        Modifier.toString(executable.getModifiers() & ~Modifier.ABSTRACT & ~Modifier.TRANSIENT),
                        returnTypeName,
                        name + generateParameters(executable.getParameters(), true),
                        generateExceptions(executable.getExceptionTypes()),
                        OPEN_CURLY_BRACKET),
                TAB.repeat(2) + body + SEMICOLON,
                TAB + CLOSE_CURLY_BRACKET);
    }

    /**
     * Generates parameters for {@link Executable}.
     *
     * <p>For example, if {@code parameters} looks like "{@code [String, double]}"
     * and {@code withTypes} is {@code true}, then this method's result will be:
     * <pre>
     *     (String arg0, double arg1)
     * </pre>
     * If {@code withTypes} is {@code false}, then:
     * <pre>
     *     (arg0, arg1)
     * </pre>
     *
     * @param parameters the Array of parameter types.
     * @param withTypes  boolean flag for including types.
     * @return generated parameters.
     */
    private String generateParameters(final Parameter[] parameters, final boolean withTypes) {
        return Arrays.stream(parameters)
                .map(p -> (withTypes ? p.getType().getCanonicalName() + SPACE : EMPTY) + p.getName())
                .collect(Collectors.joining(COMMA + SPACE, OPEN_BRACKET, CLOSE_BRACKET));
    }

    /**
     * Generates string representation of thrown exceptions. If getting array is empty, returns {@code ""}.
     * <p>For example, if {@code exceptionTypes} looks like "{@code [IOException, SecurityException]}",
     * then this method's result will be:
     * <pre>
     *     {@code throws IOException, SecurityException;}
     * </pre>
     *
     * @param exceptionTypes the array of exception types.
     * @return string representation of thrown exceptions.
     */
    private String generateExceptions(final Class<?>[] exceptionTypes) {
        if (exceptionTypes.length == 0) {
            return EMPTY;
        }

        return THROWS + SPACE + Arrays.stream(exceptionTypes)
                .map(Class::getCanonicalName)
                .collect(Collectors.joining(COMMA + SPACE));
    }

    /**
     * Generates the default return value for {@link Class}.
     *
     * @param token class for getting default value.
     * @return default value value for class.
     * <ul>
     *     <li>{@code ""} for {@code void.class}</li>
     *     <li>{@code "false"} for {@code boolean.class}</li>
     *     <li>{@code "0"} for other primitive types</li>
     *     <li>{@code "null"} in other cases</li>
     * </ul>
     */
    private String generateDefaultValue(final Class<?> token) {
        if (token.equals(void.class)) {
            return EMPTY;
        } else if (token.equals(boolean.class)) {
            return FALSE;
        } else if (token.isPrimitive()) {
            return ZERO;
        } else {
            return NULL;
        }
    }

    /**
     * Returns class name with the added suffix {@value IMPL}.
     *
     * @param token the class for getting the name.
     * @return class name with {@value IMPL} suffix.
     */
    private String getImplName(final Class<?> token) {
        return token.getSimpleName() + IMPL;
    }

    /**
     * Works like {@link Files#createDirectories(Path, FileAttribute[])}
     *
     * @param file file for creating directories.
     * @throws ImplerException if it cannot create directories
     */
    private void createDirs(final Path file) throws ImplerException {
        // :NOTE: file.getParent лучше было бы вынести в переменную
        if (file.getParent() != null) {
            try {
                Files.createDirectories(file.getParent());
            } catch (IOException e) {
                throw new ImplerException("Cannot create file's directories", e);
            }
        }
    }

    /**
     * Writes unicode representation of given string.
     *
     * @param writer given {@link Writer}.
     * @param string given string.
     * @throws IOException If an I/O error occurs
     */
    private void write(final Writer writer, final String string) throws IOException {
        writer.write(string.chars()
                .mapToObj(c -> String.format("\\u%04x", c))
                .collect(Collectors.joining()));
    }

    /**
     * Entry point of {@link Implementor}. 1 (full name of the class/interface)
     * or 3 ("-jar", name of class/interface, file.jar) arguments are expected.
     *
     * @param args given arguments.
     */
    public static void main(String[] args) {
        if (args == null || args.length != 1 && args.length != 3) {
            System.err.println("Invalid number of arguments: expected 1 or 3");
            return;
        }

        for (String arg : args) {
            if (arg == null) {
                System.err.println("Invalid argument: null");
                return;
            }
        }

        Implementor implementor = new Implementor();
        try {
            if (args.length == 1) {
                implementor.implement(Class.forName(args[0]), Path.of("./"));
            } else {
                if (args[0].equals("-jar")) {
                    implementor.implementJar(Class.forName(args[1]), Path.of(args[2]));
                } else {
                    // непонятно, что именно некорректного
                    System.err.println("Incorrect arguments");
                }
            }
            // :NOTE: хотелось бы знать, какого именно класса нет
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found");
        } catch (ImplerException e) {
            System.err.println(e.getMessage());
        }
    }
}
