package com.twisted.game.world.entity.mob.npc;

import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.method.impl.npcs.bosses.corruptedhunleff.CorruptedHunleff;
import com.twisted.game.world.entity.combat.method.impl.npcs.karuulm.Wyrm;
import com.twisted.fs.NpcDefinition;
import com.twisted.game.content.areas.wilderness.content.boss_event.BossEvent;
import com.twisted.game.content.raids.chamber_of_xeric.great_olm.GreatOlm;
import com.twisted.game.content.raids.party.Party;
import com.twisted.game.content.skill.impl.hunter.trap.impl.Chinchompas;
import com.twisted.game.task.TaskManager;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.NodeType;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.CombatMethod;
import com.twisted.game.world.entity.combat.method.impl.npcs.bosses.demonicgorillas.DemonicGorilla;
import com.twisted.game.world.entity.combat.method.impl.npcs.bosses.zulrah.Zulrah;
import com.twisted.game.world.entity.combat.method.impl.npcs.fightcaves.TzTokJad;
import com.twisted.game.world.entity.combat.method.impl.npcs.godwars.armadyl.KreeArra;
import com.twisted.game.world.entity.combat.method.impl.npcs.godwars.bandos.Graardor;
import com.twisted.game.world.entity.combat.method.impl.npcs.godwars.saradomin.Zilyana;
import com.twisted.game.world.entity.combat.method.impl.npcs.godwars.zamorak.Kril;
import com.twisted.game.world.entity.combat.method.impl.npcs.karuulm.Drake;
import com.twisted.game.world.entity.combat.method.impl.npcs.karuulm.Hydra;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.Direction;
import com.twisted.game.world.entity.mob.Flag;
import com.twisted.game.world.entity.mob.npc.NpcMovementCoordinator.CoordinateState;
import com.twisted.game.world.entity.mob.npc.bots.NPCBotHandler;
import com.twisted.game.world.entity.mob.npc.impl.MaxHitDummyNpc;
import com.twisted.game.world.entity.mob.npc.impl.UndeadMaxHitDummy;
import com.twisted.game.world.entity.mob.npc.pets.Pet;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.ControllerManager;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.game.world.route.routes.TargetRoute;
import com.twisted.util.NpcIdentifiers;
import com.twisted.util.NpcPerformance;
import com.twisted.util.SecondsTimer;
import com.twisted.util.Utils;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Predicate;

import static com.twisted.util.CustomNpcIdentifiers.BRUTAL_LAVA_DRAGON;
import static com.twisted.util.NpcIdentifiers.*;
import static org.apache.logging.log4j.util.Unbox.box;

/**
 * Represents a non-playable mob, which players can interact with.
 *
 * @author Professor Oak
 */
@Data
public class Npc extends Mob {

    private static final Logger logger = LogManager.getLogger(Npc.class);

    public boolean isRandomWalkAllowed() {
        boolean canwalk = walkRadius > 0 && spawnArea != null && inViewport && !hidden() && getMovementQueue().isAtDestination() && !locked() && !isMovementBlocked(false, false);
        if (canwalk) {
            for (var player : World.getWorld().getPlayers()) {
                if (player == null) continue;
                if (player.tile().isViewableFrom(this.tile()))
                    return true;
            }
        }
        return canwalk;
    }

    public boolean isWorldBoss() {
        return (Arrays.stream(BossEvent.values()).anyMatch(boss -> id == boss.npc)) && tile.region() != 6810 || id == BRUTAL_LAVA_DRAGON;
    }

    public boolean isPet() {
        return (Arrays.stream(Pet.values()).anyMatch(pet -> id == pet.npc));
    }

    //Target switching may be computationally expensive since it's in sequence (core processing).
    public static boolean TARG_SWITCH_ON = true;

    public String spawnStack = "";
    private boolean lockMovementCompletely;

    public boolean completelyLockedFromMoving() {
        return lockMovementCompletely;
    }

    public void completelyLockedFromMoving(boolean lockMovementCompletely) {
        this.lockMovementCompletely = lockMovementCompletely;
    }

    private boolean cantMoveUnderCombat;

    public boolean cantMoveUnderCombat() {
        return cantMoveUnderCombat;
    }

    public void cantMoveUnderCombat(boolean cantMoveUnderCombat) {
        this.cantMoveUnderCombat = cantMoveUnderCombat;
    }


