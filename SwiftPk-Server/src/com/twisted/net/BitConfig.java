package com.twisted.net;

public final class BitConfig {

    private final int id;
    private final int value;

    public BitConfig(int id, int value) {
        this.id = id;
        this.value = value;
    }

    public final int getId() {
        return id;
    }

    public final int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return BitConfig.class.getSimpleName() + "[" + id + "=" + value + "]";
    }

}
