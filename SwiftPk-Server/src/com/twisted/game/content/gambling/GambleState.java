package com.twisted.game.content.gambling;

public enum GambleState {
    /**
     * Not in a gamble stage
     */
    NONE,
    /**
     * Offering to gamble another player
     */
    REQUESTED_GAMBLE,
    /**
     * Gamble has been accepted, placing a bet
     */
    PLACING_BET,
    /**
     * The bet has been confirmed. The gamble in progress
     */
    IN_PROGRESS,
}
