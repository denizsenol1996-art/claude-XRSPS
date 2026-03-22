package com.twisted.game.world.entity.mob.player.ironman;

import com.twisted.game.world.items.Item;
import com.google.gson.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * @Author: Origin
 * @Date: 3/6/2024
 */
public final class GroupIronmanWriter {
    private static final String FILE_PATH = "./data/saves/group/";
    static Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(GroupIronman.class, new GroupIronmanSerializer()).create();

    public static synchronized void writeGroupIronman(GroupIronman group) {
        String fileName = FILE_PATH + group.getOwnerUID() + "_group.json";
        try (FileWriter writer = new FileWriter(fileName, !fileExists(fileName))) {
            gson.toJson(group, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean fileExists(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    public static class GroupIronmanSerializer implements JsonSerializer<GroupIronman> {
        @Override
        public JsonElement serialize(GroupIronman group, java.lang.reflect.Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("ownerUID", group.getOwnerUID());
            jsonObject.addProperty("memberUID", Arrays.toString(group.getMemberUID().toArray()));
            jsonObject.add("groupBank", serializeGroupBank(group.getGroupBank().container.toNonNullArray()));
            jsonObject.addProperty("isHardcore", group.isHardcore());
            jsonObject.addProperty("groupLives", group.getGroupLives());
            return jsonObject;
        }

        private JsonArray serializeGroupBank(Item[] groupBank) {
            JsonArray jsonArray = new JsonArray();
            for (Item item : groupBank) {
                JsonObject itemObject = new JsonObject();
                itemObject.addProperty("id", item.getId());
                itemObject.addProperty("amount", item.getAmount());
                jsonArray.add(itemObject);
            }
            return jsonArray;
        }
    }
}

