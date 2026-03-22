package com.twisted.game.world.entity.combat.method.impl.npcs.godwars.bandos;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.method.impl.npcs.godwars.GwdLogic;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.position.Area;
import com.twisted.util.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graardor extends CommonCombatMethod {

    private static final Area BANDOS_AREA = new Area(2863, 5350, 2877, 5370);

    public static Area getBandosArea() {
        return BANDOS_AREA;
    }

    public static boolean isMinion(Npc n) {
        return n.id() >= 2216 && n.id() <= 2218;
    }

    private static Mob lastBossDamager = null;

    public static Mob getLastBossDamager() {
        return lastBossDamager;
    }

    public static void setLastBossDamager(Mob lastBossDamager) {
        Graardor.lastBossDamager = lastBossDamager;
    }

    private final List<String> QUOTES = Arrays.asList("Death to our enemies!",
        "Brargh!",
        "Break their bones!",
        "For the glory of Bandos!",
        "Split their skulls!",
        "We feast on the bones of our enemies tonight!",
        "CHAAARGE!",
        "Crush them underfoot!",
        "All glory to Bandos!",
        "GRRRAAAAAR!",
        "FOR THE GLORY OF THE BIG HIGH WAR GOD!");

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (Utils.rollDie(2, 1)) {
            mob.forceChat(Utils.randomElement(QUOTES));
        }

        int melee_distance = mob.tile().distance(target.tile());
        boolean canMelee = melee_distance <= 1;

        if (!canMelee || Utils.rollDie(3, 1)) {
            mob.animate(7021);
            new Projectile(mob, target, 1202, 25, 65, 1, 5, 0).sendProjectile();
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), 2, CombatType.RANGED).checkAccuracy().submit();
        } else {
            mob.animate(7018);
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
            // If we're in melee distance it's actually classed as if the target hit us -- has an effect on auto-retal in gwd!
            if (GwdLogic.isBoss(mob.getAsNpc().id())) {
                Map<Mob, Long> last_attacked_map = mob.getAttribOr(AttributeKey.LAST_ATTACKED_MAP, new HashMap<Mob, Long>());
                last_attacked_map.put(target, System.currentTimeMillis());
                mob.putAttrib(AttributeKey.LAST_ATTACKED_MAP, last_attacked_map);
            }
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
