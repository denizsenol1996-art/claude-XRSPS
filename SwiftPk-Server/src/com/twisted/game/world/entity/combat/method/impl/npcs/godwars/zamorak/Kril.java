package com.twisted.game.world.entity.combat.method.impl.npcs.godwars.zamorak;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.method.impl.npcs.godwars.GwdLogic;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.position.Area;
import com.twisted.util.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kril extends CommonCombatMethod {

    public static boolean isMinion(Npc n) {
        return n.id() >= 3129 && n.id() <= 3132;
    }

    private static final Area ENCAMPMENT = new Area(2918, 5318, 2936, 5331);

    public static Area getENCAMPMENT() {
        return ENCAMPMENT;
    }

    private static Mob lastBossDamager = null;

    public static Mob getLastBossDamager() {
        return lastBossDamager;
    }

    public static void setLastBossDamager(Mob lastBossDamager) {
        Kril.lastBossDamager = lastBossDamager;
    }

    private final List<String> QUOTES = Arrays.asList("Attack them, you dogs!",
        "Forward!",
        "Death to Saradomin's dogs!",
        "Kill them, you cowards!",
        "The Dark One will have their souls!",
        "Zamorak curse them!",
        "Rend them limb from limb!",
        "No retreat!");

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (Utils.rollDie(3, 1)) {
            mob.forceChat(Utils.randomElement(QUOTES));
        }

        boolean melee_dist = mob.tile().distance(target.tile()) <= 1;

        // Attack the player
        if (melee_dist && Utils.rollDie(2, 1)) {
            mob.animate(6948);
            // If we're in melee distance it's actually classed as if the target hit us -- has an effect on auto-retal in gwd!
            if (GwdLogic.isBoss(mob.getAsNpc().id())) {
                Map<Mob, Long> last_attacked_map = mob.getAttribOr(AttributeKey.LAST_ATTACKED_MAP, new HashMap<Mob, Long>());
                last_attacked_map.put(target, System.currentTimeMillis());
                mob.putAttrib(AttributeKey.LAST_ATTACKED_MAP, last_attacked_map);
            }

            if (Utils.rollDie(5, 1)) {
                int hit = CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE);
                mob.forceChat("YARRRRRRR!"); // Overrides previous quote
                target.hit(mob, hit, CombatType.MELEE).submit();
                target.message("K'ril Tsutsaroth slams through your protection prayer, leaving you feeling drained.");
                target.skills().alterSkill(Skills.PRAYER,-20);
            } else {
                target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
            }
        } else {
            new Projectile(mob, target, 1227, 25, 65, 1, 5, 0).sendProjectile();
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), 2, CombatType.MAGIC).checkAccuracy().submit();
        }

        // Slight chance of poison
        if (Utils.rollDie(10, 1)) {
            target.poison(16);
        }

        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 10;
    }
}
