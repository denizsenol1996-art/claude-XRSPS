package com.twisted.game.content.collection_logs.listener;

import com.twisted.game.content.collection_logs.impl.*;
import com.twisted.game.content.collection_logs.impl.boxes.*;
import com.twisted.game.content.collection_logs.impl.boxes.mysterybox.*;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;

import java.util.ArrayList;
import java.util.List;

public class CollectionLogHandler {
    private static final List<CollectionListener> boxListeners;
    private static final List<CollectionListener> keyListeners;

    static {
        boxListeners = initBoxes();
        keyListeners = initKeys();
    }

    private static List<CollectionListener> initBoxes() {
        List<CollectionListener> listeners = new ArrayList<>();
        listeners.add(new MysteryTicket());
        listeners.add(new MysteryChest());
        listeners.add(new BondCasket());
        listeners.add(new EventBondCasket());
        listeners.add(new MoltenMysteryBox());
        listeners.add(new LegendaryMysteryBox());
        listeners.add(new EpicPetMysteryBox());
        listeners.add(new WeaponMysteryBox());
        listeners.add(new RaidsMysteryBox());
        listeners.add(new RevenantMysteryBox());
        listeners.add(new ZenyteMysteryBox());
        listeners.add(new ArmourMysteryBox());
        listeners.add(new PresentBox());
        listeners.add(new EmeraldMysteryBox());
        listeners.add(new RubyMysteryBox());
        listeners.add(new SapphireMysteryBox());
        listeners.add(new DiamondMysteryBox());
        listeners.add(new OnyxMysteryBox());
        listeners.add(new DragonstoneMysteryBox());
        return listeners;
    }

    private static List<CollectionListener> initKeys() {
        List<CollectionListener> listeners = new ArrayList<>();
        listeners.add(new CrystalKey());
        listeners.add(new SlayerKey());
        listeners.add(new MoltenKey());
        listeners.add(new LarransKeyIII());
        listeners.add(new LarransKeyII());
        listeners.add(new LarranKeyI());
        listeners.add(new EnchantedKeyII());
        listeners.add(new EnchantedKeysI());
        listeners.add(new WildernessKey());
        return listeners;
    }

    public static boolean rollKeyReward(Player player, int id, GameObject object, int oldId, int newId) {
        for (CollectionListener listener : keyListeners) {
            if (listener.isItem(id)) {
                listener.openKey(player, object, oldId, newId);
                return true;
            }
        }
        return false;
    }

    public static boolean rollBoxReward(Player player, int id, boolean isGrandKey, boolean isKeyofBoxes) {
        for (CollectionListener listener : boxListeners) {
            if (listener.isItem(id)) {
                listener.openBox(player, isGrandKey, isKeyofBoxes);
                return true;
            }
        }
        return false;
    }
}
