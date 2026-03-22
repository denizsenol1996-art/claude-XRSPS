package com.hazy.cache.def;

import com.hazy.Client;
import com.hazy.ClientConstants;
import com.hazy.cache.Archive;
import com.hazy.cache.anim.SeqFrame;
import com.hazy.cache.config.VariableBits;
import com.hazy.cache.def.impl.ObjectManager;
import com.hazy.collection.TempCache;
import com.hazy.entity.model.Model;
import com.hazy.io.Buffer;
import com.hazy.net.requester.ResourceProvider;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.IterableHashTable;
import net.runelite.api.Node;
import net.runelite.rs.api.RSBuffer;
import net.runelite.rs.api.RSIterableNodeHashTable;
import net.runelite.rs.api.RSObjectComposition;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Setter
public final class ObjectDefinition implements RSObjectComposition {

    public static final int REV_220_OBJ_ARCHIVE_REV = 1673;

    private boolean rev220SoundData = true;
    private boolean deferAnimChange;
    private int ambientSoundRetain;

    public static void init(Archive archive) {
        data_buffer = new Buffer(archive.get("loc.dat"));
        Buffer idx = new Buffer(archive.get("loc.idx"));
        int highestFileId = idx.readUnsignedShort();
        stream_indices = new int[highestFileId + 1];
        int i = 0;
        for (int j = 0; j <= highestFileId; j++) {
            final int size = idx.readUnsignedShort();
            if (size == -1 || size == 65535) {
                break;
            }
            stream_indices[j] = i;
            i += size;
        }
        cache = new ObjectDefinition[20];
        for (int index = 0; index < 20; index++)
            cache[index] = new ObjectDefinition();

    }

    private int category;
    private int ambientSoundId;
    private int int2083;
    private int ambientSoundChangeTicksMin;
    private int ambientSoundChangeTicksMax;
    private int ambientSoundDistance;
    public int[] ambientSoundIds;
    private boolean randomizeAnimStart;
    private Map<Integer, Object> params = null;


    public void post_decode() {
        if (wallOrDoor == -1) {
            wallOrDoor = 0;
            if (objectModels != null && (objectTypes == null || objectTypes[0] == 10))
                wallOrDoor = 1;

            for (int index = 0; index < 5; index++) {
                if (scene_actions[index] != null) {
                    wallOrDoor = 1;
                }
            }
        }

        if (supportsItems == -1) supportsItems = interactType != 0 ? 1 : 0;
    }

    void decode(Buffer buffer) {
        while (true) {
            int op = buffer.readUnsignedByte();
            if (op == 0)
                break;

            processOp(op, buffer);
        }
    }


