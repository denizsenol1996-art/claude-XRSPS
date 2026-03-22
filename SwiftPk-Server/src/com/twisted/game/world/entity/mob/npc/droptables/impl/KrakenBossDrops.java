package com.twisted.game.world.entity.mob.npc.droptables.impl;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.npc.NpcDeath;
import com.twisted.game.world.entity.mob.npc.droptables.Droptable;
import com.twisted.game.world.entity.mob.npc.droptables.ScalarLootTable;
import com.twisted.game.world.entity.mob.npc.pets.Pet;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.Area;
import com.twisted.util.Utils;

import java.util.ArrayList;
import java.util.Optional;

import static com.twisted.game.content.collection_logs.data.LogType.BOSSES;

/**
 * @author Patrick van Elderen | January, 03, 2021, 14:56
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class KrakenBossDrops implements Droptable {

    @Override
    public void reward(Npc killed, Player killer) {
        var table = ScalarLootTable.forNPC(494);
        if (table != null) {
            var loots = new ArrayList<String>();
            table.getGuaranteedDrops().forEach(tableItem -> {
                drop(killed, killer.tile(), killer, new Item(tableItem.id, Utils.random(tableItem.min, tableItem.max)));
                loots.add(lootname(tableItem.convert(), killer.getUsername()));
            });

            var rolls = 1;
            for (int i = 0; i < rolls; i++) {
                var reward = table.randomItem(World.getWorld().random());
                if (reward != null) {
                    drop(killed, killer.tile(), killer, reward);
                    loots.add(lootname(reward, killer.getUsername()));
                }
            }

            var room = new Area(killer.tile().regionCorner(), killer.tile().regionCorner().transform(64, 64));
            // Broadcast to everyone in the normal kraken room.
            World.getWorld().getPlayers().forEachInArea(room, other -> {
                for (String lootmsg : loots) {
                    other.message(lootmsg);
                }
            });

            Optional<Pet> pet = NpcDeath.checkForPet(killer, table);
            if (pet.isPresent()) {
                BOSSES.log(killer, killed.id(), new Item(pet.get().item));
            }
        }
    }

    private String lootname(Item convert, String looterName) {
        return String.format("<col=0B610B>%s received a drop: %s.", looterName, convert.unnote().getAmount() == 1 ? convert.unnote().name() : "" + convert.getAmount() + " x " + convert.unnote().name());
    }
}
