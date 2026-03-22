package com.twisted.game.content.items;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.dialogue.DialogueManager;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * mei 02, 2020
 */
public class ChristmasCrackers extends PacketInteraction {

    private static final ArrayList<Item> normal_partyhat = new ArrayList<Item>(Arrays.asList(new Item(RED_PARTYHAT, 1), new Item(GREEN_PARTYHAT, 1), new Item(WHITE_PARTYHAT, 1), new Item(YELLOW_PARTYHAT, 1), new Item(BLUE_PARTYHAT, 1), new Item(PURPLE_PARTYHAT, 1)));
    private static final ArrayList<Item> rare_partyhat = new ArrayList<Item>(Arrays.asList(new Item(RAINBOW_PARTYHAT, 1), new Item(BLACK_PARTYHAT, 1), new Item(PARTYHAT__SPECS, 1)));

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(option == 1) {
            if(item.getId() == CHRISTMAS_CRACKER) {
                Item party_hat_reward;

                if(World.getWorld().rollDie(100, 10)) {
                    party_hat_reward = Utils.randomElement(rare_partyhat);
                } else {
                    party_hat_reward = Utils.randomElement(normal_partyhat);
                }

                if (player.inventory().getFreeSlots() < 1) {
                    DialogueManager.sendStatement(player, "You require an extra inventory slot to do this.");
                } else if (player.inventory().contains(CHRISTMAS_CRACKER)) {
                    player.message("You pull a Christmas cracker...");
                    //TODO correct anim and gfx
                    //player.animate(451);
                    //player.graphic(176, 82, 0);
                    if (player.inventory().remove(new Item(CHRISTMAS_CRACKER,1),true)) {
                        player.inventory().add(party_hat_reward,true);
                    }
                }
                return true;
            }
        }
        return false;
    }
}
