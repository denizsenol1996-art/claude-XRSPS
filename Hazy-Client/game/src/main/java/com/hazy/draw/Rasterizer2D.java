package com.hazy.draw;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.util.Hashtable;

import com.hazy.Client;
import com.hazy.cache.graphics.SimpleImage;
import com.hazy.collection.Cacheable;

public class Rasterizer2D extends Cacheable {


    /**
     * Draws a box filled with a certain colour.
     * @param leftX The left edge X-Coordinate of the box.
     * @param topY The top edge Y-Coordinate of the box.
     * @param width The width of the box.
     * @param height The height of the box.
     * @param rgbColour The RGBColour of the box.
     */
    public static void drawBox(int leftX, int topY, int width, int height, int rgbColour) {
        if (leftX < Rasterizer2D.leftX) {
            width -= Rasterizer2D.leftX - leftX;
            leftX = Rasterizer2D.leftX;
        }
        if (topY < Rasterizer2D.topY) {
            height -= Rasterizer2D.topY - topY;
            topY = Rasterizer2D.topY;
        }
        if (leftX + width > bottomX)
            width = bottomX - leftX;
        if (topY + height > bottomY)
            height = bottomY - topY;
        int leftOver = Rasterizer2D.width - width;
        int pixelIndex = leftX + topY * Rasterizer2D.width;
        for (int rowIndex = 0; rowIndex < height; rowIndex++) {
            for (int columnIndex = 0; columnIndex < width; columnIndex++)
                drawAlpha(pixels, pixelIndex++, rgbColour, 255);
            pixelIndex += leftOver;
        }
    }
    public static void drawAlpha(int[] pixels, int index, int value) {
        drawAlpha(pixels,index,value,255);
    }

    public static void drawAlpha(int[] pixels, int index, int value, int alpha) {
        if (!Client.instance.isGpu() || pixels != Client.instance.getBufferProvider().getPixels())
        {
            pixels[index] = value;
            return;
        }

        // (int) x * 0x8081 >>> 23 is equivalent to (short) x / 255
        int outAlpha = alpha + ((pixels[index] >>> 24) * (255 - alpha) * 0x8081 >>> 23);
        pixels[index] = value & 0x00FFFFFF | outAlpha << 24;
    }

    /**
     * Sets the drawingArea based on the coordinates of the edges.
     * @param bottomY The bottom edge Y-Coordinate.
     * @param leftX The left edge X-Coordinate.
     * @param rightX The right edge X-Coordinate.
     * @param topY The top edge Y-Coordinate.
     */
    public static void setDrawingArea(int bottomY, int leftX, int rightX, int topY) {
        if(leftX < 0) {
            leftX = 0;
        }
        if(topY < 0) {
            topY = 0;
        }
        if(rightX > width) {
            rightX = width;
        }
        if(bottomY > height) {
            bottomY = height;
        }
        Rasterizer2D.leftX = leftX;
        Rasterizer2D.topY = topY;
        bottomX = rightX;
        Rasterizer2D.bottomY = bottomY;
        lastX = bottomX;
        viewportCenterX = bottomX / 2;
        viewportCenterY = Rasterizer2D.bottomY / 2;
    }

    /**
     * Sets the Rasterizer2D in the upper left corner with height, width and pixels set.
     * @param height The height of the drawingArea.
     * @param width The width of the drawingArea.
     * @param pixels The array of pixels (RGBColours) in the drawingArea.
     */
    public static void initDrawingArea(int height, int width, int pixels[], float[] depth) {
        depth_buffer = depth;
        Rasterizer2D.pixels = pixels;
        Rasterizer2D.width = width;
        Rasterizer2D.height = height;
        setDrawingArea(height, 0, width, 0);
    }


    public static void init(int width, int height, int[] pixels, float[] depth) {
        initDrawingArea(height,width,pixels,depth);
    }
    
    public static void draw_arc(int x, int y, int width, int height, int stroke, int start, int sweep, int color, int alpha, int closure, boolean fill) {
        Graphics2D graphics = SimpleImage.createGraphics(Rasterizer2D.pixels, Rasterizer2D.width, Rasterizer2D.height);
        graphics.setColor(new Color((color >> 16 & 0xff), (color >> 8 & 0xff), (color & 0xff), ((alpha >= 256 || alpha < 0) ? 255 : alpha)));

        RenderingHints render = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        render.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        graphics.setRenderingHints(render);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        if (!fill) {
            graphics.setStroke(new BasicStroke((stroke < 1 ? 1 : stroke)));
        }
        // Closure types - OPEN(0), CHORD(1), PIE(2)
        Arc2D.Double arc = new Arc2D.Double(x + stroke, y + stroke, width, height, start, sweep, closure);
        if (fill) {
            graphics.fill(arc);
        } else {
            graphics.draw(arc);
        }
    }

