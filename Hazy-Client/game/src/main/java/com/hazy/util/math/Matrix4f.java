/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.hazy.util.math;


import com.hazy.io.Buffer;

import java.util.Arrays;

public final class Matrix4f {
    public static final Matrix4f IDENTITY;
    public float[] values = new float[16];
    private static final Matrix4f[] p;
    private static final int pl;
    private static int pc;

    public Matrix4f() {
        this.identity();
    }

    public Matrix4f(Matrix4f matrix) {
        this.sf(matrix);
    }

    public Matrix4f(Buffer buffer, boolean use4x3) {
        this.lfb(buffer, use4x3);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void r() {
        Matrix4f[] matrix4fArray = p;
        synchronized (p) {
            if (pc < pl - 1) {
                Matrix4f.p[Matrix4f.pc++] = this;
            }
            // ** MonitorExit[var1_1] (shouldn't be in output)
            return;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Matrix4f get() {
        Matrix4f[] matrix4fArray = p;
        synchronized (p) {
            if (pc == 0) {
                // ** MonitorExit[var0] (shouldn't be in output)
                return new Matrix4f();
            }
            p[--pc].identity();
            // ** MonitorExit[var0] (shouldn't be in output)
            return p[pc];
        }
    }

    void lfb(Buffer b, boolean c) {
        if (c) {
            Matrix4x3f matrix4x3 = new Matrix4x3f();
            matrix4x3.a1(JagexAngle.mapAngleToTWO_PI(b.readShort()));
            matrix4x3.a2(JagexAngle.mapAngleToTWO_PI(b.readShort()));
            int angle = b.readShort();
            double TWO_PI = Math.PI * 2;
            float theta = (float)((double)((float)(angle &= 0x3FFF) / 16384.0f) * TWO_PI);
            matrix4x3.a3(theta);
            matrix4x3.a4(b.readShort(), b.readShort(), b.readShort());
            this.sfs(matrix4x3);
        } else {
            for (int index = 0; index < 16; ++index) {
                this.values[index] = b.readFloat();
            }
        }
    }

    public float[] gEA1() {
        float[] eulerAngles = new float[3];
        if ((double)this.values[2] < 0.999 && (double)this.values[2] > -0.999) {
            eulerAngles[1] = (float)(-Math.asin(this.values[2]));
            double c = Math.cos(eulerAngles[1]);
            eulerAngles[0] = (float)Math.atan2((double)this.values[6] / c, (double)this.values[10] / c);
            eulerAngles[2] = (float)Math.atan2((double)this.values[1] / c, (double)this.values[0] / c);
        } else {
            eulerAngles[0] = 0.0f;
            eulerAngles[1] = (float)Math.atan2(this.values[2], 0.0);
            eulerAngles[2] = (float)Math.atan2(-this.values[9], this.values[5]);
        }
        return eulerAngles;
    }

    public float[] gEA2() {
        float[] fArray = new float[]{(float)(-Math.asin(this.values[6])), 0.0f, 0.0f};
        float[] eulerAngles = fArray;
        double var2 = Math.cos(eulerAngles[0]);
        if (Math.abs(var2) > 0.005) {
            double var4 = this.values[2];
            double var6 = this.values[10];
            double var8 = this.values[4];
            double var10 = this.values[5];
            eulerAngles[1] = (float)Math.atan2(var4, var6);
            eulerAngles[2] = (float)Math.atan2(var8, var10);
        } else {
            double var4 = this.values[1];
            double var6 = this.values[0];
            eulerAngles[1] = this.values[6] < 0.0f ? (float)Math.atan2(var4, var6) : (float)(-Math.atan2(var4, var6));
            eulerAngles[2] = 0.0f;
        }
        return eulerAngles;
    }

    public void identity() {
        this.values[0] = 1.0f;
        this.values[1] = 0.0f;
        this.values[2] = 0.0f;
        this.values[3] = 0.0f;
        this.values[4] = 0.0f;
        this.values[5] = 1.0f;
        this.values[6] = 0.0f;
        this.values[7] = 0.0f;
        this.values[8] = 0.0f;
        this.values[9] = 0.0f;
        this.values[10] = 1.0f;
        this.values[11] = 0.0f;
        this.values[12] = 0.0f;
        this.values[13] = 0.0f;
        this.values[14] = 0.0f;
        this.values[15] = 1.0f;
    }

    public void zero() {
        this.values[0] = 0.0f;
        this.values[1] = 0.0f;
        this.values[2] = 0.0f;
        this.values[3] = 0.0f;
        this.values[4] = 0.0f;
        this.values[5] = 0.0f;
        this.values[6] = 0.0f;
        this.values[7] = 0.0f;
        this.values[8] = 0.0f;
        this.values[9] = 0.0f;
        this.values[10] = 0.0f;
        this.values[11] = 0.0f;
        this.values[12] = 0.0f;
        this.values[13] = 0.0f;
        this.values[14] = 0.0f;
        this.values[15] = 0.0f;
    }

    public void sf(Matrix4f m4) {
        System.arraycopy(m4.values, 0, this.values, 0, 16);
    }

    public void sc(float scalar) {
        this.sc(scalar, scalar, scalar);
    }

    public void sc(float x, float y, float z) {
        this.identity();
        this.values[0] = x;
        this.values[5] = y;
        this.values[10] = z;
    }

    public void a(Matrix4f other) {
        for (int index = 0; index < this.values.length; ++index) {
            float[] matrix = this.values;
            int n = index;
            matrix[n] = matrix[n] + other.values[index];
        }
    }

    public void mp(Matrix4f c) {
        float m00 = this.values[2] * c.values[8] + this.values[1] * c.values[4] + this.values[0] * c.values[0] + c.values[12] * this.values[3];
        float m01 = this.values[3] * c.values[13] + c.values[9] * this.values[2] + this.values[0] * c.values[1] + c.values[5] * this.values[1];
        float m02 = this.values[2] * c.values[10] + this.values[1] * c.values[6] + c.values[2] * this.values[0] + c.values[14] * this.values[3];
        float m03 = this.values[3] * c.values[15] + c.values[11] * this.values[2] + this.values[0] * c.values[3] + this.values[1] * c.values[7];
        float m10 = this.values[7] * c.values[12] + c.values[0] * this.values[4] + c.values[4] * this.values[5] + c.values[8] * this.values[6];
        float m11 = this.values[7] * c.values[13] + c.values[1] * this.values[4] + this.values[5] * c.values[5] + c.values[9] * this.values[6];
        float m12 = this.values[7] * c.values[14] + c.values[6] * this.values[5] + this.values[4] * c.values[2] + this.values[6] * c.values[10];
        float m13 = c.values[15] * this.values[7] + this.values[6] * c.values[11] + this.values[5] * c.values[7] + c.values[3] * this.values[4];
        float m20 = this.values[9] * c.values[4] + this.values[8] * c.values[0] + this.values[10] * c.values[8] + this.values[11] * c.values[12];
        float m21 = c.values[9] * this.values[10] + this.values[8] * c.values[1] + c.values[5] * this.values[9] + this.values[11] * c.values[13];
        float m22 = c.values[2] * this.values[8] + this.values[9] * c.values[6] + c.values[10] * this.values[10] + this.values[11] * c.values[14];
        float m23 = c.values[15] * this.values[11] + c.values[11] * this.values[10] + c.values[7] * this.values[9] + this.values[8] * c.values[3];
        float m30 = this.values[15] * c.values[12] + c.values[4] * this.values[13] + c.values[0] * this.values[12] + this.values[14] * c.values[8];
        float m31 = this.values[12] * c.values[1] + c.values[5] * this.values[13] + this.values[14] * c.values[9] + c.values[13] * this.values[15];
        float m32 = c.values[10] * this.values[14] + this.values[13] * c.values[6] + c.values[2] * this.values[12] + c.values[14] * this.values[15];
        float m33 = c.values[15] * this.values[15] + c.values[3] * this.values[12] + this.values[13] * c.values[7] + c.values[11] * this.values[14];
        this.s(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
    }

    public void sfq(Quaternionf quaternion) {
        float ww = quaternion.w * quaternion.w;
        float wx = quaternion.w * quaternion.x;
        float yw = quaternion.y * quaternion.w;
        float wz = quaternion.w * quaternion.z;
        float xx = quaternion.x * quaternion.x;
        float xy = quaternion.x * quaternion.y;
        float zx = quaternion.z * quaternion.x;
        float yy = quaternion.y * quaternion.y;
        float zy = quaternion.z * quaternion.y;
        float zz = quaternion.z * quaternion.z;
        this.values[0] = ww + xx - zz - yy;
        this.values[1] = wz + xy + xy + wz;
        this.values[2] = zx - yw - yw + zx;
        this.values[4] = xy + (xy - wz - wz);
        this.values[5] = ww + yy - xx - zz;
        this.values[6] = wx + wx + zy + zy;
        this.values[8] = zx + yw + zx + yw;
        this.values[9] = zy + (zy - wx - wx);
        this.values[10] = zz + ww - yy - xx;
    }

    public void sfs(Matrix4x3f matrix4x3) {
        this.values[0] = matrix4x3.m00;
        this.values[1] = matrix4x3.m10;
        this.values[2] = matrix4x3.m20;
        this.values[3] = 0.0f;
        this.values[4] = matrix4x3.m01;
        this.values[5] = matrix4x3.m11;
        this.values[6] = matrix4x3.m21;
        this.values[7] = 0.0f;
        this.values[8] = matrix4x3.m02;
        this.values[9] = matrix4x3.m12;
        this.values[10] = matrix4x3.m22;
        this.values[11] = 0.0f;
        this.values[12] = matrix4x3.m03;
        this.values[13] = matrix4x3.m13;
        this.values[14] = matrix4x3.m23;
        this.values[15] = 1.0f;
    }

    public float d() {
        return this.values[10] * this.values[4] * this.values[3] * this.values[13] + (this.values[13] * this.values[8] * this.values[7] * this.values[2] + this.values[12] * this.values[5] * this.values[2] * this.values[11] + (this.values[15] * this.values[4] * this.values[2] * this.values[9] + this.values[12] * this.values[1] * this.values[7] * this.values[10] + (this.values[14] * this.values[1] * this.values[4] * this.values[11] + (this.values[13] * this.values[6] * this.values[0] * this.values[11] + (this.values[10] * this.values[0] * this.values[5] * this.values[15] - this.values[5] * this.values[0] * this.values[11] * this.values[14] - this.values[15] * this.values[9] * this.values[0] * this.values[6]) + this.values[14] * this.values[0] * this.values[7] * this.values[9] - this.values[13] * this.values[7] * this.values[0] * this.values[10] - this.values[15] * this.values[4] * this.values[1] * this.values[10]) + this.values[1] * this.values[6] * this.values[8] * this.values[15] - this.values[12] * this.values[11] * this.values[1] * this.values[6] - this.values[14] * this.values[8] * this.values[1] * this.values[7]) - this.values[13] * this.values[11] * this.values[2] * this.values[4] - this.values[15] * this.values[5] * this.values[2] * this.values[8]) - this.values[7] * this.values[2] * this.values[9] * this.values[12] - this.values[14] * this.values[9] * this.values[4] * this.values[3]) + this.values[14] * this.values[3] * this.values[5] * this.values[8] - this.values[12] * this.values[3] * this.values[5] * this.values[10] - this.values[8] * this.values[6] * this.values[3] * this.values[13] + this.values[12] * this.values[3] * this.values[6] * this.values[9];
    }

    public void iv() {
        float det = 1.0f / this.d();
        float m00 = det * (this.values[15] * this.values[10] * this.values[5] - this.values[11] * this.values[5] * this.values[14] - this.values[9] * this.values[6] * this.values[15] + this.values[13] * this.values[11] * this.values[6] + this.values[14] * this.values[9] * this.values[7] - this.values[13] * this.values[7] * this.values[10]);
        float m01 = (this.values[13] * this.values[3] * this.values[10] + (this.values[14] * this.values[1] * this.values[11] + -this.values[1] * this.values[10] * this.values[15] + this.values[2] * this.values[9] * this.values[15] - this.values[13] * this.values[2] * this.values[11] - this.values[14] * this.values[9] * this.values[3])) * det;
        float m02 = (this.values[13] * this.values[2] * this.values[7] + (this.values[1] * this.values[6] * this.values[15] - this.values[14] * this.values[7] * this.values[1] - this.values[5] * this.values[2] * this.values[15]) + this.values[3] * this.values[5] * this.values[14] - this.values[13] * this.values[6] * this.values[3]) * det;
        float m03 = det * (this.values[11] * this.values[2] * this.values[5] + this.values[7] * this.values[1] * this.values[10] + -this.values[1] * this.values[6] * this.values[11] - this.values[9] * this.values[7] * this.values[2] - this.values[10] * this.values[3] * this.values[5] + this.values[3] * this.values[6] * this.values[9]);
        float m10 = det * (this.values[15] * this.values[6] * this.values[8] + -this.values[4] * this.values[10] * this.values[15] + this.values[11] * this.values[4] * this.values[14] - this.values[11] * this.values[6] * this.values[12] - this.values[14] * this.values[8] * this.values[7] + this.values[12] * this.values[10] * this.values[7]);
        float m11 = det * (this.values[14] * this.values[3] * this.values[8] + this.values[15] * this.values[0] * this.values[10] - this.values[14] * this.values[11] * this.values[0] - this.values[8] * this.values[2] * this.values[15] + this.values[2] * this.values[11] * this.values[12] - this.values[10] * this.values[3] * this.values[12]);
        float m12 = (this.values[6] * this.values[3] * this.values[12] + (this.values[15] * this.values[2] * this.values[4] + this.values[14] * this.values[7] * this.values[0] + this.values[15] * -this.values[0] * this.values[6] - this.values[2] * this.values[7] * this.values[12] - this.values[3] * this.values[4] * this.values[14])) * det;
        float m13 = (this.values[4] * this.values[3] * this.values[10] + this.values[0] * this.values[6] * this.values[11] - this.values[0] * this.values[7] * this.values[10] - this.values[4] * this.values[2] * this.values[11] + this.values[8] * this.values[7] * this.values[2] - this.values[3] * this.values[6] * this.values[8]) * det;
        float m20 = det * (this.values[15] * this.values[9] * this.values[4] - this.values[4] * this.values[11] * this.values[13] - this.values[15] * this.values[5] * this.values[8] + this.values[5] * this.values[11] * this.values[12] + this.values[13] * this.values[8] * this.values[7] - this.values[12] * this.values[7] * this.values[9]);
        float m21 = det * (this.values[9] * this.values[3] * this.values[12] + (this.values[9] * -this.values[0] * this.values[15] + this.values[13] * this.values[11] * this.values[0] + this.values[1] * this.values[8] * this.values[15] - this.values[11] * this.values[1] * this.values[12] - this.values[13] * this.values[3] * this.values[8]));
        float m22 = det * (this.values[5] * this.values[0] * this.values[15] - this.values[7] * this.values[0] * this.values[13] - this.values[15] * this.values[4] * this.values[1] + this.values[12] * this.values[1] * this.values[7] + this.values[3] * this.values[4] * this.values[13] - this.values[12] * this.values[5] * this.values[3]);
        float m23 = (this.values[8] * this.values[5] * this.values[3] + (this.values[9] * this.values[7] * this.values[0] + this.values[11] * -this.values[0] * this.values[5] + this.values[11] * this.values[1] * this.values[4] - this.values[7] * this.values[1] * this.values[8] - this.values[9] * this.values[4] * this.values[3])) * det;
        float m30 = (this.values[6] * this.values[9] * this.values[12] + (this.values[14] * this.values[8] * this.values[5] + this.values[14] * -this.values[4] * this.values[9] + this.values[13] * this.values[10] * this.values[4] - this.values[10] * this.values[5] * this.values[12] - this.values[13] * this.values[8] * this.values[6])) * det;
        float m31 = (this.values[2] * this.values[8] * this.values[13] + this.values[12] * this.values[10] * this.values[1] + (this.values[9] * this.values[0] * this.values[14] - this.values[13] * this.values[0] * this.values[10] - this.values[8] * this.values[1] * this.values[14]) - this.values[9] * this.values[2] * this.values[12]) * det;
        float m32 = (this.values[13] * this.values[0] * this.values[6] + -this.values[0] * this.values[5] * this.values[14] + this.values[14] * this.values[4] * this.values[1] - this.values[12] * this.values[6] * this.values[1] - this.values[13] * this.values[4] * this.values[2] + this.values[2] * this.values[5] * this.values[12]) * det;
        float m33 = (this.values[5] * this.values[0] * this.values[10] - this.values[6] * this.values[0] * this.values[9] - this.values[4] * this.values[1] * this.values[10] + this.values[8] * this.values[1] * this.values[6] + this.values[2] * this.values[4] * this.values[9] - this.values[5] * this.values[2] * this.values[8]) * det;
        this.s(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
    }

    public void s(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
        this.values[0] = m00;
        this.values[1] = m01;
        this.values[2] = m02;
        this.values[3] = m03;
        this.values[4] = m10;
        this.values[5] = m11;
        this.values[6] = m12;
        this.values[7] = m13;
        this.values[8] = m20;
        this.values[9] = m21;
        this.values[10] = m22;
        this.values[11] = m23;
        this.values[12] = m30;
        this.values[13] = m31;
        this.values[14] = m32;
        this.values[15] = m33;
    }

    public float[] gs() {
        float[] s2 = new float[3];
        Vector3f x = new Vector3f(this.values[0], this.values[1], this.values[2]);
        Vector3f y = new Vector3f(this.values[4], this.values[5], this.values[6]);
        Vector3f z = new Vector3f(this.values[8], this.values[9], this.values[10]);
        s2[0] = x.length();
        s2[1] = y.length();
        s2[2] = z.length();
        return s2;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Matrix4f)) {
            return false;
        }
        Matrix4f other = (Matrix4f)o;
        for (int var3 = 0; var3 < 16; ++var3) {
            if (other.values[var3] == this.values[var3]) continue;
            return false;
        }
        return true;
    }

    public int hashCode() {
        return 31 + Arrays.hashCode(this.values);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.gEA2();
        this.gEA1();
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                float value;
                if (j > 0) {
                    sb.append("\t");
                }
                if (Math.sqrt((value = this.values[j + i * 4]) * value) < (double)1.0E-4f) {
                    value = 0.0f;
                }
                sb.append(value);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    static {
        p = new Matrix4f[100];
        pl = 100;
        pc = 0;
        IDENTITY = new Matrix4f();
    }
}

