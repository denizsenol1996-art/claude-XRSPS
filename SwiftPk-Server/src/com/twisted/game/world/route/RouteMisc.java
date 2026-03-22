package com.twisted.game.world.route;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.position.Tile;

public class RouteMisc {


    public static int getDistance(Tile src, Tile dest) {
        return getDistance(src.getX(), src.getY(), dest.getX(), dest.getY());
    }

    public static int getDistance(Tile src, int destX, int destY) {
        return getDistance(src.getX(), src.getY(), destX, destY);
    }

    public static int getDistance(int x1, int y1, int x2, int y2) {
        int diffX = Math.abs(x1 - x2);
        int diffY = Math.abs(y1 - y2);
        return Math.max(diffX, diffY);
    }

    public static int getClosestX(Mob src, Mob target) {
        return getClosestX(src, target.tile());
    }

    public static int getClosestX(Mob src, Tile target) {
        if (src.getSize() == 1)
            return src.getAbsX();
        if (target.getX() < src.getAbsX())
            return src.getAbsX();
        else if (target.getX() >= src.getAbsX() && target.getX() <= src.getAbsX() + src.getSize() - 1)
            return target.getX();
        else
            return src.getAbsX() + src.getSize() - 1;
    }

    public static int getClosestY(Mob src, Mob target) {
        return getClosestY(src, target.tile());
    }

    public static int getClosestY(Mob src, Tile target) {
        if (src.getSize() == 1)
            return src.getAbsY();
        if (target.getY() < src.getAbsY())
            return src.getAbsY();
        else if (target.getY() >= src.getAbsY() && target.getY() <= src.getAbsY() + src.getSize() - 1)
            return target.getY();
        else
            return src.getAbsY() + src.getSize() - 1;
    }

    public static Tile getClosestPosition(Mob src, Mob target) {
        return new Tile(getClosestX(src, target), getClosestY(src, target), src.getZ());
    }

    public static Tile getClosestPosition(Mob src, Tile target) {
        return new Tile(getClosestX(src, target), getClosestY(src, target), src.getZ());
    }

    public static int getEffectiveDistance(Mob src, Mob target) {
        Tile pos = getClosestPosition(src, target);
        Tile pos2 = getClosestPosition(target, src);
        return getDistance(pos, pos2);
    }
}
