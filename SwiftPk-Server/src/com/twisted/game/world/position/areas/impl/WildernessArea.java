package com.twisted.game.world.position.areas.impl;

import com.twisted.game.content.EffectTimer;
import com.twisted.game.content.areas.riskzone.RiskFightArea;
import com.twisted.game.content.areas.wilderness.content.key.EscapeKeyPlugin;
import com.twisted.game.content.areas.wilderness.content.key.WildernessKeyPlugin;
import com.twisted.game.content.duel.Dueling;
import com.twisted.game.content.mechanics.Transmogrify;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.bountyhunter.BountyHunter;
import com.twisted.game.world.entity.combat.bountyhunter.EarningPotential;
import com.twisted.game.world.entity.combat.skull.Skulling;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.QuestTab;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.Controller;
import com.twisted.util.CustomItemIdentifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

import static com.twisted.game.world.entity.mob.player.QuestTab.InfoTab.PLAYERS_PKING;

public class WildernessArea extends Controller {

    public static final Area getFeroxCenter = new Area(3118, 3623, 3153, 3634, 0); //block this when tb'd

    public static final Area getFeroxUpperNorth =  new Area(3120, 3636, 3157, 3640, 0); //block this when tb'd

    public static final Area getFeroxNorthEntrance =  new Area(3122, 3636, 3157, 3643, 0); //block this when tb'd

    public static final Area getFeroxNorthEdges =  new Area(3136, 3642, 3156, 3646, 0); //block this when tb'd

    public static final Area getFeroxEastEdges =  new Area(3154, 3627, 3159, 3633, 0); //block this when tb'd

    public static final Area getFeroxLowerSouth =  new Area(3120, 3621, 3151, 3622, 0); //block this when tb'd

    public static final Area getFeroxLowerSouthEdges =  new Area(3124, 3616, 3144, 3620, 0); //block this when tb'd

    public static final Area getFeroxSouthEntrance =  new Area(3129, 3610, 3140, 3615, 0); //block this when tb'd

    public static final Area getFeroxRandomLine =  new Area(3120, 3635, 3154, 3635, 0); //block this when tb'd

    private static final Logger log = LoggerFactory.getLogger(WildernessArea.class);

    public static boolean inDiamondZone(Tile tile) {
        return tile.region() == 9771;
    }

    public static boolean inZenyteZone(Tile tile) {
        return tile.region() == 9770;
    }

    public static boolean inWilderness(Tile tile) {
        return wildernessLevel(tile) > 0;
    }

    public static int wildernessLevel(Tile tile) {
        int z = (tile.y > 6400) ? tile.y - 6400 : tile.y;

        //Custom wilderness areas here
        if (tile.region() == 9369 || tile.region() == 9370) { // Wilderness cave member zone)
            return 40;
        }

        if (tile.inArea(RiskFightArea.NH_AREA)) {
            return 1;
        }

        CustomWildernessRegions customWildernessRegions = CustomWildernessRegions.byRegion(tile.region());
        if (customWildernessRegions != null && customWildernessRegions.region == tile.region()) {
            return customWildernessRegions.level;
        }

        if (tile.region() == 12606) {
            return 15;
        }

        if (!(tile.x > 2941 && tile.x < 3392 && tile.y > 3524 && tile.y < 3968) && !inUndergroundWilderness(tile))
            return 0;

        // North of black knights fortress and more - people lure here.
        if (tile.inArea(2998, 3525, 3026, 3536) || tile.inArea(3005, 3537, 3023, 3545)
            || tile.equals(2997, 3525) || tile.inArea(3024, 3537, 3026, 3542)
            || tile.inArea(3027, 3525, 3032, 3530) || tile.inArea(3003, 3537, 3004, 3538)
            // And level 20, west side of wildy, trollhiem shortcut. More people lure here :)
            || tile.inArea(2941, 3676, 2947, 3681)) {
            return 0;
        }

        //OSRS calculated by coordinates

        return ((z - 3520) >> 3) + 1;
    }

