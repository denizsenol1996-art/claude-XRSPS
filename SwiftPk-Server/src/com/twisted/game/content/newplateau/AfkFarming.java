package com.twisted.game.content.newplateau;



import com.twisted.game.world.entity.dialogue.DialogueManager;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.object.GameObject;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.chainedwork.Chain;


import static com.twisted.util.ItemIdentifiers.AFK_TICKET;//update19



/**
 * @author The Plateau
 * @data 20/6/2023
 */
public class AfkFarming extends PacketInteraction {


    public enum Farming {


        AFK_FARMING(1, 0, 7.0, "Afk Farming", new int[][]{{18826},});

        public final int levelReq, respawnTime;
        public final int[][] objIDs;
        public final double experience;
        public final String name;

        Farming(int levelReq, int respawnTime, double experience, String name, int[][] objIDs) {
            this.levelReq = levelReq;
            this.respawnTime = respawnTime * 1000 / 600;
            this.experience = experience;
            this.name = name;
            this.objIDs = objIDs;
        }
    }

    private void attempt(Player player, Farming farming, GameObject object, int replacementID) {
        player.faceObj(object);
        if (!player.skills().check(Skills.FARMING, farming.levelReq, "farming from the " + farming.name))
            return;
        if (player.inventory().isFull()) {
            player.sound(2277);
            DialogueManager.sendStatement(player, "Your inventory is too full to hold any more.");
            return;
        }


        Chain.bound(player).repeatingTask(6, t -> {
            player.unlock();
            player.animate(2273);
            player.getInventory().add(new Item(AFK_TICKET, 2), true);//up2
            boolean geniePet = player.hasPetOut("Genie pet");
            if(geniePet && !DoubleExperience.isDoubleExperience()) {
                player.skills().addXp(Skills.FARMING, farming.experience * 2, true);
            } else {
                player.skills().addXp(Skills.FARMING, farming.experience, true);

            }
            player.unlock();



        });

    }


    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        if (option == 1 || option == 2) {
            for (Farming farming : Farming.values()) {
                for (int[] ids : farming.objIDs) {
                    if (object.getId() == ids[0]) {
                        attempt(player, farming, object, ids[0]);
                        return true;
                    }
                }
            }
        }
        return false;
    }


}

