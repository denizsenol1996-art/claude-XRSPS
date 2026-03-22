package com.hazy.entity;

import com.hazy.Client;
import com.hazy.cache.anim.SeqDefinition;

public class Entity extends Renderable {

    public int index = -1;
    public final int[] waypoint_x;
    public final int[] waypoint_y;
    public int engaged_entity_id;
    public int step_tracker;
    public int rotation;

    public byte field1191;
    public byte field1192;
    public byte field1193;
    public byte field1133;
    public int running_animation_id;
    public String entity_message;
    public int height;
    public int turn_direction;
    public int seqStandID;
    public int standing_turn_animation_id;
    public int textColour;
    public final int[] damage_dealt;
    public final int[] damage_marker;
    public final int[] damage_cycle;
    public int secondarySeqID;
    public int secondarySeqFrame;
    public int queued_animation_duration;
    public int currentGfxId;
    public int spotanimFrame;
    public int spotanimCycle;
    public int spotanimEndCycle;
    public int spotanimY;
    public int waypoint_index;
    public int sequence;
    public int sequenceFrame;
    public int sequenceFrameCycle;
    public int sequenceDelay;
    public int sequenceAnimationLoops;
    public int textEffect;
    public int game_tick_status;
    public int current_hitpoints;
    public int maximum_hitpoints;
    public int message_cycle;
    public int time;
    public int faceX;
    public int faceY;
    public int occupied_tiles; //occupied_tiles is also known as size.
    public boolean dynamic;
    public int stationaryPathPosition;
    public int initialX;
    public int destinationX;
    public int initialY;
    public int destinationY;
    public int initiate_movement;
    public int cease_movement;
    public int direction;
    public int world_x;
    public int world_y;
    public int current_rotation;
    public final boolean[] waypoint_traveled;
    public int walk_animation_id;
    public int turn_around_animation_id;
    public int pivot_right_animation_id;
    public int pivot_left_animation_id;
    public int next_animation_frame;
    public int next_graphic_frame;
    public int next_idle_frame;

    public byte recolorHue;
    public byte recolourSaturation;
    public byte recolourLuminance;
    public byte recolourAmount;
    public int recolourStartCycle;
    public int recolourEndCycle;

    public Entity() {
        waypoint_x = new int[10];
        waypoint_y = new int[10];
        engaged_entity_id = -1;
        rotation = 32;
        running_animation_id = -1;
        height = 200;
        seqStandID = -1;
        standing_turn_animation_id = -1;
        damage_dealt = new int[15];
        damage_marker = new int[15];
        damage_cycle = new int[15];
        secondarySeqID = -1;
        currentGfxId = -1;
        sequence = -1;
        game_tick_status = -1000;
        message_cycle = 100;
        occupied_tiles = 1;
        dynamic = false;
        waypoint_traveled = new boolean[10];
        walk_animation_id = -1;
        turn_around_animation_id = -1;
        pivot_right_animation_id = -1;
        pivot_left_animation_id = -1;
        field1191 = -1;
        field1192 = -1;
        field1193 = -1;
        field1133 = -1;
    }

    public final void setPos(int x, int y, boolean flag) {
        if (sequence != -1 && SeqDefinition.get(sequence).idleStyle == 1)
            sequence = -1;

        if (!flag) {
            int dx = x - waypoint_x[0];
            int dy = y - waypoint_y[0];
            if (dx >= -8 && dx <= 8 && dy >= -8 && dy <= 8) {
                if (waypoint_index < 9)
                    waypoint_index++;
                for (int i1 = waypoint_index; i1 > 0; i1--) {
                    waypoint_x[i1] = waypoint_x[i1 - 1];
                    waypoint_y[i1] = waypoint_y[i1 - 1];
                    waypoint_traveled[i1] = waypoint_traveled[i1 - 1];
                }

                waypoint_x[0] = x;
                waypoint_y[0] = y;
                waypoint_traveled[0] = false;
                return;
            }
        }
        waypoint_index = 0;
        stationaryPathPosition = 0;
        step_tracker = 0;
        waypoint_x[0] = x;
        waypoint_y[0] = y;
        this.world_x = waypoint_x[0] * 128 + occupied_tiles * 64;
        this.world_y = waypoint_y[0] * 128 + occupied_tiles * 64;
    }

    public final void resetPath() {
        waypoint_index = 0;
        stationaryPathPosition = 0;
    }

    public final void updateHitData(int hitType, int hitDamage, int currentTime) {
        for (int hitPtr = 0; hitPtr < 4; hitPtr++)
            if (damage_cycle[hitPtr] <= currentTime) {
                damage_dealt[hitPtr] = hitDamage;
                damage_marker[hitPtr] = hitType;
                damage_cycle[hitPtr] = currentTime + 70;
                return;
            }
    }

    public void refreshEntityPosition() {
        int remaining = initiate_movement - Client.tick;
        int tempX = initialX * 128 + occupied_tiles * 64;
        int tempY = initialY * 128 + occupied_tiles * 64;
        world_x += (tempX - world_x) / remaining;
        world_y += (tempY - world_y) / remaining;

        step_tracker = 0;

        if (direction == 0) {
            turn_direction = 1024;
        }

        if (direction == 1) {
            turn_direction = 1536;
        }

        if (direction == 2) {
            turn_direction = 0;
        }

        if (direction == 3) {
            turn_direction = 512;
        }
    }

