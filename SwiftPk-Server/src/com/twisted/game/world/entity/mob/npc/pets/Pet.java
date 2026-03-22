package com.twisted.game.world.entity.mob.npc.pets;

import com.twisted.fs.NpcDefinition;
import com.twisted.game.world.World;
import com.twisted.util.*;

import java.util.*;

import static com.twisted.game.world.entity.mob.npc.pets.PetVarbits.*;
import static com.twisted.util.NpcIdentifiers.LITTLE_NIGHTMARE_9399;

/**
 * Created by Bart on 2/18/2016.
 *
 * UPDATE 14/10/2016 : npc_option ID no longer used in Npc Updating. See issue #557
 */
public enum Pet {

    //Custom
    SERENIC(CustomItemIdentifiers.SERENIC, CustomNpcIdentifiers.SERENIC, -1),
    DERANGED_ARCHAEOLOGIST(CustomItemIdentifiers.DERANGED_ARCHAEOLOGIST, CustomNpcIdentifiers.DERANGED_ARCHAEOLOGIST, -1),
    LAVA_BABY(CustomItemIdentifiers.LAVA_BABY, CustomNpcIdentifiers.LAVA_BABY, -1),
    SNOWBIRD(CustomItemIdentifiers.SNOWBIRD, CustomNpcIdentifiers.SNOWBIRD, -1),
    ELVARG_JR(CustomItemIdentifiers.ELVARG_JR, CustomNpcIdentifiers.ELVARG_JR, -1),
    NAGINI(CustomItemIdentifiers.NAGINI, CustomNpcIdentifiers.NAGINI, -1),
    SKELETON_HELLHOUND_PET(CustomItemIdentifiers.SKELETON_HELLHOUND_PET, CustomNpcIdentifiers.SKELETON_HELLHOUND_PET,-1),
    BLOOD_REAPER(CustomItemIdentifiers.BLOOD_REAPER, CustomNpcIdentifiers.BLOOD_REAPER,-1),
    ZIFFLER(CustomItemIdentifiers.ZIFFLER, CustomNpcIdentifiers.ZIFFLER,-1),//up02

    YOUNGLLEF(ItemIdentifiers.YOUNGLLEF, NpcIdentifiers.YOUNGLLEF,-1),
    CORRUPTED_YOUNGLLEF(ItemIdentifiers.CORRUPTED_YOUNGLLEF, NpcIdentifiers.CORRUPTED_YOUNGLLEF,-1),
    FAWKES_RECOLOR(CustomItemIdentifiers.FAWKES_24937, CustomNpcIdentifiers.FAWKES_10981,-1),
    THE_NIGHTMARE(ItemIdentifiers.LITTLE_NIGHTMARE, LITTLE_NIGHTMARE_9399,-1),
    ELYSIAN_PET(CustomItemIdentifiers.RING_OF_ELYSIAN, CustomNpcIdentifiers.ELYSIAN_PET,-1),
    NEX(CustomItemIdentifiers.NEXLING, CustomNpcIdentifiers.NEXLING,-1),

