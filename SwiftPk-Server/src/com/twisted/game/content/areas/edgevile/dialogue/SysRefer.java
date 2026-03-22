package com.twisted.game.content.areas.edgevile.dialogue;

import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.dialogue.Expression;
import com.twisted.game.world.items.Item;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.PlayerPunishment;


import static com.twisted.game.world.entity.AttributeKey.MAC_ADDRESS;
import static com.twisted.util.NpcIdentifiers.SHURA;

/**
 * @author the plateau
 *
 */
public class SysRefer extends Dialogue {

    @Override
    protected void start(Object... parameters) {
        send(DialogueType.NPC_STATEMENT, SHURA, Expression.NODDING_THREE, "How did you find us?");
        setPhase(0);
    }

    @Override
    protected void next() {
        if(isPhase(0)) {
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Youtube","Rspstoplist","EverythingRS","RuneLocus","Arenatop100");
            setPhase(1);
        } else if(isPhase(2)) {
            send(DialogueType.NPC_STATEMENT, SHURA, Expression.NODDING_FIVE, "Thank you for joining Hazy.");
            setPhase(3);
        } else if(isPhase(3)) {
            stop();
        }
    }

    @Override
    protected void select(int option) {

        String mac = player.getAttribOr(MAC_ADDRESS, "invalid");


        if(isPhase(1)) {
            if(option == 1) {
                player.message("Thank you for joined Hazy from youtube and here your reward!");
                PlayerPunishment.addReferReclaim(mac);
                PlayerPunishment.addToIPReferList(player.getHostAddress());
                player.getInventory().addOrBank(new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(CustomItemIdentifiers.BLOOD_MONEY_CASKET), new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX));
                stop();

            }
            if(option == 2) {
                player.getInventory().addOrBank(new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(CustomItemIdentifiers.BLOOD_MONEY_CASKET), new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX));
                player.message("Thank you for joined Hazy from rspstoplist and here your reward!");
                PlayerPunishment.addReferReclaim(mac);
                PlayerPunishment.addToIPReferList(player.getHostAddress());
                stop();

            }
            if(option == 3) {
                player.getInventory().addOrBank(new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(CustomItemIdentifiers.BLOOD_MONEY_CASKET), new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX));
                player.message("Thank you for joined Hazy from everythingrs and here your reward!");
                PlayerPunishment.addReferReclaim(mac);
                PlayerPunishment.addToIPReferList(player.getHostAddress());
                stop();

            }
            if(option == 4) {
                player.getInventory().addOrBank(new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(CustomItemIdentifiers.BLOOD_MONEY_CASKET), new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX));
                player.message("Thank you for joined Hazy from runelocus and here your reward!");
                PlayerPunishment.addReferReclaim(mac);
                PlayerPunishment.addToIPReferList(player.getHostAddress());
                stop();

            }
            if(option == 5) {
                player.getInventory().addOrBank(new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(CustomItemIdentifiers.BLOOD_MONEY_CASKET), new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX));
                player.message("Thank you for joined Hazy from arenatop100 and here your reward!");
                PlayerPunishment.addReferReclaim(mac);
                PlayerPunishment.addToIPReferList(player.getHostAddress());
                stop();

            }
        }
    }
}
