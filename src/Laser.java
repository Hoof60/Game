import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Laser extends Enemy {
    PVector position;
    PVector movementDir;
    LevelManager level;
    PApplet app;
    float moveSpeed = 0.04f;

    public Laser(PVector position, PVector movementDir, PApplet app) {
        this.position = position;
        this.movementDir = movementDir.get();
        this.app = app;
    }
    @Override
    boolean checkCollision(){
        if (position.x/300 < 0 || position.x/300 > level.groundHeight.size()){
            return true;
        }
        if (position.y > app.displayHeight - level.groundHeight.get((int) position.x/300)){
            return true;
        }
        return false;
    }

    void startLevel(LevelManager l){
        this.level = l;
    }

    @Override
    ArrayList<Boundary> getHitbox() {
        ArrayList<Boundary> translated = new ArrayList<>();
        translated.add(new Boundary(position.x, position.y, position.x, position.y + 5, app));
        return translated;
    }

    @Override
    boolean hitcheck(Character player) {
        if (player.ducking) {
            if (position.x < player.position.x + 130 && position.x > player.position.x && position.y > player.position.y + 120 && position.y < player.position.y + player.height) {
                return true;
            }
        } else if (position.x < player.position.x + 100 && position.x > player.position.x && position.y > player.position.y && position.y < player.position.y + player.height) {
            return true;
        }
        return false;
    }

    void update() {
        position.add(movementDir.get().mult(moveSpeed));
    }

    void draw() {
        app.noStroke();
        app.fill(255, 0, 0);
        app.rect(position.x, position.y, 20, 5);
    }

    @Override
    public PVector getPosition() {
        return position;
    }
}
