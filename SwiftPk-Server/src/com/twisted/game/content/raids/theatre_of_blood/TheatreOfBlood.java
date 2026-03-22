package com.twisted.game.content.raids.theatre_of_blood;

import com.twisted.game.content.achievements.AchievementsManager;
import com.twisted.game.content.daily_tasks.DailyTaskManager;
import com.twisted.game.content.daily_tasks.DailyTasks;
import com.google.common.collect.Lists;

import com.twisted.game.content.mechanics.Poison;
import com.twisted.game.content.raids.Raids;
import com.twisted.game.content.raids.RaidsNpc;
import com.twisted.game.content.achievements.Achievements;
import com.twisted.game.content.raids.party.Party;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.Venom;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.ObjectManager;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Color;

import static com.twisted.game.world.entity.AttributeKey.PERSONAL_POINTS;
import static com.twisted.game.world.entity.AttributeKey.THEATRE_OF_BLOOD_POINTS;
import static com.twisted.util.ItemIdentifiers.DAWNBRINGER;
import static com.twisted.util.NpcIdentifiers.*;
import static com.twisted.util.NpcIdentifiers.THE_MAIDEN_OF_SUGADINTI;
import static com.twisted.util.ObjectIdentifiers.*;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since January 07, 2022
 */
public class TheatreOfBlood extends Raids {

    @Override
    public void startup(Player player) {
        Party party = player.raidsParty;
        if (party == null) return;
        party.setRaidStage(1);
        party.setPartySize(party.getSize());
        final int height = party.getLeader().getIndex() * 4;

        for (Player member : party.getMembers()) {
            member.setRaids(this);
            member.teleport(new Tile((3218 + World.getWorld().random(0, 3)), (4459 + World.getWorld().random(0, 1)), height));
            party.setHeight(height);
        }

        //Clear kills
        party.setKills(0);

        //Clear npcs that somehow survived first
        clearParty(player);

        //Spawn all monsters
        spawnMonsters(player);

        //Spawn all resources chests
        spawnChests(player);
    }

    @Override
    public void exit(Player player) {
        player.setRaids(null);

        Party party = player.raidsParty;

        //Remove players from the party if they are not the leader
        if (party != null) {
            int partySize = party.getPartySize();

            party.setPartySize(partySize - 1);
            partySize--;
            //Last player in the party leaves clear the whole thing
            if (partySize == 0) {
                //Clear all party members that are left
                Lists.newArrayList(party.getMembers()).clear();
                clearParty(player);
            }
            player.raidsParty = null;
        }

        //Reset points
        player.putAttrib(PERSONAL_POINTS, 0);
        player.putAttrib(THEATRE_OF_BLOOD_POINTS, 0);
        player.message("<col=" + Color.BLUE.getColorValue() + ">You have restored your hitpoints, run energy and prayer.");
        player.message("<col=" + Color.HOTPINK.getColorValue() + ">You've also been cured of poison and venom.");
        player.skills().resetStats();
        int increase = player.getEquipment().hpIncrease();
        player.hp(Math.max(increase > 0 ? player.skills().level(Skills.HITPOINTS) + increase : player.skills().level(Skills.HITPOINTS), player.skills().xpLevel(Skills.HITPOINTS)), 39); //Set hitpoints to 100%
        player.skills().replenishSkill(5, player.skills().xpLevel(5)); //Set the players prayer level to full
        player.setRunningEnergy(100.0, true);
        Poison.cure(player);
        Venom.cure(2, player);

        //Clear any dawnbringers
        player.removeAll(new Item(DAWNBRINGER));

        //Move outside of raids
        player.teleport(1245, 3561, 0);
        player.getInterfaceManager().close(true);
    }

    @Override
    public void complete(Party party) {
        party.forPlayers(p -> {
            p.message(Color.RAID_PURPLE.wrap("Congratulations - your raid is complete!"));
            var completed = p.<Integer>getAttribOr(AttributeKey.THEATRE_OF_BLOOD_RUNS_COMPLETED, 0) + 1;
            p.putAttrib(AttributeKey.THEATRE_OF_BLOOD_RUNS_COMPLETED, completed);

            var bossPoints = p.<Integer>getAttribOr(AttributeKey.BOSS_POINTS, 0) + 100;
            p.putAttrib(AttributeKey.BOSS_POINTS, bossPoints);
            p.message(Color.RAID_PURPLE.wrap("You were awarded 100 boss points for completing the raid."));

            p.message(String.format("Total points: " + Color.RAID_PURPLE.wrap("%,d") + ", Personal points: " + Color.RAID_PURPLE.wrap("%,d") + " (" + Color.RAID_PURPLE.wrap("%.2f") + "%%)",
                party.totalPoints(), p.<Integer>getAttribOr(PERSONAL_POINTS, 0), (double) (p.<Integer>getAttribOr(PERSONAL_POINTS, 0) / party.totalPoints()) * 100));

            //Daily raids task
            DailyTaskManager.increase(DailyTasks.DAILY_RAIDS, p);
            AchievementsManager.activate(p, Achievements.RAIDS_GRIENDER_I, 1);

            //Roll a reward for each individual player
            TheatreOfBloodRewards.giveRewards(p);
        });
    }

    @Override
    public void clearParty(Player player) {
        Party party = player.raidsParty;

        if (party == null)
            return;

        if (party.monsters == null)
            return;


        for (final Npc npc : Lists.newArrayList(party.monsters.iterator())) {
            if (npc.isRegistered() || !npc.dead()) {
                World.getWorld().unregisterNpc(npc);
            }
        }
        party.monsters.clear();

        if (party.objects == null) {
            return;
        }
        for (GameObject obj : party.objects) {
            ObjectManager.removeObj(obj);
        }
        party.objects.clear();

        player.putAttrib(PERSONAL_POINTS, 0);
    }

