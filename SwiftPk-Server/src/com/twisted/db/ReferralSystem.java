package com.twisted.db;

import com.twisted.game.GameEngine;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.rights.PlayerRights;
import com.twisted.game.world.items.Item;
import com.twisted.net.Session;
import com.twisted.net.login.LoginDetailsMessage;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;

import java.util.Objects;

public class ReferralSystem {

    public static Object2ObjectLinkedOpenHashMap<String, Referral> CLAIMED_REFERRALS = new Object2ObjectLinkedOpenHashMap<>();
    public static final String[] VALID_REFERRALS = new String[]{"vihtic", "catherby", "jipy", "klka", "merk", "sohan", "insomnia"};

    public static void claim(final Player player, String youtuber) {
        boolean valid = isValid(youtuber);

        if (!valid) {
            player.message("You've entered an invalid referral name, please try again.");
            return;
        }

        final String name = player.getUsername();

        if (CLAIMED_REFERRALS.containsKey(name) && !PlayerRights.OWNER.equals(player.getPlayerRights())) {
            player.message("You cannot claim anymore referral's.");
            return;
        }

        final Session session = player.getSession();
        final LoginDetailsMessage message = session.getMsg();

        final String mac = message.getMac();
        final String ipAddress = message.getHost();
        final long uuid = player.<Long>getAttribOr(AttributeKey.PLAYER_UID, 0L);
        final int amountClaimed = player.<Integer>getAttribOr(AttributeKey.REFERRALS_CLAIMED, 0);

        boolean canClaim = isCanClaim(player, mac, uuid, ipAddress, amountClaimed);
        if (!canClaim) return;

        player.putAttrib(AttributeKey.REFERRALS_CLAIMED, +1);

        Referral referral = new Referral(name, uuid, ipAddress, mac, amountClaimed, youtuber);
        CLAIMED_REFERRALS.put(referral.name(), referral);

        player.getInventory().addOrBank(new Item(ItemIdentifiers.BLOOD_MONEY, 250_000));
        player.getInventory().addOrBank(new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX, 2));

        player.message("You've successfully claimed your referral code from " + youtuber + "!");
        player.message("You've received x250,000 Blood Money as a reward from your referral code!");
        player.message("You've received x2 Legendary Mystery Boxes as a reward from your referral code!");

        player.animate(2106);
        player.performGraphic(new Graphic(2365, 0));

        GameEngine.getInstance().submitLowPriority(() ->
            GameEngine.getInstance().addSyncTask(() ->
                referral.save(CLAIMED_REFERRALS)));
    }

    private static boolean isCanClaim(Player player, String mac, long uuid, String ipAddress, int claimed) {
        if (claimed >= 1 && !PlayerRights.OWNER.equals(player.getPlayerRights())) {
            player.message("You cannot claim anymore referral's.");
            return false;
        }
        boolean canClaim = true;
        for (final var referral : CLAIMED_REFERRALS.object2ObjectEntrySet()) {
            final Referral value = referral.getValue();
            if (!PlayerRights.OWNER.equals(player.getPlayerRights()) && (value.mac().equalsIgnoreCase(mac) || Objects.equals(value.uuid(), uuid) || Objects.equals(value.ipAddress(), ipAddress))) {
                canClaim = false;
                player.message("You cannot claim anymore referral's.");
                break;
            }
        }
        return canClaim;
    }

    private static boolean isValid(String youtuber) {
        boolean valid = false;
        for (final String s : VALID_REFERRALS) {
            if (s.equalsIgnoreCase(youtuber)) {
                valid = true;
                break;
            }
        }
        return valid;
    }
}
