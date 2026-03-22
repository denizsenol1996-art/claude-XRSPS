package com.twisted.game.content.raids.chamber_of_xeric;

import com.twisted.game.content.daily_tasks.DailyTaskManager;
import com.twisted.game.content.daily_tasks.DailyTasks;
import com.twisted.game.content.mechanics.Poison;
import com.twisted.game.content.raids.Raids;
import com.twisted.game.content.raids.RaidsNpc;
import com.twisted.game.content.raids.party.Party;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.Venom;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.ObjectManager;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Color;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import java.util.Objects;

import static com.twisted.game.content.raids.chamber_of_xeric.RaidNpcConstants.*;
import static com.twisted.game.world.entity.AttributeKey.*;
import static com.twisted.util.NpcIdentifiers.*;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 29, 2021
 */

@Slf4j
public class ChamberOfXerics extends Raids {

    @Override
    public void startup(Player player) {
        Party party = player.raidsParty;

        if (party == null)
            return;

        final int height = party.getLeader().getIndex() * 4;
        party.setHeight(height);
        party.setRaidStage(1);
        party.setKills(0);

        party.forPlayers(member -> {
            final Raids currentRaid = member.getRaids();

            this.clearParty(member);

            if (currentRaid != null) {
                member.setRaids(null);
            }

            member.setRaids(this);
            member.teleport(new Tile(3299, 5189, party.getHeight()));
            member.message("<col=ef20ff>The raid has begun!");
        });

        //Spawn all monsters
        spawnMonsters(player);
    }

    @Override
    public void exit(Player player) {
        final Party party = player.raidsParty;

        if (party == null) {
            log.info("Raid Party is null");
            return;
        }

        party.getMembers().removeIf(p -> {
            final boolean isPresent = p != null && p.equals(player);
            if (isPresent) {
                p.setRaidsParty(null);
                p.setRaids(null);
                p.putAttrib(GOT_RARE, false);
                p.putAttrib(PERSONAL_POINTS, 0);
                p.message("<col=" + Color.BLUE.getColorValue() + ">You have restored your hitpoints, run energy and prayer.");
                p.message("<col=" + Color.HOTPINK.getColorValue() + ">You've also been cured of poison and venom.");
                p.skills().resetStats();
                int increase = p.getEquipment().hpIncrease();
                p.hp(Math.max(increase > 0 ? p.skills().level(Skills.HITPOINTS) + increase : p.skills().level(Skills.HITPOINTS), p.skills().xpLevel(Skills.HITPOINTS)), 39); //Set hitpoints to 100%
                p.skills().replenishSkill(5, p.skills().xpLevel(5));
                p.setRunningEnergy(100.0, true);
                Poison.cure(p);
                Venom.cure(2, p);
                p.healPlayer();
                p.teleport(1245, 3561, 0);
                p.getInterfaceManager().close(true);
            }
            return isPresent;
        });
    }

    @Override
    public void complete(Party party) {
        party.forPlayers(p -> {
            p.message(Color.RAID_PURPLE.wrap("Congratulations - your raid is complete!"));
            var ticket = p.<Integer>getAttribOr(AttributeKey.TICKET_RUNS_CHALLENGE_COMPLETED, 0) + 1;
            var completed = p.<Integer>getAttribOr(AttributeKey.CHAMBER_OF_XERIC_RUNS_CHALLENGE_COMPLETED, 0) + 1;
            var bossPoint = p.<Integer>getAttribOr(BOSS_POINTS, 0) + 20;
            p.putAttrib(AttributeKey.CHAMBER_OF_XERIC_RUNS_CHALLENGE_COMPLETED, completed);
            p.putAttrib(AttributeKey.TICKET_RUNS_CHALLENGE_COMPLETED, ticket);
            p.message("@red@You received 20 boss points from Chambers of Xeric");
            p.putAttrib(BOSS_POINTS, bossPoint);
            p.message("You now have " + ticket + " ticket kc");
            p.message("@red@You now have " + ticket + " You need 300 to get raid kc every time to claim an raid rare ticket");
            p.message("@red@And this going be reset every 300 kc but not include raid kc when reset.");
            p.message(String.format("Total points: " + Color.RAID_PURPLE.wrap("%,d") + ", Personal points: " + Color.RAID_PURPLE.wrap("%,d") + " (" + Color.RAID_PURPLE.wrap("%.2f") + "%%)",
                party.totalPoints(), p.<Integer>getAttribOr(PERSONAL_POINTS, 0), (double) (p.<Integer>getAttribOr(PERSONAL_POINTS, 0) / party.totalPoints()) * 100));

            //Daily raids task
            DailyTaskManager.increase(DailyTasks.DAILY_RAIDS, p);

            //Roll a reward for each individual player
            ChamberOfXericReward.giveRewards(p);
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
                npc.remove();
            }
        }

        party.monsters.clear();
    }

    @Override
    public boolean death(Player player) {
        Party party = player.raidsParty;
        if (party == null) return false;
        player.teleport(respawnTile(party, player.tile().level));
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
            case 1 -> new Tile(3310, 5277, level);
            case 2 -> new Tile(3311, 5279, level);
            case 3 -> new Tile(3311, 5311, level);
            case 4 -> new Tile(3311, 5309, level);
            case 5 -> new Tile(3311, 5277, level);
            case 6 -> new Tile(3232, 5721, level);
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
    public void addDamagePoints(Player player, Npc target, int points) {//this is old point on damage check it
        if (!raiding(player))
            return;
        if (target.getAttribOr(AttributeKey.RAIDS_NO_POINTS, false))
            return;
        points *= 10;
        addPoints(player, points);
    }

    private void spawnMonsters(Player player) {
        Party party = player.raidsParty;

        final int partyHeight = party.getHeight();
        final int partySize = party.getSize();

        final GameObject meatTree = new GameObject(30013, new Tile(3301, 5320, partyHeight + 1));
        party.setMeatTree(meatTree);
        ObjectManager.addObj(meatTree);

        Npc[] NPCS =
            {
                new RaidsNpc(VASA_NISTIRIO, VASA_TILE.transform(0, 0, partyHeight), partySize),
                new RaidsNpc(VANGUARD_7527, VANGUARD_7527_TILE.transform(0, 0, partyHeight), partySize),
                new RaidsNpc(VANGUARD_7528, VANGUARD_7528_TILE.transform(0, 0, partyHeight), partySize),
                new RaidsNpc(VANGUARD_7529, VANGUARD_7529_TILE.transform(0, 0, partyHeight), partySize),
                new RaidsNpc(TEKTON_ENRAGED_7544, TEKTON_ENRAGED_TILE.transform(0, 0, partyHeight + 1), partySize),
                new RaidsNpc(MUTTADILE_7562, MUTTADILE_7562_TILE.transform(0, 0, partyHeight + 1), partySize),
                new RaidsNpc(MUTTADILE, MUTTADILE_TILE.transform(0, 0, partyHeight + 1), partySize),
                new RaidsNpc(VESPULA, VESPULA_TILE.transform(0, 0, partyHeight + 2), partySize)
            };

        boolean mommaMuttadile = false;
        for (final Npc npc : NPCS) {
            if (Objects.equals(npc.id(), MUTTADILE) && !mommaMuttadile) {
                party.setMommaMuttadile(npc);
                mommaMuttadile = true;
            }

            World.getWorld().registerNpc(npc);
            party.monsters.add(npc);
        }
    }
}