    public static boolean inUndergroundWilderness(Tile tile) {
        int region = tile.region();

        if (region == 12606) {
            return true;
        }

        // Revenant caves:
        if (region == 12701 || region == 12702 || region == 12703 || region == 12957 || region == 12958 || region == 12959)
            return true;

        return region == 12192 || region == 12193 || region == 12961 || region == 11937 || region == 12443 || region == 12190;
    }

    // A small custom area between 1-4 wilderness were range is disabled in an attempt to stop raggers. Note: may not be in use.
    public static boolean inRestrictedRangeZone(Tile tile) {
        return tile.inArea(3041, 3548, 3069, 3561);
    }

    /**
     * Any area, such as Wilderness, dangerous Instances, FFA clan war arena which a Player can attack another Player
     */
    public static boolean inAttackableArea(Mob player) {
        return WildernessArea.inWilderness(player.tile()) || player.getController() instanceof TournamentArea || Dueling.in_duel(player);
    }

    public static boolean inside_pirates_hideout(Tile tile) {
        Area original = new Area(3038, 3949, 3044, 3959);
        return tile.inArea(original);
    }

    public static boolean inside_axehut(Tile tile) {
        return tile.inArea(3187, 3958, 3194, 3962);
    }

    public static boolean inside_rouges_castle(Tile tile) {
        return tile.inArea(3275, 3922, 3297, 3946);
    }

    public static boolean inside_extended_pj_timer_zone(Tile tile) {
        return tile.inArea(3047, 3524, 3096, 3539);
    }

    public static boolean at_west_dragons(Tile tile) {
        return tile.inArea(2964, 3585, 2999, 3622);
    }

    public WildernessArea() {
        super(Collections.emptyList());
    }

    @Override
    public void enter(Mob mob) {
        if (mob.isPlayer()) {
            Player player = mob.getAsPlayer();
            player.getCombat().getDamageMap().clear();
            player.getRisk().update();
            EarningPotential.increasePotential(player);
            player.getPacketSender().sendString(PLAYERS_PKING.childId, QuestTab.InfoTab.INFO_TAB.get(PLAYERS_PKING.childId).fetchLineData(player));
            if (player.hasAttrib(AttributeKey.OVERLOAD_TASK_RUNNING)) {
                player.skills().resetStats();
                player.clearAttrib(AttributeKey.OVERLOAD_POTION);
                player.clearAttrib(AttributeKey.OVERLOAD_TASK_RUNNING);
                player.message("Your overload effect wears off upon entering the wilderness.");
                player.getPacketSender().sendEffectTimer(0, EffectTimer.OVERLOAD);
            }
        }
    }

    @Override
    public void leave(Mob mob) {
        if (mob.isPlayer()) {
            Player player = mob.getAsPlayer();
            player.putAttrib(AttributeKey.LAST_WILD_LVL, 0);
            if (!Skulling.skulled(player)) {
                // wipe skull history incase
                player.clearAttrib(AttributeKey.SKULL_ENTRIES_TRACKER);
            }
            player.getInterfaceManager().openWalkable(-1);
            player.getPacketSender().sendInteractionOption("null", 2, true);
            BountyHunter.PLAYERS_IN_WILD.remove(player);
            player.clearAttrib(AttributeKey.SPECIAL_ATTACK_USED);
            player.clearAttrib(AttributeKey.INWILD);
            player.clearAttrib(AttributeKey.PVP_WILDY_AGGRESSION_TRACKER);
            player.clearAttrib(AttributeKey.PLAYER_KILLS_WITHOUT_LEAVING_WILD);
            player.getPacketSender().sendString(PLAYERS_PKING.childId, QuestTab.InfoTab.INFO_TAB.get(PLAYERS_PKING.childId).fetchLineData(player));


            player.clearAttrib(AttributeKey.OVERLOAD_POTION);

            if (player.inventory().contains(CustomItemIdentifiers.WILDERNESS_KEY)) {
                log.info("{} - escaped with the Wilderness key.", player.getUsername());
                WildernessKeyPlugin.announceKeyEscape(player);
            }

            if (player.inventory().contains(CustomItemIdentifiers.ESCAPE_KEY)) {
                log.info("{} - made it to safety with the Escaped key.", player.getUsername());
                EscapeKeyPlugin.announceKeyEscape(player);
            }
        }
    }

