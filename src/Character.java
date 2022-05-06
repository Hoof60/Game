import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import static processing.core.PApplet.atan2;
import static processing.core.PConstants.PI;

final class Character {
    PVector position;
    PApplet app;
    PVector velocity;
    Level1 level;
    private float invMass = 0.05f;
    boolean onGround = true;
    static final PVector gravity = new PVector(0, 20f);
    static final float xDrag = 0.5f;
    static final float moveSpeed = 10;
    private float XAcceleration = 2f;
    boolean aiming = false;
    int height = 310;
    int width = 170;
    PImage Idle;
    PImage AimingBody;
    PImage gunArm;
    PImage crouch;
    Float orientation = 0f;
    boolean ducking;

    int bullets;

    Character(PApplet app) {
        position = new PVector(100, 400);
        bullets = 30;
        this.app = app;
        this.velocity = new PVector(0, 0);
        Idle = app.loadImage("Assets/Idle.png");
        AimingBody = app.loadImage("Assets/Dude.png");
        gunArm = app.loadImage("Assets/gunArm.png");
        crouch = app.loadImage("Assets/crouch.png");
        AimingBody.resize(150, 300);
        gunArm.resize(150, 40);
        Idle.resize(170, 300);
        crouch.resize(220, 250);
    }

    void startLevel(Level1 l){
        this.level = l;
    }

    void draw() {
        app.fill(200);
        if (ducking){
            app.image(crouch, position.x, position.y+50);
        } else if (aiming) {

            float angle = atan2(app.mouseY - (position.y + 90), (app.mouseX + position.x - 500) - (position.x + 140));

            if (angle < -2 || angle > 1) {
                app.pushMatrix();
                app.translate(Idle.width, 0);
                app.scale(-1, 1);
                app.image(AimingBody, -position.x, position.y);
                app.pushMatrix();
                app.translate(-position.x + 140, position.y + 90);
                app.rotate(-(PI +angle));
                //app.line(0, 0, app.mouseX + position.x - 500, app.mouseY);
                app.image(gunArm, 0, -20);
                app.popMatrix();
            } else {
                app.image(AimingBody, position.x, position.y);
                app.pushMatrix();
                app.translate(position.x + 140, position.y + 90);
                app.rotate(angle);
                app.image(gunArm, 0, -20);
            }
            app.popMatrix();
        } else {
            if (orientation == 0) {
                app.image(Idle, position.x, position.y);
            } else {
                app.pushMatrix();
                app.translate(Idle.width, 0);
                app.scale(-1, 1);
                app.image(Idle, -position.x, position.y);
                app.popMatrix();
            }
        }

        app.fill(0);
        app.text(bullets, 10, 40);
        app.fill(200);
        app.stroke(255);

        //CrossHair
        app.line(app.mouseX+ position.x - 500 - 20, app.mouseY, app.mouseX+ position.x - 500 - 10, app.mouseY);
        app.line(app.mouseX+ position.x - 500 + 20, app.mouseY, app.mouseX+ position.x - 500 + 10, app.mouseY);
        app.line(app.mouseX+ position.x - 500, app.mouseY - 20, app.mouseX + position.x - 500, app.mouseY - 10);
        app.line(app.mouseX+ position.x - 500, app.mouseY + 20, app.mouseX + position.x - 500, app.mouseY + 10);

        app.noStroke();
        if (position.y + height +15 + level.groundHeight.get((int) position.x/300) >= app.displayHeight || position.y + height + level.groundHeight.get((int) (position.x+ width)/300 ) >= app.displayHeight) {
            onGround = true;
            velocity.y = 0;
        }
        integrate(gravity);
    }

    void move(int dir) {
        if (!ducking) {
            switch (dir) {
                case 0: {
                    if (velocity.x < moveSpeed) {
                        velocity.x += XAcceleration;
                    }
                    orientation = 0f;
                    break;
                }
                case 1: {
                    if (onGround) {
                        velocity.y = -30f;
                        onGround = false;
                        integrate(gravity);
                    }
                    break;
                }
                case 2: {
                    if (velocity.x > -moveSpeed) {
                        velocity.x -= XAcceleration;
                    }
                    orientation = -1f;
                    break;
                }
                case 3: {
                    break;
                }
            }
        }
    }

    boolean integrate(PVector force) {
        // If infinite mass, we don't integrate
        if (invMass <= 0f) return false;

        // update position

        System.out.println(position.y+height);
        System.out.println(app.displayHeight - level.groundHeight.get((int) (position.x/300) + 1));
        position.y += velocity.y;
        if ((velocity.x > 0 && (position.x + width -5) % 300 < 10  && position.y + height >= app.displayHeight - level.groundHeight.get((int) (position.x/300) + 1) )|| (
                velocity.x < 0 && (position.x -5) % 300 < 10  && position.y + height >= app.displayHeight - level.groundHeight.get((int) (position.x/300) - 1))){

        } else {
            position.x += velocity.x;
//            position.add(velocity);
        }
//        } else if (velocity.x < 0){
//            if (position.y + height > level.groundHeight.get((int) position.x/300 )){
//                position.add(velocity);
//            }
//        }

        PVector acceleration = force.get();
        acceleration.mult(invMass);

        if (velocity.x > 0) {
            acceleration.x = -xDrag;
        } else if (velocity.x < 0) {
            acceleration.x = xDrag;
        }

        // update velocity
        velocity.add(acceleration);

        //calculate drag
        PVector n = new PVector(velocity.x, velocity.y);
        n.normalize();

        // Apply an impulse to bounce off the edge of the screen
        return false;
    }


    // Reduces the number of available missiles.
    Ray fire() {
        if (aiming) {
            app.stroke(255, 0, 0);
            app.strokeWeight(4);
            float angle = atan2(app.mouseY - (position.y + 90), (app.mouseX + position.x - 500) - (position.x + 140));
            app.pushMatrix();
            app.translate(-position.x + 500, 0);
            if (angle < -2 || angle > 1) {
                //app.line(position.x + 30, position.y + 100, app.mouseX + position.x - 500, app.mouseY);
            } else {
                //app.line(position.x + 190, position.y + 100, app.mouseX + position.x - 500, app.mouseY);
            }
            app.popMatrix();
            app.strokeWeight(1);

            app.line(position.x + 140-position.x + 500, position.y + 90, app.mouseX, app.mouseY);
            //Fire the bullet
            bullets--;
            Ray hitscanRay = new Ray(new PVector(position.x + 140-position.x + 500, position.y + 90), angle, app);
            return hitscanRay;

        }

        return null;
    }

    public void duck(boolean toggle) {
        if (onGround && toggle){
           ducking = true;
        } else {
            ducking = false;
        }
    }
}
