package com.twisted.game.content.areas.wilderness.mageArena;

import com.twisted.game.content.areas.wilderness.content.key.WildernessKeyPlugin;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.ObjectManager;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.chainedwork.Chain;
import com.twisted.util.timers.TimerKey;

/**
 * @author Patrick van Elderen | January, 27, 2021, 15:58
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class MageBankLever extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if (option == 1) {
            //Outside magebank
            if (obj.getId() == 5959) {
                player.faceObj(obj);
                //Check to see if the player is teleblocked
                if (player.getTimerRepository().has(TimerKey.TELEBLOCK) || player.getTimerRepository().has(TimerKey.SPECIAL_TELEBLOCK)) {
                    player.teleblockMessage();
                    return true;
                }

                if (WildernessKeyPlugin.hasKey(player) && WildernessArea.inWilderness(player.tile())) {
                    player.message("You cannot teleport outside the Wilderness with the Wilderness key.");
                    return true;
                }
                player.lockNoDamage();
                Chain.bound(null).runFn(1, () -> {
                    player.animate(2140);
                    player.message("You pull the lever...");
                    GameObject spawned = new GameObject(5961, obj.tile(), obj.getType(), obj.getRotation());
                    ObjectManager.replace(obj, spawned, 5);
                }).then(2, () -> {
                    player.animate(714);
                    player.graphic(111, 110, 0);
                }).then(4, () -> {
                    var targetTile = new Tile(2539, 4712);

                    player.teleport(targetTile);
                    player.animate(-1);
                    player.unlock();
                    player.message("...And teleport into the mage's cave.");

                    // So we can tele straight away
                    player.clearAttrib(AttributeKey.LAST_WAS_ATTACKED_TIME);
                    if (player.getCombat().getFightTimer().isRunning()) {
                        player.getCombat().getFightTimer().reset();
                    }
                });
                return true;
            }

            //Inside magebank.. to outside
            if (obj.getId() == 5960) {
                if (!player.getPlayerRights().isDeveloperOrGreater(player)) {
                    if (player.inventory().count(6685) > 18) {
                        player.message("" + player.inventory().count(6685) + " brews is a little excessive don't you think?");
                        return true;
                    }
                }

                player.faceObj(obj);
                //Check to see if the player is teleblocked
                if (player.getTimerRepository().has(TimerKey.TELEBLOCK) || player.getTimerRepository().has(TimerKey.SPECIAL_TELEBLOCK)) {
                    player.teleblockMessage();
                    return true;
                }

                if (WildernessKeyPlugin.hasKey(player) && WildernessArea.inWilderness(player.tile())) {
                    player.message("You cannot teleport outside the Wilderness with the Wilderness key.");
                    return true;
                }

                Chain.bound(null).runFn(1, () -> {
                    player.lockNoDamage();
                    player.animate(2140);
                    player.message("You pull the lever...");
                    var spawned = new GameObject(5961, obj.tile(), obj.getType(), obj.getRotation());
                    ObjectManager.replace(obj, spawned, 5);
                }).then(2, () -> {
                    player.animate(714);
                    player.graphic(111, 110, 0);
                }).then(4, () -> {
                    var targetTile = new Tile(3090, 3956);
                    player.teleport(targetTile);
                    player.animate(-1);
                    player.unlock();
                    player.message("...And teleport out of the mage's cave.");
                });
                return true;
            }
        }
        return false;
    }
}
