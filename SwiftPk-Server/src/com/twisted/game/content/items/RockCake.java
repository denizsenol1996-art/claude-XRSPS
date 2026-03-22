package com.twisted.game.content.items;

import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueManager;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.util.timers.TimerKey;

import static com.twisted.util.ItemIdentifiers.BLOOD_MONEY;

public class RockCake {

    private static final int ROCKCAKE = 7510;
    private static final int OLD_DWARF = 6254;

    public static boolean onItemOption1(Player player, Item item) {
        if(item.getId() == ROCKCAKE) {
            if (player.hp() > 1 && !player.getTimerRepository().has(TimerKey.EAT_ROCKCAKE)) {

                player.hit(player,1);
                player.getTimerRepository().extendOrRegister(TimerKey.EAT_ROCKCAKE, 1);
                player.sound(1018);
            }
            return true;
        }
        return false;
    }

    public static boolean onItemOption3(Player player, Item item) {
        if(item.getId() == ROCKCAKE) {
            if (player.hp() > 1 && !player.getTimerRepository().has(TimerKey.EAT_ROCKCAKE)) {
                int dmg = (player.hp() + 10) / 10;
                player.hit(player, dmg);
                player.getTimerRepository().extendOrRegister(TimerKey.EAT_ROCKCAKE, 1);
                player.sound(1018);
            }
            return true;
        }
        return false;
    }

    public static boolean onNpcOption1(Player player, Npc npc) {
        if(npc.id() == OLD_DWARF) {
            player.getDialogueManager().start(new Dialogue() {
                @Override
                protected void start(Object... parameters) {
                    send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Trade 100 gold for the cake.", "No thanks.");
                setPhase(0);
                }

                @Override
                protected void select(int option) {
                    if(isPhase(0)) {
                        if(option == 1) {
                            var bm = player.inventory().count(BLOOD_MONEY);
                            if (!would_have_space(player, new Item(ROCKCAKE))) {
                                DialogueManager.sendStatement(player,"You don't have enough inventory space.");
                            } else if (bm < 10) {
                                DialogueManager.sendStatement(player,"You don't have enough BM.");
                            } else if (bm > 10) {
                                player.inventory().remove(new Item(BLOOD_MONEY,10));
                                player.inventory().add(new Item(ROCKCAKE), true);
                            }
                            stop();
                        } else if(option == 2) {
                            stop();
                        }
                    }
                }
            });
            return true;
        }
        return false;
    }

    private static boolean would_have_space(Player player, Item foritem) {
        int free = player.inventory().getFreeSlots();
        return free >= foritem.getAmount();
    }
}
