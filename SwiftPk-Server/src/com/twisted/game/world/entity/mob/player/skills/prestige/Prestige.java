package com.twisted.game.world.entity.mob.player.skills.prestige;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skill;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;
import com.twisted.util.Color;
import org.apache.commons.lang.ArrayUtils;

import java.util.Map;

import static com.twisted.game.world.entity.mob.player.skills.prestige.PrestigeUtils.LOCKED_EQUIPMENT;

/**
 * An enumeration that contains all the prestige skills and their respective attribute keys.
 * <p>
 *     The prestige level can be any integer value 1-10.
 *     The prestige level is used to calculate the experience decrease for the given skill.
 *     The prestige level is also used to determine if the player can equip certain items.
 *     The prestige level is increased by 1 when the player reaches level 99 in the given skill.
 *     The prestige level is reset to 1 when the player reaches level 99 in the given skill.
 *     The prestige level is stored in the player's attributes.
 *     The prestige level is used to determine if the player can equip certain items.
 *     The prestige level is used to calculate the experience decrease for the given skill.
 *</p>
 */
public enum Prestige {

    ATTACK(Skill.ATTACK, AttributeKey.ATTACK_PRESITGE),
    STRENGTH(Skill.STRENGTH, AttributeKey.STRENGTH_PRESTIGE),
    DEFENCE(Skill.DEFENCE, AttributeKey.DEFENCE_PRESTIGE),
    RANGED(Skill.RANGED, AttributeKey.RANGED_PRESTIGE),
    PRAYER(Skill.PRAYER, AttributeKey.PRAYER_PRESTIGE),
    MAGIC(Skill.MAGIC, AttributeKey.MAGIC_PRESTIGE),
    HITPOINTS(Skill.HITPOINTS, AttributeKey.HITPOINTS_PRESTIGE),
    RUNECRAFTING(Skill.RUNECRAFTING, AttributeKey.RUNECRAFTING_PRESTIGE),
    AGILITY(Skill.AGILITY, AttributeKey.AGILITY_PRESTIGE),
    HERBLORE(Skill.HERBLORE, AttributeKey.HERBLORE_PRESTIGE),
    THIEVING(Skill.THIEVING, AttributeKey.THIEVING_PRESTIGE),
    CRAFTING(Skill.CRAFTING, AttributeKey.CRAFTING_PRESTIGE),
    FLETCHING(Skill.FLETCHING, AttributeKey.FLETCHING_PRESTIGE),
    SLAYER(Skill.SLAYER, AttributeKey.SLAYER_PRESTIGE),
    HUNTER(Skill.HUNTER, AttributeKey.HUNTER_PRESTIGE),
    MINING(Skill.MINING, AttributeKey.MINING_PRESTIGE),
    SMITHING(Skill.SMITHING, AttributeKey.SMITHING_PRESTIGE),
    FISHING(Skill.FISHING, AttributeKey.FISHING_PRESTIGE),
    COOKING(Skill.COOKING, AttributeKey.COOKING_PRESTIGE),
    FIREMAKING(Skill.FIREMAKING, AttributeKey.FIREMAKING_PRESTIGE),
    WOODCUTTING(Skill.WOODCUTTING, AttributeKey.WOODCUTTING_PRESTIGE);

    public static final Prestige[] VALUES = values();

    public final Skill skill;
    public final AttributeKey key;

    /**
     * Constructs a new {@link Prestige} with the given skill and attribute key.
     * @param skill
     * @param key
     */
    Prestige(final Skill skill, final AttributeKey key) {
        this.skill = skill;
        this.key = key;
    }

    /**
     * Returns the prestige for the given skill.
     * @param skill
     * @return
     */
    public static Prestige forSkill(final Skill skill) {
        for (final Prestige prestige : VALUES) {
            if (prestige.skill == skill)
                return prestige;
        }
        return null;
    }

    /**
     * Checks if the player has the prestige requirement to equip the item.
     * @param player
     * @param item
     * @return
     */
    public static boolean[] hasPrestigeRequirement(final Player player, final Item item) {
        final Map<Integer, Integer> cachedRequirements = World.getWorld().equipmentInfo().requirementsFor(item.getId());
        if (!ArrayUtils.contains(LOCKED_EQUIPMENT[0], item.getId())) {
            return new boolean[]{false};
        }

        if (cachedRequirements != null && !cachedRequirements.isEmpty()) {
            for (final var skill : cachedRequirements.keySet()) {
                final Prestige prestige = forSkill(Skill.fromId(skill));
                if (prestige == null)
                    continue;

                for (final int[] cache : LOCKED_EQUIPMENT) {
                    final int itemId = cache[0];
                    if (itemId <= 0)
                        continue;

                    final int prestigeLevel = cache[1];
                    if (item.getId() != itemId)
                        continue;

                    if (player.<Integer>getAttribOr(prestige.key, 0) < prestigeLevel) {
                        player.message(Color.RED.wrap("You must have a prestige level of " + prestigeLevel + " in the " + prestige.skill.getName() + " skill to equip this item."));
                        return new boolean[]{true};
                    }
                }
            }
        }
        return new boolean[]{false};
    }

    /**
     * Increases the prestige level for the given skill.
     * @param player
     * @param skill
     */
    public static void increase(final Player player, final Skill skill) {
        final int skillId = skill.getId();
        final Skills skills = player.skills();
        if (skills.xpLevel(skillId) < 99) {
            player.message(Color.RED.wrap("You must have 99 " + skill.getName() + " to prestige."));
            return;
        }

        final Prestige prestige = forSkill(skill);
        if (prestige == null)
            return;

        final int state = player.<Integer>getAttribOr(prestige.key, 0) + 1;
        final int level = skillId == Skill.HITPOINTS.getId() ? 10 : 1;
        player.putAttrib(prestige.key, state);
        skills.setXp(skillId, Skills.levelToXp(level));
        skills.sync();
    }

    /**
     * Returns the percentage decrease for the given skill.
     * @param player
     * @param skill
     * @return
     */
    public static double percentage(final Player player, Skill skill) {
        return calculate(player, skill);
    }

    public int getRequiredLevelForTier(int tier) {
        int totalLevels = 120;
        int startLevel = 105;
        int tiers = 10;
        int step = (totalLevels - startLevel) / (tiers - 1);
        return startLevel + step * (tier - 1);
    }

    /**
     * Calculates the prestige experience decrease for the given skill.
     * @param player
     * @param skill
     * @return
     */
    public static double calculate(final Player player, final Skill skill) {
        final Prestige prestige = forSkill(skill);
        if (prestige == null)
            return 0.0;

        final int prestigeLevel = player.<Integer>getAttribOr(prestige.key, 0);
        return switch (prestigeLevel) {
            case 1 -> 0.03;
            case 2 -> 0.06;
            case 3 -> 0.09;
            case 4 -> 0.12;
            case 5 -> 0.15;
            case 6 -> 0.18;
            case 7 -> 0.21;
            case 8 -> 0.24;
            case 9 -> 0.27;
            case 10 -> 0.30;
            default -> 0.0;
        };
    }
}
