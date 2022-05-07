import processing.core.PVector;

import java.util.ArrayList;

public abstract class Enemy {
    ArrayList<Boundary> hitbox;

    void draw(){

    }

    ArrayList<Boundary> getHitbox(){

        return null;
    }

    void update() {

    }
    boolean damage(){

        return true;
    }

    void startLevel(LevelManager l){

    }

    PVector getPosition(){

        return null;
    }

    boolean hitcheck(Character player) {

        return false;
    }

    boolean checkCollision(){

        return false;
    }
}
