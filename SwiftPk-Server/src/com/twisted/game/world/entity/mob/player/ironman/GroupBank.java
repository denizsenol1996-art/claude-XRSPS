package com.twisted.game.world.entity.mob.player.ironman;

import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.InterfaceConstants;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.magic.Autocasting;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.container.ItemContainer;
import com.twisted.util.Color;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: Origin
 * @Date: 3/6/2024
 */
public final class GroupBank {
    private boolean placeHolder;
    private int[] tabAmounts = new int[10];
    private static final int SIZE = 816;
    private int bankTab = 0;
    public final ItemContainer container;
    private final List<Player> viewers;
    private int placeHolderAmount = 0;
    public int currentQuantityX = 0;
    public boolean quantityAll = false, quantityX = false, quantityTen = false, quantityFive = false, quantityOne = true, noting = false, show_item_in_tab = true, show_number_in_tab = false, show_roman_number_in_tab = false, inserting = false;

    public GroupBank() {
        this.container = new ItemContainer(GroupBank.SIZE, ItemContainer.StackPolicy.ALWAYS);
        this.viewers = new ArrayList<>();
    }

    public synchronized void addViewer(Player player, GroupIronman group) {
        if (player == null || group == null) return;
        if (this.viewers.contains(player)) return;
        if (player.getUID() == group.getOwnerUID()) {
            this.viewers.add(player);
            return;
        }
        for (long uid : group.getMemberUID()) {
            if (uid == -1L) continue;
            if (uid != player.getUID()) continue;
            this.viewers.add(player);
        }
    }

    public synchronized void open(Player viewing, GroupIronman group) {
        if (viewing == null || group == null) return;
        if (viewing.getRaids() != null) return;
        if (viewing.getParticipatingTournament() != null) return;
        if (viewing.inActiveTournament() || viewing.isInTournamentLobby()) return;
        if (!this.viewers.isEmpty()) {
            viewing.message(Color.RED.wrap("You cannot view the bank at this time, as it is currently occupied by another member in your group."));
            return;
        }
        addViewer(viewing, group);
        for (Player viewer : this.viewers) {
            if (viewer == null) continue;
            viewer.getPacketSender().sendString(InterfaceConstants.BANK_WIDGET + 5, "Group Ironman Bank Storage");
            viewer.getPacketSender().setWidgetActive(26102, false);
            viewer.getPacketSender().sendString(26019, "");
            viewer.getPacketSender().sendString(26018, "816");
            viewer.putAttrib(AttributeKey.BANKING, true);
            viewer.getInterfaceManager().openInventory(InterfaceConstants.BANK_WIDGET, InterfaceConstants.INVENTORY_STORE - 1);
            this.refreshVarps(viewer);
            this.container.refresh();
            this.syncContaiers(viewer);
        }
    }

    public synchronized void deposit(int slot, int amount) {
        for (Player viewer : this.viewers) {
            if (viewer == null) continue;
            if (viewer.getRaids() != null) return;
            if (viewer.getParticipatingTournament() != null) return;
            if (!viewer.getInterfaceManager().isInterfaceOpen(InterfaceConstants.BANK_WIDGET)) return;
            if (viewer.inActiveTournament() || viewer.isInTournamentLobby()) return;
            this.deposit(slot, amount, viewer.inventory());
        }
    }

