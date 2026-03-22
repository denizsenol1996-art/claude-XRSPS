package com.twisted.game.world.entity.combat.method.impl.npcs.godwars.saradomin;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.method.impl.npcs.godwars.GwdLogic;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Area;
import com.twisted.util.Utils;
import com.twisted.util.timers.TimerKey;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Zilyana extends CommonCombatMethod {

    public static boolean isMinion(Npc n) {
        return n.id() >= 2206 && n.id() <= 2208;
    }

    private static final Area ENCAMPMENT = new Area(2888, 5257, 2908, 5276);

    public static Area getENCAMPMENT() {
        return ENCAMPMENT;
    }

    private static Mob lastBossDamager = null;

    public static Mob getLastBossDamager() {
        return lastBossDamager;
    }

    public static void setLastBossDamager(Mob lastBossDamager) {
        Zilyana.lastBossDamager = lastBossDamager;
    }

    private final List<String> QUOTES = Arrays.asList("Death to the enemies of the light!",
        "Slay the evil ones!",
        "Saradomin lend me strength!",
        "By the power of Saradomin!",
        "May Saradomin be my sword!",
        "Good will always triumph!",
        "Forward! Our allies are with us!",
        "Saradomin is with us!",
        "In the name of Saradomin!",
        "Attack! Find the Godsword!");

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (mob.isNpc()) {
            Npc npc = (Npc) mob;
            Player player = (Player) target;
            int melee_distance = mob.tile().distance(target.tile());
            boolean canMelee = melee_distance <= 1;
            if (Utils.rollDie(3, 1)) {
                npc.forceChat(Utils.randomElement(QUOTES));
            }

            if (canMelee) {
                mob.animate(6967);
                target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
                // If we're in melee distance it's actually classed as if the target hit us -- has an effect on auto-retal in gwd!
                if (GwdLogic.isBoss(npc.id())) {
                    Map<Mob, Long> last_attacked_map = npc.getAttribOr(AttributeKey.LAST_ATTACKED_MAP, new HashMap<Mob, Long>());
                    last_attacked_map.put(target, System.currentTimeMillis());
                    npc.putAttrib(AttributeKey.LAST_ATTACKED_MAP, last_attacked_map);
                }
            } else {
                mob.animate(6970);
                npc.getTimerRepository().extendOrRegister(TimerKey.ZILY_SPEC_COOLDOWN, 7);
                npc.getMovementQueue().clear();
                player.graphic(1221);
                target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), 2, CombatType.MAGIC).checkAccuracy().submit();
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
