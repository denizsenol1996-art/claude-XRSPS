package com.twisted.game.world.position.areas.impl;

import com.twisted.game.content.tournaments.TournamentManager;
import com.twisted.game.content.tournaments.TournamentUtils;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.prayer.default_prayer.DefaultPrayerData;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.areas.Controller;

import java.util.Arrays;

public class TournamentArea extends Controller {

    public TournamentArea() {
        super(Arrays.asList(new Area(3321, 4940, 3325, 4979), new Area(3267, 4931, 3318, 4988)));
    }

    @Override
    public void enter(Mob mob) {
        if (mob.isPlayer()) {
            Player player = mob.getAsPlayer();
            if (player.getAttribOr(AttributeKey.RUINOUS_PRAYERS, false)) {
                for (int i = 0; i < DefaultPrayerData.values().length; i++) {
                    Prayers.deactivatePrayer(player, i);
                }
                player.clearAttrib(AttributeKey.RUINOUS_PRAYERS);
                player.getPacketSender().updateTab(0, 5);
                player.getInterfaceManager().setSidebar(5, 5608);
            }
        }
    }

    @Override
    public void leave(Mob mob) {
        if (mob.isPlayer()) {
            Player player = (Player) mob;
            player.getInterfaceManager().openWalkable(-1);
            TournamentManager.leaveTourny(player,false,false);
        }
    }

    @Override
    public void process(Mob mob) {
        if (mob.isPlayer()) {
            Player player = mob.getAsPlayer();
            if (player.getParticipatingTournament() != null) {
                if (player.getParticipatingTournament().getFighters().size() == 1) {
                    player.getParticipatingTournament().checkForWinner();
                }
            }
            if(!player.isTournamentSpectating()) {
                player.getInterfaceManager().openWalkable(TournamentUtils.TOURNAMENT_WALK_INTERFACE);
            }
        }
    }

    @Override
    public boolean canTeleport(Player player) {
        return player.getPlayerRights().isDeveloperOrGreater(player);
    }

    @Override
    public boolean canAttack(Mob attacker, Mob target) {
        return true;
    }

    @Override
    public boolean canTrade(Player player, Player target) {
        return false;
    }

    @Override
    public boolean isMulti(Mob mob) {
        return false;
    }

    @Override
    public boolean canEat(Player player, int itemId) {
        return true;
    }

    @Override
    public boolean canDrink(Player player, int itemId) {
        return true;
    }

    @Override
    public void onPlayerRightClick(Player player, Player rightClicked, int option) {
    }

    @Override
    public void defeated(Player player, Mob mob) {
    }

    @Override
    public boolean handleObjectClick(Player player, GameObject object, int type) {
        return false; // dealt with elsewhere
    }

    @Override
    public boolean handleNpcOption(Player player, Npc npc, int type) {
        return false;
    }

    @Override
    public boolean inside(Mob mob) {
        return false; // no need, assuming coords are accurate
    }

    @Override
    public boolean useInsideCheck() {
        return false; // no need, assuming coords are accurate
    }
}
