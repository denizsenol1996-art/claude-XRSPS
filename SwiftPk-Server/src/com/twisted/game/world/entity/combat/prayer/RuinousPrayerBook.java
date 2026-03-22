package com.twisted.game.world.entity.combat.prayer;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.prayer.default_prayer.DefaultPrayerData;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;

public class RuinousPrayerBook extends PacketInteraction {
    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if (option == 1) {
            if (item.getId() == 30305) {
                for (int i = 0; i < DefaultPrayerData.values().length; i++) {
                    Prayers.deactivatePrayer(player, i);
                }
                if (!player.hasAttrib(AttributeKey.RUINOUS_PRAYERS)) {
                    player.putAttrib(AttributeKey.RUINOUS_PRAYERS, true);
                    player.getPacketSender().updateTab(1, 5);
                    player.getInterfaceManager().setSidebar(5, 24493);
                } else {
                    player.clearAttrib(AttributeKey.RUINOUS_PRAYERS);
                    player.getPacketSender().updateTab(0, 5);
                    player.getInterfaceManager().setSidebar(5, 5608);
                }
                return true;
            }
        }
        return false;
    }
}
