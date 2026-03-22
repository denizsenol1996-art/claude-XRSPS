package com.twisted.game.world.entity.mob.player.commands.impl.kotlin

import com.twisted.GameServer
import com.twisted.db.makeQuery
import com.twisted.db.onDatabase
import com.twisted.db.query
import com.twisted.db.submit
import com.twisted.db.transactions.CollectPayments.collectPayments
import com.twisted.fs.ObjectDefinition
import com.twisted.game.GameEngine
import com.twisted.game.TimesCycle
import com.twisted.game.content.EffectTimer
import com.twisted.game.content.collection_logs.data.LogType
import com.twisted.game.content.mechanics.Poison
import com.twisted.game.content.mechanics.referrals.Referrals
import com.twisted.game.content.mechanics.referrals.Referrals.displayMyReferrals
import com.twisted.game.content.minigames.MinigameManager
import com.twisted.game.content.minigames.impl.fight_caves.FightCavesMinigame
import com.twisted.game.content.presets.PresetManager
import com.twisted.game.content.raids.party.Party
import com.twisted.game.content.tournaments.TournamentManager
import com.twisted.game.task.Task
import com.twisted.game.world.World
import com.twisted.game.world.entity.AttributeKey
import com.twisted.game.world.entity.Mob
import com.twisted.game.world.entity.combat.CombatType
import com.twisted.game.world.entity.combat.magic.MagicClickSpells
import com.twisted.game.world.entity.int
import com.twisted.game.world.entity.masks.Projectile
import com.twisted.game.world.entity.mob.movement.MovementQueue
import com.twisted.game.world.entity.mob.npc.Npc
import com.twisted.game.world.entity.mob.npc.NpcMovementCoordinator
import com.twisted.game.world.entity.mob.player.EquipSlot
import com.twisted.game.world.entity.mob.player.Player
import com.twisted.game.world.entity.mob.player.skills.Skills
import com.twisted.game.world.entity.mob.player.commands.Command
import com.twisted.game.world.entity.mob.player.commands.CommandManager
import com.twisted.game.world.entity.mob.player.save.PlayerSave
import com.twisted.game.world.entity.set
import com.twisted.game.world.items.Item
import com.twisted.game.world.items.ground.GroundItem
import com.twisted.game.world.items.ground.GroundItemHandler
import com.twisted.game.world.`object`.GameObject
import com.twisted.game.world.`object`.MapObjects
import com.twisted.game.world.position.Tile
import com.twisted.game.world.region.RegionManager
import com.twisted.test.unit.IKODTest
import com.twisted.test.unit.PlayerDeathDropResult
import com.twisted.util.*
import com.twisted.util.CustomItemIdentifiers.BARRELCHEST_PET
import com.twisted.util.ItemIdentifiers.CRYSTAL_KEY
import com.twisted.util.chainedwork.Chain
import com.twisted.util.timers.TimerKey
import com.google.common.base.Stopwatch
import com.google.common.collect.Lists
import io.netty.handler.timeout.IdleStateEvent
import io.netty.handler.timeout.IdleStateHandler
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.config.Configuration
import org.apache.logging.log4j.core.config.LoggerConfig
import java.io.File
import java.util.*
import java.util.function.Predicate


/**
 * by default all these commands are dev+ only, unless explicitilty given a rank
 * <br>parts[0] = the command entered, such as ::bob >> is bob
 * <br>parts[1] = the first argument such as ::bob hello >> is hello
 *
 * @author Shadowrs/Jak tardisfan121@gmail.com
 */
object KtCommands {

    @JvmStatic
    var BOT_ACC_INCREMENTOR = 1

    private val logger: Logger = LogManager.getLogger(KtCommands::class.java)

