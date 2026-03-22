package com.twisted.game.world.entity.combat.method.impl.specials.melee;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.util.ItemIdentifiers;

public class ArmadylGodsword extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        final Player player = (Player) mob;
        int animation = 7644;
        if (player.getEquipment().contains(ItemIdentifiers.ARMADYL_GODSWORD_OR)) animation = 7645;
        player.animate(animation);
        boolean gfx_gold = player.getAttribOr(AttributeKey.AGS_GFX_GOLD, false);
        player.graphic(gfx_gold ? 1747 : 1211);
        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 1, CombatType.MELEE).checkAccuracy();
        hit.submit();
        CombatSpecial.drain(mob, CombatSpecial.ARMADYL_GODSWORD.getDrainAmount());
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 1;
    }
}
