package com.twisted.game.content.packet_actions.interactions.container;

import com.twisted.game.content.duel.Dueling;
import com.twisted.game.content.gambling.GamblingSession;
import com.twisted.game.content.packet_actions.interactions.equipment.EquipmentActions;
import com.twisted.game.content.skill.impl.crafting.impl.Jewellery;
import com.twisted.game.content.skill.impl.smithing.EquipmentMaking;
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
import com.twisted.util.Color;
import com.twisted.util.CustomItemIdentifiers;

import static com.twisted.game.content.skill.impl.smithing.EquipmentMaking.*;
import static com.twisted.game.world.InterfaceConstants.*;

/**
 * @author PVE
 * @Since augustus 26, 2020
 */
public class SecondContainerAction {

    public static void secondAction(Player player, int interfaceId, int slot, int id) {
        if (PacketInteractionManager.checkItemContainerActionInteraction(player, new Item(id), slot, interfaceId, 2)) {
            return;
        }

        if (player.getRunePouch().removeFromPouch(interfaceId, id, slot, 2)) {
            return;
        }

        if (TradingPost.handleSellingItem(player, interfaceId, id, 5))
            return;

        if (player.getRunePouch().moveToRunePouch(interfaceId, id, slot, 2)) {
            return;
        }

        GroupIronman group = GroupIronman.getGroup(player.getUID());

        if (interfaceId == EQUIPMENT_CREATION_COLUMN_1 || interfaceId == EQUIPMENT_CREATION_COLUMN_2 || interfaceId == EQUIPMENT_CREATION_COLUMN_3 || interfaceId == EQUIPMENT_CREATION_COLUMN_4 || interfaceId == EQUIPMENT_CREATION_COLUMN_5) {
            if (player.getInterfaceManager().isInterfaceOpen(EquipmentMaking.EQUIPMENT_CREATION_INTERFACE_ID)) {
                EquipmentMaking.initialize(player, id, interfaceId, slot, 5);
                return;
            }
        }

        if (id == CustomItemIdentifiers.SUMMER_SOAKER) {
            int eventKills = player.<Integer>getAttribOr(AttributeKey.EVENT_MONSTERS_KILLED, 0);
            player.message(Color.GREEN.wrap("You have killed a total of " + eventKills + " event monsters."));
        }

        if (interfaceId == EQUIPMENT_DISPLAY_ID) {
            final Item item = player.getEquipment().get(slot);
            if (item == null || item.getId() != id) return;
            if (EquipmentActions.operate(player, slot, item)) {
                return;
            }
        }

        /* Jewellery */
        if (interfaceId == JEWELLERY_INTERFACE_CONTAINER_ONE || interfaceId == JEWELLERY_INTERFACE_CONTAINER_TWO || interfaceId == JEWELLERY_INTERFACE_CONTAINER_THREE) {
            Jewellery.click(player, id, 5);
            return;
        }

        /* Looting bag */
        if (interfaceId == LOOTING_BAG_BANK_CONTAINER_ID) {
            Item item = player.getLootingBag().get(slot);
            if (item == null) {
                return;
            }
            boolean banking = player.getAttribOr(AttributeKey.BANKING, false);

            if (banking) {
                player.getLootingBag().withdrawBank(item.createWithAmount(5), slot);
                return;
            }
        }

        if (interfaceId == LOOTING_BAG_DEPOSIT_CONTAINER_ID) {
            Item item = player.inventory().get(slot);
            if (item == null) return;
            player.getLootingBag().deposit(item, 5, null);
            return;
        }

        if (interfaceId == WITHDRAW_BANK) {
            if (group == null) {
                player.getBank().withdraw(id, slot, 5);
                return;
            } else {
                group.getGroupBank().withdraw(id, slot, 5);
                group.update();
                return;
            }
        }

        if (interfaceId == INVENTORY_STORE) {
            boolean priceChecking = player.getAttribOr(AttributeKey.PRICE_CHECKING, false);
            if (priceChecking) {
                player.getPriceChecker().deposit(slot, 5);
                return;
            }

            boolean banking = player.getAttribOr(AttributeKey.BANKING, false);
            if (banking) {
                if (group == null) {
                    player.getBank().deposit(slot, 5);
                    return;
                } else {
                    group.getGroupBank().deposit(slot, 5);
                    group.update();
                    return;
                }
            }
        }

        if (interfaceId == PRICE_CHECKER_DISPLAY_ID) {
            boolean priceChecking = player.getAttribOr(AttributeKey.PRICE_CHECKING, false);
            if (priceChecking) {
                player.getPriceChecker().withdraw(id, 5);
                return;
            }
        }

        if (interfaceId == ShopUtility.ITEM_CHILD_ID || interfaceId == ShopUtility.SLAYER_BUY_ITEM_CHILD_ID) {
            Shop.exchange(player, id, slot, 2, true);
            return;
        }

        if (interfaceId == SHOP_INVENTORY) {
            Shop.exchange(player, id, slot, 2, false);
            return;
        }

        if (interfaceId == Dueling.MAIN_INTERFACE_CONTAINER) {
            if (player.getStatus() == PlayerStatus.DUELING) {
                player.getDueling().handleItem(id, 5, slot, player.getDueling().getContainer(), player.inventory());
                return;
            }
        }

        // Withdrawing items from gamble
        if (interfaceId == GamblingSession.MY_ITEMS_OFFERED || interfaceId == GamblingSession.OPPONENT_ITEMS_OFFERED) {
            if (player.getStatus() == PlayerStatus.GAMBLING) {
                player.getGamblingSession().handleItem(id, 5, slot, player.getGamblingSession().getContainer(), player.inventory());
                return;
            }
        }

        if (interfaceId == REMOVE_INVENTORY_ITEM) { // Duel/Trade inventory
            if (player.getStatus() == PlayerStatus.TRADING) {
                player.getTrading().handleItem(id, 5, slot, player.inventory(), player.getTrading().getContainer());
                return;
            } else if (player.getStatus() == PlayerStatus.DUELING) {
                player.getDueling().handleItem(id, 5, slot, player.inventory(), player.getDueling().getContainer());
                return;
            } else if (player.getStatus() == PlayerStatus.GAMBLING) {
                player.getGamblingSession().handleItem(id, 5, slot, player.inventory(), player.getGamblingSession().getContainer());
                return;
            }
        }

        if (interfaceId == Trading.CONTAINER_INTERFACE_ID) {
            if (player.getStatus() == PlayerStatus.TRADING) {
                player.getTrading().handleItem(id, 5, slot, player.getTrading().getContainer(), player.inventory());
                return;
            }
        }

        if (interfaceId == PRICE_CHECKER_CONTAINER) {
            player.getPriceChecker().withdraw(id, 5);
        }
    }
}
