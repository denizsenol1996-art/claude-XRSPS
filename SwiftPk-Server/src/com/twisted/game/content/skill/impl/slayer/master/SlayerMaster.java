package com.twisted.game.content.skill.impl.slayer.master;

import com.twisted.game.content.skill.impl.slayer.Slayer;
import com.twisted.game.content.skill.impl.slayer.slayer_task.SlayerCreature;
import com.twisted.game.content.skill.impl.slayer.slayer_task.SlayerTaskDef;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.QuestTab;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;

import java.util.ArrayList;
import java.util.List;

import static com.twisted.game.world.entity.mob.player.QuestTab.InfoTab.SLAYER_TASK;
import static com.twisted.util.NpcIdentifiers.*;
import static com.twisted.util.NpcIdentifiers.ELF_TRACKER;

/**
 * @author PVE
 * @Since juli 19, 2020
 */
public class SlayerMaster {

    public int npcId;
    public int id;

    public final List<SlayerTaskDef> defs = new ArrayList<>();

    private SlayerTaskDef randomTask(Player player) {
        // Grab our last task and exclude it to avoid b2b
        int last = player.getAttribOr(AttributeKey.SLAYER_TASK_ID, 0);

        // Build a map and fill it with possible tasks.
        final int[] tmp = {0};

        ImmutableRangeMap.Builder<Integer, SlayerTaskDef> builder = ImmutableRangeMap.builder();
        defs.forEach(task -> {
            if (task != null && task.getCreatureUid() != last && player.skills().xpLevel(Skills.SLAYER) >= SlayerCreature.lookup(task.getCreatureUid()).req && player.skills().combatLevel() >= SlayerCreature.lookup(task.getCreatureUid()).cbreq &&
                !player.getSlayerRewards().isTaskBlocked(task) && player.getSlayerRewards().canAssign(task)) {
                builder.put(Range.closedOpen(tmp[0], tmp[0] + task.getWeighing()), task);
                tmp[0] += task.getWeighing();
            }
        });
        ImmutableRangeMap<Integer, SlayerTaskDef> build = builder.build();
        if (tmp[0] == 0) {
            // builder is empty u cant do any tasks at all
            return null;
        }
        Range<Integer> range = build.span();
        int rnd = World.getWorld().random(range.upperEndpoint() - 1);
        return build.get(rnd);
    }

    public static void assign(Player player, int id) {
        var master = Slayer.master(id);
        if (master == null)
            return;
        SlayerTaskDef def = master.randomTask(player);
        if(def == null) {
            System.out.println("no task available.");
            return;
        }

        player.putAttrib(AttributeKey.SLAYER_TASK_ID, def.getCreatureUid());
        int task_amt = player.getSlayerRewards().slayerTaskAmount(player, def);
        if (master.npcId == STEVE_6799) {
            player.putAttrib(AttributeKey.SLAYER_MASTER, STEVE_6799);
        } else if(master.npcId == NIVEN) {
            player.putAttrib(AttributeKey.SLAYER_MASTER, NIVEN);
        } else if(master.npcId == DURADEL) {
            player.putAttrib(AttributeKey.SLAYER_MASTER, DURADEL);
        } else if (master.npcId == KRYSTILIA) {
            player.putAttrib(AttributeKey.SLAYER_MASTER, KRYSTILIA);
        } else if (master.npcId == ELF_TRACKER) {
            player.putAttrib(AttributeKey.SLAYER_MASTER, ELF_TRACKER);
        }
        player.putAttrib(AttributeKey.SLAYER_TASK_AMT, task_amt);

        player.getPacketSender().sendString(SLAYER_TASK.childId, QuestTab.InfoTab.INFO_TAB.get(SLAYER_TASK.childId).fetchLineData(player));
        Slayer.displayCurrentAssignment(player);
    }
}
