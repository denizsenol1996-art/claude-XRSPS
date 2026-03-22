package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.GameServer;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.IronMode;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.items.Item;
import org.apache.commons.lang3.StringUtils;

public class ItemSpawnCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (player.ironMode() != IronMode.NONE && !player.getPlayerRights().isCmAndManger(player)&& !player.getPlayerRights().isDeveloperOrGreater(player)) {
            player.message("As an ironman you cannot use this command.");
            return;
        }

        int amount = 1;
        if (parts.length < 1 || (!StringUtils.isNumeric(parts[1]) || (parts.length > 2 && !StringUtils.isNumeric(parts[2])))) {
            player.message("Invalid syntax. Please use: ::item [ID] (amount)");
            player.message("Example: ::item 385 or ::item 385 20");
            return;
        }
        if (parts.length > 2) {
            amount = Integer.parseInt(parts[2]);
        }
        int id = Integer.parseInt(parts[1]);

        Item item = new Item(id);

        if(item.getId() > 34_000) {
            player.message("Item id not supported, this item doesn't exist.");
            return;
        }

        if (!player.canSpawn() && !player.getPlayerRights().isCmAndManger(player) && !player.getPlayerRights().isDeveloperOrGreater(player)) {
            player.message("You can't spawn items here.");
            return;
        }

        if (Item.valid(item) && (player.getPlayerRights().isDeveloperOrGreater(player) || (player.getPlayerRights().isCmAndManger(player) || GameServer.properties().test || item.definition(World.getWorld()).pvpAllowed))) {
            player.getInventory().add(new Item(id, amount));
            player.message("You have just spawned x"+amount+" "+new Item(Integer.parseInt(parts[1])).unnote().name()+".");
        }
    }//update5/2

    @Override
    public boolean canUse(Player player) {
        if (!GameServer.properties().pvpMode) {
            return (player.getPlayerRights().isDeveloperOrGreater(player) || player.getUsername().equalsIgnoreCase("Joyner") || player.getPlayerRights().isCmAndManger(player));
        }
        return true;
    }
}
