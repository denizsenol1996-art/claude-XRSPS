package com.twisted.game.world.entity.combat.method.impl.npcs.slayer.kraken;

import com.twisted.fs.NpcDefinition;
import com.twisted.game.content.instance.impl.KrakenInstance;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueManager;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.npc.NpcDeath;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;
import com.twisted.util.chainedwork.Chain;
import com.twisted.util.timers.TimerKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KrakenBoss {

    // Npc ids
    public static final int KRAKEN_WHIRLPOOL = 496;
    public static final int TENTACLE_WHIRLPOOL = 5534;

    public static final int KRAKEN_NPCID = 494;
    public static final int TENTACLE_NPCID = 5535;

    // Object id
    private static final int CREVICE = 537;

    // Relevent tiles
    private static final Tile ENTER_TILE = new Tile(2280, 10022);
    private static final Tile BOSS_TILE = new Tile(2278, 10034);

    // Going from bottom left, top left, top right, bottom right
    public static final List<Tile> TENT_TILES = Arrays.asList(new Tile(2276, 10033), new Tile(2276, 10037), new Tile(2283, 10037), new Tile(2283, 10033));
    private static final Area REAL_ROOM_AREA = new Area(new Tile(2269, 10023), new Tile(2302, 10046));

    // Obj id
    private static final int EXIT_CREVICE = 538;

    //Leave cove obj
    private static final int CAVE_EXIT = 30178;

    // Tile to teleport to when you leave
    private static final Tile ROOM_EXIT = new Tile(2280, 10016);

    // Corner of the region we are going to create an Instance of
    private static final Tile CORNER = new Tile(2240, 9984);

    public static boolean onObject(Player player, GameObject obj, int opt) {
        // Enter crevice
        if (obj.getId() == CREVICE) {
            switch (opt) {
                case 1 -> {// Enter
                    if (CombatFactory.inCombat(player)) {
                        DialogueManager.sendStatement(player, "You can't go in here when under attack.");
                        player.message("You can't go in here when under attack.");
                    } else {
                        player.teleport(ENTER_TILE);
                    }
                }
                case 2 -> {// Private, instanced
                    player.getDialogueManager().start(new KrakenInstanceD());
                }

                case 3 -> {// Look inside
                    int count = 0;
                    for (Player p : World.getWorld().getPlayers()) {
                        if (p != null && p.tile().inArea(REAL_ROOM_AREA))
                            count++;
                        String strEnd = count == 1 ? "" : "s";
                        String isAre = count == 1 ? "is" : "are";
                        DialogueManager.sendStatement(player, "There " + isAre + " currently " + count + " player" + strEnd + " in the cave.");
                    }
                }
            }

            return true;
        }

        //Leaving cove.
        if (obj.getId() == CAVE_EXIT) {
            player.teleport(3088, 3505);
            return true;
        }

        // Leaving
        if (obj.getId() == EXIT_CREVICE) {
            KrakenInstance krakenInstance = player.getKrakenInstance();
            //Check if instance is active
            if (krakenInstance != null) {
                player.getDialogueManager().start(new Dialogue() {
                    @Override
                    protected void start(Object... parameters) {
                        send(DialogueType.OPTION, "Leave the instance? You cannot return.", "Yes, I want to leave.", "No, I'm staying for now.");
                    }

                    @Override
                    protected void select(int option) {
                        if (option == 1) {
                            player.teleport(ROOM_EXIT);
                        } else if (option == 2) {
                            stop();
                        }
                    }
                });
            } else {
                player.teleport(ROOM_EXIT);
            }
        }
        return false;
    }

    // Spawn hook
    public static void onNpcSpawn(Npc kraken) {
        if (kraken.id() == KRAKEN_WHIRLPOOL) {

            kraken.combatInfo().respawntime = 9;

            // Is it spawned in an Instance?
            //TODO?

            // Must be spawned into normal world
            for (Tile tile : TENT_TILES) {
                Npc tentacle = new Npc(TENTACLE_WHIRLPOOL, tile).spawnDirection(6);
                World.getWorld().registerNpc(tentacle);
                tentacle.putAttrib(AttributeKey.BOSS_OWNER, kraken);

                var list = kraken.<ArrayList<Npc>>getAttribOr(AttributeKey.MINION_LIST, new ArrayList<Npc>());
                list.add(tentacle);
                kraken.putAttrib(AttributeKey.MINION_LIST, list);
            }
        }
    }

    public static void onHit(Player player, Npc npc) {
        var minions = npc.<ArrayList<Npc>>getAttribOr(AttributeKey.MINION_LIST, new ArrayList<Npc>());
        // Have all minions been attacked first?
        var awake = 0;
        for (Npc minion : minions) {
            if (minion.transmog() == KrakenBoss.TENTACLE_NPCID) {
                awake++;
            } else {
                //System.out.println("minion not awake: "+minion.id()+" "+minion.transmog());
            }
        }

        //System.out.println("awake: "+awake);

        if(awake != 4) {
            var amt = "";

            switch (awake) {
                case 1 -> amt = "other three tentacles";
                case 2 -> amt = "other two tentacles";
                case 3 -> amt = "last tentacle";
                default -> amt = "four tentacles";
            }
            player.message("The "+amt+" need to be disturbed before the Kraken emerges.");
        } else {
            // Do transform and retaliate
            npc.getTimerRepository().addOrSet(TimerKey.COMBAT_ATTACK, 1);
            npc.getCombat().attack(player);

            npc.transmog(KRAKEN_NPCID);
            npc.def(World.getWorld().definitions().get(NpcDefinition.class, KRAKEN_NPCID));
            npc.combatInfo(World.getWorld().combatInfo(KRAKEN_NPCID)); // Quickly replace scripts for retaliation before Java finishes processing.
            npc.setCombatMethod(World.getWorld().combatInfo(KRAKEN_NPCID).scripts.newCombatInstance());
            npc.animate(7135);
        }
    }

    public static void onDeath(Npc npc) {
        // Then do the death anim on all tentacles
        List<Npc> minions = npc.<ArrayList<Npc>>getAttribOr(AttributeKey.MINION_LIST, new ArrayList<Npc>());
        for (Npc minion : minions) {
            if (!minion.hidden()) { // Already dead maybe from recoil/venom
                minion.stopActions(false);
                minion.getCombat().reset();
                NpcDeath.deathReset(minion);
                minion.animate(minion.combatInfo().animations.death);
            }
        }

        // Wait 2 for tents to die
        Chain.bound(null).runFn(2, () -> {
            // Hide them
            for (Npc minion : minions) {
                minion.hidden(true);
                minion.transmog(TENTACLE_WHIRLPOOL); // Set it back to the whirlpool
                minion.combatInfo(World.getWorld().combatInfo(TENTACLE_WHIRLPOOL));
                minion.hp(minion.maxHp(), 0);
            }
        }).then(11, () -> {
            // Wait some seconds - during this time the default NpcDeath script will respawn the Kraken boss

            // Respawn the minions
            for (Npc m : minions) {
                NpcDeath.respawn(m);
            }
        });
    }

}
