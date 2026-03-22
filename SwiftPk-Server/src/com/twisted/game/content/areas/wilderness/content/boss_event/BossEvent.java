package com.twisted.game.content.areas.wilderness.content.boss_event;

import com.twisted.game.world.position.Tile;
import com.twisted.util.CustomNpcIdentifiers;
import com.twisted.util.NpcIdentifiers;

/**
 * Boss event data. Contains all the types of boss events that can occur - sequentially - across the server.
 * @author Patrick van Elderen | February, 13, 2021, 09:09
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public enum BossEvent {

    GRIM(CustomNpcIdentifiers.GRIM, "Grim"),
    CORRUPTED_HUNLEFF(NpcIdentifiers.CORRUPTED_HUNLLEF, "Corrupted Hunllef"),
    // SKOTIZO(NpcIdentifiers.SKOTIZO, "Skotizo"),
    TEKTON(NpcIdentifiers.TEKTON_7542, "Tekton"),
    ZOMBIES_CHAMPION(NpcIdentifiers.ZOMBIES_CHAMPION, "Zombies champion"),
    NOTHING(-1, "Nothing"); // Filler

    public final int npc;
    public final String description;

    BossEvent(int npc, String description) {
        this.npc = npc;
        this.description = description;
    }

    public String spawnLocation(Tile tile) {
       /* if (tile.equals(new Tile(3167, 3757))) {
            return "south east of the black chins hills";
        } else if (tile.equals(new Tile(3166, 3832))) {
            return "near the Lava dragons";
        } else if (tile.equals(new Tile(3073, 3687))) {
            return "outside of the bandit camp";
        } else if (tile.equals(new Tile(3194,3951))) {
            return "next to resource area";
        } else if (tile.equals(new Tile(2963,3819))) {
            return "near lvl 40 wild altar";*/
        if (tile.equals(new Tile(3077,3687))) {
            return "outside bandit camp";
        } else if (tile.equals(new Tile(2966,3403))) {
            return "outside falador entrance";
        }

        //We shouldn't be getting here
        return "Nothing";
    }
}
