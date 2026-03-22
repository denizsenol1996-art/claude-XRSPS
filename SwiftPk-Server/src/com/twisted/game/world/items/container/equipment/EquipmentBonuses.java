package com.twisted.game.world.items.container.equipment;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.npc.NpcCombatInfo;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import lombok.Data;

import static com.twisted.util.CustomItemIdentifiers.HWEEN_BLOWPIPE;
import static com.twisted.util.CustomItemIdentifiers.MAGMA_BLOWPIPE;
import static com.twisted.util.ItemIdentifiers.TOXIC_BLOWPIPE;

@Data
public class EquipmentBonuses {
    public int stab;
    public int slash;
    public int crush;
    public int range;
    public int mage;
    public int stabdef;
    public int slashdef;
    public int crushdef;
    public int rangedef;
    public int magedef;
    public int str;
    public int rangestr;
    public int magestr;
    public int pray;

    public EquipmentBonuses totalBonuses(Mob mob, EquipmentInfo info) {
        return totalBonuses(mob, info, false);
    }

    public EquipmentBonuses totalBonuses(Mob entity, EquipmentInfo info, boolean ignoreAmmo) {
        EquipmentBonuses bonuses = new EquipmentBonuses();
        if (entity instanceof Player player) {
            Item wep = player.getEquipment().get(EquipSlot.WEAPON);
            int weaponId = wep != null ? wep.getId() : -1;
            if (Equipment.hasAmmyOfDamned(player) && Equipment.hasVerac(player)) bonuses.pray += 4;
            for (int i = 0; i < 14; i++) {
                if (i == EquipSlot.AMMO && ignoreAmmo) continue;
                Item equipped = player.getEquipment().get(i);
                if (equipped != null) {
                    if (i == EquipSlot.AMMO && ((weaponId >= 4212 && weaponId <= 4223) || weaponId == TOXIC_BLOWPIPE || weaponId == MAGMA_BLOWPIPE || weaponId == HWEEN_BLOWPIPE))
                        continue;
                    EquipmentBonuses equip = info.bonuses(equipped.getId());
                    bonuses.stab += equip.stab;
                    bonuses.slash += equip.slash;
                    bonuses.crush += equip.crush;
                    bonuses.range += equip.range;
                    bonuses.mage += equip.mage;

                    bonuses.stabdef += equip.stabdef;
                    bonuses.slashdef += equip.slashdef;
                    bonuses.crushdef += equip.crushdef;
                    bonuses.rangedef += equip.rangedef;
                    bonuses.magedef += equip.magedef;

                    bonuses.str += equip.str;
                    bonuses.rangestr += equip.rangestr;
                    bonuses.magestr += equip.magestr;
                    bonuses.pray += equip.pray;
                }
            }

            if (Prayers.usingPrayer(player, Prayers.ANCIENT_STRENGTH)) {
                final Mob target = player.getCombat().getTarget();
                if (target != null) {
                    if (target instanceof Npc) {
                        bonuses.stab *= 1.20D;
                        bonuses.str *= 1.20D;
                    } else if (target instanceof Player) {
                        bonuses.stab *= 1.10D;
                        bonuses.str *= 1.10D;
                    }
                }
            }
            if (Prayers.usingPrayer(player, Prayers.ANCIENT_SIGHT)) {
                final Mob target = player.getCombat().getTarget();
                if (target != null) {
                    if (target instanceof Npc) {
                        bonuses.range *= 1.20D;
                        bonuses.rangestr *= 1.20D;
                    } else if (target instanceof Player) {
                        bonuses.range *= 1.10D;
                        bonuses.rangestr *= 1.10D;
                    }
                }
            }

            if (Prayers.usingPrayer(player, Prayers.ANCIENT_WILL)) {
                bonuses.mage *= 1.20D;
            }

            if (Prayers.usingPrayer(player, Prayers.VAPORISE)) {
                bonuses.mage *= 1.25D;
            }

            if (Prayers.usingPrayer(player, Prayers.ANNIHILATE)) {
                final Mob target = player.getCombat().getTarget();
                if (target != null) {
                    if (target instanceof Npc) {
                        bonuses.range *= 1.25D;
                        bonuses.rangestr *= 1.27D;
                    } else if (target instanceof Player) {
                        bonuses.range *= 1.12D;
                        bonuses.rangestr *= 1.12D;
                    }
                }
            }
            if (Prayers.usingPrayer(player, Prayers.DECIMATE)) {
                final Mob target = player.getCombat().getTarget();
                if (target != null) {
                    if (target instanceof Npc) {
                        bonuses.stab *= 1.25D;
                        bonuses.str *= 1.27D;
                    } else if (target instanceof Player) {
                        bonuses.stab *= 1.12D;
                        bonuses.str *= 1.12D;
                    }
                }
            }
            if (Prayers.usingPrayer(player, Prayers.TRINITAS)) {
                final Mob target = player.getCombat().getTarget();
                if (target != null) {
                    if (target instanceof Npc) {
                        bonuses.stab *= 1.15D;
                        bonuses.str *= 1.15D;
                        bonuses.range *= 1.15D;
                        bonuses.rangestr *= 1.15D;
                        bonuses.mage *= 1.15D;
                    } else if (target instanceof Player) {
                        bonuses.stab *= 1.05D;
                        bonuses.str *= 1.05D;
                        bonuses.range *= 1.05D;
                        bonuses.rangestr *= 1.05D;
                        bonuses.mage *= 1.05D;
                    }
                }
            }
        } else if (entity instanceof Npc npc) {
            if (npc.combatInfo() != null) {
                NpcCombatInfo.Bonuses i = npc.combatInfo().originalBonuses;
                bonuses.stabdef = i.stabdefence;
                bonuses.slashdef = i.slashdefence;
                bonuses.crushdef = i.crushdefence;
                bonuses.rangedef = i.rangeddefence;
                bonuses.magedef = i.magicdefence;
                bonuses.range = i.ranged;
                bonuses.mage = i.magic;
                bonuses.str = i.strength;
                bonuses.crush = i.attack;
                bonuses.stab = i.attack;
                bonuses.slash = i.attack;
            }
        }
        return bonuses;
    }

    public int[] bonuses() {
        return new int[]{stab, slash, crush, range, mage};
    }

    public String[] bonusesAtk() {
        return new String[]{"Stab", "Slash", "Crush", "Range", "Mage"};
    }

    @Override
    public String toString() {
        return "EquipmentBonuses{" +
            "stab=" + stab +
            ", slash=" + slash +
            ", crush=" + crush +
            ", range=" + range +
            ", mage=" + mage +
            ", stabdef=" + stabdef +
            ", slashdef=" + slashdef +
            ", crushdef=" + crushdef +
            ", rangedef=" + rangedef +
            ", magedef=" + magedef +
            ", str=" + str +
            ", rangestr=" + rangestr +
            ", magestr=" + magestr +
            ", pray=" + pray +
            '}';
    }
}
