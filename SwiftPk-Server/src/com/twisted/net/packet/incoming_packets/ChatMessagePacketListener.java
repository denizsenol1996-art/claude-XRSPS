package com.twisted.net.packet.incoming_packets;

import com.twisted.GameServer;
import com.twisted.game.content.mechanics.AntiSpam;
import com.twisted.game.content.mechanics.Censor;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.dialogue.DialogueManager;
import com.twisted.game.world.entity.masks.chat.ChatMessage;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;
import com.twisted.util.Utils;
import com.twisted.util.flood.Buffer;

/**
 * This packet listener manages the spoken text by a player.
 * 
 * @author Gabriel Hannason
 */
public class ChatMessagePacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int size = packet.getSize() - 2;
        int color = packet.readByteS();
        int effect = packet.readByteS();
        byte[] text = packet.readReversedBytesA(size);
        String raw = Utils.textUnpack(text, size);
        String chatMessage = Utils.ucFirst(Utils.textUnpack(text, size).toLowerCase());

        if (chatMessage.length() <= 0) {
            return;
        }

        if (chatMessage.length() > 80) {
            chatMessage = chatMessage.substring(0, 79);
        }

        if (AntiSpam.isNewPlayerSpamming(player, chatMessage)) {
            return;
        }

        boolean newAccount = player.getAttribOr(AttributeKey.NEW_ACCOUNT, false);
        if (newAccount) {
            player.message("You can chat with friends after you have chosen your game mode.");
            return;
        }

        player.afkTimer.reset();
        
        if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
            player.getBankPin().openIfNot();
            return;
        }

        if(player.askForAccountPin()) {
            player.sendAccountPinMessage();
            return;
        }

        if (chatMessage.toLowerCase().contains("img=") || chatMessage.toLowerCase().contains("col=")) {
            return;
        }

        if (player.muted()) {
            player.message("You are muted and cannot chat. Please try again later.");
            return;
        }

        String filtered = Censor.starred(raw);
        if (filtered != null)
            text = Utils.encode(filtered, Buffer.create());
        
        if (Utils.blockedWord(chatMessage)) {
            DialogueManager.sendStatement(player, "A word was blocked in your sentence. Please do not repeat it!");
            return;
        }

        if (player.getChatMessageQueue().size() >= 5) {
            player.message("Please do not spam.");
            return;
        }

        player.getChatMessageQueue().add(new ChatMessage(color, effect, text));
        Utils.sendDiscordInfoLog(player, player.getUsername() + ": " + chatMessage, "chat");
    }

}
