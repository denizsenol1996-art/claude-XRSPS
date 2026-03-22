package com.twisted.game.content.item_forging;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Color;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.twisted.game.world.entity.AttributeKey.LAST_ENCHANT_SELECTED;

/**
 * This class represents the item forging table.
 *
 * @author Patrick van Elderen | 16 okt. 2019 : 09:49
 * @see <a href="https://github.com/Patrick9-10-1995">Github profile</a>
 */
public class ItemForgingTable extends PacketInteraction {

    private static final int INTERFACE = 69000;
    private static final int REQUIRED_ITEMS_CONTAINER = 69016;
    private static final int SUCCESS_RATE_STRING = 69008;
    private static final int ENCHANTED_ITEM_REWARD = 69009;
    public static final int START_OF_FORGE_LIST = 69021;
    private static final int ITEM_SCROLL_ID = 69020;

    private void clear(Player player) {
        //Clear out old text
        for (int index = 0; index < 50; index++) {
            player.getPacketSender().sendString(START_OF_FORGE_LIST + index, "");
        }
    }

    public void open(Player player, ItemForgingCategory tier) {
        player.getInterfaceManager().open(INTERFACE);

        //We're viewing the items for tier...
        player.putAttrib(AttributeKey.VIEWING_FORGING_CATEGORY, tier);

        //Clear previous data
        clear(player);

        final List<ItemForgement> items = ItemForgement.sortByTier(tier);
        final int size = items.size();

        player.getPacketSender().sendScrollbarHeight(ITEM_SCROLL_ID, size * 14 + 5);

        for (int index = 0; index < size; index++) {
            final ItemForgement itemForgement = items.get(index);
            player.getPacketSender().sendString(START_OF_FORGE_LIST + index, itemForgement.name);
        }

        //Open first enchantment in the list
        loadInfo(player, ItemForgement.ARMADYL_GODSWORD);
        player.getPacketSender().setClickedText(START_OF_FORGE_LIST,true);
    }

    private void loadInfo(Player player, ItemForgement itemForgement) {
        //Write the success rate
        String rate = "Success rate: "+itemForgement.successRate+"%";
        player.getPacketSender().sendString(SUCCESS_RATE_STRING, rate);

        //Write the required items
        player.getPacketSender().sendItemOnInterface(REQUIRED_ITEMS_CONTAINER, itemForgement.requiredItems);

        //Write the reward
        player.getPacketSender().sendItemOnInterfaceSlot(ENCHANTED_ITEM_REWARD, itemForgement.enchantedItem, 0);
    }

    private void forge(Player player, ItemForgement itemForgement) {//upos

        boolean[] missingItems = new boolean[1];
        AtomicReference<String> itemsMissing = new AtomicReference<>("");
        Arrays.stream(itemForgement.requiredItems).forEach(requiredItem -> {
            if (!player.inventory().containsAll(requiredItem)) {
                //System.out.println("missing items ye");
                itemsMissing.set(requiredItem.unnote().name());
                missingItems[0] = true;
            }
        });

        //Check if we actually have the required items to enchant.
        if (missingItems[0]) {
            player.message(Color.RED.tag() + "You do not have the required items to attempt this enchantment.");
            player.message("Required item: " + itemsMissing);
            return;
        }

        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... options) {
                send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Attempt to sacrifice items for an enchantment.", "Would you like to purchase insurance on this item", "Nevermind.");
                setPhase(0);
            }

