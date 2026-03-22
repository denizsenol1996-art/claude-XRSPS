package com.twisted.game.world.entity.mob.player.commands.impl.owner;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.entity.mob.player.rights.MemberRights;
import com.twisted.util.Utils;

import java.util.Arrays;
import java.util.Optional;

public class SetMemberRightsCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        //We probably don't need parts, that's what parts should be.
        if (parts.length != 3) {
            player.message("The correct format is '::setmember name rights'. Not "+ Arrays.toString(parts));
            player.message("Example '::setmember t_e_s_t 1'. ");
            return;
        }

        String name = parts[1].replace("_", " ");

        Optional<Player> other = World.getWorld().getPlayerByName(name);
        int rightValue;
        try {
            rightValue = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            player.message("The level of rights must be a whole number. "+parts[2]);
            return;
        }
        MemberRights right = MemberRights.get(rightValue);
        if (right == null) {
            player.message("The level of rights you've requested is unknown.");
            return;
        }
        if (other.isPresent()) {
            other.get().setMemberRights(right);
            other.get().getPacketSender().sendRights();
            player.message("You have made " + name + " a " + Utils.capitalizeJustFirst(right.getName()) + ".");
            other.get().message("You have been made a " + Utils.capitalizeJustFirst(right.getName()) + ".");
        }
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isOwner(player));
    }

}
