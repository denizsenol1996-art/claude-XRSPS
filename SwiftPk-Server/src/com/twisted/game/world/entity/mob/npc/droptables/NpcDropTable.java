package com.twisted.game.world.entity.mob.npc.droptables;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.twisted.game.content.skill.impl.slayer.SlayerConstants;
import com.twisted.game.content.skill.impl.slayer.slayer_task.SlayerCreature;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.npc.pets.Pet;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.ground.GroundItem;
import com.twisted.game.world.items.ground.GroundItemHandler;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.Color;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NpcDropTable {
    @JsonProperty("npcId")
    private int[] npcId;
    @JsonProperty("petItem")
    private String petItem;
    @JsonProperty("petRarity")
    private int petRarity;
    @JsonProperty("alwaysDrops")
    private List<ItemDrop> alwaysDrops;
    @JsonProperty("drops")
    private List<ItemDrop> drops;

    public void postLoad() {
        drops.sort(Comparator.comparingInt(ItemDrop::getChance));
    }

    public List<Item> getDrops(Player player, int rolls) {
        List<Item> list = new ArrayList<>();
        if (!alwaysDrops.isEmpty()) {
            for (var drops : alwaysDrops) {
                list.add(new Item(ItemRepository.getItemId(drops.getItem()), World.getWorld().random(Math.max(drops.getMinimumAmount(), 1), Math.max(drops.getMaximumAmount(), 1))));
            }
        }
        double rateBonus = 1D - (player.dropRateBonus() / 100D);
        List<ItemDrop> temp = new ArrayList<>(drops);
        Collections.shuffle(temp);
        for (int i = 0; i < rolls; i++) {
            if (!temp.isEmpty()) {
                for (var drop : temp) {
                    if (World.getWorld().random().nextInt((int) Math.ceil(drop.getChance() * rateBonus)) == 1) {
                        int minimum = Math.max(drop.getMinimumAmount(), 1);
                        int maximum = Math.max(drop.getMaximumAmount(), 1);
                        list.add(new Item(ItemRepository.getItemId(drop.getItem()), World.getWorld().random(minimum, maximum)));
                        break;
                    }
                }
            }
        }
        return list;
    }

    public void rollForLarransKey(Npc npc, Player player) {
        var inWilderness = WildernessArea.inWilderness(player.tile());
        SlayerCreature task = SlayerCreature.lookup(player.getAttribOr(AttributeKey.SLAYER_TASK_ID, 0));

        if (inWilderness) {
            var isSlayerTask = task != null && task.matches(npc.id());
            var combatLvl = npc.def().combatlevel;
            var dropRate = combatLvl >= 1 && combatLvl <= 80 ? 1972 : 99;

            //if players are assigned to kill slayer monsters, the drop chance is increased by 25%
            if (isSlayerTask) {
                var reduction = dropRate * 25 / 100;
                dropRate -= reduction;
            }

            var larransLuck = player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.LARRANS_LUCK);
            if (larransLuck) {
                var reduction = dropRate * 15 / 100;
                dropRate -= reduction;
            }

            if (World.getWorld().rollDie(dropRate, 1)) {
                GroundItemHandler.createGroundItem(new GroundItem(new Item(ItemIdentifiers.LARRANS_KEY), player.tile(), player));
                player.message(Color.PURPLE.wrap("A larran's key appeared."));
            }
        }
    }

    public void rollForKeyOfDrops(Player player, Npc npc) {
        SlayerCreature task = SlayerCreature.lookup(player.getAttribOr(AttributeKey.SLAYER_TASK_ID, 0));
        if (task != null && task.matches(npc.id()) && player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.KEY_OF_BOXES)) {
            int roll = 1000;
            if (World.getWorld().rollDie(roll, 1)) {
                player.message(Color.PURPLE.wrap("A key of drops appeared."));
                player.inventory().addOrDrop(new Item(CustomItemIdentifiers.KEY_OF_BOXES));
            }
        }
    }

    public Optional<Pet> rollForPet(Player player) {
        int petItemId = ItemRepository.getItemId(petItem);
        if (petItemId > 0 && petRarity > 0) {
            var roll = petRarity;
            var reduction = roll * player.dropRateBonus() / 150;
            roll -= reduction;
            if (World.getWorld().rollDie(player.hasPetOut("Jawa pet") ? roll / 2 : roll, 1)) {
                return Optional.ofNullable(Pet.getPetByItem(petItemId));
            }
        }
        return Optional.empty();
    }
}

