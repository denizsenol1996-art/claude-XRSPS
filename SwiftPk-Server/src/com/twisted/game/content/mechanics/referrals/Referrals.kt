package com.twisted.game.content.mechanics.referrals

import com.twisted.GameServer
import com.twisted.db.Dbt
import com.twisted.db.query
import com.twisted.db.submit
import com.twisted.db.transactions.FindPlayersFromIpDatabaseTransaction
import com.twisted.game.GameEngine
import com.twisted.game.content.syntax.EnterSyntax
import com.twisted.game.world.World
import com.twisted.game.world.entity.*
import com.twisted.game.world.entity.AttributeKey.*
import com.twisted.game.world.entity.mob.player.Player
import com.twisted.game.world.entity.mob.player.QuestTab.InfoTab
import com.twisted.game.world.items.Item
import com.twisted.util.Color
import com.twisted.util.CustomItemIdentifiers.*
import com.twisted.util.Utils
import java.sql.Timestamp
import java.util.*

/**
 * @author Shadowrs/Jak tardisfan121@gmail.com
 * November 8 2020
 */
object Referrals {

    // note: there is no event for ID 0
    const val REFEREE_ENTERED_REFERRER_NAME_EVENT = 1
    const val REFERER_REWARDED_FROM_REFEREE = 5
    const val REFEREE_ONE_DAY_PLAYTIME = 2
    const val REFEREE_TEN_HOURS_PLAYTIME = 3
    const val REFEREE_HAS_THREE_REFERRALS_THEMSELVES = 4
    var COMMAND_ENABLED = true

    /**
     * called when a player logs in.
     */
    fun Player.onLoginReferals() {
        val gameTime = getAttribOr<Int>(GAME_TIME, 0)
        val time = (gameTime * 0.6).toInt()
        val days = time / 86400
        val hours = time / 3600 - days * 24

        val referrerName = getAttribOr<String?>(REFERRER_USERNAME, "") ?: return
        if (referrerName.isEmpty())
            return
        if (hours >= 10 && !getAttribOr<Boolean>(REFERRAL_MILESTONE_10HOURS, false)) {
            rewardReferrerByName(referrerName, REFEREE_TEN_HOURS_PLAYTIME, this)
            putAttrib(REFERRAL_MILESTONE_10HOURS, true)
        }
        if (days >= 1 && !getAttribOr<Boolean>(REFERRAL_MILESTONE_1_DAY, false)) {
            rewardReferrerByName(referrerName, REFEREE_ONE_DAY_PLAYTIME, this)
            putAttrib(REFERRAL_MILESTONE_1_DAY, true)
        }
        if (REFERRALS_COUNT.int(this) >= 3 && !REFERRAL_MILESTONE_THREE_REFERRALS.yes(this)) {
            REFERRAL_MILESTONE_THREE_REFERRALS.set(this, true)
            rewardReferrerByName(referrerName, REFEREE_HAS_THREE_REFERRALS_THEMSELVES, this)

            // trigger claiming manually since there won't be a db entry on the UI to click claim on
            processAllReferralsForMe(eventIdOnly = REFEREE_HAS_THREE_REFERRALS_THEMSELVES)
        }

        // silent handling -- now claimed through ingame UI only, not automatically on login.
        //processAllReferralsForMe()
    }

    data class ReferralRewardResult(var row: ReferralRow, var success: Boolean = false) {
        override fun toString(): String {
            return "ReferralRewardResult(row=$row, success=$success)"
        }
    }

    data class ReferralsFuture(var done: Boolean = false, val results: List<ReferralRewardResult>) {
        override fun toString(): String {
            return "ReferralsFuture(done=$done, results=$results)"
        }
    }

    private fun Player.processAllReferralsForMe(eventIdOnly: Int = -1): ReferralsFuture {
        val list = mutableListOf<ReferralRewardResult>()
        val future = ReferralsFuture(results = list)
        // should absolutely put this on a npc chat and lock the player + stop logout to stop items being lost when
        // database is doing async IO operations. stops player logging out before operations are complete.
        getUnclaimedForReferrerId(getAttribOr<Int>(DATABASE_PLAYER_ID, -1)).submit { rows ->
            rows.forEachIndexed { index, row ->
                if (eventIdOnly != -1 && eventIdOnly == row.eventId)
                    return@forEachIndexed // skip TODO is this continue or need to wrap elseif
                val result = processUnclaimedEntry(row, false)
                list.add(result)
                if (index == rows.size - 1) {
                    future.done = true // mark completed
                }
            }
            if (rows.isEmpty())
                future.done = true
        }
        return future
    }

