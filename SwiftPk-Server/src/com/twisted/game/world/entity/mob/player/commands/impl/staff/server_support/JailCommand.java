package com.twisted.game.world.entity.mob.player.commands.impl.staff.server_support;

import com.twisted.game.content.duel.Dueling;
import com.twisted.game.content.mechanics.Poison;
import com.twisted.game.content.teleport.Teleports;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Utils;

import java.util.Optional;

/**
 * @author Patrick van Elderen | April, 01, 2021, 16:23
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class JailCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        String opname = Utils.formatText(command.substring(5)); // after "jail "
        Optional<Player> otherp = World.getWorld().getPlayerByName(opname);

        if (otherp.isPresent()) {
            Player other = otherp.get();

            if (Dueling.in_duel(other)) {
                player.message("You can't jail to someone during a duel.");
            } else if (other.getPlayerRights().greater(player.getPlayerRights())) {
                player.message("You can't jail other staff members.");
            } else {
                other.stopActions(true);
                World.getWorld().tileGraphic(110, other.tile(), 110, 0);
                Teleports.basicTeleport(other, new Tile(3290, 3017));
                other.putAttrib(AttributeKey.JAILED, 1);
                other.putAttrib(AttributeKey.LOC_BEFORE_JAIL, other.tile());
                Poison.cure(other);
                player.message("Player " + opname + " ("+other.getUsername()+") has been jailed.");
                Utils.sendDiscordInfoLog(player, player.getUsername() + " has jailed " + other.getUsername(), "sanctions");
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
