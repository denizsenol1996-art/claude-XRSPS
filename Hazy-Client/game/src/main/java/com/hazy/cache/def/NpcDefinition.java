package com.hazy.cache.def;

import com.hazy.Client;
import com.hazy.ClientConstants;
import com.hazy.cache.Archive;
import com.hazy.cache.anim.SeqDefinition;
import com.hazy.cache.anim.SeqFrame;
import com.hazy.cache.config.VariableBits;
import com.hazy.cache.def.impl.npcs.CustomBosses;
import com.hazy.cache.def.impl.npcs.CustomPets;
import com.hazy.cache.def.impl.NpcManager;
import com.hazy.cache.def.impl.npcs.MemberNpcs;
import com.hazy.collection.TempCache;
import com.hazy.entity.model.Model;
import com.hazy.io.Buffer;
import com.hazy.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.HeadIcon;
import net.runelite.api.IterableHashTable;
import net.runelite.rs.api.RSIterableNodeHashTable;
import net.runelite.rs.api.RSNPCComposition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class NpcDefinition implements RSNPCComposition {

    public static int totalNPCs;
    public int height = -1;
    public int[] stats = {1, 1, 1, 1, 1, 1};

    public static void init(Archive streamLoader) {
        buffer = new Buffer(streamLoader.get("npc.dat"));
        final Buffer idx = new Buffer(streamLoader.get("npc.idx"));
        int highestFileId = idx.readUnsignedShort();
        offsets = new int[highestFileId + 1 + 20_000];
        int offset = 0;
        for (int i = 0; i < highestFileId; i++) {
            final int size = idx.readUnsignedShort();
            if (size == -1 || size == 65535) {
                break;
            }
            offsets[i] = offset;
            offset += size;
        }

        cache = new NpcDefinition[20];

        for (int k = 0; k < 20; k++) {
            cache[k] = new NpcDefinition();
        }
    }


    private static final boolean dump = false;

    public static int getModelIds(final int id, final int models) {
        final NpcDefinition npcDefinition = get(id);
        return npcDefinition.models[models];
    }

    public static int getadditionalModels(final int id, final int models) {
        final NpcDefinition npcDefinition = get(id);
        return npcDefinition.chatheadModels[models];
    }

    public static int getModelColorIds(final int id, final int color) {
        final NpcDefinition npcDefinition = get(id);
        return npcDefinition.recolorToFind[color];
    }

    public static int getStandAnim(final int id) {
        final NpcDefinition npcDefinition = get(id);
        return npcDefinition.standingAnimation;
    }

    public static int getWalkAnim(final int id) {
        final NpcDefinition entityDef = get(id);
        return entityDef.walkingAnimation;
    }

    public static int getHalfTurnAnimation(final int id) {
        final NpcDefinition entityDef = get(id);
        return entityDef.rotate180Animation;
    }

    public static int getQuarterClockwiseTurnAnimation(final int id) {
        final NpcDefinition entityDef = get(id);
        return entityDef.quarterClockwiseTurnAnimation;
    }

    public static int getQuarterAnticlockwiseTurnAnimation(final int id) {
        final NpcDefinition entityDef = get(id);
        return entityDef.quarterAnticlockwiseTurnAnimation;
    }

    public static NpcDefinition get(int id) {
        for (int i = 0; i < 20; i++) {
            if (cache[i].interfaceType == (long) id) {
                return cache[i];
            }
        }

        cache_index = (cache_index + 1) % 20;
        NpcDefinition npcDefinition = cache[cache_index] = new NpcDefinition();
        buffer.pos = offsets[id];
        npcDefinition.id = id;
        npcDefinition.interfaceType = id;
        npcDefinition.decode(buffer);

        if (id == 13391) {
            npcDefinition.name = "Mini donator boss";
            npcDefinition.models = new int[]{34163};
            npcDefinition.occupied_tiles = 5;
            npcDefinition.standingAnimation = 90;
            npcDefinition.walkingAnimation = 79;
            npcDefinition.rotate180Animation = 79;
            npcDefinition.quarterClockwiseTurnAnimation = 79;
            npcDefinition.quarterAnticlockwiseTurnAnimation = 79;
            npcDefinition.actions = new String[]{null, "Attack", null, null, null, null};
            npcDefinition.combatLevel = 135;
            npcDefinition.ambient = 40;
            npcDefinition.contrast = 40;
            npcDefinition.recolorToFind = new short[]{0, (short) 43105, (short) 43098, (short) 43107, 163, (short) 43090, 5219, (short) 38119, (short) 43086};
            npcDefinition.recolorToReplace = new short[]{(short) 689484, (short) 689484, (short) 689484, (short) 689484, (short) 689484, (short) 689484, (short) 689484, (short) 689484, (short) 689484};

        }
        if (id == 9019) {
            npcDefinition.name = "Mending Superior revenant";
            npcDefinition.models = new int[]{34163};
            npcDefinition.occupied_tiles = 5;
            npcDefinition.standingAnimation = 90;
            npcDefinition.walkingAnimation = 79;
            npcDefinition.rotate180Animation = 79;
            npcDefinition.quarterClockwiseTurnAnimation = 79;
            npcDefinition.quarterAnticlockwiseTurnAnimation = 79;
            npcDefinition.actions = new String[]{null, "Attack", null, null, null, null};
            npcDefinition.combatLevel = 135;
            npcDefinition.ambient = 40;
            npcDefinition.contrast = 40;
            npcDefinition.recolorToFind = new short[]{0, (short) 43105, (short) 43098, (short) 43107, 163, (short) (short) 43090, 5219, (short) 38119, (short) 43086};
            npcDefinition.recolorToReplace = new short[]{(short) 86933, (short) 86933, (short) 86933, (short) 86933, (short) 86933, (short) 86933, (short) 86933, (short) 86933, (short) 86933};
        }
        if (id == 2616) {
            npcDefinition.actions = new String[]{null, "Attack", null, null, null, null};
            npcDefinition.name = "Mending Revenant Ork";
            npcDefinition.models = new int[]{34154};
            npcDefinition.occupied_tiles = 3;
            npcDefinition.standingAnimation = 4318;
            npcDefinition.walkingAnimation = 4319;
            npcDefinition.rotate180Animation = 4319;
            npcDefinition.quarterClockwiseTurnAnimation = 4319;
            npcDefinition.quarterAnticlockwiseTurnAnimation = 4319;
            npcDefinition.chatheadModels = new int[]{16214};
            npcDefinition.combatLevel = 105;
            npcDefinition.widthScale = 176;
            npcDefinition.heightScale = 176;
            npcDefinition.ambient = 60;
            npcDefinition.contrast = 60;
            npcDefinition.recolorToFind = new short[]{24, 16, 45, 33, 37, (short) 43100, (short) 43086, (short) 43109, 8286};
            npcDefinition.recolorToReplace = new short[]{(short) 86933, (short) 86933, (short) 86933, (short) 86933, (short) 86933, (short) 86933, (short) 86933, (short) 86933, (short) 86933};
        }

        if (id == 2617) {
            npcDefinition.name = "Mending Revenant knight";
            npcDefinition.models = new int[]{34145};
            npcDefinition.standingAnimation = 813;
            npcDefinition.walkingAnimation = 1205;
            npcDefinition.rotate180Animation = 1206;
            npcDefinition.quarterClockwiseTurnAnimation = 1207;
            npcDefinition.quarterAnticlockwiseTurnAnimation = 1208;
            npcDefinition.actions = new String[]{null, "Attack", null, null, null, null};
            npcDefinition.combatLevel = 126;
            npcDefinition.ambient = 75;
            npcDefinition.contrast = 75;
            npcDefinition.recolorToFind = new short[]{(short) 43080, (short) 43086, (short) 43072, 20, 272, (short) 43100, (short) 43057, (short) 43053, 4626, (short) 43045, (short) 43061, 47, 30, 30, 0};
            npcDefinition.recolorToReplace = new short[]{(short) 86933, (short) 86933, (short) 86933, (short) 86933, (short) 86933, (short) 86933, (short) 86933, (short) 86933, (short) 86933, (short) 86933, (short) 86933, (short) 86933, (short) 86933, (short) 86933, (short) 86933};
        }

        if (!dump) {
            NpcManager.unpack(id);
            CustomPets.unpack(id);
            CustomBosses.unpack(id);
            MemberNpcs.unpack(id);
        }

        return npcDefinition;
    }

    public static void dump() {
        File f = new File(System.getProperty("user.home") + "/Desktop/npcs.txt");
        try {
            f.createNewFile();
            BufferedWriter bf = new BufferedWriter(new FileWriter(f));
            for (int id = 0; id < NpcDefinition.totalNPCs; id++) {
                NpcDefinition definition = NpcDefinition.get(id);

                bf.write("case " + id + ":");
                bf.write(System.getProperty("line.separator"));
                if (definition.name == null || definition.name.equals("null") ||
                    definition.name.isEmpty()) continue;

                bf.write("definition[id].name = " + definition.name + ";");
                bf.write(System.getProperty("line.separator"));
                if (definition.models != null) {
                    bf.write("definition[id].model_id = new int[] "
                        + Arrays.toString(definition.models).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.occupied_tiles != 1) {
                    bf.write("definition[id].occupied_tiles = " + definition.occupied_tiles + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.standingAnimation != -1) {
                    bf.write("definition[id].standingAnimation = " + definition.standingAnimation + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.walkingAnimation != -1) {
                    bf.write("definition[id].walkingAnimation = " + definition.walkingAnimation + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.rotate180Animation != -1) {
                    bf.write("definition[id].halfTurnAnimation = " + definition.rotate180Animation + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.quarterClockwiseTurnAnimation != -1) {
                    bf.write("definition[id].quarterClockwiseTurnAnimation = " + definition.quarterClockwiseTurnAnimation + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.quarterAnticlockwiseTurnAnimation != -1) {
                    bf.write("definition[id].quarterAnticlockwiseTurnAnimation = " + definition.quarterAnticlockwiseTurnAnimation + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.actions != null) {
                    bf.write("definition[id].actions = new int[] "
                        + Arrays.toString(definition.actions).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.recolorToFind != null) {
                    bf.write("definition[id].src_color = new int[] "
                        + Arrays.toString(definition.recolorToFind).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.recolorToReplace != null) {
                    bf.write("definition[id].dst_color = new int[] "
                        + Arrays.toString(definition.recolorToReplace).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.retextureToFind != null) {
                    bf.write("definition[id].src_texture = new int[] "
                        + Arrays.toString(definition.retextureToFind).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.retextureToReplace != null) {
                    bf.write("definition[id].dst_texture = new int[] "
                        + Arrays.toString(definition.retextureToReplace).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.chatheadModels != null) {
                    bf.write("definition[id].additionalModels = new int[] "
                        + Arrays.toString(definition.chatheadModels).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.combatLevel != -1) {
                    bf.write("definition[id].cmb_level = " + definition.combatLevel + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.widthScale != 128) {
                    bf.write("definition[id].model_scale_xy = " + definition.widthScale + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.heightScale != 128) {
                    bf.write("definition[id].model_scale_z = " + definition.heightScale + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (!definition.hasRenderPriority) {
                    bf.write("definition[id].render_priority = " + definition.hasRenderPriority + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.ambient != -1) {
                    bf.write("definition[id].ambient = " + definition.ambient + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.contrast != -1) {
                    bf.write("definition[id].contrast = " + definition.contrast + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.headIcon != -1) {
                    bf.write("definition[id].headIcon = " + definition.headIcon + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.rotationSpeed != 32) {
                    bf.write("definition[id].rotation = " + definition.rotationSpeed + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.varbitId != -1) {
                    bf.write("definition[id].varbit = " + definition.varbitId + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.varpIndex != -1) {
                    bf.write("definition[id].varp = " + definition.varpIndex + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.configs != null) {
                    bf.write("definition[id].configs = new int[] "
                        + Arrays.toString(definition.configs).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                bf.write("break;");
                bf.write(System.getProperty("line.separator"));
                bf.write(System.getProperty("line.separator"));
            }
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copy(NpcDefinition definition, int id) {
        NpcDefinition copy = NpcDefinition.get(id);
        definition.occupied_tiles = copy.occupied_tiles;
        definition.rotationSpeed = copy.rotationSpeed;
        definition.standingAnimation = copy.standingAnimation;
        definition.walkingAnimation = copy.walkingAnimation;
        definition.rotate180Animation = copy.rotate180Animation;
        definition.quarterClockwiseTurnAnimation = copy.quarterClockwiseTurnAnimation;
        definition.quarterAnticlockwiseTurnAnimation = copy.quarterAnticlockwiseTurnAnimation;
        definition.varbitId = copy.varbitId;
        definition.varpIndex = copy.varpIndex;
        definition.combatLevel = copy.combatLevel;
        definition.name = copy.name;
        definition.description = copy.description;
        definition.headIcon = copy.headIcon;
        definition.isClickable = copy.isClickable;
        definition.ambient = copy.ambient;
        definition.heightScale = copy.heightScale;
        definition.widthScale = copy.widthScale;
        definition.isMinimapVisible = copy.isMinimapVisible;
        definition.contrast = copy.contrast;
        definition.actions = new String[copy.actions.length];
        System.arraycopy(copy.actions, 0, definition.actions, 0, definition.actions.length);
        definition.models = new int[copy.models.length];
        System.arraycopy(copy.models, 0, definition.models, 0, definition.models.length);
        definition.hasRenderPriority = copy.hasRenderPriority;
    }

    public Model get_dialogue_model() {
        if (configs != null) {
            NpcDefinition entityDef = get_configs();
            if (entityDef == null)
                return null;
            else
                return entityDef.get_dialogue_model();
        }
        if (chatheadModels == null)
            return null;
        boolean cached = false;
        for (int index = 0; index < chatheadModels.length; index++)
            if (!Model.cached(chatheadModels[index]))
                cached = true;

        if (cached)
            return null;
        Model[] head_model = new Model[chatheadModels.length];
        for (int index = 0; index < chatheadModels.length; index++)
            head_model[index] = Model.get(chatheadModels[index]);

        Model dialogue_model;
        if (head_model.length == 1)
            dialogue_model = head_model[0];
        else
            dialogue_model = new Model(head_model.length, head_model);

        if (modelCustomColor > 0) {
            dialogue_model.completelyRecolor(modelCustomColor);
        }
        if (modelCustomColor2 != 0) {
            dialogue_model.shadingRecolor(modelCustomColor2);
        }
        if (modelCustomColor3 != 0) {
            dialogue_model.shadingRecolor2(modelCustomColor3);
        }
        if (modelCustomColor4 != 0) {
            dialogue_model.shadingRecolor4(modelCustomColor4);
        }
        if (modelSetColor != 0) {
            dialogue_model.shadingRecolor3(modelSetColor);
        }

        if (recolorToFind != null) {
            for (int k = 0; k < recolorToFind.length; k++)
                dialogue_model.recolor(recolorToFind[k], recolorToReplace[k]);
        }

        if (retextureToFind != null) {
            for (int index = 0; index < retextureToFind.length; index++)
                dialogue_model.retexture(retextureToFind[index], retextureToReplace[index]);
        }

        return dialogue_model;
    }

    public NpcDefinition get_configs() {
        try {
            int j = -1;
            if (varbitId != -1) {
                VariableBits varBit = VariableBits.cache[varbitId];
                int k = varBit.configId;
                int l = varBit.leastSignificantBit;
                int i1 = varBit.mostSignificantBit;
                int j1 = Client.BIT_MASKS[i1 - l];
                j = clientInstance.settings[k] >> l & j1;
            } else if (varpIndex != -1)
                j = clientInstance.settings[varpIndex];
            if (j < 0 || j >= configs.length || configs[j] == -1)
                return null;
            else
                return get(configs[j]);
        } catch (Exception e) {
            System.err.println("There was an error getting configs for NPC " + id);
            e.printStackTrace();
            return null;
        }
    }

    public static void clear() {
        model_cache = null;
        offsets = null;
        cache = null;
        buffer = null;
    }

    public Model get_animated_model(int animation, int current, int[] label) {
        if (configs != null) {
            final NpcDefinition def = get_configs();
            if (def == null) {
                return null;
            } else {
                try {
                    return def.get_animated_model(animation, current, label);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Model model = (Model) model_cache.get(interfaceType);
        if (model == null) {
            boolean cached = false;
            if (models == null) {
                return null;
            }
            for (int i : models) {
                if (!Model.cached(i)) {
                    cached = true;
                }
            }
            if (cached) {
                return null;
            }
            final Model[] models = new Model[this.models.length];
            for (int index = 0; index < this.models.length; index++) {
                models[index] = Model.get(this.models[index]);
            }
            if (models.length == 1) {
                model = models[0];
            } else {
                model = new Model(models.length, models);
            }
            if (recolorToFind != null) {
                for (int k1 = 0; k1 < recolorToFind.length; k1++) {
                    model.recolor(recolorToFind[k1], recolorToReplace[k1]);
                }
            }
            if (retextureToFind != null) {
                for (int index = 0; index < retextureToFind.length; index++)
                    model.retexture(retextureToFind[index], retextureToReplace[index]);
            }

            if (modelCustomColor > 0) {
                model.completelyRecolor(modelCustomColor);
            }
            if (modelCustomColor2 != 0) {
                model.shadingRecolor(modelCustomColor2);
            }
            if (modelCustomColor3 != 0) {
                model.shadingRecolor2(modelCustomColor3);
            }
            if (modelCustomColor4 != 0) {
                model.shadingRecolor4(modelCustomColor4);
            }
            if (modelSetColor != 0) {
                model.shadingRecolor3(modelSetColor);
            }

            model.generateBones();
            model.light(84 + ambient, 1000 + contrast, -90, -580, -90, true);
            model_cache.put(model, interfaceType);
        }
        final Model animated_model = Model.EMPTY_MODEL;
        animated_model.replace(model, SeqFrame.noAnimationInProgress(current) & SeqFrame.noAnimationInProgress(animation));

        if (current != -1 && animation != -1)
            animated_model.mix(label, animation, current);
        else if (current != -1) {
            animated_model.interpolate(current);
        }

        if (widthScale != 128 || heightScale != 128) {
            animated_model.scale(widthScale, heightScale, widthScale);
        }

        animated_model.calc_diagonals();
        animated_model.faceGroups = null;
        animated_model.groupedTriangleLabels = null;
        if (occupied_tiles == 1) {
            animated_model.singleTile = true;
        }
        return animated_model;
    }

    public Model getAnimatedModelSkeletal(SeqDefinition primary, SeqDefinition secondary, int primaryTick, int secondaryTick) {
        if (configs != null) {
            final NpcDefinition def = get_configs();
            if (def == null) {
                return null;
            } else {
                return getAnimatedModelSkeletal(primary, secondary, primaryTick, secondaryTick);
            }
        }
        Model model = (Model) model_cache.get(interfaceType);
        if (model == null) {
            boolean cached = false;
            if (models == null) {
                return null;
            }
            for (int i : models) {
                if (!Model.cached(i)) {
                    cached = true;
                }
            }
            if (cached) {
                return null;
            }
            final Model[] models = new Model[this.models.length];
            for (int index = 0; index < this.models.length; index++) {
                models[index] = Model.get(this.models[index]);
            }
            if (models.length == 1) {
                model = models[0];
            } else {
                model = new Model(models.length, models);
            }
            if (recolorToFind != null) {
                for (int k1 = 0; k1 < recolorToFind.length; k1++) {
                    model.recolor(recolorToFind[k1], recolorToReplace[k1]);
                }
            }
            if (retextureToFind != null) {
                for (int index = 0; index < retextureToFind.length; index++)
                    model.retexture(retextureToFind[index], retextureToReplace[index]);
            }

            if (modelCustomColor > 0) {
                model.completelyRecolor(modelCustomColor);
            }
            if (modelCustomColor2 != 0) {
                model.shadingRecolor(modelCustomColor2);
            }
            if (modelCustomColor3 != 0) {
                model.shadingRecolor2(modelCustomColor3);
            }
            if (modelCustomColor4 != 0) {
                model.shadingRecolor4(modelCustomColor4);
            }
            if (modelSetColor != 0) {
                model.shadingRecolor3(modelSetColor);
            }

            model.generateBones();
            model.light(84 + ambient, 1000 + contrast, -90, -580, -90, true);
            model_cache.put(model, interfaceType);
        }
        final Model animated_model = Model.EMPTY_MODEL;
        animated_model.replace(model, false);
        if (primary != null && secondary != null) {
            animated_model.playSkeletalDouble(primary, secondary, primaryTick, secondaryTick);
        } else {
            if (primary != null) {
                animated_model.playSkeletal(primary, primaryTick);
            } else if (secondary != null) {
                animated_model.playSkeletal(secondary, secondaryTick);
            }
        }

        if (widthScale != 128 || heightScale != 128) {
            animated_model.scale(widthScale, heightScale, widthScale);
        }

        animated_model.calc_diagonals();
        animated_model.faceGroups = null;
        animated_model.groupedTriangleLabels = null;
        if (occupied_tiles == 1) {
            animated_model.singleTile = true;
        }
        return animated_model;
    }

    void decode(Buffer buffer) {
        while (true) {
            int op = buffer.readUnsignedByte();
            if (op == 0)
                break;

            decodeValues(op, buffer);
        }
    }

    private void decodeValues(int opcode, Buffer stream)
    {
        int length;
        int index;
        if (opcode == 1)
        {
            length = stream.readUnsignedByte();
            models = new int[length];

            for (index = 0; index < length; ++index)
            {
                models[index] = stream.readUnsignedShort();
            }
        }
        else if (opcode == 2)
        {
            name = stream.readStringCp1252NullTerminated();
        }
        else if (opcode == 12)
        {
            occupied_tiles = stream.readUnsignedByte();
        }
        else if (opcode == 13)
        {
            standingAnimation = stream.readUnsignedShort();
        }
        else if (opcode == 14)
        {
            walkingAnimation = stream.readUnsignedShort();
        }
        else if (opcode == 15)
        {
            rotateLeftAnimation = stream.readUnsignedShort();
        }
        else if (opcode == 16)
        {
            rotateRightAnimation = stream.readUnsignedShort();
        }
        else if (opcode == 17)
        {
            walkingAnimation = stream.readUnsignedShort();
            rotate180Animation = stream.readUnsignedShort();
            rotateLeftAnimation = stream.readUnsignedShort();
            rotateRightAnimation = stream.readUnsignedShort();
            if (walkingAnimation == 65535) {
                walkingAnimation = -1;
            }

            if (rotate180Animation == 65535) {
                rotate180Animation = -1;
            }
            if (quarterClockwiseTurnAnimation == 65535) {
                quarterClockwiseTurnAnimation = -1;
            }
            if (quarterAnticlockwiseTurnAnimation == 65535) {
                quarterAnticlockwiseTurnAnimation = -1;
            }
        }
        else if (opcode == 18)
        {
            category = stream.readUnsignedShort();
        }
        else if (opcode >= 30 && opcode < 35)
        {
            actions[opcode - 30] = stream.readStringCp1252NullTerminated();
            if (actions[opcode - 30].equalsIgnoreCase("Hidden"))
            {
                actions[opcode - 30] = null;
            }
        }
        else if (opcode == 40)
        {
            length = stream.readUnsignedByte();
            recolorToFind = new short[length];
            recolorToReplace = new short[length];

            for (index = 0; index < length; ++index)
            {
                recolorToFind[index] = (short) stream.readUnsignedShort();
                recolorToReplace[index] = (short) stream.readUnsignedShort();
            }

        }
        else if (opcode == 41)
        {
            length = stream.readUnsignedByte();
            retextureToFind = new short[length];
            retextureToReplace = new short[length];

            for (index = 0; index < length; ++index)
            {
                retextureToFind[index] = (short) stream.readUnsignedShort();
                retextureToReplace[index] = (short) stream.readUnsignedShort();
            }

        }
        else if (opcode == 60)
        {
            length = stream.readUnsignedByte();
            chatheadModels = new int[length];

            for (index = 0; index < length; ++index)
            {
                chatheadModels[index] = stream.readUnsignedShort();
            }
        }
        else if (opcode == 74)
        {
            stats[0] = stream.readUnsignedShort();
        }
        else if (opcode == 75)
        {
            stats[1] = stream.readUnsignedShort();
        }
        else if (opcode == 76)
        {
            stats[2] = stream.readUnsignedShort();
        }
        else if (opcode == 77)
        {
            stats[3] = stream.readUnsignedShort();
        }
        else if (opcode == 78)
        {
            stats[4] = stream.readUnsignedShort();
        }
        else if (opcode == 79)
        {
            stats[5] = stream.readUnsignedShort();
        }
        else if (opcode == 93)
        {
            isMinimapVisible = false;
        }
        else if (opcode == 95)
        {
            combatLevel = stream.readUnsignedShort();
        }
        else if (opcode == 97)
        {
            widthScale = stream.readUnsignedShort();
        }
        else if (opcode == 98)
        {
            heightScale = stream.readUnsignedShort();
        }
        else if (opcode == 99)
        {
            hasRenderPriority = true;
        }
        else if (opcode == 100)
        {
            ambient = stream.readByte();
        }
        else if (opcode == 101)
        {
            contrast = stream.readByte();
        }
        else if (opcode == 102)
        {
            if (!rev210HeadIcons)
            {
                headIconArchiveIds = new int[]{defaultHeadIconArchive};
                headIconSpriteIndex = new short[]{(short) stream.readUnsignedShort()};
            }
            else
            {
                int bitfield = stream.readUnsignedByte();
                int len = 0;
                for (int var5 = bitfield; var5 != 0; var5 >>= 1)
                {
                    ++len;
                }

                headIconArchiveIds = new int[len];
                headIconSpriteIndex = new short[len];

                for (int i = 0; i < len; i++)
                {
                    if ((bitfield & 1 << i) == 0)
                    {
                        headIconArchiveIds[i] = -1;
                        headIconSpriteIndex[i] = -1;
                    }
                    else
                    {
                        headIconArchiveIds[i] = stream.readBigSmart2();
                        headIconSpriteIndex[i] = (short) stream.readUnsignedShortSmartMinusOne();
                    }
                }
            }
        }
        else if (opcode == 103)
        {
            rotationSpeed = stream.readUnsignedShort();
        }
        else if (opcode == 106)
        {
            varbitId = stream.readUnsignedShort();
            if (varbitId == 65535)
            {
                varbitId = -1;
            }

            varpIndex = stream.readUnsignedShort();
            if (varpIndex == 65535)
            {
                varpIndex = -1;
            }

            length = stream.readUnsignedByte();
            configs = new int[length + 2];

            for (index = 0; index <= length; ++index)
            {
                configs[index] = stream.readUnsignedShort();
                if (configs[index] == 65535)
                {
                    configs[index] = -1;
                }
            }

            configs[length + 1] = -1;

        }
        else if (opcode == 107)
        {
            isClickable = false;
        }
        else if (opcode == 109)
        {
            rotationFlag = false;
        }
        else if (opcode == 111)
        {
            // removed in 220
            isFollower = true;
            lowPriorityFollowerOps = true;
        }
        else if (opcode == 114)
        {
            runAnimation = stream.readUnsignedShort();
        }
        else if (opcode == 115)
        {
            runAnimation = stream.readUnsignedShort();
            runRotate180Animation = stream.readUnsignedShort();
            runRotateLeftAnimation = stream.readUnsignedShort();
            runRotateRightAnimation = stream.readUnsignedShort();
        }
        else if (opcode == 116)
        {
            crawlAnimation = stream.readUnsignedShort();
        }
        else if (opcode == 117)
        {
            crawlAnimation = stream.readUnsignedShort();
            crawlRotate180Animation = stream.readUnsignedShort();
            crawlRotateLeftAnimation = stream.readUnsignedShort();
            crawlRotateRightAnimation = stream.readUnsignedShort();
        }
        else if (opcode == 118)
        {
            varbitId = stream.readUnsignedShort();
            if (varbitId == 65535)
            {
                varbitId = -1;
            }

            varpIndex = stream.readUnsignedShort();
            if (varpIndex == 65535)
            {
                varpIndex = -1;
            }

            int var = stream.readUnsignedShort();
            if (var == 0xFFFF)
            {
                var = -1;
            }

            length = stream.readUnsignedByte();
            configs = new int[length + 2];

            for (index = 0; index <= length; ++index)
            {
                configs[index] = stream.readUnsignedShort();
                if (configs[index] == '\uffff')
                {
                    configs[index] = -1;
                }
            }

            configs[length + 1] = var;
        }
        else if (opcode == 122)
        {
            isFollower = true;
        }
        else if (opcode == 123)
        {
            lowPriorityFollowerOps = true;
        }
        else if (opcode == 124)
        {
            height = stream.readUnsignedShort();
        }
        else if (opcode == 249)
        {
            length = stream.readUnsignedByte();

            params = new HashMap<>(length);

            for (int i = 0; i < length; i++)
            {
                boolean isString = stream.readUnsignedByte() == 1;
                int key = stream.read24BitInt();
                Object value;

                if (isString)
                {
                    value = stream.readStringCp1252NullTerminated();
                }

                else
                {
                    value = stream.readInt();
                }

                params.put(key, value);
            }
        }
        else
        {
            log.warn("Unrecognized opcode {}", opcode);
        }
    }

    public boolean rotationFlag = true;
    public int rotateLeftAnimation = -1;
    public int rotateRightAnimation = -1;
    public boolean isFollower;
    private int category;

    public NpcDefinition() {
        actions = new String[5];
        modelCustomColor = 0;
        modelCustomColor2 = 0;
        modelCustomColor3 = 0;
        modelCustomColor4 = 0;
        modelSetColor = 0;
        quarterAnticlockwiseTurnAnimation = -1;
        varbitId = -1;
        rotate180Animation = -1;
        varpIndex = -1;
        combatLevel = -1;
        anInt64 = 1834;
        walkingAnimation = -1;
        occupied_tiles = 1;
        headIcon = -1;
        standingAnimation = -1;
        interfaceType = -1L;
        rotationSpeed = 32;
        quarterClockwiseTurnAnimation = -1;
        isInteractable = true;
        isClickable = true;
        heightScale = 128;
        isMinimapVisible = true;
        widthScale = 128;
        hasRenderPriority = false;
    }

    public boolean lowPriorityFollowerOps;
    public int modelCustomColor;
    public int modelCustomColor2;
    public int modelCustomColor3;
    public int modelCustomColor4;
    public int modelSetColor;
    public int quarterAnticlockwiseTurnAnimation;
    public static int cache_index;
    public int varbitId;
    public int rotate180Animation;
    public int varpIndex;
    public static Buffer buffer;
    public int combatLevel;
    public boolean largeHpBar;
    public final int anInt64;
    public String name;
    public String[] actions;
    public int walkingAnimation;
    public int occupied_tiles;
    public short[] recolorToReplace;
    public static int[] offsets;
    public int[] chatheadModels;
    public int headIcon;
    public short[] retextureToFind;
    public short[] retextureToReplace;
    public short[] recolorToFind;
    public int standingAnimation;
    public long interfaceType;
    public int rotationSpeed;
    public static NpcDefinition[] cache;
    public static Client clientInstance;
    public int quarterClockwiseTurnAnimation;
    public boolean isClickable;
    public int ambient;
    public int heightScale;
    public boolean isMinimapVisible;
    public boolean pet;
    public int[] configs;
    public String description;
    public int widthScale;
    public int contrast;
    public boolean hasRenderPriority;
    public int[] models;

    public boolean clickable;
    public int[] headIconArchiveIds;
    public short[] headIconSpriteIndex;
    public int runRotate180Animation = -1;
    public int runRotateLeftAnimation = -1;
    public int runRotateRightAnimation = -1;
    public int crawlAnimation = -1;
    public int crawlRotate180Animation = -1;
    public int runAnimation = -1;
    public int crawlRotateLeftAnimation = -1;
    public int crawlRotateRightAnimation = -1;
    public int defaultHeadIconArchive = -1;
    public int interfaceZoom = 0;
    public int id;
    public static TempCache model_cache = new TempCache(30);
    private Map<Integer, Object> params = null;
    public static final int REV_210_NPC_ARCHIVE_REV = 1493;
    private boolean rev210HeadIcons = true;
    public boolean isInteractable = true;
    public int size = 1;

    @Override
    public void setIndex(int npcIndex) {

    }

    @Override
    public HeadIcon getOverheadIcon() {
        return null;
    }

    @Override
    public int getIntValue(int paramID) {
        return 0;
    }

    @Override
    public void setValue(int paramID, int value) {

    }

    @Override
    public String getStringValue(int paramID) {
        return null;
    }

    @Override
    public void setValue(int paramID, String value) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int[] getModels() {
        return new int[0];
    }

    @Override
    public String[] getActions() {
        return new String[0];
    }

    @Override
    public boolean isSmoothWalk() {
        return false;
    }

    @Override
    public boolean isFollower() {
        return false;
    }

    @Override
    public boolean isInteractible() {
        return false;
    }

    @Override
    public boolean isMinimapVisible() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public int getCombatLevel() {
        return 0;
    }

    @Override
    public int[] getConfigs() {
        return new int[0];
    }

    @Override
    public RSNPCComposition transform() {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public int getRsOverheadIcon() {
        return 0;
    }

    @Override
    public RSIterableNodeHashTable getParams() {
        return null;
    }

    @Override
    public void setParams(IterableHashTable params) {

    }

    @Override
    public void setParams(RSIterableNodeHashTable params) {

    }
}
