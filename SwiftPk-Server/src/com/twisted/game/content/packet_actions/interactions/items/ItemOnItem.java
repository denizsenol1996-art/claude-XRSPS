package com.twisted.game.content.packet_actions.interactions.items;

import com.twisted.game.content.collection_logs.listener.CollectionLogHandler;
import com.twisted.game.content.consumables.potions.Potions;
import com.twisted.game.content.items.combine.*;
import com.twisted.game.content.skill.impl.firemaking.LogLighting;
import com.twisted.game.content.skill.impl.herblore.HerbTar;
import com.twisted.game.content.skill.impl.herblore.PestleAndMortar;
import com.twisted.game.content.skill.impl.herblore.PotionBrewing;
import com.twisted.game.content.skill.impl.herblore.SuperCombatPotions;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.npc.pets.PetPaint;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteractionManager;
import com.twisted.util.CustomItemIdentifiers;
import org.apache.commons.lang.ArrayUtils;

import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * juni 15, 2020
 */
public class ItemOnItem {

    public static final int[] anyBox = new int[]{CustomItemIdentifiers.PRESENT_MYSTERY_BOX, CustomItemIdentifiers.ARMOUR_MYSTERY_BOX, CustomItemIdentifiers.DONATOR_MYSTERY_BOX, CustomItemIdentifiers.VOTEBOSS_MYSTERY_BOX, CustomItemIdentifiers.MOLTEN_MYSTERY_BOX,CustomItemIdentifiers.PET_MYSTERY_BOX, CustomItemIdentifiers.HWEEN_MYSTERY_BOX, CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX, CustomItemIdentifiers.WEAPON_MYSTERY_BOX};
    public static final int[] grandKeyEpicPetBox = new int[]{CustomItemIdentifiers.GRAND_KEY, CustomItemIdentifiers.EPIC_PET_BOX};
    public static final int[] grandKeyMysteryChest = new int[]{CustomItemIdentifiers.GRAND_KEY, CustomItemIdentifiers.MYSTERY_CHEST};
    public static final int[] grandKeyGrandBox = new int[]{CustomItemIdentifiers.GRAND_KEY, CustomItemIdentifiers.GRAND_MYSTERY_BOX};

    public static int slotOf(Player player, int item) {

        Item from = player.getAttrib(AttributeKey.FROM_ITEM);
        Item to = player.getAttrib(AttributeKey.TO_ITEM);
        if (from == null || to == null)
            return -1;

        if (from.getId() == item)
            return player.getAttrib(AttributeKey.ITEM_SLOT);
        if (to.getId() == item)
            return player.getAttrib(AttributeKey.ALT_ITEM_SLOT);

        return -1;
    }

    public static void itemOnItem(Player player, Item use, Item with) {
        if (PacketInteractionManager.checkItemOnItemInteraction(player, use, with)) {
            return;
        }

        if(player.getMysteryBox().onItemonItem(use, with)) {
            return;
        }

        if(PetPaint.paintPet(player, use, with)) {
            return;
        }

        if(LogLighting.onItemOnItem(player, use, with)) {
            return;
        }
        if(PotionBrewing.onItemOnItem(player, use, with)) {
            return;
        }

        if(PestleAndMortar.onItemOnItem(player, use, with)) {
            return;
        }
        if(HerbTar.onItemOnItem(player, use, with)) {
            return;
        }
        if(Potions.onItemOnItem(player, use, with)) {
            return;
        }
        if(SuperCombatPotions.makePotion(player, use, with)) {
            return;
        }

        if(player.getRunePouch().itemOnItem(use, with)) {
            return;
        }

        if (player.getLootingBag().itemOnItem(use, with)) {
            return;
        }

        if (ArrayUtils.contains(grandKeyEpicPetBox, use.getId())) {
            if (ArrayUtils.contains(grandKeyEpicPetBox, with.getId())) {
                CollectionLogHandler.rollBoxReward(player, grandKeyEpicPetBox[1], true, false);
                return;
            }
        }

        if (ArrayUtils.contains(grandKeyMysteryChest, use.getId())) {
            if (ArrayUtils.contains(grandKeyMysteryChest, with.getId())) {
                CollectionLogHandler.rollBoxReward(player, grandKeyMysteryChest[1], true, false);
                return;
            }
        }

        if (use.getId() == CustomItemIdentifiers.KEY_OF_BOXES) {
            if (ArrayUtils.contains(anyBox, with.getId())) {
                for (var i : anyBox) {
                    if (i == with.getId()) {
                        CollectionLogHandler.rollBoxReward(player, i, false, true);
                        return;
                    }
                }
            }
        }

        if ((use.getId() == VOLATILE_ORB || with.getId() == VOLATILE_ORB) && (use.getId() == NIGHTMARE_STAFF || with.getId() == NIGHTMARE_STAFF)) {
            player.getDialogueManager().start(new VolatileNightmareStaff());
            return;
        }

        if ((use.getId() == ELDRITCH_ORB || with.getId() == ELDRITCH_ORB) && (use.getId() == NIGHTMARE_STAFF || with.getId() == NIGHTMARE_STAFF)) {
            player.getDialogueManager().start(new EldritchNightmareStaff());
            return;
        }

        if ((use.getId() == HARMONISED_ORB || with.getId() == HARMONISED_ORB) && (use.getId() == NIGHTMARE_STAFF || with.getId() == NIGHTMARE_STAFF)) {
            player.getDialogueManager().start(new HarmonisedNightmareStaff());
            return;
        }

        player.message("Nothing interesting happens.");
    }
}
