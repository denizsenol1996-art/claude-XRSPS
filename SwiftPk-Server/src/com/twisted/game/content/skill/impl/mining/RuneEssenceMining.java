package com.twisted.game.content.skill.impl.mining;

import com.twisted.game.action.impl.UnwalkableAction;
import com.twisted.game.world.entity.dialogue.DialogueManager;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.object.GameObject;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.chainedwork.Chain;

import java.util.Optional;

import static com.twisted.util.ObjectIdentifiers.RUNE_ESSENCE_34773;

/**
 * Created by Bart on 10/27/2015.
 */
public class RuneEssenceMining extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            if (obj.getId() == RUNE_ESSENCE_34773) {
                mineEssence(player);
                return true;
            }
            if (obj.getId() == EXIT_PORTAL) {
                player.lockNoDamage();
                player.graphic(110, 124, 30);
                player.message("You step through the portal.");
                Chain.bound(player).runFn(2, () -> {
                    player.teleport(3253, 3401);
                    player.unlock();
                });
                return true;
            }
        }
        return false;
    }

    private static final int EXIT_PORTAL = 7479;

    private int power(Player player, Mining.Pickaxe pick) {
        double points = ((player.skills().level(Skills.MINING) - 1) + 1 + pick.points);
        return (int) Math.min(80, points);
    }

    private int ticks(int power) {
        return 2 + power >= 0 && power <= 14 ? 3 // Total of 5 (3.0s)
            : power >= 15 && power <= 29 ? 2 // Total of 4
            : power >= 30 && power <= 50 ? 1 // Total of 3
            : 0;// Total of 2 (1.2s)
    }

    private void mineEssence(Player player) {
        Optional<Mining.Pickaxe> pick = Mining.findPickaxe(player);
        
        if (pick.isEmpty()) {
            player.sound(2277, 0);
            DialogueManager.sendStatement(player,"You need a pickaxe to mine this rock. You do not have a pickaxe", "which you have the Mining level to use.");
            return;
        }

        if (player.inventory().isFull()) {
            player.sound(2277, 0);
            player.message("Your inventory is too full to hold any more rune stones.");
            player.animate(-1);
            return;
        }

        Chain.bound(player).runFn(1, () -> player.message("You swing your pick at the rock."));

        player.action.execute(new UnwalkableAction(player, 1) {
            int ticks = 1; // ticks at start
            @Override
            protected void execute() {
                ticks--;

                if (ticks == 0) {
                    if (player.inventory().isFull()) {
                        player.sound(2277, 0);
                        player.message("Your inventory is too full to hold any more rune stones.");
                        player.animate(-1);
                        return;
                    }

                    player.animate(pick.get().anim);
                    Chain.bound(player).runFn(ticks(power(player, pick.get())), () -> {
                        player.inventory().add(new Item(player.skills().level(Skills.MINING) >= 30 ? 7936 : 1436));
                        player.skills().addXp(Skills.MINING, 5.0);
                    });
                }

                if (ticks == 0) {
                    ticks = 4;
                }
            }
        });
    }


}
