package com.twisted.game.content.gambling;

import com.twisted.game.world.entity.mob.player.Player;

/**
 * The structure of any single game of chance.
 */
public abstract class Gamble {

    /**
     * Players inside the 'gamble'
     */
    public Player host, opponent;

    /**
     * Score betwen the players inside the 'gamble'
     */
    public int hostScore, opponentScore;

    public int gameId;

    /**
     * Checks if its the host's turn
     */
    public boolean yourTurn;

    public Gamble(Player host, Player opponent) {
        this.host = host;
        this.opponent = opponent;
    }

    public abstract void gamble();
}