    BLOOD_MONEY(CustomItemIdentifiers.BLOOD_MONEY_PET, CustomNpcIdentifiers.BLOOD_MONEY_PET,-1),
    KERBEROS(CustomItemIdentifiers.KERBEROS_PET, CustomNpcIdentifiers.KERBEROS_PET,-1),
    SKORPIOS(CustomItemIdentifiers.SKORPIOS_PET, CustomNpcIdentifiers.SKORPIOS_PET,-1),
    ARACHNE(CustomItemIdentifiers.ARACHNE_PET, CustomNpcIdentifiers.ARACHNE_PET,-1),
    ARTIO(CustomItemIdentifiers.ARTIO_PET, CustomNpcIdentifiers.ARTIO_PET,-1),
    ANCIENT_KING_BLACK_DRAGON(CustomItemIdentifiers.ANCIENT_KING_BLACK_DRAGON_PET, CustomNpcIdentifiers.ANCIENT_KING_BLACK_DRAGON_PET,-1),
    ANCIENT_CHAOS_ELEMENTAL(CustomItemIdentifiers.ANCIENT_CHAOS_ELEMENTAL_PET, CustomNpcIdentifiers.ANCIENT_CHAOS_ELEMENTAL_PET,-1),
    ANCIENT_BARRELCHEST(CustomItemIdentifiers.ANCIENT_BARRELCHEST_PET, CustomNpcIdentifiers.ANCIENT_BARRELCHEST_PET,-1),
    BLOOD_FIREBIRD(CustomItemIdentifiers.BLOOD_FIREBIRD, CustomNpcIdentifiers.BLOOD_FIREBIRD,-1),
    LIL_ZIK(ItemIdentifiers.LIL_ZIK, NpcIdentifiers.LIL_ZIK_8337, UNLOCKED_PET_LIL_ZIK),
    LIL_MAIDEN(ItemIdentifiers.LIL_MAIDEN, NpcIdentifiers.LIL_MAIDEN_10870, UNLOCKED_PET_LIL_ZIK),
    LIL_BLOAT(ItemIdentifiers.LIL_BLOAT, NpcIdentifiers.LIL_BLOAT_10871, UNLOCKED_PET_LIL_ZIK),
    LIL_NYLO(ItemIdentifiers.LIL_NYLO, NpcIdentifiers.LIL_NYLO_10872, UNLOCKED_PET_LIL_ZIK),
    LIL_SOT(ItemIdentifiers.LIL_SOT, NpcIdentifiers.LIL_SOT_10873, UNLOCKED_PET_LIL_ZIK),
    LIL_XARP(ItemIdentifiers.LIL_XARP, NpcIdentifiers.LIL_XARP_10874, UNLOCKED_PET_LIL_ZIK),
    ZRIAWK(CustomItemIdentifiers.ZRIAWK, CustomNpcIdentifiers.ZRIAWK,-1),
    MENDING_LIFE_BIRD(CustomItemIdentifiers.MENDING_LIFE_BIRD, CustomNpcIdentifiers.MENDING_LIFE_BIRD,-1),
    ZAWEKS_RECOLOR(CustomItemIdentifiers.ZAWEKS_PET, CustomNpcIdentifiers.ZAWEKS_PET,-1),//updateo
    FENRIR_GREYBACK_JR(CustomItemIdentifiers.FENRIR_GREYBACK_JR, CustomNpcIdentifiers.FENRIR_GREYBACK_JR,-1),
    FLUFFY_JR(CustomItemIdentifiers.FLUFFY_JR, CustomNpcIdentifiers.FLUFFY_JR,-1),
    DEMENTOR(CustomItemIdentifiers.DEMENTOR_PET, CustomNpcIdentifiers.DEMENTOR_PET,-1),
    CENTAUR_MALE(CustomItemIdentifiers.CENTAUR_MALE, CustomNpcIdentifiers.CENTAUR_MALE_PET,-1),
    CENTAUR_FEMALE(CustomItemIdentifiers.CENTAUR_FEMALE, CustomNpcIdentifiers.CENTAUR_FEMALE_PET,-1),
    TZKAL_ZUK(ItemIdentifiers.TZREKZUK, NpcIdentifiers.TZREKZUK_8011, NpcIdentifiers.JALNIBREK_7675,-1),
    JAL_NIB_REK(ItemIdentifiers.JALNIBREK, NpcIdentifiers.JALNIBREK_7675, NpcIdentifiers.TZREKZUK_8011,-1),
    FOUNDER_IMP(CustomItemIdentifiers.FOUNDER_IMP, CustomNpcIdentifiers.FOUNDER_IMP, -1),
    PET_CORRUPTED_NECHRYARCH(CustomItemIdentifiers.PET_CORRUPTED_NECHRYARCH, CustomNpcIdentifiers.PET_CORRUPTED_NECHRYARCH, -1),
    MINI_NECROMANCER(CustomItemIdentifiers.MINI_NECROMANCER, CustomNpcIdentifiers.MINI_NECROMANCER, -1),
    JALTOK_JAD(CustomItemIdentifiers.JALTOK_JAD, CustomNpcIdentifiers.JALTOK_JAD, -1),
    BABY_LAVA_DRAGON(CustomItemIdentifiers.BABY_LAVA_DRAGON, CustomNpcIdentifiers.BABY_LAVA_DRAGON, -1),
    JAWA(CustomItemIdentifiers.JAWA_PET, CustomNpcIdentifiers.JAWA, -1),
    BABY_ARAGOG(CustomItemIdentifiers.BABY_ARAGOG, CustomNpcIdentifiers.BABY_ARAGOG, -1),
    WAMPA(CustomItemIdentifiers.WAMPA, CustomNpcIdentifiers.WAMPA, -1),
    FAWKES(CustomItemIdentifiers.FAWKES, CustomNpcIdentifiers.FAWKES,-1),
    BABY_SQUIRT(CustomItemIdentifiers.BABY_SQUIRT, CustomNpcIdentifiers.BABY_SQUIRT, UNLOCKED_PET_BABY_SQUIRT),
    GRIM_REAPER(CustomItemIdentifiers.GRIM_REAPER_PET, CustomNpcIdentifiers.GRIM_REAPER, -1),
    BABY_DARK_BEAST(CustomItemIdentifiers.BABY_DARK_BEAST_EGG, CustomNpcIdentifiers.BABY_DARK_BEAST,-1 , UNLOCKED_PET_BABY_DARK_BEAST),
    BABY_ABYSSAL_DEMON(CustomItemIdentifiers.BABY_ABYSSAL_DEMON,CustomNpcIdentifiers.BABY_ABYSSAL_DEMON, UNLOCKED_PET_BABY_ABYSSAL_DEMON),
    ZOMBIES_CHAMPION(CustomItemIdentifiers.PET_ZOMBIES_CHAMPION, CustomNpcIdentifiers.ZOMBIES_CHAMPION, -1),
    BARRLECHEST_PET(CustomItemIdentifiers.BARRELCHEST_PET, CustomNpcIdentifiers.BARRELCHEST_PET, -1),
    NIFFLER(CustomItemIdentifiers.NIFFLER, CustomNpcIdentifiers.NIFFLER, -1),
    DHAROK_PET(CustomItemIdentifiers.DHAROK_PET, CustomNpcIdentifiers.DHAROK_PET, -1),
    GENIE_PET(CustomItemIdentifiers.GENIE_PET, CustomNpcIdentifiers.GENIE_PET, -1),

