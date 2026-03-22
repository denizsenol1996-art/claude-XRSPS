package com.twisted.game.world.entity.combat.method.impl.npcs.fightcaves;

import com.twisted.game.content.minigames.impl.fight_caves.FightCavesMinigame;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Tile;
import com.twisted.util.NpcIdentifiers;

import java.util.ArrayList;
import java.util.stream.IntStream;

import static com.twisted.util.Utils.random;

/**
 * @author Patrick van Elderen | December, 23, 2020, 14:35
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class TzTokJad extends Npc {

    private final ArrayList<YtHurKot> healers = new ArrayList<>();

    public TzTokJad(int id, Tile tile) {
        super(id, tile);
        respawns(false);
    }

    /**
     * Spawn healers.
     */
    public void spawnHealers(Player player) {

        if(player.getMinigame() == null || !(player.getMinigame() instanceof FightCavesMinigame)) {
            return;
        }

        if (!healers.isEmpty()) {
            return;
        }

        IntStream.range(0, 4 - healers.size()).forEach(i -> {
            Tile tile = FightCavesMinigame.COORDINATES[random(FightCavesMinigame.COORDINATES.length)].transform(0, 0, tile().getLevel());

            YtHurKot npc = new YtHurKot(NpcIdentifiers.YTHURKOT, tile,this);

            healers.add(npc);
            FightCavesMinigame minigame = (FightCavesMinigame) player.getMinigame();
            minigame.addNpc(npc);
            World.getWorld().registerNpc(npc);
            npc.respawns(false);
        });
    }

    /**
     * Removes the healer from the healers list.
     *
     * @param healer
     */
    public void removeHealer(YtHurKot healer) {
        healers.remove(healer);
    }
}
