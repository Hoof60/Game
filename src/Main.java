import processing.core.PImage;
import processing.core.PVector;
import processing.core.PApplet;

import java.util.ArrayList;

public class Main extends PApplet {

    // Global vars:
    PVector v;
    int px = 10;
    int py = 10;
    Character player;
    int scoreboardHeight;
    boolean RHELD = false;
    boolean LHELD = false;
    static final PVector gravity = new PVector(0, 30f);
    Boss boss;
    LevelManager levelManager;
    PImage UI;
    PImage fillHeart;
    PImage emptyHeart;
    PImage bullet;
    ArrayList<PVector> lives;
    ArrayList<PVector> bullets;
    int levelBreakTimer = 0;

    // Stores the current state of the game.
    enum gameState {
        MainMenu,
        Running,
        Paused,
        LevelEnd,
        GameOver,
        Intro
    }

    gameState state = gameState.MainMenu;

    public void settings() {
        fullScreen();
        noSmooth();
    }

    public void setup() {

        player = new Character(this);
        scoreboardHeight = displayHeight / 25;
        boss = new Boss(this);
        levelManager = new LevelManager(this, player);
        UI = loadImage("Assets/UI.png");
        noSmooth();
        bullet = loadImage("Assets/bullet.png");
        fillHeart = loadImage("Assets/fillHeart.png");
        emptyHeart = loadImage("Assets/emptyHeart.png");
        fillHeart.resize(50, 50);
        emptyHeart.resize(50, 50);
        bullet.resize(50, 60);
        lives = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            lives.add(new PVector(10 + (60 * i), 20));
        }
        bullets = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            bullets.add(new PVector(500 + (50 * i), 20));
        }
    }

    public void draw() {

        if (player.health <= 0){
            state = gameState.GameOver;
        }
        if (levelManager.levelOver){
            state = gameState.LevelEnd;
            levelBreakTimer = 500;
        }
        background(60);
        rect(0, 0, displayWidth, scoreboardHeight);
        textSize(42);
        text("SCORE: ", 0, displayHeight / 30);
        fill(255);

        switch (state) {
            case MainMenu:
                background(100);
                textSize(42);
                text("SIGN HERE", displayHeight/2,displayHeight/2);
                text( "Press [ENTER] to start", displayHeight/2, displayHeight/2 + 200);
                break;
            case Paused:
                text("PAUSED", displayWidth / 4, displayHeight / 2);
                break;
            case LevelEnd:
                textSize(42);
                background(200);
                boss.draw();
                boss.killedCEO = true;
                text("CEO retired" , displayWidth/2, displayHeight/2);
                levelBreakTimer--;
                break;
            case Intro:
                if (boss.introDone) state = gameState.Running;
                boss.draw();
                break;
            case GameOver:
                textSize(40);
                text("GAME OVER", displayWidth / 4, displayHeight / 2);
                boss.playerDied();
                boss.draw();
                //text("FINAL SCORE : ", displayWidth / 4, (float) (displayHeight / 1.5));
                break;
            case Running:
//                boss.draw();
                drawGraphics();
                pushMatrix();
                translate(-player.position.x + 500, 0);
                levelManager.draw();
                player.draw();
                update();
                fill(255, 0, 0);
                popMatrix();
                image(UI, 0, 0);

                for (int i = 0; i < 5; i++) {
                    if (player.health > i) {
                        image(fillHeart, lives.get(i).x, lives.get(i).y);
                    } else {
                        image(emptyHeart, lives.get(i).x, lives.get(i).y);
                    }
                    if (player.Reloading){
                        fill(200,20,0);
                        rect(500, 20, 300 - player.reloadTimer*2, 50);
                        fill(0);
                        text("RELOADING", 555, 55);
                    } else if (player.bullets > i) {
                        image(bullet, bullets.get(i).x, bullets.get(i).y);
                    }
                }
                break;
        }
    }

    private void drawGraphics() {
        fill(200);
        strokeWeight(0.5f);
        stroke(200);
        strokeWeight(1);
    }

    private void update() {
        levelManager.update();
        if (LHELD) {
            player.move(2);
            px += 20;
        } else if (RHELD) {
            player.move(0);
            px -= 20;
        }
    }

    // Check the key pressed to either pause of explode a missile.
    public void keyPressed() {
        if (key == 32) {
            boss.Intro();
        } else if (key == 112) {
            if (state == gameState.Running) {
                state = gameState.Paused;
            } else if (state == gameState.Paused) {
                state = gameState.Running;
            }
        } else if (key == 'd') {
            RHELD = true;
        } else if (key == 'a') {
            LHELD = true;
        } else if (key == 'w') {
            player.move(1);
        } else if (key == 's') {
            player.duck(true);
        } else if (key == ENTER && state == gameState.MainMenu){
            state = gameState.Intro;
        } else if (key == ENTER && state == gameState.LevelEnd){
            levelManager.Levelnum++;
            setup();
            state = gameState.Running;
        }
    }


    public void keyReleased() {
        switch (key) {
            case 'd': {
                RHELD = false;
                break;
            }
            case 'a': {
                LHELD = false;
                break;
            }
            case 's': {
                player.duck(false);
                break;
            }
            case 'r': {
                player.reload();
                break;
            }
        }
    }


    // Activates when the LMB is pressed and fires a missile.
    public void mousePressed() {
        if (state == gameState.Running & mouseButton == LEFT) {
            PVector pos = player.position.get();
            Ray bullet = player.fire();
            if (bullet != null) {
                levelManager.hitCheck(bullet);
            }

            //v = new PVector(mouseX - pos.x, (mouseY - pos.y) * 1.1f);
            //bullets.add(new Bullet(pos, v, .011f, this));
        }
        if (state == gameState.Running & mouseButton == RIGHT) {
            player.aiming = true;
        }
    }

    public void mouseReleased() {
        if (state == gameState.Running & mouseButton == RIGHT) {
            player.aiming = false;
        }
    }


    public static void main(String... args) {
        PApplet.main("Main");
    }


}
