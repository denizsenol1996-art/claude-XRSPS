package com.twisted.game.content.teleport.royal_seed_pot;

import com.twisted.GameServer;
import com.twisted.game.content.teleport.TeleportType;
import com.twisted.game.content.teleport.Teleports;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.ItemIdentifiers;
import com.twisted.util.chainedwork.Chain;
import com.twisted.util.timers.TimerKey;

public class RoyalSeedPot extends PacketInteraction {

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(option == 1) {
            if(item.getId() == ItemIdentifiers.ROYAL_SEED_POD) {
                player.stopActions(true);
                if (!Teleports.canTeleport(player, true, TeleportType.ABOVE_20_WILD))
                    return true;
                player.graphic(767);
                player.animate(4544);
                player.lockNoDamage();
                Chain.bound(null).runFn(3, () -> player.looks().transmog(716)).then(1, () -> player.teleport(GameServer.properties().defaultTile)).then(2, () -> player.graphic(769)).then(2, () -> {
                    player.looks().transmog(-1);
                    player.animate(-1);
                    player.getTimerRepository().cancel(TimerKey.FROZEN);
                    player.getTimerRepository().cancel(TimerKey.REFREEZE);
                    player.unlock();
                });
                return true;
            }
        }
        return false;
    }

}