    public static Graphics2D createGraphics(boolean renderingHints) {
        Graphics2D g2d = createGraphics(pixels, width, height);
        if (renderingHints) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        return g2d;
    }

    private static final ColorModel COLOR_MODEL = new DirectColorModel(32, 0xff0000, 0xff00, 0xff);

    public static Graphics2D createGraphics(int[] pixels, int width, int height) {
        return new BufferedImage(COLOR_MODEL, Raster.createWritableRaster(COLOR_MODEL.createCompatibleSampleModel(width, height), new DataBufferInt(pixels, width * height), null), false, new Hashtable<Object, Object>()).createGraphics();
    }

    public static Shape createSector(int x, int y, int r, int angle) {
        return new Arc2D.Double(x, y, r, r, 90, -angle, Arc2D.PIE);
    }

    public static Shape createCircle(int x, int y, int r) {
        return new Ellipse2D.Double(x, y, r, r);
    }


    /**
     * Draws a transparent box with a gradient that changes from top to bottom.
     * @param leftX The left edge X-Coordinate of the box.
     * @param topY The top edge Y-Coordinate of the box.
     * @param width The width of the box.
     * @param height The height of the box.
     * @param topColour The top rgbColour of the gradient.
     * @param bottomColour The bottom rgbColour of the gradient.
     * @param opacity The opacity value ranging from 0 to 256.
     */
    public static void drawTransparentGradientBox(int leftX, int topY, int width, int height, int topColour, int bottomColour, int opacity) {
        int gradientProgress = 0;
        int progressPerPixel = 0x10000 / height;
        if(leftX < Rasterizer2D.leftX) {
            width -= Rasterizer2D.leftX - leftX;
            leftX = Rasterizer2D.leftX;
        }
        if(topY < Rasterizer2D.topY) {
            gradientProgress += (Rasterizer2D.topY - topY) * progressPerPixel;
            height -= Rasterizer2D.topY - topY;
            topY = Rasterizer2D.topY;
        }
        if(leftX + width > bottomX)
            width = bottomX - leftX;
        if(topY + height > bottomY)
            height = bottomY - topY;
        int leftOver = Rasterizer2D.width - width;
        int transparency = 256 - opacity;
        int pixelIndex = leftX + topY * Rasterizer2D.width;
        for(int rowIndex = 0; rowIndex < height; rowIndex++) {
            int gradient = 0x10000 - gradientProgress >> 8;
            int inverseGradient = gradientProgress >> 8;
            int gradientColour = ((topColour & 0xff00ff) * gradient + (bottomColour & 0xff00ff) * inverseGradient & 0xff00ff00) + ((topColour & 0xff00) * gradient + (bottomColour & 0xff00) * inverseGradient & 0xff0000) >>> 8;
            int transparentPixel = ((gradientColour & 0xff00ff) * opacity >> 8 & 0xff00ff) + ((gradientColour & 0xff00) * opacity >> 8 & 0xff00);
            for(int columnIndex = 0; columnIndex < width; columnIndex++) {
                int backgroundPixel = pixels[pixelIndex];
                backgroundPixel = ((backgroundPixel & 0xff00ff) * transparency >> 8 & 0xff00ff) + ((backgroundPixel & 0xff00) * transparency >> 8 & 0xff00);
                drawAlpha(pixels, pixelIndex++, transparentPixel + backgroundPixel, opacity);
            }
            pixelIndex += leftOver;
            gradientProgress += progressPerPixel;
        }
    }


    public static void set_clip(int x, int y, int width, int height) {
        if(x < 0) {
            x = 0;
        }
        if(y < 0) {
            y = 0;
        }
        if (width > Rasterizer2D.width) {
            width = Rasterizer2D.width;
        }
        if (height > Rasterizer2D.height) {
            height = Rasterizer2D.height;
        }
        leftX = x;
        topY = y;
        bottomX = width;
        bottomY = height;
        lastX = bottomX;
        viewportCenterX = bottomX / 2;
        viewportCenterY = bottomY / 2;
    }

