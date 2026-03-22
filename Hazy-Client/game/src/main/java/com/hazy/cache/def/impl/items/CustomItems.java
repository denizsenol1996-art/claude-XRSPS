package com.hazy.cache.def.impl.items;

import com.hazy.ClientConstants;
import com.hazy.cache.def.ItemDefinition;
import com.hazy.cache.def.NpcDefinition;
import com.hazy.util.ItemIdentifiers;
import com.hazy.util.NpcIdentifiers;

import static com.hazy.util.CustomItemIdentifiers.*;
import static com.hazy.util.ItemIdentifiers.*;

public class CustomItems {

    public static void unpack(int id) {
        ItemDefinition def = ItemDefinition.get(id);
        def.set_defaultsCustom();

        switch (id) {
            case 30400:
                ItemDefinition.copyInventory(def, 6199);
                def.name = "<col=0099ff>Sapphire mystery box";
                def.colorFind = new short[]{22410, 2999};
                def.colorReplace = new short[]{(short) 42937, (short) 56443};
                break;


            case 30415:
                ItemDefinition.copyInventory(def, SPECTRAL_SIGIL);
                def.inventoryModel = 69847;
                def.name = "<col=ff4500>Divine Sigil";
                def.zoom2d = 1000;
                def.xan2d = 385;
                def.yan2d = 178;
                def.yOffset2d = 11;
                break;

            case 30416:
                ItemDefinition.copyInventory(def, ELYSIAN_SPIRIT_SHIELD);
                def.inventoryModel = 69848;
                def.name = "<col=ff4500>Divine Spirit Shield";
                def.maleModel0 = 69846;
                def.femaleModel0 = 69846;
                def.zoom2d = 1898;
                def.xan2d = 521;
                def.yan2d = 1027;
                def.yOffset2d = 11;
                break;

            case 30401:
                ItemDefinition.copyInventory(def, 6199);
                def.name = "<col=13388800>Emerald mystery box";
                def.colorFind = new short[]{22410, 2999};
                def.colorReplace = new short[]{(short) 22308, (short) 56443};
                break;

            case 30402:
                ItemDefinition.copyInventory(def, 6199);
                def.name = "<col=993333>Ruby mystery box";
                def.colorFind = new short[]{22410, 2999};
                def.colorReplace = new short[]{(short) 922, (short) 56443};
                break;

            case 30403:
                ItemDefinition.copyInventory(def, 6199);
                def.name = "<col=ffffff>Diamond mystery box";
                def.colorFind = new short[]{22410, 2999};
                def.colorReplace = new short[]{(short) 21747, (short) 21747};
                break;

            case 30404:
                ItemDefinition.copyInventory(def, 6199);
                def.name = "<col=9966ff>Dragonstone mystery box";
                def.colorFind = new short[]{22410, 2999};
                def.colorReplace = new short[]{(short) 51100, (short) 56443};
                break;

            case 30405:
                ItemDefinition.copyInventory(def, 6199);
                def.name = "<col=130606>Onyx mystery box";
                def.colorFind = new short[]{22410, 2999};
                def.colorReplace = new short[]{(short) 21505, (short) 56443};
                break;
            case 30406:
                ItemDefinition.copyInventory(def, IMBUED_HEART);
                def.name = "<col=993333>Imbued heart (Melee)";
                def.colorFind = new short[]{(short) 54544, (short) 54561, (short) 59796, (short) 58904, (short) 60826};
                def.colorReplace = new short[]{(short) 922, (short) 922, (short) 922, (short) 922, (short) 922};
                break;
            case 30407:
                ItemDefinition.copyInventory(def, IMBUED_HEART);
                def.name = "<col=13388800>Imbued heart (Range)";
                def.colorFind = new short[]{(short) 54544, (short) 54561, (short) 59796, (short) 58904, (short) 60826};
                def.colorReplace = new short[]{(short) 20390, (short) 20390, (short) 20390, (short) 20390, (short) 20390};
                break;
            case 30408:
                ItemDefinition.copyInventory(def, IMBUED_HEART);
                def.name = "<col=130606>Imbued heart (Overload)";
                def.colorFind = new short[]{(short) 54544, (short) 54561, (short) 59796, (short) 58904, (short) 60826};
                def.colorReplace = new short[]{(short) 19456, (short) 19456, (short) 19456, (short) 19456, (short) 19456};
                break;

        }
        if (id == 13148) {
            def.name = "XP Lamp";

        }
        if (id == 30056) {
            def.name = "<col=00ffff>Memory of seren";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.zoom2d = 1575;
            def.xan2d = 60;
            def.yan2d = 0;
            def.xOffset2d = 0;
            def.yOffset2d = 90;
            NpcDefinition npcInstance = NpcDefinition.get(NpcIdentifiers.SEREN);
            def.inventoryModel = npcInstance.models[0];
        }

        if (id == 30054) {
            def.name = "<col=cd2626>Escape key";
            ItemDefinition.copyInventory(def, WILDERNESS_KEY);
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.colorFind = new short[]{8128, 0, 5231};
            def.colorReplace = new short[]{933, 0, 5231};
        }
        if (id == 26922) {//69719
            def.name = "<col=65280>Enchanted salazar locket";
            def.inventoryModel = 69719;
            def.zoom2d = 590;
            def.xan2d = 500;
            def.yan2d = 0;
            def.xOffset2d = 0;
            def.yOffset2d = 7;
            def.stackable = false;
            def.options = new String[]{null, null, "Take", null, null};
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.maleModel0 = 69720;
            def.femaleModel0 = 69721;

        }
        if (id == 30050) {
            ItemDefinition.copyInventory(def, 13204);
            def.name = "<col=00fa9a>OS-GP token";
            def.colorFind = new short[]{5813, 9139, 26006};
            def.colorReplace = new short[]{(short) 353770,(short)  353770, (short) 353770};
            def.countCo = new int[]{2, 3, 4, 5, 0, 0, 0, 0, 0, 0};
            def.countObj = new int[]{30051, 30052, 30053, 30053, 0, 0, 0, 0, 0, 0};
        }
        if (id == 20321) {
            ItemDefinition ankoumask = ItemDefinition.get(ANKOU_MASK);
            def.name = "@bla@Black Ankou Mask";
            def.inventoryModel = ankoumask.inventoryModel;
            def.zoom2d = ankoumask.zoom2d;
            def.xan2d = ankoumask.xan2d;
            def.yan2d = ankoumask.yan2d;
            def.yOffset2d = ankoumask.yOffset2d;
            def.stackable = ankoumask.stackable;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.maleModel0 = ankoumask.maleModel0;
            def.femaleModel0 = ankoumask.femaleModel0;
            def.modelCustomColor4 = 235;
        }
        if (id == 20322) {
            ItemDefinition ankouGloves = ItemDefinition.get(ANKOU_GLOVES);
            def.inventoryModel = ankouGloves.inventoryModel;
            def.zoom2d = ankouGloves.zoom2d;
            def.xan2d = ankouGloves.xan2d;
            def.yan2d = ankouGloves.yan2d;
            def.yOffset2d = ankouGloves.yOffset2d;
            def.stackable = ankouGloves.stackable;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.maleModel0 = ankouGloves.maleModel0;
            def.femaleModel0 = ankouGloves.femaleModel0;
            def.name = "@bla@Black Ankou Gloves";
            def.modelCustomColor4 = 235;
        }
        if (id == 24855) {
            def.name = "Enchanted Max Cape";
            def.colorFind = new short[]{(short) 38325, (short) 38333, (short) 38210, (short) 38356, (short) 38366, (short) 38348, (short) 38362, 88, 99, 107, 127, (short) 38113, (short) 38356, 552};
            def.colorReplace = new short[]{933, 933, 933, 933, 933, 933, 933, 0, 0, 0, 0, 0, 0, 0};
        }
        if (id == 20323) {
            ItemDefinition ankouGloves = ItemDefinition.get(ANKOU_TOP);
            def.inventoryModel = ankouGloves.inventoryModel;
            def.zoom2d = ankouGloves.zoom2d;
            def.xan2d = ankouGloves.xan2d;
            def.yan2d = ankouGloves.yan2d;
            def.yOffset2d = ankouGloves.yOffset2d;
            def.stackable = ankouGloves.stackable;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.maleModel0 = ankouGloves.maleModel0;
            def.maleModel1 = ankouGloves.maleModel1;
            def.femaleModel1 = ankouGloves.femaleModel1;
            def.femaleModel0 = ankouGloves.femaleModel0;
            def.name = "@bla@Black Ankou Top";
            def.modelCustomColor4 = 235;// all done on live i'll put pet for collection log on localhost and u can test it changge rate of drop pet

            //in elvarg file and check it :D bet ty did you do  lava beast elvarg and deranged?ye
        }

        //you dont want to add it to live server? nah just tell me which ones i have to copy and paste over to update it :) im still kinda figuring out where i want to put it as a drop or box or forge

        //ok so you dontt have to update client for now, and i'll add part that make u able to wear it when u update new client to everyone. perfect i just wanna do a teaser for a few days to get ppl excited.
        //alright
        if (id == 20324) {
            ItemDefinition ankouGloves = ItemDefinition.get(ANKOUS_LEGGINGS);
            def.inventoryModel = ankouGloves.inventoryModel;
            def.zoom2d = ankouGloves.zoom2d;
            def.xan2d = ankouGloves.xan2d;
            def.yan2d = ankouGloves.yan2d;
            def.yOffset2d = ankouGloves.yOffset2d;
            def.stackable = ankouGloves.stackable;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.maleModel0 = ankouGloves.maleModel0;
            def.femaleModel0 = ankouGloves.femaleModel0;

            def.name = "@bla@Black Ankou Leggings";
            def.modelCustomColor4 = 235;
        }
        if (id == 20325) {
            ItemDefinition ankouGloves = ItemDefinition.get(ANKOU_SOCKS);
            def.inventoryModel = ankouGloves.inventoryModel;
            def.zoom2d = ankouGloves.zoom2d;
            def.xan2d = ankouGloves.xan2d;
            def.yan2d = ankouGloves.yan2d;
            def.yOffset2d = ankouGloves.yOffset2d;
            def.stackable = ankouGloves.stackable;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.maleModel0 = ankouGloves.maleModel0;
            def.femaleModel0 = ankouGloves.femaleModel0;
            def.name = "@bla@Black Ankou Socks";
            def.modelCustomColor4 = 235;
        }
        if (id == 30051) {
            ItemDefinition.copyInventory(def, 3985);
            def.name = "<col=00fa9a>OS-GP token";
            def.colorFind = new short[]{5813, 9139, 26006};
            def.colorReplace = new short[]{(short) 353770,(short)  353770, (short) 353770};
        }

        if (id == 30052) {
            ItemDefinition.copyInventory(def, 3987);
            def.name = "<col=00fa9a>OS-GP token";
            def.colorFind = new short[]{5813, 9139, 26006};
            def.colorReplace = new short[]{(short) 353770,(short)  353770, (short) 353770};
        }

        if (id == 30053) {
            ItemDefinition.copyInventory(def, 3989);
            def.name = "<col=00fa9a>OS-GP token";
            def.colorFind = new short[]{5813, 9139, 26006};
            def.colorReplace = new short[]{(short) 353770,(short)  353770, (short) 353770};
        }

        if (id == 30047) {
            def.name = "<col=9b30ff>Anathematic heart";
            ItemDefinition.copyInventory(def, IMBUED_HEART);
            ItemDefinition.copyEquipment(def, IMBUED_HEART);
            def.colorFind = new short[]{-4710, -6632, -5740, -10975, -10992};
            def.colorReplace = new short[]{(short) 374770, (short) 246770,(short)  374770, (short) 374770, (short) 374770};
        }

        if (id == 30046) {
            def.name = "<col=9b30ff>Enchanted key (r)";
            ItemDefinition.copyInventory(def, LARGE_ORNATE_KEY);
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.colorFind = new short[]{9777, 10931};
            def.colorReplace = new short[]{933,(short)  130770};
        }
        if (id == 29312) {
            ItemDefinition.copyInventory(def, FAWKES);
            def.name = "@pur@Zaweks pet";
            def.modelCustomColor4 = 51136;
        }
        if (id == 23912) {
            def.name = "<col=65280> Mending life bird";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.inventoryModel = 26853;
            def.zoom2d = 2340;
            def.yan2d = 210;
            def.xan2d = 10;
            def.xOffset2d = -2;
            def.yOffset2d = 9;
            def.colorFind = new short[]{10176, 2880, 6082, 6084, 23492, 2983, 4391, 8, 29867, 4011, 4013, 2733, 2735, 4399, 914, 20, 8150, 10167, 1946, 23483, 28, 5053};
            def.colorReplace = new short[]{(short) 347770, (short) 347770,(short)  347770, (short) 347770, (short) 347770, (short) 347770, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 28, 0};
        }
        if (id == 30045) {
            def.name = "<col=9b30ff>Enchanted key (p)";
            ItemDefinition.copyInventory(def, LARGE_ORNATE_KEY);
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.colorFind = new short[]{9777, 10931};
            def.colorReplace = new short[]{(short) 374770, (short) 374770};
        }

        if (id == 30043) {
            def.name = "<col=9b30ff>Deranged archaeologist";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.zoom2d = 750;
            def.xan2d = 100;
            def.yan2d = 100;
            def.xOffset2d = 0;
            def.yOffset2d = 115;
            NpcDefinition npcInstance = NpcDefinition.get(NpcIdentifiers.DERANGED_ARCHAEOLOGIST);
            def.inventoryModel = npcInstance.models[0];
            def.colorFind = new short[]{0, 4550, 6798};
            def.colorReplace = new short[]{0, (short) 374770, (short) 491770};
        }//your pc lagged too ? no idea lol was texting

        if (id == 30042) {
            def.name = "<col=9b30ff>Enchanted bones";
            ItemDefinition.copyInventory(def, FAYRG_BONES);
            ItemDefinition.copyEquipment(def, FAYRG_BONES);
            def.colorFind = new short[]{7370, 8094};
            def.colorReplace = new short[]{(short) 374770,(short)  311770};
            def.stackable = true;
        }

        if (id == 30041) {
            def.name = "<col=9b30ff>Anathematic wand";
            ItemDefinition.copyInventory(def, KODAI_WAND);
            ItemDefinition.copyEquipment(def, KODAI_WAND);
            def.colorFind = new short[]{-19153, -16339, 37, -19145, -16331, -19500};
            def.colorReplace = new short[]{-19153, (short) 246770, 37, -19145, -16331,(short)  374770};
        }

        if (id == 30040) {
            def.name = "<col=9b30ff>Ring of divination";
            ItemDefinition.copyInventory(def, TOPAZ_RING);
            ItemDefinition.copyEquipment(def, TOPAZ_RING);
            def.colorFind = new short[]{9152, 82, 123, 127};
            def.colorReplace = new short[]{(short) 374770, 82, 123, 127};
        }

        if (id == 30039) {
            def.name = "<col=9b30ff>Deranged manifesto";
            ItemDefinition.copyInventory(def, CHRONICLE);
            ItemDefinition.copyEquipment(def, CHRONICLE);
            def.interfaceOptions = new String[]{null, "Wield", null, "Teleport", "Drop"};
            def.colorFind = new short[]{9152, 0, -27364, 103, -29001, -2125, 11212, 332, 78, 49, 115, -28761, -1210, 187, 127};
            def.colorReplace = new short[]{9152, 0, -27364, 103, (short) 374770,(short)  374770,(short)  374770, (short) 374770,(short)  374770, (short) 374770, 115, (short) 129770, (short) 129770, 187, 127};
        }

        if (id == 30035) {
            def.name = "<col=9b30ff>Anathematic stone";
            ItemDefinition.copyInventory(def, CORRUPTING_STONE);
            ItemDefinition.copyEquipment(def, CORRUPTING_STONE);
            def.colorFind = new short[]{-22297, 127};
            def.colorReplace = new short[]{(short) 374770, (short) 246770};
        }

        if (id == 30034) {
            def.name = "<col=9b30ff>Anathema ward";
            ItemDefinition.copyInventory(def, TOKTZKETXIL);
            ItemDefinition.copyEquipment(def, TOKTZKETXIL);
            def.colorFind = new short[]{33, 20, 8, 28, 908, 7054};//The color of the item itself grab by using ::getcolors itemid
            def.colorReplace = new short[]{33, 20, 8, 28, (short) 374770, (short) 374770};//Here u edit the colors
        }

        if (id == 30032) {
            def.name = "<col=cd2626>Corrupted crystal helm";
            ItemDefinition.copyInventory(def, CRYSTAL_HELM);
            ItemDefinition.copyEquipment(def, CRYSTAL_HELM);
            def.colorFind = new short[]{-32593, -32484, -32725, -27417, -32479, -27423};//The color of the item itself grab by using ::getcolors itemid
            def.colorReplace = new short[]{945, 945, 945, 945, 582, 582};//Here u edit the colors
        }
        if (id == 30031) {
            def.name = "<col=cd2626>Corrupted crystal legs";
            ItemDefinition.copyInventory(def, CRYSTAL_LEGS);
            ItemDefinition.copyEquipment(def, CRYSTAL_LEGS);
            def.colorFind = new short[]{-32593, -32484, -32725, -27417, -32479, -27423};//The color of the item itself grab by using ::getcolors itemid
            def.colorReplace = new short[]{945, 945, 945, 945, 582, 582,};//Here u edit the colors
        }
        if (id == 30030) {
            def.name = "<col=cd2626>Corrupted crystal body";
            ItemDefinition.copyInventory(def, CRYSTAL_BODY);
            ItemDefinition.copyEquipment(def, CRYSTAL_BODY);
            def.colorFind = new short[]{-32593, -32484, -27417, -27423, -32479};//The color of the item itself grab by using ::getcolors itemid
            def.colorReplace = new short[]{945, 945, 945, 945, 582};//Here u edit the colors
        }
        if (id == 30028) {
            def.name = "<col=ff4500>Molten defender";
            ItemDefinition.copyInventory(def, AVERNIC_DEFENDER);
            ItemDefinition.copyEquipment(def, AVERNIC_DEFENDER);
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.colorFind = new short[]{16, 20, 7333, 8390, 24, 8377, 10283, 12};//The color of the item itself grab by using ::getcolors itemid
            def.colorReplace = new short[]{(short) 332770, (short) 332770, (short) 332770, (short) 332770, (short) 338770, (short) 338770, 933, 933};//Here u edit the colors
        }
        if (id == 29109) {//fawzi
            def.name = "<col=65280>sanguinesti Kit";
            ItemDefinition.copyInventory(def, TWISTED_BOW_KIT);
            def.modelCustomColor4 = 1111;
        }

        if (id == 30098) {
            ItemDefinition looting = ItemDefinition.get(LOOTING_BAG_22586);
            ItemDefinition.copyInventory(def, LOOTING_BAG_22586);
            def.name = "Looting bag (I)";
            def.interfaceOptions = looting.interfaceOptions;
            def.modelCustomColor4 = 933;

        }
        if (id == 30099) {
            ItemDefinition looting = ItemDefinition.get(LOOTING_BAG);
            ItemDefinition.copyInventory(def, LOOTING_BAG);
            def.name = "Looting bag (I)";
            def.interfaceOptions = looting.interfaceOptions;
            def.modelCustomColor4 = 933;
        }
        if (id == 30027) {
            def.name = "<col=ff4500>Lava beast pet";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.zoom2d = 5000;
            def.xan2d = 100;
            def.yan2d = 100;
            def.xOffset2d = 0;
            def.yOffset2d = 10;
            NpcDefinition npcInstance = NpcDefinition.get(NpcIdentifiers.LAVA_BEAST);
            def.inventoryModel = npcInstance.models[0];
        }

        if (id == 30029) {
            def.name = "<col=ff4500>Molten key";
            ItemDefinition.copyInventory(def, LARGE_ORNATE_KEY);
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.colorFind = new short[]{9777, 10931};
            def.colorReplace = new short[]{(short) 338770, (short) 332770};
            def.textureFind = new short[]{9777, 10931};
            def.textureReplace = new short[]{54, 54};
        }

        if (id == 30025) {
            def.name = "<col=00ffff>Frost imbued max hood";
            ItemDefinition.copyInventory(def, MAX_HOOD);
            ItemDefinition.copyEquipment(def, MAX_HOOD);
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.colorFind = new short[]{784, 0, 945, 914, 5458, 675, 4820, 4550, 972, 685, 815};//The color of the item itself grab by using ::getcolors itemid
            def.colorReplace = new short[]{(short) 34770, (short) 491770, (short) 491770, (short) 491770, 124, 124, 124, 124, 124, 124, 124};//Here u edit the colors
        }
        if (id == 30023) {
            def.name = "<col=00ffff>Frost imbued max cape";
            ItemDefinition.copyInventory(def, MAX_CAPE);
            ItemDefinition.copyEquipment(def, MAX_CAPE);
            def.interfaceOptions = new String[]{null, "Wear", "Features", null, "Drop"};
            def.colorFind = new short[]{673, 675, 902, 5706, 522, 5708, 4300, 972, 815, 784, 945, 5458, 5714, 5683, 4820, 668, 4316, 5437};//The color of the item itself grab by using ::getcolors itemid
            def.colorReplace = new short[]{(short) 34770, (short) 34770, (short) 34770, (short) 34770, (short) 34770, (short) 34770, (short) 491770,(short)  491770, (short) 491770, 124, 124, 124, 124, 124, 124, 124, 124, (short) 34770};//Here u edit the colors
        }
        //find a id not in use
        if (id == 30259) {
            def.name = "<col=96933>Tome of frost";
            //copy all item attributes of item u wanne recolor, equipment too if its a equip item ofc
            ItemDefinition.copyInventory(def, TOME_OF_FIRE);
            ItemDefinition.copyEquipment(def, TOME_OF_FIRE);
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.colorFind = new short[]{0, 902, 5706, 522, 12, 5708, 4300, 972, 945, 5458, 5714, 5683, 4820, 668, 4316, 5437};//The color of the item itself grab by using ::getcolors itemid
            def.colorReplace = new short[]{(short) 34770, (short) 34770,(short)  34770, (short) 34770, (short) 34770, (short) 34770, (short) 34770,(short)  34770, (short) 34770, (short) 34770};//Here u edit the colors
        }
        if (id == 30101) {//DZONE3
            def.name = "<col=007500>Mending stone";
            ItemDefinition.copyInventory(def, CORRUPTING_STONE);
            ItemDefinition.copyEquipment(def, CORRUPTING_STONE);
            def.colorFind = new short[]{-22297, 127};
            def.colorReplace = new short[]{17350, 17350};
        }
        if (id == 30103) {//DZONE3
            def.name = "<col=65280>Mending sword of gryffindor";
            ItemDefinition.copyInventory(def, SWORD_OF_GRYFFINDOR);
            ItemDefinition.copyEquipment(def, SWORD_OF_GRYFFINDOR);
            def.colorFind = new short[]{10258, 10291, 10275, 10262, 10266, 10283};
            def.colorReplace = new short[]{17350, 17350, 17350, 17350, 17350, 17350};
        }
        if (id == 32000) {
            def.name = "<col=96933>Frost bow";
            //copy all item attributes of item u wanne recolor, equipment too if its a equip item ofc
            ItemDefinition.copyInventory(def, ELEMENTAL_BOW);
            ItemDefinition.copyEquipment(def, ELEMENTAL_BOW);
            def.options = new String[]{null, null, "Take", null, null};
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
            def.colorFind = new short[]{66, -15910, 74, 10283, 10299, -18446, 127};//The color of the item itself grab by using ::getcolors itemid
            def.colorReplace = new short[]{1000, 1000, 1000, 1000, 1000, 1000, 1000};//Here u edit the colors
            def.inventoryModel = 28678;
            def.maleModel0 = 28622;
            def.femaleModel0 = 28622;
        }

        if (id == 30019) {
            def.name = "<col=006400>Baby Elvarg";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.zoom2d = 550;
            def.xan2d = 76;
            def.yan2d = 16;
            def.xOffset2d = 0;
            def.yOffset2d = 0;
            def.inventoryModel = 7171;
            def.colorFind = new short[]{476};
            def.colorReplace = new short[]{(short) 479770};
        }

        if (id == 30017) {
            def.name = "<col=00ffff>Snowbird";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.inventoryModel = 26853;
            def.zoom2d = 2340;
            def.yan2d = 210;
            def.xan2d = 10;
            def.xOffset2d = -2;
            def.yOffset2d = 9;
            def.colorFind = new short[]{10176, 2880, 6082, 6084, 23492, 2983, 4391, 8, 29867, 4011, 4013, 2733, 2735, 4399, 914, 20, 8150, 10167, 1946, 23483, 28, 5053};
            def.colorReplace = new short[]{124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124};
        }

        if (id == 30015) {
            def.name = "<col=00ffff>Frost imbued cape";
            //copy all item attributes of item u wanne recolor, equipment too if its a equip item ofc
            ItemDefinition.copyInventory(def, IMBUED_ZAMORAK_CAPE);
            ItemDefinition.copyEquipment(def, IMBUED_ZAMORAK_CAPE);
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.colorFind = new short[]{929, 33, 918, 935, 24, 922, 924};//The color of the item itself grab by using ::getcolors itemid
            def.colorReplace = new short[]{124, (short) 34770, 124, (short) 34770, (short) 491770, (short) 34770, (short) 491770};//Here u edit the colors
        }

        if (id == 30013) {
            def.name = "<col=00ffff>Infinity boots (winter)";
            //copy all item attributes of item u wanne recolor, equipment too if its a equip item ofc
            ItemDefinition.copyInventory(def, INFINITY_BOOTS);
            ItemDefinition.copyEquipment(def, INFINITY_BOOTS);
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.colorFind = new short[]{9152, 4626, -22248, 695};//The color of the item itself grab by using ::getcolors itemid
            def.colorReplace = new short[]{(short) 34770, (short) 76770,(short)  491770, 124};//Here u edit the colors
        }

        if (id == 30001) {
            def.name = "<col=00ffff>Frost claws";
            def.femaleModel0 = 29191;
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
            def.inventoryModel = 32784;
            def.maleModel0 = 29191;
            def.xOffset2d = -1;
            def.yOffset2d = 8;
            def.xan2d = 349;
            def.yan2d = 15;
            def.zoom2d = 886;
            def.colorFind = new short[]{929, 914, 918, 922};
            def.colorReplace = new short[]{124,(short)  34770, (short) 34770, 124};
        }

        if (id == 30003) {
            def.name = "<col=00ffff>Armadyl frostsword";
            def.femaleModel0 = 27649;
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
            def.inventoryModel = 28075;
            def.maleModel0 = 27649;
            def.xOffset2d = -1;
            def.yOffset2d = -1;
            def.xan2d = 498;
            def.yan2d = 484;
            def.zoom2d = 1957;
            def.colorFind = new short[]{-22419, -24279, -22423, -22444, -22477, -24271, -22415, -22208, -22464};
            def.colorReplace = new short[]{124, (short) 34770, 124, 124, (short) 34770, 124, 124, 124, 124};
        }
        if (id == 30005) {
            ItemDefinition.copyInventory(def, 21003);
            ItemDefinition.copyEquipment(def, 21003);
            def.name = "<col=00ffff>Elder ice maul";
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
            def.modelCustomColor4 = 555500;
        }

        if (id == 24950) {
            def.name = "<col=00ffff>Iced partyhat";
            def.inventoryModel = 2635;
            def.maleModel0 = 187;
            def.femaleModel0 = 363;
            def.zoom2d = 440;
            def.yan2d = 1852;
            def.xan2d = 76;
            def.xOffset2d = 1;
            def.yOffset2d = 1;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.colorFind = new short[1];
            def.colorFind[0] = 926;
            def.colorReplace = new short[1];
            def.colorReplace[0] = (short) 34770;
        }

        if (id == 30239) {
            def.name = "<col=00ffff>Snowy sled";
            def.interfaceOptions = new String[]{null, "Ride", null, null, "Drop"};
            def.femaleModel0 = 4946;
            def.inventoryModel = 4937;
            def.maleModel0 = 4946;
            def.xan2d = 136;
            def.yan2d = 160;
            def.zoom2d = 1960;
            def.modelCustomColor4 = 469;
        }
        if (id == 4083) {
            def.name = "<col=65280>Sled";
            def.interfaceOptions = new String[]{null, "Ride", null, null, "Drop"};
        }
        if (id == 29111) {
            def.name = "<col=65280>Bottomless divine super combat";
            ItemDefinition.copyInventory(def, DIVINE_SUPER_COMBAT_POTION4);
            def.animateInventory = true;
            def.inventoryModel = 69812;
        }
        if (id == 29112) {
            def.name = "<col=65280>Bottomless divine super range";
            ItemDefinition.copyInventory(def, DIVINE_RANGING_POTION4);
            def.animateInventory = true;
            def.inventoryModel = 69813;
        }
        if (id == 29113) {
            def.name = "<col=65280>Bottomless saradomin brew";
            ItemDefinition.copyInventory(def, DIVINE_RANGING_POTION4);
            def.animateInventory = true;
            def.inventoryModel = 69814;
        }
        if (id == 29114) {
            def.name = "<col=65280>Bottomless sanfew";
            ItemDefinition.copyInventory(def, DIVINE_RANGING_POTION4);
            def.animateInventory = true;
            def.inventoryModel = 69815;
        }
        if (id == 29115) {
            def.name = "<col=65280>Bottomless overload";
            ItemDefinition.copyInventory(def, DIVINE_RANGING_POTION4);
            def.animateInventory = true;
            def.inventoryModel = 69816;
        }
        if (id == 30007) {
            def.name = "<col=00ffff>Iced h'ween mask";
            def.inventoryModel = 2438;
            def.maleModel0 = 3188;
            def.femaleModel0 = 3192;
            def.zoom2d = 730;
            def.xan2d = 516;
            def.yan2d = 0;
            def.yOffset2d = -10;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.colorFind = new short[2];
            def.colorFind[0] = 926;
            def.colorFind[1] = 0;
            def.colorReplace = new short[2];
            def.colorReplace[0] = (short) 34770; // Background colour
            def.colorReplace[1] = 0; // Eyes colour
        }

        if (id == 30009) {
            def.name = "<col=00ffff>Iced santa hat";
            def.inventoryModel = 2537;
            def.zoom2d = 540;
            def.xan2d = 72;
            def.yan2d = 136;
            def.xOffset2d = 0;
            def.yOffset2d = -3;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.maleModel0 = 189;
            def.femaleModel0 = 366;
            def.maleHeadModel = 69;
            def.femaleHeadModel = 127;
            def.colorFind = new short[]{933, 10351};
            def.colorReplace = new short[]{(short) 34770, 10351};
        }

        if (id == 30011) {
            def.name = "<col=cd2626>Ugly santa hat";
            def.inventoryModel = 2537;
            def.zoom2d = 540;
            def.xan2d = 72;
            def.yan2d = 136;
            def.xOffset2d = 0;
            def.yOffset2d = -3;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.maleModel0 = 189;
            def.femaleModel0 = 366;
            def.maleHeadModel = 69;
            def.femaleHeadModel = 127;
            def.colorFind = new short[]{933, 10351};
            def.colorReplace = new short[]{(short) 350770, 933};
        }

        if (id == 30253) {
            def.name = "<col=65280>Cloak of invisibility";
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.femaleModel0 = 37051;
            def.inventoryModel = 37197;
            def.maleModel0 = 37019;
            def.xOffset2d = 3;
            def.yOffset2d = 12;
            def.xan2d = 361;
            def.yan2d = 47;
            def.zoom2d = 2130;
            def.modelCustomColor4 = 490;
        }

        if (id == 30252) {
            def.name = "<col=65280>Marvolo gaunts ring";
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.inventoryModel = 29141;
            def.xOffset2d = -1;
            def.yOffset2d = -1;
            def.xan2d = 550;
            def.yan2d = 1800;
            def.zoom2d = 550;
            def.modelCustomColor3 = 3020;
        }
        if (id == 20400) {
            def.name = "<col=cd2626>Bond's casket";
            def.interfaceOptions = new String[]{"Open", null, null, null, "Drop"};
            def.inventoryModel = 69821;
            def.xOffset2d = 3;
            def.yOffset2d = -6;
            def.xan2d = 164;
            def.yan2d = 1592;
            def.zoom2d = 1410;
        }
        if (id == 30251) {
            def.name = "<col=65280>Tom Riddle's diary";
            def.ambient = 15;
            def.femaleModel0 = 10699;
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
            def.inventoryModel = 10573;
            def.maleModel0 = 10698;
            def.xOffset2d = 2;
            def.yOffset2d = 10;
            def.xan2d = 260;
            def.yan2d = 1948;
            def.zoom2d = 950;
            def.modelCustomColor4 = 490;
        }

        if (id == 30250) {
            def.name = "<col=65280>Nagini";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.inventoryModel = 13556;
            def.xOffset2d = 3;
            def.yOffset2d = 6;
            def.xan2d = 429;
            def.yan2d = 1985;
            def.zoom2d = 2128;
        }

        if (id == 30224) {
            def.name = "<col=65280>Grim h'ween mask";
            def.inventoryModel = 2438;
            def.maleModel0 = 3188;
            def.femaleModel0 = 3192;
            def.zoom2d = 730;
            def.xan2d = 516;
            def.yan2d = 0;
            def.yOffset2d = -10;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.colorFind = new short[2];
            def.colorFind[0] = 926;
            def.colorFind[1] = 0;
            def.colorReplace = new short[2];
            def.colorReplace[0] = 28; // Background colour
            def.colorReplace[1] = 9152; // Eyes colour
        }

        if (id == 30225) {
            def.name = "<col=65280>Grim partyhat";
            def.inventoryModel = 2635;
            def.maleModel0 = 187;
            def.femaleModel0 = 363;
            def.zoom2d = 440;
            def.yan2d = 1852;
            def.xan2d = 76;
            def.xOffset2d = 1;
            def.yOffset2d = 1;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.colorFind = new short[1];
            def.colorFind[0] = 926;
            def.colorReplace = new short[1];
            def.colorReplace[0] = 28;
        }

        if (id == 30226) {
            def.name = "<col=65280>Grim scythe";
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
            def.inventoryModel = 2511;
            def.maleModel0 = 507;
            def.femaleModel0 = 507;
            def.xOffset2d = 1;
            def.yOffset2d = 17;
            def.xan2d = 336;
            def.yan2d = 20;
            def.zoom2d = 1930;
            def.colorFind = new short[]{7073, 61};
            def.colorReplace = new short[]{28, 61};
        }

        if (id == 30227) {
            def.animateInventory = true;
            def.name = "<col=65280>H'ween mystery chest";
            def.interfaceOptions = new String[]{"Open", null, null, null, "Drop"};
            def.inventoryModel = 55606;
            def.xOffset2d = 0;
            def.yOffset2d = 0;
            def.xan2d = 114;
            def.yan2d = 1883;
            def.zoom2d = 2640;
            def.colorFind = new short[]{24, 49, 4510, 4502, 8128, 7093};
            def.colorReplace = new short[]{24, 49, (short) 374770, (short) 374770, (short) 87770, (short) 87770};
        }

        if (id == 30228) {
            def.name = "<col=65280>Haunted hellhound";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.inventoryModel = 69803;
            def.zoom2d = 3100;
            def.xan2d = 250;
            def.yan2d = 280;
            def.xOffset2d = -7;
            def.yOffset2d = -329;
        }

        if (id == 31000) {
            def.name = "<col=65280>Yoshi Pet";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.inventoryModel = 69834;
            def.zoom2d = 826;
            def.xan2d = 43;
            def.yan2d = 1852;
            def.xOffset2d = 1;
            def.yOffset2d = 78;
        }


        if (id == 31001) {
            def.name = "<col=65280>Cursed Element";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.inventoryModel = 69833;
            def.zoom2d = 757;
            def.xan2d = 9;
            def.yan2d = 1765;
            def.xOffset2d = 0;
            def.yOffset2d = 130;
        }


        if (id == 30229) {
            def.name = "<col=65280>H'ween armadyl godsword";
            def.femaleModel0 = 27649;
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
            def.inventoryModel = 28075;
            def.maleModel0 = 27649;
            def.xOffset2d = -1;
            def.yOffset2d = -1;
            def.xan2d = 498;
            def.yan2d = 484;
            def.zoom2d = 1957;
            def.modelCustomColor3 = 24;
        }

        if (id == 30230) {
            def.name = "<col=65280>H'ween blowpipe";
            def.femaleModel0 = 14403;
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
            def.inventoryModel = 19219;
            def.maleModel0 = 14403;
            def.xOffset2d = -7;
            def.yOffset2d = 4;
            def.xan2d = 768;
            def.yan2d = 189;
            def.zoom2d = 1158;
            def.modelCustomColor3 = 24;
        }

        if (id == 30231) {
            def.name = "<col=65280>H'ween dragon claws";
            def.femaleModel0 = 29191;
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
            def.inventoryModel = 32784;
            def.maleModel0 = 29191;
            def.xOffset2d = -1;
            def.yOffset2d = 8;
            def.xan2d = 349;
            def.yan2d = 15;
            def.zoom2d = 886;
            def.modelCustomColor3 = 24;
        }

        if (id == 30232) {
            def.name = "<col=65280>H'ween craw's bow";
            def.femaleModel0 = 35769;
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
            def.inventoryModel = 35775;
            def.maleModel0 = 35769;
            def.xOffset2d = -1;
            def.yOffset2d = -3;
            def.xan2d = 1463;
            def.yan2d = 510;
            def.zan2d = 835;
            def.zoom2d = 1979;
            def.modelCustomColor3 = 24;
        }

        if (id == 30233) {
            def.name = "<col=65280>H'ween chainmace";
            def.femaleModel0 = 35771;
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
            def.inventoryModel = 35780;
            def.maleModel0 = 35771;
            def.xOffset2d = 11;
            def.yOffset2d = -8;
            def.xan2d = 1495;
            def.yan2d = 256;
            def.zoom2d = 1488;
            def.modelCustomColor3 = 24;
        }

        if (id == 30234) {
            ItemDefinition.copyInventory(def, GRANITE_MAUL_24225);
            ItemDefinition.copyEquipment(def, GRANITE_MAUL_24225);
            def.name = "<col=65280>H'ween granite maul";
            def.modelCustomColor3 = 24;
        }


        if (id == 30240) {
            def.name = "<col=65280>Haunted crossbow";
            def.femaleModel0 = 15472;
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
            def.inventoryModel = 15493;
            def.maleModel0 = 15472;
            def.xOffset2d = 2;
            def.yOffset2d = 8;
            def.xan2d = 444;
            def.yan2d = 1658;
            def.zoom2d = 1104;
            def.modelCustomColor3 = 24;
        }

        if (id == 30241) {
            def.name = "<col=65280>Haunted dragonfire shield";
            def.ambient = 15;
            def.contrast = 15;
            def.femaleModel0 = 26423;
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
            def.inventoryModel = 26457;
            def.maleModel0 = 26423;
            def.xan2d = 540;
            def.yan2d = 123;
            def.zoom2d = 2022;
            def.modelCustomColor3 = 24;
        }

        if (id == 30242) {
            def.name = "<col=cd2626>Winter item casket";
            def.interfaceOptions = new String[]{"Open", null, null, null, "Drop"};
            def.inventoryModel = 2450;
            def.xOffset2d = 3;
            def.yOffset2d = -6;
            def.xan2d = 164;
            def.yan2d = 1592;
            def.zoom2d = 1410;
            def.modelCustomColor4 = 190;
        }

        if (id == 30235) {
            ItemDefinition.copyInventory(def, 13204);
            def.name = "<col=65280>H'ween token";
            def.colorFind = new short[]{5813, 9139, 26006};
            def.colorReplace = new short[]{25, 26, 27};
            def.countCo = new int[]{2, 3, 4, 5, 0, 0, 0, 0, 0, 0};
            def.countObj = new int[]{30236, 30237, 30238, 30238, 0, 0, 0, 0, 0, 0};
        }

        if (id == 30236) {
            ItemDefinition.copyInventory(def, 3985);
            def.name = "<col=65280>H'ween token";
            def.colorFind = new short[]{5813, 9139, 26006};
            def.colorReplace = new short[]{25, 26, 27};
        }

        if (id == 30237) {
            ItemDefinition.copyInventory(def, 3987);
            def.name = "<col=65280>H'ween token";
            def.colorFind = new short[]{5813, 9139, 26006};
            def.colorReplace = new short[]{25, 26, 27};
        }

        if (id == 30238) {
            ItemDefinition.copyInventory(def, 3989);
            def.name = "<col=65280>H'ween token";
            def.colorFind = new short[]{5813, 9139, 26006};
            def.colorReplace = new short[]{25, 26, 27};
        }

        if (id == 32236) {
            ItemDefinition.copyInventory(def, 13204);
            def.name = "<col=00ffff>Winter token";
            def.colorFind = new short[]{5813, 9139, 26006};
            def.colorReplace = new short[]{124, (short) 34770, 124};
            def.countCo = new int[]{2, 3, 4, 5, 0, 0, 0, 0, 0, 0};
            def.countObj = new int[]{32237, 32238, 32239, 32239, 0, 0, 0, 0, 0, 0};
        }

        if (id == 32237) {
            ItemDefinition.copyInventory(def, 3985);
            def.name = "<col=00ffff>Winter token";
            def.colorFind = new short[]{5813, 9139, 26006};
            def.colorReplace = new short[]{124, (short) 34770, 124};
        }

        if (id == 32238) {
            ItemDefinition.copyInventory(def, 3987);
            def.name = "<col=00ffff>Winter token";
            def.colorFind = new short[]{5813, 9139, 26006};
            def.colorReplace = new short[]{124, (short) 34770, 124};
        }

        if (id == 32239) {
            ItemDefinition.copyInventory(def, 3989);
            def.name = "<col=00ffff>Winter token";
            def.colorFind = new short[]{5813, 9139, 26006};
            def.colorReplace = new short[]{124, (short) 34770, 124};
        }

        if (id == 30222) {
            def.animateInventory = true;
            def.name = "<col=461770>Mystery ticket";
            def.interfaceOptions = new String[]{"Tear", null, null, null, null};
            def.colorFind = new short[]{7364, 11078, -327, -329, 7496, 7500};
            def.colorReplace = new short[]{(short) 374770, (short) 374770, -327, -329, (short) 374770, (short) 374770};
            def.inventoryModel = 55601;
            def.stackable = true;
            def.xan2d = 308;
            def.yan2d = 1888;
            def.zoom2d = 1160;
        }

        if (id == 30223) {
            def.name = "<col=65280>Blood money casket (100-250k)";
            def.inventoryModel = 69808;
            def.zoom2d = 2635;
            def.xan2d = 9;
            def.yan2d = 1922;
            def.zan2d = 9;
            def.xOffset2d = 0;
            def.yOffset2d = 0;
            def.animateInventory = true;
            def.interfaceOptions = new String[]{"Open", null, null, null, "Drop"};
            def.colorFind = new short[]{24, 49, 4510, 4502, 8128, 7093};
            def.colorReplace = new short[]{933, 933, 933, 933, 933, 933};
        }
        if (id == 29007) {
            ItemDefinition.copyInventory(def, FEROCIOUS_GLOVES);
            ItemDefinition.copyEquipment(def, FEROCIOUS_GLOVES);
            def.name = "@gre@Enchanted gloves";
            def.colorFind = new short[]{13493, 10411, 12484, 10394, 30643};
            def.colorReplace = new short[]{933, 933, 933, 933, 933};
        }
        if (id == 30326) {
            def.name = "<col=9b30ff>Enchanted helm";
            ItemDefinition.copyInventory(def, TANZANITE_HELM);
            ItemDefinition.copyEquipment(def, TANZANITE_HELM);
            def.colorFind = new short[]{(short) 49088,(short)  48055, 7700,(short)  33692,(short)  33676,(short)  33680, (short) 49083, (short) 47031, (short) 34714, (short) 33678, (short) 33686,
                29522, 3346, 29625, 30656, 29656, (short) 33672, 28597, (short) 33707, (short) 33701, (short) 33697, 11169, (short) 33699, (short) 33705, 15244, 7834};
            def.colorReplace = new short[]{933, 933, 933, 933, 933, 933, 933, 933, 933, 933, 933, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        }
        if (id == 30122) {
            def.interfaceOptions = new String[]{null, null, null, null, null};
            def.name = "<col=65280>Corrupt totem base";
            def.inventoryModel = 31621;
            def.yOffset2d = -4;
            def.xan2d = 188;
            def.yan2d = 108;
            def.zoom2d = 530;
            def.colorFind = new short[]{22290, -26664};
            def.colorReplace = new short[]{945, 582};
        }

        if (id == 30123) {
            def.interfaceOptions = new String[]{null, null, null, null, null};
            def.name = "<col=65280>Corrupt totem middle";
            def.inventoryModel = 31622;
            def.yOffset2d = -1;
            def.xan2d = 188;
            def.yan2d = 664;
            def.zoom2d = 480;
            def.colorFind = new short[]{-26664, -27417};
            def.colorReplace = new short[]{945, 582};
        }

        if (id == 30124) {
            def.interfaceOptions = new String[]{null, null, null, null, null};
            def.name = "<col=65280>Corrupt totem top";
            def.inventoryModel = 31623;
            def.xOffset2d = -3;
            def.yOffset2d = -1;
            def.xan2d = 111;
            def.yan2d = 194;
            def.zoom2d = 724;
            def.colorFind = new short[]{-26808, -26664, -26713, -26825};
            def.colorReplace = new short[]{945, 582, 712, 728};
        }

        if (id == 30125) {
            def.interfaceOptions = new String[]{null, null, null, null, null};
            def.name = "<col=65280>Corrupt totem";
            def.inventoryModel = 31620;
            def.xOffset2d = 2;
            def.yOffset2d = -3;
            def.xan2d = 152;
            def.yan2d = 111;
            def.zoom2d = 1150;
            def.colorFind = new short[]{22290, -26808, -26664, -26713, -26825, -27417};
            def.colorReplace = new short[]{945, 582, 152, 712, 728, -77};
        }

        if (id == 23759 || id == 22319 || id == 24491) {
            def.name = "<col=65280>" + def.name + " pet";
        }

        if (id == 25731) {
            def.femaleModel0 = 42276;
            def.interfaceOptions = new String[]{null, "Wield", null, null, null};
            def.inventoryModel = 42292;
            def.maleModel0 = 42271;
            def.name = "<col=65280>Holy sanguinesti staff";
            def.xOffset2d = -5;
            def.yOffset2d = 3;
            def.cost = 5000000;
            def.xan2d = 552;
            def.yan2d = 1558;
            def.zoom2d = 2258;
        }
        if (id == 25734) {
            def.colorFind = new short[]{90};
            def.colorReplace = new short[]{-9762};
            def.femaleModel0 = 42278;
            def.interfaceOptions = new String[]{null, "Wield", null, null, null};
            def.inventoryModel = 42294;
            def.maleModel0 = 42273;
            def.name = "<col=65280>Holy ghrazi rapier";
            def.xOffset2d = 5;
            def.yOffset2d = -18;
            def.cost = 5000000;
            def.xan2d = 1603;
            def.yan2d = 552;
            def.zoom2d = 2064;
        }
        if (id == 23214) {
            def.colorFind = new short[]{90, 6709, 6744, 6736};
            def.colorReplace = new short[]{-9762,(short)  86933, (short) 86933,(short)  86933};
            def.femaleModel0 = 42278;
            def.interfaceOptions = new String[]{null, "Wield", null, null, null};
            def.inventoryModel = 42294;
            def.maleModel0 = 42273;
            def.name = "<col=65280>Holy mending rapier";
            def.xOffset2d = 5;
            def.yOffset2d = -18;
            def.cost = 5000000;
            def.xan2d = 1603;
            def.yan2d = 552;
            def.zoom2d = 2064;
        }


        if (id == 25736) {
            def.femaleModel0 = 42270;
            def.interfaceOptions = new String[]{null, "Wield", "Un-attach", null, null};
            def.inventoryModel = 42293;
            def.maleModel0 = 42277;
            def.name = "<col=65280>Holy scythe of vitur";
            def.xOffset2d = 1;
            def.yOffset2d = 17;
            def.cost = 4000000;
            def.xan2d = 327;
            def.yan2d = 23;
            def.zoom2d = 2105;
        }

        if (id == 25739) {
            def.femaleModel0 = 42272;
            def.interfaceOptions = new String[]{null, "Wield", "Un-attach", null, null};
            def.inventoryModel = 42295;
            def.maleModel0 = 42279;
            def.name = "<col=65280>Sanguine scythe of vitur";
            def.xOffset2d = 1;
            def.yOffset2d = 17;
            def.cost = 4000000;
            def.xan2d = 327;
            def.yan2d = 23;
            def.zoom2d = 2105;
            def.modelCustomColor4 = 111;
        }

        if (id == 25753) {
            def.colorFind = new short[]{11191, 11183};
            def.colorReplace = new short[]{580, 557};
            def.interfaceOptions = new String[]{"Rub", null, null, null, "Drop"};
            def.inventoryModel = 3348;
            def.name = "<col=65280>99 lamp";
            def.xOffset2d = 2;
            def.yOffset2d = -2;
            def.xan2d = 28;
            def.yan2d = 228;
            def.zoom2d = 840;
        }

        if (id == 30315) {
            def.name = "<col=65280>Darklord bow";
            def.femaleModel0 = 59109;
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
            def.inventoryModel = 59108;
            def.maleModel0 = 59109;
            def.xOffset2d = 9;
            def.yOffset2d = 9;
            def.yan2d = 183;
            def.xan2d = 320;
            def.zoom2d = 2061;
        }

        if (id == 30309) {
            def.name = "<col=65280>Darklord sword";
            def.femaleModel0 = 59113;
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
            def.inventoryModel = 59112;
            def.maleModel0 = 59113;
            def.xOffset2d = 1;
            def.yOffset2d = 1;
            def.yan2d = 270;
            def.xan2d = 548;
            def.zoom2d = 2061;
        }

        if (id == 30312) {
            def.name = "<col=65280>Darklord staff";
            def.femaleModel0 = 59111;
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
            def.inventoryModel = 59110;
            def.maleModel0 = 59111;
            def.xOffset2d = 9;
            def.yOffset2d = 9;
            def.yan2d = 183;
            def.xan2d = 1713;
            def.zoom2d = 2061;
        }

        if (id == 30175) {
            def.name = "<col=65280>Ancestral hat (i)";
            def.colorFind = new short[]{6323, 6331, 6340, 6348, 6356, 6364, -21992, -22235};
            def.colorReplace = new short[]{15, 17, 19, 23, 25, 27, 29, (short) 491770};
            def.femaleHeadModel = 34263;
            def.femaleModel0 = 32663;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.inventoryModel = 32794;
            def.maleHeadModel = 32774;
            def.maleModel0 = 32655;
            def.yOffset2d = -12;
            def.xan2d = 118;
            def.yan2d = 10;
            def.zoom2d = 1236;
        }

        if (id == 30177) {
            def.name = "<col=65280>Ancestral robe top (i)";
            def.colorFind = new short[]{6348, -16318, 6331, -22225, 7108, -22235, -16327, -22231, -16339, 6323};
            def.colorReplace = new short[]{12, 19, 14, (short) 36133, 27,(short)  491770, 15, (short) 36133, 17, 8};
            def.femaleModel0 = 32664;
            def.femaleModel1 = 32665;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.inventoryModel = 32790;
            def.maleModel0 = 32657;
            def.maleModel1 = 32658;
            def.yOffset2d = -3;
            def.xan2d = 514;
            def.yan2d = 2041;
            def.zoom2d = 1358;
        }

        if (id == 30179) {
            def.name = "<col=65280>Ancestral robe bottom (i)";
            def.ambient = 30;
            def.colorFind = new short[]{-16339, 6348, -16327, 6331, -16318, -22225, -22235, 6323, -22231};
            def.colorReplace = new short[]{17, 12, 15, 14, 25, (short) 36133, (short) 491770, 13, (short) 491770};
            def.contrast = 20;
            def.femaleModel0 = 32662;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.inventoryModel = 32787;
            def.maleModel0 = 32653;
            def.xOffset2d = -1;
            def.yOffset2d = 7;
            def.xan2d = 435;
            def.yan2d = 9;
            def.zoom2d = 1690;
        }
        if (id == 30091) {//bloodrevsupdate
            def.name = "@gre@Mending life wand";
            ItemDefinition.copyInventory(def, KODAI_WAND);
            ItemDefinition.copyEquipment(def, KODAI_WAND);
            def.colorFind = new short[]{-19153, -16339, 37, -19145, -16331, -19500};
            def.colorReplace = new short[]{0, (short) 86933, 0, 0, 0, (short) 86933};
        }
        if (id == 30189) {
            def.name = "<col=461770>Vote boss mystery box";
            ItemDefinition.copyInventory(def, 6199);
            def.colorFind = new short[]{22410, 2999};
            def.colorReplace = new short[]{(short) 58933, 0};
            System.out.println(def.inventoryModel);
        }
        if (id == 30183) {
            def.name = "<col=65280>Twisted bow (i)";
            def.colorFind = new short[]{10318, 10334, 14236, 13223};
            def.colorReplace = new short[]{524, 527, 524, 527};
            def.femaleModel0 = 32674;
            def.interfaceOptions = new String[]{null, "Wield", "Un-attach", null, "Drop"};
            def.inventoryModel = 32799;
            def.maleModel0 = 32674;
            def.xOffset2d = -3;
            def.yOffset2d = 1;
            def.xan2d = 720;
            def.yan2d = 1500;
        }

        if (id == 30049) {
            def.name = "<col=65280>Magma blowpipe";
            def.femaleModel0 = 58976;
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
            def.inventoryModel = 58975;
            def.maleModel0 = 58976;
            def.xOffset2d = -7;
            def.yOffset2d = 4;
            def.xan2d = 768;
            def.yan2d = 189;
            def.zoom2d = 1158;
        }

        if (id == 15331) {
            def.name = "<col=65280>Ring of confliction";
            ItemDefinition.copyInventory(def, TYRANNICAL_RING);
            ItemDefinition.copyInventory(def, TYRANNICAL_RING);
            def.modelCustomColor4 = 333344;
        }

        if (id == 15300) {
            def.name = "<col=65280>Recover special (4)";
            ItemDefinition.copyInventory(def, 2436);
            def.colorReplace = new short[]{(short) 38222};
            def.colorFind = new short[]{61};
        }

        if (id == 15301) {
            def.name = "<col=65280>Recover special (3)";
            ItemDefinition.copyInventory(def, 145);
            def.colorReplace = new short[]{(short) 38222};
            def.colorFind = new short[]{61};
        }

        if (id == 15302) {
            def.name = "<col=65280>Recover special (2)";
            ItemDefinition.copyInventory(def, 147);
            def.colorReplace = new short[]{(short) 38222};
            def.colorFind = new short[]{61};
        }

        if (id == 15303) {
            def.name = "<col=65280>Recover special (1)";
            ItemDefinition.copyInventory(def, 149);
            def.colorReplace = new short[]{(short) 38222};
            def.colorFind = new short[]{61};
        }

        if (id == 27000) {
            ItemDefinition.copyInventory(def, HELLPUPPY);
            def.name = "<col=65280>Kerberos";
            def.modelCustomColor4 = 125;
        }

        if (id == 27001) {
            ItemDefinition.copyInventory(def, SCORPIAS_OFFSPRING);
            def.name = "<col=65280>Skorpios";
            def.modelCustomColor4 = 125;
        }

        if (id == 27002) {
            ItemDefinition.copyInventory(def, VENENATIS_SPIDERLING);
            def.name = "<col=65280>Arachne";
            def.modelCustomColor4 = 125;
        }

        if (id == 27003) {
            ItemDefinition.copyInventory(def, CALLISTO_CUB);
            def.name = "<col=65280>Artio";
            def.modelCustomColor4 = 125;
        }

        if (id == 27004) {
            def.name = "<col=65280>Blood money pet";
            ItemDefinition.copyInventory(def, 13316);
            def.stackable = false;
            def.colorFind = new short[]{8128};
            def.colorReplace = new short[]{947};
        }

        if (id == 27005) {
            def.name = "<col=65280>Ring of elysian";
            ItemDefinition.copyInventory(def, 23185);
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.colorFind = new short[]{7378, 8289, 8282, 7244};
            def.colorReplace = new short[]{-29116, -29019, -29125, -29110};
        }

        if (id == 27006) {
            def.name = "<col=65280>Toxic staff of the dead (i)";
            ItemDefinition.copyInventory(def, TOXIC_STAFF_OF_THE_DEAD);
            ItemDefinition.copyEquipment(def, TOXIC_STAFF_OF_THE_DEAD);
            def.modelCustomColor4 = 222255;
        }

        if (id == 15304) {
            def.name = "<col=65280>Ring of vigour";
            ItemDefinition.copyInventory(def, LUNAR_RING);
            ItemDefinition.copyEquipment(def, LUNAR_RING);
            def.modelCustomColor4 = 222200;
        }

        if (id == 32131) {
            def.name = "<col=65280>Bandos chestplate (g)";
            def.inventoryModel = 28042;
            def.zoom2d = 984;
            def.xan2d = 501;
            def.yan2d = 6;
            def.xOffset2d = 1;
            def.yOffset2d = 4;
            def.stackable = false;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.maleModel0 = 27636;
            def.maleModel1 = 28826;
            def.femaleModel0 = 27644;
            def.femaleModel1 = 28827;
            def.colorFind = new short[]{8384, 163, 10275, 10502, 4550, 9515, 8076, 142, 8367, 9523, 22, 8375, 10266, 9403, 8379};
            def.colorReplace = new short[]{7114, 7114, 28, 7114, 4550, 28, 124, 7114, 7114, 7114, 28, 7114, 28, 124, 7114};
        }

        if (id == 32133) {
            def.name = "<col=65280>Bandos tassets (g)";
            def.inventoryModel = 28047;
            def.zoom2d = 854;
            def.xan2d = 540;
            def.yan2d = 2039;
            def.xOffset2d = 3;
            def.yOffset2d = 3;
            def.stackable = false;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.maleModel0 = 27625;
            def.femaleModel0 = 27640;
            def.colorFind = new short[]{163, 9523, 4550, 22, 8390, 39, 154};
            def.colorReplace = new short[]{7114, 7114, 4550, 7114, 124, 28, 124};
        }

       /* if (id == 26500) {
            def.name = "<col=65280>Bandos chestplate (g)";
            def.inventory_model = 28042;
            def.model_zoom = 984;
            def.rotation_y = 501;
            def.rotation_x = 6;
            def.translate_x = 1;
            def.translate_y = 4;
            def.stackable = false;
            def.widget_actions = new String[]{null, "Wear", null, null, "Drop"};
            def.male_equip_main = 27636;
            def.male_equip_attachment = 28826;
            def.female_equip_main = 27644;
            def.female_equip_attachment = 28827;
            def.color_to_replace = new int[]{8384, 163, 10275, 10502, 4550, 9515, 8076, 142, 8367, 9523, 22, 8375, 10266, 9403, 8379};
            def.color_to_replace_with = new int[]{7114, 7114, 28, 7114, 4550, 28, 124, 7114, 7114, 7114, 28, 7114, 28, 124, 7114};
        }

        if (id == 26501) {
            def.name = "<col=65280>Bandos tassets (g)";
            def.inventory_model = 28047;
            def.model_zoom = 854;
            def.rotation_y = 540;
            def.rotation_x = 2039;
            def.translate_x = 3;
            def.translate_y = 3;
            def.stackable = false;
            def.widget_actions = new String[]{null, "Wear", null, null, "Drop"};
            def.male_equip_main = 27625;
            def.female_equip_main = 27640;
            def.color_to_replace = new int[]{163, 9523, 4550, 22, 8390, 39, 154};
            def.color_to_replace_with = new int[]{7114, 7114, 4550, 7114, 124, 28, 124};
        }*/

        if (id == 26502) {
            def.name = "<col=65280>Enchanted armadyl helmet";
            def.inventoryModel = 28043;
            def.zoom2d = 789;
            def.xan2d = 66;
            def.yan2d = 372;
            def.xOffset2d = 9;
            def.yOffset2d = 0;
            def.stackable = false;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.maleModel0 = 27623;
            def.femaleModel0 = 27639;
            def.maleHeadModel = 27699;
            def.femaleHeadModel = 27700;
            def.colorFind = new short[]{0, -22452, 4550, -22456, -22506, 8650, -22460, -22448};
            def.colorReplace = new short[]{0, -22452, 4550, -22456, -22506, (short) 374770, -22460, -22448};
        }

        if (id == 26503) {
            def.name = "<col=65280>Enchanted armadyl chestplate";
            def.inventoryModel = 28039;
            def.zoom2d = 854;
            def.xan2d = 453;
            def.yan2d = 0;
            def.xOffset2d = 1;
            def.yOffset2d = -5;
            def.stackable = false;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.maleModel0 = 27633;
            def.maleModel1 = 27629;
            def.femaleModel0 = 27645;
            def.femaleModel1 = 28828;
            def.colorFind = new short[]{8658, -22452, 4550, -22440, -22489, 8650, -22460, -22448, -22464};
            def.colorReplace = new short[]{(short) 374770, -22452, 4550, -22440, -22489, (short) 374770, -22460, -22448, -22464};
        }

        if (id == 26504) {
            def.name = "<col=65280>Enchanted armadyl chainskirt";
            def.inventoryModel = 28046;
            def.zoom2d = 1957;
            def.xan2d = 555;
            def.yan2d = 2036;
            def.yOffset2d = -3;
            def.stackable = false;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.maleModel0 = 27627;
            def.femaleModel0 = 27641;
            def.colorFind = new short[]{-22485, -22440, -22456, -22489, -22473, 8650, -22448, -22464};
            def.colorReplace = new short[]{-22485, -22440, -22456, -22489, -22473, (short) 374770, -22448, -22464};
        }

        if (id == 24937) {
            ItemDefinition.copyInventory(def, FAWKES);
            def.name = "<col=65280>Fawkes";
            def.modelCustomColor4 = 666600;
        }

        if (id == 24938) {
            ItemDefinition.copyInventory(def, VOID_KNIGHT_GLOVES);
            ItemDefinition.copyEquipment(def, VOID_KNIGHT_GLOVES);
            def.name = "<col=65280>Void knight gloves";
            def.modelCustomColor4 = 222200;
        }

        if (id == 24939) {
            ItemDefinition.copyInventory(def, VOID_RANGER_HELM);
            ItemDefinition.copyEquipment(def, VOID_RANGER_HELM);
            def.name = "<col=65280>Void ranger helm";
            def.modelCustomColor4 = 222200;
        }

        if (id == 24940) {
            ItemDefinition.copyInventory(def, VOID_MAGE_HELM);
            ItemDefinition.copyEquipment(def, VOID_MAGE_HELM);
            def.name = "<col=65280>Void mage helm";
            def.modelCustomColor4 = 222200;
        }

        if (id == 24941) {
            ItemDefinition.copyInventory(def, VOID_MELEE_HELM);
            ItemDefinition.copyEquipment(def, VOID_MELEE_HELM);
            def.name = "<col=65280>Void melee helm";
            def.modelCustomColor4 = 222200;
        }

        if (id == 24942) {
            ItemDefinition.copyInventory(def, ELITE_VOID_ROBE);
            ItemDefinition.copyEquipment(def, ELITE_VOID_ROBE);
            def.name = "<col=65280>Elite void robe";
            def.modelCustomColor4 = 222200;
        }

        if (id == 24943) {
            ItemDefinition.copyInventory(def, ELITE_VOID_TOP);
            ItemDefinition.copyEquipment(def, ELITE_VOID_TOP);
            def.name = "<col=65280>Elite void top";
            def.modelCustomColor4 = 222200;
        }

        if (id == 24944) {
            ItemDefinition.copyInventory(def, GRANITE_MAUL_24225);
            ItemDefinition.copyEquipment(def, GRANITE_MAUL_24225);
            def.name = "<col=65280>Granite maul";
            def.modelCustomColor4 = 23523;
        }

        if (id == 24945) {
            ItemDefinition.copyInventory(def, PARTYHAT__SPECS);
            ItemDefinition.copyEquipment(def, PARTYHAT__SPECS);
            def.name = "<col=65280>Partyhat & specs";
            def.modelCustomColor4 = 235;
        }

        if (id == 24946) {
            ItemDefinition.copyInventory(def, FREMENNIK_KILT);
            ItemDefinition.copyEquipment(def, FREMENNIK_KILT);
            def.name = "<col=65280>Fremennik kilt";
            def.modelCustomColor4 = 222200;
        }

        if (id == 24947) {
            ItemDefinition.copyInventory(def, SPIKED_MANACLES);
            ItemDefinition.copyEquipment(def, SPIKED_MANACLES);
            def.name = "<col=65280>Spiked manacles";
            def.modelCustomColor4 = 222222;
        }

        if (id == 24948) {
            ItemDefinition.copyInventory(def, ABYSSAL_TENTACLE);
            ItemDefinition.copyEquipment(def, ABYSSAL_TENTACLE);
            def.name = "<col=00ffff>Abyssal tentacle";
            def.modelCustomColor4 = 222200;
        }

        if (id == 24949) {
            def.name = "<col=65280>Dragon dagger(p++)";
            ItemDefinition.copyInventory(def, DRAGON_DAGGERP_5698);
            ItemDefinition.copyEquipment(def, DRAGON_DAGGERP_5698);
            def.modelCustomColor4 = 22459;
        }

        if (id == 18336) {
            def.name = "<col=ff4500>Molten partyhat";
            def.interfaceOptions = new String[5];
            def.interfaceOptions[1] = "Wear";
            def.zoom2d = 440;
            def.inventoryModel = 55602;
            def.animateInventory = true;
            def.xOffset2d = 1;
            def.yOffset2d = 1;
            def.yan2d = 1852;
            def.xan2d = 76;
            def.maleModel0 = 55603;
            def.femaleModel0 = 55604;
            def.colorFind = new short[]{926};
            def.colorReplace = new short[]{926};
            def.textureFind = new short[]{926};
            def.textureReplace = new short[]{54};
        }

        if (id == 24951) {
            def.name = "<col=65280>Lime partyhat";
            def.inventoryModel = 2635;
            def.maleModel0 = 187;
            def.femaleModel0 = 363;
            def.zoom2d = 440;
            def.yan2d = 1852;
            def.xan2d = 76;
            def.xOffset2d = 1;
            def.yOffset2d = 1;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.colorFind = new short[1];
            def.colorFind[0] = 926;
            def.colorReplace = new short[1];
            def.colorReplace[0] = 17350;
        }

        if (id == 24952) {
            def.name = "<col=65280>Orange partyhat";
            def.inventoryModel = 2635;
            def.maleModel0 = 187;
            def.femaleModel0 = 363;
            def.zoom2d = 440;
            def.yan2d = 1852;
            def.xan2d = 76;
            def.xOffset2d = 1;
            def.yOffset2d = 1;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.colorFind = new short[1];
            def.colorFind[0] = 926;
            def.colorReplace = new short[1];
            def.colorReplace[0] = 6073;
        }

        if (id == 24953) {
            def.name = "<col=65280>White h'ween mask";
            def.inventoryModel = 2438;
            def.maleModel0 = 3188;
            def.femaleModel0 = 3192;
            def.zoom2d = 730;
            def.xan2d = 516;
            def.yan2d = 0;
            def.yOffset2d = -10;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.colorFind = new short[2];
            def.colorFind[0] = 926;
            def.colorFind[1] = 0;
            def.colorReplace = new short[2];
            def.colorReplace[0] = 124; // Background colour
            def.colorReplace[1] = 933; // Eyes colour
        }

        if (id == 24954) {
            def.name = "<col=65280>Purple h'ween mask";
            def.inventoryModel = 2438;
            def.maleModel0 = 3188;
            def.femaleModel0 = 3192;
            def.zoom2d = 730;
            def.xan2d = 516;
            def.yan2d = 0;
            def.yOffset2d = -10;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.colorFind = new short[2];
            def.colorFind[0] = 926;
            def.colorFind[1] = 0;
            def.colorReplace = new short[2];
            def.colorReplace[0] = (short) 51136; // Background colour
            def.colorReplace[1] = 0; // Eyes colour
        }

        if (id == 24955) {
            def.name = "<col=65280>Lime green h'ween mask";
            def.inventoryModel = 2438;
            def.maleModel0 = 3188;
            def.femaleModel0 = 3192;
            def.zoom2d = 730;
            def.xan2d = 516;
            def.yan2d = 0;
            def.yOffset2d = -10;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.colorFind = new short[2];
            def.colorFind[0] = 926;
            def.colorFind[1] = 0;
            def.colorReplace = new short[2];
            def.colorReplace[0] = 17350; // Background colour
            def.colorReplace[1] = 0; // Eyes colour
        }

        if (id == 24956) {
            ItemDefinition.copyInventory(def, ELDER_MAUL);
            ItemDefinition.copyEquipment(def, ELDER_MAUL);
            def.name = "<col=65280>Enchanted maul";
            def.modelCustomColor4 = 444400;
        }

        if (id == 28957) {
            ItemDefinition.copyInventory(def, TWISTED_BOW);
            ItemDefinition.copyEquipment(def, TWISTED_BOW);
            def.name = "<col=65280>Sanguine twisted bow";
            def.interfaceOptions = new String[]{null, "Wield", null, "Un-attach", "Drop"};
            def.modelCustomColor4 = 1111;
            def.stackable = false;
        }
        if (id == 621) {
            def.name = "<col=65280> Pk Ticket";
            def.stackable = true;
        }
        if (id == 16475) {
            def.name = "@yel@Activity chest";
            def.copyInventory(def, 8151);
            def.interfaceOptions = new String[]{"Open", null, null, null, "Drop"};
            def.colorFind = new short[]{24, 49, 4510, 4502, 8128, 7093};
            def.colorReplace = new short[]{6837, 6837, 6837, 6837, 6837, 6837};
        }
        if (id == 24958) {
            ItemDefinition.copyInventory(def, ELDER_MAUL);
            ItemDefinition.copyEquipment(def, ELDER_MAUL);
            def.name = "<col=65280>Dark elder maul";
            def.modelCustomColor4 = 222200;
        }

        if (id == 24981) {
            ItemDefinition.copyInventory(def, 7807);
            ItemDefinition.copyEquipment(def, 7807);
            def.interfaceOptions = new String[]{null, "Wield", null, "Dismantle", "Drop"};
            def.name = "<col=65280>Ancient warrior axe (c)";
            def.modelCustomColor3 = 23532;
        }

        if (id == 24982) {
            ItemDefinition.copyInventory(def, 7808);
            ItemDefinition.copyEquipment(def, 7808);
            def.interfaceOptions = new String[]{null, "Wield", null, "Dismantle", "Drop"};
            def.name = "<col=65280>Ancient warrior maul (c)";
            def.modelCustomColor3 = 23532;
        }

        if (id == 24983) {
            ItemDefinition.copyInventory(def, 7806);
            ItemDefinition.copyEquipment(def, 7806);
            def.interfaceOptions = new String[]{null, "Wield", null, "Dismantle", "Drop"};
            def.name = "<col=65280>Ancient warrior sword (c)";
            def.modelCustomColor3 = 23532;
        }

        if (id == 24984) {
            ItemDefinition.copyInventory(def, NEITIZNOT_FACEGUARD);
            ItemDefinition.copyEquipment(def, NEITIZNOT_FACEGUARD);
            def.name = "<col=65280>Enchanted faceguard";
            def.modelCustomColor4 = 444400;
        }

        if (id == 24985) {
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.name = "<col=65280>Ancient warrior clamp";
            def.inventoryModel = 55580;
            def.zoom2d = 1280;
            def.xOffset2d = -22;
            def.yOffset2d = -7;
            def.yan2d = 0;
            def.xan2d = 0;
            def.stackable = false;
            def.animateInventory = true;
        }

        if (id == 786) {
            def.name = "<col=65280>Gambler scroll";
            def.interfaceOptions = new String[]{"Redeem", null, null, null, "Drop"};
        }

        if (id == 24986) {
            ItemDefinition.copyInventory(def, PRINCE_BLACK_DRAGON);
            def.name = "<col=65280>Ancient king black dragon";
            def.modelCustomColor4 = 235;
        }

        if (id == 24987) {
            ItemDefinition.copyInventory(def, CHAOS_ELEMENTAL);
            def.name = "<col=65280>Ancient chaos elemental";
            def.modelCustomColor4 = 235;
        }

        if (id == 24988) {
            ItemDefinition.copyInventory(def, BARRELCHEST_PET);
            def.name = "<col=65280>Ancient barrelchest";
            def.modelCustomColor4 = 235;
        }

        if (id == 24989) {
            ItemDefinition.copyInventory(def, ANCIENT_EMBLEM);
            def.name = "<col=65280>Dark ancient emblem";
            def.modelCustomColor4 = 235;
        }
        if (id == 24990) {
            ItemDefinition.copyInventory(def, ANCIENT_TOTEM);
            def.name = "<col=65280>Dark ancient totem";
            def.modelCustomColor4 = 235;
        }
        if (id == 24991) {
            ItemDefinition.copyInventory(def, ANCIENT_STATUETTE);
            def.name = "<col=65280>Dark ancient statuette";
            def.modelCustomColor4 = 235;
        }
        if (id == 24992) {
            ItemDefinition.copyInventory(def, ANCIENT_MEDALLION);
            def.name = "<col=65280>Dark ancient medallion";
            def.modelCustomColor4 = 235;
        }
        if (id == 24993) {
            ItemDefinition.copyInventory(def, ANCIENT_EFFIGY);
            def.name = "<col=65280>Dark ancient effigy";
            def.modelCustomColor4 = 235;
        }
        if (id == 24994) {
            ItemDefinition.copyInventory(def, ANCIENT_RELIC);
            def.name = "<col=65280>Dark ancient relic";
            def.modelCustomColor4 = 235;
        }
        if (id == 24995) {
            ItemDefinition.copyInventory(def, VESTAS_LONGSWORD);
            ItemDefinition.copyEquipment(def, VESTAS_LONGSWORD);
            def.name = "<col=65280>Ancient vesta's longsword";
            def.modelCustomColor4 = 235;
        }
        if (id == 24996) {
            ItemDefinition.copyInventory(def, STATIUSS_WARHAMMER);
            ItemDefinition.copyEquipment(def, STATIUSS_WARHAMMER);
            def.name = "<col=65280>Ancient statius' warhammer";
            def.modelCustomColor4 = 235;
        }

        if (id == 24999) {
            def.name = "<col=65280>Blood money casket (5-50k)";
            def.inventoryModel = 69808;
            def.zoom2d = 2635;
            def.xan2d = 9;
            def.yan2d = 1922;
            def.zan2d = 9;
            def.xOffset2d = 0;
            def.yOffset2d = 0;
            def.animateInventory = true;
            def.interfaceOptions = new String[]{"Open", null, null, null, "Drop"};
        }

        if (id == 28000) {
            def.name = "<col=65280>Blood firebird";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.inventoryModel = 26853;
            def.zoom2d = 2340;
            def.yan2d = 210;
            def.xan2d = 10;
            def.xOffset2d = -2;
            def.yOffset2d = 9;
            def.colorFind = new short[]{10176, 2880, 6082, 6084, 23492, 2983, 4391, 8, 29867, 4011, 4013, 2733, 2735, 4399, 914, 20, 8150, 10167, 1946, 23483, 28, 5053};
            def.colorReplace = new short[]{933, 933, 933, 933, 933, 933, 4391, 8, 29867, 4011, 4013, 2733, 2735, 4399, 914, 20, 8150, 10167, 1946, 23483, 28, 5053};
        }

        if (id == 28001) {
            def.ambient = 15;
            def.contrast = 35;
            def.femaleModel0 = 55555;
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
            def.inventoryModel = 55557;
            def.maleModel0 = 55555;
            def.xOffset2d = 1;
            def.yOffset2d = 16;
            def.cost = 5000000;
            def.xan2d = 348;
            def.zoom2d = 1642;
            def.name = "<col=65280>Shadow mace";
            def.modelCustomColor4 = 11111;
        }

        if (id == 28002) {
            ItemDefinition.copyInventory(def, INQUISITORS_GREAT_HELM);
            ItemDefinition.copyEquipment(def, INQUISITORS_GREAT_HELM);
            def.name = "<col=65280>Shadow great helm";
            def.modelCustomColor4 = 11111;
        }

        if (id == 28003) {
            ItemDefinition.copyInventory(def, INQUISITORS_HAUBERK);
            ItemDefinition.copyEquipment(def, INQUISITORS_HAUBERK);
            def.name = "<col=65280>Shadow hauberk";
            def.modelCustomColor4 = 11111;
        }

        if (id == 28004) {
            ItemDefinition.copyInventory(def, INQUISITORS_PLATESKIRT);
            ItemDefinition.copyEquipment(def, INQUISITORS_PLATESKIRT);
            def.name = "<col=65280>Shadow plateskirt";
            def.modelCustomColor4 = 11111;
        }

        if (id == 28005) {
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.name = "<col=65280>Shadow inquisitor ornament kit";
            def.zoom2d = 1616;
            def.yan2d = 1943;
            def.xan2d = 564;
            def.xOffset2d = -10;
            def.yOffset2d = 20;
            def.inventoryModel = 31973;
            def.colorReplace = new short[]{10, 0, 1, 1, 1, 1, 1, 1, 1, 1};
            def.colorFind = new short[]{7607, 0, 908, (short) 54162, (short) 41137, (short) 41149,(short)  41143, 6998, (short) 40107, 14734};
        }

        if (id == 28006) {
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.name = "<col=65280>Inquisitor's mace ornament kit";
            def.zoom2d = 1616;
            def.yan2d = 1943;
            def.xan2d = 564;
            def.xOffset2d = -10;
            def.yOffset2d = 20;
            def.inventoryModel = 55556;
            def.animateInventory = true;
            def.colorReplace = new short[]{10, 0, 1, 1, 1, 1, 1, 1, 1, 1};
            def.colorFind = new short[]{7607, 0, 908,(short)  54162, (short) 41137, (short) 41149, (short) 41143, 6998,(short)  40107, 14734};
        }

        if (id == 28007) {
            def.name = "<col=65280>Ethereal partyhat";
            def.inventoryModel = 2635;
            def.maleModel0 = 187;
            def.femaleModel0 = 363;
            def.zoom2d = 440;
            def.yan2d = 1852;
            def.xan2d = 76;
            def.xOffset2d = 1;
            def.yOffset2d = 1;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.colorFind = new short[1];
            def.colorFind[0] = 926;
            def.colorReplace = new short[1];
            def.colorReplace[0] = (short)  374770;
        }

        if (id == 28008) {
            def.name = "<col=65280>Ethereal h'ween mask";
            def.inventoryModel = 2438;
            def.maleModel0 = 3188;
            def.femaleModel0 = 3192;
            def.zoom2d = 730;
            def.xan2d = 516;
            def.yan2d = 0;
            def.yOffset2d = -10;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.colorFind = new short[2];
            def.colorFind[0] = 926;
            def.colorFind[1] = 0;
            def.colorReplace = new short[2];
            def.colorReplace[0] = (short) 374770; // Background colour
            def.colorReplace[1] = 933; // Eyes colour
        }

        if (id == 28009) {
            def.name = "<col=65280>Ethereal santa hat";
            def.inventoryModel = 2537;
            def.zoom2d = 540;
            def.xan2d = 72;
            def.yan2d = 136;
            def.xOffset2d = 0;
            def.yOffset2d = -3;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.maleModel0 = 189;
            def.femaleModel0 = 366;
            def.maleHeadModel = 69;
            def.femaleHeadModel = 127;
            def.colorFind = new short[]{933, 10351};
            def.colorReplace = new short[]{(short) 374770, 10351};
        }

        if (id == 14479) {
            def.name = "<col=65280>Beginner weapon pack";
            def.inventoryModel = 20587;
            def.xOffset2d = 0;
            def.yOffset2d = 12;
            def.yan2d = 456;
            def.xan2d = 318;
            def.zoom2d = 2216;
            def.interfaceOptions = new String[]{"Open", null, null, null, "Destroy"};
        }

        if (id == 14486) {
            def.name = "<col=65280>Beginner dragon claws";
            def.femaleModel0 = 29191;
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Destroy"};
            def.inventoryModel = 32784;
            def.maleModel0 = 29191;
            def.xOffset2d = -1;
            def.yOffset2d = 8;
            def.xan2d = 349;
            def.yan2d = 15;
            def.zoom2d = 886;
            def.colorFind = new short[]{929, 914, 918, 922};
            def.colorReplace = new short[]{(short) 34770,(short)  34770, (short) 34770, (short) 34770};
        }

        if (id == 14487) {
            def.name = "<col=65280>Beginner AGS";
            def.femaleModel0 = 27649;
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Destroy"};
            def.inventoryModel = 28075;
            def.maleModel0 = 27649;
            def.xOffset2d = -1;
            def.yOffset2d = -1;
            def.xan2d = 498;
            def.yan2d = 484;
            def.zoom2d = 1957;
            def.colorFind = new short[]{-22419, -24279, -22423, -22444, -22477, -24271, -22415, -22208, -22464};
            def.colorReplace = new short[]{(short) 34770, (short) 34770,(short)  34770, (short) 34770, (short) 34770, (short) 34770,(short)  34770, (short) 34770, (short) 34770};
        }

        if (id == 14488) {
            def.name = "<col=65280>Beginner chainmace";
            def.femaleModel0 = 35771;
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Destroy"};
            def.inventoryModel = 35780;
            def.maleModel0 = 35771;
            def.xOffset2d = 11;
            def.yOffset2d = -8;
            def.xan2d = 1495;
            def.yan2d = 256;
            def.zoom2d = 1488;
            def.colorFind = new short[]{16, -11234, -11238, -11242, -11246, -10719};
            def.colorReplace = new short[]{(short) 34770, (short) 34770,(short)  34770, (short) 34770, (short) 34770, (short) 34770};
        }

        if (id == 14489) {
            def.name = "<col=65280>Beginner craw's bow";
            def.femaleModel0 = 35769;
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Destroy"};
            def.inventoryModel = 35775;
            def.maleModel0 = 35769;
            def.xOffset2d = -1;
            def.yOffset2d = -3;
            def.xan2d = 1463;
            def.yan2d = 510;
            def.zan2d = 835;
            def.zoom2d = 1979;
            def.colorFind = new short[]{-22242, -6087, -22440, 6602, 6699, -22448, 6736, -22225, 3346, -6099, 7124, 6709, -22431};
            def.colorReplace = new short[]{(short) 34770,(short)  34770, (short) 34770, (short) 34770, (short) 34770,(short)  34770,(short)  34770,(short)  34770, (short) 34770, (short) 34770,(short)  34770,(short)  34770, (short) 34770};
        }

        if (id == 12810) {
            def.name = "<col=65280>Darklord helm";
            def.modelCustomColor4 = 33785;
        }

        if (id == 12811) {
            def.name = "<col=65280>Darklord platebody";
            def.modelCustomColor4 = 33785;
        }

        if (id == 12812) {
            def.name = "<col=65280>Darklord platelegs";
        }

        if (id == 28013) {
            def.name = "<col=65280>Veteran partyhat";
            def.inventoryModel = 2635;
            def.maleModel0 = 187;
            def.femaleModel0 = 363;
            def.zoom2d = 440;
            def.yan2d = 1852;
            def.xan2d = 76;
            def.xOffset2d = 1;
            def.yOffset2d = 1;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.colorFind = new short[1];
            def.colorFind[0] = 926;
            def.colorReplace = new short[1];
            def.colorReplace[0] = (short) 614770;
        }

        if (id == 28014) {
            def.name = "<col=65280>Veteran h'ween mask";
            def.inventoryModel = 2438;
            def.maleModel0 = 3188;
            def.femaleModel0 = 3192;
            def.zoom2d = 730;
            def.xan2d = 516;
            def.yan2d = 0;
            def.yOffset2d = -10;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.colorFind = new short[2];
            def.colorFind[0] = 926;
            def.colorFind[1] = 0;
            def.colorReplace = new short[2];
            def.colorReplace[0] = (short) 614770; // Background colour
            def.colorReplace[1] = 933; // Eyes colour
        }

        if (id == 28015) {
            def.name = "<col=65280>Veteran santa hat";
            def.inventoryModel = 2537;
            def.zoom2d = 540;
            def.xan2d = 72;
            def.yan2d = 136;
            def.xOffset2d = 0;
            def.yOffset2d = -3;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.maleModel0 = 189;
            def.femaleModel0 = 366;
            def.maleHeadModel = 69;
            def.femaleHeadModel = 127;
            def.colorFind = new short[]{933, 10351};
            def.colorReplace = new short[]{(short) 614770, 10351};
        }

        if (id == 30180) {
            ItemDefinition.copyInventory(def, PEGASIAN_BOOTS);
            ItemDefinition.copyEquipment(def, PEGASIAN_BOOTS);
            def.name = "<col=65280>Pegasian boots (or)";
            def.colorFind = new short[]{8481, 17746, 15252, 16549, 8493, 17294};
            def.colorReplace = new short[]{7114, 7114, 15252, 7114, 7114, 17294};
        }

        if (id == 30182) {
            ItemDefinition.copyInventory(def, ETERNAL_BOOTS);
            ItemDefinition.copyEquipment(def, ETERNAL_BOOTS);
            def.name = "<col=65280>Eternal boots (or)";
            def.colorFind = new short[]{9152, -22242, -22326, -10839, -22248, 695, -22361, -22510};
            def.colorReplace = new short[]{9152, -22242, 7114, 7114, 7114, 695, 7114, -22510};
        }

        if (id == 29000) {
            ItemDefinition.copyInventory(def, VIGGORAS_CHAINMACE);
            ItemDefinition.copyEquipment(def, VIGGORAS_CHAINMACE);
            def.name = "<col=65280>Viggora's chainmace (c)";
            def.colorFind = new short[]{16, -11234, -11238, -11242, -11246, -10719};
            def.colorReplace = new short[]{16, 1255, 1255, 5, 5, 1255};
        }

        if (id == 29001) {
            ItemDefinition.copyInventory(def, CRAWS_BOW);
            ItemDefinition.copyEquipment(def, CRAWS_BOW);
            def.name = "<col=65280>Craw's bow (c)";
            def.colorFind = new short[]{-22242, -6087, -22440, 6602, 6699, -22448, 6736, -22225, 3346, -6099, 7124, 6709, -22431};
            def.colorReplace = new short[]{7, 1, 7, 1255, 10, 1255, 1255, 10, 5, 1255, 1255, 1255, 10};
        }

        if (id == 29002) {
            ItemDefinition.copyInventory(def, THAMMARONS_SCEPTRE);
            ItemDefinition.copyEquipment(def, THAMMARONS_SCEPTRE);
            def.name = "<col=65280>Thammaron's sceptre (c)";
            def.colorFind = new short[]{960, 33, 417, 20, 53, 555, 939, 12, 28, 284};
            def.colorReplace = new short[]{10, 15, 1, 15, 1400, 5, 1, 10, 1400, 1400};
        }

        if (id == 2396) {
            def.name = "<col=65280>3% drop rate boost scroll";
        }

        if (id == 13215) {
            ItemDefinition.copyInventory(def, 13204);
            def.name = "<col=65280>Bloody token";
            def.colorFind = new short[]{5813, 9139, 26006};
            def.colorReplace = new short[]{947, 948, 949};
            def.countCo = new int[]{2, 3, 4, 5, 0, 0, 0, 0, 0, 0};
            def.countObj = new int[]{13216, 13217, 13218, 13218, 0, 0, 0, 0, 0, 0};
        }

        if (id == 13216) {
            ItemDefinition.copyInventory(def, 3985);
            def.name = "<col=65280>Bloody token";
            def.colorFind = new short[]{5813, 9139, 26006};
            def.colorReplace = new short[]{947, 948, 949};
        }

        if (id == 13217) {
            ItemDefinition.copyInventory(def, 3987);
            def.name = "<col=65280>Bloody token";
            def.colorFind = new short[]{5813, 9139, 26006};
            def.colorReplace = new short[]{947, 948, 949};
        }

        if (id == 13218) {
            ItemDefinition.copyInventory(def, 3989);
            def.name = "<col=65280>Bloody token";
            def.colorFind = new short[]{5813, 9139, 26006};
            def.colorReplace = new short[]{947, 948, 949};
        }

        if (id == ItemIdentifiers.TOXIC_BLOWPIPE || id == ItemIdentifiers.SERPENTINE_HELM || id == ItemIdentifiers.TRIDENT_OF_THE_SWAMP || id == ItemIdentifiers.TOXIC_STAFF_OF_THE_DEAD
            || id == ItemIdentifiers.TOME_OF_FIRE || id == ItemIdentifiers.SCYTHE_OF_VITUR || id == ItemIdentifiers.SANGUINESTI_STAFF || id == ItemIdentifiers.CRAWS_BOW
            || id == ItemIdentifiers.VIGGORAS_CHAINMACE || id == ItemIdentifiers.THAMMARONS_SCEPTRE || id == ItemIdentifiers.TRIDENT_OF_THE_SEAS || id == ItemIdentifiers.MAGMA_HELM
            || id == ItemIdentifiers.TANZANITE_HELM || id == ItemIdentifiers.DRAGONFIRE_SHIELD || id == ItemIdentifiers.DRAGONFIRE_WARD || id == ItemIdentifiers.ANCIENT_WYVERN_SHIELD
            || id == ItemIdentifiers.ABYSSAL_TENTACLE || id == 26484 || id == ItemIdentifiers.BARRELCHEST_ANCHOR || id == ItemIdentifiers.SARADOMINS_BLESSED_SWORD) {
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
        }

        if (id == 16167) {
            ItemDefinition.copyInventory(def, 24731);
            ItemDefinition.copyEquipment(def, 24731);
            def.name = "<col=65280>Alchemist's ring";
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.modelCustomColor4 = 444400;
        }

        if (id == 16168) {
            def.name = "<col=65280>Deadeye's ring";
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            ItemDefinition.copyInventory(def, 12601);
            ItemDefinition.copyEquipment(def, 12601);
            def.modelCustomColor4 = 244444;
        }

        if (id == 16169) {
            def.name = "<col=65280>Ring of perfection";
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            ItemDefinition.copyInventory(def, 12603);
            ItemDefinition.copyEquipment(def, 12603);
            def.modelCustomColor4 = 244444;
        }

        if (id == 16172) {
            def.name = "<col=65280>Baby aragog";
            ItemDefinition.copyInventory(def, VENENATIS_SPIDERLING);
            def.colorFind = new short[]{912, 0, 916, 103, 138, 794, 107, 908};
            def.colorReplace = new short[]{138, 908, 4769, 4769, 4769, 0, 0, 0};
        }

        if (id == 16173) {
            def.name = "<col=65280>Jawa";
            ItemDefinition.copyInventory(def, ATTACK_HOOD);
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.modelCustomColor = 4769;
        }

        if (id == 21205) {
            ItemDefinition.copyInventory(def, 21003);
            ItemDefinition.copyEquipment(def, 21003);
            def.name = "<col=65280>Elder maul";
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Destroy"};
            def.colorFind = new short[]{5056, 8125};
            def.colorReplace = new short[]{7114, 7114};
        }

        if (id == 23490) {
            def.name = "<col=65280>Larran's key tier I";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.countCo = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            def.countObj = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        }

        if (id == 14900) {
            def.name = "<col=65280>Larran's key tier II";
            def.animateInventory = true;
            def.inventoryModel = 55560;
            def.colorFind = new short[]{8784, 2974, 8640};
            def.colorReplace = new short[]{8107, 8103, 9129};
            def.zoom2d = 968;
            def.xan2d = 512;
            def.yan2d = 741;
            def.zan2d = 1980;
            def.xOffset2d = -7;
            def.yOffset2d = 3;
            def.stackable = true;
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.colorFind = new short[]{8784, 2974, 8640};
            def.colorReplace = new short[]{8107, 8103, 9129};
        }

        if (id == 14901) {
            def.name = "<col=65280>Larran's key tier III";
            def.animateInventory = true;
            def.inventoryModel = 55561;
            def.colorFind = new short[]{8784, 2974, 8640};
            def.colorReplace = new short[]{8107, 8103, 9129};
            def.zoom2d = 968;
            def.xan2d = 512;
            def.yan2d = 741;
            def.zan2d = 1980;
            def.xOffset2d = -7;
            def.yOffset2d = 3;
            def.stackable = true;
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.colorFind = new short[]{8784, 2974, 8640};
            def.colorReplace = new short[]{8107, 8103, 9129};
        }

        if (id == 16020) {
            def.name = "<col=65280>Dharok Jr.";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.zoom2d = 1000;
            def.xan2d = 100;
            def.yan2d = 100;
            def.xOffset2d = 0;
            def.yOffset2d = 100;
            NpcDefinition npcInstance = NpcDefinition.get(NpcIdentifiers.DHAROK_THE_WRETCHED);
            def.inventoryModel = npcInstance.models[0];
        }

        if (id == ItemIdentifiers.ARMADYL_GODSWORD_OR || id == ItemIdentifiers.BANDOS_GODSWORD_OR || id == ItemIdentifiers.SARADOMIN_GODSWORD_OR || id == ItemIdentifiers.ZAMORAK_GODSWORD_OR || id == ItemIdentifiers.GRANITE_MAUL_12848 || id == DRAGON_CLAWS_OR) {
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
        }

        if (id == ItemIdentifiers.ATTACKER_ICON || id == ItemIdentifiers.COLLECTOR_ICON || id == ItemIdentifiers.DEFENDER_ICON || id == ItemIdentifiers.HEALER_ICON || id == ItemIdentifiers.AMULET_OF_FURY_OR || id == ItemIdentifiers.OCCULT_NECKLACE_OR || id == ItemIdentifiers.NECKLACE_OF_ANGUISH_OR || id == ItemIdentifiers.AMULET_OF_TORTURE_OR || id == ItemIdentifiers.BERSERKER_NECKLACE_OR || id == ItemIdentifiers.TORMENTED_BRACELET_OR || id == ItemIdentifiers.DRAGON_DEFENDER_T || id == ItemIdentifiers.DRAGON_BOOTS_G) {
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Destroy"};
        }

        if (id == 2944) {
            def.name = "<col=65280>Key of boxes";
        }
        if (id == 2955) {//update19
            def.name = "<col=65280>Grand key";
            ItemDefinition.copyInventory(def, 2944);
            def.inventoryModel = 69801;
            def.ambient = 80;
            def.animateInventory = true;
        }

        if (id == 298) {
            ItemDefinition.copyInventory(def, 298);
            def.name = "<col=65280>Wilderness Boss Key";
            def.interfaceOptions = new String[]{"Summon", null, null, null, "Drop"};
            def.inventoryModel = 69827;
            def.animateInventory = true;
        }

        if (id == 24445) {
            def.name = "<col=65280>Twisted slayer helmet (i)";
            def.femaleHeadModel = 38997;
            def.femaleModel0 = 38970;
            def.interfaceOptions = new String[]{null, "Wear", null, "Disassemble", "Drop"};
            def.inventoryModel = 38958;
            def.maleHeadModel = 38997;
            def.maleModel0 = 38960;
            def.xOffset2d = -4;
            def.yOffset2d = -3;
            def.xan2d = 30;
            def.yan2d = 1773;
            def.zoom2d = 779;
            def.colorFind = new short[]{16, 14272, 33, 10306, 37, 4550, 10343, 24, 10312, 12, 10334, 10318};
            def.colorReplace = new short[]{8, 6073, 8, 10306, 8, 4550, 10343, 8, 10312, 8, 10334, 10318};
        }

        if (id == ItemIdentifiers.BANK_KEY) {
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
        }

        if (id == 20238) {
            def.name = "<col=65280>Imbuement scroll";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.description = "Use this scroll to imbue certain items.";
        }

        if (id == 12646) {
            def.inventoryModel = 69819;
            def.animateInventory = true;
            def.name = "<col=65280>Niffler";
        }
        if (id == 29116) {//up02
            ItemDefinition.copyInventory(def, NIFFLER);
            def.name = "<col=65280>Ziffler";
            def.inventoryModel = 69820;
            def.animateInventory = true;
        }
        if (id == 20693) {
            def.name = "<col=65280>Fawkes";
        }

        if (id == 28663) {
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.name = "<col=65280>Zriawk";
            def.modelCustomColor4 = 33235;
            def.ambient = 40;
            def.inventoryModel = 26852;
            def.xOffset2d = -8;
            def.yOffset2d = -13;
            def.xan2d = 141;
            def.yan2d = 1790;
            def.zoom2d = 2768;
        }

        if (id == 28639) {
            def.name = "<col=65280>Elder wand handle";
            def.colorFind = new short[]{-19153, 33, -19145, -19500};
            def.colorReplace = new short[]{530, 540, 529, 10};
            def.interfaceOptions = new String[]{"Inspect", null, null, null, "Drop"};
            def.inventoryModel = 32805;
            def.xOffset2d = -1;
            def.yOffset2d = -1;
            def.xan2d = 606;
            def.yan2d = 498;
            def.zoom2d = 716;
        }

        if (id == 28640) {
            def.name = "<col=65280>Elder wand stick";
            def.colorFind = new short[]{9024, 9009, 5652, 8070, 9015, 7050, 4634, -22413, 8877, 3614};
            def.colorReplace = new short[]{530, 540, 529, 10, 13, 16, 19, 22, 25, 28};
            def.interfaceOptions = new String[]{"Inspect", null, null, null, "Drop"};
            def.inventoryModel = 10565;
            def.xOffset2d = 6;
            def.yOffset2d = -7;
            def.xan2d = 68;
            def.yan2d = 1092;
            def.zoom2d = 540;
        }

        if (id == 28641) {
            def.name = "<col=65280>Talonhawk crossbow";
            def.colorFind = new short[]{49, 10471, 10475};
            def.colorReplace = new short[]{33, 33, 124};
            def.femaleModel0 = 15472;
            def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
            def.inventoryModel = 15493;
            def.maleModel0 = 15472;
            def.xOffset2d = 2;
            def.yOffset2d = 8;
            def.xan2d = 444;
            def.yan2d = 1658;
            def.zoom2d = 1104;
        }

        if (id == 28642) {
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.name = "<col=65280>Fluffy Jr.";
            def.colorFind = new short[]{0, 11200, 929, 931, 9542, 902, 262, 906, 910, 914, 918, 922, 955, 9149, 7101, 8125, 6077, 4029, 957, 1981, 926};
            def.colorReplace = new short[]{4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 0, 4769, 0, 0, 4769, 4769};
            def.inventoryModel = 29240;
            def.xan2d = 3;
            def.yan2d = 213;
            def.zoom2d = 1616;
        }

        if (id == 30338) {
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.name = "<col=65280>Centaur";
            def.inventoryModel = 16213;
            def.zoom2d = 3500;
            def.xan2d = 25;
            def.yan2d = 25;
            def.yOffset2d = -10;
        }

        if (id == 30016) {
            def.name = "<col=65280>Founder imp";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.inventoryModel = 58916;
            def.xOffset2d = 11;
            def.yOffset2d = -1;
            def.xan2d = 116;
            def.yan2d = 1778;
            def.zoom2d = 1424;
        }

        if (id == 30018) {
            def.name = "<col=65280>Corrupted nechryarch";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.inventoryModel = 58922;
            def.zoom2d = 7000;
            def.xan2d = 25;
            def.yan2d = 25;
            def.yOffset2d = -10;
        }

        if (id == 30033) {
            def.name = "<col=65280>Mini necromancer";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.inventoryModel = 58979;
            def.yOffset2d = -12;
            def.xan2d = 118;
            def.yan2d = 10;
            def.zoom2d = 1136;
        }

        if (id == 30048) {
            def.name = "<col=65280>Corrupted gauntlets";
            def.femaleModel0 = 36335;
            def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
            def.inventoryModel = 36141;
            def.maleModel0 = 36325;
            def.xOffset2d = -1;
            def.xan2d = 420;
            def.yan2d = 1082;
            def.zoom2d = 917;
            def.colorFind = new short[]{16, 30643, 12484, 13493, -32630, 8, 24, 10411, 12};
            def.colorReplace = new short[]{968, (short) 130770, (short) 130770, (short) 130770, 0, 0, 0, 968, 0};
        }

        if (id == 21291) {
            def.name = "<col=65280>Jal-nib-rek";
        }

        if (id == 23757) {
            def.name = "<col=65280>Yougnleff";
        }

        if (id == 23759) {
            def.name = "<col=65280>Corrupted yougnleff";
        }

        if (id == 8788) {
            def.name = "<col=cd2626>Corrupting stone";
            def.colorFind = new short[]{-22297, 127};
            def.colorReplace = new short[]{945, 582};
        }

        if (id == 30044) {
            def.name = "<col=65280>Jaltok-jad";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.inventoryModel = 33012;
            def.xOffset2d = -3;
            def.yOffset2d = -30;
            def.yan2d = 553;
            def.zoom2d = 12000;
        }

        if (id == 30131) {
            def.name = "<col=65280>Baby lava dragon";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.inventoryModel = 58995;
            def.xOffset2d = -100;
            def.yOffset2d = 9;
            def.xan2d = 83;
            def.yan2d = 1855;
            def.zoom2d = 2541;
        }

        if (id == 16171) {
            def.name = "<col=65280>Wampa";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.inventoryModel = 21802;
            def.zoom2d = 4380;
        }

        if (id == 30340) {
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.name = "<col=65280>Centaur";
            def.inventoryModel = 16212;
            def.zoom2d = 2300;
            def.xan2d = 25;
            def.yan2d = 25;
            def.yOffset2d = -10;
        }

        if (id == 30342) {
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.name = "<col=65280>Dementor";
            def.colorFind = new short[]{3346, 6371};
            def.colorReplace = new short[]{0, 0};
            def.inventoryModel = 14992;
            def.xOffset2d = -1;
            def.yOffset2d = -10;
            def.xan2d = 160;
            def.yan2d = 64;
            def.zoom2d = 530;
        }

        if (id == 28643) {
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.name = "<col=65280>Fenrir Greyback Jr.";
            def.colorFind = new short[]{0, 11200, 929, 931, 9542, 902, 262, 906, 910, 914, 918, 922, 955, 9149, 7101, 8125, 6077, 4029, 957, 1981, 926};
            def.colorReplace = new short[]{4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 0, 4769, 0, 0, 4769, 4769};
            def.inventoryModel = 26177;
            def.yOffset2d = 100;
            def.xan2d = 100;
            def.yan2d = 100;
            def.zoom2d = 1000;
        }

        if (id == 10858) {
            def.name = "<col=65280>Sword of gryffindor";
        }

        if (id == 12873 || id == 12875 || id == 12877 || id == 12879 || id == 12881 || id == 12883 || id == 6759 || id == DWARF_CANNON_SET) {
            def.interfaceOptions = new String[5];
            def.interfaceOptions[0] = "Open";
        }

        if (id == 13188) {
            ItemDefinition.copyInventory(def, 13652);
            def.name = "<col=65280>Dragon claws (or)";
            def.colorFind = new short[]{929, 914, 918, 922};
            def.colorReplace = new short[]{929, 7114, 7114, 7114};
        }

        if (id == 12791) {
            def.interfaceOptions = new String[]{"Open", null, "Quick-Fill", "Empty", "Drop"};
        }

        if (id == 23650) {
            ItemDefinition.copyInventory(def, 12791);
            def.interfaceOptions = new String[]{"Open", null, "Quick-Fill", "Empty", "Destroy"};
            def.name = "<col=65280>Rune pouch (i)";
            def.ambient = 120;
        }

        if (id == 14500) {
            ItemDefinition.copyInventory(def, 23650);
            def.name = "<col=65280>Rune pouch (i) (broken)";
            def.ambient = -25;
            def.interfaceOptions = new String[]{null, null, null, null, "Destroy"};
        }

        if (id == 14501) {
            ItemDefinition.copyInventory(def, 12436);
            def.name = "<col=65280>Amulet of fury (or) (broken)";
            def.ambient = -25;
            def.interfaceOptions = new String[]{null, null, null, null, "Destroy"};
        }

        if (id == 14502) {
            ItemDefinition.copyInventory(def, 19720);
            def.name = "<col=65280>Occult necklace (or) (broken)";
            def.ambient = -25;
            def.interfaceOptions = new String[]{null, null, null, null, "Destroy"};
        }

        if (id == 14503) {
            ItemDefinition.copyInventory(def, 20366);
            def.name = "<col=65280>Amulet of torture (or) (broken)";
            def.ambient = -25;
            def.interfaceOptions = new String[]{null, null, null, null, "Destroy"};
        }

        if (id == 14504) {
            ItemDefinition.copyInventory(def, 22249);
            def.name = "<col=65280>Necklace of anguish (or) (broken)";
            def.ambient = -25;
            def.interfaceOptions = new String[]{null, null, null, null, "Destroy"};
        }

        if (id == 14505) {
            ItemDefinition.copyInventory(def, 23444);
            def.name = "<col=65280>Tormented bracelet (or) (broken)";
            def.ambient = -25;
            def.interfaceOptions = new String[]{null, null, null, null, "Destroy"};
        }

        if (id == 14506) {
            ItemDefinition.copyInventory(def, 19722);
            def.name = "<col=65280>Dragon defender (t) (broken)";
            def.ambient = -25;
            def.interfaceOptions = new String[]{null, null, null, null, "Destroy"};
        }

        if (id == 14507) {
            ItemDefinition.copyInventory(def, 22234);
            def.name = "<col=65280>Dragon boots (g) (broken)";
            def.ambient = -25;
            def.interfaceOptions = new String[]{null, null, null, null, "Destroy"};
        }

        if (id == 4447) {
            def.name = "<col=65280>Double drops lamp";
            def.description = "Receive double drops when killing bosses. (for 1 hour)";
        }

        if (id == 7956) {
            ItemDefinition.copyInventory(def, ItemIdentifiers.CASKET);
            def.name = "<col=65280>Small blood money casket";
            def.colorFind = new short[]{13248, 7062, -22477};
            def.colorReplace = new short[]{7114, 929, 929};
        }

        if (id == 13302) {
            ItemDefinition.copyInventory(def, KEY_298);
            def.name = "<col=cd2626><col=cd2626>Wilderness key</col>";
            def.interfaceOptions = new String[]{null, null, null, null, "Destroy"};
            def.inventoryModel = 69799;
            def.animateInventory = true;
        }

        if (id == 6542) {
            def.name = "<col=65280>Present mystery box";
        }
        if (id == 30222) {
            def.animateInventory = true;
            def.name = "Mystery ticket";
            def.interfaceOptions = new String[]{"Tear", null, null, null, null};
            def.colorFind = new short[]{7364, 11078, -327, -329, 7496, 7500};
            def.colorReplace = new short[]{(short) 374770, (short) 374770, -327, -329, (short) 374770, (short) 374770};
            def.inventoryModel = 55601;
            def.stackable = true;
            def.xan2d = 308;
            def.yan2d = 1888;
            def.zoom2d = 1160;
        }
        if (id == 16464 || id == 30026 || id == 30244 || id == 30186 || id == 30185 || id == 16451 || id == 16452 || id == 16453 || id == 16454 || id == 16455 || id == 16456 || id == 16457 || id == 16458 || id == 16459 || id == 16460 || id == 16461 || id == 16419 || id == 16462 || id == 16466 || id == 16468) {
            ItemDefinition.copyInventory(def, 6199);
            switch (id) {
                case 30185:
                    def.name = "<col=461770>Donator mystery box";
                    def.inventoryModel = 55566;
                    def.animateInventory = true;
                    def.colorFind = new short[]{2999, 22410};
                    def.colorReplace = new short[]{524, 13};
                    break;

                case 16468:
                case 30026:
                    def.name = "<col=ff4500>Molten mystery box";
                    def.interfaceOptions = new String[]{"Open", null, null, null, null};
                    def.animateInventory = true;
                    def.inventoryModel = 69818;
                    break;

                case 30186:
                    def.name = "<col=65280>Halloween mystery box";
                    def.inventoryModel = 55605;
                    def.animateInventory = true;
                    def.colorFind = new short[]{2999, 22410};
                    def.colorReplace = new short[]{524, 13};
                    break;

                case 16451:
                    def.name = "<col=0000ee>Weapon mystery box";
                    def.inventoryModel = 69804;
                    def.animateInventory = true;
                    break;
                case 16452:
                    def.name = "<col=65280>Armour mystery box";
                    def.inventoryModel = 69805;
                    def.animateInventory = true;
                    break;
                case 16453:
                    def.name = "<col=65280>Donator mystery box";
                    def.colorFind = new short[]{22410, 2999};
                    def.colorReplace = new short[]{926, 127};
                    break;

                case 16454:
                    def.name = "<col=ffd700>Legendary mystery box";
                    def.inventoryModel = 69806;
                    def.animateInventory = true;
                    break;

                case 16455:
                    def.name = "<col=cd2626>Grand mystery box";
                    def.inventoryModel = 69810;
                    def.animateInventory = true;
                    break;

                case 16456:
                    def.name = "<col=65280>Pet mystery box";
                    def.colorFind = new short[]{22410, 2999};
                    def.colorReplace = new short[]{127, 0};
                    break;

                case 16457:
                    def.interfaceOptions = new String[]{"Open", null, null, null, null};
                    def.name = "<col=65280>Fancy mystery box";
                    def.modelCustomColor4 = 125;
                    break;

                case 16458:
                    def.name = "<col=cd2626>Epic pet mystery box";
                    def.interfaceOptions = new String[]{"Open", null, null, null, null};
                    def.animateInventory = true;
                    def.inventoryModel = 55567;
                    break;

                case 16459:
                    def.interfaceOptions = new String[]{"Open", null, null, null, null};
                    def.name = "<col=cd2626>Raids mystery box";
                    def.inventoryModel = 69811;
                    def.animateInventory = true;
                    break;

                case 16460:
                    def.interfaceOptions = new String[]{"Open", null, null, null, null};
                    def.name = "<col=65280>Zenyte mystery box";
                    def.inventoryModel = 69807;
                    def.animateInventory = true;
                    break;

                case 16461:
                    def.interfaceOptions = new String[]{"Open", null, null, null, null};
                    def.name = "<col=65280>Starter box";
                    def.animateInventory = true;
                    def.inventoryModel = 55574;
                    break;
                case 16419:
                    def.interfaceOptions = new String[]{"Open", null, null, null, null};
                    def.name = "<col=65280>Darklord starter weapon box";
                    def.animateInventory = true;
                    def.inventoryModel = 55574;
                    break;
                case 16462:
                    def.interfaceOptions = new String[]{"Open", null, null, null, null};
                    def.name = "<col=65280>Clan box";
                    def.animateInventory = true;
                    def.inventoryModel = 55575;
                    break;

                case 30244:
                    def.interfaceOptions = new String[]{"Open", null, null, null, null};
                    def.name = "<col=65280>Revenant mystery box";
                    def.animateInventory = true;
                    def.inventoryModel = 69817;
                    break;
            }
        }
        if (id == 26170) {
            def.name = "@gre@DarkLord helm";
        }
        if (id == 26172) {
            def.name = "@gre@DarkLord body";
        }
        if (id == 26180) {
            def.name = "@gre@DarkLord platelegs";
        }
        if (id == 10858) {
            def.colorFind = new short[]{10258, 10291, 10275, 10262, 10266, 10283};
            def.colorReplace = new short[]{82, 125, 125, 121, 125, 125};
        }

        if (id == 3269) {
            def.colorFind = new short[]{57, 49};
            def.colorReplace = new short[]{926, 9152};
            def.animateInventory = true;
        }


        if (id == 962) {
            def.interfaceOptions = new String[]{"Open", null, null, null, null};
        }

        if (id == 6722) {
            def.name = "<col=65280>Zombies champion";
            def.interfaceOptions = new String[]{null, null, null, null, null};
        }

        if (id == 23818) {
            def.name = "<col=65280>Mini barrelchest";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.inventoryModel = 22790;
            def.zoom2d = 5980;
            def.yan2d = 0;
            def.xan2d = 100;
            def.xOffset2d = 2;
            def.yOffset2d = 153;
            def.stackable = false;
        }

        if (id == 29102) {
            def.name = "<col=65280>Scythe of vitur kit";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.inventoryModel = 12841;
            def.xOffset2d = -10;
            def.yOffset2d = 20;
            def.xan2d = 564;
            def.yan2d = 1943;
            def.zoom2d = 1616;
            def.modelCustomColor4 = 235;
        }

        if (id == 29103) {
            def.name = "<col=65280>Twisted bow kit";
            def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
            def.inventoryModel = 12841;
            def.xOffset2d = -10;
            def.yOffset2d = 20;
            def.xan2d = 564;
            def.yan2d = 1943;
            def.zoom2d = 1616;
            def.modelCustomColor4 = 31575;
        }

        switch (id) {

            case 30074:
                def.name = "<col=65280>Lava d'hide coif";
                def.femaleModel0 = 58987;
                def.femaleModel1 = 403;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 58990;
                def.maleModel0 = 59000;
                def.maleModel1 = 230;
                def.yOffset2d = -35;
                def.xan2d = 194;
                def.yan2d = 1784;
                def.zoom2d = 789;
                break;

            case 30077:
                def.name = "<col=65280>Lava d'hide body";
                def.femaleModel0 = 58986;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 58991;
                def.maleModel0 = 58999;
                def.yOffset2d = -4;
                def.yan2d = 0;
                def.xan2d = 548;
                def.zoom2d = 1180;
                break;

            case 30080:
                def.name = "<col=65280>Lava d'hide chaps";
                def.femaleModel0 = 58988;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 58992;
                def.maleModel0 = 59001;
                def.yOffset2d = -1;
                def.xan2d = 444;
                def.yan2d = 0;
                def.zoom2d = 1827;
                break;

            case 30038:
                def.name = "<col=65280>Primordial boots (or)";
                def.femaleModel0 = 58967;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 58966;
                def.maleModel0 = 58968;
                def.xOffset2d = 5;
                def.yOffset2d = -5;
                def.xan2d = 147;
                def.yan2d = 279;
                def.zoom2d = 976;
                break;


            case 30297:
                def.name = "<col=65280>Enchanted boots";
                def.femaleModel0 = 59096;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59095;
                def.maleModel0 = 59097;
                def.xOffset2d = 5;
                def.yOffset2d = -5;
                def.xan2d = 147;
                def.yan2d = 279;
                def.zoom2d = 976;
                def.colorFind = new short[]{(short) 47511, (short) 49424, 126};
                def.colorReplace = new short[]{933, 933, 933};
                break;

            case 30181:
            case 30184:
                def.name = "<col=65280>Elder wand";
                def.colorFind = new short[]{-19153, -19500, -19145, 37, -16339, -16331};
                def.colorReplace = new short[]{530, 540, 529, 10, 13, 16};
                def.femaleModel0 = 32669;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.inventoryModel = 32789;
                def.maleModel0 = 32669;
                def.xOffset2d = 2;
                def.yOffset2d = -4;
                def.xan2d = 140;
                def.yan2d = 1416;
                def.zoom2d = 668;
                break;

            case 2572:
                ItemDefinition.copyInventory(def, RING_OF_WEALTH_1);
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                break;

            case 16012:
                def.name = "<col=65280>Baby dark beast";
                def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
                def.zoom2d = 550;
                def.xan2d = 76;
                def.yan2d = 16;
                def.xOffset2d = 0;
                def.yOffset2d = 0;
                def.inventoryModel = 7171;
                def.colorFind = new short[]{476};
                def.colorReplace = new short[]{(short) 54444};
                break;

            case 16013:
                ItemDefinition.copyInventory(def, 12649);
                def.name = "<col=65280>Pet kree'arra (white)";
                def.interfaceOptions = new String[]{null, null, null, "Wipe-off paint", null};
                def.modelCustomColor4 = 31575;
                break;

            case 16014:
                ItemDefinition.copyInventory(def, 12651);
                def.name = "<col=65280>Pet zilyana (white)";
                def.interfaceOptions = new String[]{null, null, null, "Wipe-off paint", null};
                def.modelCustomColor4 = 33785;
                break;

            case 16015:
                ItemDefinition.copyInventory(def, 12650);
                def.name = "<col=65280>Pet general graardor (black)";
                def.interfaceOptions = new String[]{null, null, null, "Wipe-off paint", null};
                def.modelCustomColor4 = 235;
                break;

            case 16016:
                ItemDefinition.copyInventory(def, 12652);
                def.name = "<col=65280>Pet k'ril tsutsaroth (black)";
                def.interfaceOptions = new String[]{null, null, null, "Wipe-off paint", null};
                def.modelCustomColor4 = 235;
                break;

            case 16024:
                def.name = "<col=65280>Baby abyssal demon";
                def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
                def.zoom2d = 550;
                def.xan2d = 76;
                def.yan2d = 16;
                def.xOffset2d = 0;
                def.yOffset2d = 0;
                def.inventoryModel = 7171;
                def.modelCustomColor3 = 1343;
                break;

            case 964:
                def.interfaceOptions = new String[]{"Cast", null, null, null, "Destroy"};
                def.name = "<col=65280>Vengeance";
                def.description = "Rebound damage to an opponent.";
                def.colorFind = new short[]{5231};
                def.colorReplace = new short[]{(short) 130770};
                break;

            case 2685:
                def.name = "<col=65280>PvP task scroll";
                def.interfaceOptions = new String[]{"Read", null, "Skip task", null, "Destroy"};
                def.colorFind = new short[]{6464, 6608, 22305, 22034, 6740, 22422, 6583, 6587, 6604};
                def.colorReplace = new short[]{933, 926, 926, 926, 933, 926, 926, 926, 933};
                break;

            case 16755:
                ItemDefinition.copyInventory(def, 13648);
                def.name = "<col=65280>PvP task bottle";
                def.ambient = 20;
                def.colorFind = new short[]{22422};
                def.colorReplace = new short[]{933};
                def.interfaceOptions = new String[]{"Open", null, null, null, "Drop"};
                break;

            case 14524:
                ItemDefinition.copyInventory(def, 8151);
                def.name = "<col=65280>Blood money chest";
                def.description = "Opens for 10.000 blood money.";
                def.interfaceOptions = new String[]{"Open", null, null, null, "Drop"};
                def.colorFind = new short[]{24, 49, 4510, 4502, 8128, 7093};
                def.colorReplace = new short[]{926, 926, 926, 926, 926, 926};
                break;

            case 14525:
                def.inventoryModel = 55570;
                def.zoom2d = 2640;
                def.xan2d = 114;
                def.yan2d = 1883;
                def.animateInventory = true;
                def.name = "<col=461770>Mystery chest";
                def.interfaceOptions = new String[]{"Open", null, null, null, "Drop"};
                def.colorFind = new short[]{24, 49, 4510, 4502, 8128, 7093};
                def.colorReplace = new short[]{24, 49, (short) 184770, (short) 184770, (short) 87770, (short) 87770};
                break;

            case 7237:
                def.name = "<col=65280>PvP reward casket";
                def.colorFind = new short[]{13248, 7062, -22477};
                def.colorReplace = new short[]{13248, 563, -22477};
                def.interfaceOptions = new String[]{"Open", null, null, null, "Drop"};
                break;

            case 16005:
                def.name = "<col=65280>Baby squirt";
                def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
                def.zoom2d = 550;
                def.xan2d = 76;
                def.yan2d = 16;
                def.xOffset2d = 0;
                def.yOffset2d = 0;
                def.inventoryModel = 7171;
                def.colorFind = new short[]{476};
                def.colorReplace = new short[]{(short) 246770};
                break;

            case 7999:
                def.modelCustomColor4 = 155;
                def.name = "<col=65280>Pet paint (black)";
                def.description = "Changes color of baby K'ril and baby Graardor.";
                break;

            case 8000:
                def.modelCustomColor4 = 2;
                def.name = "<col=65280>Pet paint (white)";
                def.description = "Changes color of baby Kree'Arra and baby Zilyana.";
                break;

            case 14222:
                def.name = "<col=65280>X2 PK points (+30)";
                def.zoom2d = 1020;
                def.xan2d = 344;
                def.yan2d = 656;
                def.xOffset2d = -1;
                def.yOffset2d = 11;
                def.inventoryModel = 10347;
                def.interfaceOptions[1] = "Claim";
                def.stackable = true;
                def.description = "+30 minutes of 2x PkP.";
                break;

            case 13190:
                def.name = "<col=65280>$5 bond";
                def.interfaceOptions = new String[]{"Redeem", null, null, null, null};
                break;

            case 16278:
                ItemDefinition.copyInventory(def, 13190);
                def.name = "<col=65280>$10 bond";
                def.interfaceOptions = new String[]{"Redeem", null, null, null, null};
                def.inventoryModel = 69791;
                break;

            case 16263:
                ItemDefinition.copyInventory(def, 13190);
                def.name = "<col=65280>$20 bond";
                def.interfaceOptions = new String[]{"Redeem", null, null, null, null};
                def.inventoryModel = 69792;
                break;

            case 16264:
                ItemDefinition.copyInventory(def, 13190);
                def.name = "<col=65280>$40 bond";
                def.interfaceOptions = new String[]{"Redeem", null, null, null, null};
                def.colorFind = new short[]{20416, 22451, 11224, 22181, 22449, 22305, 21435, 9164, 11092, 9152, 7087, (short) 32821, (short) 32846, 7997, 8117, (short) 32829,(short)  32838, 22464};
                def.colorReplace = new short[]{25416, 27451, 16224, 27181, 27449, 27305, 26435, 14164, 16092, 14152, 12087, (short) 37821, (short) 37846, 12997, 13117, (short) 37829, (short) 37838, 27464};
                break;

            case 16265:
                ItemDefinition.copyInventory(def, 13190);
                def.name = "<col=65280>$50 bond";
                def.interfaceOptions = new String[]{"Redeem", null, null, null, null};
                def.inventoryModel = 69793;
                def.animateInventory = true;
                break;

            case DOPPELGANGER_PET:
                def.name = "<col=65280>Doppelganger Pet";
                break;
            case 5021://afkticketupdate
                def.name = "<col=65280>Afk ticket";
                def.stackable = true;
                break;
            case 20391:
                def.name = "@blu@Raid rare ticket";
                ItemDefinition.copyInventory(def, DONATOR_TICKET);
                def.modelCustomColor4 = 689484;
                break;
            case 16266:
                ItemDefinition.copyInventory(def, 13190);
                def.name = "<col=65280>$100 bond";
                def.interfaceOptions = new String[]{"Redeem", null, null, null, null};
                def.inventoryModel = 69795;
                def.animateInventory = true;
                break;

            case 2866:
                def.name = "<col=65280>Earth arrows";
                break;

            case 4160:
                def.name = "<col=65280>Fire arrows";
                def.colorFind = new short[]{57, 61, 5012, 926};
                def.colorReplace = new short[]{57, 61, 5012, 926};
                break;

            case 7806:
                def.name = "<col=65280>Ancient warrior sword";
                def.colorFind = new short[]{920, 0, 103};
                def.colorReplace = new short[]{(short) 391770, 0, 110};
                break;

            case 7808:
                def.name = "<col=65280>Ancient warrior maul";
                def.colorFind = new short[]{78, 103, 920};
                def.colorReplace = new short[]{(short) 391470, (short) 391470, 100, 100};
                break;

            case 7807:
                def.name = "<col=65280>Ancient warrior axe";
                def.colorFind = new short[]{0, 78, 920};
                def.colorReplace = new short[]{(short) (short) 191770, (short) 191770, 110};
                break;

            case 17000:
                ItemDefinition.copyInventory(def, 995);
                def.countCo = new int[]{2, 3, 4, 5, 25, 100, 250, 1000, 10000, 0};
                def.countObj = new int[]{17001, 17002, 17003, 17004, 17005, 17006, 17007, 17008, 17009, 0};
                def.name = ClientConstants.CLIENT_NAME + " coins";
                def.colorFind = new short[]{8128};
                def.colorReplace = new short[]{5706};
                break;
            case 17001:
                ItemDefinition.copyInventory(def, 996);
                def.name = ClientConstants.CLIENT_NAME + " coins";
                def.colorFind = new short[]{8128};
                def.colorReplace = new short[]{5706};
                break;
            case 17002:
                ItemDefinition.copyInventory(def, 997);
                def.name = ClientConstants.CLIENT_NAME + " coins";
                def.colorFind = new short[]{8128};
                def.colorReplace = new short[]{5706};
                break;
            case 17003:
                ItemDefinition.copyInventory(def, 998);
                def.name = ClientConstants.CLIENT_NAME + " coins";
                def.colorFind = new short[]{8128};
                def.colorReplace = new short[]{5706};
                break;
            case 17004:
                ItemDefinition.copyInventory(def, 999);
                def.name = ClientConstants.CLIENT_NAME + " coins";
                def.colorFind = new short[]{8128};
                def.colorReplace = new short[]{5706};
                break;
            case 17005:
                ItemDefinition.copyInventory(def, 1000);
                def.name = ClientConstants.CLIENT_NAME + " coins";
                def.colorFind = new short[]{8128};
                def.colorReplace = new short[]{5706};
                break;
            case 17006:
                ItemDefinition.copyInventory(def, 1001);
                def.name = ClientConstants.CLIENT_NAME + " coins";
                def.colorFind = new short[]{8128};
                def.colorReplace = new short[]{5706};
                break;
            case 17007:
                ItemDefinition.copyInventory(def, 1002);
                def.name = ClientConstants.CLIENT_NAME + " coins";
                def.colorFind = new short[]{8128};
                def.colorReplace = new short[]{5706};
                break;
            case 17008:
                ItemDefinition.copyInventory(def, 1003);
                def.name = ClientConstants.CLIENT_NAME + " coins";
                def.colorFind = new short[]{8128};
                def.colorReplace = new short[]{5706};
                break;
            case 17009:
                ItemDefinition.copyInventory(def, 1004);
                def.name = ClientConstants.CLIENT_NAME + " coins";
                def.colorFind = new short[]{8128};
                def.colorReplace = new short[]{5706};
                break;

            case 2944:
                def.modelCustomColor3 = 3193931;
                break;

            case 12773:
                def.name = "<col=ff4500>Lava whip";
                break;

            case 12774:
                def.name = "<col=00ffff>Frost whip";
                break;

            case 10586:
                def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
                def.name = "<col=65280>Genie";
                break;

            case 12102:
                def.name = "<col=65280>Grim";
                def.zoom2d = 1010;
                def.xan2d = 0;
                def.yan2d = 0;
                def.xOffset2d = 1;
                def.yOffset2d = 79;
                def.inventoryModel = 5100;
                def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
                break;

            case 13346:
                def.name = "<col=00ffff>Present";
                def.interfaceOptions = new String[]{"Quick-open", null, null, "Open", null};
                break;

            case 20834:
                def.interfaceOptions = new String[]{null, "Wear", "Clear", null, null};
                break;
            case 14025:
                def.name = "Hazy Wings";
                def.inventoryModel = 66220;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 3500;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.maleModel0 = 66221;
                def.femaleModel0 = 66221;
                def.colorFind = new short[]{(short) 51998, 127};
                def.colorReplace = new short[]{0, 926};
                break;
            case 14026:
                def.name = "Demonic Wings";
                def.inventoryModel = 69727;
                def.xOffset2d = -24;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 3500;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.maleModel0 = 69726;
                def.femaleModel0 = 69726;
                break;
            case 14027:
                def.name = "Shoulder Bird";
                def.inventoryModel = 69723;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 3500;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.maleModel0 = 69724;
                def.femaleModel0 = 69724;
                break;
            case 14028:
                def.name = "Dragon Companion";
                def.inventoryModel = 69725;
                def.xOffset2d = -15;
                def.yOffset2d = 5;
                def.xan2d = 550;
                def.yan2d = 1017;
                def.zoom2d = 1700;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.maleModel0 = 69722;
                def.femaleModel0 = 69722;
                break;
            case 14029:
                def.name = "Chaos Torva Full Helm";
                def.inventoryModel = 69733;
                ItemDefinition helm = ItemDefinition.get(26382);
                def.ambient = helm.ambient;
                def.contrast = helm.contrast;
                def.yan2d = helm.yan2d;
                def.xan2d = helm.xan2d;
                def.zoom2d = helm.zoom2d;
                def.stackable = false;
                def.interfaceOptions = helm.interfaceOptions;
                def.maleModel0 = 69736;
                def.femaleModel0 = 69730;
                break;
            case 14030:
                def.name = "Chaos Torva Platebody";
                ItemDefinition top = ItemDefinition.get(26384);
                def.inventoryModel = 69732;
                def.ambient = top.ambient;
                def.contrast = top.contrast;
                def.yan2d = top.yan2d;
                def.xan2d = top.xan2d;
                def.zoom2d = top.zoom2d;
                def.stackable = false;
                def.interfaceOptions = top.interfaceOptions;
                def.maleModel0 = 69735;
                def.femaleModel0 = 69728;
                break;
            case 14031:
                def.name = "Chaos Torva Platelegs";
                def.inventoryModel = 69731;
                ItemDefinition legs = ItemDefinition.get(26386);
                def.ambient = legs.ambient;
                def.contrast = legs.contrast;
                def.yan2d = legs.yan2d;
                def.xan2d = legs.xan2d;
                def.zoom2d = legs.zoom2d;
                def.stackable = false;
                def.interfaceOptions = legs.interfaceOptions;
                def.maleModel0 = 69734;
                def.femaleModel0 = 69729;
                break;
            case 14032:
                def.name = "Chaos Ancestral hat";
                def.inventoryModel = 69755;
                ItemDefinition hat = ItemDefinition.get(21018);
                def.ambient = hat.ambient;
                def.contrast = hat.contrast;
                def.yan2d = hat.yan2d;
                def.xan2d = hat.xan2d;
                def.zoom2d = hat.zoom2d;
                def.stackable = false;
                def.interfaceOptions = hat.interfaceOptions;
                def.maleModel0 = 69748;
                def.femaleModel0 = 69756;
                break;
            case 14033:
                def.name = "Chaos Ancestral top";
                ItemDefinition robetop = ItemDefinition.get(21021);
                def.inventoryModel = 69754;
                def.ambient = robetop.ambient;
                def.contrast = robetop.contrast;
                def.yan2d = robetop.yan2d;
                def.xan2d = robetop.xan2d;
                def.zoom2d = robetop.zoom2d;
                def.stackable = false;
                def.interfaceOptions = robetop.interfaceOptions;
                def.maleModel0 = 69750;
                def.maleModel1 = 69749;
                def.femaleModel0 = 69757;
                def.femaleModel1 = 69752;
                break;
            case 14034:
                def.name = "Chaos Ancestral legs";
                def.inventoryModel = 69753;
                ItemDefinition robeleg = ItemDefinition.get(21024);
                def.ambient = robeleg.ambient;
                def.contrast = robeleg.contrast;
                def.yan2d = robeleg.yan2d;
                def.xan2d = robeleg.xan2d;
                def.zoom2d = robeleg.zoom2d;
                def.stackable = false;
                def.interfaceOptions = robeleg.interfaceOptions;
                def.maleModel0 = 69747;
                def.femaleModel0 = 69751;
                break;
            case 14035:
                def.name = "Chaos Masori coif";
                def.inventoryModel = 69764;
                ItemDefinition coif = ItemDefinition.get(27235);
                def.ambient = coif.ambient;
                def.contrast = coif.contrast;
                def.yan2d = coif.yan2d;
                def.xan2d = coif.xan2d;
                def.zoom2d = coif.zoom2d;
                def.stackable = false;
                def.interfaceOptions = coif.interfaceOptions;
                def.maleModel0 = 69758;
                def.femaleModel0 = 69761;
                break;
            case 14036:
                def.name = "Chaos Masori body";
                ItemDefinition body = ItemDefinition.get(27238);
                def.inventoryModel = 69765;
                def.ambient = body.ambient;
                def.contrast = body.contrast;
                def.yan2d = body.yan2d;
                def.xan2d = body.xan2d;
                def.zoom2d = body.zoom2d;
                def.stackable = false;
                def.interfaceOptions = body.interfaceOptions;
                def.maleModel0 = 69760;
                def.femaleModel0 = 69763;
                break;
            case 14037:
                def.name = "Chaos Masori chaps";
                def.inventoryModel = 69766;
                ItemDefinition chaps = ItemDefinition.get(27241);
                def.ambient = chaps.ambient;
                def.contrast = chaps.contrast;
                def.yan2d = chaps.yan2d;
                def.xan2d = chaps.xan2d;
                def.zoom2d = chaps.zoom2d;
                def.stackable = false;
                def.interfaceOptions = chaps.interfaceOptions;
                def.maleModel0 = 69759;
                def.femaleModel0 = 69762;
                break;
            case 32102:
                def.name = "<col=65280>Blood reaper";
                def.zoom2d = 1010;
                def.xan2d = 0;
                def.yan2d = 0;
                def.xOffset2d = 1;
                def.yOffset2d = 79;
                def.inventoryModel = 5100;
                def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
                def.modelCustomColor4 = 964;
                break;
            case 30094:
                def.name = "<col=65280>White Orb (Recolor EB)";
                def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
                ItemDefinition.copyInventory(def, VOLATILE_ORB);
                def.modelCustomColor4 = 124;
                break;
            case 30095:
                def.name = "<col=65280>Donation bow";
                def.zoom2d = 1862;
                def.xan2d = 456;
                def.yan2d = 1232;
                def.xOffset2d = 13;
                def.yOffset2d = -7;
                def.colorReplace = new short[]{124, 74, 10283, 124, 124, 124, 10299, 66, 127};
                def.colorFind = new short[]{26772, 74, 10283, 41, 26780, 26776, 10299, 66, 127};
                def.options = new String[]{null, null, "Take", null, null};
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.inventoryModel = 28678;
                def.maleModel0 = 28622;
                def.femaleModel0 = 28622;
                break;
            case 12081:
                def.name = "<col=65280>Elemental bow";
                def.zoom2d = 1862;
                def.xan2d = 456;
                def.yan2d = 1232;
                def.xOffset2d = 13;
                def.yOffset2d = -7;
                def.colorReplace = new short[]{(short) 311770, 74, 10283,(short)  374770, (short) 311770, (short) 311770, 10299, 66, 127};
                def.colorFind = new short[]{26772, 74, 10283, 41, 26780, 26776, 10299, 66, 127};
                def.options = new String[]{null, null, "Take", null, null};
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.inventoryModel = 28678;
                def.maleModel0 = 28622;
                def.femaleModel0 = 28622;
                break;

            case 12082:
                ItemDefinition.copyInventory(def, 12373);
                ItemDefinition.copyEquipment(def, 12373);
                def.name = "<col=65280>Elemental staff";
                def.colorReplace = new short[]{(short) 311770, (short) 347770,(short)  347770, 37};
                def.colorFind = new short[]{924, 0,(short) 43164, 37};
                break;

            case 4067:
                def.name = "<col=65280>Donator ticket";
                break;

            case 619:
                def.name = "<col=65280>Vote ticket";
                def.stackable = true;
                def.interfaceOptions = new String[]{"Convert to Points", null, null, null, "Drop"};
                break;

            case 18335:
                def.name = "<col=ff4500>Lava partyhat";
                def.interfaceOptions = new String[5];
                def.interfaceOptions[1] = "Wear";
                def.zoom2d = 440;
                def.inventoryModel = 55571;
                def.animateInventory = true;
                def.xOffset2d = 1;
                def.yOffset2d = 1;
                def.yan2d = 1852;
                def.xan2d = 76;
                def.maleModel0 = 55572;
                def.femaleModel0 = 55573;
                def.colorFind = new short[]{926};
                def.colorReplace = new short[]{926};
                def.textureFind = new short[]{926};
                def.textureReplace = new short[]{31};
                break;


            case 25936:
                def.name = "<col=65280>Pharaoh's hilt";
                def.inventoryModel = 42786;
                def.zoom2d = 932;
                def.xan2d = 465;
                def.yan2d = 1079;
                def.yOffset2d = 9;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.maleModel0 = 42750;
                def.femaleModel0 = 42753;
                break;

            case 25916:
                def.name = "<col=00ffff>Dragon hunter crossbow (t)";
                def.inventoryModel = 42792;
                def.zoom2d = 926;
                def.xan2d = 432;
                def.yan2d = 258;
                def.xOffset2d = 0;
                def.yOffset2d = 9;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.maleModel0 = 42744;
                def.femaleModel0 = 42740;
                break;

            case 25913:
                def.name = "<col=65280>Twisted slayer helmet (i)";
                def.inventoryModel = 42726;
                def.zoom2d = 779;
                def.xan2d = 30;
                def.yan2d = 1773;
                def.xOffset2d = -4;
                def.yOffset2d = -3;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wear", null, "Disassemble", "Drop"};
                def.maleModel0 = 42733;
                def.femaleModel0 = 42733;
                def.maleHeadModel = 42782;
                def.femaleHeadModel = 42782;
                break;

            case 25907:
                def.name = "<col=65280>Twisted slayer helmet (i)";
                def.inventoryModel = 42725;
                def.zoom2d = 779;
                def.xan2d = 30;
                def.yan2d = 1773;
                def.xOffset2d = -4;
                def.yOffset2d = -3;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wear", null, "Disassemble", "Drop"};
                def.maleModel0 = 42735;
                def.femaleModel0 = 42738;
                def.maleHeadModel = 42783;
                def.femaleHeadModel = 42783;
                break;

            case 25902:
                def.name = "<col=65280>Twisted slayer helmet (i)";
                def.inventoryModel = 42724;
                def.zoom2d = 779;
                def.xan2d = 30;
                def.yan2d = 1773;
                def.xOffset2d = -4;
                def.yOffset2d = -3;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wear", null, "Disassemble", "Drop"};
                def.maleModel0 = 42734;
                def.femaleModel0 = 42737;
                def.maleHeadModel = 42784;
                def.femaleHeadModel = 42784;
                break;

            case 25866:
                def.name = "<col=cd2626>Bow of faerdhinen (c)";
                def.inventoryModel = 42605;
                def.colorFind = new short[]{(short) 33001, (short) 32995};
                def.colorReplace = new short[]{827, 957};
                def.xan2d = 561;
                def.yan2d = 15;
                def.yOffset2d = 4;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.maleModel0 = 42602;
                def.femaleModel0 = 42602;
                def.femaleOffset = 6;
                break;

            case 25870:
                def.name = "<col=65280>Blade of saeldor";
                def.inventoryModel = 37980;
                def.colorFind = new short[]{(short) 33001, (short) 32995};
                def.colorReplace = new short[]{111, 127};
                def.zoom2d = 1876;
                def.xan2d = 438;
                def.yan2d = 40;
                def.zan2d = 84;
                def.xOffset2d = 15;
                def.yOffset2d = -3;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.maleModel0 = 38270;
                def.femaleModel0 = 38280;
                break;

            case 25871:
                def.name = "<col=65280>Blade of saeldor";
                def.inventoryModel = 37980;
                def.colorFind = new short[]{(short) 33001, (short) 32995};
                def.colorReplace = new short[]{4, 8};
                def.zoom2d = 1876;
                def.xan2d = 438;
                def.yan2d = 40;
                def.zan2d = 84;
                def.xOffset2d = 15;
                def.yOffset2d = -3;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.maleModel0 = 38270;
                def.femaleModel0 = 38280;
                break;

            case 25873:
                def.name = "<col=65280>Blade of saeldor";
                def.inventoryModel = 37980;
                def.colorFind = new short[]{(short) 33001, (short) 32995};
                def.colorReplace = new short[]{(short) 53671, (short) 52655};
                def.zoom2d = 1876;
                def.xan2d = 438;
                def.yan2d = 40;
                def.zan2d = 84;
                def.xOffset2d = 15;
                def.yOffset2d = -3;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.maleModel0 = 38270;
                def.femaleModel0 = 38280;
                break;

            case 25875:
                def.name = "<col=65280>Blade of saeldor";
                def.inventoryModel = 37980;
                def.colorFind = new short[]{(short) 33001, (short) 32995};
                def.colorReplace = new short[]{21947, 21958};
                def.zoom2d = 1876;
                def.xan2d = 438;
                def.yan2d = 40;
                def.zan2d = 84;
                def.xOffset2d = 15;
                def.yOffset2d = -3;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.maleModel0 = 38270;
                def.femaleModel0 = 38280;
                break;

            case 25877:
                def.name = "<col=65280>Blade of saeldor";
                def.inventoryModel = 37980;
                def.colorFind = new short[]{(short) 33001, (short) 32995};
                def.colorReplace = new short[]{9668, 9804};
                def.zoom2d = 1876;
                def.xan2d = 438;
                def.yan2d = 40;
                def.zan2d = 84;
                def.xOffset2d = 15;
                def.yOffset2d = -3;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.maleModel0 = 38270;
                def.femaleModel0 = 38280;
                break;

            case 25879:
                def.name = "<col=65280>Blade of saeldor";
                def.inventoryModel = 37980;
                def.zoom2d = 1876;
                def.xan2d = 438;
                def.yan2d = 40;
                def.zan2d = 84;
                def.xOffset2d = 15;
                def.yOffset2d = -3;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.maleModel0 = 38270;
                def.femaleModel0 = 38280;
                break;

            case 25881:
                def.name = "<col=65280>Blade of saeldor (t)";
                def.inventoryModel = 37980;
                def.colorFind = new short[]{(short) 33001, (short) 32995};
                def.colorReplace = new short[]{(short) 39469, (short) 39607};
                def.zoom2d = 1876;
                def.xan2d = 438;
                def.yan2d = 40;
                def.zan2d = 84;
                def.xOffset2d = 15;
                def.yOffset2d = -3;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.maleModel0 = 38270;
                def.femaleModel0 = 38280;
                break;

            case 25883:
                def.name = "<col=65280>Bow of faerdhinen";
                def.inventoryModel = 42605;
                def.colorFind = new short[]{(short) 33001, (short) 32995};
                def.colorReplace = new short[]{111, 127};
                def.xan2d = 561;
                def.yan2d = 15;
                def.yOffset2d = 4;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.maleModel0 = 42602;
                def.femaleModel0 = 42602;
                def.femaleOffset = 6;
                break;

            case 25885:
                def.name = "<col=65280>Bow of faerdhinen";
                def.inventoryModel = 42605;
                def.colorFind = new short[]{(short) 33001, (short) 32995};
                def.colorReplace = new short[]{4, 8};
                def.xan2d = 561;
                def.yan2d = 15;
                def.yOffset2d = 4;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.maleModel0 = 42602;
                def.femaleModel0 = 42602;
                def.femaleOffset = 6;
                break;

            case 25887:
                def.name = "<col=65280>Bow of faerdhinen";
                def.inventoryModel = 42605;
                def.colorFind = new short[]{(short) 33001, (short) 32995};
                def.colorReplace = new short[]{(short) 53671, (short) 52655};
                def.xan2d = 561;
                def.yan2d = 15;
                def.yOffset2d = 4;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.maleModel0 = 42602;
                def.femaleModel0 = 42602;
                def.femaleOffset = 6;
                break;

            case 25889:
                def.name = "<col=65280>Bow of faerdhinen";
                def.inventoryModel = 42605;
                def.colorFind = new short[]{(short) 33001, (short) 32995};
                def.colorReplace = new short[]{21947, 21958};
                def.xan2d = 561;
                def.yan2d = 15;
                def.yOffset2d = 4;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.maleModel0 = 42602;
                def.femaleModel0 = 42602;
                def.femaleOffset = 6;
                break;

            case 25891:
                def.name = "<col=65280>Bow of faerdhinen";
                def.inventoryModel = 42605;
                def.colorFind = new short[]{(short) 33001, (short) 32995};
                def.colorReplace = new short[]{9668, 9804};
                def.xan2d = 561;
                def.yan2d = 15;
                def.yOffset2d = 4;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.maleModel0 = 42602;
                def.femaleModel0 = 42602;
                def.femaleOffset = 6;
                break;

            case 25893:
                def.name = "<col=65280>Bow of faerdhinen";
                def.inventoryModel = 42605;
                def.xan2d = 561;
                def.yan2d = 15;
                def.yOffset2d = 4;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.maleModel0 = 42602;
                def.femaleModel0 = 42602;
                def.femaleOffset = 6;
                break;

            case 25895:
                def.name = "<col=65280>Bow of faerdhinen";
                def.inventoryModel = 42605;
                def.colorFind = new short[]{(short) 33001, (short) 32995};
                def.colorReplace = new short[]{(short) 39469,(short)  39607};
                def.xan2d = 561;
                def.yan2d = 15;
                def.yOffset2d = 4;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.maleModel0 = 42602;
                def.femaleModel0 = 42602;
                def.femaleOffset = 6;
                break;

            case 30032:
            case 30031:
            case 30030:
            case 23975:
            case 23971:
            case 23979:
            case 24668:
            case 24664:
            case 24666:
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                break;

            case 22517:
                def.name = "<col=65280>Saeldor shard";
                def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
                break;

            case 25600:
                def.name = "<col=65280>Ranger gloves";
                def.inventoryModel = 24569;
                def.colorFind = new short[]{14658, 14649, 14645, 14637, 16536, 13716};
                def.colorReplace = new short[]{(short) 43059, (short) 43055, (short) 43051,(short)  43047, (short) 43030, (short) 43030};
                def.zoom2d = 917;
                def.xan2d = 408;
                def.yan2d = 150;
                def.xOffset2d = 0;
                def.stackable = false;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.maleModel0 = 55558;
                def.femaleModel0 = 29056;
                break;

            case 27644:
                def.name = "<col=65280>Salazar Slytherin's locket";
                def.inventoryModel = 55562;
                def.zoom2d = 590;
                def.xan2d = 500;
                def.yan2d = 0;
                def.xOffset2d = 0;
                def.yOffset2d = 7;
                def.stackable = false;
                def.options = new String[]{null, null, "Take", null, null};
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.maleModel0 = 55563;
                def.femaleModel0 = 55564;
                break;

            case 3269:
                def.name = "<col=65280>Slayer key";
                def.inventoryModel = 69826;
                def.animateInventory = true;
                def.zoom2d = 860;
                def.xan2d = 460;
                def.yan2d = 20;
                def.xOffset2d = -9;
                def.yOffset2d = -2;
                def.stackable = true;
                break;

            case 24670:
                def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
                break;

            case 30196:
                def.name = "<col=65280>Totemic helmet";
                def.femaleModel0 = 59029;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59030;
                def.maleModel0 = 59031;
                def.yOffset2d = -12;
                def.xan2d = 118;
                def.yan2d = 10;
                def.zoom2d = 1236;
                break;

            case 30199:
                def.name = "<col=65280>Totemic platebody";
                def.femaleModel0 = 59028;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59025;
                def.maleModel0 = 59035;
                def.yOffset2d = -3;
                def.xan2d = 514;
                def.yan2d = 2041;
                def.zoom2d = 1358;
                break;

            case 30202:
                def.name = "<col=65280>Totemic platelegs";
                def.ambient = 30;
                def.contrast = 20;
                def.femaleModel0 = 59033;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59032;
                def.maleModel0 = 59034;
                def.yOffset2d = -6;
                def.xan2d = 496;
                def.yan2d = 9;
                def.zoom2d = 2061;
                break;

            case 30000:
                def.name = "<col=65280>Dark sage hat";
                def.femaleModel0 = 58915;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 58914;
                def.maleModel0 = 58913;
                def.yOffset2d = -26;
                def.xan2d = 118;
                def.yan2d = 10;
                def.zoom2d = 1336;
                break;

            case 30002:
                def.name = "<col=65280>Dark sage robe top";
                def.femaleModel0 = 58911;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 58919;
                def.maleModel0 = 58912;
                def.yOffset2d = -3;
                def.xan2d = 514;
                def.yan2d = 2041;
                def.zoom2d = 1550;
                break;

            case 30004:
                def.name = "<col=65280>Dark sage robe bottoms";
                def.ambient = 30;
                def.contrast = 20;
                def.femaleModel0 = 58918;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 58917;
                def.maleModel0 = 58909;
                def.xOffset2d = -1;
                def.yOffset2d = 7;
                def.xan2d = 435;
                def.yan2d = 9;
                def.zoom2d = 2300;
                break;

            case 30036:
                def.name = "<col=65280>Twisted bow";
                def.femaleModel0 = 58973;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.inventoryModel = 58974;
                def.maleModel0 = 58973;
                def.xOffset2d = -3;
                def.yOffset2d = 1;
                def.xan2d = 720;
                def.yan2d = 1500;
                break;

            case 30037:
                def.name = "<col=65280>Twisted bow";
                def.femaleModel0 = 58965;
                def.interfaceOptions = new String[]{null, "Wield", null, null, "Drop"};
                def.inventoryModel = 58964;
                def.maleModel0 = 58965;
                def.xOffset2d = -3;
                def.yOffset2d = 1;
                def.xan2d = 720;
                def.yan2d = 1500;
                break;
            case 23154:
                def.name = "<col=65280>Enchanted totemic helmet";
                def.femaleModel0 = 69739;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 69740;
                def.maleModel0 = 69741;
                def.yOffset2d = -12;
                def.xan2d = 118;
                def.yan2d = 10;
                def.zoom2d = 1236;
//                def.color_to_replace = new int[]{35978,33199,35983,34962,33182,34964,34966,32151,34961,33190,34958,33159,33157,32142,32141,33167,32140,32138,33161};
//                def.color_to_replace_with = new int[]{0,933,0,0,933,0,0,0,0,933,0,933,933,933,933,933,933,933,933};
                break;

            case 23156:
                def.name = "<col=65280>Enchanted totemic platebody ";
                def.femaleModel0 = 69738;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
//                def.color_to_replace = new int[]{34964,34963,34962,35990,34968,34959,32158,8,35994,32166,30858,32163,32168,32160,34965,34961};
//                def.color_to_replace_with = new int[]{0,0,0,0,0,0,933,933,933,933,933,933,933,933,933,933};
                def.inventoryModel = 69737;
                def.maleModel0 = 69745;
                def.yOffset2d = -3;
                def.xan2d = 514;
                def.yan2d = 2041;
                def.zoom2d = 1358;
                break;

            case 23158:
                def.name = "<col=65280>Enchanted totemic platelegs";
                def.ambient = 30;
                def.contrast = 20;
                def.femaleModel0 = 69743;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
//                def.color_to_replace = new int[]{34965,34961,34968,32163,31907,35991};
//                def.color_to_replace_with = new int[]{0,0,0,933,933,933};
                def.inventoryModel = 69742;
                def.maleModel0 = 69744;
                def.yOffset2d = -6;
                def.xan2d = 496;
                def.yan2d = 9;
                def.zoom2d = 2061;
                break;
            case 30020:
                def.name = "<col=65280>Sarkis dark coif";
                def.femaleModel0 = 58945;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 58947;
                def.maleModel0 = 58908;
                def.yOffset2d = -12;
                def.xan2d = 118;
                def.yan2d = 10;
                def.zoom2d = 1236;
                break;

            case 30021:
                def.name = "<col=65280>Sarkis dark body";
                def.femaleModel0 = 58952;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 58954;
                def.maleModel0 = 58953;
                def.yOffset2d = -3;
                def.xan2d = 514;
                def.yan2d = 2041;
                def.zoom2d = 1358;
                break;

            case 30022:
                def.name = "<col=65280>Sarkis dark legs";
                def.ambient = 30;
                def.contrast = 20;
                def.femaleModel0 = 58946;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 58951;
                def.maleModel0 = 58950;
                def.yOffset2d = 3;
                def.xan2d = 1646;
                def.yan2d = 9;
                def.zoom2d = 2100;
                break;

            case 30104:
                def.name = "<col=65280>Resource pack";
                def.interfaceOptions = new String[]{"Open", null, null, null, "Drop"};
                def.inventoryModel = 59006;
                def.stackable = true;
                def.xOffset2d = -3;
                def.yOffset2d = -3;
                def.yan2d = 2047;
                def.xan2d = 0;
                def.zoom2d = 951;
                break;

            case 30219:
                def.name = "<col=65280>Summer token";
                def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
                def.inventoryModel = 59047;
                def.xan2d = 468;
                def.yan2d = 56;
                def.yOffset2d = 6;
                def.zoom2d = 450;
                break;

            case 30280:
                def.name = "<col=65280>Agility master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 20, 37, 22};
                def.colorReplace = new short[]{677, 801, -21996, -21993, -21990, -21987, -21986, -21984, -21982, -21978, -21978, -21978};
                def.femaleModel0 = 59050;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59049;
                def.maleModel0 = 59050;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30282:
                def.name = "<col=65280>Attack master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 37, 22, 20};
                def.colorReplace = new short[]{7104, 9151, 911, 914, 917, 920, 921, 923, 925, 925, 925, 929};
                def.femaleModel0 = 59052;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59051;
                def.maleModel0 = 59052;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30284:
                def.name = "<col=65280>Construction master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 20, 37, 22};
                def.colorReplace = new short[]{6061, 5945, 6327, 6330, 6333, 6336, 6337, 6339, 6341, 6345, 6345, 6345};
                def.femaleModel0 = 59054;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59053;
                def.maleModel0 = 59054;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30286:
                def.name = "<col=65280>Cooking master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 20, 37, 22};
                def.colorReplace = new short[]{920, 920, -13680, -13677, -13674, -13671, -13670, -13668, -13666, -13662, -13662, -13662};
                def.femaleModel0 = 59056;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59055;
                def.maleModel0 = 59056;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30288:
                def.name = "<col=65280>Crafting master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 20, 37, 22};
                def.colorReplace = new short[]{9142, 9152, 4511, 4514, 4517, 4520, 4521, 4523, 4525, 4529, 4529, 4529};
                def.femaleModel0 = 59058;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59057;
                def.maleModel0 = 59058;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30290:
                def.name = "<col=65280>Defence master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 20, 37, 22};
                def.colorReplace = new short[]{10460, 10473, -24126, -24123, -24120, -24117, -24116, -24114, -24112, -24108, -24108, -24108};
                def.femaleModel0 = 59060;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59059;
                def.maleModel0 = 59060;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30292:
                def.name = "<col=65280>Farming master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 20, 37, 22};
                def.colorReplace = new short[]{14775, 14792, 22026, 22029, 22032, 22035, 22036, 22038, 22040, 22044, 22044, 22044};
                def.femaleModel0 = 59062;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59061;
                def.maleModel0 = 59062;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30294:
                def.name = "<col=65280>Firemaking master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 20, 37, 22};
                def.colorReplace = new short[]{8125, 9152, 4015, 4018, 4021, 4024, 4025, 4027, 4029, 4033, 4033, 4033};
                def.femaleModel0 = 59064;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59063;
                def.maleModel0 = 59064;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30246:
                def.name = "<col=65280>Fishing master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 20, 37, 22};
                def.colorReplace = new short[]{9144, 9152, -27334, -27331, -27328, -27325, -27324, -27322, -27316, -27314, -27314, -27314};
                def.femaleModel0 = 59066;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59065;
                def.maleModel0 = 59066;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30248:
                def.name = "<col=65280>Fletching master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 20, 37, 22};
                def.colorReplace = new short[]{6067, 9152, -31866, -31863, -31860, -31857, -31856, -31854, -31852, -31848, -31848, -31848};
                def.femaleModel0 = 59068;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59067;
                def.maleModel0 = 59068;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30298:
                def.name = "<col=65280>Herblore master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 20, 37, 22};
                def.colorReplace = new short[]{9145, 9156, 22414, 22417, 22420, 22423, 22424, 22426, 22428, 22432, 22432, 22432};
                def.femaleModel0 = 59070;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59069;
                def.maleModel0 = 59070;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30296:
                def.name = "<col=65280>Hitpoints master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 20, 37, 22};
                def.colorReplace = new short[]{818, 951, 8291, 8294, 8297, 8300, 8301, 8303, 8305, 8309, 8309, 8309};
                def.femaleModel0 = 59072;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59071;
                def.maleModel0 = 59072;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30254:
                def.name = "<col=65280>Hunter master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 20, 37, 22};
                def.colorReplace = new short[]{5262, 6020, 8472, 8475, 8478, 8481, 8482, 8484, 8486, 8490, 8490, 8490};
                def.femaleModel0 = 59074;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59073;
                def.maleModel0 = 59074;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30256:
                def.name = "<col=65280>Magic master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 20, 37, 22};
                def.colorReplace = new short[]{-21967, -21951, 6336, 6339, 6342, 6345, 6346, 6348, 6350, 6354, 6354, 6354};
                def.femaleModel0 = 59076;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59075;
                def.maleModel0 = 59076;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30258:
                def.name = "<col=65280>Mining master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 20, 37, 22};
                def.colorReplace = new short[]{-29240, -29257, 10386, 10389, 10392, 10395, 10396, 10398, 10400, 10404, 10404, 10404};
                def.femaleModel0 = 59078;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59077;
                def.maleModel0 = 59078;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30260:
                def.name = "<col=65280>Prayer master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 20, 37, 22};
                def.colorReplace = new short[]{9163, 9168, 117, 120, 123, 126, 127, 127, 127, 127, 127, 127};
                def.femaleModel0 = 59080;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59079;
                def.maleModel0 = 59080;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30262:
                def.name = "<col=65280>Range master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 20, 37, 22};
                def.colorReplace = new short[]{3755, 3998, 15122, 15125, 15128, 15131, 15132, 15134, 15136, 15140, 15140, 15140};
                def.femaleModel0 = 59082;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59081;
                def.maleModel0 = 59082;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30264:
                def.name = "<col=65280>Runecrafting master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 20, 37, 22};
                def.colorReplace = new short[]{9152, 8128, 10318, 10321, 10324, 10327, 10328, 10330, 10332, 10336, 10336, 10336};
                def.femaleModel0 = 59084;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59083;
                def.maleModel0 = 59084;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30266:
                def.name = "<col=65280>Slayer master cape";
                def.colorFind = new short[]{-8514, -16725};
                def.colorReplace = new short[]{912, 920};
                def.femaleModel0 = 59048;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59085;
                def.maleModel0 = 59048;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30268:
                def.name = "<col=65280>Smithing master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 20, 37, 22};
                def.colorReplace = new short[]{8115, 9148, 10380, 10389, 10392, 10395, 10396, 10398, 10400, 10406, 10406, 10406};
                def.femaleModel0 = 59093;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59086;
                def.maleModel0 = 59093;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30270:
                def.name = "<col=65280>Strength master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 20, 37, 22};
                def.colorReplace = new short[]{935, 931, 27538, 27541, 27544, 27547, 27548, 27550, 27552, 27556, 27556, 27556};
                def.femaleModel0 = 59088;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59087;
                def.maleModel0 = 59088;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30272:
                def.name = "<col=65280>Thieving master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 20, 37, 22};
                def.colorReplace = new short[]{11, 1, -6757, -6754, -6751, -6748, -6747, -6745, -6743, -6739, -6739, -6739};
                def.femaleModel0 = 59090;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59089;
                def.maleModel0 = 59090;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30274:
                def.name = "<col=65280>Woodcutting master cape";
                def.colorFind = new short[]{-8514, -16725, 2, 1029, 1032, 11, 12, 14, 16, 20, 37, 22};
                def.colorReplace = new short[]{25109, 24088, 6693, 6696, 6699, 6702, 6703, 6705, 6707, 6711, 6711, 6711};
                def.femaleModel0 = 59092;
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.inventoryModel = 59091;
                def.maleModel0 = 59092;
                def.xOffset2d = -26;
                def.yOffset2d = 9;
                def.xan2d = 617;
                def.yan2d = 1017;
                def.zoom2d = 2461;
                break;

            case 30275:
                ItemDefinition.copyInventory(def, CAPE_OF_LEGENDS);
                def.name = "<col=65280>Grandmaster Cape";
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.femaleModel0 = 69777;
                def.maleModel0 = 69777;
                def.inventoryModel = 69790;
                def.xOffset2d = -3;
                def.yOffset2d = 147;
                def.xan2d = 252;
                def.yan2d = 896;
                def.zan2d = 2;
                def.zoom2d = 1574;
                def.ambient = 80;
                def.animateInventory = true;
                break;

            case 30276:
                ItemDefinition.copyInventory(def, NEITIZNOT_FACEGUARD);
                def.name = "<col=65280>Grandmaster's Helm";
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.femaleModel0 = 69776;
                def.maleModel0 = 69776;
                def.inventoryModel = 69798;
                def.animateInventory = true;
                break;

            case 30277:
                ItemDefinition.copyInventory(def, PRIMORDIAL_BOOTS);
                def.name = "<col=65280>Grandmaster's Boots";
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.femaleModel0 = 69778;
                def.maleModel0 = 69778;
                def.inventoryModel = 69778;
                def.ambient = 80;
                def.animateInventory = true;
                break;

            case 30278:
                ItemDefinition.copyInventory(def, 27660);
                def.name = "<col=65280>Grandmaster's Mace";
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.femaleModel0 = 69786;
                def.maleModel0 = 69786;
                def.inventoryModel = 69787;
                def.ambient = 100;
                def.animateInventory = true;
                break;

            case 30279:
                ItemDefinition.copyInventory(def, 27679);
                def.name = "<col=65280>Grandmaster's Sceptre";
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.femaleModel0 = 69784;
                def.maleModel0 = 69784;
                def.inventoryModel = 69785;
                def.ambient = 100;
                def.animateInventory = true;
                break;

            case 30281:
                ItemDefinition.copyInventory(def, 27655);
                def.name = "<col=65280>Grandmaster's Bow";
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.femaleModel0 = 69788;
                def.maleModel0 = 69788;
                def.inventoryModel = 69789;
                def.ambient = 100;
                def.animateInventory = true;
                break;

            case 30300:
                ItemDefinition.copyInventory(def, MAX_CAPE);
                def.name = "<col=65280>Grandmaster's Cape";
                def.femaleModel0 = 69782;
                def.maleModel0 = 69782;
                def.inventoryModel = 69783;
                def.xOffset2d = -10;
                def.yOffset2d = -252;
                def.xan2d = 245;
                def.yan2d = 896;
                def.zan2d = 26;
                def.zoom2d = 2113;
                def.ambient = 20;
                def.animateInventory = true;
                break;

            case 30301:
                ItemDefinition.copyInventory(def, SCYTHE_OF_VITUR);
                def.name = "<col=65280>Ethereal Scythe";
                def.femaleModel0 = 69767;
                def.maleModel0 = 69767;
                def.inventoryModel = 69768;
                def.ambient = 100;
                def.animateInventory = true;
                break;

            case 30302:
                ItemDefinition.copyInventory(def, 13190);
                def.name = "<col=65280>$75 bond";
                def.interfaceOptions = new String[]{"Redeem", null, null, null, null};
                def.inventoryModel = 69802;
                def.animateInventory = true;
                break;

            case 30303:
                ItemDefinition.copyInventory(def, FEROCIOUS_GLOVES);
                def.name = "<col=65280>Grandmaster's Gloves";
                def.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
                def.femaleModel0 = 69796;
                def.maleModel0 = 69796;
                def.inventoryModel = 69797;
                def.zoom2d = 965;
                def.yan2d = 1087;
                def.xan2d = 496;
                def.zan2d = 2043;
                def.xOffset2d = 0;
                def.yOffset2d = 0;
                def.animateInventory = true;
                break;

            case 30304:
                ItemDefinition.copyInventory(def, 745);
                def.name = "<col=65280>Grandmaster's Heart";
                def.interfaceOptions = new String[]{null, "Use", null, null, "Drop"};
                def.inventoryModel = 69800;
                def.animateInventory = true;
                break;

            case 30305:
                ItemDefinition.copyInventory(def, 19515);
                def.name = "<col=65280>Ruinous Prayer Book";
                def.inventoryModel = 69824;
                def.animateInventory = true;
                break;
            case 30306:
                def.name = "<col=cd2626>Bond's casket";
                def.interfaceOptions = new String[]{"Open", null, null, null, "Drop"};
                def.inventoryModel = 69821;
                def.xOffset2d = 3;
                def.yOffset2d = -6;
                def.xan2d = 164;
                def.yan2d = 1592;
                def.zoom2d = 1410;
                break;

            case 30310:
                def.name = "<col=cd2626>Wings";
                def.interfaceOptions = new String[]{"Open", null, null, null, "Drop"};
                def.inventoryModel = 69835;
                def.xOffset2d = 0;
                def.yOffset2d = 0;
                def.xan2d = 130;
                def.yan2d = 1835;
                def.zoom2d = 3000;
                break;

            case 31003:
                ItemDefinition.copyInventory(def, SALVE_AMULETEI);
                def.name = "<col=cd2626>Summer Amulet";
                def.inventoryModel = 69844;
                def.maleModel0 = 69841;
                def.femaleModel0 = 69841;
                def.animateInventory = true;
                break;

            case 31004:
                ItemDefinition.copyInventory(def, 9911);
                def.name = "<col=cd2626>Summer Soaker";
                def.inventoryModel = 69845;
                def.maleModel0 = 69843;
                def.femaleModel0 = 69843;
                def.animateInventory = true;
                break;

            case 31005:
                def.name = "<col=65280>The One Above All";
                def.interfaceOptions = new String[]{null, null, null, null, "Drop"};
                def.inventoryModel = 58995;
                def.textureFind = new short[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 31};
                def.textureReplace = new short[]{167, 167, 167, 167, 167, 167, 167, 167, 167, 167, 167, 167, 79};
                def.xOffset2d = -100;
                def.yOffset2d = 9;
                def.xan2d = 83;
                def.yan2d = 1855;
                def.zoom2d = 2541;
                break;

            case 31006:
                ItemDefinition.copyInventory(def, 13204);
                def.name = "<col=00ffff>Summer token";
                def.textureFind = new short[]{-1, -1, -1};
                def.textureReplace = new short[]{144, 144, 144};
                def.countCo = new int[]{2, 3, 4, 5, 0, 0, 0, 0, 0, 0};
                def.countObj = new int[]{32237, 32238, 32239, 32239, 0, 0, 0, 0, 0, 0};
            break;

        }
    }
}
