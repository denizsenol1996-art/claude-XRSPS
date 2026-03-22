package com.twisted.game.world.entity.mob.movement;

import com.twisted.game.content.mechanics.MultiwayCombat;
import com.twisted.game.task.Task;
import com.twisted.game.task.TaskManager;
import com.twisted.game.task.impl.MobFollowTask;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.mob.Direction;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.ground.GroundItem;
import com.twisted.game.world.items.ground.GroundItemHandler;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.region.RegionManager;
import com.twisted.game.world.route.ClipUtils;
import com.twisted.util.Debugs;
import com.twisted.util.ItemIdentifiers;
import com.twisted.util.chainedwork.Chain;
import org.apache.commons.lang.ArrayUtils;

import java.util.*;

import static com.twisted.game.world.entity.AttributeKey.MOVEMENT_PACKET_STEPS;
import static com.twisted.game.world.route.routes.DumbRoute.getStepDirection;
import static com.twisted.util.NpcIdentifiers.*;

/**
 * A queue of {@link Direction}s which a {@link Mob} will follow.
 *
 * @author Graham Edgecombe Edited by Gabbe
 */
public final class MovementQueue {

    /**
     * The maximum size of the queue. If any additional steps are added, they are
     * discarded.
     */
    private static final int MAXIMUM_SIZE = 100;

    /**
     * The mob whose walking queue this is.
     */
    private final Mob mob;

    /**
     * The {@link Task} which handles following.
     */
    private MobFollowTask followingTask;

    /**
     * The queue of directions.
     */
    public final Deque<Step> steps = new ArrayDeque<Step>();

    private boolean blockMovement = false;

    /**
     * Are we currently moving?
     */
    private boolean isMoving = false;

    /**
     * Are we running.
     */
    private boolean running;

    // runite
    public int followX = -1, followY = -1;

    // runite
    public int lastFollowX = -1, lastFollowY = -1;

    /**
     * Creates a walking queue for the specified mob.
     *
     * @param mob
     *            The mob.
     */
    public MovementQueue(Mob mob) {
        this.mob = mob;
    }

    /**
     * Checks if we can walk from one position to another.
     *
     * @param from
     * @param to
     * @param size
     * @return
     */
    public static boolean canWalk(Tile from, Tile to, int size) {
        return RegionManager.canMove(from, to, size, size);
    }
    /**
     * Steps away from a Gamemob
     *
     * @param mob
     *            The gamemob to step away from
     */
    public static void clippedStep(Mob mob, int size) {
        if (mob.getMovementQueue().canWalk(-size, 0))
            mob.getMovementQueue().walkTo(-size, 0);
        else if (mob.getMovementQueue().canWalk(size, 0))
            mob.getMovementQueue().walkTo(size, 0);
        else if (mob.getMovementQueue().canWalk(0, -size))
            mob.getMovementQueue().walkTo(0, -size);
        else if (mob.getMovementQueue().canWalk(0, size))
            mob.getMovementQueue().walkTo(0, size);
    }

    /**
     * Adds the first step to the queue, attempting to connect the server and client
     * position by looking at the previous queue.
     *
     * @param clientConnectionTile
     *            The first step.
     * @return {@code true} if the queues could be connected correctly,
     *         {@code false} if not.
     */
    public boolean addFirstStep(Tile clientConnectionTile) {
        clear();
        walkTo(clientConnectionTile);
        return true;
    }

    public void interpolate(Tile tile) {
        interpolate(tile.x, tile.y, StepType.REGULAR);
    }

    public void interpolate(int toX, int toY) {
        interpolate(toX, toY, StepType.REGULAR);
    }

    public int interpolate(Tile tile, StepType type) {
        return interpolate(tile.x, tile.y, type, Integer.MAX_VALUE);
    }

    public int interpolate(int tx, int tz, StepType type) {
        return interpolate(tx, tz, type, Integer.MAX_VALUE);
    }

    public void step(int x, int y, StepType type) {
        final Step last = getLast();
        final int deltaX = x - last.toPosition().x;
        final int deltaY = y - last.toPosition().y;
        final Direction direction = Direction.fromDeltas(deltaX, deltaY);
        steps.add(new Step(new Tile(x, y, mob.tile().level), direction, type));
    }