    /**
     * Clears the drawingArea by setting every pixel to 0 (black).
     */
    public static void clear()    {
        int size = width * height;
        for(int coordinates = 0; coordinates < size; coordinates++) {
            pixels[coordinates] = 0;
            depth_buffer[coordinates] = Float.MAX_VALUE;
        }
    }

    public static void draw_filled_rect(int x, int y, int width, int height, int color) {
        drawTransparentBox(x, y, width, height, color, 255);
    }

    /**
     * Draws a 1 pixel thick box outline in a certain colour.
     * @param leftX The left edge X-Coordinate.
     * @param topY The top edge Y-Coordinate.
     * @param width The width.
     * @param height The height.
     * @param rgbColour The RGB-Colour.
     */
    public static void drawBoxOutline(int leftX, int topY, int width, int height, int rgbColour){
        drawHorizontalLine(leftX, topY, width, rgbColour);
        drawHorizontalLine(leftX, (topY + height) - 1, width, rgbColour);
        drawVerticalLine(leftX, topY, height, rgbColour);
        drawVerticalLine((leftX + width) - 1, topY, height, rgbColour);
    }


    /**
     * Draws a transparent box.
     * @param leftX The left edge X-Coordinate of the box.
     * @param topY The top edge Y-Coordinate of the box.
     * @param width The box width.
     * @param height The box height.
     * @param rgbColour The box colour.
     * @param opacity The opacity value ranging from 0 to 256.
     */
    public static void drawTransparentBox(int leftX, int topY, int width, int height, int rgbColour, int opacity){
        if(leftX < Rasterizer2D.leftX){
            width -= Rasterizer2D.leftX - leftX;
            leftX = Rasterizer2D.leftX;
        }
        if(topY < Rasterizer2D.topY){
            height -= Rasterizer2D.topY - topY;
            topY = Rasterizer2D.topY;
        }
        if(leftX + width > bottomX)
            width = bottomX - leftX;
        if(topY + height > bottomY)
            height = bottomY - topY;
        int transparency = 256 - opacity;
        int red = (rgbColour >> 16 & 0xff) * opacity;
        int green = (rgbColour >> 8 & 0xff) * opacity;
        int blue = (rgbColour & 0xff) * opacity;
        int leftOver = Rasterizer2D.width - width;
        int pixelIndex = leftX + topY * Rasterizer2D.width;
        for(int rowIndex = 0; rowIndex < height; rowIndex++){
            for(int columnIndex = 0; columnIndex < width; columnIndex++){
                int otherRed = (pixels[pixelIndex] >> 16 & 0xff) * transparency;
                int otherGreen = (pixels[pixelIndex] >> 8 & 0xff) * transparency;
                int otherBlue = (pixels[pixelIndex] & 0xff) * transparency;
                int transparentColour = ((red + otherRed >> 8) << 16) + ((green + otherGreen >> 8) << 8) + (blue + otherBlue >> 8);
                drawAlpha(pixels, pixelIndex++, transparentColour, opacity);
            }
            pixelIndex += leftOver;
        }
    }

    public static void drawPixels(int height, int posY, int posX, int color, int width) {
        if (posX < leftX) {
            width -= leftX - posX;
            posX = leftX;
        }
        if (posY < topY) {
            height -= topY - posY;
            posY = topY;
        }
        if (posX + width > bottomX) {
            width = bottomX - posX;
        }
        if (posY + height > bottomY) {
            height = bottomY - posY;
        }
        int k1 = Rasterizer2D.width - width;
        int l1 = posX + posY * Rasterizer2D.width;
        for (int i2 = -height; i2 < 0; i2++) {
            for (int j2 = -width; j2 < 0; j2++) {
                drawAlpha(pixels,l1++,color);
            }
            l1 += k1;
        }
    }

    /**
     * Draws a 1 pixel thick box outline in a certain colour.
     * @param x The left edge X-Coordinate.
     * @param y The top edge Y-Coordinate.
     * @param width The width.
     * @param height The height.
     * @param rgbColour The RGB-Colour.
     */
    public static void draw_rect_outline(int x, int y, int width, int height, int rgbColour) {
        drawHorizontalLine(x, y, width, rgbColour);
        drawHorizontalLine(x, (y + height) - 1, width, rgbColour);
        drawVerticalLine(x, y, height, rgbColour);
        drawVerticalLine((x + width) - 1, y, height, rgbColour);
    }

