package com.hazy.cache.anim;

import com.hazy.cache.Archive;
import com.hazy.collection.TempCache;
import com.hazy.entity.model.Model;
import com.hazy.io.Buffer;
import net.runelite.rs.api.RSModel;
import net.runelite.rs.api.RSSpotAnimationDefinition;

public final class SpotAnimation implements RSSpotAnimationDefinition {

    public int gfxId;

    private SpotAnimation(int gfxId) {
        this.gfxId = gfxId;
        this.animationId = -1;
        this.originalModelColors = new short[6];
        this.modifiedModelColors = new short[6];
        this.model_scale_x = 128;
        this.model_scale_y = 128;
    }

    public static SpotAnimation[] spotAnims;

    public static void init(Archive archive) {
        int id;
        Buffer buffer = new Buffer(archive.get("spotanim.dat"));
        int graphicCount = buffer.readUnsignedShort();
        SpotAnimation[] spotAnims = SpotAnimation.spotAnims;
        if (spotAnims == null) {
            spotAnims = SpotAnimation.spotAnims = new SpotAnimation[graphicCount + 1 + 2000];
        }
        for (int i = 0; i <= graphicCount && (id = buffer.readUnsignedShort()) != 65535; ++i) {
            SpotAnimation spotAnim = spotAnims[id];
            if (spotAnim == null) {
                spotAnim = spotAnims[id] = new SpotAnimation(id);
            }
            int dataSize = buffer.readUnsignedShort();
            byte[] data2 = buffer.readBytes(dataSize);
            Buffer dataBuffer = new Buffer(data2);
            spotAnim.decoder(dataBuffer);
            unpack(spotAnim.gfxId);
        }


    }

    public static void unpack(int graphic) {

        if (graphic == 5000) {
            spotAnims[graphic] = new SpotAnimation();
            spotAnims[graphic].gfxId = graphic;
            spotAnims[graphic].animationId = 6960;
            spotAnims[graphic].seq = SeqDefinition.get(6960);
            spotAnims[graphic].modelId = 58928;
            spotAnims[graphic].model_scale_x = 180;
            spotAnims[graphic].model_scale_y = 180;
        }

        if (graphic == 5001) {
            spotAnims[graphic] = new SpotAnimation();
            spotAnims[graphic].gfxId = graphic;
            spotAnims[graphic].animationId = 465;
            spotAnims[graphic].seq = SeqDefinition.get(465);
            spotAnims[graphic].modelId = 58925;
            spotAnims[graphic].model_scale_x = 80;
            spotAnims[graphic].model_scale_y = 80;
        }

        if (graphic == 5002) {
            spotAnims[graphic] = new SpotAnimation();
            spotAnims[graphic].gfxId = graphic;
            spotAnims[graphic].animationId = 7080;
            spotAnims[graphic].seq = SeqDefinition.get(7080);
            spotAnims[graphic].modelId = 58926;
        }

        if (graphic == 5004) {
            spotAnims[graphic] = new SpotAnimation();
            spotAnims[graphic].gfxId = graphic;
            spotAnims[graphic].animationId = 8287;
            spotAnims[graphic].seq = SeqDefinition.get(8287);
            spotAnims[graphic].modelId = 58929;
        }

        if (graphic == 5005) {
            spotAnims[graphic] = new SpotAnimation();
            spotAnims[graphic].gfxId = graphic;
            spotAnims[graphic].animationId = 5358;
            spotAnims[graphic].seq = SeqDefinition.get(5358);
            spotAnims[graphic].modelId = 58930;
        }

        if (graphic == 10006) {
            spotAnims[graphic] = new SpotAnimation();
            spotAnims[graphic].gfxId = graphic;
            spotAnims[graphic].modelId = 3479;
            spotAnims[graphic].animationId = 1061;
            spotAnims[graphic].seq = SeqDefinition.get(1061);
            spotAnims[graphic].ambient = 50;
            spotAnims[graphic].contrast = 50;
            spotAnims[graphic].originalModelColors = new short[]{960};
            spotAnims[graphic].modifiedModelColors = new short[]{-12535};
        }

        if (graphic == 10004) {
            spotAnims[graphic] = new SpotAnimation();
            spotAnims[graphic].gfxId = graphic;
            spotAnims[graphic].modelId = 42604;
            spotAnims[graphic].ambient = 40;
            spotAnims[graphic].contrast = 120;
            spotAnims[graphic].originalModelColors = new short[]{-32539, 127};
            spotAnims[graphic].modifiedModelColors = new short[]{-32535, -32541};
        }

        if (graphic == 10005) {
            spotAnims[graphic] = new SpotAnimation();
            spotAnims[graphic].gfxId = graphic;
            spotAnims[graphic].modelId = 42603;
            spotAnims[graphic].animationId = 366;
            spotAnims[graphic].seq = SeqDefinition.get(366);
            spotAnims[graphic].ambient = 40;
            spotAnims[graphic].contrast = 120;
            spotAnims[graphic].originalModelColors = new short[]{-32539, 127};
            spotAnims[graphic].modifiedModelColors = new short[]{-32535, -32541};
        }
        if (graphic == 2283) {
            System.out.println(spotAnims[graphic].seq.animId + " " + spotAnims[graphic].seq);
        }

        if (graphic == 61) {
            spotAnims[graphic] = new SpotAnimation();
            spotAnims[graphic].gfxId = graphic;
            spotAnims[graphic].modelId = 69837;
        }

        if (graphic == 60) {
            spotAnims[graphic] = new SpotAnimation();
            spotAnims[graphic].gfxId = graphic;
            spotAnims[graphic].modelId = 69837;
            spotAnims[graphic].animationId = 366;
            spotAnims[graphic].seq = SeqDefinition.get(366);
            spotAnims[graphic].ambient = 40;
            spotAnims[graphic].contrast = 120;
        }
    }


