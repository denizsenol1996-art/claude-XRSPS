package com.twisted.game.world.entity.mob.player.skills;

import com.twisted.GameServer;
import com.twisted.fs.ItemDefinition;
import com.twisted.game.content.achievements.Achievements;
import com.twisted.game.content.achievements.AchievementsManager;
import com.twisted.game.content.areas.edgevile.Mac;
import com.twisted.game.content.events.ServerEvent;
import com.twisted.game.content.newplateau.DoubleExperience;
import com.twisted.game.content.newplateau.PoolOfWealth;
import com.twisted.game.content.skill.Skillable;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.bountyhunter.BountyHunter;
import com.twisted.game.world.entity.combat.prayer.default_prayer.DefaultPrayerData;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.Flag;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.GameMode;
import com.twisted.game.world.entity.mob.player.IronMode;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.prestige.Prestige;
import com.twisted.game.world.entity.mob.player.skills.prestige.PrestigeDialogue;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.Color;
import com.twisted.util.Utils;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static com.twisted.game.world.entity.AttributeKey.DOUBLE_EXP_TICKS;

/**
 * Created by Bart Pelle on 8/23/2014.
 */
public class Skills {

    public static final int SKILL_COUNT = 23;
    private static final int[] XP_TABLE = new int[121];
    public static boolean USE_EXPERIMENTAL_PERFORMANCE = false;
    private double[] xps = new double[SKILL_COUNT];
    private int[] levels = new int[SKILL_COUNT];
    private final Player player;
    private int combat;
    private final boolean[] dirty = new boolean[SKILL_COUNT];

    public Skills(Player player) {
        this.player = player;

        Arrays.fill(levels, 1);

        /* Hitpoints differs :) */
        xps[3] = levelToXp(10);
        levels[3] = 10;
    }

    private double expModifiers(int skill) {
        switch (skill) {
            case PRAYER -> {
                return player.<Boolean>getAttribOr(AttributeKey.HARD_EXP_MODE, false) ? 15 : player.ironMode() != IronMode.NONE || player.mode().isDarklord() ? 5 : 50.0;
            }
            case COOKING -> {
                return player.<Boolean>getAttribOr(AttributeKey.HARD_EXP_MODE, false) ? 15 : player.ironMode() != IronMode.NONE || player.mode().isDarklord() ? 5 : 30.0;
            }
            case WOODCUTTING -> {
                return player.<Boolean>getAttribOr(AttributeKey.HARD_EXP_MODE, false) ? 15 : player.ironMode() != IronMode.NONE || player.mode().isDarklord() ? 5 : 30.0;
            }
            case FLETCHING -> {
                return player.<Boolean>getAttribOr(AttributeKey.HARD_EXP_MODE, false) ? 15 : player.ironMode() != IronMode.NONE || player.mode().isDarklord() ? 5 : 30.0;
            }
            case FISHING -> {
                return player.<Boolean>getAttribOr(AttributeKey.HARD_EXP_MODE, false) ? 15 : player.ironMode() != IronMode.NONE || player.mode().isDarklord() ? 5 : 40.0;
            }
            case FIREMAKING -> {
                return player.<Boolean>getAttribOr(AttributeKey.HARD_EXP_MODE, false) ? 15 : player.ironMode() != IronMode.NONE || player.mode().isDarklord() ? 5 : 30.0;
            }
            case CRAFTING -> {
                return player.<Boolean>getAttribOr(AttributeKey.HARD_EXP_MODE, false) ? 15 : player.ironMode() != IronMode.NONE || player.mode().isDarklord() ? 5 : 30.0;
            }
            case SMITHING -> {
                return player.<Boolean>getAttribOr(AttributeKey.HARD_EXP_MODE, false) ? 15 : player.ironMode() != IronMode.NONE || player.mode().isDarklord() ? 5 : 60.0;
            }
            case MINING -> {
                return player.<Boolean>getAttribOr(AttributeKey.HARD_EXP_MODE, false) ? 35 : player.ironMode() != IronMode.NONE || player.mode().isDarklord() ? 5 : 50.0;
            }
            case HERBLORE -> {
                return player.<Boolean>getAttribOr(AttributeKey.HARD_EXP_MODE, false) ? 15 : player.ironMode() != IronMode.NONE || player.mode().isDarklord() ? 5 : 30.0;
            }
            case AGILITY -> {
                return player.<Boolean>getAttribOr(AttributeKey.HARD_EXP_MODE, false) ? 15 : player.ironMode() != IronMode.NONE || player.mode().isDarklord() ? 5 : 30.0;
            }
            case THIEVING -> {
                return player.<Boolean>getAttribOr(AttributeKey.HARD_EXP_MODE, false) ? 15 : player.ironMode() != IronMode.NONE || player.mode().isDarklord() ? 5 : 35.0;
            }
            case SLAYER -> {
                return player.<Boolean>getAttribOr(AttributeKey.HARD_EXP_MODE, false) ? 15 : player.ironMode() != IronMode.NONE || player.mode().isDarklord() ? 5 : 25.0;
            }
            case FARMING -> {
                return player.<Boolean>getAttribOr(AttributeKey.HARD_EXP_MODE, false) ? 15 : player.ironMode() != IronMode.NONE || player.mode().isDarklord() ? 5 : 55.0;
            }
            case RUNECRAFTING -> {
                return player.<Boolean>getAttribOr(AttributeKey.HARD_EXP_MODE, false) ? 15 : player.ironMode() != IronMode.NONE || player.mode().isDarklord() ? 5 : 50.0;
            }
            case HUNTER -> {
                return player.<Boolean>getAttribOr(AttributeKey.HARD_EXP_MODE, false) ? 15 : player.ironMode() != IronMode.NONE || player.mode().isDarklord() ? 5 : 30.0;
            }
        }
        return 1.0;
    }


