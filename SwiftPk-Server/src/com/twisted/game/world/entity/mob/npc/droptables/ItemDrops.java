package com.twisted.game.world.entity.mob.npc.droptables;

import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.events.ServerEvent;
import com.twisted.game.content.skill.impl.slayer.SlayerConstants;
import com.twisted.game.content.skill.impl.slayer.slayer_task.SlayerCreature;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.npc.NpcDeath;
import com.twisted.game.world.entity.mob.npc.pets.Pet;
import com.twisted.game.world.entity.mob.npc.pets.PetAI;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.ground.GroundItem;
import com.twisted.game.world.items.ground.GroundItemHandler;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.*;
import com.twisted.util.chainedwork.Chain;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.twisted.game.content.collection_logs.CollectionLog.RAIDS_KEY;
import static com.twisted.game.world.entity.AttributeKey.DOUBLE_DROP_LAMP_TICKS;
import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;

public class ItemDrops {
    public static final List<Integer> BONES = Arrays.asList(ItemIdentifiers.BONES, BURNT_BONES, BAT_BONES, BIG_BONES, BABYDRAGON_BONES, DRAGON_BONES, JOGRE_BONES, ZOGRE_BONES, OURG_BONES, WYVERN_BONES, DAGANNOTH_BONES, LONG_BONE, CURVED_BONE, LAVA_DRAGON_BONES, SUPERIOR_DRAGON_BONES, WYRM_BONES, DRAKE_BONES, HYDRA_BONES);
    private static final Logger npcDropLogs = LogManager.getLogger("NpcDropLogs");
    private static final Level NPC_DROPS;

    static {
        NPC_DROPS = Level.getLevel("NPC_DROPS");
    }