    public Step peekLast() {
        return steps.peekLast();
    }

    public Tile peekLastTile() {
        Step step = peekLast();
        if (step == null)
            return mob.tile();
        return step.toPosition();
    }

    public Step step(Direction dir, StepType type) {
        if (dir == null)
            return null;

        Tile last = peekLastTile();
        Step step = new Step(new Tile(last.x + dir.x(), last.y + dir.y()), Direction.NONE, type);
        steps.add(step);
        return step;
    }

    public Step step(Direction dir) {
        return step(dir, StepType.REGULAR);
    }

    public void step(int x, int y) {
        steps.add(new Step(new Tile(x, y, mob.tile().level), Direction.NONE, StepType.REGULAR));
    }

    public int interpolate(int toX, int toY, StepType stepType, int maxSteps) {

        int cx = steps.isEmpty() ? mob.tile().x : steps.getLast().tile.x;
        int cy = steps.isEmpty() ? mob.tile().y : steps.getLast().tile.y;

        int taken = 0;
        while (maxSteps-- > 0) {
            if (cx == toX && cy == toY) {
                break;
            }
            if (cx < toX)
                cx++;
            else if (cx > toX)
                cx--;
            if (cy < toY)
                cy++;
            else if (cy > toY)
                cy--;

            step(cx, cy, stepType);

            taken++;
        }

        return taken;
    }


    /**
     * Adds a step to walk to the queue. This method shouldn't be used for player walking.
     *
     * @param x
     *            X to walk to
     * @param y
     *            Y to walk to
     */
    public void walkTo(int x, int y) {
        // theres nothing on the map at x/y 0-128 just black regions, if you called this method
        // you probably want to translate(+x, +y) not walk to exact coordinates
        if (x > 128 || y > 128) {
            // walk to exact
            walkTo(new Tile(x, y, mob.getZ()));
        } else {
            // translate difs
            Tile tile = mob.tile().transform(x, y);
            walkTo(tile);
        }
    }

    /**
     * Adds a single step to the queue internally without counting gaps. This
     * method is unsafe if used incorrectly so it is private to protect the
     * queue.
     *
     * @param x
     *            The x coordinate of the step.
     * @param y
     *            The y coordinate of the step.
     */
    private void walkTo(int x, int y, int zLvl) {
        /*
         * Check if we are going to violate capacity restrictions.
         */
        if (steps.size() >= MAXIMUM_SIZE) {
            /*
             * If we are we'll just skip the point. The player won't get a
             * complete route by large routes are not probable and are more
             * likely sent by bots to crash servers.
             */
            return;
        }

        /*
         * We retrieve the previous point (this is to calculate the direction to
         * move in).
         */
        final Step last = getLast();

        /*
         * Now we work out the difference between these steps.
         */
        final int diffX = x - last.tile.getX();
        final int diffY = y - last.tile.getY();

        /*
         * And calculate the direction between them.
         */
        final Direction direction = Direction.fromDeltas(diffX, diffY);

        if (direction != Direction.NONE) {
            /*
             * We now have the information to add a point to the queue! We
             * create the actual point object and add it.
             */
            steps.add(new Step(new Tile(x, y, zLvl), direction, StepType.REGULAR));
            Debugs.MOB_STEPS.debug(mob, "stepadd "+direction+" to "+x+","+y);
        } else {
            Debugs.MOB_STEPS.debug(mob, "stepadd failed "+direction+" to "+x+","+y+" is "+last);
        }
    }