    public synchronized void deposit(int slot, int amount, ItemContainer fromContainer) {
        for (Player viewer : this.viewers) {
            if (viewer == null) continue;
            if (viewer.getRaids() != null) return;
            if (viewer.getParticipatingTournament() != null) return;
            if (viewer.inActiveTournament() || viewer.isInTournamentLobby()) return;
            Item item = fromContainer.get(slot);
            if (item == null) return;
            if (!item.isValid()) return;
            int id = item.getId();
            boolean lootingBagEmpty = viewer.getLootingBag().isEmpty();
            if ((id == 11941 || id == 22586 || id == 30098 || id == 30099) && !lootingBagEmpty) {
                viewer.getInterfaceManager().openInventory(InterfaceConstants.BANK_WIDGET, InterfaceConstants.LOOTING_BAG_BANK_ID);
                viewer.getLootingBag().setBankStoring(true);
                viewer.getLootingBag().onRefresh();
                return;
            }
            int invAmount = fromContainer.count(id);
            if (invAmount < amount) amount = invAmount;
            viewer.auditTabs();
            this.container.setFiringEvents(false);
            if (item.noted()) id = item.unnote().getId();
            if (!this.container.contains(id)) {
                if (this.container.size() + 1 > this.container.capacity()) {
                    viewer.message("Your bank is full! You need to clear some items from your bank.");
                    this.container.setFiringEvents(true);
                    return;
                }

                int destinationSlot = this.container.nextFreeSlot();
                int placeholderSlot = this.computeNextEmptyPlaceholder(id);
                if (placeHolder && placeholderSlot != -1) {
                    this.container.get(placeholderSlot).setAmount(1);
                } else {
                    this.changeTabAmount(bankTab, 1);
                    this.container.add(new Item(id, amount), destinationSlot);
                    int to = this.slotForTab(bankTab);
                    this.container.swap(true, destinationSlot, to, false);
                }
            } else {
                Item depositItem = this.container.get(this.container.getSlot(id));
                if (depositItem == null) return;
                if (!depositItem.isValid()) return;
                if (Integer.MAX_VALUE - depositItem.getAmount() < amount) {
                    amount = Integer.MAX_VALUE - depositItem.getAmount();
                    viewer.message("Your bank didn't have enough space to deposit all that!");
                }
                depositItem.incrementAmountBy(amount);
            }
            if (amount > 0) fromContainer.remove(item.getId(), amount);
            this.container.setFiringEvents(true);
            this.container.refresh();
            this.syncContaiers(viewer);
        }
    }

    public synchronized void changeTabAmount(int tab, int amount) {
        if (tab < 0 || tab >= tabAmounts.length) return;
        tabAmounts[tab] += amount;
        if (tabAmounts[tab] == 0) collapse(tab, false);
    }

    public synchronized int computeNextEmptyPlaceholder(int id) {
        for (int index = 0; index < this.container.capacity(); index++) {
            if (this.container.get(index) != null && this.container.get(index).getId() == id && this.container.get(index).getAmount() == 0) return index;
        }
        return -1;
    }

    public synchronized void collapse(int tab, boolean collapseAll) {
        for (Player viewer : this.viewers) {
            if (viewer == null) continue;
            if (!viewer.getInterfaceManager().isInterfaceOpen(InterfaceConstants.BANK_WIDGET)) return;
            if (tab == 0 && collapseAll) {
                Arrays.fill(this.tabAmounts, 0);
                this.tabAmounts[0] = this.container.size();
                this.container.shift();
                return;
            }
            int tabAmount = this.tabAmounts[tab];
            if (tabAmount > 0) this.moveTab(tab, 0);
            this.recursiveCollapse(tab);
            viewer.getPacketSender().sendConfig(211, this.bankTab = 0);
        }
    }

    private synchronized void recursiveCollapse(int tab) {
        if (tab == this.tabAmounts.length - 1) return;
        this.moveTab(tab + 1, tab);
        this.recursiveCollapse(tab + 1);
    }

    private synchronized void moveTab(int tab, int toTab) {
        int tabAmount = this.tabAmounts[tab];
        int fromSlot = this.slotForTab(tab);
        int toSlot = this.slotForTab(toTab) + 1;
        this.tabAmounts[tab] -= tabAmount;
        this.tabAmounts[toTab] += tabAmount;
        this.container.setFiringEvents(false);
        for (int i = 0; i < tabAmount; i++) this.container.swap(true, fromSlot, toSlot, false);
        this.container.setFiringEvents(true);
    }

