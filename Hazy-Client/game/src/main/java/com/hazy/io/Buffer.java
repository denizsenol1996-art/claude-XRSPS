package com.hazy.io;

import com.hazy.Client;
import com.hazy.ClientConstants;
import com.hazy.cache.anims.datastructure.IterableNodeHashTable;
import com.hazy.collection.Cacheable;
import com.hazy.collection.Node;
import com.hazy.net.IsaacCipher;
import net.runelite.api.IntegerNode;

import java.math.BigInteger;

public final class Buffer extends Cacheable {

    private static final int[] BIT_MASKS = {0, 1, 3, 7, 15, 31, 63, 127, 255,
            511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 0x1ffff, 0x3ffff,
            0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff,
            0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff,
            0x7fffffff, -1};


    public float readFloat() {
        return Float.intBitsToFloat(this.readInt());
    }

    public int readShortOSRS() {
        this.pos += 2;
        int var1 = (this.payload[this.pos - 1] & 0xFF) + ((this.payload[this.pos - 2] & 0xFF) << 8);
        if (var1 > Short.MAX_VALUE) {
            var1 -= 65536;
        }
        return var1;
    }


    public int read24BitInt()
    {
        return (this.readUnsignedByte() << 16) + (this.readUnsignedByte() << 8) + this.readUnsignedByte();
    }

    private static final char[] CHARACTERS = new char[]
        {
            '\u20ac', '\u0000', '\u201a', '\u0192', '\u201e', '\u2026',
            '\u2020', '\u2021', '\u02c6', '\u2030', '\u0160', '\u2039',
            '\u0152', '\u0000', '\u017d', '\u0000', '\u0000', '\u2018',
            '\u2019', '\u201c', '\u201d', '\u2022', '\u2013', '\u2014',
            '\u02dc', '\u2122', '\u0161', '\u203a', '\u0153', '\u0000',
            '\u017e', '\u0178'
        };

    public String readJagexString() {
        int i = pos;
        while (payload[pos++] != 10)
            ;
        return new String(payload, i, pos - i - 1);
    }


    public int readBigSmart2()
    {
        if (pos < 0)
        {
            return readInt() & Integer.MAX_VALUE; // and off sign bit
        }
        int value = readUnsignedShort();
        return value == 32767 ? -1 : value;
    }
     public static final BigInteger RSA_MODULUS = new BigInteger(
            "131409501542646890473421187351592645202876910715283031445708554322032707707649791604685616593680318619733794036379235220188001221437267862925531863675607742394687835827374685954437825783807190283337943749605737918856262761566146702087468587898515768996741636870321689974105378482179138088453912399137944888201");
    public static final BigInteger RSA_EXPONENT = new BigInteger("65537");
    private IsaacCipher cipher;

    public byte[] payload;
    public int pos;
    public int bitPosition;

    public int readSmart() {
        int value = payload[pos] & 0xff;
        if (value < 128)
            return readUnsignedByte() - 64;
        else
            return readUnsignedShort() - 49152;
    }

    public Buffer(byte[] payload) {
        this.payload = payload;
        pos = 0;
    }

    public int readMedium() {
        this.pos += 3;
        return ((this.payload[this.pos - 3] & 0xFF) << 16) + (this.payload[this.pos - 1] & 0xFF) + ((this.payload[this.pos - 2] & 0xFF) << 8);
    }


    public static Buffer create(int size, IsaacCipher cipher) {
        Buffer stream_1 = new Buffer(new byte[size]);
        stream_1.payload = new byte[size];
        stream_1.cipher = cipher;
        return stream_1;
    }

    public byte readByte() {
        return this.payload[++this.pos - 1];
    }

    public int getUIncrementalSmart() {
        int value = 0, remainder;
        for (remainder = readUSmart(); remainder == 32767; remainder = readUSmart()) {
            value += 32767;
        }
        value += remainder;
        return value;
    }

    public final int readUTriByte() {
        pos += 3;
        return (0xff & payload[pos - 3] << 16) + (0xff & payload[pos - 2] << 8) + (0xff & payload[pos - 1]);
    }

    public final int readUTriByte(int i) {
        pos += 3;
        return (0xff & payload[pos - 3] << 16) + (0xff & payload[pos - 2] << 8) + (0xff & payload[pos - 1]);
    }

