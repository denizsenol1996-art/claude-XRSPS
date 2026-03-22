package com.twisted.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.twisted.game.GameEngine;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public record Referral(@JsonProperty("name") String name,
                       @JsonProperty("uuid") long uuid,
                       @JsonProperty("ipAddress") String ipAddress,
                       @JsonProperty("mac") String mac,
                       @JsonProperty("amountGiven") int amountGiven,
                       @JsonProperty("youtuber") String youtuber) {


    static final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new AfterburnerModule());

    public static Object2ObjectLinkedOpenHashMap<String, Referral> compute(final String filePath) throws IOException {
        final File file = new File(filePath);
        final Object2ObjectLinkedOpenHashMap<String, Referral> mapped = objectMapper.readValue(file, new TypeReference<>() {
        });
        for (final var information : mapped.object2ObjectEntrySet()) {
            ReferralSystem.CLAIMED_REFERRALS.put(information.getKey(), information.getValue());
        }
        return ReferralSystem.CLAIMED_REFERRALS;
    }

    @SneakyThrows
    public static void loadReferrals() {
        ReferralSystem.CLAIMED_REFERRALS = compute(new File("data/saves/referrals.json").getAbsolutePath());
        log.info("Loaded {} Potion Consumable Information", ReferralSystem.CLAIMED_REFERRALS.size());
    }

    public void save(Object2ObjectLinkedOpenHashMap<String, Referral> potionDataList) {
        GameEngine.getInstance().submitLowPriority(() ->
            GameEngine.getInstance().addSyncTask(() -> {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                try {
                    final String path = "data/saves/referrals.json";
                    File file = new File(path);
                    ObjectNode newDataNode = objectMapper.valueToTree(potionDataList);
                    objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, newDataNode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
    }
}

