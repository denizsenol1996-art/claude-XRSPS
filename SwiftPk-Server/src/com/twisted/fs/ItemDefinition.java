package com.twisted.fs;

import com.twisted.game.GameConstants;
import com.twisted.game.world.definition.BloodMoneyPrices;
import com.twisted.game.world.items.Item;
import com.twisted.io.RSBuffer;
import com.twisted.util.ItemIdentifiers;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;

/**
 * Created by Bart Pelle on 10/4/2014.
 */
public class ItemDefinition implements Definition {
    public boolean isNote() {
        return notelink != -1 && noteModel != -1;
    }

    public static Int2ObjectMap<ItemDefinition> cached = new Int2ObjectOpenHashMap<>();
    public Map<Integer, Object> params = null;
    public int resizey;
    public int xan2d;
    public int cost = 1;
    public int wearPos3;
    public int inventoryModel;
    public int resizez;
    public short[] recol_s;

    public String unknown1;

    public int wearPos1;
    public int wearPos2;

    public short[] recol_d;
    public String name = "null";
    public int zoom2d = 2000;
    public int yan2d;
    public int zan2d;
    public int yof2d;
    private boolean stackable;
    public int[] countco;
    public boolean members = false;
    public String[] options = new String[5];
    public String[] ioptions = new String[5];
    public int maleModel0;
    public int maleModel1;
    public short[] retex_s;
    public short[] retex_d;
    public int femaleModel1;
    public int maleModel2;
    public int xof2d;
    public int manhead;
    public int manhead2;
    public int womanhead;
    public int womanhead2;
    public int[] countobj;
    public int femaleModel2;
    public int notelink;
    public int femaleModel0;
    public int resizex;
    public int noteModel;
    public int ambient;
    public int contrast;
    public int team;
    public boolean grandexchange;
    public boolean unprotectable;
    public boolean dummyitem;

    public int category;
    public int weight;
    public int placeheld = -1;
    public int pheld14401 = -1;
    public int shiftClickDropType = -1;
    private int op139 = -1;
    private int op140 = -1;

    public int id;

    // our fields: optimized speed so you dont need 1k loops
    public boolean isCrystal;
    public boolean tradeable_special_items;
    public boolean changes;
    public boolean autoKeptOnDeath;
    public BloodMoneyPrices bm;
    public boolean pvpAllowed;

    public ItemDefinition(int id, byte[] data) {
        this.id = id;

        if (data != null && data.length > 0) decode(new RSBuffer(Unpooled.wrappedBuffer(data)));
        custom();
        cached.put(id, this);
    }

    void decode(RSBuffer buffer) {
        while (true) {
            int op = buffer.readUByte();
            if (op == 0)
                break;
            decodeValues(buffer, op);
        }
        postDecode(id);
    }

