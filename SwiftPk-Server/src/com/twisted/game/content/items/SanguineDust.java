package com.twisted.game.content.items;

import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;

import static com.twisted.game.world.entity.AttributeKey.SANGUINE_DUST_USED_ON;
import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since March 06, 2022
 */
public class SanguineDust extends PacketInteraction {

    private static class SanguineDustD extends Dialogue {

        @Override
        protected void start(Object... parameters) {
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Lil' Maiden", "Lil' Bloat", "Lil' Nylo", "Lil' Sot", "More...");
            setPhase(0);
        }

        @Override
        protected void select(int option) {
            int usedOnId = player.<Integer>getAttribOr(SANGUINE_DUST_USED_ON, -1);
            if(isPhase(0)) {
                if(option == 1) {
                    stop();
                    if(!player.inventory().contains(usedOnId))  {
                        return;
                    }
                    player.inventory().replace(usedOnId, LIL_MAIDEN,true);
                }
                if(option == 2) {
                    stop();
                    if(!player.inventory().contains(usedOnId))  {
                        return;
                    }
                    player.inventory().replace(usedOnId, LIL_BLOAT,true);
                }
                if(option == 3) {
                    stop();
                    if(!player.inventory().contains(usedOnId))  {
                        return;
                    }
                    player.inventory().replace(usedOnId, LIL_NYLO,true);
                }
                if(option == 4) {
                    stop();
                    if(!player.inventory().contains(usedOnId))  {
                        return;
                    }
                    player.inventory().replace(usedOnId, LIL_SOT,true);
                }
                if(option == 5) {
                    send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Lil' Xarp", "Lil' Zik", "Back...");
                    setPhase(1);
                }
            } else if(isPhase(1)) {
                if(option == 1) {
                    stop();
                    if(!player.inventory().contains(usedOnId))  {
                        return;
                    }
                    player.inventory().replace(usedOnId, LIL_XARP,true);
                }
                if(option == 2) {
                    stop();
                    if(!player.inventory().contains(usedOnId))  {
                        return;
                    }
                    player.inventory().replace(usedOnId, LIL_ZIK,true);
                }
                if(option == 3) {
                    send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Lil' Maiden", "Lil' Bloat", "Lil' Nylo", "Lil' Sot", "More...");
                    setPhase(0);
                }
            }
        }
    }

    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        if ((use.getId() == SANGUINE_DUST || usedWith.getId() == SANGUINE_DUST) && (use.getId() == LIL_ZIK || usedWith.getId() == LIL_ZIK)) {
            player.putAttrib(SANGUINE_DUST_USED_ON, LIL_ZIK);
            player.getDialogueManager().start(new SanguineDustD());
            return true;
        }
        if ((use.getId() == SANGUINE_DUST || usedWith.getId() == SANGUINE_DUST) && (use.getId() == LIL_MAIDEN || usedWith.getId() == LIL_MAIDEN)) {
            player.putAttrib(SANGUINE_DUST_USED_ON, LIL_MAIDEN);
            player.getDialogueManager().start(new SanguineDustD());
            return true;
        }
        if ((use.getId() == SANGUINE_DUST || usedWith.getId() == SANGUINE_DUST) && (use.getId() == LIL_BLOAT || usedWith.getId() == LIL_BLOAT)) {
            player.putAttrib(SANGUINE_DUST_USED_ON, LIL_BLOAT);
            player.getDialogueManager().start(new SanguineDustD());
            return true;
        }
        if ((use.getId() == SANGUINE_DUST || usedWith.getId() == SANGUINE_DUST) && (use.getId() == LIL_NYLO || usedWith.getId() == LIL_NYLO)) {
            player.putAttrib(SANGUINE_DUST_USED_ON, LIL_NYLO);
            player.getDialogueManager().start(new SanguineDustD());
            return true;
        }
        if ((use.getId() == SANGUINE_DUST || usedWith.getId() == SANGUINE_DUST) && (use.getId() == LIL_SOT || usedWith.getId() == LIL_SOT)) {
            player.putAttrib(SANGUINE_DUST_USED_ON, LIL_SOT);
            player.getDialogueManager().start(new SanguineDustD());
            return true;
        }
        if ((use.getId() == SANGUINE_DUST || usedWith.getId() == SANGUINE_DUST) && (use.getId() == LIL_XARP || usedWith.getId() == LIL_XARP)) {
            player.putAttrib(SANGUINE_DUST_USED_ON, LIL_XARP);
            player.getDialogueManager().start(new SanguineDustD());
            return true;
        }
        return false;
    }
}
