package com.twisted.game.world.entity;

import com.twisted.fs.AnimationDefinition;
import com.twisted.fs.DefinitionRepository;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.items.container.equipment.EquipmentBonuses;
import com.twisted.game.world.region.Region;
import com.twisted.game.world.region.RegionManager;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.twisted.GameServer;
import com.twisted.game.TimesCycle;
import com.twisted.game.action.ActionManager;
import com.twisted.game.content.EffectTimer;
import com.twisted.game.content.instance.InstancedAreaManager;
import com.twisted.game.content.mechanics.Poison;
import com.twisted.game.content.mechanics.Transmogrify;
import com.twisted.game.content.sound.CombatSounds;
import com.twisted.game.task.Task;
import com.twisted.game.world.entity.combat.Combat;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.Venom;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.hit.Splat;
import com.twisted.game.world.entity.combat.hit.SplatType;
import com.twisted.game.world.entity.masks.animations.Animation;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.Direction;
import com.twisted.game.world.entity.mob.Flag;
import com.twisted.game.world.entity.mob.UpdateFlag;
import com.twisted.game.world.entity.mob.movement.MovementQueue;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.InfectionType;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.container.equipment.Equipment;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Boundary;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.Controller;
import com.twisted.game.world.route.RouteFinder;
import com.twisted.game.world.route.StepType;
import com.twisted.game.world.route.routes.TargetRoute;
import com.twisted.util.Debugs;
import com.twisted.util.chainedwork.Chain;
import com.twisted.util.timers.TimerKey;
import com.twisted.util.timers.TimerRepository;
import lombok.Getter;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.twisted.game.world.entity.AttributeKey.VENOMED_BY;

/**
 * A player or NPC
 *
 * @author Swiffy
 */
public abstract class Mob extends Entity {

    private static final Logger logger = LogManager.getLogger(Mob.class);
    public int pidOrderIndex;

    public Mob() {

    }

    public Mob(NodeType type, Tile tile) {
        super(type, tile);
    }

    /**
     * Gets the width of the entity.
     *
     * @return The width of the entity.
     */
    public abstract int xLength();

    /**
     * Gets the width of the entity.
     *
     * @return The width of the entity.
     */
    public abstract int yLength();

    /**
     * Gets the centre position of the entity.
     *
     * @return The centre Position of the entity.
     */
    public abstract Tile getCentrePosition();

    /**
     * Gets the projectile lockon index of this mob.
     *
     * @return The projectile lockon index of this mob.
     */
    public abstract int getProjectileLockonIndex();

    public int projectileSpeed(Mob target) {
        int clientSpeed;
        if (this.tile().isWithinDistance(this, target, 1)) {
            clientSpeed = 56;
        } else if (this.tile().isWithinDistance(this, target, 5)) {
            clientSpeed = 61;
        } else if (this.tile().isWithinDistance(this, target, 8)) {
            clientSpeed = 71;
        } else {
            clientSpeed = 81;
        }
        return clientSpeed;
    }

    public int getProjectileHitDelay(Mob target) {
        int gfxDelay;
        if (this.tile().isWithinDistance(this, target, 1)) {
            gfxDelay = 80;
        } else if (this.tile().isWithinDistance(this, target, 5)) {
            gfxDelay = 100;
        } else if (this.tile().isWithinDistance(this, target, 8)) {
            gfxDelay = 120;
        } else {
            gfxDelay = 140;
        }

        return (gfxDelay / 20) - 2;
    }

    public Npc[] closeNpcs(int span) {
        return closeNpcs(254, span);
    }

    public Npc[] closeNpcs(int maxCapacity, int span) {
        Npc[] entities = new Npc[maxCapacity];
        int count = 0;
        for (var npc : World.getWorld().getNpcs()) {
            if (npc == null || npc == this || npc.tile() == null || npc.tile().level != tile().level || npc.finished())
                continue;
            if (tile().distance(npc.tile()) > span) continue;
            if (npc.tile().inSqRadius(tile, span)) entities[count++] = npc;
            if (count >= entities.length) break;
        }
        Npc[] set = new Npc[count];
        System.arraycopy(entities, 0, set, 0, count);
        return set;
    }

    public Player[] closePlayers(int span) {
        return closePlayers(254, span);
    }

    public Player[] closePlayers(int maxCapacity, int span) {
        Player[] entities = new Player[maxCapacity];
        int count = 0;
        for (var p : World.getWorld().getPlayers()) {
            if (p == null || p == this || p.tile() == null || p.tile().level != tile().level || p.looks().hidden() || p.finished())
                continue;
            if (tile().distance(p.tile()) > span) continue;
            if (p.tile().inSqRadius(tile, span)) entities[count++] = p;
            if (count >= entities.length) break;
        }
        Player[] set = new Player[count];
        System.arraycopy(entities, 0, set, 0, count);
        return set;
    }

    @Getter
    public boolean teleportJump;

    /**
     * An abstract method used for handling actions
     * once this entity has been added to the world.
     */
    public abstract void onAdd();

    /**
     * An abstract method used for handling actions
     * once this entity has been removed from the world.
     */
    public abstract void onRemove();

    public abstract Hit manipulateHit(Hit hit);

    public void teleblock(int time) {
        this.teleblock(time, false);
    }

    /**
     * The effects of the teleblock are lifted when the affected player logs out, leaves the Wilderness by any means (jumping over the wilderness ditch, dying, etc.)
     * or when the teleblock timer expires. In addition, it is lifted if players kill the opponent that cast the spell on them, though this does not give a
     * temporary immunity to another Tele Block.
     *
     * @param time The tele block time in ticks
     */
    public void teleblock(int time, boolean triggerOnLogin) {
        if (triggerOnLogin) {
            timerRepository.extendOrRegister(TimerKey.SPECIAL_TELEBLOCK, time);

            String end = "approximately 2 minutes";
            if (time > 400) {
                end = "5 minutes";
            }

            if (isPlayer()) {
                Player player = (Player) this;
                player.getPacketSender().sendEffectTimer((int) (time * 0.6), EffectTimer.TELEBLOCK);
                player.message("<col=804080>A teleblock spell has been cast on you. It will expire in " + end + ".");
            }
            return;
        }

        if (timerRepository.has(TimerKey.SPECIAL_TELEBLOCK)) {
            return;
        }

        if (timerRepository.has(TimerKey.TELEBLOCK)) {
            return;
        }

        if (timerRepository.has(TimerKey.TELEBLOCK_IMMUNITY)) {
            return;
        }

        if (!timerRepository.has(TimerKey.TELEBLOCK)) {

            if (tile.region() == 6992 || tile.region() == 6993) {
                time = 100;
            }

            timerRepository.extendOrRegister(TimerKey.TELEBLOCK, time);
            timerRepository.extendOrRegister(TimerKey.TELEBLOCK_IMMUNITY, time + 55);
            String end = "approximately 2 minutes";
            if (time > 400) {
                end = "5 minutes";
            }

            if (isPlayer()) {
                Player player = (Player) this;
                player.graphic(345);
                player.getPacketSender().sendEffectTimer((int) (time * 0.6), EffectTimer.TELEBLOCK);
                player.message("<col=804080>A teleblock spell has been cast on you. It will expire in " + end + ".");
            }
        }
    }

