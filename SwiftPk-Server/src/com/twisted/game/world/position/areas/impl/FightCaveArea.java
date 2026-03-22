package com.twisted.game.world.position.areas.impl;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.areas.Controller;

import java.util.Collections;

/**
 * @author Patrick van Elderen | December, 23, 2020, 15:49
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class FightCaveArea extends Controller {

    public FightCaveArea() {
        super(Collections.singletonList(new Area(2360, 5045, 2445, 5125)));
    }

    @Override
    public void enter(Mob mob) {

    }

    @Override
    public void leave(Mob mob) {
        if(mob.isPlayer()) {
            Player player = (Player) mob;
            var minigame = player.getMinigame();
            if (minigame != null) {
                minigame.end(player);
            }
        }
    }

    @Override
    public void process(Mob mob) {

    }

    @Override
    public boolean canTeleport(Player player) {
        player.message("Please use the southern exit if you wish to leave the caves.");
        return false;
    }

    @Override
    public boolean canAttack(Mob attacker, Mob target) {
        return true;
    }

    @Override
    public void defeated(Player player, Mob mob) {
        var minigame = player.getMinigame();
        if (minigame != null) {
            minigame.end(player);
        }
    }

    @Override
    public boolean canTrade(Player player, Player target) {
        return false;
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
    public boolean handleObjectClick(Player player, GameObject object, int type) {
        return false;
    }

    @Override
    public boolean handleNpcOption(Player player, Npc npc, int type) {
        return false;
    }

    @Override
    public boolean useInsideCheck() {
        return false;// no need, assuming coords are accurate
    }

    @Override
    public boolean inside(Mob mob) {
        return false;// no need, assuming coords are accurate
    }
}