    /**
     * for a player who has logged in, process ONE ReferralRow and give rewards and set claimed=1 for that ONE row.
     */
    fun Player.processUnclaimedEntry(
        row: ReferralRow,
        insertAsAlreadyDoneInsteadOfUpdate: Boolean = false
    ): ReferralRewardResult {
        val result = ReferralRewardResult(row)
        when (row.eventId) {
            REFEREE_ENTERED_REFERRER_NAME_EVENT -> {
                // this is always handled instantly when you do ::refer, so no need
                // to handle any queued up requests, they wont exist.
            }
            REFEREE_ONE_DAY_PLAYTIME -> {
                Utils.sendDiscordInfoLog(this,
                    this.username + " has received a 5$ bond for referring a player who passed 1 day playtime.",
                    "referrals"
                )
                inventory.addOrBank(Item(FIVE_DOLLAR_BOND))
                message("You've been awarded a 5$ bond for referring a player who passed 1 day playtime.")
                result.success = true
                if (!insertAsAlreadyDoneInsteadOfUpdate)
                    row.updateClaimed()
                else {
                    row.claimed = 1
                    insertReferralEvent(row)
                }
            }
            REFEREE_TEN_HOURS_PLAYTIME -> {
                inventory.addOrBank(Item(WEAPON_MYSTERY_BOX), Item(ARMOUR_MYSTERY_BOX))
                message("You've been awarded a weapon and armour box for referring a player who passed 10 hours playtime.")
                Utils.sendDiscordInfoLog(this,
                    this.username + " has received a weapon and armour box for referring a player who passed 10 hours playtime.",
                    "referrals"
                )
                result.success = true
                if (!insertAsAlreadyDoneInsteadOfUpdate)
                    row.updateClaimed()
                else {
                    row.claimed = 1
                    insertReferralEvent(row)
                }
            }
            REFEREE_HAS_THREE_REFERRALS_THEMSELVES -> {
                inventory.addOrBank(Item(DONATOR_MYSTERY_BOX))
                Utils.sendDiscordInfoLog(this,
                    this.username + " has received a donator mystery box for referring 3 players!",
                    "referrals"
                )
                message("You've been rewarded a donator mystery box for referring 3 players!")
                result.success = true
                if (!insertAsAlreadyDoneInsteadOfUpdate)
                    row.updateClaimed()
                else {
                    row.claimed = 1
                    insertReferralEvent(row)
                }
            }
            else -> System.err.println("Unknown event id $row")
        }
        return result
    }

    /**
     * returns a list of rows where claimed=0
     */
    private fun getUnclaimedForReferrerId(referrerId: Int): Dbt<List<ReferralRow>> {
        return query<List<ReferralRow>> {
            prepareStatement(
                connection,
                "SELECT * FROM referral_events WHERE referrerId = :userId AND claimed = 0"
            ).apply {
                setInt("userId", referrerId)
                execute()
            }.run {
                val r = mutableListOf<ReferralRow>()
                while (resultSet.next()) {
                    r.add(
                        ReferralRow(
                            resultSet.getInt("claimed"),
                            resultSet.getInt("eventId"),
                            resultSet.getInt("referrerId"),
                            resultSet.getInt("refereeId"),
                            resultSet.getString("refereeIP"),
                            resultSet.getString("refereeMAC")
                        )
                    )
                }
                r
            }
        }
    }

    /**
     * insert to db, we can give the reward instantly if the player is online.
     */
    fun rewardReferrerByName(referrerName: String, eventId: Int, referee: Player) {
        if (referrerName.isEmpty())
            return
        val referrer = World.getWorld().getPlayerByName(referrerName)
        // no longer instantly reward an online player.
        // queue it up in db, and when referrer uses the UI and clicks "claim" it'll be
        // given.
        /*if (referrer.isPresent) {
            referrer.get().apply {
                processUnclaimedEntry(buildRow(eventId, 1, DATABASE_PLAYER_ID.int(this), referee), true)
            }
        } else {*/
        getPlayerDbIdForName(referrerName).submit {
            if (it == -1) {
                referee.message("Unknown player: $referrerName")
                return@submit
            }
            // assertion: event only inserted once, unlock is is on the referee
            insertReferralEvent(buildRow(eventId, 0, it, referee))
        }
        //}
    }

