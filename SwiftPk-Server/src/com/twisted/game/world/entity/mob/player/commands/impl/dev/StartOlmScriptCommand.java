package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.content.raids.chamber_of_xeric.great_olm.GreatOlm;
import com.twisted.game.content.raids.party.Party;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.position.Tile;

/**
 * @author Patrick van Elderen | May, 16, 2021, 22:37
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class StartOlmScriptCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        //Get the party
        Party party = player.raidsParty;

        if (party != null) {
            //Set raid finished
            party.setRaidStage(7);

            //Teleport to boss
            Tile bossRoomTile = new Tile(3232, 5730, party.getHeight());
            player.teleport(bossRoomTile);
            if(!party.bossFightStarted()) {
                GreatOlm.start(party);
            }
        }
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isDeveloperOrGreater(player);
    }
}
