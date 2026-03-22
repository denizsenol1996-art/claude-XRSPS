package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.db.ReferralSystem;
import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import org.jetbrains.annotations.NotNull;

public class ReferralCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.setEnterSyntax(new EnterSyntax() {
            @Override
            public void handleSyntax(Player player, @NotNull String input) {
                ReferralSystem.claim(player, input);
            }
        });
        player.getPacketSender().sendEnterInputPrompt("Claim Your Youtuber Referral Code");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
