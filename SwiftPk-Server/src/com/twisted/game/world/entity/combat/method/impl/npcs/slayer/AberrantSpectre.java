package com.twisted.game.world.entity.combat.method.impl.npcs.slayer;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.util.ItemIdentifiers;

public class AberrantSpectre extends CommonCombatMethod {

    private static final int[] DRAIN = { Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.RANGED, Skills.MAGIC};

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());
        new Projectile(mob, target, 336, 45, 5, 37, 38, 0,16, 0).sendProjectile();

        Player player = (Player) target;
// you was mean to protect you from drain? yeah that and like others like osrs. so it would be a face mask, nose peg, all that
        //those are nose peg now like if you wear it u'll be protected idk if that what u mean or no
        if(!player.getEquipment().contains(ItemIdentifiers.NOSE_PEG)  && !player.getEquipment().wearingSlayerHelm()) {
            player.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC) + 3, CombatType.MAGIC).submit();
            for (int skill : DRAIN) {
                player.skills().alterSkill(skill, -6);
            }
            player.message("<col=ff0000>The aberrant spectre's stench disorients you!");
            player.message("<col=ff0000>A nose peg can protect you from this attack.");
        } else {
            player.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), CombatType.MAGIC).checkAccuracy().submit();
        }
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 8;
    }
}
