package com.twisted.util;

import static com.google.common.io.ByteStreams.toByteArray;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 * A static-utility class containing containing extension or helper methods for
 * <b>co</b>mpressor-<b>dec</b>compressor<b>'s</b>.
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class CompressionUtil {

    /**
     * Uncompresses a {@code byte} array of g-zipped data.
     * @param data The compressed, g-zipped data.
     * @return The uncompressed data.
     * @throws IOException If some I/O exception occurs.
     */
    public static byte[] gunzip(byte[] data) throws IOException {
        return toByteArray(new GZIPInputStream(new ByteArrayInputStream(data)));
    }

    /**
     * Suppresses the default-public constructor preventing this class from
     * being instantiated by other classes.
     * @throws UnsupportedOperationException If this class is instantiated
     * within itself.
     */
    private CompressionUtil() {
        throw new UnsupportedOperationException("static-utility classes may not be instantiated.");
    }

}
