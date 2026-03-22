package com.twisted.game.content.gambling;

import com.twisted.GameServer;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.impl.owner.ToggleGambleCommand;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.areas.Controller;

import java.util.Collections;

public class GamblingArea extends Controller {

    public GamblingArea() {
        super(Collections.singletonList(
            new Area(3084, 3459, 3097, 3483)));
    }

    @Override
    public void enter(Mob mob) {

    }

    @Override
    public void leave(Mob mob) {
        if (mob.isPlayer()) {
            Player player = (Player) mob;
            player.getPacketSender().sendInteractionOption("null", 1, false); //Remove gamble option
        }
    }

    @Override
    public void process(Mob mob) {
        if (mob.isPlayer()) {
            Player player = (Player) mob;
            if (player.getGamblingSession().state() == GambleState.NONE || player.getGamblingSession().state() == GambleState.REQUESTED_GAMBLE) {
                player.getPacketSender().sendInteractionOption("Gamble", 1, false);
            } else if (player.getGamblingSession().matchActive()) {
                player.getPacketSender().sendInteractionOption("null", 1, false); //Remove gamble option
            }
        }
    }

    @Override
    public boolean canTeleport(Player player) {
        return true;
    }

    @Override
    public boolean canAttack(Mob attacker, Mob target) {
        return false;
    }

    @Override
    public void defeated(Player player, Mob mob) {

    }

    @Override
    public boolean canTrade(Player player, Player target) {
        return true;
    }

    @Override
    public boolean isMulti(Mob mob) {
        return false;
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
        if (option == 1) {
            //System.out.println("Gamble right click");
            if (player.busy()) {
                player.message("You cannot do that right now.");
                return;
            }
            if (rightClicked.busy()) {
                player.message("That player is currently busy.");
                return;
            }
            if (!GameServer.properties().enableGambling) {
                player.message("Gambling is currently disabled.");
                return;
            }
            if (ToggleGambleCommand.gambleDisabled) {//updateox
                player.message("@red@Gambling is currently disabled.");
                return;
            }
            player.getGamblingSession().requestGamble(rightClicked,false);
        }
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
        return false;
    }

    @Override
    public boolean inside(Mob mob) {
        return false;
    }


}