    public int readUnsignedShortSmartMinusOne()
    {
        int peek = bitPosition & 0xFF;
        return peek < 128 ? this.readUnsignedByte() - 1 : this.readUnsignedShort() - 0x8001;
    }

    public int readUSmart2() {
        int baseVal = 0;
        int lastVal = 0;
        while ((lastVal = readUSmart()) == 32767) {
            baseVal += 32767;
        }
        return baseVal + lastVal;
    }

    public String readNewString() {
        int i = pos;
        while (payload[pos++] != 0)
            ;
        return new String(payload, i, pos - i - 1);
    }

    public void writeOpcode(int i) {
        payload[pos++] = (byte) (i + cipher.value());
    }

    public void writeByte(int value) {
        payload[pos++] = (byte) value;
    }

    public String readStrings() {
        int i = pos;
        while (payload[pos++] != 10)
            ;
        return new String(payload, i, pos - i - 1);
    }

    public int writeWordBigEndian(int value) {
        payload[pos++] = (byte) value;
        return (byte) value;
    }

    public void writeShort(int value) {
        payload[pos++] = (byte) (value >> 8);
        payload[pos++] = (byte) value;
    }

    public void writeTriByte(int value) {
        payload[pos++] = (byte) (value >> 16);
        payload[pos++] = (byte) (value >> 8);
        payload[pos++] = (byte) value;
    }

    public void writeInt(int value) {
        payload[pos++] = (byte) (value >> 24);
        payload[pos++] = (byte) (value >> 16);
        payload[pos++] = (byte) (value >> 8);
        payload[pos++] = (byte) value;
    }

    public void writeLEInt(int value) {
        payload[pos++] = (byte) value;
        payload[pos++] = (byte) (value >> 8);
        payload[pos++] = (byte) (value >> 16);
        payload[pos++] = (byte) (value >> 24);
    }

    public void writeLong(long value) {
        try {
            payload[pos++] = (byte) (int) (value >> 56);
            payload[pos++] = (byte) (int) (value >> 48);
            payload[pos++] = (byte) (int) (value >> 40);
            payload[pos++] = (byte) (int) (value >> 32);
            payload[pos++] = (byte) (int) (value >> 24);
            payload[pos++] = (byte) (int) (value >> 16);
            payload[pos++] = (byte) (int) (value >> 8);
            payload[pos++] = (byte) (int) value;
        } catch (RuntimeException runtimeexception) {
            System.out.println("14395, " + 5 + ", " + value + ", " + runtimeexception.toString());
            throw new RuntimeException();
        }
    }

    public void writeString(String text) {
        System.arraycopy(text.getBytes(), 0, payload, pos,
                text.length());
        pos += text.length();
        payload[pos++] = 10;
    }

    public void writeBytes(byte data[], int offset, int length) {
        for (int index = length; index < length + offset; index++)
            payload[pos++] = data[index];
    }

    public void writeBytes(byte data[]) {
        for (byte b : data) {
            writeByte(b);
        }
    }

    public void writeByteAtPosition(int value) {
        payload[pos - value - 1] = (byte) value;
    }

    public int method440() {
        pos += 4;
        return ((payload[pos - 3] & 0xFF) << 24) + ((payload[pos - 4] & 0xFF) << 16) + ((payload[pos - 1] & 0xFF) << 8) + (payload[pos -2] & 0xFF); //Used to be [- 2] now changed to [pos - 2] may not be correct but this is unused anyway
    }

    public int readShort2() {
        pos += 2;
        int i = ((payload[pos - 2] & 0xff) << 8) + (payload[pos - 1] & 0xff);
        if (i > 32767)
            i -= 65537;
        return i;
    }

    public byte readSignedByte() {
        return payload[pos++];
    }

    public int readUnsignedShort() {
        this.pos += 2;
        return ((this.payload[this.pos - 2] & 0xff) << 8) + (this.payload[this.pos - 1] & 0xff);
    }

    public int readUnsignedShort2() {
        this.pos += 2;
        return (this.payload[this.pos - 1] & 255) + ((this.payload[this.pos - 2] & 255) << 8);
    }

