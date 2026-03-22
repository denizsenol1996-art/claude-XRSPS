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

public class ZamorakGodsword extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        final Player player = (Player) mob;
        player.animate(player.getEquipment().contains(ItemIdentifiers.SARADOMIN_GODSWORD_OR) ? 7639 : 7638);
        boolean gfx_gold = player.getAttribOr(AttributeKey.ZGS_GFX_GOLD, false);
        player.graphic(gfx_gold ? 1746 : 1210);
        //TODO it.player().world().spawnSound(it.player().tile(), 3869, 0, 10)

        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE),1, CombatType.MELEE).checkAccuracy();
        hit.submit();

        if (hit.getDamage() > 0) {
            target.graphic(369);
            mob.freeze(33, target);
        }
        CombatSpecial.drain(mob, CombatSpecial.ZAMORAK_GODSWORD.getDrainAmount());
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