    private void processOp(int opcode, Buffer is)
    {
        if (opcode == 1)
        {
            int length = is.readUnsignedByte();
            if (length > 0)
            {
                int[] objectTypes = new int[length];
                int[] objectModels = new int[length];

                for (int index = 0; index < length; ++index)
                {
                    objectModels[index] = is.readUnsignedShort();
                    objectTypes[index] = is.readUnsignedByte();
                }

                setObjectTypes(objectTypes);
                setObjectModels(objectModels);
            }
        }
        else if (opcode == 2)
        {
            setName(is.readStringCp1252NullTerminated());
        }
        else if (opcode == 5)
        {
            int length = is.readUnsignedByte();
            if (length > 0)
            {
                setObjectTypes(null);
                int[] objectModels = new int[length];

                for (int index = 0; index < length; ++index)
                {
                    objectModels[index] = is.readUnsignedShort();
                }

                setObjectModels(objectModels);
            }
        }
        else if (opcode == 14)
        {
            setWidth(is.readUnsignedByte());
        }
        else if (opcode == 15)
        {
            setHeight(is.readUnsignedByte());
        }
        else if (opcode == 17)
        {
            setInteractType(0);
            setBlocksProjectile(false);
        }
        else if (opcode == 18)
        {
            setBlocksProjectile(false);
        }
        else if (opcode == 19)
        {
            setWallOrDoor(is.readUnsignedByte());
        }
        else if (opcode == 21)
        {
            setContouredGround(true);
        }
        else if (opcode == 22)
        {
            setMergeNormals(true);
        }
        else if (opcode == 23)
        {
            setABool2111(true);
        }
        else if (opcode == 24)
        {
            setAnimationId(is.readUnsignedShort());
            if (getAnimationId() == 0xFFFF)
            {
                setAnimationId(-1);
            }
        }
        else if (opcode == 27)
        {
            setInteractType(1);
        }
        else if (opcode == 28)
        {
            setDecorDisplacement(is.readUnsignedByte());
        }
        else if (opcode == 29)
        {
            setAmbient(is.readByte());
        }
        else if (opcode == 39)
        {
            setContrast(is.readByte() * 25);
        }
        else if (opcode >= 30 && opcode < 35)
        {
            String[] actions = getActions();
            actions[opcode - 30] = is.readStringCp1252NullTerminated();
            if (actions[opcode - 30].equalsIgnoreCase("Hidden"))
            {
                actions[opcode - 30] = null;
            }
        }
        else if (opcode == 40)
        {
            int length = is.readUnsignedByte();
            int[] recolorToFind = new int[length];
            int[] recolorToReplace = new int[length];

            for (int index = 0; index < length; ++index)
            {
                recolorToFind[index] = (short) is.readShort();
                recolorToReplace[index] = (short) is.readShort();
            }

            setRecolorToFind(recolorToFind);
            setRecolorToReplace(recolorToReplace);
        }
        else if (opcode == 41)
        {
            int length = is.readUnsignedByte();
            short[] retextureToFind = new short[length];
            short[] textureToReplace = new short[length];

            for (int index = 0; index < length; ++index)
            {
                retextureToFind[index] = (short) is.readShort();
                textureToReplace[index] = (short) is.readShort();
            }

            setRetextureToFind(retextureToFind);
            setTextureToReplace(textureToReplace);
        }
        else if (opcode == 61)
        {
            setCategory(is.readUnsignedShort());
        }
        else if (opcode == 62)
        {
            setRotated(true);
        }
        else if (opcode == 64)
        {
            setShadow(false);
        }
        else if (opcode == 65)
        {
            setScaleX(is.readUnsignedShort());
        }
        else if (opcode == 66)
        {
            setModelSizeHeight(is.readUnsignedShort());
        }
        else if (opcode == 67)
        {
            setScaleZ(is.readUnsignedShort());
        }
        else if (opcode == 68)
        {
            setMapSceneId(is.readUnsignedShort());
        }
        else if (opcode == 69)
        {
            setBlockingMask(is.readByte());
        }
        else if (opcode == 70)
        {
            setTranslateX(is.readUnsignedShort());
        }
        else if (opcode == 71)
        {
            setTranslateY(is.readUnsignedShort());
        }
        else if (opcode == 72)
        {
            setTranslateZ(is.readUnsignedShort());
        }
        else if (opcode == 73)
        {
            setObstructsGround(true);
        }
        else if (opcode == 74)
        {
            setHollow(true);
        }
        else if (opcode == 75)
        {
            setSupportsItems(is.readUnsignedByte());
        }
        else if (opcode == 77)
        {
            int varpID = is.readUnsignedShort();
            if (varpID == 0xFFFF)
            {
                varpID = -1;
            }
            setVarbitID(varpID);

            int configId = is.readUnsignedShort();
            if (configId == 0xFFFF)
            {
                configId = -1;
            }
            setVarpID(configId);

            int length = is.readUnsignedByte();
            int[] configChangeDest = new int[length + 2];

            for (int index = 0; index <= length; ++index)
            {
                configChangeDest[index] = is.readUnsignedShort();
                if (0xFFFF == configChangeDest[index])
                {
                    configChangeDest[index] = -1;
                }
            }

            configChangeDest[length + 1] = -1;

            setConfigChangeDest(configChangeDest);
        }
        else if (opcode == 78)
        {
            setAmbientSoundId(is.readUnsignedShort());
            setAmbientSoundDistance(is.readUnsignedByte());
            if (rev220SoundData)
            {
                setAmbientSoundRetain(is.readUnsignedByte());
            }
        }
        else if (opcode == 79)
        {
            setAmbientSoundChangeTicksMin(is.readUnsignedShort());
            setAmbientSoundChangeTicksMax(is.readUnsignedShort());
            setAmbientSoundDistance(is.readUnsignedByte());
            if (rev220SoundData)
            {
                setAmbientSoundRetain(is.readUnsignedByte());
            }
            int length = is.readUnsignedByte();
            int[] ambientSoundIds = new int[length];

            for (int index = 0; index < length; ++index)
            {
                ambientSoundIds[index] = is.readUnsignedShort();
            }

            setAmbientSoundIds(ambientSoundIds);
        }
        else if (opcode == 81)
        {
            is.readUnsignedByte();
        }
        else if (opcode == 82)
        {
            setMapAreaId(is.readUnsignedShort());
        }
        else if (opcode == 89)
        {
            setRandomizeAnimStart(true);
        }
        else if (opcode == 90)
        {
            setDeferAnimChange(true);
        }
        else if (opcode == 92)
        {
            int varpID = is.readUnsignedShort();
            if (varpID == 0xFFFF)
            {
                varpID = -1;
            }
            setVarbitID(varpID);

            int configId = is.readUnsignedShort();
            if (configId == 0xFFFF)
            {
                configId = -1;
            }
            setVarpID(configId);


            int var = is.readUnsignedShort();
            if (var == 0xFFFF)
            {
                var = -1;
            }

            int length = is.readUnsignedByte();
            int[] configChangeDest = new int[length + 2];

            for (int index = 0; index <= length; ++index)
            {
                configChangeDest[index] = is.readUnsignedShort();
                if (0xFFFF == configChangeDest[index])
                {
                    configChangeDest[index] = -1;
                }
            }

            configChangeDest[length + 1] = var;

            setConfigChangeDest(configChangeDest);
        }
        else if (opcode == 249)
        {
            int length = is.readUnsignedByte();

            Map<Integer, Object> params = new HashMap<>(length);
            for (int i = 0; i < length; i++)
            {
                boolean isString = is.readUnsignedByte() == 1;
                int key = is.read24BitInt();
                Object value;

                if (isString)
                {
                    value = is.readStringCp1252NullTerminated();
                }

                else
                {
                    value = is.readInt();
                }

                params.put(key, value);
            }
        }
        else
        {
            log.warn("Unrecognized opcode {}", opcode);
        }
    }

