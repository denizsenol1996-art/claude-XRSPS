package com.twisted.game.content.announcements;

import com.twisted.game.content.kill_logs.BossKillLog;
import com.twisted.game.content.kill_logs.SlayerKillLog;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.util.Utils;

import java.util.Arrays;

public class ServerAnnouncements {

    public static void tryBroadcastDrop(Player player, Npc npc, Item item) {
        for (BossKillLog.Bosses boss : BossKillLog.Bosses.values()) {
            if (Arrays.stream(boss.getNpcs()).anyMatch(id -> id == npc.id())) {
                String name = npc.def() == null ? "Unknown" : npc.def().name;
                if (item.getValue() > 100_000) {
                    int kc = player.getAttribOr(boss.getKc(), 0);
                    World.getWorld().sendWorldMessage("<img=1875><shad=2><col=0052cc>" + player.getUsername() + " Killed a " + name + " and received " + Utils.getVowelFormat(item.unnote().name()) + " drop! KC " + kc + "!");
                }
                break;
            }
        }

        for (SlayerKillLog.SlayerMonsters slayerMonster : SlayerKillLog.SlayerMonsters.values()) {
            if (Arrays.stream(slayerMonster.getNpcs()).anyMatch(id -> id == npc.id())) {
                String name = npc.def() == null ? "Unknown" : npc.def().name;
                if (item.getValue() > 100_000) {
                    int kc = player.getAttribOr(slayerMonster.getKc(), 0);
                    World.getWorld().sendWorldMessage("<img=1875><shad=2><col=0052cc>" + player.getUsername() + " Killed a " + name + " and received " + Utils.getVowelFormat(item.unnote().name()) + " drop! KC " + kc + "!");
                }
                break;
            }
        }
    }

}
