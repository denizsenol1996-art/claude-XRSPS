package com.twisted.game.world.entity.combat.sigils;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Entity;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.Combat;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.formula.accuracy.AbstractAccuracy;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.sigils.combat.*;
import com.twisted.game.world.entity.combat.sigils.data.SigilData;
import com.twisted.game.world.entity.combat.sigils.misc.*;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sigil extends PacketInteraction implements SigilListener {
    private static final List<AbstractSigil> handler;

    static {
        List<AbstractSigil> sigilList = new ArrayList<>(
            List.of(
                new FeralFighter(),
                new MenacingMage(),
                new RuthlessRanger(),
                new DeftStrikes(),
                new MeticulousMage(),
                new Consistency(),
                new FormidableFighter(),
                new Resistance(),
                new Precision(),
                new Fortification(),
                new Stamina(),
                new Alchemaniac(),
                new Exaggeration(),
                new Devotion(),
                new LastRecall(),
                new RemoteStorage(),
                new Ninja(),
                new InfernalSmith()
            ));
        handler = Collections.unmodifiableList(sigilList);
    }

    @Override
    public void processResistance(Mob attacker, Mob target, Hit hit) {
        if (!(attacker instanceof Npc)) return;
        if (target instanceof Player player) {
            for (SigilData data : SigilData.values()) {
                for (AbstractSigil sigil : handler) {
                    if (sigil == null) continue;
                    if (data.handler.equals(sigil.getClass()) && sigil.attuned(player)) {
                        sigil.resistanceModification(attacker, player, hit);
                    }
                }
            }
        }
    }

    @Override
    public void processDamage(Player player, Hit hit) {
        if (WildernessArea.inWilderness(player.tile())) return;
        Combat combat = player.getCombat();
        if (combat == null) return;
        CombatType combatType = combat.getCombatType();
        if (combatType == null) return;
        Entity combatTarget = combat.getTarget();
        if (combatTarget instanceof Player) return;
        for (SigilData data : SigilData.values()) {
            for (AbstractSigil sigil : handler) {
                if (sigil == null) continue;
                if (data.handler.equals(sigil.getClass()) && sigil.attuned(player)) {
                    if (sigil.validateCombatType(player)) {
                        sigil.damageModification(player, hit);
                    }
                }
            }
        }
    }

    @Override
    public void process(Player player, Mob target) {
        if (WildernessArea.inWilderness(player.tile()) && target instanceof Player) return;
        Combat combat = player.getCombat();
        if (combat == null) return;
        CombatType combatType = combat.getCombatType();
        if (combatType == null) return;
        Entity combatTarget = combat.getTarget();
        if (combatTarget instanceof Player) return;
        for (SigilData data : SigilData.values()) {
            for (AbstractSigil sigil : handler) {
                if (sigil == null) continue;
                if (data.handler.equals(sigil.getClass()) && sigil.attuned(player)) {
                    if (sigil.validateCombatType(player)) {
                        sigil.processCombat(player, target);
                    }
                }
            }
        }
    }

    @Override
    public double processAccuracy(Player player, Mob target, AbstractAccuracy accuracy) {
        if (WildernessArea.inWilderness(player.tile()) && target instanceof Player) return 0;
        Combat combat = player.getCombat();
        if (combat == null) return 0;
        CombatType combatType = combat.getCombatType();
        if (combatType == null) return 0;
        Entity combatTarget = combat.getTarget();
        if (combatTarget instanceof Player) return 0;
        for (SigilData data : SigilData.values()) {
            for (AbstractSigil sigil : handler) {
                if (sigil == null) continue;
                if (data.handler.equals(sigil.getClass()) && sigil.attuned(player)) {
                    if (sigil.validateCombatType(player))
                       return sigil.accuracyModification(player, target, accuracy);
                }
            }
        }
        return 0;
    }

    @Override
    public void HandleLogin(Player player) {
        for (SigilData data : SigilData.values()) {
            for (AbstractSigil listener : handler) {
                if (listener == null) continue;
                if (data.handler.equals(listener.getClass()) && listener.attuned(player)) {
                    listener.processMisc(player);
                }
            }
        }
    }

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        var total = player.<Integer>getAttribOr(AttributeKey.TOTAL_SIGILS_ACTIVATED, 0);
        int activationCap = 3;
        switch (player.getMemberRights()) {
            case DIAMOND_MEMBER, DRAGONSTONE_MEMBER -> activationCap = 4;
            case ONYX_MEMBER, ZENYTE_MEMBER -> activationCap = 5;
        }
        if (option == 1) {
            for (SigilData data : SigilData.values()) {
                if (item.getId() == data.unattuned) {
                    if (player.hasAttrib(data.attributeKey)) {
                        player.message(Color.RED.wrap("You cannot have more than one of the same sigil activated."));
                        return false;
                    }
                    if (total == activationCap) {
                        player.message(Color.RED.wrap("You can only have " + activationCap + " sigil's activated at one time."));
                        return false;
                    }
                    total += 1;
                    player.putAttrib(data.attributeKey, true);
                    player.putAttrib(AttributeKey.TOTAL_SIGILS_ACTIVATED, total);
                    player.animate(713);
                    player.graphic(data.graphic, 100, 20);
                    player.getInventory().replace(data.unattuned, data.attuned, true);
                    for (AbstractSigil listener : handler) {
                        if (listener == null) throw new RuntimeException("Exception in AbstractSigil");
                        if (data.handler.equals(listener.getClass()) && listener.attuned(player)) {
                            listener.processMisc(player);
                            break;
                        }
                    }
                    return true;
                }
            }
        } else if (option == 2) {
            for (SigilData data : SigilData.values()) {
                if (item.getId() == data.attuned) {
                    for (AbstractSigil listener : handler) {
                        if (listener == null) throw new RuntimeException("Exception in AbstractSigil");
                        if (data.handler.equals(listener.getClass()) && listener.attuned(player)) {
                            listener.onRemove(player);
                            break;
                        }
                    }
                    total -= 1;
                    player.putAttrib(AttributeKey.TOTAL_SIGILS_ACTIVATED, total);
                    player.clearAttrib(data.attributeKey);
                    player.getInventory().replace(data.attuned, data.unattuned, true);
                    return true;
                }
            }
        }
        return false;
    }
}