    public int readSignedShort() {
        this.pos += 2;
        int value = ((this.payload[this.pos - 2] & 0xff) << 8) + (this.payload[this.pos - 1] & 0xff);
        if (value > 32767)
            value -= 0x10000;

        return value;
    }

    public int readShort() {
        this.pos += 2;
        int value = ((this.payload[this.pos - 2] & 0xff) << 8) + (this.payload[this.pos - 1] & 0xff);

        if (value > 60000)
            value = -65535 + value;
        return value;
    }

    public int readTriByte() {
        pos += 3;
        return ((payload[pos - 3] & 0xff) << 16)
                + ((payload[pos - 2] & 0xff) << 8)
                + (payload[pos - 1] & 0xff);
    }

    public int readInt() {
        pos += 4;
        return ((payload[pos - 4] & 0xff) << 24)
            + ((payload[pos - 3] & 0xff) << 16)
            + ((payload[pos - 2] & 0xff) << 8)
            + (payload[pos - 1] & 0xff);
    }

    public int readInt2() {
        this.pos += 4;
        return ((this.payload[this.pos - 3] & 255) << 16) + (this.payload[this.pos - 1] & 255) + ((this.payload[this.pos - 2] & 255) << 8) + ((this.payload[this.pos - 4] & 255) << 24);
    }

    public int read24Int() {
        pos += 3;
        return ((payload[pos - 3] & 0xff) << 16) + ((payload[pos - 2] & 0xff) << 8) + (payload[pos - 1] & 0xff);
    }

    public long readLong() {
        long msi = (long) readInt() & 0xffffffffL;
        long lsi = (long) readInt() & 0xffffffffL;
        return (msi << 32) + lsi;
    }

    public String readStringCp1252NullTerminated() {
        int var1 = this.pos;

        while (this.payload[++this.pos - 1] != 0) {
        }

        int var2 = this.pos - var1 - 1;
        return var2 == 0 ? "" : decodeStringCp1252(this.payload, var1, var2);
    }

    public static int method8302(int var0) {
        --var0;
        var0 |= var0 >>> 1;
        var0 |= var0 >>> 2;
        var0 |= var0 >>> 4;
        var0 |= var0 >>> 8;
        var0 |= var0 >>> 16;
        return var0 + 1;
    }

    public static final char[] cp1252AsciiExtension = new char[]{
        '€', '\u0000', '‚', 'ƒ', '„', '…', '†', '‡', 'ˆ', '‰', 'Š', '‹', 'Œ', '\u0000', 'Ž', '\u0000',
        '\u0000', '‘', '’', '“', '”', '•', '–', '—', '˜', '™', 'š', '›', 'œ', '\u0000', 'ž', 'Ÿ'
    };

    public String decodeStringCp1252(byte[] var0, int var1, int var2) {
        char[] var3 = new char[var2];
        int var4 = 0;

        for (int var5 = 0; var5 < var2; ++var5) {
            int var6 = var0[var5 + var1] & 255;
            if (var6 != 0) {
                if (var6 >= 128 && var6 < 160) {
                    char var7 = cp1252AsciiExtension[var6 - 128];
                    if (var7 == 0) {
                        var7 = '?';
                    }

                    var6 = var7;
                }

                var3[var4++] = (char) var6;
            }
        }

        return new String(var3, 0, var4);
    }

    public String readString() {
        int index = pos;
        while (payload[pos++] != 10)
            ;
        return new String(payload, index, pos - index - 1);
    }


    public byte[] readBytes() {
        int index = pos;
        while (payload[pos++] != 10)
            ;
        byte data[] = new byte[pos - index - 1];
        System.arraycopy(payload, index, data, index - index, pos - 1 - index);
        return data;
    }

    public byte[] readBytes(int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(this.payload, this.pos, bytes, 0, length);
        this.pos += length;
        return bytes;
    }


    public void readBytes(int offset, int length, byte data[]) {
        for (int index = length; index < length + offset; index++)
            data[index] = payload[pos++];
    }

    public void initBitAccess() {
        bitPosition = pos * 8;
    }

