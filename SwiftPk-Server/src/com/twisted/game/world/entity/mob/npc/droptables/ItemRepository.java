package com.twisted.game.world.entity.mob.npc.droptables;

import com.twisted.fs.ItemDefinition;
import com.twisted.game.world.items.Item;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ItemRepository {
    public static HashMap<String, Integer> itemIds = new HashMap<>();
    public static HashMap<Integer, String> itemNames = new HashMap<>();

    public static List<Integer> customItemds = new ArrayList<>();
    public static String getItemName(int id) {
        if (id <= 0) return "null";
        var def = ItemDefinition.cached.get(id);
        String itemName;
        if (def != null && def.noted()) {
            var item = new Item(def.id);
            var notedID = item.note().getId();
            itemName = "NOTED_" + item.unnote().name().replaceAll(" ", "_").replaceAll("[(]", "").replaceAll("[)]", "").replaceAll("[+(]", "_").toUpperCase();
            return itemNames.getOrDefault(notedID, itemName);
        } else return itemNames.getOrDefault(id, null);
    }
    public static int getItemId(String name) {
        if (name == null) return -1;
        if (name.contains("NOTED_")) {
            Integer cached = itemIds.get(name);
            if (cached != null) return cached;
            int unnotedId = getItemId(name.substring("NOTED_".length()));
            ItemDefinition def = getItemDefinition(unnotedId);
            if (def != null) {
                itemIds.put(name, def.notelink);
                return def.notelink;
            }
        }
        return itemIds.getOrDefault(name, -1);
    }

    private static ItemDefinition getItemDefinition(int unnotedId) {
        return ItemDefinition.cached.get(unnotedId);
    }
    public static void load() {
        List<Field> item = Arrays.stream(ItemIdentifiers.class.getFields()).filter(field -> Modifier.isPublic(field.getModifiers())).toList();
        List<Field> customItems = Arrays.stream(CustomItemIdentifiers.class.getFields()).filter(field -> Modifier.isPublic(field.getModifiers())).toList();
        item.forEach(it -> {
            try {
                itemIds.put(it.getName(), it.getInt(ItemIdentifiers.class));
                itemNames.put(it.getInt(ItemIdentifiers.class), it.getName());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        customItems.forEach(it -> {
            try {
                customItemds.add(it.getInt(CustomItemIdentifiers.class));
                itemIds.put(it.getName(), it.getInt(CustomItemIdentifiers.class));
                itemNames.put(it.getInt(CustomItemIdentifiers.class), it.getName());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println("Loaded " + itemIds.size() + " item names");
        System.out.println("Found " + itemIds.get("WINTER_TOKENS"));
    }
}