    private boolean canAttack = true;

    public boolean canAttack() {
        return canAttack;
    }

    public void canAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }

    private int id;
    private final Tile spawnTile;
    private boolean ancientSpawn;
    private int walkRadius;
    private int spawnDirection;
    private int lastDirection;
    // If a player can see this npc. if not, what's the point in processing it?
    private boolean inViewport = true;
    private NpcDefinition def;
    private int hp;
    private NpcCombatInfo combatInfo;
    private boolean hidden;
    private boolean respawns = true;
    private boolean venomImmune;
    private boolean poisonImmune;
    private Area spawnArea;
    private int transmog = -1;

    // A list of npc-ids such as Bosses that are immune to venom.
    public static final int[] venom_immunes = new int[]{NYLOCAS_VASILIAS_8355, NYLOCAS_VASILIAS_8356, NYLOCAS_VASILIAS_8357, NpcIdentifiers.COMBAT_DUMMY, NpcIdentifiers.UNDEAD_COMBAT_DUMMY, 3127, 494, 2265, 2266, 2267, 7144, 7145, 7146, 7147, 7148, 7149, 6611, 6612, 2042, 2043, 2044, 9035, 9036, 9037};
    public static final int[] poison_immunes = new int[]{NYLOCAS_VASILIAS_8355, NYLOCAS_VASILIAS_8356, NYLOCAS_VASILIAS_8357, NpcIdentifiers.COMBAT_DUMMY, NpcIdentifiers.UNDEAD_COMBAT_DUMMY, 9035, 9036, 9037};

    public Npc spawn(boolean respawns) {
        World.getWorld().registerNpc(this);
        respawns(respawns);
        return this;
    }

    public Npc(int id, Tile tile) {
        super(NodeType.NPC, tile);
        this.id = id;
        spawnTile = tile;
        def = World.getWorld().definitions().get(NpcDefinition.class, id);
        combatInfo = World.getWorld().combatInfo(id);
        hp = combatInfo == null ? 50 : combatInfo.stats.hitpoints;
        spawnArea = new Area(spawnTile, walkRadius);
        putAttrib(AttributeKey.MAX_DISTANCE_FROM_SPAWN, id == GIANT_MOLE ? 64 : 12);

        for (int types : venom_immunes) {
            if (id == types) {
                setVenomImmune(true);
            }
        }
        for (int types : poison_immunes) {
            if (id == types) {
                setPoisonImmune(true);
            }
        }

        try {
            NPCBotHandler.assignBotHandler(this);
        } catch (Exception e) {
            logger.catching(e);
            logger.error("NPC {} might not have an NPC definition entry.", box(id));
        }

        if (combatInfo() != null && combatInfo().scripts != null && combatInfo().scripts.combat_ != null) {
            if (id == NpcIdentifiers.ZULRAH || id == NpcIdentifiers.ZULRAH_2043 || id == NpcIdentifiers.ZULRAH_2044) {
                setCombatMethod(Zulrah.EmptyCombatMethod.make());
            }
            setCombatMethod(combatInfo().scripts.newCombatInstance());
        }

        if (getMobName().toLowerCase().contains("clerk") || getMobName().toLowerCase().contains("banker")) {
            skipReachCheck = t -> {
                Direction current = Direction.fromDeltas(getX() - t.getX(), getY() - t.getY());
                return current.isDiagonal || t.distance(tile()) == 1;
            };
        }
        if (tile().equals(3109, 3517))
            walkTo = tile.transform(1, 0);
    }

    /**
     * Returns a new instance of the npc with its respective extension.
     *
     * @param id
     * @param tile
     * @return the NPC
     */
    public static Npc of(int id, Tile tile) {
        return switch (id) {
            case NpcIdentifiers.COMBAT_DUMMY -> new MaxHitDummyNpc(id, tile);
            case NpcIdentifiers.UNDEAD_COMBAT_DUMMY -> new UndeadMaxHitDummy(id, tile);
            case Wyrm.IDLE, Wyrm.ACTIVE -> new Wyrm(id, tile);
            case 8609 -> new Hydra(id, tile);
            case 8612, 8613 -> new Drake(id, tile);
            case NpcIdentifiers.TZTOKJAD -> new TzTokJad(id, tile);
            case NpcIdentifiers.DEMONIC_GORILLA,
                 NpcIdentifiers.DEMONIC_GORILLA_7145,
                 NpcIdentifiers.DEMONIC_GORILLA_7146 -> new DemonicGorilla(id, tile);
            case 7555, 7554, 7553 -> new GreatOlm(id, tile);
            case NpcIdentifiers.CORRUPTED_HUNLLEF,
                 NpcIdentifiers.CORRUPTED_HUNLLEF_9036,
                 NpcIdentifiers.CORRUPTED_HUNLLEF_9037 -> new CorruptedHunleff(id, tile);
            default -> new Npc(id, tile);
        };
    }

    public int transmog() {
        return transmog;
    }

    public void transmog(int id) {
        this.transmog = id;
        this.id = id;
        NpcDefinition def = def();
        setSize(def.size);
        if (combatInfo != null)
            this.combatInfo(World.getWorld().combatInfo(id));
        this.getUpdateFlag().flag(Flag.TRANSFORM);
    }

    public void transmog(int id, boolean maxHp) {
        this.transmog = id;
        this.id = id;
        this.def(World.getWorld().definitions().get(NpcDefinition.class, id));
        if (combatInfo != null) this.combatInfo = (World.getWorld().combatInfo(id));
        this.setHitpoints(maxHp ? this.maxHp() : this.hp());
        NpcDefinition def = def();
        setSize(def.size);
        getUpdateFlag().flag(Flag.TRANSFORM);
    }

    public void inViewport(boolean b) {
        inViewport = b;
    }

    public boolean inViewport() {
        return inViewport;
    }

    public void walkRadius(int r) {
        if (walkRadius != r) {
            spawnArea = new Area(spawnTile, r);
        }
        walkRadius = r;
    }

    public int walkRadius() {
        return walkRadius;
    }

    public boolean ancientSpawn() {
        return ancientSpawn;
    }

    public void ancientSpawn(boolean ancient) {
        ancientSpawn = ancient;
    }

    public Npc spawnDirection(int d) {
        spawnDirection = d;
        return this;
    }

    public int spawnDirection() {
        return spawnDirection;
    }

    public Npc lastDirection(int d) {
        lastDirection = d;
        return this;
    }

    public int lastDirection() {
        return lastDirection;
    }

    public Tile spawnTile() {
        return spawnTile;
    }

    public int id() {
        if (transmog != -1) {
            return transmog();
        }
        return id;
    }

    public NpcDefinition def() {
        return def;
    }

    public void def(NpcDefinition d) {
        this.def = d;
    }

    public NpcCombatInfo combatInfo() {
        return combatInfo;
    }

    public void combatInfo(NpcCombatInfo info) {
        combatInfo = info;
    }

    public void hidden(boolean b) {
        hidden = b;
        Tile.occupy(this);
    }

    public boolean hidden() {
        return hidden;
    }

    public Npc respawns(boolean b) {
        respawns = b;
        return this;
    }

    public boolean respawns() {
        return respawns;
    }

    public boolean isBot() {
        return id >= 13000 && id <= 13009;
    }

    public boolean isVenomImmune() {
        return venomImmune;
    }

    public void setVenomImmune(boolean venomImmune) {
        this.venomImmune = venomImmune;
    }

    public boolean isPoisonImmune() {
        return poisonImmune;
    }

    public void setPoisonImmune(boolean poisonImmune) {
        this.poisonImmune = poisonImmune;
    }

    /**
     * The npc's movement coordinator.
     * Handles random walking.
     */
    private final NpcMovementCoordinator movementCoordinator = new NpcMovementCoordinator(this);

    /**
     * The npc's combat method, used
     * for attacking.
     */
    private CombatMethod combatMethod;

    /**
     * The {@link SecondsTimer} where this npc is
     * immune to attacks.
     */
    private final SecondsTimer immunity = new SecondsTimer();

    public boolean canSeeTarget(Mob attacker, Mob target) {
        return attacker.tile().isWithinDistance(target.tile());
    }

    public boolean isCombatDummy() {
        return this.id == NpcIdentifiers.COMBAT_DUMMY || this.id == NpcIdentifiers.UNDEAD_COMBAT_DUMMY;
    }

    public boolean isPvPCombatDummy() {
        return this.id == NpcIdentifiers.UNDEAD_COMBAT_DUMMY;
    }

    public NpcPerformance performance = new NpcPerformance();

    /**
     * Processes this npc. Previously called onTick.
     */
    public void sequence() {
        sequenceNormal();
    }

    private void sequenceNormal() {
        action.sequence();
        TaskManager.sequenceForMob(this);
        getTimerRepository().cycle(this);
        getCombat().followTarget();
        getCombat().preAttack();
        if (id == 8063) TargetRoute.beforeMovement(this);
        getMovementQueue().process();
        if (id == 8063) TargetRoute.afterMovement(this);
        movementCoordinator.process();
        if (getBotHandler() != null) {
            getBotHandler().process();
        }
        if (this.getCombatMethod() instanceof CommonCombatMethod method) {
            method.process(this);
        }
        getCombat().process();
        ControllerManager.process(this);
    }

    public void findAgroTarget() {
        if (combatMethod instanceof CommonCombatMethod ccm) {
            if (!ccm.isAggressive())
                return;
        }

        boolean wilderness = (WildernessArea.wildernessLevel(tile()) >= 1) && !WildernessArea.inside_rouges_castle(tile()) && !Chinchompas.hunterNpc(id);

        if (dead() || !inViewport || locked() || combatInfo == null || !(combatInfo.aggressive || (wilderness && getBotHandler() == null)))
            return;

        final int ceil = def.combatlevel * 2;
        final boolean override = combatInfo != null && combatInfo.scripts != null && combatInfo.scripts.agro_ != null;
        var bounds = boundaryBounds(combatInfo != null ? combatInfo.aggroradius : 1);

        Set<Player> temp = new HashSet<>();
        for (final Player player : this.getLocalPlayers()) {
            if (player == null || player.getZ() != this.getZ() || player.looks().hidden() || temp.contains(player))
                continue;
            if (this.getCombat().inCombat() || !bounds.inside(player.tile()))
                continue;
            if (override) {
                if (!combatInfo.scripts.agro_.shouldAgro(this, player)) continue;
                temp.add(player);
                continue;
            }
            if (player.getSkills().combatLevel() <= ceil) {
                CombatFactory.bothInFixedRoom(this, player);
                temp.add(player);
            }
        }

        for (Player p : temp) {
            long lastTime = System.currentTimeMillis() - (long) p.getAttribOr(AttributeKey.LAST_WAS_ATTACKED_TIME, 0L);
            Mob lastAttacker = p.getAttrib(AttributeKey.LAST_DAMAGER);
            if (lastTime > 5000L || lastAttacker == this ||
                (lastAttacker != null && (lastAttacker.dead() || lastAttacker.finished()))
                || p.<Integer>getAttribOr(AttributeKey.MULTIWAY_AREA, -1) == 1) {
                if (CombatFactory.canAttack(this, combatMethod, p)) {
                    getCombat().attack(p);
                    break;
                }
            }
        }
    }

    /**
     * Sets the interacting entity.
     *
     * @param mob The new entity to interact with.
     */
    public void faceEntity(Mob mob) {
        this.setEntityInteraction(mob);
        this.getUpdateFlag().flag(Flag.ENTITY_INTERACTION);
    }

    public NpcMovementCoordinator getMovementCoordinator() {
        return movementCoordinator;
    }

    /**
     * The npc's head icon.
     */
    private int PKBotHeadIcon = -1;

    public int getPKBotHeadIcon() {
        return PKBotHeadIcon;
    }

    public void setPKBotHeadIcon(int PKBotHeadIcon) {
        this.PKBotHeadIcon = PKBotHeadIcon;
        //We used to flag APPEARANCE, now we flag TRANSFORM.
        getUpdateFlag().flag(Flag.TRANSFORM);
    }

    /**
     * The npc bot handler.
     */
    private NPCBotHandler botHandler;

    public NPCBotHandler getBotHandler() {
        return botHandler;
    }

    public void setBotHandler(NPCBotHandler botHandler) {
        this.botHandler = botHandler;
    }

    public CombatMethod getCombatMethod() {
        return combatMethod;
    }

    public void setCombatMethod(CombatMethod combatMethod) {
        this.combatMethod = combatMethod;
    }

    public SecondsTimer getImmunity() {
        return immunity;
    }

    public void graphic(int graphic) {
        this.performGraphic(new Graphic(graphic));
    }

    private boolean target_fleeing(Area room, Mob attacker) {

        Mob target = getCombat().getTarget();
        if (target != null && room != null) {
            Map<Mob, Long> last_attacked_map = getAttribOr(AttributeKey.LAST_ATTACKED_MAP, new HashMap<>());
            List<Mob> invalid = new ArrayList<>();

            // Identify when our current focused target attacked us.
            long[] last_time = new long[1];

            // Identify invalid entries and our current targets last attack time
            if (last_attacked_map.size() > 0) {
                last_attacked_map.forEach((p, t) -> {
                    if (target == p) // Our current target hasn't attacked for 10s. Fuck that guy, change!
                        last_time[0] = t;
                    if (!room.contains(p)) {
                        invalid.add(p);
                    }
                    //System.out.println(p.index()+" vs "+target.index());
                });
            }
            // Remove invalid entries
            invalid.forEach(last_attacked_map::remove);
            invalid.clear();

            // 0L = never attacked in the first place. otherwise 10s check
            if (last_time[0] == 0L || System.currentTimeMillis() - last_time[0] >= 8000) {
                if (last_attacked_map.size() > 0) {
                    // Retaliate to a random person who has recently attacked us in this room.
                    super.autoRetaliate(last_attacked_map.keySet().toArray(new Mob[0])[Utils.random(last_attacked_map.size() - 1)]);
                } else {
                    // Fall back to whoever actually hit us
                    super.autoRetaliate(attacker);
                }
                return true;
            }
        }
        return false;
    }

    public void cloneDamage(Npc npc) {
        this.getCombat().setDamageMap(npc.getCombat().getDamageMap());
    }


    static final int[] PERMANENT_MOVEMENT_BLOCKED = {
        NpcIdentifiers.VORKATH_8061, NpcIdentifiers.PORTAL_1747, NpcIdentifiers.PORTAL_1748, NpcIdentifiers.PORTAL_1749, NpcIdentifiers.PORTAL_1750, NpcIdentifiers.VOID_KNIGHT_2950, NpcIdentifiers.VOID_KNIGHT_2951, NpcIdentifiers.VOID_KNIGHT_2952
    };

    public boolean permaBlockedMovement() {
        return Arrays.stream(PERMANENT_MOVEMENT_BLOCKED).anyMatch(n -> this.id == n);
    }

    @Override
    public String toString() {
        return MessageFormat.format("Npc'{'name={0},spawnStack=''{1}'', id={2}, spawnTile={3}, walkRadius={4}, spawnDirection={5}, inViewport={6}, def={7}, hp={8}, combatInfo={9}, hidden={10}, respawns={11}, venomImmune={12}, poisonImmune={13}, spawnArea={14}, movementCoordinator={15}, combatMethod={16}, immunity={17}, transmog={18} lock: {19} idx:{20} '}'", getMobName(), spawnStack, id, spawnTile, walkRadius, spawnDirection, inViewport, def == null ? "?" : "def", hp, combatInfo == null ? "?" : "ci", hidden, respawns, venomImmune, poisonImmune, spawnArea, movementCoordinator == null ? "?" : "mc", combatMethod, immunity, transmog, lockState(), getIndex());
    }

    @Override
    public int yLength() {
        return def().size;
    }

    @Override
    public int xLength() {
        return def().size;
    }

    @Override
    public Tile getCentrePosition() {
        Tile base = this.tile();

        if (this.getSize() > 1) {
            base = this.tile().transform(this.getSize() / 2, this.getSize() / 2, 0);
        }
        return base;
    }

    @Override
    public int getProjectileLockonIndex() {
        return getIndex() + 1;
    }

    @Override
    public void onAdd() {
        setNeedsPlacement(true);
        Tile.occupy(this);
    }

    @Override
    public void onRemove() {
        TaskManager.cancelTasks(this);
    }

    @Override
    public Hit manipulateHit(Hit hit) {
        return hit;
    }

    @Override
    public void die() {
        NpcDeath.execute(this);
    }

    @Override
    public int hp() {
        return hp;
    }

    @Override
    public void hp(int hp, int exceed) {
        this.hp = Math.min(maxHp() + exceed, hp);
    }

    @Override
    public int maxHp() {
        return combatInfo == null ? 50 : combatInfo.stats.hitpoints;
    }

    @Override
    public Npc setHitpoints(int hitpoints) {
        if (isCombatDummy()) {
            if (combatInfo.stats.hitpoints > hitpoints) {
                return this;
            }
        }
        this.hp = hitpoints;
        if (this.hp <= 0 && getCombatMethod() != null && getCombatMethod().customOnDeath(this)) {
            // Set local hp variable to 1 to prevent it from reaching statement below.
            this.hp = 1;
        }

        if (this.hp <= 0)
            die();
        return this;
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public boolean isNpc() {
        return true;
    }

    @Override
    public boolean dead() {
        return hp == 0;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Npc && ((Npc) other).getIndex() == getIndex() && ((Npc) other).id() == id();
    }

    @Override
    public int getSize() {
        return def == null ? 1 :
            Math.max(1, def.size);
    }

    @Override
    public int getBaseAttackSpeed() {
        return combatInfo != null ? combatInfo.attackspeed : 4;
    }

    @Override
    public int attackAnimation() {
        if (combatInfo != null && combatInfo.animations != null) {
            return combatInfo.animations.attack;
        }

        return 422;
    }

    @Override
    public int getBlockAnim() {
        if (combatInfo != null && combatInfo.animations != null) {
            return combatInfo.animations.block;
        }

        return -1;//TODO default
    }

    @Override
    public void autoRetaliate(Mob attacker) {

        // If the bosses' current target has not attacked us back for at least 10, we change target to whoever attacked us last.
        if ((id == 2215 && target_fleeing(Graardor.getBandosArea(), attacker))
            || (id == 3162 && target_fleeing(KreeArra.getENCAMPMENT(), attacker))
            || (id == 2205 && target_fleeing(Zilyana.getENCAMPMENT(), attacker))
            || (id == 3129 && target_fleeing(Kril.getENCAMPMENT(), attacker))
            || (id == 7709)
            || (id == 7710)
            || (id == 7707) || Zulrah.is(this)) {
            return;
        }
        if (def != null && combatInfo != null && !combatInfo.retaliates) {
            //System.out.println("STOP AUTORETALIATE");
            return;
        }
        if (movementCoordinator.getCoordinateState() == CoordinateState.RETREATING) {
            // dont fight back until we're back at spawn location.
            return;
        }
        super.autoRetaliate(attacker);
    }

    public Pet petType() {
        return this.getAttribOr(AttributeKey.PET_TYPE, null);
    }

    public Tile walkTo;
    public Predicate<Tile> skipReachCheck;

    public void performGreatOlmAttack(Party party) {
        if (party.getCurrentPhase() == 3) {
            if (tile().getX() >= 3238) {
                if (spawnDirection == Direction.SOUTH.toInteger())
                    party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getGreatOlmObject(), 7372));
                else if (spawnDirection == Direction.NORTH.toInteger())
                    party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getGreatOlmObject(), 7373));
                else if (spawnDirection == Direction.NONE.toInteger())
                    party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getGreatOlmObject(), 7371));
            } else {
                if (spawnDirection == Direction.SOUTH.toInteger())
                    party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getGreatOlmObject(), 7373));
                else if (spawnDirection == Direction.NORTH.toInteger())
                    party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getGreatOlmObject(), 7372));
                else if (spawnDirection == Direction.NONE.toInteger())
                    party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getGreatOlmObject(), 7371));
            }
        } else {
            if (tile().getX() >= 3238) {
                if (spawnDirection == Direction.SOUTH.toInteger())
                    party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getGreatOlmObject(), 7347));
                else if (spawnDirection == Direction.NORTH.toInteger())
                    party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getGreatOlmObject(), 7346));
                else if (spawnDirection == Direction.NONE.toInteger())
                    party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getGreatOlmObject(), 7345));
            } else {
                if (spawnDirection == Direction.SOUTH.toInteger())
                    party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getGreatOlmObject(), 7346));
                else if (spawnDirection == Direction.NORTH.toInteger())
                    party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getGreatOlmObject(), 7347));
                else if (spawnDirection == Direction.NONE.toInteger())
                    party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getGreatOlmObject(), 7345));
            }
        }
    }

    public void remove() {
        World.getWorld().unregisterNpc(this);
    }
}
