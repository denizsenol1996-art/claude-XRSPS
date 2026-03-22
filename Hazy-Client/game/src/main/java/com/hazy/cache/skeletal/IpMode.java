/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.hazy.cache.skeletal;

import com.hazy.util.SerialEnum;

public enum IpMode implements SerialEnum
{
    NULL(0, 0),
    field1423(1, 1),
    field1424(2, 2),
    field1425(3, 3),
    field1422(4, 4);

    final int ordinal;
    final int serialId;
    public static final IpMode[] VALUES;

    private IpMode(int ordinal, int serielId) {
        this.ordinal = ordinal;
        this.serialId = serielId;
    }

    @Override
    public int id() {
        return this.serialId;
    }

    static {
        VALUES = IpMode.values();
    }
}

