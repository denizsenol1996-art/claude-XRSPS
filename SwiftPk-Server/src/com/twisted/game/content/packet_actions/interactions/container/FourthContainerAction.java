package com.twisted.game.content.packet_actions.interactions.container;

import com.twisted.game.content.duel.Dueling;
import com.twisted.game.content.gambling.GamblingSession;
import com.twisted.game.content.syntax.impl.LootingBagX;
import com.twisted.game.content.trade.Trading;
import com.twisted.game.content.tradingpost.TradingPost;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.PlayerStatus;
import com.twisted.game.world.entity.mob.player.ironman.GroupIronman;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.container.shop.Shop;
import com.twisted.game.world.items.container.shop.ShopUtility;
import com.twisted.net.packet.interaction.PacketInteractionManager;

import static com.twisted.game.world.InterfaceConstants.*;

/**
 * @author PVE
 * @Since augustus 26, 2020
 */
public class FourthContainerAction {

    public static void fourthAction(Player player, int interfaceId, int slot, int id) {
        var count = player.inventory().count(id);
        Item item = new Item(id, count);
        if (PacketInteractionManager.checkItemContainerActionInteraction(player, item, slot, interfaceId, 4)) {
            return;
        }

        if (player.getRunePouch().removeFromPouch(interfaceId, id, slot, 4)) {
            return;
        }

        if (TradingPost.handleSellingItem(player, interfaceId, id, count))
            return;

        if (player.getRunePouch().moveToRunePouch(interfaceId, id, slot, 4)) {
            return;
        }

        /* Looting bag */
        if (interfaceId == LOOTING_BAG_BANK_CONTAINER_ID) {
            Item lootingBagItem = player.getLootingBag().get(slot);
            if (lootingBagItem == null) {
                return;
            }
            boolean banking = player.getAttribOr(AttributeKey.BANKING, false);

            if (banking) {
                player.setEnterSyntax(new LootingBagX(lootingBagItem.getId(), slot, true));
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to deposit?");
                return;
            }
        }

        if (interfaceId == LOOTING_BAG_DEPOSIT_CONTAINER_ID) {
            Item lootingBagItem = player.inventory().get(slot);
            if (lootingBagItem == null) {
                return;
            }

            player.setEnterSyntax(new LootingBagX(lootingBagItem.getId(), slot, false));
            player.getPacketSender().sendEnterAmountPrompt("How many would you like to deposit?");
            return;
        }

        GroupIronman group = GroupIronman.getGroup(player.getUID());

        if (interfaceId == WITHDRAW_BANK) {
            if (group == null) {
                player.getBank().withdraw(id, slot, Integer.MAX_VALUE);
                return;
            } else {
                group.getGroupBank().withdraw(id, slot, Integer.MAX_VALUE);
                group.update();
                return;
            }
        }

        if (interfaceId == INVENTORY_STORE) {
            boolean banking = player.getAttribOr(AttributeKey.BANKING, false);
            boolean priceChecking = player.getAttribOr(AttributeKey.PRICE_CHECKING, false);

            if (priceChecking) {
                player.getPriceChecker().deposit(slot, Integer.MAX_VALUE);
                return;
            }

            if (banking) {
                if (group == null) {
                    player.getBank().deposit(slot, Integer.MAX_VALUE);
                    return;
                } else {
                    group.getGroupBank().deposit(slot, Integer.MAX_VALUE);
                    group.update();
                    return;
                }
            }
        }

        if (interfaceId == PRICE_CHECKER_DISPLAY_ID) {
            boolean priceChecking = player.getAttribOr(AttributeKey.PRICE_CHECKING, false);
            if (priceChecking) {
                int amount_to_withdraw = player.getPriceChecker().count(id);
                player.getPriceChecker().withdraw(id, amount_to_withdraw);
                return;
            }
        }


        if (interfaceId == ShopUtility.ITEM_CHILD_ID || interfaceId == ShopUtility.SLAYER_BUY_ITEM_CHILD_ID) {
            Shop.exchange(player, id, slot, 4, true);
            return;
        }

        if (interfaceId == SHOP_INVENTORY) {
            Shop.exchange(player, id, slot, 4, false);
            return;
        }

        // Withdrawing items from duel
        if (interfaceId == Dueling.MAIN_INTERFACE_CONTAINER) {
            if (player.getStatus() == PlayerStatus.DUELING) {
                player.getDueling().handleItem(id, player.getDueling().getContainer().count(id), slot, player.getDueling().getContainer(), player.inventory());
                return;
            }
        }

        // Withdrawing items from gamble
        if (interfaceId == GamblingSession.MY_ITEMS_OFFERED || interfaceId == GamblingSession.OPPONENT_ITEMS_OFFERED) {
            if (player.getStatus() == PlayerStatus.GAMBLING) {
                player.getGamblingSession().handleItem(id, player.getGamblingSession().getContainer().count(id), slot, player.getGamblingSession().getContainer(), player.inventory());
                return;
            }
        }

        if (interfaceId == REMOVE_INVENTORY_ITEM) {// Duel/Trade inventory
            boolean priceChecking = player.getAttribOr(AttributeKey.PRICE_CHECKING, false);
            if (priceChecking) {
                player.getPriceChecker().deposit(slot, player.inventory().count(id));
                return;
            } else if (player.getStatus() == PlayerStatus.TRADING) {
                player.getTrading().handleItem(id, player.inventory().count(id), slot, player.inventory(), player.getTrading().getContainer());
                return;
            } else if (player.getStatus() == PlayerStatus.DUELING) {
                player.getDueling().handleItem(id, player.inventory().count(id), slot, player.inventory(), player.getDueling().getContainer());
                return;
            } else if (player.getStatus() == PlayerStatus.GAMBLING) {
                player.getGamblingSession().handleItem(id, player.inventory().count(id), slot, player.inventory(), player.getGamblingSession().getContainer());
                return;
            }
        }

        if (interfaceId == Trading.CONTAINER_INTERFACE_ID) {
            if (player.getStatus() == PlayerStatus.TRADING) {
                player.getTrading().handleItem(id, player.getTrading().getContainer().count(id), slot, player.getTrading().getContainer(), player.inventory());
                return;
            }
        }

        if (interfaceId == 48542) {
            player.getPriceChecker().withdraw(id, player.getPriceChecker().count(id));
        }
    }
}