    public void decoder(Buffer buffer) {
        while (true) {
            int opcode = buffer.readUnsignedByte();
            if (opcode == 0) {
                break;
            }

            this.decodeNext(buffer, opcode);
        }
    }

    public void decodeNext(Buffer var1, int var2) {
        if (var2 == 1) {
            modelId = var1.readUnsignedShort();
        } else if (var2 == 2) {
            animationId = var1.readUnsignedShort();
            seq = SeqDefinition.get(animationId);
        } else if (var2 == 4) {
            model_scale_x = var1.readUnsignedShort();
        } else if (var2 == 5) {
            model_scale_y = var1.readUnsignedShort();
        } else if (var2 == 6) {
            rotation = var1.readUnsignedShort();
        } else if (var2 == 7) {
            ambient = var1.readUnsignedByte();
        } else if (var2 == 8) {
            contrast = var1.readUnsignedByte();
        } else if (var2 == 40) {

            int var3 = var1.readUnsignedByte();
            originalModelColors = new short[var3];
            modifiedModelColors = new short[var3];

            for (int var4 = 0; var4 < var3; ++var4) {
                originalModelColors[var4] = (short) var1.readUnsignedShort();
                modifiedModelColors[var4] = (short) var1.readUnsignedShort();
            }
        } else if (var2 == 41) {
            int var3 = var1.readUnsignedByte();
            textureToFind = new short[var3];
            textureToReplace = new short[var3];

            for (int var4 = 0; var4 < var3; ++var4) {
                textureToFind[var4] = (short) var1.readUnsignedShort();
                textureToReplace[var4] = (short) var1.readUnsignedShort();
            }
        }
    }

    public final Model get(int var1) {
        Model var2 = this.get_model();
        if (var2 == null) {
            return null;
        } else {
            Model var3;
            if (this.seq.animId != -1 && var1 != -1) {
                var3 = Model.get(this.modelId).toSharedSpotAnimationModel(true);
            } else {
                var3 = var2.toSharedSpotAnimationModel(true);
            }

            if (this.model_scale_x != 128 || this.model_scale_y != 128) {
                var3.translate(this.model_scale_x, this.model_scale_y, this.model_scale_x);
            }

            if (this.rotation != 0) {
                if (this.rotation == 90) {
                    var3.rotateY90Ccw();
                }

                if (this.rotation == 180) {
                    var3.rotateY90Ccw();
                    var3.rotateY90Ccw();
                }

                if (this.rotation == 270) {
                    var3.rotateY90Ccw();
                    var3.rotateY90Ccw();
                    var3.rotateY90Ccw();
                }
            }

            return var3;
        }
    }

    public Model get_model() {
        Model model = (Model) model_cache.get(gfxId);
        if (model != null)
            return model;

        model = Model.get(modelId);
        if (model == null)
            return null;

        if (originalModelColors != null) {
            for (int index = 0; index < originalModelColors.length; index++)
                if (originalModelColors[0] != 0)
                    model.recolor(originalModelColors[index], modifiedModelColors[index]);

        }
        if (textureToFind != null) {
            for (int index = 0; index < textureToFind.length; index++)
                if (textureToFind[0] != 0)
                    model.retexture(textureToFind[index], textureToReplace[index]);
        }

        model_cache.put(model, gfxId);
        return model;
    }

    public SpotAnimation() {

    }

    public int modelId;
    public int animationId = -1;
    public SeqDefinition seq;
    public short[] originalModelColors;
    public short[] modifiedModelColors;
    public short[] textureToFind;
    public short[] textureToReplace;

    public int model_scale_x = 128;
    public int model_scale_y = 128;
    public int rotation = 0;
    public int ambient = 0;
    public int contrast = 0;
    public static TempCache model_cache = new TempCache(30);

    @Override
    public RSModel getModel(int var1) {
        return get_model();
    }

    @Override
    public void setRecolorFrom(short[] from) {
        originalModelColors = from;
    }

    @Override
    public void setRecolorTo(short[] to) {
        modifiedModelColors = to;
    }

    @Override
    public int getSequence() {
        return seq.animId;
    }
}