    public void stun(int time) {
        stun(time, true);
    }

    public void stun(int time, boolean message) {
        stun(time, message, true, false);
    }

    public void stun(int time, boolean message, boolean gfx, boolean npcStun) {
        if (timerRepository.has(TimerKey.STUN_IMMUNITY)) {
            return;
        }

        //Stun the player.
        timerRepository.extendOrRegister(TimerKey.STUNNED, time);

        //In addition to this, players are given a (3.0 seconds) period of immunity after a stun wears off in which they cannot be stunned again.
        timerRepository.extendOrRegister(TimerKey.STUN_IMMUNITY, time + 5);

        if (message) {
            message("You have been stunned!");
        }

        if (gfx) {
            graphic(245, 124, 0);
        }

        stopActions(false);

        //Despite this, it is popular in player killing as the brief stun causes all incoming damage to be ignored until it dissipates, after which all damage taken is applied at once.
        if (!npcStun) {
            lockDelayDamage();
        }

        if (isPlayer()) {
            clearAttrib(AttributeKey.TARGET); // Does actually stop us interacting
        }

        Chain.bound(this).runFn(time, this::unlock);
    }

    public boolean frozen() {
        return timerRepository.has(TimerKey.FROZEN);
    }

    public boolean stunned() {
        return timerRepository.has(TimerKey.STUNNED);
    }

    public void freeze(int time, @NonNull Mob target) {
        if (target instanceof Player player) {
            if (player.getTimerRepository().has(TimerKey.FREEZE_IMMUNITY)) {
                return;
            }
            if (player.getTimerRepository().has(TimerKey.FROZEN)) {
                return;
            }
            player.stopActions(true);
            player.putAttrib(AttributeKey.FROZEN_BY, this);
            player.getPacketSender().sendEffectTimer((int) Math.round(time * 0.6), EffectTimer.FREEZE);
            player.getTimerRepository().extendOrRegister(TimerKey.FROZEN, time);
            player.getTimerRepository().extendOrRegister(TimerKey.FREEZE_IMMUNITY, time + 5);
            player.getPacketSender().sendMessage("<col=ef1020>You have been frozen!</col>");
        } else if (target instanceof Npc npc) {
            if (npc.getTimerRepository().has(TimerKey.FROZEN)) {
                return;
            }

            npc.stopActions(true);
            npc.putAttrib(AttributeKey.FROZEN_BY, this);
            npc.getTimerRepository().extendOrRegister(TimerKey.FROZEN, time);
        }
    }

    public void stopActions(boolean cancelMoving) {
        if (locked()) {
            return;
        }

        this.setEntityInteraction(null);
        this.getMovementQueue().resetFollowing();
        resetFaceTile();
        // Graphics and animations are not reset when you walk.
        if (cancelMoving)
            movementQueue.clear();
        action.clearNonWalkableActions();
        interruptChains();
        TargetRoute.reset(this);
    }

    public void interruptChains() {
        chains.forEach(c -> { // interrupt chains
            c.nextNode = null;
            c.interrupted = true;
            if (c.task != null)
                c.task.stop();
        });
        chains.removeIf(c -> c.interrupted || (c.task != null && c.task.isStopped()));
    }

    public final ArrayList<Chain<?>> chains = new ArrayList<>();

    public String getMobName() {
        if (isNpc()) {
            return (getAsNpc().def().name != null ? getAsNpc().def().name : "Unknown");
        } else {
            return getAsPlayer().getUsername();
        }
    }

    private boolean[] prayerActive = new boolean[53], curseActive = new boolean[20];
    @Getter
    EquipmentBonuses bonuses = new EquipmentBonuses();

    public boolean[] getCurseActive() {
        return curseActive;
    }

    public Mob setCurseActive(boolean[] curseActive) {
        this.curseActive = curseActive;
        return this;
    }

    public Mob setCurseActive(int id, boolean curseActive) {
        this.curseActive[id] = curseActive;
        return this;
    }

    /**
     * Teleports the mob to a target location
     */
    public void teleport(Tile teleportTarget) {
        if (isPlayer() && !getAsPlayer().getInterfaceManager().isClear()) {
            getAsPlayer().getInterfaceManager().close(false);
        }

        if (this.isPlayer() && Transmogrify.isTransmogrified((Player) this) && !this.getAsPlayer().inActiveTournament() && !this.getAsPlayer().isInTournamentLobby()) {
            Transmogrify.hardReset((Player) this);
        }

        if (this.isPlayer()) {
            var instancedArea = InstancedAreaManager.getSingleton().ofZ(getAsPlayer().getZ());
            if (instancedArea != null)
                instancedArea.onTeleport(getAsPlayer(), teleportTarget);
        }

        if (this instanceof Player player) {
            var instancedArea = InstancedAreaManager.getSingleton().ofZ(getAsPlayer().getZ());
            if (instancedArea != null && player.hasAttrib(AttributeKey.RECALL_ATTUNE_ACTIVE) && !player.isUsingLastRecall()) {
                player.setLastSavedTile(this.tile.copy());
            }

            if (instancedArea != null) {
                player.checkDisband();
            }
        }

        setTile(teleportTarget);
        Tile.occupy(this);
        setPreviousTile(teleportTarget);
        setNeedsPlacement(true);
        setResetMovementQueue(true);
        setEntityInteraction(null);
        if (isPlayer()) {
            getMovementQueue().handleRegionChange();
        }
        getMovementQueue().clear();

        getMovementQueue().lastFollowX = teleportTarget.x;
        getMovementQueue().lastFollowY = teleportTarget.y;
    }

    public MovementQueue getMovementQueue() {
        return movementQueue;
    }

    public void teleport(int x, int y) {
        teleport(new Tile(x, y));
    }

    public void teleport(int x, int y, int z) {
        teleport(new Tile(x, y, z));
    }

