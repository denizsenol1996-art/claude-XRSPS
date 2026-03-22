package com.hazy.cache.def.impl;

import com.hazy.cache.def.ObjectDefinition;

public class ObjectManager {

    public static void get(int id) {
        ObjectDefinition definition = ObjectDefinition.get(id);

        if(id == 27095) {
            definition.name = "Portal";
        }
        if(id == 29867) {
            definition.name = "Afk Smelting";
            definition.scene_actions = new String[]{"Smith", null, null, null, null};

        }
        if(id == 33318) {//update19
            definition.name = "Afk firemaking";
            definition.scene_actions = new String[] {"Action",null,null,null,null};

        }
        if(id == 18826) {//update19
            definition.name = "Afk farming";
            definition.scene_actions = new String[] {"Action",null,null,null,null};

        }
        if(id == 31825) {
            definition.name = "Pool of wealth";
            definition.scene_actions = new String[]{"Fill",null, null, null, null};
        }
        if(id == 20839) {
            definition.name = "Boss barrier";
            definition.scene_actions = new String[]{"Enter", null, null, null, null};

        }
        if(id == 32629) {
            definition.scene_actions = new String[]{"Loot", null, null, null, null};
        }

        if (id == 2341 || id == 2342 || id == 17977) {
            definition.scene_actions = new String[]{null, null, null, null, null};
        }

        if (id == 6437) {
            definition.name = "Tom Riddle’s gravestone";
            definition.scene_actions = new String[]{"Reward", null, null, null, null};
        }

        if (id == 33456) {
            definition.name = "Portkey";
            definition.scene_actions = new String[]{"Apparate", null, null, null, null};
        }

        if (id == 13503) {
            definition.scene_actions = new String[]{"Leave", null, null, null, null};
        }

        if (id == 50004) {
            definition.name = "Dark rejuvenation pool";
            definition.scene_actions = new String[]{"Drink", null, null, null, null};
            definition.ambient = 40;
            definition.animationID = 7304;
            definition.interactType = 1;
            definition.wallOrDoor = 1;
            definition.height = 2;
            definition.objectModels = new int[]{58959};
            definition.mergeNormals = true;
            definition.supportsItems = 1;
            definition.width = 2;
        }

        if (id == 31621) {
            definition.name = "50s";
        }

        if (id == 31622) {
            definition.name = "Member cave";
        }

        if (id == 31618) {
            definition.name = "gdz";
        }

        if(id == 2515) {
            definition.scene_actions = new String[] {"Travel", null, null, null, null};
        }

        if(id == 10060 || id == 7127 || id == 31626 || id == 4652 || id == 4653 || id == 34728) {
            definition.scene_actions = new String[] {null, null, null, null, null};
        }

        if(id == 562 || id == 3192) {
            definition.scene_actions = new String[] {"Live scoreboard", "Todays top pkers", null, null, null};
        }

        if(id == 6552 || id == 31923) {
            definition.scene_actions = new String[] {"Change spellbook", null, null, null, null};
        }

        if (id == 29165) {
            definition.name = "Pile Of Coins";
            definition.scene_actions[0] = null;
            definition.scene_actions[1] = null;
            definition.scene_actions[2] = null;
            definition.scene_actions[3] = null;
            definition.scene_actions[4] = null;
        }

        if(id == 33020) {
            definition.name = "Forging table";
            definition.scene_actions = new String[] {"Forge", null, null, null, null};
        }

        if(id == 8878) {
            definition.name = "Item dispenser";
            definition.scene_actions = new String[] {"Dispense", "Exchange coins", null, null, null};
        }

        if(id == 637) {
            definition.name = "Item cart";
            definition.scene_actions = new String[] {"Check cart", "Item list", "Clear cart", null, null};
        }

        if (id == 13291) {
            definition.scene_actions = new String[] {"Open", null, null, null, null};
        }

        if (id == 23709) {
            definition.scene_actions[0] = "Use";
        }

        if (id == 2156) {
            definition.name = "World Boss Portal";
        }

        if (id == 27780) {
            definition.name = "Scoreboard";
        }

        if (id == 14986) {
            definition.name = "Key Chest";

            ObjectDefinition deadmanChest = ObjectDefinition.get(27269);

            definition.objectModels = deadmanChest.objectModels;
            definition.recolorToFind = deadmanChest.recolorToFind;
            definition.scene_actions = deadmanChest.scene_actions;
            definition.recolorToReplace = deadmanChest.recolorToReplace;
        }
        if (id == 6574) {
            definition.name = "Afk stall";
        }
        if (id == 3881) {
            definition.name = "Afk tree";
        }
        if (id == 7811) {
            definition.name = "Supplies";
            definition.scene_actions[0] = "Blood money supplies";
            definition.scene_actions[1] = "Vote-rewards";
            definition.scene_actions[2] = "Donator-store";
        }
        if (id == 31944) {
            definition.name = "Food/Potions/Gear";
            definition.scene_actions[0] = "Food";
            definition.scene_actions[1] = "Potions";
            definition.scene_actions[2] = "Gear";
        }

        if(id == 27095) {
            definition.name = "Portal";
        }

        if(id == 32629) {
            definition.scene_actions = new String[]{"Loot", null, null, null, null};
        }

        if (id == 2341 || id == 2342 || id == 17977) {
            definition.scene_actions = new String[]{null, null, null, null, null};
        }

        if (id == 6437) {
            definition.name = "Tom Riddle’s gravestone";
            definition.scene_actions = new String[]{"Reward", null, null, null, null};
        }

        if (id == 33456) {
            definition.name = "Portkey";
            definition.scene_actions = new String[]{"Apparate", null, null, null, null};
        }

        if (id == 13503) {
            definition.scene_actions = new String[]{"Leave", null, null, null, null};
        }

        if (id == 50004) {
            definition.name = "Dark rejuvenation pool";
            definition.scene_actions = new String[]{"Drink", null, null, null, null};
            definition.ambient = 40;
            definition.animationID = 7304;
            definition.interactType = 1;
            definition.wallOrDoor = 1;
            definition.height = 2;
            definition.objectModels = new int[]{58959};
            definition.mergeNormals = true;
            definition.supportsItems = 1;
            definition.width = 2;
        }

        if (id == 31621) {
            definition.name = "50s";
        }

        if (id == 31622) {
            definition.name = "Member cave";
        }

        if (id == 31618) {
            definition.name = "gdz";
        }

        if(id == 2515) {
            definition.scene_actions = new String[] {"Travel", null, null, null, null};
        }

        if(id == 10060 || id == 7127 || id == 31626 || id == 4652 || id == 4653) {
            definition.scene_actions = new String[] {null, null, null, null, null};
        }

        if(id == 562 || id == 3192) {
            definition.scene_actions = new String[] {"Live scoreboard", "Todays top pkers", null, null, null};
        }

        if(id == 6552 || id == 31923) {
            definition.scene_actions = new String[] {"Change spellbook", null, null, null, null};
        }

        if (id == 29165) {
            definition.name = "Pile Of Coins";
            definition.scene_actions[0] = null;
            definition.scene_actions[1] = null;
            definition.scene_actions[2] = null;
            definition.scene_actions[3] = null;
            definition.scene_actions[4] = null;
        }

        if(id == 8878) {
            definition.name = "Item dispenser";
            definition.scene_actions = new String[] {"Dispense", "Exchange coins", null, null, null};
        }

        if(id == 637) {
            definition.name = "Item cart";
            definition.scene_actions = new String[] {"Check cart", "Item list", "Clear cart", null, null};
        }

        if (id == 13291) {
            definition.scene_actions = new String[] {"Open", null, null, null, null};
        }

        if (id == 23709) {
            definition.scene_actions[0] = "Use";
        }

        if (id == 2156) {
            definition.name = "World Boss Portal";
        }

        if (id == 27780) {
            definition.name = "Scoreboard";
        }

        if (id == 14986) {
            definition.name = "Key Chest";

            ObjectDefinition deadmanChest = ObjectDefinition.get(27269);

            definition.objectModels = deadmanChest.objectModels;
            definition.recolorToFind = deadmanChest.recolorToFind;
            definition.scene_actions = deadmanChest.scene_actions;
            definition.recolorToReplace = deadmanChest.recolorToReplace;
        }

        if (id == 7811) {
            definition.name = "Supplies";
            definition.scene_actions[0] = "Blood money supplies";
            definition.scene_actions[1] = "Vote-rewards";
            definition.scene_actions[2] = "Donator-store";
        }
        if (id == 31944) {
            definition.name = "Food/Potions/Gear";
            definition.scene_actions[0] = "Food";
            definition.scene_actions[1] = "Potions";
            definition.scene_actions[2] = "Gear";
        }

        if(id == 2654) {
            definition.name = "Blood fountain";
            definition.width = 3;
            definition.height = 3;
            definition.scene_actions[0] = "Rewards";
            definition.scene_actions[1] = null;
            definition.recolorToFind = new int[]{10266, 10270, 10279, 10275, 10283, (short) 33325, (short) 33222};
            definition.recolorToReplace = new int[]{10266, 10270, 10279, 10275, 10283, 926, 926};
        }
    }
}