    private synchronized int slotForTab(int tab) {
        int passed = -1;
        for (int index = tab; index >= 0; index--) passed += tabAmounts[index];
        return passed;
    }

    private synchronized void refreshVarps(Player viewer) {
        if (viewer == null) return;
        viewer.getPacketSender().sendConfig(750, this.show_item_in_tab ? 1 : 0);
        viewer.getPacketSender().sendConfig(304, this.inserting ? 1 : 0);
        viewer.getPacketSender().sendConfig(115, this.noting ? 1 : 0);
        viewer.getPacketSender().sendConfig(314, this.quantityAll ? 1 : 0);
        viewer.getPacketSender().sendConfig(315, this.quantityX ? 1 : 0);
        viewer.getPacketSender().sendConfig(316, this.quantityTen ? 1 : 0);
        viewer.getPacketSender().sendConfig(317, this.quantityFive ? 1 : 0);
        viewer.getPacketSender().sendConfig(320, this.quantityOne ? 1 : 0);
        viewer.getPacketSender().setWidgetActive(26101, this.placeHolder);
    }

    public synchronized void syncContaiers(Player viewer) {
        viewer.inventory().refresh(viewer, InterfaceConstants.INVENTORY_STORE);
        viewer.getPacketSender().sendItemOnInterface(InterfaceConstants.WITHDRAW_BANK, this.container.toArray());
        viewer.getPacketSender().sendBanktabs();
        viewer.inventory().refresh();
        viewer.getEquipment().refresh();
        viewer.getRisk().update();
    }

    public synchronized void withdraw(int itemId, int slot, int amount) {
        for (Player viewer : this.viewers) {
            if (viewer == null) continue;
            if (!viewer.getInterfaceManager().isInterfaceOpen(InterfaceConstants.BANK_WIDGET)) return;
            if (viewer.getRaids() != null) return;
            if (viewer.getParticipatingTournament() != null) return;
            if (viewer.inActiveTournament() || viewer.isInTournamentLobby()) return;
            if (itemId < 0 || slot < 0 || slot > this.container.capacity()) return;
            Item item = this.container.get(slot);
            if (item == null || itemId != item.getId()) return;
            if (!item.isValid()) return;
            if (item.getAmount() == 0) {
                boolean hold = this.placeHolder;
                this.placeHolder = false;
                int tabSlot = this.container.getSlot(item.getId());
                int tab = this.tabForSlot(tabSlot);
                this.changeTabAmount(tab, -1);
                this.container.remove(item);
                this.container.shift();
                this.placeHolder = hold;
                this.placeHolderAmount--;
                this.container.refresh();
                return;
            }
            if (item.getAmount() < amount) amount = item.getAmount();
            int id = item.getId();
            if (noting) {
                if (!item.noteable()) {
                    viewer.message("This item cannot be withdrawn as a note.");
                } else {
                    id = item.note().getId();
                }
            }
            this.container.setFiringEvents(false);
            if (!new Item(id).stackable() && amount > viewer.getInventory().getFreeSlots()) {
                amount = viewer.getInventory().getFreeSlots();
            } else if (item.stackable() && viewer.getInventory().getFreeSlots() == 0) {
                if (!viewer.getInventory().contains(id)) {
                    amount = 0;
                } else if (viewer.getInventory().count(id) + amount > Integer.MAX_VALUE) {
                    amount = Integer.MAX_VALUE - viewer.getInventory().count(id);
                }
            }

            if (amount == 0) {
                viewer.message("You do not have enough inventory spaces to withdraw this item.");
                return;
            }

            boolean pressedAnyWithdrawButtons = this.quantityOne || this.quantityFive || this.quantityTen || this.quantityX || this.quantityAll;
            if (pressedAnyWithdrawButtons && viewer.getInventory().isFull()) {
                viewer.message("You do not have enough inventory spaces to withdraw this item.");
                return;
            }

            int withdrawSlot = viewer.getInventory().getSlot(id);
            if (withdrawSlot != -1) {
                Item withdrawItem = viewer.getInventory().get(withdrawSlot);
                if (withdrawItem == null) return;
                if (!withdrawItem.isValid()) return;
                if (Integer.MAX_VALUE - withdrawItem.getAmount() < amount) {
                    amount = Integer.MAX_VALUE - withdrawItem.getAmount();
                    viewer.message("Your inventory didn't have enough space to withdraw all that!");
                }
            }
            if (this.container.remove(item.getId(), amount)) {
                viewer.getInventory().add(new Item(id, amount));
                if (!this.container.contains(item.getId())) {
                    int tab = tabForSlot(slot);
                    this.changeTabAmount(tab, -1);
                    this.container.shift();
                }
            }
            this.container.setFiringEvents(true);
            this.container.refresh();
            this.syncContaiers(viewer);
        }
    }

