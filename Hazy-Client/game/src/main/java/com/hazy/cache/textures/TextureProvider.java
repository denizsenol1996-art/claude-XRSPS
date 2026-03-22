package com.hazy.cache.textures;

import com.hazy.cache.Archive;
import com.hazy.collection.Deque;
import com.hazy.draw.TextureAnimationData;
import com.hazy.io.Buffer;
import net.runelite.rs.api.RSTexture;
import net.runelite.rs.api.RSTextureProvider;

public class TextureProvider implements RSTextureProvider, TextureLoader {

    private final Texture[] textures;

    private Deque deque;

    private int capacity;

    private int remaining;

    private double brightness;

    private int textureSize;

    private final Archive archive;

    public TextureProvider(Archive textureArchive, Archive configArchive, int capacity, int textureSize) {
        deque = new Deque();
        archive = textureArchive;
        this.capacity = capacity;
        this.remaining = this.capacity;
        this.textureSize = textureSize;
        int textureCount;

        Buffer stream = new Buffer(configArchive.get("textures.dat"));
        //textureCount = stream.readUShort();
        textureCount = 60; //Should be UShort but i have made it stop at 60 due to loading custom textures and using old system
        int customTextures = 120;
        textures = new Texture[textureCount + customTextures];

        for (int var9 = 0; var9 < textureCount; ++var9) {
            Texture text =  new Texture(stream);
            textures[text.id] = text;
        }

        for (int var9 = textureCount; var9 < textures.length; ++var9) {
            TextureAnimationData data = TextureAnimationData.getById(var9);
            int speed = data == null ? 0 : data.getSpeed();
            int direction = data == null ? 0 : data.getDirection();
            Texture text =  new Texture(var9,false,var9,speed,direction,0);
            textures[text.id] = text;
        }


        setMaxSize(128);
        setSize(128);

    }

    @Override
    public double getBrightness() {
        return brightness;
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
        clear();
    }

    public void setTextureSize(int textureSize) {
        this.textureSize = textureSize;
        clear();
    }

    @Override
    public void setMaxSize(int maxSize) {
        capacity = maxSize;
    }

    @Override
    public void setSize(int size) {
        remaining = size;
    }

    @Override
    public RSTexture[] getTextures() {
        return textures;
    }

    @Override
    public int[] load(int textureId) {
        return getTexturePixels(textureId);
    }

    public int[] getTexturePixels(int textureID) {
        Texture texture = textures[textureID];
        if (texture != null) {
            if (texture.pixels != null) {
                deque.insertTail(texture);
                texture.isLoaded = true;
                return texture.pixels;
            }

            boolean hasLoaded = texture.load(brightness, textureSize, archive);
            if (hasLoaded) {
                if (remaining == 0) {
                    Texture currentTexture = (Texture)deque.popHead();
                    currentTexture.reset();
                } else {
                    --remaining;
                }

                deque.insertTail(texture);
                texture.isLoaded = true;
                return texture.pixels;
            }
        }

        return null;
    }

    public int getAverageTextureRGB(int textureID) {
        return textures[textureID] != null ? textures[textureID].averageRGB : 0;
    }

    public boolean isTransparent(int textureID) {
        return textures[textureID].isTransparent;
    }

    @Override
    public boolean isLowDetail(int textureID) {
        return textureSize == 64;
    }

    public void clear() {
        for (Texture texture : textures) {
            if (texture != null) {
                texture.reset();
            }
        }

        deque = new Deque();
        remaining = capacity;
    }

    public void animate(int textureID) {
        for (Texture texture : textures) {
            if (texture != null && texture.animationDirection != 0 && texture.isLoaded) {
                texture.animate(textureID);
                texture.isLoaded = false;
            }
        }
    }

}
