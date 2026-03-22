package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.wilderness;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.magic.Autocasting;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Utils;

public class ChaosFanatic extends CommonCombatMethod {

    private static final String[] QUOTES = {
        "Burn!",
        "WEUGH!",
        "Develish Oxen Roll!",
        "All your wilderness are belong to them!",
        "AhehHeheuhHhahueHuUEehEahAH",
        "I shall call him squidgy and he shall be my squidgy!",
    };

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (!mob.isNpc() || !target.isPlayer())
            return false;

        mob.forceChat(QUOTES[Utils.getRandom(QUOTES.length)]);

        //Send the explosives!
        Npc npc = (Npc) mob;
        if (World.getWorld().rollDie(20, 1)) //5% chance the npc sends explosives
            explosives(npc, target);

        if (World.getWorld().rollDie(30, 1)) //3.3% chance of getting disarmed
            disarm(target);

        // Attack the player
        attack(npc, target);
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

    private void attack(Npc npc, Mob target) {
        var tileDist = npc.tile().distance(target.tile());
        new Projectile(npc, target, 554, 12 * tileDist, 35, 40, 25, 0, 15, 10).sendProjectile();
        var delay = Math.max(1, (20 + tileDist * 12) / 30);

        npc.animate(811);
        target.hit(npc, delay, CombatType.MAGIC).checkAccuracy().submit();
    }

    private void disarm(Mob target) {
        Player player = (Player) target;
        final Item item = player.getEquipment().get(EquipSlot.WEAPON);
        if (item != null && player.inventory().hasCapacityFor(item)) {
            player.getEquipment().remove(item, EquipSlot.WEAPON, true);
            player.getEquipment().unequip(EquipSlot.WEAPON);
            Autocasting.setAutocast(player, null);
            player.looks().resetRender();
            player.inventory().add(item);
            target.message("The fanatic disarms you!");
        }
    }

    private void explosives(Mob npc, Mob target) {
        var x = target.tile().x; //The target's x tile
        var z = target.tile().y; //The target's z tile

        //Handle the first explosive
        var explosive_one = new Tile(x + World.getWorld().random(2), z);
        var explosive_one_distance = npc.tile().distance(explosive_one);
        var explosive_one_delay = Math.max(1, (20 + explosive_one_distance * 12) / 30);

        //Handle the second explosive
        var explosive_two = new Tile(x, z + World.getWorld().random(2));
        var explosive_two_distance = npc.tile().distance(explosive_two);
        var explosive_two_delay = Math.max(1, (20 + explosive_two_distance * 12) / 30);

        //Handle the third explosive
        var explosive_three = new Tile(x, z + World.getWorld().random(2));
        var explosive_three_distance = npc.tile().distance(explosive_three);
        var explosive_three_delay = Math.max(1, (20 + explosive_three_distance * 12) / 30);

        //Send the projectiles
        new Projectile(npc.tile(), explosive_one, 0, 551, 24 * explosive_one_distance, explosive_one_delay, 50, 0, 0, 35, 10).sendProjectile();
        new Projectile(npc.tile(), explosive_two, 0, 551, 24 * explosive_two_distance, explosive_two_delay, 50, 0, 0, 35, 10).sendProjectile();
        new Projectile(npc.tile(), explosive_three, 0, 551, 24 * explosive_three_distance, explosive_three_delay, 50, 0, 0, 35, 10).sendProjectile();

        //Send the tile graphic
        World.getWorld().tileGraphic(157, explosive_one, 1, 24 * explosive_one_distance);
        World.getWorld().tileGraphic(157, explosive_two, 1, 24 * explosive_two_distance);
        World.getWorld().tileGraphic(552, explosive_three, 1, 24 * explosive_three_distance);
        //Create a delay before checking if the player is on the explosive tile
        target.runFn(6, () -> {
            //For each player in the world we..
            var target_x = target.tile().x;
            var target_z = target.tile().y;
            //Check to see if the player's tile is the same as the first explosive..
            if (target_x == explosive_one.x && target_z == explosive_one.y)
                target.hit(npc, World.getWorld().random(15), CombatType.MAGIC).submit();
            //Check to see if the player's tile is the same as the second explosive..
            if (target_x == explosive_two.x && target_z == explosive_two.y)
                target.hit(npc, World.getWorld().random(15), CombatType.MAGIC).submit();
        });
    }
}
