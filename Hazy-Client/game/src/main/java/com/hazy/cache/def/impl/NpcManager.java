package com.hazy.cache.def.impl;

import com.hazy.ClientConstants;
import com.hazy.cache.def.NpcDefinition;

import static com.hazy.util.NpcIdentifiers.*;

public class NpcManager {

    public static void unpack(int id) {
        NpcDefinition definition = NpcDefinition.get(id);
        boolean impling = false;

        switch (id) {
            case 2312:
                definition.name = "Jawa";
                definition.models = new int[]{28285, 181, 249, 28286, 28226, 176, 28223};
                definition.modelCustomColor = 4769;
                definition.heightScale = 80;
                definition.widthScale = 80;
                definition.rotate180Animation = 820;
                definition.quarterClockwiseTurnAnimation = 822;
                definition.quarterAnticlockwiseTurnAnimation = 821;
                definition.standingAnimation = 808;
                definition.walkingAnimation = 819;
                break;
            case TRADE_REFEREE:
                definition.name = "RiskZone Point Shop";
                definition.actions = new String[]{"Talk-to", null, "Shop", null, null};
                break;

            case EMERALD_BENEDICT:
                definition.name = "Tournament Point Shop";
                definition.actions = new String[]{"Talk-to", null, "Bank", "Shop", null};
                break;

            case 11112:
                definition.name = "Santa";
                definition.actions = new String[]{"Talk-to", null, null, null, null};
                definition.recolorToFind = new short[]{6798, 8078, 8741, 25238, 6587, 5400};
                definition.recolorToReplace = new short[]{86, 10351, 933, 933, 10351, 0};
                definition.combatLevel = 0;
                definition.models = new int[]{235, 189, 299, 4226, 4218, 162, 4924, 4925, 4926};
                definition.chatheadModels = new int[]{63, 82, 69};
                definition.isMinimapVisible = true;
                definition.rotate180Animation = 820;
                definition.quarterAnticlockwiseTurnAnimation = 822;
                definition.quarterClockwiseTurnAnimation = 821;
                definition.standingAnimation = 808;
                definition.occupied_tiles = 1;
                definition.walkingAnimation = 819;
                break;

            case 3443:
                definition.name = "Lord voldemort";
                definition.combatLevel = 1433;
                definition.actions = new String[]{null, "Attack", null, null, null, null};
                break;

            case 1922:
                definition.actions = new String[]{"Talk-to", null, "Lost-items", null, null, null};
                break;

            case 7632:
                definition.name = "Men in black";
                definition.combatLevel = 80;
                definition.actions = new String[]{null, "Attack", null, null, null, null};
                break;

            case 13000:
                definition.name = "<col=ffb000>Pure bot";
                definition.combatLevel = 80;
                definition.actions = new String[]{null, "Attack", null, null, null, null, null};
                //Copied these animations and initial model ids from 1158. This is how to do custom NPCs properly.
                definition.standingAnimation = 808;
                definition.walkingAnimation = 819;
                definition.rotate180Animation = 820;
                definition.quarterAnticlockwiseTurnAnimation = 821;
                definition.quarterClockwiseTurnAnimation = 821;
                definition.models = new int[]{214, 250, 3379, 164, 179, 268, 185, 550, 521, 3189};
                definition.models[5] = 258; //black d'hide chaps
                definition.models[0] = 9639; //Obsidian cape
                definition.models[1] = 218; //Head - let's recolor steel full helm
                //definition.recolorTarget = new int[] { 33, -17506 }; //add +65536 to negative number.
                //definition.modelId[1] = 220; //Head - coif
                definition.models[2] = 306; // let's recolor steel platebody
                //definition.modelId[2] = 306; //Steel platebody
                //start of rune platebody.
                //anything that uses model 306 would have src colors 24, 61, 41 and need to be recolored.
                //itemDefinition.destItemColors = new int[] { 61, -29403, -28266 }; //add +65536 to negative numbers, these are straight from item def configuration 74 txt.
                //itemDefinition.srcItemColors = new int[] { 24, 61, 41 };
                //definition.recolorOriginal = new int[] { 24, 61, 41 };
                //definition.recolorTarget = new int[] { 61, 36133, 37270 }; //this is rune
                //end of rune platebody.
                definition.models[8] = 15413; //Shield iron defender
                definition.models[7] = 6031; // weapon d scim
                definition.models[4] = 24558; //Combat bracelet
                definition.models[6] = 3704; // boots climbing
                definition.models[9] = 290; //amulet glory
                //If recoloring multiple items, make sure to add all numbers in one array, and make sure the indexes match for source/original and destination/target.
                definition.recolorToReplace = new short[]{33, (short) 48030, 61, 33, 24, -14425 + (short) 65536, 127, 127, 4, 16};
                definition.recolorToFind = new short[]{61, 926, 24, 61, 41, 127, 5916, 4882, 22416, 22424};
                break;
            case 13001:
                NpcDefinition.copy(definition, 13000);
                //definition.modelId[7] = 19839; // weapon armadyl c'bow
                //definition.modelId[7] = 4037; // weapon dragon warhammer
                definition.models[7] = 539; // weapon dds
                definition.standingAnimation = 1832; // re-use whip standing anim
                //recolors must be copied to the definition copy (spec bot from original bot).
                definition.recolorToReplace = new short[]{33, (short) 48030, 61, 33, 24, -14425 + (short) 65536, 127, 127, 4, 16};
                definition.recolorToFind = new short[]{61, 926, 24, 61, 41, 127, 5916, 4882, 22416, 22424};
                //definition.walkingAnimation = 7223;
                //definition.modelId[7] = 539; // weapon dds
                break;
            case 13002:
                definition.name = "<col=ffb000>F2p bot";
                definition.combatLevel = 68;
                definition.actions = new String[]{null, "Attack", null, null, null, null, null};
                //Copied these animations and initial model ids from 1158. This is how to do custom NPCs properly.
                definition.standingAnimation = 808;
                definition.walkingAnimation = 819;
                definition.rotate180Animation = 820;
                definition.quarterAnticlockwiseTurnAnimation = 821;
                definition.quarterClockwiseTurnAnimation = 821;
                //monk robe looks weird with this, don't use warrior, use man ints
                //definition.modelId = new int[] { 214, 250, 3379, 164, 179, 268, 185, 550, 521, 3189, 292 };
                //type.models = new int[] { 215, 281, 246, 292, 151, 176, 254, 181, 323 };
                //man ints:
                //These are man ints plus the 292
                definition.models = new int[]{215, 281, 246, 292, 151, 176, 254, 181, 323, 292};
                definition.models[5] = 258; //green d'hide chaps
                //definition.modelId[0] = 3189; //legends cape
                //TODO by ken: find a more suitable (full) helmet than ironman helm
                definition.models[1] = 498; //Head - ironman helm
                definition.models[2] = 170; //Monk's robe top
                //definition.modelId[8] = 541; //Iron sq shield
                definition.models[7] = 490; //weapon r scim
                definition.models[4] = 355; //green d'hide vamb
                definition.models[6] = 185; //leather boots
                definition.models[9] = 290; //amulet str
                definition.recolorToReplace = new short[]{960, 127, 127, -29403, 33, 4769, 1950, 2749};
                definition.recolorToFind = new short[]{127, 5916, 4882, 61, 57, 8741, 7700, 11200};
                break;
            case 13003:
                NpcDefinition.copy(definition, 13002);
                //definition.modelId[7] = 19839; // weapon armadyl c'bow
                //definition.modelId[7] = 550; // weapon rune battleaxe
                definition.models[7] = 546; // weapon rune 2h
                definition.recolorToReplace = new short[]{960, 127, 127, -29403, 33, 4769};
                definition.recolorToFind = new short[]{127, 5916, 4882, 61, 57, 8741};
                definition.standingAnimation = 2561; // 2h anim
                definition.walkingAnimation = 2064; // 2h anim

                //definition.modelId[7] = 539; // weapon dds
                break;
            case 13004:
                definition.name = "<col=ffb000>Maxed bot";
                definition.combatLevel = 126;
                definition.actions = new String[]{null, "Attack", null, null, null, null, null};
                definition.standingAnimation = 808;
                definition.walkingAnimation = 819;
                definition.rotate180Animation = 820;
                definition.quarterAnticlockwiseTurnAnimation = 821;
                definition.quarterClockwiseTurnAnimation = 821;
                definition.models = new int[]{214, 250, 3379, 164, 179, 268, 185, 550, 521, 3189};
                definition.models[5] = 268; //platelegs rune
                definition.models[0] = 18954; //Str cape
                //definition.modelId[1] = 21873; //Head - neitznot
                definition.models[1] = 218; //Head - rune full helm
                definition.models[8] = 35376; //Shield avernic defender (or 31904 for dragon defender t the gold dragon defender)
                definition.models[7] = 5409; // weapon whip
                definition.models[4] = 13307; //Gloves barrows
                definition.models[6] = 3704; // boots climbing
                definition.models[9] = 290; //amulet glory
                definition.recolorToReplace = new short[]{935, 931, 924, 27544, 27544, 26516, 61, -29403 + (short) 65536, -28266 + (short) 65536, -29403 + (short) 65536, -28266 + (short) 65536, -29403 + (short) 65536, -29403 + (short) 65536, -17506 + (short) 65536};
                definition.recolorToFind = new short[]{-8256 + (short) 65536, -11353 + (short) 65536, -11033 + (short) 65536, 960, 22464, -21568 + (short) 65536, 24, 61, 41, 61, 41, 57, 61, 926};
                break;
            case 13005:
                NpcDefinition.copy(definition, 13004);
                //definition.modelId[7] = 27649; // weapon ags
                //definition.modelId[7] = 5410; // weapon gmaul
                definition.models[7] = 539; // weapon dds
                definition.standingAnimation = 1832; // re-use whip standing anim
                //recolors must be copied to the definition copy (spec bot from original bot).
                definition.recolorToReplace = new short[]{935, 931, 924, 27544, 27544, 26516, 61, -29403 + (short) 65536, -28266 + (short) 65536, -29403 + (short) 65536, -28266 + (short) 65536, -29403 + (short) 65536, -29403 + (short) 65536, -17506 + (short) 65536};
                definition.recolorToFind = new short[]{-8256 + (short) 65536, -11353 + (short) 65536, -11033 + (short) 65536, 960, 22464, -21568 + (short) 65536, 24, 61, 41, 61, 41, 57, 61, 926};
                //definition.standingAnimation = 7053; //ags
                //definition.walkingAnimation = 7052; //ags
                //definition.modelId[7] = 539; // weapon dds
                break;
            case 13006:
                definition.name = "<col=ffb000>Archer bot";
                definition.combatLevel = 90;
                definition.actions = new String[]{null, "Attack", null, null, null, null, null};
                //from 4096 npc archer
                definition.models = new int[]{220, 246, 303, 167, 179, 277, 185, 563};
                definition.models[0] = 20423; //cape avas
                //definition.modelId[1] = 21873; //Head - neitznot
                definition.models[1] = 6655; //Head - karil's coif
                definition.models[7] = 31237; // weapon crossbow, possibly ballista
                definition.models[4] = 13307; //Gloves barrows
                definition.models[6] = 3704; // boots climbing
                definition.models[5] = 20139; //platelegs zammy hides
                definition.models[2] = 20157; //platebody zammy hides
                definition.standingAnimation = 7220;
                definition.walkingAnimation = 7223;
                definition.rotate180Animation = 7220;
                definition.quarterAnticlockwiseTurnAnimation = 7220;
                definition.quarterClockwiseTurnAnimation = 7220;
                definition.recolorToReplace = new short[]{10388, 10512, 10508, 10760, 11140};
                definition.recolorToFind = new short[]{10394, 10388, 10514, 10638, 10762};
                break;
            case 13008:
                definition.name = "<col=ffb000>Pure Archer bot";
                definition.combatLevel = 80;
                definition.actions = new String[]{null, "Attack", null, null, null, null, null};
                //from 4096 npc archer
                definition.models = new int[]{220, 246, 303, 167, 179, 277, 185, 563};
                definition.models[0] = 20423; //cape avas
                definition.models[1] = 218; //Head - let's recolor steel full helm
                //definition.modelId[0] = 3189; //legends cape
                //definition.modelId[8] = 541; //Iron sq shield
                //entityDef.modelId[7] = 6712; //weapon karils cbow
                definition.models[7] = 512; //weapon karils cbow
                //entityDef.modelId[4] = 355; //black d'hide vamb
                definition.models[4] = 24558; //Combat bracelet
                definition.models[6] = 185; //leather boots
                //entityDef.modelId[4] = 13307; //Gloves barrows
                definition.models[6] = 3704; // boots climbing
                definition.models[5] = 258; //platelegs zammy hides
                definition.models[2] = 311; //leather body
                definition.standingAnimation = 808;
                definition.walkingAnimation = 819;
                definition.rotate180Animation = 820;
                definition.quarterAnticlockwiseTurnAnimation = 821;
                definition.quarterClockwiseTurnAnimation = 821;
                definition.recolorToReplace = new short[]{929, 31516, 33, (short) 48030, 4, 16, -14425 + (short) 65536, 127, 127};
                definition.recolorToFind = new short[]{127, 6674, 61, 926, 22416, 22424, 127, 5916, 4882};
                break;
            case 13009:
                NpcDefinition.copy(definition, 13008);
                //definition.modelId[7] = 27649; // weapon ags
                //definition.modelId[7] = 5410; // weapon gmaul
                definition.models[7] = 31237; // weapon crossbow, possibly ballista
                definition.standingAnimation = 7220;
                definition.walkingAnimation = 7223;
                definition.rotate180Animation = 7220;
                definition.quarterAnticlockwiseTurnAnimation = 7220;
                definition.quarterClockwiseTurnAnimation = 7220;
                //recolors must be copied to the definition copy (spec bot from original bot).
                definition.recolorToReplace = new short[]{929, 31516, 33, (short) 48030, 4, 16};
                definition.recolorToFind = new short[]{127, 6674, 61, 926, 22416, 22424};
                //definition.standingAnimation = 7053; //ags
                //definition.walkingAnimation = 7052; //ags
                //definition.modelId[7] = 539; // weapon dds
                break;

            case 317:
                definition.actions = new String[]{"Talk-to", null, "Boss-points", null, null};
                break;

            case 2149:
                definition.actions = new String[]{"Exchange", null, null, null, null};
                break;

            // Shura
            case 9413:
                definition.name = "<col=00ACFF>Referral Manager</col>";
                definition.actions = new String[]{"Talk-to", null, null, null, null};
                definition.combatLevel = 0;
                definition.models = new int[]{39207};
                definition.chatheadModels = new int[]{39485};
                definition.standingAnimation = 8551;
                definition.isMinimapVisible = true;
                definition.walkingAnimation = 8552;
                break;

            case 15016:
            case 15021:
            case 15019:
            case SHOP_ASSISTANT_2820:
            case GRUM_2889:
                definition.isClickable = true;
                break;
            case THORODIN_5526:
                definition.name = "Boss slayer master";
                definition.actions = new String[]{"Talk-to", null, "Slayer-Equipment", "Slayer-Rewards", null};
                break;
            case TRAIBORN:
            case GUNNJORN:
                definition.isClickable = true;
                definition.actions = new String[]{"Weapons", null, "Armour", "Ironman", null};
                break;
            case TWIGGY_OKORN:
                definition.actions = new String[]{"Talk-to", null, "Claim-cape", null, null};
                break;

            case 8231:
                definition.name = "Supplies shop";
                definition.actions[0] = "Boss-points";
                definition.actions[2] = "Skill Supplies";
                break;

            case IRON_MAN_TUTOR:
                definition.actions[0] = "Change-game-mode";
                definition.actions[2] = "Ironman-supplies";
                definition.actions[3] = "Ironman-herblore-supplies";
                break;

            case FANCY_DAN:
                definition.name = "Vote Manager";
                definition.actions[0] = "Trade";
                definition.actions[2] = "Cast-votes";
                break;

            case WISE_OLD_MAN:
                definition.name = "Credit Manager";
                definition.actions[0] = "Talk-to";
                definition.actions[2] = "Open-Shop";
                definition.actions[3] = "Claim-purchases";
                break;

            case SHOP_CONSUMABLES:
                definition.name = "Consumables";
                definition.actions[0] = "Open-Shop";
                break;

            case SHOP_GEAR:
                definition.name = "Early & Mid Equipments";
                definition.actions[0] = "Open-Shop";
                break;

            case SECURITY_GUARD:
                definition.name = "Security Advisor";
                definition.actions[0] = "Check Pin Settings";
                break;

            case SIGMUND_THE_MERCHANT:
                definition.actions[0] = "Buy-items";
                definition.actions[2] = "Sell-items";
                definition.actions[3] = "Sets";
                definition.actions[4] = null;
                break;

            case MAKEOVER_MAGE_1307:
                definition.actions[0] = "Change-looks";
                definition.actions[2] = "Title-unlocks";
                definition.actions[3] = null;
                definition.actions[4] = null;
                break;

            case GRAND_EXCHANGE_CLERK:
                definition.actions[0] = "Exchange";
                definition.actions[2] = null;
                definition.actions[3] = null;
                definition.actions[4] = null;
                break;

            case FRANK:
                definition.name = "Shop";
                definition.actions[0] = "Untradeable";
                break;

            case RADIGAD_PONFIT:
                definition.name = "Ranged Shop";
                definition.actions = new String[]{"Weapons", null, "Armour", "Ironman", null};
                break;

            case CLAUS_THE_CHEF:
                definition.name = "Shop";
                definition.actions[0] = "Consumable";
                break;

            case SPICE_SELLER_4579:
                definition.name = "Shop";
                definition.actions[0] = "Misc";
                break;

            case LISA:
                definition.name = "Tournament Manager";
                definition.actions = new String[]{"Sign-up", null, "Quick-join", "Quick-spectate", null, null, null};
                break;

            case VANNAKA:
                definition.name = "Task master";
                definition.actions = new String[]{"Talk-to", null, "Progress", null, null};
                break;

            case COMBAT_DUMMY:
                definition.actions = new String[]{null, "Attack", null, null, null};
                definition.hasRenderPriority = true;
                break;
            case ROCK_CRAB:
            case ROCK_CRAB_102:
                definition.hasRenderPriority = true;
                break;
            case 6481:
                definition.actions = new String[]{"Talk-to", null, "Skillcape", null, null, null, null};
                break;
            case 1635:
            case 1636:
            case 1637:
            case 1638:
            case 1639:
            case 1640:
            case 1641:
            case 1642:
            case 1643:
            case 1644:
            case 7233:
                impling = true;
                break;

            // tournament guy
            case 8146:
                definition.actions = new String[]{"Tournament", null, "Quick-join", "Quick-spectate", null, null, null};
                break;
            // emblem trader
            case 308:
                definition.actions = new String[]{"Talk-to", null, "Trade", null, null, null, null};
                break;

            // Sigbert the Adventurer
            case 3254:
                definition.actions = new String[]{"Talk-to", null, "Trade", null, null};
                break;
            case 5979:
                definition.name = "Weapon Upgrader";
                break;

            case 306:
                definition.name = ClientConstants.CLIENT_NAME + " Guide";
                break;
            // Zombies Champion
            case 3359:
                definition.combatLevel = 785;
                break;
            //Vorkath
            case 319:
            case 8061:
                definition.largeHpBar = true;
                break;
        }

        if (definition != null && impling) {
            definition.isClickable = true;
        }
    }
}