    /**
     * Resets all flags related to updating.
     */
    public void resetUpdating() {
        getUpdateFlag().reset();
        walkingDirection = Direction.NONE;
        runningDirection = Direction.NONE;
        needsPlacement = false;
        resetMovementQueue = false;
        forcedChat = null;
        interactingEntity = null;
        faceTile = null;
        animation = null;
        graphic = null;
        splats.clear();
    }

    public Mob forceChat(String message) {
        setForcedChat(message);
        getUpdateFlag().flag(Flag.FORCED_CHAT);
        return this;
    }

    public Region[] surrounding;

    /**
     * @author Shadowrs
     */
    public Region[] getSurroundingRegions() {
        final var region = tile.region();
        if (surrounding != null && surrounding[0] != null && surrounding[0].getRegionId() == region)
            return surrounding;
        surrounding = new Region[9];
        surrounding[0] = RegionManager.getRegion(region);
        surrounding[1] = RegionManager.getRegion(region - 1);
        surrounding[2] = RegionManager.getRegion(region + 1);
        surrounding[3] = RegionManager.getRegion(region - 256);
        surrounding[4] = RegionManager.getRegion(region + 256);
        surrounding[5] = RegionManager.getRegion(region + 257);
        surrounding[6] = RegionManager.getRegion(region - 257);
        surrounding[7] = RegionManager.getRegion(region - 255);
        surrounding[8] = RegionManager.getRegion(region + 255);
        return surrounding;
    }

    public int executeProjectile(Projectile projectile) {
        if (projectile == null)
            return 0;

        Tile source = projectile.getStart();
        Tile target = projectile.getOffset();

        if (target == null)
            return 0;

        int creatorSize = getSize();

        Tile distance = source.getDistanceTo(target);

        if (distance.getX() <= 64 && distance.getY() <= 64) {
            for (final Player p : this.closePlayers(14)) {
                if (p == null)
                    continue;
                if (!source.isViewableFrom(p.getCentrePosition()))
                    continue;
                if (p.getCentrePosition().getZ() != source.getZ())
                    continue;

                p.getPacketSender().sendProjectile(
                    projectile.getStart().getX(),
                    projectile.getStart().getY(),
                    projectile.getOffset().getX(),
                    projectile.getOffset().getY(),
                    projectile.getAngle(),
                    projectile.getSpeed(),
                    projectile.getProjectileId(),
                    projectile.getStartHeight(),
                    projectile.getEndHeight(),
                    projectile.getLockon(),
                    projectile.getDelay(),
                    projectile.getSlope(),
                    projectile.getRadius());
            }
        }

        return projectile.getTime(projectile.getStart(), projectile.getOffset());
    }

    public Entity lastFaceEntity;

    public Mob setEntityInteraction(Entity entity) {
        getUpdateFlag().flag(Flag.ENTITY_INTERACTION);
        this.interactingEntity = entity;
        if (lastFaceEntity == entity) // stop spamming the same thing
            return this;
        lastFaceEntity = entity;
        return this;
    }

    public void animate(int animation) {
        animate(new Animation(animation));
    }

    public void animate(int animation, int delay) {
        animate(new Animation(animation, delay));
    }

    public void resetAnimation() {
        animate(-1, 0);
    }

    public void graphic(int id) {
        performGraphic(new Graphic(id, 0, 0));
    }

    public void graphic(int id, int height, int delay) {
        performGraphic(new Graphic(id, height, delay));
    }

    public void animate(Animation animation) {
        if (this.animation != null && animation != null) {
            AnimationDefinition currentAnimation = DefinitionRepository.animationDefinitions.get(this.animation.getId());
            AnimationDefinition nextAnimation = DefinitionRepository.animationDefinitions.get(animation.getId());
            if (currentAnimation != null) {
                if (nextAnimation != null) {
                    if (currentAnimation.priority > nextAnimation.priority) {
                        return;
                    }
                }
            }
        }
        this.animation = animation;
        getUpdateFlag().flag(Flag.ANIMATION);
    }

    public void performGraphic(Graphic graphic) {
        this.graphic = graphic;
        getUpdateFlag().flag(Flag.GRAPHIC);
    }

    /**
     * The {@link TimerRepository} which manages all of the
     * timers/delays for this {@link Entity}.
     */
    private final TimerRepository timerRepository = new TimerRepository();

    public TimerRepository getTimerRepository() {
        return timerRepository;
    }

    /*
     * Fields
     */
    private final Combat combat = new Combat(this);
    private final MovementQueue movementQueue = new MovementQueue(this);
    private String forcedChat;
    private boolean fixingDiagonal = false;
    private boolean repositioning = false;
    private Direction walkingDirection = Direction.NONE, runningDirection = Direction.NONE;
    private final UpdateFlag updateFlag = new UpdateFlag();
    private Animation animation;
    private Graphic graphic;
    private Entity interactingEntity;
    public Tile singlePlayerTileFacing;
    private boolean resetMovementQueue;
    private boolean needsPlacement;
    private int specialAttackPercentage = 100;
    private boolean specialActivated;
    private boolean recoveringSpecialAttack;
    protected Controller controller;

    public abstract Mob setHitpoints(int hitpoints);

    protected abstract void die();

    public abstract int getBaseAttackSpeed();

    public abstract int attackAnimation();

    public abstract int getBlockAnim();

    public ActionManager action = new ActionManager();

    public void heal(int amount) {
        heal(amount, 0);
    }

    public void heal(int amount, int exceed) {
        hp(hp() + amount, exceed);
    }

    public abstract void hp(int hp, int exceed);

    public abstract int hp();

    public abstract int maxHp();

    public boolean dead() {
        return hp() < 1;
    }

    public void message(String format, Object... params) {
        // Stub to ease player-specific messaging
    }

    /**
     * The player's head icon hint.
     */
    private int headHint = -1;


    /**
     * Gets the player's current head hint index.
     *
     * @return The player's head hint.
     */
    public int getHeadHint() {
        return headHint;
    }

    /**
     * Sets the player's head icon hint.
     *
     * @param headHint The hint index to use.
     * @return The Appearance instance.
     */
    public Mob setHeadHint(int headHint) {
        this.headHint = headHint;
        getUpdateFlag().flag(Flag.APPEARANCE);
        return this;
    }

    /**
     * Is this entity registered.
     */
    private boolean registered;

    public Graphic graphic() {
        return graphic;
    }

    public Animation getAnimation() {
        return animation;
    }

    private Tile faceTile;

    /**
     * Gets the face tile.
     *
     * @return The face tile, or <code>null</code> if the entity is not facing.
     */
    public Tile getFaceTile() {
        return faceTile;
    }

