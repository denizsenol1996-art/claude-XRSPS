package com.twisted.game.content.skill.impl.thieving;


import com.twisted.game.world.World;
import com.twisted.game.world.entity.dialogue.DialogueManager;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.object.GameObject;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.chainedwork.Chain;

import static com.twisted.util.ItemIdentifiers.AFK_TICKET;


/**
 * @author The Plateau
 * @data 5/7/2022
 */
public class AfkStall extends PacketInteraction {


    public enum Stall {


        //Afk Stalls
        AFK_STALL(1, 0, 7.0, 1000, "Afk Stall", new int[][]{{6574},});

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

    private void attempt(Player player, Stall stall, GameObject object) {
        for (Player otherPlayer : player.getLocalPlayers()) {
            if (otherPlayer == null)
                continue;

            if (player.getHostAddress().equalsIgnoreCase(otherPlayer.getHostAddress())) {
                player.message("You can't use alt on same ip with this stall.");
                return;
            }
        }


        player.faceObj(object);
        if (!player.skills().check(Skills.THIEVING, stall.levelReq, "steal from the " + stall.name))
            return;
        if (player.inventory().isFull()) {
            player.sound(2277);
            DialogueManager.sendStatement(player, "Your inventory is too full to hold any more.");
            return;
        }


        Chain.bound(player).repeatingTask(3, t -> {
            player.animate(832);
            player.getInventory().add(new Item(AFK_TICKET, 1), true);
            var odds = (int) (stall.petOdds * player.getMemberRights().petRateMultiplier());
            if (World.getWorld().rollDie(odds, 1)) {
                ThievingPet.unlockRaccoon(player);
            }
            player.skills().addXp(Skills.THIEVING, stall.experience, true);
        });
    }


    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        if (option == 1 || option == 2) {
            for (Stall stall : Stall.values()) {
                for (int[] ids : stall.objIDs) {
                    if (object.getId() == ids[0]) {
                        attempt(player, stall, object);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
