import processing.core.PImage;
import processing.core.PVector;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Main extends PApplet {

    // Global vars:
    PVector v;
    int px = 10;
    int py = 10;
    ArrayList<Bullet> bullets;
    Character player;
    int scoreboardHeight;
    boolean RHELD = false;
    boolean LHELD = false;
    static final PVector gravity = new PVector(0, 30f);
    Boss boss;
    Level1 level1;
    PImage UI;
    PImage fillHeart;
    PImage emptyHeart;
    ArrayList<PVector> lives;



    // Stores the current state of the game.
    enum gameState {
        //MainMenu,
        Running,
        Paused,
        GameOver
    }

    gameState state = gameState.Running;

    int score;

    public void settings() {
        fullScreen();
        noSmooth();
    }

    public void setup() {
//        noSmooth();

        bullets = new ArrayList<>();
        player = new Character(this);
        scoreboardHeight = displayHeight / 25;
        boss = new Boss(this);
        level1 = new Level1(this, player);
        UI = loadImage("Assets/UI.png");
        fillHeart = loadImage("Assets/fillHeart.png");
        noSmooth();
        fillHeart.resize(50, 50);
        emptyHeart = loadImage("Assets/emptyHeart.png");
        lives = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            lives.add(new PVector( 10 + (50*i), 20));
        }
    }

    public void draw() {

        background(60);
        noStroke();
        rect(0, 0, displayWidth, scoreboardHeight);
        textSize(42);
        text("SCORE: " + score, 0, displayHeight / 30);
        fill(255);

        switch (state) {
            case Paused:
                text("PAUSED", displayWidth / 4, displayHeight / 2);
                break;
            case GameOver:
                textSize(40);
                text("GAME OVER", displayWidth / 4, displayHeight / 2);
                text("FINAL SCORE : " + score, displayWidth / 4, (float) (displayHeight / 1.5));
                break;
            case Running:
//                boss.draw();
                drawGraphics();
                pushMatrix();
                translate(-player.position.x + 500, 0);
                level1.draw();
                player.draw();
                update();
                fill(255,0,0);
                circle(2000,100,50);
                popMatrix();
                image(UI, 0,0);

                for (int i = 0; i < 5 ; i++) {
                    image(fillHeart, lives.get(i).x, lives.get(i).y);
                }

                break;
        }
    }

    private void drawGraphics() {
        fill(200);
        strokeWeight(0.5f);
        stroke(200);
        strokeWeight(1);
        //rect();
    }

    private void update() {
        level1.update();
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
            while (!bullets.isEmpty()) {
                bullets.remove(0);
            }
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
        }
    }


    public void keyReleased() {
        switch (key){
            case 'd':{
                RHELD = false;
                break;
            }
            case 'a':{
                LHELD = false;
                break;
            }
            case 's':{
                player.duck(false);
                break;
            }
        }
    }


    // Activates when the LMB is pressed and fires a missile.
    public void mousePressed() {
        if (state == gameState.Running & mouseButton == LEFT) {
            PVector pos = player.position.get();
            Ray bullet = player.fire();
            if (bullet != null){
                level1.hitCheck(bullet);
            }

            //v = new PVector(mouseX - pos.x, (mouseY - pos.y) * 1.1f);
            //bullets.add(new Bullet(pos, v, .011f, this));
        } if (state == gameState.Running & mouseButton == RIGHT){
            player.aiming = true;
        }
    }

    public void mouseReleased(){
        if (state == gameState.Running & mouseButton == RIGHT){
            player.aiming = false;
        }
    }


    public static void main(String... args) {
        PApplet.main("Main");
    }


}
