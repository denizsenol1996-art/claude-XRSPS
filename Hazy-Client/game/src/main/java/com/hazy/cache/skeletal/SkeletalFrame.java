/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.hazy.cache.skeletal;

import com.hazy.cache.anim.SeqFrame;

import com.hazy.cache.anim.FrameBase;
import com.hazy.cache.anims.datastructure.DualNode;
import com.hazy.cache.anims.datastructure.EvictingDualNodeHashTable;
import com.hazy.io.Buffer;
import com.hazy.util.math.Matrix4f;
import com.hazy.util.math.Quaternionf;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

public final class SkeletalFrame
extends DualNode
implements SeqFrame {
    public TO[][] tt = null;
    public TO[][] vt = null;
    private final FrameBase frameBase;
    int fid = 0;
    public boolean hExisting;
    public final int v;
    public final int baseGroupId;
    public static final EvictingDualNodeHashTable cache = new EvictingDualNodeHashTable(100);
    public static final IntSet skeletalFrameIds = new IntOpenHashSet();

    public  SkeletalFrame(int groupId, byte[] frameData) {
        Buffer frameBuffer = new Buffer(frameData);
        this.v = frameBuffer.readUnsignedByte();
        this.baseGroupId = frameBuffer.readUnsignedShort();
        this.frameBase = FrameBase.frameBases[this.baseGroupId];
        this.decode(frameBuffer);
    }

    public static SkeletalFrame getSkeletalFrame(int skeletalId) {
        return (SkeletalFrame) SeqFrame.getFrame(skeletalId);
    }

    void decode(Buffer buffer) {
        buffer.readUnsignedShort();
        buffer.readUnsignedShort();
        this.fid = buffer.readUnsignedByte();
        int tc = buffer.readUnsignedShort();
        FrameBase base = this.getFrameBase();
        AnimationBoneWrapper animationBoneWrapper = base.getAbw();
        this.vt = new TO[animationBoneWrapper.getEsLength()][];
        this.tt = new TO[base.getCount()][];
        for (int index = 0; index < tc; ++index) {
            int tId = buffer.readUnsignedByte();
            ToType to = ToType.lookUpById(tId);
            int ti = buffer.readSignedSmart();
            int tcmpId = buffer.readUnsignedByte();
            ToCmp tcmp = ToCmp.lookup(tcmpId);
            TO curTo = new TO();
            curTo.dc(buffer);
            int count = to.getDimensions();
            TO[][] TOS = to == ToType.TV ? this.vt : this.tt;
            if (TOS[ti] == null) {
                TOS[ti] = new TO[count];
            }
            TOS[ti][tcmp.component()] = curTo;
            if (to != ToType.TT) continue;
            this.hExisting = true;
        }
    }

    public int getFid() {
        return this.fid;
    }

    public boolean hasAlphaTransforms() {
        return this.hExisting;
    }

    public void du(int cT, AnimationBone cB, int ti, int fid) {
        Matrix4f clm = Matrix4f.get();
        this.ur(clm, ti, cB, cT);
        this.us(clm, ti, cB, cT);
        this.ut(clm, ti, cB, cT);
        cB.setClm(clm);
        clm.r();
    }

    void ur(Matrix4f clm, int transformIndex, AnimationBone cB, int cT) {
        float[] eA = cB.getEa(this.fid);
        float e1 = eA[0];
        float e2 = eA[1];
        float e3 = eA[2];
        if (this.vt[transformIndex] != null) {
            TO to1 = this.vt[transformIndex][0];
            TO to2 = this.vt[transformIndex][1];
            TO to3 = this.vt[transformIndex][2];
            if (to1 != null) {
                e1 = to1.gv(cT);
            }
            if (to2 != null) {
                e2 = to2.gv(cT);
            }
            if (to3 != null) {
                e3 = to3.gv(cT);
            }
        }
        Quaternionf xrq = Quaternionf.take();
        xrq.faa(1.0f, 0.0f, 0.0f, e1);
        Quaternionf yrq = Quaternionf.take();
        yrq.faa(0.0f, 1.0f, 0.0f, e2);
        Quaternionf zrq = Quaternionf.take();
        zrq.faa(0.0f, 0.0f, 1.0f, e3);
        Quaternionf frq = Quaternionf.take();
        frq.mp(zrq);
        frq.mp(xrq);
        frq.mp(yrq);
        Matrix4f rm2 = Matrix4f.get();
        rm2.sfq(frq);
        clm.mp(rm2);
        xrq.release();
        yrq.release();
        zrq.release();
        frq.release();
        rm2.r();
    }

    void ut(Matrix4f clm, int ti, AnimationBone cB, int cT) {
        float[] gt = cB.getTs(this.fid);
        float x = gt[0];
        float y = gt[1];
        float z = gt[2];
        if (this.vt[ti] != null) {
            TO to1 = this.vt[ti][3];
            TO to2 = this.vt[ti][4];
            TO to3 = this.vt[ti][5];
            if (to1 != null) {
                x = to1.gv(cT);
            }
            if (to2 != null) {
                y = to2.gv(cT);
            }
            if (to3 != null) {
                z = to3.gv(cT);
            }
        }
        clm.values[12] = x;
        clm.values[13] = y;
        clm.values[14] = z;
    }

    void us(Matrix4f clm, int ti, AnimationBone cB, int cT) {
        float[] scale = cB.getSc(this.fid);
        float x = scale[0];
        float y = scale[1];
        float z = scale[2];
        if (this.vt[ti] != null) {
            TO to1 = this.vt[ti][6];
            TO to2 = this.vt[ti][7];
            TO to3 = this.vt[ti][8];
            if (to1 != null) {
                x = to1.gv(cT);
            }
            if (to2 != null) {
                y = to2.gv(cT);
            }
            if (to3 != null) {
                z = to3.gv(cT);
            }
        }
        Matrix4f sm = Matrix4f.get();
        sm.sc(x, y, z);
        clm.mp(sm);
        sm.r();
    }

    @Override
    public FrameBase getFrameBase() {
        return this.frameBase;
    }

    @Override
    public boolean noAnimationInProgress() {
        return false;
    }
}

