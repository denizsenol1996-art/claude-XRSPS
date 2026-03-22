package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.entity.combat.bountyhunter.BountyHunter;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.ItemIdentifiers;
import com.twisted.util.SecondsTimer;

public class SkipTargetCommand implements Command {

    private final SecondsTimer targetskip_delay = new SecondsTimer();


    @Override
    public void execute(Player player, String command, String[] parts) {

        int amount = 10000;
        if (!WildernessArea.inWilderness(player.tile())) {
            player.message("You must be in Wilderness to use this command!");
            return;
        }
        if (targetskip_delay.active()) {
            int seconds = targetskip_delay.secondsRemaining();
            player.message("You must wait another " + (seconds == 1 ? "second" : "" + seconds + " seconds") + " before skipping your target.");
            return;
        }
        if (player.getInventory().contains(ItemIdentifiers.BLOOD_MONEY, amount)) {
            targetskip_delay.start(30);
            BountyHunter.unassign(player);
            player.getInventory().remove(new Item(ItemIdentifiers.BLOOD_MONEY, amount), true);
            return;


        } else {
            player.message("You need " + amount + "k to skip your target or you dont have any target.");

        }
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
