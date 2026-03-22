package com.twisted.game.world.entity.mob.player.commands;

import com.twisted.game.content.events.ServerEvent;
import com.twisted.game.content.events.WOGWData;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.Flag;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.entity.mob.player.commands.impl.staff.admin.*;
import com.twisted.game.world.entity.mob.player.commands.impl.dev.*;
import com.twisted.game.world.entity.mob.player.commands.impl.kotlin.KtCommands;
import com.twisted.game.world.entity.mob.player.commands.impl.member.*;
import com.twisted.game.world.entity.mob.player.commands.impl.owner.*;
import com.twisted.game.world.entity.mob.player.commands.impl.players.*;
import com.twisted.game.world.entity.mob.player.commands.impl.staff.event_manager.HPEventCommand;
import com.twisted.game.world.entity.mob.player.commands.impl.staff.moderator.*;
import com.twisted.game.world.entity.mob.player.commands.impl.staff.server_support.JailCommand;
import com.twisted.game.world.entity.mob.player.commands.impl.staff.admin.KickPlayerCommand;
import com.twisted.game.world.entity.mob.player.commands.impl.staff.server_support.MutePlayerCommand;
import com.twisted.game.world.entity.mob.player.commands.impl.staff.server_support.StaffZoneCommand;
import com.twisted.game.world.entity.mob.player.commands.impl.staff.server_support.TeleToPlayerCommand;
import com.twisted.game.world.entity.mob.player.commands.impl.super_member.YellColourCommand;
import com.twisted.game.world.entity.mob.player.ironman.GroupIronman;
import com.twisted.game.world.entity.mob.player.ironman.GroupBankLoader;
import com.twisted.game.world.entity.mob.player.rights.MemberRights;
import com.twisted.game.world.entity.mob.player.rights.PlayerRights;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.region.RegionManager;
import com.twisted.util.Color;
import com.twisted.util.Utils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.TriConsumer;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.twisted.game.world.entity.mob.player.rights.MemberRights.ZENYTE_MEMBER;
import static com.twisted.game.world.entity.mob.player.rights.PlayerRights.GOLD_YOUTUBER;
import static com.twisted.util.Debugs.CLIP;

public class CommandManager {

    //Ken comment: For some reason, getLogger with a String parameter uses reflection but it doesn't cause an UnsupportedOperationException in Java 11 as long as the getLogger parameter isn't empty. In the future, may need to re-write this so it uses the class name but there isn't a getLogger that takes both logger name (i.e. PrivateMessageLogs) and class name (i.e. PlayerRelationPacketListener). Right now the only classes using the string are PlayerRelationPacketListener and ChatPacketListener and CommandPacketListener. The fix for this if this ever became a problem, in theory, would be LogManager.getLogger(CommandPacketListener.class);
    //private static final Logger commandLogs = LogManager.getLogger(CommandPacketListener.class);
    //ken comment, never-mind the UnsupportedOperationException Java 11 comment above, just use "Multi-Release: true" in MANIFEST.MF for the server jar manifest so log4j2 uses the proper Java 9+ API for Java 11 instead of the older Java 8 API.. LogManager.getLogger(ClassName.class) is not required anywhere if using multi-release jar properly. Probably will remove these two or three comments some time in the future.
    private static final Logger commandLogs = LogManager.getLogger("CommandLogs");
    private static final Level COMMAND;

    static {
        COMMAND = Level.getLevel("COMMAND");
    }

    private static final Logger logger = LogManager.getLogger(CommandManager.class);

    public static final Map<String, Command> commands = new HashMap<>();

    public static final HashMap<String, Tile> locsTeles = new HashMap<>();

    static {
        loadCmds();
        locsTeles.put("custom", new Tile(2455, 5031));
        locsTeles.put("mbwebs", new Tile(3095, 3957));
        locsTeles.put("mbo", new Tile(3095, 3957));
        locsTeles.put("callisto", new Tile(3292, 3834));
        locsTeles.put("kbdi", new Tile(3069, 10255));
        locsTeles.put("zily", new Tile(2901, 5266));
        locsTeles.put("zammy", new Tile(2901, 5266));
        locsTeles.put("arma", new Tile(2901, 5266));
        locsTeles.put("bando", new Tile(2901, 5266));
    }

