package com.twisted.game.world.entity.mob.player;

import com.twisted.game.world.entity.mob.FaceDirection;
import com.twisted.game.world.position.Tile;

public class ForceMovement {

    private Tile start;
    private Tile end;
    private int speed;
    private int reverseSpeed;
    private int direction;
    
    public ForceMovement(Tile start, Tile end, int speed, int reverseSpeed, int direction) {
        this.setStart(start);
        this.setEnd(end);
        this.setSpeed((short)speed);
        this.setReverseSpeed((short)reverseSpeed);
        this.setDirection((byte)direction);
    }

    public ForceMovement(Tile start, Tile end, int speed, int reverseSpeed, FaceDirection direction) {
        this.setStart(start);
        this.setEnd(end);
        this.setSpeed((short)speed);
        this.setReverseSpeed((short)reverseSpeed);
        this.setDirection((byte)direction.direction);
    }

    public ForceMovement(int dx, int dy, int dx2, int dy2, int speed1, int speed2, FaceDirection direction) {
        this.setStart(new Tile(dx, dy));
        this.setEnd(new Tile(dx2, dy2));
        this.setSpeed((short)speed1);
        this.setReverseSpeed((short)speed2);
        this.setDirection((byte)direction.direction);
    }

    public Tile getStart() {
        return start;
    }

    public void setStart(Tile start) {
        this.start = start;
    }

    public Tile getEnd() {
        return end;
    }

    public void setEnd(Tile end) {
        this.end = end;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getReverseSpeed() {
        return reverseSpeed;
    }

    public void setReverseSpeed(int reverseSpeed) {
        this.reverseSpeed = reverseSpeed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

}
