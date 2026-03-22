package com.hazy.entity;

import com.hazy.Client;
import com.hazy.cache.anim.SeqDefinition;
import com.hazy.cache.anim.SeqFrame;
import com.hazy.cache.anim.SpotAnimation;
import com.hazy.cache.def.NpcDefinition;
import com.hazy.entity.model.Model;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.NpcChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.rs.api.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class Npc extends Entity implements RSNPC {

    public NpcDefinition desc;
    public int headIcon = -1;
    public int ownerIndex = -1;
    public int ownedNpcIndex = -1;

    public boolean showActions() {
        if (ownerIndex == -1) {
            return true;
        }
        return (Client.instance.localPlayerIndex == ownerIndex);
    }

    public int getHeadIcon() {
        if (headIcon == -1) {
            if (desc != null && desc.headIconSpriteIndex != null)
                return desc.headIconSpriteIndex[0];
            if (desc != null) {
                return desc.defaultHeadIconArchive;
            }
        }
        return headIcon;
    }

    private Model get_animated_model() {
        SeqDefinition primarySeq = null;
        SeqDefinition secondarySeq = null;
        if ((super.sequence >= 0) && (super.sequenceDelay == 0)) {
            primarySeq = SeqDefinition.get(super.sequence);
            boolean primarySeqIsSkeletal = primarySeq.isSkeletalAnimation();
            int primaryTransformID = primarySeqIsSkeletal ? -1 : primarySeq.getPrimaryFrameIds()[super.sequenceFrame];
            int secondaryTransformID = -1;
            if ((super.secondarySeqID >= 0) && (super.secondarySeqID != super.seqStandID)) {
                secondarySeq = SeqDefinition.get(super.secondarySeqID);
                boolean secondarySeqIsSkeletal = primarySeq.isSkeletalAnimation();
                secondaryTransformID = secondarySeqIsSkeletal ? -1 : secondarySeq.getPrimaryFrameIds()[super.secondarySeqFrame];
            }
            // double anim
            if (primarySeq.isSkeletalAnimation() || (secondarySeq != null && secondarySeq.isSkeletalAnimation())) {
                return desc.getAnimatedModelSkeletal(primarySeq, secondarySeq, sequenceFrame, secondarySeqFrame);
            }
            return desc.get_animated_model(secondaryTransformID, primaryTransformID, SeqDefinition.get(super.sequence).getInterleaveOrder());
        }

        int transformID = -1;

        if (super.secondarySeqID >= 0) {
            secondarySeq = SeqDefinition.get(super.secondarySeqID);
            transformID = secondarySeq.isSkeletalAnimation() ? -1 : secondarySeq.getPrimaryFrameIds()[super.secondarySeqFrame];
        }

        if (secondarySeq != null && secondarySeq.isSkeletalAnimation()) {
            return desc.getAnimatedModelSkeletal(primarySeq, secondarySeq, sequenceFrame, secondarySeqFrame);
        }

        return desc.get_animated_model(-1, transformID, null);
    }

    public Model get_rotated_model() {
        if (desc == null)
            return null;

        Model animated = get_animated_model();
        if (animated == null)
            return null;

        super.height = animated.modelBaseY;
        if (super.currentGfxId != -1 && super.spotanimFrame != -1) {
            SpotAnimation anim = SpotAnimation.spotAnims[super.currentGfxId];
            Model model = anim.get_model();
            if (model != null) {
                Model graphic = new Model(true, SeqFrame.noAnimationInProgress(super.spotanimFrame), false, model);
                graphic.translate(0, -super.spotanimY, 0);
                if (anim.seq != null && anim.seq.isSkeletalAnimation()) {
                    graphic.playSkeletal(anim.seq, super.spotanimFrame);
                } else {
                    graphic.generateBones();
                    graphic.interpolate(anim.seq.primaryFrameIds[super.spotanimFrame]);
                }

                graphic.faceGroups = null;
                graphic.groupedTriangleLabels = null;
                if (anim.model_scale_x != 128 || anim.model_scale_y != 128)
                    graphic.scale(anim.model_scale_x, anim.model_scale_x, anim.model_scale_y);
                graphic.light(64 + anim.ambient, 850 + anim.contrast, -30, -50, -30, true);
                Model[] build = {
                    animated, graphic
                };
                animated = new Model(build);
            }
        }
        if (desc.occupied_tiles == 1)
            animated.singleTile = true;

        if (super.recolourAmount != 0 && Client.tick >= super.recolourStartCycle && Client.tick < super.recolourEndCycle) { // L: 240
            animated.overrideHue = super.recolorHue; // L: 241
            animated.overrideSaturation = super.recolourSaturation; // L: 242
            animated.overrideLuminance = super.recolourLuminance; // L: 243
            animated.overrideAmount = super.recolourAmount; // L: 244
        } else {
            animated.overrideAmount = 0; // L: 247
        }
        return animated;
    }

    public boolean visible() {
        return desc != null;
    }

    @Override
    public int getRSInteracting() {
        return index;
    }

    @Override
    public String getOverheadText() {
        return "";
    }

    @Override
    public void setOverheadText(String overheadText) {

    }

    @Override
    public int getX() {
        return world_x;
    }

    @Override
    public int getY() {
        return world_y;
    }

    @Override
    public int[] getPathX() {
        return waypoint_x;
    }

    @Override
    public int[] getPathY() {
        return waypoint_y;
    }

    @Override
    public int getRSAnimation() {
        return 0;
    }

    @Override
    public int getAnimation() {
        return 0;
    }

    @Override
    public void setAnimation(int animation) {

    }

    @Override
    public int getAnimationFrame() {
        return 0;
    }

    @Override
    public int getActionFrame() {
        return 0;
    }

    @Override
    public void setAnimationFrame(int frame) {
    }

    @Override
    public boolean hasSpotAnim(int spotAnimId) {
        return false;
    }

    @Override
    public void createSpotAnim(int id, int spotAnimId, int height, int delay) {

    }

    @Override
    public void removeSpotAnim(int id) {

    }

    @Override
    public void clearSpotAnims() {

    }

    @Override
    public void setActionFrame(int frame) {
    }

    @Override
    public int getActionFrameCycle() {
        return 0;
    }

    @Override
    public RSIterableNodeHashTable getSpotAnims() {
        return null;
    }

    @Override
    public RSActorSpotAnim newActorSpotAnim(int id, int height, int delay, int frame) {
        return null;
    }

    @Override
    public int getGraphicsCount() {
        return 0;
    }

    @Override
    public void setGraphicsCount(int count) {

    }

    @Override
    public int getGraphic() {
        return 0;
    }

    @Override
    public void setGraphic(int id) {

    }

    @Override
    public int getGraphicHeight() {
        return 0;
    }

    @Override
    public void setGraphicHeight(int height) {

    }

    @Override
    public int getSpotAnimFrame() {
        return 0;
    }

    @Override
    public void setSpotAnimFrame(int id) {
    }

    @Override
    public int getSpotAnimationFrameCycle() {
        return 0;
    }

    @Override
    public int getIdlePoseAnimation() {
        return 0;
    }

    @Override
    public void setIdlePoseAnimation(int animation) {
    }

    @Override
    public int getPoseAnimation() {
        return 0;
    }

    @Override
    public void setPoseAnimation(int animation) {

    }

    @Override
    public int getPoseFrame() {
        return 0;
    }

    @Override
    public void setPoseFrame(int frame) {
    }

    @Override
    public int getPoseFrameCycle() {
        return 0;
    }

    @Override
    public int getLogicalHeight() {
        return height;
    }

    @Override
    public int getOrientation() {
        return current_rotation;
    }

    @Override
    public int getCurrentOrientation() {
        return current_rotation;
    }

    @Override
    public RSIterableNodeDeque getHealthBars() {
        return null;
    }

    @Override
    public int[] getHitsplatValues() {
        return null;
    }

    @Override
    public int[] getHitsplatTypes() {
        return null;
    }

    @Override
    public int[] getHitsplatCycles() {
        return null;
    }

    @Override
    public int getIdleRotateLeft() {
        return 0;
    }

    @Override
    public int getIdleRotateRight() {
        return 0;
    }

    @Override
    public int getWalkAnimation() {
        return 0;
    }

    @Override
    public int getWalkRotate180() {
        return 0;
    }

    @Override
    public int getWalkRotateLeft() {
        return 0;
    }

    @Override
    public int getWalkRotateRight() {
        return 0;
    }

    @Override
    public int getRunAnimation() {
        return 0;
    }

    @Override
    public void setDead(boolean dead) {
    }

    @Override
    public int getPathLength() {
        return 0;
    }

    @Override
    public int getOverheadCycle() {
        return 0;
    }

    @Override
    public void setOverheadCycle(int cycle) {
    }

    @Override
    public int getPoseAnimationFrame() {
        return 0;
    }

    @Override
    public void setPoseAnimationFrame(int frame) {

    }

    @Override
    public RSModel getModel() {
        return get_rotated_model();
    }

    @Override
    public int getCombatLevel() {
        return desc.combatLevel;
    }

    @Override
    public String getName() {
        return desc.getName();
    }

    @Override
    public boolean isInteracting() {
        return false;
    }

    @Override
    public Actor getInteracting() {
        int index = getRSInteracting();
        if (index == -1 || index == 65535) {
            return null;
        }
        Client client = Client.instance;

        if (index < 32768) {
            NPC[] npcs = client.getCachedNPCs();
            return npcs[index];
        }

        index -= 32768;
        Player[] players = client.players;
        return players[index];
    }

    @Override
    public int getHealthRatio() {
        return 0;
    }

    @Override
    public int getHealthScale() {
        return 0;
    }

    @Override
    public WorldPoint getWorldLocation() {
        return WorldPoint.fromLocal(Client.instance, getLocalLocation());
    }

    @Override
    public LocalPoint getLocalLocation() {
        return new LocalPoint(this.world_x, this.world_y);
    }

    @Override
    public void setIdleRotateLeft(int animationID) {
    }

    @Override
    public void setIdleRotateRight(int animationID) {
    }

    @Override
    public void setWalkAnimation(int animationID) {
    }

    @Override
    public void setWalkRotateLeft(int animationID) {
    }

    @Override
    public void setWalkRotateRight(int animationID) {
    }

    @Override
    public void setWalkRotate180(int animationID) {
    }

    @Override
    public void setRunAnimation(int animationID) {
    }

    @Override
    public Polygon getCanvasTilePoly() {
        return Perspective.getCanvasTilePoly(Client.instance, this.getLocalLocation());
    }

    @Override
    public net.runelite.api.Point getCanvasTextLocation(Graphics2D graphics, String text, int zOffset) {
        return Perspective.getCanvasTextLocation(Client.instance, graphics, getLocalLocation(), text, zOffset);
    }

    @Override
    public net.runelite.api.Point getCanvasImageLocation(BufferedImage image, int zOffset) {
        return Perspective.getCanvasImageLocation(Client.instance, getLocalLocation(), image, zOffset);
    }

    @Override
    public net.runelite.api.Point getCanvasSpriteLocation(SpritePixels sprite, int zOffset) {
        return null;
    }

    @Override
    public Point getMinimapLocation() {
        return Perspective.localToMinimap(Client.instance, getLocalLocation());
    }

    @Override
    public Shape getConvexHull() {
        RSModel model = getModel();
        if (model == null) {
            return null;
        }

        int size = getComposition().getSize();
        LocalPoint tileHeightPoint = new LocalPoint(
            size * Perspective.LOCAL_HALF_TILE_SIZE - Perspective.LOCAL_HALF_TILE_SIZE + getX(),
            size * Perspective.LOCAL_HALF_TILE_SIZE - Perspective.LOCAL_HALF_TILE_SIZE + getY());

        int tileHeight = Perspective.getTileHeight(Client.instance, tileHeightPoint, Client.instance.getPlane());

        return model.getConvexHull(getX(), getY(), getOrientation(), tileHeight);
    }

    @Override
    public WorldArea getWorldArea() {
        int size = 1;
        if (this instanceof NPC) {
            NPCComposition composition = ((NPC) this).getComposition();
            if (composition != null && composition.getConfigs() != null) {
                composition = composition.transform();
            }
            if (composition != null) {
                size = composition.getSize();
            }
        }

        return new WorldArea(this.getWorldLocation(), size, size);
    }

    @Override
    public boolean isDead() {
        return false;
    }

    @Override
    public boolean isMoving() {
        return false;
    }

    @Override
    public int getId() {
        if (desc == null)
            return -1;

        return desc.id;
    }

    @Override
    public NPCComposition getTransformedComposition() {
        RSNPCComposition composition = getComposition();
        if (composition != null && composition.getConfigs() != null) {
            composition = composition.transform();
        }
        return composition;
    }

    @Override
    public void onDefinitionChanged(NPCComposition composition) {
        if (composition == null) {
            Client.instance.getCallbacks().post(new NpcDespawned(this));
        } else if (this.getId() != -1) {
            RSNPCComposition oldComposition = getComposition();
            if (oldComposition == null) {
                return;
            }

            if (composition.getId() == oldComposition.getId()) {
                return;
            }

            Client.instance.getCallbacks().postDeferred(new NpcChanged(this, oldComposition));
        }
    }

    @Override
    public RSNPCComposition getComposition() {
        return desc;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int id) {
        this.index = id;
    }

}
