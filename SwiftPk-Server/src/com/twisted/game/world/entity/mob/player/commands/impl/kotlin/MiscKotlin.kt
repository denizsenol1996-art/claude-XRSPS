package com.twisted.game.world.entity.mob.player.commands.impl.kotlin

import com.twisted.db.Dbt
import com.twisted.db.query
import com.twisted.db.submit
import com.twisted.game.GameEngine
import com.twisted.game.task.TaskManager
import com.twisted.game.world.World
import com.twisted.game.world.entity.AttributeKey
import com.twisted.game.world.entity.Mob
import com.twisted.game.world.entity.mob.player.Player
import java.io.File
import java.sql.Timestamp

/**
 * @author Shadowrs/jak tardisfan121@gmail.com
 */
object MiscKotlin {

    fun addIPBan(player: Player, user: String, expires: Timestamp, reason: String,
                 feedbackKicked: Function1<List<Player>, Unit>? = null) {
        val plr = World.getWorld().getPlayerByName(user)
        var ip = ""
        plr.ifPresent {
            ip = it.hostAddress
        }
        if (!plr.isPresent) {
            getIPForUsername(user).submit {
                ip = it
                if (ip == "")
                    player.message("Player with name '$user' has no IP. They cannot be banned.")
                else
                    ipban(ip, expires, reason, feedbackKicked)
            }
        } else {
            if (ip == "")
                player.message("Player with name '$user' has no IP. They cannot be banned.")
            else
                ipban(ip, expires, reason, feedbackKicked)
        }
    }

    fun getIPForUsername(user: String): Dbt<String> {
        return query<String> {
            prepareStatement(connection, "SELECT last_login_ip FROM users WHERE lower(username) = :user").apply {
                setString("user", user.toLowerCase())
                execute()
            }.run {
                if (resultSet.next())
                    resultSet.getString("last_login_ip")
                else
                    ""
            }
        }
    }

    private fun ipban(ip: String, expires: Timestamp, reason: String, feedbackKicked: ((List<Player>) -> Unit)?) {
        query {
            prepareStatement(connection, "INSERT INTO ip_bans (ip, unban_at, reason) VALUES (:ip, :unban, :reason)").apply {
                setString("ip", ip)
                setTimestamp("unban", expires)
                setString("reason", reason)
                execute()
            }
        }
        val removed = mutableListOf<Player>()
        World.getWorld().players.filterNotNull().forEach {
            if (it.hostAddress.equals(ip)) {
                it.requestLogout()
                removed.add(it)
            }
        }
        feedbackKicked?.let { it(removed) }
    }

    fun addMacBan(player: Player, user: String, expires: Timestamp, reason: String,
                  feedbackKicked: Function1<List<Player>, Unit>? = null) {
        val plr = World.getWorld().getPlayerByName(user)
        var mac = ""
        plr.ifPresent {
            mac = it.getAttribOr(AttributeKey.MAC_ADDRESS, "invalid")
        }
        if (!plr.isPresent) {
            getMacForUsername(user).submit {
                mac = it
                if (mac == "")
                    player.message("Player with name '$user' has no MAC (probably on a VM/VPN) use account ban/cid ban instead.")
                else
                    macban(mac, expires, reason, feedbackKicked)
            }
        } else {
            if (mac == "")
                player.message("Player with name '$user' has no MAC (probably on a VM/VPN) use account ban/cid ban instead.")
            else
                macban(mac, expires, reason, feedbackKicked)
        }
    }

    fun getMacForUsername(user: String): Dbt<String> {
        return query<String> {
            prepareStatement(connection, "SELECT last_login_mac FROM users WHERE lower(username) = :user").apply {
                setString("user", user.toLowerCase())
                execute()
            }.run {
                if (resultSet.next())
                    resultSet.getString("last_login_mac")
                else
                    ""
            }
        }
    }

    fun macban(mac: String, expires: Timestamp, reason: String, feedbackKicked: ((List<Player>) -> Unit)?) {
        query {
            prepareStatement(connection, "INSERT INTO macid_bans (macid, unban_at, reason) VALUES (:mac, :unban, :reason)").apply {
                setString("mac", mac)
                setTimestamp("unban", expires)
                setString("reason", reason)
                execute()
            }
        }
        val removed = mutableListOf<Player>()
        World.getWorld().players.filterNotNull().forEach {
            if (it.getAttribOr<String>(AttributeKey.MAC_ADDRESS, "invalid") == mac) {
                it.requestLogout()
                removed.add(it)
            }
        }
        feedbackKicked?.let { it(removed) }
    }

    @JvmStatic
    fun dumpStateOnBaddie() {
        val sb = StringBuilder()
        World.getWorld().players.filterNotNull().forEach {
            sb.append("player ${it.toString()} combat = ${it.combat.toString()}\n")
        }
        val dump = sb.toString()
        GameEngine.getInstance().submitLowPriority {
            File("./data/lag trigger info.txt").createNewFile()
            File("./data/lag trigger info.txt").writeText(dump)
        }
    }

    @JvmField var lastTasksComputed = ""
    @JvmField var lastTasksComputedTick = -1

    /**
     * compute and write taskmanager-breakdown.txt to /data/
     */
    fun runningTasks() {
        if (lastTasksComputedTick == GameEngine.gameTicksIncrementor)
            return
        lastTasksComputedTick = GameEngine.gameTicksIncrementor
        // fetch on game thread
        val count = TaskManager.getTaskAmount()
        val tasks = TaskManager.getActiveTasks()
        GameEngine.getInstance().submitLowPriority {
            val mobtasks = tasks.filterNotNull().filter { it.key is Mob }.count()
            val names = mutableMapOf<String, Int>()
            tasks.filterNotNull().forEach {
                names.compute(it.keyOrOrigin(), { k, u -> 1 + (u?: 0) })
            }
            var str = """TaskManager tasks: $count, of which $mobtasks are owned by mobs, the other ${count-mobtasks} have non-mob key types.
                    |Types are:
                    |${names.toList().sortedByDescending { (_: String, v: Int) -> v}.toMap().map { "${it.key} x ${it.value}\n" }}
                """.trimMargin()
            lastTasksComputed = str
            File("./data/taskmanager-breakdown.txt").createNewFile()
            File("./data/taskmanager-breakdown.txt").writeText(str)
        }
    }
}
