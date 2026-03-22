package com.hazy.scene.object;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;

import com.hazy.Client;
import com.hazy.cache.def.ObjectDefinition;

import com.hazy.entity.Renderable;
import net.runelite.api.Model;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.geometry.Shapes;
import net.runelite.client.util.ObjectKeyUtil;
import net.runelite.rs.api.RSModel;
import net.runelite.rs.api.RSRenderable;
import net.runelite.rs.api.RSWallDecoration;

import javax.annotation.Nullable;


public final class WallDecoration implements RSWallDecoration {

    public int plane;
    public int world_x;
    public int world_y;
    public int config_mask;
    public int orientation;
    public Renderable node;
    public long uid;
    public byte mask;

    @Override
    public Shape getConvexHull() {
        return null;
    }

    @Override
    public Shape getConvexHull2() {
        return null;
    }

    @Override
    public WorldPoint getWorldLocation() {
        return null;
    }

    @Override
    public LocalPoint getLocalLocation() {
        return null;
    }

    @Override
    public int getPlane() {
        return 0;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Point getCanvasLocation() {
        return null;
    }

    @Override
    public Point getCanvasLocation(int zOffset) {
        return null;
    }

    @Override
    public Polygon getCanvasTilePoly() {
        return null;
    }

    @Override
    public Point getCanvasTextLocation(Graphics2D graphics, String text, int zOffset) {
        return null;
    }

    @Override
    public Point getMinimapLocation() {
        return null;
    }

    @Nullable
    @Override
    public Shape getClickbox() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String[] getActions() {
        return new String[0];
    }

    @Override
    public int getConfig() {
        return 0;
    }

    @Override
    public long getHash() {
        return 0;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public int getXOffset() {
        return 0;
    }

    @Override
    public int getYOffset() {
        return 0;
    }

    @Override
    public int getOrientation() {
        return 0;
    }

    @Override
    public RSRenderable getRenderable() {
        return null;
    }

    @Override
    public RSRenderable getRenderable2() {
        return null;
    }

    @Override
    public void setPlane(int plane) {

    }
}