    //Recolors
    PET_GENERAL_GRAARDOR_BLACK(CustomItemIdentifiers.PET_GENERAL_GRAARDOR_BLACK, CustomNpcIdentifiers.PET_GENERAL_GRAARDOR_BLACK, UNLOCKED_PET_GRAARDOR_BLACK),
    PET_KRIL_TSUTSAROTH_BLACK(CustomItemIdentifiers.PET_KRIL_TSUTSAROTH_BLACK, CustomNpcIdentifiers.PET_KRIL_TSUTSAROTH_BLACK, UNLOCKED_PET_KRIL_BLACK),
    PET_ZILYANA_WHITE(CustomItemIdentifiers.PET_ZILYANA_WHITE, CustomNpcIdentifiers.PET_ZILYANA_WHITE, UNLOCKED_PET_ZILYANA_WHITE),
    PET_KREEARRA_WHITE(CustomItemIdentifiers.PET_KREE_ARRA_WHITE, CustomNpcIdentifiers.PET_KREEARRA_WHITE, UNLOCKED_PET_KREE_WHITE),

    //OSRS
    ABYSSAL_ORPHAN(ItemIdentifiers.ABYSSAL_ORPHAN, NpcIdentifiers.ABYSSAL_ORPHAN_5884, UNLOCKED_PET_ABYSSAL),
    CALLISTO_CUB(ItemIdentifiers.CALLISTO_CUB, NpcIdentifiers.CALLISTO_CUB_5558, UNLOCKED_PET_CALLISTO),
    HELLPUPPY(ItemIdentifiers.HELLPUPPY, NpcIdentifiers.HELLPUPPY_3099, UNLOCKED_PET_CERBERUS),
    KALPHITE_PRINCESS(ItemIdentifiers.KALPHITE_PRINCESS, NpcIdentifiers.KALPHITE_PRINCESS_6638, NpcIdentifiers.KALPHITE_PRINCESS, UNLOCKED_PET_KQ),
    KALPHITE_PRINCESS_2(ItemIdentifiers.KALPHITE_PRINCESS_12654, NpcIdentifiers.KALPHITE_PRINCESS, NpcIdentifiers.KALPHITE_PRINCESS_6638, UNLOCKED_PET_KQ),
    PET_CHAOS_ELEMENTAL(ItemIdentifiers.PET_CHAOS_ELEMENTAL, NpcIdentifiers.CHAOS_ELEMENTAL_JR, UNLOCKED_PET_CHAOS_ELE),
    PET_DAGANNOTH_PRIME(ItemIdentifiers.PET_DAGANNOTH_PRIME, NpcIdentifiers.DAGANNOTH_PRIME_JR_6629, UNLOCKED_PET_DAGANNOTH_PRIME),
    PET_DAGANNOTH_REX(ItemIdentifiers.PET_DAGANNOTH_REX, NpcIdentifiers.DAGANNOTH_REX_JR, UNLOCKED_PET_DAGANNOTH_REX),
    PET_DAGGANOTH_SUPREME(ItemIdentifiers.PET_DAGANNOTH_SUPREME, NpcIdentifiers.DAGANNOTH_SUPREME_JR_6628, UNLOCKED_PET_DAGANNOTH_SUPREME),
    PET_DARK_CORE(ItemIdentifiers.PET_DARK_CORE, NpcIdentifiers.DARK_CORE,NpcIdentifiers.CORPOREAL_CRITTER_8010, UNLOCKED_PET_CORE),
    PET_GENERAL_GRAARDOR(ItemIdentifiers.PET_GENERAL_GRAARDOR, NpcIdentifiers.GENERAL_GRAARDOR_JR, UNLOCKED_PET_GRAARDOR),
    PET_KRIL_TSUTSAROTH(ItemIdentifiers.PET_KRIL_TSUTSAROTH, NpcIdentifiers.KRIL_TSUTSAROTH_JR, UNLOCKED_PET_KRIL),
    PET_KREEARRA(ItemIdentifiers.PET_KREEARRA, NpcIdentifiers.KREEARRA_JR, UNLOCKED_PET_KREEARRA),
    PET_ZILYANA(ItemIdentifiers.PET_ZILYANA, NpcIdentifiers.ZILYANA_JR, UNLOCKED_PET_ZILYANA),
    PET_KRAKEN(ItemIdentifiers.PET_KRAKEN, NpcIdentifiers.KRAKEN_6640, UNLOCKED_PET_KRAKEN),
    PET_PENANCE_QUEEN(ItemIdentifiers.PET_PENANCE_QUEEN, NpcIdentifiers.PENANCE_PET_6674, UNLOCKED_PET_PENANCE_QUEEN),
    PET_SMOKE_DEVIL(ItemIdentifiers.PET_SMOKE_DEVIL, NpcIdentifiers.SMOKE_DEVIL_6639, UNLOCKED_PET_SMOKE_DEVIL),
    SNAKELING(ItemIdentifiers.PET_SNAKELING, NpcIdentifiers.SNAKELING_2130, NpcIdentifiers.SNAKELING_2128, UNLOCKED_PET_ZULRAH),
    MAGMA_SNAKELING(ItemIdentifiers.PET_SNAKELING_12939, NpcIdentifiers.SNAKELING_2131, NpcIdentifiers.SNAKELING_2129, UNLOCKED_PET_ZULRAH),
    TANZANITE_SNAKELING(ItemIdentifiers.PET_SNAKELING_12940, NpcIdentifiers.SNAKELING_2132, NpcIdentifiers.SNAKELING_2127, UNLOCKED_PET_ZULRAH),
    PRINCE_BLACK_DRAGON(ItemIdentifiers.PRINCE_BLACK_DRAGON, NpcIdentifiers.PRINCE_BLACK_DRAGON, UNLOCKED_PET_KBD),
    SCORPIAS_OFFSPRING(ItemIdentifiers.SCORPIAS_OFFSPRING, NpcIdentifiers.SCORPIAS_OFFSPRING_5561, UNLOCKED_PET_SCORPIA),
    TZREK_JAD(ItemIdentifiers.TZREKJAD, NpcIdentifiers.TZREKJAD_5893, UNLOCKED_PET_JAD),
    VENENATIS_SPIDERLING(ItemIdentifiers.VENENATIS_SPIDERLING, NpcIdentifiers.VENENATIS_SPIDERLING_5557, UNLOCKED_PET_VENENATIS),
    VETION_JR_PURPLE(ItemIdentifiers.VETION_JR, NpcIdentifiers.VETION_JR_5559, NpcIdentifiers.VETION_JR_5560, UNLOCKED_PET_VETION_PURPLE),
    VETION_JR_ORANGE(ItemIdentifiers.VETION_JR_13180, NpcIdentifiers.VETION_JR_5560, NpcIdentifiers.VETION_JR_5559, UNLOCKED_PET_VETION_PURPLE),
    NOON(ItemIdentifiers.NOON, NpcIdentifiers.NOON_7892, NpcIdentifiers.MIDNIGHT_7893, UNLOCKED_PET_NOON),
    MIDNIGHT(ItemIdentifiers.MIDNIGHT, NpcIdentifiers.MIDNIGHT_7893, NpcIdentifiers.NOON_7892, UNLOCKED_PET_NOON),
    SKOTOS(ItemIdentifiers.SKOTOS, NpcIdentifiers.SKOTOS_7671, UNLOCKED_PET_SKOTOS),
    VORKI(ItemIdentifiers.VORKI, NpcIdentifiers.VORKI_8029, UNLOCKED_PET_VORKI),
    OLMLET(ItemIdentifiers.OLMLET, NpcIdentifiers.OLMLET_7520, UNLOCK_PET_OLMLET),
    PUPPADILE(ItemIdentifiers.PUPPADILE, NpcIdentifiers.PUPPADILE_8201, NpcIdentifiers.OLMLET_7520, UNLOCK_PET_OLMLET),
    TEKTINY(ItemIdentifiers.TEKTINY, NpcIdentifiers.TEKTINY_8202, NpcIdentifiers.OLMLET_7520, UNLOCK_PET_OLMLET),
    VANGUARD(ItemIdentifiers.VANGUARD, NpcIdentifiers.VANGUARD_8203, NpcIdentifiers.OLMLET_7520, UNLOCK_PET_OLMLET),
    VASA_MINIRIO(ItemIdentifiers.VASA_MINIRIO, NpcIdentifiers.VASA_MINIRIO_8204, NpcIdentifiers.OLMLET_7520, UNLOCK_PET_OLMLET),
    VESPINA(ItemIdentifiers.VESPINA, NpcIdentifiers.VESPINA_8205, NpcIdentifiers.OLMLET_7520, UNLOCK_PET_OLMLET),
    IKKLE_HYDRA_GREEN(ItemIdentifiers.IKKLE_HYDRA, NpcIdentifiers.IKKLE_HYDRA, NpcIdentifiers.IKKLE_HYDRA_8493,-1),
    IKKLE_HYDRA_BLUE(ItemIdentifiers.IKKLE_HYDRA_22748, NpcIdentifiers.IKKLE_HYDRA_8493, NpcIdentifiers.IKKLE_HYDRA_8494,-1),
    IKKLE_HYDRA_RED(ItemIdentifiers.IKKLE_HYDRA_22750, NpcIdentifiers.IKKLE_HYDRA_8494, NpcIdentifiers.IKKLE_HYDRA_8495,-1),
    IKKLE_HYDRA_BLACK(ItemIdentifiers.IKKLE_HYDRA_22752, NpcIdentifiers.IKKLE_HYDRA_8495, NpcIdentifiers.IKKLE_HYDRA,-1),

