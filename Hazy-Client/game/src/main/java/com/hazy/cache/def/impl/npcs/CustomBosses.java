package com.hazy.cache.def.impl.npcs;

import com.hazy.cache.def.NpcDefinition;
import com.hazy.util.CustomNpcIdentifiers;
import com.hazy.util.NpcIdentifiers;

import static com.hazy.util.NpcIdentifiers.GIANT_ROC;

/**
 * @author Patrick van Elderen | April, 07, 2021, 15) {49
 * @see <a href="https) {//www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class CustomBosses {

    public static void unpack(int id) {
        NpcDefinition definition = NpcDefinition.get(id);

        if(id == 7806) {
            definition.name = "Deranged archaeologist";
            definition.actions = new String[]{null, "Attack", null, null, null, null};
            definition.combatLevel = 276;
            definition.recolorToFind = new short[]{0, 4550, 6798};
            definition.recolorToReplace = new short[]{0, (short)374770, (short)491770};
        }

        if(id == 5912) {
            definition.name = "Diamond deranged arch";
            NpcDefinition.copy(definition, NpcIdentifiers.DERANGED_ARCHAEOLOGIST);
            definition.actions = new String[]{null, "Attack", null, null, null, null};
            definition.combatLevel = 276;
            definition.recolorToFind = new short[]{0, 4550, 6798};
            definition.recolorToReplace = new short[]{0, (short)38693, (short)38693};
        }
        if(id == 8928) {
            definition.name = "Fragment of Seren Z";
            NpcDefinition.copy(definition,NpcIdentifiers.FRAGMENT_OF_SEREN);
        }

        if(id == 5913) {
            definition.name = "DragonStone El Fuego";
            NpcDefinition.copy(definition, CustomNpcIdentifiers.EL_FUEGO);
            definition.actions = new String[]{null, "Attack", null, null, null, null};
            definition.modelCustomColor4 = 49863;
        }

        if (id == 5923) {
            definition.name = "Diamond grim";
            definition.actions = new String[]{null, "Attack", null, null, null};
            definition.combatLevel = 1322;
            definition.recolorToFind = new short[] {10004,25238,8741,4550,908,7073,0};
            definition.recolorToReplace = new short[] {(short)49863,(short)49863,(short)49863,(short)49863,(short)49863,(short)49863,(short)49863};
            definition.models = new int[] {5100,292,170,179,256,507};
            definition.isMinimapVisible = true;
            definition.rotate180Animation = 820;
            definition.quarterClockwiseTurnAnimation = 822;
            definition.quarterAnticlockwiseTurnAnimation = 821;
            definition.standingAnimation = 847;
            definition.occupied_tiles = 3;
            definition.walkingAnimation = 819;
            definition.widthScale = 250;
            definition.heightScale = 250;
        }
        if(id == 7817) {
            definition.name = "Lava beast";
            definition.actions = new String[]{null, "Attack", null, null, null, null};
            definition.combatLevel = 435;//exact combat here
            definition.walkingAnimation = 7675;
            definition.standingAnimation = 7677;
        }

        if(id == 15031) {
            NpcDefinition.copy(definition, NpcIdentifiers.ICE_DEMON);
            definition.name = "Ice demon";
            definition.actions = new String[]{null, "Attack", null, null, null};
            definition.combatLevel = 230;//Combat here
        }

        if (id == 15021) {
            definition.name = "Grim";
            definition.actions = new String[]{null, "Attack", null, null, null};
            definition.combatLevel = 1322;
            definition.recolorToFind = new short[] {10004,25238,8741,4550,908,7073};
            definition.recolorToReplace = new short[] {5231,0,0,5353,0,8084};
            definition.models = new int[] {5100,292,170,179,256,507};
            definition.isMinimapVisible = true;
            definition.rotate180Animation = 820;
            definition.quarterClockwiseTurnAnimation = 822;
            definition.quarterAnticlockwiseTurnAnimation = 821;
            definition.standingAnimation = 847;
            definition.occupied_tiles = 3;
            definition.walkingAnimation = 819;
            definition.widthScale = 250;
            definition.heightScale = 250;
        }

        if (id == 11113) {
            definition.name = "El Fuego";
            definition.actions = new String[]{null, "Attack", null, null, null};
            definition.combatLevel = 420;
            definition.models = new int[]{58995, 58995, 58994, 58996};
            definition.widthScale = 170;
            definition.heightScale = 170;
            definition.standingAnimation = 7870;
            definition.occupied_tiles = 7;
            definition.walkingAnimation = 7870;
        }

        if (id == 15016) {
            definition.name = "Brutal Lava Dragon";
            definition.actions = new String[]{null, "Attack", null, null, null};
            definition.combatLevel = 420;
            definition.models = new int[]{58995, 58995, 58994, 58996};
            definition.widthScale = 170;
            definition.heightScale = 170;
            definition.standingAnimation = 7870;
            definition.occupied_tiles = 7;
            definition.walkingAnimation = 7870;
        }

        if (id == 15019) {
            definition.name = "Brutal Lava Dragon";
            definition.actions = new String[]{null, "Attack", null, null, null};
            definition.combatLevel = 420;
            definition.models = new int[]{58995, 58995, 58994, 58996};
            definition.widthScale = 170;
            definition.heightScale = 170;
            definition.standingAnimation = 90;
            definition.occupied_tiles = 7;
            definition.walkingAnimation = 79;
        }

        if (id == 15001) {
            definition.name = "Corrupted Nechryarch";
            definition.actions = new String[]{null, "Attack", null, null, null};
            definition.combatLevel = 300;
            definition.models = new int[]{58922};
            definition.standingAnimation = 4650;
            definition.occupied_tiles = 2;
            definition.walkingAnimation = 6372;
        }

        if (id == 15003) {
            definition.name = "Necromancer";
            definition.actions = new String[]{null, "Attack", null, null, null};
            definition.recolorToFind = new short[]{-26527, -24618, -26073, 5018, 61, 10351, 33, 24};
            definition.recolorToReplace = new short[]{-19054, 12, 12, -16870, 11177, 61, 16, 12};
            definition.combatLevel = 300;
            definition.models = new int[]{4953, 4955, 556, 58948, 58907, 58950, 58953, 58956};
            definition.widthScale = 160;
            definition.heightScale = 160;
            definition.standingAnimation = 808;
            definition.occupied_tiles = 2;
            definition.walkingAnimation = 819;
        }

        if (id == 15020) {
            definition.name = "Aragog";
            definition.actions = new String[]{null, "Attack", null, null, null};
            definition.recolorToFind = new short[]{138, 908, 794, 912, 916, 0, 103, 107};
            definition.recolorToReplace = new short[]{138, 908, 4769, 4769, 4769, 0, 0, 0};
            definition.combatLevel = 1123;
            definition.models = new int[]{28294, 28295};
            definition.widthScale = 190;
            definition.heightScale = 190;
            definition.standingAnimation = 5318;
            definition.occupied_tiles = 4;
            definition.walkingAnimation = 5317;
        }

        if (id == 15026) {
            definition.name = "Fluffy";
            definition.actions = new String[]{null, "Attack", null, null, null};
            definition.recolorToFind = new short[]{929, 960, 1981, 0, 931, 4029, 926, 902, 922, 918, 924, 904, 916, 912, 935, 939, 906, 920, 955, 910, 914, 7101, 11200, 957, 9149, 908, 4, 5053, 8125, 6069};
            definition.recolorToReplace = new short[]{4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 4769, 0, 4769, 0, 4769, 0, 0, 4769, 4769};
            definition.combatLevel = 636;
            definition.models = new int[]{29270};
            definition.standingAnimation = 4484;
            definition.occupied_tiles = 5;
            definition.walkingAnimation = 4488;
            definition.widthScale = 100;
            definition.heightScale = 100;
        }

        if (id == 15028) {
            definition.name = "Dementor";
            definition.actions = new String[]{null, "Attack", null, null, null};
            definition.ambient = 20;
            definition.recolorToFind = new short[]{10343, -22250, -22365, -22361, -22353, -22464, -22477, -22456, -22473, -22452};
            definition.recolorToReplace = new short[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            definition.combatLevel = 126;
            definition.contrast = 20;
            definition.models = new int[]{21154};
            definition.chatheadModels = new int[]{21394};
            definition.standingAnimation = 5538;
            definition.walkingAnimation = 5539;
        }

        if (id == 15030) {
            definition.name = "Centaur";
            definition.actions = new String[]{null, "Attack", null, null, null};
            definition.combatLevel = 126;
            definition.models = new int[]{16196, 16202, 16199, 16200};
            definition.chatheadModels = new int[]{16213};
            definition.standingAnimation = 4311;
            definition.occupied_tiles = 2;
            definition.walkingAnimation = 4310;
        }

        if (id == 15032) {
            definition.name = "Centaur";
            definition.actions = new String[]{null, "Attack", null, null, null};
            definition.combatLevel = 126;
            definition.models = new int[]{16195, 16201, 16198, 16197, 16200};
            definition.chatheadModels = new int[]{16212, 16211};
            definition.standingAnimation = 4311;
            definition.occupied_tiles = 2;
            definition.walkingAnimation = 4310;
        }

        if (id == 15034) {
            definition.name = "Hungarian horntail";
            definition.actions = new String[]{null, "Attack", null, null, null};
            definition.ambient = 30;
            definition.recolorToFind = new short[]{0, 30635, 29390, 29526, 31271, 31393, 31151, 32200, 31192, 127};
            definition.recolorToReplace = new short[]{5662, 127, 5662, 5662, 5662, 5662, 5662, 5662, 127, 5662};
            definition.combatLevel = 172;
            definition.models = new int[]{38610};
            definition.widthScale = 110;
            definition.heightScale = 110;
            definition.standingAnimation = 90;
            definition.occupied_tiles = 4;
            definition.walkingAnimation = 79;
        }

        if (id == 15050) {
            definition.name = "Fenrir greyback";
            definition.actions = new String[]{null, "Attack", null, null, null};
            definition.combatLevel = 655;
            definition.models = new int[]{26177, 26188, 26181};
            definition.chatheadModels = new int[]{26113};
            definition.standingAnimation = 6539;
            definition.walkingAnimation = 6541;
        }

        if (id == 16008) {
            NpcDefinition.copy(definition, NpcIdentifiers.CERBERUS);
            definition.name = "Kerberos";
            definition.modelCustomColor4 = 125;
        }

        if (id == 16009) {
            NpcDefinition.copy(definition, NpcIdentifiers.SCORPIA);
            definition.name = "Skorpios";
            definition.modelCustomColor4 = 125;
        }

        if (id == 16010) {
            NpcDefinition.copy(definition, NpcIdentifiers.VENENATIS);
            definition.name = "Arachne";
            definition.modelCustomColor4 = 125;
        }

        if (id == 16011) {
            NpcDefinition.copy(definition, NpcIdentifiers.CALLISTO);
            definition.name = "Artio";
            definition.modelCustomColor4 = 115;
        }

        if(id == 16012) {
            NpcDefinition.copy(definition, NpcIdentifiers.REVENANT_IMP);
            definition.name = "Ethereal Imp";
            definition.actions = new String[]{null, "Attack", null, null, null};
            definition.combatLevel = 350;
            definition.models = new int[]{69769};
        }
        if(id == 16013) {
            NpcDefinition.copy(definition, NpcIdentifiers.REVENANT_DRAGON);
            definition.name = "Ethereal Dragon";
            definition.actions = new String[]{null, "Attack", null, null, null};
            definition.combatLevel = 350;
            definition.models = new int[]{69770};
        }
        if(id == 16014) {
            NpcDefinition.copy(definition, NpcIdentifiers.REVENANT_KNIGHT);
            definition.name = "Ethereal Knight";
            definition.actions = new String[]{null, "Attack", null, null, null};
            definition.combatLevel = 350;
            definition.models = new int[]{69771};
        }
        if(id == 16015) {
            NpcDefinition.copy(definition, NpcIdentifiers.REVENANT_CYCLOPS);
            definition.name = "Ethereal Cyclopse";
            definition.actions = new String[]{null, "Attack", null, null, null};
            definition.combatLevel = 350;
            definition.models = new int[]{69772};
        }
        if(id == 16016) {
            NpcDefinition.copy(definition, NpcIdentifiers.REVENANT_HOBGOBLIN);
            definition.name = "Ethereal Goblin";
            definition.actions = new String[]{null, "Attack", null, null, null};
            definition.combatLevel = 350;
            definition.models = new int[]{69773};
        }
        if(id == 16017) {
            NpcDefinition.copy(definition, NpcIdentifiers.CORPOREAL_BEAST);
            definition.name = "Demonic Corporeal Beast";
            definition.actions = new String[]{null, "Attack", null, null, null};
            definition.combatLevel = 350;
            definition.models = new int[]{69781};
            definition.widthScale = 120;
            definition.heightScale = 120;
        }

        if (id == 16018) {
            NpcDefinition.copy(definition,GIANT_ROC);
            definition.name = "Grandmaster's Skeletal Roc";
            definition.models = new int[]{69823};
        }
    }
}
