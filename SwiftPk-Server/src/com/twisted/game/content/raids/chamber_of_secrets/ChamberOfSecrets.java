package com.twisted.game.content.raids.chamber_of_secrets;

import com.twisted.game.content.daily_tasks.DailyTaskManager;
import com.twisted.game.content.daily_tasks.DailyTasks;
import com.twisted.game.content.mechanics.Poison;
import com.twisted.game.content.raids.RaidsNpc;
import com.twisted.game.content.raids.Raids;
import com.twisted.game.content.raids.party.Party;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.Venom;
import com.twisted.game.world.entity.combat.magic.Autocasting;
import com.twisted.game.world.entity.combat.magic.Spell;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Color;
import org.apache.commons.compress.utils.Lists;

import static com.twisted.game.world.entity.AttributeKey.PERSONAL_POINTS;
import static com.twisted.util.CustomItemIdentifiers.ELDER_WAND_RAIDS;

/**
 * @author Patrick van Elderen | April, 26, 2021, 16:58
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class ChamberOfSecrets extends Raids {

    public final static int MALE_CENTAUR = 15030;
    public final static int FEMALE_CENTAUR = 15032;
    public final static int DEMENTOR = 15028;
    public final static int ARAGOG = 15020;
    public final static int ARAGOG_MINION = 134;
    public final static int FLUFFY = 15026;
    public final static int HUNGARIAN_HORNTAIL = 15034;
    public final static int FENRIR_GREYBACK = 15050;
    public final static int LORD_VOLDEMORT = 3443;

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
            case 1 -> new Tile(3141, 4547, level);
            case 2 -> new Tile(3189, 4667, level);
            case 3 -> new Tile(3158, 4606, level);
            case 4 -> new Tile(3173, 4598, level);
            case 5 -> new Tile(3176, 4559, level);
            case 6 -> new Tile(3158, 4654, level);
            default -> throw new IllegalStateException("Unexpected value: " + party.getRaidStage());
        };
    }

    @Override
    public void exit(Player player) {
        player.setRaids(null);

        //Delete the elder wand before doing any other actions
        boolean hasElderWand = player.inventory().contains(ELDER_WAND_RAIDS) || player.getEquipment().contains(ELDER_WAND_RAIDS);
        Spell autocastSpell = player.getCombat().getAutoCastSpell();
        if (hasElderWand) {
            if (autocastSpell != null) {
                Autocasting.setAutocast(player, null);
            }
            player.message(Color.RED.wrap("Your elder wand crumbles to dust as you exit the chamber."));
        }
        player.removeAll(new Item(ELDER_WAND_RAIDS));

        Party party = player.raidsParty;

        //Remove players from the party if they are not the leader
        if (party != null) {
            party.removeMember(player);
            //Last player in the party leaves clear the whole thing
            if (party.getMembers().isEmpty()) {
                //Clear all party members that are left
                clearParty(player);
            }
            player.raidsParty = null;
        }

        //Reset points
        player.putAttrib(PERSONAL_POINTS, 0);
        player.message("<col=" + Color.BLUE.getColorValue() + ">You have restored your hitpoints, run energy and prayer.");
        player.message("<col=" + Color.HOTPINK.getColorValue() + ">You've also been cured of poison and venom.");
        player.skills().resetStats();
        int increase = player.getEquipment().hpIncrease();
        player.hp(Math.max(increase > 0 ? player.skills().level(Skills.HITPOINTS) + increase : player.skills().level(Skills.HITPOINTS), player.skills().xpLevel(Skills.HITPOINTS)), 39); //Set hitpoints to 100%
        player.skills().replenishSkill(5, player.skills().xpLevel(5)); //Set the players prayer level to fullplayer.putAttrib(AttributeKey.RUN_ENERGY, 100.0);
        player.setRunningEnergy(100.0, true);
        Poison.cure(player);
        Venom.cure(2, player);

        //Move outside of raids
        player.teleport(1245, 3561, 0);
        player.getInterfaceManager().close(true);
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
        points *= 5;
        addPoints(player, points);
    }

    @Override
    public void complete(Party party) {
        party.forPlayers(p -> {
            p.message(Color.RAID_PURPLE.wrap("Congratulations - your raid is complete!"));
            var ticket = p.<Integer>getAttribOr(AttributeKey.TICKET_RUNS_CHALLENGE_COMPLETED, 0) + 1;
            var completed = p.<Integer>getAttribOr(AttributeKey.CHAMBER_OF_SECRET_RUNS_COMPLETED, 0) + 1;
            p.putAttrib(AttributeKey.TICKET_RUNS_CHALLENGE_COMPLETED, ticket);
            p.putAttrib(AttributeKey.CHAMBER_OF_SECRET_RUNS_COMPLETED, completed);
            p.message("@red@You now have " + ticket + " You need 200 to get raid kc every time to claim an raid rare ticket");
            p.message("@red@And this going be reset every 200 kc but not include raid kc when reset.");
            p.message(String.format("Total points: " + Color.RAID_PURPLE.wrap("%,d") + ", Personal points: " + Color.RAID_PURPLE.wrap("%,d") + " (" + Color.RAID_PURPLE.wrap("%.2f") + "%%)",
                party.totalPoints(), p.<Integer>getAttribOr(PERSONAL_POINTS, 0), (double) (p.<Integer>getAttribOr(PERSONAL_POINTS, 0) / party.totalPoints()) * 100));

            //Daily raids task
            DailyTaskManager.increase(DailyTasks.DAILY_RAIDS, p);

            //Roll a reward for each individual player
            ChamberOfSecretsReward.giveRewards(p);
        });
    }

    @Override
    public void startup(Player player) {
        Party party = player.raidsParty;
        if (party == null) return;
        party.setRaidStage(1);
        final int height = party.getLeader().getIndex() * 4;

        for (Player member : party.getMembers()) {
            if (member == null)
                continue;

            member.setRaids(this);
            member.teleport(new Tile(3141, 4547, height));
            party.setHeight(height);
        }

        //Clear kills
        party.setKills(0);

        //Clear npcs that somehow survived first:
        clearParty(player);

        //Spawn all creatures
        spawnCentaurs(player);
        spawnDementors(player);
        spawnFluffy(player);
        spawnAragog(player);
        spawnAragogMinions(player);
        //TODO spawnHungarianHorntail(player);
        spawnFenrirGreyback(player);
        spawnLordVoldemort(player);
    }

    private void spawnCentaurs(Player player) {
        //Get the raids party
        Party party = player.raidsParty;

        //Create centaurs
        Npc centaurMale = new RaidsNpc(MALE_CENTAUR, new Tile(3193, 4634, party.getHeight()), party.getSize());
        Npc centaurMale2 = new RaidsNpc(MALE_CENTAUR, new Tile(3191, 4630, party.getHeight()), party.getSize());
        Npc centaurMale3 = new RaidsNpc(MALE_CENTAUR, new Tile(3189, 4632, party.getHeight()), party.getSize());
        Npc centaurMale4 = new RaidsNpc(MALE_CENTAUR, new Tile(3188, 4634, party.getHeight()), party.getSize());
        Npc centaurFemale = new RaidsNpc(FEMALE_CENTAUR, new Tile(3184, 4628, party.getHeight()), party.getSize());
        Npc centaurFemale2 = new RaidsNpc(FEMALE_CENTAUR, new Tile(3184, 4632, party.getHeight()), party.getSize());
        Npc centaurFemale3 = new RaidsNpc(FEMALE_CENTAUR, new Tile(3180, 4635, party.getHeight()), party.getSize());
        Npc centaurFemale4 = new RaidsNpc(FEMALE_CENTAUR, new Tile(3179, 4629, party.getHeight()), party.getSize());

        //Spawn centaurs
        World.getWorld().registerNpc(centaurMale);
        party.monsters.add(centaurMale);
        World.getWorld().registerNpc(centaurMale2);
        party.monsters.add(centaurMale2);
        World.getWorld().registerNpc(centaurMale3);
        party.monsters.add(centaurMale3);
        World.getWorld().registerNpc(centaurMale4);
        party.monsters.add(centaurMale4);
        World.getWorld().registerNpc(centaurFemale);
        party.monsters.add(centaurFemale);
        World.getWorld().registerNpc(centaurFemale2);
        party.monsters.add(centaurFemale2);
        World.getWorld().registerNpc(centaurFemale3);
        party.monsters.add(centaurFemale3);
        World.getWorld().registerNpc(centaurFemale4);
        party.monsters.add(centaurFemale4);
    }

    private void spawnDementors(Player player) {
        //Get the raids party
        Party party = player.raidsParty;

        //Create dementors
        Npc dementor = new RaidsNpc(DEMENTOR, new Tile(3155, 4612, party.getHeight()), party.getSize());
        Npc dementor2 = new RaidsNpc(DEMENTOR, new Tile(3159, 4615, party.getHeight()), party.getSize());
        Npc dementor3 = new RaidsNpc(DEMENTOR, new Tile(3163, 4614, party.getHeight()), party.getSize());
        Npc dementor4 = new RaidsNpc(DEMENTOR, new Tile(3160, 4619, party.getHeight()), party.getSize());
        Npc dementor5 = new RaidsNpc(DEMENTOR, new Tile(3155, 4616, party.getHeight()), party.getSize());
        Npc dementor6 = new RaidsNpc(DEMENTOR, new Tile(3155, 4621, party.getHeight()), party.getSize());
        Npc dementor7 = new RaidsNpc(DEMENTOR, new Tile(3161, 4621, party.getHeight()), party.getSize());
        //Spawn dementors
        World.getWorld().registerNpc(dementor);
        party.monsters.add(dementor);
        World.getWorld().registerNpc(dementor2);
        party.monsters.add(dementor2);
        World.getWorld().registerNpc(dementor3);
        party.monsters.add(dementor3);
        World.getWorld().registerNpc(dementor4);
        party.monsters.add(dementor4);
        World.getWorld().registerNpc(dementor5);
        party.monsters.add(dementor5);
        World.getWorld().registerNpc(dementor6);
        party.monsters.add(dementor6);
        World.getWorld().registerNpc(dementor7);
        party.monsters.add(dementor7);
    }

    private void spawnFluffy(Player player) {
        //Get the raids party
        Party party = player.raidsParty;

        //Create fluffy
        Npc fluffy = new RaidsNpc(FLUFFY, new Tile(3139, 4563, party.getHeight()), party.getSize());
        //Spawn fluffy
        World.getWorld().registerNpc(fluffy);
        party.monsters.add(fluffy);
    }

    private void spawnAragog(Player player) {
        //Get the raids party
        Party party = player.raidsParty;

        //Create aragog
        Npc aragog = new RaidsNpc(ARAGOG, new Tile(3186, 4558, party.getHeight()), party.getSize());

        //Spawn aragog
        World.getWorld().registerNpc(aragog);
        party.monsters.add(aragog);
    }

    private void spawnAragogMinions(Player player) {
        //Get the raids party
        Party party = player.raidsParty;

        //Create minions
        Npc minion1 = new RaidsNpc(ARAGOG_MINION, new Tile(3188, 4552, party.getHeight()), party.getSize());
        Npc minion2 = new RaidsNpc(ARAGOG_MINION, new Tile(3192, 4554, party.getHeight()), party.getSize());
        Npc minion3 = new RaidsNpc(ARAGOG_MINION, new Tile(3191, 4561, party.getHeight()), party.getSize());
        Npc minion4 = new RaidsNpc(ARAGOG_MINION, new Tile(3188, 4564, party.getHeight()), party.getSize());
        Npc minion5 = new RaidsNpc(ARAGOG_MINION, new Tile(3184, 4567, party.getHeight()), party.getSize());
        Npc minion6 = new RaidsNpc(ARAGOG_MINION, new Tile(3179, 4563, party.getHeight()), party.getSize());
        Npc minion7 = new RaidsNpc(ARAGOG_MINION, new Tile(3179, 4558, party.getHeight()), party.getSize());
        Npc minion8 = new RaidsNpc(ARAGOG_MINION, new Tile(3182, 4553, party.getHeight()), party.getSize());

        //Spawn minions
        World.getWorld().registerNpc(minion1);
        World.getWorld().registerNpc(minion2);
        World.getWorld().registerNpc(minion3);
        World.getWorld().registerNpc(minion4);
        World.getWorld().registerNpc(minion5);
        World.getWorld().registerNpc(minion6);
        World.getWorld().registerNpc(minion7);
        World.getWorld().registerNpc(minion8);

        //Add to list
        party.monsters.add(minion1);
        party.monsters.add(minion2);
        party.monsters.add(minion3);
        party.monsters.add(minion4);
        party.monsters.add(minion5);
        party.monsters.add(minion6);
        party.monsters.add(minion7);
        party.monsters.add(minion8);
    }

    private void spawnHungarianHorntail(Player player) {
        //Get the raids party
        Party party = player.raidsParty;

        //Create hungarian horntails
        Npc hungarianHorntail = new RaidsNpc(HUNGARIAN_HORNTAIL, new Tile(3186, 4564, party.getHeight()), party.getSize());
        Npc hungarianHorntail2 = new RaidsNpc(HUNGARIAN_HORNTAIL, new Tile(3190, 4551, party.getHeight()), party.getSize());
        //Spawn horntails
        World.getWorld().registerNpc(hungarianHorntail);
        party.monsters.add(hungarianHorntail);
        World.getWorld().registerNpc(hungarianHorntail2);
        party.monsters.add(hungarianHorntail2);
    }

    private void spawnFenrirGreyback(Player player) {
        //Get the raids party
        Party party = player.raidsParty;

        //Create fenrir greyback
        Npc fenrirGreyback = new RaidsNpc(FENRIR_GREYBACK, new Tile(3192, 4599, party.getHeight()), party.getSize());
        //Spawn fenrir greyback
        World.getWorld().registerNpc(fenrirGreyback);
        party.monsters.add(fenrirGreyback);
    }

    private void spawnLordVoldemort(Player player) {
        //Get the raids party
        Party party = player.raidsParty;

        //Create lord voldemort instance
        Npc lordVoldemort = new RaidsNpc(LORD_VOLDEMORT, new Tile(3150, 4661, party.getHeight()), party.getSize());
        //Spawn the lord voldemort
        World.getWorld().registerNpc(lordVoldemort);
        party.monsters.add(lordVoldemort);
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
    }
}
