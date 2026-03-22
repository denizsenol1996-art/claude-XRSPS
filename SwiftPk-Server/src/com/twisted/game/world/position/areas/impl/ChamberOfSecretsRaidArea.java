package com.twisted.game.world.position.areas.impl;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.areas.Controller;
import com.twisted.util.Utils;

import java.util.Collections;

import static com.twisted.game.content.raids.party.Party.*;
import static com.twisted.game.world.entity.AttributeKey.PERSONAL_POINTS;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 25, 2021
 */
public class ChamberOfSecretsRaidArea extends Controller {

    private static final int POINTS_WIDGET = 12000;

    public ChamberOfSecretsRaidArea() {
        super(Collections.emptyList());
    }

    @Override
    public void enter(Mob mob) {

    }

    @Override
    public void leave(Mob mob) {
    }

    @Override
    public void process(Mob mob) {
        if (mob.isPlayer()) {
            Player player = (Player) mob;
            var party = player.raidsParty;
            if (party != null) {
                player.getPacketSender().sendString(NAME_FRAME, player.getUsername() + ":");
                player.getPacketSender().sendString(TOTAL_POINTS, "" + Utils.formatNumber(party.totalPoints()));
                player.getPacketSender().sendString(POINTS, "" + Utils.formatNumber(player.<Integer>getAttribOr(PERSONAL_POINTS, 0)));
            }
            player.getInterfaceManager().openWalkable(POINTS_WIDGET);
        }
    }

    @Override
    public boolean canTeleport(Player player) {
        return false;
    }

    @Override
    public boolean canAttack(Mob attacker, Mob target) {
        return !attacker.isPlayer() || !target.isPlayer();
    }

    @Override
    public boolean canTrade(Player player, Player target) {
        return true;
    }

    @Override
    public boolean isMulti(Mob mob) {
        return true;
    }

    @Override
    public boolean canEat(Player player, int itemId) {
        return true;
    }

    @Override
    public boolean canDrink(Player player, int itemId) {
        return true;
    }

    @Override
    public void onPlayerRightClick(Player player, Player rightClicked, int option) {
    }

    @Override
    public void defeated(Player player, Mob mob) {
    }

    @Override
    public boolean handleObjectClick(Player player, GameObject object, int type) {
        return false;
    }

    @Override
    public boolean handleNpcOption(Player player, Npc npc, int type) {
        return false;
    }

    @Override
    public boolean inside(Mob mob) {
        return mob.tile().region() == 12615 || mob.tile().region() == 12616;
    }

    @Override
    public boolean useInsideCheck() {
        return true;
    }
}
