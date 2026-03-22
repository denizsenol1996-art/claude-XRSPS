package com.hazy.model.content.hoverMenu;

import com.hazy.Client;
import com.hazy.ClientConstants;
import com.hazy.cache.def.ItemDefinition;
import com.hazy.cache.factory.ItemSpriteFactory;
import com.hazy.cache.graphics.SimpleImage;
import com.hazy.draw.Rasterizer2D;
import com.hazy.engine.impl.MouseHandler;
import com.hazy.util.CustomItemIdentifiers;
import com.hazy.util.ItemIdentifiers;

import java.util.Arrays;
import java.util.HashMap;

import static com.hazy.util.CustomItemIdentifiers.*;
import static com.hazy.util.CustomItemIdentifiers.WAMPA;
import static com.hazy.util.ItemIdentifiers.*;

public class HoverMenuManager {

    public static final int TEXT_COLOUR = 0xFFFFFFF;

    public static HashMap<Integer, HoverMenu> menus = new HashMap<>();

    public static void init() {
        menus.put(PURPLE_DYE, new HoverMenu("Using this dye on a twisted bow colors it purple"));
        menus.put(ORANGE_DYE, new HoverMenu("Using this dye on a twisted bow colors it orange"));
        menus.put(TOTEMIC_HELMET, new HoverMenu("@red@+5%@whi@ extra melee damage and accuracy boost This effect works only in PvM situations"));
        menus.put(TOTEMIC_PLATEBODY, new HoverMenu("@red@+5%@whi@ extra melee damage and accuracy boost This effect works only in PvM situations"));
        menus.put(TOTEMIC_PLATELEGS, new HoverMenu("@red@+5%@whi@ extra melee damage and accuracy boost This effect works only in PvM situations"));
        menus.put(E_TOTEMIC_HELMET, new HoverMenu("@red@+10%@whi@ extra all style damage and accuracy boost This effect works only in PvM situations"));
        menus.put(E_TOTEMIC_PLATEBODY, new HoverMenu("@red@+15%@whi@ extra all style damage and accuracy boost This effect works only in PvM situations"));
        menus.put(E_TOTEMIC_PLATELEGS, new HoverMenu("@red@+15%@whi@ extra all style damage and accuracy boost This effect works only in PvM situations"));
        menus.put(DARK_SAGE_HAT, new HoverMenu("@red@+5%@whi@ extra magic damage and accuracy boost This effect works only in PvM situations"));
        menus.put(DARK_SAGE_ROBE_TOP, new HoverMenu("@red@+5%@whi@ extra magic damage and accuracy boost This effect works only in PvM situations"));
        menus.put(DARK_SAGE_ROBE_BOTTOM, new HoverMenu("@red@+5%@whi@ extra magic damage and accuracy boost This effect works only in PvM situations"));
        menus.put(SARKIS_DARK_COIF, new HoverMenu("@red@+5%@whi@ extra range damage and accuracy boost This effect works only in PvM situations"));
        menus.put(SARKIS_DARK_BODY, new HoverMenu("@red@+5%@whi@ extra range damage and accuracy boost This effect works only in PvM situations"));
        menus.put(SARKIS_DARK_LEGS, new HoverMenu("@red@+5%@whi@ extra range damage and accuracy boost This effect works only in PvM situations"));
        menus.put(GAMBLER_SCROLL, new HoverMenu("Claiming this item makes you gambler! As a gambler, you can host games and play flower poker at @yel@::gambling"));
        menus.put(BLOOD_MONEY_PET, new HoverMenu("A cosmetic blood money pet, whoever owns this pet is very rich"));
        menus.put(TOXIC_STAFF_OF_THE_DEAD_C, new HoverMenu("A much stronger version of the toxic staff of the dead"));
        menus.put(RING_OF_ELYSIAN, new HoverMenu("When dropping this ring it becomes an elysian pet. When wearing this ring, you turn into either a elysian sigil or shield"));
        menus.put(CORRUPTED_BOOTS, new HoverMenu("The best in slot boots, combined effects of all 3 (or) boots"));
        menus.put(BLOOD_FIREBIRD, new HoverMenu("It has a @red@20%@whi@ chance of passively healing the player @red@15%@whi@ of the damage you deal And gives @red@+500@whi@ blood money when killing players"));
        menus.put(VIGGORAS_CHAINMACE_C, new HoverMenu("An upgraded version of the Viggora's chainmace, effects work outside of wild"));
        menus.put(CRAWS_BOW_C, new HoverMenu("An upgraded version of the Craw's bow, effects work outside of wild"));
        menus.put(THAMMARONS_STAFF_C, new HoverMenu("An upgraded version of the Thammaron's sceptre, effects work outside of wild"));
        menus.put(HOLY_SANGUINESTI_STAFF, new HoverMenu("@red@+10@whi@ default spell max hit"));
        menus.put(HOLY_SCYTHE_OF_VITUR, new HoverMenu("@red@+9@whi@ max hit and slightly more accurate"));
        menus.put(HOLY_GHRAZI_RAPIER, new HoverMenu("A much more powerful version of the Ghrazi rapier"));
        menus.put(SANGUINE_SCYTHE_OF_VITUR, new HoverMenu("A much stronger version of the Scythe of vitur"));
        menus.put(SANGUINE_TWISTED_BOW, new HoverMenu("A much stronger version of the Twisted bow"));
        menus.put(TWISTED_BOW_I, new HoverMenu("@red@+30@whi@ accuracy and @red@+6 max hit@whi@ , Auto keep on death"));
        menus.put(LVL_LAMP, new HoverMenu("Ability to make a skill of choice level 99"));
        menus.put(SWORD_OF_GRYFFINDOR, new HoverMenu("@red@+25%@whi@ damage and accuracy boost against any monster"));
        menus.put(ARMADYL_GODSWORD_OR, new HoverMenu("Increases your max hit by @red@+1@whi@ and @red@+25%@whi@ accuracy"));
        menus.put(BANDOS_GODSWORD_OR, new HoverMenu("Increases your max hit by @red@+1@whi@"));
        menus.put(SARADOMIN_GODSWORD_OR, new HoverMenu("Increases your max hit by @red@+1@whi@"));
        menus.put(ZAMORAK_ARMOUR_SET_SK, new HoverMenu("Increases your max hit by @red@+1@whi@"));
        menus.put(AMULET_OF_FURY_OR, new HoverMenu("Increases your max hit by @red@+1@whi@"));
        menus.put(OCCULT_NECKLACE_OR, new HoverMenu("Increases your max hit by @red@+1@whi@"));
        menus.put(AMULET_OF_TORTURE_OR, new HoverMenu("Increases your max hit by @red@+1@whi@"));
        menus.put(NECKLACE_OF_ANGUISH_OR, new HoverMenu("Increases your max hit by @red@+1@whi@"));
        menus.put(TORMENTED_BRACELET_OR, new HoverMenu("Increases your max hit by @red@+1@whi@"));
        menus.put(GRANITE_MAUL_12848, new HoverMenu("Increases your max hit by @red@+1@whi@"));
        menus.put(DRAGON_CLAWS_OR, new HoverMenu("Increases your max hit by @red@+1@whi@ and @red@+50%@whi@ accuracy"));
        menus.put(RING_OF_MANHUNTING, new HoverMenu("Has both the effects of the @red@Berserker Ring@whi@ and @red@Ring Of suffering@whi@"));
        menus.put(RING_OF_SORCERY, new HoverMenu("Has both the effects of the @red@Seers Ring (i)@whi@ and @red@Ring Of recoil@whi@"));
        menus.put(RING_OF_PRECISION, new HoverMenu("Has both the effects of the Archers Ring (i)@whi@ and @red@Ring Of recoil@whi@@red@+5@whi@ ranged strength"));
        menus.put(RING_OF_TRINITY, new HoverMenu("The effects of all 3 forged rings combined into one + @red@5%@whi@ drop rate bonus "));
        menus.put(SLAYER_HELMET_I, new HoverMenu("Whilst wearing you receive a @red@5%@whi@ extra PKP boost for killing players."));
        menus.put(GREEN_SLAYER_HELMET_I, new HoverMenu("Whilst wearing you receive a @red@10%@whi@ ranged strength boost"));
        menus.put(TURQUOISE_SLAYER_HELMET_I, new HoverMenu("Whilst wearing you receive a @red@10%@whi@ magic strength boost"));
        menus.put(RED_SLAYER_HELMET_I, new HoverMenu("Whilst wearing you receive a @red@10%@whi@ melee strength boost"));
        //raidsupdate
        menus.put(LIL_ZIK, new HoverMenu("Gives @red@10%@whi@ extra damage and accuracy during raids. And 10% boost in personal points."));
        menus.put(LIL_MAIDEN, new HoverMenu("Gives @red@10%@whi@ extra damage and accuracy during raids. And 10% boost in personal points."));
        menus.put(LIL_BLOAT, new HoverMenu("Gives @red@10%@whi@ extra damage and accuracy during raids. And 10% boost in personal points."));
        menus.put(LIL_NYLO, new HoverMenu("Gives @red@10%@whi@ extra damage and accuracy during raids. And 10% boost in personal points."));
        menus.put(LIL_SOT, new HoverMenu("Gives @red@10%@whi@ extra damage and accuracy during raids. And 10% boost in personal points."));
        menus.put(LIL_XARP, new HoverMenu("Gives @red@10%@whi@ extra damage and accuracy during raids. And 10% boost in personal points."));
        //endofraidsupdate
        menus.put(BLACK_SLAYER_HELMET_I, new HoverMenu("Whilst wearing you receive a @red@10%@whi@ extra PKP boost for killing players"));
        menus.put(TWISTED_SLAYER_HELMET_I, new HoverMenu("Combined effect of all imbued Slayer helmets into one"));
        menus.put(TWISTED_SLAYER_HELMET_I_KBD_HEADS, new HoverMenu("Combined effect of all Imbued slayer helmets into one"));
        menus.put(PURPLE_SLAYER_HELMET_I, new HoverMenu("Combined effect of all Imbued slayer helmets into one"));
        menus.put(29312, new HoverMenu("Ability to teleport you out of wilderness from any level and gives @red@15%@whi@ Drop rate boost")); //one sec ill brb dang kids l0l NP
        menus.put(23912, new HoverMenu("Ability to teleport you out of wilderness from any level and gives @red@20%@whi@ chance to heal up to @red@15%@whi@ of damage +@red@500@whi@ BM when killing players"));
        menus.put(HYDRA_SLAYER_HELMET_I, new HoverMenu("Combined effect of all Imbued slayer helmets into one"));
        menus.put(ELDER_MAUL_21205, new HoverMenu("A recoloured version of the Elder maul +1 max hit and better accuracy <col=65535>Untradeable and auto-keep"));
        menus.put(DARK_ELDER_MAUL, new HoverMenu("A much upgraded version of the Elder maul"));
        menus.put(ELDER_ICE_MAUL, new HoverMenu("A slightly upgraded version of the Elder maul"));
        menus.put(MAGMA_BLOWPIPE, new HoverMenu("A recoloured version of the Toxic blowpipe +3 max hit and better accuracy <col=65535>Untradeable and auto-keep"));
        menus.put(VENGEANCE_SKULL, new HoverMenu("With this item in your inventory, you won't need the required runes for Vengeance! <col=65535>Untradeable and auto-keep"));
        menus.put(KEY_OF_BOXES, new HoverMenu("When using the key on a mystery box you get 2 rolls on the table instead of 1"));
        menus.put(GRAND_MYSTERY_BOX, new HoverMenu("Gives random mystery boxes in return"));
        menus.put(MYTHICAL_MAX_CAPE, new HoverMenu("Bis Cape for all 3 styles +@red@5%@whi@ Drop rate bonus "));
        menus.put(ANCESTRAL_HAT_I, new HoverMenu("+@red@10%@whi@ extra accuracy and +@red@5@whi@ magic damage"));
        menus.put(ANCESTRAL_ROBE_TOP_I, new HoverMenu("+@red@10%@whi@ extra accuracy and +@red@5@whi@ magic damage"));
        menus.put(ANCESTRAL_ROBE_BOTTOM_I, new HoverMenu("+@red@10%@whi@ extra accuracy and +@red@5@whi@ magic damage"));
        menus.put(PRIMORDIAL_BOOTS_OR, new HoverMenu("An upgraded version of the Primordial boots"));
        menus.put(PEGASIAN_BOOTS_OR, new HoverMenu("An upgraded version of the Pegasian boots"));
        menus.put(ETERNAL_BOOTS_OR, new HoverMenu("An upgraded version of the Eternal boots"));
        menus.put(ANCIENT_VESTAS_LONGSWORD, new HoverMenu("An upgraded version of the Vesta's longsword"));
        menus.put(ANCIENT_FACEGAURD, new HoverMenu("An upgraded version of the Neitziot faceguard"));
        menus.put(PHARAOHS_HILT, new HoverMenu("An upgraded avernic defender +@red@10@whi@ accuracy and +@red@2@whi@ strength"));
        menus.put(ABYSSAL_TENTACLE_24948, new HoverMenu("An upgraded Abyssal tentacle +@red@10@whi@ accuracy and +@red@1@whi@ max hit"));
        menus.put(MOLTEN_DEFENDER, new HoverMenu("A Molten avernic defender, +@red@10%@whi@ accuracy"));
        menus.put(DERANGED_MANIFESTO, new HoverMenu("A magic book with the ability to teleport you home from @red@30@whi@ wild"));
        menus.put(ANATHEMATIC_STONE, new HoverMenu("Attach this stone to a Kodai wand to create a new wand"));
        menus.put(ANATHEMA_WARD, new HoverMenu("A fused ward useful for both magic and ranged"));
        menus.put(RING_OF_DIVINATION, new HoverMenu("This ring will heal you @red@20%@whi@ of your damage, @red@10%@whi@ of the time"));
        menus.put(RING_OF_WEALTH_I, new HoverMenu("This ring give you a @red@5%@whi@ drop rate bonus"));
        menus.put(CORRUPTING_STONE, new HoverMenu("Attach this stone to a Yougnleff pet, Bow of Faerdhinen or Crystal armour pieces"));
        menus.put(29007, new HoverMenu("The best in slot gloves, combined effects of all 3 gloves"));
        menus.put(30326, new HoverMenu("The best in slot Helm"));
        menus.put(SERPENTINE_HELM, new HoverMenu("Gives an increase in Range accuracy and damage"));
        menus.put(TANZANITE_HELM, new HoverMenu("Gives an increase in Magic accuracy and damage"));
        menus.put(MAGMA_HELM, new HoverMenu("Gives an increase in Melee accuracy and damage"));
        menus.put(30091, new HoverMenu("gives @red@20%@whi@ chance to heal up to @red@15%@whi@ of damage"));
        menus.put(30103, new HoverMenu("@red@+25%@whi@ damage and accuracy boost against any monster & gives @red@20%@whi@ chance to heal up to @red@15%@whi@ of damage"));
        menus.put(23214, new HoverMenu("A much more powerful version of the Ghrazi rapier & gives @red@20%@whi@ chance to heal up to @red@15%@whi@ of damage"));
        menus.put(26922, new HoverMenu("Best in slot amulet & has the healing effect of the blood fury"));
        menus.put(27251, new HoverMenu("More powerful version of the Eldinis ward"));
        menus.put(27253, new HoverMenu("One of the most powerful shields in game for tribriding"));
        menus.put(27255, new HoverMenu("attach to the Eldinis ward F to make the OR version"));
        menus.put(30101, new HoverMenu("Can be used in forge to make Mending weapons"));
        menus.put(29111, new HoverMenu("A Super divine combat potion that allows you to infinitely use it, will restore stats if lowered for full 5 mins. @red@cannot be used in pvp@whi@, will not do any damage when used"));
        menus.put(29112, new HoverMenu("A Super divine ranging potion that allows you to infinitely use it, will restore stats if lowered for full 5 mins. @red@cannot be used in pvp@whi@, will not do any damage when used"));
        menus.put(29113, new HoverMenu("A Saradomin brew that allows you to infinitely use it. @red@cannot be used in pvp"));
        menus.put(29114, new HoverMenu("A Sanfew potion that allows you to infinitely use it. @red@cannot be used in pvp"));
        menus.put(29115, new HoverMenu("A overload potion that allows you to infinitely use it. @red@can only be used in raids @whi@does not damage you when used"));
        menus.put(2955, new HoverMenu("This key can be used to get 2 rolls on the loot table for an Epic pet box or Grand mystery box"));
        menus.put(NAGINI, new HoverMenu("+@red@10%@whi@ magic accuracy, +@red@10@whi@ damage vs npcs and +@red@1@whi@ damage vs players"));
        menus.put(SKELETON_HELLHOUND_PET, new HoverMenu("+@red@7.5%@whi@ drop rate, +@red@5%@whi@ damage and +@red@5%@whi@ accuracy"));
        menus.put(JALNIBREK, new HoverMenu("Acts as a ring of recoil and +@red@10%@whi@ special attack accuracy."));
        menus.put(BLOOD_REAPER, new HoverMenu("Keep one extra item on death.This doesn't work while @red@ smited or red skulled@whi@ And @red@+10%@whi@ extra Blood money during any activity including chests"));
        menus.put(YOUNGLLEF, new HoverMenu("@red@+1@whi@ max hit and @red@7.5%@whi@ accuracy for all attack styles"));
        menus.put(CORRUPTED_YOUNGLLEF, new HoverMenu("@red@+2@whi@ max hit and @red@12.5%@whi@ accuracy for all attack styles"));
        menus.put(JALTOK_JAD, new HoverMenu("Extra @red@25%@whi@ drop rate when fighting Jad"));
        menus.put(LITTLE_NIGHTMARE, new HoverMenu("@red@+1@whi@ max hit and @red@5%@whi@ accuracy for ranged and magic."));
        menus.put(ARTIO_PET, new HoverMenu("@red@20%@whi@ Bonus accuracy for special attacks"));
        menus.put(KERBEROS_PET, new HoverMenu("@red@10%@whi@ accuracy to ranged, mage and melee"));
        menus.put(SKORPIOS_PET, new HoverMenu("Your Venom goes through resistances"));
        menus.put(ARACHNE_PET, new HoverMenu("Binds and freezes are 2 seconds longer"));
        menus.put(CENTAUR_FEMALE, new HoverMenu("+@red@10%@whi@ more raids points."));
        menus.put(CENTAUR_MALE, new HoverMenu("+@red@10%@whi@ more raids points"));
        menus.put(FLUFFY_JR, new HoverMenu("+@red@10@whi@ overload boost in raids"));
        menus.put(FENRIR_GREYBACK_JR, new HoverMenu("+@red@15%@whi@ chance of dealing a second melee hit"));
        menus.put(DEMENTOR_PET, new HoverMenu("+@red@35%@whi@ extra smite prayer damage"));
        menus.put(ANCIENT_BARRELCHEST_PET, new HoverMenu("+@red@20%@whi@ more melee accuracy"));
        menus.put(ANCIENT_CHAOS_ELEMENTAL_PET, new HoverMenu("+@red@5%@whi@ extra special attack restore"));
        menus.put(ANCIENT_KING_BLACK_DRAGON_PET, new HoverMenu("@red@25%@whi@ damage and accuracy boosts vs dragons"));
        menus.put(BLOODHOUND, new HoverMenu("Gives at least one roll on the rare table when opening treasure caskets"));
        menus.put(ZRIAWK, new HoverMenu("@red@15%@whi@ extra drop rate"));
        menus.put(JAWA_PET, new HoverMenu("When having the Jawa pet out you have @red@50%@whi@ more chance on receiving a pet drop"));
        menus.put(FAWKES, new HoverMenu("Ability to teleport you out of wilderness from any level and any place"));
        menus.put(FAWKES_24937, new HoverMenu("Ability to teleport you out of wilderness from any level and any place"));
        menus.put(BARRELCHEST_PET, new HoverMenu("@red@+1@whi@ max hit And @red@+10%@whi@ attack bonus accuracy"));
        menus.put(WAMPA, new HoverMenu("Has a @red@10%@whi@ chance to freeze your opponent"));
        menus.put(NIFFLER, new HoverMenu("Niffler stores items that you've received from a player kill or pvm drop into his pouch"));
        menus.put(BABY_ARAGOG, new HoverMenu("Gives @red@5%@whi@ vs players and @red@10%@whi@ vs npcs more Ranged damage and @red@15%@whi@ more Ranged accuracy"));
        menus.put(BABY_LAVA_DRAGON, new HoverMenu("Gives @red@+5%@whi@ Magic damage vs players and @red@+10%@whi@ vs npcs and @red@+10%@whi@ Magic accuracy"));
        menus.put(MINI_NECROMANCER, new HoverMenu("Increases the max hit of any spell by @red@+3@whi@"));
        menus.put(PET_CORRUPTED_NECHRYARCH, new HoverMenu("@red@+1@whi@ Max hit And @red@+15%@whi@ melee attack bonus accuracy"));
        menus.put(FOUNDER_IMP, new HoverMenu("Having this pet out there is a @red@+10%@whi@ chance that your drop is being doubled Does not stack with other double drop features"));
        menus.put(TZREKZUK, new HoverMenu("Gives @red@+5%@whi@ damage reduction"));
        menus.put(GENIE_PET, new HoverMenu("Whilst having this pet out your experience is always boosted by @red@X2@whi@ Does not stack with other exp boosts"));
        menus.put(DHAROK_PET, new HoverMenu("Increases your Dharok's axe accuracy by @red@+10%@whi@"));
        menus.put(PET_ZOMBIES_CHAMPION, new HoverMenu("Deals @red@+10%@whi@ extra damage and accuracy to the boss version"));
        menus.put(BABY_ABYSSAL_DEMON, new HoverMenu("Changes the dragon dagger special attack requirement to @red@-20%@whi@ instead of @red@-25%@whi@"));
        menus.put(BABY_DARK_BEAST_EGG, new HoverMenu("Makes the dark bow @red@+10%@whi@ more accurate"));
        menus.put(BABY_SQUIRT, new HoverMenu("Gives @red@10%@whi@ more accuracy and acts as a ring of vigour"));
        menus.put(GRIM_REAPER_PET, new HoverMenu("Keep one extra item on death This doesn't work while @red@ smited or red skulled@whi@"));
        menus.put(OLMLET, new HoverMenu("Gives @red@10%@whi@ extra damage and accuracy during raids. And 10% boost in personal points"));
        menus.put(SNOWBIRD, new HoverMenu("Gives @red@+5%@whi@ Magic accuracy and @red@+2@whi@ Ice barrage max hit"));
        menus.put(ELVARG_JR, new HoverMenu("Gives @red@+5%@whi@ Magic accuracy and @red@+2@whi@ Fire surge max hit"));
        menus.put(LAVA_BABY, new HoverMenu("@red@+1@whi@ max hit for all attack styles"));
        menus.put(DERANGED_ARCHAEOLOGIST, new HoverMenu("@red@No@whi@ skull effect when opening chests in the wild and @red@+10%@whi@ Magic accuracy"));
        menus.put(SERENIC, new HoverMenu("@red@+15%@whi@ ranged accuracy and @red@25%@whi@ chance of dealing a second ranged hit"));
        menus.put(29116, new HoverMenu("Ziffler stores items that you've received from a player kill or pvm drop into his pouch + Gives a @red@15%@whi@ extra drop rate"));
        menus.put(CustomItemIdentifiers.OG_CRIMSON_PET, new HoverMenu(""));

        menus.put(MYSTERY_TICKET, new HoverMenu("Has a chance to give some of the most valuable items in the game!",
            Arrays.asList(
                MYSTERY_CHEST,
                EPIC_PET_BOX,
                LEGENDARY_MYSTERY_BOX,
                ARMADYL_GODSWORD_OR,
                DRAGON_CLAWS_OR,
                CRAWS_BOW_C,
                VIGGORAS_CHAINMACE_C,
                THAMMARONS_STAFF_C,
                TOXIC_STAFF_OF_THE_DEAD_C,
                RING_OF_PRECISION,
                RING_OF_SORCERY,
                RING_OF_MANHUNTING,
                ANCIENT_FACEGAURD,
                AMULET_OF_TORTURE_OR,
                NECKLACE_OF_ANGUISH_OR,
                OCCULT_NECKLACE_OR,
                PEGASIAN_BOOTS_OR,
                PRIMORDIAL_BOOTS_OR,
                ETERNAL_BOOTS_OR
            )));
        menus.put(BONDS_CASKET, new HoverMenu("Has a chance to give some of the most valuable bonds in game!",
            Arrays.asList(
                FIVE_DOLLAR_BOND,
                TWENTY_DOLLAR_BOND,
                FORTY_DOLLAR_BOND,
                FIFTY_DOLLAR_BOND,
                ONE_HUNDRED_DOLLAR_BOND
            )));
        menus.put(REVENANT_MYSTER_BOX, new HoverMenu("Has a chance to give some of the most valuable items in the game!",
            Arrays.asList(
                ItemIdentifiers.VIGGORAS_CHAINMACE,
                ItemIdentifiers.CRAWS_BOW,
                ItemIdentifiers.THAMMARONS_SCEPTRE,
                ItemIdentifiers.AMULET_OF_AVARICE,
                ItemIdentifiers.STATIUSS_FULL_HELM,
                ItemIdentifiers.STATIUSS_PLATEBODY,
                ItemIdentifiers.STATIUSS_PLATELEGS,
                ItemIdentifiers.STATIUSS_WARHAMMER,
                ItemIdentifiers.VESTAS_CHAINBODY,
                ItemIdentifiers.VESTAS_PLATESKIRT,
                ItemIdentifiers.VESTAS_LONGSWORD,
                ItemIdentifiers.MORRIGANS_COIF,
                ItemIdentifiers.MORRIGANS_LEATHER_BODY,
                ItemIdentifiers.MORRIGANS_LEATHER_CHAPS,
                ItemIdentifiers.ZURIELS_HOOD,
                ItemIdentifiers.ZURIELS_ROBE_TOP,
                ItemIdentifiers.ZURIELS_ROBE_BOTTOM,
                ItemIdentifiers.ZURIELS_STAFF
            )));

        menus.put(16459, new HoverMenu("Has a chance to give some of the most valuable items in the game!",
            Arrays.asList(
                ItemIdentifiers.TWISTED_BOW,
                ItemIdentifiers.SCYTHE_OF_VITUR,
                CustomItemIdentifiers.HOLY_GHRAZI_RAPIER,
                30251,
                30253,
                SWORD_OF_GRYFFINDOR,
                29103,
                28005,
                SANGUINESTI_STAFF,
                ItemIdentifiers.GHRAZI_RAPIER,
                ItemIdentifiers.ANCESTRAL_ROBE_TOP,
                ItemIdentifiers.ANCESTRAL_ROBE_BOTTOM,
                ELDER_MAUL,
                ItemIdentifiers.DRAGON_CLAWS,
                ItemIdentifiers.KODAI_WAND,
                ItemIdentifiers.ANCESTRAL_HAT

            )));

        menus.put(16460, new HoverMenu("Has a chance to give some of the most valuable items in the game!",
            Arrays.asList(
                ItemIdentifiers.LIGHT_BALLISTA,
                ItemIdentifiers.HEAVY_BALLISTA,
                ItemIdentifiers.AMULET_OF_TORTURE,
                ItemIdentifiers.NECKLACE_OF_ANGUISH,
                ItemIdentifiers.RING_OF_SUFFERING,
                ItemIdentifiers.TORMENTED_BRACELET,
                ItemIdentifiers.AMULET_OF_TORTURE_OR,
                ItemIdentifiers.NECKLACE_OF_ANGUISH_OR,
                ItemIdentifiers.RING_OF_SUFFERING_I,
                ItemIdentifiers.TORMENTED_BRACELET_OR
            )));

        menus.put(PET_MYSTERY_BOX, new HoverMenu("Has a chance to give some valuable, and cosmetic pets!",
            Arrays.asList(
                ItemIdentifiers.BLOODHOUND,
                ItemIdentifiers.OLMLET,
                ItemIdentifiers.TZREKZUK,
                CustomItemIdentifiers.BABY_ABYSSAL_DEMON,
                CustomItemIdentifiers.BABY_DARK_BEAST_EGG,
                CustomItemIdentifiers.BABY_SQUIRT,
                ItemIdentifiers.VORKI,
                ItemIdentifiers.PET_SNAKELING,
                ItemIdentifiers.NOON,
                ItemIdentifiers.MIDNIGHT
            )));


        menus.put(16458, new HoverMenu("Has a chance to give some of the most valuable pets in the game!",
            Arrays.asList(
                CustomItemIdentifiers.JAWA_PET,
                CustomItemIdentifiers.DEMENTOR_PET,
                CustomItemIdentifiers.ANCIENT_BARRELCHEST_PET,
                CustomItemIdentifiers.SNOWBIRD,
                CustomItemIdentifiers.NIFFLER,
                CustomItemIdentifiers.BABY_ARAGOG,
                CustomItemIdentifiers.FOUNDER_IMP,
                CustomItemIdentifiers.BABY_LAVA_DRAGON,
                CustomItemIdentifiers.MINI_NECROMANCER,
                CustomItemIdentifiers.PET_CORRUPTED_NECHRYARCH,
                CustomItemIdentifiers.GRIM_REAPER_PET,
                CustomItemIdentifiers.GENIE_PET,
                CustomItemIdentifiers.DHAROK_PET,
                ELVARG_JR,
                CustomItemIdentifiers.BABY_SQUIRT
            )));

        menus.put(DONATOR_MYSTERY_BOX, new HoverMenu("Has a chance to give some of the most valuable items in the game!",
            Arrays.asList(
                _3RD_AGE_DRUIDIC_CLOAK,
                _3RD_AGE_DRUIDIC_ROBE_TOP,
                _3RD_AGE_DRUIDIC_ROBE_BOTTOMS,
                _3RD_AGE_DRUIDIC_STAFF,
                BLACK_PARTYHAT,
                BLACK_HWEEN_MASK,
                BLACK_SANTA_HAT,
                INVERTED_SANTA_HAT,
                PARTYHAT__SPECS,
                ANTISANTA_MASK,
                ANTISANTA_JACKET,
                ANTISANTA_PANTALOONS,
                ANTISANTA_GLOVES,
                ANTISANTA_BOOTS
            )));

        menus.put(GRAND_MYSTERY_BOX, new HoverMenu("Has a chance to give multiple expensive boxes!",
            Arrays.asList(
                DONATOR_MYSTERY_BOX,
                LEGENDARY_MYSTERY_BOX,
                MYSTERY_TICKET,
                EPIC_PET_BOX,
                MYSTERY_CHEST
            )));

        menus.put(HWEEN_MYSTERY_BOX, new HoverMenu("Has a chance to give some of the most valuable items in the game!",
            Arrays.asList(
                STATIUSS_PLATEBODY,
                STATIUSS_PLATELEGS,
                MORRIGANS_LEATHER_BODY,
                MORRIGANS_LEATHER_CHAPS,
                _3RD_AGE_LONGSWORD,
                _3RD_AGE_BOW,
                _3RD_AGE_WAND,
                MORRIGANS_COIF,
                ZURIELS_ROBE_TOP,
                ZURIELS_ROBE_BOTTOM,
                VESTAS_SPEAR,
                STATIUSS_FULL_HELM,
                STATIUSS_WARHAMMER,
                TOXIC_BLOWPIPE,
                NEITIZNOT_FACEGUARD,
                TOXIC_STAFF_OF_THE_DEAD
            )));

        menus.put(WEAPON_MYSTERY_BOX, new HoverMenu("Has a chance to give some of the most valuable items in the game!",
            Arrays.asList(
                INQUISITORS_MACE,
                VESTAS_LONGSWORD,
                STATIUSS_WARHAMMER,
                TOXIC_STAFF_OF_THE_DEAD,
                TOXIC_BLOWPIPE,
                ARMADYL_GODSWORD,
                DRAGON_CLAWS
            )));

        menus.put(ARMOUR_MYSTERY_BOX, new HoverMenu("Has a chance to give some of the most valuable items in the game!",
            Arrays.asList(
                //JUSTICIAR_FACEGUARD,
                //JUSTICIAR_CHESTGUARD,
                //JUSTICIAR_LEGGUARDS,
                INQUISITORS_GREAT_HELM,
                INQUISITORS_HAUBERK,
                INQUISITORS_PLATESKIRT,
                DARK_INFINITY_HAT,
                DARK_INFINITY_TOP,
                DARK_INFINITY_BOTTOMS,
                LIGHT_INFINITY_HAT,
                LIGHT_INFINITY_TOP,
                LIGHT_INFINITY_BOTTOMS
            )));

        menus.put(LEGENDARY_MYSTERY_BOX, new HoverMenu("Has a chance to give some of the most valuable items in the game!",
            Arrays.asList(
                ARCANE_SPIRIT_SHIELD,
                AVERNIC_DEFENDER,
                INFERNAL_CAPE,
                FEROCIOUS_GLOVES,
                HARMONISED_NIGHTMARE_STAFF,
                VOLATILE_NIGHTMARE_STAFF,
                SANGUINESTI_STAFF,
                DEXTEROUS_PRAYER_SCROLL,
                ARCANE_PRAYER_SCROLL,
                DONATOR_TICKET,
                MYSTERY_TICKET,
                LARRANS_KEY_TIER_III,
                ELDER_MAUL
            )));

        menus.put(MYSTERY_BOX, new HoverMenu("Has a chance to give some of the most valuable items in the game!",
            Arrays.asList(

                //Ext rare
                CustomItemIdentifiers.GRIM_REAPER_PET,
                CustomItemIdentifiers.BARRELCHEST_PET,
                ARCANE_SPIRIT_SHIELD,
                ELDER_MAUL,

                //Rare
                DRAGON_CLAWS,
                ARMADYL_GODSWORD,
                GHRAZI_RAPIER,
                SPECTRAL_SPIRIT_SHIELD,
                HEAVY_BALLISTA,
                TOXIC_STAFF_OF_THE_DEAD,
                SERPENTINE_HELM
            )));

        menus.put(MYSTERY_CHEST, new HoverMenu("Has a chance to give some of the most rare items in the game!",
            Arrays.asList(TWISTED_BOW_I, HOLY_SCYTHE_OF_VITUR, HOLY_SANGUINESTI_STAFF, ELYSIAN_SPIRIT_SHIELD, SHADOW_GREAT_HELM, SHADOW_HAUBERK, SHADOW_PLATESKIRT, ANCIENT_WARRIOR_AXE_C, ANCIENT_WARRIOR_MAUL_C, ANCIENT_WARRIOR_SWORD_C, SALAZAR_SLYTHERINS_LOCKET, SWORD_OF_GRYFFINDOR, TALONHAWK_CROSSBOW, ANCESTRAL_HAT_I, ANCESTRAL_ROBE_BOTTOM_I, ANCESTRAL_ROBE_TOP_I, CORRUPTED_BOOTS, RING_OF_TRINITY,
                CRAWS_BOW_C, VIGGORAS_CHAINMACE_C, BLADE_OF_SAELDOR_8, DARK_ARMADYL_HELMET, DARK_ARMADYL_CHESTPLATE, DARK_ARMADYL_CHAINSKIRT, DARK_BANDOS_CHESTPLATE, DARK_BANDOS_TASSETS, SHADOW_INQUISITOR_ORNAMENT_KIT)));

        menus.put(HWEEN_MYSTERY_CHEST, new HoverMenu("Has a chance to give some of the most rare items in the game!",
            Arrays.asList(SANGUINE_SCYTHE_OF_VITUR, SANGUINE_TWISTED_BOW, ELYSIAN_SPIRIT_SHIELD, TWISTED_BOW, SCYTHE_OF_VITUR, ANCIENT_WARRIOR_AXE_C, ANCIENT_WARRIOR_MAUL_C, ANCIENT_WARRIOR_SWORD_C, SALAZAR_SLYTHERINS_LOCKET, SWORD_OF_GRYFFINDOR, TALONHAWK_CROSSBOW, ANCESTRAL_HAT_I, ANCESTRAL_ROBE_BOTTOM_I, ANCESTRAL_ROBE_TOP_I, LAVA_DHIDE_BODY, LAVA_DHIDE_CHAPS, LAVA_DHIDE_COIF, CORRUPTED_BOOTS, RING_OF_TRINITY,
                CRAWS_BOW_C, VIGGORAS_CHAINMACE_C, BOW_OF_FAERDHINEN_3, DARK_ARMADYL_HELMET, DARK_ARMADYL_CHESTPLATE, DARK_ARMADYL_CHAINSKIRT)));

        menus.put(PRESENT_13346, new HoverMenu("Has a chance to give some of the most rare items in the game!",
            Arrays.asList(
                SNOWBIRD,
                ELDER_ICE_MAUL,
                DRAGON_HUNTER_CROSSBOW_T,
                FROST_IMBUED_CAPE,
                INFINITY_WINTER_BOOTS,
                MYSTERY_TICKET,
                LEGENDARY_MYSTERY_BOX,
                SNOWY_SLED,
                UGLY_SANTA_HAT,
                ICED_SANTA_HAT,
                WISE_OLD_MANS_SANTA_HAT,
                FROST_CLAWS,
                ARMADYL_FROSTSWORD,
                ELDER_MAUL
            )));

        menus.put(MOLTEN_MYSTERY_BOX, new HoverMenu("Has a chance to give some of the most rare items in the game!",
            Arrays.asList(
                MYSTERY_TICKET,
                LEGENDARY_MYSTERY_BOX,
                MOLTEN_DEFENDER,
                LAVA_DHIDE_COIF,
                LAVA_DHIDE_BODY,
                LAVA_DHIDE_CHAPS,
                INFERNAL_CAPE,
                MOLTEN_PARTYHAT,
                LAVA_PARTYHAT,
                LAVA_WHIP,
                LAVA_DRAGON_MASK
            )));

        menus.put(WINTER_ITEM_CASKET, new HoverMenu("Has a chance to give each of the new winter items in the game!",
            Arrays.asList(
                ICED_PARTYHAT,
                ICED_HWEEN_MASK,
                ICED_SANTA_HAT,
                UGLY_SANTA_HAT,
                SNOWY_SLED,
                FROST_WHIP,
                FROST_CLAWS,
                ARMADYL_FROSTSWORD,
                INFINITY_WINTER_BOOTS,
                FROST_IMBUED_CAPE,
                DRAGON_HUNTER_CROSSBOW_T,
                ELDER_ICE_MAUL
            )));

        System.out.println(ClientConstants.CLIENT_NAME + " has loaded " + menus.size() + "x menu hovers.");
    }

