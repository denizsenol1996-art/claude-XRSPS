package com.twisted.game.world.entity;

import com.twisted.game.task.TaskManager;
import com.twisted.game.task.impl.TickAndStop;
import com.twisted.game.task.impl.TickableTask;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Tile;
import com.twisted.util.CustomNpcIdentifiers;

import java.util.function.Consumer;

public abstract class Entity {

    /**
     * runite, if entity's process() has been called. used to determine which entity e1 or e2
     * <br> was handled first in 1 cycle
     */
    public boolean processed;

    public Entity() {}

    /**
     * The Entity constructor.
     * @param tile    The position the entity is currently in.
     */
    public Entity(NodeType type, Tile tile) {
        this.tile = tile;
        this.type = type;
        this.lastKnownRegion = tile;
    }

    /**
     * The entity's type.
     */
    private NodeType type;

    /**
     * The entity's unique index.
     */
    private int index;

    /**
     * The entity's tile size.
     */
    private int size = 1;

    protected Tile tile;

    /**
     * The entity's first position in current map region.
     */
    private Tile lastKnownRegion;

    /**
     * Gets this type.
     * @return
     */
    public NodeType getNodeType() {
        return type;
    }

    public boolean finished() {
        return index < 1;
    }

    /**
     * Gets the entity's unique index.
     * @return    The entity's index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the entity's index.
     * @param index        The value the entity's index will contain.
     * @return            The Entity instance.
     */
    public Entity setIndex(int index) {
        this.index = index;
        return this;
    }

    /**
     * Gets this entity's first position upon entering their
     * current map region.
     * @return    The lastKnownRegion instance.
     */
    public Tile getLastKnownRegion() {
        return lastKnownRegion;
    }

    /**
     * Sets the entity's current region's position.
     * @param lastKnownRegion    The position in which the player first entered the current region.
     * @return                    The Entity instance.
     */
    public Entity setLastKnownRegion(Tile lastKnownRegion) {
        this.lastKnownRegion = lastKnownRegion;
        return this;
    }

    public boolean isAt(Tile pos) {
        return isAt(pos.getX(), pos.getY());
    }

    public boolean isAt(int x, int y) {
        return tile.getX() == x && tile.getY() == y;
    }

    public int getX() {
        return tile().getX();
    }

    public int getY() {
        return tile().getY();
    }

    public int getZ() {
        return tile().getLevel();
    }

    /**
     * Sets the entity position
     * @param tile the world position
     */
    public Entity setTile(Tile tile) {
        this.tile = tile;
        return this;
    }

    /**
     * Gets the entity position.
     * @return the entity's world position
     */
    public Tile tile() {
        return tile;
    }

    /**
     * gets the entity's tile size.
     * @return    The size the entity occupies in the world.
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the entity's tile size
     * @return    The Entity instance.
     */
    public Entity setSize(int size) {
        this.size = size;
        return this;
    }

    public boolean isNpc() {
        return this instanceof Npc;
    }

    public boolean isPlayer() {
        return this instanceof Player;
    }

    public boolean isGameObject() {
        return this instanceof GameObject;
    }

    public Player getAsPlayer() {
        return ((Player)this);
    }

    public Npc getAsNpc() {
        return ((Npc)this);
    }

    /**
     * a task that runs every 1 game tick. Aka repeatingTask
     * <br> IMPORTANT: not a chain, this is uninterruptable, unless stop is hardcoded
     */
    public TickableTask repeatingTask(Consumer<TickableTask> r) {
        TickableTask task = new TickableTask(true, 1) {
            @Override
            protected void tick() {
                r.accept(this);
            }
        };
        task.bind(this);
        TaskManager.submit(task);
        return task;
    }

    /**
     * a task that runs Once after {@code delay} ticks.
     */
    public TickAndStop runOnceTask(int delay, Consumer<TickAndStop> r) {
        TickAndStop task = new TickAndStop(delay) {
            @Override
            public void executeAndStop() {
                r.accept(this);
            }
        };
        task.bind(this);
        TaskManager.submit(task);
        return task;
    }

    public boolean isEventNpc() {
        return this instanceof Npc npc && npc.id() == CustomNpcIdentifiers.SUMMER_IMP;
    }

}