    /**
     * Draws a coloured horizontal line in the drawingArea.
     * @param xPosition The start X-Position of the line.
     * @param yPosition The Y-Position of the line.
     * @param width The width of the line.
     * @param rgbColour The colour of the line.
     */
    public static void drawHorizontalLine(int xPosition, int yPosition, int width, int rgbColour){
        if(yPosition < topY || yPosition >= bottomY)
            return;
        if(xPosition < leftX){
            width -= leftX - xPosition;
            xPosition = leftX;
        }
        if(xPosition + width > bottomX)
            width = bottomX - xPosition;
        int pixelIndex = xPosition + yPosition * Rasterizer2D.width;
        for(int i = 0; i < width; i++)
            drawAlpha(pixels, pixelIndex + i, rgbColour, 255);
    }

    public static void drawStroke(int xPos, int yPos, int width, int height, int color, int strokeWidth) {

        drawVerticalStrokeLine(xPos, yPos, height, color, strokeWidth);
        drawVerticalStrokeLine((xPos + width) - strokeWidth, yPos, height, color, strokeWidth);
        drawHorizontalStrokeLine(xPos, yPos, width, color, strokeWidth);
        drawHorizontalStrokeLine(xPos, (yPos + height) - strokeWidth, width, color, strokeWidth);

    }

    public static void drawHorizontalStrokeLine(int xPos, int yPos, int w, int hexColor, int strokeWidth) {

        if (yPos < topY || yPos >= bottomY)
            return;
        if (xPos < leftX) {
            w -= leftX - xPos;
            xPos = leftX;
        }
        if (xPos + w > bottomX)
            w = bottomX - xPos;
        int index = xPos + yPos * width;
        int leftWidth = width - w;
        for (int x = 0; x < strokeWidth; x++) {
            for (int y = 0; y < w; y++) {
                drawAlpha(pixels,index++,hexColor);
            }
            index += leftWidth;
        }

    }

    public static void drawVerticalStrokeLine(int xPosition, int yPosition, int height, int hexColor, int strokeWidth) {
        if (xPosition < leftX || xPosition >= bottomX)
            return;
        if (yPosition < topY) {
            height -= topY - yPosition;
            yPosition = topY;
        }
        if (yPosition + height > bottomY)
            height = bottomY - yPosition;
        int pixelIndex = xPosition + yPosition * width;
        for (int rowIndex = 0; rowIndex < height; rowIndex++) {
            for (int x = 0; x < strokeWidth; x++) {
                drawAlpha(pixels,pixelIndex + x + rowIndex * width,hexColor);
            }
        }
    }

    /**
     * Draws a coloured vertical line in the drawingArea.
     * @param xPosition The X-Position of the line.
     * @param yPosition The start Y-Position of the line.
     * @param height The height of the line.
     * @param rgbColour The colour of the line.
     */
    public static void drawVerticalLine(int xPosition, int yPosition, int height, int rgbColour){
        if(xPosition < leftX || xPosition >= bottomX)
            return;
        if(yPosition < topY){
            height -= topY - yPosition;
            yPosition = topY;
        }
        if(yPosition + height > bottomY)
            height = bottomY - yPosition;
        int pixelIndex = xPosition + yPosition * width;
        for(int rowIndex = 0; rowIndex < height; rowIndex++)
            drawAlpha(pixels, pixelIndex + rowIndex * width, rgbColour, 255);
    }

    public static void drawHorizontalLine(int x, int y, int length, int color, int alpha) {
        if (y < topY || y >= bottomY) {
            return;
        }
        if (x < leftX) {
            length -= leftX - x;
            x = leftX;
        }
        if (x + length > bottomX) {
            length = bottomX - x;
        }
        final int j1 = 256 - alpha;
        final int k1 = (color >> 16 & 0xff) * alpha;
        final int l1 = (color >> 8 & 0xff) * alpha;
        final int i2 = (color & 0xff) * alpha;
        int i3 = x + y * width;
        for (int j3 = 0; j3 < length; j3++) {
            final int j2 = (pixels[i3] >> 16 & 0xff) * j1;
            final int k2 = (pixels[i3] >> 8 & 0xff) * j1;
            final int l2 = (pixels[i3] & 0xff) * j1;
            final int k3 = (k1 + j2 >> 8 << 16) + (l1 + k2 >> 8 << 8) + (i2 + l2 >> 8);
            drawAlpha(pixels, i3++, k3, alpha);
        }
    }

