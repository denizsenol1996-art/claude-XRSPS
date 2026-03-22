package com.twisted.game.world.entity.masks;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Tile;

/**
 * A graphic propelled through the air by some sort of spell, weapon, or other
 * miscellaneous force.
 *
 * @author lare96
 */
public final class Projectile {

    /**
     * The starting position of the projectile.
     */
    private final Tile start;

    /**
     * The offset position of the projectile.
     */
    private final Tile offset;

    /**
     * The speed of the projectile.
     */
    private final int speed;

    /**
     * The id of the projectile.
     */
    private final int projectileId;

    /**
     * The starting height of the projectile.
     */
    private final int startHeight;

    /**
     * The ending height of the projectile.
     */
    private final int endHeight;

    /**
     * The lock on value of the projectile.
     */
    private final int lockon;

    /**
     * The delay of the projectile.
     */
    private final int delay;

    /**
     * The curve angle of the projectile.
     */
    private final int angle;

    /**
     * The slope of the projectile.
     */
    private final int slope;

    /**
     * The radius that the projectile is launched from.
     */
    private final int radius;

    /**
     * Create a new {@link Projectile}.
     *
     * @param start        the starting position of the projectile.
     * @param end          the ending position of the projectile.
     * @param projectileId the id of the projectile.
     * @param lockon       the lock on value of the projectile.
     * @param speed        the speed of the projectile.
     * @param delay        the delay of the projectile.
     * @param startHeight  the starting height of the projectile.
     * @param endHeight    the ending height of the projectile.
     * @param angle        the curve angle of the projectile.
     * @param slope        the slope of the projectile.
     * @param radius       The radius that the projectile is launched from.
     */
    public Projectile(Tile start, Tile end, int projectileId, int lockon, int speed, int delay, int startHeight, int endHeight, int angle, int slope, int radius) {
        this.start = start;
        int offX = (start.getY() - end.getY()) * -1;
        int offY = (start.getX() - end.getX()) * -1;
        this.offset = new Tile(offX, offY, start.getZ());
        this.lockon = lockon;
        this.projectileId = projectileId;
        this.delay = delay;
        this.speed = speed;
        this.startHeight = startHeight;
        this.endHeight = endHeight;
        this.angle = angle;
        this.slope = slope;
        this.radius = radius;
    }

    /**
     * Grabbing Client speed
     *
     * @return
     */

    public int getClientTicks() {
        return speed;
    }

    public Projectile(Tile start, Tile end, int lockon, int projectileId, int speed, int delay, int startHeight, int endHeight, int angle) {
        this.start = start;
        int offX = (start.getY() - end.getY()) * -1;
        int offY = (start.getX() - end.getX()) * -1;
        this.offset = new Tile(offX, offY, start.getZ());
        this.lockon = lockon;
        this.projectileId = projectileId;
        this.delay = delay;
        this.speed = speed;
        this.startHeight = startHeight;
        this.endHeight = endHeight;
        this.angle = angle;
        this.slope = 16;
        this.radius = 64;
    }

    /**
     * Create a new {@link Projectile}.
     *
     * @param source       the entity that is firing this projectile.
     * @param victim       the victim that this projectile is being fired at.
     * @param projectileId the id of the projectile.
     * @param speed        the speed of the projectile. ANYTHING UNDER 40 MIGHT BE TOO FAST TO SEE ON SCREEN
     * @param delay        the delay of the projectile.
     * @param startHeight  the starting height of the projectile.
     * @param endHeight    the ending height of the projectile.
     * @param angle        the curve angle of the projectile.
     * @param slope        the slope of the projectile.
     * @param radius       The radius that the projectile is launched from.
     */
    public Projectile(Mob source, Mob victim, int projectileId, int speed, int delay, int startHeight, int endHeight, int angle, int slope, int radius) {
        // interesting thing about projectile packet, if we're using the client's PLAYER index array, the id is short.MAX_VAL + id (32k + 2048 max players)
        // otherwise for npcs its just id (range 0-short.max)
        this(source.getCentrePosition(), victim.getCentrePosition(), projectileId, victim.getProjectileLockonIndex(), speed, delay, startHeight, endHeight, angle, slope, radius);
    }