    //Random junk pets
    BABY_CHINCHOMPA_BLACK(ItemIdentifiers.BABY_CHINCHOMPA_13325, NpcIdentifiers.BABY_CHINCHOMPA_6758, NpcIdentifiers.BABY_CHINCHOMPA_6759, UNLOCKED_PET_CHINCHOMPA),
    BABY_CHINCHOMPA_GREY(ItemIdentifiers.BABY_CHINCHOMPA_13324, NpcIdentifiers.BABY_CHINCHOMPA_6757, NpcIdentifiers.BABY_CHINCHOMPA_6758, UNLOCKED_PET_CHINCHOMPA),
    BABY_CHINCHOMPA_RED(ItemIdentifiers.BABY_CHINCHOMPA, NpcIdentifiers.BABY_CHINCHOMPA_6756, NpcIdentifiers.BABY_CHINCHOMPA_6757, UNLOCKED_PET_CHINCHOMPA),
    BABY_CHINCHOMPA_YELLOW(ItemIdentifiers.BABY_CHINCHOMPA_13326, NpcIdentifiers.BABY_CHINCHOMPA_6759, NpcIdentifiers.BABY_CHINCHOMPA_6756, UNLOCKED_PET_CHINCHOMPA),
    BEAVER(ItemIdentifiers.BEAVER, NpcIdentifiers.BEAVER_6724, UNLOCKED_PET_BEAVER),
    HERON(ItemIdentifiers.HERON, NpcIdentifiers.HERON_6722, UNLOCKED_PET_HERON),
    ROCK_GOLEM(ItemIdentifiers.ROCK_GOLEM, NpcIdentifiers.ROCK_GOLEM_7439, UNLOCKED_PET_GOLEM),
    ROCK_GOLEM_TIN(21187, NpcIdentifiers.ROCK_GOLEM_7440, UNLOCKED_PET_GOLEM),
    ROCK_GOLEM_COPPER(21188, NpcIdentifiers.ROCK_GOLEM_7441, UNLOCKED_PET_GOLEM),
    ROCK_GOLEM_IRON(21189, NpcIdentifiers.ROCK_GOLEM_7442, UNLOCKED_PET_GOLEM),
    ROCK_GOLEM_BLURITE(21190, NpcIdentifiers.ROCK_GOLEM_7443, UNLOCKED_PET_GOLEM),
    ROCK_GOLEM_SILVER(21191, NpcIdentifiers.ROCK_GOLEM_7444, UNLOCKED_PET_GOLEM),
    ROCK_GOLEM_DAEYALT(21360, -1, UNLOCKED_PET_GOLEM),
    ROCK_GOLEM_COAL(21192, NpcIdentifiers.ROCK_GOLEM_7445, UNLOCKED_PET_GOLEM),
    ROCK_GOLEM_ELEMENTAL(21359, NpcIdentifiers.ROCK_GOLEM_7737, UNLOCKED_PET_GOLEM),
    ROCK_GOLEM_GOLD(21193, NpcIdentifiers.ROCK_GOLEM_7446, UNLOCKED_PET_GOLEM),
    ROCK_GOLEM_GRANITE(21195, NpcIdentifiers.ROCK_GOLEM_7448, UNLOCKED_PET_GOLEM),
    ROCK_GOLEM_MITHRIL(21194, NpcIdentifiers.ROCK_GOLEM_7447, UNLOCKED_PET_GOLEM),
    ROCK_GOLEM_LOVAKITE(21358, NpcIdentifiers.ROCK_GOLEM_7736, UNLOCKED_PET_GOLEM),
    ROCK_GOLEM_ADAMANTITE(21196, NpcIdentifiers.ROCK_GOLEM_7449, UNLOCKED_PET_GOLEM),
    ROCK_GOLEM_RUNITE(21197, NpcIdentifiers.ROCK_GOLEM_7450, UNLOCKED_PET_GOLEM),
    ROCK_GOLEM_AMETHYST(21340, NpcIdentifiers.ROCK_GOLEM_7711, UNLOCKED_PET_GOLEM),
    GIANT_SQUIRREL(ItemIdentifiers.GIANT_SQUIRREL, NpcIdentifiers.GIANT_SQUIRREL_7351, UNLOCK_PET_GIANT_SQUIRREL),
    TANGLEROOT(ItemIdentifiers.TANGLEROOT, NpcIdentifiers.TANGLEROOT_7352, UNLOCK_PET_TANGLEROOT),
    ROCKY(ItemIdentifiers.ROCKY, NpcIdentifiers.ROCKY_7353, UNLOCKED_PET_ROCKY),
    RIFT_GUARDIAN_FIRE(ItemIdentifiers.RIFT_GUARDIAN, NpcIdentifiers.RIFT_GUARDIAN_7354, UNLOCK_PET_RIFT_GUARDIAN),
    RIFT_GUARDIAN_AIR(ItemIdentifiers.RIFT_GUARDIAN_20667, NpcIdentifiers.RIFT_GUARDIAN_7355, UNLOCK_PET_RIFT_GUARDIAN),
    RIFT_GUARDIAN_MIND(ItemIdentifiers.RIFT_GUARDIAN_20669, NpcIdentifiers.RIFT_GUARDIAN_7356, UNLOCK_PET_RIFT_GUARDIAN),
    RIFT_GUARDIAN_WATER(ItemIdentifiers.RIFT_GUARDIAN_20671, NpcIdentifiers.RIFT_GUARDIAN_7357, UNLOCK_PET_RIFT_GUARDIAN),
    RIFT_GUARDIAN_EARTH(ItemIdentifiers.RIFT_GUARDIAN_20673, NpcIdentifiers.RIFT_GUARDIAN_7358, UNLOCK_PET_RIFT_GUARDIAN),
    RIFT_GUARDIAN_BODY(ItemIdentifiers.RIFT_GUARDIAN_20675, NpcIdentifiers.RIFT_GUARDIAN_7359, UNLOCK_PET_RIFT_GUARDIAN),
    RIFT_GUARDIAN_COSMIC(ItemIdentifiers.RIFT_GUARDIAN_20677, NpcIdentifiers.RIFT_GUARDIAN_7360, UNLOCK_PET_RIFT_GUARDIAN),
    RIFT_GUARDIAN_CHAOS(ItemIdentifiers.RIFT_GUARDIAN_20679, NpcIdentifiers.RIFT_GUARDIAN_7361, UNLOCK_PET_RIFT_GUARDIAN),
    RIFT_GUARDIAN_NATURE(ItemIdentifiers.RIFT_GUARDIAN_20681, NpcIdentifiers.RIFT_GUARDIAN_7362, UNLOCK_PET_RIFT_GUARDIAN),
    RIFT_GUARDIAN_LAW(ItemIdentifiers.RIFT_GUARDIAN_20683, NpcIdentifiers.RIFT_GUARDIAN_7363, UNLOCK_PET_RIFT_GUARDIAN),
    RIFT_GUARDIAN_DEATH(ItemIdentifiers.RIFT_GUARDIAN_20685, NpcIdentifiers.RIFT_GUARDIAN_7364, UNLOCK_PET_RIFT_GUARDIAN),
    RIFT_GUARDIAN_SOUL(ItemIdentifiers.RIFT_GUARDIAN_20687, NpcIdentifiers.RIFT_GUARDIAN_7365, UNLOCK_PET_RIFT_GUARDIAN),
    RIFT_GUARDIAN_ASTRAL(ItemIdentifiers.RIFT_GUARDIAN_20689, NpcIdentifiers.RIFT_GUARDIAN_7366, UNLOCK_PET_RIFT_GUARDIAN),
    RIFT_GUARDIAN_BLOOD(ItemIdentifiers.RIFT_GUARDIAN_20691, NpcIdentifiers.RIFT_GUARDIAN_7367, UNLOCK_PET_RIFT_GUARDIAN),
    WRATH_RIFT_GUARDIAN(ItemIdentifiers.RIFT_GUARDIAN_21990, NpcIdentifiers.RIFT_GUARDIAN_8024, UNLOCK_PET_RIFT_GUARDIAN),
    HERBI(ItemIdentifiers.HERBI, NpcIdentifiers.HERBI_7760, UNLOCKED_PET_HERBI),
    BLOODHOUND(ItemIdentifiers.BLOODHOUND, NpcIdentifiers.BLOODHOUND_7232, UNLOCK_PET_BLOODHOUND),
    CHOMPY_CHICK(ItemIdentifiers.CHOMPY_CHICK, NpcIdentifiers.CHOMPY_CHICK_4002, UNLOCKED_PET_CHOMPY),
    DOPPELGANGER(CustomItemIdentifiers.DOPPELGANGER_PET, CustomNpcIdentifiers.DOPPELGANGER, PetVarbits.UNLOCKED_PET_DOPPELGANGER),
    YOSHI(CustomItemIdentifiers.YOSHI_PET, CustomNpcIdentifiers.YOSHI, PetVarbits.UNLOCKED_PET_YOSHI),
    CURSED_ELEMENT(CustomItemIdentifiers.CURSED_ELEMENT_PET, CustomNpcIdentifiers.CURSED_ELEMENT, PetVarbits.UNLOCKED_PET_CURSED_ELEMENT),
    THE_ONE_ABOVE_ALL_PET(CustomItemIdentifiers.THE_ONE_ABOVE_ALL_PET, CustomNpcIdentifiers.THE_ONE_ABOVE_ALL, PetVarbits.UNLOCKED_THE_ONE_ABOVE_ALL);

