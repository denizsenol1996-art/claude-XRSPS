package com.twisted.game.content.areas.wilderness.content.larrans_key;

import com.twisted.game.content.collection_logs.listener.CollectionLogHandler;
import com.twisted.game.world.entity.combat.skull.SkullType;
import com.twisted.game.world.entity.combat.skull.Skulling;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.net.packet.interaction.PacketInteraction;

import static com.twisted.util.CustomItemIdentifiers.*;

public class EnchantedChestP extends PacketInteraction {
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if (obj.getId() == 4125) {
            if(!WildernessArea.inDiamondZone(player.tile()) && player.inventory().contains(ENCHANTED_KEY_II)){
                player.teleblock( 500, true);
                Skulling.assignSkullState(player, SkullType.RED_SKULL);
            }
            if (player.hasPetOut("Deranged archaeologist")) {
                Skulling.assignSkullState(player, SkullType.NO_SKULL);
            }
            if (player.inventory().contains(ENCHANTED_KEY_II)) {
                if (CollectionLogHandler.rollKeyReward(player, ENCHANTED_KEY_II, null, -1, -1)) {
                    return true;
                }
            } else {
                player.message("This enchanted chest wont budge, I think I need to find a key that fits.");
            }
            return true;
        }
        return false;
    }
}

