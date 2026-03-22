package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.GameServer;
import com.twisted.fs.NpcDefinition;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.util.Utils;

/**
 * @author Patrick van Elderen | Zerikoth | PVE
 * @date februari 09, 2020 11:29
 */
public class MassKillNpc implements Command {
    /*
     * WARNING: THIS COMMAND CAN BREAK NPC UPDATING, IT TAKES A REALLY LONG TIME TO REMOVE THE NPCS FROM THE LIST. IT CAN TAKE UP TO 5 MINUTES TO REMOVE ALL THE NPCS.
     * USE THIS COMMAND WITH EXTREME CAUTION.
     * THIS COMMAND SHOULD PROBABLY NOT BE USED IN PRODUCTION.
     */
    @Override
    public void execute(Player player, String command, String[] parts) {
        if (parts.length != 3) {
            player.message("Invalid use of command.");
            player.message("Use: ::mkn 100 20");
            return;
        }
        if (GameServer.properties().production) {
            player.message("This command is only for testing purposes because of it's \"destructive\" nature.");
            return;
        }
        int npcId = Integer.parseInt(parts[1]);
        int amount = Integer.parseInt(parts[2]);
        int maxAmount = 5000;
        if (amount > maxAmount || (World.getWorld().getNpcs().size() + maxAmount > World.getWorld().getNpcs().capacity())) {
            player.message("You can only mass kill " + Utils.formatRunescapeStyle(maxAmount) + " NPCs at a time");
            return;
        }
        for(int i = 0; i < amount; i++) {
            Npc npc = new Npc(npcId, player.tile().clone().add(1, 0));
            //We want to check that it was added successfully, since add returns a boolean.
            if (!World.getWorld().registerNpc(npc)) {
                break;
            }
            npc.respawns(false);
            npc.hit(player, npc.hp(), CombatType.MELEE).submit();
        }
        NpcDefinition def = World.getWorld().definitions().get(NpcDefinition.class, npcId);
        player.message("You have killed "+amount+" "+ def.name+".");
        player.message("Drops sent to bank.");
        player.message("Please log out and back in for drops to appear normally.");
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }
}
