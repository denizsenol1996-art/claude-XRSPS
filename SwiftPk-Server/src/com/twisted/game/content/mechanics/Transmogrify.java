package com.twisted.game.content.mechanics;

import com.twisted.fs.NpcDefinition;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.combat.magic.Autocasting;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.CustomItemIdentifiers;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by Mack on 10/23/2017.
 */
public class Transmogrify {

    private static final boolean ENABLED = true;

    enum TransmogItems {
        RING_OF_STONE(6583, 2188),
        RING_OF_NATURE(20005, 7314),
        RING_OF_COINS(20017, 7315),
        RING_OF_ELYSIAN(CustomItemIdentifiers.RING_OF_ELYSIAN,329, 336),
        EASTER_RING(7927, 5538, 5539, 5540, 5541, 5542, 5543);

        ;//end

        private final int id;
        private final int[] transmogId;

        TransmogItems(int id, int... transmogId) {
            this.id = id;
            this.transmogId = transmogId;
        }

        public int id() {
            return id;
        }

        public int[] transmogId() {
            return transmogId;
        }

    }

    public static void onItemEquip(Player player, Item item) {
        Arrays.stream(TransmogItems.values()).forEach(type -> {
            if (item.getId() == type.id()) {
                if (!canTransmogrify(player)) {
                    player.message("This doesn't seem like a good time to do this.");
                    return;
                }
                if (isTransmogrified(player)) {
                    player.message("This doesn't seem like a good idea while I'm like this.");
                    return;
                }
                transmog(player, type);
            }
        });
    }

    public static void onLogin(Player player) {
        Optional<TransmogItems> transmogType = Arrays.stream(TransmogItems.values()).filter(type -> player.getEquipment().contains(type.id())).findAny();
        transmogType.ifPresent(type -> transmog(player, type));
    }

    public static void onItemUnequip(Player player) {
        if (isTransmogrified(player)) {
            Optional<TransmogItems> transmogType = Arrays.stream(TransmogItems.values()).filter(type -> player.getEquipment().contains(type.id())).findAny();
            transmogType.ifPresent(transmogItems -> untransmog(player));
            Autocasting.setAutocast(player,null);
        }
    }

    /**
     * A flag to check if the player is currently transmog'd.
     */
    public static boolean isTransmogrified(Player player) {
        return (player.looks().trans() > -1 && player.looks().trans() != 716);
    }

    /**
     * Preconditions to be met prior to allowing the player to transmogrify.
     */
    public static boolean canTransmogrify(Player player) {
        if (!ENABLED) {
            return false;
        }
        if (WildernessArea.inWilderness(player.tile())) {
            return false;
        }
        if (player.getDueling().inDuel()) {
            return false;
        }
        return true;
    }

    /**
     * The main function to transmogrifying the player.
     */
    public static void transmog(Player player, TransmogItems data) {
        if (!ENABLED) {
            return;
        }

        player.stopActions(true);
        player.getInterfaceManager().clearAllSidebars();
        player.looks().transmog(World.getWorld().random(data.transmogId()));
        player.looks().renderData(World.getWorld().definitions().get(NpcDefinition.class, World.getWorld().random(data.transmogId())).renderpairs());
    }

    /**
     * Hard resets the player's render under certain circumstance such as being morphed in a area
     * they shouldn't or dying whilst morphed.
     */
    public static void hardReset(Player player) {
        untransmog(player);
    }

    /**
     * The main function to untransmogrifying the player.
     */
    public static void untransmog(Player player) {
        player.unlock();
        player.getInterfaceManager().close();
        player.getPacketSender().sendTabs();
        player.looks().transmog(-1);
        player.looks().resetRender();
        player.message("You return to your human-like state.");
    }

}
