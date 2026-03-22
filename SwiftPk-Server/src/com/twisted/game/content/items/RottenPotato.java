package com.twisted.game.content.items;

import com.twisted.GameServer;
import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.entity.mob.player.commands.CommandManager;
import com.twisted.game.world.entity.mob.player.ironman.GroupIronman;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.ObjectManager;
import com.twisted.game.world.position.Tile;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Debugs;

import java.util.Optional;

import static com.twisted.util.ItemIdentifiers.ROTTEN_POTATO;

/**
 * item 5733
 * <br>
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * mei 17, 2020
 */
public class RottenPotato extends PacketInteraction {

    public static boolean onItemOnMob(Player player, Mob target) {
        // Give you the name and distance to a target
        if (player.getPlayerRights().isDeveloperOrGreater(player)) {
            if (target.isPlayer()) {
                player.debugMessage(String.format("Distance to %s (%d) : %d. ", (target.getAsPlayer()).getUsername(), target.getIndex(), player.tile().distance(target.tile())));
            } else {
                Debugs.CMB.debug(player, String.format("on %s %s", target, target.tile()), target, true);
                //System.out.println(String.format("on %s %s", target, target.tile()));
                player.getMovementQueue().clear();
                //System.out.println("mob pid "+target.getIndex());
                Npc npc = (Npc) target;
                player.debugMessage(String.format("Distance to %s (%d) : %d size %d. ", npc.def().name, target.getIndex(), player.tile().distance(target.tile()), npc.getSize()));
                potatoOnMob(player, npc);
            }
            return true;
        }
        return false;
    }

