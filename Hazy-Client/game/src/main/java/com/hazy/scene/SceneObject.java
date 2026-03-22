package com.hazy.scene;

import com.hazy.Client;
import com.hazy.cache.anim.SeqDefinition;
import com.hazy.cache.config.VariableBits;
import com.hazy.cache.def.ObjectDefinition;
import com.hazy.entity.Renderable;
import com.hazy.entity.model.Model;
import net.runelite.api.DynamicObject;

public final class SceneObject extends Renderable implements DynamicObject {

    private int animation_frame;
    private final int[] configs;
    private final int varbit_id;
    private final int config_id;
    private final int cos_y;
    private final int sin_y;
    private final int cos_x;
    private final int sin_x;
    private SeqDefinition seq;
    private int cycle_delay;
    private final int object_id;
    private final int click_type;
    private final int orientation;

    private ObjectDefinition get_configs() {
        int index = -1;
        if (varbit_id != -1) {
            try {
                VariableBits varBit = VariableBits.cache[varbit_id];
                int setting = varBit.configId;
                int low_varbit = varBit.leastSignificantBit;
                int high_varbit = varBit.mostSignificantBit;
                int bit_mask = Client.BIT_MASKS[high_varbit - low_varbit];
                index = Client.instance.settings[setting] >> low_varbit & bit_mask;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (config_id != -1 && config_id < Client.instance.settings.length) {
            index = Client.instance.settings[config_id];
        }
        int var;
        if (index >= 0 && index < configs.length) {
            var = configs[index];
        } else
            var = configs[configs.length - 1];

        return var != -1 ? ObjectDefinition.get(var) : null;
    }

    public Model get_rotated_model() {
        int animation_id = -1;
        if (seq != null) {
            if(seq.isSkeletalAnimation()) {
                int length = seq.getSkeletalLength();
                animation_frame++;
                if (animation_frame >= length) {
                    seq = null;
                    //animation_frame = 0;
                }

                if (seq != null) {
                    animation_id = seq.getSkeletalId();
                }
            } else {
                int step = Client.tick - cycle_delay;
                if (step > 100 && seq.getFrameStep() > 0) {
                    step = 100;
                }
                while (step > seq.duration(animation_frame)) {
                    step -= seq.duration(animation_frame);
                    animation_frame++;
                    if (animation_frame < seq.getFrameCount())
                        continue;

                    animation_frame -= seq.getFrameStep();
                    if (animation_frame >= 0 && animation_frame < seq.getFrameCount())
                        continue;

                    seq = null;
                    break;
                }
                cycle_delay = Client.tick - step;
                if (seq != null) {
                    animation_id = seq.isSkeletalAnimation() ? seq.getSkeletalId() : seq.getPrimaryFrameIds()[animation_frame];
                }
            }
        }
        ObjectDefinition def;
        if (configs != null)
            def = get_configs();
        else
            def = ObjectDefinition.get(object_id);

        if (def == null) {
            return null;
        } else {
            return def.modelAt(click_type, orientation, cos_y, sin_y, cos_x, sin_x, animation_id);
        }
    }

    public SceneObject(int id, int orientation, int click_type, int sin_y, int cos_x, int cos_y, int sin_x, int animation_id, boolean flag) {
        object_id = id;
        this.click_type = click_type;
        this.orientation = orientation;
        this.cos_y = cos_y;
        this.sin_y = sin_y;
        this.cos_x = cos_x;
        this.sin_x = sin_x;
        if (animation_id != -1) {
            seq = SeqDefinition.get(animation_id);
            animation_frame = 0;
            cycle_delay = Client.tick;
            if (flag && seq.frameStep != -1) {
                animation_frame = (int) (Math.random() * (double) seq.frameCount);
                cycle_delay -= (int) (Math.random() * (double) seq.duration(animation_frame));
            }
        }
        ObjectDefinition def = ObjectDefinition.get(object_id);
        this.varbit_id = def.varbitID;
        this.config_id = def.varpID;
        this.configs = def.configChangeDest;
    }

    @Override
    public int getAnimationID() {
        return this.seq.animId;
    }

    @Override
    public int getAnimFrame() {
        return this.animation_frame;
    }

    @Override
    public int getAnimCycleCount() {
        return this.cycle_delay;
    }
}
