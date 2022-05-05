import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;

public class Robot {

    PVector position;
    PImage Idle;
    ArrayList<Boundary> hitbox;

    public Robot(int x, int y, PApplet app) {
        this.app = app;
        position = new PVector(x, y);
        Idle = app.loadImage("Assets/Robot.png");
        Boundary eyeBox = new Boundary(position.x, position.y, position.x, position.y + 100, app);
        hitbox = new ArrayList<>();
        hitbox.add(eyeBox);
    }

    PApplet app;

    void draw() {
        app.image(Idle, position.x, position.y);
    }

}
