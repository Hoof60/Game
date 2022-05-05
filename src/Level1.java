import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;

public class Level1 {

    //Robot r;
    ArrayList<Enemy> enemies;
    PImage background;
    PApplet app;
    Character player;

    public Level1(PApplet app, Character player) {
        this.app = app;
        enemies = new ArrayList<>();
        enemies.add(new Robot(2000, 1000, app));
        enemies.add(new Robot(5000, 1000, app));
        enemies.add(new Robot(8000, 1000, app));
        //background = app.loadImage("Assets/Level1Background.png");
        //background.resize(400, 150);
        this.player = player;
    }

    void update() {
        for(Enemy e: enemies){
            e.update();
            e.hitcheck(player);
        }
    }

    public void hitCheck(Ray hitscanRay) {
        //Check if player bullet hits enemies

        ArrayList<Enemy> killed = new ArrayList<>();
        for (Enemy e : enemies) {
            ArrayList<Boundary> hitboxes = translateHitboxes(e.getHitbox());
            if (hitscanRay.cast(hitboxes.get(0)) != null) {
                killed.add(e);
            }
        }
        enemies.removeAll(killed);

    }

    private ArrayList<Boundary> translateHitboxes(ArrayList<Boundary> hitbox) {
        ArrayList<Boundary> translated = new ArrayList<>();
        for (Boundary bound : hitbox) {
            translated.add(translate(bound));
        }
        return translated;
    }


    Boundary translate(Boundary bound) {
        return new Boundary(bound.a.x - player.position.x + 500, bound.a.y, bound.b.x - player.position.x + 500, bound.b.y, app);
    }


    void draw() {
        //app.image(background, 0, 0);
        for (Enemy e : enemies) {
            e.draw();
        }
    }

}
