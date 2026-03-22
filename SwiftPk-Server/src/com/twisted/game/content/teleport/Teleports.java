package com.twisted.game.content.teleport;

import com.twisted.GameServer;
import com.twisted.game.content.areas.wilderness.content.key.WildernessKeyPlugin;
import com.twisted.game.content.duel.Dueling;
import com.twisted.game.content.instance.InstancedAreaManager;
import com.twisted.game.content.tournaments.TournamentManager;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.masks.animations.Animation;
import com.twisted.game.world.entity.masks.animations.Priority;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.player.MagicSpellbook;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.impl.TournamentArea;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.ItemIdentifiers;
import com.twisted.util.SecondsTimer;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;
import com.twisted.util.timers.TimerKey;

import java.util.concurrent.TimeUnit;

import static com.twisted.game.content.presets.PresetManager.lastTimeDied;
import static com.twisted.util.CustomNpcIdentifiers.CURSED_ELEMENT;

/**
 * @author Patrick van Elderen | January, 10, 2021, 11:08
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class Teleports {
     static final SecondsTimer teleport_delay = new SecondsTimer();

    /**
     * Determines if the player is able to teleport. The inform parameter
     * allows us to inform the player of the reason they cannot teleport
     * if we so wish to.
     */
    public static boolean canTeleport(Player player, boolean inform, TeleportType teletype) {
        if ((WildernessKeyPlugin.hasKey(player) && WildernessArea.inWilderness(player.tile()) && !player.getPlayerRights().isDeveloperOrGreater(player))) {
            player.message("You cannot teleport outside the Wilderness with the Wilderness key.");
            return false;
        }

        //Put most important safety in this method just to be sure
        if (TournamentManager.teleportBlocked(player,true)) {
            return false;
        }

        if (player.getController() != null) {
            if (!player.getController().canTeleport(player) && (!(player.getController() instanceof TournamentArea))) {
                player.message("You aren't allowed to teleport from here.");
                player.getInterfaceManager().closeDialogue();
                return false;
            }
        }

        if (Dueling.in_duel(player)) {
            if (inform) {
                player.message("You cannot teleport out of a duel.");
            }
            return false;
        }

        if (player.getTimerRepository().has(TimerKey.SPECIAL_TELEBLOCK)) {
            if(inform) {
                long millis = player.getTimerRepository().left(TimerKey.SPECIAL_TELEBLOCK) * 600L;
                player.message(String.format("A teleport block has been cast on you. It should wear off in %d minutes, %d seconds.", TimeUnit.MILLISECONDS.toMinutes(millis), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
            }
            return false;
        }

        if (player.getTimerRepository().has(TimerKey.TELEBLOCK)) {
            if (inform) {
                player.teleblockMessage();
            }
            return false;
        }

        boolean fawkes = player.hasPetOut("Fawkes") || player.hasPetOut("Zaweks pet") || player.hasPetOut("Mending life bird") || player.hasPetOut(CURSED_ELEMENT);//updateo
        var capLvl = teletype == TeleportType.ABOVE_20_WILD ? 30 : 20;
        if (WildernessArea.wildernessLevel(player.tile()) > capLvl && !fawkes && (!player.getPlayerRights().isDeveloperOrGreater(player))) {

            if (inform) {
                player.message("A mysterious force blocks your teleport spell!");
                player.message("You can't use this teleport after level " + capLvl+ " wilderness.");
            }
            return false;
        }
       /* if(teleport_delay.active() && fawkes){
            int seconds = teleport_delay.secondsRemaining();
            player.message("You must wait another " + (seconds == 1 ? "second" : "" + seconds + " seconds") + " before teleport with your pet.");
            return false;
        }*/
        if (player.jailed()) {
            if (inform) {
                player.message("You can't leave the jail yet.");
            }
            return false;
        }

        //Is our player currently in an active Fight Cave?
        if (player.getMinigame() != null && !player.getMinigame().canTeleportOut()) {
            player.message("You cannot do that right now.");
            return false;
        }

        if (player.getTimerRepository().has(TimerKey.BLOCK_SPEC_AND_TELE) && player.<Integer>getAttribOr(AttributeKey.MULTIWAY_AREA,-1) == 0) {
            player.message("<col=804080>Teleport blocked for "+ player.getTimerRepository().asSeconds(TimerKey.BLOCK_SPEC_AND_TELE)+" more secs after using spec at the start of a battle.");
            return false;
        }

        var mage_arena = player.<Boolean>getAttribOr(AttributeKey.MAGEBANK_MAGIC_ONLY,false);
        if(fawkes && mage_arena) {
            player.message("A mysterious force blocks your teleport, you can't use fawkes to escape from here!");
            return false;
        }

        if (player.looks().hidden()) {
            player.looks().hide(false);
        }

        return true;
    }

    public static void teleportToTarget(Player player, Tile targetTile) {
        player.lockNoDamage();
        if (player.getSpellbook() == MagicSpellbook.NORMAL) {
            //Modern spells
            player.animate(714);
            player.graphic(111, 92, 0);
        } else if (player.getSpellbook() == MagicSpellbook.ANCIENT) {
            //Ancient spells
            player.animate(1979);
            player.graphic(392);
        }

        Chain.bound(null).runFn(3, () -> {
            player.teleport(World.getWorld().randomTileAround(targetTile, 1));
            player.animate(-1);
            player.graphic(-1);
            player.unlock();
        });
    }

    public static boolean pkTeleportOk(Player player, Tile tile) {
        return pkTeleportOk(player, tile.x, tile.y, true);
    }

    public static boolean pkTeleportOk(Player player, int x, int z) {
        return pkTeleportOk(player, x, z, true);
    }

    public static boolean pkTeleportOk(Player player, Tile tile, boolean preventQuickRespawn) {
        return pkTeleportOk(player, tile.x, tile.y, preventQuickRespawn);
    }

    // Execute a teleport, checking if locked or jailed etc.
    public static boolean pkTeleportOk(Player player, int x, int z, boolean preventQuickRespawn) {
        if (player.locked()) {
            // Stops players doing ::mb while jumping over, for example, the wildy ditch. This would fly them off into random places.
            return false;
        }
        if (!player.getPlayerRights().isDeveloperOrGreater(player)) {
            if (player.jailed()) {
                player.message("You can't use commands when Jailed.");
                return false;
            }
            if (!wildernessTeleportAntiragOk(x, z, player, preventQuickRespawn)) {
                return false;
            }
        } else {
            player.message("As an admin you bypass pk-tele restrictions.");
        }
        return true;
    }

    public static boolean wildernessTeleportAntiragOk(int x, int z, Player player, boolean preventQuickRespawn) {
        if (WildernessArea.inWilderness(new Tile(x, z))) {
            if (preventQuickRespawn && lastTimeDied(player, GameServer.properties().pkTelesAfterSetupSet)) {
                player.message("Quick wilderness teleports are off limits %ds <col=FF0000>after death.</col>", (int)Utils.ticksToSeconds(GameServer.properties().pkTelesAfterSetupSet));
                return false;
            }

            if (player.inventory().count(ItemIdentifiers.SARADOMIN_BREW4) > GameServer.properties().brewCap) {
                player.message("You cannot take more than " + GameServer.properties().brewCap + " Saradomin brews into the wilderness.");
                return false;
            }
        }
        return true;
    }

    public static boolean rolTeleport(Player player) {
        // rol ringoflife ring of life
        player.stopActions(true);
        return canTeleport(player, true, TeleportType.GENERIC);
    }

    public static void ringOfLifeTeleport(Player player) {
        player.lockNoDamage();
        player.animate(714);
        player.graphic(111, 92, 0);
        Chain.bound(null).runFn(3, () -> {
            player.teleport(3094, 3469); //Teleport the player edge coffin spot
            player.animate(-1);
            player.graphic(-1);
            player.unlock();
        });
    }

    public static void basicTeleport(Player player, Tile tile) {
        basicTeleport(player, tile, 714, new Graphic(111, 92));
    }

    public static void basicTeleport(Player player, Tile tile, int anim, Graphic gfx) {
        //If the player is locked or dead
        if (player.locked() || player.dead() || player.hp() <= 0)
            return;

        //Close all interfaces
        player.getInterfaceManager().close();

        //Stop the players actions
        player.stopActions(true);
        //trigger checks
        var instancedArea = InstancedAreaManager.getSingleton().ofZ(player.getZ());
        if (instancedArea != null)
            instancedArea.onTeleport(player, tile);

        //remove from tourny
        TournamentManager.leaveTourny(player, false,true);

        player.lockNoDamage();
        player.animate(anim);
        player.graphic(gfx.id(), gfx.height(), gfx.delay());
        player.sound(200);
        Chain.bound(null).runFn(3, () -> {
      //      boolean fawkes = player.hasPetOut("Fawkes") || player.hasPetOut("Zaweks pet") || player.hasPetOut("Mending life bird");//updateo
           /* if(fawkes && WildernessArea.inWilderness(player.tile())) {
                teleport_delay.start(30);

            }*/
            player.teleport(tile);
            player.animate(new Animation(-1, Priority.HIGH));
            player.graphic(-1);
            player.unlock();
        });
    }

    /**
     * For uninterruptable scripts (BOTS ONLY!)
     */
    public static void teleportContextless(Player player, Tile tile, int anim, Graphic gfx) {
        player.lockNoDamage();
        player.animate(anim);
        player.graphic(gfx.id(), gfx.height(), gfx.delay());
        player.sound(200);
        Chain.bound(null).runFn(3, () -> {
            player.teleport(tile);
            player.animate(-1);
            player.graphic(-1);
            player.unlock();
        });
    }

}