    public static int drawType() {
        if (MouseHandler.mouseX > 0 && MouseHandler.mouseX < 500 && MouseHandler.mouseY > 0
            && MouseHandler.mouseY < 300) {
            return 1;
        }
        return 0;
    }

    public static boolean shouldDraw(int id) {
        return menus.get(id) != null;
    }

    public static boolean showMenu;

    public static String hintName;

    public static int hintId;

    public static int displayIndex;

    public static long displayDelay;

    public static int[] itemDisplay = new int[4];

    private static int lastDraw;

    public static void reset() {
        showMenu = false;
        hintId = -1;
        hintName = "";
    }

    public static boolean canDraw() {
		/*if (Client.singleton.menuActionRow < 2 && Client.singleton.itemSelected == 0
				&& Client.singleton.spellSelected == 0) {
			return false;
		}*/
        if (Client.instance.menuActionText[Client.instance.menuActionRow] != null) {
            if (Client.instance.menuActionText[Client.instance.menuActionRow].contains("Walk")) {
                return false;
            }
        }
        if (Client.instance.tooltip != null && (Client.instance.tooltip.contains("Walk") || Client.instance.tooltip.contains("World"))) {
            return false;
        }
        if (Client.instance.menuOpen) {
            return false;
        }
        if (hintId == -1) {
            return false;
        }
        return showMenu;
    }

