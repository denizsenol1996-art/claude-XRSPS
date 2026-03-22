package com.hazy.entity;

import com.hazy.Client;
import com.hazy.cache.anim.SeqDefinition;
import com.hazy.cache.anim.SeqFrame;
import com.hazy.cache.anim.SpotAnimation;
import com.hazy.cache.def.ItemDefinition;
import com.hazy.cache.def.NpcDefinition;
import com.hazy.collection.TempCache;
import com.hazy.entity.model.IdentityKit;
import com.hazy.entity.model.Model;
import com.hazy.io.Buffer;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.rs.api.*;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.image.BufferedImage;

public final class Player extends Entity implements RSPlayer {

    public Model get_rotated_model() {
        if (!visible)
            return null;

        Model player = get_animated_model();
        if (player == null)
            return null;

        super.height = player.modelBaseY;

        player.singleTile = true;
        if (reference_pose)
            return player;

        if (super.currentGfxId != -1 && super.spotanimFrame != -1) {
            SpotAnimation anim = SpotAnimation.spotAnims[super.currentGfxId];
            Model model = anim.get_model();
            if (model != null) {
                SeqDefinition seq = anim.seq;
                Model graphic = new Model(false, SeqFrame.noAnimationInProgress(super.spotanimFrame), false, model);
                graphic.translate(0, -super.spotanimY, 0);
                if (seq != null && seq.isSkeletalAnimation()) {
                    graphic.playSkeletal(seq, super.spotanimFrame);
                } else {
                    graphic.generateBones();
                    graphic.interpolate(anim.seq.primaryFrameIds[super.spotanimFrame]);
                }

                graphic.faceGroups = null;
                graphic.groupedTriangleLabels = null;
                if (anim.model_scale_x != 128 || anim.model_scale_y != 128)
                    graphic.scale(anim.model_scale_x, anim.model_scale_x, anim.model_scale_y);

                if (anim.textureToFind != null) {
                    for (int index = 0; index < anim.textureToFind.length; ++index) {
                        graphic.retexture(anim.textureToFind[index], anim.textureToReplace[index]);
                    }
                }
                graphic.light(64 + anim.ambient, 850 + anim.contrast, -30, -50, -30, true);
                Model[] merged = {player, graphic};
                player = new Model(merged);
            } else {
                return null;
            }
        }
        if (transformed_model != null) {
            if (Client.tick >= transform_duration)
                transformed_model = null;

            if (Client.tick >= transform_delay && Client.tick < transform_duration) {
                Model model = transformed_model;
                model.translate(x_offset - super.world_x, z_offset - height, y_offset - super.world_y);
                if (super.turn_direction == 512) {
                    model.rotate_90();
                    model.rotate_90();
                    model.rotate_90();
                } else if (super.turn_direction == 1024) {
                    model.rotate_90();
                    model.rotate_90();
                } else if (super.turn_direction == 1536)
                    model.rotate_90();

                Model[] merged = {player, model};
                player = new Model(merged);

                if (super.turn_direction == 512)
                    model.rotate_90();
                else if (super.turn_direction == 1024) {
                    model.rotate_90();
                    model.rotate_90();
                } else if (super.turn_direction == 1536) {
                    model.rotate_90();
                    model.rotate_90();
                    model.rotate_90();
                }
                model.translate(super.world_x - x_offset, height - z_offset, super.world_y - y_offset);
            }
        }
        player.singleTile = true; // L: 239 //anceint godsword spec updating
        if (super.recolourAmount != 0 && Client.tick >= super.recolourStartCycle && Client.tick < super.recolourEndCycle) { // L: 240
            player.overrideHue = super.recolorHue; // L: 241
            player.overrideSaturation = super.recolourSaturation; // L: 242
            player.overrideLuminance = super.recolourLuminance; // L: 243
            player.overrideAmount = super.recolourAmount; // L: 244
        } else {
            player.overrideAmount = 0; // L: 247
        }
        return player;
    }

