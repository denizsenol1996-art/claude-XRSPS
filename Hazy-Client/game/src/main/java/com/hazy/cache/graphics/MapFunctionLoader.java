package com.hazy.cache.graphics;
import com.hazy.cache.Archive;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import static java.nio.file.StandardOpenOption.READ;
public class MapFunctionLoader  implements Closeable {

    public static SimpleImage[] cache;

    private ByteBuffer dataBuffer;
    private ByteBuffer metaBuffer;

    public void init(Archive archive, String dataFile, String metaFile) throws IOException {

        byte[] dataBytes = archive.get(dataFile);
        byte[] metaBytes = archive.get(metaFile);

        metaBuffer = ByteBuffer.wrap(metaBytes);
        dataBuffer = ByteBuffer.wrap(dataBytes);

        final int spriteCount = metaBytes.length / 10;

        cache = new SimpleImage[spriteCount];
    }

    public SimpleImage get(int id) {
        try {
            if (contains(id)) {
                return cache[id];
            }

            if (dataBuffer == null || metaBuffer == null) {
                System.err.println("Sprite buffers are not initialized!");
                return null;
            }

            final int entries = metaBuffer.capacity() / 10;

            if (id > entries) {
               // System.err.printf("id=%d > size=%d%n", id, entries);
                return null;
            }

            metaBuffer.position(id * 10);
            final ByteBuffer metaBuf = metaBuffer.slice();
            metaBuf.limit(10);

            final int pos = ((metaBuf.get() & 0xFF) << 16) | ((metaBuf.get() & 0xFF) << 8) | (metaBuf.get() & 0xFF);
            final int len = ((metaBuf.get() & 0xFF) << 16) | ((metaBuf.get() & 0xFF) << 8) | (metaBuf.get() & 0xFF);
            final int offsetX = metaBuf.getShort() & 0xFFFF;
            final int offsetY = metaBuf.getShort() & 0xFFFF;

            dataBuffer.position(pos);
            final ByteBuffer dataBuf = dataBuffer.slice();
            dataBuf.limit(len);

            try (InputStream is = new ByteArrayInputStream(dataBuf.array())) {
                BufferedImage bimage = ImageIO.read(is);

                if (bimage == null) {
                    System.err.printf("Could not read image at %d%n", id);
                    return null;
                }

                if (bimage.getType() != BufferedImage.TYPE_INT_ARGB) {
                    bimage = convert(bimage);
                }

                final int[] pixels = ((DataBufferInt) bimage.getRaster().getDataBuffer()).getData();

                final SimpleImage sprite = new SimpleImage(bimage.getWidth(), bimage.getHeight(), offsetX, offsetY, pixels);
                cache[id] = sprite;

                return sprite;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.err.printf("No sprite found for id=%d%n", id);
        return null;
    }

    public void draw(int id, int x, int y) {
        draw(id, x, y, false);
    }

    public void draw(int id, int x, int y, int alpha, boolean advanced) {
        SimpleImage sprite = get(id);
        if (sprite != null) {
            if (advanced) {
                sprite.drawAdvancedSprite(x, y, alpha);
            } else {
                sprite.drawSprite(x, y, alpha);
            }
        }
    }

    public void draw(int id, int x, int y, boolean advanced) {
        SimpleImage sprite = get(id);
        if (sprite != null) {
            if (advanced) {
                sprite.drawAdvancedSprite(x, y);
            } else {
                sprite.drawSprite(x, y);
            }
        }
    }

    public boolean contains(int id) {
        return id < cache.length && cache[id] != null;
    }

    public void set(int id, SimpleImage sprite) {
        if (!contains(id)) {
            return;
        }

        cache[id] = sprite;
    }

    public void clear() {
        Arrays.fill(cache, null);
    }

    private static BufferedImage convert(BufferedImage bimage) {
        BufferedImage converted = new BufferedImage(bimage.getWidth(), bimage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        converted.getGraphics().drawImage(bimage, 0, 0, null);
        return converted;
    }

    public void close() throws IOException {

    }

}