    fun init() {
        cmd("dc", PLAYER) {
            player.requestLogout()
        }
        cmd("refer", PLAYER) {
            if (Referrals.COMMAND_ENABLED) {
                Referrals.askReferrerName(player)
            } else {
                player.message("Referrals are currently disabled.")
            }
        }
        cmd("ref1") {
            println("prev ${player.putAttrib(AttributeKey.REFERRAL_MILESTONE_THREE_REFERRALS, false)}")
        }
        cmd("ref2") {
            println("prev ${player.putAttrib(AttributeKey.REFERRER_USERNAME, "")}")
        }
        cmd("ref3") {
            Referrals.COMMAND_ENABLED = !Referrals.COMMAND_ENABLED
            player.message("Refs enabled = ${Referrals.COMMAND_ENABLED}")
        }
        cmd("ref4") {
            Referrals.verifyReferralRequestStage1(player, this.parts[1], true)
        }
        cmd("ref5") {
            player.message("my db id is ${AttributeKey.DATABASE_PLAYER_ID.int(player)}")
        }
        cmd("delobj") {
            // deletes cache objects not just custom spawned or modified objects from World.getWorld().getObjects()
            val hash: Long = MapObjects.getHash(player.x, player.y, player.z)
            val gameObjects: List<GameObject> = MapObjects.mapObjects[hash] ?: emptyList()
            player.message(String.format("total %s objs at pos", gameObjects.size))
            gameObjects.iterator().forEachRemaining { // avoid ConcurrentModException
                it.remove()
            }
        }
        cmd("fi") { // finditem
            CommandManager.commands["getid"]!!.execute(player, command, parts)
        }
        cmd("dechain") {
            Chain.DEBUG_CHAIN = !Chain.DEBUG_CHAIN
        }
        cmd("poison") {
            player.poison(6)
        }
        cmd("curepoison") {
            Poison.cure(player)
        }
        cmd("fixtabs") {
            // on dev accs just reset the whole thing to instantly fix (we dont care about loss of tab order)
            player.bank.tabAmounts = IntArray(10)
            player.bank.tabAmounts[0] = player.bank.size()
        }
        cmd("logkey") {
            LogType.KEYS.log(player, CRYSTAL_KEY, Item(6889))
        }
        cmd("logkey2") {
            LogType.BOSSES.log(player, NpcIdentifiers.BARRELCHEST_6342, Item(BARRELCHEST_PET))
        }
        cmd("fakevote") {
            query<Unit> {
                // id should be auto generated
                prepareStatement(
                    connection,
                    "INSERT INTO votes (site_id, ip_address, username, vote_key, voted_on) VALUES (:site, :ip, :user, :key, :vo)"
                ).apply {
                    setString("site", "2")
                    setString("ip", "213.93.143.69")
                    setString("user", "zoo")
                    setString("key", "AIOAlHxTZZXwt0A")
                    setInt("vo", 1607698304)
                    execute()
                }
            }.onDatabase(GameServer.votesDb) {
                player.message("ok try")
            }
        }
        cmd("resetpayments") {
            //Reset new database
            query<Unit> {
                prepareStatement(
                    connection,
                    "UPDATE wpwo_rs_orders SET claimed='unclaimed', claim_ip='unclaimed', claim_date='unclaimed' WHERE lower(claimed)='claimed'"
                ).apply {
                    execute()
                }
            }.onDatabase(GameServer.getDatabaseService()) {
                player.message("Database has been reset")
            }
        }
        cmd("freezet") {
            player.packetSender.sendEffectTimer(100, EffectTimer.FREEZE)
        }
        cmd("vengt") {
            player.packetSender.sendEffectTimer(100, EffectTimer.VENGEANCE)
        }
        cmd("myreferrals") {
            player.displayMyReferrals()
        }
        cmd("unlock") {
            player.unlock()
        }
        cmd("lockcheck") {
            player.message("we're locked: " + player.locked())
        }
        cmd("cia1") {
            player.teleport(2663, 3297, 1)
            player.resetWalkSteps()
            player.runFn(1) {
                player.movementQueue.interpolate(2666, 3297, MovementQueue.StepType.FORCED_WALK)
            }
        }
        cmd("cia2") {
            TournamentManager.checkAndOpenLobby(true)
        }
        cmd("clienttele", ADMIN) {
            val x = parts[1].toInt()
            val y = parts[2].toInt()
            var z = player.tile().getLevel() // stay on current lvl
            if (parts.size == 4 && z < 4) { // only use z from client if we're not in an instance!
                z = parts[3].toInt()
            }
            player.teleport(Tile(x, y, z))
        }
        /*cmd("uncidban") {
            val username = Utils.formatText(command.substring(9))
            MiscKotlin.getCidForUsername(username).submit { cid->
                if (cid == "")
                    player.message("$username had no cid on db.")
                else {
                    makeQuery {
                        prepareStatement(connection, "UPDATE clientid_bans SET unban_at=NOW() WHERE clientid=:cid").apply {
                            setString("cid", cid)
                            execute()
                        }
                    }.submit {
                        player.message("$username was un cidbanned")
                    }
                }
            }
        }
        cmd("unmacban") {
            val username = Utils.formatText(command.substring(9))
            MiscKotlin.getMacForUsername(username).submit { mac->
                if (mac == "")
                    player.message("$username had no mac on db.")
                else {
                    makeQuery {
                        prepareStatement(connection, "UPDATE macid_bans SET unban_at=NOW() WHERE macid=:mac").apply {
                            setString("mac", mac)
                            execute()
                        }
                    }.submit {
                        player.message("$username was un macbanned")
                    }
                }
            }
        }
        cmd("unipban") {
            val username = Utils.formatText(command.substring(8))
            MiscKotlin.getIPForUsername(username).submit { ip->
                if (ip == "")
                    player.message("$username had no ip on db.")
                else {
                    makeQuery {
                        prepareStatement(connection, "UPDATE ip_bans SET unban_at=NOW() WHERE ip=:ip").apply {
                            setString("ip", ip)
                            execute()
                        }
                    }.submit {
                        player.message("$username was un ipbanned")
                    }
                }
            }
        }*/
        cmd("avp1") {
            player.putAttrib(AttributeKey.VOTE_POINS, player.getAttribOr<Int>(AttributeKey.VOTE_POINS, 0) + 100)
        }
        cmd("avp2") {
            player.putAttrib(AttributeKey.VOTE_POINS, 0)
        }
        cmd("livenpefind") {
            tempLoggingOfNPE = !tempLoggingOfNPE
            player.message("temp now: $tempLoggingOfNPE")
        }
        cmd("cia3") {
            player.runFn(1) {
                player.message("1")
            }.runFn(0) {
                player.message("2")
            }
        }
        cmd("testbot") {
            CommandManager.attempt(player, "addbots 1 false 0 1")
        }
        cmd("addbots") {
            /*val botcount = if (parts.size < 2) 100 else parts[1].toInt()
            val walk = if (parts.size < 3) false else parts[2].toBoolean()
            val walkdist = if (parts.size < 4) 3 else parts[3].toInt()
            val tome = if (parts.size < 5) 0 else parts[4].toInt()
            for (i in 1..botcount) {
                val msg = LoginDetailsMessage(
                    DummyChannelHandlerContext.DUMMY,
                    "Testbot${BOT_ACC_INCREMENTOR++}",
                    "test",
                    "127.0.0.1",
                    "",
                    GameServer.properties().gameVersion,
                    IsaacRandom.DUMMY(),
                    IsaacRandom.DUMMY()
                )
                val bot = null
                bot.putAttrib(AttributeKey.IS_BOT, true)
                bot.session.finalizeLogin(msg) // calls loginservice.submit
                bot.isInvulnerable = true
                bot.hp(5000, 5000)
                bot.putAttrib(AttributeKey.NEW_ACCOUNT, false) // save over saved attrib, starter is done
                for (i2 in 1..50)
                    bot.relations.addIgnore("testbot$i2")
                if (tome == 1) {
                    bot.runFn(1) {
                        bot.teleport(player.tile())
                    }
                }
                if (walk) {
                    Chain.bound(null).repeatingTask(2 + Utils.random(2)) {
                        var pos = 10
                        var targPos =
                            bot.tile().transform(-walkdist + Utils.random(walkdist), -walkdist + Utils.random(walkdist))
                        while (pos-- > 0 && World.getWorld().clipAt(targPos) == 0) {
                            targPos = bot.tile()
                                .transform(-walkdist + Utils.random(walkdist), -walkdist + Utils.random(walkdist))
                        }
                        bot.smartPathTo(targPos);
                        val chat = "we ${targPos.distance(bot.tile())} i:${bot.relations.ignoreList.size}"
                        bot.forceChat(chat)
                        bot.chatMessageQueue.add(ChatMessage(0, 0, chat.toByteArray()))
                        if (!bot.isRegistered)
                            it.stop()
                    }
                }
                PetAI.spawnPet(bot, Pet.PET_DAGANNOTH_PRIME, false)
            }
            player.message("$botcount submitted")*/
        }
        cmd("cia5") {
            //Add the reward in the correct slot
            val slot = if (parts.size == 1) -1 else parts[1].toInt()
            player.inventory().set(0, Item(4151), true)
            player.inventory().add(Item(9244, 50), slot, true)
        }
        cmd("cia6") {
            // testing
            val idle =
                player.session.channel?.pipeline()?.get("timeout") as IdleStateHandler // IdleStateHandler::class.java
            //idle.userEventTriggered(player.session.ctx, IdleStateEvent.READER_IDLE_STATE_EVENT)
            player.session.ctx.fireUserEventTriggered(IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT)
        }
        cmd("cia7") {
            player.session.ctx.channel().close()
            // so what happens when you try to close a closed channel?
            player.session.ctx.channel().close()
        }
        cmd("cia8") {
            val ctx: LoggerContext = LogManager.getContext(false) as LoggerContext
            val config: Configuration = ctx.getConfiguration()
            val loggerConfig: LoggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME)
            val level = if (parts.size == 1) Level.INFO else when (parts[1].toInt()) {
                1 -> Level.DEBUG
                2 -> Level.TRACE
                else -> Level.INFO
            }
            loggerConfig.level = level
            ctx.updateLoggers()
        }
        cmd("hardkick") {
            val player2 = Utils.formatText(command.substring(9)) // after "hardkick "

            val plr = World.getWorld().getPlayerByName(player2)
            if (plr.isPresent) {
                val p2 = plr.get()
                if (p2.isRegistered()) {
                    GameEngine.getInstance().addSyncTask {
                        p2.getPacketSender().sendLogout()
                        // Finalize player and stop all processing.
                        p2.onLogout() // triggers onLogout : removes following pets etc. Content code.
                        World.getWorld().players.remove(p2) // Finish the request by removing the player from the world.
                        p2.onRemove() // currently does nothing (used to call onlogout but not really the bet place for it).
                        // player.message("${p2.mobName} hard removed")
                        GameEngine.getInstance().submitLowPriority {
                            PlayerSave.save(player)
                        }
                    }

                } else {
                    player.message("${p2.mobName} not registered")
                }
            }
        }
        /*cmd("lsinfo") {
            var str = "Login service: IO loads:${World.getWorld().loginService.REQUESTS_IN_PROGRESS.size} ${
                World.getWorld().loginService.REQUESTS_IN_PROGRESS.map { it.message.username }.toTypedArray()
                    .contentToString()
            }"
            str += " Requests: ${World.getWorld().loginService.wip.size} ${
                World.getWorld().loginService.wip.map { it.message.username }.toTypedArray().contentToString()
            }"
            str += "\nPlayers: ${
                World.getWorld().players.filterNotNull()
                    .map { "$it -- reg:${it.isRegistered} active:${it.session.channel?.isActive}" }
            }"
            str += "\nLogout service: IO saves: ${World.getWorld().logoutService.SAVES_IN_PROGRESS.size} ${
                World.getWorld().logoutService.SAVES_IN_PROGRESS.map { it.username }.toTypedArray().contentToString()
            }"
            str += " Requests: ${World.getWorld().logoutService.wip.size} ${
                World.getWorld().logoutService.wip.map { it.username }.toTypedArray().contentToString()
            }"
            GameEngine.getInstance().submitLowPriority {
                File("./data/loginserver-info.txt").createNewFile()
                File("./data/loginserver-info.txt").writeText(str)
            }
        }*/
        cmd("cia9") {
            PlayerDeathDropResult.DEBUG = !PlayerDeathDropResult.DEBUG // toggle
        }
        cmd("cia10") {
            // lets fuck it up on purpose to see what happens yeah? nice
            player.presets[0].equipment[EquipSlot.WEAPON].amount = 2
            player.presetManager.load(player.presets[0])
        }
        cmd("cia11") {
            // lets fuck it up on purpose to see what happens yeah? nice
            player.presets[0].equipment[EquipSlot.WEAPON].amount = 2
            player.presets[0].inventory[0].amount = 11
        }
        cmd("cia12") {
            World.getWorld().players.filterNotNull()
                .filter { it != player && it.getAttribOr(AttributeKey.IS_BOT, false) }.forEach {
                it.presetManager.load(PresetManager.GLOBAL_PRESETS[0])
                it.teleport(player.tile())
            }
        }
        cmd("cia13") {
            // nuffler testing: have max stack of the noted item, and then some of the unnoted.
            // stacking > Integer.max should be tested
            player.inventory().add(4709, Integer.MAX_VALUE)
        }
        cmd("cia14") {
            // make a 10mb file lol
            for (i in 0..100_000) {
                logger.trace("testing kek 123 ${Utils.random(1000)} testing kek 12 testing kek 12 testing kek 12testing kek 12testing kek 12testing kek 12testing kek 12")
            }
        }
        cmd("1hp") {
            player.hit(player,player.hp() - 1,0)
        }
        cmd("codeset") {
            CommandManager.attempt(player, "heal")
            CommandManager.attempt(player, "infpray true")
            player.setInvulnerable(true)
            player.inventory().clear()
            player.inventory().addAll(
                Item(4151), Item(12954), Item(19553), Item(21295), Item(6737),
                Item(7462), Item(22616), Item(10828), Item(22619), Item(22613), Item(22625), Item(13239)
            )
            player.inventory().forEachIndexed { index, item ->
                player.equipment.equip(index)
            }
            CommandManager.attempt(player, "max")
            CommandManager.attempt(player, "spec 123123123")
        }
        cmd("togglertcheck") {
            NpcPerformance.DETAL_LOG_ENABLED = !NpcPerformance.DETAL_LOG_ENABLED
            player.message("performance = ${NpcPerformance.DETAL_LOG_ENABLED}")
        }
        cmd("cia18") {
            val amt = if (parts.size < 3) 1 else parts[2].toInt()
            for (i in 1..amt) {
                World.getWorld().registerNpc(Npc(parts[1].toInt(), player.tile()).respawns(false))
            }
        }
      /*  cmd("addbotswildypvm") {
            // pvm in wildy 1v1 fighting
            val startpos = Tile(3353, 3645)
            var ypos = 0
            while (ypos < 150) {
                ypos += 3
                val npcpos = startpos.transform(0, ypos)
                val npc = Npc(2077, npcpos).respawns(false)
                World.getWorld().registerNpc(npc)
                val msg = LoginDetailsMessage(
                    DummyChannelHandlerContext.DUMMY,
                    "Testbot${BOT_ACC_INCREMENTOR++}",
                    "test",
                    "127.0.0.1",
                    "",
                    GameServer.properties().gameVersion,
                    IsaacRandom.DUMMY(),
                    IsaacRandom.DUMMY()
                )
                val bot = Session(null).player
                bot.putAttrib(AttributeKey.IS_BOT, true)
                bot.session.finalizeLogin(msg)
                bot.runFn(3) {
                    bot.setPlayerRights(PlayerRights.DEVELOPER)
                    CommandManager.attempt(bot, "heal")
                    bot.isInvulnerable = true
                    bot.teleport(npcpos.transform(0, -2))
                    bot.combat.attack(npc)
                    npc.combat.attack(bot)
                }
            }
        }*/
        cmd("cia20") {
            World.getWorld().players.filterNotNull().filter { it.dead() }.forEach {
                // fix glitched ones
                it.hp(1, 0)
            }
        }
        cmd("cia21") {
            World.getWorld().npcs.filterNotNull().forEach {
                if (it.id() == 2077 && !it.respawns())
                    it.teleport(player.tile())
            }
        }
        /*cmd("addbotsditch") {
            // wildy ditch test pvm distanced attacking
            val startpos = Tile(3044, 3520)
            var pos = 0
            while (pos < 40) {
                pos += 1
                val npcpos = startpos.transform(pos, 0)
                val npc = Npc(2077, npcpos.transform(0, 3)).respawns(false)
                World.getWorld().registerNpc(npc)
                val msg = LoginDetailsMessage(
                    DummyChannelHandlerContext.DUMMY,
                    "Testbot${BOT_ACC_INCREMENTOR++}",
                    "test",
                    "127.0.0.1",
                    "",
                    GameServer.properties().gameVersion,
                    IsaacRandom.DUMMY(),
                    IsaacRandom.DUMMY()
                )
                val bot = Session(null).player
                bot.putAttrib(AttributeKey.IS_BOT, true)
                bot.session.finalizeLogin(msg)
                bot.runFn(3) {
                    bot.setPlayerRights(PlayerRights.DEVELOPER)
                    CommandManager.attempt(bot, "heal")
                    bot.isInvulnerable = true
                    bot.teleport(npcpos)
                    bot.equipment.set(3, Item(864, 1000), true)
                    bot.combat.attack(npc)
                    npc.combat.attack(bot)
                }
            }
        }*/
        cmd("runningtasks") {
            MiscKotlin.runningTasks()
        }

