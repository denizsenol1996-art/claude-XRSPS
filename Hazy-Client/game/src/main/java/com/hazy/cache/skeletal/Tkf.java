/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.hazy.cache.skeletal;

import com.hazy.io.Buffer;

public class Tkf {
    public int kti;
    public float vAtTkf;
    public float etDelta = Float.MAX_VALUE;
    public float evDelta = Float.MAX_VALUE;
    public float btDelta = Float.MAX_VALUE;
    public float bvDelta = Float.MAX_VALUE;
    Tkf nTkf;

    Tkf() {
    }

    void decode(Buffer buffer) {
        this.kti = buffer.readShortOSRS();
        this.vAtTkf = buffer.readFloat();
        this.etDelta = buffer.readFloat();
        this.evDelta = buffer.readFloat();
        this.btDelta = buffer.readFloat();
        this.bvDelta = buffer.readFloat();
    }
}

