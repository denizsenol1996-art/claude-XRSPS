/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.hazy.cache.skeletal;

import com.hazy.util.SerialEnum;

public enum ToType implements SerialEnum
{
    NULL(0, 0, null, 0),
    TV(1, 1, null, 9),
    TB(2, 2, null, 3),
    TC(3, 3, null, 6),
    TT(4, 4, null, 1),
    field1452(5, 5, null, 3);

    final int serialId;
    private final int id;
    private final int dimensions;
    private static final ToType[] VALUES;

    private ToType(int serialId, int id, String name, int dimensions) {
        this.serialId = serialId;
        this.id = id;
        this.dimensions = dimensions;
    }

    public static ToType lookUpById(int id) {
        ToType type2 = (ToType)SerialEnum.findEnumerated((SerialEnum[])VALUES, (int)id);
        if (type2 == null) {
            type2 = NULL;
        }
        return type2;
    }

    @Override
    public int id() {
        return this.id;
    }

    int getDimensions() {
        return this.dimensions;
    }

    static {
        VALUES = ToType.values();
    }
}

