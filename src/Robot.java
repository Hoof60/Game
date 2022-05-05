import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import java.util.ArrayList;

public class Robot extends Enemy {

    PVector position;
    PImage Idle;
    ArrayList<Boundary> hitbox;
    PApplet app;


    public Robot(int x, int y, PApplet app){
        this.app = app;
        position = new PVector(x, y);
        Idle = app.loadImage("Assets/Robot.png");
        //Boundary eyeBox = new Boundary(position.x + 50, position.y + 50, position.x+50, position.y + 100, app);
        hitbox = new ArrayList<>();
        //hitbox.add(eyeBox);
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
        position.x -= 5;
    }

    @Override
    void draw() {

        app.image(Idle, position.x, position.y);
//        app.strokeWeight(50);
//        hitbox.get(0).show();
    }

}