    public int item;
    public int npc;
    public int morphId;
    public int varbit;

    Pet(int item, int npc, int morphId, int varbit) {
        this.item = item;
        this.npc = npc;
        this.morphId = morphId;
        this.varbit = varbit;
    }

    Pet(int item, int npc, int varbit) {
        this.item = item;
        this.npc = npc;
        this.varbit = varbit;
    }

    private static final Map<Integer, Pet> MAP_BY_NPC = new HashMap<>();

    static {
        for (Pet pet : values()) {
            MAP_BY_NPC.put(pet.npc, pet);
        }
    }

    public static Optional<Pet> fromNpc(int identifier) {
        return Arrays.stream(values()).filter(s -> s.npc == identifier).findFirst();
    }

    public static Pet getByNpc(int npc) {
        return MAP_BY_NPC.get(npc);
    }

    public static Pet getPetByItem(int item) {
        for (Pet pet : values()) {
            if (pet.item == item) {
                return pet;
            }
        }
        return null;
    }

    public static int getBasePetNpcId(int petId) {
        NpcDefinition definition = World.getWorld().definitions().get(NpcDefinition.class, petId);

        switch (definition.name) {
            case "Rift guardian" -> petId = Pet.RIFT_GUARDIAN_FIRE.npc;
            case "Snakeling" -> petId = Pet.SNAKELING.npc;
            case "Kalphite Princess" -> petId = Pet.KALPHITE_PRINCESS.npc;
            case "Vet'ion Jr." -> petId = Pet.VETION_JR_PURPLE.npc;
            case "Baby Chinchompa" -> petId = Pet.BABY_CHINCHOMPA_RED.npc;
            case "Rock Golem" -> petId = Pet.ROCK_GOLEM.npc;
        }
        return petId;
    }

