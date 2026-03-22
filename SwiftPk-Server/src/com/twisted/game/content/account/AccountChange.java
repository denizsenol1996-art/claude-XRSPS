package com.twisted.game.content.account;

import com.twisted.game.content.kill_logs.BossKillLog;
import com.twisted.game.world.entity.mob.player.Player;

public class AccountChange {

    /**
     * The player this is relative to
     */
    private final Player p;

    /**
     * Creates a new {@link BossKillLog} object for a singular player
     *
     * @param p the player
     * @param p
     */
    public AccountChange(Player p) {
        this.p = p;
    }


    public void start(){
        p.message("test");

    }

}
