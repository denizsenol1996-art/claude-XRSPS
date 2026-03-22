package com.hazy.entity.model;

import com.hazy.cache.Archive;
import com.hazy.io.Buffer;

public final class IdentityKit {

    public static int length;
    public static IdentityKit[] cache;
    public int bodyPartId = -1;
    private int[] models;
    private int[] recolorToFind = new int[6];
    private int[] recolorToReplace = new int[6];
    public short[] retextureToFind;
    public short[] retextureToReplace;
    private final int[] chatheadModels = { -1, -1, -1, -1, -1 };
    public boolean nonSelectable;

    private IdentityKit() {
    }

    public static void init(Archive archive) {
        Buffer buffer = new Buffer(archive.get("idk.dat"));
        length = buffer.readUnsignedShort();

        if (cache == null) {
            cache = new IdentityKit[length];
        }

        for (int index = 0; index < length; index++) {
            if (cache[index] == null) {
                cache[index] = new IdentityKit();
            }

            IdentityKit kit = cache[index];
            kit.load(buffer);
        }
    }

    public void load(Buffer is)
    {
        while (true) {
            final int opcode = is.readUnsignedByte();

            if (opcode == 0) {
                break;
            }

            if (opcode == 1)
            {
                bodyPartId = is.readUnsignedByte();
            }
            else if (opcode == 2)
            {
                int length = is.readUnsignedByte();
                models = new int[length];

                for (int index = 0; index < length; ++index)
                {
                    models[index] = is.readUnsignedShort();
                }
            }
            else if (opcode == 3)
            {
                nonSelectable = true;
            }
            else if (opcode == 40)
            {
                int length = is.readUnsignedByte();
                recolorToFind = new int[length];
                recolorToReplace = new int[length];

                for (int index = 0; index < length; ++index)
                {
                    recolorToFind[index] = is.readShort();
                    recolorToReplace[index] = is.readShort();
                }
            }
            else if (opcode == 41)
            {
                int length = is.readUnsignedByte();
                retextureToFind = new short[length];
                retextureToReplace = new short[length];

                for (int index = 0; index < length; ++index)
                {
                    retextureToFind[index] = (short) is.readShort();
                    retextureToReplace[index] = (short) is.readShort();
                }
            }
            else if (opcode >= 60 && opcode < 70)
            {
                chatheadModels[opcode - 60] = is.readUnsignedShort();
            }
        }
    }

    public boolean body_cached() {
        if (models == null) {
            return true;
        }
        boolean ready = true;
        for (int part = 0; part < models.length; part++) {
            if (!Model.cached(models[part]))
                ready = false;
        }
        return ready;
    }

    public Model get_body() {
        if (models == null) {
            return null;
        }

        Model[] models = new Model[this.models.length];

        for (int part = 0; part < this.models.length; part++) {
            models[part] = Model.get(this.models[part]);
        }

        Model model;
        if (models.length == 1) {
            model = models[0];
        } else {
            model = new Model(models.length, models);
        }

        for (int part = 0; part < 6; part++) {
            if (recolorToFind[part] == 0) {
                break;
            }
            model.recolor(recolorToFind[part], recolorToReplace[part]);
        }

        model.recolor(55232, 6804);
        return model;
    }

    public boolean headLoaded() {
        boolean ready = true;
        for (int part = 0; part < 5; part++) {
            if (chatheadModels[part] != -1 && !Model.cached(chatheadModels[part])) {
                ready = false;
            }
        }
        return ready;
    }

    public Model get_head() {
        Model[] models = new Model[5];
        int count = 0;
        for (int part = 0; part < 5; part++) {
            if (chatheadModels[part] != -1) {
                models[count++] = Model.get(chatheadModels[part]);
            }
        }

        Model model = new Model(count, models);
        for (int part = 0; part < 6; part++) {
            if (recolorToFind[part] == 0) {
                break;
            }
            model.recolor(recolorToFind[part], recolorToReplace[part]);
        }
        return model;
    }
}
