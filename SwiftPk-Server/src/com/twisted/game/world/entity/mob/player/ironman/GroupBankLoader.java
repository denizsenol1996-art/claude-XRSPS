package com.twisted.game.world.entity.mob.player.ironman;

import com.twisted.game.world.items.Item;
import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @Author: Origin
 * @Date: 3/6/2024
 */
public class GroupBankLoader {
    Gson gson = new GsonBuilder().registerTypeAdapter(GroupIronman.class, new GroupIronmanDeserializer()).excludeFieldsWithoutExposeAnnotation().create();
    public static Long2ObjectMap<GroupIronman> groupIronmanMap = new Long2ObjectOpenHashMap<>();
    private static final Logger logger = LogManager.getLogger(GroupBankLoader.class);
    public void loadGroupBanks(File file) throws IOException {
        long start = System.currentTimeMillis();
        for (File files : Objects.requireNonNull(file.listFiles())) {
            if (files == null || !files.getName().endsWith(".json")) continue;
            try (FileReader reader = new FileReader(files)) {
                GroupIronman group = gson.fromJson(reader, new TypeToken<GroupIronman>() {}.getType());
                groupIronmanMap.put(group.getOwnerUID(), group);
                long elapsed = System.currentTimeMillis() - start;
                logger.info("Loaded Group Ironman Banks It Took {}ms.", elapsed);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class GroupIronmanDeserializer implements JsonDeserializer<GroupIronman> {
        @Override
        public GroupIronman deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            long ownerUID = jsonObject.get("ownerUID").getAsLong();
            Set<Long> memberUID = new HashSet<>();
            JsonElement memberUIDElement = jsonObject.get("memberUID");
            if (memberUIDElement != null && memberUIDElement.isJsonArray()) {
                JsonArray memberUIDArray = memberUIDElement.getAsJsonArray();
                if (!memberUIDArray.isEmpty()) {
                    for (JsonElement element : memberUIDArray) {
                        if (element == null) continue;
                        memberUID.add(element.getAsLong());
                    }
                }
            }
            JsonArray groupBankArray = jsonObject.getAsJsonArray("groupBank");
            GroupBank groupBank = new GroupBank();
            for (JsonElement element : groupBankArray) {
                JsonObject itemObject = element.getAsJsonObject();
                int id = itemObject.get("id").getAsInt();
                int amount = itemObject.get("amount").getAsInt();
                groupBank.container.add(new Item(id, amount));
            }
            boolean isHardcore = jsonObject.get("isHardcore").getAsBoolean();
            int groupLives = jsonObject.get("groupLives").getAsInt();
            return new GroupIronman(ownerUID, memberUID, groupBank, isHardcore, groupLives);
        }
    }
}

