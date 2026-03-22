package com.twisted.game.world.region;

import com.twisted.game.world.position.Tile;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class Region {

    /**
     * This region's id.
     */
    private final int regionId;

    /**
     * This region's terrain file id.
     */
    public final int terrainFile;

    /**
     * This region's object file id.
     */
    public final int objectFile;

    /**
     * The clipping in this region.
     */
    public int[][][] clips = new int[4][][];
    public int[][][] projectileClip = new int[4][][];
    public byte[][][] heightMap;

    /**
     * Has this region been loaded?
     */
    private boolean loaded;
    public final int baseX;
    public final int baseY;

    /**
     * Creates a new region.
     *
     * @param regionId
     * @param terrainFile
     * @param objectFile
     */
    public Region(int regionId, int terrainFile, int objectFile) {
        this.regionId = regionId;
        this.terrainFile = terrainFile;
        this.objectFile = objectFile;
        this.baseX = (regionId >> 8) * 64;
        this.baseY = (regionId & 0xff) * 64;
    }

    public static @Nonnull Region get(int absX, int absY) {
        return RegionManager.getRegion(absX, absY);
    }

    @Override
    public String toString() {
        return "Region{" +
            "regionId=" + regionId +
            ", terrainFile=" + terrainFile +
            ", objectFile=" + objectFile +
            ", clips=" + Arrays.toString(clips) +
            ", projectileClip=" + Arrays.toString(projectileClip) +
            ", heightMap=" + Arrays.toString(heightMap) +
            ", loaded=" + loaded +
            ", baseX=" + baseX +
            ", baseY=" + baseY +
            '}';
    }

    public int getRegionId() {
        return regionId;
    }

    public int getTerrainFile() {
        return terrainFile;
    }

    public int getObjectFile() {
        return objectFile;
    }

    /**
     * Gets clipping
     *
     * @param x
     * @param y
     * @param height
     * @return
     */
    public int getClip(int x, int y, int height) {
        int regionAbsX = (regionId >> 8) * 64;
        int regionAbsY = (regionId & 0xff) * 64;
        if (height < 0)
            height = 0;
        if (height >3)
            height %= 4;
        if (clips[height] == null) {
            clips[height] = new int[64][64];
        }
        return clips[height][x - regionAbsX][y - regionAbsY];
    }

    public int getClipProj(int x, int y, int height) {
        int regionAbsX = (regionId >> 8) * 64;
        int regionAbsY = (regionId & 0xff) * 64;
        if (height < 0)
            height = 0;
        if (height > 3)
            height %= 4;
        if (projectileClip[height] == null) {
            projectileClip[height] = new int[64][64];
        }
        return projectileClip[height][x - regionAbsX][y - regionAbsY];
    }

    /**
     * Adds clipping
     *
     * @param x
     * @param y
     * @param height
     * @param shift
     */
    public void addClip(int x, int y, int height, int shift) {
        int regionAbsX = (regionId >> 8) * 64;
        int regionAbsY = (regionId & 0xff) * 64;
        if (height < 0 || height >= 4)
            height = 0;
        if (clips[height] == null) {
            clips[height] = new int[64][64];
        }
        // asuming xy is abs xy
        if (x >= 2944 && x<= 3330 && y >= 3521 && y <= 3522) {
            //System.out.println("clip change "+x+", "+y+", "+height+" by "+shift);
            if (shift == 262144 || shift == 256) {
                return; // fuck wildy ditch
            }
        }
        clips[height][x - regionAbsX][y - regionAbsY] |= shift;
    }

    public void addClipProj(int x, int y, int height, int shift) {
        int regionAbsX = (regionId >> 8) * 64;
        int regionAbsY = (regionId & 0xff) * 64;
        if (height < 0 || height >= 4)
            height = 0;
        if (projectileClip[height] == null) {
            projectileClip[height] = new int[64][64];
        }
        projectileClip[height][x - regionAbsX][y - regionAbsY] |= shift;
    }

    /**
     * Removes clipping.
     *
     * @param x
     * @param y
     * @param height
     * @param shift
     */
    public void removeClip(int x, int y, int height, int shift) {
        int regionAbsX = (regionId >> 8) * 64;
        int regionAbsY = (regionId & 0xff) * 64;
        if (height < 0 || height >= 4)
            height = 0;
        if (clips[height] == null) {
            clips[height] = new int[64][64];
        }
        clips[height][x - regionAbsX][y - regionAbsY] &= ~shift;
    }

    public void removeClipProj(int x, int y, int height, int shift) {
        int regionAbsX = (regionId >> 8) * 64;
        int regionAbsY = (regionId & 0xff) * 64;
        if (height < 0 || height >= 4)
            height = 0;
        if (projectileClip[height] == null) {
            projectileClip[height] = new int[64][64];
        }
        projectileClip[height][x - regionAbsX][y - regionAbsY] &= ~shift;
    }

    public void setClip(int x, int y, int height, int value) {
        int regionAbsX = (regionId >> 8) * 64;
        int regionAbsY = (regionId & 0xff) * 64;
        if (height < 0 || height >= 4)
            height = 0;
        if (clips[height] == null) {
            clips[height] = new int[64][64];
        }
        clips[height][x - regionAbsX][y - regionAbsY] = value;
    }

    /**
     * Gets the local region position.
     *
     * @param tile
     * @return
     */
    public int[] getLocalPosition(Tile tile) {
        int absX = tile.getX();
        int absY = tile.getY();
        int regionAbsX = (regionId >> 8) * 64;
        int regionAbsY = (regionId & 0xff) * 64;
        int localX = absX - regionAbsX;
        int localY = absY - regionAbsY;
        return new int[]{localX, localY};
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
