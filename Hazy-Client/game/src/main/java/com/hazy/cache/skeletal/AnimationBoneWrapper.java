/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.hazy.cache.skeletal;

import com.hazy.io.Buffer;

public final class AnimationBoneWrapper {
    AnimationBone[] es;
    int mc;

    public AnimationBoneWrapper(Buffer buffer, int length) {
        this.es = new AnimationBone[length];
        this.mc = buffer.readUnsignedByte();
        for (int index = 0; index < this.es.length; ++index) {
            AnimationBone bone;
            this.es[index] = bone = new AnimationBone(this.mc, buffer, false);
        }
        try {
            this.ip();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ip() {
        AnimationBone[] es;
        for (AnimationBone animationBone : es = this.es) {
            int b_pid = animationBone.b_pid;
            if (b_pid < 0 || b_pid >= es.length) continue;
            animationBone.p_b = es[b_pid];
        }
    }

    public int getEsLength() {
        return this.es.length;
    }

    public AnimationBone getAB(int bi) {
        return this.es[bi];
    }

    public AnimationBone[] getEs() {
        return this.es;
    }

    public void update(SkeletalFrame skeletalFrame, int tick) {
        this.update(skeletalFrame, tick, null, false);
    }

    public void update(SkeletalFrame sk, int ct, boolean[] mask, boolean state) {
        AnimationBone[] es;
        int fid = sk.getFid();
        int ti = 0;
        for (AnimationBone cB : es = this.getEs()) {
            if (mask == null || state == mask[ti]) {
                sk.du(ct, cB, ti, fid);
            }
            ++ti;
        }
    }
}

