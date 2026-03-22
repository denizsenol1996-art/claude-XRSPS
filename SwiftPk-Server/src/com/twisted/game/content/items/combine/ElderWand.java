package com.twisted.game.content.items.combine;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;

import static com.twisted.util.CustomItemIdentifiers.*;

/**
 * @author Patrick van Elderen | May, 19, 2021, 18:05
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class ElderWand extends PacketInteraction {

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(option == 1) {
            if(item.getId() == ELDER_WAND_HANDLE) {
                player.message("Perhaps I should combine this with a Elder wand stick.");
                return true;
            }
            if(item.getId() == ELDER_WAND_STICK) {
                player.message("Perhaps I should combine this with a Elder wand handle.");
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        if ((use.getId() == ELDER_WAND_HANDLE || usedWith.getId() == ELDER_WAND_HANDLE) && (use.getId() == ELDER_WAND_STICK || usedWith.getId() == ELDER_WAND_STICK)) {
            if (player.inventory().containsAll(ELDER_WAND_HANDLE, ELDER_WAND_STICK)) {
                player.confirmDialogue(new String[]{"Are you sure you wish to combine the Elder wand stick and the", "handle to create the Elder wand", "This can not be reversed."}, "", "Proceed with the combination.", "Cancel.", () -> {
                    if(!player.inventory().containsAll(ELDER_WAND_HANDLE, ELDER_WAND_STICK)) {
                        return;
                    }
                    player.animate(4462);
                    player.graphic(759,15,0);
                    player.inventory().remove(ELDER_WAND_HANDLE);
                    player.inventory().remove(ELDER_WAND_STICK);
                    player.inventory().add(new Item(ELDER_WAND));
                    player.message("You successfully combine the Elder wand stick and the handle to create the");
                    player.message("Elder wand.");
                    player.itemDialogue("You successfully combine the Elder wand stick and the<br>handle to create the Elder wand.", ELDER_WAND);
                });
            }
            return true;
        }
        return false;
    }

    public static final int BASE_EXP = 100;
    public static final int CRUCIATUS_CURSE_SPELL = 6;
    public static final int PETRIFICUS_TOTALUS_SPELL = 7;
    public static final int AVADA_KEDAVRA_SPELL = 8;
    public static final int EXPELLIARMUS_SPELL = 9;
    public static final int SECTUMSEMPRA_SPELL = 10;
    public static final int CRUCIATUS_CURSE_PROJECTILE = 127;
    public static final int PETRIFICUS_TOTALUS_PROJECTILE = 1535;
    public static final int AVADA_KEDAVRA_PROJECTILE = 335;
    public static final int EXPELLIARMUS_PROJECTILE = 1737;
    public static final int SECTUMSEMPRA_PROJECTILE = 1735;
    public static final int AVADA_KEDAVRA_BASE_MAX_HIT = 42;//Killing curse, one hits non player targets.
    public static final int CRUCIATUS_CURSE_SPELL_BASE_MAX_HIT = 47;//The curse inflicts intense, excruciating pain on the victim.
    public static final int PETRIFICUS_TOTALUS_SPELL_BASE_MAX_HIT = 47;//Binding spell, 10 seconds.
    public static final int EXPELLIARMUS_SPELL_BASE_MAX_HIT = 47;//Disarming spell.
    public static final int SECTUMSEMPRA_SPELL_BASE_MAX_HIT = 47;//Spell dealing small cuts.

}
