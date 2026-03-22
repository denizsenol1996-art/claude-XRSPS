package com.twisted.game.world.entity.mob.player.commands.impl.member;

import com.twisted.game.content.teleport.TeleportType;
import com.twisted.game.content.teleport.Teleports;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.impl.WildernessArea;

public class DCaveCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        double totalAmountPaid = player.getAttribOr(AttributeKey.TOTAL_PAYMENT_AMOUNT, 0D);

        if (WildernessArea.inWilderness(player.tile()) && !player.getPlayerRights().isDeveloperOrGreater(player)) {
            player.message("You can't use this command in the wilderness.");
            return;
        }
        if(totalAmountPaid < 10){
            player.message("You need to be a donator rank to use this command.");
            return;
        }
        if(player.isInTournamentLobby() || player.inActiveTournament()) {
            player.message("You can't use this command in a tournament");
            return;
        }

        Tile tile = new Tile(2335, 9795);

        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC) || !Teleports.pkTeleportOk(player, tile)) {
            return;
        }

        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.STATEMENT, "This teleport will send you to a dangerous area.", "Do you wish to continue?");
                setPhase(1);
            }

            @Override
            protected void next() {
                if (isPhase(1)) {
                    send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Yes.", "No.");
                    setPhase(2);
                }
            }

            @Override
            protected void select(int option) {
                if (option == 1) {
                    if (!Teleports.canTeleport(player, true, TeleportType.GENERIC)) {
                        stop();
                        return;
                    }
                    Teleports.basicTeleport(player, tile);
                } else if (option == 2) {
                    stop();
                }
            }
        });
    }

    @Override
    public boolean canUse(Player player) {
        return player.getMemberRights().isRubyOrGreater(player) || player.getPlayerRights().isStaffMember(player);
    }
}