    /**
     * Adds a step to the queue.
     *
     * @param step
     *            The step to add.
     * @oaram flag
     */
    public void walkTo(Tile step) {
        if (!canMove()) {
            return;
        }

        /*
         * The RuneScape client will not send all the points in the queue. It
         * just sends places where the direction changes.
         *
         * For instance, walking from a route like this:
         *
         * <code> ***** * * ***** </code>
         *
         * Only the places marked with X will be sent:
         *
         * <code> X***X * * X***X </code>
         *
         * This code will 'fill in' these points and then add them to the queue.
         */

        /*
         * We need to know the previous point to fill in the path.
         */
        if (steps.size() == 0) {
            /*
             * There is no last point, reset the queue to add the player's
             * current location.
             */
            clear();
        }

        /*
         * We retrieve the previous point here.
         */
        final Step last = getLast();

        /*
         * We now work out the difference between the points.
         */
        final int x = step.getX();
        final int y = step.getY();
        int diffX = x - last.tile.getX();
        int diffY = y - last.tile.getY();

        /*
         * And calculate the number of steps there is between the points.
         */
        final int max = Math.max(Math.abs(diffX), Math.abs(diffY));

        //Building the string with String.format is very computationally expensive, only do it when clip debug enabled.
        if (Debugs.CLIP.enabled) {
            Debugs.CLIP.debug(mob, String.format("walkTo by %s,%s %s %s,%s", diffX, diffY, max, x, y));
        }

        for (int i = 0; i < max; i++) {
            /*
             * Keep lowering the differences until they reach 0 - when our route
             * will be complete.
             */
            if (diffX < 0) {
                diffX++;
            } else if (diffX > 0) {
                diffX--;
            }
            if (diffY < 0) {
                diffY++;
            } else if (diffY > 0) {
                diffY--;
            }
            /*
             * Add this next step to the queue.
             */
            walkTo(x - diffX, y - diffY, step.getLevel());
        }
    }

    public static final int[] lockedFromMovement = new int[]{GREAT_OLM_RIGHT_CLAW_7553, GREAT_OLM_LEFT_CLAW_7555, GREAT_OLM_7554, COMBAT_DUMMY, UNDEAD_COMBAT_DUMMY, AWAKENED_ALTAR, AWAKENED_ALTAR_7290, AWAKENED_ALTAR_7292, AWAKENED_ALTAR_7294};

    public boolean canMove() {
        return canMove(false);
    }

    public boolean canMove(boolean sendMessage) {
        CombatFactory.unfreezeWhenOutOfRange(mob);
        if (mob.isNeedsPlacement()) {
            return false;
        }

        if (mob.frozen()) {
            if (sendMessage) {
                mob.message("A magical force stops you from moving.");
            }
            return false;
        }

        if(mob.isNpc()) {
            Npc npc = mob.getAsNpc();
            if (ArrayUtils.contains(lockedFromMovement, npc.id()) || npc.completelyLockedFromMoving()) {
                return false;
            }
        }

        if (blockMovement) {
            //System.out.println("movement blocked in canMove()");
            if (sendMessage) {
                mob.message("You can't move right now.");
            }
            return false;
        }

        if(mob.stunned()) {
            if (sendMessage) {
                mob.message("You're stunned and cannot move.");
            }
            return false;
        }
        if (mob.dead())
            return false;
        return true;
    }

    public boolean canWalk(int deltaX, int deltaY) {
        if (!canMove()) {
            return false;
        }

        final Tile to = new Tile(mob.tile().getX() + deltaX,
            mob.tile().getY() + deltaY, mob.tile().getLevel());
        if (mob.tile().getLevel() == -1 && to.getLevel() == -1)
            return true;
        return canWalk(mob.tile(), to, mob.getSize());
    }

    public boolean canWalkNoLogicCheck(int deltaX, int deltaY) {
        // removed canMove on purpose. you can spear a frozen target and they'll move
        final Tile to = new Tile(mob.tile().getX() + deltaX,
            mob.tile().getY() + deltaY, mob.tile().getLevel());
        if (mob.tile().getLevel() == -1 && to.getLevel() == -1)
            return true;
        return canWalk(mob.tile(), to, mob.getSize());
    }

    public static boolean dumbReachable(int destX, int destY, Tile start) {
        int x = start.getX();
        int y = start.getY();
        int z = start.getZ();
        int size = 1;
        var maxSteps = 0;
        while (true) {
            com.twisted.game.world.route.Direction stepDir = getStepDirection(ClipUtils.REGULAR, x, y, z, size, destX, destY);
            if (stepDir == null || maxSteps++ > 50) break;
            x += stepDir.deltaX;
            y += stepDir.deltaY;
            if ((x == destX && y == destY))
                return true;
        }
        return false;
    }

