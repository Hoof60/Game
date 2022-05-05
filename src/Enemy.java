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


    boolean hitcheck(Character player) {

        return false;
    }
}
