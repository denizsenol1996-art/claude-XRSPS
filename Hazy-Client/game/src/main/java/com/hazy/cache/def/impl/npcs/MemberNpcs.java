package com.hazy.cache.def.impl.npcs;

import com.hazy.cache.def.NpcDefinition;
import com.hazy.util.NpcIdentifiers;

/**
 * @author Patrick van Elderen | July, 12, 2021, 14:02
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class MemberNpcs {

    public static void unpack(int id) {
        NpcDefinition definition = NpcDefinition.get(id);
        if(id == 2314) {

            definition.name = "Afk shop";

        }
        if(id == 3092) {

            definition.name = "PK shop";

        }
        if(id == 3001) {

            definition.name = "Raid shop";


        }
        if(id == 16000) {
            NpcDefinition.copy(definition, NpcIdentifiers.REVENANT_DARK_BEAST);
            definition.name = "Ancient revenant dark beast";
            definition.modelCustomColor4 = 235;
        }
        if(id == 16001) {
            NpcDefinition.copy(definition, NpcIdentifiers.REVENANT_ORK);
            definition.name = "Ancient revenant ork";
            definition.modelCustomColor4 = 235;
        }
        if(id == 16002) {
            NpcDefinition.copy(definition, NpcIdentifiers.REVENANT_CYCLOPS);
            definition.name = "Ancient revenant cyclops";
            definition.modelCustomColor4 = 235;
        }
        if(id == 16003) {
            NpcDefinition.copy(definition, NpcIdentifiers.REVENANT_DRAGON);
            definition.name = "Ancient revenant dragon";
            definition.modelCustomColor4 = 235;
        }
        if(id == 16004) {
            NpcDefinition.copy(definition, NpcIdentifiers.REVENANT_KNIGHT);
            definition.name = "Ancient revenant knight";
            definition.modelCustomColor4 = 235;
        }
        if(id == 16005) {
            NpcDefinition.copy(definition, NpcIdentifiers.BARRELCHEST_6342);
            definition.name = "Ancient barrelchest";
            definition.modelCustomColor4 = 235;
        }
        if(id == 16006) {
            NpcDefinition.copy(definition, NpcIdentifiers.KING_BLACK_DRAGON);
            definition.name = "Ancient king black dragon";
            definition.modelCustomColor4 = 235;
        }
        if(id == 16007) {
            NpcDefinition.copy(definition, NpcIdentifiers.CHAOS_ELEMENTAL);
            definition.name = "Ancient chaos elemental";
            definition.modelCustomColor4 = 235;
        }
    }
}
