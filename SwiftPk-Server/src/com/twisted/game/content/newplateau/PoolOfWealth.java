package com.twisted.game.content.newplateau;

import com.twisted.game.content.events.ServerEvent;
import com.twisted.game.content.events.WOGWData;
import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.container.inventory.Inventory;
import com.twisted.game.world.object.GameObject;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Color;
import com.twisted.util.ItemIdentifiers;
import com.twisted.util.SecondsTimer;
import com.twisted.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PoolOfWealth extends PacketInteraction {

    public static final SecondsTimer perkDuration = new SecondsTimer();
    public static String activePerk = null;

    @Override
    public boolean handleItemOnObject(Player player, Item item, GameObject object) {
        if (object.getId() == 31825) {
            if (item != null && item.rawtradable() && item.getValue() > 0) {
                if (isAllEventsActivated(player))
                    return true;

                Item donated = item;
                if (donated.noted()) {
                    donated = donated.unnote();
                }
                sendAmountScript(player, donated);
                return true;
            }
            return true;
        }
        return false;
    }


    static boolean isAllEventsActivated(final Player player) {
        if (player == null) return true;
        if (ServerEvent.activeEvents.size() >= WOGWData.VALUES.length) {
            player.message("You cannot donate to the Well at this current time, all events are activated.");
            return true;
        }
        return false;
    }

    final void sendAmountScript(final Player player, final Item item) {
        if (player == null || item == null)
            return;

        player.setEnterSyntax(new EnterSyntax() {
            @Override
            public void handleSyntax(Player player, long input) {
                int amount = (int) input;
                if (amount <= 0)
                    return;
                final Inventory inventory = player.getInventory();
                int count = (int) (1L * inventory.count(item.getId()));
                if (count < amount) {
                    player.message(Color.RED.wrap("You do not have enough " + item.name() + "'s to do this."));
                    return;
                }
                if (amount >= ServerEvent.WELL_OF_GOODWILL_MAX) {
                    amount = ServerEvent.WELL_OF_GOODWILL_MAX;
                }
                int value = item.getValue() * amount;
                inventory.remove(new Item(item.getId(), amount));
                handle(player, value, false, false);
                String amountString = Utils.formatPriceKMB(value);
                player.message(Color.GREEN.wrap("Thank you for donating to the Well Of Goodwill!"));
                String message = "You donated a total of " + amountString + " coin's worth of " + item.name() + "'s.";
                if (item.getId() == ItemIdentifiers.BLOOD_MONEY) {
                    message = "You donated a total of " + amountString + " blood money";
                }
                player.message(message);
            }
        });
        player.getPacketSender().sendEnterAmountPrompt("How many " + item.name() + "'s would you like to sacrifice?");
    }

    public static void handle(final Player player, final int value, final boolean isVoting, final boolean isDonating) {
        if (player == null) return;
        String message = getMessage(player, value, isVoting, isDonating);
        if (value > 500_000) {
            World.getWorld().sendWorldMessage(message);
        }

        ServerEvent.incrementWOGWTotal(value);

        int maxIterations = WOGWData.VALUES.length + 1;
        int iterations = 0;

        while (ServerEvent.WELL_OF_GOODWILL_AMOUNT >= 5_000_000) {
            if (iterations++ > maxIterations) {
                System.err.println("Exceeded maximum iterations while processing Well of Goodwill donations.");
                break;
            }
            ServerEvent.WELL_OF_GOODWILL_AMOUNT -= 5_000_000;
            startEvent(get());
        }
    }

    static void startEvent(List<ServerEvent> inactiveOutcomes) {
        if (!inactiveOutcomes.isEmpty()) {
            final var serverEvent = Utils.randomElement(inactiveOutcomes);
            serverEvent.start();
            ServerEvent.activeEvents.add(serverEvent);
            World.getWorld().sendWorldMessage(Color.ORANGE_2.wrap("<shad=0> [WOGW] " + serverEvent.name + " has been activated for 1 Hour!</shad>"));
        }
    }

    static @NotNull String getMessage(final Player player, final int value, final boolean isVoting, final boolean isDonating) {
        String message = "<col=6a1a18><shad=0> " + player.getUsername() + " has donated " + Utils.formatPriceKMB(value) + " to the Well Of Goodwill!</shad>";
        if (isVoting) {
            message = "<col=6a1a18><shad=0> " + player.getUsername() + " has donated " + Utils.formatPriceKMB(value) + " to the Well Of Goodwill through Voting!</shad>";
        }
        if (isDonating) {
            message = "<col=6a1a18><shad=0> " + player.getUsername() + " has donated " + Utils.formatPriceKMB(value) + " to the Well Of Goodwill as a bonus from Donating!</shad>";
        }
        return message;
    }

    static @NotNull List<ServerEvent> get() {
        List<ServerEvent> inactives = new ArrayList<>();
        ServerEvent event;
        for (final WOGWData wogwData : WOGWData.VALUES) {
            if (ServerEvent.activeEvents.contains(wogwData.event)) continue;
            event = wogwData.event;
            inactives.add(event);
        }
        Collections.shuffle(inactives);
        return inactives;
    }
}
