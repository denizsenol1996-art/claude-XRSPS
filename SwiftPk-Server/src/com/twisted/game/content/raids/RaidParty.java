package com.twisted.game.content.raids;

import com.twisted.game.world.entity.mob.player.Player;
import lombok.Getter;

import java.util.List;

public class RaidParty {
    @Getter
    Player owner;
    @Getter public List<Player> players;

    public RaidParty(Player owner, List<Player> players) {
        this.owner = owner;
        this.players = players;
    }

    public void addOwner() {
        if (!players.contains(owner))
            players.add(owner);
    }

    public void clear() {
        players.clear();
    }

}
