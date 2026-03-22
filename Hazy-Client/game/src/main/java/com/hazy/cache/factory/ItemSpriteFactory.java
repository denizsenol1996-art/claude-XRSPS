package com.hazy.cache.factory;

import com.hazy.cache.def.ItemDefinition;
import com.hazy.cache.graphics.SimpleImage;
import com.hazy.draw.Rasterizer2D;
import com.hazy.draw.Rasterizer3D;
import com.hazy.entity.model.Model;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

/**
 * This class represents the item icon sprites. A.K.A inventory models on
 * widgets.
 *
 * @author Stan van der Bend (https://www.rune-server.ee/members/StanDev/)
 * @author Patrick van Elderen (https://www.rune-server.ee/members/zerikoth/)
 * Reference: https://www.rune-server.ee/runescape-development/rs2-client/snippets/656135-item-icons-fix.html
 * @version 1.0
 * @since 2019-02-14
 */
public class ItemSpriteFactory {

    public static Long2ObjectMap<SimpleImage> itemSpriteCache =
        new Long2ObjectOpenHashMap<>();

    public static long packCacheKey(final int itemId,
                                    final int thickness,
                                    final double scale) {
        final int scaleInt = (int) (scale * (1 << 16)) & 0xFFFFFF;
        return ((long) itemId << 48) | ((long) thickness << 24) | scaleInt;
    }

    public static int unpackCacheKeyItemId(final long key) {
        return (int) (key >> 48);
    }

    public static int unpackCacheKeyThickness(final long key) {
        return (int) (key >> 24) & 0xFFFFFF;
    }

    public static double unpackCacheKeyScale(final long key) {
        final int scaleInt = (int) (key & 0xFFFFFF);
        return scaleInt / (double) (1 << 16);
    }

    public static SimpleImage get_sized_item_sprite(int itemIdForCache, int stack_size, int border, int w, int h, boolean dumpImages) {
        try {
            boolean ignoreCache = false;
            SimpleImage image;

            ItemDefinition def = ItemDefinition.get(itemIdForCache);
            if (def == null)
                return null;

            int zoom2d = def.zoom2d;

            if (border == -1)
                zoom2d = (int) ((double) zoom2d * 1.5D);
            if (border > 0)
                zoom2d = (int) ((double) zoom2d * 1.04D);

            if (!ignoreCache) {
                final long key = packCacheKey(itemIdForCache, border, zoom2d);
                final SimpleImage sprite = itemSpriteCache.get(key);
                if (sprite != null) {
                    return sprite;
                }
            }

            if (def.countObj == null) {
                stack_size = -1;
            }

            if (stack_size > 1) {
                int stack_item_id = -1;
                for (int index = 0; index < 10; index++)
                    if (stack_size >= def.countCo[index] && def.countCo[index] != 0)
                        stack_item_id = def.countObj[index];

                if (stack_item_id != -1) {
                    def = ItemDefinition.get(stack_item_id);
                }
            }

            Model model = def.get_model(1);
            if (model == null)
                return null;

            SimpleImage noted_sprite = null;
            if (def.notedTemplate != -1) {
                noted_sprite = get_item_sprite(def.notedID, 10, -1);

                if (noted_sprite == null)
                    return null;
            }

            image = new SimpleImage(w, h);
            final int center_x = Rasterizer3D.originViewX;
            final int center_y = Rasterizer3D.originViewY;
            final int[] line_offsets = Rasterizer3D.scanOffsets;
            final int[] pixels = Rasterizer2D.pixels;
            final float[] depthBuffer = Rasterizer2D.depth_buffer;
            final int width = Rasterizer2D.width;
            final int height = Rasterizer2D.height;
            final int viewport_left = Rasterizer2D.leftX;
            final int viewport_right = Rasterizer2D.bottomX;
            final int viewport_top = Rasterizer2D.topY;
            final int viewport_bottom = Rasterizer2D.bottomY;
            Rasterizer3D.world = false;
            Rasterizer3D.mapped = false;
            Rasterizer2D.init(w, h, image.myPixels, new float[w * h]);
            Rasterizer2D.draw_filled_rect(0, 0, w, h, 0);
            Rasterizer2D.clear();
            Rasterizer3D.useViewport();

            int sine = Rasterizer3D.SINE[def.xan2d] * zoom2d >> 16;
            int cosine = Rasterizer3D.COSINE[def.xan2d] * zoom2d >> 16;
            Rasterizer3D.renderOnGpu = true;
            model.calculateBoundsCylinder();
            model.renderModel(def.yan2d, def.zan2d, def.xan2d, def.xOffset2d, sine + model.modelBaseY / 2 + def.yOffset2d, cosine + def.yOffset2d);
            Rasterizer3D.lowMem = true;
            Rasterizer3D.renderOnGpu = false;

            if (def.notedID != -1) {
                image.drawAdvancedSprite(0, 0);
            }

            image.outline(1);
            if (border == 0xffffff) {
                image.outline(16777215);
            } else {
                if (border < 1 && border != -1)
                    image.shadow(3153952);
            }

            Rasterizer2D.init(w, h, image.myPixels, new float[w * h]);

            if (def.notedTemplate != -1) {
                int old_w = noted_sprite.max_width;
                int old_h = noted_sprite.max_height;
                noted_sprite.max_width = w;
                noted_sprite.max_height = h;
                noted_sprite.drawSprite(0, 0);
                noted_sprite.max_width = old_w;
                noted_sprite.max_height = old_h;
            }

            Rasterizer2D.init(width, height, pixels, depthBuffer);
            Rasterizer2D.set_clip(viewport_left, viewport_top, viewport_right, viewport_bottom);
            Rasterizer3D.originViewX = center_x;
            Rasterizer3D.originViewY = center_y;
            Rasterizer3D.scanOffsets = line_offsets;
            Rasterizer3D.world = true;
            Rasterizer3D.mapped = true;

            if (def.stackable)
                image.max_width = w + 1;
            else
                image.max_width = w;

            image.max_height = stack_size;

            if (!ignoreCache) {
                final long key = packCacheKey(itemIdForCache, border, zoom2d);
                itemSpriteCache.put(key, image);
            }

            return image;
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new NullPointerException("Error generating item sprite! [ItemSpriteFactory -> get_item_sprite()]");
        }
    }

