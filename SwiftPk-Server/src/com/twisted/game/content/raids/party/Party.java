package com.twisted.game.content.raids.party;

import com.twisted.game.content.raids.Raids;
import com.twisted.game.content.raids.RaidsType;
import com.twisted.game.content.raids.chamber_of_xeric.ChamberOfXerics;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.impl.COXArea;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.Consumer;

import static com.twisted.game.world.entity.AttributeKey.PERSONAL_POINTS;
import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen | April, 26, 2021, 16:56
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class Party {

    public static final List<Item> COX_REWARDS = Arrays.asList(new Item(DEXTEROUS_PRAYER_SCROLL), new Item(ARCANE_PRAYER_SCROLL), new Item(TWISTED_BUCKLER), new Item(DRAGON_HUNTER_CROSSBOW), new Item(DINHS_BULWARK), new Item(ANCESTRAL_HAT), new Item(ANCESTRAL_ROBE_TOP), new Item(ANCESTRAL_ROBE_BOTTOM), new Item(ELDER_MAUL), new Item(KODAI_WAND), new Item(TWISTED_BOW), new Item(SANGUINE_ORNAMENT_KIT));
    public static final List<Item> TOB_REWARDS = Arrays.asList(new Item(AVERNIC_DEFENDER), new Item(GHRAZI_RAPIER), new Item(SANGUINESTI_STAFF), new Item(JUSTICIAR_FACEGUARD), new Item(JUSTICIAR_CHESTGUARD), new Item(JUSTICIAR_LEGGUARDS), new Item(SCYTHE_OF_VITUR), new Item(SANGUINE_DUST), new Item(HOLY_ORNAMENT_KIT), new Item(SANGUINE_ORNAMENT_KIT));
    public static final List<Item> HP_REWARDS = Arrays.asList(new Item(NAGINI), new Item(TOM_RIDDLE_DIARY), new Item(MARVOLO_GAUNTS_RING), new Item(CLOAK_OF_INVISIBILITY), new Item(ELDER_WAND_HANDLE), new Item(ELDER_WAND_STICK), new Item(SWORD_OF_GRYFFINDOR), new Item(TALONHAWK_CROSSBOW), new Item(SALAZAR_SLYTHERINS_LOCKET));

    public static final int REWARDS_CONTAINER_ID = 12137;
    public static final int TOTAL_POINTS = 12003;
    public static final int NAME_FRAME = 12004;
    public static final int POINTS = 12005;
    public static final int COS_CONFIG_ID = 1123;
    public static final int TOB_CONFIG_ID = 1124;
    public static final int HP_CONFIG_ID = 1125;
    private static final int PARTY_INTERFACE = 12100;
    private static final int LEADER_FRAME = 12117;

    @Setter
    @Getter
    private Player leader;
    @Getter
    private final List<Player> members;
    public ArrayList<Npc> monsters = new ArrayList<>();
    public ArrayList<GameObject> objects = new ArrayList<>();

    @Setter
    private RaidsType raidsSelected = RaidsType.CHAMBER_OF_XERICS;
    @Getter
    @Setter
    private int height;
    @Getter
    @Setter
    private int kills;
    @Getter
    @Setter
    private int raidStage = 0;
    private boolean bossFightStarted;

    @Setter
    @Getter
    private int partySize;

    public Party(Player leader) {
        this.leader = leader;
        members = new ArrayList<>();
        members.add(leader);
    }

    public int getSize() {
        return members.size();
    }

    public void addMember(Player player) {
        if (player == null)
            return;

        members.add(player);
    }

    public boolean bossFightStarted() {
        return bossFightStarted;
    }

    public void bossFightStarted(boolean bossFightStarted) {
        this.bossFightStarted = bossFightStarted;
    }

    @Setter
    @Getter
    private Npc mommaMuttadile;

    @Setter
    @Getter
    private GameObject meatTree;

    public void removeMember(Player player) {
        if (player == null)
            return;

        Iterator<Player> iterator = members.iterator();
        while (iterator.hasNext()) {
            Player member = iterator.next();
            if (member == null) {
                continue;
            }

            if (member == player) {
                iterator.remove();
                if (member == leader && !members.isEmpty()) {
                    leader = members.getFirst();
                }
                break;
            }
        }
    }

    public Player randomPartyPlayer() {
        int index = World.getWorld().random(this.getMembers().size() - 1);
        if (index == 0) {
            return this.getMembers().getFirst();
        }

        return this.getMembers().get(index);
    }

    public void forPlayers(Consumer<Player> action) {
        for (final Player player : members) {
            if (player == null || !player.isRegistered())
                continue;

            action.accept(player);
        }
    }

    public void addPersonalPoints(Player player, int points) {//s1update
        boolean centaurPet = player.hasPetOut("Centaur");
        boolean olmletPet = player.hasPetOut("Olmlet");
        boolean eliteMember = player.getMemberRights().isEmeraldOrGreater(player);
        boolean extremeMember = player.getMemberRights().isDiamondOrGreater(player);
        boolean LegendaryMember = player.getMemberRights().isDragonstoneOrGreater(player);
        boolean vipMember = player.getMemberRights().isOnyxOrGreater(player);
        boolean sponsorMember = player.getMemberRights().isZenyteOrGreater(player);
        boolean lilZikPet = player.hasPetOut("Lil' Zik");
        boolean lilMaidenPet = player.hasPetOut("Lil' Maiden");
        boolean lilBloatPet = player.hasPetOut("Lil' Bloat");
        boolean lilNyloPet = player.hasPetOut("Lil' Nylo");
        boolean lilSotPet = player.hasPetOut("Lil' Sot");
        boolean lilXarpPet = player.hasPetOut("Lil' Xarp");
        boolean tobPet = lilZikPet
            || lilMaidenPet || lilBloatPet || lilNyloPet || lilSotPet || lilXarpPet;
        var percentageBoost = 0;
        if (centaurPet || olmletPet) {
            percentageBoost += 10;
        }
        if (tobPet) {
            percentageBoost += 10;
        }

        if (eliteMember) {
            percentageBoost += 5;
        } else if (extremeMember) {
            percentageBoost += 10;
        } else if (LegendaryMember) {
            percentageBoost += 15;
        } else if (vipMember) {
            percentageBoost += 20;
        } else if (sponsorMember) {
            percentageBoost += 25;
        }

        var extraPoints = points * percentageBoost / 100;
        points += extraPoints;
        var increaseBy = player.<Integer>getAttribOr(PERSONAL_POINTS, 0) + points;
        player.putAttrib(PERSONAL_POINTS, increaseBy);
    }

    public int totalPoints() {
        return members.stream().filter(Objects::nonNull).filter(Mob::isRegistered).mapToInt(m -> m.<Integer>getAttribOr(PERSONAL_POINTS, 0)).sum();
    }

    public void teamMessage(String message) {
        forPlayers(p -> p.message(message));
    }

    private static void clearInterface(Player player) {
        //Party members
        if (player == null)
            return;

        for (int i = 12117; i <= 12121; i++) {
            player.getPacketSender().sendString(i, "");
        }
    }

    public static void createParty(Player player) {
        if (player == null)
            return;

        player.raidsParty = new Party(player);
    }

    public static void openPartyInterface(Player player, boolean updateMembers) {
        if (player == null)
            return;

        clearInterface(player);//Clear previous frames

        //Default COX
        player.getPacketSender().sendConfig(COS_CONFIG_ID, 1).sendConfig(TOB_CONFIG_ID, 0).sendConfig(HP_CONFIG_ID, 0);
        player.getPacketSender().sendItemOnInterface(REWARDS_CONTAINER_ID, COX_REWARDS);

        //Set leader info
        Player partyLeader = player.raidsParty.getLeader();
        player.getPacketSender().sendString(12103, "Raiding party setup - " + partyLeader.getUsername() + "'s party");
        player.getPacketSender().sendString(LEADER_FRAME, "<col=ffffff>" + partyLeader.getUsername());

        //Set the raids we're going to enter, by default COX
        Party party = partyLeader.raidsParty;
        party.raidsSelected = RaidsType.CHAMBER_OF_XERICS;
        player.setRaids(new ChamberOfXerics());

        //Open interface
        player.getInterfaceManager().open(PARTY_INTERFACE);

        if (updateMembers)
            displayPartyMembers(player, player.raidsParty);
    }

    public static void displayPartyMembers(Player player, Party party) {
        if (player == null || party == null) {
            return;
        }

        if (!party.getMembers().isEmpty()) {
            for (int i = 0; i < party.getMembers().size(); i++) {
                final Player member = party.getMembers().get(i);
                if (member == null)
                    continue;

                if (member == party.getLeader())
                    continue;

                player.getPacketSender().sendString(LEADER_FRAME + i, "<col=9f9f9f>" + member.getUsername());
            }
        }
    }

    public static void refreshInterface(Player leader, Party party) {
        if (party == null) {
            return;
        }

        for (final Player partyMembers : party.getMembers()) {
            if (partyMembers == null)
                continue;

            //Clear ghost entries
            for (int i = 0; i < 4; i++) {
                partyMembers.getPacketSender().sendString(LEADER_FRAME + i, "");
                leader.getPacketSender().sendString(LEADER_FRAME + i, "");
            }

            //Shift party members
            for (int i = 0; i < party.getMembers().size(); i++) {
                if (leader.raidsParty != null) {
                    partyMembers.getPacketSender().sendString(LEADER_FRAME + i, "<col=9f9f9f>" + leader.raidsParty.getMembers().get(i).getUsername());
                    leader.getPacketSender().sendString(LEADER_FRAME + i, "<col=9f9f9f>" + leader.raidsParty.getMembers().get(i).getUsername());
                    partyMembers.getPacketSender().sendConfig(COS_CONFIG_ID, leader.raidsParty.raidsSelected == RaidsType.CHAMBER_OF_XERICS ? 1 : 0).sendConfig(TOB_CONFIG_ID, leader.raidsParty.raidsSelected == RaidsType.THEATRE_OF_BLOOD ? 1 : 0).sendConfig(HP_CONFIG_ID, leader.raidsParty.raidsSelected == RaidsType.CHAMBER_OF_SECRETS ? 1 : 0);
                    partyMembers.getPacketSender().sendItemOnInterface(REWARDS_CONTAINER_ID, leader.raidsParty.raidsSelected == RaidsType.CHAMBER_OF_XERICS ? COX_REWARDS : leader.raidsParty.raidsSelected == RaidsType.THEATRE_OF_BLOOD ? TOB_REWARDS : HP_REWARDS);
                }
            }
        }
    }

    public static void kick(Player player, int index) {
        if (player == null)
            return;

        Party party = player.raidsParty;
        if (party == null) {
            return;
        }

        //There has to be a party member
        if (party.getMembers().size() < index + 1) {
            player.message("There is no member to kick.");
            return;
        }

        //We have at least 2 party members (leader included), check if we are the leader.
        if (party.getLeader() != player) {
            player.message("Only the leader of this party can kick members.");
            return;
        }

        //We are the leader lets continue and grab the party member in the list.
        final Player partyMember = party.getMembers().get(index);
        if (partyMember == null)
            return;

        party.removeMember(partyMember);
        partyMember.getInterfaceManager().close();
        player.message(partyMember.getUsername() + " has been successfully kicked from the party.");
        partyMember.message("You have been kicked out the raids party.");
        refreshInterface(party.getLeader(), party);
    }

    public static void leaveParty(Player player, boolean destroyFromBoard) {
        if (player == null)
            return;

        final Party party = player.raidsParty;
        if (party == null) {
            return;
        }

        final Player partyLeader = party.getLeader();
        if (partyLeader == player) {
            disbandParty(player, destroyFromBoard);
            return;
        }

        player.message("<col=ef20ff>You leave " + player.raidsParty.getLeader().getUsername() + "'s party.");
        party.removeMember(player);
        player.raidsParty = null;
        refreshInterface(partyLeader, partyLeader.raidsParty);
        player.getInterfaceManager().close();
    }

    public static void disbandParty(Player player, boolean destroyFromBoard) {
        /* terminate the party */
        if (player == null)
            return;

        final var raidsParty = player.raidsParty;
        if (raidsParty == null)
            return;

        if (raidsParty.getLeader() != player)
            return;


        for (Player member : player.raidsParty.getMembers()) {
            if (member == null)
                continue;

            member.message("<col=ef20ff>" + player.getUsername() + " has disbanded the party.");
            member.raidsParty = null;

            if (!destroyFromBoard) {
                if (member.getRaids() != null) {
                    member.getRaids().exit(member);
                }
            } else {
                member.getInterfaceManager().close();
            }
        }

        raidsParty.members.clear();
    }

    public static void onLogout(Player player) {
        try {
            if (player == null)
                return;

            if (player.raidsParty == null)
                return;

            leaveParty(player, false);
            player.teleport(1245, 3561, 0);

            final Raids raids = player.getRaids();

            if (raids == null)
                return;

            raids.exit(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startRaid(Player p) {
        Party party = p.raidsParty;
        if (party.getLeader() != p) {
            p.message("Only the party leader can start the fight.");
            return;
        }

        p.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                Party party = p.raidsParty;
                if (party != null) {
                    for (Player member : party.getMembers()) {
                        if (member == null)
                            continue;

                        if (member.getInventory().contains(30098) || member.getInventory().contains(30099)) {
                            // Member has Looting bag(i), display message and stop the dialogue
                            member.message("Sorry, you can't bring the Looting bag(i) into a raid.");
                            stop();
                            return;
                        }
                    }
                }

                send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Start raid.", "Nevermind.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if (isPhase(0)) {
                    if (option == 1) {
                        Party party = p.raidsParty;

                        if (party == null) {
                            stop();
                            return;
                        }

                        Raids ownerRaids = p.getRaids();
                        if (ownerRaids == null) {
                            stop();
                            return;
                        }

                        ownerRaids.startup(p);
                        p.getInterfaceManager().close();
                        stop();
                    } else if (option == 2) {
                        stop();
                    }
                }
            }
        });
    }

    public int getPlayersInRaidsLocation(Party party) {
        int inRaids = 0;
        if (party == null) {
            return 0;
        }
        for (Player member : party.getMembers()) {
            if (member == null)
                continue;

            if (member.getController() != null && member.getController() instanceof COXArea) {
                inRaids++;
            }
        }
        return inRaids;
    }

    private Tile greatolmTile = new Tile(3238, 5738, height);
    @Getter
    @Setter
    private Tile leftHandTile = new Tile(3238, 5733, height);
    @Getter
    @Setter
    private Tile rightHandTile = new Tile(3238, 5743, height);

    @Setter
    @Getter
    Npc leftHandNpc;
    Npc greatolmNpc;
    @Setter
    @Getter
    Npc rightHandNpc;

    GameObject greatolmObject = new GameObject(29881, greatolmTile, 10, 1);
    @Setter
    @Getter
    GameObject leftHandObject = new GameObject(29884, leftHandTile, 10, 1);
    @Setter
    @Getter
    GameObject rightHandObject = new GameObject(29887, rightHandTile, 10, 1);

    @Setter
    @Getter
    private boolean canAttackLeftHand;
    @Setter
    @Getter
    private boolean transitionPhase;

    @Setter
    @Getter
    private int currentPhase = 0;

    @Setter
    private int phaseAmount;
    @Setter
    private boolean olmTurning;
    @Setter
    @Getter
    private boolean clenchedHand;

    @Getter
    private final Tile[] crystalBursts = new Tile[5];
    @Getter
    private final Tile[] lightningSpots = new Tile[5];

    @Setter
    @Getter
    private int crystalAmount;

    @Setter
    @Getter
    private int olmAttackTimer;
    @Setter
    @Getter
    private int leftHandAttackTimer;
    @Setter
    private boolean canAttack;
    @Setter
    @Getter
    private boolean canAttackHand;
    @Setter
    @Getter
    private Tile swapTile;

    @Getter
    private final ArrayList<Player> swapPlayers = new ArrayList<>();

    @Setter
    @Getter
    private boolean leftHandDead;
    @Setter
    @Getter
    private boolean rightHandDead;

    @Setter
    @Getter
    private int graphicSwap;
    @Setter
    @Getter
    private boolean lonePair;

    @Setter
    @Getter
    private int attackCount;

    @Setter
    @Getter
    private boolean leftHandProtected;

    @Setter
    @Getter
    private int currentLeftHandCycle;
    @Setter
    private boolean healingOlmLeftHand;

    @Setter
    @Getter
    private Player fireWallPlayer;
    @Getter
    private final ArrayList<Npc> fireWallNpcs = new ArrayList<>();

    @Setter
    @Getter
    private Npc fireWallSpawn;
    @Setter
    @Getter
    private Npc fireWallSpawn1;

    @Getter
    private ArrayList<String> phaseAttack = new ArrayList<>();

    @Setter
    @Getter
    private boolean lastPhaseStarted;

    @Setter
    @Getter
    private boolean clenchedHandFirst;
    @Setter
    @Getter
    private boolean clenchedHandSecond;

    @Getter
    @Setter
    private boolean unClenchedHandFirst;

    @Getter
    @Setter
    private boolean unClenchedHandSecond;

    @Getter
    @Setter
    private boolean switchingPhases;
    @Getter
    @Setter
    private boolean olmAttacking;
    @Setter
    @Getter
    private boolean switchAfterAttack;

    @Getter
    private final ArrayList<Player> burnPlayers = new ArrayList<>();

    public boolean getCanAttack() {
        return canAttack;
    }

    public boolean getOlmTurning() {
        return olmTurning;
    }

    @Setter
    @Getter
    private ArrayList<Player> playersToAttack = new ArrayList<>();

    public int getPhaseAmount() {
        return phaseAmount;
    }

    public GameObject getGreatOlmObject() {
        return greatolmObject;
    }

    public void setGreatOlmObject(GameObject greatolmObject) {
        this.greatolmObject = greatolmObject;
    }

    public Tile getGreatOlmTile() {
        return greatolmTile;
    }

    public void setGreatOlmTile(Tile GreatOlmTile) {
        greatolmTile = GreatOlmTile;
    }

    public Npc getGreatOlmNpc() {
        return greatolmNpc;
    }

    public void setGreatOlmNpc(Npc greatolmNpc) {
        this.greatolmNpc = greatolmNpc;
    }

}
