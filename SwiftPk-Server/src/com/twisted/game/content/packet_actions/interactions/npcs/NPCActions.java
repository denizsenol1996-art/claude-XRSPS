package com.twisted.game.content.packet_actions.interactions.npcs;

import com.twisted.game.content.achievements.AchievementsManager;
import com.twisted.game.content.achievements.Achievements;
import com.twisted.game.content.areas.edgevile.BobBarter;
import com.twisted.game.content.areas.edgevile.dialogue.GeneralStoreDialogue;
import com.twisted.game.content.areas.edgevile.dialogue.SurgeonGeneralTafaniDialogue;
import com.twisted.game.content.areas.home.TwiggyOKorn;
import com.twisted.game.content.areas.wilderness.dialogue.MandrithDialogue;
//import com.twisted.game.content.areas.wilderness.dialogue.PilesDialogue;
import com.twisted.game.content.areas.zeah.woodcutting_guild.dialogue.*;
import com.twisted.game.content.bank_pin.dialogue.BankTellerDialogue;
import com.twisted.game.content.items.RockCake;
import com.twisted.game.content.mechanics.Poison;
import com.twisted.game.content.skill.impl.fishing.Fishing;
import com.twisted.game.content.skill.impl.hunter.HuntingExpert;
import com.twisted.game.content.skill.impl.hunter.Impling;
import com.twisted.game.content.tradingpost.TradingPost;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.Venom;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.npc.pets.PetAI;
import com.twisted.game.world.entity.mob.npc.pets.insurance.PetInsurance;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.entity.mob.player.ironman.GroupIronman;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteractionManager;
import com.twisted.util.Color;
import com.twisted.util.ItemIdentifiers;
import com.twisted.util.NpcIdentifiers;
import com.twisted.util.timers.TimerKey;

/**
 * A class to distinguish click actions from the packet classes.
 *
 * @author Patrick van Elderen | Zerikoth (PVE) | 16 okt. 2019 : 09:49
 * @see <a href="https://github.com/Patrick9-10-1995">Github profile</a>
 */
public class NPCActions extends NpcIdentifiers {