    public void update(Buffer buffer) {
        buffer.pos = 0;
        title = buffer.readString();
        titleColor = buffer.readString();
        gender = buffer.readUnsignedByte();
        overhead_icon = buffer.readUnsignedByte();
        skull_icon = buffer.readUnsignedByte();
        hint_arrow_icon = buffer.readUnsignedByte();
        desc = null;
        team_id = 0;
        for (int bodyPart = 0; bodyPart < 12; bodyPart++) {
            int reset = buffer.readUnsignedByte();
            if (reset == 0) {
                player_appearance[bodyPart] = 0;
                continue;
            }

            int id = buffer.readUnsignedByte();
            this.player_appearance[bodyPart] = (reset << 8) + id;

            if (bodyPart == 0 && this.player_appearance[0] == 65535) {
                desc = NpcDefinition.get(buffer.readUnsignedShort());
                break;
            }

            if (this.player_appearance[bodyPart] >= 512 && this.player_appearance[bodyPart] - 512 < ItemDefinition.length) {
                int team_cape = ItemDefinition.get(this.player_appearance[bodyPart] - 512).team;
                if (team_cape != 0) {
                    this.team_id = team_cape;
                }
            }
        }

        for (int index = 0; index < 5; index++) {
            int color = buffer.readUnsignedByte();
            if (color < 0 || color >= Client.APPEARANCE_COLORS[index].length) {
                color = 0;
            }
            appearance_colors[index] = color;
        }

        super.seqStandID = buffer.readUnsignedShort();
        if (super.seqStandID == 65535) {
            super.seqStandID = -1;
        }

        super.standing_turn_animation_id = buffer.readUnsignedShort();
        if (super.standing_turn_animation_id == 65535) {
            super.standing_turn_animation_id = -1;
        }

        super.walk_animation_id = buffer.readUnsignedShort();
        if (super.walk_animation_id == 65535) {
            super.walk_animation_id = -1;
        }

        super.turn_around_animation_id = buffer.readUnsignedShort();
        if (super.turn_around_animation_id == 65535) {
            super.turn_around_animation_id = -1;
        }

        super.pivot_right_animation_id = buffer.readUnsignedShort();
        if (super.pivot_right_animation_id == 65535) {
            super.pivot_right_animation_id = -1;
        }

        super.pivot_left_animation_id = buffer.readUnsignedShort();
        if (super.pivot_left_animation_id == 65535) {
            super.pivot_left_animation_id = -1;
        }

        super.running_animation_id = buffer.readUnsignedShort();
        if (super.running_animation_id == 65535) {
            super.running_animation_id = -1;
        }

        username = buffer.readString();
        combat_level = buffer.readUnsignedByte();
        rights = buffer.readUnsignedByte();
        donatorRights = buffer.readUnsignedByte();

        visible = true;
        appearance_offset = 0L;

        for (int index = 0; index < 12; index++) {
            appearance_offset <<= 4;
            if (player_appearance[index] >= 256) {
                appearance_offset += player_appearance[index] - 256;
            }
        }

        if (player_appearance[0] >= 256) {
            appearance_offset += player_appearance[0] - 256 >> 4;
        }

        if (player_appearance[1] >= 256) {
            appearance_offset += player_appearance[1] - 256 >> 8;
        }

        for (int index = 0; index < 5; index++) {
            appearance_offset <<= 3;
            appearance_offset += appearance_colors[index];
        }

        appearance_offset <<= 1;
        appearance_offset += gender;
    }

