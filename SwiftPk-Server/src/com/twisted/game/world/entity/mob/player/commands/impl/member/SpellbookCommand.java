package com.twisted.game.world.entity.mob.player.commands.impl.member;

import com.twisted.game.world.entity.mob.player.MagicSpellbook;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.position.areas.impl.WildernessArea;

public class SpellbookCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        // Known exploit
        if (command.contains("\r") || command.contains("\n")) {
            return;
        }
        if (parts.length < 2) {
            player.message("Spellbook usage: ::spellbook 0-2 or ::spellbook modern, ancient, lunar");
            return;
        }

        if(WildernessArea.inWilderness(player.tile())) {
            player.message("You can't use this command here.");
            return;
        }

        MagicSpellbook book = switch (parts[1].toLowerCase()) {
            case "0", "normal", "regular", "modern" -> MagicSpellbook.NORMAL;
            case "1", "ancients", "ancient" -> MagicSpellbook.ANCIENT;
            case "2", "lunar" -> MagicSpellbook.LUNAR;
            default -> player.getSpellbook();
        };
        MagicSpellbook.changeSpellbook(player, book, true);
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getMemberRights().isEmeraldOrGreater(player));
    }
}