    void custom() {//updatevoidor
        if (id == ItemIdentifiers.TOXIC_BLOWPIPE || id == ItemIdentifiers.SERPENTINE_HELM || id == ItemIdentifiers.TRIDENT_OF_THE_SWAMP || id == ItemIdentifiers.TOXIC_STAFF_OF_THE_DEAD
            || id == ItemIdentifiers.TOME_OF_FIRE || id == ItemIdentifiers.SCYTHE_OF_VITUR || id == ItemIdentifiers.SANGUINESTI_STAFF || id == ItemIdentifiers.CRAWS_BOW
            || id == ItemIdentifiers.VIGGORAS_CHAINMACE || id == ItemIdentifiers.THAMMARONS_SCEPTRE || id == ItemIdentifiers.TRIDENT_OF_THE_SEAS || id == ItemIdentifiers.MAGMA_HELM
            || id == ItemIdentifiers.TANZANITE_HELM || id == ItemIdentifiers.DRAGONFIRE_SHIELD || id == ItemIdentifiers.DRAGONFIRE_WARD || id == ItemIdentifiers.ANCIENT_WYVERN_SHIELD
            || id == ABYSSAL_TENTACLE_OR || id == ItemIdentifiers.ABYSSAL_TENTACLE || id == BARRELCHEST_ANCHOR || id == ItemIdentifiers.SARADOMINS_BLESSED_SWORD) {
            ioptions = new String[]{null, "Wield", null, null, "Drop"};
        }


        boolean replace_drop_with_destroy = Arrays.stream(Item.AUTO_KEPT_LIST).anyMatch(auto_kept_id -> auto_kept_id == id);

        if (replace_drop_with_destroy) {
            ioptions = new String[]{null, null, null, null, "Destroy"};
        }

        int[] untradeables_with_destroy = new int[]{
            VOLATILE_NIGHTMARE_STAFF,
            HARMONISED_NIGHTMARE_STAFF,
            ELDRITCH_NIGHTMARE_STAFF,
        };

        if (IntStream.of(untradeables_with_destroy).anyMatch(untradeable -> id == untradeable)) {
            ioptions = new String[]{null, null, null, null, "Destroy"};
        }

        if (name.contains("slayer helmet") || name.contains("Slayer helmet")) {
            ioptions = new String[]{null, "Wear", null, null, "Drop"};
        }

        //Bounty hunter emblem hardcoding.
        if (id == 12746 || (id >= 12748 && id <= 12756)) {
            unprotectable = true;
        } else if (id == 5021) {//UPDATEAFKTICKET
            name = "Afk ticket";
            stackable = true;
        } else if (id == 30099) {//ibag
            name = "Looting bag (I)";
        } else if (id == 30098) {//ibag
            name = "Looting bag (I)";
        } else if (id == 30054) {
            name = "Escape key";
            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == CURSED_ELEMENT_PET) {
            name = "Cursed Element Pet";
        } else if (id == THE_ONE_ABOVE_ALL_PET) {
            name = "The One Above All Pet";
        } else if (id == YOSHI_PET) {
            name = "Yoshi Pet";
        } else if (id == 30046) {
            name = "Enchanted Key (r)";
            stackable = true;
            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == 30045) {
            name = "Enchanted Key (p)";
            stackable = true;
            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == 30043) {
            name = "Deranged archaeologist";

            //fixtoday
        } else if (id == 20321) {
            name = "Black Ankou mask";
        } else if (id == 20322) {
            name = "Black Ankou Gloves";
        } else if (id == 20323) {
            name = "Black Ankou Top";
        } else if (id == 20324) {
            name = "Black Ankou leggingas";
        } else if (id == 20325) {
            name = "Black Ankou Socks";
        } else if (id == 25731) {
            name = "Holy Sanguinesti Staff";
        } else if (id == VOTEBOSS_MYSTERY_BOX) {//updatevoteboss
            name = "Vote Boss Mystery Box";
            //fixtoday
        } else if (id == 30042) {
            name = "Enchanted bones";
        } else if (id == 621) {
            name = "Pk ticket";
            stackable = true;
        } else if (id == 30041) {
            name = "Anathematic wand";
        } else if (id == 30091) {//BLOODREVSUPDATE
            name = "Mending life wand";
        } else if (id == 30040) {
            name = "Ring of divination";
        } else if (id == 30039) {
            name = "Deranged manifesto";
            ioptions = new String[]{null, "Wield", null, "Teleport", "Drop"};
        } else if (id == 30035) {
            name = "Anathematic stone";
        } else if (id == 30034) {
            name = "Anathema ward";
        } else if (id == 18336) {
            name = "Molten partyhat";
        } else if (id == 30032) {
            name = "Corrupted crystal helm";
        } else if (id == 30031) {
            name = "Corrupted crystal legs";
        } else if (id == 30030) {
            name = "Corrupted crystal body";
        } else if (id == 30028) {
            name = "Molten defender";
        } else if (id == 30026) {
            name = "Molten Mystery Box";
        } else if (id == 30027) {
            name = "Lava beast pet";
        } else if (id == 30056) {
            name = "Memory of seren";
        } else if (id == 30058) {
            name = "Dark magician";
        } else if (id == 30019) {
            name = "Baby Elvarg";
        } else if (id == 30043) {
            name = "Deranged archaeologist";
        } else if (id == 30017) {
            name = "Snowbird";
        } else if (id == 30005) {
            name = "Elder ice maul";
            ioptions = new String[]{null, "Wield", null, null, "Drop"};
        } else if (id == 30001) {
            name = "Frost claws";
            ioptions = new String[]{null, "Wield", null, null, "Drop"};
        } else if (id == 30003) {
            name = "Armadyl frostsword";
            ioptions = new String[]{null, "Wield", null, null, "Drop"};
        } else if (id == 30239) {
            name = "Snowy sled";
        } else if (id == 4083) {
            name = "Sled";
        } else if (id == 24950) {
            name = "Iced partyhat";
        } else if (id == 30007) {
            name = "Iced h'ween mask";
        } else if (id == 30009) {
            name = "Iced santa hat";
        } else if (id == 30011) {
            name = "Ugly santa hat";
        } else if (id == 30013) {
            name = "Infinity winter boots";
        } else if (id == 30015) {
            name = "Frost imbued cape";
        } else if (id == 30023) {
            name = "Frost imbued max cape";
        } else if (id == 30025) {
            name = "Frost imbued max hood";
        } else if (id == GIANT_PRESENT) {
            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == 4564) {
            stackable = true;
        } else if (id == 30196) {
            name = "Totemic helmet";
        } else if (id == 30305) {
            name = "Ruinous Prayer Book";
        } else if (id == 30199) {
            name = "Totemic platebody";
        } else if (id == 30202) {
            name = "Totemic platelegs";
        } else if (id == 23154) {
            name = "Enchanted totemic helmet";
        } else if (id == 23156) {
            name = "Enchanted totemic platebody";
        } else if (id == 23158) {
            name = "Enchanted totemic platelegs";
        } else if (id == 30000) {
            name = "Dark sage hat";
        } else if (id == 30002) {
            name = "Dark sage robe top";
        } else if (id == 30004) {
            name = "Dark sage robe bottoms";
        } else if (id == 30036 || id == 30037) {
            name = "Twisted bow";
        } else if (id == 30020) {
            name = "Sarkis dark coif";
        } else if (id == 30021) {
            name = "Sarkis dark body";
        } else if (id == 30022) {
            name = "Sarkis dark legs";
        } else if (id == 30104) {
            name = "Resource pack";
        } else if (id == SUMMER_TOKEN) {
            name = "Summer token";
            stackable = true;
        } else if (id == 30280) {
            name = "Agility master cape";
        } else if (id == 30282) {
            name = "Attack master cape";
        } else if (id == 30284) {
            name = "Construction master cape";
        } else if (id == 30286) {
            name = "Cooking master cape";
        } else if (id == 30288) {
            name = "Crafting master cape";
        } else if (id == 30290) {
            name = "Defence master cape";
        } else if (id == 30292) {
            name = "Farming master cape";
        } else if (id == 30294) {
            name = "Firemaking master cape";
        } else if (id == 30246) {
            name = "Fishing master cape";
        } else if (id == 30248) {
            name = "Fletching master cape";
        } else if (id == 30298) {
            name = "Herblore master cape";
        } else if (id == 30296) {
            name = "Hitpoints master cape";
        } else if (id == 30254) {
            name = "Hunter master cape";
        } else if (id == 30256) {
            name = "Magic master cape";
        } else if (id == 30258) {
            name = "Mining master cape";
        } else if (id == 30260) {
            name = "Prayer master cape";
        } else if (id == 30262) {
            name = "Range master cape";
        } else if (id == 30264) {
            name = "Runecrafting master cape";
        } else if (id == 30266) {
            name = "Slayer master cape";
        } else if (id == 30268) {
            name = "Smithing master cape";
        } else if (id == 30270) {
            name = "Strength master cape";
        } else if (id == 24855) {
            name = "Enchanted max cape";
        } else if (id == 30272) {
            name = "Thieving master cape";
        } else if (id == 30274) {
            name = "Woodcutting master cape";
        } else if (id == 30244) {
            name = "Revenant mystery box";
        } else if (id == 30224) {
            name = "Grim h'ween mask";
        } else if (id == 30225) {
            name = "Grim partyhat";
        } else if (id == 30226) {
            name = "Grim scythe";
        } else if (id == 30227) {
            name = "H'ween mystery chest";
        } else if (id == 30228) {
            name = "Haunted hellhound pet";
        } else if (id == 30229) {
            name = "H'ween armadyl godsword";
        } else if (id == 30230) {
            name = "H'ween blowpipe";
        } else if (id == 30231) {
            name = "H'ween dragon claws";
        } else if (id == 30232) {
            name = "H'ween craw's bow";
        } else if (id == 30233) {
            name = "H'ween chainmace";
        } else if (id == 30234) {
            name = "H'ween granite maul";
        } else if (id == 30240) {
            name = "Haunted crossbow";
        } else if (id == 30241) {
            name = "Haunted dragonfire shield";
        } else if (id == 30222) {
            name = "Enchanted ticket";
            stackable = true;
        } else if (id == 30223) {
            name = "Blood money casket (100-250k)";
        } else if (id == 30315) {
            name = "Darklord bow";
        } else if (id == 30309) {
            name = "Darklord sword";
        } else if (id == 30312) {
            name = "Darklord staff";
        } else if (id == 16475) {
            name = "Activity casket";
        } else if (id == 3269) {
            name = "Slayer key";
            stackable = true;
        } else if (id == 30029) {
            name = "Molten key";
            stackable = true;
            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == 3325) {
            notelink = -1;
            noteModel = -1;
        } else if (id == 28013) {
            name = "Veteran partyhat";
            ioptions = new String[]{null, "Wear", null, null, "Destroy"};
        } else if (id == 28014) {
            name = "Veteran halloween mask";
            ioptions = new String[]{null, "Wear", null, null, "Destroy"};
        } else if (id == 28015) {
            name = "Veteran santa hat";
            ioptions = new String[]{null, "Wear", null, null, "Destroy"};
        } else if (id == 14479) {
            name = "Beginner weapon pack";
            ioptions = new String[]{"Open", null, null, null, "Destroy"};
        } else if (id == 14486) {
            name = "Beginner dragon claws";
            ioptions = new String[]{null, "Wield", null, null, "Destroy"};
        } else if (id == 14487) {
            name = "Beginner AGS";
            ioptions = new String[]{null, "Wield", null, null, "Destroy"};
        } else if (id == 14488) {
            name = "Beginner chainmace";
            ioptions = new String[]{null, "Wield", null, null, "Destroy"};
        } else if (id == 14489) {
            name = "Beginner craw's bow";
            ioptions = new String[]{null, "Wield", null, null, "Destroy"};
        } else if (id == 28663) {
            name = "Zriawk pet";
        } else if (id == 786) {
            name = "Gambler scroll";
        } else if (id == 24937) {
            name = "Fawkes pet";
        } else if (id == 24938) {
            name = "Void knight gloves";
        } else if (id == 24939) {
            name = "Void ranger helm";
        } else if (id == 24940) {
            name = "Void mage helm";
        } else if (id == 24941) {
            name = "Void melee helm";
        } else if (id == 24942) {
            name = "Elite void robe";
        } else if (id == 24943) {
            name = "Elite void top";
        } else if (id == 24949) {
            name = "Dragon dagger(p++)";
            stackable = false;
        } else if (id == 24948) {
            name = "Abyssal tentacle";
            stackable = false;
        } else if (id == 24947) {
            name = "Spiked manacles";
        } else if (id == 24946) {
            name = "Fremennik kilt";
        } else if (id == 24945) {
            name = "Partyhat & specs";
        } else if (id == 24944) {
            name = "Granite maul";
        } else if (id == 15304) {
            name = "Ring of vigour";
        } else if (id == 32131) {
            name = "Bandos chestplate (g)";
        } else if (id == 32133) {
            name = "Bandos tassets (g)";
        } else if (id == 26502) {
            name = "Enchanted armadyl helmet";
        } else if (id == 26503) {
            name = "Enchanted armadyl chestplate";
        } else if (id == 26504) {
            name = "Enchanted armadyl chainskirt";
        } else if (id == 27004) {
            name = "Blood money pet";
        } else if (id == 27005) {
            name = "Ring of elysian";
        } else if (id == 27006) {
            name = "Toxic staff of the dead (i)";
        } else if (id == 27000) {
            name = "Kerberos pet";
        } else if (id == 27001) {
            name = "Skorpios pet";
        } else if (id == 27002) {
            name = "Arachne pet";
        } else if (id == 27003) {
            name = "Artio pet";
        } else if (id == 22517) {
            name = "Saeldor shard";
        } else if (id == 25866) {
            name = "Bow of faerdhinen (c)";
        } else if (id == 25887) {
            name = "Bow of faerdhinen";
        } else if (id == 25881) {
            name = "Blade of saeldor (t)";
        } else if (id == 25916) {
            name = "Dragon hunter crossbow (t)";
        } else if (id == 25936) {
            name = "Pharaoh's hilt";
        } else if (id == 24951) {
            name = "Lime partyhat";
        } else if (id == 24952) {
            name = "Orange partyhat";
        } else if (id == 24953) {
            name = "White h'ween mask";
        } else if (id == 24954) {
            name = "Purple h'ween mask";
        } else if (id == 24955) {
            name = "Lime green h'ween mask";
        } else if (id == 24956 || id == 24958) {
            name = "Enchanted maul";
        } else if (id == 28957) {
            name = "Sanguine twisted bow";
        } else if (id == 24984) {
            name = "Enchanted faceguard";
        } else if (id == 24985) {
            name = "Ancient warrior clamp";
        } else if (id == 24986) {
            name = "Ancient king black dragon pet";
        } else if (id == 24987) {
            name = "Ancient chaos elemental pet";
        } else if (id == 24988) {
            name = "Ancient barrelchest pet";
        } else if (id == 24989) {
            name = "Dark ancient emblem";
        } else if (id == 24990) {
            name = "Dark ancient totem";
        } else if (id == 24991) {
            name = "Dark ancient statuette";
        } else if (id == 24992) {
            name = "Dark ancient medallion";
        } else if (id == 24993) {
            name = "Dark ancient effigy";
        } else if (id == 24994) {
            name = "Dark ancient relic";
        } else if (id == 24995) {
            name = "Ancient vesta's longsword";
        } else if (id == 24996) {
            name = "Ancient statius's warhammer";
        } else if (id == 24999) {
            name = "Blood money casket (5-50k)";
            ioptions = new String[]{"Open", null, null, null, "Drop"};
        } else if (id == 28000) {
            name = "Blood firebird pet";
        } else if (id == 28001) {
            name = "Shadow mace";
        } else if (id == 28002) {
            name = "Shadow great helm";
        } else if (id == 28003) {
            name = "Shadow hauberk";
        } else if (id == 28004) {
            name = "Shadow plateskirt";
        } else if (id == 28005) {
            name = "Shadow inquisitor ornament kit";
        } else if (id == 28006) {
            name = "Inquisitor's mace ornament kit";
        } else if (id == 29000) {
            name = "Viggora's chainmace (c)";
        } else if (id == 29001) {
            name = "Craw's bow (c)";
        } else if (id == 29002) {
            name = "Thammaron's sceptre (c)";
        } else if (id == 30180) {
            name = "Pegasian boots (or)";
        } else if (id == 30182) {
            name = "Eternal boots (or)";
        } else if (id == 24780) {
            name = "Amulet of blood fury";
        } else if (id == 25731) {
            name = "Holy sanguinesti staff";
        } else if (id == 25734) {//s23update
            name = "Holy ghrazi rapier";
        } else if (id == 23214) {//s23update
            name = "Holy mending rapier";
        } else if (id == 25736) {
            name = "Holy scythe of vitur";
        } else if (id == 25739) {
            name = "Sanguine scythe of vitur";
        } else if (id == 25753) {
            name = "99 lamp";
        } else if (id == 30306) {
            name = "Bond's Casket (Event)";
        } else if (id == 17000) {
            name = GameConstants.SERVER_NAME + " coins";
            countco = new int[]{2, 3, 4, 5, 25, 100, 250, 1000, 10000, 0};
            countobj = new int[]{17001, 17002, 17003, 17004, 17005, 17006, 17007, 17008, 17009, 0};
            stackable = true;
        } else if (id == 23490) {
            name = "Larran's key tier I";
            ioptions = new String[]{null, null, null, null, "Drop"};
            countco = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            countobj = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        } else if (id == 14900) {
            name = "Larran's key tier II";
            stackable = true;
            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == 14901) {
            name = "Larran's key tier III";
            stackable = true;
            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == 25902 || id == 25907 || id == 24445 || id == 25913) {
            name = "Twisted slayer helmet (i)";
            ioptions = new String[]{null, "Wear", null, null, "Drop"};
        } else if (id == 30219) {

        } else if (id == 12791) {
            ioptions = new String[]{"Open", null, "Quick-Fill", "Empty", "Drop"};
        } else if (id == 14525) {
            name = "Mystery chest";
        } else if (id == 20238) {
            name = "Imbuement scroll";
            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == BIG_CHEST) {
            name = "Blood money chest";
        } else if (id == 7956) {
            name = "Small blood money casket";
        } else if (id == 13302) {
            name = "Wilderness key";
            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == 12646) {
            name = "Niffler pet";
        } else if (id == ZIFFLER) {
            name = "Ziffler";
        } else if (id == 20693) {
            name = "Fawkes pet";
        } else if (id == 29312) {//updateo
            name = "Zaweks pet";
        } else if (id == 29109) {
            name = "Sanguinesti Kit";
        } else if (id == 20391) {
            name = "Raid rare ticket";
            stackable = true;
        } else if (id == 23912) {//updateo
            name = "Mending life bird";
        } else if (id == 29007) {
            name = "Enchanted gloves";
            //startdzone3
        } else if (id == TORVA_FULL_HELM) {
            name = "Torva helm";
        } else if (id == TORVA_PLATEBODY) {
            name = "Torva Platebody";
        } else if (id == TORVA_PLATELEGS) {
            name = "Torva Platelegs";
        } else if (id == 26235) {
            name = "Zaryte vambraces";
        } else if (id == 26370) {
            name = "Ancient hilt";
        } else if (id == 26372) {
            name = "Nihil horn";
        } else if (id == 26231) {
            name = "Nihil shards";
            stackable = true;
        } else if (id == 26348) {
            name = "Nexling";
        } else if (id == MASORI_MASK) {
            name = "Masori mask";
        } else if (id == MASORI_MASK_F) {
            name = "Masori mask f";
        } else if (id == ELIDINIS_WARD) {
            name = "Elidinis ward";
        } else if (id == ELIDINIS_WARD_F) {
            name = "Elidinis ward f";
        } else if (id == ELIDINIS_WARD_OR) {
            name = "Elidinis ward or";
        } else if (id == TUMEKENS_SHADOW) {
            name = "Tumekens shadow";
        } else if (id == OSMUMTENS_FANG) {
            name = "Osmumtens fang";
        } else if (id == 14063) {
            name = "Osmumtens fang (or)";
        } else if (id == LIGHTBEARER) {
            name = "Light bearer";
        } else if (id == MENAPHITE_ORNAMENT_KIT) {
            name = "Menaphite ornament kit";
        } else if (id == MASORI_BODY) {
            name = "Masori body";
        } else if (id == MASORI_BODY_F) {
            name = "Masori body f";
        } else if (id == MASORI_CHAPS) {
            name = "Masori chaps";
        } else if (id == MASORI_CHAPS_F) {
            name = "Masori chaps f";
            //enddzone3
        } else if (id == 30326) {
            name = "Enchanted helm";
            /**
             *   start of potions update
             */
        } else if (id == 30415) {
            name = "Divine Sigil";
        } else if (id == 30416) {
            name = "Divine Spirit Shield";
        } else if (id == 29111) {
            name = "Bottomless divine super comba";
        } else if (id == 29112) {
            name = "Bottomless divine super range";
        } else if (id == 29113) {
            name = "Bottomless saradomin brew";
        } else if (id == 29114) {
            name = "Bottomless sanfew";
        } else if (id == 29115) {
            name = "Bottomless overload";
        } else if (id == 30302) {
            name = "$75 Bond";
        } else if (id == 30303) {
            name = "Grandmaster Gloves";
        } else if (id == 30304) {
            name = "Grandmaster's Heart";
        } else if (id == 30301) {
            name = "Ethereal Scythe";
        } else if (id == 30300) {
            name = "Grandmaster Cape";
        } else if (id == 30281) {
            name = "Grandmaster Bow";
        } else if (id == 30279) {
            name = "Grandmaster Sceptre";
        } else if (id == 30278) {
            name = "Grandmaster Mace";
        } else if (id == 30277) {
            name = "Grandmaster Boots";
        } else if (id == 30276) {
            name = "Grandmaster Helm";
        } else if (id == 26922) {
            name = "Enchanted salazar locket";
        } else if (id == 20400) {
            name = "Bond's Casket";
        } else if (id == HAZY_WINGS) {
            name = "Hazy Wings";
        } else if (id == DEMONIC_WINGS) {
            name = "Demonic Wings";
        } else if (id == SHOULDER_BIRD) {
            name = "Shoulder Bird";
        } else if (id == DRAGON_COMPANION) {
            name = "Dragon Companion";
        } else if (id == BLOOD_TORVA_HELM) {
            name = "Chaos Torva Full Helm";
        } else if (id == BLOOD_TORVA_PLATEBODY) {
            name = "Chaos Torva Platebody";
        } else if (id == BLOOD_TORVA_PLATELEGS) {
            name = "Chaos Torva Platelegs";
        } else if (id == BLOOD_ANCESTRAL_HELM) {
            name = "Chaos Ancestral Hat";
        } else if (id == BLOOD_ANCESTRAL_PLATEBODY) {
            name = "Chaos Ancestral robe";
        } else if (id == BLOOD_ANCESTRAL_PLATELEGS) {
            name = "Chaos Ancestral robelegs";
        } else if (id == BLOOD_MASORI_HELM) {
            name = "Chaos Masori coif";
        } else if (id == BLOOD_MASORI_PLATEBODY) {
            name = "Chaos Masori body";
        } else if (id == BLOOD_MASORI_PLATELEGS) {
            name = "Chaos Masori chaps";
        } else if (id == 26170) {
            name = "DarkLord helm";
        } else if (id == 26172) {
            name = "DarkLord body";
        } else if (id == 26180) {
            name = "DarkLord platelegs";
        } else if (id == 619) {
            name = "Vote ticket";
            stackable = true;
            ioptions = new String[]{"Convert to Points", null, null, null, "Drop"};
        } else if (id == 13188) {
            name = "Dragon claws (or)";
        } else if (id == 28007) {
            name = "Ethereal partyhat";
        } else if (id == 28008) {
            name = "Ethereal halloween mask";
        } else if (id == 28009) {
            name = "Ethereal santa hat";
        } else if (id == 30074) {
            name = "Elvarg d'hide coif";
        } else if (id == 30077) {
            name = "Elvarg d'hide body";
        } else if (id == ANCIENT_GODSWORD) {
            name = "Ancient godsword";
        } else if (id == 30080) {
            name = "Elvarg d'hide chaps";
        } else if (id == 30183) {
            name = "Twisted bow (i)";
        } else if (id == 30175) {
            name = "Ancestral hat (i)";
        } else if (id == 30177) {
            name = "Ancestral robe top (i)";
        } else if (id == 30179) {
            name = "Ancestral robe bottom (i)";
        } else if (id == 30038) {
            name = "Primordial boots (or)";
        } else if (id == 23650) {
            notelink = -1;
            noteModel = -1;
            name = "Rune pouch (i)";
            ioptions = new String[]{"Open", null, null, "Empty", "Destroy"};
        } else if (id == 4447) {
            name = "Double drops lamp";
        } else if (id == 6199) {
            ioptions = new String[]{"Quick-open", null, "Open", null, null};
        } else if (id == 16451) {
            name = "Weapon Mystery Box";
            stackable = false;
        } else if (id == 16452) {
            name = "Armour mystery box";
            stackable = false;
        } else if (id == 30185) {
            name = "Donator mystery box";
        } else if (id == 30026) {
            name = "Molten mystery box";
            stackable = false;
        } else if (id == 30186) {
            name = "H'ween mystery box";
            stackable = false;
        } else if (id == 30242) {
            name = "Winter item casket";
        } else if (id == 16454) {
            name = "Legendary mystery box";
            stackable = false;
        } else if (id == 16455) {
            name = "Grand chest";
            stackable = false;
        } else if (id == 16456) {
            name = "Pet mystery box";
            stackable = false;
        } else if (id == 16458) {
            name = "Epic pet mystery box";//that rigght epic pet ? yeah but on server annouce as elite pet box//that new change i made oh perfect
            stackable = false;
        } else if (id == 16459) {
            name = "Raids mystery box";
            stackable = false;
        } else if (id == 16460) {
            name = "Zenyte mystery box";
            stackable = false;
        } else if (id == 16461) {
            name = "Starter box";
            stackable = false;
        } else if (id == STARTER_BOX_DARK_LORD) {
            name = "Darklord starter weapon box";
            stackable = false;
        } else if (id == 16462) {
            name = "Clan box";
            stackable = false;
        } else if (id == 6722) {
            name = "Zombies champion pet";
        } else if (id == 2866) {
            name = "Earth arrows";
        } else if (id == 4160) {
            name = "Fire arrows";
        } else if (id == 7806) {
            name = "Ancient warrior sword";
        } else if (id == 7807) {
            name = "Ancient warrior axe";
        } else if (id == 7808) {
            name = "Ancient warrior maul";
        } else if (id == 24983) {
            name = "Ancient warrior sword (c)";
        } else if (id == 24981) {
            name = "Ancient warrior axe (c)";
        } else if (id == 24982) {
            name = "Ancient warrior maul (c)";
        } else if (id == 2944) {
            name = "Key of boxes";
        } else if (id == 2955) {//update19
            name = "Grand key";
        } else if (id == 12773) {
            name = "Lava whip";
        } else if (id == 12774) {
            name = "Frost whip";
        } else if (id == 10586) {
            ioptions = new String[]{null, null, null, null, "Drop"};
            name = "Genie pet";
        } else if (id == 12102) {
            name = "Grim Reaper pet";
        } else if (id == 12081) {
            name = "Elemental bow";
        } else if (id == 30094) {
            name = "White Orb (Recolor EB)";
        } else if (id == 30095) {
            name = "Donation bow (White)";
        } else if (id == 4067) {
            name = "Donator ticket";
            stackable = true;
        } else if (id == 13190) {
            name = "5$ bond";
        } else if (id == 8013) {
            name = "Home teleport";
        } else if (id == 964) {
            name = "Vengeance";
        } else if (id == 18335) {
            stackable = false;
            name = "Lava partyhat";
        } else if (id == 16278) {
            stackable = false;
            name = "$10 bond";
        } else if (id == 16263) {
            stackable = false;
            name = "$20 bond";
        } else if (id == 16264) {
            stackable = false;
            name = "$40 bond";
        } else if (id == 16265) {
            stackable = false;
            name = "$50 bond";
        } else if (id == 16266) {
            stackable = false;
            name = "$100 bond";
        } else if (id == 16012) {
            stackable = false;
            name = "Baby Dark Beast pet";
            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == 16024) {
            stackable = false;
            name = "Baby Abyssal demon pet";
            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == 15331) {
            stackable = false;
            name = "Ring of confliction";
            ioptions = new String[]{null, "Wear", null, null, "Drop"};
        } else if (id == 16167) {
            stackable = false;
            name = "Alchemist's ring";
            ioptions = new String[]{null, "Wear", null, null, "Drop"};
        } else if (id == 16168) {
            stackable = false;
            name = "Deadeye's ring";
            ioptions = new String[]{null, "Wear", null, null, "Drop"};
        } else if (id == 16169) {
            stackable = false;
            name = "Ring of perfection";
            ioptions = new String[]{null, "Wear", null, null, "Drop"};
        } else if (id == 13215) {
            name = "Bloody Token";
            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == 30235) {
            name = "H'ween token";
            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == 32236) {
            name = "Winter token";
            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == 30050) {
            name = "OS-GP token";
            ioptions = new String[]{null, null, null, null, "Drop"};
        } else if (id == 30297) {
            name = "Enchanted boots";
        } else if (id == 27644) {
            name = "Enchanted pendant";
        } else if (id == 28643) {
            name = "Fenrir greyback Jr. pet";
        } else if (id == 28642) {
            name = "Fluffy Jr. pet";
        } else if (id == 28641) {
            name = "Talonhawk crossbow";
        } else if (id == 28640) {
            name = "Elder wand stick";
        } else if (id == 28639) {
            name = "Elder wand handle";
        } else if (id == 30181 || id == 30184) {
            name = "Elder wand";
        } else if (id == 30253) {
            name = "Cloak of invisibility";
        } else if (id == 30252) {
            name = "Marvolo Gaunts Ring";
        } else if (id == 30251) {
            name = "Tom Riddle's Diary";
        } else if (id == 30250) {
            name = "Nagini";
        } else if (id == 10858) {
            name = "Sword of gryffindor";
        } else if (id == 30338) {
            name = "Male centaur pet";
        } else if (id == 30340) {
            name = "Female centaur pet";
        } else if (id == 30342) {
            name = "Dementor pet";
        } else if (id == 21291) {
            name = "Jal-nib-rek pet";
        } else if (id == 8788) {
            name = "Corrupting stone";
        } else if (id == 30048) {
            name = "Corrupted gauntlets";
        } else if (id == 32102) {
            name = "Blood Reaper pet";
        } else if (id == 23757) {
            name = "Yougnleff pet";
        } else if (id == 23759) {
            name = "Corrupted yougnleff pet";
        } else if (id == 30016) {
            name = "Founder imp pet";
        } else if (id == 30018) {
            name = "Corrupted nechryarch pet";
        } else if (id == 30033) {
            name = "Mini necromancer pet";
        } else if (id == 30044) {
            name = "Jaltok-jad pet";
        } else if (id == 13225) {
            name = "Tzrek-jad pet";
        } else if (id == 30131) {
            name = "Baby lava dragon pet";
        } else if (id == 16173) {
            name = "Jawa pet";
        } else if (id == 16172) {
            name = "Baby aragog pet";
        } else if (id == 16020) {
            name = "Dharok pet";
        } else if (id == 22319) {
            name = "TzRek-Zuk pet";
        } else if (id == 24491) {
            name = "Little nightmare pet";
        } else if (id == 23759) {
            name = "Corrupted youngllef pet";
        } else if (id == 30122) {
            name = "Corrupt totem base";
        } else if (id == 30123) {
            name = "Corrupt totem middle";
        } else if (id == 30124) {
            name = "Corrupt totem top";
        } else if (id == 30125) {
            name = "Corrupt totem";
        } else if (id == 16005) {
            name = "Baby Squirt pet";
            stackable = false;
        } else if (id == 7999) {
            name = "Pet paint (black)";
        } else if (id == 8000) {
            name = "Pet paint (white)";
        } else if (id == 15300) {
            stackable = false;
            name = "Recover special (4)";
        } else if (id == 15301) {
            stackable = false;
            name = "Recover special (3)";
        } else if (id == 15302) {
            stackable = false;
            name = "Recover special (2)";
        } else if (id == 15303) {
            stackable = false;
            name = "Recover special (1)";
        } else if (id == 23818) {
            name = "Barrelchest pet";
            ioptions = new String[]{null, null, null, null, "Drop"};
            stackable = false;
        } else if (id == 30049) {
            name = "Magma blowpipe";
        } else if (id == 16171) {
            name = "Wampa pet";
            ioptions = new String[]{null, null, null, null, "Drop"};
            stackable = false;
        } else if (id == 16013) {
            name = "Pet kree'arra (white)";
            stackable = false;
            ioptions = new String[]{null, null, null, "Wipe-off paint", null};
        } else if (id == 16014) {
            name = "Pet zilyana (white)";
            stackable = false;
            ioptions = new String[]{null, null, null, "Wipe-off paint", null};
        } else if (id == 29102) {
            name = "Scythe of vitur kit";
        } else if (id == 29103) {
            name = "Twisted bow kit";
        } else if (id == 16015) {
            name = "Pet general graardor (black)";
            stackable = false;
            ioptions = new String[]{null, null, null, "Wipe-off paint", null};
        } else if (id == 16016) {
            name = "Pet k'ril tsutsaroth (black)";
            stackable = false;
            ioptions = new String[]{null, null, null, "Wipe-off paint", null};
        } else if (id == 12873 || id == 12875 || id == 12877 || id == 12879 || id == 12881 || id == 12883) {
            ioptions = new String[5];
            ioptions[0] = "Open";
        } else if (id == 14500) {
            name = "Rune pouch (i) (broken)";
            stackable = false;
            ioptions = new String[]{null, null, null, null, "Destroy"};
        } else if (id == 14501) {
            name = "Amulet of fury (or) (broken)";
            stackable = false;
            ioptions = new String[]{null, null, null, null, "Destroy"};
        } else if (id == 14502) {
            name = "Occult necklace (or) (broken)";
            stackable = false;
            ioptions = new String[]{null, null, null, null, "Destroy"};
        } else if (id == 14503) {
            name = "Amulet of torture (or) (broken)";
            stackable = false;
            ioptions = new String[]{null, null, null, null, "Destroy"};
        } else if (id == 14504) {
            name = "Necklace of anguish (or) (broken)";
            stackable = false;
            ioptions = new String[]{null, null, null, null, "Destroy"};
        } else if (id == 14505) {
            name = "Tormented bracelet (or) (broken)";
            stackable = false;
            ioptions = new String[]{null, null, null, null, "Destroy"};
        } else if (id == 14506) {
            name = "Dragon defender (t) (broken)";
        } else if (id == 14507) {
            name = "Dragon boots (g) (broken)";
            stackable = false;
            ioptions = new String[]{null, null, null, null, "Destroy"};
        } else if (id == ELDER_WAND_HANDLE) {
            name = "Elder wand handle";
        } else if (id == ELDER_WAND_STICK) {
            name = "Elder wand stick";
        } else if (id == ELDER_WAND || id == ELDER_WAND_RAIDS) {
            name = "Elder wand";
        } else if (id == TALONHAWK_CROSSBOW) {
            name = "Talonhawk crossbow";
        } else if (id == SALAZAR_SLYTHERINS_LOCKET) {
            name = "Salazar slytherins locket";
        } else if (id == CORRUPTED_BOOTS) {
            name = "Corrupted boots";
        } else if (id == FENRIR_GREYBACK_JR) {
            name = "Fenrir greyback Jr pet";
        } else if (id == FLUFFY_JR) {
            name = "Fluffy Jr pet";
        } else if (id == LUMBERJACK_HAT) {
            name = "Lumberjacket hat";
        } else if (id == LUMBERJACK_TOP) {
            name = "Lumberjacket top";
        } else if (id == LUMBERJACK_LEGS) {
            name = "Lumberjacket legs";
        } else if (id == LUMBERJACK_BOOTS) {
            name = "Lumberjacket boots";
        } else if (id == SUMMER_SOAKER) {
            name = "Summer Soaker";
        } else if (id == SUMMER_AMULET) {
            name = "Summer Amulet";
        } else if (id == ANGLER_HAT) {
            name = "Angler hat";
        } else if (id == ANGLER_TOP) {
            name = "Angler top";
        } else if (id == ANGLER_WADERS) {
            name = "Angler waders";
        } else if (id == ANGLER_BOOTS) {
            name = "Angler boots";
        } else if (id == PROSPECTOR_HELMET) {
            name = "Prospector hat";
        } else if (id == PROSPECTOR_JACKET) {
            name = "Prospector jaket";
        } else if (id == PROSPECTOR_LEGS) {
            name = "Prospector legs";
        } else if (id == PROSPECTOR_BOOTS) {
            name = "Prospector boots";
            //enddzone3
        } else if (id == CENTAUR_MALE) {
            name = "Centaur male pet";
        } else if (id == CENTAUR_FEMALE) {
            name = "Centaur female pet";
        } else if (id == DEMENTOR_PET) {
            name = "Dementor pet";
        } else if (id == FOUNDER_IMP) {
            name = "Founder imp pet";
        } else if (id == PET_CORRUPTED_NECHRYARCH) {
            name = "Corrupted nechryarch pet";
        } else if (id == MINI_NECROMANCER) {
            name = "Mini necromancer";
        } else if (id == JALTOK_JAD) {
            name = "Jaltok-jad";
        } else if (id == BABY_LAVA_DRAGON) {
            name = "Baby lava dragon";
        } else if (id == JAWA_PET) {
            name = "Jawa pet";
        } else if (id == BABY_ARAGOG) {
            name = "Baby aragog pet";
        } else if (id == WAMPA) {
            name = "Wampa pet";
        } else if (id == DHAROK_PET) {
            name = "Dharok pet";
        } else if (id == MYSTERY_CHEST) {
            name = "Enchanted grand chest";
        } else if (id == LAVA_DHIDE_COIF) {
            name = "Elvarg d'hide coif";
        } else if (id == LAVA_DHIDE_BODY) {
            name = "Elvarg d'hide body";
        } else if (id == LAVA_DHIDE_CHAPS) {
            name = "Elvarg d'hide chaps";
        } else if (id == TWISTED_BOW_I) {
            name = "Twisted bow (i)";
        } else if (id == ANCESTRAL_HAT_I) {
            name = "Ancestral hat (i)";
        } else if (id == ANCESTRAL_ROBE_TOP_I) {
            name = "Ancestral robe top (i)";
        } else if (id == ANCESTRAL_ROBE_BOTTOM_I) {
            name = "Ancestral robe bottom (i)";
        } else if (id == SWORD_OF_GRYFFINDOR) {
            name = "Sword of gryffindor";
            ioptions = new String[]{null, "Wield", null, null, "Drop"};
        } else if (id == MENDING_STONE) {//dzone3
            name = "Mending stone";
        } else if (id == 30103) {//dzone3
            name = "Mending sword of gryffindor";
            ioptions = new String[]{null, "Wield", null, null, "Drop"};
        } else if (id == VETERAN_PARTYHAT) {
            name = "Veteran partyhat";
        } else if (id == VETERAN_HWEEN_MASK) {
            name = "Veteran h'ween mask";
        } else if (id == VETERAN_SANTA_HAT) {
            name = "Veteran santa hat";
        } else if (id == MAGMA_BLOWPIPE) {
            name = "Magma blowpipe";
            ioptions = new String[]{null, "Wield", null, null, "Drop"};
        } else if (id == ELDER_MAUL_21205 || id == ItemIdentifiers.ARMADYL_GODSWORD_OR || id == ItemIdentifiers.BANDOS_GODSWORD_OR || id == ItemIdentifiers.SARADOMIN_GODSWORD_OR || id == ItemIdentifiers.ZAMORAK_GODSWORD_OR || id == ItemIdentifiers.GRANITE_MAUL_12848) {
            ioptions = new String[]{null, "Wield", null, null, "Drop"};
        } else if (id == ItemIdentifiers.ATTACKER_ICON || id == ItemIdentifiers.COLLECTOR_ICON || id == ItemIdentifiers.DEFENDER_ICON || id == ItemIdentifiers.HEALER_ICON || id == ItemIdentifiers.AMULET_OF_FURY_OR || id == ItemIdentifiers.OCCULT_NECKLACE_OR || id == ItemIdentifiers.NECKLACE_OF_ANGUISH_OR || id == ItemIdentifiers.AMULET_OF_TORTURE_OR || id == ItemIdentifiers.BERSERKER_NECKLACE_OR || id == ItemIdentifiers.TORMENTED_BRACELET_OR || id == ItemIdentifiers.DRAGON_DEFENDER_T || id == ItemIdentifiers.DRAGON_BOOTS_G) {
            ioptions = new String[]{null, "Wear", null, null, "Destroy"};
            stackable = false;
        }

    }

