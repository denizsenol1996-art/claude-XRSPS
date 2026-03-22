package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.GameEngine;
import com.twisted.game.content.skill.impl.fishing.Fishing;
import com.twisted.game.world.World;
import com.twisted.game.world.definition.loader.impl.ObjectSpawnDefinitionLoader;
import com.twisted.game.world.definition.loader.impl.BloodMoneyPriceLoader;
import com.twisted.game.world.definition.loader.impl.PresetLoader;
import com.twisted.game.world.definition.loader.impl.ShopLoader;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;

import static java.lang.String.format;

public class ReloadCommand implements Command {

    private static final Logger logger = LogManager.getLogger(ReloadCommand.class);

    @Override
    public void execute(Player player, String command, String[] parts) {
        String reload = parts[1];
        if (reload.equalsIgnoreCase("bm")) {
            player.message("Reloading blood money prices...");
            new BloodMoneyPriceLoader().run();
            player.message("Finished.");
        } else if (reload.equalsIgnoreCase("shops")) {
            player.message("Reloading shops...");
            new ShopLoader().run();
            player.message("Finished.");
        } else if (reload.equalsIgnoreCase("npcs")) {
            player.message("Reloading npcs...");
            GameEngine.getInstance().submitLowPriority(() ->
                GameEngine.getInstance().addSyncTask(() -> {
                    for (Npc worldNpcs : World.getWorld().getNpcs()) {
                        if (worldNpcs == null || worldNpcs.def().ispet) {
                            continue;
                        }
                        World.getWorld().unregisterNpc(worldNpcs);
                    }
                    World.loadNpcSpawns(new File("data/map/npcs"));
                    try {
                        Fishing.respawnAllSpots(World.getWorld());
                    } catch (FileNotFoundException e) {
                        logger.catching(e);
                    }
                    player.message(format("Reloaded %d npcs. <col=ca0d0d>Warning: Npcs in Instances will not be respawned.", World.getWorld().getNpcs().size()));
                    player.message("<col=ca0d0d>Must be done manually.");
                }));
        } else if (reload.equalsIgnoreCase("drops")) {
            player.message("Reloading drops...");
            World.getWorld().loadDrops();
            player.message("Finished.");
        } else if (reload.equalsIgnoreCase("equipinfo")) {
            player.message("Reloading equipment info...");
            World.getWorld().loadEquipmentInfo();
            player.message("Reloaded equip info.");
        } else if (reload.equalsIgnoreCase("combatdefs") || reload.equalsIgnoreCase("npcinfo")) {
            player.message("Reloading npc combat info...");
            World.getWorld().loadNpcCombatInfo();

            // Reload npcs
            World.getWorld().getNpcs().forEach(n -> {
                if (n != null) {
                    n.combatInfo(World.getWorld().combatInfo(n.id()));
                }
            });
            player.message("Finished.");
        } else if (reload.equalsIgnoreCase("objects")) {
            player.message("Reloading objects...");
            //TODO ask Jak why this is broken
            /*World.getWorld().getObjects().forEach(obj -> {
                if(obj != null) {
                    ObjectManager.removeObj(obj);
                }
            });*/
            new ObjectSpawnDefinitionLoader().run();
            player.message("Finished.");
        } else if (reload.equalsIgnoreCase("presets")) {
            player.message("Reloading presets...");
            new PresetLoader().run();
            player.message("Finished.");
        }
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player) || player.getUsername().equalsIgnoreCase("Joyner"));
    }

}
