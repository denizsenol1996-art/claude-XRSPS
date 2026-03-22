package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.GameConstants;
import com.twisted.game.content.achievements.Achievements;
import com.twisted.game.content.achievements.AchievementsManager;
import com.twisted.game.content.events.ServerEvent;
import com.twisted.game.content.newplateau.PoolOfWealth;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Color;
import com.twisted.util.ItemIdentifiers;
import com.twisted.util.VoteRecord;
import com.teamgames.endpoints.vote.VoteEndpoint;
import com.teamgames.endpoints.vote.obj.ClaimReward;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author the plateau
 */

@Slf4j
public class RewardCommand implements Command {

    public static int voteCount;

    @Override
    public void execute(Player player, String command, String[] parts) {
        final String name = player.getUsername().toLowerCase();
        final String key = GameConstants.EVERYTHING_RS_API_KEY;
        final VoteEndpoint vote = (new VoteEndpoint()).setApiKey(key).setPlayerName(name).setRewardId("1").setAmount("all");
        if (vote != null) {
            try {
                final ClaimReward[] rewards = vote.getReward();

                if (rewards == null) {
                    log.info("Voting Rewards Returned Null.");
                    return;
                }

                if (rewards[0].getMessage() != null) {
                    player.message("You cannot claim your vote without voting first.");
                    return;
                }

                final List<VoteRecord> records = new ArrayList<>();

                for (var reward : rewards) {
                    if (reward.getMessage() != null && reward.getMessage().contains("Insufficient Points"))
                        continue;

                    final VoteRecord rewardRecord = new VoteRecord(reward.rewardId, reward.giveAmount);
                    records.add(rewardRecord);
                }

                if (records.isEmpty()) {
                    player.message(Color.RED.wrap("You do not have any votes to claim."));
                    return;
                }

                for (var record : records) {
                    int amount = record.amount();
                    if (ServerEvent.isDoubleVotes()) {
                        amount *= 2;
                    }
                    final Item reward = new Item(record.itemId(), amount);
                    player.getInventory().addOrBank(reward);
                }

                PoolOfWealth.handle(player, 100_000, true, false);

                final Item bloodMoney = new Item(ItemIdentifiers.BLOOD_MONEY, 100_000);
                player.getInventory().addOrBank(bloodMoney);

                if (voteCount >= 1 && voteCount <= 19) {
                    World.getWorld().sendWorldMessage("<img=939>[@red@<shad=2>Vote</col></shad>] The Vote Boss Needs [@red@ 20</col>] Vote Count to be spawn we now have " + "[@red@" + voteCount + "</col>] VC!");
                }

                if (voteCount >= 25) {
                    World.getWorld().sendWorldMessage("<img=939><col=65280><shad=2>Vote boss has been spawned use ::voteboss to teleport!");//updatet3
                    final Npc npc2 = new Npc(8633, new Tile(3120, 3473));
                    World.getWorld().getNpcs().add(npc2);
                    npc2.respawns(false);
                    voteCount = 0;
                }

                AchievementsManager.activate(player, Achievements.VOTE_FOR_US_I, 1);
                AchievementsManager.activate(player, Achievements.VOTE_FOR_US_II, 1);
                AchievementsManager.activate(player, Achievements.VOTE_FOR_US_III, 1);

                World.getWorld().sendWorldMessage("@blu@[Vote] " + player.getUsername() + " has just voted! support us by using ::vote!");

                player.message("Vote count is " + voteCount);
                player.message(Color.GREEN.wrap("Thank you for voting for Hazy!"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