    public static void draw_line(int i, int j, int k, int l)
    {
        if (i < topY || i >= bottomY)
            return;
        if (l < leftX)
        {
            k -= leftX - l;
            l = leftX;
        }
        if (l + k > bottomX)
            k = bottomX - l;
        int i1 = l + i * width;
        for (int j1 = 0; j1 < k; j1++) {
            drawAlpha(pixels, i1 + j1, j, 255);
        }

    }

    public static void drawAlphaBox(int x, int y, int lineWidth, int lineHeight, int color, int alpha) {
        drawTransparentBox(x,y,lineWidth,lineHeight,color,alpha);
    }

    /**
     * Draws a 1 pixel thick transparent box outline in a certain colour.
     * @param leftX The left edge X-Coordinate
     * @param topY The top edge Y-Coordinate.
     * @param width The width.
     * @param height The height.
     * @param rgbColour The RGB-Colour.
     * @param opacity The opacity value ranging from 0 to 256.
     */
    public static void drawTransparentBoxOutline(int leftX, int topY, int width, int height, int rgbColour, int opacity) {
        drawTransparentHorizontalLine(leftX, topY, width, rgbColour, opacity);
        drawTransparentHorizontalLine(leftX, topY + height - 1, width, rgbColour, opacity);
        if (height >= 3) {
            drawTransparentVerticalLine(leftX, topY + 1, height - 2, rgbColour, opacity);
            drawTransparentVerticalLine(leftX + width - 1, topY + 1, height - 2, rgbColour, opacity);
        }
    }

    /**
     * Draws a transparent coloured horizontal line in the drawingArea.
     * @param xPosition The start X-Position of the line.
     * @param yPosition The Y-Position of the line.
     * @param width The width of the line.
     * @param rgbColour The colour of the line.
     * @param opacity The opacity value ranging from 0 to 256.
     */
    public static void drawTransparentHorizontalLine(int xPosition, int yPosition, int width, int rgbColour, int opacity) {
        if(yPosition < topY || yPosition >= bottomY) {
            return;
        }
        if(xPosition < leftX) {
            width -= leftX - xPosition;
            xPosition = leftX;
        }
        if(xPosition + width > bottomX) {
            width = bottomX - xPosition;
        }
        final int transparency = 256 - opacity;
        final int red = (rgbColour >> 16 & 0xff) * opacity;
        final int green = (rgbColour >> 8 & 0xff) * opacity;
        final int blue = (rgbColour & 0xff) * opacity;
        int pixelIndex = xPosition + yPosition * Rasterizer2D.width;
        for(int i = 0; i < width; i++) {
            final int otherRed = (pixels[pixelIndex] >> 16 & 0xff) * transparency;
            final int otherGreen = (pixels[pixelIndex] >> 8 & 0xff) * transparency;
            final int otherBlue = (pixels[pixelIndex] & 0xff) * transparency;
            final int transparentColour = (red + otherRed >> 8 << 16) + (green + otherGreen >> 8 << 8) + (blue + otherBlue >> 8);
            drawAlpha(pixels, pixelIndex, transparentColour, opacity);
        }
    }

    /**
     * Draws a transparent coloured vertical line in the drawingArea.
     * @param xPosition The X-Position of the line.
     * @param yPosition The start Y-Position of the line.
     * @param height The height of the line.
     * @param rgbColour The colour of the line.
     * @param opacity The opacity value ranging from 0 to 256.
     */
    public static void drawTransparentVerticalLine(int xPosition, int yPosition, int height, int rgbColour, int opacity) {
        if(xPosition < leftX || xPosition >= bottomX) {
            return;
        }
        if(yPosition < topY) {
            height -= topY - yPosition;
            yPosition = topY;
        }
        if(yPosition + height > bottomY) {
            height = bottomY - yPosition;
        }
        final int transparency = 256 - opacity;
        final int red = (rgbColour >> 16 & 0xff) * opacity;
        final int green = (rgbColour >> 8 & 0xff) * opacity;
        final int blue = (rgbColour & 0xff) * opacity;
        int pixelIndex = xPosition + yPosition * width;
        for(int i = 0; i < height; i++) {
            final int otherRed = (pixels[pixelIndex] >> 16 & 0xff) * transparency;
            final int otherGreen = (pixels[pixelIndex] >> 8 & 0xff) * transparency;
            final int otherBlue = (pixels[pixelIndex] & 0xff) * transparency;
            final int transparentColour = (red + otherRed >> 8 << 16) + (green + otherGreen >> 8 << 8) + (blue + otherBlue >> 8);
            drawAlpha(pixels, pixelIndex, transparentColour, opacity);
            pixelIndex += width;
        }
    }

