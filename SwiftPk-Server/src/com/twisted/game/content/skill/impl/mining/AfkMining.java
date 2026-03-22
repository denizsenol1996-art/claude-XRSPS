package com.twisted.game.content.skill.impl.mining;


import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.dialogue.DialogueManager;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.object.GameObject;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Color;
import com.twisted.util.chainedwork.Chain;

import java.util.Iterator;

import static com.twisted.util.ItemIdentifiers.AFK_TICKET;


/**
 * @author The Plateau
 * @data 5/7/2022
 */
public class AfkMining extends PacketInteraction {


    public enum Mining {


        //Afk Stalls
        AFK_STALL(1, 0, 7.0, "Afk Stall", new int[][]{{11379},});

        public final int levelReq, respawnTime;
        public final int[][] objIDs;
        public final double experience;
        public final String name;

        Mining(int levelReq, int respawnTime, double experience, String name, int[][] objIDs) {
            this.levelReq = levelReq;
            this.respawnTime = respawnTime * 1000 / 600;
            this.experience = experience;
            this.name = name;
            this.objIDs = objIDs;
        }
    }

    private void attempt(Player player, Mining mining, GameObject object, int replacementID) {
        double totalAmountPaid = player.getAttribOr(AttributeKey.TOTAL_PAYMENT_AMOUNT, 0D);
        if (totalAmountPaid >= 250) {
            for (Iterator<Player> playerIterator = player.getLocalPlayers().iterator(); playerIterator.hasNext(); ) {
                Player otherPlayer = playerIterator.next();
                if (player.getHostAddress().equalsIgnoreCase(otherPlayer.getHostAddress())) {
                    player.message("You can't use alt on same ip with this Afk Ore.");
                    return;
                }
            }


            player.faceObj(object);
            if (!player.skills().check(Skills.MINING, mining.levelReq, "Mine from the " + mining.name))
                return;
            if (player.inventory().isFull()) {
                player.sound(2277);
                DialogueManager.sendStatement(player, "Your inventory is too full to hold any more.");
                return;
            }


            Chain.bound(player).repeatingTask(3, t -> {

                player.unlock();
                player.animate(629);
                player.getInventory().add(new Item(AFK_TICKET, 2), true);
                player.skills().addXp(Skills.MINING, mining.experience, true);
                player.unlock();
            });
        } else {
            player.message("You need to be "+ Color.WHITE.wrap("<img=1078>Diamond Member</col> to use afk ore"));
        }

    }


    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        if (option == 1 || option == 2) {
            for (Mining stall : Mining.values()) {
                for (int[] ids : stall.objIDs) {
                    if (object.getId() == ids[0]) {
                        attempt(player, stall, object, ids[0]);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
