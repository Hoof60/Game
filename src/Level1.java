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
    ArrayList<Enemy> created = new ArrayList<>();
    ArrayList<Boundary> bounds;

    public Level1(PApplet app, Character player) {
        this.app = app;
        enemies = new ArrayList<>();
        enemies.add(new Robot(2000, 100, app));
        enemies.add(new Robot(5000, 100, app));
        enemies.add(new Robot(8000, 100, app));
        background = app.loadImage("Assets/backgrond.png");

        //background.resize(20000, 1080);
        this.player = player;
        player.startLevel(this);
        for (Enemy e : enemies) {
            e.startLevel(this);
        }
        groundHeight.addAll(Arrays.asList(2000, 100, 100, 100, 400, 500, 100, 100, 100, 100, 100, 100, 100, 300, 400, 500, 100, 100, 100, 100, 100, 300, 300, 300, 500, 500, 500, 300, 700, 300, 100, 300, 500, 500, 500, 500, 2000));
        bounds = new ArrayList<>();
        for (int i = 0; i < groundHeight.size(); i++) {
            bounds.add(new Boundary(300 * i, app.displayHeight, 300 * i, app.displayHeight - groundHeight.get(i), app));
            bounds.add(new Boundary(300 * i, app.displayHeight - groundHeight.get(i), 300 * i + 300, app.displayHeight - groundHeight.get(i), app));
            bounds.add(new Boundary(300 * i + 300, app.displayHeight - groundHeight.get(i), 300 * i + 300, app.displayHeight, app));
        }

    }

    void update() {
        ArrayList<Enemy> killed = new ArrayList<>();
        for (Enemy e : enemies) {
            e.update();
            if (e.hitcheck(player)) {
                killed.add(e);
                player.getHit();
            }
        }
        enemies.removeAll(killed);
        enemies.addAll(created);
        created.clear();
    }

    public void hitCheck(Ray hitscanRay) {
        //Check if player bullet hits enemies
        ArrayList<Enemy> killed = new ArrayList<>();
        ArrayList<PVector> hit = new ArrayList<>();

        for (Enemy e : enemies) {
            ArrayList<Boundary> hitboxes = translateHitboxes(e.getHitbox());
            if (hitscanRay.cast(hitboxes.get(0)) != null) {
                ArrayList<Boundary> walls = translateHitboxes(bounds);
                for (Boundary b : walls) {
                    PVector point;
                    if ((point = hitscanRay.cast(b)) != null) {
                        hit.add(point);
                    }
                }
                if (hit.size() > 0) {
                    boolean hitWall = false;
                    for (PVector p : hit) {
                        app.fill(255, 0, 0);
                        app.circle(p.x, p.y, 10);
                        if (p.x < e.getPosition().x + 100) {
                            hitWall = true;
                            break;
                        }
                    }
                    if (!hitWall) {
                        if (e.damage()) {
                            killed.add(e);
                        }
                    }
                }
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
        app.fill(200);
        for (int i = 0; i < groundHeight.size(); i++) {
            app.rect(i * 300, app.displayHeight - groundHeight.get(i), 300, 3000);
        }
        for (Boundary b : bounds) {
            b.show();
        }

    }

    void makeEnemy(Enemy e) {
        created.add(e);
    }

}
