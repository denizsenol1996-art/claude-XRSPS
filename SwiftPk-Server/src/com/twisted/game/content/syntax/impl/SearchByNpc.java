package com.twisted.game.content.syntax.impl;

import com.twisted.fs.NpcDefinition;
import com.twisted.game.content.DropsDisplay;
import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * juni 22, 2020
 */
public class SearchByNpc implements EnterSyntax {

    @Override
    public void handleSyntax(Player player, String input) {
        DropsDisplay.search(player, input, DropsDisplay.Type.NPC);
        for (int i = 0; i < World.getWorld().definitions().total(NpcDefinition.class); i++) {
            NpcDefinition npcDefinition = World.getWorld().definitions().get(NpcDefinition.class, i);
            if (npcDefinition.name != null && npcDefinition.name.equalsIgnoreCase(input)) {
                if (DropsDisplay.display(player, i)) {
                    DropsDisplay.open(player, i);
                    return;
                }
                break;
            }
        }
    }

    @Override
    public void handleSyntax(Player player, long input) {

    }
}