    public static SimpleImage get_item_sprite(int id, int stack_size, int highlight) {
        try {
            ItemDefinition def = ItemDefinition.get(id);
            if (def == null) {
                System.out.println("[ERROR] ItemSpriteFactory get_item_sprite - def == null! " + id);
                return null;
            }

            if (def.countObj == null)
                stack_size = -1;

            if (stack_size > 1) {
                int stack_item_id = -1;
                for (int index = 0; index < 10; index++)
                    if (stack_size >= def.countCo[index] && def.countCo[index] != 0)
                        stack_item_id = def.countObj[index];

                if (stack_item_id != -1)
                    def = ItemDefinition.get(stack_item_id);

            }

            if (def == null)
                throw new RuntimeException("def == null");

            Model model = def.get_model(1);

            if (model == null)
                return null;


            int zoom = def.zoom2d;
            if (highlight == -1)
                zoom = (int) ((double) zoom * 1.5D);
            if (highlight > 0)
                zoom = (int) ((double) zoom * 1.04D);

            final long key = packCacheKey(id, highlight, zoom);
            SimpleImage sprite = itemSpriteCache.get(key);
            if (sprite != null) {
                return sprite;
            }

            SimpleImage noted_sprite = null;

            if (def.notedTemplate != -1) {
                noted_sprite = get_item_sprite(def.notedID, 10, -1);

                if (noted_sprite == null)
                    return null;

            }

            sprite = new SimpleImage(32, 32);
            int center_x = Rasterizer3D.originViewX;
            int center_y = Rasterizer3D.originViewY;
            int[] line_offsets = Rasterizer3D.scanOffsets;
            int[] pixels = Rasterizer2D.pixels;
            float[] depth = Rasterizer2D.depth_buffer;
            int width = Rasterizer2D.width;
            int height = Rasterizer2D.height;
            int viewport_left = Rasterizer2D.leftX;
            int viewport_right = Rasterizer2D.bottomX;
            int viewport_top = Rasterizer2D.topY;
            int viewport_bottom = Rasterizer2D.bottomY;
            Rasterizer3D.world = false;
            Rasterizer3D.mapped = false;

            Rasterizer2D.init(32, 32, sprite.myPixels, new float[32 * 32]);
            Rasterizer2D.clear();
            Rasterizer2D.draw_filled_rect(0, 0, 32, 32, 0);
            Rasterizer3D.useViewport();

            int sine = Rasterizer3D.SINE[def.xan2d] * zoom >> 16;
            int cosine = Rasterizer3D.COSINE[def.xan2d] * zoom >> 16;
            Rasterizer3D.renderOnGpu = true;
            model.calculateBoundsCylinder();
            model.renderModel(def.yan2d, def.zan2d, def.xan2d, def.xOffset2d, sine + model.modelBaseY / 2 + def.yOffset2d, cosine + def.yOffset2d);
            Rasterizer3D.lowMem = true;
            Rasterizer3D.renderOnGpu = false;

            if (def.notedID != -1) {
                sprite.drawAdvancedSprite(0, 0);
            }

            sprite.outline(1);
            if (highlight == 0xffffff) {
                sprite.outline(16777215);//16777215 = white
            } else {
                if (highlight < 1 && highlight != -1)
                    sprite.shadow(3153952);//3153952 = black
            }

            Rasterizer2D.init(32, 32, sprite.myPixels, new float[32 * 32]);
            if (def.notedTemplate != -1) {
                int old_w = noted_sprite.max_width;
                int old_h = noted_sprite.max_height;
                noted_sprite.max_width = 32;
                noted_sprite.max_height = 32;
                noted_sprite.drawSprite(0, 0);
                noted_sprite.max_width = old_w;
                noted_sprite.max_height = old_h;
            }

            Rasterizer2D.init(width, height, pixels, depth);
            Rasterizer2D.set_clip(viewport_left, viewport_top, viewport_right, viewport_bottom);
            Rasterizer3D.originViewX = center_x;
            Rasterizer3D.originViewY = center_y;
            Rasterizer3D.scanOffsets = line_offsets;
            Rasterizer3D.world = true;
            Rasterizer3D.mapped = true;
            if (def.stackable)
                sprite.max_width = 33;
            else
                sprite.max_width = 32;

            sprite.max_height = stack_size;
            return sprite;

        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new NullPointerException("Error generating item sprite! [ItemSpriteFactory -> get_item_sprite()]");
        }
    }
}
