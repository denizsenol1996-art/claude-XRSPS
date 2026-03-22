package com.hazy.scene;

import com.hazy.Client;
import com.hazy.cache.anim.SeqDefinition;
import com.hazy.cache.anim.SeqFrame;
import com.hazy.cache.anim.SpotAnimation;
import com.hazy.entity.Renderable;
import com.hazy.entity.model.Model;
import net.runelite.api.events.ProjectileSpawned;

public final class Projectile extends Renderable {

    public final int delay;
    public final int cycle_limit;
    private double vector_speed_x;
    private double vector_speed_y;
    private double momentum_scalar;
    private double vector_speed_z;
    private double height_offset;
    private boolean traveling;
    private final int start_x;
    private final int start_y;
    private final int start_z;
    public final int end_z;
    public double current_x;
    public double current_y;
    public double current_height;
    private final int slope_start;
    private final int distance;
    public final int target_id;
    private final SeqDefinition sequenceDefinition;
    private int flow;
    private int duration;
    public int jaw;
    private int pitch;
    public final int plane;
    public final int id;

    public void track(int cycle, int target_y, int k, int target_x) {
        if (!traveling) {
            double vector_x = target_x - start_x;
            double vector_y = target_y - start_y;
            double scalar = Math.sqrt(vector_x * vector_x + vector_y * vector_y);
            current_x = (double) start_x + (vector_x * (double) distance) / scalar;
            current_y = (double) start_y + (vector_y * (double) distance) / scalar;
            current_height = start_z;
        }
        double remaining = (cycle_limit + 1) - cycle;
        vector_speed_x = ((double) target_x - current_x) / remaining;
        vector_speed_y = ((double) target_y - current_y) / remaining;
        momentum_scalar = Math.sqrt(vector_speed_x * vector_speed_x + vector_speed_y * vector_speed_y);
        if (!traveling) {
            vector_speed_z = -momentum_scalar * Math.tan((double) slope_start * 0.02454369D);
        }
        height_offset = (2D * ((double) k - current_height - vector_speed_z * remaining)) / (remaining * remaining);
    }

    public Model get_rotated_model() {
        SpotAnimation animation = SpotAnimation.spotAnims[this.id];
        Model model = animation.get_model();
        if (model == null) {
            return null;
        }
        boolean skeletal = sequenceDefinition.isSkeletalAnimation();
        int frame = sequenceDefinition.primaryFrameIds[flow];
        Model animated_model = new Model(true, SeqFrame.noAnimationInProgress(frame), false, model);
        if (frame != -1) {
            if (skeletal) {
                animated_model.playSkeletal(sequenceDefinition, frame);
            } else {
                animated_model.generateBones();
                animated_model.interpolate(frame);
            }
            animated_model.faceGroups = null;
            animated_model.groupedTriangleLabels = null;
        }
        if (animation.model_scale_x != 128 || animation.model_scale_y != 128) {
            animated_model.translate(animation.model_scale_x, animation.model_scale_y, animation.model_scale_x);
        }
        animated_model.leanOverX(pitch);
        animated_model.light(64 + animation.ambient, 850 + animation.contrast, -30, -50, -30, true);
        return animated_model;
    }

    public Projectile(int slope_start, int end_z, int delay, int cycle_limit, int distance, int plane, int start_z, int start_y, int start_x, int target_id, int id) {
        this.id = id;
        SeqDefinition sequence = SpotAnimation.spotAnims[id].seq;
        if (sequence != null) {
            this.sequenceDefinition = SeqDefinition.get(sequence.animId);
        } else {
            this.sequenceDefinition = null;
        }
        this.plane = plane;
        this.start_x = start_x;
        this.start_y = start_y;
        this.start_z = start_z;
        this.delay = delay;
        this.cycle_limit = cycle_limit;
        this.slope_start = slope_start;
        this.distance = distance;
        this.target_id = target_id;
        this.end_z = end_z;
        traveling = false;
    }

    public void travel(int step) {
        traveling = true;
        current_x += vector_speed_x * (double) step;
        current_y += vector_speed_y * (double) step;
        current_height += vector_speed_z * (double) step + 0.5D * height_offset * (double) step * (double) step;
        vector_speed_z += height_offset * (double) step;
        jaw = (int) (Math.atan2(vector_speed_x, vector_speed_y) * 325.94900000000001D) + 1024 & 0x7ff;
        pitch = (int) (Math.atan2(vector_speed_z, momentum_scalar) * 325.94900000000001D) & 0x7ff;
        if (sequenceDefinition != null) {
            for (duration += step; duration > sequenceDefinition.duration(flow); ) {
                duration -= sequenceDefinition.duration(flow) + 1;
                flow++;
                if (flow >= sequenceDefinition.primaryFrameIds.length) {
                    flow = 0;
                }
            }
        }
    }
}
