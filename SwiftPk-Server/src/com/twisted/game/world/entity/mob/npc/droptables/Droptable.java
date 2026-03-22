package com.twisted.game.world.entity.mob.npc.droptables;

import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.ground.GroundItem;
import com.twisted.game.world.items.ground.GroundItemHandler;
import com.twisted.game.world.position.Tile;
import com.twisted.util.NpcIdentifiers;

import static com.twisted.game.content.collection_logs.data.LogType.BOSSES;
import static com.twisted.game.content.collection_logs.data.LogType.OTHER;

/**
 * Created by Bart on 10/6/2015.
 */
public interface Droptable {

    public void reward(Npc npc, Player killer);

    default void drop(Npc npc, Player player, Item item) {
        drop(npc, npc.tile(), player, item);
    }

    default void drop(Npc npc, Tile tile, Player player, Item item) {
        if (player.nifflerPetOut() && player.nifflerCanStore(npc) && item.getValue() > 0) {
            player.nifflerStore(item);
        } else if (!player.nifflerPetOut() && !player.nifflerCanStore(npc) && item.getValue() > 0 && npc.id() == NpcIdentifiers.ZULRAH || npc.id() == NpcIdentifiers.ZULRAH || npc.id() == NpcIdentifiers.ZULRAH) {
            player.getInventory().addOrDrop(new Item(item));
        } else {
            GroundItemHandler.createGroundItem(new GroundItem(item, tile, player));
        }
        BOSSES.log(player, npc.id(), item);
        OTHER.log(player, npc.id(), item);
    }

}
