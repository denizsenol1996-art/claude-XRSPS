package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.content.raids.chamber_of_secrets.ChamberOfSecretsReward;
import com.twisted.game.content.raids.party.Party;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

import static com.twisted.game.world.entity.AttributeKey.PERSONAL_POINTS;

/**
 * @author Patrick van Elderen | May, 13, 2021, 15:14
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class RaidsrewardCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        for (int i = 0; i < 1; i++) {
            Party.createParty(player);
            Party party = player.raidsParty;
            /*Optional<Player> test = World.getWorld().getPlayerByName("Test");
            if(test.isEmpty()) {
                return;
            }*/
           /* Optional<Player> test2 = World.getWorld().getPlayerByName("Test2");
            if(test2.isEmpty()) {
                return;
            }
            Optional<Player> test3 = World.getWorld().getPlayerByName("Test3");
            if(test3.isEmpty()) {
                return;
            }
            Optional<Player> test4 = World.getWorld().getPlayerByName("Test4");
            if(test4.isEmpty()) {
                return;
            }*/
            //party.getMembers().add(test.get());
            /*party.getMembers().add(test2.get());
            party.getMembers().add(test3.get());
            party.getMembers().add(test4.get());*/
            player.putAttrib(PERSONAL_POINTS, 200_000);
           /*test.get().putAttrib(PERSONAL_POINTS, 200_000);
            test2.get().putAttrib(PERSONAL_POINTS, 200_000);
            test3.get().putAttrib(PERSONAL_POINTS, 200_000);
            test4.get().putAttrib(PERSONAL_POINTS, 200_000);*/
            for (Player p : party.getMembers()) {
                ChamberOfSecretsReward.giveRewards(p);
                ChamberOfSecretsReward.displayRewards(p);
                ChamberOfSecretsReward.withdrawReward(p);
            }
        }
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isDeveloperOrGreater(player);
    }
}
