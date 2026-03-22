package com.hazy.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;

import com.hazy.Client;
import com.hazy.cache.FileStore.Store;
import com.hazy.cache.def.ItemDefinition;
import com.hazy.cache.factory.ItemSpriteFactory;
import com.hazy.cache.graphics.SimpleImage;
import com.hazy.sign.SignLink;

import javax.imageio.ImageIO;

public final class CacheUtils {

    public static void repackCacheIndex(Client client, Store cacheIndex) {
        System.out.println("Started repacking index " + cacheIndex.getIndex() + ".");
        int indexLength = new File(SignLink.indexLocation(cacheIndex.getIndex(), -1)).listFiles().length;
        File[] file = new File(SignLink.indexLocation(cacheIndex.getIndex(), -1)).listFiles();
        try {
            for (int index = 0; index < indexLength; index++) {
                int fileIndex = Integer.parseInt(
                    getFileNameWithoutExtension(file[index].toString()));
                byte[] data = FileUtils.fileToByteArray(cacheIndex.getIndex(), fileIndex);
                if (data != null && data.length > 0) {
                    client.indices[cacheIndex.getIndex()].writeFile(data.length, data, fileIndex);
                    System.out.println("Repacked cache index " + cacheIndex.getIndex() + " file index " + fileIndex + ".");
                } else {
                    System.err.println("Unable to locate cache index " + cacheIndex.getIndex() + " file index " + fileIndex + ".");
                }
            }
        } catch (Exception ex) {
            System.err.println("Error packing cache index " + cacheIndex.getIndex() + ".");
        }
        System.out.println("Finished repacking " + cacheIndex.getIndex() + ".");
    }

    public static String getFileNameWithoutExtension(String fileName) {
        File tmpFile = new File(fileName);
        tmpFile.getName();
        int whereDot = tmpFile.getName().lastIndexOf('.');
        if (0 < whereDot && whereDot <= tmpFile.getName().length() - 2) {
            return tmpFile.getName().substring(0, whereDot);
        }
        return "";
    }

    /**
     * Makes the specified color transparent in a buffered image.
     *
     * @param im
     * @param color
     * @return
     */
    public static Image makeColorTransparent(BufferedImage im, final Color color) {
        RGBImageFilter filter = new RGBImageFilter() {
            public int markerRGB = color.getRGB() | 0xFF000000;

            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    return 0x00FFFFFF & rgb;
                } else {
                    return rgb;
                }
            }
        };
        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    /**
     * Turns an Image into a BufferedImage.
     *
     * @param image
     * @return
     */
    private static BufferedImage imageToBufferedImage(Image image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bufferedImage;
    }

    static int imageAmount = 0;

    /**
     * Dumps a sprite with the specified name.
     *
     * @param image
     */
    public static void dumpImageOld(SimpleImage image, String name) {
        File directory = new File(SignLink.findCacheDir() + "/itemspritesdump/");
        if (!directory.exists()) {
            System.out.println("Creating directory");
            directory.mkdir();
        } else {
            System.out.println("Directory exists.");
        }
        BufferedImage bi = new BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB);
        bi.setRGB(0, 0, image.width, image.height, image.myPixels, 0, image.width);
        Image img = makeColorTransparent(bi, new Color(0, 0, 0));
        BufferedImage trans = imageToBufferedImage(img);
        File f = new File(SignLink.findCacheDir() + "/itemspritesdump/" + name + ".png");
        boolean duplicate = false;
        if (f.exists() && !f.isDirectory()) {
            imageAmount++;
            duplicate = true;
        }
        try {
            System.out.println("Dump: " + name);
            if (duplicate) {
                //Print the same file name, for now.
                //File out = new File(SignLink.findcachedir() + "/itemspritesdump/" + name + " " + imageAmount + ".png");
                File out = new File(SignLink.findCacheDir() + "/itemspritesdump/" + name + ".png");
                ImageIO.write(trans, "png", out);
            } else {
                File out = new File(SignLink.findCacheDir() + "/itemspritesdump/" + name + ".png");
                ImageIO.write(trans, "png", out);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Client.addReportToServer(e.getMessage());
        }
    }

    public static void dumpImage(SimpleImage image, String name, int targetWidth, int targetHeight) {
        File directory = new File(SignLink.findCacheDir() + "/itemspritesdump/");
        if (!directory.exists()) {
            directory.mkdir();
        }
        BufferedImage bi = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        bi.setRGB(0, 0, targetWidth, targetHeight, image.myPixels, 0, image.width);
        BufferedImage downscaledImage = downscaleImage(bi, targetWidth, targetHeight);

        Image img = makeColorTransparent(downscaledImage, new Color(0, 0, 0));
        BufferedImage trans = imageToBufferedImage(img);

        try {
            File out = new File(SignLink.findCacheDir() + "/itemspritesdump/" + name + ".png");
            ImageIO.write(trans, "png", out);
        } catch (Exception e) {
            e.printStackTrace();
            Client.addReportToServer(e.getMessage());
        }
    }

    public static BufferedImage downscaleImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage scaledImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return scaledImage;
    }

    public static void dumpItemImage(int id, int width, int height) { // int id, int stackSize, int backColour
        SimpleImage image = ItemSpriteFactory.get_sized_item_sprite(id, 1, 0, width, height, true);
        if (image != null) {
            dumpImage(image, Integer.toString(id), width, height);
        }
    }

    //We need to run this method twice.
    public static void dumpItemImages(int idStart, int idEnd, int width, int height) { // int id, int stackSize, int backColour
        for (int id = idStart; id < idEnd; id++) {
            SimpleImage image = ItemSpriteFactory.get_sized_item_sprite(id, 1, 0, width, height, true);
            if (image != null) {
                //System.out.println("Dumping " + id + ": " + ItemDefinition.get(id).name);
                dumpImage(image, Integer.toString(id), width, height);
            }
        }
    }

    public static void dumpItemImages(int idStart, int idEnd) { // int id, int stackSize, int backColour
        for (int id = idStart; id < idEnd; id++) {
            SimpleImage image = ItemSpriteFactory.get_item_sprite(id, 1, 0);
            if (image != null) {
                //System.out.println("Dumping " + id + ": " + ItemDefinition.get(id).name);
                dumpImage(image, Integer.toString(id), 5, 5);
            }
        }
    }

}
