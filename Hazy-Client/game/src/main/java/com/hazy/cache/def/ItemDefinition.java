package com.hazy.cache.def;

import com.hazy.ClientConstants;
import com.hazy.cache.Archive;
import com.hazy.cache.BufferExt;
import com.hazy.cache.def.impl.items.CustomItems;
import com.hazy.cache.factory.ItemSpriteFactory;
import com.hazy.collection.TempCache;
import com.hazy.entity.model.Model;
import com.hazy.io.Buffer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import static com.hazy.util.ItemIdentifiers.*;

@Slf4j
public final class ItemDefinition {


    public static void init(Archive archive) {
        data_buffer = new Buffer(archive.get("obj.dat"));
        final Buffer idxBuffer = new Buffer(archive.get("obj.idx"));

        length = idxBuffer.readUnsignedShort();
        pos = new int[length + 30_000];

        int offset = 0;
        for (int i = 0; i < length; i++) {
            final int size = idxBuffer.readUnsignedShort();
            if (size == 65535) break;
            pos[i] = offset;
            offset += size;
        }
        cache = new ItemDefinition[10];

        for (int _ctr = 0; _ctr < 10; _ctr++) {
            cache[_ctr] = new ItemDefinition();
        }
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
        if (opcode == 1)
        {
            inventoryModel = stream.readUnsignedShort();
        }
        else if (opcode == 2)
        {
            name = stream.readStringCp1252NullTerminated();
        }
        else if (opcode == 3)
        {
            examine = stream.readStringCp1252NullTerminated();
        }
        else if (opcode == 4)
        {
            zoom2d = stream.readUnsignedShort();
        }
        else if (opcode == 5)
        {
            xan2d = stream.readUnsignedShort();
        }
        else if (opcode == 6)
        {
            yan2d = stream.readUnsignedShort();
        }
        else if (opcode == 7)
        {
            xOffset2d = stream.readUnsignedShort();
            if (xOffset2d > 32767)
            {
                xOffset2d -= 65536;
            }
        }
        else if (opcode == 8)
        {
            yOffset2d = stream.readUnsignedShort();
            if (yOffset2d > 32767)
            {
                yOffset2d -= 65536;
            }
        }
        else if (opcode == 9)
        {
            unknown1 = stream.readStringCp1252NullTerminated();
        }
        else if (opcode == 11)
        {
            stackable = true;
        }
        else if (opcode == 12)
        {
            cost = stream.readInt();
        }
        else if (opcode == 13)
        {
            wearPos1 = stream.readByte();
        }
        else if (opcode == 14)
        {
            wearPos2 = stream.readByte();
        }
        else if (opcode == 16)
        {
            members = true;
        }
        else if (opcode == 23)
        {
            maleModel0 = stream.readUnsignedShort();
            maleOffset = stream.readUnsignedByte();
        }
        else if (opcode == 24)
        {
            maleModel1 = stream.readUnsignedShort();
        }
        else if (opcode == 25)
        {
            femaleModel0 = stream.readUnsignedShort();
            femaleOffset = stream.readUnsignedByte();
        }
        else if (opcode == 26)
        {
            femaleModel1 = stream.readUnsignedShort();
        }
        else if (opcode == 27)
        {
            wearPos3 = stream.readByte();
        }
        else if (opcode >= 30 && opcode < 35)
        {
            options[opcode - 30] = stream.readStringCp1252NullTerminated();
            if (options[opcode - 30].equalsIgnoreCase("Hidden"))
            {
                options[opcode - 30] = null;
            }
        }
        else if (opcode >= 35 && opcode < 40)
        {
            if (interfaceOptions == null)
                interfaceOptions = new String[5];
            interfaceOptions[opcode - 35] = stream.readStringCp1252NullTerminated();
        }
        else if (opcode == 40)
        {
            int var5 = stream.readUnsignedByte();
            colorFind = new short[var5];
            colorReplace = new short[var5];

            for (int var4 = 0; var4 < var5; ++var4)
            {
                colorFind[var4] = (short) stream.readUnsignedShort();
                colorReplace[var4] = (short) stream.readUnsignedShort();
            }

        }
        else if (opcode == 41)
        {
            int var5 = stream.readUnsignedByte();
            textureFind = new short[var5];
            textureReplace = new short[var5];

            for (int var4 = 0; var4 < var5; ++var4)
            {
                textureFind[var4] = (short) stream.readUnsignedShort();
                textureReplace[var4] = (short) stream.readUnsignedShort();
            }

        }
        else if (opcode == 42)
        {
            shiftClickDropIndex = stream.readByte();
        }
        else if (opcode == 43)
        {
            int opId = stream.readUnsignedByte();
            if (subops == null)
            {
                subops = new String[5][];
            }

            boolean valid = opId >= 0 && opId < 5;
            if (valid && subops[opId] == null)
            {
                subops[opId] = new String[20];
            }

            while (true)
            {
                int subopId = stream.readUnsignedByte() - 1;
                if (subopId == -1)
                {
                    break;
                }

                String op = stream.readStringCp1252NullTerminated();
                if (valid && subopId >= 0 && subopId < 20)
                {
                    subops[opId][subopId] = op;
                }
            }
        }
        else if (opcode == 65)
        {
            isTradeable = true;
        }
        else if (opcode == 75)
        {
            weight = stream.readShort();
        }
        else if (opcode == 78)
        {
            maleModel2 = stream.readUnsignedShort();
        }
        else if (opcode == 79)
        {
            femaleModel2 = stream.readUnsignedShort();
        }
        else if (opcode == 90)
        {
            maleHeadModel = stream.readUnsignedShort();
        }
        else if (opcode == 91)
        {
            femaleHeadModel = stream.readUnsignedShort();
        }
        else if (opcode == 92)
        {
            maleHeadModel2 = stream.readUnsignedShort();
        }
        else if (opcode == 93)
        {
            femaleHeadModel2 = stream.readUnsignedShort();
        }
        else if (opcode == 94)
        {
            category = stream.readUnsignedShort();
        }
        else if (opcode == 95)
        {
            zan2d = stream.readUnsignedShort();
        }
        else if (opcode == 97)
        {
            notedID = stream.readUnsignedShort();
        }
        else if (opcode == 98)
        {
            notedTemplate = stream.readUnsignedShort();
        }
        else if (opcode >= 100 && opcode < 110)
        {
            if (countObj == null)
            {
                countObj = new int[10];
                countCo = new int[10];
            }

            countObj[opcode - 100] = stream.readUnsignedShort();
            countCo[opcode - 100] = stream.readUnsignedShort();
        }
        else if (opcode == 110)
        {
            resizeX = stream.readUnsignedShort();
        }
        else if (opcode == 111)
        {
            resizeY = stream.readUnsignedShort();
        }
        else if (opcode == 112)
        {
            resizeZ = stream.readUnsignedShort();
        }
        else if (opcode == 113)
        {
            ambient = stream.readByte();
        }
        else if (opcode == 114)
        {
            contrast = stream.readByte();
        }
        else if (opcode == 115)
        {
            team = stream.readUnsignedByte();
        }
        else if (opcode == 139)
        {
            boughtId = stream.readUnsignedShort();
        }
        else if (opcode == 140)
        {
            boughtTemplateId = stream.readUnsignedShort();
        }
        else if (opcode == 148)
        {
            placeholderId = stream.readUnsignedShort();
        }
        else if (opcode == 149)
        {
            placeholderTemplateId = stream.readUnsignedShort();
        }
        else if (opcode == 249)
        {
            int length = stream.readUnsignedByte();

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

    private void post() {
        if (stackable == true) {
            weight = 0;
        }
    }

    private int shiftClickDropIndex = -2;
    private int category;
    public int placeholderId;
    public int placeholderTemplateId;
    public HashMap<Integer, Object> params;

    public void set_defaultsCustom() {
        resizeX = 128;
        resizeY = 128;
        resizeZ = 128;
    }

    public static void copyInventory(ItemDefinition itemDef, int id) {
        ItemDefinition copy = ItemDefinition.get(id);
        itemDef.inventoryModel = copy.inventoryModel;
        itemDef.zoom2d = copy.zoom2d;
        itemDef.xan2d = copy.xan2d;
        itemDef.yan2d = copy.yan2d;
        itemDef.zan2d = copy.zan2d;
        itemDef.resizeX = copy.resizeX;
        itemDef.resizeY = copy.resizeY;
        itemDef.resizeZ = copy.resizeZ;
        itemDef.xOffset2d = copy.xOffset2d;
        itemDef.yOffset2d = copy.yOffset2d;
        itemDef.interfaceOptions = copy.interfaceOptions;
        itemDef.cost = copy.cost;
        itemDef.stackable = copy.stackable;
        if (itemDef.textureFind != null) itemDef.textureFind = copy.textureFind;
        if (itemDef.textureReplace != null) itemDef.textureReplace = copy.textureReplace;
        if (itemDef.colorFind != null) itemDef.colorFind = copy.colorFind;
    }

    public static void copyEquipment(ItemDefinition itemDef, int id) {
        ItemDefinition copy = ItemDefinition.get(id);
        itemDef.maleModel0 = copy.maleModel0;
        itemDef.maleModel1 = copy.maleModel1;
        itemDef.femaleModel0 = copy.femaleModel0;
        itemDef.femaleModel1 = copy.femaleModel1;
        itemDef.maleOffset = copy.maleOffset;
        itemDef.femaleOffset = copy.femaleOffset;
    }

    public static void printStatement(final String text) {
        System.out.println(text + ";");
    }

    public static void printDefinitions(final ItemDefinition definition) {
        printStatement("definition.name = \"" + definition.name + "\"");
        printStatement("definition.model_zoom = " + definition.zoom2d);
        printStatement("definition.rotation_y = " + definition.xan2d);
        printStatement("definition.rotation_x = " + definition.yan2d);
        printStatement("definition.translate_x = " + definition.xOffset2d);
        printStatement("definition.translate_y = " + definition.yOffset2d);
        printStatement("definition.inventory_model = " + definition.inventoryModel);
        printStatement("definition.male_equip_main = " + definition.maleModel0);
        printStatement("definition.female_equip_main = " + definition.femaleModel0);
        printStatement("definition.color_to_replace = " + Arrays.toString(definition.colorFind));
        printStatement("definition.color_to_replace_with = " + Arrays.toString(definition.colorReplace));
    }

    public static void dump() {
        File f = new File(System.getProperty("user.home") + "/Desktop/items.txt");
        try {
            f.createNewFile();
            BufferedWriter bf = new BufferedWriter(new FileWriter(f));
            for (int id = 20000; id <= 30000; id++) {
                ItemDefinition definition = ItemDefinition.get(id);

                bf.write("case " + id + ":");
                bf.write(System.getProperty("line.separator"));
                if (definition.name == null || definition.name.equals("null") ||
                    definition.name.isEmpty()) continue;

                bf.write("definition[id].name = " + definition.name + ";");
                bf.write(System.getProperty("line.separator"));
                if (definition.inventoryModel != 0) {
                    bf.write("definition[id].inventory_model = " + definition.inventoryModel + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.colorFind != null) {
                    bf.write("definition[id].color_to_replace = new int[] "
                        + Arrays.toString(definition.colorFind).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.colorReplace != null) {
                    bf.write("definition[id].color_to_replace_with = new int[] "
                        + Arrays.toString(definition.colorReplace).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.textureFind != null) {
                    bf.write("definition[id].src_texture = new int[] "
                        + Arrays.toString(definition.textureFind).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.textureReplace != null) {
                    bf.write("definition[id].dst_texture = new int[] "
                        + Arrays.toString(definition.textureReplace).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.zoom2d != 2000) {
                    bf.write("definition[id].model_zoom = " + definition.zoom2d + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.xan2d != 0) {
                    bf.write("definition[id].rotation_y = " + definition.xan2d + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.yan2d != 0) {
                    bf.write("definition[id].rotation_x = " + definition.yan2d + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.zan2d != 0) {
                    bf.write("definition[id].rotation_z = " + definition.zan2d + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.xOffset2d != -1) {
                    bf.write("definition[id].translate_x = " + definition.xOffset2d + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.yOffset2d != -1) {
                    bf.write("definition[id].translate_y = " + definition.yOffset2d + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                bf.write("definition[id].stackable = " + definition.stackable + ";");
                bf.write(System.getProperty("line.separator"));
                if (definition.options != null) {
                    bf.write("definition[id].scene_actions = new int[] "
                        + Arrays.toString(definition.options).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.interfaceOptions != null) {
                    bf.write("definition[id].widget_actions = new int[] "
                        + Arrays.toString(definition.interfaceOptions).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.maleModel0 != -1) {
                    bf.write("definition[id].male_equip_main = " + definition.maleModel0 + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.maleModel1 != -1) {
                    bf.write("definition[id].male_equip_attachment = " + definition.maleModel1 + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.maleOffset != 0) {
                    bf.write("definition[id].male_equip_translate_y = " + definition.maleOffset + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.femaleModel0 != -1) {
                    bf.write("definition[id].female_equip_main = " + definition.femaleModel0 + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.femaleModel1 != -1) {
                    bf.write("definition[id].female_equip_attachment = " + definition.femaleModel1 + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.femaleOffset != 0) {
                    bf.write("definition[id].female_equip_translate_y = " + definition.femaleOffset + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.maleModel2 != -1) {
                    bf.write("definition[id].male_equip_emblem = " + definition.maleModel2 + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.femaleModel2 != -1) {
                    bf.write("definition[id].female_equip_emblem = " + definition.femaleModel2 + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.maleHeadModel != -1) {
                    bf.write("definition[id].male_dialogue_head = " + definition.maleHeadModel + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.maleHeadModel2 != -1) {
                    bf.write("definition[id].male_dialogue_headgear = " + definition.maleHeadModel2 + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.femaleHeadModel != -1) {
                    bf.write("definition[id].female_dialogue_head = " + definition.femaleHeadModel + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.femaleHeadModel2 != -1) {
                    bf.write("definition[id].female_dialogue_headgear = " + definition.femaleHeadModel2 + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.countObj != null) {
                    bf.write("definition[id].stack_variant_id = new int[] "
                        + Arrays.toString(definition.countObj).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.countCo != null) {
                    bf.write("definition[id].stack_variant_size = new int[] "
                        + Arrays.toString(definition.countCo).replace("[", "{").replace("]", "}") + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.notedID != -1) {
                    bf.write("definition[id].unnoted_item_id = " + definition.notedID + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.notedTemplate != -1) {
                    bf.write("definition[id].model_scale_xy = " + definition.notedTemplate + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.resizeX != 128) {
                    bf.write("definition[id].model_scale_x = " + definition.resizeX + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.resizeY != 128) {
                    bf.write("definition[id].model_scale_y = " + definition.resizeY + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.resizeZ != 128) {
                    bf.write("definition[id].model_scale_z = " + definition.resizeZ + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.ambient != 0) {
                    bf.write("definition[id].ambient = " + definition.ambient + ";");
                    bf.write(System.getProperty("line.separator"));
                }
                if (definition.contrast != 0) {
                    bf.write("definition[id].contrast = " + definition.contrast + ";");
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

    public static ItemDefinition get(int id) {
        if (id == -1)
            return null;

        for (int j = 0; j < 10; j++)
            if (cache[j].id == id)
                return cache[j];

        cache_index = (cache_index + 1) % 10;
        ItemDefinition def = cache[cache_index];
        data_buffer.pos = pos[id];
        def.id = id;
        def.set_defaults();
        def.decode(data_buffer);
        def.post();

        if (def.name != null && (def.name.contains("Max cape") || def.name.contains("max cape"))) {
            def.interfaceOptions = new String[]{null, "Wear", "Features", null, "Drop"};
        }

        if (def.name != null && (def.name.contains("slayer helmet") || def.name.contains("Slayer helmet"))) {
            def.interfaceOptions = new String[]{null, "Wear", null, "Disassemble", "Drop"};
        }

        if (id == 6199) {
            def.interfaceOptions = new String[]{"Quick-open", null, null, "Open", null};
        }

        if (id == 24225) {
            def.interfaceOptions = new String[]{null, "Wield", null, null, null};
        }

        CustomItems.unpack(id);

        if (def.notedTemplate != -1)
            def.set_noted_values();

        int[] items = {BABYDRAGON_BONES, DRAGON_BONES, WYVERN_BONES, DAGANNOTH_BONES, FLIPPERS, SLED_4084, FISHBOWL_HELMET, DIVING_APPARATUS, CHICKEN_WINGS_11020, CHICKEN_LEGS_11022, CHICKEN_FEET_11019, CHICKEN_HEAD_11021, FANCY_BOOTS, GAS_MASK, MIME_MASK_10629, FROG_MASK, LIT_BUG_LANTERN, SKELETON_BOOTS, SKELETON_GLOVES, SKELETON_MASK, SKELETON_LEGGINGS, SKELETON_SHIRT, LIT_BUG_LANTERN, FOX, CHICKEN, BONESACK, GRAIN_5607, RUBBER_CHICKEN, BUNNY_EARS};
        String name = def.name;
        if (name != null) {
            if (!name.startsWith("<col=65280>")) {
                for (int item_id : items) {
                    if (id == item_id) {
                        name = ("<col=65280>" + name);
                        break;
                    }
                }
            }
            if (!name.startsWith("<col=65280>")) {
                final String lowercase = name.toLowerCase();
                if ((lowercase.contains("pet")) || (lowercase.contains("3rd")) || (lowercase.contains("toxic")) || (lowercase.contains("occult")) || (lowercase.contains("anguish")) || (lowercase.contains("torture")) || (lowercase.contains("avernic")) || (lowercase.contains("serpentine")) || (lowercase.contains("tanzanite")) || (lowercase.contains("magma")) || (lowercase.contains("ancestral")) || (lowercase.contains("armadyl")) || (lowercase.contains("void"))
                    || (lowercase.contains("bandos")) || (lowercase.contains("pegasian")) || (lowercase.contains("primordial")) || (lowercase.contains("eternal")) || (lowercase.contains("partyhat")) || (lowercase.contains("staff of light")) || (lowercase.contains("infernal")) || (lowercase.contains("slayer helm")) || (lowercase.contains("dragon hunter")) || (lowercase.contains("spectral")) || (lowercase.contains("ballista"))
                    || (lowercase.contains("justiciar")) || (lowercase.contains("dragon claws")) || (lowercase.contains("bulwark")) || (lowercase.contains("dragon warhammer")) || (lowercase.contains("blessed sword")) || (lowercase.contains("godsword")) || (lowercase.contains("ward")) || (lowercase.contains("wyvern shield")) || (lowercase.contains("morrigan")) || (lowercase.contains("vesta")) || (lowercase.contains("zuriel"))
                    || (lowercase.contains("statius")) || (lowercase.contains("dragon crossbow")) || (lowercase.contains("abyssal dagger")) || (lowercase.contains("ghrazi")) || (lowercase.contains("elder maul")) || (lowercase.contains("tormented")) || (lowercase.contains("infinity")) || (lowercase.contains("dragonfire")) || (lowercase.contains("blessed spirit shield")) || (lowercase.contains("of the dead")) || (lowercase.contains("ice arrow"))
                    || (lowercase.contains("dragon javelin")) || (lowercase.contains("dragon knife")) || (lowercase.contains("dragon thrownaxe")) || (lowercase.contains("abyssal tentacle")) || (lowercase.contains("dark bow")) || (lowercase.contains("fremennik kilt")) || (lowercase.contains("spiked manacles")) || (lowercase.contains("fury")) || (lowercase.contains("dragon boots")) || (lowercase.contains("ranger boots")) || (lowercase.contains("mage's book"))
                    || (lowercase.contains("master wand")) || (lowercase.contains("granite maul")) || (lowercase.contains("tome of fire")) || (lowercase.contains("recoil")) || (lowercase.contains("dharok")) || (lowercase.contains("karil")) || (lowercase.contains("guthan")) || (lowercase.contains("torag")) || (lowercase.contains("verac")) || (lowercase.contains("ahrim")) || (lowercase.contains("fire cape")) || (lowercase.contains("max cape"))
                    || (lowercase.contains("blighted")) || (lowercase.contains("dragon defender")) || (lowercase.contains("healer hat")) || (lowercase.contains("fighter hat")) || (lowercase.contains("runner hat")) || (lowercase.contains("ranger hat")) || (lowercase.contains("fighter torso")) || (lowercase.contains("runner boots")) || (lowercase.contains("penance skirt")) || (lowercase.contains("looting bag")) || (lowercase.contains("rune pouch"))
                    || (lowercase.contains("stamina")) || (lowercase.contains("anti-venom")) || (lowercase.contains("zamorakian")) || (lowercase.contains("blood money")) || (lowercase.contains("hydra")) || (lowercase.contains("ferocious")) || (lowercase.contains("jar of")) || (lowercase.contains("brimstone")) || (lowercase.contains("crystal")) || (lowercase.contains("dagon")) || (lowercase.contains("dragon pickaxe")) || (lowercase.contains("tyrannical"))
                    || (lowercase.contains("dragon 2h")) || (lowercase.contains("elysian")) || (lowercase.contains("holy elixer")) || (lowercase.contains("odium")) || (lowercase.contains("malediction")) || (lowercase.contains("fedora")) || (lowercase.contains("suffering")) || (lowercase.contains("mole")) || (lowercase.contains("vampyre dust")) || (lowercase.contains("bludgeon")) || (lowercase.contains("kbd heads")) || (lowercase.contains("trident"))
                    || (lowercase.contains("nightmare")) || (lowercase.contains("kodai wand")) || (lowercase.contains("dragon sword")) || (lowercase.contains("dragon harpoon")) || (lowercase.contains("mystery box")) || (lowercase.contains("crystal key")) || (lowercase.contains("volatile")) || (lowercase.contains("eldritch")) || (lowercase.contains("harmonised")) || (lowercase.contains("inquisitor")) || (lowercase.contains("treasonous")) || (lowercase.contains("ring of the gods"))
                    || (lowercase.contains("vorkath")) || (lowercase.contains("dragonbone")) || (lowercase.contains("uncut onyx")) || (lowercase.contains("zulrah")) || (lowercase.contains("zul-andra")) || (lowercase.contains("sanguinesti")) || (lowercase.contains("blade of saeldor")) || (lowercase.contains("barrelchest anchor")) || (lowercase.contains("staff of balance")) || (lowercase.contains("twisted bow")) || (lowercase.contains("facegaurd")) || (lowercase.contains("guardian"))
                    || (lowercase.contains("twisted buckler")) || (lowercase.contains("dragon dart")) || (lowercase.contains("guthix rest")) || (lowercase.contains("obsidian")) || (lowercase.contains("regen bracelet")) || (lowercase.contains("rangers'")) || (lowercase.contains("dragon scimitar (or)")) || (lowercase.contains("Hazy coins")) || (lowercase.contains("divine")) || (lowercase.contains("super antifire")) || (lowercase.contains("robin hood hat")) || (lowercase.contains("ankou"))
                    || (lowercase.contains("santa")) || (lowercase.contains("halloween")) || (lowercase.contains("present")) || (lowercase.contains("sack of presents")) || (lowercase.contains("dye"))) {
                    name = ("<col=65280>" + name);
                }
                if ((lowercase.contains("berserker ring")) || (lowercase.contains("seers")) || (lowercase.contains("archers")) || (lowercase.contains("warrior ring"))) {
                    name = ("<col=65280>" + name);
                }
                if (lowercase.contains("scythe")) {
                    name = ("<col=65280>" + name);
                }
                if (lowercase.contains("gilded")) {
                    name = ("<col=65280>" + name);
                }
                if (lowercase.contains("bunny")) {
                    name = ("<col=65280>" + name);
                }
                if (lowercase.contains("zanik")) {
                    name = ("<col=65280>" + name);
                }
                if (lowercase.contains("ele'")) {
                    name = ("<col=65280>" + name);
                }
                if (lowercase.contains("prince")) {
                    name = ("<col=65280>" + name);
                }
                if (lowercase.contains("zombie")) {
                    name = ("<col=65280>" + name);
                }
                if (lowercase.contains("mithril seeds")) {
                    name = ("<col=65280>" + name);
                }
                if (lowercase.contains("tribal")) {
                    name = ("<col=65280>" + name);
                }
                if (lowercase.contains("broodoo")) {
                    name = ("<col=65280>" + name);
                }
                if ((lowercase.contains("scarf")) || (lowercase.contains("woolly")) || (lowercase.contains("bobble"))) {
                    name = ("<col=65280>" + name);
                }
                if (lowercase.contains("cane")) {
                    name = ("<col=65280>" + name);
                }
                if (lowercase.contains("jester")) {
                    name = ("<col=65280>" + name);
                }
                if (lowercase.contains("(g)")) {
                    name = ("<col=65280>" + name);
                }
                if ((lowercase.contains("(t)")) && (!lowercase.endsWith("cape(t)"))) {
                    name = ("<col=65280>" + name);
                }
                if ((lowercase.contains("camo")) || (lowercase.contains("boxing glove"))) {
                    name = ("<col=65280>" + name);
                }
                if (lowercase.contains("dharok")) {
                    name = ("<col=65280>" + name);
                }
                if (lowercase.contains("dragon spear")) {
                    name = ("<col=65280>" + name);
                }
                if (lowercase.contains("phoenix neck")) {
                    name = ("<col=65280>" + name);
                }
                if (lowercase.contains("dragon bolts (e)")) {
                    name = ("<col=65280>" + name);
                }
            }
        }

        return def;
    }

    private void set_defaults() {
        inventoryModel = 0;
        name = null;
        description = null;
        colorFind = null;
        colorReplace = null;
        textureFind = null;
        textureReplace = null;
        zoom2d = 2000;
        xan2d = 0;
        yan2d = 0;
        zan2d = 0;
        xOffset2d = 0;
        yOffset2d = 0;
        stackable = false;
        cost = 1;
        members = false;
        options = null;
        interfaceOptions = null;
        maleModel0 = -1;
        maleModel1 = -1;
        maleOffset = 0;
        femaleModel0 = -1;
        femaleModel1 = -1;
        femaleOffset = 0;
        maleModel2 = -1;
        femaleModel2 = -1;
        maleHeadModel = -1;
        maleHeadModel2 = -1;
        femaleHeadModel = -1;
        femaleHeadModel2 = -1;
        countObj = null;
        countCo = null;
        notedID = -1;
        notedTemplate = -1;
        resizeX = 128;
        resizeY = 128;
        resizeZ = 128;
        ambient = 0;
        contrast = 0;
        team = 0;
        modelCustomColor = 0;
        modelCustomColor2 = 0;
        modelCustomColor3 = 0;
        modelCustomColor4 = 0;
        modelSetColor = 0;
    }

    private void set_noted_values() {
        ItemDefinition noted = get(notedTemplate);
        if (noted == null)
            return;

        inventoryModel = noted.inventoryModel;
        zoom2d = noted.zoom2d;
        xan2d = noted.xan2d;
        yan2d = noted.yan2d;
        zan2d = noted.zan2d;
        xOffset2d = noted.xOffset2d;
        yOffset2d = noted.yOffset2d;
        colorFind = noted.colorFind;
        colorReplace = noted.colorReplace;

        ItemDefinition unnoted = get(notedID);
        if (unnoted == null)
            return;

        name = unnoted.name;
        cost = unnoted.cost;

        String consonant_or_vowel_lead = "a";
        if (!ClientConstants.OSRS_DATA) {
            char character = unnoted.name.charAt(0);
            if (character == 'A' || character == 'E' || character == 'I' || character == 'O' || character == 'U')
                consonant_or_vowel_lead = "an";
        } else {
            String character = unnoted.name;
            if (character == null)
                return;

            if (character.equals("A") || character.equals("E") || character.equals("I") || character
                .equals("O") || character.equals("U"))
                consonant_or_vowel_lead = "an";
        }
        description = ("Swap this note at any bank for " + consonant_or_vowel_lead + " " + unnoted.name + ".");
        stackable = true;
    }

    public Model get_model(int stack_size) {
        if (countObj != null && stack_size > 1) {
            int stack_item_id = -1;
            for (int index = 0; index < 10; index++)
                if (stack_size >= countCo[index] && countCo[index] != 0)
                    stack_item_id = countObj[index];

            if (stack_item_id != -1)
                return get(stack_item_id).get_model(1);

        }
        Model model = (Model) model_cache.get(id);
        if (model != null) {
            return model;
        }

        model = Model.get(inventoryModel, true);
        if (model == null) {
            return null;
        }
        if (resizeX != 128 || resizeY != 128 || resizeZ != 128)
            model.scale(resizeX, resizeZ, resizeY);

        if (colorFind != null && colorReplace != null && colorFind.length == colorReplace.length) {
            for (int index = 0; index < colorFind.length; index++) {
                model.recolor(colorFind[index], colorReplace[index]);
            }
        }

        if (textureFind != null) {
            for (int index = 0; index < textureFind.length; index++) {
                model.retexture(textureFind[index], textureReplace[index]);
            }
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

        model.light(64 + ambient, 768 + contrast, -50, -10, -50, true);
        model.singleTile = true;
        model_cache.put(model, id);
        return model;
    }

    public Model get_widget_model(int stack_size) {
        if (countObj != null && stack_size > 1) {
            int stack_item_id = -1;
            for (int index = 0; index < 10; index++) {
                if (stack_size >= countCo[index] && countCo[index] != 0)
                    stack_item_id = countObj[index];
            }
            if (stack_item_id != -1)
                return get(stack_item_id).get_widget_model(1);

        }
        Model widget_model = Model.get(inventoryModel, true);
        if (widget_model == null)
            return null;
        //System.err.println("Color to replace: " + color_to_replace + " | for id: " + id);
        if (colorFind != null) {
            //System.out.println("ISNT for model: " + id);
            for (int index = 0; index < colorFind.length; index++) {
                widget_model.recolor(colorFind[index], colorReplace[index]);
            }

        }
        if (textureFind != null) {
            for (int index = 0; index < textureFind.length; index++) {
                widget_model.retexture(textureFind[index], textureReplace[index]);
            }
        }
        /*if (color_to_replace != null && color_to_replace_with != null) {
            if (src_texture != null && dst_texture != null) {
                for (int index = 0; index < color_to_replace.length; index++) {
                    widget_model.color_to_texture(widget_model, src_texture[index], dst_texture[index], false);
                }
            }
        }*/

        //System.err.println("Color to replace: " + color_to_replace + " | for id: " + id);

        if (modelCustomColor > 0) {
            widget_model.completelyRecolor(modelCustomColor);
        }
        if (modelCustomColor2 != 0) {
            widget_model.shadingRecolor(modelCustomColor2);
        }
        if (modelCustomColor3 != 0) {
            widget_model.shadingRecolor2(modelCustomColor3);
        }
        if (modelCustomColor4 != 0) {
            widget_model.shadingRecolor4(modelCustomColor4);
        }
        if (modelSetColor != 0) {
            widget_model.shadingRecolor3(modelSetColor);
        }

        return widget_model;
    }

    public Model get_equipped_model(int gender) {
        int main = maleModel0;
        int attatchment = maleModel1;
        int emblem = maleModel2;
        if (gender == 1) {
            main = femaleModel0;
            attatchment = femaleModel1;
            emblem = femaleModel2;
        }
        if (main == -1)
            return null;

        Model equipped_model = Model.get(main);
        if (equipped_model == null) {
            return null;
        }
        if (attatchment != -1) {
            if (emblem != -1) {
                Model attachment_model = Model.get(attatchment);
                Model emblem_model = Model.get(emblem);
                Model[] list = {
                    equipped_model, attachment_model, emblem_model
                };
                equipped_model = new Model(3, list);
            } else {
                Model attachment_model = Model.get(attatchment);
                Model[] list = {
                    equipped_model, attachment_model
                };
                equipped_model = new Model(2, list);
            }
        }
        if (gender == 0 && maleOffset != 0)
            equipped_model.translate(0, maleOffset, 0);

        if (gender == 1 && femaleOffset != 0)
            equipped_model.translate(0, femaleOffset, 0);

        if (colorFind != null) {
            //System.out.println("ISNT for model: " + id);
            for (int index = 0; index < colorFind.length; index++) {
                equipped_model.recolor(colorFind[index], colorReplace[index]);
            }
        }
        if (textureFind != null) {
            for (int index = 0; index < textureFind.length; index++) {
                equipped_model.retexture(textureFind[index], textureReplace[index]);
            }
        }
       /* if (color_to_replace != null && color_to_replace_with != null) {
            if (src_texture != null && dst_texture != null) {
                for (int index = 0; index < color_to_replace.length; index++) {
                    equipped_model.color_to_texture(equipped_model, src_texture[index], dst_texture[index], true);
                }
            }
        }*/
        if (modelCustomColor > 0) {
            equipped_model.completelyRecolor(modelCustomColor);
        }
        if (modelCustomColor2 != 0) {
            equipped_model.shadingRecolor(modelCustomColor2);
        }
        if (modelCustomColor3 != 0) {
            equipped_model.shadingRecolor2(modelCustomColor3);
        }
        if (modelCustomColor4 != 0) {
            equipped_model.shadingRecolor4(modelCustomColor4);
        }
        if (modelSetColor != 0) {
            equipped_model.shadingRecolor3(modelSetColor);
        }

        return equipped_model;
    }

    public boolean equipped_model_cached(int gender) {
        int main = maleModel0;
        int attachment = maleModel1;
        int emblem = maleModel2;
        if (gender == 1) {
            main = femaleModel0;
            attachment = femaleModel1;
            emblem = femaleModel2;
        }
        if (main == -1)
            return true;

        boolean cached = true;
        if (!Model.cached(main))
            cached = false;

        if (attachment != -1 && !Model.cached(attachment))
            cached = false;

        if (emblem != -1 && !Model.cached(emblem))
            cached = false;

        return cached;
    }

    public Model get_equipped_dialogue_model(int gender) {
        int head_model = maleHeadModel;
        int equipped_headgear = maleHeadModel2;
        if (gender == 1) {
            head_model = femaleHeadModel;
            equipped_headgear = femaleHeadModel2;
        }
        if (head_model == -1)
            return null;

        Model dialogue_model = Model.get(head_model);
        if (equipped_headgear != -1) {
            Model headgear = Model.get(equipped_headgear);
            Model[] list = {
                dialogue_model, headgear
            };
            dialogue_model = new Model(2, list);
        }
        if (colorFind != null) {
            for (int index = 0; index < colorFind.length; index++) {
                dialogue_model.recolor(colorFind[index], colorReplace[index]);
            }

        }
        if (textureFind != null) {
            for (int index = 0; index < textureFind.length; index++) {
                dialogue_model.retexture(textureFind[index], textureReplace[index]);
            }
        }
       /* if (color_to_replace != null && color_to_replace_with != null) {
            if (src_texture != null && dst_texture != null) {
                for (int index = 0; index < color_to_replace.length; index++) {
                    dialogue_model.color_to_texture(dialogue_model, src_texture[index], dst_texture[index], false);
                }
            }
        }*/
        return dialogue_model;
    }

    public boolean dialogue_model_cached(int gender) {
        int head_model = maleHeadModel;
        int equipped_headgear = maleHeadModel2;
        if (gender == 1) {
            head_model = femaleHeadModel;
            equipped_headgear = femaleHeadModel2;
        }
        if (head_model == -1)
            return true;

        boolean cached = true;
        if (!Model.cached(head_model))
            cached = false;

        if (equipped_headgear != -1 && !Model.cached(equipped_headgear))
            cached = false;

        return cached;
    }

    public static void release() {
        model_cache = null;
        ItemSpriteFactory.itemSpriteCache = null;
        pos = null;
        cache = null;
        data_buffer = null;
    }

    private ItemDefinition() {
        id = -1;
    }

    public static int length;
    private static int cache_index;
    private static Buffer data_buffer;
    private static ItemDefinition[] cache;
    private static int[] pos;
    public static TempCache model_cache = new TempCache(50);

    public String unknown1;
    public int wearPos1;
    public int wearPos2;
    public int wearPos3;
    public int cost;
    public int id;
    public int team;
    public int weight;
    public int zoom2d;
    public int yan2d;
    public int xan2d;
    public int zan2d;
    public int inventoryModel;
    public int maleModel0;
    public int maleModel1;
    public int maleModel2;

    public int femaleModel0;
    public int femaleModel1;
    public int femaleModel2;

    public int maleHeadModel;
    private int maleHeadModel2;
    public int maleOffset;

    public int femaleHeadModel;
    private int femaleHeadModel2;
    public int femaleOffset;

    public int xOffset2d;
    public int yOffset2d;
    private int resizeX;
    private int resizeY;
    private int resizeZ;
    public int notedTemplate;
    public int notedID;
    public int ambient;
    public int contrast;
    public int[] countObj;
    public int[] countCo;
    public short[] colorFind;
    public short[] colorReplace;
    public short[] textureFind;
    public short[] textureReplace;
    public String[][] subops;
    public String examine;

    public String[] interfaceOptions;
    public String[] options;
    public String name;
    public String description;
    public static boolean members;
    public boolean stackable;
    public boolean animateInventory;

    public boolean isTradeable;
    public int boughtId;
    public int boughtTemplateId;

    //Custom coloring
    public int modelCustomColor = 0;
    public int modelCustomColor2 = 0;
    public int modelCustomColor3 = 0;
    public int modelCustomColor4 = 0;
    public int modelSetColor = 0;

    public static int setInventoryModel(final int id) {
        final ItemDefinition definition = get(id);
        return definition.inventoryModel;
    }

    public static String setItemName(final int id) {
        final ItemDefinition definition = get(id);
        return definition.name;
    }

    public static int setMaleEquipmentId(final int id) {
        final ItemDefinition definition = get(id);
        return definition.maleModel0;
    }

    public static int setFemaleEquipmentId(final int id) {
        final ItemDefinition definition = get(id);
        return definition.femaleModel0;
    }

    public static int setModelZoom(final int id) {
        final ItemDefinition definition = get(id);
        return definition.zoom2d;
    }

    public static int setRotationX(final int id) {
        final ItemDefinition definition = get(id);
        return definition.yan2d;
    }

    public static int setRotationY(final int id) {
        final ItemDefinition definition = get(id);
        return definition.xan2d;
    }

    public static int setTranslateX(final int id) {
        final ItemDefinition definition = get(id);
        return definition.xOffset2d;
    }

    public static int setTranslateY(final int id) {
        final ItemDefinition definition = get(id);
        return definition.yOffset2d;
    }
}

