package com.twisted.game.content.wilderness.keys;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Tuple;
import lombok.Getter;

/**
 * @Author: Origin
 * @Date: 2/22/24
 */
public final class KeyNpc extends Npc {
    @Getter public final Player player;
    /**
     * Constructor for @KeyNpc
     * @param id
     * @param tile
     * @param player
     */
    public KeyNpc(int id, Tile tile, Player player) {
        super(id, tile);
        this.player = player;
        this.putAttrib(AttributeKey.OWNING_PLAYER, new Tuple<>(player.getIndex(), player));
        if (this.combatInfo() != null) this.getCombatInfo().setAggressive(true);
        this.setRespawns(false);
        this.getCombat().setTarget(player);
        this.face(player.tile());
    }

    /**
     * @KeyNpc Death Sequence
     */
    @Override
    public void die() {
        super.die();
        player.getPacketSender().sendEntityHintRemoval(true);
        this.clearAttrib(AttributeKey.OWNING_PLAYER);
    }
}
