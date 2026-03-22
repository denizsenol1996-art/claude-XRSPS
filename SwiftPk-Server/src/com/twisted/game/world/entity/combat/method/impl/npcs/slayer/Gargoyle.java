package com.twisted.game.world.entity.combat.method.impl.npcs.slayer;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.util.ItemIdentifiers;

/**
 * @author PVE
 * @Since augustus 05, 2020
 */
public class Gargoyle extends CommonCombatMethod {

    public static int getNormalId() {
        return 412;
    }

    public static int getCrumblingId() {
        return 412;
    }

    public static void onDeath(Npc npc) {
        npc.transmog(getNormalId());
    }

    public static void smash(Player player, Npc npc, boolean manual) {
        if (npc.getCombat().getTarget() != player) {
            player.message("That gargoyle is not fighting you.");
            return;
        }
        if (manual && npc.hp() > 9) {
            player.message("The gargoyle is not weak enough to be smashed!");
            return;
        }

        player.animate(401);
        String plural = player.getEquipment().containsAny(ItemIdentifiers.GRANITE_MAUL, ItemIdentifiers.GRANITE_MAUL_12848, ItemIdentifiers.GRANITE_MAUL_24225) ? "granite maul" : "rock hammer";
        player.message("You smash the Gargoyle with the "+plural+".");
        npc.hp(0, 0);
        npc.die();
        npc.transmog(getCrumblingId());
        npc.animate(1520);
    }

    private void basicAttack(Mob mob, Mob target) {
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
        mob.animate(mob.attackAnimation());
    }

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        basicAttack(mob, target);

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
