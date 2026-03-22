package com.twisted.game.content.packet_actions.interactions.container;

import com.twisted.game.content.duel.Dueling;
import com.twisted.game.content.gambling.GamblingSession;
import com.twisted.game.content.syntax.impl.*;
import com.twisted.game.content.trade.Trading;
import com.twisted.game.content.tradingpost.TradingPost;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.PlayerStatus;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.container.shop.Shop;
import com.twisted.game.world.items.container.shop.ShopUtility;
import com.twisted.net.packet.interaction.PacketInteractionManager;

import static com.twisted.game.world.InterfaceConstants.*;
import static com.twisted.game.world.entity.AttributeKey.USING_TRADING_POST;

/**
 * @author PVE
 * @Since augustus 26, 2020
 */
public class FifthContainerAction {

    public static void fifthAction(Player player, int interfaceId, int slot, int id) {
        boolean banking = player.getAttribOr(AttributeKey.BANKING, false);
        boolean priceChecking = player.getAttribOr(AttributeKey.PRICE_CHECKING, false);
        if (PacketInteractionManager.checkItemContainerActionInteraction(player, new Item(id), slot, interfaceId, 5)) return;
        if (player.getRunePouch().removeFromPouch(interfaceId, id, slot, 5)) return;
        if (player.getRunePouch().moveToRunePouch(interfaceId, id, slot, 5)) return;

        if (interfaceId == WITHDRAW_BANK) {
            if (banking) {
                player.setEnterSyntax(new BankX(id, slot, false));
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to withdraw?");
                return;
            }
        }

        if (interfaceId == INVENTORY_STORE) {
            if (priceChecking) {
                player.setEnterSyntax(new PriceCheckX(id, slot, true));
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to deposit?");
                return;
            } else if (banking) {
                player.setEnterSyntax(new BankX(id, slot, true));
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to deposit?");
                return;
            }
        }

        if (interfaceId == PRICE_CHECKER_DISPLAY_ID) {
            player.setEnterSyntax(new PriceCheckX(id, slot, false));
            player.getPacketSender().sendEnterAmountPrompt("How many would you like to withdraw?");
            return;
        }

        if (interfaceId == ShopUtility.ITEM_CHILD_ID) {
            Shop.exchange(player, id, slot, 5, true);
            return;
        }

        if (interfaceId == SHOP_INVENTORY) {
            Shop.exchange(player, id, slot, 5, false);
            return;
        }

        if (interfaceId == REMOVE_INVENTORY_ITEM) { // Duel/Trade inventory
            if (player.<Boolean>getAttribOr(USING_TRADING_POST, false)) {
                TradingPost.handleXOptionInput(player, id, slot);
                return;
            } else if (player.getStatus() == PlayerStatus.TRADING) {
                player.setEnterSyntax(new TradeX(id, slot, true));
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to offer?");
                return;
            } else if (player.getStatus() == PlayerStatus.DUELING) {
                player.setEnterSyntax(new StakeX(id, slot, true));
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to offer?");
                return;
            } else if (player.getStatus() == PlayerStatus.GAMBLING) {
                player.setEnterSyntax(new GambleX(id, slot, true));
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to offer?");
                return;
            }
        }

        if (interfaceId == Trading.CONTAINER_INTERFACE_ID) {
            if (player.getStatus() == PlayerStatus.TRADING) {
                player.setEnterSyntax(new TradeX(id, slot, false));
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
                return;
            }
        }

        if (interfaceId == Dueling.MAIN_INTERFACE_CONTAINER) {
            if (player.getStatus() == PlayerStatus.DUELING) {
                player.setEnterSyntax(new StakeX(id, slot, false));
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
                return;
            }
        }

        if (interfaceId == GamblingSession.MY_ITEMS_OFFERED || interfaceId == GamblingSession.OPPONENT_ITEMS_OFFERED) {
            if (player.getStatus() == PlayerStatus.GAMBLING) {
                player.setEnterSyntax(new GambleX(id, slot, false));
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
                return;
            }
        }
    }
}
