package com.twisted.game.world.entity.mob.player.commands.impl.staff.admin;

import com.twisted.game.GameEngine;
import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OsrsBroadcastCommand implements Command {

private static final Logger logger = LogManager.getLogger(OsrsBroadcastCommand.class);

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (parts.length > 1) {
            player.message("Usage: ::osrsbroadcast");
            return;
        }
        player.getDialogueManager().start(new MessageDialogue());
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isAdminOrGreater(player) || player.getPlayerRights().isEventManager(player));
    }

    public static final class MessageDialogue extends Dialogue {
        @Override
        protected void start(Object... parameters) {
            setPhase(0);
            send(DialogueType.OPTION, "Does this announcement have a URL?", "Yes.", "No.");
        }

        @Override
        public void select(int option) {
            if (option == 1) {
                enterUrl();
            } else if (option == 2) {
                enterText(" ");
            }
        }

        private void enterUrl() {
            player.getInterfaceManager().close();
            player.setEnterSyntax(new EnterSyntax() {
                @Override
                public void handleSyntax(Player player, String input) {
                    //This is a sync task because it needs to be done on the next tick, since inputs cannot be chained, it closes immediately if you try to put an input event inside another one.
                    GameEngine.getInstance().addSyncTask(() -> enterText(input));
                }
            });
            player.getPacketSender().sendEnterInputPrompt("Enter the URL:");
        }

        private void enterText(String url) {
            player.getInterfaceManager().close();
            player.setEnterSyntax(new EnterSyntax() {
                @Override
                public void handleSyntax(Player player, String input) {
                    if (!url.equals(" ")) {
                        logger.info(player.getPlayerRights().getName() + " " + player.getUsername() + " entered the URL: '" + url + "' for the ::osrsbroadcast command.");
                    }
                    World.getWorld().sendWorldMessage("osrsbroadcast##" + input + "%%" + url);
                }
            });
            player.getPacketSender().sendEnterInputPrompt("Enter the message:");
        }

    }

}
