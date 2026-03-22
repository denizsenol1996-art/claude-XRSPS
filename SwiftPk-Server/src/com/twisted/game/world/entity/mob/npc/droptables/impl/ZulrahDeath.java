package com.twisted.game.world.entity.mob.npc.droptables.impl;

import com.twisted.game.content.announcements.ServerAnnouncements;
import com.twisted.game.content.skill.impl.slayer.SlayerConstants;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.npc.NpcDeath;
import com.twisted.game.world.entity.mob.npc.droptables.Droptable;
import com.twisted.game.world.entity.mob.npc.droptables.ScalarLootTable;
import com.twisted.game.world.entity.mob.npc.pets.Pet;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.Tile;

import java.util.Optional;

import static com.twisted.game.content.collection_logs.data.LogType.BOSSES;
import static com.twisted.game.content.treasure.TreasureRewardCaskets.MASTER_CASKET;
import static com.twisted.game.world.entity.AttributeKey.DOUBLE_DROP_LAMP_TICKS;
import static com.twisted.util.ItemIdentifiers.*;
//night
/**
 * @author Patrick van Elderen | January, 03, 2021, 14:37
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class ZulrahDeath implements Droptable {

    @Override//night
    public void reward(Npc npc, Player killer) {
        var table = ScalarLootTable.forNPC(2042);
        if (table != null) {
            drop(npc, new Tile(killer.getX(), killer.getY(), killer.tile().level), killer, new Item(ZULRAHS_SCALES, World.getWorld().random(400, 800)));
            drop(npc, new Tile(killer.getX(), killer.getY(), killer.tile().level), killer, new Item(BLOOD_MONEY, World.getWorld().random(100, 500)));

            Optional<Pet> pet = NpcDeath.checkForPet(killer, table);
            pet.ifPresent(value -> BOSSES.log(killer, npc.id(), new Item(value.item)));

            if (World.getWorld().rollDie(50, 1)) {
                drop(npc, new Tile(2262, 3072, killer.tile().level), killer, new Item(MASTER_CASKET,1));
                killer.message("<col=0B610B>You have received a treasure casket drop!");
            }

            var rolls = 2;
            var reward = table.randomItem(World.getWorld().random());
            for (int i = 0; i < rolls; i++) {
                if (reward != null) {
                    boolean doubleDropsLampActive = (Integer) killer.getAttribOr(DOUBLE_DROP_LAMP_TICKS, 0) > 0;
                    boolean founderImp = killer.pet() != null && killer.pet().def().name.equalsIgnoreCase("Founder Imp");
                    if (doubleDropsLampActive || founderImp) {
                        if(World.getWorld().rollDie(10, 1)) {
                            reward.setAmount(reward.getAmount() * 2);
                            killer.message("The double drop lamp effect doubled your drop.");
                        }
                    }
                    drop(npc, new Tile(killer.getX(), killer.getY(), killer.tile().level), killer, reward);
                    ServerAnnouncements.tryBroadcastDrop(killer, npc, reward);
                }
            }

            // Slayer unlock
            if (killer.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.DOUBLE_DROP_CHANCE) && World.getWorld().rollDie(100, 1)) {
                killer.message("The Double drops perk grants you a second drop!");
                if (reward != null) {
                    drop(npc, new Tile(killer.getX(), killer.getY(), killer.tile().level), killer, reward);
                    ServerAnnouncements.tryBroadcastDrop(killer, npc, reward);
                }
            }
        }
    }
}
