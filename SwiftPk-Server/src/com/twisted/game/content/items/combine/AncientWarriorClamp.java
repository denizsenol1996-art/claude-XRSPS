package com.twisted.game.content.items.combine;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;

import static com.twisted.util.CustomItemIdentifiers.*;

/**
 * @author Patrick van Elderen | July, 12, 2021, 21:08
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class AncientWarriorClamp extends PacketInteraction {

    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        if ((use.getId() == ANCIENT_WARRIOR_AXE || usedWith.getId() == ANCIENT_WARRIOR_AXE) && (use.getId() == ANCIENT_WARRIOR_CLAMP || usedWith.getId() == ANCIENT_WARRIOR_CLAMP)) {
            player.inventory().remove(new Item(ANCIENT_WARRIOR_AXE), true);
            player.inventory().remove(new Item(ANCIENT_WARRIOR_CLAMP), true);
            player.inventory().add(new Item(ANCIENT_WARRIOR_AXE_C), true);
            return true;
        }
        if ((use.getId() == ANCIENT_WARRIOR_MAUL || usedWith.getId() == ANCIENT_WARRIOR_MAUL) && (use.getId() == ANCIENT_WARRIOR_CLAMP || usedWith.getId() == ANCIENT_WARRIOR_CLAMP)) {
            player.inventory().remove(new Item(ANCIENT_WARRIOR_MAUL), true);
            player.inventory().remove(new Item(ANCIENT_WARRIOR_CLAMP), true);
            player.inventory().add(new Item(ANCIENT_WARRIOR_MAUL_C), true);
            return true;
        }
        if ((use.getId() == ANCIENT_WARRIOR_SWORD || usedWith.getId() == ANCIENT_WARRIOR_SWORD) && (use.getId() == ANCIENT_WARRIOR_CLAMP || usedWith.getId() == ANCIENT_WARRIOR_CLAMP)) {
            player.inventory().remove(new Item(ANCIENT_WARRIOR_SWORD), true);
            player.inventory().remove(new Item(ANCIENT_WARRIOR_CLAMP), true);
            player.inventory().add(new Item(ANCIENT_WARRIOR_SWORD_C), true);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(item.getId() == ANCIENT_WARRIOR_AXE_C) {
            player.confirmDialogue(new String[]{"Are you sure you wish to dismantle the Ancient Warrior Axe (c)?", "You will lose the Ancient Warrior Axe."}, "", "Proceed.", "Nevermind.", () -> {
                if(!player.inventory().contains(ANCIENT_WARRIOR_AXE_C)) {
                    return;
                }
                player.inventory().remove(new Item(ANCIENT_WARRIOR_AXE_C), true);
                player.inventory().addOrBank(new Item(ANCIENT_WARRIOR_CLAMP));
            });
            return true;
        }
        if(item.getId() == ANCIENT_WARRIOR_MAUL_C) {
            player.confirmDialogue(new String[]{"Are you sure you wish to dismantle the Ancient Warrior Maul (c)?", "You will lose the Ancient Warrior Maul."}, "", "Proceed.", "Nevermind.", () -> {
                if(!player.inventory().contains(ANCIENT_WARRIOR_MAUL_C)) {
                    return;
                }
                player.inventory().remove(new Item(ANCIENT_WARRIOR_MAUL_C), true);
                player.inventory().addOrBank(new Item(ANCIENT_WARRIOR_CLAMP));
            });
            return true;
        }
        if(item.getId() == ANCIENT_WARRIOR_SWORD_C) {
            player.confirmDialogue(new String[]{"Are you sure you wish to dismantle the Ancient Warrior Sword (c)?", "You will lose the Ancient Warrior Sword."}, "", "Proceed.", "Nevermind.", () -> {
                if(!player.inventory().contains(ANCIENT_WARRIOR_SWORD_C)) {
                    return;
                }
                player.inventory().remove(new Item(ANCIENT_WARRIOR_SWORD_C), true);
                player.inventory().addOrBank(new Item(ANCIENT_WARRIOR_CLAMP));
            });
            return true;
        }
        return false;
    }
}
