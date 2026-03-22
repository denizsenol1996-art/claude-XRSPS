package com.twisted.game.world.entity.mob.player.ironman;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.twisted.game.world.entity.mob.player.IronMode;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.util.Utils;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.compress.utils.Lists;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: Origin
 * @Date: 3/6/2024
 */
@Data
@ToString
@SuppressWarnings("ALL")
public final class GroupIronman {
    @JsonProperty("ownerUID")
    long ownerUID;
    @JsonProperty("memberUID")
    Set<Long> memberUID;
    @JsonProperty("groupBank")
    final GroupBank groupBank;
    @JsonProperty("isHardcore")
    final boolean isHardcore;
    @JsonProperty("groupLives")
    int groupLives;

    /**
     * Constructor used to create a Normal Group Ironman Group
     *
     * @param ownerUID
     * @param memberUID
     * @param groupBank
     * @param gameMode
     * @param isHardcore
     */
    public GroupIronman(long ownerUID, Set<Long> memberUID, GroupBank groupBank, boolean isHardcore) {
        this.ownerUID = ownerUID;
        this.memberUID = memberUID;
        this.groupBank = groupBank;
        this.isHardcore = isHardcore;
    }

    /**
     * Constructor used to create a Hardcore Group Ironman Group
     *
     * @param ownerUID
     * @param memberUID
     * @param groupBank
     * @param gameMode
     * @param isHardcore
     * @param groupLives
     */
    public GroupIronman(long ownerUID, Set<Long> memberUID, GroupBank groupBank, boolean isHardcore, int groupLives) {
        this.ownerUID = ownerUID;
        this.memberUID = memberUID;
        this.groupBank = groupBank;
        this.isHardcore = isHardcore;
        this.groupLives = groupLives;
    }

    /**
     * Method to append and update the Group Ironman group
     * @param group
     */
    public synchronized void update() {
        GroupIronmanWriter.writeGroupIronman(this);
    }

    /**
     * Method to identify and get the Group Ironman Group
     *
     * @param UUID
     * @return
     */
    public static GroupIronman getGroup(Long UUID) {
        return GroupBankLoader.groupIronmanMap.getOrDefault(UUID, null);
    }

    /**
     * Method builder to create The Group Ironman Group
     *
     * @param owner
     */
    public static void create(@Nonnull Player owner) {
        GroupIronman group = owner.ironMode().equals(IronMode.HARDCORE) ? createHardcoreGroup(owner, 3) : createGroup(owner);
        if (owner.ironMode().equals(IronMode.HARDCORE)) GroupIronmanWriter.writeGroupIronman(group);
        else GroupIronmanWriter.writeGroupIronman(group);
    }


    /**
     * @param owner
     * @param gameMode
     * @param groupLives
     * @return
     * @Static Class Builder to create a Hardcore Group Ironman Group
     */
    public static GroupIronman createHardcoreGroup(@Nonnull Player owner, int groupLives) {
        GroupIronman existing = GroupIronman.getGroup(owner.getUID());
        if (existing != null) return existing;
        else return new GroupIronman(owner.getUID(), new HashSet<>(), new GroupBank(), true, groupLives);
    }

    /**
     * @param owner
     * @param gameMode
     * @return
     * @Static Class Builder to create a Normal Group Ironman Group
     */
    public static GroupIronman createGroup(@Nonnull Player owner) {
        GroupIronman existing = GroupIronman.getGroup(owner.getUID());
        if (existing != null) return existing;
        else return new GroupIronman(owner.getUID(), new HashSet<>(), new GroupBank(), false);
    }

    /**
     * Method to release Ownership of The currrent Group Ironmans Group leader & Reset
     *
     * @param currentOwner
     * @param newOwner
     */
    public void releaseOwnership(@Nonnull Player currentOwner, Player newOwner) {
        if (!isInGroup(currentOwner))
            return;

        this.setOwnerUID(-1L);
        for (long groupUID : Lists.newArrayList(this.memberUID.iterator())) {
            if (groupUID == -1L) continue;
            if (groupUID == currentOwner.getUID()) continue;
            this.memberUID.remove(groupUID);
        }
        long randomOwner = Utils.randomElement(this.memberUID);
        this.setOwnerUID(randomOwner);
    }

    /**
     * Method to add a member to the Group Ironmans Group
     *
     * @param member
     */
    public void addToGroup(@Nonnull Player member, @Nonnull Player owner) {
        if (!isRequirementsMetFor(member, owner)) return;
        this.memberUID.add(member.getUID());
    }

    /**
     * Method to leave the current Group Ironman group
     *
     * @param player
     */
    public void leaveGroup(@Nonnull Player player) {
        if (!isInGroup(player)) return;
        for (long groupUID : Lists.newArrayList(this.memberUID.iterator())) {
            if (groupUID == -1L) continue;
            if (groupUID == player.getUID()) continue;
            this.memberUID.remove(groupUID);
        }
    }

    /**
     * Method to check If enquiring player is inside the Group Ironmans group
     *
     * @param player
     * @return
     */
    private boolean isInGroup(@Nonnull Player player) {
        return this.memberUID.contains(player.getUID());
    }

    /**
     * Method to provide Pre-Requisites of GameMode to the Requesting Group Ironmans Group
     *
     * @param member
     * @return
     */
    private boolean isGameModeEqualTo(@Nonnull Player member, @Nonnull Player owner) {
        return member.ironMode().equals(owner.ironMode());
    }

    /**
     * Method to validate Requirements
     *
     * @param player
     * @return
     */
    public boolean isRequirementsMetFor(@Nonnull Player member, @Nonnull Player owner) {
        return !isInGroup(member) && isGameModeEqualTo(member, owner);
    }

    @Override
    public String toString() {
        String groupBankString = groupBank != null ? Arrays.toString(groupBank.container.toNonNullArray()) : "null";
        String memberUIDString = memberUID.isEmpty() ? "[]" : memberUID.toString();
        return "GroupIronman{" +
            "ownerUID=" + ownerUID +
            ", memberUID=" + memberUIDString +
            ", groupBank=" + groupBankString +
            ", isHardcore=" + isHardcore +
            ", groupLives=" + groupLives +
            '}';
    }
}
