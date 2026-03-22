package com.twisted.game.world.entity.mob.player.commands.impl.staff.admin;

import com.twisted.game.content.areas.wilderness.content.key.EscapeKeyLocation;
import com.twisted.game.content.areas.wilderness.content.key.EscapeKeyPlugin;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EscapeKeyCommand implements Command {
    private static final Logger log = LoggerFactory.getLogger(EscapeKeyCommand.class);

    @Override
    public void execute(Player player, String command, String[] parts) {
        EscapeKeyLocation location = EscapeKeyPlugin.spawnKeys();
        if (location != null) {
            //log.trace("Key location selected: {} (absolute: {}).", location, location.tile());
        } else {
            player.message("There is already an Escape key spawned.");
        }
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isAdminOrGreater(player);
    }
}