    public static ObjectDefinition get(int id) {

        if (id > stream_indices.length)
            id = stream_indices.length - 1;
        for (int j = 0; j < 20; j++)
            if (cache[j].id == id)
                return cache[j];
        cache_index = (cache_index + 1) % 20;

        ObjectDefinition def = cache[cache_index];
        data_buffer.pos = stream_indices[id];
        def.id = id;
        def.set_defaults();
        def.decode(data_buffer);
        def.post_decode();
        if (def.id == 29308)//wintertoldt snow storm 1639 // 3997 cheap fix
            def.mergeNormals = false;

        if (def.id >= 29167 && def.id <= 29225) {
            def.width = 1;
            def.interactType = 0;
            def.scene_actions = new String[]{"Take", null, null, null, null};
        }

        if (def.id == 34683) {
            def.interactType = 0;
            def.scene_actions = new String[]{"Teleport", null, null, null, null};
        }

        if (def.id == 70000) {
            def.name = "Summer Pool";
            def.scene_actions = new String[]{"Sacrifice", null, null, null, null};
            def.objectModels = new int[]{69842};
        }

        if (def.id == 13322) {
            def.interactType = 0;
        }

        if (def.id == 14924) {
            def.width = 1;
        }

        if (ClientConstants.WILDERNESS_DITCH_DISABLED) {
            if (id == 23271) {
                def.objectModels = null;
                def.wallOrDoor = 0;
                def.interactType = 0;
                return def;
            }
        }

        if (def.blocksProjectile) {
            def.interactType = 0;
            def.blocksProjectile = false;
        }

        ObjectManager.get(id);

        return def;
    }

