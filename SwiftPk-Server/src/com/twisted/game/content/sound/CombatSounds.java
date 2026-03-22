package com.twisted.game.content.sound;

import com.twisted.game.world.entity.combat.weapon.WeaponType;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.util.ItemIdentifiers;
import com.twisted.util.Utils;

/**
 * Created by Situations on 2/8/2016.
 */
public class CombatSounds {

    public static int weapon_attack_sounds(Player player) {
        Item weapon = player.getEquipment().get(EquipSlot.WEAPON);

        WeaponType byInterfaceType = player.getCombat().getWeaponType();
        if (weapon == null) {
            return -1;
        }
        // Special cases that don't match the below
        switch (weapon.getId()) {
        // Godswords
        case 11802:
        case 20593:
        case 20368:
        case 11804:
        case 20370:
        case 11806:
        case 20372:
        case 11808:
        case 20374:
            return 3846;
            
        // Wands
        case 6908:
        case 6910:
        case 6912:
        case 6914:
        case 10150:
        case 11012:
        case 12422:
            return 2563;

        // Misc items
        case ItemIdentifiers.GRANITE_MAUL_24225:
            return 2714;
        case 4726:
            return 1328;
        case 4755:
            return 1323;
        case 4747:
            return 1332;
        case 4718:
            return 1321;
        case 6528:
            return 2520;

        }
        
        // Fallback cases
        
        switch(byInterfaceType) {
        case LONGSWORD:
            return 2500;
        case DAGGER:
            return 2517;
        case PICKAXE:
            return 2498;
            case MAGIC_STAFF:
            return 2555;
        case AXE:
            return 2498;
        case MACE:
            return 2508; 
        case HAMMER:
            return 2567;
        case CROSSBOW:
            return 2695;
        case BOW:
            return 2693;
        case THROWN:
            return 2696;
        case WHIP:
            return 2720;
        case CLAWS:
            break;
        case DINHS_BULWARK:
            break;
        case GHRAZI_RAPIER:
            break;
        case HALBERD:
            break;
        case SALAMANDER:
            break;
        case SCYTHE:
            break;
        case SPEAR:
            break;
        case SWORD:
            break;
        case TWOHANDED:
            break;
        case UNARMED:
            break;
        default:
            break;
        }
        return -1;
    }

    public static int block_sound(Player player, int hit) {
        Item shield = player.getEquipment().get(EquipSlot.SHIELD);
        
        if (shield != null && shield.getId() != -1) {
            int sounds[] = {2860, 2861, 2862, 2863};
            return Utils.randomElement(sounds);
        }
        
        return 511; // Block without shield
    }

    public static int damage_sound() {
        int[] array = {518, 509, 510};
        return Utils.randomElement(array);
    }

    // For weapons it's bound to be off of the attack styles interface
    public static void weapon_equip_sounds(Player player, int slot, int itemId) {
        boolean other = Utils.random(1) == 1;
        int sound = other ? 2238 : 2244;
        String name = new Item(itemId).name().toLowerCase();
        boolean metal = name.contains("plate") || name.contains("sword") || name.contains("dagger") || name.contains("hammer");
        boolean shield = name.contains("shield");
        boolean chaps = name.contains("chaps"); // dhide body aint same sound
        boolean bow = name.contains("bow");
        if (bow)
            sound = 2244;
        else if (metal && slot == 3)
            sound = 2248; // metal 'ting' like platebody wearing
        if (shield)
            sound = 2245;
        if (chaps)
            sound = 2241;
        if (metal && slot != 3)
            sound = 2242;
        
        switch(slot) {
        case EquipSlot.HEAD:
            sound = 2240;
            break;
        case EquipSlot.HANDS:
            sound = 2236;
            break;
        case EquipSlot.FEET:
            sound = 2237;
            break;
        }
        
        if (slot == 3 && name.contains("dark bow"))
            sound = 3738;
        if (name.contains("ava"))
            sound = 3284;
        
        player.sound(sound);
    }

}
