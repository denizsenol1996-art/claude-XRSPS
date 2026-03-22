package com.twisted.game.content.skill.impl.thieving;

import com.twisted.game.content.achievements.AchievementsManager;
import com.twisted.game.content.achievements.Achievements;
import com.twisted.game.content.skill.impl.slayer.SlayerConstants;
import com.twisted.game.content.tasks.impl.Tasks;
import com.twisted.game.task.TaskManager;
import com.twisted.game.task.impl.ForceMovementTask;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.dialogue.DialogueManager;
import com.twisted.game.world.entity.mob.player.ForceMovement;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.ObjectManager;
import com.twisted.game.world.position.Tile;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Color;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;

import static com.twisted.util.CustomItemIdentifiers.DOUBLE_DROPS_LAMP;

/**
 * @author Patrick van Elderen | April, 21, 2021, 11:44
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class Stalls extends PacketInteraction {

    public enum Stall {

        //Donator zone stalls
        SPICE_STALL(65, 5, 181.0, 13000, "spice stall", new int[][]{{6572, 6573}, {11733, 634}, {20348, 20349},}),
        GEM_STALL(75, 10, 200.0, 8500, "gem stall", new int[][]{{6162, 6984}, {11731, 634},}),

        //Normal stalls
        CRAFTING_STALL(1, 2, 16.0, 49000, "crafting stall", new int[][]{{4874, 4797}, {6166, 6984},}),
        MONKEY_GENERAL_STALL(5, 2, 36.0, 49000, "general stall", new int[][]{{4876, 4797},}),
        MAGIC_STALL(65, 2, 100, 12000, "magic stall", new int[][]{{4877, 4797},}),
        SCIMITAR_STALL(65, 2, 100.0, 1000, "scimitar stall", new int[][]{{4878, 4797},});

        public final int levelReq, respawnTime, petOdds;
        public final int[][] objIDs;
        public final double experience;
        public final String name;

        Stall(int levelReq, int respawnTime, double experience, int petOdds, String name, int[][] objIDs) {
            this.levelReq = levelReq;
            this.respawnTime = respawnTime * 1000 / 600;
            this.experience = experience;
            this.petOdds = petOdds;
            this.name = name;
            this.objIDs = objIDs;
        }
    }

    private void attempt(Player player, Stall stall, GameObject object, int replacementID) {
        player.faceObj(object);
        if (!player.skills().check(Skills.THIEVING, stall.levelReq, "steal from the " + stall.name))
            return;

        if (player.inventory().isFull()) {
            player.sound(2277);
            DialogueManager.sendStatement(player, "Your inventory is too full to hold any more.");
            return;
        }

        if (player.inventory().contains(22521, 28) || player.inventory().contains(22522, 28) || player.inventory().contains(22523, 28) || player.inventory().contains(22524, 28) || player.inventory().contains(22525, 28) || player.inventory().contains(22526, 28) || player.inventory().contains(22527, 28) || player.inventory().contains(22528, 28) || player.inventory().contains(22529, 28) || player.inventory().contains(22530, 28)) {
            player.message(Color.RED.wrap("You must claim all your coin pouches before performing this action."));
            return;
        }

        player.message("You attempt to steal from the " + stall.name + "...");
        player.lock();
        player.animate(832);

        Chain.bound(player).runFn(1, () -> {
            replaceStall(stall, object, replacementID, player);

            if (stall == Stall.CRAFTING_STALL) {
                AchievementsManager.activate(player, Achievements.THIEF_I, 1);
                AchievementsManager.activate(player, Achievements.MASTER_THIEF, 1);
                player.getInventory().add(new Item(22521, 1));
            } else if (stall == Stall.MONKEY_GENERAL_STALL) {
                AchievementsManager.activate(player, Achievements.THIEF_II, 1);
                AchievementsManager.activate(player, Achievements.MASTER_THIEF, 1);
                player.getInventory().add(new Item(22522, 1));
            } else if (stall == Stall.MAGIC_STALL) {
                AchievementsManager.activate(player, Achievements.THIEF_III, 1);
                AchievementsManager.activate(player, Achievements.MASTER_THIEF, 1);
                player.getInventory().add(new Item(22523, 1));
            } else if (stall == Stall.SCIMITAR_STALL) {
                AchievementsManager.activate(player, Achievements.THIEF_IV, 1);
                AchievementsManager.activate(player, Achievements.MASTER_THIEF, 1);
                player.getTaskMasterManager().increase(Tasks.STEAL_FROM_SCIMITAR_STALL);
                player.getInventory().add(new Item(22524, 1));
            } else if (stall == Stall.SPICE_STALL) {
                AchievementsManager.activate(player, Achievements.THIEF_IV, 1);
                AchievementsManager.activate(player, Achievements.MASTER_THIEF, 1);
                player.getInventory().add(new Item(22525, 1));
            } else if (stall == Stall.GEM_STALL) {
                AchievementsManager.activate(player, Achievements.THIEF_IV, 1);
                AchievementsManager.activate(player, Achievements.MASTER_THIEF, 1);
                player.getInventory().add(new Item(22526, 1));
            }

            var slayerUnlock = player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.DOUBLE_DROP_LAMPS);
            if (World.getWorld().rollDie(200, 1) && slayerUnlock) {
                player.inventory().addOrDrop(new Item(DOUBLE_DROPS_LAMP));
                player.message(Color.RED.wrap("Double drops lamp slayer perk activated."));
            }

            if (Utils.percentageChance(5)) {
                TaskManager.submit(new ForceMovementTask(player, 3, new ForceMovement(player.tile().clone(), new Tile(0, 1), 0, 70, 2)));
                player.animate(3130);
                player.getMovementQueue().clear();
                player.stun(10);
                player.message("A mysterious force knocks you back.");
            }

            // Woo! A pet!
            var odds = (int) (stall.petOdds * player.getMemberRights().petRateMultiplier());
            if (World.getWorld().rollDie(odds, 1)) {
                ThievingPet.unlockRaccoon(player);
            }
            player.skills().addXp(Skills.THIEVING, stall.experience, true);
            player.unlock();
        });
    }

    private void replaceStall(Stall stall, GameObject object, int replacementID, Player player) {
        var replacement = new GameObject(replacementID, object.tile(), object.getType(), object.getRotation());
        ObjectManager.replace(object, replacement, stall.respawnTime);
    }

    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        if (option == 1 || option == 2) {
            for (Stall stall : Stall.values()) {
                for (int[] ids : stall.objIDs) {
                    if (object.getId() == ids[0]) {
                        attempt(player, stall, object, ids[1]);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