    public void refreshEntityFaceDirection() {
        if (cease_movement == Client.tick || sequence == -1
                || sequenceDelay != 0
                || sequenceFrameCycle
                + 1 > SeqDefinition.get(sequence)
                        .duration(sequenceFrame)) {
            int remaining = cease_movement - initiate_movement;
            int elapsed = Client.tick - initiate_movement;
            int initialX = this.initialX * 128 + occupied_tiles * 64;
            int initialY = this.initialY * 128 + occupied_tiles * 64;
            int endX = destinationX * 128 + occupied_tiles * 64;
            int endY = destinationY * 128 + occupied_tiles * 64;
            world_x = (initialX * (remaining - elapsed) + endX * elapsed) / remaining;
            world_y = (initialY * (remaining - elapsed) + endY * elapsed) / remaining;
        }
        step_tracker = 0;

        if (direction == 0) {
            turn_direction = 1024;
        }

        if (direction == 1) {
            turn_direction = 1536;
        }

        if (direction == 2) {
            turn_direction = 0;
        }

        if (direction == 3) {
            turn_direction = 512;
        }

        current_rotation = turn_direction;
    }

    public void getDegreesToTurn() {
        secondarySeqID = seqStandID;

        if (waypoint_index == 0) {
            step_tracker = 0;
            return;
        }

        if (sequence > SeqDefinition.animations.length) {
            sequence = -1;
            return;
        }

        if (sequence != -1 && sequenceDelay == 0) {
            SeqDefinition seq = SeqDefinition.get(sequence);
            if (seq != null) {
                if (stationaryPathPosition > 0 && seq.moveStyle == 0) {
                    step_tracker++;
                    return;
                }
                if (stationaryPathPosition <= 0 && seq.idleStyle == 0) {
                    step_tracker++;
                    return;
                }
            }
        }
        int tempX = world_x;
        int tempY = world_y;
        int nextX = waypoint_x[waypoint_index - 1] * 128 + occupied_tiles * 64;
        int nextY = waypoint_y[waypoint_index - 1] * 128 + occupied_tiles * 64;
        if (nextX - tempX > 256 || nextX - tempX < -256 || nextY - tempY > 256 || nextY - tempY < -256) {
            world_x = nextX;
            world_y = nextY;
            return;
        }
        if (tempX < nextX) {
            if (tempY < nextY) {
                turn_direction = 1280;
            } else if (tempY > nextY) {
                turn_direction = 1792;
            } else {
                turn_direction = 1536;
            }
        } else if (tempX > nextX) {
            if (tempY < nextY) {
                turn_direction = 768;
            } else if (tempY > nextY) {
                turn_direction = 256;
            } else {
                turn_direction = 512;
            }
        } else if (tempY < nextY) {
            turn_direction = 1024;
        } else {
            turn_direction = 0;
        }

        int rotation = turn_direction - current_rotation & 0x7ff;

        if (rotation > 1024) {
            rotation -= 2048;
        }

        int animation = turn_around_animation_id;

        if (rotation >= -256 && rotation <= 256) {
            animation = walk_animation_id;
        } else if (rotation >= 256 && rotation < 768) {
            animation = pivot_left_animation_id;
        } else if (rotation >= -768 && rotation <= -256) {
            animation = pivot_right_animation_id;
        }

        if (animation == -1) {
            animation = walk_animation_id;
        }

        secondarySeqID = animation;

        int positionDelta = 4;

        if (current_rotation != turn_direction && engaged_entity_id == -1
                && rotation != 0) {
            positionDelta = 2;
        }

        if (waypoint_index > 2) {
            positionDelta = 6;
        }

        if (waypoint_index > 3) {
            positionDelta = 8;
        }

        if (step_tracker > 0 && waypoint_index > 1) {
            positionDelta = 8;
            step_tracker--;
        }

        if (waypoint_traveled[waypoint_index - 1]) {
            positionDelta <<= 1;
        }

        if (positionDelta >= 8 && secondarySeqID == walk_animation_id
                && running_animation_id != -1) {
            secondarySeqID = running_animation_id;
        }

        if (tempX < nextX) {
            world_x += positionDelta;
            if (world_x > nextX) {
                world_x = nextX;
            }
        } else if (tempX > nextX) {
            world_x -= positionDelta;
            if (world_x < nextX) {
                world_x = nextX;
            }
        }
        if (tempY < nextY) {
            world_y += positionDelta;
            if (world_y > nextY) {
                world_y = nextY;
            }
        } else if (tempY > nextY) {
            world_y -= positionDelta;

            if (world_y < nextY) {
                world_y = nextY;
            }
        }
        if (world_x == nextX && world_y == nextY) {
            waypoint_index--;

            if (stationaryPathPosition > 0) {
                stationaryPathPosition--;
            }
        }
    }

    public final void moveInDir(boolean run, int direction) {
        int x = waypoint_x[0];
        int y = waypoint_y[0];
        if (direction == 0) {
            x--;
            y++;
        }
        if (direction == 1)
            y++;
        if (direction == 2) {
            x++;
            y++;
        }
        if (direction == 3)
            x--;
        if (direction == 4)
            x++;
        if (direction == 5) {
            x--;
            y--;
        }
        if (direction == 6)
            y--;
        if (direction == 7) {
            x++;
            y--;
        }
        if (sequence != -1 && SeqDefinition.get(sequence).idleStyle == 1)
            sequence = -1;
        if (waypoint_index < 9)
            waypoint_index++;
        for (int l = waypoint_index; l > 0; l--) {
            waypoint_x[l] = waypoint_x[l - 1];
            waypoint_y[l] = waypoint_y[l - 1];
            waypoint_traveled[l] = waypoint_traveled[l - 1];
        }
        waypoint_x[0] = x;
        waypoint_y[0] = y;
        waypoint_traveled[0] = run;
    }

    public boolean visible() {
        return false;
    }

}
