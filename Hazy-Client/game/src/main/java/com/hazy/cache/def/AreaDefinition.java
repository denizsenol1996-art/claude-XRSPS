package com.hazy.cache.def;

import com.hazy.Client;
import com.hazy.cache.Archive;
import com.hazy.cache.graphics.SimpleImage;
import com.hazy.io.Buffer;

import java.util.HashMap;


public final class AreaDefinition {

    public static int size;
    public int[] field3292;
    public static int mapFunctionsSize;
    public static AreaDefinition[] cache;
    public static HashMap<Integer, SimpleImage> sprites = new HashMap<>();
    private static int cacheIndex;
    private static Buffer area_data;
    private static int[] indices;

    public int id;
    public int spriteId = -1;
    public int field3294 = -1;
    public String name = "";
    public int fontColor = -1;
    public int field3297 = -1;
    public String[] actions;
    public int field3310 = -1;

    public int field3296;

    public String[] field3298 = new String[5];
    public int[] field3300;
    public String field3308;
    public byte[] field3309;

    public static void init(Archive archive) {
        area_data = new Buffer(
            archive.get("areas.dat")
        );
        Buffer stream = new Buffer(
            archive.get("areas.idx")
        );

        size = stream.readUnsignedShort();
        System.out.println("areas "+size);

        indices = new int[size];
        int offset = 2;

        for (int _ctr = 0; _ctr < size; _ctr++) {
            indices[_ctr] = offset;
            offset += stream.readUnsignedShort();
        }

        cache = new AreaDefinition[10];

        for (int _ctr = 0; _ctr < 10; _ctr++) {
            cache[_ctr] = new AreaDefinition();
        }

        System.out.println("Areas read -> " + size);

    }

    public static SimpleImage getImage(int sprite) {
        return sprites.get(sprite);
    }

    public static AreaDefinition lookup(int area) {
        for (int count = 0; count < 10; count++) {
            if (cache[count].id == area) {
                return cache[count];
            }
        }
        cacheIndex = (cacheIndex + 1) % 10;
        AreaDefinition data = cache[cacheIndex];
        if (area >= 0) {
            area_data.pos = indices[area];
            while (true) {
                int opcode = area_data.readUnsignedByte(); // show me what we're looking at
                if (opcode == 0)
                    break;
                data.processOpcode(area_data, opcode);
            }
            if (!sprites.containsKey(data.spriteId)) {
                try {
                    sprites.put(data.spriteId, new SimpleImage(Client.instance.mediaStreamLoader, "mapfunction", data.spriteId));
                } catch (Exception e) {
                    System.out.println("Missing Sprite: " + data.spriteId + " Using Shop Icon");
                    sprites.put(data.spriteId, new SimpleImage(Client.instance.mediaStreamLoader, "mapfunction", 0));
                }
            }
        }
        return data;
    }


    private AreaDefinition() {
        id = -1;
    }

    public static void clear() {
        indices = null;
        cache = null;
        area_data = null;
    }

    private void processOpcode(Buffer in, int opcode)
    {
        if (opcode == 1)
        {
            spriteId = in.readBigSmart2();
        }
        else if (opcode == 2)
        {
            field3294 = in.readBigSmart2();
        }
        else if (opcode == 3)
        {
            name = in.readString();
        }
        else if (opcode == 4)
        {
            field3296 = in.read24Int();
        }
        else if (opcode == 5)
        {
            in.read24Int();
        }
        else if (opcode == 6)
        {
            field3310 = in.readUnsignedByte();
        }
        else if (opcode == 7)
        {
            int var3 = in.readUnsignedByte();
            if ((var3 & 1) == 0)
            {
                ;
            }

            if ((var3 & 2) == 2)
            {
                ;
            }
        }
        else if (opcode == 8)
        {
            in.readUnsignedByte();
        }
        else if (opcode >= 10 && opcode <= 14)
        {
            field3298[opcode - 10] = in.readString();
        }
        else if (opcode == 15)
        {
            int var3 = in.readUnsignedByte();
            field3300 = new int[var3 * 2];

            int var4;
            for (var4 = 0; var4 < var3 * 2; ++var4)
            {
                field3300[var4] = in.readShort();
            }

            in.readInt();
            var4 = in.readUnsignedByte();
            field3292 = new int[var4];

            int var5;
            for (var5 = 0; var5 < field3292.length; ++var5)
            {
                field3292[var5] = in.readInt();
            }

            field3309 = new byte[var3];

            for (var5 = 0; var5 < var3; ++var5)
            {
                field3309[var5] = in.readByte();
            }
        }
        else if (opcode == 16)
        {

        }
        else if (opcode == 17)
        {
            field3308 = in.readString();
        }
        else if (opcode == 18)
        {
            in.readBigSmart2();
        }
        else if (opcode == 19)
        {
            field3297 = in.readUnsignedShort();
        }
        else if (opcode == 21)
        {
            in.readInt();
        }
        else if (opcode == 22)
        {
            in.readInt();
        }
        else if (opcode == 23)
        {
            in.readUnsignedByte();
            in.readUnsignedByte();
            in.readUnsignedByte();
        }
        else if (opcode == 24)
        {
            in.readShort();
            in.readShort();
        }
        else if (opcode == 25)
        {
            in.readBigSmart2();
        }
        else if (opcode == 28)
        {
            in.readUnsignedByte();
        }
        else if (opcode == 29)
        {
            in.readUnsignedByte();
//			class257[] var6 = new class257[]
//			{
//				class257.field3538, class257.field3539, class257.field3540
//			};
//			this.field3299 = (class257) Item.method1751(var6, var1.readUnsignedByte());
        }
        else if (opcode == 30)
        {
            in.readUnsignedByte();
//			class239[] var7 = new class239[]
//			{
//				class239.field3273, class239.field3275, class239.field3271
//			};
//			this.field3306 = (class239) Item.method1751(var7, var1.readUnsignedByte());
        }

    }

    public void readValues(Buffer buffer) {
        do {
            int opCode = buffer.readUnsignedByte();
            if (opCode == 0)
                return;
            if (opCode == 1)
                spriteId = buffer.readBigSmart2();
            else if (opCode == 2)
                field3294 = buffer.readBigSmart2();
            else if (opCode == 3)
                name = buffer.readString();
            else if (opCode == 4)
                fontColor = buffer.read24Int();
            else if (opCode == 5)
                field3297 = buffer.read24Int();
            else if (opCode == 6)
                fontColor = buffer.readUnsignedByte();

        } while (true);
    }
}
