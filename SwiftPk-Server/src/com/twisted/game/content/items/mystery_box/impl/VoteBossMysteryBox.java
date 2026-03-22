package com.twisted.game.content.items.mystery_box.impl;

import com.twisted.game.content.items.mystery_box.MboxItem;
import com.twisted.game.content.items.mystery_box.MysteryBox;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import static com.twisted.util.ItemIdentifiers.*;

public class VoteBossMysteryBox extends MysteryBox {//updatevoteboss

    public static final int VOTEBOSS_MYSTERY_BOX = 30189;

    @Override
    protected String name() {
        return "Vote boss mystery box";
    }

    @Override
    public int mysteryBoxId() {
        return VOTEBOSS_MYSTERY_BOX;
    }

    private static final int EXTREME_ROLL = 40;
    private static final int RARE_ROLL = 20;
    private static final int UNCOMMON_ROLL = 10;

    private static final MboxItem[] EXTREMELY_RARE = new MboxItem[]{
        new MboxItem(CustomItemIdentifiers.WEAPON_MYSTERY_BOX, 5).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX, 5).broadcastWorldMessage(true),
        new MboxItem(BLOOD_MONEY, 50000).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.EPIC_PET_BOX, 1).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.MYSTERY_TICKET, 1).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.VOTE_TICKET, 10).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.TEN_DOLLAR_BOND, 1).broadcastWorldMessage(true)

    };

    private static final MboxItem[] RARE = new MboxItem[]{

        new MboxItem(CustomItemIdentifiers.DONATOR_MYSTERY_BOX,5).broadcastWorldMessage(true),
        new MboxItem(BLOOD_MONEY, 25000).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.KEY_OF_BOXES).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.DONATOR_TICKET, 500).broadcastWorldMessage(true)


    };

    private static final MboxItem[] UNCOMMON = new MboxItem[]{
        new MboxItem(CustomItemIdentifiers.DONATOR_MYSTERY_BOX,1).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.WEAPON_MYSTERY_BOX, 1).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX,1).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.VOTE_TICKET,5).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.DONATOR_TICKET, 250).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.DONATOR_TICKET, 250).broadcastWorldMessage(true)


    };



    private MboxItem[] allRewardsCached;

    public MboxItem[] allPossibleRewards() {
        if (allRewardsCached == null) {
            ArrayList<MboxItem> mboxItems = new ArrayList<>();
            mboxItems.addAll(Arrays.asList(EXTREMELY_RARE));
            mboxItems.addAll(Arrays.asList(RARE));
            mboxItems.addAll(Arrays.asList(UNCOMMON));
            allRewardsCached = mboxItems.toArray(new MboxItem[0]);
        }
        return allRewardsCached;
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.VOTEBOSS_MYSTERY_BOXES_OPENED;
    }

    @Override
    public MboxItem rollReward() {
        if (Utils.rollDie(EXTREME_ROLL, 1)) {
            return Utils.randomElement(EXTREMELY_RARE);
        } else if (Utils.rollDie(RARE_ROLL, 1)) {
            return Utils.randomElement(RARE);
        } else {
            return Utils.randomElement(UNCOMMON);

        }
    }
}
