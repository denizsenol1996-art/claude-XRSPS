package com.twisted.game.world.entity.mob.player;

import com.twisted.GameServer;
import com.twisted.game.GameConstants;
import com.twisted.game.GameEngine;
import com.twisted.game.content.EffectTimer;
import com.twisted.game.content.account.AccountChange;
import com.twisted.game.content.achievements.Achievements;
import com.twisted.game.content.areas.wilderness.content.RiskManagement;
import com.twisted.game.content.areas.wilderness.content.boss_event.WildernessBossEvent;
import com.twisted.game.content.areas.wilderness.content.key.WildernessKeyPlugin;
import com.twisted.game.content.areas.wilderness.content.todays_top_pkers.TopPkers;
import com.twisted.game.content.bank_pin.BankPin;
import com.twisted.game.content.bank_pin.BankPinSettings;
import com.twisted.game.content.clan.Clan;
import com.twisted.game.content.clan.ClanManager;
import com.twisted.game.content.collection_logs.CollectionLog;
import com.twisted.game.content.consumables.potions.impl.*;
import com.twisted.game.content.daily_tasks.DailyTaskManager;
import com.twisted.game.content.daily_tasks.DailyTasks;
import com.twisted.game.content.duel.Dueling;
import com.twisted.game.content.events.ServerEvent;
import com.twisted.game.content.gambling.GambleState;
import com.twisted.game.content.gambling.GamblingSession;
import com.twisted.game.content.instance.InstancedAreaManager;
import com.twisted.game.content.instance.impl.AlchemicalHydraInstance;
import com.twisted.game.content.instance.impl.VorkathInstance;
import com.twisted.game.content.instance.impl.ZulrahInstance;
import com.twisted.game.content.item_forging.ItemForgingTable;
import com.twisted.game.content.items.mystery_box.MysteryBoxManager;
import com.twisted.game.content.kill_logs.BossKillLog;
import com.twisted.game.content.kill_logs.SlayerKillLog;
import com.twisted.game.content.mechanics.*;
import com.twisted.game.content.mechanics.item_dispenser.ItemDispenser;
import com.twisted.game.content.mechanics.promo.PaymentPromo;
import com.twisted.game.content.mechanics.referrals.Referrals;
import com.twisted.game.content.members.MemberFeatures;
import com.twisted.game.content.minigames.Minigame;
import com.twisted.game.content.minigames.MinigameManager;
import com.twisted.game.content.minigames.impl.fight_caves.FightCavesMinigame;
import com.twisted.game.content.newplateau.DoubleExperience;
import com.twisted.game.content.newplateau.DoubleSlayer;
import com.twisted.game.content.newplateau.PoolOfWealth;
import com.twisted.game.content.packet_actions.GlobalStrings;
import com.twisted.game.content.presets.PresetManager;
import com.twisted.game.content.presets.Presetable;
import com.twisted.game.content.raids.Raids;
import com.twisted.game.content.raids.party.Party;
import com.twisted.game.content.seasonal_events.christmas.SackOfPresents;
import com.twisted.game.content.seasonal_events.christmas.SnowMonster;
import com.twisted.game.content.seasonal_events.rewards.UnlockEventRewards;
import com.twisted.game.content.skill.Skillable;
import com.twisted.game.content.skill.impl.farming.Farming;
import com.twisted.game.content.skill.impl.farming.patch.Farmbit;
import com.twisted.game.content.skill.impl.farmingOld.FarmingOld;
import com.twisted.game.content.skill.impl.hunter.Hunter;
import com.twisted.game.content.skill.impl.slayer.SlayerConstants;
import com.twisted.game.content.skill.impl.slayer.SlayerKey;
import com.twisted.game.content.skill.impl.slayer.SlayerRewards;
import com.twisted.game.content.skill.impl.slayer.slayer_partner.SlayerPartner;
import com.twisted.game.content.sound.CombatSounds;
import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.content.security.impl.EnterAccountPin;
import com.twisted.game.content.tasks.TaskMasterManager;
import com.twisted.game.content.teleport.Teleports;
import com.twisted.game.content.teleport.world_teleport_manager.TeleportInterface;
import com.twisted.game.content.title.AvailableTitle;
import com.twisted.game.content.title.TitleCategory;
import com.twisted.game.content.title.TitleColour;
import com.twisted.game.content.title.TitlePlugin;
import com.twisted.game.content.title.req.impl.other.TitleUnlockRequirement;
import com.twisted.game.content.tournaments.Tournament;
import com.twisted.game.content.tournaments.TournamentManager;
import com.twisted.game.content.tournaments.TournamentUtils;
import com.twisted.game.content.trade.Trading;
import com.twisted.game.content.tradingpost.TradingPostListing;
import com.twisted.game.task.Task;
import com.twisted.game.task.TaskManager;
import com.twisted.game.task.impl.*;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.*;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.Venom;
import com.twisted.game.world.entity.combat.bountyhunter.BountyHunter;
import com.twisted.game.world.entity.combat.magic.CombatSpells;
import com.twisted.game.world.entity.combat.method.impl.npcs.bosses.vorkath.VorkathState;
import com.twisted.game.world.entity.combat.prayer.default_prayer.DefaultPrayerData;
import com.twisted.game.world.entity.combat.sigils.Sigil;
import com.twisted.game.world.entity.combat.skull.SkullType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.content.instance.impl.KrakenInstance;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.combat.prayer.QuickPrayers;
import com.twisted.game.world.entity.combat.skull.Skulling;
import com.twisted.game.world.entity.combat.weapon.WeaponInterfaces;
import com.twisted.game.world.entity.dialogue.*;
import com.twisted.game.world.entity.masks.chat.ChatMessage;
import com.twisted.game.world.entity.mob.Flag;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.npc.pets.PetAI;
import com.twisted.game.world.entity.mob.npc.pets.insurance.PetInsurance;
import com.twisted.game.world.entity.mob.player.commands.impl.staff.admin.UpdateServerCommand;
import com.twisted.game.world.entity.mob.player.ironman.GroupIronman;
import com.twisted.game.world.entity.mob.player.relations.PlayerRelations;
import com.twisted.game.world.entity.mob.player.rights.MemberRights;
import com.twisted.game.world.entity.mob.player.rights.PlayerRights;
import com.twisted.game.world.entity.mob.player.save.PlayerSave;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.entity.mob.updating.Tinting;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.container.ItemContainer;
import com.twisted.game.world.items.container.bank.Bank;
import com.twisted.game.world.items.container.equipment.Equipment;
import com.twisted.game.world.items.container.equipment.EquipmentInfo;
import com.twisted.game.world.items.container.inventory.Inventory;
import com.twisted.game.world.items.container.looting_bag.LootingBag;
import com.twisted.game.world.items.container.price_checker.PriceChecker;
import com.twisted.game.world.items.container.rune_pouch.RunePouch;
import com.twisted.game.world.object.OwnedObject;
import com.twisted.game.world.object.dwarf_cannon.DwarfCannon;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.ControllerManager;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.game.world.route.routes.TargetRoute;
import com.twisted.net.Session;
import com.twisted.net.SessionState;
import com.twisted.net.packet.PacketBuilder;
import com.twisted.net.packet.PacketSender;
import com.twisted.net.packet.outgoing.UnnecessaryPacketDropper;
import com.twisted.util.*;
import com.twisted.util.chainedwork.Chain;
import com.twisted.util.timers.TimerKey;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import kotlinsave.save.PlayerSaves;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serial;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static com.twisted.game.content.areas.wilderness.content.EloRating.DEFAULT_ELO_RATING;
import static com.twisted.game.content.daily_tasks.DailyTaskUtility.DAILY_TASK_MANAGER_INTERFACE;
import static com.twisted.game.content.daily_tasks.DailyTaskUtility.TIME_FRAME_TEXT_ID;
import static com.twisted.game.content.tournaments.TournamentUtils.TOURNAMENT_REGION;
import static com.twisted.game.world.entity.AttributeKey.*;
import static com.twisted.game.world.entity.AttributeKey.RESET_ACCOUNT;
import static com.twisted.game.world.entity.mob.player.QuestTab.InfoTab.*;
import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.TUMEKENS_SHADOW;

@Data
public class Player extends Mob {

    private static final Logger logoutLogs = LogManager.getLogger("LogoutLogs");
    private static final Level LOGOUT;
    @Getter
    public Sigil sigil = new Sigil();
    public long UID = -1L;
    public List<Player> currentTeleblockedPlayers = new ArrayList<>();

    static {
        LOGOUT = Level.getLevel("LOGOUT");
    }

    private Tinting tinting;

    public Tinting tinting() {
        return tinting;
    }

    public void setTinting(Tinting tinting, Player player) {
        this.tinting = tinting;
        tinting();
        player.getUpdateFlag().flag(Flag.LUMINANCE);
    }

    private final SnowMonster snowMonster = new SnowMonster(this);

    public SnowMonster getSnowMonster() {
        return snowMonster;
    }

    private final SackOfPresents sackOfPresents = new SackOfPresents(this);

    public SackOfPresents getSackOfPresents() {
        return sackOfPresents;
    }

    public void removeAll(Item item) {
        int inventoryCount = inventory.count(item.getId());
        for (int i = 0; i < inventoryCount; i++) {
            inventory.remove(item, true);
        }

        //Equipment can only have one item in a slot
        equipment.remove(item, true);

        int bankCount = bank.count(item.getId());
        for (int i = 0; i < bankCount; i++) {
            bank.removeFromBank(item);
        }
        if (item.getId() == DAWNBRINGER) {
            message("The weapon falls apart in your hand as Verzik's shield is destroyed.");
        }
    }

    /**
     * Save a new player's chat sent to ip-mute for advertising.
     */
    public ArrayList<String> newPlayerChat = new ArrayList<String>();

    @Setter
    @Getter
    private Raids raids;

    public void heal() {
        graphic(436, 48, 0);
        message("<col=" + Color.BLUE.getColorValue() + ">You have restored your hitpoints, run energy and prayer.");
        message("<col=" + Color.HOTPINK.getColorValue() + ">You've also been cured of poison and venom.");
        skills().resetStats();
        int increase = getEquipment().hpIncrease();

        hp(Math.max(increase > 0 ? skills().level(Skills.HITPOINTS) + increase : skills().level(Skills.HITPOINTS), skills().xpLevel(Skills.HITPOINTS)), 39); //Set hitpoints to 100%
        skills().replenishSkill(5, skills().xpLevel(5)); //Set the players prayer level to fullputAttrib(AttributeKey.RUN_ENERGY, 100.0);
        setRunningEnergy(100.0, true);
        Poison.cure(this);
        Venom.cure(2, this);

        message(Color.RED.tag() + "When being a member your special attack will also regenerate.");
        if (memberRights.isRubyOrGreater(this)) {
            if (getTimerRepository().has(TimerKey.RECHARGE_SPECIAL_ATTACK)) {
                message("Special attack energy can be restored in " + getTimerRepository().asMinutesAndSecondsLeft(TimerKey.RECHARGE_SPECIAL_ATTACK) + ".");
            } else {
                restoreSpecialAttack(100);
                setSpecialActivated(false);
                CombatSpecial.updateBar(this);
                int time = 0;
                if (memberRights.isRubyOrGreater(this))
                    time = 300;//3 minutes
                if (memberRights.isSaphireOrGreater(this))
                    time = 100;//1 minute
                if (memberRights.isEmeraldOrGreater(this))
                    time = 0;//always
                getTimerRepository().register(TimerKey.RECHARGE_SPECIAL_ATTACK, time); //Set the value of the timer.
                message("<col=" + Color.HOTPINK.getColorValue() + ">You have restored your special attack.");
            }
        }
    }

    public static class TextData {

        public final String text;
        public final int id;

        public TextData(String text, int id) {
            this.text = text;
            this.id = id;
        }
    }

    private final UnlockEventRewards unlockEventRewards = new UnlockEventRewards(this);

    public UnlockEventRewards getEventRewards() {
        return unlockEventRewards;
    }

    private final Map<Integer, TinterfaceText> interfaceText = new HashMap<>();

    public static class TinterfaceText {
        public int id;
        public String currentState;

        public TinterfaceText(String s, int id) {
            this.currentState = s;
            this.id = id;
        }
    }

    public boolean checkPacket126Update(String text, int id) {
        if (interfaceText.containsKey(id)) {
            TinterfaceText t = interfaceText.get(id);
            if (text.equals(t.currentState)) {
                return false;
            }
        }
        interfaceText.put(id, new TinterfaceText(text, id));
        return true;
    }

    private final UnnecessaryPacketDropper packetDropper = new UnnecessaryPacketDropper();

    public UnnecessaryPacketDropper getPacketDropper() {
        return packetDropper;
    }

    public int extraItemRollChance() {
        return switch (getMemberRights()) {
            case NONE, RUBY_MEMBER, SAPPHIRE_MEMBER -> 0;
            case EMERALD_MEMBER -> 1;
            case DIAMOND_MEMBER -> 3;
            case DRAGONSTONE_MEMBER -> 5;
            case ONYX_MEMBER -> 8;
            case ZENYTE_MEMBER -> 10;
        };
    }

    public void checkTeleblockListRemoval() {
        if (this.currentTeleblockedPlayers.isEmpty()) {
            return;
        }

        this.currentTeleblockedPlayers.removeIf(mob -> {
            boolean shouldRemove = !mob.getTimerRepository().has(TimerKey.TELEBLOCK) || mob.getTimerRepository().left(TimerKey.TELEBLOCK) <= 0;
            if (shouldRemove) {
                mob.removeTeleblock();
            }
            return shouldRemove;
        });
    }

    public void removeTeleblock() {
        this.getTimerRepository().cancel(TimerKey.TELEBLOCK);
        this.getTimerRepository().cancel(TimerKey.SPECIAL_TELEBLOCK);
        this.getTimerRepository().cancel(TimerKey.TELEBLOCK_IMMUNITY);
        this.clearAttrib(AttributeKey.TELEBLOCKED_BY);
        this.getTimerRepository().register(TimerKey.TELEBLOCK_IMMUNITY, 5);
        this.message("<col=4f006f>The teleblock spell cast on you fades away.");
    }

    public int totemDropRateBonus() {
        var extraPercentageChance = 0;
        if (getMemberRights().isZenyteOrGreater(this) && tile().memberCave())
            extraPercentageChance = 30;
        else if (getMemberRights().isOnyxOrGreater(this) && tile().memberCave())
            extraPercentageChance = 20;
        else if (getMemberRights().isDragonstoneOrGreater(this) && tile().memberCave())
            extraPercentageChance = 10;
        else if (getMemberRights().isDiamondOrGreater(this) && tile().memberCave())
            extraPercentageChance = 5;
        else if (getMemberRights().isEmeraldOrGreater(this) && tile().memberCave())
            extraPercentageChance = 5;
        else if (getMemberRights().isSaphireOrGreater(this) && tile().memberCave())
            extraPercentageChance = 5;

        return extraPercentageChance;
    }

    public int memberAncientRevBonus() {
        var extraPercentageChance = 0;
        if (getMemberRights().isZenyteOrGreater(this) && tile().memberCave())
            extraPercentageChance = 10;
        else if (getMemberRights().isOnyxOrGreater(this) && tile().memberCave())
            extraPercentageChance = 8;
        else if (getMemberRights().isDragonstoneOrGreater(this) && tile().memberCave())
            extraPercentageChance = 6;
        else if (getMemberRights().isDiamondOrGreater(this) && tile().memberCave())
            extraPercentageChance = 4;
        else if (getMemberRights().isEmeraldOrGreater(this) && tile().memberCave())
            extraPercentageChance = 2;
        else if (getMemberRights().isSaphireOrGreater(this) && tile().memberCave())
            extraPercentageChance = 1;
        else if (getMemberRights().isRubyOrGreater(this) && tile().memberCave())
            extraPercentageChance = 0;

        return extraPercentageChance;
    }