    /**
     * creates an instance of ReferralRow representing a row in the database.
     */
    fun buildRow(eventId: Int, claimed: Int, referrerId: Int, referee: Player) = ReferralRow(
        claimed, eventId, referrerId,
        DATABASE_PLAYER_ID.int(referee),
        referee.hostAddress, referee.getAttribOr(MAC_ADDRESS, "invalid")
    )

    /**
     * inserts a new ReferralRow into the database.
     */
    fun insertReferralEvent(row: ReferralRow) {
        assert(row.referrerId > -1)
        query {
            val sql = if (row.claimed == 1)
                "INSERT INTO referral_events (claimed, eventId, referrerId, refereeId, refereeIP, refereeMAC, refereeClientID, claimedTime, eventTime) VALUES (:claimed, :eventId, :p1ID, :p2ID, :p2IP, :p2MAC, :p2CID, :claimedTime, :eventTime)"
            else
                "INSERT INTO referral_events (claimed, eventId, referrerId, refereeId, refereeIP, refereeMAC, refereeClientID, eventTime) VALUES (:claimed, :eventId, :p1ID, :p2ID, :p2IP, :p2MAC, :p2CID, :eventTime)"
            prepareStatement(connection, sql).apply {
                setInt("claimed", row.claimed)
                setInt("eventId", row.eventId)
                setInt("p1ID", row.referrerId)
                setInt("p2ID", row.refereeId)
                setString("p2IP", row.refereeIP ?: "")
                setString("p2MAC", row.refereeMAC ?: "")
                setString("p2CID", "") // empty: depreciated. field needed for DB though.
                if (row.claimed == 1)
                    setTimestamp("claimedTime", Timestamp(Date().time))
                setTimestamp("eventTime", Timestamp(Date().time))
                execute()
            }
        }
    }

    /**
     * see getPlayerDbIdForName
     */
    fun Player.fetchDbId(setAttrib: Boolean = true) {
        getPlayerDbIdForName(username).submit {
            if (it == -1) {
                return@submit
            }
            if (setAttrib)
                putAttrib(DATABASE_PLAYER_ID, it)
        }
    }

    /**
     * matches a user in the database by username and returns the rowID
     */
    fun getPlayerDbIdForName(username: String): Dbt<Int> {
        return query<Int> {
            prepareStatement(connection, "SELECT id FROM users WHERE lower(username) = :username").apply {
                setString("username", username.toLowerCase())
                execute()
            }.run {
                if (resultSet.next())
                    resultSet.getInt("id")
                else
                    -1
            }
        }
    }

    /**
     * test by ::refer username. use ::ref2 to reset the username.
     */
    fun askReferrerName(referee: Player) {
        if (referee.getAttribOr<String>(REFERRER_USERNAME, "").isNotEmpty()) {
            referee.message("You already have a Referrer set and have claimed the reward.")
            return
        }
        referee.enterSyntax = object : EnterSyntax {
            override fun handleSyntax(referee: Player, referrerName: String) {
                verifyReferralRequestStage1(referee, referrerName)
            }
        }
        referee.packetSender.sendEnterInputPrompt("Enter the username of the person who referred you to join, or blank if none.")
    }

    fun verifyReferralRequestStage1(referee: Player, referrerName: String, skipSecurity: Boolean = false) {
        if (referrerName.isEmpty())
            return
        if (referrerName.equals(referee.username, ignoreCase = true) && !referee.playerRights.isDeveloperOrGreater(
                referee
            )
        ) {
            referee.message("You can't set yourself as your Referrer.")
            if (referee.playerRights.isDeveloperOrGreater(referee))
                referee.message("But you can as an admin.")
            return
        }
        referee.message("Checking eligibility for referral by $referrerName...")
        // here we search the db for existing eventId=1 (enter referral name events). if any other exist, that means the this IP has been used to claim a referral already.
        query<Int> {
            prepareStatement(
                connection,
                "SELECT COUNT(*) FROM referral_events WHERE refereeIP LIKE :myip AND eventId=1"
            ).apply {
                setString("myip", referee.hostAddress)
                execute()
            }.run {
                if (resultSet.next())
                    resultSet.getInt(1)
                else
                    0
            }
        }.submit { count ->
            referee.debug("Debug: ip matches count: $count")
            if (!skipSecurity && count > 0) {
                referee.message("A referral has already been claimed from your IP address. You can't claim more.")
            } else {
                verifyReferralRequestStage2(referee, referrerName, skipSecurity)
            }
        }
    }