    public void update() {
        update(false);
    }

    public void restoreLevels(double[] xp, int[] levels) {
        this.xps = xp;
        this.levels = levels;
    }

    public void update(boolean ignore) {
        if (!ignore) {
            for (int skill = 0; skill < SKILL_COUNT; skill++) {
                //Send the skill
                player.getPacketSender().updateSkill(skill, levels[skill], (int) xps[skill]);
            }

            updatePrayerText();
            recalculateCombat();

            //Update prayer orb
            player.getPacketSender().sendString(4012, "" + levels[PRAYER] + "");
            player.getPacketSender().sendString(4013, "" + xpToLevel((int) xps[PRAYER]) + "");

            //Update hp orbo
            player.getPacketSender().sendString(4016, "" + levels[HITPOINTS]);
            player.getPacketSender().sendString(4017, "" + xpToLevel((int) xps[HITPOINTS]));

            //Send total level
            player.getPacketSender().sendString(10121, "" + totalLevel());
        }
    }

    public void updatePrayerText() {
        int currentLevel = levels[PRAYER];
        int maxLevel = xpToLevel((int) xps[PRAYER]);
        player.getPacketSender().sendString(687, Color.ORANGE.tag() + currentLevel + "/" + maxLevel);
    }

    public void syncDirty() {
        for (int skill = 0; skill < SKILL_COUNT; skill++) {
            if (dirty[skill]) {
                player.getPacketSender().updateSkill(skill, levels[skill], (int) xps[skill]);
                dirty[skill] = false;
            }
        }
    }

    public void makeDirty(int skill) {
        this.makeDirty(skill, false);
    }

    public void makeDirty(int skill, boolean ignore) {
        dirty[skill] = true;

        player.getPacketSender().updateSkill(skill, levels[skill], (int) xps[skill]);

        if (player.skills().combatLevel() >= 126 && player.mode() == GameMode.TRAINED_ACCOUNT || player.mode() == GameMode.INSTANT_PKER) {//okupdate
            player.putAttrib(AttributeKey.COMBAT_MAXED, true);
        }

        //Only unlockable for trained accounts.
        if (player.mode() == GameMode.TRAINED_ACCOUNT) {
            if (totalLevel() >= 750) {
                AchievementsManager.activate(player, Achievements.SKILLER_I, 1);
            }
            if (totalLevel() >= 1000) {
                AchievementsManager.activate(player, Achievements.SKILLER_II, 1);
            }
            if (totalLevel() >= 1500) {
                AchievementsManager.activate(player, Achievements.SKILLER_III, 1);
            }
            if (totalLevel() >= Mac.TOTAL_LEVEL_FOR_MAXED) {
                AchievementsManager.activate(player, Achievements.SKILLER_IV, 1);
            }
        }

        //Update prayer orb
        player.getPacketSender().sendString(4012, "" + levels[PRAYER]);
        player.getPacketSender().sendString(4013, "" + xpToLevel((int) xps[PRAYER]));

        player.getPacketSender().sendString(4016, "" + levels[HITPOINTS]);
        player.getPacketSender().sendString(4017, "" + xpToLevel((int) xps[HITPOINTS]));

        if (!ignore) {
            player.skills().updatePrayerText();
        }
    }

