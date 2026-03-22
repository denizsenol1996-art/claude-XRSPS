package com.twisted.game.world.entity.combat.method.impl.npcs.slayer.superiors.nechryarch;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Utils;
import com.twisted.util.timers.TimerKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Like nechryael, nechryarch can summon minions to assist it in battle. It will summon 3 chaotic death spawns, with each one attacking with magic, ranged and melee.
 * Given the fact that the chaotic spawns are more damaging, a common method is to run away when the chaotic spawns appear, and return to attack the nechryarch when they lose interest.
 * The player can also kill them, though this is only useful when bursting or barraging greater nechryael.
 *
 * Like their standard counterparts, the chaotic death spawns will not be summoned if the nechryarch is unable to attack the player back (such as when being safespotted).
 *
 * Any chaotic death spawns that the nechryarch summons will despawn when its host is killed.
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * maart 31, 2020
 */
public class Nechryarch extends CommonCombatMethod {

    private static final byte[][] BASIC_OFFSETS = new byte[][]{{0, -1}, {-1, 0}, {0, 1}, {1, 0}};
    private static final int TELEPORT_GRAPHICS = 333;
    private static final int SPECIAL_ATTACK_ANIMATION = 7549;

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        boolean spawnsAlreadySpawned = mob.getAttribOr(AttributeKey.DEATH_SPAWNS_SPAWNED, false);
        boolean canAttack = CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target);

        mob.animate(mob.attackAnimation());
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target,CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();

        if (Utils.random(5) == 0 && !spawnsAlreadySpawned && canAttack) {
            mob.animate(SPECIAL_ATTACK_ANIMATION);
            spawnMinions((Npc) mob, target);
            mob.getTimerRepository().register(TimerKey.COMBAT_ATTACK, 5); //Give the minions chance to attack the player
        }
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 2;
    }

    private void spawnMinions(Npc nechryarch, Mob target) {
        List<Npc> minions = new ArrayList<>();
        for (int index = 0; index < 3; index++) {
            final byte[] offsets = BASIC_OFFSETS[Utils.random(3)];
            Tile tile = new Tile(nechryarch.getX() + offsets[0], nechryarch.getY() + offsets[1], nechryarch.getZ());
            NechryarchDeathSpawn nechryarchDeathSpawn = new NechryarchDeathSpawn(nechryarch, target, index == 0 ? 6716 : index == 1 ? 6723 : 7649, tile, 5);
            minions.add(nechryarchDeathSpawn);
            World.getWorld().registerNpc(nechryarchDeathSpawn);
            nechryarchDeathSpawn.graphic(TELEPORT_GRAPHICS);
        }

        nechryarch.putAttrib(AttributeKey.DEATH_SPAWNS_SPAWNED, true);
        nechryarch.putAttrib(AttributeKey.MINION_LIST, minions);
    }

    @Override
    public boolean canMultiAttackInSingleZones() {
        return true;
    }
}
