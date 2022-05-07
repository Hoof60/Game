import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;

public class CEO extends Enemy{
    PVector position;
    PImage alive;
    PImage death;
    boolean dead = false;
    PApplet app;
    LevelManager l;
    int deathTimer = 100;

    public CEO(PVector position, PApplet app) {
        this.position = position;
        this.alive = app.loadImage("Assets/Greebles1.png");
        this.death = app.loadImage("Assets/Greebles2.png");
        alive.resize(250, 250);
        death.resize(250, 250);
        this.app = app;
    }

    void draw(){
        if (deathTimer == 0){
            l.endLevel();
        }
        if (!dead){
            app.image(alive, position.x, position.y);
        } else {
            deathTimer--;
            app.image(death, position.x, position.y);
        }
    }

    void startLevel(LevelManager l){
        this.l = l;
    }

    ArrayList<Boundary> getHitbox(){
        ArrayList<Boundary> translated = new ArrayList<>();
        translated.add(new Boundary(position.x + 50, position.y + 50, position.x + 50, position.y + 200, app));
        return translated;
    }

    boolean damage(){
        dead = true;
        return false;
    }

    PVector getPosition(){

        return position;
    }

}
