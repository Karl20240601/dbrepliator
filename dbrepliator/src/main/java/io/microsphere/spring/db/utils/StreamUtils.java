package io.microsphere.spring.db.utils;


import org.apache.commons.io.input.ReaderInputStream;
import org.springframework.util.Assert;

import java.io.*;

public class StreamUtils {

    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final int BUFFER_SIZE = 4096;
    public static final int INPUTSTREAM_MAX_BUFFER_SIZE = 16*1024;
    public static final int CHAR_MAX_BUFFER_SIZE = 4096;

    public static byte[] streamToBytes(InputStream in, int length) {
        in.mark(length);
        try {
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            copy(in, bytesOut, length);
            return bytesOut.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.reset();
            } catch (IOException e) {
            }
        }
    }

    public static byte[] streamToBytes(Reader reader, int length) {
        try {
            reader.mark(length);
            ReaderInputStream readerInputStream = new ReaderInputStream(reader, DEFAULT_CHARSET, length);
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            copy(readerInputStream, bytesOut, length);
            return bytesOut.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                reader.reset();
            } catch (IOException e) {
            }
        }
    }

    public static int copy(InputStream in, OutputStream out, int length) throws IOException {
        Assert.notNull(in, "No InputStream specified");
        Assert.notNull(out, "No OutputStream specified");

        int byteCount = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while (((bytesRead = in.read(buffer)) != -1) && bytesRead >= length) {
            out.write(buffer, 0, bytesRead);
            byteCount += bytesRead;
        }
        out.flush();
        return byteCount;
    }
}