    /**
     * Checks if this entity is facing a location.
     *
     * @return The entity face flag.
     */
    public boolean isFacing() {
        return faceTile != null;
    }

    /**
     * Resets the facing location.
     */
    public void resetFaceTile() {
        this.faceTile = null;
    }

    public Mob face(Tile tile) {
        if (tile == null) {
            resetFaceTile();
            return this;
        }
        this.faceTile = tile;
        //Combat dummy can't face.
        if (isNpc() && (getAsNpc().isCombatDummy())) {
            return this;
        }
        if (faceTile != null) // only put this flag if the location isnt null, if its null we're just resetting
            this.getUpdateFlag().flag(Flag.FACE_TILE);
        return this;
    }

    public Mob face(int x, int y) {
        face(new Tile(x, y));
        return this;
    }

    /**
     * Face coordinates, but take into consideration the center of a large than 1x1 object
     */
    public void faceObj(GameObject obj) {
        if (obj == null) {
            resetFaceTile();
            return;
        }
        int x = obj.tile().x;
        int y = obj.tile().y;

        Tile faceTile;

        // Do some trickery to face properly
        if (tile().x == x && tile().y == y && (obj.getType() == 0 || obj.getType() == 5)) {
            if (obj.getRotation() == 0) {
                x--;
            } else if (obj.getRotation() == 1) {
                y++;
            } else if (obj.getRotation() == 2) {
                x++;
            } else if (obj.getRotation() == 3) {
                y--;
            }

            int sx = obj.definition().sizeX;
            int sy = obj.definition().sizeY;
            faceTile = new Tile(x + (sx / 2), y + (sy / 2));
        } else {
            faceTile = obj.tile;
        }

        face(faceTile);
    }

    public UpdateFlag getUpdateFlag() {
        return updateFlag;
    }

    public Combat getCombat() {
        return combat;
    }

    public Entity getInteractingEntity() {
        return interactingEntity;
    }

    public String getForcedChat() {
        return forcedChat;
    }

    public Mob setForcedChat(String forcedChat) {
        this.forcedChat = forcedChat;
        return this;
    }

    public boolean[] getPrayerActive() {
        return prayerActive;
    }

    public Mob setPrayerActive(boolean[] prayerActive) {
        this.prayerActive = prayerActive;
        return this;
    }

    public Mob setPrayerActive(int id, boolean prayerActive) {
        this.prayerActive[id] = prayerActive;
        return this;
    }

    public void healHit(Mob attacker, int heal, int delay) {
        hit(attacker, heal, delay, null, SplatType.NPC_HEALING_HITSPLAT);
    }

    public void decrementHealth(Hit hit) {
        if (dead() || hit.splatType == SplatType.NPC_HEALING_HITSPLAT) return;
        int outcome = hp() - hit.getDamage();
        if (outcome < 0) {
            outcome = 0;
            putAttrib(AttributeKey.KILLING_BLOW_HIT, hit);
        }
        setHitpoints(outcome);
        if (isNpc() && hp() <= 0) {
            if (getAsNpc().getCombatMethod() != null && getAsNpc().getCombatMethod().customOnDeath(hit)) {
                return;
            }
        }
        if (hp() < 1 && !locked()) {
            die();
        }
    }

    public Boundary getBoundary() {
        int x = tile().getX();
        int y = tile().getY();
        int size = (getSize() - 1);
        return new Boundary(x, x + size, y, y + size);
    }

    public Boundary boundaryBounds() {
        return new Boundary(tile(), getSize());
    }

    public Boundary boundaryBounds(int enlarge) {
        return new Boundary(tile().getX() - enlarge, tile().getY() - enlarge, (tile().getX() + getSize() - 1) + enlarge,
            (tile().getY() + getSize() - 1) + enlarge, tile().getLevel());
    }

    public Boundary getBounds() {
        return new Boundary(tile().getX(), tile().getY(), tile().getX() + getSize() - 1, tile().getY() + getSize() - 1, tile().getLevel());
    }

    public Area bounds(int enlargedBy) {
        return new Area(tile.x - enlargedBy, tile.y - enlargedBy, (tile.x + getSize() - 1) + enlargedBy, (tile.y + getSize() - 1) + enlargedBy);
    }

    public Area bounds() {
        return new Area(tile.x, tile.y, tile.x, tile.y);
    }

    public List<Splat> splats = new ArrayList<>(4);

    public void setWalkingDirection(Direction walkDirection) {
        this.walkingDirection = walkDirection;
    }

    public void setRunningDirection(Direction runDirection) {
        this.runningDirection = runDirection;
    }

    public Direction getWalkingDirection() {
        return walkingDirection;
    }

    public Direction getRunningDirection() {
        return runningDirection;
    }

    /**
     * Determines if this mob needs to reset their movement queue.
     *
     * @return {@code true} if this mob needs to reset their movement
     * queue, {@code false} otherwise.
     */
    public final boolean isResetMovementQueue() {
        return resetMovementQueue;
    }

    /**
     * Gets if this entity is registered.
     *
     * @return the unregistered.
     */
    public boolean isRegistered() {
        return registered;
    }

    /**
     * Sets if this entity is registered,
     *
     * @param registered the registered to set.
     */
    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    /**
     * Sets the value for resetMovementQueue.
     *
     * @param resetMovementQueue the new value to set.
     */
    public final void setResetMovementQueue(boolean resetMovementQueue) {
        this.resetMovementQueue = resetMovementQueue;
    }

    public void setNeedsPlacement(boolean needsPlacement) {
        this.needsPlacement = needsPlacement;
    }

    public boolean isNeedsPlacement() {
        return needsPlacement;
    }

    public boolean isSpecialActivated() {
        return specialActivated;
    }

    public void setSpecialActivated(boolean specialActivated) {
        this.specialActivated = specialActivated;
    }

    public int getSpecialAttackPercentage() {
        return specialAttackPercentage;
    }

    public void setSpecialAttackPercentage(int specialAttackPercentage) {
        this.specialAttackPercentage = specialAttackPercentage;
    }

    public void desecreaseSpecialAttack(int drainAmount) {
        this.specialAttackPercentage -= drainAmount;

        if (specialAttackPercentage < 0) {
            specialAttackPercentage = 0;
        }
    }

    public void restoreSpecialAttack() {
        if (specialAttackPercentage == 100)
            return;
        restoreSpecialAttack(10);
        if (specialAttackPercentage == 100 || specialAttackPercentage == 50)
            message("<col=00FF00>Your special attack energy is now " + specialAttackPercentage + "%.");
    }