    public void set_defaults() {
        objectModels = null;
        objectTypes = null;
        name = null;
        description = null;
        recolorToFind = null;
        recolorToReplace = null;
        textureToReplace = null;
        retextureToFind = null;
        width = 1;
        height = 1;
        interactType = 2;
        blocksProjectile = true;
        wallOrDoor = -1;
        contouredGround = false;
        mergeNormals = false;
        aBool2111 = false;
        animationID = -1;
        decorDisplacement = 16;
        ambient = 0;
        contrast = 0;
        mapAreaId = -1;
        mapsceneID = -1;
        rotated = false;
        shadow = true;
        scaleX = 128;
        modelSizeHeight = 128;
        scaleZ = 128;
        scene_actions = new String[5];
        blockingMask = 0;
        translateX = 0;
        translateY = 0;
        translateZ = 0;
        obstructsGround = false;
        hollow = false;
        supportsItems = -1;
        varbitID = -1;
        varpID = -1;
        configChangeDest = null;
    }

    public void passive_request_load(ResourceProvider provider) {
        if (objectModels == null)
            return;

        for (int index = 0; index < objectModels.length; index++)
            provider.passive_request(objectModels[index] & 0xffff, 0);

    }

    public Model modelAt(int type, int orientation, int aY, int bY, int cY, int dY, int frameId) {
        Model model = model(type, frameId, orientation);
        if (model == null)
            return null;
        if (contouredGround || mergeNormals)
            model = new Model(contouredGround, mergeNormals, model);
        if (contouredGround) {
            int y = (aY + bY + cY + dY) / 4;
            for (int vertex = 0; vertex < model.verticesCount; vertex++) {
                int x = model.verticesX[vertex];
                int z = model.verticesZ[vertex];
                int l2 = aY + ((bY - aY) * (x + 64)) / 128;
                int i3 = dY + ((cY - dY) * (x + 64)) / 128;
                int j3 = l2 + ((i3 - l2) * (z + 64)) / 128;
                model.verticesY[vertex] += j3 - y;
            }

            model.normalise();
            model.resetBounds();
        }
        return model;
    }


    public boolean group_cached(int type) {
        if (objectTypes == null) {
            if (objectModels == null)
                return true;

            if (type != 10)
                return true;

            boolean cached = true;
            for (int index = 0; index < objectModels.length; index++)
                cached &= Model.cached(objectModels[index]);

            return cached;
        }
        for (int index = 0; index < objectTypes.length; index++)
            if (objectTypes[index] == type)
                return Model.cached(objectModels[index]);

        return true;
    }

    public boolean cached() {
        if (objectModels == null)
            return true;

        boolean cached = true;
        for (int model_id : objectModels) cached &= Model.cached(model_id);

        return cached;
    }

    public ObjectDefinition get_configs() {
        int setting_id = -1;
        if (varbitID != -1) {
            VariableBits bit = VariableBits.cache[varbitID];
            int setting = bit.configId;
            int low = bit.leastSignificantBit;
            int high = bit.mostSignificantBit;
            int mask = Client.BIT_MASKS[high - low];
            setting_id = Client.instance.settings[setting] >> low & mask;
        } else if (varpID != -1)
            setting_id = Client.instance.settings[varpID];

        if (setting_id < 0 || setting_id >= configChangeDest.length || configChangeDest[setting_id] == -1)
            return null;
        else
            return get(configChangeDest[setting_id]);
    }

