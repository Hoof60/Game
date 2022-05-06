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

    void startLevel(Level1 l){

    }


    boolean hitcheck(Character player) {

        return false;
    }
}
