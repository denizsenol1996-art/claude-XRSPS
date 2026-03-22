package com.twisted.game.content.skill.impl.crafting.impl;

import com.twisted.fs.ItemDefinition;
import com.twisted.game.action.Action;
import com.twisted.game.action.policy.WalkablePolicy;
import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.dialogue.ChatBoxItemDialogue;
import com.twisted.game.world.entity.dialogue.DialogueManager;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;
import com.twisted.util.Utils;

import java.util.Arrays;
import java.util.Optional;

import static com.twisted.util.ItemIdentifiers.*;

/**
 * Handles stringing amulets.
 * @author PVE
 * @Since juli 08, 2020
 */
public class Stringing {

    /**
     * The amulet data.
     */
    public enum AmuletData {
        GOLD(GOLD_AMULET_U,GOLD_AMULET,8),
        SAPPHIRE(SAPPHIRE_AMULET_U,SAPPHIRE_AMULET,24),
        EMERALD(EMERALD_AMULET_U,EMERALD_AMULET,31),
        RUBY(RUBY_AMULET_U,RUBY_AMULET,50),
        DIAMOND(DIAMOND_AMULET_U,DIAMOND_AMULET,70),
        DRAGONSTONE(DRAGONSTONE_AMULET_U,DRAGONSTONE_AMULET,80),
        ONYX(ONYX_AMULET_U,ONYX_AMULET,90),
        ZENYTE(ZENYTE_AMULET_U,ZENYTE_AMULET,98);

        /**
         * The amulet item.
         */
        private final int ingredient;

        /**
         * The product item.
         */
        private final int product;

        /**
         * The level required.
         */
        private final int level;

        /**
         * Constructs a new <code>AmuletData</code>.
         *
         * @param ingredient The amulet item.
         * @param product    The product item.
         * @param level      The level required.
         */
        AmuletData(int ingredient, int product, int level) {
            this.ingredient = ingredient;
            this.product = product;
            this.level = level;
        }

        /**
         * Grabs the amulet data.
         *
         * @param ingredient The amulet ingredient.
         * @return The amulet data.
         */
        public static Optional<AmuletData> forAmulet(int ingredient) {
            return Arrays.stream(values()).filter(a -> a.ingredient == ingredient).findAny();
        }
    }

    /**
     * Handles using item.
     *
     * @param player The player instance.
     * @param used   The item being used.
     * @param with   The item being used with.
     */
    public static boolean useItem(Player player, Item used, Item with) {
        if (used.getId() != BALL_OF_WOOL && with.getId() != BALL_OF_WOOL) {
            return false;
        }

        Item wool = used.getId() == BALL_OF_WOOL ? used : with;
        Item amulet = wool.getId() == used.getId() ? with : used;

        if (!AmuletData.forAmulet(amulet.getId()).isPresent()) {
            return false;
        }

        AmuletData data = AmuletData.forAmulet(amulet.getId()).get();
        craft(player, data);
        return false;
    }

    /**
     * Handles crafting the amulet.
     *
     * @param player The player instance.
     * @param amulet The amulet data.
     */
    public static void craft(Player player, AmuletData amulet) {

        if (player.skills().level(Skills.CRAFTING) < amulet.level) {
            DialogueManager.sendStatement(player,"You need a crafting level of " + amulet.level + " to string this!");
            return;
        }

        if (!player.inventory().contains(amulet.ingredient) || !player.inventory().contains(1759)) {
            DialogueManager.sendStatement(player,"You do not have the required items to do this!");
            return;
        }

        ChatBoxItemDialogue.sendInterface(player, 1746, amulet.ingredient, 170);
        player.chatBoxItemDialogue = new ChatBoxItemDialogue(player) {
            @Override
            public void firstOption(Player player) {
                player.action.execute(string(player, amulet, 1), false);
            }

            @Override
            public void secondOption(Player player) {
                player.action.execute(string(player, amulet, 5), true);
            }

            @Override
            public void thirdOption(Player player) {
                player.setEnterSyntax(new EnterSyntax() {
                    @Override
                    public void handleSyntax(Player player, long input) {
                        player.action.execute(string(player, amulet, (int) input));
                    }
                });
                player.getPacketSender().sendEnterAmountPrompt("Enter amount.");
            }

            @Override
            public void fourthOption(Player player) {
                player.action.execute(string(player, amulet, 14), true);
            }
        };
    }

    /**
     * The amulet stringing action.
     *
     * @param player The player instance.
     * @param amulet The amulet data.
     * @param amount The amount beeing spun.
     * @return The spinnable action.
     */
    private static Action<Player> string(Player player, AmuletData amulet, int amount) {
        return new Action<Player>(player, 2, true) {
            int ticks = 0;

            @Override
            public void execute() {
                if (!player.inventory().contains(amulet.ingredient) || !player.inventory().contains(BALL_OF_WOOL)) {
                    DialogueManager.sendStatement(player,"You have run out of material!");
                    stop();
                    return;
                }

                player.inventory().remove(amulet.ingredient, 1);
                player.inventory().remove(BALL_OF_WOOL, 1);
                player.inventory().add(amulet.product, 1);
                ItemDefinition def = World.getWorld().definitions().get(ItemDefinition.class, amulet.ingredient);
                player.message("You string the " + def.name + " into " + Utils.getAOrAn(World.getWorld().definitions().get(ItemDefinition.class, amulet.product).name) + " " + World.getWorld().definitions().get(ItemDefinition.class, amulet.product).name + ".");

                if (++ticks == amount) {
                    stop();
                }
            }

            @Override
            public String getName() {
                return "Stringing";
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
