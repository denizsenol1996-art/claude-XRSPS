package com.twisted.game.content.items;

import com.twisted.game.content.gambling.GamblingArea;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueManager;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.movement.MovementQueue;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.ObjectManager;
import com.twisted.game.world.position.Tile;
import com.twisted.util.chainedwork.Chain;

public class MithrilSeeds {

    //Item ID
    private static final int MITHRIL_SEEDS = 299;

    public static boolean onItemOption1(Player player, Item item) {
        if(item.getId() == MITHRIL_SEEDS) {
            if (player.tile().region() == 13103) {
                DialogueManager.sendStatement(player,"You can't plant seeds in this arena.");
                return false;
            }

            if(player.getController() instanceof GamblingArea) {
                var gambler = player.<Boolean>getAttribOr(AttributeKey.GAMBLER,false);
                if(!gambler) {
                    DialogueManager.sendStatement(player,"You can't plant seeds in this arena.");
                    return false;
                }
            }
            plant_seed(player);
            return true;
        }
        return false;
    }

    private static void plant_seed(Player player) {
        //Check to see if we're able to plant a flower
        if(ObjectManager.objWithTypeExists(10, new Tile(player.tile().x, player.tile().y, player.tile().level)) || ObjectManager.objWithTypeExists(11, new Tile(player.tile().x, player.tile().y, player.tile().level))) {
            player.message("You can't plant a seed here.");
            return;
        }

        if (player.getDueling().inDuel() || player.getController() instanceof GamblingArea) {
            player.message("You can't plant flowers in here.");
            return;
        }

        Tile targTile = player.tile().transform(-1, 0, 0);

        boolean legal = player.getMovementQueue().canWalkNoLogicCheck(-1, 0);
        if (!legal) {
            targTile = player.tile().transform(1, 0, 0);
            legal = player.getMovementQueue().canWalkNoLogicCheck(1, 0);
            if (!legal) {
                player.getMovementQueue().canMove(true); // only sending messges. wont dont care about this particular result.
                return; // No valid move to go!
            }
        }
        player.getMovementQueue().interpolate(targTile, MovementQueue.StepType.FORCED_WALK);

        //Lock the player
        player.lockDamageOk();

        //Send the player a message
        player.message("You open the small mithril case.");
        player.message("You drop a seed by your feet.");

        //Remove a seed from the players inventory
        player.inventory().remove(new Item(MITHRIL_SEEDS, 1), true);

        //Create the object
        spawn_flower(player, targTile);
    }

    private static void spawn_flower(Player player, Tile walkTo) {
        GameObject flower = new GameObject(2986, player.tile(), 10, 0);

        ObjectManager.addObj(flower);

        //Move the player, create a delay, face the object and unlock the player.
        player.putAttrib(AttributeKey.IGNORE_FREEZE_MOVE, true);
        player.getMovementQueue().walkTo(walkTo);

        Chain.bound(null).name("MithrilSeedsFreezeTask").runFn(1, () -> {
            player.clearAttrib(AttributeKey.IGNORE_FREEZE_MOVE);
            player.faceObj(flower);
            player.unlock();
        }).then(30, () -> {// Despawn after 30 ticks
            ObjectManager.removeObj(flower);
        });

        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Pick the flowers.", "Leave the flowers.");
                setPhase(0);
            }

            //Prompt the player with some options.
            @Override
            protected void select(int option) {
                if (isPhase(0)) {
                    if (option == 1) {
                        if(!player.inventory().contains(2472)) {
                            stop();
                            return;
                        }
                        player.animate(827);
                        ObjectManager.removeObj(flower);
                        player.inventory().add(new Item(2472, 1), true);
                        stop();
                    } else if (option == 2) {
                        stop();
                    }
                }
            }
        });
    }

}
