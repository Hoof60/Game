import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Arrays;

public class Level1 {

    ArrayList<Enemy> enemies;
    PImage background;
    PApplet app;
    Character player;
    ArrayList<Integer> groundHeight = new ArrayList<>();


    public Level1(PApplet app, Character player) {
        this.app = app;
        enemies = new ArrayList<>();
        enemies.add(new Robot(500, 300, app));
        enemies.add(new Robot(5000, 300, app));
        enemies.add(new Robot(8000, 300, app));
//        background = app.loadImage("Assets/Level1Background.png");
        //background.resize(400, 150);
        this.player = player;
        player.startLevel(this);
        for (Enemy e: enemies) {
            e.startLevel(this);
        }
        groundHeight.addAll(Arrays.asList(50,100,100,500,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100, 300,300,300,500,500,500,300,700,300,100,300,500,500,500,500,500));
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
        for (int i = 0; i < groundHeight.size(); i++) {
            app.rect(i * 300, app.displayHeight - groundHeight.get(i), 290, 1000);
        }
//        for (Integer i : groundHeight){
//
//        }
    }

}