    fun verifyReferralRequestStage2(referee: Player, referrerName: String, skipSecurity: Boolean = false) {
        // validation: make sure we have space for rewards before even trying SQL.
        val theReferrer = World.getWorld().getPlayerByName(referrerName)
        // referrer is Online. we know they are real. skip SQL to check if real player.
        if (theReferrer.isPresent) {
            handleEnteringReferrerWhenOnline(theReferrer.get(), referee, referrerName, skipSecurity)
        } else {
            // confirm a player exists via SQL
            referee.message("Finding referral by $referrerName...")
            getPlayerDbIdForName(referrerName).submit { dbid ->
                GameEngine.getInstance().addSyncTask {
                    // check if user is real
                    if (dbid == -1) {
                        // maybe dont offer ::refer, people will do after the fact for infinite prizes
                        referee.message("No player exists with username $referrerName. You can use ::refer to enter the correct...")
                        referee.message("name within the first hour of gameplay. Afterwards, you will not be able to set a...")
                        referee.message("Referrer.")
                    } else {
                        // prepare query
                        query<String> {
                            prepareStatement(connection, "SELECT id, last_login_ip FROM users WHERE id=:id").apply {
                                setInt("id", dbid)
                                execute()
                            }.run {
                                // prepare the result as a string
                                if (resultSet.next())
                                    resultSet.getString("last_login_ip")
                                else
                                    ""
                            }
                        }.submit { ip ->
                            // with the IP from the offline player, check anticheat
                            GameServer.getDatabaseService().submit(FindPlayersFromIpDatabaseTransaction(ip)) { ips ->
                                if (!skipSecurity && ips.any { referee.username == it }) {
                                    referee.message("You can't use this person as a Referrer as they are connected on the same address.")
                                } else {
                                    referee.rewardForValidReferrer(referrerName)
                                    // queue the offline referrer to be rewarded
                                    // assertion: will only log here once, referee's REFERRER_USERNAME only set once.
                                    insertReferralEvent(buildRow(REFEREE_ENTERED_REFERRER_NAME_EVENT, 0, dbid, referee))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun handleEnteringReferrerWhenOnline(
        referrer: Player,
        referee: Player,
        referrerName: String,
        skipSecurity: Boolean = false
    ) {
        referee.message("Verifying referral of $referrerName...")
        GameServer.getDatabaseService().submit(FindPlayersFromIpDatabaseTransaction(referrer.hostAddress)) { ips ->
            // anticheat: reject IPs from yourselfironman_mode
            if (!skipSecurity && ips.any { referee.username == it }) {
                referee.message("You can't use this person as a Referrer as they are connected on the same address.")
            } else {
                // ######## REWARD NEW PLAYER
                // ready to reward! inv space has already been checked.
                referee.rewardForValidReferrer(referrerName)

                // confirm reward for ::refer is rewarded
                insertReferralEvent(
                    buildRow(
                        REFEREE_ENTERED_REFERRER_NAME_EVENT,
                        1,
                        referrer.getAttrib(DATABASE_PLAYER_ID),
                        referee
                    )
                )

                // ######### REWARD OLD PLAYER (REFERRER)
                // increase their referral count + reward IF POSSIBLE
                val spaceFor =
                    (!referrer.bank.contains(DONATOR_MYSTERY_BOX) || referrer.bank.count(DONATOR_MYSTERY_BOX) < Int.MAX_VALUE - 1)
                if (!spaceFor) {
                    referrer.message("You have Referral Rewards waiting but your bank is full. Make space and relog.")

                    // queue the reward in db to be claimed in the future when space available.
                    insertReferralEvent(
                        buildRow(
                            REFERER_REWARDED_FROM_REFEREE,
                            0,
                            referrer.getAttrib(DATABASE_PLAYER_ID),
                            referee
                        )
                    )
                } else {
                    // give the reward now
                    REFERRALS_COUNT.increment(referrer)
                    referrer.packetSender.sendString(
                        InfoTab.REFERRALS.childId,
                        InfoTab.INFO_TAB[InfoTab.REFERRALS.childId]!!.fetchLineData(referrer)
                    )

                    // and also flag that p2 (referrer) has been given their points since they were online
                    insertReferralEvent(
                        buildRow(
                            REFERER_REWARDED_FROM_REFEREE,
                            1,
                            referrer.getAttrib(DATABASE_PLAYER_ID),
                            referee
                        )
                    )
                }
            }
        }
    }

    /**
     * After using ::refer and providing a real players name, you get a mbox and ddr lamp. no security here.
     */
    private fun Player.rewardForValidReferrer(referrerName: String) {
        // valid referrer. try to give reward.
        if (getAttribOr<String>(MAC_ADDRESS, "invalid").isNotEmpty()) {
            putAttrib(REFERRER_USERNAME, referrerName)
            Utils.sendDiscordInfoLog(this, this.username + " has received a new referral: $referrerName", "referrals")
            message("You have a new referral: $referrerName.")
            inventory.addOrBank(Item(DONATOR_MYSTERY_BOX))
            inventory.addOrBank(Item(LEGENDARY_MYSTERY_BOX))
            inventory.addOrBank(Item(DOUBLE_DROPS_LAMP))
            message("You've been rewarded with a Donator Mystery Box and Double Drop-Rate Lamp.")
        } else {
            message("You are not logged in on a valid machine.")
        }
    }

    fun Player.clearReferralInterface() {
        for (i in 70020..70020 + 100) {
            packetSender.sendString(i, "")
        }
    }

    data class Data(val claimed: Int, val event: Int, val refereeId: Int, val refereeName: String?, val playtime: Int) {
        override fun toString(): String {
            return "Data(claimed=$claimed, event=$event, refereeId=$refereeId, refereeName='$refereeName', playtime=$playtime)"
        }
    }

    fun Player.displayMyReferrals() {
        clearReferralInterface()
        val id = getAttribOr<Int>(DATABASE_PLAYER_ID, -1)
        if (id == -1)
            return
        message("Loading referrals...")
        query<List<Data>> {
            prepareStatement(
                connection, "SELECT * FROM referral_events LEFT JOIN " +
                        "users ON referral_events.refereeId=users.id WHERE referrerId = :userId"
            ).apply {
                setInt("userId", id)
                execute()
            }.run {
                val r = mutableListOf<Data>()
                while (resultSet.next()) {
                    r.add(
                        Data(
                            resultSet.getInt("claimed"),
                            resultSet.getInt("eventId"),
                            resultSet.getInt("refereeId"),
                            resultSet.getString("username"), // from refs table
                            resultSet.getInt("playtime") // from user table!
                        )
                    )
                }
                r
            }
        }.submit { list ->
            displayReferralResults(processQueryToDataFormat(list))
        }
    }

    private fun processQueryToDataFormat(list: List<Data>): HashMap<String, MutableList<Data>> {
        val referralEventsToReferralId = HashMap<String, MutableList<Data>>()
        list.forEach {
            if (it.refereeName == null) {
                System.err.println("No username of referral: $it")
            } else {
                referralEventsToReferralId.compute(it.refereeName, { _, u ->
                    var entries = u
                    if (entries != null) {
                        entries.add(it)
                    } else {
                        entries = mutableListOf(it)
                    }
                    entries
                })
            }
        }
        return referralEventsToReferralId
    }

    private fun Player.displayReferralResults(data: HashMap<String, MutableList<Data>>) {
        //Open interface even tho there are no referrals
        interfaceManager.open(70000)
        GameEngine.getInstance().addSyncTask {
            if (data.isEmpty()) {
                println("You have no referrals.")
                message("You have no referrals.")
                return@addSyncTask
            }
            var line = 70020

            // one name can have multiple related events.
            data.forEach { newbieUsername, u ->
                packetSender.sendString(line, "$newbieUsername")

                val gameTime = u[0].playtime // playtime will be the same on all events
                val time = (gameTime * 0.6).toInt()
                val days = time / 86400
                val hours = time / 3600 - days * 24
                packetSender.sendString(line + 1, "${days}d ${hours} hours")
                println("$newbieUsername playtime $gameTime $time $days $hours")

                // order important for which will display first
                val state: Pair<String, String> =

                    // if the final stage has been claimed, set as complete
                    if (days >= 1 && u.any { it.event == REFEREE_ONE_DAY_PLAYTIME && it.claimed == 1 })
                        "None" to Color.GREEN.wrap("All Claimed")

                    // now check other states
                    else if (days >= 1 && u.any { it.event == REFEREE_TEN_HOURS_PLAYTIME && it.claimed == 1 } && u.any { it.event == REFEREE_ONE_DAY_PLAYTIME && it.claimed == 0 }) // event might not be in db yet, only triggered when referee logs in with playtime
                        "1x wep and arm Mbox" to Color.GREEN.wrap("Claimed")

                    // 10h claimed, next unlock is l.mbox
                    else if (hours >= 10 && u.any { it.event == REFEREE_TEN_HOURS_PLAYTIME && it.claimed == 1 })
                        "1x Donator Mbox" to Color.RED.wrap("1 day playtime")

                    // 10h passed but wasnt claimed (above code would catch it)
                    // event might not be in db yet, only triggered when referee logs in with playtime
                    // only show 10h unclaimed if the 1st reward
                    else if (hours >= 10 && u.any { it.event == REFERER_REWARDED_FROM_REFEREE && it.claimed == 1 } && u.any { it.event == REFEREE_TEN_HOURS_PLAYTIME && it.claimed == 0 })
                        "1x wep and arm Mbox" to Color.GREEN.wrap("Claimed")

                    // havent passed 10h but a referral does exist.
                    // also 1st reward (event 5) has been claimed, so we know this is next
                    else if (u.any { it.event == REFERER_REWARDED_FROM_REFEREE && it.claimed == 1 })
                        "1x wep and arm Mbox" to Color.RED.wrap("10 hours playtime")

                    // the first price, defaults to unclaimed
                    else
                        "1x Mystery box" to Color.GREEN.wrap("Claimed")

                packetSender.sendString(line + 2, state.first)
                packetSender.sendString(line + 3, state.second)
                line += 4
            }
            interfaceManager.open(70000)
        }
    }

    fun handleButton(player: Player, button: Int): Boolean {
        return when (button) {
            70005 -> {
                askReferrerName(player)
                return true
            }
            70006 -> {
                player.message("Fetching Referrals...")
                val results = player.processAllReferralsForMe()
                player.event {
                    if (tick > 100) {
                        stop()
                        System.err.println("Referral events future timed out")
                        return@event
                    }
                    if (!results.done) // wait until all processed
                        return@event
                    if (results.results.isEmpty()) { // odd one out responce.
                        player.message("You have no pending Referrals.")
                        stop()
                        return@event
                    }
                    // messages will be sent if given or not.
                    stop()

                    player.displayMyReferrals() // refresh UI
                }
                return true
            }

            70002 -> {
                //Closing an interface will unlock the player
                player.interfaceManager.close()
                return true
            }
            else -> false
        }
    }

    /**
     * represents a Row in the database.
     */
    class ReferralRow(
        var claimed: Int, val eventId: Int, val referrerId: Int,
        val refereeId: Int, val refereeIP: String?, val refereeMAC: String?
    ) {

        /**
         * updates the row to set claimed=1 so it can't be used again.
         */
        fun updateClaimed() {
            query<Int> {
                prepareStatement(
                    connection,
                    "UPDATE referral_events SET claimed=1, claimedTime=:claimedTime WHERE eventId=:eId AND referrerId=:p1id AND refereeId=:p2id AND claimed=0 LIMIT 1"
                ).apply {
                    setInt("eId", eventId)
                    setInt("p1id", referrerId)
                    setInt("p2id", refereeId)
                    setTimestamp("claimedTime", Timestamp(Date().time))
                }.run {
                    // this SQL method returns the num of rows updated. that what we want to return as Int
                    this.executeUpdate()
                }
            }.submit {
                // assert rowupdated == 1
                assert(it == 1)
            }
        }

        override fun toString(): String {
            return "ReferralRow(claimed=$claimed, eventId=$eventId, referrerId=$referrerId, refereeId=$refereeId, refereeIP='$refereeIP', refereeMAC='$refereeMAC')"
        }

    }
}