    final double isDoubleExperienceEvent(double amt) {
        if (ServerEvent.isDoubleExperience()) {
            amt *= 2.0;
        }
        return amt;
    }

    /**
     * Returns the current level a stat is at, could be 50/99 for HP.
     * <br>Use XP to get the real level.
     *
     * @param skill
     * @return
     */
    public int level(int skill) {
        return levels[skill];
    }

    /**
     * Gets the level which your XP qualifies you for
     */
    public int xpLevel(int skill) {
        return xpToLevel((int) xps[skill]);
    }

    public int[] levels() {
        return levels;
    }

    public void setAllLevels(int[] levels) {
        this.levels = levels;
    }

    public double[] xp() {
        return xps;
    }

    public void setAllXps(double[] xps) {
        this.xps = xps;
    }

    public void setXp(int skill, double amt) {
        this.setXp(skill, amt, false);
    }

    public void sync() {
        update();
        recalculateCombat();
    }

    public void setXp(int skill, double amt, boolean ignore) {
        xps[skill] = Math.min(200000000, amt);
        int newLevel = xpToLevel((int) xps[skill]);
        levels[skill] = newLevel;

        if (!ignore) {
            recalculateCombat();
            makeDirty(skill);
        }
    }

    public void setLevel(int skill, int lvtemp) {
        this.setLevel(skill, lvtemp, false);
    }

    public void setLevel(int skill, int lvtemp, boolean ignore) {
        levels[skill] = lvtemp;
        if (!ignore) {
            makeDirty(skill);
        }
    }

    public boolean addXp(int skill, double amt) {
        return addXp(skill, amt, true, true);
    }

    public boolean addXp(int skill, double amt, boolean multiplied) {
        return addXp(skill, amt, multiplied, true);
    }

