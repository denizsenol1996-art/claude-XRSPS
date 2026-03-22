package com.twisted.game.content.gambling;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.packet.interaction.PacketInteraction;

public class GambleInterface extends PacketInteraction {

    @Override
    public boolean handleButtonInteraction(Player player, int button) {
        return player.getGamblingSession() != null && player.getGamblingSession().handleButton(button);
    }
}