    public Model model(int j, int k, int l) {
        Model model = null;
        long l1;
        if (objectTypes == null) {
            if (j != 10)
                return null;
            l1 = (long) ((id << 6) + l) + ((long) (k + 1) << 32);
            Model model_1 = (Model) model_cache.get(l1);
            if (model_1 != null) {
                return model_1;
            }
            if (objectModels == null)
                return null;
            boolean flag1 = rotated ^ (l > 3);
            int k1 = objectModels.length;
            for (int i2 = 0; i2 < k1; i2++) {
                int l2 = objectModels[i2];
                if (flag1)
                    l2 += 0x10000;
                model = (Model) animated_model_cache.get(l2);
                if (model == null) {
                    model = Model.get(l2 & 0xffff);
                    if (model == null)
                        return null;
                    if (flag1)
                        model.invert();
                    animated_model_cache.put(model, l2);
                }
                if (k1 > 1)
                    models[i2] = model;
            }

            if (k1 > 1)
                model = new Model(k1, models);
        } else {
            int i1 = -1;
            for (int j1 = 0; j1 < objectTypes.length; j1++) {
                if (objectTypes[j1] != j)
                    continue;
                i1 = j1;
                break;
            }

            if (i1 == -1)
                return null;
            l1 = (long) ((id << 8) + (i1 << 3) + l) + ((long) (k + 1) << 32);
            Model model_2 = (Model) model_cache.get(l1);
            if (model_2 != null) {
                return model_2;
            }
            if (objectModels == null) {
                return null;
            }
            int j2 = objectModels[i1];
            boolean flag3 = rotated ^ (l > 3);
            if (flag3)
                j2 += 0x10000;
            model = (Model) animated_model_cache.get(j2);
            if (model == null) {
                model = Model.get(j2 & 0xffff);
                if (model == null)
                    return null;
                if (flag3)
                    model.invert();
                animated_model_cache.put(model, j2);
            }
        }
        boolean flag;
        flag = scaleX != 128 || modelSizeHeight != 128 || scaleZ != 128;
        boolean flag2;
        flag2 = translateX != 0 || translateY != 0 || translateZ != 0;
        Model model_3 = new Model(recolorToFind == null,
            SeqFrame.noAnimationInProgress(k), l == 0 && k == -1 && !flag
            && !flag2, retextureToFind == null, model);
        if (k != -1) {
            model_3.generateBones();
            model_3.interpolate(k);
            model_3.faceGroups = null;
            model_3.groupedTriangleLabels = null;
        }
        while (l-- > 0)
            model_3.rotate_90();

        if (recolorToFind != null) {
            for (int k2 = 0; k2 < recolorToFind.length; k2++)
                model_3.recolor(recolorToFind[k2],
                    recolorToReplace[k2]);

        }
        if (retextureToFind != null) {
            for (int k2 = 0; k2 < retextureToFind.length; k2++)
                model_3.retexture(retextureToFind[k2],
                    textureToReplace[k2]);

        }
        if (flag)
            model_3.scale(scaleX, scaleZ, modelSizeHeight);
        if (flag2)
            model_3.translate(translateX, translateY, translateZ);
        model_3.light(85 + ambient, 768 + contrast, -50, -10, -50, !mergeNormals);
        if (supportsItems == 1)
            model_3.itemDropHeight = model_3.modelBaseY;
        animated_model_cache.put(model_3, l1);
        return model_3;
    }

    public static void release() {
        model_cache = null;
        animated_model_cache = null;
        stream_indices = null;
        cache = null;
        data_buffer = null;
    }

    public ObjectDefinition() {
        id = -1;
    }