    public static void drawRectangle(int x, int y, int width, int height, int color, int alpha) {
        drawHorizontalLine(x, y, width, color, alpha);
        drawHorizontalLine(x, y + height - 1, width, color, alpha);
        if(height >= 3) {
            drawVerticalLine(x, y + 1, height - 2, color, alpha);
            drawVerticalLine(x + width - 1, y + 1, height - 2, color, alpha);
        }
    }

    public static void drawVerticalLine(int x, int y, int length, int color, int alpha) {
        if(x < leftX || x >= bottomX) {
            return;
        }
        if(y < topY) {
            length -= topY - y;
            y = topY;
        }
        if(y + length > bottomY) {
            length = bottomY - y;
        }
        final int j1 = 256 - alpha;
        final int k1 = (color >> 16 & 0xff) * alpha;
        final int l1 = (color >> 8 & 0xff) * alpha;
        final int i2 = (color & 0xff) * alpha;
        int i3 = x + y * width;
        for(int j3 = 0; j3 < length; j3++) {
            final int j2 = (pixels[i3] >> 16 & 0xff) * j1;
            final int k2 = (pixels[i3] >> 8 & 0xff) * j1;
            final int l2 = (pixels[i3] & 0xff) * j1;
            final int k3 = (k1 + j2 >> 8 << 16) + (l1 + k2 >> 8 << 8) + (i2 + l2 >> 8);
            drawAlpha(pixels, i3, k3, alpha);
            i3 += width;
        }
    }

    public static void fillRectangle(int x, int y, int w, int h, int color) {
        if (x < leftX) {
            w -= leftX - x;
            x = leftX;
        }
        if (y < topY) {
            h -= topY - y;
            y = topY;
        }
        if (x + w > bottomX) {
            w = bottomX - x;
        }
        if (y + h > bottomY) {
            h = bottomY - y;
        }
        int k1 = width - w;
        int l1 = x + y * width;
        for (int i2 = -h; i2 < 0; i2++) {
            for (int j2 = -w; j2 < 0; j2++) {
                drawAlpha(pixels, l1++, color);
            }
            l1 += k1;
        }
    }

    public static void fillRectangle(int x, int y, int w, int h, int color, int alpha) {
        if (x < leftX) {
            w -= leftX - x;
            x = leftX;
        }
        if (y < topY) {
            h -= topY - y;
            y = topY;
        }
        if (x + w > bottomX) {
            w = bottomX - x;
        }
        if (y + h > bottomY) {
            h = bottomY - y;
        }
        int a2 = 256 - alpha;
        int r1 = (color >> 16 & 0xff) * alpha;
        int g1 = (color >> 8 & 0xff) * alpha;
        int b1 = (color & 0xff) * alpha;
        int k3 = width - w;
        int pixel = x + y * width;
        for (int i4 = 0; i4 < h; i4++) {
            for (int j4 = -w; j4 < 0; j4++) {
                int r2 = (pixels[pixel] >> 16 & 0xff) * a2;
                int g2 = (pixels[pixel] >> 8 & 0xff) * a2;
                int b2 = (pixels[pixel] & 0xff) * a2;
                int rgb = ((r1 + r2 >> 8) << 16) + ((g1 + g2 >> 8) << 8) + (b1 + b2 >> 8);
                drawAlpha(pixels, pixel++, rgb,alpha);
            }
            pixel += k3;
        }
    }

    public static void draw_rectangle_outline(int x, int y, int line_width, int line_height, int color) {
        draw_rect_outline(x,y,line_width,line_height,color);
    }

    public static void draw_vertical_line1(int x, int y, int line_width, int color) {
        drawVerticalLine(x,y,line_width,color);
    }

    public static void draw_horizontal_line1(int x, int y, int line_height, int color) {
        drawHorizontalLine(x,y,line_height,color);
    }

    public static int[] pixels;
    public static int width;
    public static int height;
    public static int topY;
    public static int bottomY;
    public static int leftX;
    public static int bottomX;
    public static int lastX;
    public static int viewportCenterX;
    public static int viewportCenterY;
    public static float[] depth_buffer;

    static {
        topY = 0;
        bottomY = 0;
        leftX = 0;
        bottomX = 0;
    }

}
