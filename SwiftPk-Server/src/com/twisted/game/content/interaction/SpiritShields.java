package com.twisted.game.content.interaction;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.ItemIdentifiers;

public class SpiritShields extends PacketInteraction {

    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        final Item resource = use.name().equalsIgnoreCase("spectral sigil") || use.name().equalsIgnoreCase("arcane sigil") || use.name().equalsIgnoreCase("elysian sigil") ? use : usedWith;

        final Item spiritShield;
        if (resource == usedWith) {
            spiritShield = use;
        } else {
            spiritShield = usedWith;
        }

        if (!spiritShield.name().equalsIgnoreCase("blessed spirit shield")) {
            System.out.println("Spirit shield is not blessed");
            return false;
        }

        Item sigil = null;
        if (resource.name().equalsIgnoreCase("spectral sigil")) {
            sigil = resource;
        } else if (resource.name().equalsIgnoreCase("arcane sigil")) {
            sigil = resource;
        } else if (resource.name().equalsIgnoreCase("elysian sigil")) {
            sigil = resource;
        }

        if (sigil == null)
            return false;

        final Item shield = switch (resource.name().toLowerCase()) {
            case "spectral sigil" -> new Item(ItemIdentifiers.SPECTRAL_SPIRIT_SHIELD);
            case "arcane sigil" -> new Item(ItemIdentifiers.ARCANE_SPIRIT_SHIELD);
            case "elysian sigil" -> new Item(ItemIdentifiers.ELYSIAN_SPIRIT_SHIELD);
            default -> null;
        };

        if (shield == null)
            return false;

        if (player.skills().xpLevel(Skills.PRAYER) < 90) {
            player.message("You need a Prayer level of 90 to create an " + shield.name() + ".");
            return true;
        }

        if (player.skills().xpLevel(Skills.SMITHING) < 85) {
            player.message("You need a Smithing level of 85 to create an " + shield.name() + ".");
            return true;
        }

        player.getInventory().remove(sigil);
        player.getInventory().remove(spiritShield);
        player.getInventory().add(shield);
        return true;
    }
}
