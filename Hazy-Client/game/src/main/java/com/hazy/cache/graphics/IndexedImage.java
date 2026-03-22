package com.hazy.cache.graphics;
import com.hazy.cache.Archive;
import com.hazy.cache.def.SpriteDefinition;
import com.hazy.draw.Rasterizer2D;
import com.hazy.io.Buffer;

public final class IndexedImage extends Rasterizer2D {

    public byte[] palettePixels;
    public final int[] palette;
    public int width;
    public int height;
    public int drawOffsetX;
    public int drawOffsetY;
    public int subWidth;
    public int subHeight;
    public int[] pixels;

    public void setTransparency(int transRed, int transGreen, int transBlue) {
        for (int index = 0; index < palette.length; index++)
            if (((palette[index] >> 16) & 255) == transRed && ((palette[index] >> 8) & 255) == transGreen && (palette[index] & 255) == transBlue)
                palette[index] = 0;
    }

    public void normalize() {
        if (subWidth != width || subHeight != height) { // L: 18
            byte[] pixels = new byte[width * height]; // L: 19
            int var2 = 0; // L: 20

            for (int var3 = 0; var3 < subHeight; ++var3) { // L: 21
                for (int var4 = 0; var4 < subWidth; ++var4) { // L: 22
                    pixels[var4 + (var3 + drawOffsetY) * width + drawOffsetX] = palettePixels[var2++]; // L: 23
                }
            }

            this.palettePixels = pixels; // L: 26
            subWidth = width; // L: 27
            subHeight = height; // L: 28
            drawOffsetX = 0; // L: 29
            drawOffsetY = 0; // L: 30
        }
    }

    public IndexedImage(SpriteDefinition definition) {
        palettePixels = definition.getPixelIdx();
        palette = definition.getPalette();
        width = definition.getWidth();
        height = definition.getHeight();
        drawOffsetX = definition.getOffsetX();
        drawOffsetY = definition.getOffsetY();
        subWidth = definition.getMaxWidth();
        subHeight = definition.getMaxHeight();
        pixels = definition.getPixels();
    }