    private void decodeValues(RSBuffer stream, int opcode) {
        if (opcode == 1) {
            inventoryModel = stream.readUShort();
        } else if (opcode == 2) {
            name = stream.readJagexString();
        } else if (opcode == 4) {
            zoom2d = stream.readUShort();
        } else if (opcode == 5) {
            xan2d = stream.readUShort();
        } else if (opcode == 6) {
            yan2d = stream.readUShort();
        } else if (opcode == 7) {
            xof2d = stream.readUShort();
            if (xof2d > 32767) {
                xof2d -= 65536;
            }
        } else if (opcode == 8) {
            yof2d = stream.readUShort();
            if (yof2d > 32767) {
                yof2d -= 65536;
            }
        } else if (opcode == 9) {
            unknown1 = stream.readString();
        } else if (opcode == 11) {
            stackable = true;
        } else if (opcode == 12) {
            cost = stream.readInt();
        } else if (opcode == 13) {
            wearPos1 = stream.readByte();
        } else if (opcode == 14) {
            wearPos2 = stream.readByte();
        } else if (opcode == 16) {
            members = true;
        } else if (opcode == 23) {
            maleModel0 = stream.readUShort();
            maleModel2 = stream.readUByte();
        } else if (opcode == 24) {
            maleModel1 = stream.readUShort();
        } else if (opcode == 25) {
            femaleModel0 = stream.readUShort();
            femaleModel2 = stream.readUByte();
        } else if (opcode == 26) {
            femaleModel1 = stream.readUShort();
        } else if (opcode == 27) {
            wearPos3 = stream.readByte();
        } else if (opcode >= 30 && opcode < 35) {
            options[opcode - 30] = stream.readString();
            if (options[opcode - 30].equalsIgnoreCase("Hidden")) {
                options[opcode - 30] = null;
            }
        } else if (opcode >= 35 && opcode < 40) {
            ioptions[opcode - 35] = stream.readString();
        } else if (opcode == 40) {
            int var5 = stream.readUByte();
            recol_s = new short[var5];
            recol_d = new short[var5];

            for (int var4 = 0; var4 < var5; ++var4) {
                recol_s[var4] = (short) stream.readUShort();
                recol_d[var4] = (short) stream.readUShort();
            }

        } else if (opcode == 41) {
            int var5 = stream.readUByte();
            retex_s = new short[var5];
            retex_d = new short[var5];

            for (int var4 = 0; var4 < var5; ++var4) {
                retex_s[var4] = (short) stream.readUShort();
                retex_d[var4] = (short) stream.readUShort();
            }

        } else if (opcode == 42) {
            shiftClickDropType = stream.readByte();
        } else if (opcode == 65) {
            grandexchange = true;
        } else if (opcode == 75) {
            weight = stream.readShort();
        } else if (opcode == 78) {
            maleModel2 = stream.readUShort();
        } else if (opcode == 79) {
            femaleModel2 = stream.readUShort();
        } else if (opcode == 90) {
            manhead = stream.readUShort();
        } else if (opcode == 91) {
            womanhead = stream.readUShort();
        } else if (opcode == 92) {
            manhead2 = stream.readUShort();
        } else if (opcode == 93) {
            womanhead2 = stream.readUShort();
        } else if (opcode == 94) {
            category = stream.readUShort();
        } else if (opcode == 95) {
            zan2d = stream.readUShort();
        } else if (opcode == 97) {
            notelink = stream.readUShort();
        } else if (opcode == 98) {
            noteModel = stream.readUShort();
        } else if (opcode >= 100 && opcode < 110) {
            if (countobj == null) {
                countobj = new int[10];
                countco = new int[10];
            }

            countobj[opcode - 100] = stream.readUShort();
            countco[opcode - 100] = stream.readUShort();
        } else if (opcode == 110) {
            resizex = stream.readUShort();
        } else if (opcode == 111) {
            resizey = stream.readUShort();
        } else if (opcode == 112) {
            resizez = stream.readUShort();
        } else if (opcode == 113) {
            ambient = stream.readByte();
        } else if (opcode == 114) {
            contrast = stream.readByte();
        } else if (opcode == 115) {
            team = stream.readUByte();
        } else if (opcode == 139) {
            op139 = stream.readUShort();
        } else if (opcode == 140) {
            op140 = stream.readUShort();
        } else if (opcode == 148) {
            placeheld = stream.readUShort();
        } else if (opcode == 149) {
            pheld14401 = stream.readUShort();
        } else if (opcode == 249) {
            int length = stream.readUByte();

            params = new HashMap<>(length);

            for (int i = 0; i < length; i++) {
                boolean isString = stream.readUByte() == 1;
                int key = stream.read24BitInt();
                Object value;

                if (isString) {
                    value = stream.readString();
                } else {
                    value = stream.readInt();
                }

                params.put(key, value);
            }
        }
    }

