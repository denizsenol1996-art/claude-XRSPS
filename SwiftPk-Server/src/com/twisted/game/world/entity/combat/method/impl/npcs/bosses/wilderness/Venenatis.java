package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.wilderness;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.method.impl.npcs.bosses.superiorbosses.Arachne;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.util.Utils;
import lombok.NonNull;

import javax.annotation.Nonnull;

import static com.twisted.util.CustomNpcIdentifiers.ARACHNE;

public class Venenatis extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob entity, Mob target) {
        if (!withinDistance(1)) {
            if (Utils.rollDie(3, 1)) rangeAttack(entity, target);
            else magicAttack(entity, target);
        } else meleeAttack(entity, target);
        return true;
    }

    public void meleeAttack(@NonNull final Mob entity, @NonNull Mob target) {
        entity.animate(9991);
        new Hit(entity, target, Utils.random(15, 30), 0, CombatType.MELEE).checkAccuracy().submit();
    }

    public void magicAttack(@Nonnull final Mob entity, @Nonnull Mob target) {
        entity.animate(9990);
        var tile = entity.tile().transform(4, 4, 0);
        var tileDist = tile.distance(target.tile());
        var duration = (tileDist * 2) + 25;
        Projectile p = new Projectile(entity.tile(), target.tile(), 2358, 25, duration, 37, 22, 14, 4, 48, 2);
        p.sendProjectile();
        target.graphic(2359, 50, p.getSpeed());
        new Hit(entity, target, Utils.random(15, 30), (int) (p.getSpeed() / 30D), CombatType.MAGIC).checkAccuracy().submit();

    }

    private void rangeAttack(@Nonnull final Mob entity, @Nonnull Mob target) {
        entity.animate(9989);
        var tile = entity.tile().transform(4, 4, 0);
        var tileDist = tile.distance(target.tile());
        var duration = (tileDist * 2) + 25;
        Projectile p = new Projectile(entity, target, 2356, duration, 25, 37, 22, 14, 4, 2);
        p.sendProjectile();
        target.graphic(2357, 0, p.getSpeed());
        new Hit(entity, target, Utils.random(15, 30), (int) (p.getSpeed() / 30D), CombatType.RANGED).checkAccuracy().submit();

    }

    public int getRotation(int tileX, int tileY, int startX, int startY, int squareWidth, int squareHeight) {
        if (tileX == startX + squareWidth && tileY == startY - 1) {
            return 0; // Bottom right corner - Rotate East
        } else if (tileX == startX - 1 && tileY == startY + squareHeight) {
            return 2; // Top left corner - Rotate West
        } else if (tileX == startX + squareWidth && tileY == startY + squareHeight) {
            return 3; // Bottom left corner - Rotate South
        } else if (tileX == startX - 1 && tileY == startY - 1) {
            return 1; // Top right corner - Rotate North
        } else if (tileX == startX - 1) {
            return 2; // West border outline - Rotate South
        } else if (tileX == startX + squareWidth) {
            return 0; // East border outline - Rotate North
        } else if (tileY == startY - 1) {
            return 1; // North border outline - Rotate West
        } else if (tileY == startY + squareHeight) {
            return 3; // South border outline - Rotate East
        }
        return 0;
    }

    @Override
    public int getAttackSpeed(@NonNull final Mob entity) {
        return entity.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(@NonNull final Mob entity) {
        return 10;
    }

    @Override
    public boolean rollSuperior(Npc npc) {
        if (World.getWorld().rollDie(25, 1)) {
            npc.transmog(ARACHNE, true);
            npc.setCombatMethod(new Arachne());
            return true;
        }
        return false;
    }
}
