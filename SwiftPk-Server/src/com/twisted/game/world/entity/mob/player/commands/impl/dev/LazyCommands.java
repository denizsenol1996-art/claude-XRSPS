package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.fs.ObjectDefinition;
import com.twisted.game.GameConstants;
import com.twisted.game.content.DropsDisplay;
import com.twisted.game.content.mechanics.Poison;
import com.twisted.game.content.mechanics.referrals.Referrals;
import com.twisted.game.content.skill.impl.hunter.Hunter;
import com.twisted.game.content.skill.impl.hunter.trap.impl.Chinchompas;
import com.twisted.game.content.treasure.TreasureRewardCaskets;
import com.twisted.game.task.Task;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.Venom;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.entity.mob.player.commands.CommandManager;
import com.twisted.game.world.entity.mob.player.save.PlayerSave;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.ground.GroundItem;
import com.twisted.game.world.items.ground.GroundItemHandler;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.MapObjects;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.region.RegionManager;
import com.twisted.test.generic.ChainWorkTest;
import com.twisted.util.Debugs;
import com.twisted.util.ItemIdentifiers;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;
import kotlinsave.save.PlayerSaves;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.*;

public class LazyCommands {

    public static void byLazy(String key, TriConsumer<Player,String,String[]> consumer) {
        CommandManager.commands.put(key, new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                consumer.accept(player, command, parts);
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
    }

    public static void init() {
        Map<String, Command> commands = CommandManager.commands;
        /*
         * Misc testing commands
         */
        commands.put("ztest", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.message("testing");
                //System.out.println("start");
                ChainWorkTest.test();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("vorkath", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.teleport(2273, 4049);
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        // soz for spam xd
        commands.put("hitt1", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.message("ok");
                player.lockDelayDamage();
                player.hit(player,5);
                player.hit(player,5);
                Chain.bound(player).name("hitt1Task").runFn(5, () -> {
                    player.unlock();
                    player.message("unlock 5t later");
                });
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("hitt2", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.message("ok");
                player.lockNoDamage();
                player.hit(player,5);
                player.hit(player,5);
                Chain.bound(player).name("hitt2Task").runFn(5, () -> {
                    player.unlock();
                    player.message("unlock 5t later - expect damage to be nullified");
                });
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("hitt3", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.message("ok");
                player.lockDamageOk();
                player.hit(player,5);
                player.hit(player,5);
                Chain.bound(player).name("hitt3Task").runFn(5, () -> {
                    player.unlock();
                    player.message("unlock 5t later - expect damage to have appeared instantly, but we cant move");
                });
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("recmd", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                commands.clear();
                CommandManager.loadCmds();
                player.message("Commands have been reloaded.");
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("waittest", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                commands.clear();
                Chain.bound(player).name("waittest").waitForTile(player.tile().transform(3, 0, 0), () -> {
                    player.message("arrived");
                });
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("huntt1", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                for (int i = 0; i < 100; i++) {
                    Chain.bound(player).name("huntt1").runFn(1 + i, () -> {
                        player.teleport(player.tile().transform(1, 0, 0)); // move right every tick
                        World.getWorld().registerNpc(new Npc(2912, player.tile()));
                    }).then(1, () -> {
                        Hunter.lay(player, new Chinchompas(player));
                    });
                }
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
        commands.put("hit", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.hit(player, Utils.random(40), 1, CombatType.MELEE).checkAccuracy().submit();
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isAdminOrGreater(player);
            }
        });

        byLazy("save", (p,cmd,parts) -> {
            PlayerSaves.requestSave(p);
            p.message("Saving " + p.getUsername());
        });
        for (String s : new String[] {"cpa", "clipat", "clippos"})
            byLazy(s, (p,cmd,parts) -> {
                int c = RegionManager.getClipping(p.tile().x, p.tile().y, p.tile().level);

                p.message("cur clip %s = %s", c, World.clipstr(c));
                p.message(String.format("%s", World.clipstrMethods(p.tile())));
                Debugs.CLIP.debug(p, String.format("%s", World.clipstrMethods(p.tile())));
            });
        byLazy("scm", (player,cmd,parts) -> {
            ArrayList<GroundItem> gis = new ArrayList<>();
            int baseitem = 1;
            int radius = parts.length > 1 ? Integer.parseInt(parts[1]) : 4;
            for (int x = player.getX() - radius; x < player.getX() + radius; x++) {
                for (int y = player.getY() - radius; y < player.getY() + radius; y++) {
                    int clip = RegionManager.getClipping(x, y, player.getZ());
                    int item = 0;
                    item = clip==0 ? 227 : baseitem++;
                    Debugs.CLIP.debug(player, String.format("%s is %s %s = %s %s", new Tile(x,y,player.getZ()), item, new Item(item).name(),
                        clip, World.getWorld().clipstr(clip)));
                    if (clip != 0) {
                        GroundItem gi = new GroundItem(new Item(item, 1), Tile.create(x, y, player.tile().level), player);
                        player.getPacketSender().createGroundItem(gi);
                        gis.add(gi);
                    }
                }
            }
            Task.runOnceTask(10, c -> {
                gis.forEach(GroundItemHandler::sendRemoveGroundItem);
            });
        });
        byLazy("rpk", (p,cmd,s) -> {
            // makes a npc index attack us, used for clipping testing
            World.getWorld().getNpcs().get(Integer.parseInt(s[1])).getCombat().attack(p);
        });
        byLazy("odef", (p,cmd,s) -> {
            ObjectDefinition def = World.getWorld().definitions().get(ObjectDefinition.class, Integer.parseInt(s[1]));
            p.message(def.name+" "+def.tall +" "+def.tall);
        });
        byLazy("dprints", (p,cmd,s) -> {
            // usage is ::dprints tftftftf for true/false in order of each Debug enum
            for (int i = 0; i < Math.min(s[1].length(), Debugs.values().length); i++) {
                //noinspection StringOperationCanBeSimplified
                Debugs.values()[i].enabled = s[1].substring(i, i + 1).equals("t");
                //noinspection StringOperationCanBeSimplified
                System.out.println(Debugs.values()[i].name()+" "+Debugs.values()[i].enabled + " by " + s[1].substring(i, i + 1).equals("t"));
            }
        });
        byLazy("dp2", (p,cmd,s) -> {
            // usage is ::dprints tftftftf for true/false in order of each Debug enum
            for (int i = 0; i < s[1].length(); i++) {
                //noinspection StringOperationCanBeSimplified
                int id = Integer.parseInt(s[1].substring(i, i + 1));
                Debugs.values()[id].enabled = !Debugs.values()[id].enabled;
                //noinspection StringOperationCanBeSimplified
                System.out.println(Debugs.values()[id].name()+" "+Debugs.values()[id].enabled);
            }
        });

        commands.put("drop", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                DropsDisplay.start(player);
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });

        commands.put("sidebar", new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                player.getInterfaceManager().setSidebar(GameConstants.LOGOUT_TAB, 46500);
                player.message("sending sidebar");
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });


        byLazy("up", (p,cmd,parts) -> {
            p.teleport(p.tile().transform(0,0,1));
        });
        byLazy("down", (p,cmd,parts) -> {
            p.teleport(p.tile().transform(0,0,-1));
        });
        byLazy("openpresets", (p,cmd,parts) -> {
            p.getPresetManager().open();
        });
        byLazy("objsat", (p,cmd,parts) -> {
            int total = MapObjects.mapObjects.size();
            boolean ignoreZ = (parts.length < 2 ? 0 : Integer.parseInt(parts[1])) == 1;
            long hash = MapObjects.getHash(p.getX(), p.getY(), 0);
            ArrayList<GameObject> gameObjects = MapObjects.mapObjects.get(hash);
            if (gameObjects == null)
                gameObjects = new ArrayList<>(0);
            p.message(String.format("total %s objs cached. %s at pos", total, gameObjects.size()));
            System.out.println(Arrays.toString(gameObjects.toArray()));
        });

        byLazy("iot1", (p,cmd,parts) -> {
            // fuck me up fam
            p.putAttrib(AttributeKey.BGS_GFX_GOLD, 1); // expected type = Boolean
            p.requestLogout();
        });
        byLazy("bc1", (p,cmd,parts) -> { // banking case 1
            p.getBank().clear();
            p.getBank().addAll(
                new Item(ItemIdentifiers.RING_OF_SUFFERING, 50),
                new Item(ItemIdentifiers.RING_OF_RECOIL, 50),
                new Item(ItemIdentifiers.RING_OF_SUFFERING_R),
                new Item(ItemIdentifiers.RING_OF_SUFFERING_R)
            );
            p.getBank().tabAmounts = new int[10];
            p.getBank().tabAmounts [0] = 4;
            p.inventory().clear();
            p.inventory().addAll(
                new Item(ItemIdentifiers.FIRE_RUNE, 1000),
                new Item(ItemIdentifiers.NATURE_RUNE, 1000),
                new Item(ItemIdentifiers.RING_OF_RECOIL+1, 100)
            );
        });
        byLazy("ps", (p,cmd,parts) -> { // save .. dont have to relog to refresh json file
            PlayerSave.save(p);
        });
        byLazy("openmaster", (p,cmd,parts) -> {
            int amount = Integer.parseInt(parts[1]);
            p.inventory().add(new Item(19836, amount));
            for (int i = 0; i < amount; i++) {
                TreasureRewardCaskets.openCasket(p, new Item(19836));
            }
        });
        byLazy("obj", (p,cmd,parts) -> {
            int id = Integer.parseInt(parts[1]);
            int type = parts.length >= 3 ? Integer.parseInt(parts[2]) : 10;
            int rot = parts.length >= 4 ? Integer.parseInt(parts[3]) : 0;
            p.getPacketSender().sendObject(new GameObject(id, p.tile().copy(), type, rot));
        });
        byLazy("ndrop", (p,cmd,parts) -> {
            DropsDisplay.clickActions(p, 35143);
        });
        byLazy("venom", (p,cmd,parts) -> {
            p.venom(p);
        });
        byLazy("curevenom", (p,cmd,parts) -> {
            Venom.cure(2, p);
        });
        byLazy("curepoison", (p,cmd,parts) -> {
            Poison.cure(p);
        });
        byLazy("tpro", ((player, s, strings) -> {
            new Projectile(player.getCentrePosition(), player.tile().transform(3, 3), 0, 1482, 65, 20, 20, 20, 1).sendProjectile();
        }));
        byLazy("tpro2", ((player, s, strings) -> {
            World.getWorld().getNpcs().filter(Objects::nonNull).min(Comparator.comparingInt(o -> o.tile().distance(player.tile()))).ifPresent(n -> {
                n.forceChat("hit me");
                new Projectile(player, n, 1482, 65, 150, 20, 20, 1).sendProjectile();
            });
        }));
        byLazy("askref", (player, s, strings) -> {
            Referrals.INSTANCE.askReferrerName(player);
        });
        byLazy("cia4", (player, s, strings) -> {
            throw new RuntimeException("testy");
        });
    }
}