    public int masterCasketMemberBonus() {
        var extraPercentageChance = 0;
        if (getMemberRights().isZenyteOrGreater(this) && tile().memberCave())
            extraPercentageChance = 25;
        else if (getMemberRights().isOnyxOrGreater(this) && tile().memberCave())
            extraPercentageChance = 15;
        else if (getMemberRights().isDragonstoneOrGreater(this) && tile().memberCave())
            extraPercentageChance = 10;
        else if (getMemberRights().isDiamondOrGreater(this) && tile().memberCave())
            extraPercentageChance = 7;
        else if (getMemberRights().isEmeraldOrGreater(this) && tile().memberCave())
            extraPercentageChance = 4;
        else if (getMemberRights().isSaphireOrGreater(this) && tile().memberCave())
            extraPercentageChance = 2;

        return extraPercentageChance;
    }

    public int dropRateBonus() {
        var percent = switch (getMemberRights()) {
            case NONE -> 0;
            case RUBY_MEMBER -> 1;
            case SAPPHIRE_MEMBER -> 3;
            case EMERALD_MEMBER -> 5;
            case DIAMOND_MEMBER -> 7;
            case DRAGONSTONE_MEMBER -> 9;
            case ONYX_MEMBER -> 11;
            case ZENYTE_MEMBER -> 13;
        };

        if (mode() == GameMode.TRAINED_ACCOUNT) {
            percent += 5;
        }
        if (mode() == GameMode.DARK_LORD) {
            percent += 7;
        }
        if (hasPetOut(12022)) {
            percent += 7.5;
        }

        if (hasPetOut(2312)) {
            percent += 20;
        }

        if (hasPetOut(2321)) {
            percent += 15;
        }

        if (hasPetOut(CustomNpcIdentifiers.THE_ONE_ABOVE_ALL)) {
            percent += 25;
        }

        if (hasPetOut(366)) {
            percent += 8.5;
        }
        if (getEquipment().hasAt(EquipSlot.RING, RING_OF_WEALTH_I)) {
            percent += 10;
        }
        if (getEquipment().hasAt(EquipSlot.RING, 16169)) {
            percent += 5;
        }
        if (getEquipment().hasAt(EquipSlot.CAPE, MAX_CAPE)) {
            percent += 5;
        }
        if (getEquipment().hasAt(EquipSlot.CAPE, MYTHICAL_MAX_CAPE)) {//5? YEAH DONE BRO TY! ALRIGHT WANT TO SEE SAFESPOT? SO YOU HAVE LOCATIONS
            //OK
            percent += 13;
        }
        var dropRateBoostPerk = getSlayerRewards().getUnlocks().containsKey(SlayerConstants.DROP_RATE_BOOST);

        if (dropRateBoostPerk) {
            percent += 3;
        }
        if (WildernessArea.inDiamondZone(this.tile())) {
            percent += 25;
        }
        if (WildernessArea.inZenyteZone(this.tile())) {
            percent += 40;
        }

        if (ServerEvent.isDropRateBoost()) {
            percent += 10;
        }

        if (this.tile().region() == 11563) {
            percent += 35;
        }

        var petBoostPerk = getSlayerRewards().getUnlocks().containsKey(SlayerConstants.ZRIAWK_BOOST);

        if (hasPetOut(9340)) {
            percent += petBoostPerk ? 20 : 15;
        }

        if (hasPetOut(4928)) {
            percent += 15;
        }

        if (this.getEquipment().contains(DRAGON_COMPANION)) {
            percent += 10;
        }

        var cap = 65;

        if (this.tile().region() == 9770) {
            cap = 80;
        }

        if (percent > cap) {
            percent = cap;
        }

        return percent;
    }

    private int base() {
        return switch (getMemberRights()) {
            case NONE -> GameServer.properties().baseBMValue;
            case RUBY_MEMBER -> 750;
            case SAPPHIRE_MEMBER -> 1000;
            case EMERALD_MEMBER -> 1100;
            case DIAMOND_MEMBER -> 1300;
            case DRAGONSTONE_MEMBER -> 1500;
            case ONYX_MEMBER -> 1750;
            case ZENYTE_MEMBER -> 2000;
        };
    }

    public int shutdownValueOf(int streak) {
        int bonus = 1000 * streak;
        return bonus;
    }

    private int killstreakValueOf(int streak) {
        int bonus = 50 * streak;
        return bonus;
    }

