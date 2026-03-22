package com.twisted.game.world.entity.mob.player.commands.impl.member;

import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.Color;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * mei 21, 2020
 */
public class SpecCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if(!player.getPlayerRights().isDeveloperOrGreater(player) && ((!player.tile().inArea(Tile.EDGEVILE_HOME_AREA) || WildernessArea.inWilderness(player.tile())))) {
            player.message("<col="+ Color.RED.getColorValue()+">You can only restore your special attack at home.");
            return;
        }

        if (!player.getPlayerRights().isDeveloperOrGreater(player) && CombatFactory.inCombat(player)) {
            player.message("You cannot use this command during combat.");
            return;
        }

        boolean legendaryMember = player.getMemberRights().isDragonstoneOrGreater(player);
        if(!legendaryMember && !player.getPlayerRights().isDeveloperOrGreater(player)) {
            player.message("You need to be at least a Dragonstone member to use this command.");
            return;
        }

        int amt = 100;
        if (parts.length > 1 && player.getPlayerRights().isDeveloperOrGreater(player)) {
            amt = Integer.parseInt(parts[1]);
        }
        player.setSpecialAttackPercentage(amt);
        player.setSpecialActivated(false);
        CombatSpecial.updateBar(player);
        player.message("<col="+ Color.HOTPINK.getColorValue()+">Special energy has been restored to full.");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
