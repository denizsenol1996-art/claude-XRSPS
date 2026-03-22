package com.hazy.cache.factory;

import com.hazy.cache.graphics.SimpleImage;

public class ItemSpriteCache {

    public ItemSpriteCache(SimpleImage sprite, int spriteOutline, double scale) {
        this.sprite = sprite;
        this.spriteOutline = spriteOutline;
        this.scale = scale;
    }

    public int spriteOutline;
    public SimpleImage sprite;
    public double scale;

}