    public int readBits(int amount) {
        int byteOffset = bitPosition >> 3;
        int bitOffset = 8 - (bitPosition & 7);
        int value = 0;
        bitPosition += amount;
        for (; amount > bitOffset; bitOffset = 8) {
            value += (payload[byteOffset++] & BIT_MASKS[bitOffset]) << amount
                    - bitOffset;
            amount -= bitOffset;
        }
        if (amount == bitOffset)
            value += payload[byteOffset] & BIT_MASKS[bitOffset];
        else
            value += payload[byteOffset] >> bitOffset - amount
                    & BIT_MASKS[amount];
        return value;
    }

    public void disableBitAccess() {
        pos = (bitPosition + 7) / 8;
    }

    public int readSignedSmart() {
        int value = this.payload[this.pos] & 0xff;
        if (value < 128)
            return this.readUnsignedByte() - 64;
        else
            return this.readUnsignedShort() - 49152;
    }

    public void setOffset(int offset) {
        this.pos = offset;
    }


    public int getSmart() {
        try {
            // checks current without modifying position
            if (pos >= payload.length) {
                return payload[payload.length - 1] & 0xFF;
            }
            int value = payload[pos] & 0xFF;

            if (value < 128) {
                return readUnsignedByte();
            } else {
                return readUnsignedShort() - 32768;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Client.addReportToServer(e.getMessage());
            return readUnsignedShort() - 32768;
        }
    }

    public int readUSmart() {
        int peek = payload[pos] & 0xFF;
        return peek < 128 ? this.readUnsignedByte() : this.readUnsignedShort() - 0x8000;
    }

    public void encodeRSA(BigInteger exponent, BigInteger modulus) {
        int length = pos;
        pos = 0;
        byte buffer[] = new byte[length];
        readBytes(length, 0, buffer);

        byte rsa[] = buffer;

        if (ClientConstants.ENABLE_RSA) {
            rsa = new BigInteger(buffer).modPow(exponent, modulus)
                    .toByteArray();
        }

        pos = 0;
        writeByte(rsa.length);
        writeBytes(rsa, rsa.length, 0);
    }

    public void writeNegatedByte(int value) {
        payload[pos++] = (byte) (-value);
    }

    public void writeByteS(int value) {
        payload[pos++] = (byte) (128 - value);
    }

    public int readUByteA() {
        return payload[pos++] - 128 & 0xff;
    }

    public int readNegUByte() {
        return -payload[pos++] & 0xff;
    }

    public int readUnsignedByte() {
        return payload[pos++] & 0xff;
    }

    public int readNullableLargeSmart() {
        if (payload[pos] < 0) {
            return this.readInt() & Integer.MAX_VALUE;
        } else {
            int var1 = this.readUShort();
            return var1 == 32767 ? -1 : var1;
        }
    }

    public int readShortSmartSub() {
        int var1 = payload[pos] & 255;
        return var1 < 128 ? this.readUnsignedByte() - 1 : this.readUShort() - 0x8000 - 1;
    }

    public int readUShort() {
        pos += 2;
        return ((payload[pos - 2] & 0xff) << 8) + (payload[pos - 1] & 0xff);
    }

    public int readUnsignedShort3() {
        this.pos += 2;
        return (this.payload[this.pos - 1] & 255) + ((this.payload[this.pos - 2] & 255) << 8);
    }

    public int readUByteS() {
        return 128 - payload[pos++] & 0xff;
    }

    public byte readNegByte() {
        return (byte) -payload[pos++];
    }

    public byte readByteS() {
        return (byte) (128 - payload[pos++]);
    }

    public void writeLEShort(int value) {
        payload[pos++] = (byte) value;
        payload[pos++] = (byte) (value >> 8);
    }

    public void writeShortA(int value) {
        payload[pos++] = (byte) (value >> 8);
        payload[pos++] = (byte) (value + 128);
    }

    public void writeLEShortA(int value) {
        payload[pos++] = (byte) (value + 128);
        payload[pos++] = (byte) (value >> 8);
    }

    public int readLEUShort() {
        pos += 2;
        return ((payload[pos - 1] & 0xff) << 8) + (payload[pos - 2] & 0xff);
    }

    public int readUShortA() {
        pos += 2;
        return ((payload[pos - 2] & 0xff) << 8)
                + (payload[pos - 1] - 128 & 0xff);
    }

    public int readLEUShortA() {
        pos += 2;
        return ((payload[pos - 1] & 0xff) << 8)
                + (payload[pos - 2] - 128 & 0xff);
    }

    public int readLEShort() {
        pos += 2;
        int value = ((payload[pos - 1] & 0xff) << 8) + (payload[pos - 2] & 0xff);

        if (value > 32767) {
            value -= 0x10000;
        }
        return value;
    }

    public int readLEShortA() {
        pos += 2;
        int value = ((payload[pos - 1] & 0xff) << 8) + (payload[pos - 2] - 128 & 0xff);
        if (value > 32767)
            value -= 0x10000;
        return value;
    }

    public int readIntLittleEndian() {
        pos += 4;
        return ((payload[pos - 4] & 0xFF) << 24) + ((payload[pos - 3] & 0xFF) << 16) + ((payload[pos - 2] & 0xFF) << 8) + (payload[pos - 1] & 0xFF);
    }

    public int readMEInt() { // V1
        pos += 4;
        return ((payload[pos - 2] & 0xff) << 24)
                + ((payload[pos - 1] & 0xff) << 16)
                + ((payload[pos - 4] & 0xff) << 8)
                + (payload[pos - 3] & 0xff);
    }

    public int readIMEInt() {
        pos += 4;
        return ((payload[pos - 3] & 0xff) << 24)
                + ((payload[pos - 4] & 0xff) << 16)
                + ((payload[pos - 1] & 0xff) << 8)
                + (payload[pos - 2] & 0xff);
    }

    public void writeReverseDataA(byte data[], int length, int offset) {
        for (int index = (length + offset) - 1; index >= length; index--) {
            payload[pos++] = (byte) (data[index] + 128);
        }
    }

    public void readReverseData(byte data[], int offset, int length) {
        for (int index = (length + offset) - 1; index >= length; index--) {
            data[index] = payload[pos++];
        }
    }

    public void getBytes(int len, int off, byte[] dest) {
        for (int i = off; i < off + len; i++) {
            dest[i] = payload[pos++];
        }
    }

    public void resetPosition() {
        pos = 0;
    }

    public void encryptRSAContent() {
        /* Cache the current position for future use */
        int cachedPosition = pos;

        /* Reset the position */
        pos = 0;

        /* An empty byte array with a capacity of {@code #currentPosition} bytes */
        byte[] decodeBuffer = new byte[cachedPosition];

        /*
         * Gets bytes up to the current position from the buffer and populates
         * the {@code #decodeBuffer}
         */
        getBytes(cachedPosition, 0, decodeBuffer);

        /*
         * The decoded big integer which translates the {@code #decodeBuffer}
         * into a {@link BigInteger}
         */
        BigInteger decodedBigInteger = new BigInteger(decodeBuffer);

        /*
         * This is going to be a mouthful... the encoded {@link BigInteger} is
         * responsible of returning a value which is the value of {@code
         * #decodedBigInteger}^{@link #RSA_EXPONENT} mod (Modular arithmetic can
         * be handled mathematically by introducing a congruence relation on the
         * integers that is compatible with the operations of the ring of
         * integers: addition, subtraction, and multiplication. For a positive
         * integer n, two integers a and b are said to be congruent modulo n)
         * {@link #RSA_MODULES}
         */
        BigInteger encodedBigInteger = decodedBigInteger.modPow(RSA_EXPONENT, RSA_MODULUS);

        /*
         * Returns the value of the {@code #encodedBigInteger} translated to a
         * byte array in big-endian byte-order
         */
        byte[] encodedBuffer = encodedBigInteger.toByteArray();

        /* Reset the position so we can write fresh to the buffer */
        pos = 0;

        /*
         * We put the length of the {@code #encodedBuffer} to the buffer as a
         * standard byte. (Ignore the naming, that really writes a byte...)
         */
        writeByte(encodedBuffer.length);

        /* Put the bytes of the {@code #encodedBuffer} into the buffer. */
        writeBytes(encodedBuffer, encodedBuffer.length, 0);
    }

    public int readUnsignedIntSmartShortCompat() {
        int var1 = 0;

        int var2;
        for (var2 = this.readUSmart(); var2 == 32767; var2 = this.readUSmart()) {
            var1 += 32767;
        }

        var1 += var2;
        return var1;
    }

}