    public void walk(Deque<Tile> path) {
        clear();
        for (Tile tile : path) {

            if (steps.size() >= 100) {
                break;
            }

            /*Step last = steps.peekLast();
            int deltaX = p.getX() - last.position.getX();
            int deltaY = p.getY() - last.position.getY();
            Direction dir = Direction.fromDeltas(deltaX, deltaY);
            if (dir.toInteger() > -1) {*/
            steps.add(new Step(tile, Direction.NONE, StepType.REGULAR));
            //}

            if (Debugs.WALK.enabled) {
                GroundItem marker = new GroundItem(new Item(ItemIdentifiers.VIAL_OF_WATER, 1), tile, null);
                GroundItemHandler.createGroundItem(marker);
                Chain.bound(this).runFn(10, () -> {
                    GroundItemHandler.sendRemoveGroundItem(marker);
                });
            }
        }
        Debugs.WALK.debug(mob, steps.size()+" steps are: "+Arrays.toString(steps.stream().map(e->e.tile.toString()+" @"+e.tile.distance(mob.tile())+",").toArray()),
            mob.getCombat().getTarget(), true);
    }

    public void stepAside() {
        clear();
        Step last = steps.peekLast();
        int deltaX = mob.getX() - last.tile.getX();
        int deltaY = mob.getY() - last.tile.getY();
        Direction stepDir = Direction.fromDeltas(deltaX, deltaY);
        walkTo(stepDir.opposite().x(), stepDir.opposite().y());
    }

    public boolean empty() {
        return steps.isEmpty();
    }

    /**
     * Gets the last point.
     *
     * @return The last point.
     */
    public Step getLast() {
        final Step last = steps.peekLast();
        if (last == null)
            return new Step(mob.tile(), Direction.NONE, StepType.REGULAR);
        return last;
    }

    public Tile lastStep() {
        Step last = steps.peekLast();
        if (last == null) {
            return mob.tile();
        }

        return getLast().toPosition();
    }

    /**
     * @return true if the mob is moving.
     */
    public boolean isMoving() {
        return isMoving; // !points.isEmpty();
    }

    /**
     * Processes the movement queue.
     *
     * Polls through the queue of steps and handles them.
     *
     */
    public void process() {
        runiteProcess();

        traditionalProcess();
    }

    private void traditionalProcess() {
        final Tile beforeWalk = mob.tile();
        /*
         * The points which we are walking to.
         */
        Step walkStep, runStep = null;

        walkStep = steps.poll();

        if (isRunning()) {
            runStep = steps.poll();
        }
        /*if (mob.isPlayer() && walkStep != null || runStep != null) {
            System.out.printf("%s %s %s%n", mob.getMobName(), walkStep, runStep);
        }*/

        boolean moved = false;

        if (walkStep != null) {
            walkStep.direction = Direction.getDirection(beforeWalk, walkStep.tile);

            if (walkStep.direction != Direction.NONE) {
                Tile next = walkStep.tile;
                if (validateStep(walkStep)) {
                    mob.setPreviousTile(mob.tile());
                    mob.setTile(next);
                    mob.setWalkingDirection(walkStep.direction);
                    if(mob.isNpc()) {
                        mob.getAsNpc().lastDirection(walkStep.direction.toInteger());
                    }
                    moved = true;
                } else {
                    clear();
                    return;
                }
            }
        }

        if (runStep != null) {
            runStep.direction = Direction.getDirection(mob.tile(), runStep.tile);
            if ((walkStep != null && walkStep.direction != Direction.NONE) || (runStep != null && runStep.direction != Direction.NONE)) {
                Debugs.CB_FOLO.debug(mob, "client step " + (walkStep == null ? "?" : walkStep.direction) + " " + (runStep == null ? "?" : runStep.direction));
            }
            if (runStep != null && runStep.direction != Direction.NONE) {
                Tile next = runStep.tile;
                if (validateStep(runStep)) {
                    mob.setPreviousTile(mob.tile());
                    mob.setTile(next);
                    mob.setRunningDirection(runStep.direction);
                    if(mob.isNpc()) {
                        mob.getAsNpc().lastDirection(runStep.direction.toInteger());
                    }
                    moved = true;
                } else {
                    clear();
                    return;
                }
            }
        }

        if (mob.isPlayer()) {
            Player player = (Player) mob;
            boolean running = walkStep != null && (walkStep.stepType == StepType.FORCED_RUN || player.getMovementQueue().isRunning()) && !player.getMovementQueue().empty() && walkStep.stepType != StepType.FORCED_WALK;
            if (!running) {
                player.runEnergy().update();
            } else {
                player.runEnergy().drainForMove();
            }
            MultiwayCombat.tileChanged(player);
            handleRegionChange();
        }

        isMoving = moved;
        if (isMoving) {
            mob.clearAttrib(MOVEMENT_PACKET_STEPS);
            Tile.occupy(mob);
        }
    }

