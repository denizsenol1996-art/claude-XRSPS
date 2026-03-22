package com.twisted.game.world.position.areas.impl;

import com.twisted.GameServer;
import com.twisted.game.content.duel.DuelRule;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.dialogue.DialogueManager;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.areas.Controller;

import java.util.Arrays;

import static com.twisted.game.content.duel.DuelState.IN_DUEL;
import static com.twisted.game.content.duel.DuelState.STARTING_DUEL;

public class DuelArenaArea extends Controller {

    // In any of the 6 challenge areas.
    public DuelArenaArea() {
        super(Arrays.asList(
            new Area(3318, 3247, 3327, 3247),
            new Area(3324, 3247, 3328, 3264),
            new Area(3327, 3262, 3342, 3270),
            new Area(3342, 3262, 3387, 3280),
            new Area(3387, 3262, 3394, 3271),
            new Area(3312, 3224, 3325, 3247),
            new Area(3326, 3200, 3398, 3267)
        ));
    }

    @Override
    public void enter(Mob mob) {

    }

    @Override
    public void leave(Mob mob) {
        if (mob.isPlayer()) {
            Player player = mob.getAsPlayer();
            player.getInterfaceManager().openWalkable(-1);
            //System.out.println(player.getUsername() + " is leaving duel arena");
            if (player.getDueling().inDuel()) {
                player.getDueling().onDeath();
            }
            player.getPacketSender().sendInteractionOption("null", 1, false);
            player.getPacketSender().sendInteractionOption("null", 2, true);
        }
    }

    @Override
    public void process(Mob mob) {
        //System.out.println("still processing");
        if (mob.isPlayer()) {
            //System.out.println("Inside...");
            Player player = mob.getAsPlayer();
            //if (mob.isPlayer())
            //System.out.println("open: "+player.getInterfaceManager().getWalkable()); // so this says 201
            if (player.getInterfaceManager().getWalkable() != 201) // therefore it won't send, it thinks its already 201
                player.getInterfaceManager().openWalkable(201);
            if (!player.getDueling().inDuel() && GameServer.properties().enableDueling) {
                player.getPacketSender().sendInteractionOption("Challenge", 1, false);
                player.getPacketSender().sendInteractionOption("null", 2, true);
            } else {
                player.getPacketSender().sendInteractionOption("null", 1, true);
                player.getPacketSender().sendInteractionOption("Attack", 2, true);
            }
        }
    }

    @Override
    public boolean canTeleport(Player player) {
        if (player.getDueling().inDuel()) {
            DialogueManager.sendStatement(player, "You cannot teleport out of a duel!");
            return false;
        }
        return true;
    }

    @Override
    public boolean canAttack(Mob mob, Mob target) {
        if (mob.isPlayer() && target.isPlayer()) {
            Player a = mob.getAsPlayer();
            Player t = target.getAsPlayer();
            if (a.getDueling().getState() == IN_DUEL && t.getDueling().getState() == IN_DUEL) {
                return true;
            } else if (a.getDueling().getState() == STARTING_DUEL || t.getDueling().getState() == STARTING_DUEL) {
                a.message("The duel hasn't started yet!");
                return false;
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean canTrade(Player player, Player target) {
        if (player.getDueling().inDuel()) {
            DialogueManager.sendStatement(player, "You cannot trade during a duel!");
            return false;
        }
        return true;
    }

    @Override
    public boolean isMulti(Mob mob) {
        return true;
    }

    @Override
    public boolean canEat(Player player, int itemId) {
        if (player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_FOOD.ordinal()]) {
            DialogueManager.sendStatement(player, "Food has been disabled in this duel!");
            return true;
        }
        return true;
    }

    @Override
    public boolean canDrink(Player player, int itemId) {
        if (player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_POTIONS.ordinal()]) {
            DialogueManager.sendStatement(player, "Potions have been disabled in this duel!");
            return true;
        }
        return true;
    }

    @Override
    public void onPlayerRightClick(Player player, Player rightClicked, int option) {
        if (option == 1) {
            //System.out.println("Duel right click");
            if (player.busy()) {
                player.message("You cannot do that right now.");
                return;
            }
            if (rightClicked.busy()) {
                player.message("That player is currently busy.");
                return;
            }
            if (!GameServer.properties().enableDueling) {
                player.message("Dueling is currently disabled until we have a larger playerbase.");
                return;
            }
            var rightClickedAttribOr = rightClicked.<Integer>getAttribOr(AttributeKey.CUSTOM_DUEL_RULE,0);
            player.putAttrib(AttributeKey.CUSTOM_DUEL_RULE, rightClickedAttribOr);
            player.getDueling().requestDuel(rightClicked);
        }
    }

    @Override
    public void defeated(Player player, Mob mob) {
    }

    @Override
    public boolean handleObjectClick(Player player, GameObject object, int type) {
        if (type == 1 && object.getId() == 3203) {
            player.message("Forfeit is currently disabled.");
            /*if (player.getController() instanceof DuelArenaArea) {
                if (player.getDueling().getRules()[DuelRule.NO_FORFEIT.ordinal()]) {
                    DialogueManager.sendStatement(player, "This duel cannot be forfeited.");
                    return true;
                }

                if (Dueling.in_duel(player)) {
                    player.getDialogueManager().start(new DuelForfeitDialogue());
                } else {
                    DialogueManager.sendStatement(player, "The duel has not yet begun.");
                    return true;
                }
            }*/
            return true;
        }
        return false;
    }

    @Override
    public boolean handleNpcOption(Player player, Npc npc, int type) {
        return false;
    }

    @Override
    public boolean inside(Mob mob) {
        return false; // no need to use this, super(area) works fine (if coords are accurate)
    }

    @Override
    public boolean useInsideCheck() {
        return false; // no need, assuming coords are accurate
    }
}
