package com.twisted.game.content.events;

import lombok.Getter;

@Getter
public enum WOGWData {

    DOUBLE_VOTES(new ServerEvent("Double Votes")),
    DOUBLE_EXP(new ServerEvent("Double Experience")),
    DOUBLE_DROPS(new ServerEvent("Double Drops")),
    DROP_RATE(new ServerEvent("1.5X Drop Rate")),
    BLOOD_MONEY_BOOST(new ServerEvent("2X Blood Money")),
    DOUBLE_SLAYER_POINTS(new ServerEvent("Double Slayer Points"));

    public final ServerEvent event;
    public static final WOGWData[] VALUES = values();

    WOGWData(ServerEvent event) {
        this.event = event;
    }
}