    private void runiteProcess() {
        final Tile beforeWalk = mob.tile();
        mob.setPreviousTile(mob.tile());
        boolean runiteStep = step(mob);
        if (runiteStep) {
            steps.clear(); // wipe old pf, using this instead
            final Tile afterWalk = mob.tile();
            boolean forceRun = stepType == com.twisted.game.world.route.StepType.FORCE_RUN;
            boolean ran = (forceRun || (isRunning() && stepType != com.twisted.game.world.route.StepType.FORCE_WALK)) && step(mob);

            mob.setWalkingDirection(Direction.getDirection(beforeWalk, afterWalk));
            mob.setRunningDirection(Direction.getDirection(afterWalk, mob.tile()));
            if (mob.isPlayer()) {
                Player player = (Player) mob;
                if (!ran) {
                    player.runEnergy().update();
                } else {
                    player.runEnergy().drainForMove();
                }
                MultiwayCombat.tileChanged(player);
                handleRegionChange();
            }
            else if(mob.isNpc()) {
                mob.getAsNpc().lastDirection(mob.getRunningDirection().toInteger());
            }
            isMoving = true;
            int diffX = mob.tile().getX() - mob.getPreviousTile().getX();
            int diffY = mob.tile().getY() - mob.getPreviousTile().getY();
            lastFollowX = followX;
            lastFollowY = followY;
            followX = mob.getPreviousTile().getX();
            followY = mob.getPreviousTile().getY();
            if(diffX >= 2)
                followX++;
            else if(diffX <= -2)
                followX--;
            if(diffY >= 2)
                followY++;
            else if(diffY <= -2)
                followY--;
            Tile.occupy(mob);
            if (isMoving) {
                mob.clearAttrib(MOVEMENT_PACKET_STEPS);
            }
        }
    }

    public boolean movementPacketThisCycle() {
        return mob.isPlayer() && Optional.ofNullable(mob.<Deque<Tile>>getAttribOr(AttributeKey.MOVEMENT_PACKET_STEPS, null)).map(v -> v != null ? v.peekLast() : null).orElse(null) != null;
    }

    private boolean validateStep(Step next) {
        if (followingTask != null && next.equals(followingTask.getFollow().tile())) {
            return false;
        }
        // Make sure movement isn't restricted..
        if (next.stepType == StepType.REGULAR && (!canMove(movementPacketThisCycle()) || !RegionManager.canMove(mob.tile(), next.toPosition(), mob.getSize(), mob.getSize()))) {
            return false;
        }

        return true;
    }

    public void handleRegionChange() {
        if(mob.isPlayer()) {
            Player player = ((Player) mob);
            final int diffX = mob.tile().getX() - mob.getLastKnownRegion().getRegionX() * 8;
            final int diffY = mob.tile().getY() - mob.getLastKnownRegion().getRegionY() * 8;
            boolean regionChanged = false;
            if (diffX < 16)
                regionChanged = true;
            else if (diffX >= 88)
                regionChanged = true;
            if (diffY < 16)
                regionChanged = true;
            else if (diffY >= 88)
                regionChanged = true;
            if (regionChanged || player.getRegionHeight() != player.tile().getLevel()) {
                player.getPacketSender().sendMapRegion();
                player.setRegionHeight(player.tile().getLevel());
                player.setActiveMap(new Tile(player.tile().x, player.tile().y, player.tile().level));
            }
        }
    }

    /**
     * Stops the movement.
     */
    public MovementQueue clear() {
        steps.clear();
        isMoving = false;
        mob.clearAttrib(MOVEMENT_PACKET_STEPS);
        //no need to add step at current location (graham pathing)
        Debugs.WALK.debug(mob, "walk reset");
        reset(); // runite reset
        return this;
    }