    public static ArrayList<Integer> getItemAlternatives(int petId) {
        ArrayList<Integer> alternatives = new ArrayList<>();

        Pet pet = Pet.getPetByItem(petId);

        if(pet == null)
            return null;

        NpcDefinition definition = World.getWorld().definitions().get(NpcDefinition.class, pet.npc);

        switch (definition.name) {
            case "Rift guardian" -> alternatives.addAll(
                Arrays.asList(
                    Pet.RIFT_GUARDIAN_AIR.item,
                    Pet.RIFT_GUARDIAN_MIND.item,
                    Pet.RIFT_GUARDIAN_WATER.item,
                    Pet.RIFT_GUARDIAN_EARTH.item,
                    Pet.RIFT_GUARDIAN_FIRE.item,
                    Pet.RIFT_GUARDIAN_BODY.item,
                    Pet.RIFT_GUARDIAN_COSMIC.item,
                    Pet.RIFT_GUARDIAN_CHAOS.item,
                    Pet.RIFT_GUARDIAN_ASTRAL.item,
                    Pet.RIFT_GUARDIAN_NATURE.item,
                    Pet.RIFT_GUARDIAN_LAW.item,
                    Pet.RIFT_GUARDIAN_DEATH.item,
                    Pet.RIFT_GUARDIAN_BLOOD.item,
                    Pet.RIFT_GUARDIAN_SOUL.item,
                    Pet.WRATH_RIFT_GUARDIAN.item
                )
            );
            case "Snakeling" -> alternatives.addAll(
                Arrays.asList(
                    Pet.SNAKELING.item,
                    Pet.MAGMA_SNAKELING.item,
                    Pet.TANZANITE_SNAKELING.item
                )
            );
            case "Kalphite Princess" -> alternatives.addAll(
                Arrays.asList(
                    Pet.KALPHITE_PRINCESS.item,
                    Pet.KALPHITE_PRINCESS_2.item
                )
            );
            case "Noon" -> alternatives.addAll(
                Arrays.asList(
                    Pet.NOON.item,
                    Pet.MIDNIGHT.item
                )
            );
            case "Vet'ion Jr." -> alternatives.addAll(
                Arrays.asList(
                    Pet.VETION_JR_PURPLE.item,
                    Pet.VETION_JR_ORANGE.item
                )
            );
            case "Baby Chinchompa" -> alternatives.addAll(
                Arrays.asList(
                    Pet.BABY_CHINCHOMPA_RED.item,
                    Pet.BABY_CHINCHOMPA_BLACK.item,
                    Pet.BABY_CHINCHOMPA_GREY.item,
                    Pet.BABY_CHINCHOMPA_YELLOW.item
                )
            );
            case "Rock Golem" -> alternatives.addAll(
                Arrays.asList(
                    Pet.ROCK_GOLEM.item,
                    Pet.ROCK_GOLEM_TIN.item,
                    Pet.ROCK_GOLEM_COPPER.item,
                    Pet.ROCK_GOLEM_IRON.item,
                    Pet.ROCK_GOLEM_BLURITE.item,
                    Pet.ROCK_GOLEM_SILVER.item,
                    Pet.ROCK_GOLEM_DAEYALT.item,
                    Pet.ROCK_GOLEM_COAL.item,
                    Pet.ROCK_GOLEM_ELEMENTAL.item,
                    Pet.ROCK_GOLEM_GOLD.item,
                    Pet.ROCK_GOLEM_MITHRIL.item,
                    Pet.ROCK_GOLEM_GRANITE.item,
                    Pet.ROCK_GOLEM_MITHRIL.item,
                    Pet.ROCK_GOLEM_LOVAKITE.item,
                    Pet.ROCK_GOLEM_ADAMANTITE.item,
                    Pet.ROCK_GOLEM_RUNITE.item,
                    Pet.ROCK_GOLEM_AMETHYST.item
                )
            );
            default -> alternatives.add(petId);
        }

        return alternatives;
    }

    public boolean canMorph() {
        return morphId > 0;
    }
}