    public static int length;
    public static int cache_index;
    public static boolean low_detail = ClientConstants.OBJECT_DEFINITION_LOW_MEMORY;
    public static Buffer data_buffer;
    public static ObjectDefinition[] cache;
    public static int[] stream_indices;
    public static final Model[] models = new Model[4];
    public static TempCache model_cache = new TempCache(500);
    public static TempCache animated_model_cache = new TempCache(30);

    public int id;
    public int width;
    public int height;
    public int animationID;
    public int blockingMask;

    public int scaleX;
    public int modelSizeHeight;
    public int scaleZ;
    public int translateX;
    public int translateY;
    public int translateZ;
    public int mapAreaId;
    public int mapsceneID;
    public int wallOrDoor;
    public int decorDisplacement;//
    public int supportsItems;//
    public int varpID;
    public int varbitID;

    public int[] objectModels;
    public int[] configChangeDest;
    public int[] objectTypes;

    public int[] recolorToFind;
    public int[] recolorToReplace;

    public short[] retextureToFind;
    public short[] textureToReplace;

    public String name;
    public String description;
    public String[] scene_actions;

    public int contrast;
    public int ambient;

    public boolean rotated;
    public boolean blocksProjectile;
    public boolean contouredGround;
    public boolean aBool2111;
    public boolean hollow;
    public int interactType;
    public boolean shadow;
    public boolean mergeNormals;//
    public boolean obstructsGround;

    /**
     * Later revisions
     */
    int opcode_78_1 = 2019882883;
    int opcode_79_1 = 0;
    int opcode_79_2 = 0;
    int opcode_78_and_79 = 0;
    int[] opcode_79_3;

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String[] getActions() {
        return this.scene_actions;
    }

    @Override
    public int getMapSceneId() {
        return this.mapsceneID;
    }

    @Override
    public int getMapIconId() {
        return this.mapAreaId;
    }

    @Override
    public int[] getImpostorIds() {
        return this.configChangeDest;
    }

    @Override
    public RSObjectComposition getImpostor() {
        return get_configs();
    }

    @Override
    public int getAccessBitMask() {
        return 0;
    }

    @Override
    public RSIterableNodeHashTable getParams() {
        return (RSIterableNodeHashTable) this.params;
    }

    @Override
    public int getIntValue(int paramID) {
        return 0;
    }

    @Override
    public void setValue(int paramID, int value) {

    }

    @Override
    public String getStringValue(int paramID) {
        return "";
    }

    @Override
    public void setValue(int paramID, String value) {

    }

    @Override
    public void setParams(IterableHashTable params) {

    }

    @Override
    public void setParams(RSIterableNodeHashTable params) {

    }

    @Override
    public void decodeNext(RSBuffer buffer, int opcode) {

    }

    @Override
    public int[] getModelIds() {
        return objectModels;
    }

    @Override
    public void setModelIds(int[] modelIds) {
        this.objectModels = modelIds;
    }

    @Override
    public int[] getModels() {
        return this.objectTypes;
    }

    @Override
    public void setModels(int[] models) {
        this.objectTypes = models;
    }

    @Override
    public boolean getObjectDefinitionIsLowDetail() {
        return false;
    }

    @Override
    public int getSizeX() {
        return this.scaleX;
    }

    @Override
    public void setSizeX(int sizeX) {
        this.scaleX = sizeX;
    }

    @Override
    public int getSizeY() {
        return this.modelSizeHeight;
    }

    @Override
    public void setSizeY(int sizeY) {
        this.modelSizeHeight = sizeY;
    }

    @Override
    public int getInteractType() {
        return this.interactType;
    }

    @Override
    public void setInteractType(int interactType) {
        this.interactType = interactType;
    }

    @Override
    public boolean getBoolean1() {
        return this.blocksProjectile;
    }

    @Override
    public void setBoolean1(boolean boolean1) {
        this.blocksProjectile = boolean1;
    }

    @Override
    public int getInt1() {
        return this.wallOrDoor;
    }