    public static void drawHintMenu() {
        int cursor_x = MouseHandler.mouseX;
        int cursor_y = MouseHandler.mouseY;
        if (!canDraw()) {
            return;
        }

        if (Client.instance.isResized()) {
            if (MouseHandler.mouseY < Client.canvasHeight - 450
                && MouseHandler.mouseX < Client.canvasWidth - 200) {
                return;
            }
            cursor_x -= 100;
            cursor_y -= 50;
        } else {
            if (MouseHandler.mouseY < 210 || MouseHandler.mouseX < 561) {
            } else {
                cursor_x -= 516;
                cursor_y -= 158;
            }
            if (MouseHandler.mouseX > 600 && MouseHandler.mouseX < 685) {
                cursor_x -= 60;

            }
            if (MouseHandler.mouseX > 685) {
                cursor_x -= 80;
            }
        }

        if (lastDraw != hintId) {
            lastDraw = hintId;
            itemDisplay = new int[4];
        }

        HoverMenu menu = menus.get(hintId);
        if (menu != null) {
            String[] text = split(menu.text).split("<br>");

            int height = (text.length * 12) + (menu.items != null ? 40 : 0);

            cursor_x += 15;
            int width = (16 + text[0].length() * 5) + (menu.items != null ? 30 : 0);

            int drawX = 295 / 2 + 512 / 2;
            int drawY = 230 / 2 + 334 / 2;
            if (Client.isCtrlPressed) {
                if (!Client.instance.isResized()) {
                    cursor_x = drawX - width / 2 - width / 2;
                    cursor_y = drawY - height / 2 - height / 2;
                    cursor_x += 80;
                    cursor_y += 23;
                } else {
                    cursor_x = drawX - width / 2 - width / 2;
                    cursor_y = drawY - height / 2 - height / 2;
                    cursor_x += 83;
                    cursor_y += 425;
                }
            } else {
                if (!Client.instance.isResized()) {
                    if (drawType() == 1) {
                        if (width + cursor_x > 500) {
                            cursor_x = 500 - width;
                        }
                    } else {
                        if (width + cursor_x > 250) {
                            cursor_x = 245 - width;
                        }

                        if (height + cursor_y > 250) {
                            cursor_y = 250 - height;
                        }
                    }
                }
            }

            Rasterizer2D.draw_rect_outline(cursor_x, cursor_y + 5, width + text[0].length(), 26 + height, 0x383023);
            Rasterizer2D.drawTransparentBox(cursor_x, cursor_y + 6, width + text[0].length(), 24 + height, 0x534a40, 200);

            Client.adv_font_small.draw("<col=ff9040>" + hintName, cursor_x + 4, cursor_y + 19, TEXT_COLOUR, 1);
            int y = 0;

            for (String string : text) {
                Client.adv_font_small.draw(string, cursor_x + 4, cursor_y + 35 + y, TEXT_COLOUR, 1);
                y += 12;
            }

            if (menu.items != null) {
                int spriteX = 10;

                if (System.currentTimeMillis() - displayDelay > 300) {
                    displayDelay = System.currentTimeMillis();
                    displayIndex++;
                    if (displayIndex == menu.items.size()) {
                        displayIndex = 0;
                    }

                    if (menu.items.size() <= 4) {
                        for (int i = 0; i < menu.items.size(); i++) {
                            itemDisplay[i] = menu.items.get(i);
                        }
                    } else {
                        if (displayIndex >= menu.items.size() - 1) {
                            displayIndex = menu.items.size() - 1;
                        }
                        int next = menu.items.get(displayIndex);
                        if (itemDisplay.length - 1 >= 0)
                            System.arraycopy(itemDisplay, 1, itemDisplay, 0, itemDisplay.length - 1);
                        itemDisplay[3] = next;
                    }
                }

                for (int id : itemDisplay) {
                    if (id < 1) continue;
                    SimpleImage item = ItemSpriteFactory.get_item_sprite(id, 1, 0);
                    if (item != null) {
                        item.drawSprite(cursor_x + spriteX, cursor_y + 35 + y);
                        spriteX += 40;
                    }
                }
            }
        }
    }

    private static String split(String text) {
        StringBuilder string = new StringBuilder();

        int size = 0;

        for (String s : text.split(" ")) {
            string.append(s).append(" ");
            size += s.length();
            if (size > 20) {
                string.append("<br>");
                size = 0;
            }
        }
        return string.toString();
    }

}
