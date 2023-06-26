package info.kgeorgiy.ja.ibragimov.walk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RecursiveWalk {
    private static final String ERROR_SHA1 = "0".repeat(40);
    private static final char[] hexDigit =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static void main(String[] args) {
        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            System.err.println("Invalid input arguments.");
            return;
        }

        final Path inputFilePath;
        try {
            inputFilePath = Path.of(args[0]);
        } catch (InvalidPathException e) {
            System.err.println("Invalid path of input file");
            return;
        }

        final Path outputFilePath;
        try {
            outputFilePath = Path.of(args[1]);
        } catch (InvalidPathException e) {
            System.err.println("Invalid path of output file");
            return;
        }

        if (!Files.isRegularFile(inputFilePath)) {
            System.err.println("No input file");
            return;
        }

        try {
            if (outputFilePath.getParent() != null) {
                Files.createDirectories(outputFilePath.getParent());
            }
        } catch (IOException e) {
            System.err.println("Could not find or create output file with its directories");
            return;
        }

        try (final BufferedReader in = Files.newBufferedReader(inputFilePath, StandardCharsets.UTF_8)) {
            try (final BufferedWriter out = Files.newBufferedWriter(outputFilePath, StandardCharsets.UTF_8)) {
                solve(in, out);
            } catch (IOException e) {
                System.err.println("Could not open output file");
            }
        } catch (IOException e) {
            System.err.println("Could not open input file");
        }
    }

    private static void solve(final BufferedReader in, final BufferedWriter out) {
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error creating a hashing algorithm");
            return;
        }

        in.lines().forEach(line -> {
            try {
                final Path path = Path.of(line);
                walk(out, digest, path);
            } catch (InvalidPathException e) {
                writeSha1(out, ERROR_SHA1, line);
            }
        });
    }

    private static void walk(final BufferedWriter out, final MessageDigest digest, final Path path) {
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) {
                    final String sha1 = getSha1(file);
                    writeSha1(out, sha1, String.valueOf(file));

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(final Path file, final IOException e) {
                    writeSha1(out, ERROR_SHA1, String.valueOf(file));

                    return FileVisitResult.CONTINUE;
                }

                private String getSha1(final Path file) {
                    final int bufferSize = 4096;
                    final byte[] buffer = new byte[bufferSize];

                    try (final InputStream fileInputStream = Files.newInputStream(file)) {
                        int n = 0;

                        while (n != -1) {
                            n = fileInputStream.read(buffer);
                            if (n > 0) {
                                digest.update(buffer, 0, n);
                            }
                        }

                        return bytesToHex(digest.digest());
                    } catch (IOException e) {
                        return ERROR_SHA1;
                    }
                }
            });
        } catch (IOException e) {
            System.err.println("File crawl error");
        }
    }

    private static void writeSha1(final BufferedWriter out, final String sha1, final String file) {
        try {
            out.write(sha1 + " " + file + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("Error writing to file");
        }
    }

    private static String bytesToHex(final byte[] b) {
        final StringBuilder sb = new StringBuilder();

        for (byte value : b) {
            sb.append(hexDigit[(value >> 4) & 0x0f]);
            sb.append(hexDigit[value & 0x0f]);
        }

        return sb.toString();
    }
}
