package com.twisted.game.content.areas.zulandra;

import com.twisted.game.content.areas.wilderness.content.key.WildernessKeyPlugin;
import com.twisted.game.content.areas.zulandra.dialogue.*;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueManager;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.dialogue.Expression;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.NpcIdentifiers;
import com.twisted.util.chainedwork.Chain;

import static com.twisted.util.ObjectIdentifiers.SACRIFICIAL_BOAT;
import static com.twisted.util.ObjectIdentifiers.ZULANDRA_TELEPORT;

/**
 * no need to use custom region packet, the instance system simply changes the heightlevel and uses the real world-map coords for
 * areas.
 */
public class ZulAndra extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if (option == 1) {
            if (obj.getId() == SACRIFICIAL_BOAT) {

                if (WildernessKeyPlugin.hasKey(player)) {
                    player.message("Nice try, you should keep walking because the boat is leaving.");
                    return false;
                }

                player.getDialogueManager().start(new Dialogue() {
                    @Override
                    protected void start(Object... parameters) {
                        send(DialogueType.OPTION, "Return to Zulrah's shrine?", "Yes.", "No.");
                        setPhase(0);
                    }

                    @Override
                    protected void select(int option) {
                        if (getPhase() == 0) {
                            if (option == 1) {
                                stop();
                                player.getPacketSender().sendScreenFade("", 1, 5);
                                DialogueManager.sendStatement(player, "The priestess rows you to Zulrah's shrine,", "then hurriedly paddles away.");
                                player.getZulrahInstance().enterInstance(player, false);
                            } else if (option == 2) {
                                stop();
                            }
                        }
                    }
                });
                return true;
            }
            if (obj.getId() == ZULANDRA_TELEPORT) {
                player.getDialogueManager().start(new Dialogue() {
                    @Override
                    protected void start(Object... parameters) {
                        send(DialogueType.OPTION, "Return to Zulrah's shrine?", "Yes.", "No.");
                        setPhase(0);
                    }

                    @Override
                    protected void select(int option) {
                        if (getPhase() == 0) {
                            if (option == 1) {
                                player.lock();
                                player.animate(3864);
                                player.graphic(1039, 100, 0);
                                stop();
                                Chain.bound(null).name("ZulrahReturnTask").runFn(4, () -> {
                                    player.animate(-1);
                                    player.getInterfaceManager().close();
                                    player.getZulrahInstance().enterInstance(player, true);
                                });
                            } else if (option == 2) {
                                stop();
                            }
                        }
                    }
                });
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        if (npc.id() == NpcIdentifiers.PRIESTESS_ZULGWENWYNIG_2033) {
            if (option == 1) {
                player.getDialogueManager().start(new PriestessZulGwenwynig());
            } else if (option == 2) {
                player.getDialogueManager().start(new Dialogue() {
                    @Override
                    protected void start(Object... parameters) {
                        send(DialogueType.NPC_STATEMENT, NpcIdentifiers.PRIESTESS_ZULGWENWYNIG_2033, Expression.DEFAULT, "I'm afraid I don't have anything for you to collect. If I", "had any of your items, but you died before collecting", "them from me, I'd lose them.");
                    }
                });
            }
            return true;
        }

        if (npc.id() == NpcIdentifiers.SACRIFICE) {
            player.getDialogueManager().start(new Sacrafise());
            return true;
        }

        if (npc.id() == NpcIdentifiers.ZULURGISH) {
            player.getDialogueManager().start(new ZulUrgish());
            return true;
        }

        if (npc.id() == NpcIdentifiers.ZULARETH) {
            player.getDialogueManager().start(new ZulAreth());
            return true;
        }

        if (npc.id() == NpcIdentifiers.ZULANIEL) {
            player.getDialogueManager().start(new ZulAniel());
            return true;
        }

        if (npc.id() == NpcIdentifiers.HIGH_PRIESTESS_ZULHARCINQA) {
            player.getDialogueManager().start(new HighPriestessZulHarcinqa());
            return true;
        }

        if (npc.id() == NpcIdentifiers.ZULONAN) {
            player.getDialogueManager().start(new ZulOnan());
            return true;
        }

        if (npc.id() == NpcIdentifiers.ZULGUTUSOLLY) {
            player.getDialogueManager().start(new ZulGutusolly());
            return true;
        }

        if (npc.id() == NpcIdentifiers.ZULCHERAY) {
            player.getDialogueManager().start(new ZulCheray());
            return true;
        }
        return false;
    }
}
