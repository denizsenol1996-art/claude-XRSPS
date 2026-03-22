package com.twisted.game.world.entity.mob.player.commands.impl.staff.moderator;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.PlayerStatus;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.position.areas.impl.WildernessArea;

import java.util.Optional;

public class TeleToMePlayerCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (command.length() <= 9)
            return;
        Optional<Player> plr = World.getWorld().getPlayerByName(command.substring(parts[0].length() + 1));
        if (plr.isPresent()) {
            if(plr.get().getStatus() == PlayerStatus.TRADING && !player.getPlayerRights().isDeveloperOrGreater(player)) {
                player.message(plr.get().getUsername()+" is in a active trade, you cannot teleport to "+plr.get().getUsername()+".");
                return;
            }
            if(plr.get().getStatus() == PlayerStatus.DUELING && !player.getPlayerRights().isDeveloperOrGreater(player)) {
                player.message(plr.get().getUsername()+" is in a active duel, you cannot teleport to "+plr.get().getUsername()+".");
                return;
            }
            if(plr.get().getStatus() == PlayerStatus.GAMBLING && !player.getPlayerRights().isDeveloperOrGreater(player)) {
                player.message(plr.get().getUsername()+" is in a active gamble, you cannot teleport to "+plr.get().getUsername()+".");
                return;
            }
            if(WildernessArea.inWilderness(plr.get().tile()) && CombatFactory.inCombat(plr.get()) && !player.getPlayerRights().isDeveloperOrGreater(player)) {
                player.message(plr.get().getUsername()+" is in the wilderness and in combat, you cannot teleport to "+plr.get().getUsername()+".");
                return;
            }
            var in_tournament = plr.get().inActiveTournament() || plr.get().isInTournamentLobby();
            if(in_tournament && !player.getPlayerRights().isDeveloperOrGreater(player)) {
                player.message(plr.get().getUsername()+" is in an active tournament, you cannot teleport to "+plr.get().getUsername()+".");
                return;
            }
            plr.get().teleport(player.tile().clone());
        }
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isModeratorOrGreater(player) || player.getPlayerRights().isCmAndManger(player));//update5/2
    }

}