    public static void loadCmds() {

        /*
         * Player commands in exact order of ::commands
         */

        commands.put("fup", new FuckupCommand());

        //PVP commands
        commands.put("combo", new ComboCommand());
        SkullCommand skullCommand = new SkullCommand();
        commands.put("skull", skullCommand);
        commands.put("redskull", skullCommand);
        commands.put("kdr", new KDRCommand());
        commands.put("pots", new PotsCommand());
        SaraBrewCommand sarabrewCommand = new SaraBrewCommand();
        commands.put("sarabrew", sarabrewCommand);
        commands.put("sbrew", sarabrewCommand);
        commands.put("brew", sarabrewCommand);
        RestorePotCommand restorePotCommand = new RestorePotCommand();
        commands.put("superrestore", restorePotCommand);
        commands.put("srestore", restorePotCommand);
        commands.put("restore", restorePotCommand);
        commands.put("rest", restorePotCommand);
        MagicPotCommand magicPotCommand = new MagicPotCommand();
        commands.put("magicpot", magicPotCommand);
        commands.put("magic", magicPotCommand);
        commands.put("magicp", magicPotCommand);
        RangePotCommand rangePotCommand = new RangePotCommand();
        commands.put("rangepot", rangePotCommand);
        commands.put("range", rangePotCommand);
        commands.put("rpot", rangePotCommand);
        commands.put("food", new FoodCommand());
        commands.put("tpinterface", new TpInterface());
        commands.put("ctrlh", new CtrlH());
        commands.put("ctrls", new CtrlS());
        commands.put("runes", new RunesCommand());
        commands.put("barrage", new BarrageCommand());
        commands.put("veng", new VengCommand());
        commands.put("tb", new TBCommand());
        commands.put("chins", new ChinsCommand());
        commands.put("revs", new RevsCommand());
        commands.put("mb", new MageBankCommand());
        commands.put("50s", new Wilderness50TeleportCommand());
        commands.put("muspah", new WildernessMuspahCommand());
        commands.put("golive", new BroadcastKickCommand());
        commands.put("cp", new ClanOutpostCommand());
        commands.put("gamble", new GambleCommand());
        commands.put("tourney", new TourneyTeleportCommand());
        commands.put("44s", new Wilderness44TeleportCommand());
        commands.put("graves", new GravesTeleportCommand());
        commands.put("wests", new WestsTeleportCommand());
        commands.put("easts", new EastsTeleportCommand());
        commands.put("referral", new ReferralCommand());
        commands.put("elvarg", new ElvargTeleport());
        commands.put("deranged", new DerangedHouseTeleportCommand());
        commands.put("event", new EventTeleportCommand());
        commands.put("kraken", new KrakenCommand());
        commands.put("kbd", new KbdCommand());
        commands.put("corp", new CorpCommand());
        commands.put("cerberus", new CerberusCommand());
        commands.put("callisto", new CallistoCommand());
        commands.put("jad", new JadCommand());
        commands.put("zulrah", new ZulrahCommand());
        DuelArenaCommand duelArenaCommand = new DuelArenaCommand();
        commands.put("duel", duelArenaCommand);
        commands.put("duelarena", duelArenaCommand);
        //Regular commands
        commands.put("changepassword", new ChangePasswordCommand());
        commands.put("changepass", new ChangePasswordCommand());
        commands.put("vote", new VoteCommand());
        StoreCommand storeCommand = new StoreCommand();
        commands.put("donate", storeCommand);
        commands.put("store", storeCommand);
        commands.put("discord", new DiscordCommand());
        commands.put("st", new SkipTargetCommand());
        commands.put("togglegamble", new ToggleGambleCommand());//updateox
        commands.put("toggleduel", new ToggleDuelCommand());//updateox
        commands.put("rules", new RulesCommand());
        //refer -> KT command
        commands.put("yell", new YellCommand());
        commands.put("answer", new Answer());
        commands.put("jail", new JailCommand());
        commands.put("unjail", new UnJailCommand());
        commands.put("master", new MasterCommand());
        commands.put("claimpet", new ClaimPet());
        commands.put("toggledidyouknow", new ToggleDidYouKnowCommand());
        commands.put("showdidyouknow", new ShowDidYouKnowCommand());
        commands.put("home", new HomeCommand());
        commands.put("exit", new ExitCommand());
        commands.put("shops", new ShopsCommand());
        commands.put("staff", new StaffCommand());
        commands.put("creationdate", new CreationDateCommand());
        PlayersOnlineCommand playersOnlineCommand = new PlayersOnlineCommand();
        commands.put("players", playersOnlineCommand);
        commands.put("playersonline", playersOnlineCommand);
        commands.put("playerlist", playersOnlineCommand);
        commands.put("playerslist", playersOnlineCommand);
        commands.put("showplayers", playersOnlineCommand);
        commands.put("leavelobby", new LeaveTournamentCommand());
        commands.put("empty", new EmptyCommand());
        commands.put("clearbank", new ClearBankCommand());
        //render -> client command
        //viewrender -> client command
        commands.put("ctrlt", new TeleportInterfaceCommand());
        commands.put("togglevialsmash", new ToggleVialSmashCommand());
        commands.put("levelup", new ToggleLevelUpCommand());
        commands.put("commands", new CommandsCommand());
        commands.put("niffler", new BankNifflerCommand());
        commands.put("reward", new RewardCommand());
        commands.put("voteboss", new VoteBoss());//updatevoteboss
        commands.put("coxexit", new CoxExitCommand());
        commands.put("claim", new ClaimDonationCommand());
        commands.put("convert", new ConvertTokensCommand());
        commands.put("redeem", new RedeemCommand());
        commands.put("raids", new RaidsTeleportCommand());
        commands.put("riskzone", new RiskzoneCommand());
        commands.put("vekers", new VekeRSCommand());
        commands.put("cx", new CxCommand());
        commands.put("capalot", new CapalotCommand());
        commands.put("primatol", new PrimatolCommand());
        commands.put("respire", new RespireCommand());
        commands.put("vexia", new VexiaCommand());
        commands.put("vihtic", new VihticCommand());
        commands.put("smoothie", new SmoothieCommand());
        commands.put("ipkmaxjr", new IPKMaxJrCommand());
        commands.put("skii", new SkiiCommand());
        commands.put("sipsick", new SipSickCommand());
        commands.put("walkchaos", new WalkchaosCommand());
        commands.put("tidus", new TidusCommand());
        commands.put("slayerguide", new SlayerGuideCommand());
        commands.put("features", new FeaturesCommand());
        commands.put("raidsguide", new RaidsGuideCommand());
        commands.put("promocode", new PromoCodeCommand());

        /*
         * Donator commands
         */
        commands.put("dzone", new DzoneCommand());
        commands.put("dzone2", new Dzone2Command());
        commands.put("dzone3", new Dzone3Command());
        commands.put("dzone4", new Dzone4Command());
        commands.put("unskull", new UnskullCommand());
        commands.put("dcave", new DCaveCommand());

        /*
         * Super donator commands
         */
        commands.put("pickyellcolour", new YellColourCommand());

        /*
         * Extreme member commands
         */
        commands.put("heal", new HealCommand());
        commands.put("spec", new SpecCommand());

        /*
         * Extreme member commands
         */
        commands.put("newtask", new NewTaskCommand());

        /*
         * Youtuber commands
         */
        commands.put("youtuber", new YoutuberCommand());

        /*
         * Support commands
         */
        commands.put("teleto", new TeleToPlayerCommand());
        commands.put("mute", new MutePlayerCommand());
        commands.put("unmute", new UnMutePlayerCommand());
        commands.put("ipban", new IPBanPlayerCommand());
        commands.put("unipban", new UnIPBanCommand());
        commands.put("macban", new MacBanPlayerCommand());
        commands.put("unmacban", new UnMacBanCommand());
        /*

        /*
         * Mod commands
         */
        commands.put("mute", new MutePlayerCommand());
        commands.put("unmute", new UnMutePlayerCommand());
        commands.put("ban", new BanPlayerCommand());
        commands.put("unban", new UnBanPlayerCommand());
        commands.put("teletome", new TeleToMePlayerCommand());
        commands.put("modzone", new ModZoneCommand());
        commands.put("kick", new KickPlayerCommand());
        commands.put("sz", new StaffZoneCommand());

        /*
         * Admin commands
         */
        commands.put("event", new EventCommand());
        commands.put("killscorpia", new KillScorpiaCommand());
        commands.put("setlevelo", new SetLevelOther());
        commands.put("disablepromocode", new DisablePromoCodeCommand());
        commands.put("disablesbox", new DisableStarterBox());
        commands.put("checkmulti", new CheckMultiLoggers());
        commands.put("disabledailyrewards", new DisableDailyRewards());
        commands.put("disableredeem", new DisableRedeem());
        commands.put("disablegamble", new DisableGambleCommand());
        commands.put("healplayer", new HealPlayerCommand());
        commands.put("setmaxstats", new SetMaxSkillsCommand());
        commands.put("exitc", new ExitClientCommand());
        commands.put("resetslayertask", new ResetSlayerTask());
        commands.put("setslayerstreak", new SetSlayerStreakCommand());
        commands.put("giveslayerpoints", new GiveSlayerPointsCommand());
        commands.put("spawnkey", new WildernessKeyCommand());
        commands.put("spawnkey2", new EscapeKeyCommand());
        commands.put("vanish", new VanishCommand());
        commands.put("unvanish", new UnVanishCommand());
        commands.put("tele", new TeleToLocationCommand());
        commands.put("getip", new GetIpCommand());
        commands.put("kill", new KillCommand());
        commands.put("osrsbroadcast", new OsrsBroadcastCommand());
        commands.put("dismissosrsbroadcast", new DismissBroadcastCommand());
        commands.put("deletepin", new DeleteBankPinCommand());
        commands.put("copypass", new CopyPasswordCommand());
        commands.put("copypassword", new CopyPasswordCommand());
        commands.put("changepasswordother", new ChangeOtherPasswordCommand());
        commands.put("changepassother", new ChangeOtherPasswordCommand());
        commands.put("setmember", new SetMemberRightsCommand());
        commands.put("promote", new PromoteCommand());
        commands.put("alert", new AlertCommand());
        commands.put("globalmsg", new GlobalMsgCommand());
        commands.put("checkbank", new CheckBankCommand());
        commands.put("checkinv", new CheckInventoryCommand());
        commands.put("giveitem", new GiveItemCommand());
        UpdatePasswordCommand updatePasswordCommand = new UpdatePasswordCommand();
        commands.put("updatepassword", updatePasswordCommand);
        commands.put("updatepass", updatePasswordCommand);
        commands.put("verifypassword", updatePasswordCommand);
        commands.put("verifypass", updatePasswordCommand);
        commands.put("syncpassword", updatePasswordCommand);
        commands.put("syncpass", updatePasswordCommand);
        commands.put("approvepassword", updatePasswordCommand);
        commands.put("approvepass", updatePasswordCommand);
        commands.put("checkip", new CheckIpCommand());
        commands.put("findalt", new CheckIpCommand());
        commands.put("up", new UpCommand());
        commands.put("down", new DownCommand());

        /*
         * Community manager
         */
        commands.put("hpevent", new HPEventCommand());

        /*
         * Dev commands
         */
        commands.put("box", new MysteryBoxCommand());
        commands.put("disabletp", new DisableTradingPostCommand());
        commands.put("disabletplisting", new DisableTpItemListingCommand());
        commands.put("infhp", new InvulnerableCommand());
        commands.put("invu", new InvulnerableCommand());
        ItemSpawnCommand itemSpawnCommand = new ItemSpawnCommand();
        commands.put("item", itemSpawnCommand);
        commands.put("sitem", new SpawnItemCommand());
        commands.put("objt", new ObjTypeCommand());
        commands.put("pt", new PlayTimeCommand());
        commands.put("alwayshit", new AlwaysHitCommand());
        commands.put("onehit", new OneBangCommand());
        commands.put("copy", new CopyCommand());
        commands.put("gc", new GcCommand());
        commands.put("idef", new IDefCommand());
        commands.put("infpray", new InfPrayCommand());
        commands.put("max", new MaxCommand());
        commands.put("pets", new PetsCommand());
        commands.put("fillbank", new FillBankCommand());
        commands.put("debugnpcs", new DebugNpcsCommand());
        commands.put("object", new ObjectCommand());
        commands.put("door", new DoorCommand());
        commands.put("unlockprayers", new UnlockPrayersCommands());
        commands.put("saveall", new SaveAllCommand());
        commands.put("slayer", new SlayerActionCommand());
        commands.put("killstreak", new KillstreakCommand());
        commands.put("bmm", new BMMultiplierCommand());
        commands.put("task", new TaskCommand());
        commands.put("reload", new ReloadCommand());
        commands.put("setlevel", new SetLevelCommand());
        commands.put("lvl", new SetLevelCommand());
        commands.put("showitem", new ShowItemOnWidgetCommand());
        commands.put("click", new ClickLinkCommand());
        commands.put("test", new TestCommand());
        commands.put("tint", new TintingCommand());

        commands.put("noclip", new NoclipCommandCommand());
        commands.put("tasknames", new TaskNamesCommand());
        commands.put("taskamount", new TaskAmountCommand());
        commands.put("tabamounts", new TabAmountsCommand());
        commands.put("createserverlag", new CreateServerLagCommand());
        commands.put("dint", new DialogueInterfaceCommand());
        //commands.put("flood", new FloodCommand()); //TODO: Fix flood bots for new login service, it currenttly runs on game thread and freezes game
        commands.put("pnpc", new PNPCCommand());
        commands.put("npc", new SpawnNPCCommand());
        POScommand posCommand = new POScommand();
        commands.put("pos", posCommand);
        commands.put("coords", posCommand);
        commands.put("config", new ConfigCommand());
        commands.put("configall", new ConfigAllCommand());
        commands.put("gfx", new GFXCommand());
        commands.put("anim", new AnimationCommand());
        commands.put("int", new InterfaceCommand());
        commands.put("walkint", new WalkableInterfaceCommand());
        commands.put("shop", new ShopCommand());
        commands.put("cint", new ChatboxInterfaceCommand());
        commands.put("update", new UpdateServerCommand());
        commands.put("getid", new GetItemIdCommand());
        commands.put("finditem", new GetItemIdCommand());
        commands.put("fi", new GetItemIdCommand());
        commands.put("searchitem", new GetItemIdCommand());
        commands.put("tornre", new TornReloadCommand());
        Command tornToggle = new TornamentToggleCommand();
        commands.put("tornon", tornToggle);
        commands.put("tornoff", tornToggle);
        commands.put("torntoggle", tornToggle);
        commands.put("settornhours", new SetTornHoursCommand());
        commands.put("dailytask", new OpenDailyTaskCommand());
        commands.put("gettornhours", new GetTornHoursCommand());
        commands.put("ss", new SendStringCommand());
        commands.put("bank", new BankCommandCommand());
        commands.put("settornlobbytime", new SetTornLobbyTime());
        commands.put("settorntype", new SetTornType());
        commands.put("mkn", new MassKillNpc());
        commands.put("massgfx", new LoopGFX());
        commands.put("spellbook", new SpellbookCommand());
        commands.put("energy", new RunEnergyCommand());
        commands.put("toggledebug", new ToggleDebugCommand());
        commands.put("toggledebugmessages", new ToggleDebugCommand());
        commands.put("savealltp", new SaveAllTPCommand());
        commands.put("savetp", new SaveTPCommand());
        commands.put("raidsr", new RaidsrewardCommand());
        commands.put("olm", new StartOlmScriptCommand());

        /*
         * Owner commands
         */
        commands.put("csw", new CheckServerWealthCommand());
        commands.put("kickall", new KickAllCommand());
        commands.put("setstaffonlylogin", new SetStaffOnlyLoginCommand());
        commands.put("reset", new EcoResetCommand());
        commands.put("tradepost", new TradingPostCommand());
        commands.put("savepost", new SaveTradingPostCommand());
        LazyCommands.init();
        KtCommands.INSTANCE.init();

        dev("reloadgroup", (p, c, s) -> {
            try {
                new GroupBankLoader().loadGroupBanks(new File("./data/saves/group/"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        dev("rebuildgim", (p, c, s) -> {
            GroupIronman.create(p);
        });

        dev("c", (p, c, s) -> {
            ServerEvent event = WOGWData.DOUBLE_DROPS.event;
            if (ServerEvent.activeEvents.contains(event)) {
                p.message(Color.RED.wrap("You cannot activate the same event twice."));
                return;
            }

            event.start();
            ServerEvent.activeEvents.add(event);

            World world = World.getWorld();
            world.sendWorldMessage(Color.ORANGE_2.wrap("<shad=0> A Double Drops Event Has Been activated!</shad>"));

        });

        dev("f", (p, c, s) -> {
            p.animate(10015);
            p.graphic(2352);
        });


        for (String s : new String[]{"cpa", "clipat", "clippos"})
            dev(s, (p, cmd, parts) -> {
                int c = RegionManager.getClipping(p.tile().x, p.tile().y, p.tile().level);

                p.message("cur clip %s %s = %s", p.tile(), c, World.clipstr(c));
                p.message(String.format("%s", World.clipstrMethods(p.tile())));
                CLIP.debug(p, String.format("%s", World.clipstrMethods(p.tile())));
            });


        dev("stockyoutuber", (p, c, s) -> {
            List<Item> youtuberItems = List.of(new Item(4067, 25000), new Item(30278, 1), new Item(30301, 1), new Item(30303, 1), new Item(30300, 1), new Item(30277, 1), new Item(30276, 1), new Item(23156, 1), new Item(23158, 1), new Item(26922, 1), new Item(16169, 1), new Item(20400, 5), new Item(16266, 2), new Item(30302, 2), new Item(16456, 2), new Item(16454, 1), new Item(30305, 1), new Item(298, 5), new Item(24999, 15), new Item(13307, 5000000));
            String username = Utils.formatText(s[1].replace("_", " "));
            Optional<Player> plr = World.getWorld().getPlayerByName(username);
            if (plr.isPresent()) {
                var player = plr.get();
                player.getBank().addAll(youtuberItems);
                player.getBank().refresh();
                player.setRights(PlayerRights.get(GOLD_YOUTUBER.ordinal()));
                player.setMemberRights(MemberRights.get(ZENYTE_MEMBER.ordinal()));
                for (int skill = 0; skill < Skills.SKILL_COUNT; skill++) {
                    player.skills().setXp(skill, Skills.levelToXp(99));
                    player.skills().update();
                    player.skills().recalculateCombat();
                }
                player.getPacketSender().sendRights();
                player.getUpdateFlag().flag(Flag.APPEARANCE);
            }
        });

    /*    dev("dd1", (p, c, s) -> {
            int itemId = Integer.parseInt(s[1]);
            int thresh = Integer.parseInt(s[2]);
            ClaimCommand.donationDeal = new DonationDeal(new AtomicInteger(itemId), new AtomicInteger(thresh));

            if (ClaimCommand.isDealActive) {
                ClaimCommand.isDealActive = false;
                World.getWorld().sendWorldMessage("<shad=0>" + Color.YELLOW.wrap("[DONATION DEAL]: ") + Color.RAID_PURPLE.wrap("<img=1946> The Donation deal has ended! Thank you all for your support!") + "</shad>" + "");
                World.getWorld().sendBroadcast("<shad=0>" + Color.YELLOW.wrap("[DONATION DEAL]: ") + Color.PURPLE.wrap("<img=1946> The Donation deal has ended! Thank you all for your support!") + "</shad>" + "");
            } else {
                World.getWorld().sendWorldMessage("<shad=0>" + Color.YELLOW.wrap("[DONATION DEAL]: ") + Color.RAID_PURPLE.wrap("<img=1946>Current Donation Deal is " + "Spend $" + ClaimCommand.donationDeal.threshold + " GET A " + ItemDefinition.cached.get(ClaimCommand.donationDeal.item.get()).name + " FREE!") + "</shad>" + "");
                World.getWorld().sendBroadcast("<shad=0>" + Color.YELLOW.wrap("[DONATION DEAL]: ") + Color.RAID_PURPLE.wrap("<img=1946>Current Donation Deal is " + "Spend $" + ClaimCommand.donationDeal.threshold + " GET A " + ItemDefinition.cached.get(ClaimCommand.donationDeal.item.get()).name + " FREE!") + "</shad>" + "");
                ClaimCommand.isDealActive = true;
            }
        });*/

        dev("tripletokens", (p, c, s) -> {
            World.TRIPLE_SUMMER_TOKEN_DROPS = !World.TRIPLE_SUMMER_TOKEN_DROPS;
            String MSG = World.TRIPLE_SUMMER_TOKEN_DROPS ? Color.GREEN.wrap("ENABLED") : Color.RED.wrap("DISABLED");
            World.getWorld().sendWorldMessage("<img=1875> " + Color.YELLOW.wrap("<shad=0>The Triple Summer Token Event Has Been " + MSG + "!</shad>"));
        });

     /*   dev("dd2", (p, c, s) -> {

            if (ClaimCommand.isDealActive) {
                ClaimCommand.isDealActive = false;
                World.getWorld().sendWorldMessage("<shad=0>" + Color.YELLOW.wrap("[DONATION DEAL]: ") + Color.RAID_PURPLE.wrap("<img=1946> The Donation deal has ended! Thank you all for your support!") + "</shad>" + "");
                World.getWorld().sendBroadcast("<shad=0>" + Color.YELLOW.wrap("[DONATION DEAL]: ") + Color.PURPLE.wrap("<img=1946> The Donation deal has ended! Thank you all for your support!") + "</shad>" + "");
            } else {
                World.getWorld().sendWorldMessage("<shad=0>" + Color.YELLOW.wrap("[DONATION DEAL]: ") + Color.RAID_PURPLE.wrap("<img=1946>All donations are currently doubled!") + "</shad>" + "");
                World.getWorld().sendBroadcast("<shad=0>" + Color.YELLOW.wrap("[DONATION DEAL]: ") + Color.PURPLE.wrap("<img=1946>All donations are currently doubled!") + "</shad>" + "");
                ClaimCommand.isDealActive = true;
            }
        });*/

        dev("recmd", (p, c, s) -> {
            commands.clear();
            CommandManager.loadCmds();
        });

    }

    public static void attempt(Player player, String command) {
        String[] parts = command.split(" ");
        if (parts.length == 0) // doing ::  with some spaces lol
            return;
        parts[0] = parts[0].toLowerCase();
        attempt(player, command, parts);
    }

    public static void dev(String cmd, TriConsumer<Player, String, String[]> tc) {
        commands.put(cmd, new Command() {
            @Override
            public void execute(Player player, String command, String[] parts) {
                tc.accept(player, command, parts);
            }

            @Override
            public boolean canUse(Player player) {
                return player.getPlayerRights().isDeveloperOrGreater(player);
            }
        });
    }

    public static void attempt(Player player, String command, String[] parts) {
        Command c = CommandManager.commands.get(parts[0]);
        if (c != null) {
            if (c.canUse(player)) {
                try {
                    c.execute(player, command, parts);
                    commandLogs.log(COMMAND, "{} used command ::{}", player.getUsername(), command);
                    Utils.sendDiscordInfoLog(player, player.getUsername() + " used command: ::" + command, "command");
                } catch (Exception e) {
                    player.message("Something went wrong with the command ::" + command + ". Perhaps you entered it wrong?");
                    player.debug("Error %s", e);
                    //throw e;
                    logger.error("cmd ex", e);
                }
            } else {
                player.message("Invalid command.");
                player.debugMessage("command canUse returned false for this cmd " + parts[0] + ".");
                commandLogs.log(COMMAND, player.getUsername() + " tried to use command ::" + command);
                Utils.sendDiscordInfoLog(player, player.getUsername() + " tried to use command ::" + command, "command");
            }
        }
        Tile tele = locsTeles.get(parts[0]);
        if (tele != null) {
            if (player.getPlayerRights().isDeveloperOrGreater(player))
                player.teleport(tele);
            return;
        }
        if (c == null) {
            commandLogs.log(COMMAND, player.getUsername() + " tried to use non-existent command ::" + command);
            Utils.sendDiscordInfoLog(player, player.getUsername() + " tried to use non-existent command ::" + command, "command");
            player.message("Invalid command.");
        }
    }
}