        cmd("cia24") {
            Projectile(
                player.getCentrePosition().transform(2, 2), player.tile(), 1, 1491,
                40 + (20 * 2), 20, 30, 30, 0, 50, 25
            ).sendProjectile()
        }
        cmd("toggleikod") {
            IKODTest.IKOD_DEBUG = !IKODTest.IKOD_DEBUG
        }
        cmd("macbanlist") {
            println("macbans: ${PlayerPunishment.macBannedUsers.map { it }.toTypedArray().contentToString()}")
        }
        cmd("maxcons1") {
            //LoginHandler.maximumShortTermOpenChannels = parts[1].toInt()
            //println("maximumConcurrentRequests: ${LoginHandler.maximumShortTermOpenChannels}")
        }
        cmd("maxcons2") {
            //LoginHandler.timeLimitForMaxConnections = parts[1].toLong()
            //println("maximumConcurrentRequests: ${LoginHandler.timeLimitForMaxConnections}")
        }
      /*  cmd("addbotszulrah") {
            // put bots into zulrah
            val amt = if (parts.size == 1) 2 else parts[1].toInt()
            val dh = Tournament(
                Arrays.stream(TournamentManager.settings.tornConfigs)
                    .filter { c: TornConfig -> c.key.equals("hybrid", ignoreCase = true) }.findFirst().get()
            )
            for (i in 1..amt) {
                val msg = LoginDetailsMessage(
                    DummyChannelHandlerContext.DUMMY,
                    "Testbot${BOT_ACC_INCREMENTOR++}",
                    "test",
                    "127.0.0.1",
                    "",
                    GameServer.properties().gameVersion,
                    IsaacRandom.DUMMY(),
                    IsaacRandom.DUMMY()
                )
                val bot = Session(null).player
                bot.putAttrib(AttributeKey.IS_BOT, true)
                bot.session.finalizeLogin(msg)
                bot.runFn(3) {
                    bot.setPlayerRights(PlayerRights.DEVELOPER)
                    CommandManager.attempt(bot, "heal")
                    bot.isInvulnerable = true
                    dh.setLoadoutOnPlayer(bot)
                    bot.zulrahInstance.enterInstance(bot, true)
                }
            }
        }*/
       /* cmd("addbotsvorkath") {
            // put bots into vorky
            val amt = if (parts.size == 1) 2 else parts[1].toInt()
            val dh = Tournament(
                Arrays.stream(TournamentManager.settings.tornConfigs)
                    .filter { c: TornConfig -> c.key.equals("hybrid", ignoreCase = true) }.findFirst().get()
            )
            for (i in 1..amt) {
                val msg = LoginDetailsMessage(
                    DummyChannelHandlerContext.DUMMY,
                    "Testbot${BOT_ACC_INCREMENTOR++}",
                    "test",
                    "127.0.0.1",
                    "",
                    GameServer.properties().gameVersion,
                    IsaacRandom.DUMMY(),
                    IsaacRandom.DUMMY()
                )
                val bot = Session(null).player
                bot.putAttrib(AttributeKey.IS_BOT, true)
                bot.session.finalizeLogin(msg)
                bot.runFn(3) {
                    bot.setPlayerRights(PlayerRights.DEVELOPER)
                    CommandManager.attempt(bot, "heal")
                    bot.isInvulnerable = true
                    dh.setLoadoutOnPlayer(bot)
                    bot.runFn(1) {
                        val area = InstancedAreaManager.getSingleton()
                            .createSingleInstancedArea(bot, Area(2257, 4053, 2286, 4077)) as SingleInstancedArea?
                        if (area == null) return@runFn
                        bot.teleport(VorkathInstance.ENTRANCE_POINT.transform(0, 0, area.getzLevel()))
                        val vorkath = Npc(
                            NpcIdentifiers.VORKATH_8061,
                            VorkathInstance.ENTRANCE_POINT.transform(-3, 9, area.getzLevel())
                        )
                        World.getWorld().registerNpc(vorkath)
                        vorkath.putAttrib(AttributeKey.OWNING_PLAYER, Tuple(bot.index, bot))
                        vorkath.combatMethod = Vorkath()
                        vorkath.movementQueue.setBlockMovement(true)
                        vorkath.respawns(true)
                        bot.runFn(2) s@{
                            bot.combat.attack(vorkath)
                            vorkath.combat.attack(bot)
                        }
                    }
                }
            }
        }
        cmd("addbotscorp") {
            // put bots into zulrah
            val amt = if (parts.size == 1) 2 else parts[1].toInt()
            val dh = Tournament(
                Arrays.stream(TournamentManager.settings.tornConfigs)
                    .filter { c: TornConfig -> c.key.equals("hybrid", ignoreCase = true) }.findFirst().get()
            )
            for (i in 1..amt) {
                val msg = LoginDetailsMessage(
                    DummyChannelHandlerContext.DUMMY,
                    "Testbot${BOT_ACC_INCREMENTOR++}",
                    "test",
                    "127.0.0.1",
                    "",
                    GameServer.properties().gameVersion,
                    IsaacRandom.DUMMY(),
                    IsaacRandom.DUMMY()
                )
                val bot = Session(null).player
                bot.putAttrib(AttributeKey.IS_BOT, true)
                bot.session.finalizeLogin(msg)
                val corp = World.getWorld().npcs.filterNotNull().find { it.id() == 319 }
                bot.runFn(3) {
                    bot.setPlayerRights(PlayerRights.DEVELOPER)
                    CommandManager.attempt(bot, "heal")
                    bot.isInvulnerable = true
                    dh.setLoadoutOnPlayer(bot)
                    bot.teleport(Tile(2980, 4384, 2))
                    bot.runFn(2) {
                        bot.combat.attack(corp!!)
                    }
                }
            }
        }
        cmd("botattnpc") {
            val amt = if (parts.size == 1) 2 else parts[1].toInt()
            val corp = World.getWorld().npcs.filterNotNull().find { it.id() == amt }
            player.closePlayers(64).forEach {
                it.combat.attack(corp)
            }
        }*/
        cmd("cia26") {
            val opponent = player
            val item = Item(13215, Integer.MAX_VALUE)
            //If inv full send to bank!
            val requestedAmt: Int = item.getAmount()
            opponent.inventory().addOrExecute({ t: Item ->
                val added: Int = opponent.getBank().depositFromNothing(t)
                opponent.message("%s x %s was banked as you didn't have enough space.", added, t.name())
                if (added != requestedAmt) {
                    opponent.message(
                        "The remaining %s x %s was dropped as you didn't have enough space.",
                        requestedAmt - added,
                        t.name()
                    )
                    GroundItemHandler.createGroundItem(
                        GroundItem(
                            Item(t.id, requestedAmt - added),
                            opponent.tile(),
                            opponent
                        )
                    )
                }
            }, Optional.empty<String>(), Arrays.asList(item))
        }
        cmd("hitop") {
            val amt = if (parts.size <= 1) 1 else parts[1].toInt()
            val target: Mob = if (parts.size <= 2) player.combat.target else World.getWorld()
                .getPlayerByName(parts[2].replace("_", " ")).get()
            target.hit(player, amt, 1, CombatType.MELEE).submit()
        }
        cmd("cia27") {
            player.hit(player,player.hp() - 1,0)
            player.poison(6)
        }
        cmd("dumpobjdef") {
            println("kk")
            var str = StringBuilder()
            for (i in 0..World.getWorld().definitions().total(ObjectDefinition::class.java)) {
                val def: ObjectDefinition? = World.getWorld().definitions().get(ObjectDefinition::class.java, i)
                if (def == null)
                    continue
                str.append("$i = ${def.toStringBig()}\n")
            }
            GameEngine.getInstance().submitLowPriority {
                File("./data/osrs objs dump.txt").createNewFile()
                File("./data/osrs objs dump.txt").writeText(str.toString())
                println("done")
            }
        }
        cmd("fn") {
            val amt = if (parts.size <= 1) "dragon" else parts[1].replace("_", " ")
            for (i in 0..World.getWorld().definitions().total(com.twisted.fs.NpcDefinition::class.java)) {
                val def: com.twisted.fs.NpcDefinition? =
                    World.getWorld().definitions().get(com.twisted.fs.NpcDefinition::class.java, i)
                if (def == null)
                    continue
                if ((def.name ?: "").contains(amt, true))
                    player.debug("found %s : %s", i, def.name)
            }
        }
        cmd("hp") {
            val amt = if (parts.size <= 1) 1 else parts[1].toInt()
            player.hp(amt, amt)
        }
        cmd("pray") {
            val amt = if (parts.size <= 1) 1 else parts[1].toInt()
            player.skills().setLevel(Skills.PRAYER, 1);
        }
        cmd("cia28") {
            RegionManager.loadMapFiles(player.x, player.y, true)
        }
        cmd("toggleautolog") {
            GameEngine.autoEnableTimerDebug = !GameEngine.autoEnableTimerDebug
            player.message("now = ${GameEngine.autoEnableTimerDebug}")
        }
        cmd("perftest1") {
            player.teleport(3201, 3852)
            player.runFn(2) {
                CommandManager.attempt(player, "npc 3269 500 50")
                player.localNpcs.filterNotNull().filter { it.id() == 3269 }.forEach {
                    it.respawns(false)
                    it.putAttrib(AttributeKey.MAX_DISTANCE_FROM_SPAWN, 50)
                    it.combatInfo().combatFollowDistance = 50
                    it.walkRadius(50)
                    it.combat.attack(player)
                }
            }
        }
        cmd("perftest2") {
            player.teleport(3201, 3852)
            player.runFn(2) {
                CommandManager.attempt(player, "npc 6593 500 50")
                player.runFn(4) {
                    World.getWorld().npcs.filterNotNull()
                        .filter { it.id() == 6593 && it.tile().distance(player.tile()) < 15 }.forEach {
                        it.respawns(false)
                        it.putAttrib(AttributeKey.MAX_DISTANCE_FROM_SPAWN, 50)
                        it.combatInfo().combatFollowDistance = 50
                        it.walkRadius(50)
                        it.combat.attack(player)
                    }
                }
            }
        }
        cmd("masstest") {
            if (GameServer.properties().production)
                return@cmd
            CommandManager.attempt(player, "addbots 50 true 10") // testbot1-50 walking
            CommandManager.attempt(player, "addbotswildypvm") // next 50 easts pvp
            CommandManager.attempt(player, "addbotsditch") // testbot 120-140 at home ditch
            CommandManager.attempt(player, "addbotszulrah 100") // testbots 141-241 zulrah
            CommandManager.attempt(
                player,
                "addbotsvorkath 100"
            ) // testbots 241-341 zulrah, could be 1 player doing 100 runs
        }
        cmd("togglenpcwalk") {
            NpcMovementCoordinator.RANDOM_WALK_ENABLED = !NpcMovementCoordinator.RANDOM_WALK_ENABLED
            player.message("walk now ${NpcMovementCoordinator.RANDOM_WALK_ENABLED}")
        }
        cmd("szpf3") {
            player.localNpcs.filterNotNull().forEach {
                it.combat.attack(player)
                it.combatInfo().combatFollowDistance = 50
                it.walkRadius(50)
                it.putAttrib(AttributeKey.MAX_DISTANCE_FROM_SPAWN, 100)
            }
        }
        cmd("jadstress1") {
            World.getWorld().players.filterNotNull().filter { it.mobName.startsWith("Testbot") }.forEach {
                MinigameManager.playMinigame(it, FightCavesMinigame(63))
            }
        }
        cmd("jadstress2") {
            World.getWorld().npcs.filterNotNull().filter { it.id() == NpcIdentifiers.TZTOKJAD }.forEach {
                it.hit(player,150,0) // trigger healers
            }
        }
        cmd("pfstress1") {
            val sw = Stopwatch.createStarted()
            val amt = if (parts.size <= 1) 1 else parts[1].toInt()
            for (i in 0..amt)
                player.smartPathTo(player.tile().transform(50, 50))
            sw.stop()
            player.message("took ${sw.elapsed().toMillis()} ms for $amt pf")
        }
        cmd("pfstress2") {
            val sw = Stopwatch.createStarted()
            val amt = if (parts.size <= 1) 1 else parts[1].toInt()
            for (i in 0..amt)
                player.routeFinder.routeObject(GameObject(1, player.tile().transform(60, 60)), { } );
            sw.stop()
            player.message("took ${sw.elapsed().toMillis()} ms for $amt pf")
        }
        cmd("lagdumptest") {
            MiscKotlin.dumpStateOnBaddie()
        }
        cmd("toggleperfmode") {
            NpcPerformance.PERF_CHECK_MODE_ENABLED = !NpcPerformance.PERF_CHECK_MODE_ENABLED
        }
        cmd("clearhits") {
            World.getWorld().players.filterNotNull().forEach {
                it.combat.hitQueue.clear()
            }
        }
        cmd("teleton") {
            val id = if (parts.size <= 1) 1 else parts[1].toInt()
            player.teleport(World.getWorld().npcs.get(id).tile())
        }
        cmd("tiletest") {
            Task.repeatingTask {
                for (z in -5..5) {
                    for (y in -5..5) {
                        val pos = player.tile().transform(z, y)
                        World.getWorld().tileGraphic(624, pos, 10, 0)
                        Projectile(
                            pos,
                            pos.transform(5, 5),
                            1,
                            1491,
                            40 + 20 * 5,
                            20,
                            30,
                            30,
                            0,
                            50,
                            25
                        ).sendProjectile()
                    }
                }
                if (it.tick > 10)
                    it.stop()
            }
        }
        cmd("addnpc") {
            val id = if (parts.size <= 1) 1 else parts[1].toInt()
            val hp = if (parts.size <= 2) 1 else parts[2].toInt()
            val dist = if (parts.size <= 3) 1 else parts[3].toInt()
            val rad = if (parts.size <= 4) 1 else parts[4].toInt()
            for (z in -dist..dist) {
                for (y in -dist..dist) {
                    val pos = player.tile().transform(z, y)
                    val npc = Npc(id, pos).respawns(false)
                    npc.walkRadius(rad)
                    npc.setHitpoints(hp)
                    npc.combatInfo().aggroradius = rad
                    npc.combatInfo().combatFollowDistance = rad
                    AttributeKey.MAX_DISTANCE_FROM_SPAWN.set(npc, rad)
                    World.getWorld().registerNpc(npc)
                }
            }
        }
        cmd("yeet1") {
            player.inventory().addAll(
                Item(ItemIdentifiers.HYDRAS_HEART), Item(ItemIdentifiers.HYDRAS_FANG), Item(
                    ItemIdentifiers.HYDRAS_EYE
                )
            )
        }
        cmd("hydra") {
            player.teleport(1354, 10258)
        }
        cmd("cia36") {
            player.inventory().add(11941, 1)
            player.inventory().add(11802, 1)
            player.lootingBag.deposit(Item(11802, 1), 1, null)
        }
        cmd("cia37") {

            val form1 = Npc(6500, player.tile().transform(1, 1));
            form1.respawns(false)
            form1.lock()
            World.getWorld().registerNpc(form1)
            Chain.bound(null).runFn(1) {
                form1.animate(6242) // die anim
                // nvm its players only.. shit idk
            }.then(4) {
                form1.transmog(6501)
                form1.animate(6270)
                form1.graphic(1055)
            }.then(4) {
                println("")
            }.then(1) {
                println("")
            }.then(7) {
                World.getWorld().unregisterNpc(form1)
                form1.animate(6241)
            }
        }
        cmd("cia38") {
            val npc1 = Npc(100, player.tile().transform(1, 1));
            val npc2 = Npc(100, player.tile().transform(1, 1));
            val npc3 = Npc(100, player.tile().transform(1, 1));
            val npc4 = Npc(100, player.tile().transform(1, 1));
            val npc5 = Npc(100, player.tile().transform(1, 1));
            World.getWorld().registerNpc(npc1)
            World.getWorld().registerNpc(npc2)
            World.getWorld().registerNpc(npc3)
            World.getWorld().registerNpc(npc4)
            World.getWorld().registerNpc(npc5)
        }
        cmd("cia39") {
            player.closeNpcs(1)
            println(player.closeNpcs(1).size)
            println(Arrays.toString(player.closeNpcs(1)))
        }
        cmd("cia40") {
            player.collectPayments()
        }
        cmd("cia41") {
            makeQuery {
                val sql =
                    "INSERT INTO wpwo_rs_orders (username, item_id, item_amount, claimed, claim_date, claim_ip) VALUES (:name, :item, :amt, :claim, :date, :ip)"
                prepareStatement(connection, sql).apply {
                    setInt("item", 995)
                    setInt("amt", 10)
                    setString("name", player.username)
                    setString("claim", "unclaimed")
                    setString("date", "unclaimed")
                    setString("ip", "unclaimed")
                    execute()
                }
            }.submit {
                player.message("done")
            }
        }
        cmd("cia42") {
            player.inventory().add(2, 120)
            player.inventory().add(6, 1)
            player.inventory().add(8, 1)
            player.inventory().add(10, 1)
            player.inventory().add(12, 1)
        }
        cmd("party") {
            Party.createParty(player)
            //val matthew = World.getWorld().getPlayerByName("Matthew");
            //player.raidsParty.addMember(matthew.get())
        }
        cmd("startraids") {
            Party.startRaid(player)
        }
        cmd("cia43") {
            player.bank.depositFromNothing(Item(995, 2_100_000_000))
        }
        cmd("cia45") {

        }
        cmd("cia46") {
            player.packetSender.sendItemOnInterfaceSlot(12022, Item(4151, 1), 0)
            player.packetSender.sendItemOnInterfaceSlot(12023, Item(4153, 1), 0)
        }
        cmd("cia47") {
            player.packetSender.sendItemOnInterfaceSlot(12022, null, 0)
            player.packetSender.sendItemOnInterfaceSlot(12023, null, 0)
        }
        cmd("lagme") {
            Thread.sleep(1_000)
        }
        cmd("regionmapsize") {
            player.message("${RegionManager.regions.size}")
        }
        cmd("bmnpcwalk") {
            var idxs = Lists.newLinkedList<Int>()
            var size = World.getWorld().npcs.size()
            for (i in 0 .. 100) {
                idxs.add(Utils.random(size))
            }
            var s = System.currentTimeMillis()
            for (i in idxs) {
                val npc = World.getWorld().npcs.get(i)
                npc.setTile(npc.spawnTile().transform(-5, -5))
                npc.smartPathTo(Tile(npc.spawnTile().x, npc.spawnTile().y));
            }
            player.message("took ${System.currentTimeMillis() - s} ms for 100 walks")
        }
        cmd("ppwarntime") {
            Player.warnTimeMs = parts[1].toInt()
            player.message("Player.warnTimeMs: ${Player.warnTimeMs}")
        }
        cmd("pktcap") {
            //Session.threshold = parts[1].toLong()
            //player.message("PlayerSession.threshold: ${Session.threshold}")
        }
        cmd("showworldinfo") {
            TimesCycle.APPEND_WORLDINFO = !TimesCycle.APPEND_WORLDINFO
            player.message("TimesCycle.APPEND_WORLDINFO: ${TimesCycle.APPEND_WORLDINFO}")
        }
        cmd("bmxtoggle") {
            TimesCycle.BENCHMARKING_ENABLED = !TimesCycle.BENCHMARKING_ENABLED
            player.message("TimesCycle.BENCHMARKING_ENABLED: ${TimesCycle.BENCHMARKING_ENABLED}")
        }
        cmd("cia48") {
            player.repeatingTask {
                println("aye ${it.tick} ${it.keyOrOrigin()}")
            }
        }
        cmd("cia49") {
            val runUninterruptable = player.runUninterruptable(1) {
                
            }
            println(runUninterruptable.source())
        }
        cmd("cia50") {
            player.freeze(5, player)
        }
        cmd("cia51") {
            player.stun(5)
        }
        cmd("syncmode1") {
            World.SYNCMODE1 = !World.SYNCMODE1
            player.message("syncmode: ${World.SYNCMODE1}")
        }
        cmd("cia52") {
            for (i in 0 .. 10)
                player.collectPayments()
        }
        cmd("cia53") {
            player.repeatingTask {
                for (i in 0 .. 10)
                    player.collectPayments()
                if (it.tick > 20)
                    it.stop()
            }
        }
        cmd("infspec") {
            CommandManager.attempt(player, "spec 123123123")
        }
        cmd("vengme") {
            player.timerRepository.cancel(TimerKey.VENGEANCE_COOLDOWN)
            MagicClickSpells.MagicSpells.VENGEANCE.spell.startCast(player, null)
        }
        cmd("cia54") {
            for (i in 1 .. 5)
            player.hit(player, i)
        }
    }

    var tempLoggingOfNPE = true

    /**
     * simple predicates used by [Command.execute] canUse(player)
     */
    val PLAYER = Predicate<Player> { true }
    val DEV = Predicate<Player> { it.playerRights.isDeveloperOrGreater(it) }
    val ADMIN = Predicate<Player> { it.playerRights.isAdminOrGreater(it) }
    val MOD = Predicate<Player> { it.playerRights.isModerator() || it.playerRights.isAdminOrGreater(it) }
    val SUPPORT = Predicate<Player> {
        it.playerRights.isServerSupport() || it.playerRights.isModerator() || it.playerRights.isAdminOrGreater(it)
    }

    /**
     * registers a command. does some complicated kotlin syntax to make the Higher Order Function able to
     * consume the 3 args from [Command.execute]
     */
    fun cmd(command: String, canUse: Predicate<Player> = DEV, job: CommandWrapper.() -> Unit) {
        CommandManager.commands[command] = object : Command {
            override fun execute(player: Player, command: String, parts: Array<out String>) {
                job(CommandWrapper(player, command, parts))
            }

            override fun canUse(player: Player): Boolean = canUse.test(player)

        }
    }

    class CommandWrapper(
        val player: Player,
        val command: String,
        val parts: Array<out String>,
    )
}
