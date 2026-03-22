/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.hazy.cache.skeletal;

import com.hazy.util.SerialEnum;

public enum Toca implements SerialEnum
{
    VR(0, 0),
    VT(1, 1),
    aClass146_1(2, 2),
    VS(3, 3),
    field1506(4, 4),
    field1507(5, 5),
    field1508(6, 6),
    field1509(7, 7),
    NULL(8, 8);

    final int field1512;
    final int serialId;
    public static final Toca[] VALUES;

    private Toca(int var3, int serialId) {
        this.field1512 = var3;
        this.serialId = serialId;
    }

    @Override
    public int id() {
        return this.serialId;
    }

    static {
        VALUES = Toca.values();
    }
}

