package com.twisted.game.content.raids.chamber_of_secrets;

import com.twisted.game.content.packet_actions.interactions.objects.Ladders;
import com.twisted.game.content.raids.Raids;
import com.twisted.game.content.raids.party.Party;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.movement.MovementQueue;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.ObjectManager;
import com.twisted.game.world.position.Tile;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Color;
import com.twisted.util.chainedwork.Chain;

import static com.twisted.util.CustomItemIdentifiers.ELDER_WAND_RAIDS;
import static com.twisted.util.ObjectIdentifiers.*;

/**
 * @author Patrick van Elderen | May, 10, 2021, 11:09
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class Chamber extends PacketInteraction {

    private static final String[] QUOTES = {
        "It's got horrible breath!",
        "The floor? I wasn’t looking at its feet, I was too busy with its heads.",
        "It’s obviously guarding something.",
    };

    private void fluffyDoor(Player player, GameObject obj) {
        if (obj.interactAble()) {
            if (player.tile().y <= 4557) {
                player.smartPathTo(obj.tile());
                Chain.bound(player).name("EnterFluffyDoor").waitForTile(new Tile(3141, 4557), () -> {
                    player.lock(); // Lock the player
                    obj.interactAble(false); // We can not interact with the door
                    player.forceChat(QUOTES[World.getWorld().random(QUOTES.length - 1)]);
                    // Handle the door
                    GameObject openDoor1 = new GameObject(DOOR_136, new Tile(3141, 4557, player.tile().level), 0, 1);
                    GameObject rotateDoor1 = new GameObject(DOOR_136, new Tile(3141, 4557, player.tile().level), 0, 4);
                    ObjectManager.replace(openDoor1, rotateDoor1, 2);

                    player.getMovementQueue().clear();//Clear previous movement queues
                    var walkToTile = new Tile(3141, 4558);
                    player.getMovementQueue().interpolate(walkToTile, MovementQueue.StepType.FORCED_WALK);
                }).name("EnterFluffyDoorUnlockTask").then(1, () -> {
                    player.unlock();
                    obj.interactAble(true);
                });
            } else if (player.tile().y >= 4558) {
                player.smartPathTo(obj.tile());
                Chain.bound(player).name("ExitFluffyDoor").waitForTile(new Tile(3141, 4558), () -> {
                    player.lock(); // Lock the player
                    obj.interactAble(false); // We can not interact with the door

                    // Handle the door
                    GameObject openDoor1 = new GameObject(DOOR_136, new Tile(3141, 4557, player.tile().level), 0, 1);
                    GameObject rotateDoor1 = new GameObject(DOOR_136, new Tile(3141, 4557, player.tile().level), 0, 2);
                    ObjectManager.replace(openDoor1, rotateDoor1, 2);

                    player.getMovementQueue().clear();//Clear previous movement queues
                    var walkToTile = new Tile(3141, 4557);
                    player.getMovementQueue().interpolate(walkToTile, MovementQueue.StepType.FORCED_WALK);
                }).name("ExitFluffyDoorUnlockTask").then(1, () -> {
                    player.unlock();
                    obj.interactAble(true);
                });
            }
        }
    }

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if (option == 1) {
            Party party = player.raidsParty;

            final boolean isSteps = obj.getId() == 29778;
            final boolean isEntranceSteps = isSteps && obj.getX() == 3298 && obj.getY() == 5185;

            if (isEntranceSteps) {
                final Raids raids = player.getRaids();
                if (raids != null) {
                    raids.exit(player);
                    return true;
                }
            }

            //Fluffy door
            if (obj.getId() == DOOR_136) {
                fluffyDoor(player, obj);
                return true;
            }

            //Fluffy trapdoor
            if (obj.getId() == TRAPDOOR_21921) {
                player.faceObj(obj);
                if (party != null && party.getRaidStage() >= 2) {
                    GameObject closed = new GameObject(TRAPDOOR_21921, new Tile(3141, 4562, party.getHeight()), obj.getType(), obj.getRotation());
                    GameObject openTrapdoor = new GameObject(TRAPDOOR_21922, new Tile(3141, 4562, party.getHeight()), obj.getType(), obj.getRotation());
                    ObjectManager.replace(closed, openTrapdoor, 25); // Remain open for 15 seconds, close after.
                } else {
                    player.message(Color.RED.wrap("You have to defeat Fluffy before you can enter the trapdoor."));
                }
                return true;
            }

            //Fluffy trapdoor stairs
            if (obj.getId() == TRAPDOOR_21922) {
                player.faceObj(obj);
                if (party != null && party.getRaidStage() >= 2) {
                    Ladders.ladderDown(player, new Tile(3189, 4667, party.getHeight()), true);
                } else {
                    player.message(Color.RED.wrap("You have to defeat Fluffy before you can enter the trapdoor."));
                }
                return true;
            }

            //Centaurs portal to Dementors
            if (obj.getId() == PORTAL_4157) {
                if (party != null && party.getRaidStage() >= 3) {
                    Tile dementorTile = new Tile(3158, 4606, party.getHeight());
                    player.teleport(dementorTile);
                } else {
                    player.message(Color.RED.wrap("You have to defeat the centaurs before you can enter the portal."));
                }
                return true;
            }

            //Dementors portal to Fenrir
            if (obj.getId() == PORTAL_26740) {
                if (party != null && party.getRaidStage() >= 4) {
                    Tile fenrirTile = new Tile(3173, 4598, party.getHeight());
                    player.teleport(fenrirTile);
                } else {
                    player.message(Color.RED.wrap("You have to defeat the dementors before you can enter the portal."));
                }
                return true;
            }

            //Fenrir to Aragog
            if (obj.getId() == HOLE_15202) {
                if (party != null && party.getRaidStage() >= 5) {
                    Tile aragogTile = new Tile(3176, 4559, party.getHeight());
                    player.teleport(aragogTile);
                } else {
                    player.message(Color.RED.wrap("You have to defeat the Fenrir before you can enter the portal."));
                }
                return true;
            }

            //Aragog to Lord Voldemort
            if (obj.getId() == CLAN_CUP_PORTAL) {
                if (party != null && party.getRaidStage() >= 6) {
                    Tile voldemortTile = new Tile(3158, 4654, party.getHeight());
                    player.teleport(voldemortTile);
                } else {
                    player.message(Color.RED.wrap("You have to defeat the Aragog before you can enter the portal."));
                }
                return true;
            }

            if (obj.getId() == LARGE_GRAVE_32629) {
                var alreadyCollectedElderWand = player.inventory().contains(ELDER_WAND_RAIDS) || player.getEquipment().contains(ELDER_WAND_RAIDS);
                if (alreadyCollectedElderWand) {
                    player.message("You can't take more then one elder wand.");
                    return true;
                }
                player.animate(536);
                if (player.inventory().hasFreeSlots(1)) {
                    player.inventory().add(new Item(ELDER_WAND_RAIDS));
                } else {
                    player.message("You don't have enough inventory space to do that.");
                }
                return true;
            }

            if (obj.getId() == VAMPYRE_TOMB) {
                if (player.getRaidRewards().isEmpty()) {
                    player.message(Color.RED.wrap("You have already looted the tombstone, or your points are below 1,000."));
                    return true;
                }
                ChamberOfSecretsReward.displayRewards(player);
                ChamberOfSecretsReward.withdrawReward(player);
                return true;
            }
        }
        if (option == 2) {
            Party party = player.raidsParty;
            if (party == null) {
                return false;
            }

            //Fluffy trapdoor stairs
            if (obj.getId() == TRAPDOOR_21922) {
                player.faceObj(obj);
                GameObject open = new GameObject(TRAPDOOR_21922, new Tile(3141, 4562, party.getHeight()), obj.getType(), obj.getRotation());
                GameObject close = new GameObject(TRAPDOOR_21921, new Tile(3141, 4562, party.getHeight()), obj.getType(), obj.getRotation());
                ObjectManager.replaceWith(open, close);
                return true;
            }
        }
        return false;
    }

}
