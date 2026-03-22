package com.twisted.db.transactions

import com.twisted.GameServer
import com.twisted.db.makeQuery
import com.twisted.db.onDatabase
import com.twisted.db.query
import com.twisted.game.world.World
import com.twisted.game.world.entity.dialogue.DialogueManager
import com.twisted.game.world.entity.dialogue.Expression
import com.twisted.game.world.entity.mob.player.Player
import com.twisted.game.world.items.Item
import com.twisted.util.Color
import com.twisted.util.CustomItemIdentifiers.*
import com.twisted.util.NpcIdentifiers.WISE_OLD_MAN
import java.time.LocalDateTime

object CollectPayments {

    class DonationRow(val itemId: Int, val itemAmt: Int, val rowId: Int)

    fun Player.collectPayments() {
        // get a list of itemNumer, amt, paidAmt
        query<List<DonationRow>> {
            val list = mutableListOf<DonationRow>()
            prepareStatement(
                    connection,
                    "SELECT * FROM wpwo_rs_orders WHERE lower(username)=:user AND lower(claimed)='unclaimed'"
            ).apply {
                setString("user", username)
                execute()
                while (resultSet.next()) {
                    list.add(
                            DonationRow(
                                    resultSet.getInt("item_id"),
                                    resultSet.getInt("item_amount"),
                                    resultSet.getInt("id")
                            )
                    )
                }
            }
            list
        }.onDatabase(GameServer.getDatabaseService()) { list ->
            if (list.isEmpty()) {
                DialogueManager.npcChat(
                        this,
                        Expression.DEFAULT,
                        WISE_OLD_MAN,
                        "There are no pending purchases to claim.",
                        "Wait a minute and try again, or contact an Administrator."
                )
            }
            list.forEach { row ->

                val item = row.itemId

                // each row is a reward in the table. do checks that we can fit it inside the bank before actually giving.
                var spaceFor = (!bank.contains(item) || bank.count(item) < Int.MAX_VALUE - row.itemAmt)

                // if a purchase is like 2 items in one you'll need a custom check here:
                if (row.itemId == 100_000) { // example purchase product_number
                    // in this example, purchase 100k is actually 2 items, item 1 x5 and item2 x10. here we check bank space.
                    spaceFor = (!bank.contains(100_001) || bank.count(100_001) < Int.MAX_VALUE - 5)
                            && (!bank.contains(100_002) || bank.count(100_002) < Int.MAX_VALUE - 10)
                }

                if (!spaceFor) {
                    // no space. inform user, purchase is NOT set as claimed.
                    message("Your bank was too full. Make some space and reclaim.")
                } else {
                    // now query again, setting claimed after we've confirmed they have space
                    val currentDateTime = LocalDateTime.now()
                    makeQuery {
                        prepareStatement(
                                connection,
                                "UPDATE wpwo_rs_orders SET claimed='Claimed', claim_date=:date, claim_ip=:ip WHERE id=:id"
                        ).apply {
                            setInt("id", row.rowId)
                            setString("date", currentDateTime.toString())
                            setString("ip", hostAddress ?: "unknown")
                            execute()
                        }
                    }.onDatabase(GameServer.getDatabaseService()) {
                        var paymentAmount = 0.0
                        when (row.itemId) {
                            FIVE_DOLLAR_BOND -> {
                                paymentAmount = 5.0
                            }

                            TEN_DOLLAR_BOND -> {
                                paymentAmount = 10.0
                            }

                            TWENTY_DOLLAR_BOND -> {
                                paymentAmount = 20.0
                            }

                            FORTY_DOLLAR_BOND -> {
                                paymentAmount = 40.0
                            }

                            FIFTY_DOLLAR_BOND -> {
                                paymentAmount = 50.0
                            }

                            ONE_HUNDRED_DOLLAR_BOND -> {
                                paymentAmount = 100.0
                            }

                            ARMOUR_MYSTERY_BOX -> {
                                paymentAmount = 3.00
                            }

                            WEAPON_MYSTERY_BOX -> {
                                paymentAmount = 3.00
                            }

                            DONATOR_MYSTERY_BOX -> {
                                paymentAmount = 5.00
                            }

                            LEGENDARY_MYSTERY_BOX -> {
                                paymentAmount = 20.00
                            }
                            PRESENT_MYSTERY_BOX -> {
                                paymentAmount = 5.00
                            }
                            RAIDS_MYSTERY_BOX -> {
                                paymentAmount = 50.00
                            }
                            EPIC_PET_BOX -> {
                                paymentAmount = 30.00
                            }
                        }

                        if(GameServer.properties().donatorPromo) {
                            val mysteryTickets = paymentAmount.toInt() / 24
                            if (mysteryTickets > 0) {
                                inventory.addOrBank(Item(MYSTERY_TICKET, mysteryTickets))
                                World.getWorld().sendWorldMessage("<img=1081>" + username.toString() + " just received <col=" + Color.BLUE.colorValue.toString() + ">x" + mysteryTickets + " mystery tickets</col> for donating! Support us at <col=" + Color.BLUE.colorValue.toString() + ">::donate</col>!")
                            }
                        }

                        //Buy two get one free promo
                        if (GameServer.properties().buyTwoGetOneFree) {
                            // Award a 'buy-two-get-one free' special if acceptable.
                            val bonus = row.itemAmt / 2
                            inventory.addOrBank(Item(row.itemId, row.itemAmt + bonus))
                            if (bonus > 0)
                                message("${Color.RED.tag()}You have been rewarded extra items because of our active payment deal.")
                        } else {
                            //No promo active, give the purchase items without bonus
                            inventory.addOrBank(Item(row.itemId, row.itemAmt))
                        }

                        if (GameServer.properties().promoEnabled) {
                            this.paymentPromo.checkForPromoReward(paymentAmount)
                        }
                    }
                }
            }
        }
    }
}
