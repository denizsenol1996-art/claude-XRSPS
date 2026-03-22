package com.twisted.game.content.mechanics.item_dispenser;

import com.twisted.game.GameConstants;
import com.twisted.game.content.mechanics.item_simulator.ItemSimulatorUtility;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Color;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;
import com.twisted.util.Utils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.regex.Pattern;

import static com.twisted.game.world.entity.AttributeKey.*;

/**
 * The item dispenser is an object which crushes all your items into Ferox coins.
 *
 * @author Patrick van Elderen | February, 14, 2021, 23:12
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class ItemDispenser {

    private static final Logger dispenserLogs = LogManager.getLogger("DispenserLogs");
    private static final Level DISPENSER;

    static {
        DISPENSER = Level.getLevel("DISPENSER");
    }

    /**
     * The Player instance of this class.
     */
    private final Player player;

    public ItemDispenser(Player player) {
        this.player = player;
    }

    public void checkCart() {
        player.getInterfaceManager().open(ItemSimulatorUtility.WIDGET_ID);
        player.getPacketSender().sendString(ItemSimulatorUtility.WIDGET_TITLE_ID,"Items to dispense");
        var totalValue = player.<Integer>getAttribOr(CART_ITEMS_TOTAL_VALUE, 0);
        var items = player.<ArrayList<Item>>getAttribOr(CART_ITEMS, new ArrayList<Item>());
        player.getPacketSender().sendString(ItemSimulatorUtility.SUB_TITLE_ID,"Total: "+Utils.formatNumber(totalValue)+" "+GameConstants.SERVER_NAME+" coins");
        player.getPacketSender().sendItemOnInterface(ItemSimulatorUtility.DISPENSER_CONTAINER_ID, items);
    }

    /**
     * Adds items into the cart, which will be dispensed later on.
     * @param src The item to dispense.
     */
    public void addItemToCart(Item src) {
        Optional<Cart> cart = Cart.get(src.getId());

        if(cart.isEmpty()) {
            player.message("You can't dispense your "+src.unnote().name()+".");
            return;
        }

        int amt = player.inventory().count(src.getId());

        final Item item = src.copy();

        if(!player.inventory().contains(item)) {
            return;
        }

        if(amt > 1) {
            player.getDialogueManager().start(new Dialogue() {
                @Override
                protected void start(Object... parameters) {
                    send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Deposit all.", "Nevermind.");
                    setPhase(0);
                }

                @Override
                protected void select(int option) {
                    if(isPhase(0)) {
                        if(option == 1) {
                            if(!player.inventory().contains(item)) {
                                stop();
                                return;
                            }
                            //Change the item amount
                            item.setAmount(amt);
                            store(item);
                            stop();
                        } else if ((option == 2)) {
                            stop();
                        }
                    }
                }
            });
            return;
        }

        store(item);
    }

    static final int[] pets = new int[]{ItemIdentifiers.TZREKZUK, CustomItemIdentifiers.FOUNDER_IMP, CustomItemIdentifiers.BLOOD_FIREBIRD, CustomItemIdentifiers.GRIM_REAPER_PET, CustomItemIdentifiers.JALTOK_JAD, CustomItemIdentifiers.BABY_ABYSSAL_DEMON, CustomItemIdentifiers.SKORPIOS_PET, 30056, CustomItemIdentifiers.ARACHNE_PET, ItemIdentifiers.LITTLE_NIGHTMARE, ItemIdentifiers.JALNIBREK, ItemIdentifiers.PET_ZILYANA,ItemIdentifiers.PET_KREEARRA,ItemIdentifiers.PET_SNAKELING,ItemIdentifiers.PET_KRAKEN,ItemIdentifiers.PET_GENERAL_GRAARDOR,ItemIdentifiers.PET_KRIL_TSUTSAROTH,ItemIdentifiers.PET_CHAOS_ELEMENTAL,ItemIdentifiers.PET_SMOKE_DEVIL,ItemIdentifiers.PET_DARK_CORE,ItemIdentifiers.PET_CORPOREAL_CRITTER,ItemIdentifiers.PET_DAGANNOTH_PRIME,ItemIdentifiers.PET_DAGANNOTH_REX,ItemIdentifiers.PET_DAGANNOTH_SUPREME,ItemIdentifiers.PET_DAGANNOTH_PRIME,ItemIdentifiers.JALNIBREK,CustomItemIdentifiers.ANCIENT_BARRELCHEST_PET,CustomItemIdentifiers.ANCIENT_CHAOS_ELEMENTAL_PET,CustomItemIdentifiers.ANCIENT_KING_BLACK_DRAGON_PET,CustomItemIdentifiers.JAWA_PET,CustomItemIdentifiers.PET_CORRUPTED_NECHRYARCH,CustomItemIdentifiers.PET_ZOMBIES_CHAMPION,CustomItemIdentifiers.PET_MYSTERY_BOX,CustomItemIdentifiers.PET_GENERAL_GRAARDOR_BLACK,CustomItemIdentifiers.PET_KRIL_TSUTSAROTH_BLACK,CustomItemIdentifiers.PET_KREE_ARRA_WHITE,CustomItemIdentifiers.PET_ZILYANA_WHITE,CustomItemIdentifiers.SKELETON_HELLHOUND_PET,CustomItemIdentifiers.BLOOD_MONEY_PET,CustomItemIdentifiers.EPIC_PET_BOX,CustomItemIdentifiers.DOPPELGANGER_PET,CustomItemIdentifiers.DEMENTOR_PET,CustomItemIdentifiers.DHAROK_PET,CustomItemIdentifiers.BARRELCHEST_PET,CustomItemIdentifiers.SKELETON_HELLHOUND_PET,CustomItemIdentifiers.GENIE_PET,CustomItemIdentifiers.ZAWEKS_PET,CustomItemIdentifiers.KERBEROS_PET};

    private void store(Item src) {
        final Item item = src.copy();

        if(!player.inventory().contains(item)) {
            return;
        }

        player.stopActions(true);

        //Do some animation
        player.animate(832);

        //Get the current stored item list
        var items = player.<ArrayList<Item>>getAttribOr(CART_ITEMS, new ArrayList<Item>());

        if (items != null) {
            //Store the items in it's respective attribute
            Optional<Item> any = items.stream().filter(i -> i.matchesId(item.getId())).findAny();
            if (any.isPresent() && ((1L * any.get().getAmount()) + item.getAmount() <= Integer.MAX_VALUE)) {
                any.get().setAmount(any.get().getAmount() + item.getAmount());

                if(Cart.get(any.get().getId()).isPresent()) {
                    var totalItems = item.getAmount();
                    var itemValue = Cart.get(item.getId()).get().value;
                    var total = totalItems * itemValue;
                    var increaseTotalValue = player.<Integer>getAttribOr(CART_ITEMS_TOTAL_VALUE, 0) + total;
                    player.putAttrib(CART_ITEMS_TOTAL_VALUE, increaseTotalValue);
                }
            } else {
                items.add(item);
                if(Cart.get(item.getId()).isPresent()) {
                    var value = Cart.get(item.getId()).get().value;
                    String itemName = item.definition(World.getWorld()).name;
                    if (ArrayUtils.contains(pets, item.getId())) {
                        var bonusPoints = value * .50;
                        player.message(Color.PURPLE.wrap("You will get 50% more coins for exchanging the "+itemName+", extra: "+(int)bonusPoints+"."));
                        value += bonusPoints;
                    }
                    var totalValue = player.<Integer>getAttribOr(CART_ITEMS_TOTAL_VALUE, 0) + value * item.getAmount();
                    player.putAttrib(CART_ITEMS_TOTAL_VALUE, totalValue);
                }
            }

            //Remove the item from inventory
            if (item.getAmount() > 1)
                player.inventory().removeAll(item);
            else
                player.inventory().remove(item, true);

            //Update the attribute
            player.putAttrib(CART_ITEMS, items);

            //Send a message so people know something has been stored
            String itemName = item.unnote().name();
            boolean amOverOne = item.getAmount() > 1;
            String amtString = amOverOne ? "x" + Utils.format(item.getAmount()) + "" : Utils.getAOrAn(item.name());
            player.message(Color.RED.tag() + "You've added " + amtString + " " + itemName + " into the cart.");
        }
    }

    /**
     * Remove all the items from the cart.
     */
    public void clearCart() {
        var items = player.<ArrayList<Item>>getAttribOr(CART_ITEMS, new ArrayList<Item>());

        if(items.isEmpty()) {
            player.message(Color.RED.tag()+"There are no items in the cart.");
            return;
        }

        //Do some animation
        player.animate(832);

        //Add items to inventory or send to bank when inventory is full.
        for (final Item item : items) {
            player.inventory().addOrBank(item);
        }

        //Clear the list
        items.clear();
        player.putAttrib(CART_ITEMS_TOTAL_VALUE,0);
    }

    private void dispense() {
        if(!player.inventory().hasCapacityFor(new Item(CustomItemIdentifiers.HAZY_COINS))) {
            player.message("You have no room for any "+GameConstants.SERVER_NAME+" coins!");
           return;
        }

        player.face(new Tile(3083, 3483));

        //Do some animation
        player.animate(832);

        //Get total value
        var totalCartValue = player.<Integer>getAttribOr(CART_ITEMS_TOTAL_VALUE,0);

        //Add coins to inv
        player.inventory().add(new Item(CustomItemIdentifiers.HAZY_COINS, totalCartValue),true);

        //Clear items after we received the coins not before!
        var items = player.<ArrayList<Item>>getAttribOr(CART_ITEMS, new ArrayList<Item>());
        dispenserLogs.log(DISPENSER, player.getUsername() + " put " + Arrays.toString(items.toArray()) + " in the item dispenser.");
        items.clear();
        player.putAttrib(CART_ITEMS_TOTAL_VALUE,0);
    }

    /**
     * Opens up a list with all the items and their respected values.
     */
    public void loadValueList() {
        ObjectList<String> prices = new ObjectArrayList<>();
        prices.add("<br><col=" + Color.MITHRIL.getColorValue() + "> Item - Value</col><br><br>");
        var values = Cart.values();
        List<Cart> sortedValues = Arrays.stream(values).sorted(Comparator.comparingInt(Cart::getValue)).toList();
        for (Cart cart : sortedValues) {
            prices.add(new Item(cart.item).unnote().name()+" - "+ Utils.formatNumber(cart.value)+" "+ GameConstants.SERVER_NAME+" coins<br>");
        }
        player.sendScroll("Item values", Collections.singletonList(prices).toString().replaceAll(Pattern.quote("["), "").replaceAll(Pattern.quote("]"), "").replaceAll(",", ""));
    }

    public void dispenseItemsDialogue() {
        //First check if we have items
        var items = player.<ArrayList<Item>>getAttribOr(CART_ITEMS, new ArrayList<Item>());
        if(items.isEmpty()) {
            player.message(Color.RED.tag()+"There are no items in the cart.");
            return;
        }

        //We have items in our cart lets continue...
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.STATEMENT,"Are you sure you wish to destroy your items?");
                setPhase(0);
            }

            @Override
            protected void next() {
                if(isPhase(0)) {
                    send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Yes, i'm sure.", "No, stop the process and return my items.");
                    setPhase(1);
                }
            }

            @Override
            protected void select(int option) {
                if(isPhase(1)) {
                    if(option == 1) {
                        if(items.isEmpty()) {
                            player.message(Color.RED.tag()+"There are no items in the cart.");
                            stop();
                            return;
                        }
                        dispense();
                        stop();
                    } else if(option == 2) {
                        if(items.isEmpty()) {
                            stop();
                            return;
                        }
                        clearCart();//Return items
                        stop();
                    }
                }
            }
        });
    }
}
