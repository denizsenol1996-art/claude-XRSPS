package com.twisted.game.content.raids.chamber_of_xeric.great_olm;

import com.twisted.game.content.raids.party.Party;
import com.twisted.game.world.entity.mob.Direction;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Tile;

import java.util.Objects;

/**
 * @author Patrick van Elderen | May, 16, 2021, 12:41
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class OlmAnimations {

    public static void resetAnimation(Party party) {
        final GameObject greatOlmObject = party.getGreatOlmObject();
        final Npc greatOlmNpc = party.getGreatOlmNpc();
        final Tile olmTile = greatOlmNpc.tile();

        final int spawnDirection = party.getGreatOlmNpc().spawnDirection();
        final int currentPhase = party.getCurrentPhase();
        final int xPosition = olmTile.getX();

        if (currentPhase == 3 && xPosition >= 3238) {
            if (getDirection(Direction.NONE.toInteger(), spawnDirection))
                sendObjectAnimation(party, greatOlmObject, 7374);

            else if (getDirection(Direction.NORTH.toInteger(), spawnDirection))
                sendObjectAnimation(party, greatOlmObject, 7376);

            else if (getDirection(Direction.SOUTH.toInteger(), spawnDirection))
                sendObjectAnimation(party, greatOlmObject, 7375);

            party.setOlmAttacking(false);
            return;
        }

        if (xPosition >= 3238) {
            if (getDirection(Direction.NONE.toInteger(), spawnDirection))
                sendObjectAnimation(party, greatOlmObject, 7336);

            else if (getDirection(Direction.NORTH.toInteger(), spawnDirection))
                sendObjectAnimation(party, greatOlmObject, 7337);


            else if (getDirection(Direction.SOUTH.toInteger(), spawnDirection))
                sendObjectAnimation(party, greatOlmObject, 7338);


            party.setOlmAttacking(false);
            return;
        }

        if (getDirection(Direction.NONE.toInteger(), spawnDirection))
            sendObjectAnimation(party, greatOlmObject, 7336);

        else if (getDirection(Direction.NORTH.toInteger(), spawnDirection))
            sendObjectAnimation(party, greatOlmObject, 7338);

        else if (getDirection(Direction.SOUTH.toInteger(), spawnDirection))
            sendObjectAnimation(party, greatOlmObject, 7337);

        party.setOlmAttacking(false);
    }

    static void sendObjectAnimation(final Party party, GameObject greatOlmObject, final int animation) {
        party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(greatOlmObject, animation));
    }

    static boolean getDirection(int direction, int spawnDirection) {
        return Objects.equals(direction, spawnDirection);
    }

}
