package com.twisted.game.world.entity.mob.player.rights;

import com.twisted.GameServer;
import com.twisted.game.world.entity.mob.player.Player;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * The rights of a player determines their authority. Every right can be viewed
 * with a name and a value. The value is used to separate each right from one
 * another.
 * 
 * @author Jason
 * @author Patrick van Elderen | zondag 14 juli 2019 (CEST) : 13:25
 * @see <a href="https://github.com/Patrick9-10-1995">Github profile</a>
 *
 */
public enum PlayerRights {

    PLAYER("Player", -1, -1, 0),

    MODERATOR("Moderator", 494, 1, 3),

    ADMINISTRATOR("Administrator", 495, 2, 4),

    OWNER("Owner", 496, 3, 7),

    DEVELOPER("Developer", 497, 4, 6),

    BRONZE_YOUTUBER("Bronze Youtuber", 1087, 8, 1),

    IRON_MAN("Iron Man", 502, 9, 0),

    ULTIMATE_IRON_MAN("Ultimate Iron Man", 503, 10, 0),

    HARDCORE_IRON_MAN("Hardcore Iron Man", 504, 11, 0),

    SUPPORT("Support Team", 505, 12, 2),

    SILVER_YOUTUBER("Silver Youtuber", 1088, 18, 1),

    GOLD_YOUTUBER("Gold Youtuber", 1089, 19, 1),

    ELITE_IRON_MAN("Elite Iron Man", 1769, 16, 0),

    GROUP_IRON_MAN("Group Iron Man", 1947, 17, 0),

    DARK_LORD("Dark Lord", 1838, 18, 0),

    COMMUNITY_MANAGER("Community Manager", 1871, 19, 12),//12 is cmok fine 12 is cm and manger is 5 ok? yes

    MANAGER("Manager", 1861, 20, 5),
    HARDCORE_GROUP_IRONMAN("Hardcore Group Ironman", 1948, 21, 0)

    ;

    private final String name;

    private final int spriteId;

    private final int right;

    /**
     * The value of the right. The higher the value, the more permissions the player has.
     */
    private final int rightValue;

    PlayerRights(String name, int spriteId, int right, int rightValue) {
        this.name = name;
        this.spriteId = spriteId;
        this.right = right;
        this.rightValue = rightValue;
    }

    @Override
    public String toString() {
        return "PlayerRights{" + "name='" + name + '\'' + '}';
    }

    /**
     * A {@link Set} of all {@link PlayerRights} elements that cannot be directly
     * modified.
     */
    private static final Set<PlayerRights> RIGHTS = Collections.unmodifiableSet(EnumSet.allOf(PlayerRights.class));

    /**
     * Returns a {@link PlayerRights} object for the value.
     *
     * @param value the right level
     * @return the rights object
     */
    public static PlayerRights get(int value) {
        return RIGHTS.stream().filter(element -> element.rightValue == value).findFirst().orElse(PLAYER);
    }

    public final String getName() {
        return name;
    }

    public final int getSpriteId() {
        return spriteId;
    }

    public final int getRight() {
        return right;
    }

    public final int getRightValue() {
        return rightValue;
    }

    public boolean isSupportOrGreater(Player player) {
        return getRightValue() >= SUPPORT.getRightValue() || isOwner(player);
    }

    public boolean isModeratorOrGreater(Player player) {
        return getRightValue() >= MODERATOR.getRightValue() || isOwner(player);
    }

    public boolean isEventManager(Player player) {
        return getRightValue() >= MANAGER.getRightValue() || isOwner(player);
    }

    public boolean isAdminOrGreater(Player player) {
        return getRightValue() >= ADMINISTRATOR.getRightValue() || isOwner(player);
    }
    public boolean isIronMan(Player player) {
        return getRightValue() == IRON_MAN.getRightValue();
    }
    public boolean isUltmateIronMan(Player player) {
        return getRightValue() == ULTIMATE_IRON_MAN.getRightValue();
    }
    /** Checks if the player is a developer or greater. */
    public boolean isDeveloperOrGreater(Player player) {
        return getRightValue() >= DEVELOPER.getRightValue() || isOwner(player);
    }
    public boolean isDeveloperOrGreaterNoUpdate() {
        return getRightValue() == DEVELOPER.getRightValue() || getRightValue() == OWNER.getRightValue();
    }
    public boolean isServerSupport() {
        return getRightValue() == SUPPORT.getRightValue();
    }

    public boolean isModerator() {
        return getRightValue() == MODERATOR.getRightValue();
    }

    public boolean isAdmin(Player player) {
        return getRightValue() == ADMINISTRATOR.getRightValue() || isOwner(player);
    }

    public boolean isOwner(Player player) {
        return getRightValue() == OWNER.getRightValue();
    }

    public String[] getOwners() {
        if(!GameServer.properties().production) {
            return new String[]{"Zach", "a pk mission"};
        } else {
            return new String[]{""}; //TODO OWNER ADD
        }
    }

    public final boolean greater(PlayerRights other) {
        return getRightValue() > other.getRightValue();
    }

    public final boolean less(PlayerRights other) {
        return getRightValue() < other.getRightValue();
    }

    public boolean isStaffMember(Player player) {
        return isServerSupport() || isModerator() || isAdminOrGreater(player);
    }

    public boolean isStaffMemberOrYoutuber(Player player) {
        return isServerSupport() || isModerator() || isAdminOrGreater(player)|| isYoutuber(player);
    }

    public boolean isYoutuber(Player player) {
        return player.getPlayerRights().equals(BRONZE_YOUTUBER) || player.getPlayerRights().equals(SILVER_YOUTUBER) || player.getPlayerRights().equals(GOLD_YOUTUBER);
    }

    /** Gets the crown display. */
    public static String getCrown(Player player) {
        return player.getPlayerRights().equals(PLAYER) ? "" : "<img=" + (player.getPlayerRights().getSpriteId()) + ">";
    }

    public boolean isCmAndManger(Player player) {
        return getRightValue() == MANAGER.getRightValue() || getRightValue() == COMMUNITY_MANAGER.getRightValue();
    }
    public boolean isCm(Player player) {
        return getRightValue() == COMMUNITY_MANAGER.getRightValue();
    }

    public static boolean is(Player player, PlayerRights rights) {
        return player.getPlayerRights().ordinal() >= rights.ordinal();
    }
}
