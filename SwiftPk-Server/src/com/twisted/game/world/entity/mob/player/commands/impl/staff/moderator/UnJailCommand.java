package com.twisted.game.world.entity.mob.player.commands.impl.staff.moderator;

import com.twisted.game.content.teleport.Teleports;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Utils;

import java.util.Optional;

/**
 * @author Patrick van Elderen | April, 01, 2021, 16:35
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class UnJailCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        String opname = Utils.formatText(command.substring(7)); // after "unjail "

        Optional<Player> otherp = World.getWorld().getPlayerByName(opname);

        if (otherp.isPresent()) {
            Player other = otherp.get();
            if (other.jailed()) {
                other.stopActions(true);
                other.putAttrib(AttributeKey.JAILED, 0);
                Tile end = other.getAttribOr(AttributeKey.LOC_BEFORE_JAIL, new Tile(3092, 3500));
                World.getWorld().tileGraphic(110, other.tile(), 110, 0);
                Teleports.basicTeleport(other, end);
                other.putAttrib(AttributeKey.JAIL_ORES_MINED, 0);
                for (Item i : other.inventory().getItems()) { // Clear blurite ores.
                    if (i == null) continue;
                    if (i.getId() == 668) {
                        other.inventory().remove(i, true);
                    }
                }
                player.message("Player " + opname + " ("+other.getUsername()+") has been unjailed.");
                Utils.sendDiscordInfoLog(player, player.getUsername() + " has unjailed " + other.getUsername(), "sanctions");
            } else {
                player.message(opname + " is not jailed.");
            }
        } else {
            player.message("<col=FF0000>" + opname + "</col> does not exist or is not online.");
        }
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isSupportOrGreater(player));
    }

}
