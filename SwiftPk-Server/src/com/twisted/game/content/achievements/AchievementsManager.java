package com.twisted.game.content.achievements;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.QuestTab;
import com.twisted.game.world.entity.mob.player.rights.PlayerRights;
import com.twisted.game.world.items.Item;
import com.twisted.util.Color;
import com.twisted.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

import static com.twisted.game.world.entity.mob.player.QuestTab.InfoTab.SLAYER_POINTS;
import static java.lang.String.format;

/**
 * @author PVE
 * @Since juli 08, 2020
 */
public class AchievementsManager {
    
    private static final Logger logger = LogManager.getLogger(AchievementsManager.class);

    public static void activate(Player player, Achievements achievement, int increaseBy) {
        //Can't increase during tourneys
        if(player.inActiveTournament() || player.isInTournamentLobby()) {
            return;
        }

        //The user box test can't complete achievements.
        if(player.getUsername().equalsIgnoreCase("Box test")) {
            return;
        }

        final int current = player.achievements().computeIfAbsent(achievement, a -> 0);

        if (current >= achievement.getCompleteAmount())
            return;

        player.achievements().put(achievement, current + increaseBy);

        if (player.achievements().get(achievement) >= achievement.getCompleteAmount()) {
            int achievementsCompleted = player.getAttribOr(AttributeKey.ACHIEVEMENTS_COMPLETED, 0);
            player.putAttrib(AttributeKey.ACHIEVEMENTS_COMPLETED, achievementsCompleted);

            //When achievements complete, check if we can complete the COMPLETIONIST achievement.
            if(player.completedAllAchievements()) {
                activate(player, Achievements.COMPLETIONIST, 1);
            }

            player.message("<col=297A29>Congratulations! You have completed the "+achievement.getName()+" achievement.");
            World.getWorld().sendWorldMessage(format("<img=1051>[<col="+ Color.MEDRED.getColorValue()+">Achievement</col>]: %s just completed the %s achievement.", (PlayerRights.getCrown(player) + player.getUsername()), achievement.getName()));

            if(achievement.otherRewardString() != null) {
                checkForOtherReward(player, achievement);
            }

            Item[] reward = achievement.getReward();

            if (reward != null) {
                player.inventory().addOrBank(reward.clone());
                Utils.sendDiscordInfoLog(player, player.getUsername()+" has completed " + achievement.getName() + " and got " + Arrays.toString(reward.clone()), "achievements");
            }
        }
    }

    private static void checkForOtherReward(Player player, Achievements achievement) {
        switch(achievement) {
            case LARRANS_LOOTER_I:
                int slayerPoints = (Integer) player.getAttribOr(AttributeKey.SLAYER_REWARD_POINTS, 0) + 10;
                player.putAttrib(AttributeKey.SLAYER_REWARD_POINTS, slayerPoints);
                player.getPacketSender().sendString(SLAYER_POINTS.childId, QuestTab.InfoTab.INFO_TAB.get(SLAYER_POINTS.childId).fetchLineData(player));
                break;
            case LARRANS_LOOTER_II:
                slayerPoints = (Integer) player.getAttribOr(AttributeKey.SLAYER_REWARD_POINTS, 0) + 50;
                player.putAttrib(AttributeKey.SLAYER_REWARD_POINTS, slayerPoints);
                player.getPacketSender().sendString(SLAYER_POINTS.childId, QuestTab.InfoTab.INFO_TAB.get(SLAYER_POINTS.childId).fetchLineData(player));
                break;
            case LARRANS_LOOTER_III:
                slayerPoints = (Integer) player.getAttribOr(AttributeKey.SLAYER_REWARD_POINTS, 0) + 100;
                player.putAttrib(AttributeKey.SLAYER_REWARD_POINTS, slayerPoints);
                player.getPacketSender().sendString(SLAYER_POINTS.childId, QuestTab.InfoTab.INFO_TAB.get(SLAYER_POINTS.childId).fetchLineData(player));
                break;
            case PET_TAMER_I:
                player.putAttrib(AttributeKey.ANTI_FIRE_RESISTANT,true);
                break;
            case PET_TAMER_II:
                player.putAttrib(AttributeKey.VENOM_RESISTANT,true);
                break;
            case PUNCHING_BAGS_III:
                player.putAttrib(AttributeKey.ROCKY_BALBOA_TITLE_UNLOCKED,true);
                break;
        }
    }

    public static boolean isCompleted(Player player, Achievements achievement) {
        return player.achievements().get(achievement) >= achievement.getCompleteAmount();
    }
}