    void postDecode(int id) {
        if (id == 6808) {
            name = "Scroll of Imbuement";
        }
        // bm = new BloodMoneyPrices(id, this.getKeptOnDeathValue());
    }

    public int getKeptOnDeathValue() {
        return cost;
    }

    public int highAlchValue() {
        return (int) (cost * 0.65);
    }

    public int getWeight() {
        return weight;
    }

    public static int method32(int var0) {
        --var0;
        var0 |= var0 >>> 1;
        var0 |= var0 >>> 2;
        var0 |= var0 >>> 4;
        var0 |= var0 >>> 8;
        var0 |= var0 >>> 16;
        return var0 + 1;
    }

    public Map<Integer, Object> clientScriptData;

    public boolean stackable() {
        return stackable || noteModel > 0 || id == 13215 || id == 32236 || id == 30050 || id == 30235;
    }

    public boolean noted() {
        return noteModel > 0;
    }

    @Override
    public String toString() {
        return "ItemDefinition{" +
            "resizey=" + resizey +
            ", xan2d=" + xan2d +
            ", cost=" + cost +
            ", inventoryModel=" + inventoryModel +
            ", resizez=" + resizez +
            ", recol_s=" + Arrays.toString(recol_s) +
            ", recol_d=" + Arrays.toString(recol_d) +
            ", name='" + name + '\'' +
            ", zoom2d=" + zoom2d +
            ", yan2d=" + yan2d +
            ", zan2d=" + zan2d +
            ", yof2d=" + yof2d +
            ", stackable=" + stackable +
            ", countco=" + Arrays.toString(countco) +
            ", members=" + members +
            ", options=" + Arrays.toString(options) +
            ", ioptions=" + Arrays.toString(ioptions) +
            ", maleModel0=" + maleModel0 +
            ", maleModel1=" + maleModel1 +
            ", retex_s=" + Arrays.toString(retex_s) +
            ", retex_d=" + Arrays.toString(retex_d) +
            ", femaleModel1=" + femaleModel1 +
            ", maleModel2=" + maleModel2 +
            ", xof2d=" + xof2d +
            ", manhead=" + manhead +
            ", manhead2=" + manhead2 +
            ", womanhead=" + womanhead +
            ", womanhead2=" + womanhead2 +
            ", countobj=" + Arrays.toString(countobj) +
            ", femaleModel2=" + femaleModel2 +
            ", notelink=" + notelink +
            ", femaleModel0=" + femaleModel0 +
            ", resizex=" + resizex +
            ", noteModel=" + noteModel +
            ", ambient=" + ambient +
            ", contrast=" + contrast +
            ", team=" + team +
            ", grandexchange=" + grandexchange +
            ", unprotectable=" + unprotectable +
            ", dummyitem=" + dummyitem +
            ", placeheld=" + placeheld +
            ", pheld14401=" + pheld14401 +
            ", shiftClickDropType=" + shiftClickDropType +
            ", op139=" + op139 +
            ", op140=" + op140 +
            ", id=" + id +
            ", clientScriptData=" + clientScriptData +
            '}';
    }
}