    private static void potatoOnMob(Player player, Npc npc) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Options for NPC "+ npc.def().name, "Kill NPC.", "Despawn NPC.", "Teleport to me.", "Transmog.", "Replace.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(option == 1) {
                    if(npc.dead())
                        player.message(""+ npc.def().name+" is dead.");
                    if (npc.id() == 6611)
                        npc.clearAttrib(AttributeKey.VETION_HELLHOUND_SPAWNED);
                    npc.hit(player, npc.hp(), 1);
                } else if(option == 2) {
                    World.getWorld().unregisterNpc(npc);
                } else if(option == 3) {
                    npc.teleport(player.tile());
                } else if(option == 4) {
                    player.setEnterSyntax(new EnterSyntax() {
                        @Override
                        public void handleSyntax(Player player, long id) {
                            npc.transmog(id == 0 ? -1 : (int) id);
                        }
                    });
                    player.getPacketSender().sendEnterAmountPrompt("Enter NPC ID (or 0 to reset)");
                } else if(option == 5) {
                    player.setEnterSyntax(new EnterSyntax() {
                        @Override
                        public void handleSyntax(Player player, long id) {
                            if (id > 0) {
                                World.getWorld().unregisterNpc(npc);
                                Npc newNpc = new Npc((int) id, npc.tile());
                                World.getWorld().registerNpc(newNpc);
                            }
                        }
                    });
                    player.getPacketSender().sendEnterAmountPrompt("Enter NPC ID (or 0 to cancel)");
                }
                stop();
            }
        });
    }

    public static void onItemOption1(Player player) {
        if (player.getPlayerRights().isDeveloperOrGreater(player)) {
            if (GameServer.properties().production) {
                potatoChat(player);
            } else {
                //CommandManager.attempt(player, "clipat");
                //CommandManager.attempt(player, "teleto testbot1");
                CommandManager.attempt(player, "hydra");
            }
        }
    }

    private static void potatoChat(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Op1", "Set all stats.", "Wipe inventory.", "Setup POH", "Teleport to player", "Spawn aggressive NPC.");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if (option == 1) {
                    stop();
                    player.setEnterSyntax(new EnterSyntax() {
                        @Override
                        public void handleSyntax(Player player, long lvl) {
                            lvl = Math.max(1, Math.min(99, lvl));
                            for (int i = 0; i < Skills.SKILL_COUNT; i++) {
                                player.skills().setXp(i, Skills.levelToXp((int) lvl));
                                player.skills().update();
                                player.skills().recalculateCombat();
                            }
                        }
                    });
                    player.getPacketSender().sendEnterAmountPrompt("Set to what level?");
                } else if (option == 2) {
                    stop();
                    player.inventory().clear();
                    player.inventory().add(new Item(ROTTEN_POTATO));
                } else if (option == 3) {
                    stop();
                    player.message("We don't have the Construction skill. This option isn't available.");
                } else if (option == 4) {
                    stop();
                    player.setEnterSyntax(new EnterSyntax() {
                        @Override
                        public void handleSyntax(Player player, String input) {
                            Optional<Player> teleportTo = World.getWorld().getPlayerByName(input);

                            if(teleportTo.isPresent()) {
                                player.teleport(teleportTo.get().tile());
                                player.message("You have teleported to "+teleportTo.get().getUsername()+".");
                            } else {
                                player.message(input+" is not online right now.");
                            }
                        }
                    });
                    player.getPacketSender().sendEnterInputPrompt("Teleport to?");
                } else if (option == 5) {
                    stop();
                    player.setEnterSyntax(new EnterSyntax() {
                        @Override
                        public void handleSyntax(Player player, long id) {
                            Npc npc = new Npc((int) id, new Tile(player.tile().x - 2, player.tile().y));
                            World.getWorld().registerNpc(npc);
                            if (npc.combatInfo() != null)
                                npc.combatInfo().aggressive = true;
                        }
                    });
                    player.getPacketSender().sendEnterAmountPrompt("Enter the npc ID");
                }
            }
        });
    }

    public static void onItemOption3(Player player) {
        if (player.getPlayerRights().isDeveloperOrGreater(player)) {
            //potatoOp3(player);
            if (!GameServer.properties().production) {
                //CommandManager.attempt(player, "addbotsvorkath 400");
            }
        }
    }

    private static void potatoOp3(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Op3", "Bank menu", "AMEs for all!", "Teleport to RARE!", "Spawn RARE!");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if (option == 1) {
                    player.getDialogueManager().start(new Dialogue() {
                        @Override
                        protected void start(Object... parameters) {
                            send(DialogueType.OPTION, "Op3", "Open bank.", "Set PIN to 2468.", "Wipe bank.");
                            setPhase(0);
                        }

                        @Override
                        protected void select(int option) {
                            if (option == 1) {
                                stop();
                                GroupIronman group = GroupIronman.getGroup(player.getUID());
                                if (group == null) {
                                    player.getBank().open();
                                } else {
                                    group.getGroupBank().open(player, group);
                                }
                            } else if (option == 2) {
                                /*player.getBankPin().setPinLength(4);
                                player.getBankPin().setHashedPin("2468");
                                player.message("Your bank pin is now 2468.");*/
                                stop();
                            } else if (option == 3) {
                                player.getBank().clear();
                            }
                        }
                    });
                } else if (option == 2) {
                    //TODO
                } else if (option == 3) {
                    //TODO
                } else if (option == 4) {
                    //TODO
                }
            }
        });
    }

    @Override
    public boolean handleEquipment(Player player, Item item) {
        if(item.getId() == ROTTEN_POTATO) {
            if (!GameServer.properties().production) {
                //CommandManager.attempt(player, "scm");
                CommandManager.attempt(player, "infhp");
            }
            return true;
        }
        return false;
    }

    public static void onItemOption2(Player player) {
        if (player.getPlayerRights().isDeveloperOrGreater(player)) {
            potatoOp2(player);
        }
    }

    private static void potatoOp2(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, "Op2", "Keep me logged in.", "Kick me out.", "Kill me.", "Transmogrify me...");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(option == 1) {
                    //TODO
                } else if(option == 2) {
                    player.requestLogout();
                } else if(option == 3) {
                    //player.typeLessHit(player.hitpoints());
                } else if(option == 4) {
                    player.setEnterSyntax(new EnterSyntax() {
                        @Override
                        public void handleSyntax(Player player, long id) {
                            player.looks().transmog((int) id);
                        }
                    });
                    player.getPacketSender().sendEnterAmountPrompt("Transmogrify me...");
                }
            }
        });
    }

    public static void used_on_object(Player player) {
        GameObject obj = player.getAttribOr(AttributeKey.INTERACTION_OBJECT, null);
        String name = obj.definition().name;
        //System.out.println(obj.definition().toStringBig());
        //System.out.println(obj.definition(World.getWorld()).toStringBig());
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Delete obj "+name, "Obj on tile count", "Clear object's attributes", "Nevermind");
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if(option == 1) {
                    ObjectManager.removeObj(obj);
                    player.message("removed object "+name+" at "+obj.tile()+".");
                    stop();
                } else if(option == 2) {
                    //TODO
                } else if(option == 3) {
                    //TODO
                } else if(option == 4) {
                    stop();
                }
            }
        });
    }
}
