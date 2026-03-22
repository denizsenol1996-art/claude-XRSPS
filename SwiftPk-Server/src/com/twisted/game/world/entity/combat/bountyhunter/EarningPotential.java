package com.twisted.game.world.entity.combat.bountyhunter;

import com.twisted.game.task.Task;
import com.twisted.game.task.TaskManager;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.skull.SkullType;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.Color;
import com.twisted.util.Utils;

import java.util.Optional;

import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen | July, 11, 2021, 00:06
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class EarningPotential {

    public static int maxPotential(Player player) {
        int total = 300;
        var risk = player.<Long>getAttribOr(AttributeKey.RISKED_WEALTH, 0L);

        //Risk increases potential
        total += risk >= 1_000_000 ? 150 : risk >= 500_000 ? 75 : risk >= 200_000 ? 30 : 0;

        //Redskull increases potential
        total += player.getSkullType().equals(SkullType.RED_SKULL) ? 150 : 0;
        return total;
    }

    /**
     * Hotspots are: Edgevile, Mage bank and Cave of death
     *
     * @param entity the player standing in a hotspot area
     * @return true if the player is in a hotspot false otherwise.
     */
    public static boolean inHotspot(Mob entity) {
        return entity.tile().region() == 12343 || entity.tile().region() == 12087 || entity.tile().region() == 12349;
    }

    /**
     * Increases the earning potential ever 15 minutes.
     *
     * @param player The player we increase the potential of
     */
    public static void increasePotential(Player player) {
        if (WildernessArea.inWilderness(player.tile())) {
            TaskManager.submit(new Task("EarningPotential", 1500) { // 1500 ticks aka 15 minutes.
                @Override
                protected void execute() {
                    var potential = player.<Integer>getAttribOr(AttributeKey.EARNING_POTENTIAL, 0);
                    var myMaxPotential = maxPotential(player);

                    //Can't go over max potential
                    if (potential >= myMaxPotential) {
                        potential = myMaxPotential;
                    }

                    //Give 25% for standing in a hotspot and 15% for anywhere else.
                    potential += inHotspot(player) ? 25 : 15;
                    player.putAttrib(AttributeKey.EARNING_POTENTIAL, potential);

                    //Update the string
                    player.getPacketSender().sendString(53722, "Earning Potential: <col=65280>" + player.<Integer>getAttribOr(AttributeKey.EARNING_POTENTIAL, 0) + "%");

                    //When leaving the wilderness stop the task
                    if (!WildernessArea.inWilderness(player.tile())) {
                        stop();
                    }
                }

                @Override
                public void onStop() {
                    super.onStop();
                }
            });
        }
    }

    /**
     * Increases potential by killing npcs in the wilderness.
     *
     * @param player The player slaying npcs
     * @param npc    The enemy
     */
    public static void increaseByKill(Player player, Npc npc) {
        if (player != null && npc != null) {
            if (!WildernessArea.inWilderness(player.tile())) {
                return;
            }

            var potential = player.<Integer>getAttribOr(AttributeKey.EARNING_POTENTIAL, 0);

            if (potential >= maxPotential(player)) {
                potential = maxPotential(player);
            }

            potential += 2;
            player.putAttrib(AttributeKey.EARNING_POTENTIAL, potential);
            player.getPacketSender().sendString(53722, "Earning Potential: <col=65280>" + player.<Integer>getAttribOr(AttributeKey.EARNING_POTENTIAL, 0) + "%");
        }
    }

    /**
     * Drops a random reward for the player and decreases your potential.
     *
     * @param player The player the server drops the reward for.
     */
    public static void randomPotentialDrop(Player player) {
        Item myDrop;
        int totalPotential;

        if (!WildernessArea.inWilderness(player.tile()))
            return;

        Optional<Player> target = BountyHunter.getTargetfor(player);

        boolean hasTarget = target.isPresent();

        if (hasTarget) { // double drop rate if target
            totalPotential = player.<Integer>getAttribOr(AttributeKey.EARNING_POTENTIAL, 0) * 2;
        } else {
            totalPotential = player.<Integer>getAttribOr(AttributeKey.EARNING_POTENTIAL, 0);
        }

        if (World.getWorld().random(1000) <= (1 + totalPotential / 100)) {
            myDrop = Utils.randomElement(RARE_REWARDS);
            player.inventory().addOrBank(myDrop);
            World.getWorld().sendWorldMessage("<img=505>[<col=" + Color.MEDRED.getColorValue() + ">Earning Potential</col>]: <col=800000>" + player.getUsername() + "<col=ff0000> has received an <col=800000>extremely rare<col=ff0000> bonus drop of: <col=ff00ff>" + myDrop.name() + "<col=ff0000>!");
            player.putAttrib(AttributeKey.EARNING_POTENTIAL, 0);
            player.getPacketSender().sendString(53722, "Earning Potential: <col=65280>" + player.<Integer>getAttribOr(AttributeKey.EARNING_POTENTIAL, 0) + "%");
        }

        if (World.getWorld().random(550) <= (1 + totalPotential / 100)) {
            myDrop = Utils.randomElement(UNCOMMON_REWARDS);
            player.inventory().addOrBank(myDrop);
            World.getWorld().sendWorldMessage("<img=505>[<col=" + Color.MEDRED.getColorValue() + ">Earning Potential</col>]: <col=800000>" + player.getUsername() + "<col=ff0000> has received a <col=800000>very rare<col=ff0000> bonus drop of: <col=ff00ff>" + myDrop.name() + "<col=ff0000>!");
            player.putAttrib(AttributeKey.EARNING_POTENTIAL, 0);
            player.getPacketSender().sendString(53722, "Earning Potential: <col=65280>" + player.<Integer>getAttribOr(AttributeKey.EARNING_POTENTIAL, 0) + "%");
        }

        if (World.getWorld().random(50) <= (1 + totalPotential / 100)) {
            myDrop = Utils.randomElement(COMMON_REWARDS);
            player.inventory().addOrBank(myDrop);
            var earningPotential = player.<Integer>getAttribOr(AttributeKey.EARNING_POTENTIAL, 0) / 3;
            player.putAttrib(AttributeKey.EARNING_POTENTIAL, earningPotential);
            player.getPacketSender().sendString(53722, "Earning Potential: <col=65280>" + player.<Integer>getAttribOr(AttributeKey.EARNING_POTENTIAL, 0) + "%");
        }
    }

    private static final Item[] RARE_REWARDS = {new Item(BARRELCHEST_PET), new Item(NIFFLER), new Item(FAWKES), new Item(GRIM_REAPER_PET)};

    private static final Item[] UNCOMMON_REWARDS = {new Item(ETERNAL_BOOTS), new Item(PRIMORDIAL_BOOTS), new Item(PEGASIAN_BOOTS), new Item(TOXIC_STAFF_OF_THE_DEAD), new Item(SERPENTINE_HELM), new Item(AMULET_OF_TORTURE), new Item(NECKLACE_OF_ANGUISH), new Item(TORMENTED_BRACELET), new Item(ARMADYL_GODSWORD), new Item(DRAGON_CLAWS), new Item(VESTAS_LONGSWORD), new Item(VESTAS_CHAINBODY), new Item(VESTAS_PLATESKIRT), new Item(VESTAS_LONGSWORD), new Item(VESTAS_SPEAR), new Item(STATIUSS_FULL_HELM), new Item(STATIUSS_PLATEBODY), new Item(STATIUSS_PLATELEGS), new Item(STATIUSS_WARHAMMER), new Item(MORRIGANS_COIF), new Item(MORRIGANS_LEATHER_CHAPS), new Item(MORRIGANS_LEATHER_BODY), new Item(ZURIELS_HOOD), new Item(ZURIELS_STAFF), new Item(ZURIELS_ROBE_BOTTOM), new Item(ZURIELS_ROBE_TOP)};

    private static final Item[] COMMON_REWARDS = {new Item(ABYSSAL_TENTACLE), new Item(BANDOS_CHESTPLATE), new Item(BANDOS_TASSETS), new Item(BANDOS_GODSWORD), new Item(SARADOMIN_GODSWORD), new Item(ZAMORAK_GODSWORD), new Item(ARMADYL_CROSSBOW), new Item(DRAGON_CROSSBOW), new Item(ARMADYL_CHESTPLATE), new Item(ARMADYL_HELMET), new Item(ARMADYL_CHAINSKIRT), new Item(ABYSSAL_DAGGER), new Item(ABYSSAL_DAGGER_P_13271), new Item(STAFF_OF_THE_DEAD), new Item(DRAGONFIRE_SHIELD), new Item(MALEDICTION_WARD), new Item(ODIUM_WARD), new Item(FREMENNIK_KILT), new Item(BLESSED_SPIRIT_SHIELD)};

}