    @Override
    public void setInt1(int int1) {
        this.wallOrDoor = int1;
    }

    @Override
    public int getInt2() {
        return this.decorDisplacement;
    }

    @Override
    public void setInt2(int int2) {
        this.decorDisplacement = int2;
    }

    @Override
    public int getClipType() {
        return 0;
    }

    @Override
    public void setClipType(int clipType) {

    }

    @Override
    public boolean getNonFlatShading() {
        return this.mergeNormals;
    }

    @Override
    public void setNonFlatShading(boolean nonFlatShading) {
        this.mergeNormals = nonFlatShading;
    }

    @Override
    public void setModelClipped(boolean modelClipped) {
        this.aBool2111 = modelClipped;
    }

    @Override
    public boolean getModelClipped() {
        return this.aBool2111;
    }

    @Override
    public int getAnimationId() {
        return this.animationID;
    }

    @Override
    public void setAnimationId(int animationId) {
        this.animationID = animationId;
    }

    @Override
    public int getAmbient() {
        return this.ambient;
    }

    @Override
    public void setAmbient(int ambient) {
        this.ambient = ambient;
    }

    @Override
    public int getContrast() {
        return this.contrast;
    }

    @Override
    public void setContrast(int contrast) {
        this.contrast = contrast;
    }

    @Override
    public int[] getRecolorFrom() {
        return this.recolorToFind;
    }

    @Override
    public void setRecolorFrom(short[] recolorFrom) {

    }

    @Override
    public short[] getRecolorTo() {
        return new short[0];
    }

    @Override
    public void setRecolorTo(short[] recolorTo) {

    }

    @Override
    public short[] getRetextureFrom() {
        return new short[0];
    }

    @Override
    public void setRetextureFrom(short[] retextureFrom) {

    }

    @Override
    public short[] getRetextureTo() {
        return new short[0];
    }

    @Override
    public void setRetextureTo(short[] retextureTo) {

    }

    @Override
    public void setIsRotated(boolean rotated) {

    }

    @Override
    public boolean getIsRotated() {
        return false;
    }

    @Override
    public void setClipped(boolean clipped) {

    }

    @Override
    public boolean getClipped() {
        return false;
    }

    @Override
    public void setMapSceneId(int mapSceneId) {

    }

    @Override
    public void setModelSizeX(int modelSizeX) {

    }

    @Override
    public int getModelSizeX() {
        return 0;
    }

    @Override
    public void setModelHeight(int modelHeight) {

    }

    @Override
    public void setModelSizeY(int modelSizeY) {

    }

    @Override
    public void setOffsetX(int modelSizeY) {

    }

    @Override
    public void setOffsetHeight(int offsetHeight) {

    }

    @Override
    public void setOffsetY(int offsetY) {

    }

    @Override
    public void setInt3(int int3) {

    }

    @Override
    public void setInt5(int int5) {

    }

    @Override
    public void setInt6(int int6) {

    }

    @Override
    public void setInt7(int int7) {

    }

    @Override
    public void setBoolean2(boolean boolean2) {

    }

    @Override
    public void setIsSolid(boolean isSolid) {

    }

    @Override
    public void setAmbientSoundId(int ambientSoundId) {

    }

    @Override
    public void setSoundEffectIds(int[] soundEffectIds) {

    }

    @Override
    public int[] getSoundEffectIds() {
        return new int[0];
    }

    @Override
    public void setMapIconId(int mapIconId) {

    }

    @Override
    public void setBoolean3(boolean boolean3) {

    }

    @Override
    public void setTransformVarbit(int transformVarbit) {

    }

    @Override
    public int getTransformVarbit() {
        return 0;
    }

    @Override
    public void setTransformVarp(int transformVarp) {

    }

    @Override
    public int getTransformVarp() {
        return 0;
    }

    @Override
    public void setTransforms(int[] transforms) {

    }

    @Override
    public int[] getTransforms() {
        return new int[0];
    }
}
