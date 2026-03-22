package com.twisted.game.content.seasonal_events.christmas;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.util.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.twisted.util.ItemIdentifiers.GIANT_PRESENT;
import static com.twisted.util.ItemIdentifiers.SACK_OF_PRESENTS;

/**
 * Sack of presents carbon copy of Niffler.
 */
public class SackOfPresents {

    private final Player player;

    public SackOfPresents(Player player) {
        this.player = player;
    }

    public int size() {
        return player.<ArrayList<Item>>getAttribOr(AttributeKey.SACK_OF_PRESENTS_LIST, new ArrayList<Item>()).size();
    }

    /**
     * Sack size, can only hold 10 items.
     */
    private static final int SIZE = 10;

    /**
     * Picks up only giant presents from the ground
     * @param source The giant present instance
     * @param target The target instance
     */
    public boolean pickup(Item source, Mob target) {
        //Make sure the item is equipped before we can continue looting presents.
        if(!player.getEquipment().contains(SACK_OF_PRESENTS)) {
            return false;
        }

        //Only loot if the target was an npc
        if(target.isNpc()) {
            Npc npc = target.getAsNpc();
            List<Integer> npcs = Arrays.asList(2005);//Write npc ids here

            //Check if the npc we just killed matches any of the npcs in the list
            if(npcs.stream().anyMatch(n -> n == npc.id())) {
                //Check if the sack is already full
                if(size() > SIZE) {
                    player.message(Color.RED.wrap("Your sack of presents is full and cannot store anymore presents."));
                    return false;
                }

                //very important to not mess with item instances
                final Item item = source.copy();

                var allowed = item.getId() == GIANT_PRESENT;

                //Can only loot giant presents
                if(!allowed) {
                    //Empty no message, otherwise SPAM!
                    return false;
                }

                //Get the current stored item list
                var currentList = player.<ArrayList<Item>>getAttribOr(AttributeKey.SACK_OF_PRESENTS_LIST, new ArrayList<Item>());

                if (currentList != null) {
                    //Store the items in its respective attribute
                    Optional<Item> any = currentList.stream().filter(i -> i.matchesId(item.getId())).findAny();
                    if (any.isPresent() && ((1L * any.get().getAmount()) + item.getAmount() <= Integer.MAX_VALUE)) { //Updates existing item instances
                        var newAmt = any.get().getAmount() + item.getAmount();
                        any.get().setAmount(newAmt);
                    } else {
                        currentList.add(item);
                    }

                    //Update the attribute
                    player.putAttrib(AttributeKey.SACK_OF_PRESENTS_LIST, currentList);
                    player.message(Color.BLUE.wrap("Your sack of presents collected a Giant present."));
                }
            }
        }
        return true;
    }

    /**
     * Clears the items from the sack.
     */
    public void clear() {
        var items = player.<ArrayList<Item>>getAttribOr(AttributeKey.SACK_OF_PRESENTS_LIST, new ArrayList<Item>());
        if(items.isEmpty()) {
            player.message("Your sack is empty.");
            return;
        }
        //Sent items to inventory or bank
        player.inventory().addOrBank(items);
        //Clear the list
        items.clear();
    }

}