    public boolean addXp(int skillId, double amt, boolean multiplied, boolean counter) {
        Mob target = ((WeakReference<Mob>) player.getAttribOr(AttributeKey.TARGET, new WeakReference<>(null))).get();
        // Active target and facing. Can't tell if combat script is running.
        boolean pvp = target != null && target.isPlayer() && target.getIndex() + 32768 == (int) player.getAttribOr(AttributeKey.LAST_FACE_ENTITY_IDX, 0);
        boolean combatxp = skillId == ATTACK || skillId == STRENGTH || skillId == DEFENCE || skillId == RANGED || skillId == MAGIC || skillId == HITPOINTS;
        boolean xplocked = player.getAttribOr(AttributeKey.XP_LOCKED, false);
        boolean x1xp = player.getAttribOr(AttributeKey.X1XP, false);
        boolean inWilderness = WildernessArea.inWilderness(player.tile());

        if (combatxp && xplocked) { // don't get combat exp when locked.
            return false;
        }

        if (target != null && target.isNpc() && combatxp) { // Don't add exp if the target is hidden or locked.
            Npc npc = (Npc) target;
            if (npc.hidden() || (npc.locked() && !npc.isDamageOkLocked()))
                return false;
        }

        // Don't add the experience if it has been locked or in a tournament..
        if (player.inActiveTournament() || player.isInTournamentLobby())
            return false;

        boolean geniePet = player.hasPetOut("Genie pet");

        double prestigeModifiedAmount = Prestige.percentage(player, Skill.fromId(skillId));
        prestigeModifiedAmount *= amt;
        amt = amt - prestigeModifiedAmount;

        if (multiplied) {
            if (player.ironMode() == IronMode.NONE) {
                if (combatxp) {
                    if (!x1xp) {//if x1, no multiplier is applied.
                        amt *= player.mode().combatXpRate();
                    }
                } else {
                    amt *= expModifiers(skillId);
                }
            } else { // Iron Man mode is always x20.
                if (!(combatxp && x1xp)) {//iron men, if x1 set, don't get multipler.
                    amt *= player.ironMode() != IronMode.NONE ? 20 : player.mode().combatXpRate();
                }
            }
        }

        amt = isDoubleExperienceEvent(amt);

        //Double exp in wilderness is only in the pvp world.
        amt *= inWilderness && GameServer.properties().pvpMode ? 2.0 : 1.0;

        var double_exp_ticks = player.<Integer>getAttribOr(DOUBLE_EXP_TICKS, 0) > 0;

        var donator_zone = player.tile().memberZone() || player.tile().memberCave();

        //Genie pet gives x2 exp
        amt *= geniePet || donator_zone || double_exp_ticks ? 2.0 : 1.0;

        //World multiplier exp gives x2 exp.//up9
        //amt *= World.getWorld().xpMultiplier > 1 ? 2.0 : 1.0;
        if (PoolOfWealth.activePerk != null) {
            if (PoolOfWealth.activePerk.equalsIgnoreCase("Xp")) {//up9
                amt *= 2.0;
            }
        }

        if (DoubleExperience.isDoubleExperience()) {//up9
            amt *= 2.0;
        }

        player.getPacketSender().sendExpDrop(skillId, (int) amt, counter);

        int oldLevel = xpToLevel((int) xps[skillId]);
        xps[skillId] = Math.min(200000000, xps[skillId] + amt);
        int newLevel = xpToLevel((int) xps[skillId]);

        if (newLevel > oldLevel) {
            if (levels[skillId] < newLevel)
                levels[skillId] += newLevel - oldLevel;
            player.graphic(199, 124, 0);
        }

        makeDirty(skillId);

        //Send total level
        player.getPacketSender().sendString(10121, "" + totalLevel());

        if (oldLevel != newLevel) {
            int levels = newLevel - oldLevel;
            if (levels == 1) {
                player.message("Congratulations, you just advanced %s %s level.", SKILL_INDEFINITES[skillId], SKILL_NAMES[skillId]);
            } else {
                player.message("Congratulations, you just advanced %d %s levels.", levels, SKILL_NAMES[skillId]);
            }

            if (newLevel == 99 && player.mode() != GameMode.INSTANT_PKER) {
                if (skillId == 0) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=82><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 1) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=83><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 2) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=84><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 3) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=85><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 4) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=86><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 5) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=87><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 6) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=88><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 7) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=89><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 8) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=90><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 9) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=91><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a " + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 10) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=92><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 11) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=93><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 12) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=94><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 13) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=95><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 14) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=96><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 15) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=97><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 16) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=98><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 17) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=99><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 18) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=100><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 19) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=101><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 20) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=102><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 21) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=103><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");
                } else if (skillId == 22) {
                    World.getWorld().sendWorldMessage("<img=1897><shad=2> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col></shad> has just achieved level 99 in <img=104><shad=2>" + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col></shad> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col></shad>!");

                } else {


                    player.graphic(1388, 124, 0);
                    player.message(Color.ORANGE_RED.tag() + "Congratulations on achieving level 99 in " + SKILL_NAMES[skillId] + "!");
                    player.message(Color.ORANGE_RED.tag() + "You may now purchase a skillcape from Mac who can be found at home.");
                    World.getWorld().sendWorldMessage("<img=1897> <col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</col> has just achieved level 99 in " + Color.ORANGE.tag() + "" + SKILL_NAMES[skillId] + "</col> on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col>!");
                }
            }
            if (totalLevel() >= Mac.TOTAL_LEVEL_FOR_MAXED && player.mode() != GameMode.INSTANT_PKER) {
                World.getWorld().sendWorldMessage("<img=1897> <shad=2><col=" + Color.ORANGE.getColorValue() + ">" + player.getUsername() + "</shad></col> has just maxed out on a <shad=2>" + Color.ORANGE.tag() + " " + Utils.gameModeToString(player) + "</col>!");
            }

            recalculateCombat();

            if (player.skills().combatLevel() >= 126 && player.mode() == GameMode.TRAINED_ACCOUNT || player.mode() == GameMode.INSTANT_PKER) {//okupdate
                player.putAttrib(AttributeKey.COMBAT_MAXED, true);
            }

            if (player.mode() == GameMode.TRAINED_ACCOUNT) {
                if (totalLevel() >= 750) {
                    AchievementsManager.activate(player, Achievements.SKILLER_I, 1);
                }
                if (totalLevel() >= 1000) {
                    AchievementsManager.activate(player, Achievements.SKILLER_II, 1);
                }
                if (totalLevel() >= 1500) {
                    AchievementsManager.activate(player, Achievements.SKILLER_III, 1);
                }
                if (totalLevel() >= Mac.TOTAL_LEVEL_FOR_MAXED) {
                    AchievementsManager.activate(player, Achievements.SKILLER_IV, 1);
                }
            }

            var levelUpActive = player.<Boolean>getAttribOr(AttributeKey.LEVEL_UP_INTERFACE, false);

            if (levelUpActive) {
                if (skillId == FARMING) {
                    player.getDialogueManager().start(new Dialogue() {
                        @Override
                        protected void start(Object... options) {
                            send(DialogueType.ITEM_STATEMENT, new Item(5340), "", "Congratulations! You've just advanced Farming level!", "You have reached level " + newLevel + "!");
                            setPhase(0);
                        }
                    });
                } else if (skillId == CONSTRUCTION) {

                } else if (skillId == HUNTER) {
                    player.getDialogueManager().start(new Dialogue() {
                        @Override
                        protected void start(Object... options) {
                            send(DialogueType.ITEM_STATEMENT, new Item(9951), "", "Congratulations! You've just advanced Hunter level!", "You have reached level " + newLevel + "!");
                            setPhase(0);
                        }
                    });
                } else {
                    player.getDialogueManager().start(new Dialogue() {
                        @Override
                        protected void start(Object... parameters) {
                            player.getPacketSender().sendString(LEVEL_UP[skillId][1], "<col=128>Congratulations, you just advanced a " + SKILL_NAMES[skillId] + " level!");
                            player.getPacketSender().sendString(LEVEL_UP[skillId][2], "Your " + SKILL_NAMES[skillId] + " level is now " + newLevel + ".");
                            player.getPacketSender().sendChatboxInterface(LEVEL_UP[skillId][0]);
                            setPhase(0);
                        }

                        @Override
                        protected void next() {
                            if (isPhase(0)) {
                                stop();
                            }
                        }
                    });
                }
            }
        }
        update();//Force an update
        return oldLevel != newLevel;
    }

    private static final int[][] LEVEL_UP = {
        {6247, 6248, 6249},
        {6253, 6254, 6255},
        {6206, 6207, 6208},
        {6216, 6217, 6218},
        {4443, 5453, 6114},
        {6242, 6243, 6244},
        {6211, 6212, 6213},
        {6226, 6227, 6228},
        {4272, 4273, 4274},
        {6231, 6232, 6233},
        {6258, 6259, 6260},
        {4282, 4283, 4284},
        {6263, 6264, 6265},
        {6221, 6222, 6223},
        {4416, 4417, 4438},
        {6237, 6238, 6239},
        {4277, 4278, 4279},
        {4261, 4263, 4264},
        {12122, 12123, 12124},
        {8267, 4268, 4269}, //farming
        {4267, 4268, 4269}, //rc
        {8267, 4268, 4269}, //construction
        {8267, 4268, 4269}}; //hunter

    /**
     * Checks if the player is maxed in all combat skills.
     */
    public boolean isCombatMaxed() {
        int maxCount = 7;
        int count = 0;
        for (int index = 0; index < maxCount; index++) {
            if (player.skills().level(index) >= 99) {
                count++;
            }
        }
        return count == maxCount;
    }

    public void update(int skill) {
        makeDirty(skill);
    }

    /**
     * @param skill
     * @param change
     */
    public void alterSkill(int skill, int change) {
        levels[skill] += change;
        if (change > 0 && levels[skill] > xpLevel(skill) + change) { // Cap at realLvl (99) + boost (20) = 118
            levels[skill] = xpLevel(skill) + change;
        }
        if (levels[skill] < 0) { // Min 0
            levels[skill] = 0;
        }
        update(skill);
    }

    public void hpEventLevel(int increaseBy) {
        levels[Skills.HITPOINTS] = increaseBy;
        update(Skills.HITPOINTS);
    }

    public void overloadPlusBoost(int skill) {
        boolean fluffyPet = player.hasPetOut("Fluffy Jr");
        int boost = fluffyPet ? 16 : 6;
        int boostedLevel = (int) ((player.skills().xpLevel(skill) * 0.16) + boost);
        levels[skill] += boostedLevel;
        if (boostedLevel > 0 && levels[skill] > xpLevel(skill) + boostedLevel) { // Cap at realLvl (99) + boost (20) = 118
            levels[skill] = xpLevel(skill) + boostedLevel;
        }
        update(skill);
    }

    public void replenishSkill(int skill, int change) {
        if (levels[skill] < xpLevel(skill)) // Current level under real level
            levels[skill] = Math.min(xpLevel(skill), level(skill) + change);//cap replenish at 99
        update(skill);
    }

    public void replenishStats() {
        if (player.dead() || player.hp() < 1)
            return;

        for (int i = 0; i < SKILL_COUNT; i++) {
            if (levels[i] < xpLevel(i)) {
                levels[i]++;
                update(i);
            } else if (levels[i] > xpLevel(i)) {
                levels[i]--;
                update(i);
            }
        }
    }

    public void replenishStatsToNorm() {
        for (int i = 0; i < SKILL_COUNT; i++) {
            if (levels[i] < xpLevel(i)) {
                levels[i] = xpLevel(i);
                update(i);
            }
        }
    }

    public void resetStats() {
        for (int i = 0; i < SKILL_COUNT; i++) {
            levels[i] = xpLevel(i);
        }
        update();
    }

    public void recalculateCombat() {
        int old = combat;
        double defence = xpLevel(Skills.DEFENCE);
        double attack = xpLevel(Skills.ATTACK);
        double strength = xpLevel(Skills.STRENGTH);
        double prayer = xpLevel(Skills.PRAYER);
        double ranged = xpLevel(Skills.RANGED);
        double magic = xpLevel(Skills.MAGIC);
        double hp = xpLevel(Skills.HITPOINTS);

        int baseMelee = (int) Math.floor(0.25 * (defence + hp + Math.floor(prayer / 2d)) + 0.325 * (attack + strength));
        int baseRanged = (int) Math.floor(0.25 * (defence + hp + Math.floor(prayer / 2d)) + 0.325 * (Math.floor(ranged / 2) + ranged));
        int baseMage = (int) Math.floor(0.25 * (defence + hp + Math.floor(prayer / 2d)) + 0.325 * (Math.floor(magic / 2) + magic));
        combat = Math.max(Math.max(baseMelee, baseMage), baseRanged);

        // If our combat changed, we need to update our looks as that contains our cb level too.
        if (combat != old && player.looks() != null) {
            player.getUpdateFlag().flag(Flag.APPEARANCE);
            player.getPacketSender().sendString(19000, "Combat level: " + combat).sendString(5858, "Combat level: " + combat);
        }
    }

    public long getTotalExperience() {
        return Arrays.stream(xps).mapToLong(e -> (long) e).sum();
    }


    public int combatLevel() {
        return combat;
    }

    public int totalLevel() {
        int total = 0;

        for (int i = 0; i < xps.length; i++) {
            total += xpLevel(i);
        }

        //Max total level is 2277 in osrs however this calculates slightly over.
        if (total > 2760) {
            total = 2760;
        }

        return total;
    }

    /**
     * Converts given XP to the equivilent skill level.
     *
     * @param xp
     * @return
     */
    public static int xpToLevel(int xp) {
        if (xp >= 104_273_167)//104,273,167 exp is level 120
            return 120;

        if (xp < 83)
            return 1;

        int lvl = 1;
        for (; lvl < 121; lvl++) {
            if (xp < XP_TABLE[lvl])
                break;
        }

        return Math.min(lvl, 120);
    }

    /**
     * Converts skill level to EXP
     *
     * @param level skill id
     * @return XP equivalent to given skill level
     */
    public static int levelToXp(int level) {
        return XP_TABLE[Math.min(120, Math.max(0, level - 1))];
    }

    static {
        // Calculate XP table
        for (int lv = 1, points = 0; lv < 121; lv++) {
            points += (int) ((double) lv + 300D * Math.pow(2D, (double) lv / 7D));
            XP_TABLE[lv] = points / 4;
        }
    }

    public static final int ATTACK = 0;
    public static final int DEFENCE = 1;
    public static final int STRENGTH = 2;
    public static final int HITPOINTS = 3;
    public static final int RANGED = 4;
    public static final int PRAYER = 5;
    public static final int MAGIC = 6;
    public static final int COOKING = 7;
    public static final int WOODCUTTING = 8;
    public static final int FLETCHING = 9;
    public static final int FISHING = 10;
    public static final int FIREMAKING = 11;
    public static final int CRAFTING = 12;
    public static final int SMITHING = 13;
    public static final int MINING = 14;
    public static final int HERBLORE = 15;
    public static final int AGILITY = 16;
    public static final int THIEVING = 17;
    public static final int SLAYER = 18;
    public static final int FARMING = 19;
    public static final int RUNECRAFTING = 20;
    public static final int HUNTER = 21;
    public static final int CONSTRUCTION = 22;
    // 23 ... sailing meme skill or unreleased w.e

    public static final String[] SKILL_NAMES = {
        "Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching",
        "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer",
        "Farming", "Runecrafting", "Hunter", "Construction"
    };

    public static final String[] SKILL_INDEFINITES = {
        "an", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "an", "a", "a", "a", "a", "a", "a"
    };

    public double totalXp() {
        double xp = 0.0D;
        for (int i = 0; i < SKILL_COUNT; i++) // 23 kills yo .. skill.length is 24!
            xp += xp()[i];
        return xp;
    }

    private boolean attackLevelBoosted() {
        return level(Skills.ATTACK) > xpLevel(Skills.ATTACK);
    }

    private boolean defenceLevelBoosted() {
        return level(Skills.DEFENCE) > xpLevel(Skills.DEFENCE);
    }

    private boolean strengthLevelBoosted() {
        return level(Skills.STRENGTH) > xpLevel(Skills.STRENGTH);
    }

    private boolean rangeLevelBoosted() {
        return level(Skills.RANGED) > xpLevel(Skills.RANGED);
    }

    private boolean magicLevelBoosted() {
        return level(Skills.MAGIC) > xpLevel(Skills.MAGIC);
    }

    public boolean combatStatsBoosted() {
        return attackLevelBoosted() || defenceLevelBoosted() || strengthLevelBoosted() || rangeLevelBoosted() || magicLevelBoosted();
    }

    /**
     * Checks if the button that was clicked is used for setting a skill to a
     * desired level.
     *
     * @param button The button that was clicked.
     * @return True if a skill should be set, false otherwise.
     */
    public boolean pressedSkill(int button) {
        Skill skill = Skill.fromButton(button);

        if (skill != null) {
            if (GameServer.properties().debugMode && player.getPlayerRights().isDeveloperOrGreater(player)) {
                player.message("Clicked skill: " + skill.name());
            }

            if (CombatFactory.inCombat(player) && !player.getPlayerRights().isDeveloperOrGreater(player)) {
                player.message("You can't change your levels during combat.");
                return true;
            }

            if (!player.getEquipment().isEmpty()) {
                player.message(Color.RED.wrap("You must unequip all items before you can prestige."));
                return true;
            }

            final String skillName = skill.getName();

            final Prestige prestige = Prestige.forSkill(skill);
            if (prestige == null) {
                player.message(Color.RED.wrap("You currently cannot prestige the " + skillName + " skill."));
                return true;
            }

            final int level = player.skills().xpLevel(skill.getId());

            final int prestigeLevel = player.<Integer>getAttribOr(prestige.key, 0);

            if (prestigeLevel >= 11) {
                player.message(Color.RED.wrap("You have already reached the maximum prestige in this skill."));
                return true;
            }

            final int prestigeLevelCap = switch (prestigeLevel) {
                case 1 -> 102;
                case 2 -> 104;
                case 3 -> 106;
                case 4 -> 108;
                case 5 -> 110;
                case 6 -> 112;
                case 7 -> 114;
                case 8 -> 116;
                case 9 -> 118;
                case 10 -> 120;
                default -> 99;
            };

            if (level < prestigeLevelCap) {
                player.message(Color.RED.wrap("You need to be level " + prestigeLevelCap + " in " + skillName + " to prestige."));
                return true;
            }

            player.getDialogueManager().start(new PrestigeDialogue(player, skill, skillName));
            return true;
        }
        return false;
    }

    /**
     * Sets a skill to the desired level.
     *
     * @param skill
     * @param level
     */
    public void clickSkillToChangeLevel(int skill, int level) {
        //Make sure they are in a safe area
        if ((!player.tile().inArea(Tile.EDGEVILE_HOME_AREA) || WildernessArea.inWilderness(player.tile())) && !player.getPlayerRights().isDeveloperOrGreater(player)) {
            player.message("You can only set levels at home.");
            return;
        }

        //make sure they aren't wearing any items which arent allowed to be worn at that level.
        for (Item item : player.getEquipment().getItems()) {
            if (item == null) {
                continue;
            }

            final Map<Integer, Integer> requiredLevels = World.getWorld().equipmentInfo().requirementsFor(item.getId());

            if (requiredLevels != null) {
                final Integer requiredLevel = requiredLevels.get(skill);
                if (requiredLevel != null) {
                    player.message("<col=FF0000>You don't have the level requirements to wear: %s.", World.getWorld().definitions().get(ItemDefinition.class, item.getId()).name);
                    return;
                }
            }
        }

        if (skill == HITPOINTS) {
            if (level < 10) {
                player.message("Hitpoints must be set to at least level 10.");
                return;
            }
        }

        //Set skill level
        player.skills().setXp(skill, Skills.levelToXp(level));
        player.skills().update();
        player.skills().recalculateCombat();

        if (skill == PRAYER) {
            player.getPacketSender().sendConfig(708, Prayers.canUse(player, DefaultPrayerData.PRESERVE, false) ? 1 : 0);
            player.getPacketSender().sendConfig(710, Prayers.canUse(player, DefaultPrayerData.RIGOUR, false) ? 1 : 0);
            player.getPacketSender().sendConfig(712, Prayers.canUse(player, DefaultPrayerData.AUGURY, false) ? 1 : 0);
        }

        //Update weapon tab to send combat level etc.
        player.clearAttrib(AttributeKey.VENGEANCE_ACTIVE);
        Prayers.closeAllPrayers(player);
        BountyHunter.unassign(player);
        player.getUpdateFlag().flag(Flag.APPEARANCE);
    }

    /**
     * Starts the {@link Skillable} skill.
     *
     * @param skill
     */
    public void startSkillable(Skillable skill) {
        //Stop previous skills..
        stopSkillable();

        //Close interfaces..
        player.getInterfaceManager().close();

        //Check if we have the requirements to start this skill..
        if (!skill.hasRequirements(player)) {
            return;
        }

        //Start the skill..
        player.setSkillable(Optional.of(skill));
        skill.start(player);
    }

    /**
     * Stops the player's current skill, if they have one active.
     */
    public void stopSkillable() {
        //Stop any previous skill..
        player.getSkillable().ifPresent(e -> e.cancel(player));
        player.setSkillable(Optional.empty());
    }

    /**
     * Gets the max level for said skill.
     *
     * @param skill The skill to get max level for.
     * @return The skill's maximum level.
     */
    public int getMaxLevel(int skill) {
        return xpLevel(skill);
    }

    /**
     * Gets the max level for said skill.
     *
     * @param skill The skill to get max level for.
     * @return The skill's maximum level.
     */
    public int getMaxLevel(Skill skill) {
        return xpLevel(skill.getId());
    }

    public boolean check(int skill, int lvlReq) {
        return level(skill) >= lvlReq;
    }

    /**
     * Checks the fixed (not boosted) level.
     */
    public boolean checkFixed(int skill, int levelReq, String action) {
        if (xpLevel(skill) < levelReq) {
            player.message("You need " + Skills.SKILL_NAMES[skill] + " level of " + levelReq + " or higher to " + action + ".");
            return false;
        }
        return true;
    }

    public boolean check(int skill, int levelReq, String action) {
        if (!check(skill, levelReq)) {
            player.message("You need " + Skills.SKILL_NAMES[skill] + " level of " + levelReq + " or higher to " + action + ".");
            return false;
        }
        return true;
    }

    public boolean check(int skill, int lvlReq, int itemId, String action) {
        if (!check(skill, lvlReq)) {
            player.getDialogueManager().start(new Dialogue() {
                @Override
                protected void start(Object... parameters) {
                    send(DialogueType.ITEM_STATEMENT, new Item(itemId), "", "You need " + Skills.SKILL_NAMES[skill] + " level of " + lvlReq + " or higher to " + action + ".");
                    setPhase(0);
                }

                @Override
                protected void next() {
                    if (isPhase(0)) {
                        stop();
                    }
                }
            });
            return false;
        }
        return true;
    }

    public boolean check(int skill, int lvlReq, int itemId1, int itemId2, String action) {
        if (!check(skill, lvlReq)) {
            player.getDialogueManager().start(new Dialogue() {
                @Override
                protected void start(Object... parameters) {
                    send(DialogueType.DOUBLE_ITEM_STATEMENT, new Item(itemId1), new Item(itemId2), "You need " + Skills.SKILL_NAMES[skill] + " level of " + lvlReq + " or higher to " + action + ".");
                    setPhase(0);
                }

                @Override
                protected void next() {
                    if (isPhase(0)) {
                        stop();
                    }
                }
            });
            return false;
        }
        return true;
    }
}