    /**
     * Starts a new {@link MobFollowTask} which starts
     * following the given {@link Mob}.
     * @param follow
     */
    public void follow(Mob follow) {
        if (follow == null) {
            //System.out.println("follow is null, reset.");
            resetFollowing();
            return;
        }

        if (followingTask == null || !followingTask.isRunning()) {
            TaskManager.submit((followingTask = new MobFollowTask(mob, follow)));
            //System.out.println("Start follow task.");
        } else {
            followingTask.setFollowing(follow);
        }
    }

    /**
     * Checks if we're currently following the given {@link Mob}.
     * @param mob
     * @return
     */
    public boolean isFollowing(Mob mob) {
        if (followingTask != null) {
            return followingTask.getFollow().equals(mob);
        }
        return false;
    }

    /**
     * Stops any following which might be active.
     */
    public void resetFollowing() {
        if (followingTask != null) {
            followingTask.stop();
        }
        followingTask = null;
    }

    /**
     * Gets the size of the queue.
     *
     * @return The size of the queue.
     */
    public int size() {
        return steps.size();
    }

    public void setRunning(boolean run) {
        running = run;
    }

    public boolean isRunning() {
        if(mob.isPlayer()) {
            return ((Player) mob).getAttribOr(AttributeKey.IS_RUNNING, false);
        }
        return running;
    }

    public MovementQueue setBlockMovement(boolean blockMovement) {
        if (mob.isNpc() && mob.getAsNpc().permaBlockedMovement()) {
            return this;
        }
        this.blockMovement = blockMovement;
        return this;
    }

    public boolean isMovementBlocked() {
        return blockMovement;
    }

    public boolean hasMoved() {
        return mob.getWalkingDirection() != Direction.NONE || mob.getRunningDirection() != Direction.NONE || mob.isNeedsPlacement();
    }

    /**
     * Represents a single step in the queue.
     *
     * @author Graham Edgecombe
     */
    public static final class Step {

        private final Tile tile;
        public Direction direction;
        private final  StepType stepType;

        public Step(Tile tile, Direction direction, StepType stepType) {
            this.tile = tile;
            this.direction = direction;
            this.stepType = stepType;
        }

        public Tile toPosition() {
            return tile;
        }

        public StepType getStepType() {
            return stepType;
        }

        @Override
        public String toString() {
            return Step.class.getName() + " [direction=" + direction + ", position=" + tile + ", stepType=" + stepType + "]";
        }

    }

    public enum StepType {
        REGULAR, FORCED_WALK, FORCED_RUN
    }

    public int readOffset, writeOffset;

    public boolean isAtDestination() {
        return readOffset >= writeOffset;
    }

    public com.twisted.game.world.route.StepType stepType = com.twisted.game.world.route.StepType.NORMAL;

    public void reset() {
        readOffset = 0;
        writeOffset = 0;
        stepType = com.twisted.game.world.route.StepType.NORMAL;
    }

    protected int[] stepsX, stepsY;

    protected boolean step(Mob entity) {
        if(isAtDestination())
            return false;

        int stepX = stepsX[readOffset];
        int stepY = stepsY[readOffset];

        Tile position = entity.tile();
        int absX = position.getX();
        int absY = position.getY();
        int diffX = stepX - absX;
        int diffY = stepY - absY;

        if(diffX < 0)
            diffX = -1;
        else if(diffX > 0)
            diffX = 1;

        if(diffY < 0)
            diffY = -1;
        else if(diffY > 0)
            diffY = 1;

        int newX = absX + diffX;
        int newY = absY + diffY;

        if(!entity.getRouteFinder().allowStep(newX, newY))
            return false;

        entity.setTile(new Tile(newX, newY, entity.getZ()));
        if (entity.isPlayer())
            handleRegionChange();
        if(newX == stepX && newY == stepY)
            readOffset++;
        return true;
    }

    public int[] getStepsX() {
        if(stepsX == null)
            stepsX = new int[50];
        return stepsX;
    }

    public int[] getStepsY() {
        if(stepsY == null)
            stepsY = new int[50];
        return stepsY;
    }

    public void outOfReach() {
        //player.privateSound(154); // TODO
        mob.message("I can't reach that!");
    }

    public boolean forcedStep() {
        return stepType == com.twisted.game.world.route.StepType.FORCE_WALK ||
            stepType == com.twisted.game.world.route.StepType.FORCE_RUN;
    }
}
