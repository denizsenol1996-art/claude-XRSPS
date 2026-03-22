package com.hazy.scene.object;

import com.hazy.Client;
import com.hazy.cache.def.ObjectDefinition;
import com.hazy.entity.Renderable;

import com.hazy.entity.model.Model;
import net.runelite.client.util.ObjectKeyUtil;
import net.runelite.rs.api.RSBoundaryObject;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.geometry.Shapes;
import net.runelite.rs.api.RSRenderable;

import java.awt.*;

public final class Wall implements RSBoundaryObject {

    public int tileHeights;
    public int zLoc;
    public int xPos;
    public int yPos;
    public int orientation1;
    public int orientation2;
    public Renderable renderable1;
    public Renderable renderable2;
    public long uid = 0L;
    public int mask = 0;

    public Wall() {
    }

    @Override
    public Model getModelA() {
        RSRenderable entity = getRenderable1();
        if (entity == null)
        {
            return null;
        }

        if (entity instanceof Model)
        {
            return (Model) entity;
        }
        else
        {
            return (Model) entity.getModel();
        }
    }
    @Override
    public Model getModelB() {
        RSRenderable entity = getRenderable2();
        if (entity == null)
        {
            return null;
        }

        if (entity instanceof Model)
        {
            return (Model) entity;
        }
        else
        {
            return (Model) entity.getModel();
        }
    }
    @Override
    public Shape getConvexHull() {
        Model model = getModelA();

        if (model == null)
        {
            return null;
        }

        int tileHeight = Perspective.getTileHeight(Client.instance, new LocalPoint(getX(), getY()), getPlane());
        return model.getConvexHull(getX(), getY(), 0, tileHeight);
    }
    @Override
    public Shape getConvexHull2() {
        Model model = getModelB();

        if (model == null)
        {
            return null;
        }

        int tileHeight = Perspective.getTileHeight(Client.instance, new LocalPoint(getX(), getY()), getPlane());
        return model.getConvexHull(getX(), getY(), 0, tileHeight);
    }
    @Override
    public int getPlane() {
        return zLoc;
    }
    @Override
    public int getId() {
        return ObjectKeyUtil.getObjectId(uid);
    }
    @Override
    public Point getCanvasLocation() {
        return Perspective.localToCanvas((net.runelite.api.Client)Client.instance, this.getLocalLocation(), this.getPlane());
    }
    @Override
    public Point getCanvasLocation(int zOffset) {
        return Perspective.localToCanvas((net.runelite.api.Client)Client.instance, this.getLocalLocation(), this.getPlane(), zOffset);
    }
    @Override
    public Polygon getCanvasTilePoly() {
        return Perspective.getCanvasTilePoly(Client.instance, this.getLocalLocation());
    }
    @Override
    public Point getCanvasTextLocation(Graphics2D graphics, String text, int zOffset) {
        return Perspective.getCanvasTextLocation(Client.instance, graphics, getLocalLocation(), text, zOffset);
    }
    @Override
    public Point getMinimapLocation() {
        return Perspective.localToMinimap(Client.instance, getLocalLocation());
    }
    @Override
    public Shape getClickbox() {
        Shape clickboxA = Perspective.getClickbox(Client.instance, getModelA(), 0, getLocalLocation());
        Shape clickboxB = Perspective.getClickbox(Client.instance, getModelB(), 0, getLocalLocation());

        if (clickboxA == null && clickboxB == null)
        {
            return null;
        }

        if (clickboxA != null && clickboxB != null)
        {
            return new Shapes(new Shape[]{clickboxA, clickboxB});
        }

        if (clickboxA != null)
        {
            return clickboxA;
        }

        return clickboxB;
    }
    @Override
    public String getName() {
        return ObjectDefinition.get(getId()).name;
    }

    @Override
    public String[] getActions() {
        return ObjectDefinition.get(getId()).scene_actions;
    }
    @Override
    public WorldPoint getWorldLocation() {
        return WorldPoint.fromLocal(Client.instance, getLocalLocation());
    }
    @Override
    public LocalPoint getLocalLocation() {
        return new LocalPoint(this.xPos, this.yPos);
    }
    @Override
    public long getHash() {
        return uid;
    }
    @Override
    public int getX() {
        return xPos;
    }
    @Override
    public int getY() {
        return yPos;
    }
    @Override
    public int getOrientationA() {
        return orientation1;
    }
    @Override
    public int getOrientationB() {
        return orientation2;
    }

    @Override
    public RSRenderable getRenderable1() {
        return renderable1;
    }

    @Override
    public RSRenderable getRenderable2() {
        return renderable2;
    }

    @Override
    public int getConfig() {
        return mask;
    }
    @Override
    public void setPlane(int plane) {
        this.zLoc = plane;
    }

}
