package com.twisted.game.content.skill.impl.woodcutting;


import com.twisted.game.world.entity.dialogue.DialogueManager;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.object.GameObject;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.chainedwork.Chain;

import java.util.Iterator;

import static com.twisted.util.ItemIdentifiers.AFK_TICKET;

//donet
/**
 * @author The Plateau
 * @data 6/13/2022
 */
public class AfkTree extends PacketInteraction {


    public enum Tree {


        //Afk Tree
        AFK_TREE(1, 0,4.0, "Afk Tree", new int[][]{{3881},});

        public final int levelReq,respawnTime;
        public final int[][] objIDs;
        public final double experience;
        public final String name;

        Tree(int levelReq, int respawnTime, double experience, String name, int[][] objIDs) {
            this.levelReq = levelReq;

            this.respawnTime = respawnTime * 1000 / 600;
            this.experience = experience;
            this.name = name;
            this.objIDs = objIDs;
        }
    }

    private void attempt(Player player, Tree tree, GameObject object, int replacementID) {
        for (Iterator<Player> playerIterator = player.getLocalPlayers().iterator(); playerIterator.hasNext(); ) {
            Player otherPlayer = playerIterator.next();
            if (player.getHostAddress().equalsIgnoreCase(otherPlayer.getHostAddress())) {
                player.message("You can't use alt on same ip with this tree.");
                return;
            }
        }


        player.faceObj(object);
        if (!player.skills().check(Skills.THIEVING, tree.levelReq, "cutting from the " + tree.name))
            return;
        if (player.inventory().isFull()) {
            player.sound(2277);
            DialogueManager.sendStatement(player, "Your inventory is too full to hold any more.");
            return;
        }


        Chain.bound(player).repeatingTask(5, t -> {

            player.unlock();
            player.animate(7264);
            player.getInventory().add(new Item(AFK_TICKET, 1), true);
            player.skills().addXp(Skills.WOODCUTTING, tree.experience, true);
            player.unlock();
        });
    }


    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        if (option == 1 || option == 2) {
            for (Tree tree : Tree.values()) {
                for (int[] ids : tree.objIDs) {
                    if (object.getId() == ids[0]) {
                        attempt(player, tree, object, ids[0]);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