    private int firstKillOfTheDay() {
        if (System.currentTimeMillis() >= (long) getAttribOr(AttributeKey.FIRST_KILL_OF_THE_DAY, 0L)) {
            putAttrib(AttributeKey.FIRST_KILL_OF_THE_DAY, System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24));
            return 10000;
        }
        return 0;
    }

    public int bloodMoneyAmount(Player target) {
        // Declare base value for our kill.
        int bm = base();

        // Double BM, if enabled. Can be toggled with ::bmm <int>. Default 1.
        bm *= World.getWorld().bmMultiplier;

        //Being a trained account gives a +100 BM boost to the base value
        if (mode() == GameMode.TRAINED_ACCOUNT)
            bm += 100;

        //Slayer helm bonus
        Item helm = getEquipment().get(EquipSlot.HEAD);
        boolean slayer_helmet_i = getEquipment().hasAt(EquipSlot.HEAD, SLAYER_HELMET_I);
        boolean special_slayer_helmet_i = helm != null && (helm.getId() == RED_SLAYER_HELMET_I || helm.getId() == TWISTED_SLAYER_HELMET_I || helm.getId() == TWISTED_SLAYER_HELMET_I_KBD_HEADS || helm.getId() == PURPLE_SLAYER_HELMET_I || helm.getId() == HYDRA_SLAYER_HELMET_I);

        bm += slayer_helmet_i ? 25 : special_slayer_helmet_i ? 50 : 0;

        // Ruin his kill streak. Only when dying to a player.
        var target_killstreak = target == null ? 0 : target.<Integer>getAttribOr(AttributeKey.KILLSTREAK, 0);
        var killstreak = this.<Integer>getAttribOr(AttributeKey.KILLSTREAK, 0) + 1;

        // Apply target's killstreak on our reward. Oh, and our streak.
        bm += shutdownValueOf(target_killstreak); //Add the shutdown value bonus to the BM reward
        bm += killstreakValueOf(killstreak); //Add the killstreak value bonus to the BM reward
        bm += WildernessArea.wildernessLevel(tile()) * 2; //Add the wilderness level bonus to the reward

        bm += firstKillOfTheDay();

        if (pet() != null && pet().def().name.equalsIgnoreCase("Blood firebird")) {
            bm += 50;
        }
        if (pet() != null && pet().def().name.equalsIgnoreCase("Mending life bird")) {//updatem
            bm += 50;
        }
        //Edgevile hotspot always bm x2
        if (tile().inArea(new Area(2993, 3523, 3124, 3597, 0))) {
            bm *= 2;
        }
        return bm;
    }

    public void healPlayer() {
        hp(Math.max(skills().level(Skills.HITPOINTS), skills().xpLevel(Skills.HITPOINTS)), 20); //Set hitpoints to 100%
        skills().replenishSkill(5, skills().xpLevel(5)); //Set the players prayer level to full
        skills().replenishStatsToNorm();
        setRunningEnergy(100.0, true);
        Poison.cure(this);
        Venom.cure(2, this);
    }

    private final SlayerKey slayerKey = new SlayerKey(this);

    public SlayerKey getSlayerKey() {
        return slayerKey;
    }

    private final ItemContainer raidRewards = new ItemContainer(2, ItemContainer.StackPolicy.ALWAYS);

    public ItemContainer getRaidRewards() {
        return raidRewards;
    }

    public boolean hasAccountPin() {
        var pin = this.<Integer>getAttribOr(ACCOUNT_PIN, 0);
        var pinAsString = pin.toString();
        return pinAsString.length() == 5;
    }

    public boolean askForAccountPin() {
        return this.<Boolean>getAttribOr(ASK_FOR_ACCOUNT_PIN, false);
    }

    public void sendAccountPinMessage() {
        int ticks = this.<Integer>getAttribOr(ACCOUNT_PIN_FREEZE_TICKS, 0);
        int convertTicksToSeconds = Utils.getSeconds(ticks);

        if (this.<Integer>getAttribOr(ACCOUNT_PIN_FREEZE_TICKS, 0) > 0) {
            DialogueManager.sendStatement(this, "Try again in " + Utils.convertSecondsToDuration(convertTicksToSeconds, false) + ".");
            return;
        }
        this.interfaceManager.closeDialogue();
        this.setEnterSyntax(new EnterAccountPin());
        this.getPacketSender().sendEnterAmountPrompt("Enter your account pin, attempts left: " + this.<Integer>getAttribOr(AttributeKey.ACCOUNT_PIN_ATTEMPTS_LEFT, 5) + "/5");
    }

    public Party raidsParty;

    private int multi_cannon_stage;

    public int getMultiCannonStage() {
        return multi_cannon_stage;
    }

    public void setMultiCannonStage(int stage) {
        this.multi_cannon_stage = stage;
    }

    private final ItemDispenser itemDispenser = new ItemDispenser(this);

    public ItemDispenser getItemDispenser() {
        return itemDispenser;
    }

    public List<TradingPostListing> tempList;

    public List<TradingPostListing> tradePostHistory = Lists.newArrayList();

    public int tradingPostListedItemId, tradingPostListedAmount;

    public String lastTradingPostUserSearch, lastTradingPostItemSearch;

    public boolean jailed() {
        return (int) getAttribOr(AttributeKey.JAILED, 0) == 1;
    }

    private int[] farmingSeedId = new int[FarmingOld.MAX_PATCHES], farmingTime = new int[FarmingOld.MAX_PATCHES],
        farmingState = new int[FarmingOld.MAX_PATCHES], farmingHarvest = new int[FarmingOld.MAX_PATCHES];

    public int getFarmingSeedId(int index) {
        return farmingSeedId[index];
    }

    public void setFarmingSeedId(int index, int farmingSeedId) {
        this.farmingSeedId[index] = farmingSeedId;
    }

    public int getFarmingTime(int index) {
        return this.farmingTime[index];
    }

    public void setFarmingTime(int index, int farmingTime) {
        this.farmingTime[index] = farmingTime;
    }

    public int getFarmingState(int index) {
        return farmingState[index];
    }

    public void setFarmingState(int index, int farmingState) {
        this.farmingState[index] = farmingState;
    }

    public int getFarmingHarvest(int index) {
        return farmingHarvest[index];
    }

    public void setFarmingHarvest(int index, int farmingHarvest) {
        this.farmingHarvest[index] = farmingHarvest;
    }

    private FarmingOld farmingOld = new FarmingOld(this);

    public FarmingOld farming() {
        if (farmingOld == null) {
            farmingOld = new FarmingOld(this);
        }
        return farmingOld;
    }

    /**
     * If the player has the tool store open.
     */
    private boolean tool_store_open;

    /**
     * Returns if the player has tool store open.
     *
     * @return if is open
     */
    public boolean isToolStoreOpen() {
        return tool_store_open;
    }

    /**
     * Sets if the player has tool store open.
     *
     * @param b
     */
    public void setToolStoreOpen(boolean b) {
        this.tool_store_open = b;
    }

    private Task currentTask;

    public Task getCurrentTask() {
        return currentTask;
    }

    public void endCurrentTask() {
        if (currentTask != null) {
            currentTask.stop();
        }
        currentTask = null;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public Triggers getTriggers() {
        return triggers;
    }

    private final transient Triggers triggers = new Triggers(this);

    private final BossTimers bossTimers = new BossTimers();

    public BossTimers getBossTimers() {
        return bossTimers;
    }

    public boolean ownsAny(int... ids) {
        return this.inventory.containsAny(ids) || this.equipment.containsAny(ids) || this.bank.containsAny(ids);
    }

    private IronMode ironMode = IronMode.NONE;

    public IronMode ironMode() {
        return ironMode;
    }

    public void ironMode(IronMode mode) {
        ironMode = mode;
    }

    private final TeleportInterface teleportInterface = new TeleportInterface(this);

    public TeleportInterface getTeleportInterface() {
        return teleportInterface;
    }

    private List<TeleportInterface.TeleportData> recentTeleports = new ArrayList<>();

    public List<TeleportInterface.TeleportData> getRecentTeleports() {
        return recentTeleports;
    }

    public void setRecentTeleports(List<TeleportInterface.TeleportData> recentTeleports) {
        this.recentTeleports = recentTeleports;
    }

    private List<TeleportInterface.TeleportData> favorites = new ArrayList<>();

    public List<TeleportInterface.TeleportData> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<TeleportInterface.TeleportData> favorites) {
        this.favorites = favorites;
    }

    private int currentTabIndex;

    public int getCurrentTabIndex() {
        return currentTabIndex;
    }

    public void setCurrentTabIndex(int currentTabIndex) {
        this.currentTabIndex = currentTabIndex;
    }

    private final GamblingSession gamblingSession = new GamblingSession(this);

    public GamblingSession getGamblingSession() {
        return gamblingSession;
    }

    private static final Logger logger = LogManager.getLogger(Player.class);

    private VorkathState state = VorkathState.SLEEPING;

    public VorkathState getVorkathState() {
        return state;
    }

    public void setVorkathState(VorkathState state) {
        this.state = state;
    }

    private final PaymentPromo paymentPromo = new PaymentPromo(this);

    public PaymentPromo getPaymentPromo() {
        return paymentPromo;
    }

    private final PetInsurance petInsurance = new PetInsurance(this);

    public PetInsurance getPetInsurance() {
        return petInsurance;
    }

    private ArrayList<Integer> unlockedPets = new ArrayList<>();

    public ArrayList<Integer> getUnlockedPets() {
        return unlockedPets;
    }

    public void setUnlockedPets(ArrayList<Integer> unlockedPets) {
        if (unlockedPets == null)
            return;
        this.unlockedPets = unlockedPets;
    }

    public boolean isPetUnlocked(int id) {
        return unlockedPets.contains(id);
    }

    public void addUnlockedPet(int id) {
        if (this.unlockedPets.contains(id)) {
            return;
        }
        unlockedPets.add(id);
    }

    private ArrayList<Integer> insuredPets = new ArrayList<>();

    public ArrayList<Integer> getInsuredPets() {
        return insuredPets;
    }

    public void setInsuredPets(ArrayList<Integer> insuredPets) {
        // lets not set the array to null, list should always exist. If the player doesn't have pets when logging in, insuredPets is null in the PlayerSave class.
        if (insuredPets == null)
            return;
        this.insuredPets = insuredPets;
    }

    public boolean isInsured(int id) {
        return insuredPets.contains(id);
    }

    public void addInsuredPet(int id) {
        insuredPets.add(id);
    }

    private Minigame minigame;

    /**
     * Sets the minigame
     *
     * @return the minigame
     */
    public Minigame getMinigame() {
        return minigame;
    }

    /**
     * Sets the minigame
     *
     * @param minigame the minigame
     */
    public void setMinigame(Minigame minigame) {
        this.minigame = minigame;
    }

    private final MinigameManager minigameManager = new MinigameManager();

    /**
     * Sets the minigameManager
     *
     * @return the minigameManager
     */
    public MinigameManager getMinigameManager() {
        return minigameManager;
    }

    // Slayer

    public void slayerWidgetActions(int buttonId, String name, int config, int type) {
        this.putAttrib(SLAYER_WIDGET_BUTTON_ID, buttonId);
        this.putAttrib(SLAYER_WIDGET_NAME, name);
        this.putAttrib(SLAYER_WIDGET_CONFIG, config);
        this.putAttrib(SLAYER_WIDGET_TYPE, type);
    }

    private final SlayerRewards slayerRewards = new SlayerRewards(this);

    public SlayerRewards getSlayerRewards() {
        return slayerRewards;
    }

    private final List<TitleUnlockRequirement.UnlockableTitle> unlockedTitles = new ArrayList<>();

    public List<TitleUnlockRequirement.UnlockableTitle> getUnlockedTitles() {
        return unlockedTitles;
    }

    private TitleCategory currentCategory = TitleCategory.PKING;
    private AvailableTitle currentSelectedTitle;
    private TitleColour currentSelectedColour;

    public TitleColour getCurrentSelectedColour() {
        return currentSelectedColour;
    }

    public void setCurrentSelectedColour(TitleColour currentSelectedColour) {
        this.currentSelectedColour = currentSelectedColour;
    }

    public AvailableTitle getCurrentSelectedTitle() {
        return currentSelectedTitle;
    }

    public void setCurrentSelectedTitle(AvailableTitle currentSelectedTitle) {

        this.currentSelectedTitle = currentSelectedTitle;
    }

    public TitleCategory getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(TitleCategory currentCategory) {
        this.currentCategory = currentCategory;
    }

    private Optional<Skillable> skillable = Optional.empty();

    public Optional<Skillable> getSkillable() {
        return skillable;
    }

    public void setSkillable(Optional<Skillable> skillable) {
        this.skillable = skillable;
    }

    public int slayerTaskAmount() {
        return this.getAttribOr(AttributeKey.SLAYER_TASK_AMT, 0);
    }

    public int slayerTaskId() {
        return this.getAttribOr(AttributeKey.SLAYER_TASK_ID, 0);
    }

    private final TaskMasterManager taskMasterManager = new TaskMasterManager(this);

    public TaskMasterManager getTaskMasterManager() {
        return taskMasterManager;
    }

    private Varps varps;

    private double[] savedTornamentXp;

    private int[] savedTornamentLevels;

    public Varps varps() {
        return varps;
    }

    /**
     * Our achieved skill levels
     */
    private Skills skills;

    public Skills skills() {
        return skills;
    }

    public void skills(Skills skills) {
        this.skills = skills;
    }

    /**
     * Our looks (clothes, colours, gender)
     */
    private final Looks looks;

    public Looks looks() {
        return looks;
    }

    private final CollectionLog collectionLog = new CollectionLog(this);

    public CollectionLog getCollectionLog() {
        return collectionLog;
    }

    private Clan clan;
    private String clanChat;
    private String savedClan;
    private String clanPromote;

    public Clan getClan() {
        return clan;
    }

    public void setClan(Clan clan) {
        this.clan = clan;
    }

    public String getSavedClan() {
        return savedClan;
    }

    public void setSavedClan(String savedClan) {
        this.savedClan = savedClan;
    }

    public String getClanPromote() {
        return clanPromote;
    }

    public void setClanPromote(String clanPromote) {
        this.clanPromote = clanPromote;
    }

    public String getClanChat() {
        return clanChat;
    }

    public void setClanChat(String clanChat) {
        this.clanChat = clanChat;
    }

    public ChatBoxItemDialogue chatBoxItemDialogue;

    //This task keeps looping until the player action has been completed.
    public Task loopTask;

    /**
     * Their skull icon identification
     */
    private SkullType skullType = SkullType.NO_SKULL;

    public SkullType getSkullType() {
        return skullType;
    }

    public void setSkullType(SkullType skullType) {
        this.skullType = skullType;
    }

    /**
     * The map which was recently sent to show
     */
    private Tile activeMap;

    public Tile activeMap() {
        return activeMap;
    }

    public void setActiveMap(Tile tile) {
        activeMap = tile;
    }

    public Area activeArea() {
        if (activeMap == null) {
            return new Area(tile().x - 52, tile().y - 52, tile().x + 52, tile().y + 52, tile().level);
        }

        return new Area(activeMap.x, activeMap.y, activeMap.x + 104, activeMap.y + 104, activeMap.level);
    }

    public boolean seesChunk(int x, int z) {
        return activeArea().contains(new Tile(x, z));
    }

    private boolean[] savedDuelConfig = new boolean[22]; // 22 rules

    public boolean[] getSavedDuelConfig() {
        return savedDuelConfig;
    }

    public void setSavedDuelConfig(boolean[] savedDuelConfig) {
        this.savedDuelConfig = savedDuelConfig;
    }

    public void setSavedDuelConfig(int index, boolean value) {
        this.savedDuelConfig[index] = value;
    }

    // Obtain the ItemContainer with our reward
    public ItemContainer clueScrollReward() {
        ItemContainer offer = getAttribOr(AttributeKey.CLUE_SCROLL_REWARD, null);
        if (offer != null)
            return offer;

        //This contain has a maximum size of 8
        ItemContainer container = new ItemContainer(8, ItemContainer.StackPolicy.ALWAYS);
        putAttrib(AttributeKey.CLUE_SCROLL_REWARD, container);
        return container;
    }

    public long lastGpi;
    public Map<Integer, Integer> commonStringsCache;

    private final InterfaceManager interfaceManager = new InterfaceManager(this);

    public InterfaceManager getInterfaceManager() {
        return interfaceManager;
    }

    public final RuntimeException initializationSource;

    /**
     * Creates this player.
     *
     * @param playerIO
     */
    public Player(Session playerIO) {
        super(NodeType.PLAYER, GameServer.properties().defaultTile);
        initializationSource = new RuntimeException("player created");
        this.session = playerIO;
        this.looks = new Looks(this);
        this.skills = new Skills(this);
        this.varps = new Varps(this);
    }

    public Player() {
        super(NodeType.PLAYER, GameServer.properties().defaultTile);
        initializationSource = new RuntimeException("player created");
        this.looks = new Looks(this);
        this.skills = new Skills(this);
        this.varps = new Varps(this);
    }

    public void teleblockMessage() {
        if (!getTimerRepository().has(TimerKey.SPECIAL_TELEBLOCK))
            return;

        long special_timer = getTimerRepository().left(TimerKey.SPECIAL_TELEBLOCK) * 600L;

        message(String.format("A teleport block has been cast on you. It should wear off in %d minutes, %d seconds.",
            TimeUnit.MILLISECONDS.toMinutes(special_timer),
            TimeUnit.MILLISECONDS.toSeconds(special_timer) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(special_timer))
        ));

        if (!getTimerRepository().has(TimerKey.TELEBLOCK))
            return;

        long millis = getTimerRepository().left(TimerKey.TELEBLOCK) * 600L;

        message(String.format("A teleport block has been cast on you. It should wear off in %d minutes, %d seconds.",
            TimeUnit.MILLISECONDS.toMinutes(millis),
            TimeUnit.MILLISECONDS.toSeconds(millis) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        ));
    }

    public boolean canSpawn() {
        if ((!this.tile().homeRegion() || WildernessArea.inWilderness(this.tile()))) {
            this.message("You can only spawn items at home.");
            return false;
        }

        if (this.busy()) {
            this.message("You can't spawn items at this time.");
            return false;
        }

        if (this.inActiveTournament()) {
            this.message("You can't spawn items inside the tournament.");
            return false;
        }

        if (CombatFactory.inCombat(this)) {
            //Silent no message
            return false;
        }
        return true;
    }

    @Override
    public int yLength() {
        return 1;
    }

    @Override
    public int xLength() {
        return 1;
    }

    @Override
    public Tile getCentrePosition() {
        return tile();
    }

    @Override
    public int getProjectileLockonIndex() {
        return -getIndex() - 1;
    }

    private KrakenInstance krakenInstance;

    public KrakenInstance getKrakenInstance() {
        if (krakenInstance == null)
            krakenInstance = new KrakenInstance();
        return krakenInstance;
    }

    private ZulrahInstance zulrahInstance;

    public ZulrahInstance getZulrahInstance() {
        if (zulrahInstance == null)
            zulrahInstance = new ZulrahInstance();
        return zulrahInstance;
    }

    private VorkathInstance vorkathInstance;

    public VorkathInstance getVorkathInstance() {
        if (vorkathInstance == null)
            vorkathInstance = new VorkathInstance();
        return vorkathInstance;
    }

    private AlchemicalHydraInstance alchemicalHydraInstance;

    public AlchemicalHydraInstance getAlchemicalHydraInstance() {
        if (alchemicalHydraInstance == null)
            alchemicalHydraInstance = new AlchemicalHydraInstance();
        return alchemicalHydraInstance;
    }

    /**
     * Actions that should be done when this mob is added to the world.
     */
    @Override
    public void onAdd() {
        Long uid = this.<Long>getAttribOr(PLAYER_UID, 0L);
        if (uid == 0L) {
            uid = Utils.generateUUID();
            this.putAttrib(PLAYER_UID, uid);
        }
        World.getWorld().ls.ONLINE.add(getMobName().toUpperCase());
        session.setState(SessionState.LOGGED_IN);
        setNeedsPlacement(true);
        packetSender.sendMapRegion().sendDetails().sendRights().sendTabs();
        Tile.occupy(this);
        try {
            onLogin();
        } catch (Exception e) {
            logger.error("crash on login", e);
        }
    }

    /**
     * Actions that should be done when this mob is removed from the world.
     */
    @Override
    public void onRemove() {
        // onlogout moved to logout service
    }

    @Override
    public Hit manipulateHit(Hit hit) {
        Mob attacker = hit.getAttacker();

        if (attacker.isNpc()) {
            Npc npc = attacker.getAsNpc();
            if (npc.id() == NpcIdentifiers.TZTOKJAD) {
                if (Prayers.usingPrayer(this, Prayers.getProtectingPrayer(hit.getCombatType()))) {
                    hit.setDamage(0);
                }
            }
        }

        return hit;
    }

    @Override
    public void die() {
        stopActions(true);
        Death.death(this);
    }

    @Override
    public int hp() {
        return skills.level(Skills.HITPOINTS);
    }

    @Override
    public int maxHp() {
        return skills.xpLevel(Skills.HITPOINTS);
    }

    @Override
    public void hp(int hp, int exceed) {
        skills.setLevel(Skills.HITPOINTS, Math.max(0, Math.min(Math.max(hp(), maxHp() + exceed), hp)));
    }

    @Override
    public Mob setHitpoints(int hitpoints) {
        if (invulnerable) {
            if (skills.level(Skills.HITPOINTS) > hitpoints) {
                return this;
            }
        }
        skills.setLevel(Skills.HITPOINTS, hitpoints);
        skills.makeDirty(Skills.HITPOINTS);//Force refresh
        if (hp() < 1 && !locked()) {
            die();
        }
        return this;
    }

    @Override
    public int attackAnimation() {
        this.sound(CombatSounds.weapon_attack_sounds(this), 10);
        return EquipmentInfo.attackAnimationFor(this);
    }

    @Override
    public int getBlockAnim() {
        return EquipmentInfo.blockAnimationFor(this);
    }

    @Override
    public int getBaseAttackSpeed() {

        // Gets attack speed for player's weapon
        // If player is using magic, attack speed is
        // Calculated in the MagicCombatMethod class.
        int speed;
        Item weapon = this.getEquipment().get(EquipSlot.WEAPON);
        if (weapon == null) {
            speed = 4; //Default is 4
        } else {
            speed = World.getWorld().equipmentInfo().weaponSpeed(weapon.getId());
        }

        if (getCombat().getTarget() instanceof Npc && (getEquipment().contains(ItemIdentifiers.TOXIC_BLOWPIPE) || getEquipment().contains(MAGMA_BLOWPIPE) || getEquipment().contains(HWEEN_BLOWPIPE))) {
            speed--;
        }

        if (getCombat().getTarget() instanceof Npc && (getEquipment().hasAt(EquipSlot.WEAPON, TALONHAWK_CROSSBOW) || getEquipment().hasAt(EquipSlot.WEAPON, HAUNTED_CROSSBOW))) {
            speed--;
        }

        if (getCombat().getFightType().toString().toLowerCase().contains("rapid")) {
            speed--;
        }

        return speed;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Player)) {
            return false;
        }
        Player p = (Player) o;
        if (p.username == null || username == null)
            return false;
        return p.getUsername().equals(username);
    }

    @Override
    public int hashCode() {
        assert false : "Hashcode not designed";
        return 0;
    }

    @Override
    public String toString() {
        return getPlayerRights().getName() + ": " + username + ", " + hostAddress + ", [" + getX() + ", " + getY() + ", " + getZ() + "], " + (WildernessArea.inWilderness(tile()) ? "in wild" : "not in wild");
    }

    @Override
    public int getSize() {
        return 1;
    }

    public PlayerPerformanceTracker perf = new PlayerPerformanceTracker();

    private void fireLogout() {
        if (username == null || this.<Boolean>getAttribOr(IS_BOT, false)) return;

        if (this.<Boolean>getAttribOr(LOGOUT_CLICKED, false) || !active()) {
            clearAttrib(LOGOUT_CLICKED);
            if (!canLogout()) {
                return;
            }
            requestLogout();
            int[] playerXP = new int[23];
            for (int i = 0; i < playerXP.length; i++) {
                playerXP[i] = (int) this.skills().xp()[i];
            }
        }
    }

    private boolean active() {
        return session.getChannel() != null && session.getChannel().isActive();
    }

    private String captureState() {
        StringBuilder sb = new StringBuilder();
        sb.append(username).append(" state: ");
        sb.append(String.format("ded %s, lock %s, moving %s", dead(), lockState(), getMovementQueue().isMoving()));
        sb.append(" inv: ").append(Arrays.toString(inventory.getValidItems().stream().map(i -> i.toShortString()).toArray()));
        sb.append(" equipment: ").append(Arrays.toString(equipment.getValidItems().stream().map(i -> i.toShortString()).toArray()));
        return sb.toString();
    }

    private boolean divinePotionEffectActive() {
        List<AttributeKey> attribList = new ArrayList<>(List.of(DIVINE_BASTION_POTION_EFFECT_ACTIVE, DIVINE_BATTLEMAGE_POTION_EFFECT_ACTIVE, DIVINE_MAGIC_POTION_EFFECT_ACTIVE, DIVINE_RANGING_POTION_EFFECT_ACTIVE, DIVINE_SUPER_ATTACK_POTION_EFFECT_ACTIVE, DIVINE_SUPER_COMBAT_POTION_EFFECT_ACTIVE, DIVINE_SUPER_DEFENCE_POTION_EFFECT_ACTIVE, DIVINE_SUPER_STRENGTH_POTION_EFFECT_ACTIVE));
        return attribList.stream().anyMatch(key -> this.getAttribOr(key, false));
    }

    private void handleContainersDirty() {
        syncContainers();
    }

    public void syncContainers() {
        GroupIronman group = GroupIronman.getGroup(this.getUID());
        if (group == null) {
            if (getBank().dirty) {
                getBank().sync();
                getBank().dirty = false;
            }
        } else {
            if (group.getGroupBank().container.dirty) {
                group.getGroupBank().container.sync();
                group.getGroupBank().container.dirty = false;
            }
        }
        if (inventory().dirty) {
            inventory().sync();
            inventory().dirty = false;
        }
        if (getEquipment().dirty) {
            getEquipment().sync();
            getEquipment().dirty = false;
        }
        if (getPriceChecker().dirty) {
            getPriceChecker().sync();
            getPriceChecker().dirty = false;
        }
        if (getRunePouch().dirty) {
            getRunePouch().sync();
            getRunePouch().dirty = false;
        }
        if (getLootingBag().dirty) {
            getLootingBag().sync();
            getLootingBag().dirty = false;
        }
        skills.syncDirty();
        if ((!this.increaseStats.active() || (this.decreaseStats.secondsElapsed() >= (Prayers.usingPrayer(this, Prayers.PRESERVE) ? 90 : 60))) && !this.divinePotionEffectActive()) {
            this.skills.replenishStats();
            if (!this.increaseStats.active()) this.increaseStats.start(60);
            if (this.decreaseStats.secondsElapsed() >= (Prayers.usingPrayer(this, Prayers.PRESERVE) ? 90 : 60))
                this.decreaseStats.start((Prayers.usingPrayer(this, Prayers.PRESERVE) ? 90 : 60));
        }
        var staminaTicks = this.<Integer>getAttribOr(STAMINA_POTION_TICKS, 0);
        if (staminaTicks > 0) {
            staminaTicks--;
            this.putAttrib(STAMINA_POTION_TICKS, staminaTicks);
            if (staminaTicks == 50) {
                message("<col=8f4808>Your stamina potion is about to expire.");
            } else if (staminaTicks == 0) {
                message("<col=8f4808>Your stamina potion has expired.");
                this.packetSender.sendStamina(false).sendEffectTimer(0, EffectTimer.STAMINA);
            }
        }
    }

    /**
     * Saves this player on the underlying thread (probably the game thread).
     */
    public void synchronousSave() {
        if (session.getState() == SessionState.LOGGED_IN || session.getState() == SessionState.LOGGING_OUT) {
            PlayerSave.save(this);
        }
    }

    /**
     * Can the player logout?
     *
     * @return Yes if they can logout, false otherwise.
     */
    public boolean canLogout() {
        boolean logCooldown = this.getAttribOr(AttributeKey.ALLOWED_TO_LOGOUT, true);

        // wait for forcemovement to finish, dont save players half on an agility obstacle they cant get out of
        if (getForceMovement() != null && getMovementQueue().forcedStep())
            return false;
        // dont save dead/tping players. login with 0hp = POSSIBLE DUPES
        if (dead() || isNeedsPlacement())
            return false;
        // extremely important only force logout via update AFTER isdead() check
        // otherwise dupes can occur.
        if (UpdateServerCommand.time < 1 || getForcedLogoutTimer().expiredAfterBeingRun()) {
            return true;
        }
        if (!logCooldown) {
            message("You must wait a few seconds before logging out.");
            return false;
        }

        if (getTimerRepository().has(TimerKey.COMBAT_LOGOUT)) {
            message("You can't log out until 10 seconds after the end of combat.");
            return false;
        }

        if (inventory.contains(CustomItemIdentifiers.WILDERNESS_KEY) && WildernessArea.inWilderness(tile)) {
            message("You cannot logout holding the Wilderness key.");
            return false;
        }

        if (inventory.contains(CustomItemIdentifiers.ESCAPE_KEY) && WildernessArea.inWilderness(tile)) {
            message("You cannot logout holding the Escape key.");
            return false;
        }

        if (locked() && getLock() != LockType.FULL_LOGOUT_OK) {
            return false;
        }
        return true;
    }

    public void requestLogout() {
        System.out.println("requesting logout current thread = " + Thread.currentThread());
        if (this.session != null) {
            this.stopActions(true);
            this.session.setState(SessionState.REQUESTED_LOG_OUT);
            this.logoutLock();
            this.onLogout();
            this.checkDwarfCannonState();
            BountyHunter.PLAYERS_IN_WILD.remove(this);
            World.getWorld().getPlayers().remove(this);
            try {
                GameEngine.getInstance().addSyncTask(() -> {
                    System.out.println("adding sync task on current thread = " + Thread.currentThread());
                    try {
                        submitSave(() -> {
                            try {
                                System.out.println("submitting save current thread = " + Thread.currentThread());
                                this.requestChannel();
                            } catch (Throwable t) {
                                throw new RuntimeException("Failure to remove player from world: " + t);
                            } finally {
                                this.deregister();
                            }
                        });
                    } catch (Throwable t) {
                        throw new RuntimeException("Error during submission of save request: " + t);
                    }
                });
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
    }

    private void deregister() {
        try {
            this.onRemove();
        } catch (Exception e) {
            System.out.println("Error Deregistering player: " + e);
        }
    }

    private void requestChannel() {
        try {
            final Channel channel = session.getChannel();
            if (channel == null) {
                throw new RuntimeException("Channel is null upon logout request");
            }
            try (final PacketBuilder builder = new PacketBuilder(109)) {
                channel.writeAndFlush(builder.toPacket()).addListener(ChannelFutureListener.CLOSE);
            } catch (Throwable t) {
                throw new RuntimeException("Error during logout process: " + t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkDwarfCannonState() {
        ObjectList<Item> temp = new ObjectArrayList<>();
        for (var o : World.getWorld().getOwnedObjects().values()) {
            if (o == null) continue;
            if (o instanceof DwarfCannon cannon) {
                if (cannon.isOwner(this)) {
                    IntStream.of(cannon.getStage().getParts()).mapToObj(Item::new).forEach(temp::add);
                    temp.add(new Item(CANNONBALL, cannon.getAmmo()));
                    this.getInventory().addOrBank(temp);
                    cannon.destroy();
                }
            }
        }
    }

    private void submitSave(Runnable whenComplete) {
        if (!World.getWorld().ls.ONLINE.contains(getMobName().toUpperCase())) {
            return;
        }

        PlayerSaves.requestSave(this, () -> {
            World.getWorld().ls.ONLINE.remove(getMobName().toUpperCase());
            whenComplete.run();
        });
    }

    private final Map<String, Runnable> onLogoutListeners = new HashMap<>();

    public Map<String, Runnable> getOnLogoutListeners() {
        return onLogoutListeners;
    }

    public void runExceptionally(Runnable r) {
        try {
            r.run();
        } catch (Throwable e) {
            logger.error("Exception during logout => Player '{}'", getUsername(), e);
        }
    }

    /**
     * Handles the actual logging out from the game.
     */
    public void onLogout() {

        System.out.println("logout current thread = " + Thread.currentThread());

        logoutLogs.log(LOGOUT, "[Logout] Deregistering player - {}", getUsername());
        Utils.sendDiscordInfoLog(this, "```Deregistering player - " + getUsername() + " with IP " + getHostAddress() + "```", "logout");

        stopActions(true);

        onLogoutListeners.values().forEach(Runnable::run);

        //If we're in certain gamble states and we logout, return items.
        if (gamblingSession.state() != GambleState.NONE && !gamblingSession.matchActive()) {
            gamblingSession.abortGambling();
        }

        // If we're in a gamble, make sure to give us a loss for logging out.
        if (gamblingSession.matchActive()) {
            gamblingSession.end(gamblingSession.game.gameId);
        }

        Party.onLogout(this);

        var minigame = this.getMinigame();
        if (minigame != null) {
            minigame.end(this);
        }

        this.getTimerRepository().cancel(TimerKey.SPECIAL_TELEBLOCK);
        this.getTimerRepository().cancel(TimerKey.TELEBLOCK);

        this.clearInstance();

        if (getDueling().inDuel()) {
            getDueling().onDeath();
        }

        OwnedObject cannon = World.getWorld().getOwnedObject(this, DwarfCannon.IDENTIFIER);
        if (cannon != null) {
            this.putAttrib(LOST_CANNON, true);
            cannon.destroy();
        }

        // Leave area
        if (getController() != null) {
            getController().leave(this);
        }

        getRelations().onLogout();

        BountyHunter.unassign(this);

        PetAI.despawnOnLogout(this);

        getInterfaceManager().close();

        TaskManager.cancelTasks(this);

        looks().hide(true);

        Hunter.abandon(this, null, true);

        if (getClan() != null) {
            ClanManager.leave(this, true);
        }


        //Leave tourny on logout
        TournamentManager.leaveTourny(this, true);

        //Technically this is the last logout, but we'll use it as the last login so the last login doesn't get "overwritten" for the welcome screen when the player logs in.
        setLastLogin(new Timestamp(new Date().getTime()));

        final Session session = this.getSession();
        if (session != null) {
            session.setState(SessionState.LOGGING_OUT);
        }

        clearAttrib(AttributeKey.PLAYER_AUTO_SAVE_TASK_RUNNING);
    }

    @Getter
    @Setter
    private Tile lastSavedTile;

    @Getter
    @Setter
    private boolean usingLastRecall = false;

    /**
     * Called by the world's login queue!
     */
    public void onLogin() {
        long startTime = System.currentTimeMillis();
        putAttrib(AttributeKey.LOGGED_IN_AT_TIME, startTime);
        // Attempt to register the player..
        //logger.info("Registering player - [username, host] : [{}, {}]", getUsername(), getHostAddress());
        this.sigil.HandleLogin(this);
        //Stuff that happens during login...
        Chain.bound(null).runFn(1, () -> {
            // Send simple player options
            packetSender.sendInteractionOption("Follow", 3, false).sendInteractionOption("Trade with", 4, false);
            relations.setPrivateMessageId(1);
            getMovementQueue().clear();
            double energy = this.getAttribOr(RUN_ENERGY, 0.0);
            // configs...
            varps.syncNonzero();// Sync varps
            packetSender.sendConfig(708, Prayers.canUse(this, DefaultPrayerData.PRESERVE, false) ? 1 : 0).sendConfig(710, Prayers.canUse(this, DefaultPrayerData.RIGOUR, false) ? 1 : 0).sendConfig(712, Prayers.canUse(this, DefaultPrayerData.AUGURY, false) ? 1 : 0).sendConfig(172, this.getCombat().autoRetaliate() ? 1 : 0).updateSpecialAttackOrb().sendRunStatus().sendRunEnergy((int) energy);
            Prayers.closeAllPrayers(this);
            setHeadHint(-1);

            replaceItems();

            skills.update();
            inventory.refresh();
            equipment.refresh();
            WeaponInterfaces.updateWeaponInterface(this);

            // Force fix any remaining bugged accounts
            if (this.<Integer>getAttribOr(MULTIWAY_AREA, -1) == 1 && !MultiwayCombat.includes(this.tile())) {
                putAttrib(MULTIWAY_AREA, 0);
            }

            getUpdateFlag().flag(Flag.APPEARANCE); //Update the players looks

            if (this.<Boolean>getAttribOr(ASK_FOR_ACCOUNT_PIN, false)) {
                askForAccountPin();
            }
            //We're logged in now, we can now send data such as quest tab friends list etc.
        }).then(1, () -> {
            if (tile().region() == 10536) {
                teleport(new Tile(2657, 2639, 0));
            }

            if (jailed() && tile().region() != 13103) { // Safety since it was possible to escape.
                Teleports.basicTeleport(this, new Tile(3290, 3017));
            }

            if (tile().region() == 9551) {
                //restart the wave on login
                heal(maxHp());
                MinigameManager.playMinigame(this, new FightCavesMinigame(63));
            }

            //Move player out Zulrah area on login
            if (tile().region() == 9008) {
                teleport(2201, 3057, 0);
            }

            //Player inside the tournament region, remove them.
            if (tile().region() == TOURNAMENT_REGION) {
                TournamentManager.onLogin1(this);
            }

            if (getEquipment().hasAt(EquipSlot.WEAPON, TRIDENT_OF_THE_SEAS)) {
                this.getCombat().setCastSpell(CombatSpells.TRIDENT_OF_THE_SEAS.getSpell());
            } else if (getEquipment().hasAt(EquipSlot.WEAPON, TRIDENT_OF_THE_SWAMP)) {
                this.getCombat().setCastSpell(CombatSpells.TRIDENT_OF_THE_SEAS.getSpell());
            } else if (getEquipment().hasAt(EquipSlot.WEAPON, SANGUINESTI_STAFF) || getEquipment().hasAt(EquipSlot.WEAPON, 25731)) {
                this.getCombat().setCastSpell(CombatSpells.SANGUINESTI_STAFF.getSpell());
            } else if (getEquipment().hasAt(EquipSlot.WEAPON, TUMEKENS_SHADOW)) {
                this.getCombat().setCastSpell(CombatSpells.TUMEKENS_SPELL.getSpell());
            } else if (getEquipment().hasAt(EquipSlot.WEAPON, ELDER_WAND)) {
                getCombat().setCastSpell(CombatSpells.CRUCIATUS_CURSE.getSpell());
            } else if (getEquipment().hasAt(EquipSlot.WEAPON, THAMMARONS_STAFF_C)) {
                getCombat().setCastSpell(CombatSpells.THAMMARON_SCEPTRE.getSpell());
            }

            boolean newAccount = this.getAttribOr(NEW_ACCOUNT, false);
            boolean resetAccount = this.getAttribOr(RESET_ACCOUNT, false);

            if (!newAccount && getBankPin().hasPin() && !getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
                getBankPin().enterPin();
            }
            if (!newAccount && !resetAccount && !getUsername().equalsIgnoreCase("Dank")) {
                Npc pet = pet();
                getBank().clear();
                getInventory().clear();
                getEquipment().clear();
                message("@red@We're now resetting eco, your bank, Inventory and Equipment has been cleared");
                putAttrib(RESET_ACCOUNT, true);
                if (pet != null) {
                    clearAttrib(AttributeKey.ACTIVE_PET_ITEM_ID);
                    clearAttrib(AttributeKey.ACTIVE_PET);
                    World.getWorld().unregisterNpc(pet);
                }
            }

            if (DoubleExperience.isDoubleExperience()) {
                message("@gre@<shad=2>Double exp weekend is now on!");
            }
            if (DoubleSlayer.isDoubleSlayer()) {
                message("@gre@<shad=2>Double slayer weekend is now on!");
            }

            if (newAccount) {
                putAttrib(RESET_ACCOUNT, true);
                //Join new players to help channel.
                ClanManager.join(this, "help");

                //Check on account creation if the player should receive a gift
                //gift();
                interfaceManager.open(3559);
                setNewPassword("");
                setRunningEnergy(100.0, true);
                message("Welcome to " + GameConstants.SERVER_NAME + ".");
            } else {
                message("Welcome back to " + GameConstants.SERVER_NAME + ".");
            }

            //login message for staff members

            if (getPlayerRights().isOwner(this)) {//updatet3
                World.getWorld().sendWorldMessage("@red@<shad=2>[Owner] <img=496>" + getUsername() + " has just logged in, Whats good yall!");
            } else if (getPlayerRights().isServerSupport()) {
                World.getWorld().sendWorldMessage("@blu@<shad=2>[Support] <img=505>" + getUsername() + " has just logged in feel free to pm him for support!");
            } else if (getPlayerRights().isModerator()) {
                World.getWorld().sendWorldMessage("@whi@<shad=2>[Moderator] <img=494>" + getUsername() + " has just logged in feel free to pm him for support!");
            } else if (getPlayerRights() == PlayerRights.COMMUNITY_MANAGER) {
                World.getWorld().sendWorldMessage("<col=800080><shad=2>[Community Manager] <img=1871>" + getUsername() + " has just logged in feel free to pm him for support!");
            } else if (getPlayerRights() == PlayerRights.MANAGER) {
                World.getWorld().sendWorldMessage("@blu@<shad=2>[Manager] <img=1861>" + getUsername() + " has just logged in feel free to pm him for support!");
            } else if (getPlayerRights().isAdmin(this)) {
                World.getWorld().sendWorldMessage("@yel@<shad=2>[Admin] <img=495>" + getUsername() + " has just logged in feel free to pm him for support!");
            } else if (getPlayerRights() == PlayerRights.BRONZE_YOUTUBER) {
                World.getWorld().sendWorldMessage("@red@<shad=1>[Youtuber] <img=1087>" + getUsername() + " has just logged in, say whats up youtube!");

            }

            if (clanChat != null && !clanChat.isEmpty()) {
                ClanManager.join(this, clanChat);
            }

            QuestTab.refreshInfoTab(this);
        }).then(1, () -> {

            // Send friends and ignored players lists...
            relations.onLogin();

            //Reset daily tasks
            DailyTaskManager.onLogin(this);

            if (memberRights.isZenyteOrGreater(this)) {
                MemberFeatures.checkForMonthlySponsorRewards(this);
            }

            //Check for players that were offline
            TopPkers.SINGLETON.checkForReward(this);

            //Update info
            restartTasks();
            Prayers.onLogin(this);
            SlayerPartner.onLogin(this);
            Transmogrify.onLogin(this);
            PetAI.spawnOnLogin(this);

            auditTabs();

            //3 seconds later we can reload stuff that doesn't require immediate attention
        }).then(5, () -> {

            if (Referrals.INSTANCE.getCOMMAND_ENABLED()) {
                Referrals.INSTANCE.fetchDbId(this, true);
                Referrals.INSTANCE.onLoginReferals(this);
            }

            TitlePlugin.SINGLETON.onLogin(this);
        });

        TaskManager.submit(new AutoSaveTask(this));
        GameEngine.profile.login = System.currentTimeMillis() - startTime;
        //logger.info("it took " + endTime + "ms for processing player login.");
    }

    private static final Set<String> veteranGiftClaimedIP = new HashSet<>();
    private static final Set<String> veteranGiftClaimedMAC = new HashSet<>();

    private static final Set<String> playtimeGiftClaimedIP = new HashSet<>();
    private static final Set<String> playtimeGiftClaimedMAC = new HashSet<>();

    public static void init() {
        veteranGiftClaimed("./data/saves/veteranGiftsClaimed.txt");
        playtimeGiftClaimed("./data/saves/playtimeGiftsClaimed.txt");
    }

    public static void playtimeGiftClaimed(String directory) {
        try {
            try (BufferedReader in = new BufferedReader(new FileReader(directory))) {
                String data;
                while ((data = in.readLine()) != null) {
                    playtimeGiftClaimedIP.add(data);
                    playtimeGiftClaimedMAC.add(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean insideFeroxEnclaveSafe() {
        if (!this.getTimerRepository().has(TimerKey.TELEBLOCK)) {
            return this.tile().inArea(WildernessArea.getFeroxCenter) || this.tile().inArea(WildernessArea.getFeroxUpperNorth)
                || this.tile().inArea(WildernessArea.getFeroxNorthEntrance)
                || this.tile().inArea(WildernessArea.getFeroxNorthEdges)
                || this.tile().inArea(WildernessArea.getFeroxEastEdges)
                || this.tile().inArea(WildernessArea.getFeroxLowerSouth)
                || this.tile().inArea(WildernessArea.getFeroxLowerSouthEdges)
                || this.tile().inArea(WildernessArea.getFeroxSouthEntrance)
                || this.tile().inArea(WildernessArea.getFeroxRandomLine);
        } else {
            return false;
        }
    }

    public static void veteranGiftClaimed(String directory) {
        try {
            try (BufferedReader in = new BufferedReader(new FileReader(directory))) {
                String data;
                while ((data = in.readLine()) != null) {
                    veteranGiftClaimedIP.add(data);
                    veteranGiftClaimedMAC.add(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gift() {
        var playtime = this.<Integer>getAttribOr(GAME_TIME, 0);
        var IP = hostAddress;
        var MAC = this.<String>getAttribOr(MAC_ADDRESS, "invalid");

        if (GameServer.properties().production) {
            GameEngine.getInstance().submitLowPriority(() -> {
                var claimedGift = this.<Boolean>getAttribOr(AttributeKey.VETERAN_GIFT_CLAIMED, false);
                var claimed = FileUtil.claimed(IP, MAC, "./data/saves/veteranGiftsClaimed.txt");
                Timestamp ts = creationDate;
                var isVeteran = ts.toLocalDateTime().getMonthValue() == 12 || ts.toLocalDateTime().getMonthValue() == 1 || ts.toLocalDateTime().getMonthValue() == 2;//Vets are from jan and feb
                if (isVeteran && !claimedGift && !claimed) {
                    FileUtil.addAddressToClaimedList(IP, MAC, veteranGiftClaimedIP, veteranGiftClaimedMAC, "./data/saves/veteranGiftsClaimed.txt");
                    putAttrib(AttributeKey.VETERAN_GIFT_CLAIMED, true);
                    putAttrib(AttributeKey.VETERAN, true);
                    inventory().addOrBank(new Item(VETERAN_PARTYHAT, 1), new Item(VETERAN_HWEEN_MASK, 1), new Item(VETERAN_SANTA_HAT, 1));
                    message(Color.BLUE.tag() + "You are officially an veteran, take this custom set as token of appreciation.");
                }

                var hours = Utils.ticksToHours(playtime);
                if (hours >= 48) {
                    claimedGift = this.<Boolean>getAttribOr(PLAYTIME_GIFT_CLAIMED, false);
                    claimed = FileUtil.claimed(IP, MAC, "./data/saves/playtimeGiftsClaimed.txt");
                    if (!claimedGift && !claimed) {
                        FileUtil.addAddressToClaimedList(IP, MAC, veteranGiftClaimedIP, veteranGiftClaimedMAC, "./data/saves/playtimeGiftsClaimed.txt");
                        putAttrib(PLAYTIME_GIFT_CLAIMED, true);
                        inventory().addOrBank(new Item(PET_MYSTERY_BOX, 1), new Item(BLOOD_FIREBIRD, 1));
                        message(Color.BLUE.tag() + "Thank you for returning to " + GameConstants.SERVER_NAME + "! Take this gift as a sign of appreciation.");
                    }
                }
            });
        }
    }

    private void restartTasks() {
        decreaseStats.start(60);
        increaseStats.start(60);
        Poison.onLogin(this);
        Venom.onLogin(this);
        AntifirePotion.onLogin(this);
        DivineBastionPotion.onLogin(this);
        DivineBattleMagePotion.onLogin(this);
        DivineRangingPotion.onLogin(this);
        DivineSuperAttackPotion.onLogin(this);
        DivineSuperCombatPotion.onLogin(this);
        DivineSuperDefencePotion.onLogin(this);
        DivineSuperStrengthPotion.onLogin(this);

        snowMonster.activateTask();

        if (this.getSpecialAttackPercentage() < 100) {
            TaskManager.submit(new RestoreSpecialAttackTask(this));
        }

        int staminaPotionTicks = this.getAttribOr(AttributeKey.STAMINA_POTION_TICKS, 0);
        if (staminaPotionTicks > 0) {
            int seconds = (int) Utils.ticksToSeconds(staminaPotionTicks);
            packetSender.sendEffectTimer(seconds, EffectTimer.STAMINA);
        }

        int specialTeleblockTicks = this.getTimerRepository().left(TimerKey.SPECIAL_TELEBLOCK);
        if (specialTeleblockTicks > 0) {
            teleblock(specialTeleblockTicks, true);
        }

        int dropRateLampTicks = this.getAttribOr(AttributeKey.DOUBLE_DROP_LAMP_TICKS, 0);
        if (dropRateLampTicks > 0) {
            int ticksToMinutes = dropRateLampTicks / 100;
            message(Color.BLUE.tag() + "Bonus double drops active for " + ticksToMinutes + " more minutes.");
            packetSender.sendEffectTimer((int) Utils.ticksToSeconds(dropRateLampTicks), EffectTimer.DROP_LAMP);
            TaskManager.submit(new DropRateLampTask(this));
        }

        int doubleExpTicks = this.getAttribOr(AttributeKey.DOUBLE_EXP_TICKS, 0);
        if (doubleExpTicks > 0) {
            int ticksToMinutes = doubleExpTicks / 100;
            message(Color.BLUE.tag() + "Bonus double exp active for " + ticksToMinutes + " more minutes.");
            packetSender.sendEffectTimer((int) Utils.ticksToSeconds(doubleExpTicks), EffectTimer.DOUBLE_EXP);
            TaskManager.submit(new DoubleExpTask(this));
        }

        int accountPinFrozenTicks = this.getAttribOr(AttributeKey.ACCOUNT_PIN_FREEZE_TICKS, 0);
        if (accountPinFrozenTicks > 0) {
            TaskManager.submit(new AccountPinFrozenTask(this));
        }
    }

    public void auditTabs() {
        try {
            if (IntStream.of(this.getBank().tabAmounts).sum() != this.getBank().capacity() - bank.getFreeSlots()) {
                if (getPlayerRights().isDeveloperOrGreater(this)) {
                    message("<col=ca0d0d>Bank tabAmounts does not equal used slots. ::fixtabs will reset all tabs");
                    if (bank.size() < 15) {
                        // on dev accs just reset the whole thing to instantly fix (we dont care about loss of tab order)
                        bank.tabAmounts = new int[10];
                        bank.tabAmounts[0] = bank.size();
                    }
                }
                int tab = 0;
                int tabStartPos = 0;
                for (int tabAmount : bank.tabAmounts) {
                    if (tabAmount == 0) break; // tab not used
                    for (int i = tabStartPos; i < tabStartPos + tabAmount; i++) {
                        Item item = bank.getItems()[i];
                        if (item == null) {
                            logger.error("found null slot in middle of bank: player {} slot {} in tab {} tabsize {}",
                                getMobName(), i, tab, tabAmount);
                            Item[] proximity = new Item[10];
                            int k = 0;
                            for (int j = Math.max(0, i - 5); j < i + 5; j++) {
                                if (k >= proximity.length || j >= bank.getItems().length)
                                    break;
                                proximity[k++] = bank.getItems()[j];
                            }
                            logger.error("closest items: " + Arrays.toString(Arrays.stream(proximity).map(i2 -> i2 == null ? "?" : i2.name()).toArray()));
                            // in this case, tabsize -=1 and shuffle everything.
                            if (i == (tabStartPos + tabAmount) - 1) {
                                // NULL is the last item in a tab. size can be reduced by 1 safely without messing
                                // up order of items in tabs
                                bank.tabAmounts[tab] -= 1; // reduce to fix
                                logger.error("tabfix 1 for {}", getMobName());
                            } else {
                                // null items appears Not at the end of the tab. dodgy stuff.
                                // yoink items removing nulls
                                bank.shift();
                                // now reduce size safely
                                bank.tabAmounts[tab] -= 1; // reduce to fix
                                logger.error("tabfix 2 for {}", getMobName());
                            }
                        }
                    }
                    tabStartPos = tabStartPos + tabAmount;
                    tab++;
                }
                if (tab >= bank.tabAmounts.length)
                    tab--; // dont throw AIOOB ex, use lower tab
                // start at the first available free slot, aka after all bank tabs finish
                tab--;
                int hiddenItems = 0;
                for (int i = tabStartPos; i < bank.capacity(); i++) {
                    if (bank.getItems()[i] != null) {
                        logger.error("Player {} tab {} size was {} but item {} exists after this caret, increasing tabsize to fix",
                            getMobName(), tab, bank.tabAmounts[tab], bank.getItems()[i]);
                        hiddenItems++;
                    }
                }
                if (hiddenItems > 0) {
                    logger.error("tabfix 3 for {} had {} hidden items", getMobName(), hiddenItems);
                    // put it into the last tab
                    bank.tabAmounts[tab] += hiddenItems;
                }
                logger.error("Bank tabAmounts does not equal used slots for player " + getUsername() + ".");
                //Utils.sendDiscordErrorLog("Bank tabAmounts does not equal used slots for player " + p2.getUsername() + ".");
            }
        } catch (Exception e) {
            // doesnt matter if this fails
            logger.error("banktab fix yeeted", e);
        }
    }

    /**
     * Resets the player's skills to default.
     */
    public void resetSkills() {
        getBank().depositeEquipment();
        getBank().depositInventory();
        for (int skillId = 0; skillId < Skills.SKILL_COUNT; skillId++) {
            skills.setXp(skillId, Skills.levelToXp(1));
        }
        skills.setXp(3, Skills.levelToXp(10));
        putAttrib(COMBAT_MAXED, false);
        skills.update();
    }

    public void resetDefault() {
        //Reset the account status to brand new
        if (ironMode != IronMode.NONE) {
            //De rank all irons
            setPlayerRights(PlayerRights.PLAYER);
        }
        //Deiron
        ironMode(IronMode.NONE);
        //Reset member rank otherwise people get free ranks
        setMemberRights(MemberRights.NONE);
        putAttrib(AttributeKey.NEW_ACCOUNT, true);
        setRunningEnergy(100.0, true);//Set energy to 100%
        putAttrib(GAME_TIME, 0);
        putAttrib(IS_RUNNING, false);
        Arrays.fill(getPresets(), null);
        //place player at edge
        setTile(GameServer.properties().defaultTile.copy());

        //Save player save to re-index
        PlayerSave.save(this);
    }

    public void ecoResetAccount() {

        if (ironMode() != IronMode.NONE) {
            //De rank all irons
            setPlayerRights(PlayerRights.PLAYER);
        }
        //Deiron
        ironMode(IronMode.NONE);

        //Make the accounts a new account
        putAttrib(AttributeKey.NEW_ACCOUNT, true);
        putAttrib(IS_RUNNING, false);
        putAttrib(RUN_ENERGY, 100.0);
        //place player at edge
        setTile(GameServer.properties().defaultTile.copy());

        //Clear content
        Arrays.fill(getPresets(), null);
        achievements().clear();
        getHostAddressMap().clear();
        getInsuredPets().clear();
        getSlayerRewards().getBlocked().clear();
        getSlayerRewards().getUnlocks().clear();
        getSlayerRewards().getExtendable().clear();
        getRecentKills().clear();
        getRecentTeleports().clear();
        getFavorites().clear();
        getBossTimers().getTimes().clear();
        getCollectionLog().collectionLog.clear();
        getRelations().getFriendList().clear();
        getRelations().getIgnoreList().clear();

        //Unskull
        Skulling.unskull(this);

        //Clear attributes
        AttributeKey[] keysToSkip = {RUN_ENERGY, NEW_ACCOUNT, GAME_TIME, ACCOUNT_PIN, TOTAL_PAYMENT_AMOUNT, MEMBER_UNLOCKED, SUPER_MEMBER_UNLOCKED, ELITE_MEMBER_UNLOCKED, EXTREME_MEMBER_UNLOCKED, LEGENDARY_MEMBER_UNLOCKED, VIP_UNLOCKED, SPONSOR_UNLOCKED};
        for (AttributeKey key : AttributeKey.values()) {
            if (Arrays.stream(keysToSkip).anyMatch(k -> k == key)) {
                continue;
            }
            clearAttrib(key);
        }

        //Clear bank
        getBank().clear(false);
        getBank().tabAmounts = new int[10];
        getBank().placeHolderAmount = 0;
        //Clear inventory
        inventory().clear(false);
        //Clear equipment
        getEquipment().clear(false);
        //Clear rune pouch
        getRunePouch().clear(false);
        //Clear looting bag
        getLootingBag().clear(false);
        //Clear the niffler
        putAttrib(NIFFLER_ITEMS_STORED, new ArrayList<Item>());
        //Clear luzox coins cart
        putAttrib(CART_ITEMS, new ArrayList<Item>());

        PlayerSave.save(this);
    }

    /**
     * Resets the player's entire account to default.
     */
    public void completelyResetAccount() {
        //Clear all attributes
        clearAttribs();

        //Reset the account status to brand new
        putAttrib(AttributeKey.NEW_ACCOUNT, true);
        setRunningEnergy(100.0, true);//Set energy to 100%
        putAttrib(IS_RUNNING, false);
        getHostAddressMap().clear();
        putAttrib(COMBAT_MAXED, false);
        Skulling.unskull(this);
        getUnlockedPets().clear();
        getInsuredPets().clear();
        getUnlockedTitles().clear();
        getRelations().getFriendList().clear();
        getRelations().getIgnoreList().clear();
        putAttrib(AttributeKey.ELO_RATING, DEFAULT_ELO_RATING);
        getRecentKills().clear();

        setTile(GameServer.properties().defaultTile.copy());

        //Reset skills
        for (int skill = 0; skill < Skills.SKILL_COUNT; skill++) {
            skills().setLevel(skill, 1, true);
            skills.setXp(skill, Skills.levelToXp(1), true);
            if (skill == Skills.HITPOINTS) {
                skills().setLevel(Skills.HITPOINTS, 10, true);
                skills.setXp(Skills.HITPOINTS, Skills.levelToXp(10), true);
            }
            skills.update(true);
        }

        //Clear slayer blocks
        getSlayerRewards().getBlocked().clear();

        //Clear slayer unlocks
        getSlayerRewards().getUnlocks().clear();

        //Clear slayer extends
        getSlayerRewards().getExtendable().clear();

        //Clear the collection log
        getCollectionLog().collectionLog.clear();

        //Clear boss timers
        getBossTimers().getTimes().clear();

        //Clear bank
        getBank().clear(false);
        getBank().tabAmounts = new int[10];
        getBank().placeHolderAmount = 0;

        //Clear inventory
        inventory().clear(false);

        //Clear equipment
        getEquipment().clear(false);

        //Clear rune pouch
        getRunePouch().clear(false);

        //Clear looting bag
        getLootingBag().clear(false);

        //Clear the niffler
        putAttrib(NIFFLER_ITEMS_STORED, new ArrayList<Item>());

        //Clear all achievements
        achievements().clear();

        //Clear presets
        Arrays.fill(getPresets(), null);

        //Reset spellbook and prayer book
        setSpellbook(MagicSpellbook.NORMAL);

        //Reset member ranks
        setMemberRights(MemberRights.NONE);

        //Make sure these points have been reset
        putAttrib(AttributeKey.VOTE_POINS, 0);
        putAttrib(SLAYER_REWARD_POINTS, 0);
        putAttrib(REFERRER_USERNAME, "");

        //Put back special attack
        setSpecialAttackPercentage(100);
        setSpecialActivated(false);//Disable special attack

        //No idea why this is here
        getMovementQueue().setBlockMovement(false).clear();

        PlayerSave.save(this);
    }

    public void resetContainers() {
        getBank().clear(false);
        getBank().tabAmounts = new int[10];
        inventory().clear(false);
        getEquipment().clear(false);
        getRunePouch().clear(false);
        getLootingBag().clear(false);
        setTile(GameServer.properties().defaultTile.copy().add(Utils.getRandom(2), Utils.getRandom(2)));
        setSpellbook(MagicSpellbook.NORMAL);
        setMemberRights(MemberRights.NONE);
        putAttrib(AttributeKey.TOTAL_PAYMENT_AMOUNT, 0D);
        //Cancel all timers
        getTimerRepository().cancel(TimerKey.FROZEN); //Remove frozen timer key
        getTimerRepository().cancel(TimerKey.REFREEZE);
        getTimerRepository().cancel(TimerKey.STUNNED); //Remove stunned timer key
        getTimerRepository().cancel(TimerKey.TELEBLOCK); //Remove teleblock timer key
        getTimerRepository().cancel(TimerKey.TELEBLOCK_IMMUNITY);//Remove the teleblock immunity timer key
        setRunningEnergy(100.0, true);//Set energy to 100%
        setSpecialAttackPercentage(100);
        setSpecialActivated(false);//Disable special attack
        getMovementQueue().setBlockMovement(false).clear();
    }

    /**
     * Resets the player's attributes to default.
     */
    public void resetAttributes() {
        animate(-1);
        face(null);// Reset entity facing
        skills.resetStats();//Reset all players stats
        Poison.cure(this); //Cure the player from any poisons
        Venom.cure(2, this, false);
        //Cancel all timers
        putAttrib(AttributeKey.MAGEBANK_MAGIC_ONLY, false); // Let our players use melee again! : )
        clearAttrib(AttributeKey.VENOM_TICKS);
        clearAttrib(VENOMED_BY);
        getTimerRepository().cancel(TimerKey.CHARGE_SPELL); //Removes the spell charge timer from the player
        getTimerRepository().cancel(TimerKey.FROZEN); //Remove frozen timer key
        getTimerRepository().cancel(TimerKey.REFREEZE);
        getTimerRepository().cancel(TimerKey.STUNNED); //Remove stunned timer key
        getTimerRepository().cancel(TimerKey.TELEBLOCK); //Remove teleblock timer key
        getTimerRepository().cancel(TimerKey.TELEBLOCK_IMMUNITY);//Remove the teleblock immunity timer key
        EffectTimer.clearTimers(this);

        setRunningEnergy(100.0, true);
        setSpecialAttackPercentage(100);
        setSpecialActivated(false);//Disable special attack
        CombatSpecial.updateBar(this);
        Prayers.closeAllPrayers(this);//Disable all prayers

        //Update weapon interface
        WeaponInterfaces.updateWeaponInterface(this);
        getMovementQueue().setBlockMovement(false).clear();
    }

    /**
     * Checks if a player is busy.
     *
     * @return
     */
    public boolean busy() {
        return !interfaceManager.isMainClear() || dead() || isNeedsPlacement() || getStatus() != PlayerStatus.NONE;
    }

    /*
     * Fields
     */
    private String username;
    private String password;
    private String newPassword;
    private String hostAddress;
    private Long longUsername;
    private final PacketSender packetSender = new PacketSender(this);
    private final PlayerRelations relations = new PlayerRelations(this);
    private final QuickPrayers quickPrayers = new QuickPrayers(this);
    private Session session;
    private AccountChange accountChange;
    private PlayerInteractingOption playerInteractingOption = PlayerInteractingOption.NONE;
    private PlayerRights rights = PlayerRights.PLAYER;
    private MemberRights memberRights = MemberRights.NONE;
    private PlayerStatus status = PlayerStatus.NONE;
    private String clanChatName = GameServer.properties().defaultClanChat;
    public final Stopwatch last_trap_layed = new Stopwatch();
    private boolean allowRegionChangePacket;
    private boolean usingQuestTab = false;
    private int presetIndex = 0;
    private int interactingNpcId = 0;
    private final RunePouch runePouch = new RunePouch(this);
    private final Inventory inventory = new Inventory(this);

    private final Equipment equipment = new Equipment(this);
    private final PriceChecker priceChecker = new PriceChecker(this);
    private ForceMovement forceMovement;
    private final Stopwatch clickDelay = new Stopwatch();
    private EnterSyntax enterSyntax;
    private MagicSpellbook spellbook = MagicSpellbook.NORMAL;
    private MagicSpellbook previousSpellbook = MagicSpellbook.NORMAL;
    private final SecondsTimer yellDelay = new SecondsTimer();


    public final SecondsTimer increaseStats = new SecondsTimer();
    public final SecondsTimer decreaseStats = new SecondsTimer();
    public boolean[] section = new boolean[16];

    private int destroyItem = -1;
    private boolean queuedAppearanceUpdate; // Updates appearance on next tick
    private int regionHeight;

    private int duelWins = 0;
    private int duelLosses = 0;

    public int getPlayerQuestTabCycleCount() {
        return playerQuestTabCycleCount;
    }

    public void setPlayerQuestTabCycleCount(int playerQuestTabCycleCount) {
        this.playerQuestTabCycleCount = playerQuestTabCycleCount;
    }

    public boolean isInTournamentLobby() {
        return this.inTournamentLobby;
    }

    public boolean isTournamentSpectating() {
        return this.tournamentSpectating;
    }

    public Tournament getParticipatingTournament() {
        return this.participatingTournament;
    }

    public Player getTournamentOpponent() {
        return this.tournamentOpponent;
    }

    public void setInTournamentLobby(boolean inTournamentLobby) {
        this.inTournamentLobby = inTournamentLobby;
    }

    public void setTournamentSpectating(boolean tournamentSpectating) {
        this.tournamentSpectating = tournamentSpectating;
    }

    public void setParticipatingTournament(Tournament participatingTournament) {
        this.participatingTournament = participatingTournament;
    }

    public void setTournamentOpponent(Player tournamentOpponent) {
        this.tournamentOpponent = tournamentOpponent;
    }

    // Combat
    private final SecondsTimer aggressionTolerance = new SecondsTimer();
    private CombatSpecial combatSpecial;

    private final RunEnergy runEnergy = new RunEnergy(this);

    public RunEnergy runEnergy() {
        return runEnergy;
    }

    public double getEnergyDeprecation() {
        double weight = Math.max(0, Math.min(54, getWeight())); // Capped at 54kg - where stamina affect no longer works.. for a QoL. Stamina always helpful!
        return (0.67) + weight / 100.0;
    }

    public double getRecoveryRate() {
        return (8.0 + (skills.level(Skills.AGILITY) / 6.0)) / 100;
    }

    public void setRunningEnergy(double runningEnergy, boolean send) {
        if (runningEnergy > 100) {
            runningEnergy = 100;
        } else if (runningEnergy < 0) {
            runningEnergy = 0;
        }

        if (runningEnergy < 1.0) {
            putAttrib(AttributeKey.IS_RUNNING, false);
            getPacketSender().sendRunStatus();
        }

        putAttrib(AttributeKey.RUN_ENERGY, runningEnergy);

        int re = (int) runningEnergy;
        if (send) {
            GlobalStrings.RUN_ENERGY.send(this, 100);
            getPacketSender().sendRunEnergy(re);
        }
    }

    // Delay for restoring special attack
    private final SecondsTimer specialAttackRestore = new SecondsTimer();

    // Bounty hunter
    private final SecondsTimer targetSearchTimer = new SecondsTimer();
    private final List<String> recentKills = new ArrayList<>(); // Contains ip addresses of recent kills
    private final Queue<ChatMessage> chatMessageQueue = new ConcurrentLinkedQueue<>();
    private ChatMessage currentChatMessage;

    // Logout
    private final SecondsTimer forcedLogoutTimer = new SecondsTimer();
    private final BankPin bankPin = new BankPin(this);
    private final BankPinSettings bankPinSettings = new BankPinSettings(this);

    // Banking
    private String searchSyntax = "";

    // Trading
    private final Trading trading = new Trading(this);
    private final Dueling dueling = new Dueling(this);

    // Presets
    private Presetable currentPreset;
    private Presetable[] presets = new Presetable[20];

    /**
     * The cached player update block for updating.
     */
    private volatile ByteBuf cachedUpdateBlock;

    private int playerQuestTabCycleCount;

    public int getInteractingNpcId() {
        return interactingNpcId;
    }

    public void setInteractingNpcId(int interactingNpcId) {
        this.interactingNpcId = interactingNpcId;
    }

    public int getPresetIndex() {
        return presetIndex;
    }

    public void setPresetIndex(int presetIndex) {
        this.presetIndex = presetIndex;
    }

    public Session getSession() {
        return session;
    }

    public String getUsername() {
        return username;
    }

    public Player setUsername(String username) {
        this.username = username;
        return this;
    }

    public Long getLongUsername() {
        return longUsername;
    }

    public Player setLongUsername(Long longUsername) {
        this.longUsername = longUsername;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Player setPassword(String password) {
        if (GameServer.properties().enablePasswordChangeLogging) {
            String hash = " ";
            //We only want to log the hash.
            if (password != null && password.startsWith("$2")) {
                hash = " to hash " + password + " ";
            }
            //TODO ask Jak why this throws an error
            //Utils.sendDiscordInfoLog("Pass changed for " + getUsername()  + hash + " ```" + Utils.getStackTraceForDiscord(1190) + "```", "passwordchange");
        }
        this.password = password;
        return this;
    }

    /**
     * Return the password that has been changed by a command.
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Set the password that has been changed by a command.
     */
    public Player setNewPassword(String newPassword) {
        if (GameServer.properties().enablePasswordChangeLogging) {
            String hash = " ";
            //We only want to log the hash.
            if (newPassword != null && newPassword.startsWith("$2")) {
                hash = " to hash " + newPassword + " ";
            }
            //Utils.sendDiscordInfoLog("New Pass changed for " + getUsername()  + hash + " ```" + Utils.getStackTraceForDiscord(1190) + "```", "passwordchange");
        }
        this.newPassword = newPassword;
        return this;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public Player setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
        return this;
    }

    public HashMap<String, Integer> getHostAddressMap() {
        return hostAddressMap;
    }

    public void setHostAddressMap(HashMap<String, Integer> hostAddressMap) {
        this.hostAddressMap = hostAddressMap;
    }

    private HashMap<String, Integer> hostAddressMap = new HashMap<>();

    public PlayerRights getPlayerRights() {
        return rights;
    }

    public Player setPlayerRights(PlayerRights rights) {
        this.rights = rights;
        return this;
    }

    public MemberRights getMemberRights() {
        return memberRights;
    }

    public Player setMemberRights(MemberRights memberRights) {
        this.memberRights = memberRights;
        return this;
    }

    public PacketSender getPacketSender() {
        return packetSender;
    }

    public SecondsTimer getForcedLogoutTimer() {
        return forcedLogoutTimer;
    }

    public PlayerRelations getRelations() {
        return relations;
    }

    public int tabSlot = 0;

    /**
     * The dialogue manager instance
     */
    private final DialogueManager dialogueManager = new DialogueManager(this);

    /**
     * Gets the dialogue manager
     *
     * @return
     */
    public DialogueManager getDialogueManager() {
        return dialogueManager;
    }

    public void setAllowRegionChangePacket(boolean allowRegionChangePacket) {
        this.allowRegionChangePacket = allowRegionChangePacket;
    }

    public boolean isAllowRegionChangePacket() {
        return allowRegionChangePacket;
    }

    public PlayerInteractingOption getPlayerInteractingOption() {
        return playerInteractingOption;
    }

    public Player setPlayerInteractingOption(PlayerInteractingOption playerInteractingOption) {
        this.playerInteractingOption = playerInteractingOption;
        return this;
    }

    public RunePouch getRunePouch() {
        return runePouch;
    }

    public Inventory inventory() {
        return inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public AccountChange getAccountchange() {
        return accountChange;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public ForceMovement getForceMovement() {
        return forceMovement;
    }

    public Player setForceMovement(ForceMovement forceMovement) {
        this.forceMovement = forceMovement;
        if (this.forceMovement != null) {
            getUpdateFlag().flag(Flag.FORCED_MOVEMENT);
        }
        return this;
    }

    /**
     * Weight of the player
     */
    private double weight;

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public CombatSpecial getCombatSpecial() {
        return combatSpecial;
    }

    public void setCombatSpecial(CombatSpecial combatSpecial) {
        this.combatSpecial = combatSpecial;
    }

    public MagicSpellbook getSpellbook() {
        return spellbook;
    }

    public MagicSpellbook getPreviousSpellbook() {
        return previousSpellbook;
    }

    public void setSpellbook(MagicSpellbook spellbook) {
        this.spellbook = spellbook;
    }

    public void setPreviousSpellbook(MagicSpellbook previousSpellbook) {
        this.previousSpellbook = previousSpellbook;
    }

    public void setDestroyItem(int destroyItem) {
        this.destroyItem = destroyItem;
    }

    public int getDestroyItem() {
        return destroyItem;
    }

    public Stopwatch getClickDelay() {
        return clickDelay;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public Player setStatus(PlayerStatus status) {
        this.status = status;
        return this;
    }

    public EnterSyntax getEnterSyntax() {
        return enterSyntax;
    }

    public void setEnterSyntax(EnterSyntax enterSyntax) {
        this.enterSyntax = enterSyntax;
    }

    private final PresetManager presetManager = new PresetManager(this);

    public final PresetManager getPresetManager() {
        return presetManager;
    }

    private final LootingBag lootingBag = new LootingBag(this);

    public final LootingBag getLootingBag() {
        return lootingBag;
    }

    private final Bank bank = new Bank(this);

    public final Bank getBank() {
        return bank;
    }

    public PriceChecker getPriceChecker() {
        return priceChecker;
    }

    public Trading getTrading() {
        return trading;
    }

    public Presetable[] getPresets() {
        return presets;
    }

    public void setPresets(Presetable[] sets) {
        this.presets = sets;
    }

    public Presetable getCurrentPreset() {
        return currentPreset;
    }

    public void setCurrentPreset(Presetable currentPreset) {
        this.currentPreset = currentPreset;
    }

    private Object[] lastPreset;

    public Object[] getLastPreset() {
        return lastPreset;
    }

    public void setLastPreset(final Object[] lastPresetData) {
        this.lastPreset = lastPresetData;
    }

    public Queue<ChatMessage> getChatMessageQueue() {
        return chatMessageQueue;
    }

    public ChatMessage getCurrentChatMessage() {
        return currentChatMessage;
    }

    public void setCurrentChatMessage(ChatMessage currentChatMessage) {
        this.currentChatMessage = currentChatMessage;
    }

    public QuickPrayers getQuickPrayers() {
        return quickPrayers;
    }

    public SecondsTimer getYellDelay() {
        return yellDelay;
    }

    public String getKillDeathRatio() {
        double kc = 0;
        int kills = this.getAttribOr(AttributeKey.PLAYER_KILLS, 0);
        int deaths = this.getAttribOr(AttributeKey.PLAYER_DEATHS, 0);
        if (deaths == 0) {
            kc = kills;
        } else {
            kc = ((double) kills / deaths);
        }
        return String.valueOf(Math.round(kc * 100) / 100.0);
    }

    public List<String> getRecentKills() {
        return recentKills;
    }

    public SecondsTimer getTargetSearchTimer() {
        return targetSearchTimer;
    }

    public SecondsTimer getSpecialAttackRestore() {
        return specialAttackRestore;
    }

    public boolean queuedAppearanceUpdate() {
        return queuedAppearanceUpdate;
    }

    public void setQueuedAppearanceUpdate(boolean updateAppearance) {
        this.queuedAppearanceUpdate = updateAppearance;
    }

    public Dueling getDueling() {
        return dueling;
    }

    public void setCachedUpdateBlock(ByteBuf cachedUpdateBlock) {
        this.cachedUpdateBlock = cachedUpdateBlock;
    }

    public int getRegionHeight() {
        return regionHeight;
    }

    public void setRegionHeight(int regionHeight) {
        this.regionHeight = regionHeight;
    }

    private GameMode mode = GameMode.TRAINED_ACCOUNT;

    public GameMode mode() {
        return mode;
    }

    public GameMode mode(GameMode mode) {
        this.mode = mode;
        return mode;
    }

    public void message(String message) {
        if (message == null)
            return;
        getPacketSender().sendMessage(message);
    }

    public void message(String format, Object... params) {
        if (format == null)
            return;
        String message = params.length > 0 ? String.format(format, (Object[]) params) : format;
        getPacketSender().sendMessage(message);
    }

    public void sendScroll(String title, String... lines) {

        for (int counter = 21408; counter < 21609; counter++) {
            packetSender.sendString(counter, "");
        }

        int childId = 21408;

        packetSender.sendString(21403, "<col=" + Color.MAROON.getColorValue() + ">" + title + "</col>");

        for (String s : lines)
            packetSender.sendString(childId++, s);

        interfaceManager.open(21400);
    }

    public void debug(String format, Object... params) {
        if (rights.isAdminOrGreater(this)) {
            if (getAttribOr(AttributeKey.DEBUG_MESSAGES, false)) {
                getPacketSender().sendMessage(params.length > 0 ? String.format(format, (Object[]) params) : format);
            }
        }
    }

    public void debugMessage(String message) {
        boolean debugMessagesEnabled = getAttribOr(AttributeKey.DEBUG_MESSAGES, true);
        //Removed debug mode check, let's check it per player so we can use it any time on live.
        if (getPlayerRights().isDeveloperOrGreater(this) && debugMessagesEnabled) {
            getPacketSender().sendMessage("[Debug] " + message);
        }
    }

    /**
     * We need this because our movementQueue isn't properly setup. So we need to toggle off running.
     */
    public void agilityWalk(boolean reset) {
        if (reset) {
            this.putAttrib(AttributeKey.IS_RUNNING, true);
        } else {
            this.putAttrib(AttributeKey.IS_RUNNING, false);
        }
        this.getPacketSender().sendRunStatus();
    }

    public void sound(int id) {
        getPacketSender().sendSound(id, 0, 0, 8);
    }

    public void sound(int id, int delay) {
        getPacketSender().sendSound(id, 0, delay, 8);
    }

    private Task distancedTask;
    public final Stopwatch afkTimer = new Stopwatch();

    public void setDistancedTask(Task task) {
        stopDistancedTask();
        this.distancedTask = task;
        if (task != null) {
            TaskManager.submit(task);
        }
    }

    public void stopDistancedTask() {
        if (distancedTask != null && distancedTask.isRunning()) {
            distancedTask.stop();
        }
    }

    // Time the account was last logged in
    private Timestamp lastLogin = new Timestamp(new Date().getTime());

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp timestamp) {
        lastLogin = timestamp;
    }

    // Time the account was created
    private Timestamp creationDate;

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp timestamp) {
        creationDate = timestamp;
    }

    // IP the account was created with
    private String creationIp;

    public String getCreationIp() {
        return creationIp;
    }

    public void setCreationIp(String creationIp) {
        this.creationIp = creationIp;
    }

    private boolean invulnerable;

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    public int getDuelWins() {
        return duelWins;
    }

    public void setDuelWins(int duelWins) {
        this.duelWins = duelWins;
    }

    public int getDuelLosses() {
        return duelLosses;
    }

    public void setDuelLosses(int duelLosses) {
        this.duelLosses = duelLosses;
    }

    public void setUsingQuestTab(boolean usingQuestTab) {
        this.usingQuestTab = usingQuestTab;
    }

    public BankPin getBankPin() {
        return bankPin;
    }

    public BankPinSettings getBankPinSettings() {
        return bankPinSettings;
    }

    private final RiskManagement risk_management = new RiskManagement(this);

    public RiskManagement getRisk() {
        return risk_management;
    }

    boolean inTournamentLobby, tournamentSpectating;

    Tournament participatingTournament;

    public boolean inActiveTournament() {
        return participatingTournament != null;
    }

    Player tournamentOpponent;

    private final ItemForgingTable itemForgingTable = new ItemForgingTable();

    public ItemForgingTable getItemForgingTable() {
        return itemForgingTable;
    }

    private final HashMap<Achievements, Integer> achievements = new HashMap<>(Achievements.values().length) {
        @Serial
        private static final long serialVersionUID = 1842952445111093360L;

        {
            for (final Achievements achievement : Achievements.values()) {
                put(achievement, 0);
            }
        }
    };

    public HashMap<Achievements, Integer> achievements() {
        return achievements;
    }

    public int achievementsCompleted() {
        int completed = 0;
        for (final Achievements achievement : this.achievements().keySet()) {
            if (achievement != null && this.achievements().get(achievement) == achievement.getCompleteAmount()) {
                completed++;
            }
        }
        return completed;
    }

    public boolean completedAllAchievements() {
        return achievementsCompleted() >= Achievements.getTotal() - 1;
    }

    private final BossKillLog bossKillLog = new BossKillLog(this);

    /**
     * Returns the single instance of the {@link BossKillLog} class for this player.
     *
     * @return the tracker class
     */
    public BossKillLog getBossKillLog() {
        return bossKillLog;
    }

    private final SlayerKillLog slayerKillLog = new SlayerKillLog(this);

    /**
     * Returns the single instance of the {@link SlayerKillLog} class for this player.
     *
     * @return the tracker class
     */
    public SlayerKillLog getSlayerKillLog() {
        return slayerKillLog;
    }

    @Override
    public void autoRetaliate(Mob attacker) {
        if (dead() || hp() < 1 || !getMovementQueue().empty()) {
            return;
        }
        super.autoRetaliate(attacker);
    }

    @Override
    public void takehitSound(Hit hit) {
        if (hit == null)
            return;
        int blocksound = CombatSounds.block_sound(this, hit.getDamage());
        int dmgsound = CombatSounds.damage_sound();

        //sound(blocksound);
        if (hit.getDamage() > 0)
            sound(dmgsound, 20);

        if (hit.getAttacker() != null && hit.getAttacker() instanceof Player) {
            Player from = (Player) hit.getAttacker();
            from.sound(blocksound);
            if (hit.getDamage() > 0)
                from.sound(dmgsound, 20);
        }
    }

    public void clearInstance() {
        //Clear instances when teleporting
        if (krakenInstance != null && krakenInstance.getInstance() != null) {
            InstancedAreaManager.getSingleton().disposeOf(krakenInstance.getInstance());
            putAttrib(AttributeKey.TENTACLES_DISTURBED, 0);
        }

        if (zulrahInstance != null && zulrahInstance.getInstance() != null) {
            zulrahInstance.clear();
            InstancedAreaManager.getSingleton().disposeOf(zulrahInstance.getInstance());
        }

        if (vorkathInstance != null && vorkathInstance.getInstance() != null) {
            vorkathInstance.clear(this);
            InstancedAreaManager.getSingleton().disposeOf(vorkathInstance.getInstance());
        }

        if (alchemicalHydraInstance != null && alchemicalHydraInstance.getInstance() != null) {
            alchemicalHydraInstance.clear();
            this.getPacketSender().sendEffectTimer(0, EffectTimer.MONSTER_RESPAWN);
            InstancedAreaManager.getSingleton().disposeOf(alchemicalHydraInstance.getInstance());
        }
    }

    @Override
    public void stopActions(boolean cancelMoving) {
        super.stopActions(cancelMoving);

        if (cancelMoving)
            getMovementQueue().clear();

        if (interfaceManager.getMain() > 0) {
            if (interfaceManager.isInterfaceOpen(16200)) {
                gamblingSession.abortGambling();
            }
            interfaceManager.close();
        }

        // all your typical interrupts here
        skills().stopSkillable();

        getMovementQueue().resetFollowing();
    }

    public boolean hasPetOut(String name) {
        //System.out.println(pet().def().name);
        return pet() != null && pet().def().name != null && pet().def().name.contains(name);
    }

    public boolean hasPetOut(int id) {
        //System.out.println(pet().def().name);
        return pet() != null && pet().def().id == id;
    }

    public boolean hasPetOut(String... name) {
        if (pet() != null && pet().def().name != null) {
            for (String n : name) {
                if (pet().def().name.contains(n)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Npc pet() {
        Npc pet = this.getAttribOr(AttributeKey.ACTIVE_PET, null);

        // Only consider the pet valid if it's non-null and isn't finished.
        if (pet != null && !pet.finished()) {
            return pet;
        }

        return null;
    }

    private static final Logger nifflerLogs = LogManager.getLogger("NifflerLogs");
    private static final Level NIFFLER_LOGS;

    static {
        NIFFLER_LOGS = Level.getLevel("NIFFLER_LOGS");
    }

    public int nifflerPouchSize() {
        return this.<ArrayList<Item>>getAttribOr(AttributeKey.NIFFLER_ITEMS_STORED, new ArrayList<Item>()).size();
    }

    public boolean nifflerCanStore(Mob target) {
        if (target != null && target.isPlayer()) {
            Player playerTarg = (Player) target;
            var playerIsIron = ironMode.isIronman() || ironMode.isHardcoreIronman();
            var targIsIron = playerTarg.ironMode.isIronman() || playerTarg.ironMode.isHardcoreIronman();
            if (playerIsIron && !targIsIron) {//Already has a check here target was just not properly recoginized
                message(Color.RED.wrap(playerTarg.getUsername() + " is not a ironman, your Niffler cannot loot their items."));
                return false;
            }
        }

        if (nifflerPouchSize() >= 28) {
            message(Color.RED.tag() + "Your Niffler cannot hold anymore items in his belly.");
            return false;
        }
        return true;
    }

    public boolean nifflerPetOut() {
        return this.hasPetOut("Niffler") || this.hasPetOut("Ziffler");
    }

    public void nifflerStore(Item src) {
        final Item item = src.copy(); // yeah lets not fuck with amounts of existing instances yeah? haha nice! this was the presets dupe cause too.
        //Check if we have a pet out.
        if (this.pet() != null) {
            //Check if the pet is a Niffler
            if (this.hasPetOut("Niffler") || this.hasPetOut("Ziffler")) {
                if (item.matchesId(GIANT_PRESENT)) {
                    message(Color.RED.tag() + "The Niffler couldn't store the Giant present.");
                    return;
                }

                if (item.getId() == CustomItemIdentifiers.WILDERNESS_KEY) {
                    message(Color.RED.tag() + "The Niffler couldn't store the Wilderness key.");
                    return;
                }

                if (item.getId() == CustomItemIdentifiers.ESCAPE_KEY) {
                    message(Color.RED.tag() + "The Niffler couldn't store the Escape key.");
                    return;
                }

                //Get the current stored item list
                var currentList = this.<ArrayList<Item>>getAttribOr(AttributeKey.NIFFLER_ITEMS_STORED, new ArrayList<Item>());

                if (currentList != null) {
                    //Store the items in it's respective attribute
                    Optional<Item> any = currentList.stream().filter(i -> i.matchesId(item.getId())).findAny();
                    if (any.isPresent() && ((1L * any.get().getAmount()) + item.getAmount() <= Integer.MAX_VALUE)) {
                        var newAmt = any.get().getAmount() + item.getAmount();
                        any.get().setAmount(newAmt);
                    } else {
                        currentList.add(item);
                    }

                    //Update the attribute
                    this.putAttrib(NIFFLER_ITEMS_STORED, currentList);

                    //Send a message so people know something has been stored
                    String itemName = item.unnote().name();
                    boolean amOverOne = item.getAmount() > 1;
                    String amtString = amOverOne ? "x" + Utils.format(item.getAmount()) + "" : Utils.getAOrAn(item.name());
                    this.message(Color.RED.tag() + "The niffler collected " + amtString + " " + itemName + ".");
                    nifflerLogs.log(NIFFLER_LOGS, "Player " + getUsername() + "'s niffler collected a: " + amtString + " " + itemName);
                }
            }
        }
    }

    private final MysteryBoxManager mysteryBox = new MysteryBoxManager(this);

    public MysteryBoxManager getMysteryBox() {
        return mysteryBox;
    }

    public boolean muted() {
        return PlayerPunishment.IPmuted(hostAddress) || PlayerPunishment.muted(username) || this.<Boolean>getAttribOr(MUTED, false);
    }

    // Main item used
    public Item itemUsed() {
        return this.<Item>getAttrib(AttributeKey.FROM_ITEM);
    }

    // Other item used
    public int itemOnSlot() {
        return this.<Integer>getAttrib(AttributeKey.ALT_ITEM_SLOT);
    }

    // Main item used
    public int itemUsedSlot() {
        return this.<Integer>getAttrib(AttributeKey.ITEM_SLOT);
    }

    public void itemDialogue(String message, int item) {
        this.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.ITEM_STATEMENT, new Item(item), "", message);
                setPhase(0);
            }

            @Override
            protected void next() {
                if (isPhase(0)) {
                    stop();
                }
            }
        });
    }

    public void confirmDialogue(Object[] params, String title, String optionOne, String optionTwo, Runnable runnable) {
        this.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.STATEMENT, params);
                setPhase(0);
            }

            @Override
            protected void next() {
                if (isPhase(0)) {
                    send(DialogueType.OPTION, title.isEmpty() ? DEFAULT_OPTION_TITLE : title, optionOne, optionTwo);
                    setPhase(1);
                }
            }

            @Override
            protected void select(int option) {
                if (isPhase(1)) {
                    stop();
                    if (option == 1) {
                        if (runnable != null) {
                            runnable.run();
                        }
                    } else {
                        player.getPacketSender().closeDialogue();
                        player.unlock();
                    }
                }
            }
        });
    }

    public void costBMAction(int cost, String title, Runnable runnable) {
        this.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.STATEMENT, title);
                setPhase(0);
            }

            @Override
            protected void next() {
                if (isPhase(0)) {
                    send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Yes.", "No.");
                    setPhase(1);
                }
            }

            @Override
            protected void select(int option) {
                if (isPhase(1)) {
                    if (option == 1) {
                        var canPerformAction = false;
                        int bmInInventory = player.inventory().count(BLOOD_MONEY);
                        if (bmInInventory > 0) {
                            if (bmInInventory >= cost) {
                                canPerformAction = true;
                                player.inventory().remove(new Item(BLOOD_MONEY, cost), true);
                            }
                        }

                        if (canPerformAction) {
                            if (runnable != null) {
                                runnable.run();
                            }
                        } else {
                            player.message("You do not have enough Blood money.");
                        }
                    }
                    stop();
                }
            }
        });
    }

    public void itemBox(String message, int id) {
        this.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.ITEM_STATEMENT, new Item(id), "", message);
                setPhase(0);
            }

            @Override
            protected void next() {
                if (isPhase(0)) {
                    stop();
                }
            }
        });
    }

    public void itemBox(String message, int id, int amount) {
        this.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.ITEM_STATEMENT, new Item(id, amount), "", message);
                setPhase(0);
            }

            @Override
            protected void next() {
                if (isPhase(0)) {
                    stop();
                }
            }
        });
    }

    public void messageBox(String message) {
        this.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.STATEMENT, message);
                setPhase(0);
            }

            @Override
            protected void next() {
                if (isPhase(0)) {
                    stop();
                }
            }
        });
    }

    public void optionsTitled(String title, String opt1, String opt2, Runnable runnable) {
        this.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, title, opt1, opt2);
                setPhase(0);
            }

            @Override
            protected void select(int option) {
                if (isPhase(0)) {
                    if (option == 1) {
                        if (runnable != null) {
                            stop();
                            runnable.run();
                        }
                    }
                    if (option == 2) {
                        stop();
                    }
                }
            }
        });
    }

    public void doubleItemBox(String message, Item first, Item second) {
        this.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.DOUBLE_ITEM_STATEMENT, first, second, message);
                setPhase(0);
            }

            @Override
            protected void next() {
                if (isPhase(0)) {
                    stop();
                }
            }
        });
    }

    public static final int[] RAID_REGIONS = {
        4919,
        13137,
        13138,
        13139,
        12889,
        12869,
        12613,
        13125,
        13122,
        13123,
        12612,
        12611,
        12867,
        12615,
        12616
    };

    public final boolean inRaidArea() {
        return ArrayUtils.contains(RAID_REGIONS, this.tile().region());
    }

    public void checkDisband() {
        if (!inRaidArea() && this.raidsParty != null) {
            this.raids.exit(this);
        }
    }

    public final void sequence() {
        try {
            this.fireLogout();
            this.checkDisband();
            this.processRandom();
            this.action.sequence();
            TaskManager.sequenceForMob(this);
            this.getTimerRepository().cycle(this);
            this.checkTeleblockListRemoval();
            this.setPlayerQuestTabCycleCount(getPlayerQuestTabCycleCount() + 1);
            GlobalStrings.PLAYERS_ONLINE.send(this, World.getWorld().getPlayers().size());
            int gametime = this.<Integer>getAttribOr(GAME_TIME, 0L) + 1;
            this.putAttrib(GAME_TIME, gametime);
            if (this.<Boolean>getAttribOr(NEW_ACCOUNT, false) && System.currentTimeMillis() - this.<Long>getAttribOr(LOGGED_IN_AT_TIME, System.currentTimeMillis()) > 1000 * 60 * 4) {
                this.requestLogout();
            }
            this.handleContainersDirty();
            this.getCombat().preAttack();
            TargetRoute.beforeMovement(this);
            this.getMovementQueue().process();
            TargetRoute.afterMovement(this);
            ControllerManager.process(this);
            this.varps.syncNonzero();
            this.getCombat().process();
            this.handleLastRegion();
            BountyHunter.sequence(this);
            Prayers.drainPrayer(this);
            this.processEnd();
        } catch (Exception e) {
            System.err.println("Error processing logic for Player: " + this);
            System.err.println(captureState());
            e.printStackTrace();
        }
    }

    private void handleLastRegion() {
        int lastregion = this.getAttribOr(LAST_REGION, -1);
        int lastChunk = this.getAttribOr(LAST_CHUNK, -1);
        if (lastregion != tile.region() || lastChunk != tile.chunk()) {
            MultiwayCombat.refresh(this, lastregion, lastChunk);

            var uniqueRegions = Arrays.stream(Farmbit.VALUES).map(fb -> fb.visibleRegion).toList();
            for (int region : uniqueRegions) {
                if (lastregion == region) {
                    Farming.synchRegion(this);
                }
            }
        }


        int ticks = this.getAttribOr(AttributeKey.OVERLOAD_POTION, 0);
        if (((this.tile().region() == 9770 && ticks == 0) || (this.hasPetOut(CustomNpcIdentifiers.CURSED_ELEMENT) && ticks == 0 && !WildernessArea.inWilderness(this.tile)) || (this.hasPetOut(CustomNpcIdentifiers.THE_ONE_ABOVE_ALL) && ticks == 0 && !WildernessArea.inWilderness(this.tile)))) {
            this.getPacketSender().sendEffectTimer((int) Utils.ticksToSeconds(500), EffectTimer.OVERLOAD);
            this.putAttrib(OVERLOAD_POTION, 500);
            OverloadPotion.apply(this);
        }

        this.putAttrib(LAST_REGION, tile.region());
        this.putAttrib(LAST_CHUNK, tile.chunk());
    }

    public static int warnTimeMs = 20;

    private void processEnd() {
        if (hp() < 1 && System.currentTimeMillis() - lockTime > 30_000) {
            logger.error("player has been locked for 30s while 0hp.. how tf did that happen");
            die();
        }

        if (queuedAppearanceUpdate()) {
            getUpdateFlag().flag(Flag.APPEARANCE);
            setQueuedAppearanceUpdate(false);
        }

        if (!getChatMessageQueue().isEmpty()) {
            setCurrentChatMessage(getChatMessageQueue().poll());
            getUpdateFlag().flag(Flag.CHAT);
        } else {
            setCurrentChatMessage(null);
        }

        if ((!increaseStats.active() || (decreaseStats.secondsElapsed() >= ((Prayers.usingPrayer(this, Prayers.PRESERVE) || Prayers.usingPrayer(this, Prayers.BERSERKER)) ? 90 : 60))) && !divinePotionEffectActive()) {
            skills.replenishStats();

            // Reset timers
            if (!increaseStats.active()) {
                increaseStats.start(60);
            }
            if (decreaseStats.secondsElapsed() >= (Prayers.usingPrayer(this, Prayers.PRESERVE) ? 90 : 60)) {
                decreaseStats.start((Prayers.usingPrayer(this, Prayers.PRESERVE) ? 90 : 60));
            }
        }

        farmingOld.farmingProcess();
    }

    private void processRandom() {
        setPlayerQuestTabCycleCount(getPlayerQuestTabCycleCount() + 1);
        //Update the players online regardless of the cycle count, this is the most important number, otherwise players might see "0" if they log in too soon. Can always remove this later.
        GlobalStrings.PLAYERS_ONLINE.send(this, World.getWorld().getPlayers().size());

        final int gametime = this.<Integer>getAttribOr(GAME_TIME, 0) + 1;
        putAttrib(GAME_TIME, gametime);// Increment ticks we've played for

        //Always update this line, only if interface is open.
        if (interfaceManager.isInterfaceOpen(TournamentUtils.TOURNAMENT_INTERFACE)) {
            getPacketSender().sendString(TournamentUtils.TOURNAMENT_TIME_LEFT_FRAME, TournamentManager.tournamentInfo());
        }

        if (interfaceManager.isInterfaceOpen(DAILY_TASK_MANAGER_INTERFACE)) {
            var dailyTask = this.<DailyTasks>getAttribOr(DAILY_TASK_SELECTED, null);
            if (dailyTask != null)
                getPacketSender().sendString(TIME_FRAME_TEXT_ID, DailyTaskManager.timeLeft(this, dailyTask));
        }

        if (this.hasAttrib(AttributeKey.RUINOUS_PRAYERS)) {
            if (!this.getInventory().contains(RUINOUS_BOOK)) {
                if (!this.getBank().contains(RUINOUS_BOOK) || this.getBank().getAmountOf(RUINOUS_BOOK) == 0) {
                    System.out.println("prayer book not found while on ruinous, resetting");
                    for (int i = 0; i < DefaultPrayerData.values().length; i++) {
                        Prayers.deactivatePrayer(this, i);
                    }
                    if (this.hasAttrib(AttributeKey.RUINOUS_PRAYERS)) {
                        this.clearAttrib(AttributeKey.RUINOUS_PRAYERS);
                        this.getPacketSender().updateTab(0, 5);
                        this.getInterfaceManager().setSidebar(5, 5608);
                    }
                }
            }
        }

        var staminaTicks = this.<Integer>getAttribOr(STAMINA_POTION_TICKS, 0);
        if (staminaTicks > 0) {
            staminaTicks--;
            this.putAttrib(STAMINA_POTION_TICKS, staminaTicks);
            if (staminaTicks == 50) {
                message("<col=8f4808>Your stamina potion is about to expire.");
                sound(3120);
            } else if (staminaTicks == 0) {
                message("<col=8f4808>Your stamina potion has expired.");
                sound(2672);
                packetSender.sendStamina(false).sendEffectTimer(0, EffectTimer.STAMINA);
            }
        }
        if (this.tile().inArea(Tile.DITCH)) {
            this.getMovementQueue().resetFollowing();
        }
        for (Player everyOne : World.getWorld().getPlayers()) {
            if (everyOne != null) {
                if (PoolOfWealth.activePerk != null) {
                    if (PoolOfWealth.activePerk.equalsIgnoreCase("Dr")) {
                        everyOne.getPacketSender().sendEffectTimer((PoolOfWealth.perkDuration.secondsRemaining()), EffectTimer.DROP_RATE_PERK);//up00
                    } else if (PoolOfWealth.activePerk.equalsIgnoreCase("Sp")) {
                        everyOne.getPacketSender().sendEffectTimer((PoolOfWealth.perkDuration.secondsRemaining()), EffectTimer.DOUBLE_SLAYER_PERK);//up00
                    } else if (PoolOfWealth.activePerk.equalsIgnoreCase("Xp")) {
                        everyOne.getPacketSender().sendEffectTimer((PoolOfWealth.perkDuration.secondsRemaining()), EffectTimer.DOUBLE_EXP_PERK);//up00
                    }
                }
            }
        }
        if (PoolOfWealth.perkDuration.expiredAfterBeingRun()) {//up9
            if (PoolOfWealth.activePerk != null) {
                PoolOfWealth.activePerk = null;
            }
        }


        QuestTab.refreshInfoTab(this);

        var overTricks = this.<Integer>getAttribOr(OVERLOAD_POTION, 0);
        if (overTricks > 0) {
            overTricks--;
            this.putAttrib(OVERLOAD_POTION, overTricks);
            if (overTricks == 100) {
                message("<col=8f4808>Your Overload potion is about to expire.");
            } else if (overTricks == 0) {
                message("<col=8f4808>Your Overload potion has expired.");
            }
        }

        LocalDateTime now = LocalDateTime.now();
        long minutesTillWildyBoss = now.until(WildernessBossEvent.getINSTANCE().next, ChronoUnit.MINUTES);
        long minutesTillWildyKey = now.until(WildernessKeyPlugin.next, ChronoUnit.MINUTES);

        // Refresh the quest tab every minute (every 100 ticks)
        if (GameServer.properties().autoRefreshQuestTab && getPlayerQuestTabCycleCount() == GameServer.properties().refreshQuestTabCycles) {
            setPlayerQuestTabCycleCount(0);

            //We only have to update the uptime here, every other line is automatically updated.
            this.getPacketSender().sendString(UPTIME.childId, QuestTab.InfoTab.INFO_TAB.get(UPTIME.childId).fetchLineData(this));

            //Update the timer frames every minute.
            this.getPacketSender().sendString(WORLD_BOSS_SPAWN.childId, QuestTab.InfoTab.INFO_TAB.get(WORLD_BOSS_SPAWN.childId).fetchLineData(this));

            if (minutesTillWildyBoss == 5) {
                if (!WildernessBossEvent.ANNOUNCE_5_MIN_TIMER) {
                    WildernessBossEvent.ANNOUNCE_5_MIN_TIMER = true;
                    World.getWorld().sendWorldMessage("<col=6a1a18><img=1100>The world boss will spawn in 5 minutes, gear up!");
                }
            }

            if (minutesTillWildyKey == 5) {
                if (!WildernessKeyPlugin.ANNOUNCE_5_MIN_TIMER) {
                    WildernessKeyPlugin.ANNOUNCE_5_MIN_TIMER = true;
                    World.getWorld().sendWorldMessage("<col=800000><img=936>The wilderness key will spawn in 5 minutes, gear up!");
                }
            }

        }
    }

    public List<String> claimedCollectionLog = Lists.newArrayList();

    public boolean isCollectionClaimed(String string) {//ye seems like he getting name of collection log that already claimed ye should be fixed okay just wanted to make sure
        return claimedCollectionLog.stream().anyMatch(c -> c.equalsIgnoreCase(string));
    }

    private void replaceItems() {

    }
}
