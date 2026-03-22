package com.twisted.game.content.gambling;

public enum GameType {
    NONE("None", 0, ""),
    FLOWER_POKER("Flower Poker", 330, "After putting up the items you wish to<br>gamble, both players plant 5 flowers.<br>The player with the best hand wins the pot.<br>Tie, white/black flowers will result in a <br>re-plant.")
    ; // End of enum

    public final String formalName;
    public final int configId;
    public final String description;

    GameType(String formalName, int config, String description) {
        this.formalName = formalName;
        this.configId = config;
        this.description = description;
    }
}
