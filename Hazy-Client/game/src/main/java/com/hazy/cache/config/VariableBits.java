package com.hazy.cache.config;

import com.hazy.ClientConstants;
import com.hazy.cache.Archive;
import com.hazy.io.Buffer;
import com.hazy.util.FileUtils;

public final class VariableBits {

    public static VariableBits[] cache;
    public int configId;
    public int leastSignificantBit;
    public int mostSignificantBit;
    private final boolean aBoolean651;

     public static void init(Archive archive) {
        Buffer stream = new Buffer(archive.get("varbit.dat"));
        int size = stream.readUnsignedShort();

        if (cache == null) {
            cache = new VariableBits[size];
        }

         System.out.printf("Loaded %d varbits loading OSRS version %d and SUB version %d%n", size, ClientConstants.OSRS_DATA_VERSION, ClientConstants.OSRS_DATA_SUB_VERSION);

        for (int index = 0; index < size; index++) {
            if (cache[index] == null) {
                cache[index] = new VariableBits();
            }

            cache[index].load(stream);

            if (cache[index].aBoolean651) {
                VariableParameter.values[cache[index].configId].aBoolean713 = true;
            }
        }

        if (stream.pos != stream.payload.length) {
            System.err.println("varbit load mismatch");
        }

    }

    private void load(Buffer buffer)
    {
        for (;;)
        {
            int opcode = buffer.readUnsignedByte();
            if (opcode == 0)
            {
                break;
            }

            if (opcode == 1)
            {
                configId = (buffer.readUnsignedShort2());
                leastSignificantBit = (buffer.readUnsignedByte());
                mostSignificantBit = (buffer.readUnsignedByte());
            }
        }
    }

    private VariableBits() {
        aBoolean651 = false;
    }

}
