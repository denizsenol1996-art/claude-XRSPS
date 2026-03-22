package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.content.mechanics.Poison;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.combat.Venom;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.util.Utils;

import java.util.Optional;

/**
 * @author Patrick van Elderen | June, 12, 2021, 14:25
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class HealPlayerCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        String username = Utils.formatText(command.substring(11)); // after "healplayer "

        Optional<Player> playerToHeal = World.getWorld().getPlayerByName(username);
        if (playerToHeal.isPresent()) {
            Player p = playerToHeal.get();
            player.message("You have healed "+p.getUsername()+".");
            p.hp(Math.max(player.skills().level(Skills.HITPOINTS), p.skills().xpLevel(Skills.HITPOINTS)), 20); //Set hitpoints to 100%
            p.skills().replenishSkill(5, p.skills().xpLevel(5)); //Set the players prayer level to full
            p.skills().replenishStatsToNorm();
            p.setRunningEnergy(100.0, true);
            Poison.cure(p);
            Venom.cure(2, p);
            p.message("You have been healed by "+player.getUsername()+".");
        }
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isAdminOrGreater(player);
    }
}
