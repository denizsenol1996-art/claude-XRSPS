package com.twisted.game.content.items;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;

import static com.twisted.util.CustomItemIdentifiers.IMBUEMENT_SCROLL;
import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen | December, 31, 2020, 18:53
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class ImbueScroll extends PacketInteraction {

    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        if ((use.getId() == IMBUEMENT_SCROLL || usedWith.getId() == IMBUEMENT_SCROLL) && (use.getId() == ARCHERS_RING || usedWith.getId() == ARCHERS_RING)) {
            player.inventory().remove(new Item(IMBUEMENT_SCROLL),true);
            player.inventory().remove(new Item(ARCHERS_RING),true);
            player.inventory().add(new Item(ARCHERS_RING_I),true);
            return true;
        }
        if ((use.getId() == IMBUEMENT_SCROLL || usedWith.getId() == IMBUEMENT_SCROLL) && (use.getId() == SEERS_RING || usedWith.getId() == SEERS_RING)) {
            player.inventory().remove(new Item(IMBUEMENT_SCROLL),true);
            player.inventory().remove(new Item(SEERS_RING),true);
            player.inventory().add(new Item(SEERS_RING_I),true);
            return true;
        }
        if ((use.getId() == IMBUEMENT_SCROLL || usedWith.getId() == IMBUEMENT_SCROLL) && (use.getId() == WARRIOR_RING || usedWith.getId() == WARRIOR_RING)) {
            player.inventory().remove(new Item(IMBUEMENT_SCROLL),true);
            player.inventory().remove(new Item(WARRIOR_RING),true);
            player.inventory().add(new Item(WARRIOR_RING_I),true);
            return true;
        }
        if ((use.getId() == IMBUEMENT_SCROLL || usedWith.getId() == IMBUEMENT_SCROLL) && (use.getId() == BERSERKER_RING || usedWith.getId() == BERSERKER_RING)) {
            player.inventory().remove(new Item(IMBUEMENT_SCROLL),true);
            player.inventory().remove(new Item(BERSERKER_RING),true);
            player.inventory().add(new Item(BERSERKER_RING_I),true);
            return true;
        }
        if ((use.getId() == IMBUEMENT_SCROLL || usedWith.getId() == IMBUEMENT_SCROLL) && (use.getId() == RING_OF_THE_GODS || usedWith.getId() == RING_OF_THE_GODS)) {
            player.inventory().remove(new Item(IMBUEMENT_SCROLL),true);
            player.inventory().remove(new Item(RING_OF_THE_GODS),true);
            player.inventory().add(new Item(RING_OF_THE_GODS_I),true);
            return true;
        }
        if ((use.getId() == IMBUEMENT_SCROLL || usedWith.getId() == IMBUEMENT_SCROLL) && (use.getId() == TYRANNICAL_RING || usedWith.getId() == TYRANNICAL_RING)) {
            player.inventory().remove(new Item(IMBUEMENT_SCROLL),true);
            player.inventory().remove(new Item(TYRANNICAL_RING),true);
            player.inventory().add(new Item(TYRANNICAL_RING_I),true);
            return true;
        }
        if ((use.getId() == IMBUEMENT_SCROLL || usedWith.getId() == IMBUEMENT_SCROLL) && (use.getId() == TREASONOUS_RING || usedWith.getId() == TREASONOUS_RING)) {
            player.inventory().remove(new Item(IMBUEMENT_SCROLL),true);
            player.inventory().remove(new Item(TREASONOUS_RING),true);
            player.inventory().add(new Item(TREASONOUS_RING_I),true);
            return true;
        }
        if ((use.getId() == IMBUEMENT_SCROLL || usedWith.getId() == IMBUEMENT_SCROLL) && (use.getId() == GRANITE_RING || usedWith.getId() == GRANITE_RING)) {
            player.inventory().remove(new Item(IMBUEMENT_SCROLL),true);
            player.inventory().remove(new Item(GRANITE_RING),true);
            player.inventory().add(new Item(GRANITE_RING_I),true);
            return true;
        }
        if ((use.getId() == IMBUEMENT_SCROLL || usedWith.getId() == IMBUEMENT_SCROLL) && (use.getId() == RING_OF_SUFFERING || usedWith.getId() == RING_OF_SUFFERING)) {
            player.inventory().remove(new Item(IMBUEMENT_SCROLL),true);
            player.inventory().remove(new Item(RING_OF_SUFFERING),true);
            player.inventory().add(new Item(RING_OF_SUFFERING_I),true);
            return true;
        }
        return false;
    }
}