    public Model get_animated_model() {
        long offset = appearance_offset;
        int next_frame = -1;
        int current_frame = -1;
        int animation = -1;
        int shield_delta = -1;
        int weapon_delta = -1;

        SeqDefinition primarySeq = null;


        if (desc != null) {
            if (super.sequence >= 0 && super.sequenceDelay == 0) {
                primarySeq = SeqDefinition.get(super.sequence);
                if (!primarySeq.isSkeletalAnimation()) {
                    current_frame = primarySeq.getPrimaryFrameIds()[super.sequenceFrame];
                }
            }
            if (primarySeq != null && primarySeq.isSkeletalAnimation()) {
                return desc.getAnimatedModelSkeletal(primarySeq, null, sequenceFrame, secondarySeqFrame);
            }
            return desc.get_animated_model(-1, current_frame, null);
        }

        if (super.sequence >= 0 && super.sequenceDelay == 0) {
            SeqDefinition seq = SeqDefinition.get(super.sequence);
            current_frame = seq.primaryFrameIds[super.sequenceFrame];
            if (super.secondarySeqID >= 0 && super.secondarySeqID != super.seqStandID) {
                animation = SeqDefinition.get(super.secondarySeqID).primaryFrameIds[super.secondarySeqFrame];
            }

            if (seq.leftHandItem >= 0) {
                shield_delta = seq.leftHandItem;
                offset += shield_delta - player_appearance[5] << 40;
            }
            if (seq.rightHandItem >= 0) {
                weapon_delta = seq.rightHandItem;
                offset += weapon_delta - player_appearance[3] << 48;
            }
        } else if (super.secondarySeqID >= 0) {
            current_frame = SeqDefinition.get(super.secondarySeqID).primaryFrameIds[super.secondarySeqFrame];
        }
        Model model = (Model) model_cache.get(offset);
        if (model == null) {
            boolean cached = false;
            for (int index = 0; index < 12; index++) {
                int appearance = player_appearance[index];
                if (weapon_delta >= 0 && index == 3)
                    appearance = weapon_delta;

                if (shield_delta >= 0 && index == 5)
                    appearance = shield_delta;

                if (appearance >= 256 && appearance < 512 && !IdentityKit.cache[appearance - 256].body_cached())
                    cached = true;

                if (appearance >= 512 && !ItemDefinition.get(appearance - 512).equipped_model_cached(gender))
                    cached = true;

            }
            if (cached) {
                if (key != -1L)
                    model = (Model) model_cache.get(key);

                if (model == null)
                    return null;
            }
        }
        if (model == null) {
            Model[] character = new Model[12];
            int equipped = 0;
            for (int index = 0; index < 12; index++) {
                int appearance = player_appearance[index];
                if (weapon_delta >= 0 && index == 3)
                    appearance = weapon_delta;

                if (shield_delta >= 0 && index == 5)
                    appearance = shield_delta;

                if (appearance >= 256 && appearance < 512) {
                    Model idk = IdentityKit.cache[appearance - 256].get_body();
                    if (idk != null)
                        character[equipped++] = idk;

                }
                if (appearance >= 512) {
                    Model items = ItemDefinition.get(appearance - 512).get_equipped_model(gender);
                    if (items != null)
                        character[equipped++] = items;

                }
            }
            model = new Model(equipped, character);
            for (int index = 0; index < 5; index++) {
                if (appearance_colors[index] != 0) {
                    model.recolor(Client.APPEARANCE_COLORS[index][0], Client.APPEARANCE_COLORS[index][appearance_colors[index]]);
                    if (index == 1)
                        model.recolor(Client.SHIRT_SECONDARY_COLORS[0], Client.SHIRT_SECONDARY_COLORS[appearance_colors[index]]);

                }
            }
            model.generateBones();
            model.light(64, 850, -30, -50, -30, true);
            model_cache.put(model, offset);
            key = offset;
        }
        if (reference_pose) {
            return model;
        }
        Model animated = Model.EMPTY_MODEL;

        animated.replace(model, SeqFrame.noAnimationInProgress(current_frame) & SeqFrame.noAnimationInProgress(animation));
        if (current_frame != -1 && animation != -1) {
            animated.mix(SeqDefinition.get(super.sequence).getInterleaveOrder(), animation, current_frame);
        } else if (current_frame != -1) {
            animated.interpolate(current_frame);
        }

        animated.calc_diagonals();
        animated.faceGroups = null;
        animated.groupedTriangleLabels = null;
        return animated;
    }

    public Model get_dialogue_model() {
        if (!visible)
            return null;

        if (desc != null)
            return desc.get_dialogue_model();

        boolean cached = false;
        for (int index = 0; index < 12; index++) {
            int appearance = player_appearance[index];
            if (appearance >= 256 && appearance < 512 && !IdentityKit.cache[appearance - 256].headLoaded())
                cached = true;

            if (appearance >= 512 && !ItemDefinition.get(appearance - 512).dialogue_model_cached(gender))
                cached = true;

        }
        if (cached)
            return null;

        Model[] character = new Model[12];
        int equipped = 0;
        for (int index = 0; index < 12; index++) {
            int appearance = player_appearance[index];
            if (appearance >= 256 && appearance < 512) {
                Model idk = IdentityKit.cache[appearance - 256].get_head();
                if (idk != null)
                character[equipped++] = idk;

            }
            if (appearance >= 512) {
                Model items = ItemDefinition.get(appearance - 512).get_equipped_dialogue_model(gender);
                if (items != null)
                    character[equipped++] = items;

            }
        }
        Model model = new Model(equipped, character);
        for (int index = 0; index < 5; index++)
            if (appearance_colors[index] != 0) {
                model.recolor(Client.APPEARANCE_COLORS[index][0], Client.APPEARANCE_COLORS[index][appearance_colors[index]]);
                if (index == 1)
                    model.recolor(Client.SHIRT_SECONDARY_COLORS[0], Client.SHIRT_SECONDARY_COLORS[appearance_colors[index]]);
            }

        return model;
    }

    public boolean visible() {
        return visible;
    }

