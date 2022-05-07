import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Arrays;

public class LevelManager {

    ArrayList<Enemy> enemies;
    PImage background;
    PImage wall;
    PApplet app;
    Character player;
    ArrayList<Integer> groundHeight = new ArrayList<>();
    ArrayList<Enemy> created = new ArrayList<>();
    ArrayList<Boundary> bounds;
    int Levelnum = 1;
    boolean levelOver = false;


    public LevelManager(PApplet app, Character player) {
        this.app = app;
        enemies = new ArrayList<>();

        switch (Levelnum){
            case 1:{
                enemies.add(new Robot(2000, 100, app));
                enemies.add(new Robot(3000, 100, app));
                enemies.add(new Robot(6000, 100, app));
                enemies.add(new Robot(6500, 100, app));
                enemies.add(new Robot(8000, 100, app));

                enemies.add(new CEO(new PVector(10500, app.displayHeight - 450), app));
                background = app.loadImage("Assets/backgrond.png");
                wall = app.loadImage("Assets/wall.png");
                groundHeight.addAll(Arrays.asList(2000, 100, 100, 100, 150, 500, 100, 100, 100, 100, 100, 100, 100, 300, 400, 500, 100, 100, 100, 100, 100, 300, 300, 300, 500, 500, 500, 300, 300, 500, 600, 700, 200, 200, 200, 200, 2000));
                break;
            }
            case 2: {
                enemies.add(new Robot(2000, 100, app));
                enemies.add(new Robot(3000, 100, app));
                enemies.add(new Robot(4000, 100, app));
                enemies.add(new Robot(5000, 100, app));
                enemies.add(new Robot(6000, 100, app));
                enemies.add(new Robot(8000, 100, app));
                enemies.add(new CEO(new PVector(500, 200), app));
                background = app.loadImage("Assets/backgrond.png");
                groundHeight.addAll(Arrays.asList(2000,100, 100, 100, 10, 10, 10, 10, 400, 420,440, 460,400, 400, 400, 400, 400, 400, 200, 200, 200, 100, 100, 100, 700, 500, 500, 500, 500, 500, 500, 700, 800,600,550,500,450, 400, 350, 250,200, 200, 2000  ));
                break;
            }
        }

        //background.resize(20000, 1080);
        this.player = player;
        player.startLevel(this);
        for (Enemy e : enemies) {
            e.startLevel(this);
        }

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
                if (e.checkCollision()) {
                    killed.add(e);
                }
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
            PVector hitZone;
            if ((hitZone = hitscanRay.cast(hitboxes.get(0))) != null) {
                if (hitZone.x - 2*player.position.x + 500  < 1500) {
                    ArrayList<Boundary> walls = translateHitboxes(bounds);
                    for (Boundary b : walls) {
                        PVector point;
                        if ((point = hitscanRay.cast(b)) != null) {
                            hit.add(point);
                        }
                    }
                    boolean hitWall = false;

                    if (hit.size() > 0) {
                        for (PVector p : hit) {
                            if (p.x + player.position.x - 500 < e.getPosition().x) {
                                hitWall = true;
                                break;
                            }
                        }
                    }
                    if (!hitWall) {
                        if (e.damage()) {
                            killed.add(e);
                            break;
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
                app.image(wall, i * 300, app.displayHeight - groundHeight.get(i));
            }
    }

    void makeEnemy(Enemy e) {
        created.add(e);
        e.startLevel(this);
    }

    public void endLevel() {
        levelOver = true;
    }
}
