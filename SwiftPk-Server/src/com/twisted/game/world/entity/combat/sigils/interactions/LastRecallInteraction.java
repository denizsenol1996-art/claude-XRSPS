package com.twisted.game.world.entity.combat.sigils.interactions;


import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Color;
import com.twisted.util.chainedwork.Chain;

public class LastRecallInteraction extends PacketInteraction { //TODO remember to make conditions if in gwd etc
    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if (option == 1) {
            if (item.getId() == 26143) {
                if (player.getLastSavedTile() != null) {
                    player.setUsingLastRecall(true);
                    player.lock();
                    player.graphic(1990, 0, 0);
                    player.animate(9147);
                    Chain.noCtx().runFn(4, () -> {
                        player.teleport(player.getLastSavedTile());
                        player.animate(9149);
                    }).then(1, () -> {
                        player.setUsingLastRecall(false);
                        player.unlock();
                    });
                    return true;
                } else {
                    player.message(Color.BLUE.wrap("You do not currently have a saved location."));
                    return true;
                }
            }
        }
        return false;
    }
}