    /**
     *
     * @param source
     * @param victim
     * @param projectileId
     * @param delay
     * @param speed  ANYTHING UNDER 40 MIGHT BE TOO FAST TO SEE ON SCREEN
     * @param startHeight
     * @param endHeight
     * @param angle
     */
    public Projectile(Mob source, Mob victim, int projectileId, int delay, int speed, int startHeight, int endHeight, int angle) {
        this(source.getCentrePosition(), victim.getCentrePosition(), projectileId, victim.getProjectileLockonIndex(), speed, delay, startHeight, endHeight, 0, 16, 64);
    }

    /**
     * Sends one projectiles using the values set when the {@link Projectile}
     * was constructed.
     */

    public static final float TICK = 600F;

    public static final float CLIENT_CYCLE = 20F;

    public static final float CYCLES_PER_TICK = TICK / CLIENT_CYCLE;

    /**
     * @return GAME TICKS UNTIL PROJECTILE REACHES END TILE
     */
   /* public int getTime(final Tile from, final Tile to) {
        float duration = getProjectileDuration(from, to) / CYCLES_PER_TICK;
        if (duration - (int) duration > 0.5F) {
            duration++;
        }
        return Math.max(0, (int) duration - 1);
    }*/

    public int getTime(final Tile from, final Tile to) {
        float duration = getProjectileDuration(from, to) / CYCLES_PER_TICK;
        return (int) Math.max(0, duration - 1);
    }

    public int getTime(final int distance) {
        final int flightDuration = distance * 1;
        final int length = delay + speed + flightDuration;
        return length / 30;
    }

    public int getProjectileDuration(final Tile from, final Tile to) {
        var flightDuration = Math.max(Math.abs(from.getX() - to.getX()), Math.abs(from.getY() - to.getY()));
        return delay + speed + flightDuration;
    }

    public void sendProjectile() {
        for (Player player : World.getWorld().getPlayers()) {
            if (player == null) continue;
            if (start.getZ() != player.getZ()) continue;
            if (start.isViewableFrom(player.tile())) {
                player.getPacketSender().sendProjectile(start.x, start.y, offset.x, offset.y, angle, speed, projectileId, startHeight, endHeight, lockon, delay, slope, radius);
            }
        }
    }

    public void sendFor(Player player) {
        if (start.isViewableFrom(player.tile())) {
            player.getPacketSender().sendProjectile(start.x, start.y, offset.x, offset.y, angle, speed, projectileId, startHeight, endHeight, lockon, delay, slope, radius);
        }
    }

    public int clientDelay() {
        return delay + speed;
    }

    /**
     * Gets the starting position of the projectile.
     *
     * @return the starting position of the projectile.
     */
    public Tile getStart() {
        return start;
    }

    /**
     * Gets the offset position of the projectile.
     *
     * @return the offset position of the projectile.
     */
    public Tile getOffset() {
        return offset;
    }

    /**
     * Gets the speed of the projectile.
     *
     * @return the speed of the projectile.  ANYTHING UNDER 40 MIGHT BE TOO FAST TO SEE ON SCREEN
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Gets the id of the projectile.
     *
     * @return the id of the projectile.
     */
    public int getProjectileId() {
        return projectileId;
    }

    /**
     * Gets the starting height of the projectile.
     *
     * @return the starting height of the projectile.
     */
    public int getStartHeight() {
        return startHeight;
    }

    /**
     * Gets the ending height of the projectile.
     *
     * @return the ending height of the projectile
     */
    public int getEndHeight() {
        return endHeight;
    }

    /**
     * Gets the lock on value of the projectile.
     *
     * @return the lock on value of the projectile.
     */
    public int getLockon() {
        return lockon;
    }

    /**
     * Gets the delay of the projectile.
     *
     * @return the delay of the projectile.
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Gets the curve angle of the projectile.
     *
     * @return the curve angle of the projectile.
     */
    public int getAngle() {
        return angle;
    }

    public int getSlope() {
        return slope;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "Projectile{" +
            "start=" + start +
            ", offset=" + offset +
            ", speed=" + speed +
            ", projectileId=" + projectileId +
            ", startHeight=" + startHeight +
            ", endHeight=" + endHeight +
            ", lockon=" + lockon +
            ", delay=" + delay +
            ", angle=" + angle +
            ", slope=" + slope +
            ", radius=" + radius +
            '}';
    }
}
