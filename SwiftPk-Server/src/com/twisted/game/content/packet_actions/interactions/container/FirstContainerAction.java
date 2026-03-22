package com.twisted.game.content.packet_actions.interactions.container;

import com.twisted.game.content.duel.Dueling;
import com.twisted.game.content.gambling.GamblingSession;
import com.twisted.game.content.interfaces.BonusesInterface;
import com.twisted.game.content.skill.impl.crafting.impl.Jewellery;
import com.twisted.game.content.skill.impl.smithing.EquipmentMaking;
import com.twisted.game.content.trade.Trading;
import com.twisted.game.content.tradingpost.TradingPost;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.npc.pets.insurance.PetInsurance;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.PlayerStatus;
import com.twisted.game.world.entity.mob.player.ironman.GroupIronman;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.container.shop.Shop;
import com.twisted.game.world.items.container.shop.ShopUtility;
import com.twisted.net.packet.interaction.PacketInteractionManager;

import static com.twisted.game.content.skill.impl.smithing.EquipmentMaking.*;
import static com.twisted.game.world.InterfaceConstants.*;

/**
 * @author PVE
 * @Since augustus 26, 2020
 */
public class FirstContainerAction {

    public static void firstAction(Player player, int interfaceId, int slot, int id) {
        if (PacketInteractionManager.checkItemContainerActionInteraction(player, new Item(id), slot, interfaceId, 1)) return;
        if (TradingPost.handleSellingItem(player, interfaceId, id, 1)) return;
        if (PetInsurance.onContainerAction(player, new Item(id), interfaceId, 1)) return;
        if (BonusesInterface.onContainerAction(player, interfaceId, slot)) return;
        if (player.getRunePouch().removeFromPouch(interfaceId, id, slot, 1)) return;
        if (player.getRunePouch().moveToRunePouch(interfaceId, id, slot, 1)) return;
        if (interfaceId == EQUIPMENT_CREATION_COLUMN_1 || interfaceId == EQUIPMENT_CREATION_COLUMN_2 || interfaceId == EQUIPMENT_CREATION_COLUMN_3 || interfaceId == EQUIPMENT_CREATION_COLUMN_4 || interfaceId == EQUIPMENT_CREATION_COLUMN_5) {
            if (player.getInterfaceManager().isInterfaceOpen(EquipmentMaking.EQUIPMENT_CREATION_INTERFACE_ID)) {
                EquipmentMaking.initialize(player, id, interfaceId, slot, 1);
                return;
            }
        }
        if (interfaceId == JEWELLERY_INTERFACE_CONTAINER_ONE || interfaceId == JEWELLERY_INTERFACE_CONTAINER_TWO || interfaceId == JEWELLERY_INTERFACE_CONTAINER_THREE) {
            Jewellery.click(player, id, 1);
            return;
        }

        GroupIronman group = GroupIronman.getGroup(player.getUID());

        if (interfaceId == PLACEHOLDER) {
            if (group == null) player.getBank().placeHolder(id, slot);
            else group.getGroupBank().placeHolder(id, slot);
            return;
        }

        if (interfaceId == LOOTING_BAG_BANK_CONTAINER_ID) {
            Item item = player.getLootingBag().get(slot);
            if (item == null) return;
            boolean banking = player.getAttribOr(AttributeKey.BANKING, false);
            if (banking) {
                player.getLootingBag().withdrawBank(item.createWithAmount(1), slot);
                return;
            }
        }

        if (interfaceId == LOOTING_BAG_DEPOSIT_CONTAINER_ID) {
            Item item = player.inventory().get(slot);
            if (item == null) return;
            player.getLootingBag().deposit(item, 1, null);
            return;
        }

        if (interfaceId == WITHDRAW_BANK) {
            if (group == null) {
                if (player.getBank().quantityFive) {
                    player.getBank().withdraw(id, slot, 5);
                    return;
                } else if (player.getBank().quantityTen) {
                    player.getBank().withdraw(id, slot, 10);
                    return;
                } else if (player.getBank().quantityAll) {
                    player.getBank().withdraw(id, slot, Integer.MAX_VALUE);
                    return;
                } else if (player.getBank().quantityX) {
                    player.getBank().withdraw(id, slot, player.getBank().currentQuantityX);
                    return;
                } else {
                    player.getBank().withdraw(id, slot, 1);
                    return;
                }
            } else {
                if (group.getGroupBank().quantityFive) {
                    group.getGroupBank().withdraw(id, slot, 5);
                    group.update();
                    return;
                } else if (group.getGroupBank().quantityTen) {
                    group.getGroupBank().withdraw(id, slot, 10);
                    group.update();
                    return;
                } else if (group.getGroupBank().quantityAll) {
                    group.getGroupBank().withdraw(id, slot, Integer.MAX_VALUE);
                    group.update();
                    return;
                } else if (group.getGroupBank().quantityX) {
                    group.getGroupBank().withdraw(id, slot, player.getBank().currentQuantityX);
                    group.update();
                    return;
                } else {
                    group.getGroupBank().withdraw(id, slot, 1);
                    group.update();
                    return;
                }
            }
        }

        if (interfaceId == INVENTORY_STORE) {
            final Item item = player.inventory().get(slot);
            if (item == null || item.getId() != id) return;
            boolean priceChecking = player.getAttribOr(AttributeKey.PRICE_CHECKING, false);
            if (priceChecking) {
                player.getPriceChecker().deposit(slot, 1);
                return;
            }
            if (group == null) {
                if (player.getBank().quantityFive) {
                    player.getBank().deposit(slot, 5);
                    return;
                } else if (player.getBank().quantityTen) {
                    player.getBank().deposit(slot, 10);
                    return;
                } else if (player.getBank().quantityAll) {
                    player.getBank().deposit(slot, Integer.MAX_VALUE);
                    return;
                } else if (player.getBank().quantityX) {
                    player.getBank().deposit(slot, player.getBank().currentQuantityX);
                    return;
                } else {
                    player.getBank().deposit(slot, 1);
                    return;
                }
            } else {
                if (group.getGroupBank().quantityFive) {
                    group.getGroupBank().deposit(slot, 5);
                    group.update();
                    return;
                } else if (group.getGroupBank().quantityTen) {
                    group.getGroupBank().deposit(slot, 10);
                    group.update();
                    return;
                } else if (group.getGroupBank().quantityAll) {
                    group.getGroupBank().deposit(slot, Integer.MAX_VALUE);
                    group.update();
                    return;
                } else if (group.getGroupBank().quantityX) {
                    group.getGroupBank().deposit(slot, player.getBank().currentQuantityX);
                    group.update();
                    return;
                } else {
                    group.getGroupBank().deposit(slot, 1);
                    group.update();
                    return;
                }
            }
        }

        if (interfaceId == PRICE_CHECKER_DISPLAY_ID) {
            boolean priceChecking = player.getAttribOr(AttributeKey.PRICE_CHECKING, false);
            if (priceChecking) {
                player.getPriceChecker().withdraw(id, 1);
                return;
            }
        }

        if (interfaceId == ShopUtility.ITEM_CHILD_ID || interfaceId == ShopUtility.SLAYER_BUY_ITEM_CHILD_ID) {
            Shop.exchange(player, id, slot, 1, true);
            return;
        }

        if (interfaceId == SHOP_INVENTORY) {
            int shop = player.getAttribOr(AttributeKey.SHOP, -1);
            Shop store = World.getWorld().shops.get(shop);
            if (store != null) {
                Shop.exchange(player, id, slot, 1, false);
                return;
            }
        }

        if (interfaceId == Dueling.MAIN_INTERFACE_CONTAINER) {
            if (player.getStatus() == PlayerStatus.DUELING) {
                player.getDueling().handleItem(id, 1, slot, player.getDueling().getContainer(), player.inventory());
                return;
            }
        }

        // Withdrawing items from gamble
        if (interfaceId == GamblingSession.MY_ITEMS_OFFERED || interfaceId == GamblingSession.OPPONENT_ITEMS_OFFERED) {
            if (player.getStatus() == PlayerStatus.GAMBLING) {
                player.getGamblingSession().handleItem(id, 1, slot, player.getGamblingSession().getContainer(), player.inventory());
                return;
            }
        }

        if (interfaceId == REMOVE_INVENTORY_ITEM) {
            if (player.getStatus() == PlayerStatus.TRADING) {
                player.getTrading().handleItem(id, 1, slot, player.inventory(), player.getTrading().getContainer());
                return;
            } else if (player.getStatus() == PlayerStatus.DUELING) {
                player.getDueling().handleItem(id, 1, slot, player.inventory(), player.getDueling().getContainer());
                return;
            } else if (player.getStatus() == PlayerStatus.GAMBLING) {
                player.getGamblingSession().handleItem(id, 1, slot, player.inventory(), player.getGamblingSession().getContainer());
                return;
            }
        }

        if (interfaceId == Trading.CONTAINER_INTERFACE_ID) {
            if (player.getStatus() == PlayerStatus.TRADING) {
                player.getTrading().handleItem(id, 1, slot, player.getTrading().getContainer(), player.inventory());
                return;
            }
        }

        if (interfaceId == REMOVE_OFFERED_GAMBLE_ITEMS && player.getGamblingSession() != null) {
            //player.getGamblingSession().removeStakedItem(id, 1, slot);
            return;
        }

        if (interfaceId == PRICE_CHECKER_CONTAINER) {
            player.getPriceChecker().withdraw(id, 1);
        }
    }
}
