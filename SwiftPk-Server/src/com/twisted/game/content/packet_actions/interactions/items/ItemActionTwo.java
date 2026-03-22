package com.twisted.game.content.packet_actions.interactions.items;

import com.twisted.game.content.consumables.potions.Potions;
import com.twisted.game.content.items.combine.EldritchNightmareStaff;
import com.twisted.game.content.items.combine.HarmonisedNightmareStaff;
import com.twisted.game.content.items.combine.VolatileNightmareStaff;
import com.twisted.game.content.items.teleport.ArdyCape;
import com.twisted.game.content.skill.impl.slayer.content.SlayerRing;
import com.twisted.game.world.entity.mob.npc.pets.PetPaint;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteractionManager;

import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * mei 08, 2020
 */
public class ItemActionTwo {

    public static void click(Player player, Item item) {
        int id = item.getId();

        PetPaint.wipePaint(player, item);
        ArdyCape.onItemOption2(player, item);

        if (PacketInteractionManager.checkItemInteraction(player, item, 2)) {
            return;
        }

        if (player.getMysteryBox().open(item)) {
            return;
        }

        if(Potions.onItemOption2(player, item)) {
            return;
        }

        if (VolatileNightmareStaff.dismantle(player, item)) {
            return;
        }

        if (EldritchNightmareStaff.dismantle(player, item)) {
            return;
        }

        if (HarmonisedNightmareStaff.dismantle(player, item)) {
            return;
        }

        if(SlayerRing.onItemOption2(player, item)) {
            return;
        }

        switch (id) {
            case RUNE_POUCH, RUNE_POUCH_I -> player.getRunePouch().empty();
            case LOOTING_BAG, LOOTING_BAG_22586, 30099, 30098 -> player.getLootingBag().setSettings();
        }
    }
}