    public Player() {
        key = -1L;
        reference_pose = false;
        appearance_colors = new int[5];
        visible = false;
        player_appearance = new int[12];
    }

    public int rights, donatorRights;
    private long key;
    public NpcDefinition desc;
    public boolean reference_pose;
    public final int[] appearance_colors;
    public int team_id;
    private int gender;
    public String username;
    public static TempCache model_cache = new TempCache(260);
    public int combat_level;
    public int overhead_icon;
    public int skull_icon;
    public int hint_arrow_icon;
    public int transform_delay;
    public int transform_duration;
    public int height;
    public boolean visible;
    public int x_offset;
    public int z_offset;
    public int y_offset;
    public Model transformed_model;
    public final int[] player_appearance;
    private long appearance_offset;
    public int transform_width;
    public int transform_height;
    public int transform_width_offset;
    public int transform_height_offset;
    public int skill_level;
    public String title = "";
    public String titleColor = "";

    /**
     * Gets the players title
     *
     * @return title
     */
    public String getTitle(boolean rightClick) {
        if (title.length() > 0) {
            if (rightClick) {
                return titleColor + title + " <col=ffffff>";
            } else {
                return titleColor + title + " <col=0>";
            }
        } else {
            return "";
        }
    }

    @Nullable
    @Override
    public String getName() {
        return this.username;
    }

    @Override
    public boolean isInteracting() {
        return false;
    }

    @Override
    public Actor getInteracting() {
        return null;
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
        return WorldPoint.fromLocal(Client.instance,
            this.getPathX()[0] * Perspective.LOCAL_TILE_SIZE + Perspective.LOCAL_TILE_SIZE / 2,
            this.getPathY()[0] * Perspective.LOCAL_TILE_SIZE + Perspective.LOCAL_TILE_SIZE / 2,
            Client.instance.getPlane());
    }

    @Override
    public LocalPoint getLocalLocation() {
        return new LocalPoint(getX(), getY());
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
        return null;
    }

    @Nullable
    @Override
    public net.runelite.api.Point getCanvasTextLocation(Graphics2D graphics, String text, int zOffset) {
        return null;
    }

    @Override
    public net.runelite.api.Point getCanvasImageLocation(BufferedImage image, int zOffset) {
        return null;
    }

    @Override
    public net.runelite.api.Point getCanvasSpriteLocation(SpritePixels sprite, int zOffset) {
        return null;
    }

    @Override
    public Point getMinimapLocation() {
        return null;
    }

    @Override
    public Shape getConvexHull() {
        return null;
    }

    @Override
    public WorldArea getWorldArea() {
        return null;
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
    public Polygon[] getPolygons() {
        return new Polygon[0];
    }

    @Nullable
    @Override
    public HeadIcon getOverheadIcon() {
        return null;
    }

    @Nullable
    @Override
    public SkullIcon getSkullIcon() {
        return null;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public int getRSInteracting() {
        return 0;
    }

    @Override
    public String getOverheadText() {
        return null;
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
        return 0;
    }

    @Override
    public int getOrientation() {
        return 0;
    }

    @Override
    public int getCurrentOrientation() {
        return 0;
    }

    @Override
    public RSIterableNodeDeque getHealthBars() {
        return null;
    }

    @Override
    public int[] getHitsplatValues() {
        return new int[0];
    }

    @Override
    public int[] getHitsplatTypes() {
        return new int[0];
    }

    @Override
    public int[] getHitsplatCycles() {
        return new int[0];
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
    public RSUsername getRsName() {
        return null;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public int getPlayerId() {
        return 0;
    }

    @Override
    public RSPlayerComposition getPlayerComposition() {
        return null;
    }

    @Override
    public int getCombatLevel() {
        return 0;
    }

    @Override
    public int getTotalLevel() {
        return 0;
    }

    @Override
    public int getTeam() {
        return 0;
    }

    @Override
    public boolean isFriendsChatMember() {
        return false;
    }

    @Override
    public boolean isClanMember() {
        return false;
    }

    @Override
    public boolean isFriend() {
        return false;
    }

    @Override
    public boolean isFriended() {
        return false;
    }

    @Override
    public int getRsOverheadIcon() {
        return 0;
    }

    @Override
    public int getRsSkullIcon() {
        return 0;
    }

    @Override
    public int getRSSkillLevel() {
        return 0;
    }

    @Override
    public String[] getActions() {
        return new String[0];
    }

    @Override
    public RSModel getModel() {
        return get_rotated_model();
    }


}
