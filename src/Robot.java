import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Robot extends Enemy {

    private float invMass = 0.1f;
    static final PVector gravity = new PVector(0, 20f);
    LevelManager level;
    PVector position;
    PImage Idle;
    ArrayList<Boundary> hitbox;
    int height = 500;
    int width = 200;
    PApplet app;
    PVector velocity = new PVector(0, 0);
    int health = 3;
    int fireCounter;
    boolean seesPlayer;


    public Robot(int x, int y, PApplet app) {
        this.app = app;
        position = new PVector(x, y);
        Idle = app.loadImage("Assets/Robot.png");
        Idle.resize(200, 500);
        hitbox = new ArrayList<>();
        velocity = new PVector(-3, 0);
    }

    @Override
    void startLevel(LevelManager l) {
        this.level = l;
    }

    @Override
    ArrayList<Boundary> getHitbox() {
        ArrayList<Boundary> translated = new ArrayList<>();
        translated.add(new Boundary(position.x + 50, position.y + 50, position.x + 50, position.y + 100, app));
        return translated;
    }

    @Override
    boolean hitcheck(Character player) {
        if (position.x < player.position.x + 100 && position.x > player.position.x && position.y < player.position.y && position.y < player.position.y + player.height) {
            return true;
        }
        return false;
    }

    boolean damage(){
        health--;
        app.fill(255,0,0);
        app.pushMatrix();
        app.translate(-level.player.position.x + 500, 0);
        app.rect(position.x, position.y, width, height);
        app.popMatrix();
        if (health == 0) {
            return true;
        }
        return false;
    }

    @Override
    void update() {
        if (position.x - level.player.position.x < 1000){
            seesPlayer = true;
        } else {
            seesPlayer = false;
        }
        if (position.y + 510 + level.groundHeight.get((int) position.x / 300) >= app.displayHeight) {
            velocity.y = 0;
        }
        if (ThreadLocalRandom.current().nextInt(0, 50) == 0 && seesPlayer) {
            fireLaser(level.player);
        } else {
            integrate(gravity);
        }
    }

    @Override
    void draw() {
        app.image(Idle, position.x, position.y);
        getHitbox().get(0).show();
    }

    boolean integrate(PVector force) {
        // If infinite mass, we don't integrate
        if (invMass <= 0f) return false;

        // update position
        position.y += velocity.y;
        if ((velocity.x > 0 && (position.x + width - 5) % 300 < 10 && position.y + height - 10 >= app.displayHeight - level.groundHeight.get((int) (position.x / 300) + 1)) || (
                velocity.x < 0 && (position.x - 5) % 300 < 10 && position.y + height - 10 >= app.displayHeight - level.groundHeight.get((int) (position.x / 300) - 1))) {
            velocity.x = -velocity.x;
        } else {
            position.x += velocity.x;
        }

        PVector acceleration = force.get();
        acceleration.mult(invMass);

        // update velocity
        velocity.add(acceleration);

        //calculate drag
        PVector n = new PVector(velocity.x, velocity.y);
        n.normalize();

        // Apply an impulse to bounce off the edge of the screen
        return false;
    }

    void fireLaser(Character p) {
        PVector dir = new PVector(p.position.x + 50 - (position.x), (p.position.y + 50 - position.y));
        level.makeEnemy(new Laser(new PVector(position.x + 50, position.y + 80), dir.get(), app));
    }

    @Override
    public PVector getPosition() {

        return position;
    }
}
