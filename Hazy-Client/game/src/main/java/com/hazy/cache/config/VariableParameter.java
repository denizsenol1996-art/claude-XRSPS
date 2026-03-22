package com.hazy.cache.config;

import com.hazy.cache.Archive;
import com.hazy.io.Buffer;

/**
 * Varps are used for inteface configuration ids and their functions, out of the current 725 config ids, only 9 or so of them are used.
 *
 */
public final class VariableParameter {

    public static VariableParameter[] values;

    private static int currentIndex;
    private static int[] configIds;
    public int type;
    public boolean aBoolean713;

    private VariableParameter() {
        aBoolean713 = false;
    }

    public static void init(Archive archive) {
        Buffer buffer = new Buffer(archive.get("varp.dat"));

        currentIndex = 0;

        final int actualSize = buffer.readUnsignedShort();

        /**
         * Cache size is 725.
         * But instead of setting array sizes to 725, we set it to 1200.
         * This leaves space for custom configs.
         */
        int customSize = 1200;

        if (values == null) {
            values = new VariableParameter[customSize];
        }

        if (configIds == null) {
            configIds = new int[customSize];
        }

        for (int index = 0; index < customSize; index++) {
            if (values[index] == null) {
                values[index] = new VariableParameter();
            }

            if (index < actualSize) {
                values[index].decode(buffer, index);
            }
        }

        if (buffer.pos != buffer.payload.length) {
            System.err.println("varptype load mismatch");
        }

    }

    private void decode(Buffer buffer, int index) {
        do {
            int opcode = buffer.readUnsignedByte();

            if (opcode == 0) {
                return;
            }

            if (opcode == 1) {
                buffer.readUnsignedByte();
            } else if (opcode == 2) {
                buffer.readUnsignedByte();
            } else if (opcode == 3) {
                configIds[currentIndex++] = index;
            } else if (opcode == 4) {
            } else if (opcode == 5) {
                type = buffer.readUnsignedShort();
            } else if (opcode == 6) {
            } else if (opcode == 7) {
                buffer.readInt();
            } else if (opcode == 8) {
                aBoolean713 = true;
            } else if (opcode == 10) {
                buffer.readString();
            } else if (opcode == 11) {
                aBoolean713 = true;
            } else if (opcode == 12) {
                buffer.readInt();
            } else if (opcode == 13) {
            } else {
                System.err.println("Error unrecognised config code: " + opcode);
            }
        } while (true);
    }
}