    // ah the index
    public IndexedImage(Archive archive, String s, int i) {
        Buffer image = new Buffer(archive.get(s + ".dat"));
        Buffer meta = new Buffer(archive.get("index.dat"));
        meta.pos = image.readUnsignedShort();
        subWidth = meta.readUnsignedShort();
        subHeight = meta.readUnsignedShort();

        int colorLength = meta.readUnsignedByte();
        palette = new int[colorLength];

        for (int index = 0; index < colorLength - 1; index++) {
            palette[index + 1] = meta.readTriByte();
        }

        for (int l = 0; l < i; l++) {
            meta.pos += 2;
            image.pos += meta.readUnsignedShort() * meta.readUnsignedShort();
            meta.pos++;
        }
        drawOffsetX = meta.readUnsignedByte();
        drawOffsetY = meta.readUnsignedByte();
        width = meta.readUnsignedShort();
        height = meta.readUnsignedShort();
        int type = meta.readUnsignedByte();
        int pixels = width * height;
        palettePixels = new byte[pixels];

        if (type == 0) {
            for (int index = 0; index < pixels; index++) {
                palettePixels[index] = image.readSignedByte();
            }
        } else if (type == 1) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    palettePixels[x + y * width] = image.readSignedByte();
                }
            }
        }
    }

    public void scale_half() {
        subWidth /= 2;
        subHeight /= 2;
        byte[] raster = new byte[subWidth * subHeight];
        int sourceIndex = 0;
        for (int y  = 0; y  < height; y ++) {
            for (int x = 0; x < width; x++) {
                raster[(x + drawOffsetX >> 1) + (y  + drawOffsetY >> 1) * subWidth] = raster[sourceIndex++];
            }
        }
        this.palettePixels = raster;
        width = subWidth;
        height = subHeight;
        drawOffsetX = 0;
        drawOffsetY = 0;
    }

    public void scale_full() {
        if (width == subWidth && height == subHeight) {
            return;
        }

        byte[] raster = new byte[subWidth * subHeight];

        int i = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                raster[x + drawOffsetX + (y + drawOffsetY) * subWidth] = raster[i++];
            }
        }
        this.palettePixels = raster;
        width = subWidth;
        height = subHeight;
        drawOffsetX = 0;
        drawOffsetY = 0;
    }

    public void flipHorizontally() {
        byte[] raster = new byte[width * height];
        int pixel = 0;
        for (int y = 0; y < height; y++) {
            for (int x = width - 1; x >= 0; x--) {
                raster[pixel++] = raster[x + y * width];
            }
        }
        this.palettePixels = raster;
        drawOffsetX = subWidth - width - drawOffsetX;
    }

    public void flipVertically() {
        byte[] raster = new byte[width * height];
        int pixel = 0;
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                raster[pixel++] = raster[x + y * width];
            }
        }
        this.palettePixels = raster;
        drawOffsetY = subHeight - height - drawOffsetY;
    }

    public void offsetColor(int redOffset, int greenOffset, int blueOffset) {
        for (int index = 0; index < palette.length; index++) {
            int red = palette[index] >> 16 & 0xff;
            red += redOffset;

            if (red < 0) {
                red = 0;
            } else if (red > 255) {
                red = 255;
            }

            int green = palette[index] >> 8 & 0xff;

            green += greenOffset;
            if (green < 0) {
                green = 0;
            } else if (green > 255) {
                green = 255;
            }

            int blue = palette[index] & 0xff;

            blue += blueOffset;
            if (blue < 0) {
                blue = 0;
            } else if (blue > 255) {
                blue = 255;
            }
            palette[index] = (red << 16) + (green << 8) + blue;
        }
    }

    public void draw(int x, int y) {
        x += drawOffsetX;
        y += drawOffsetY;
        int destOffset = x + y * Rasterizer2D.width;
        int sourceOffset = 0;
        int height = this.height;
        int width = this.width;
        int destStep = Rasterizer2D.width - width;
        int sourceStep = 0;

        if (y < Rasterizer2D.topY) {
            int dy = Rasterizer2D.topY - y;
            height -= dy;
            y = Rasterizer2D.topY;
            sourceOffset += dy * width;
            destOffset += dy * Rasterizer2D.width;
        }

        if (y + height > Rasterizer2D.bottomY) {
            height -= (y + height) - Rasterizer2D.bottomY;
        }

        if (x < Rasterizer2D.leftX) {
            int k2 = Rasterizer2D.leftX - x;
            width -= k2;
            x = Rasterizer2D.leftX;
            sourceOffset += k2;
            destOffset += k2;
            sourceStep += k2;
            destStep += k2;
        }

        if (x + width > Rasterizer2D.bottomX) {
            int dx = (x + width) - Rasterizer2D.bottomX;
            width -= dx;
            sourceStep += dx;
            destStep += dx;
        }

        if (!(width <= 0 || height <= 0)) {
            draw(height, Rasterizer2D.pixels, palettePixels, destStep, destOffset, width, sourceOffset, palette, sourceStep);
        }

    }

    private void draw(int i, int raster[], byte image[], int destStep, int destIndex, int width,  int sourceIndex, int ai1[], int sourceStep) {
        int minX = -(width >> 2);
        width = -(width & 3);
        for (int y = -i; y < 0; y++) {
            for (int x = minX; x < 0; x++) {

                byte pixel = image[sourceIndex++];

                if (pixel != 0) {
                    raster[destIndex++] = ai1[pixel & 0xff];
                } else {
                    destIndex++;
                }
                pixel = image[sourceIndex++];
                if (pixel != 0) {
                    raster[destIndex++] = ai1[pixel & 0xff];
                } else {
                    destIndex++;
                }
                pixel = image[sourceIndex++];
                if (pixel != 0) {
                    raster[destIndex++] = ai1[pixel & 0xff];
                } else {
                    destIndex++;
                }
                pixel = image[sourceIndex++];
                if (pixel != 0) {
                    raster[destIndex++] = ai1[pixel & 0xff];
                } else {
                    destIndex++;
                }
            }
            for (int x = width; x < 0; x++) {
                byte pixel = image[sourceIndex++];
                if (pixel != 0) {
                    raster[destIndex++] = ai1[pixel & 0xff];
                } else {
                    destIndex++;
                }
            }
            destIndex += destStep;
            sourceIndex += sourceStep;
        }
    }
}