    /**
     * This method determines all the clicking actions for an npc.
     * @param player The player
     * @param npc The npc
     * @param clickAction The menu action
     */
    public static void handleAction(Player player, Npc npc, int clickAction) {
        boolean handled = false;
        if (clickAction == 1) {
            if (PacketInteractionManager.checkNpcInteraction(player, npc, 1)) {
                return;
            }

            // Areas
            if (player.getController() != null) {
                if (player.getController().handleNpcOption(player, npc, 1)) {
                    return;
                }
            }

            if(Impling.onNpcOption1(player, npc)) {
                return;
            }

            if(Fishing.onNpcOption1(player, npc)) {
                return;
            }

            if(PetAI.onNpcOption1(player, npc)) {
                return;
            }

            if(RockCake.onNpcOption1(player, npc)) {
                return;
            }

            if(HuntingExpert.onNpcOption1(player, npc)) {
                return;
            }

            if(PetInsurance.handleNpc(player, npc, 1)) {
                npc.face(player.tile());
                handled = true;
            }

            if (npc.def().name != null) {
            if(npc.def().name.equalsIgnoreCase("banker")) {
                player.getDialogueManager().start(new BankTellerDialogue(), npc);
                return;
            }
            }

            switch (npc.id()) {

                case NpcIdentifiers.BOB_BARTER_HERBS:
                    player.getDialogueManager().start(new BobBarter());
                    break;

                case NpcIdentifiers.MURFET:
                    player.getDialogueManager().start(new MurfetD());
                    break;

                case NpcIdentifiers.GUILDMASTER_LARS:
                    player.getDialogueManager().start(new LarsD());
                    break;

                case NpcIdentifiers.KAI:
                    player.getDialogueManager().start(new KaiD());
                    break;

                case NpcIdentifiers.BERRY_7235:
                    player.getDialogueManager().start(new BerryD());
                    break;

                case NpcIdentifiers.THE_COLLECTOR:
                    npc.face(player.tile());
                    player.inventory().addOrDrop(new Item(ItemIdentifiers.COLLECTION_LOG, 1));
                    break;
                case NpcIdentifiers.TWIGGY_OKORN:
                    npc.face(player.tile());
                    player.getDialogueManager().start(new TwiggyOKorn());
                    break;

                case NpcIdentifiers.SUROK_MAGIS:
                    npc.face(player.tile());
                    player.getPacketSender().sendString(29078, "World Teleports - Recent");
                    player.setCurrentTabIndex(2);
                    player.getTeleportInterface().displayRecent();
                    player.getInterfaceManager().open(29050);
                    break;

                case SHOP_KEEPER:
                case SHOP_ASSISTANT_2818:
                    npc.face(player.tile());
                    player.getDialogueManager().start(new GeneralStoreDialogue());
                    break;

                case MAKEOVER_MAGE:
                case MAKEOVER_MAGE_1307:
                    npc.face(player.tile());
                    player.getInterfaceManager().close();
                    player.getInterfaceManager().open(3559);
                    break;
                case NpcIdentifiers.SURGEON_GENERAL_TAFANI:
                    npc.face(player.tile());
                    player.getDialogueManager().start(new SurgeonGeneralTafaniDialogue());
                    break;
                case NpcIdentifiers.MANDRITH:
                    if(npc.tile().equals(3183, 3945, 0)) {
                        player.getDialogueManager().start(new MandrithDialogue());
                        return;
                    }
                    npc.face(player.tile());
                    break;
                default:
                    if (!handled) {
                        player.message("They don't seem to be interested in talking right now...");
                    }
                    break;
            }
            return;
        }

        if (clickAction == 2) {
            if (PacketInteractionManager.checkNpcInteraction(player, npc, 2)) {
                return;
            }

            // Areas
            if (player.getController() != null) {
                if (player.getController().handleNpcOption(player, npc, 2)) {
                    return;
                }
            }

            if(Fishing.onNpcOption2(player, npc)) {
                return;
            }

            if(PetAI.onNpcOption2(player, npc)) {
                return;
            }

            if(Fishing.onNpcOption1(player, npc)) {
                return;
            }

            if(PetInsurance.handleNpc(player, npc, 2)) {
                npc.face(player.tile());
                return;
            }

            GroupIronman group = GroupIronman.getGroup(player.getUID());
            if(npc.def().name.equalsIgnoreCase("banker")) {
                if (group == null) {
                    player.getBank().open();
                } else {
                    group.getGroupBank().open(player, group);
                }
                return;
            }

            switch (npc.id()) {
                case NpcIdentifiers.TWIGGY_OKORN -> {
                    if (AchievementsManager.isCompleted(player, Achievements.COMPLETIONIST)) {
                        if (player.inventory().getFreeSlots() < 2) {
                            player.inventory().add(new Item(ItemIdentifiers.ACHIEVEMENT_DIARY_CAPE, 1));
                            player.inventory().add(new Item(ItemIdentifiers.ACHIEVEMENT_DIARY_HOOD, 1));
                        } else {
                            player.message("You need at least 2 free slots.");
                        }
                    } else {
                        player.message("You haven't completed all of the achievements yet.");
                    }
                    //World.getWorld().shop(41).open(player);
                }

                case GUNDAI -> {
                    if (group == null) {
                        player.getBank().open();
                    } else {
                        group.getGroupBank().open(player, group);
                    }
                }

                case MAKEOVER_MAGE_1307 -> player.getInterfaceManager().open(61380);


                case NpcIdentifiers.BOB_BARTER_HERBS -> player.getDialogueManager().start(new BobBarter());
                case NpcIdentifiers.THE_COLLECTOR -> {
                    npc.face(player.tile());
                    player.inventory().addOrDrop(new Item(ItemIdentifiers.COLLECTION_LOG, 1));
                }
                case NpcIdentifiers.FORESTER_7238 -> player.getDialogueManager().start(new ForesterD());
                case NpcIdentifiers.SURGEON_GENERAL_TAFANI -> {
                    npc.face(player.tile());
                    player.performGraphic(new Graphic(683));
                    player.message("<col=" + Color.BLUE.getColorValue() + ">You have restored your hitpoints, run energy and prayer.");
                    player.message("<col=" + Color.HOTPINK.getColorValue() + ">You've also been cured of poison and venom.");
                    player.hp(Math.max(player.skills().level(Skills.HITPOINTS), player.skills().xpLevel(Skills.HITPOINTS)), 20); //Set hitpoints to 100%
                    player.skills().replenishSkill(5, player.skills().xpLevel(5)); //Set the players prayer level to full
                    player.skills().replenishStatsToNorm();
                    player.setRunningEnergy(100.0, true);
                    Poison.cure(player);
                    Venom.cure(2, player, false);
                    if (player.getMemberRights().isEmeraldOrGreater(player)) {
                        if (player.getTimerRepository().has(TimerKey.RECHARGE_SPECIAL_ATTACK)) {
                            player.message("Special attack energy can only be restored every couple of minutes.");
                        } else {
                            player.setSpecialAttackPercentage(100);
                            player.setSpecialActivated(false);
                            CombatSpecial.updateBar(player);
                            player.getTimerRepository().register(TimerKey.RECHARGE_SPECIAL_ATTACK, 150); //Set the value of the timer.
                            player.message("<col=" + Color.HOTPINK.getColorValue() + ">You have restored your special attack.");
                        }
                    }
                }
                case SHOP_KEEPER, SHOP_ASSISTANT_2818 -> {
                    npc.face(player.tile());
                    World.getWorld().shop(1).open(player);
                }
            }
            return;
        }

        if (clickAction == 3) {
            if (PacketInteractionManager.checkNpcInteraction(player, npc, 3)) {
                return;
            }

            // Areas
            if (player.getController() != null) {
                if (player.getController().handleNpcOption(player, npc, 3)) {
                    return;
                }
            }

            if(PetAI.onNpcOption3(player, npc)) {
                return;
            }

            if (npc.id() == NpcIdentifiers.BOB_BARTER_HERBS) {
                player.getDialogueManager().start(new BobBarter());
                return;
            }

            if (npc.id() == NpcIdentifiers.TWIGGY_OKORN) {
                if (AchievementsManager.isCompleted(player, Achievements.COMPLETIONIST)) {
                    if (player.inventory().getFreeSlots() < 2) {
                        player.inventory().add(new Item(ItemIdentifiers.ACHIEVEMENT_DIARY_CAPE, 1));
                        player.inventory().add(new Item(ItemIdentifiers.ACHIEVEMENT_DIARY_HOOD, 1));
                    } else {
                        player.message("You need at least 2 free slots.");
                    }
                } else {
                    player.message("You haven't completed all of the achievements yet.");
                }
                return;
            }

            if( npc.id() == GUNDAI){
                TradingPost.open(player);
                return;
            }
        }
        if (clickAction == 4) {
            if (PacketInteractionManager.checkNpcInteraction(player, npc, 4)) {
                return;
            }

            // Areas
            if (player.getController() != null) {
                if (player.getController().handleNpcOption(player, npc, 4)) {
                    return;
                }
            }
        }
    }
}
