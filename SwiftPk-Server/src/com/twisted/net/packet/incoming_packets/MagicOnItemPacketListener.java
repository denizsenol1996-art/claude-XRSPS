package com.twisted.net.packet.incoming_packets;

import com.twisted.GameServer;
import com.twisted.game.GameConstants;
import com.twisted.game.content.skill.impl.magic.JewelleryEnchantment;
import com.twisted.game.content.skill.impl.smithing.Bar;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.magic.MagicClickSpells;
import com.twisted.game.world.entity.combat.magic.Spell;
import com.twisted.game.world.entity.mob.player.MagicSpellbook;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.RequiredItem;
import com.twisted.game.world.items.ground.GroundItem;
import com.twisted.game.world.position.Tile;
import com.twisted.net.packet.ClientToServerPackets;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;

import java.util.Arrays;
import java.util.Optional;

import static com.twisted.util.ItemIdentifiers.BLOOD_MONEY;
import static com.twisted.util.ItemIdentifiers.COINS_995;

/**
 * Handles the packet for using magic spells on items ingame.
 *
 * @author Professor Oak
 */
public class MagicOnItemPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (packet.getOpcode() == ClientToServerPackets.MAGIC_ON_ITEM_OPCODE) {
            final int slot = packet.readShort();
            final int itemId = packet.readShortA();
            final int childId = packet.readShort();
            final int spellId = packet.readShortA();

            if (player.locked()) {
                return;
            }

            // Some checks
            if (slot < 0 || slot > 27) {
                return;
            }
            Item item = player.inventory().get(slot);
            if (item == null || item.getId() != itemId) {
                return;
            }

            final Optional<MagicClickSpells.MagicSpells> magicSpell = MagicClickSpells.MagicSpells.find(spellId);

            if (magicSpell.isEmpty()) {
                return;
            }

            player.debugMessage("[MagicOnItemPacket] spell=" + magicSpell + " itemId=" + itemId + " slot=" + slot + " childId=" + childId);

            if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
                player.getBankPin().openIfNot();
                return;
            }

            if (player.askForAccountPin()) {
                player.sendAccountPinMessage();
                return;
            }

            if (!player.dead()) {
                player.stopActions(false); // Seems like this does not cancel moving on rs? I can alch while I run.

                //Do actions...
                final MagicClickSpells.MagicSpells magicSpell2 = magicSpell.get();
                final Spell spell = magicSpell2.getSpell();
                final int itemValue = item.definition(World.getWorld()).highAlchValue();

                switch (magicSpell2) {
                    case SUPERHEAT_ITEM:
                        if (player.getSpellbook() != MagicSpellbook.NORMAL)
                            return;
                        if (!player.getClickDelay().elapsed(500)) {
                            return;
                        }

                        Optional<Bar> data = Bar.getDefinitionByItem(item.getId());

                        if (data.isEmpty()) {
                            player.message("You can not super heat this item!");
                            return;
                        }

                        for (RequiredItem requiredItem : data.get().getOres()) {
                            if (!player.inventory().containsAll(requiredItem.getItem())) {
                                player.message("You do not contain the required items to super heat!");
                                return;
                            }
                        }

                        if (player.skills().xpLevel(Skills.SMITHING) < data.get().getLevelReq()) {
                            player.message("You need a smithing level of " + data.get().getLevelReq() + " to do super heat this item!");
                            return;
                        }

                        player.animate(722);
                        player.graphic(148);
                        player.getPacketSender().sendTab(6);
                        for (RequiredItem requiredItem : data.get().getOres()) {
                            player.inventory().removeAll(requiredItem.getItem());
                        }
                        player.inventory().addAll(new Item(data.get().getBar()));
                        player.skills().addXp(Skills.MAGIC, spell.baseExperience(), true);
                        player.skills().addXp(Skills.SMITHING, data.get().getXpReward(), true);
                        player.getClickDelay().reset();
                        return;
                    case ENCHANT_SAPPHIRE:
                    case ENCHANT_DIAMOND:
                    case ENCHANT_EMERALD:
                    case ENCHANT_ONYX:
                    case ENCHANT_ZENYTE:
                    case ENCHANT_DRAGONSTONE:
                    case ENCHANT_RUBY_TOPAZ:
                        if (JewelleryEnchantment.check(player, itemId, spellId)) {
                            if (!spell.canCast(player, null, spell.deleteRunes())) {
                                return;
                            }
                            JewelleryEnchantment.enchantItem(player, itemId);
                        }
                        return;
                    case LOW_ALCHEMY:
                        if (!item.rawtradable() || item.getId() == BLOOD_MONEY || item.getId() == COINS_995) {
                            player.message("You can't alch that item.");
                            return;
                        }

                        Item finalItem1 = item;
                        if (Arrays.stream(GameConstants.DONATOR_ITEMS).anyMatch(donator_item -> donator_item == finalItem1.getId())) {
                            player.message("You cannot alch that item.");
                            return;
                        }

                        if (!spell.canCast(player, null, spell.deleteRunes())) {
                            return;
                        }

                        int coinAmountToGive = (int) Math.floor(itemValue * 0.15);

                        if (item.getValue() == 0) {
                            coinAmountToGive = 0;
                        }

                        spell.startCast(player, null);

                        item = new Item(item.getId(), 1);

                        player.inventory().remove(item, slot);
                        if (!GameServer.properties().pvpMode)
                            player.inventory().add(COINS_995, coinAmountToGive);
                        return;
                    case HIGH_ALCHEMY:
                        if (!item.rawtradable() || item.getId() == BLOOD_MONEY || item.getId() == COINS_995) {
                            player.message("You can't alch that item.");
                            return;
                        }

                        Item finalItem = item;
                        if (Arrays.stream(GameConstants.DONATOR_ITEMS).anyMatch(donator_item -> donator_item == finalItem.getId())) {
                            player.message("You cannot alch that item.");
                            return;
                        }

                        if (!spell.canCast(player, null, spell.deleteRunes())) {
                            return;
                        }

                        coinAmountToGive = (int) Math.floor(itemValue * 0.25);


                        if (item.getValue() == 0) {
                            coinAmountToGive = 0;
                        }

                        spell.startCast(player, null);

                        item = new Item(item.getId(), 1);

                        player.inventory().remove(item, slot);

                        if (!GameServer.properties().pvpMode)
                            player.inventory().add(COINS_995, coinAmountToGive);
                        return;
                }
            }
        }

        if (packet.getOpcode() == ClientToServerPackets.MAGIC_ON_GROUND_ITEM_OPCODE) {
            final int groundItemX = packet.readLEShort();
            final int groundItemId = packet.readUnsignedShort();
            final int groundItemY = packet.readLEShort();
            final int spellId = packet.readShortA();

            if (!player.locked() && !player.dead()) {
                Tile tile = new Tile(groundItemX, groundItemY, player.tile().level);
                Optional<MagicClickSpells.MagicSpells> spell = MagicClickSpells.MagicSpells.find(spellId);
                if (spell.isEmpty()) {
                    return;
                }

                Optional<GroundItem> groundItem = Optional.of(new GroundItem(new Item(groundItemId), tile, player));
                player.putAttrib(AttributeKey.INTERACTED_GROUNDITEM, groundItem.get());
                player.putAttrib(AttributeKey.INTERACTION_OPTION, 4);

                switch (spell.get()) {
                    case TELEKINETIC_GRAB:
                        break;
                }
            }
        }
    }
}
