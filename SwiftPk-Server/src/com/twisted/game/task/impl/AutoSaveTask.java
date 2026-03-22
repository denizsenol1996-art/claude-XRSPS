package com.twisted.game.task.impl;

import com.twisted.GameServer;
import com.twisted.game.task.Task;
import com.twisted.game.world.entity.mob.player.Player;
import kotlinsave.save.PlayerSaves;

/**
 * @author lare96 <http://github.com/lare96>
 */
public class AutoSaveTask extends Task {

    private final Player player;

    public AutoSaveTask(Player player) {
        super("AutoSaveTask", GameServer.properties().autosaveMinutes * 100);
        this.player = player;
    }

    @Override
    protected void execute() {
        if (!player.isRegistered()) {
            stop();
            return;
        }
        PlayerSaves.requestSave(player);
    }

}