    @Override
    public void process(Mob mob) {
        if (mob.isPlayer()) {
            Player player = mob.getAsPlayer();
            if (!inWilderness(player.tile())) {
                leave(player);
                return;
            }

            //If player is in the wilderness whilst holding a wildy key broadcast it!
            if (WildernessKeyPlugin.hasKey(player)) {
                if (!WildernessKeyPlugin.ESCAPED) {
                    var wildy_lvl = WildernessArea.wildernessLevel(player.tile());
                    var message = player.getUsername() + " is holding the wilderness key at wilderness level (" + wildy_lvl + ")";
                    World.getWorld().sendBroadcast(message);
                }
            }

            if (Transmogrify.isTransmogrified(player)) {
                Transmogrify.hardReset(player);
            }

            final int lvl = wildernessLevel(player.tile());
            if (lvl != player.getOrT(AttributeKey.LAST_WILD_LVL, -1)) {
                player.putAttrib(AttributeKey.LAST_WILD_LVL, lvl);
                if (lvl > 0) {
                    player.putAttrib(AttributeKey.INWILD, World.getWorld().cycleCount());
                    player.getPacketSender().sendString(53722, "Earning Potential: <col=65280>" + player.<Integer>getAttribOr(AttributeKey.EARNING_POTENTIAL, 0) + "%");
                    player.getPacketSender().sendString(53724, "Wilderness Level: <col=65280>" + lvl);
                    player.getPacketSender().sendString(199, "Level: " + lvl);
                    player.getPacketSender().sendInteractionOption("Attack", 2, true);
                }
                player.getInterfaceManager().openWalkable(53720);
            }

            player.getPacketSender().sendString(PLAYERS_PKING.childId, QuestTab.InfoTab.INFO_TAB.get(PLAYERS_PKING.childId).fetchLineData(player)) ;

            if (!BountyHunter.PLAYERS_IN_WILD.contains(player)) {
                BountyHunter.PLAYERS_IN_WILD.add(player);
                player.getPacketSender().sendString(PLAYERS_PKING.childId, QuestTab.InfoTab.INFO_TAB.get(PLAYERS_PKING.childId).fetchLineData(player));
            }
        }
    }

    @Override
    public boolean canTeleport(Player player) {
        return true;
    }

    @Override
    public boolean canAttack(Mob attacker, Mob target) {
        // Level checks only apply to PvP
        if (attacker.isPlayer() && target.isPlayer()) {

            // Is the player deep enough in the wilderness?

            var oppWithinLvl = attacker.skills().combatLevel() >= CombatFactory.getLowestLevel(target, attacker) && attacker.skills().combatLevel() <= CombatFactory.getHighestLevel(target, attacker);

            if (!oppWithinLvl) {
                attacker.message("Your level difference is too great! You need to move deeper into the Wilderness.");
                attacker.getMovementQueue().clear();
                return false;
            } else {
                var withinLvl = (target.skills().combatLevel() >= CombatFactory.getLowestLevel(attacker, target) && target.skills().combatLevel() <= CombatFactory.getHighestLevel(attacker, target));
                if (!withinLvl) {
                    attacker.message("Your level difference is too great! You need to move deeper into the Wilderness.");
                    attacker.getMovementQueue().clear();
                    return false;
                }
            }

            if (!inWilderness(target.tile())) {
                attacker.message("That player cannot be attacked, because they are not in the Wilderness.");
                attacker.getMovementQueue().clear();
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canTrade(Player player, Player target) {
        return true;
    }

    @Override
    public boolean isMulti(Mob mob) {
        return mob.<Integer>getAttribOr(AttributeKey.MULTIWAY_AREA, -1) == 1;
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
    public void defeated(Player killer, Mob mob) {
        if (mob.isPlayer()) {
            killer.getRisk().update(); // Make sure wealth attribs are up to date!
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
    public boolean inside(Mob mob) {
        if (mob instanceof Player) {
            if (mob.getAsPlayer().insideFeroxEnclaveSafe()) {
                return false;
            }
        }
        return wildernessLevel(mob.tile()) > 0;
    }

    @Override
    public boolean useInsideCheck() {
        return true; //We want to check using the inside method of this class.
    }
}
