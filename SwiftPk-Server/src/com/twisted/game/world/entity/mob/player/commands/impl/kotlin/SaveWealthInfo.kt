package com.twisted.game.world.entity.mob.player.commands.impl.kotlin

import com.twisted.game.GameEngine
import com.twisted.game.content.tradingpost.TradingPost
import com.twisted.game.world.entity.mob.player.Player
import com.twisted.game.world.entity.mob.player.commands.impl.owner.CheckServerWealthCommand
import com.twisted.util.Utils
import java.io.File
import java.util.stream.LongStream

/**
 * @author Shadowrs/jak tardisfan121@gmail.com
 */
object SaveWealthInfo {

    @JvmStatic
    fun saveWealth(pFeedback: Player, storage: CheckServerWealthCommand.AtomicStorage) {
        // kotlin syntax is just easier to deal with than java
        var str = ""
        var topWealthString = ""
        var topPlayers = 0
        val sortedValues = Utils.sortByComparator(storage.playersValues, false)
        sortedValues.forEach { (key: String, value: Long) ->
            if (topPlayers++ < 10) {
                topWealthString += key + "=" + Utils.formatRunescapeStyle(value) + " " + "bm" + "\n"
            }
        }
        val goal = storage.toScanAmt.get()
        str += ("The players with the most wealth are: \n$topWealthString \n")
        str += ("Scanned " + (goal) + " offline players.\n")
        str += ("The total BM cash for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumBloodMoneyWealth.get()) + "\n")
        str += ("The total BM wealth for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumBloodMoneyItemWealth.get()) + "\n")
        str += ("The total item count for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.itemsCount.toLong()) + "\n")
        str += ("The total REFERRAL by name count for all " + goal + " players is: " + Utils.formatRunescapeStyle(
            storage.sumRefersByName.toLong()
        ) + "\n")
        str += ("The total VOTES count for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumVotePoints.toLong()) + "\n")
        str += ("The total ELY for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumEly.get()) + "\n")
        str += ("The total ARCANE for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumArcane.get()) + "\n")
        str += ("The total AGS for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumAgs.get()) + "\n")
        str += ("The total AGS OR for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumAgsOR.get()) + "\n")
        str += ("The total ANCIENT VLS for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumAncientVLS.get()) + "\n")
        str += ("The total ANCIENT SWH for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumAncientSWH.get()) + "\n")
        str += ("The total FACEGUARD for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumFacegaurd.get()) + "\n")
        str += ("The total ANCIENT FACEGUARD for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumAncientFacegaurd.get()) + "\n")
        str += ("The total DHL for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumDHL.get()) + "\n")
        str += ("The total 5 BOND for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sum5Bond.get()) + "\n")
        str += ("The total 10 BOND for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sum10Bond.get()) + "\n")
        str += ("The total 20 BOND for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sum20Bond.get()) + "\n")
        str += ("The total 40 BOND for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sum40Bond.get()) + "\n")
        str += ("The total 50 BOND for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sum50Bond.get()) + "\n")
        str += ("The total 100 BOND for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sum100Bond.get()) + "\n")
        str += ("The total WYVERN SHIELD for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumWyvernShield.get()) + "\n")
        str += ("The total DRAGONFIRE WARD for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumWardShield.get()) + "\n")
        str += ("The total SANG SCYTHE for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumSangScythe.get()) + "\n")
        str += ("The total HOLY SCYTHE for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumHolyScythe.get()) + "\n")
        str += ("The total SCYTHE for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumScythe.get()) + "\n")
        str += ("The total TBOW for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumTwistedbow.get()) + "\n")
        str += ("The total TBOW (I) for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumTwistedBowI.get()) + "\n")
        str += ("The total SANG TBOW for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumSangTwistedBow.get()) + "\n")
        str += ("The total ELDER MAUL for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumElderMaul.get()) + "\n")
        str += ("The total INFERNAL CAPE for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumInfernalCape.get()) + "\n")
        str += ("The total SERP for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumSerp.get()) + "\n")
        str += ("The total TSOTD for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumTSOTD.get()) + "\n")
        str += ("The total CRAWS BOW for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumCrawBow.get()) + "\n")
        str += ("The total VIG MACE for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumVig.get()) + "\n")
        str += ("The total CRAWS BOW C for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumCrawBowC.get()) + "\n")
        str += ("The total VIG MACE C for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumVigC.get()) + "\n")
        str += ("The total FERO GLOVES for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumFero.get()) + "\n")
        str += ("The total AVERNIC DEFENDER for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumAvernic.get()) + "\n")
        str += ("The total PRIMORDIAL for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumPrim.get()) + "\n")
        str += ("The total PEGASIAN for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumPeg.get()) + "\n")
        str += ("The total ETERNAL for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumEternal.get()) + "\n")
        str += ("The total PRIMORDIAL OR for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumPrimOR.get()) + "\n")
        str += ("The total PEGASIAN OR for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumPegOR.get()) + "\n")
        str += ("The total ETERNAL OR for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumEternalOR.get()) + "\n")
        str += ("The total CORRUPTED BOOTS for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumCorruptedBoots.get()) + "\n")
        str += ("The total FEROX COINS for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumLuxCoins.get()) + "\n")
        str += ("The total ANCETSTRAL HAT for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumAncestralHat.get()) + "\n")
        str += ("The total ANCESTRAL TOP for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumAncestralTop.get()) + "\n")
        str += ("The total ANCESTRAL BOTTOM for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumAncestralBottom.get()) + "\n")
        str += ("The total TWISTED ANCETSTRAL HAT for all " + goal + " players is: " + Utils.formatRunescapeStyle(
            storage.sumAncestralHatT.get()
        ) + "\n")
        str += ("The total TWISTED ANCESTRAL TOP for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumAncestralTopT.get()) + "\n")
        str += ("The total TWISTED ANCESTRAL BOTTOM for all " + goal + " players is: " + Utils.formatRunescapeStyle(
            storage.sumAncestralBottomT.get()
        ) + "\n")
        str += ("The total DRAGON CLAWS for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumClaws.get()) + "\n")
        str += ("The total DRAGON CLAWS OR for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumClawsOr.get()) + "\n")
        str += ("The total DARK ELDER MAUL for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumDarkElder.get()) + "\n")
        str += ("The total JAWA PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumJawaPet.get()) + "\n")
        str += ("The total ZRIAWK PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumZriawkPet.get()) + "\n")
        str += ("The total FAWKES PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumFawkesPet.get()) + "\n")
        str += ("The total HAZY COINS for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumHazyCoins.get()) + "\n")
        str += ("The total RECOLORED FAWKES PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumRecoloredFawkesPet.get()) + "\n")
        str += ("The total NIFFLER PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumNifflerPet.get()) + "\n")
        str += ("The total WAMPA PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumWampaPet.get()) + "\n")
        str += ("The total BABY ARAGOG PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumBabyAragogPet.get()) + "\n")
        str += ("The total MINI NECROMANCER PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumMiniNecromancerPet.get()) + "\n")
        str += ("The total CORRUPTED NECHRYARCH PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(
            storage.sumCorruptedNechryarchPet.get()
        ) + "\n")
        str += ("The total GRIM REAPER PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumGrimReaperPet.get()) + "\n")
        str += ("The total KERBEROS PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumKerberosPet.get()) + "\n")
        str += ("The total SKORPIOS PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumSkorpiosPet.get()) + "\n")
        str += ("The total ARACHNE PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumArachnePet.get()) + "\n")
        str += ("The total ARTIO PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumArtioPet.get()) + "\n")
        str += ("The total LITTLE NIGHTMARE PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumLittleNightmarePet.get()) + "\n")
        str += ("The total DEMENTOR PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumDementorPet.get()) + "\n")
        str += ("The total FENRIR GREYBACK JR PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(
            storage.sumFenrirGreybackJrPet.get()
        ) + "\n")
        str += ("The total FLUFFY JR PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumFluffyJrPet.get()) + "\n")
        str += ("The total ANCIENT KBD PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumAncientKBDPet.get()) + "\n")
        str += ("The total ANCIENT CHAOS ELE PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumAncientChaosElePet.get()) + "\n")
        str += ("The total ANCIENT BARRELCHEST PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(
            storage.sumAncientBarrelchestPet.get()
        ) + "\n")
        str += ("The total FOUNDER IMP PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumFounderImpPet.get()) + "\n")
        str += ("The total BABY LAVA DRAGON PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumBabyLavaDragonPet.get()) + "\n")
        str += ("The total JALTOK JAD PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumJaltokJadPet.get()) + "\n")
        str += ("The total TZREK ZUK PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumTzrekZukPet.get()) + "\n")
        str += ("The total RING OF ELYSIAN for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumRingOfElysianPet.get()) + "\n")
        str += ("The total BLOOD MONEY PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumBloodMoneyPet.get()) + "\n")
        str += ("The total GENIE PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumGeniePet.get()) + "\n")
        str += ("The total DHAROK PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumDharokPet.get()) + "\n")
        str += ("The total ETHEREAL SCYTHE for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumEtherealScythe.get()) + "\n")
        str += ("The total ZOMBIES CHAMPION PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumZombiesChampionPet.get()) + "\n")
        str += ("The total ABYSSAL DEMON PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumAbyssalDemonPet.get()) + "\n")
        str += ("The total DARK BEAST PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumDarkBeastPet.get()) + "\n")
        str += ("The total BABY SQUIRT PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumBabySquirtPet.get()) + "\n")
        str += ("The total JALNIB REK PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumJalnibRekPet.get()) + "\n")
        str += ("The total CENTAUR MALE PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumCentaurMalePet.get()) + "\n")
        str += ("The total CENTAUR FEMALE PET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumCentaurFemalePet.get()) + "\n")
        str += ("The total CRYSTAL HELM for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumCrystalHelm.get()) + "\n")
        str += ("The total CRYSTAL BODY for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumCrystalBody.get()) + "\n")
        str += ("The total CRYSTAL LEGS for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumCrystalLegs.get()) + "\n")
        str += ("The total DARK ARMADYL HELMET for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumDarkArmadylHelm.get()) + "\n")
        str += ("The total WILDERNESS KEYS for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumWildernessKeys.get()) + "\n")
        str += ("The total DARK ARMADYL CHESTPLATE for all " + goal + " players is: " + Utils.formatRunescapeStyle(
            storage.sumDarkArmadylChestplate.get()
        ) + "\n")
        str += ("The total DARK ARMADYL CHAINSKIRT for all " + goal + " players is: " + Utils.formatRunescapeStyle(
            storage.sumDarkArmadylChainskirt.get()
        ) + "\n")
        str += ("The total BOW OF FAERDHINEN for all " + goal + " players is: " + Utils.formatRunescapeStyle(storage.sumBowOfFaerdhinen.get()) + "\n")

        GameEngine.getInstance().submitLowPriority {
            File("./data/eco scan - summary.txt").createNewFile()
            File("./data/eco scan - summary.txt").writeText(str)
        }

        fullbm(storage)
        fullvotes(storage)
        fullRefByName(storage)
        fullitems(storage)
    }

    fun fullbm(storage: CheckServerWealthCommand.AtomicStorage) {
        val sortedValues = Utils.sortByComparator(storage.playersValues, false)
        var str = ""
        val v = LongStream.of(storage.sumAll.get()).sum();
        str += "Total Server Wealth: " + Utils.formatRunescapeStyle(v) + "\n\n"
        sortedValues.forEach { (key: String, value: Long) ->
            if (value > 0) {
                str += "[Player: " + key + "] \n" + " - Networth: " + Utils.formatRunescapeStyle(value) + " " + "bm" + "\n\n"
            }
        }
        GameEngine.getInstance().submitLowPriority {
            File("./data/eco scan - bm.txt").createNewFile()
            File("./data/eco scan - bm.txt").writeText(str)
        }
    }

    fun fullvotes(storage: CheckServerWealthCommand.AtomicStorage) {
        val sortedValues = storage.loaded.toMutableList().sortedByDescending {
            storage.vp(it)
        }
        var str = "full breakdown is:\n"
        sortedValues.forEach { p ->
            str += "${p.mobName} has ${Utils.formatRunescapeStyle(storage.vp(p))} votes\n"
        }
        GameEngine.getInstance().submitLowPriority {
            File("./data/eco scan - votes.txt").createNewFile()
            File("./data/eco scan - votes.txt").writeText(str)
        }
    }

    fun fullRefByName(storage: CheckServerWealthCommand.AtomicStorage) {
        val sortedValues = storage.loaded.toMutableList().sortedByDescending {
            storage.refc(it)
        }
        var str = "full breakdown is:\n"
        sortedValues.forEach { p ->
            str += "${p.mobName} has ${Utils.formatRunescapeStyle(storage.refc(p))} refs by name\n"
        }
        GameEngine.getInstance().submitLowPriority {
            File("./data/eco scan - ref by name.txt").createNewFile()
            File("./data/eco scan - ref by name.txt").writeText(str)
        }
    }

    fun fullitems(storage: CheckServerWealthCommand.AtomicStorage) {
        println("bm items starting")
        val sortedValues = storage.loaded.toMutableList().filter { storage.BMtotal(it) > 0 }.sortedByDescending {
            storage.BMtotal(it)
        }
        var str = ""
        sortedValues.forEachIndexed { ix, p ->
            val items = storage.playerBmItems[p]?.filterNotNull()
            str += "${"[Player Name: " + p.mobName + "]"} \n - ${"Networth: " +  Utils.formatRunescapeStyle(storage.BMtotal(p)) +" Blood Money"} \n\n"
            items?.forEachIndexed { _, it ->
                if (it.amount > 0) {
                    str += "[Item: ${it.name()}] \n - Amount: x${Utils.formatRunescapeStyle(it.amount.toLong())} \n - Value: ${Utils.formatRunescapeStyle(it.amount * 1L * it.bloodMoneyPrice.value())} \n - Trading Post Value: ${Utils.formatRunescapeStyle(
                        TradingPost.getProtectionPrice(it.id).toLong()
                    )} \n"
                }
            }
            str += "\n\n"
            if (ix % 50 == 0) println("done ${(ix.toDouble() / sortedValues.size.toDouble()) * 100.0}% bm scans...")
        }
        println("bm items save done, submit to save")
        GameEngine.getInstance().submitLowPriority {
            File("./data/eco scan - player all bm items.txt").createNewFile()
            File("./data/eco scan - player all bm items.txt").writeText(str)
            println("bm items save complete")
        }
    }
}
