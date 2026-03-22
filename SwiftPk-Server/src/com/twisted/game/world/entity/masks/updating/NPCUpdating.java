package com.twisted.game.world.entity.masks.updating;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Entity;
import com.twisted.game.world.entity.combat.hit.Splat;
import com.twisted.game.world.entity.mob.Flag;
import com.twisted.game.world.entity.mob.UpdateFlag;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Tile;
import com.twisted.net.Session;
import com.twisted.net.packet.ByteOrder;
import com.twisted.net.packet.PacketBuilder;
import com.twisted.net.packet.PacketBuilder.AccessType;
import com.twisted.net.packet.PacketType;
import com.twisted.net.packet.ValueType;

import java.util.Iterator;
import java.util.List;

/**
 * Represents a player's npc updating task, which loops through all local
 * npcs and updates their masks according to their current attributes.
 *
 * @author Relex lawl
 */

public class NPCUpdating {

    /**
     * Handles the actual npc updating for the associated player.
     * @return    The NPCUpdating instance.
     */
    public static void update(Player player) {
        final PacketBuilder packet = new PacketBuilder(65, PacketType.VARIABLE_SHORT);
        try (final PacketBuilder update = new PacketBuilder()) {
            Tile tile = player.tile();
            Session session = player.getSession();
            List<Npc> npcs = player.getLocalNpcs();
            packet.initializeAccess(AccessType.BIT).putBits(8, npcs.size());
            Iterator<Npc> iterator = npcs.iterator();
            while (iterator.hasNext()) {
                Npc npc = iterator.next();
                if (npc == null) {
                    iterator.remove();
                    continue;
                }
                if (!npc.hidden() && !npc.isTeleportJump() && !npc.isNeedsPlacement() && tile.isViewableFrom(npc.tile()) && npc.isRegistered()) {
                    updateMovement(npc, packet);
                    if (npc.getUpdateFlag().isUpdateRequired()) {
                        appendUpdates(npc, player, update);
                    }
                    npc.inViewport(true);
                } else {
                    iterator.remove();
                    packet.putBits(1, 1).putBits(2, 3);
                }
            }
            for (Npc npc : World.getWorld().getNpcs()) {

                if (player.getLocalNpcs().size() >= 79)
                    break;

                if (npc == null || !npc.isRegistered() || npc.hidden() || npcs.contains(npc))
                    continue;

                if (npc.isRegistered() && tile.isViewableFrom(npc.tile())) {
                    npcs.add(npc);
                    addNPC(player, npc, packet, npc.isTeleportJump());
                    if ((npc.getUpdateFlag().isUpdateRequired() || sendNewNpcUpdates(npc))) {
                        appendUpdates(npc, player, update);
                    }
                    npc.inViewport(true);
                }
            }
            int writerIndex = update.buffer().writerIndex();
            if (writerIndex > 0) {
                packet.putBits(14, 16383).initializeAccess(AccessType.BYTE).writeBuffer(update.buffer());
                session.write(packet);
                return;
            }
            packet.initializeAccess(AccessType.BYTE);
            session.write(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean sendNewNpcUpdates(Npc npc) {
        return npc.getInteractingEntity() != null || (npc.getInteractingEntity() == null);
    }


    /**
     * Adds an npc to the associated player's client.
     *
     * @param npc     The npc to add.
     * @param builder The packet builder to write information on.
     * @return The NPCUpdating instance.
     */
    private static void addNPC(Player player, Npc npc, PacketBuilder builder, boolean legacyTeleport) {
        int yOffset = npc.tile().getY() - player.tile().getY();
        int xOffset = npc.tile().getX() - player.tile().getX();
        builder.putBits(14, npc.getIndex());
        builder.putBits(5, yOffset);
        builder.putBits(5, xOffset);
        builder.putBits(1, legacyTeleport ? 0 : 1);
        builder.putBits(14, npc.id());
        builder.putBits(1, (npc.getUpdateFlag().isUpdateRequired() || sendNewNpcUpdates(npc)) ? 1 : 0);
        boolean updateFacing = npc.walkRadius() == 0;
        builder.putBits(1, updateFacing ? 1 : 0);
        if (updateFacing) {
            Tile tile = new Tile(face(npc).x * 2 + 1, face(npc).y * 2 + 1);
            builder.putBits(14, tile.getX()); //face x
            builder.putBits(14, tile.getY()); //face y
        }
    }

    private static Tile face(Npc npc) {
        npc.tile();
        return switch (npc.spawnDirection()) {
            case 1 -> npc.tile().transform(0, 1); // n
            case 6 -> npc.tile().transform(0, -1); // s
            case 4 -> npc.tile().transform(1, 0); // e
            case 3 -> npc.tile().transform(-1, 0); // w
            case 0 -> npc.tile().transform(-1, 1); // nw
            case 2 -> npc.tile().transform(1, 1); // ne
            case 5 -> npc.tile().transform(-1, -1); // sw
            case 7 -> npc.tile().transform(-1, 1);
            default -> npc.tile(); // se
        };
    }

    /**
     * Updates the npc's movement queue.
     * @param npc        The npc who's movement is updated.
     * @param out    The packet builder to write information on.
     * @return            The NPCUpdating instance.
     */
    private static void updateMovement(Npc npc, PacketBuilder out) {
        if (npc.getRunningDirection().toInteger() == -1) {
            if (npc.getWalkingDirection().toInteger() == -1) {
                if (npc.getUpdateFlag().isUpdateRequired()) {
                    out.putBits(1, 1);
                    out.putBits(2, 0);
                } else {
                    out.putBits(1, 0);
                }
            } else {
                out.putBits(1, 1);
                out.putBits(2, 1);
                out.putBits(3, npc.getWalkingDirection().toInteger());
                out.putBits(1, npc.getUpdateFlag().isUpdateRequired() ? 1 : 0);
            }
        } else {
            out.putBits(1, 1);
            out.putBits(2, 2);
            out.putBits(3, npc.getWalkingDirection().toInteger());
            out.putBits(3, npc.getRunningDirection().toInteger());
            out.putBits(1, npc.getUpdateFlag().isUpdateRequired() ? 1 : 0);
        }
    }

    /**
     * Appends a mask update for {@code npc}.
     * @param npc        The npc to update masks for.
     * @param block    The packet builder to write information on.
     * @return            The NPCUpdating instance.
     */
    private static void appendUpdates(Npc npc, Player player, PacketBuilder block) {
        int mask = 0;
        UpdateFlag flag = npc.getUpdateFlag();
        if (flag.flagged(Flag.ANIMATION) && npc.getAnimation() != null) {
            mask |= 0x10;
        }
        if (flag.flagged(Flag.GRAPHIC) && npc.graphic() != null) {
            mask |= 0x80;
        }
        if (flag.flagged(Flag.FIRST_SPLAT)) {
            mask |= 0x8;
        }
        if (flag.flagged(Flag.ENTITY_INTERACTION)) {
            mask |= 0x20;
        }
        if (flag.flagged(Flag.FORCED_CHAT) && npc.getForcedChat() != null) {
            mask |= 0x1;
        }
        if (flag.flagged(Flag.TRANSFORM)) {
            mask |= 0x2;
        }
        if (flag.flagged(Flag.FACE_TILE) && npc.getFaceTile() != null) {
            mask |= 0x4;
        }
        block.put(mask);
        if (flag.flagged(Flag.ANIMATION) && npc.getAnimation() != null) {
            updateAnimation(block, npc);
        }
        if (flag.flagged(Flag.GRAPHIC) && npc.graphic() != null) {
            updateGraphics(block, npc);
        }
        if (flag.flagged(Flag.FIRST_SPLAT)) {
            updateSingleHit(block, npc);
        }
        if (flag.flagged(Flag.ENTITY_INTERACTION)) {
            Entity entity = npc.getInteractingEntity();
            block.putShort(entity == null ? -1 : entity.getIndex() + (entity instanceof Player ? 32768 : 0));
        }
        if (flag.flagged(Flag.FORCED_CHAT) && npc.getForcedChat() != null) {
            block.putString(npc.getForcedChat());
        }
        if (flag.flagged(Flag.TRANSFORM)) {
            block.putShort(npc.transmog() <= 0 ? npc.id() : npc.transmog(), ValueType.A, ByteOrder.LITTLE);
        }
        if (flag.flagged(Flag.FACE_TILE) && npc.getFaceTile() != null) {
            final Tile tile = npc.getFaceTile();
            block.putShort(tile.getX() * 2 + 1, ByteOrder.LITTLE);
            block.putShort(tile.getY() * 2 + 1, ByteOrder.LITTLE);
        }
    }

    /**
     * Updates {@code npc}'s current animation and displays it for all local players.
     * @param builder    The packet builder to write information on.
     * @param npc        The npc to update animation for.
     * @return            The NPCUpdating instance.
     */
    private static void updateAnimation(PacketBuilder builder, Npc npc) {
        builder.putShort(npc.getAnimation().getId(), ByteOrder.LITTLE);
        builder.put(npc.getAnimation().getDelay());
    }

    /**
     * Updates {@code npc}'s current graphics and displays it for all local players.
     *
     * @param builder The packet builder to write information on.
     * @param npc     The npc to update graphics for.
     * @return The NPCUpdating instance.
     */
    private static void updateGraphics(PacketBuilder builder, Npc npc) {
        builder.putShort(npc.graphic().id());
        builder.putInt(npc.graphic().delay() + (65536 * npc.graphic().height()));
    }

    /**
     * Updates the npc's single hit.
     * @param builder    The packet builder to write information on.
     * @param npc        The npc to update the single hit for.
     * @return            The NPCUpdating instance.
     */
    private static void updateSingleHit(PacketBuilder builder, Npc npc) {
        builder.put(Math.min(npc.splats.size(), 4)); // count
        for (int i = 0; i < Math.min(npc.splats.size(), 4); i++) {
            Splat splat = npc.splats.get(i);
            if (splat == null) continue;
            builder.putShort(splat.getDamage());
            builder.put(splat.getType().getId());
            builder.putShort(npc.hp());
            builder.putShort(npc.combatInfo() == null ? 1 : npc.combatInfo().stats == null ? 1 : npc.combatInfo().stats.hitpoints);
        }
    }
}