    @Override
    public boolean death(Player player) {
        Party party = player.raidsParty;

        if (party == null) {
            return false;
        }

        Tile deathTile = respawnTile(party, player.tile().level);
        player.teleport(deathTile.getX(), deathTile.getY(), party.getHeight());
        int pointsLost = (int) (player.<Integer>getAttribOr(PERSONAL_POINTS, 0) * 0.4);
        if (pointsLost > 0)
            addPoints(player, -pointsLost);

        //Make sure to heal
        player.healPlayer();
        return true;
    }

    @Override
    public Tile respawnTile(Party party, int level) {
        return switch (party.getRaidStage()) {
            case 1 ->//maiden
                new Tile(3190, 4446, level);
            case 2 ->//bloat
                new Tile(3309, 4447, level);
            case 3 ->//vasilias
                new Tile(3295, 4262, level);
            case 4 ->//sotetseg
                new Tile(3280, 4302, level);
            case 5 ->//xarpus
                new Tile(3170, 4377, level + 1);
            case 6 ->//verzik
                new Tile(3168, 4303, level);
            default -> throw new IllegalStateException("Unexpected value: " + party.getRaidStage());
        };
    }

    @Override
    public void addPoints(Player player, int points) {
        if (!raiding(player))
            return;

        player.raidsParty.addPersonalPoints(player, points);
    }

    @Override
    public void addDamagePoints(Player player, Npc target, int points) {
        if (!raiding(player))
            return;
        if (target.getAttribOr(AttributeKey.RAIDS_NO_POINTS, false))
            return;
        points *= 10;
        addPoints(player, points);
    }

    private void spawnMonsters(Player player) {

        Party party = player.raidsParty;

        Npc maiden = new RaidsNpc(THE_MAIDEN_OF_SUGADINTI, new Tile(3162, 4444, party.getHeight()), true).spawn(false);
        maiden.completelyLockedFromMoving(true);
        Npc bloat = new RaidsNpc(PESTILENT_BLOAT, new Tile(3299, 4440, party.getHeight()), true).spawn(false);
        bloat.noRetaliation(true);
        bloat.resetFaceTile();
        bloat.canAttack(false);
        bloat.cantMoveUnderCombat(true);
        Npc vasilias = new RaidsNpc(NYLOCAS_VASILIAS_8355, new Tile(3293, 4246, party.getHeight()), true).spawn(false);
        vasilias.completelyLockedFromMoving(true);
        Npc sotetseg = new RaidsNpc(SOTETSEG_8388, new Tile(3278, 4329, party.getHeight()), true).spawn(false);
        sotetseg.completelyLockedFromMoving(true);
        Npc xarpus = new RaidsNpc(XARPUS, new Tile(3169, 4386, party.getHeight() + 1), true).spawn(false);
        xarpus.completelyLockedFromMoving(true);
        xarpus.canAttack(false);
        Npc verzik = new RaidsNpc(VERZIK_VITUR_8369, new Tile(3166, 4323, party.getHeight()), true).spawn(false);
        verzik.cantMoveUnderCombat(true);


        //Add to list
        party.monsters.add(maiden);
        party.monsters.add(bloat);
        party.monsters.add(vasilias);
        party.monsters.add(sotetseg);
        party.monsters.add(xarpus);
        party.monsters.add(verzik);
    }

    public static final GameObject[] CHESTS = {
        new GameObject(MONUMENTAL_CHEST, new Tile(3226, 4323), 10, 3),
        new GameObject(MONUMENTAL_CHEST, new Tile(3226, 4327), 10, 3),
        new GameObject(MONUMENTAL_CHEST, new Tile(3233, 4330), 10, 0),
        new GameObject(MONUMENTAL_CHEST, new Tile(3240, 4327), 10, 1),
        new GameObject(MONUMENTAL_CHEST, new Tile(3240, 4323), 10, 1)
    };

    public static void spawnLootChests(Player player) {
        //Get the raids party
        Party party = player.raidsParty;

        if (party == null) {
            return;
        }

        for (int index = 0; index < party.getMembers().size(); index++) {
            Player partyMember = party.getMembers().get(index);
            GameObject chest = CHESTS[index];
            if (TheatreOfBloodRewards.containsRare(partyMember)) {
                chest = chest.withId(MONUMENTAL_CHEST_32991);
            }

            Tile tile = chest.tile().transform(0, 0, party.getHeight());
            GameObject chestObject = chest.withTile(tile);
            chestObject.spawn();
            party.objects.add(chestObject);
            partyMember.getPacketSender().sendPositionalHint(new Tile(tile.getX() + 1, tile.getY() + 1), 100);
        }
    }

    private void spawnChests(Player player) {
        //Get the raids party
        Party party = player.raidsParty;

        GameObject chest1 = new GameObject(CHEST_32758, new Tile(3175, 4422, party.getHeight()), 10, 2).spawn(); //maiden
        GameObject chest2 = new GameObject(CHEST_32758, new Tile(3303, 4277, party.getHeight()), 10, 5).spawn(); //Vasilias
        GameObject chest3 = new GameObject(CHEST_32758, new Tile(3278, 4293, party.getHeight()), 10, 2).spawn(); //sotetseg
        GameObject chest4 = new GameObject(CHEST_32758, new Tile(3171, 4399, party.getHeight() + 1), 10, 5).spawn(); //xarpus

        party.objects.add(chest1);
        party.objects.add(chest2);
        party.objects.add(chest3);
        party.objects.add(chest4);
    }
}