    public synchronized void moveItem(int opcode, int from, int to) {
        if (opcode == 2) {
            this.itemToTab(from, to);
        } else if (opcode == 1) {
            this.container.swap(true, from, to, false);
            int fromTab = tabForSlot(from);
            int toTab = tabForSlot(to);
            if (fromTab != toTab) {
                this.changeTabAmount(toTab, 1);
                this.changeTabAmount(fromTab, -1);
                this.container.refresh();
            }
        } else {
            this.container.swap(from, to);
        }
    }

    private synchronized void itemToTab(int slot, int toTab) {
        int fromTab = this.tabForSlot(slot);
        if (fromTab == toTab) return;
        if (toTab > 1 && this.tabAmounts[toTab - 1] == 0 && this.tabAmounts[toTab] == 0) return;
        this.tabAmounts[toTab]++;
        this.tabAmounts[fromTab]--;
        int toSlot = slotForTab(toTab);
        if (this.tabAmounts[fromTab] == 0) this.collapse(fromTab, false);
        this.container.swap(true, slot, toSlot, false);
        this.container.refresh();
    }

    public synchronized int tabForSlot(int slot) {
        if (slot <= -1) return -1;
        int passed = -1;
        for (int tab = 0; tab < this.tabAmounts.length; tab++) {
            if (slot <= passed + this.tabAmounts[tab]) return tab;
            passed += this.tabAmounts[tab];
        }
        return -1;
    }

    public synchronized void close(Player player) {
        if (player == null) return;
        for (Player viewing : Lists.newArrayList(this.viewers.iterator())) {
            if (viewing == null) continue;
            if (!viewing.equals(player)) continue;
            this.viewers.remove(viewing);
        }
    }

    public synchronized void placeHolder(int item, int slot) {
        boolean hold = placeHolder;
        this.placeHolder = true;
        this.container.setFiringEvents(false);
        this.withdraw(item, slot, Integer.MAX_VALUE);
        this.container.setFiringEvents(true);
        this.placeHolder = hold;
        this.container.refresh();
    }

    public synchronized void depositeEquipment(Player player) {
        if (player == null) return;
        if (player.getParticipatingTournament() != null) return;
        if (player.getRaids() != null) return;
        if (player.getEquipment().isEmpty()) return;
        for (int i = 0; i <= 13; i++) {
            var itemAt = player.getEquipment().get(i);
            if (itemAt == null) continue;
            if (!itemAt.isValid()) continue;
            this.deposit(i, itemAt.getAmount(), player.getEquipment());
        }
        Autocasting.setAutocast(player, null);
        player.getCombat().setRangedWeapon(null);
        player.getEquipment().login();
    }

    public synchronized void depositInventory(Player player) {
        if (player == null) return;
        if (player.getParticipatingTournament() != null) return;
        if (player.getRaids() != null) return;
        if (player.getInventory().isEmpty()) return;
        for (int i = 0; i <= 27; i++) {
            var itemAt = player.inventory().get(i);
            if (itemAt == null) continue;
            if (!itemAt.isValid()) continue;
            this.deposit(i, itemAt.getAmount(), player.inventory());
        }
    }

