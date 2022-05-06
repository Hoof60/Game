import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import java.util.ArrayList;

public class Robot extends Enemy {

    private float invMass = 0.1f;
    static final PVector gravity = new PVector(0, 20f);
    Level1 level;
    PVector position;
    PImage Idle;
    ArrayList<Boundary> hitbox;
    PApplet app;
    PVector velocity = new PVector(0,0);


    public Robot(int x, int y, PApplet app){
        this.app = app;
        position = new PVector(x, y);
        Idle = app.loadImage("Assets/Robot.png");
        hitbox = new ArrayList<>();
    }

    @Override
    void startLevel(Level1 l){
        this.level = l;
    }

    @Override
    ArrayList<Boundary> getHitbox(){
        ArrayList<Boundary> translated = new ArrayList<>();
        translated.add(new Boundary(position.x + 50, position.y + 50, position.x+50, position.y + 100, app));
        return translated;
    }

    @Override
    boolean hitcheck(Character player) {
        if (position.x < player.position.x + 100 && position.x > player.position.x){
            System.out.println("true");
            return true;
        }
        return false;
    }

    @Override
    void update() {
        if (position.y + 510 + level.groundHeight.get((int) position.x/300 ) >= app.displayHeight) {
            velocity.y = 0;
        }
            integrate(gravity);
        //position.x -= 5;
    }

    @Override
    void draw() {
        app.image(Idle, position.x, position.y);
    }

    boolean integrate(PVector force) {
        // If infinite mass, we don't integrate
        if (invMass <= 0f) return false;

        // update position
        position.add(velocity);

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



}
