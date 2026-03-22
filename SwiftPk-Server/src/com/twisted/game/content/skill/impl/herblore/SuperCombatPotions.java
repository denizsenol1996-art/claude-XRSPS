package com.twisted.game.content.skill.impl.herblore;

import com.twisted.game.action.Action;
import com.twisted.game.action.policy.WalkablePolicy;
import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.content.tasks.impl.Tasks;
import com.twisted.game.world.entity.dialogue.ChatBoxItemDialogue;
import com.twisted.game.world.entity.dialogue.DialogueManager;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;

import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * juni 19, 2020
 */
public class SuperCombatPotions {

    public static boolean makePotion(Player player, Item used, Item with) {
        if ((used.getId() == SUPER_STRENGTH4 && with.getId() == SUPER_ATTACK4) || (used.getId() == SUPER_ATTACK4 && with.getId() == SUPER_STRENGTH4)) {
            makePotion(player);
            return true;
        }
        if ((used.getId() == SUPER_STRENGTH4 && with.getId() == SUPER_DEFENCE4) || (used.getId() == SUPER_DEFENCE4 && with.getId() == SUPER_STRENGTH4)) {
            makePotion(player);
            return true;
        }
        if ((used.getId() == SUPER_STRENGTH4 && with.getId() == TORSTOL) || (used.getId() == TORSTOL && with.getId() == SUPER_STRENGTH4)) {
            makePotion(player);
            return true;
        }
        if ((used.getId() == SUPER_DEFENCE4 && with.getId() == SUPER_ATTACK4) || (used.getId() == SUPER_ATTACK4 && with.getId() == SUPER_DEFENCE4)) {
            makePotion(player);
            return true;
        }
        if ((used.getId() == SUPER_DEFENCE4 && with.getId() == TORSTOL) || (used.getId() == TORSTOL && with.getId() == SUPER_DEFENCE4)) {
            makePotion(player);
            return true;
        }
        if ((used.getId() == SUPER_ATTACK4 && with.getId() == TORSTOL) || (used.getId() == TORSTOL && with.getId() == SUPER_ATTACK4)) {
            makePotion(player);
            return true;
        }
        return false;
    }

    private static void makePotion(Player player) {
        if (player.skills().xpLevel(Skills.HERBLORE) < 90) {
            DialogueManager.sendStatement(player, "You need a Herblore level of at least 90 to make this potion.");
            return;
        }

        if (!player.inventory().containsAll(SUPER_STRENGTH4, SUPER_ATTACK4, SUPER_DEFENCE4, TORSTOL)) {
            player.message("You need a super strength potion, super attack potion, super defence potion, and torsol to create a Super combat potion.");
            return;
        }

        ChatBoxItemDialogue.sendInterface(player, 1746, 170, SUPER_COMBAT_POTION4);
        player.chatBoxItemDialogue = new ChatBoxItemDialogue(player) {
            @Override
            public void firstOption(Player player) {
                player.action.execute(mix(player, 1), true);
            }

            @Override
            public void secondOption(Player player) {
                player.action.execute(mix(player, 5), true);
            }

            @Override
            public void thirdOption(Player player) {
                player.setEnterSyntax(new EnterSyntax() {
                    @Override
                    public void handleSyntax(Player player, long input) {
                        player.action.execute(mix(player, (int) input), true);
                    }
                });
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to make?");
            }

            @Override
            public void fourthOption(Player player) {
                player.action.execute(mix(player, 6), true);
            }
        };
    }

    /**
     * Handles the potion mixing action.
     */
    private static Action<Player> mix(Player player, int amount) {
        return new Action<Player>(player, 1) {
            int ticks = 0; // ye this is good uh ye oke

            @Override
            public void execute() {
                if (!player.inventory().contains(new Item(SUPER_STRENGTH4)) || !player.inventory().contains(SUPER_ATTACK4) || !player.inventory().contains(SUPER_DEFENCE4) || !player.inventory().contains(TORSTOL)) {
                    stop();
                    return;
                }

                player.inventory().replace(SUPER_STRENGTH4, VIAL, true);
                player.inventory().replace(SUPER_ATTACK4, VIAL, true);
                player.inventory().replace(SUPER_DEFENCE4, VIAL, true);
                player.inventory().remove(TORSTOL);
                player.inventory().add(new Item(SUPER_COMBAT_POTION4));
                player.sound(2608, 0);
                player.animate(363);
                player.getTaskMasterManager().increase(Tasks.MAKE_SUPER_COMBAT_POTIONS);

                player.message("You mix the torstol into your potion.");
                player.skills().addXp(Skills.HERBLORE, 150.0);

                if (++ticks == amount) {
                    stop();
                }
            }

            @Override
            public String getName() {
                return "Super combat potion";
            }

            @Override
            public boolean prioritized() {
                return false;
            }

            @Override
            public WalkablePolicy getWalkablePolicy() {
                return WalkablePolicy.NON_WALKABLE;
            }
        };
    }
}