            @Override
            public void select(int option) {
                if (isPhase(0)) {
                    if (option == 1) {
                        if (!player.inventory().containsAll(itemForgement.requiredItems)) {
                            stop();
                            return;
                        }

                        player.message("You attempt the enchantment...");

                        //Calculate the chance for succeeding
                        int chance = itemForgement.successRate;

                        Arrays.stream(itemForgement.requiredItems).forEach(item -> player.inventory().removeAll(item));

                        var attempts = player.<Integer>getAttribOr(itemForgement.attempts, 0) + 1;
                        player.putAttrib(itemForgement.attempts, attempts);

                        //Roll
                        if (Utils.percentageChance(chance)) {
                            //Succesfully enchanted

                            //Add enchanted item to inventory or bank
                            player.inventory().addOrBank(itemForgement.enchantedItem);

                            //Send message
                            var totalAttempts = player.<Integer>getAttribOr(itemForgement.attempts, 0);
                            player.message("<col=" + Color.GREEN.getColorValue() + ">You successfully enchanted your item.");
                            World.getWorld().sendWorldMessage("<img=1046>[<col=" + Color.MEDRED.getColorValue() + ">Item enchantment</col>]: " + Color.BLUE.tag() + "" + player.getUsername() + "</col> enchanted an " + Color.HOTPINK.tag() + "" + itemForgement.enchantedItem.name() + "</col>. (Try : " + totalAttempts + ")");
                        } else {
                            //Failed
                            player.message("<col=" + Color.MEDRED.getColorValue() + ">You tried to enchant your item but sadly failed. Try again next time.");
                        }
                        stop();
                    } else if (option == 2) {
                        if (itemForgement.enchantedItem.getId() == CustomItemIdentifiers.SANGUINE_TWISTED_BOW
                            || itemForgement.enchantedItem.getId() == CustomItemIdentifiers.SANGUINE_SCYTHE_OF_VITUR) {
                            if (!player.inventory().contains(CustomItemIdentifiers.HAZY_COINS, 20000)) {
                                player.message("You need 20k Hazy coins to do that!");
                                stop();
                                return;
                            }
                        }
                        if (!player.inventory().contains(CustomItemIdentifiers.HAZY_COINS, 5000) && itemForgement.enchantedItem.getId() != CustomItemIdentifiers.SANGUINE_TWISTED_BOW || itemForgement.enchantedItem.getId() == CustomItemIdentifiers.SANGUINE_SCYTHE_OF_VITUR) {
                            player.message("You need 5k Hazy coins to do that!");
                            stop();
                            return;
                        }
                        if (!player.inventory().containsAll(itemForgement.requiredItems)) {
                            stop();
                            return;
                        }


                        player.message("You attempt the enchantment...");

                        //Calculate the chance for succeeding
                        int chance = itemForgement.successRate;
                        var attempts = player.<Integer>getAttribOr(itemForgement.attempts, 0) + 1;
                        player.putAttrib(itemForgement.attempts, attempts);

                        //Roll
                        if (Utils.percentageChance(chance)) {
                            //Succesfully enchanted

                            //Add enchanted item to inventory or bank
                            player.inventory().addOrBank(itemForgement.enchantedItem);

                            //Send message
                            var totalAttempts = player.<Integer>getAttribOr(itemForgement.attempts, 0);
                            player.message("<col=" + Color.GREEN.getColorValue() + ">You successfully enchanted your item.");
                            //insurance on successfully code

                            final int SANGUINE_TWISTED_BOW_ID = CustomItemIdentifiers.SANGUINE_TWISTED_BOW;
                            final int SANGUINE_SCYTHE_OF_VITUR_ID = CustomItemIdentifiers.SANGUINE_SCYTHE_OF_VITUR;
                            final int HAZY_COINS_10K_ON_SUCCESSFULLY = 10000;
                            final int HAZY_COINS_2_5K_ON_FAIL = 2500;

                            int itemId = itemForgement.enchantedItem.getId();
                            int coinsToRemove = (itemId == SANGUINE_TWISTED_BOW_ID || itemId == SANGUINE_SCYTHE_OF_VITUR_ID) ? HAZY_COINS_10K_ON_SUCCESSFULLY : HAZY_COINS_2_5K_ON_FAIL;

                            player.getInventory().remove(CustomItemIdentifiers.HAZY_COINS, coinsToRemove);
                            String message = (coinsToRemove == HAZY_COINS_10K_ON_SUCCESSFULLY) ? "10K" : "2.5K";
                            player.message(Color.ROYAL.wrap("[Frog] We have removed " + message + " Hazy coins from your inventory."));
                            Arrays.stream(itemForgement.requiredItems).forEach(item -> player.inventory().removeAll(item));
                            World.getWorld().sendWorldMessage("<img=1046>[<col=" + Color.MEDRED.getColorValue() + ">Item enchantment</col>]: " + Color.BLUE.tag() + "" + player.getUsername() + "</col> enchanted an " + Color.HOTPINK.tag() + "" + itemForgement.enchantedItem.name() + "</col>. (Try : " + totalAttempts + ")");
                        } else {
                            //insurance on fail code
                            //Failed
                            final int SANGUINE_TWISTED_BOW_ID = CustomItemIdentifiers.SANGUINE_TWISTED_BOW;
                            final int SANGUINE_SCYTHE_OF_VITUR_ID = CustomItemIdentifiers.SANGUINE_SCYTHE_OF_VITUR;
                            final int HAZY_COINS_20K = 20000;
                            final int HAZY_COINS_5K = 5000;

                            int itemId = itemForgement.enchantedItem.getId();
                            int coinsToRemove = (itemId == SANGUINE_TWISTED_BOW_ID || itemId == SANGUINE_SCYTHE_OF_VITUR_ID) ? HAZY_COINS_20K : HAZY_COINS_5K;

                            player.getInventory().remove(CustomItemIdentifiers.HAZY_COINS, coinsToRemove);
                            String message = (coinsToRemove == HAZY_COINS_20K) ? "20K" : "5K";
                            player.message(Color.ROYAL.wrap("[Frog] We have removed " + message + " Hazy coins from your inventory."));
                            player.message("<col=" + Color.MEDRED.getColorValue() + ">You tried to enchant your item but sadly failed. Try again next time.");

                        }
                        stop();
                    } else if (option == 3) {
                        stop();
                    }
                }
            }
        });
    }


    @Override
    public boolean handleButtonInteraction(Player player, int button) {
        if (button == 69006) {
            ItemForgement itemForgement = null;
            button = player.getAttribOr(LAST_ENCHANT_SELECTED, -1); //Override button
            if (player.getAttribOr(AttributeKey.VIEWING_FORGING_CATEGORY, null) == ItemForgingCategory.WEAPON) {
                itemForgement = WEAPON_CATEGORY_BUTTONS.get(button);
            }

            if (player.getAttribOr(AttributeKey.VIEWING_FORGING_CATEGORY, null) == ItemForgingCategory.ARMOUR) {
                itemForgement = ARMOUR_CATEGORY_BUTTONS.get(button);
            }

            if (player.getAttribOr(AttributeKey.VIEWING_FORGING_CATEGORY, null) == ItemForgingCategory.MISC) {
                itemForgement = MISC_CATEGORY_BUTTONS.get(button);
            }

            if (itemForgement != null) {
                forge(player, itemForgement);
            }
            return true;
        }

        if (button == 69010) {
            open(player, ItemForgingCategory.WEAPON);
            loadInfo(player, ItemForgement.ARMADYL_GODSWORD);
            player.getPacketSender().setClickedText(START_OF_FORGE_LIST, true);
            player.getPacketSender().sendString(68004, "Tier I");
            return true;
        }

        if (button == 69011) {
            open(player, ItemForgingCategory.ARMOUR);
            loadInfo(player, ItemForgement.AMULET_OF_FURY);
            player.getPacketSender().setClickedText(START_OF_FORGE_LIST, true);
            player.getPacketSender().sendString(68004, "Tier II");
            return true;
        }

        if (button == 69012) {
            open(player, ItemForgingCategory.MISC);
            loadInfo(player, ItemForgement.LARRANS_KEY_TIER_II);
            player.getPacketSender().setClickedText(START_OF_FORGE_LIST, true);
            player.getPacketSender().sendString(68004, "Tier III");
            return true;
        }

        if (player.getAttribOr(AttributeKey.VIEWING_FORGING_CATEGORY, null) == ItemForgingCategory.WEAPON && WEAPON_CATEGORY_BUTTONS.containsKey(button)) {
            loadInfo(player, WEAPON_CATEGORY_BUTTONS.get(button));
            player.putAttrib(LAST_ENCHANT_SELECTED, button);
            player.getPacketSender().setClickedText(button, true);
            return true;
        }

        if (player.getAttribOr(AttributeKey.VIEWING_FORGING_CATEGORY, null) == ItemForgingCategory.ARMOUR && ARMOUR_CATEGORY_BUTTONS.containsKey(button)) {
            loadInfo(player, ARMOUR_CATEGORY_BUTTONS.get(button));
            player.putAttrib(LAST_ENCHANT_SELECTED, button);
            player.getPacketSender().setClickedText(button, true);
            return true;
        }

        if (player.getAttribOr(AttributeKey.VIEWING_FORGING_CATEGORY, null) == ItemForgingCategory.MISC && MISC_CATEGORY_BUTTONS.containsKey(button)) {
            loadInfo(player, MISC_CATEGORY_BUTTONS.get(button));
            player.putAttrib(LAST_ENCHANT_SELECTED, button);
            player.getPacketSender().setClickedText(button, true);
            return true;
        }
        return false;
    }

    private static final HashMap<Integer, ItemForgement> WEAPON_CATEGORY_BUTTONS = new HashMap<>();
    private static final HashMap<Integer, ItemForgement> ARMOUR_CATEGORY_BUTTONS = new HashMap<>();
    private static final HashMap<Integer, ItemForgement> MISC_CATEGORY_BUTTONS = new HashMap<>();

    //Store all buttons in a hashmap
    static {
        int button;
        button = START_OF_FORGE_LIST;

        for (final ItemForgement itemForgement : ItemForgement.sortByTier(ItemForgingCategory.WEAPON)) {
            WEAPON_CATEGORY_BUTTONS.put(button++, itemForgement);
        }
        button = START_OF_FORGE_LIST;
        for (final ItemForgement itemForgement : ItemForgement.sortByTier(ItemForgingCategory.ARMOUR)) {
            ARMOUR_CATEGORY_BUTTONS.put(button++, itemForgement);
        }
        button = START_OF_FORGE_LIST;
        for (final ItemForgement itemForgement : ItemForgement.sortByTier(ItemForgingCategory.MISC)) {
            MISC_CATEGORY_BUTTONS.put(button++, itemForgement);
        }
    }
}