    public void restoreSpecialAttack(int percentage) {
        if (specialAttackPercentage >= 100)
            return;
        specialAttackPercentage += specialAttackPercentage > (100 - percentage) ? 100 - specialAttackPercentage : percentage;
        CombatSpecial.updateBar(((Player) this));
    }

    public boolean isRecoveringSpecialAttack() {
        return recoveringSpecialAttack;
    }

    public void setRecoveringSpecialAttack(boolean recoveringSpecialAttack) {
        this.recoveringSpecialAttack = recoveringSpecialAttack;
    }

    public boolean inDungeon() {
        return false;
    }

    public int distanceToPoint(int pointX, int pointY) {
        return (int) Math.sqrt(Math.pow(getX() - pointX, 2) + Math.pow(getY() - pointY, 2));
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public boolean isFixingDiagonal() {
        return fixingDiagonal;
    }

    public void setFixingDiagonal(boolean fixingDiagonal) {
        this.fixingDiagonal = fixingDiagonal;
    }

    public boolean isRepositioning() {
        return repositioning;
    }

    public void setRepositioning(boolean repositioning) {
        this.repositioning = repositioning;
    }

    protected Map<AttributeKey, Object> attribs;

    public boolean hasAttrib(AttributeKey key) {
        return attribs != null && attribs.containsKey(key);
    }

    /**
     * Gets an attribute without a default value.
     * Make sure to be careful using this, to avoid
     * NullPointerExceptions because of no default value.
     *
     * @param key
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttrib(AttributeKey key) {
        return attribs == null ? null : (T) attribs.get(key);
    }

    /**
     * Gets an attribute with a default value.
     *
     * @param key
     * @param defaultValue
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribOr(AttributeKey key, Object defaultValue) {
        return attribs == null ? (T) defaultValue : (T) attribs.getOrDefault(key, defaultValue);
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrT(AttributeKey key, T defaultValue) {
        return attribs == null ? (T) defaultValue : (T) attribs.getOrDefault(key, defaultValue);
    }

    public void clearAttrib(AttributeKey key) {
        if (attribs != null)
            attribs.remove(key);
    }

    public void clearAttribs() {
        attribs.clear();
    }

    public Object putAttrib(AttributeKey key, Object v) {
        if (attribs == null)
            attribs = new EnumMap<>(AttributeKey.class);
        return attribs.put(key, v);
    }

    /**
     * Modifies the current numerical value of an attribute.
     *
     * @param key          the key of the attribute to be changed.
     * @param modifier     the value that will be modifying the current value.
     * @param defaultValue the default value to be inserted if none exists.
     * @param <T>          the type of number being modified.
     * @throws IllegalArgumentException thrown when the current value returned from the key is not parsable to numerical value
     *                                  or if the modifier and defaultValue are not the same class.
     */
    public <T extends Number> void modifyNumericalAttribute(AttributeKey key, T modifier, T defaultValue) throws
        IllegalArgumentException {
        Preconditions.checkArgument(modifier.getClass() == defaultValue.getClass(),
            "Modifier and defaultValue must have same class.");

        Number current = getAttribOr(key, defaultValue);

        if (current.getClass() == Byte.class) {
            putAttrib(key, current.byteValue() + modifier.byteValue());
        } else if (current.getClass() == Short.class) {
            putAttrib(key, current.shortValue() + modifier.shortValue());
        } else if (current.getClass() == Integer.class) {
            putAttrib(key, current.intValue() + modifier.intValue());
        } else if (current.getClass() == Long.class) {
            putAttrib(key, current.longValue() + modifier.longValue());
        } else if (current.getClass() == Float.class) {
            putAttrib(key, current.floatValue() + modifier.floatValue());
        } else if (current.getClass() == Double.class) {
            putAttrib(key, current.doubleValue() + modifier.doubleValue());
        } else {
            throw new IllegalArgumentException("current value isn't a parsable number.");
        }
    }

    /**
     * If there isn't a method for your type, it's probably not a simple primitive. Use {@link Player#getAttribTypeOr(Mob, AttributeKey, Object, Class, Supplier)} instead.
     */
    public static int getAttribIntOr(Mob player, AttributeKey key, @Nullable Object defaultValue) {
        return getAttribTypeOr(player, key, defaultValue, Integer.class, () -> 0);
    }

    /**
     * If there isn't a method for your type, it's probably not a simple primitive. Use {@link Player#getAttribTypeOr(Mob, AttributeKey, Object, Class, Supplier)} instead.
     */
    public static long getAttribLongOr(Mob player, AttributeKey key, @Nullable Object defaultValue) {
        return getAttribTypeOr(player, key, defaultValue, Long.class, () -> 0L);
    }

    /**
     * If there isn't a method for your type, it's probably not a simple primitive. Use {@link Player#getAttribTypeOr(Mob, AttributeKey, Object, Class, Supplier)} instead.
     */
    public static boolean getAttribBooleanOr(Mob player, AttributeKey key, @Nullable Object defaultValue) {
        return getAttribTypeOr(player, key, defaultValue, Boolean.class, () -> false);
    }

    /**
     * If there isn't a method for your type, it's probably not a simple primitive. Use {@link Player#getAttribTypeOr(Mob, AttributeKey, Object, Class, Supplier)} instead.
     */
    public static String getAttribStringOr(Mob player, AttributeKey key, @Nullable Object defaultValue) {
        return getAttribTypeOr(player, key, defaultValue, String.class, () -> "");
    }

    /**
     * If there isn't a method for your type, it's probably not a simple primitive. Use {@link Player#getAttribTypeOr(Mob, AttributeKey, Object, Class, Supplier)} instead.
     */
    public static double getAttribDoubleOr(Mob player, AttributeKey key, @Nullable Object defaultValue) {
        return getAttribTypeOr(player, key, defaultValue, Double.class, () -> 0d);
    }

    static final Class<?>[] DISALLOWED = new Class[]{int.class, float.class, byte.class, double.class, long.class, short.class};

    /**
     * To avoid class cast exceptions when dealing with {@link AttributeKey}, since OSS' attributeMap doesn't enforce types,
     * and specifically for mission-critical player serialization (we don't care about runtime/gameplay errors.. yet)
     * do some hardcoded type checks so serialization never breaks and we don't lose players progress. Supplier is the concrete return type, will always be 100% correct matching the expected type of an attrib's values. defaultValue is an object, has no type checking, and can be totally wrong. The point of this method is to only use default if the type won't produce a CCE.
     *
     * @param defaultValue The offending generic -- no way to get runtime types from the <T> returned generic type..
     *                     so no way to compare this class to T
     * @param type         Enforces the type check to avoid serialization exceptions
     * @param supplier     The correct return type.
     * @author Shadowrs/Jak
     * @since 06/06/2020
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAttribTypeOr(@Nonnull Mob player, @Nonnull AttributeKey key, @Nullable Object defaultValue, @Nonnull Class<T> type, @Nonnull Supplier<T> supplier) {
        Preconditions.checkArgument(Arrays.stream(DISALLOWED).noneMatch(p -> type == p), "You cannot use type %s", type.getName());
        if (!player.hasAttrib(key)) {
            if (defaultValue == null && !type.isPrimitive() || defaultValue != null && defaultValue.getClass() == type) {
                return (T) defaultValue;
            } else {
                String msg = String.format("CRITICAL ERROR: wrong fallback Type associated with AttributeKey %s (expected: %s, but got: %s) when saving Player: %s. Using fallback value %s",
                    key, type, defaultValue == null ? "null" : defaultValue.getClass(), player.getMobName(), supplier.get());
                logger.error(msg);
                if (!GameServer.properties().production) {
                    throw new RuntimeException("IncorrectFallbackTypeException");
                }
                return supplier.get();
            }
        }
        @Nullable Object stored = player.getAttrib(key);
        if (stored == null && !type.isPrimitive() || stored != null && stored.getClass() == type) {
            return (T) stored;
        } else {
            String msg = String.format("CRITICAL ERROR: wrong stored Type associated with AttributeKey %s (expected: %s, but got: %s) when saving Player: %s. Data loss possible. Replacing value '%s' with %s",
                key, type, stored == null ? "null" : stored.getClass(), player.getMobName(),
                stored, supplier.get());
            logger.error(msg);
            if (!GameServer.properties().production) {
                throw new RuntimeException("IncorrectStoredTypeException");
            }
            return supplier.get();
        }
    }

    /**
     * doesnt return {@code Hit} instance because its immidiately submitted() so you cant change properties after.
     */
    public void hit(Mob attacker, int damage) {
        hit(attacker, damage, SplatType.HITSPLAT);
    }


    /**
     * doesnt return {@code Hit} instance because its immidiately submitted() so you cant change properties after.
     */
    public void hit(Mob attacker, int damage, int delay) {
        hit(attacker, damage, SplatType.HITSPLAT);
    }

    /**
     * doesnt return {@code Hit} instance because its immidiately submitted() so you cant change properties after.
     */
    public void hit(Mob attacker, int damage, SplatType type) {
        hit(attacker, damage, 0, null).setSplatType(type).submit();
    }

    /**
     * doesnt return {@code Hit} instance because its immidiately submitted() so you cant change properties after.
     */
    public void hit(Mob attacker, int damage, CombatType combatType, SplatType type) {
        hit(attacker, damage, 0, combatType).setSplatType(type).submit();
    }

    /**
     * Use a builder pattern, allowing you to call methods to change properties of Hit before calling {@code CombatFactory.addPendingHit(hit);}
     */
    public Hit hit(Mob attacker, int damage, int delay, CombatType type) {
        Hit hit = Hit.builder(attacker, this, damage, delay, type);
        return hit;
    }

    public Hit hit(Mob attacker, int damage, CombatType type) {
        Hit hit = Hit.builder(attacker, this, damage, 1, type);
        return hit;
    }

    public void delayedGraphics(int graphic, int height, int delay) {
        Chain.bound(this).runFn(delay, () -> graphic(graphic, height, 0));
    }

    public void delayedGraphics(Graphic graphic, int delay) {
        Chain.bound(this).runFn(delay, () -> performGraphic(graphic));
    }

    protected boolean noRetaliation = false;

    public void noRetaliation(boolean b) {
        noRetaliation = b;
    }

    public void autoRetaliate(Mob attacker) {
        boolean sameAttacker = attacker == this;
        //if (isNpc() && getAsNpc().id() == SKOTIZO)
        //    System.out.println("retal skot");
        if (dead() || hp() < 1 || (isPlayer() && !getCombat().autoRetaliate()) || noRetaliation || locked() || stunned() || sameAttacker) {
            Debugs.CMB.debug(attacker, "auto ret: dead: " + dead() + " hp: " + hp() + " autoret: " + getCombat().autoRetaliate() + " noRetaliation: " + noRetaliation + " locked: " + locked() + " stunned: " + stunned() + " sameAttacker: " + sameAttacker, this, true);
            //System.out.println("dead: "+dead()+" hp: "+hp()+" autoret: "+getCombat().autoRetaliate()+" noRetaliation: "+noRetaliation+" locked: "+locked()+" stunned: "+stunned()+" sameAttacker: "+sameAttacker);
            return;
        }

        // As soon as the hit on us appears, we'll turn around and face the attacker.
        setEntityInteraction(attacker);

        runFn(2, () -> {
            // Override logic
            getCombat().setTarget(attacker);
            // this mob needs to hit the attacker, not vice versa
            this.getCombat().attack(attacker);
        });
        //System.out.println(this.getMobName()+" retal to "+attacker.getMobName());
    }

    /**
     * Sounds that happen when the hit appears.
     * Two distinct: a sound if damage>0 .. and a shield block sound.
     * Note: these sounds are special because they are _personal_ 'effect' sounds - not AREA sounds broadcast to closeby players.
     */
    public void takehitSound(Hit hit) {
        if (hit == null)
            return;
        // block sounds depends entirely on entity type

        if (hit.getAttacker() != null && hit.getAttacker() instanceof Player) {
            if (hit.getDamage() > 0)
                ((Player) hit.getAttacker()).sound(CombatSounds.damage_sound(), 20);
        }
    }

    public long lockTime;

    public LockType getLock() {
        return lock;
    }

    private LockType lock = LockType.NONE;

    public boolean locked() {
        return lock != null && lock != LockType.NONE;
    }

    public String lockState() {
        return lock == null ? "NULL" : lock.name();
    }

    public boolean isLogoutOkLocked() {
        return lock == LockType.FULL_LOGOUT_OK;
    }

    public boolean moveLocked() {
        return lock == LockType.MOVEMENT;
    }

    public void lock() {
        lock = LockType.FULL;
        lockTime = System.currentTimeMillis();
    }

    public void logoutLock() {
        this.lock = LockType.FULL_LOGOUT_OK;
        lockTime = System.currentTimeMillis();
    }

    public void lockNoDamage() {
        lock = LockType.NULLIFY_DAMAGE;
        lockTime = System.currentTimeMillis();
    }

    public void lockDelayDamage() {
        lock = LockType.DELAY_DAMAGE;
        lockTime = System.currentTimeMillis();
    }

    public void lockDamageOk() {
        lock = LockType.FULL_WITHDMG;
        lockTime = System.currentTimeMillis();
    }

    public boolean isNullifyDamageLock() {
        return lock == LockType.NULLIFY_DAMAGE;
    }

    public boolean isDelayDamageLocked() {
        lockTime = System.currentTimeMillis();
        return lock == LockType.DELAY_DAMAGE;
    }

    /**
     * Locked, unable to attack. Cerberus.
     */
    public boolean isNoAttackLocked() {
        return lock == LockType.NO_ATTACK;
    }

    public boolean isDamageOkLocked() {
        return lock == LockType.FULL_WITHDMG;
    }

    public void lockMovement() {
        lock = LockType.MOVEMENT;
        lockTime = System.currentTimeMillis();
    }

    public void lockNoAttack() {
        lock = LockType.NO_ATTACK;
        lockTime = System.currentTimeMillis();
    }

    public void unlock() {
        lock = LockType.NONE;
    }

    public boolean canWalkNPC(int toX, int toY) {
        return canWalkNPC(toX, toY, false);
    }


    private MovementQueue.Step getPreviewNextWalkStep() {
        MovementQueue.Step step = movementQueue.steps.poll();
        if (step == null)
            return null;
        return step;
    }

    public boolean canWalkNPC(int toX, int toY, boolean checkUnder) {
        if (this.<Integer>getAttribOr(AttributeKey.MULTIWAY_AREA, -1) != 1 /*|| (!checkUnder && !canWalkNPC(getX(), getY(), true))*/)
            return true;
        int size = getSize();
       /* for(int regionId : getMapRegionsIds()) {
            List<Integer> npcIndexes = World.getRegion(regionId).getNPCsIndexes();
            if(npcIndexes != null) {
                for(int npcIndex : npcIndexes) {
                    NPC target = World.getWorld().getNpcs().get(npcIndex);
                    if(target == null || target == this || target.isDead() || !target.isRegistered() || target.getZ() != getZ() || !AreaManager.inMulti(target))
                        continue;
                    int targetSize = target.getSize();
                    if(!checkUnder && target.getNextWalkDirection() == -1) { //means the walk hasnt been processed yet
                        int previewDir = getPreviewNextWalkStep();
                        if(previewDir != -1) {
                            Position tile = target.getPosition().transform(Directions.DIRECTION_DELTA_X[previewDir],
                                Directions.DIRECTION_DELTA_Y[previewDir], 0);
                            if(colides(tile.getX(), tile.getY(), targetSize, getX(), getY(), size))
                                continue;

                            if(colides(tile.getX(), tile.getY(), targetSize, toX, toY, size))
                                return false;
                        }
                    }
                    if(colides(target.getX(), target.getY(), targetSize, getX(), getY(), size))
                        continue;
                    if(colides(target.getX(), target.getY(), targetSize, toX, toY, size))
                        return false;
                }
            }
        }*/
        return true;
    }

    private static boolean colides(int x1, int y1, int size1, int x2, int y2, int size2) {
        for (int checkX1 = x1; checkX1 < x1 + size1; checkX1++) {
            for (int checkY1 = y1; checkY1 < y1 + size1; checkY1++) {
                for (int checkX2 = x2; checkX2 < x2 + size2; checkX2++) {
                    for (int checkY2 = y2; checkY2 < y2 + size2; checkY2++) {
                        if (checkX1 == checkX2 && checkY1 == checkY2)
                            return true;
                    }

                }
            }
        }
        return false;
    }

    public boolean hasWalkSteps() {
        return movementQueue.steps.size() > 1;
    }

    public void resetWalkSteps() {
        movementQueue.clear();
    }

    public Tile getPreviousTile() {
        if (previousTile == null) {
            previousTile = RouteFinder.findWalkable(tile());
        }
        return previousTile;
    }

    public void setPreviousTile(Tile previousTile) {
        this.previousTile = previousTile;
    }

    private Tile previousTile;

    private Skills skills;

    public Skills skills() {
        return skills;
    }

    public boolean poison(int damage) {
        return poison(damage, true);
    }

    private InfectionType infectionType;

    public InfectionType infection() {
        return infectionType;
    }

    public void setInfection(InfectionType infectionType) {
        this.infectionType = infectionType;
        if (this.isPlayer()) {
            this.getAsPlayer().getPacketSender().sendInfection(infectionType);
            this.getUpdateFlag().flag(Flag.APPEARANCE);
        }
    }

    public boolean poison(int dmg, boolean sendmsg) {
        int venomState = getAttribOr(AttributeKey.VENOM_TICKS, 0);
        // Can't inflict poison if venomed, it takes priority.
        if (venomState > 0)
            return false;

        if (isPlayer()) {
            Player p = (Player) this;
            if (Equipment.venomHelm(p)) { // Serp helm stops poison.
                return false;
            }
            if ((int) getAttribOr(AttributeKey.POISON_TICKS, 0) != 0) {
                // Immune or already poisoned.
                return false;
            }
            if (sendmsg)
                message("You have been poisoned!");
            p.setInfection(InfectionType.POISON_INFECTION);
            p.putAttrib(AttributeKey.POISON_TICKS, Poison.ticksForDamage(dmg));
        } else if (isNpc()) {
            Npc me = (Npc) this;
            if (me.isPoisonImmune())
                return false;
            if ((int) getAttribOr(AttributeKey.POISON_TICKS, 0) != 0) {
                // Immune / already poisoned
                return false;
            }
            putAttrib(AttributeKey.POISON_TICKS, Poison.ticksForDamage(dmg));
        }
        return true;
    }

    public void venom(Mob source) {
        if (source == null || source == this)
            return;

        if (Venom.venomed(source))
            return;

        boolean scorpiosPetOut = false;
        if (source.isPlayer()) {
            scorpiosPetOut = source.getAsPlayer().hasPetOut("Skorpios pet");
        }

        if (isPlayer()) {
            Player p = (Player) this;
            //Zulrah pet is resistant vs venom
            boolean snakelingPetout = p.hasPetOut("Snakeling");
            boolean petTamerII = p.<Boolean>getAttribOr(AttributeKey.VENOM_RESISTANT, false);

            //System.out.println(snakelingPetout);
            //System.out.println(petTamerII);
            if (snakelingPetout && petTamerII) {
                return;
            }

            if (Equipment.venomHelm(p) && !scorpiosPetOut) { // Serp helm stops venom.
                return;
            }
            boolean admin_bypass = false;
            if (source.isPlayer()) {
                boolean fromAdmin = ((Player) source).getPlayerRights().isAdminOrGreater(p);
                if (fromAdmin && GameServer.properties().venomFromAdminsOn) {
                    admin_bypass = true;
                }
            }
            // Source was a normal player
            if (!GameServer.properties().venomVsPlayersOn && !admin_bypass)
                return;
        } else if (isNpc()) {
            Npc me = (Npc) this;
            if (me.isVenomImmune() && !scorpiosPetOut) {
                return;
            }
        }

        int tick = getAttribOr(AttributeKey.VENOM_TICKS, 0);
        if (tick == 0) {
            if (isPlayer()) {
                Player me = (Player) this;
                me.setInfection(InfectionType.VENOM_INFECTION); // Now venomed
                me.message("<col=145A32>You've been infected with venom!");
            }
            putAttrib(AttributeKey.VENOM_TICKS, 8); // default start -- venom newly applied. 8 cycles
            putAttrib(VENOMED_BY, source);
            Venom.setTimer(this);
        }
    }

    private final List<Player> localPlayers = new LinkedList<>();
    private final List<Npc> localNpcs = new LinkedList<>();

    public List<Player> getLocalPlayers() {
        return localPlayers;
    }

    public List<Npc> getLocalNpcs() {
        return localNpcs;
    }

    /**
     * shortcut for {@link Chain#bound(Object)}.{@link Chain#runFn(int, Runnable)}
     */
    public Chain<Mob> runFn(int startAfterTicks, Runnable r) {
        return Chain.bound(this).runFn(startAfterTicks, r);
    }

    /**
     * shortcut for {@link Chain#bound(Object)}.{@link Chain#runFn(int, Runnable)}
     * <br>
     * not bound to anything
     */
    public Chain runUninterruptable(int startAfterTicks, Runnable r) {
        return Chain.runGlobal(startAfterTicks, r);
    }

    /**
     * shortcut to {@link Chain#waitForTile(Tile, Runnable)}
     */
    public Chain<Mob> waitForTile(Tile tile, Runnable work) {
        return Chain.bound(this).waitForTile(tile, work);
    }

    public Chain<Mob> walkAndWait(Tile tile, Runnable work) {
        //smartPathTo(tile, 1);
        smartPathTo(tile);
        return Chain.bound(this).waitForTile(tile, work);
    }

    public Chain<Mob> waitUntil(int tickBetweenLoop, BooleanSupplier condition, Runnable work) {
        return Chain.bound(this).waitUntil(tickBetweenLoop, condition, work);
    }

    public Chain<Mob> waitUntil(BooleanSupplier condition, Runnable work) {
        return Chain.bound(this).waitUntil(1, condition, work);
    }

    public void onHit(Hit hit) {

    }

    public void takeHit() {

    }

    public void smartPathTo(Tile targetPos) {
        getRouteFinder().routeAbsolute(targetPos.x, targetPos.y);
    }

    public void smartPathTo(int destX, int destY) {
        getRouteFinder().routeAbsolute(destX, destY);
    }


    public final List<Task> activeTasks = new LinkedList<>();

    private RouteFinder routeFinder;

    public RouteFinder getRouteFinder() {
        if (routeFinder == null)
            routeFinder = new RouteFinder(this);
        return routeFinder;
    }

    public void step(int diffX, int diffY, StepType stepType) {
        stepAbs(getAbsX() + diffX, getAbsY() + diffY, stepType);
    }

    public void stepAbs(Tile tile, StepType stepType) {
        var absX = tile.getX();
        var absY = tile.getY();
        /* forces a step without route finding */
        MovementQueue movement = getMovementQueue();
        movement.readOffset = 0;
        movement.getStepsX()[0] = absX;
        movement.getStepsY()[0] = absY;
        movement.writeOffset = 1;
        movement.stepType = stepType;
    }

    public void stepAbs(int absX, int absY, StepType stepType) {
        /* forces a step without route finding */
        MovementQueue movement = getMovementQueue();
        movement.readOffset = 0;
        movement.getStepsX()[0] = absX;
        movement.getStepsY()[0] = absY;
        movement.writeOffset = 1;
        movement.stepType = stepType;
    }

    public void resetSteps() {
        MovementQueue movement = getMovementQueue();
        movement.readOffset = 0;
        movement.writeOffset = 0;
        movement.stepType = StepType.NORMAL;
    }

    public boolean addStep(int absX, int absY) {
        MovementQueue movement = getMovementQueue();
        if (movement.writeOffset < 50) {
            movement.getStepsX()[movement.writeOffset] = absX;
            movement.getStepsY()[movement.writeOffset] = absY;
            movement.writeOffset++;
            return true;
        }
        return false;
    }

    public boolean isMovementBlocked(boolean message, boolean ignoreFreeze) {
        return !movementQueue.canMove(message);
    }

    public int getAbsX() {
        return tile.getX();
    }

    public int getAbsY() {
        return tile.getY();
    }

    public boolean isAt(Tile pos) {
        return isAt(pos.getX(), pos.getY());
    }

    public boolean isAt(int x, int y) {
        return tile.getX() == x && tile.getY() == y;
    }

    public static void accumulateRuntimeTo(Runnable r, Consumer<Duration> to) {
        if (!TimesCycle.BENCHMARKING_ENABLED) { // skip timer
            r.run();
            return;
        }
        com.google.common.base.Stopwatch stopwatch = Stopwatch.createStarted();
        r.run();
        stopwatch.stop();
        to.accept(stopwatch.elapsed());
    }

    public static void generalTimed(Runnable r, Consumer<Duration> to) {
        // no benchmark disabled check
        com.google.common.base.Stopwatch stopwatch = Stopwatch.createStarted();
        r.run();
        stopwatch.stop();
        to.accept(stopwatch.elapsed());
    }

    public static void time(Consumer<Duration> t, Runnable r) {
        if (!TimesCycle.BENCHMARKING_ENABLED) {// skip timer
            r.run();
            return;
        }
        com.google.common.base.Stopwatch stopwatch = Stopwatch.createStarted();
        r.run();
        stopwatch.stop();
        t.accept(stopwatch.elapsed());
    }

    private int graphicSwap;

    public int getGraphicSwap() {
        return graphicSwap;
    }

    public void setGraphicSwap(int graphicSwap) {
        this.graphicSwap = graphicSwap;
    }

    public double getHealthPercentage() {
        return (double) hp() / (double) maxHp();
    }

    public void hit(Mob attacker, int damage, int delay, CombatType combatType, SplatType type) {
        hit(attacker, damage, delay, combatType).setIsReflected().setSplatType(type).submit();
    }

    public void hit_(Mob attacker, int damage, int delay, SplatType type) {
        hit(attacker, damage, delay, null, type);
    }


}