    public synchronized boolean buttonAction(int button) {
        for (Player viewer : this.viewers) {
            if (viewer == null) continue;
            if (viewer.getParticipatingTournament() != null) return false;
            if (viewer.getRaids() != null) return false;
            if (!viewer.getInterfaceManager().isInterfaceOpen(InterfaceConstants.BANK_WIDGET) && !viewer.getInterfaceManager().isInterfaceOpen(34000)) return false;
            if (button >= 26031 && button <= 26068) {
                final int tab = (26031 - button) / -4;
                if (button % 2 == 0) {
                    this.bankTab = tab;
                    viewer.getPacketSender().sendString(26019, "");
                } else {
                    this.collapse(tab, tab == 0);
                    this.container.refresh();
                }
                return true;
            }
            switch (button) {
                case 26102 -> {
                    return true;
                }
                case 26119 -> {
                    viewer.getInterfaceManager().open(15106);
                    viewer.getPacketSender().sendInterfaceDisplayState(15150, false);
                    return true;
                }
                case 26072, 34024 -> {
                    int count = 0;
                    boolean toggle = this.placeHolder;
                    this.placeHolder = false;
                    this.container.setFiringEvents(false);
                    for (Item item : this.container.toArray()) {
                        if (item != null && item.getAmount() == 0) {
                            int slot = this.container.getSlot(item.getId());
                            int tab = this.tabForSlot(slot);
                            this.changeTabAmount(tab, -1);
                            this.container.remove(item);
                            this.container.shift();
                            this.placeHolderAmount -= this.placeHolderAmount - count;
                            count++;
                        }
                    }
                    this.placeHolder = toggle;
                    this.container.setFiringEvents(true);
                    this.container.refresh();
                    viewer.message(count == 0 ? "You don't have any placeholders to release." : "You have released " + count + " placeholders.");
                    viewer.getPacketSender().sendString(34024, "Release all placeholders (" + this.placeHolderAmount + ")");
                    return true;
                }

                // Placeholders
                case 26101 -> {
                    boolean active = this.placeHolder = !this.placeHolder;
                    viewer.getPacketSender().setWidgetActive(26101, active);
                    return true;
                }

                /* Deposit Inventory */
                case 26103 -> {
                    depositInventory(viewer);
                    return true;
                }

                /* Deposit Equipment */
                case 26104 -> {
                    depositeEquipment(viewer);
                    return true;
                }
                case 5386 -> {
                    noting = true;
                    return true;
                }
                case 5387 -> {
                    noting = false;
                    return true;
                }
                case 8130 -> {
                    inserting = false;
                    return true;
                }
                case 8131 -> {
                    inserting = true;
                    return true;
                }

                /* Close Bank */
                case 26002 -> {
                    viewer.getInterfaceManager().close();
                    return true;
                }
                case 26905 -> viewer.getLootingBag().depositLootingBag();
                case 26108 -> { // Quantity all
                    quantityAll = true;
                    quantityOne = false;
                    quantityFive = false;
                    quantityTen = false;
                    quantityX = false;
                    viewer.getPacketSender().sendConfig(314, 1);
                    viewer.getPacketSender().sendConfig(315, 0);
                    viewer.getPacketSender().sendConfig(316, 0);
                    viewer.getPacketSender().sendConfig(317, 0);
                    viewer.getPacketSender().sendConfig(320, 0);
                    return true;
                }
                case 26109 -> {
                    viewer.setEnterSyntax(new EnterSyntax() {
                        @Override
                        public void handleSyntax(Player player, long input) {
                            GroupIronman group = GroupIronman.getGroup(player.getUID());
                            group.getGroupBank().currentQuantityX = input == 0 ? 1 : (int) Math.min(input, Integer.MAX_VALUE);
                            player.getPacketSender().updateWidgetTooltipText(26109, "Default quantity: " + group.getGroupBank().currentQuantityX);
                        }
                    });
                    viewer.getPacketSender().sendEnterAmountPrompt("How many would you like to deposit/withdraw?");
                    quantityX = true;
                    quantityOne = false;
                    quantityFive = false;
                    quantityTen = false;
                    quantityAll = false;
                    viewer.getPacketSender().sendConfig(314, 0);
                    viewer.getPacketSender().sendConfig(315, 1);
                    viewer.getPacketSender().sendConfig(316, 0);
                    viewer.getPacketSender().sendConfig(317, 0);
                    viewer.getPacketSender().sendConfig(320, 0);
                }
                case 26110 -> { // Quantity ten
                    quantityTen = true;
                    quantityOne = false;
                    quantityFive = false;
                    quantityX = false;
                    quantityAll = false;
                    viewer.getPacketSender().sendConfig(314, 0);
                    viewer.getPacketSender().sendConfig(315, 0);
                    viewer.getPacketSender().sendConfig(316, 1);
                    viewer.getPacketSender().sendConfig(317, 0);
                    viewer.getPacketSender().sendConfig(320, 0);
                }
                case 26111 -> { // Quantity five
                    quantityFive = true;
                    quantityOne = false;
                    quantityTen = false;
                    quantityX = false;
                    quantityAll = false;
                    viewer.getPacketSender().sendConfig(314, 0);
                    viewer.getPacketSender().sendConfig(315, 0);
                    viewer.getPacketSender().sendConfig(316, 0);
                    viewer.getPacketSender().sendConfig(317, 1);
                    viewer.getPacketSender().sendConfig(320, 0);
                }
                case 26112 -> { //Quantity one
                    quantityOne = true;
                    quantityFive = false;
                    quantityTen = false;
                    quantityX = false;
                    quantityAll = false;
                    viewer.getPacketSender().sendConfig(314, 0);
                    viewer.getPacketSender().sendConfig(315, 0);
                    viewer.getPacketSender().sendConfig(316, 0);
                    viewer.getPacketSender().sendConfig(317, 0);
                    viewer.getPacketSender().sendConfig(320, 1);
                }
                case 34004 -> {
                    GroupIronman group = GroupIronman.getGroup(viewer.getUID());
                    group.getGroupBank().open(viewer, group);
                }
                case 34016, 34017, 34018, 34019, 34020, 34021 -> viewer.message("This option is unavailable.");
                case 34008, 34009 -> {
                    this.show_item_in_tab = !this.show_item_in_tab;
                    viewer.getPacketSender().sendConfig(750, show_item_in_tab ? 1 : 0);

                    //Reset other options
                    this.show_number_in_tab = false;
                    viewer.getPacketSender().sendConfig(751, 0);
                    this.show_roman_number_in_tab = false;
                    viewer.getPacketSender().sendConfig(752, 0);
                }
                case 34010, 34011 -> {
                    this.show_number_in_tab = !this.show_number_in_tab;
                    viewer.getPacketSender().sendConfig(751, show_number_in_tab ? 1 : 0);

                    //Reset other options
                    this.show_item_in_tab = false;
                    viewer.getPacketSender().sendConfig(750, 0);
                    this.show_roman_number_in_tab = false;
                    viewer.getPacketSender().sendConfig(752, 0);
                }
                case 34012, 34013 -> {
                    this.show_roman_number_in_tab = !this.show_roman_number_in_tab;
                    viewer.getPacketSender().sendConfig(752, show_roman_number_in_tab ? 1 : 0);

                    //Reset other options
                    this.show_item_in_tab = false;
                    viewer.getPacketSender().sendConfig(750, 0);
                    this.show_number_in_tab = false;
                    viewer.getPacketSender().sendConfig(751, 0);
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "{GroupBank}=" + Arrays.toString(this.container.toNonNullArray());
    }
}
