package com.twisted.game.world.position.areas;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Area;

import java.util.List;

public abstract class Controller {

    private final List<Area> areas;

    public Controller(List<Area> areas) {
        this.areas = areas;
    }

    public abstract void enter(Mob mob);

    public abstract void leave(Mob mob);

    public abstract void process(Mob mob);

    public abstract boolean canTeleport(Player player);

    public abstract boolean canAttack(Mob attacker, Mob target);

    public abstract void defeated(Player player, Mob mob);

    public abstract boolean canTrade(Player player, Player target);

    public abstract boolean isMulti(Mob mob);

    public abstract boolean canEat(Player player, int itemId);

    public abstract boolean canDrink(Player player, int itemId);

    public abstract void onPlayerRightClick(Player player, Player rightClicked, int option);

    public abstract boolean handleObjectClick(Player player, GameObject object, int type);

    public abstract boolean handleNpcOption(Player player, Npc npc, int type);

    public List<Area> getAreas() {
        return areas;
    }
    //If we want to use the AbstractArea inside method to check instead of the AreaManager inside method
    //to check, we should set this to true in the AbstractArea that overrides this.
    public abstract boolean useInsideCheck();

    public abstract boolean inside(Mob mob);
}