    public static void rollTheDropTable(Player player, Npc npc) {
        int npcId = NpcDropRepository.getDropNpcId(npc.getId());
        NpcDropTable table = NpcDropRepository.forNPC(npcId);
        boolean doubleDropsLampActive = (Integer) player.getAttribOr(DOUBLE_DROP_LAMP_TICKS, 0) > 0 || ServerEvent.isDoubleDrops();
        boolean dropUnderPlayer = npc.id() == NpcIdentifiers.KRAKEN || npc.id() == NpcIdentifiers.CAVE_KRAKEN || npc.id() >= NpcIdentifiers.ZULRAH && npc.id() <= NpcIdentifiers.ZULRAH_2044 || npc.id() >= NpcIdentifiers.VORKATH_8059 && npc.id() <= NpcIdentifiers.VORKATH_8061 || npc.id() == 12192 || npc.id() == 12191 || npc.id() == 12166;
        Tile tile = dropUnderPlayer ? player.tile() : npc.tile();
        if (table != null) {
            int dropRolls = npc.getCombatInfo().droprolls;
            List<Item> rewards = table.getDrops(player, dropRolls);
            for (var item : rewards) {
                LogType.BOSSES.log(player, npc.id(), item);
                LogType.BOSSES.log(player, RAIDS_KEY, item);
                LogType.OTHER.log(player, npc.id(), item);
                var drop = item.noted() ? item.unnote().note() : item;
                rollForLarransKey(npc, player);
                rollForKeyOfDrops(player, npc);
                rollForTotemBase(player);
                rollForTotemMiddle(player);
                rollForTotemTop(player);
                player.getSlayerKey().drop(npc);
                isRareDrop(player, npc, table, drop);
                if (((drop.getId() == DRAGON_BONES && player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.NOTED_DRAGON_BONES)) || (drop.getId() == LAVA_DRAGON_BONES && player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.NOTED_DRAGON_BONES)))) {
                    drop = drop.note();
                }

                if (player.hasPetOut(15000) && !doubleDropsLampActive) {
                    if (Utils.rollDie(10, 1)) {
                        drop.setAmount(drop.getAmount() * 2);
                    }
                }

                if (doubleDropsLampActive) {
                    drop.setAmount(drop.getAmount() * 2);
                }

                if (isPet(player, drop))
                    continue;

                checkEventBoosts(player, npc, drop);

                if (npc.isWorldBoss()) {
                    GroundItemHandler.createGroundItem(new GroundItem(new Item(SUMMER_TOKEN, World.getWorld().random(250, 1000)), tile, player));
                }

                if (player.hasPetOut(12302) || player.hasPetOut(CustomNpcIdentifiers.THE_ONE_ABOVE_ALL)) {
                    if (!player.locked() && !player.dead()) {
                        Optional<GroundItem> groundItem = Optional.of(new GroundItem(drop, tile, player));
                        player.putAttrib(AttributeKey.INTERACTED_GROUNDITEM, groundItem.get());
                        player.putAttrib(AttributeKey.INTERACTION_OPTION, 4);
                        final Tile actorLocation = player.pet().tile();
                        player.pet().performGraphic(new Graphic(142, 50, 0));
                        int tileDist = actorLocation.distance(tile);
                        int duration = (51 + -5 + (10 * tileDist));
                        Projectile p = new Projectile(actorLocation, tile, -1, 143, duration, 51, 50, 0, 0);
                        p.sendProjectile();
                        World.getWorld().tileGraphic(144, tile, 0, p.getSpeed());
                        Item finalDrop = drop;
                        Chain.noCtx().runFn((int) (p.getSpeed() / 30D) + 1, () -> {
                            player.getBank().add(new Item(finalDrop.getId(), finalDrop.getAmount()));
                            String itemName = item.unnote().name();
                            boolean amOverOne = item.getAmount() > 1;
                            String amtString = amOverOne ? "x" + Utils.format(item.getAmount()) : Utils.getAOrAn(item.name());
                            player.message("<shad=0>" + Color.GREEN.wrap("Your Yoshi Pet Collected " + amtString + " " + itemName + "."));
                        });
                    }
                } else if (player.nifflerPetOut() && player.nifflerCanStore(npc) && drop.getValue() > 0) {
                    player.nifflerStore(drop);
                } else if (drop.getId() == BLOOD_MONEY && NpcDeath.hasRingOut(player)) {
                    player.getInventory().addOrDrop(new Item(drop.getId(), drop.getAmount()));
                } else {
                    if (!drop.stackable()) {
                        final Item newDrop = new Item(drop.getId(), 1);
                        for (int index = 0; index < drop.getAmount(); index++) {
                            GroundItemHandler.createGroundItem(new GroundItem(newDrop, tile, player));
                        }
                    } else {
                        GroundItemHandler.createGroundItem(new GroundItem(drop, tile, player));
                    }
                }
            }
        }
    }

    static void checkEventBoosts(final Player player, final Npc npc, Item drop) {
        if (World.TRIPLE_SUMMER_TOKEN_DROPS && drop.getId() == SUMMER_TOKEN) {
            int amount = drop.getAmount();
            drop.setAmount(amount * 3);
        } else if (npc.id() == 12301 && player.getEquipment().contains(SUMMER_AMULET) && drop.getId() == SUMMER_TOKEN) {
            int total = World.getWorld().random(50, 125);
            drop.setAmount(total);
        }
    }

    static void isRareDrop(Player player, Npc npc, NpcDropTable table, Item drop) {
        for (var i : table.getDrops()) {
            var parsedID = ItemRepository.getItemId(i.getItem());
            if (parsedID == drop.getId()) {
                if (!i.isRareDrop()) {
                    continue;
                }

                var inWild = WildernessArea.inWilderness(player.tile());
                var level = WildernessArea.wildernessLevel(player.tile());
                World.getWorld().sendWorldMessage("<img=1875> " + Color.YELLOW.wrap("<shad=0>" + player.getUsername() + " has received a " + Color.BURNTORANGE.wrap(drop.name()) + " from a " + Color.BURNTORANGE.wrap(npc.getMobName()) + (!inWild ? "." : " Level: " + level + " wilderness.") + "</shad>"));
            }
        }
    }


    static boolean isPet(Player player, Item drop) {
        Pet definitions = Pet.getPetByItem(drop.getId());
        if (definitions != null) {
            final var pet = player.pet();
            player.message("You have a funny feeling you're being followed...");
            if (player.pet() != null && !player.pet().finished()) {
                player.getInventory().addOrBank(new Item(definitions.item));
                return true;
            }
            PetAI.spawnPet(player, pet.petType(), false);
            return true;
        }
        return false;
    }


    public static void rollForTotemBase(Player player) {
        var inMemberCave = player.tile().memberCave();
        if (inMemberCave) {
            var roll = 100;
            var reduction = roll * player.totemDropRateBonus() / 100;
            roll -= reduction;
            if (World.getWorld().rollDie(roll, 1)) {
                GroundItemHandler.createGroundItem(new GroundItem(new Item(CORRUPT_TOTEM_BASE), player.tile(), player));
            }
        }
    }

    public static void rollForTotemMiddle(Player player) {
        var inMemberCave = player.tile().memberCave();
        if (inMemberCave) {
            var roll = 100;
            var reduction = roll * player.totemDropRateBonus() / 100;
            roll -= reduction;

            if (World.getWorld().rollDie(roll, 1)) {
                GroundItemHandler.createGroundItem(new GroundItem(new Item(CORRUPT_TOTEM_MIDDLE), player.tile(), player));
            }
        }
    }

    public static void rollForTotemTop(Player player) {
        var inMemberCave = player.tile().memberCave();
        if (inMemberCave) {
            var roll = 100;
            var reduction = roll * player.totemDropRateBonus() / 100;
            roll -= reduction;

            if (World.getWorld().rollDie(roll, 1)) {
                GroundItemHandler.createGroundItem(new GroundItem(new Item(CORRUPT_TOTEM_TOP), player.tile(), player));
            }
        }
    }

    public static void rollForLarransKey(Npc npc, Player player) {
        var inWilderness = WildernessArea.inWilderness(player.tile());
        if (inWilderness) {
            var larransLuck = player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.LARRANS_LUCK);
            var combatLvl = npc.def().combatlevel;
            var roll = combatLvl < 50 ? larransLuck ? 875 : 1000 : larransLuck ? 350 : 400;
            if (World.getWorld().rollDie(roll, 1)) {
                GroundItemHandler.createGroundItem(new GroundItem(new Item(ItemIdentifiers.LARRANS_KEY), player.tile(), player));
            }
        }
    }

    public static void rollForKeyOfDrops(Player player, Npc npc) {
        boolean inWilderness = WildernessArea.inWilderness(player.tile());
        SlayerCreature task = SlayerCreature.lookup(player.getAttribOr(AttributeKey.SLAYER_TASK_ID, 0));

        if (inWilderness && task != null && task.matches(npc.id()) && player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.KEY_OF_BOXES)) {
            int roll = 1000;

            if (Utils.rollDie(roll, 1)) {
                GroundItemHandler.createGroundItem(new GroundItem(new Item(CustomItemIdentifiers.KEY_OF_BOXES), player.tile(), player));
            }
        }
    }

}
